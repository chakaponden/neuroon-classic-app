package com.crashlytics.android.core;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import com.crashlytics.android.core.internal.models.SessionEventData;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.common.CommonUtils;
import io.fabric.sdk.android.services.common.DeliveryMechanism;
import io.fabric.sdk.android.services.common.IdManager;
import io.fabric.sdk.android.services.persistence.FileStore;
import io.fabric.sdk.android.services.settings.SessionSettingsData;
import io.fabric.sdk.android.services.settings.Settings;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.Thread;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class CrashlyticsUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final int ANALYZER_VERSION = 1;
    static final FilenameFilter ANY_SESSION_FILENAME_FILTER = new FilenameFilter() {
        public boolean accept(File file, String filename) {
            return CrashlyticsUncaughtExceptionHandler.SESSION_FILE_PATTERN.matcher(filename).matches();
        }
    };
    private static final String EVENT_TYPE_CRASH = "crash";
    private static final String EVENT_TYPE_LOGGED = "error";
    private static final String GENERATOR_FORMAT = "Crashlytics Android SDK/%s";
    private static final String[] INITIAL_SESSION_PART_TAGS = {SESSION_USER_TAG, SESSION_APP_TAG, SESSION_OS_TAG, SESSION_DEVICE_TAG};
    static final String INVALID_CLS_CACHE_DIR = "invalidClsFiles";
    static final Comparator<File> LARGEST_FILE_NAME_FIRST = new Comparator<File>() {
        public int compare(File file1, File file2) {
            return file2.getName().compareTo(file1.getName());
        }
    };
    private static final int MAX_COMPLETE_SESSIONS_COUNT = 4;
    private static final int MAX_LOCAL_LOGGED_EXCEPTIONS = 64;
    static final int MAX_OPEN_SESSIONS = 8;
    /* access modifiers changed from: private */
    public static final Map<String, String> SEND_AT_CRASHTIME_HEADER = Collections.singletonMap("X-CRASHLYTICS-SEND-FLAGS", "1");
    static final String SESSION_APP_TAG = "SessionApp";
    static final String SESSION_BEGIN_TAG = "BeginSession";
    static final String SESSION_DEVICE_TAG = "SessionDevice";
    static final String SESSION_FATAL_TAG = "SessionCrash";
    static final FilenameFilter SESSION_FILE_FILTER = new FilenameFilter() {
        public boolean accept(File dir, String filename) {
            return filename.length() == ClsFileOutputStream.SESSION_FILE_EXTENSION.length() + 35 && filename.endsWith(ClsFileOutputStream.SESSION_FILE_EXTENSION);
        }
    };
    /* access modifiers changed from: private */
    public static final Pattern SESSION_FILE_PATTERN = Pattern.compile("([\\d|A-Z|a-z]{12}\\-[\\d|A-Z|a-z]{4}\\-[\\d|A-Z|a-z]{4}\\-[\\d|A-Z|a-z]{12}).+");
    private static final int SESSION_ID_LENGTH = 35;
    static final String SESSION_NON_FATAL_TAG = "SessionEvent";
    static final String SESSION_OS_TAG = "SessionOS";
    static final String SESSION_USER_TAG = "SessionUser";
    static final Comparator<File> SMALLEST_FILE_NAME_FIRST = new Comparator<File>() {
        public int compare(File file1, File file2) {
            return file1.getName().compareTo(file2.getName());
        }
    };
    /* access modifiers changed from: private */
    public final CrashlyticsCore crashlyticsCore;
    private final Thread.UncaughtExceptionHandler defaultHandler;
    private final DevicePowerStateListener devicePowerStateListener;
    private final AtomicInteger eventCounter = new AtomicInteger(0);
    private final CrashlyticsExecutorServiceWrapper executorServiceWrapper;
    private final FileStore fileStore;
    private final IdManager idManager;
    /* access modifiers changed from: private */
    public final AtomicBoolean isHandlingException;
    /* access modifiers changed from: private */
    public final LogFileManager logFileManager;
    private final String unityVersion;

    static class FileNameContainsFilter implements FilenameFilter {
        private final String string;

        public FileNameContainsFilter(String s) {
            this.string = s;
        }

        public boolean accept(File dir, String filename) {
            return filename.contains(this.string) && !filename.endsWith(ClsFileOutputStream.IN_PROGRESS_SESSION_FILE_EXTENSION);
        }
    }

    static class SessionPartFileFilter implements FilenameFilter {
        private final String sessionId;

        public SessionPartFileFilter(String sessionId2) {
            this.sessionId = sessionId2;
        }

        public boolean accept(File file, String fileName) {
            if (!fileName.equals(this.sessionId + ClsFileOutputStream.SESSION_FILE_EXTENSION) && fileName.contains(this.sessionId) && !fileName.endsWith(ClsFileOutputStream.IN_PROGRESS_SESSION_FILE_EXTENSION)) {
                return true;
            }
            return false;
        }
    }

    private static class AnySessionPartFileFilter implements FilenameFilter {
        private AnySessionPartFileFilter() {
        }

        public boolean accept(File file, String fileName) {
            return !CrashlyticsUncaughtExceptionHandler.SESSION_FILE_FILTER.accept(file, fileName) && CrashlyticsUncaughtExceptionHandler.SESSION_FILE_PATTERN.matcher(fileName).matches();
        }
    }

    CrashlyticsUncaughtExceptionHandler(Thread.UncaughtExceptionHandler handler, CrashlyticsExecutorServiceWrapper executorServiceWrapper2, IdManager idManager2, UnityVersionProvider unityVersionProvider, FileStore fileStore2, CrashlyticsCore crashlyticsCore2) {
        this.defaultHandler = handler;
        this.executorServiceWrapper = executorServiceWrapper2;
        this.idManager = idManager2;
        this.crashlyticsCore = crashlyticsCore2;
        this.unityVersion = unityVersionProvider.getUnityVersion();
        this.fileStore = fileStore2;
        this.isHandlingException = new AtomicBoolean(false);
        Context context = crashlyticsCore2.getContext();
        this.logFileManager = new LogFileManager(context, fileStore2);
        this.devicePowerStateListener = new DevicePowerStateListener(context);
    }

    public synchronized void uncaughtException(final Thread thread, final Throwable ex) {
        this.isHandlingException.set(true);
        try {
            Fabric.getLogger().d(CrashlyticsCore.TAG, "Crashlytics is handling uncaught exception \"" + ex + "\" from thread " + thread.getName());
            this.devicePowerStateListener.dispose();
            final Date now = new Date();
            this.executorServiceWrapper.executeSyncLoggingException(new Callable<Void>() {
                public Void call() throws Exception {
                    CrashlyticsUncaughtExceptionHandler.this.handleUncaughtException(now, thread, ex);
                    return null;
                }
            });
            Fabric.getLogger().d(CrashlyticsCore.TAG, "Crashlytics completed exception processing. Invoking default exception handler.");
            this.defaultHandler.uncaughtException(thread, ex);
            this.isHandlingException.set(false);
        } catch (Exception e) {
            Fabric.getLogger().e(CrashlyticsCore.TAG, "An error occurred in the uncaught exception handler", e);
            Fabric.getLogger().d(CrashlyticsCore.TAG, "Crashlytics completed exception processing. Invoking default exception handler.");
            this.defaultHandler.uncaughtException(thread, ex);
            this.isHandlingException.set(false);
        } catch (Throwable th) {
            Fabric.getLogger().d(CrashlyticsCore.TAG, "Crashlytics completed exception processing. Invoking default exception handler.");
            this.defaultHandler.uncaughtException(thread, ex);
            this.isHandlingException.set(false);
            throw th;
        }
        return;
    }

    /* access modifiers changed from: private */
    public void handleUncaughtException(Date time, Thread thread, Throwable ex) throws Exception {
        this.crashlyticsCore.createCrashMarker();
        writeFatal(time, thread, ex);
        doCloseSessions();
        doOpenSession();
        trimSessionFiles();
        if (!this.crashlyticsCore.shouldPromptUserBeforeSendingCrashReports()) {
            sendSessionReports();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isHandlingException() {
        return this.isHandlingException.get();
    }

    /* access modifiers changed from: package-private */
    public void writeToLog(final long timestamp, final String msg) {
        this.executorServiceWrapper.executeAsync(new Callable<Void>() {
            public Void call() throws Exception {
                if (CrashlyticsUncaughtExceptionHandler.this.isHandlingException.get()) {
                    return null;
                }
                CrashlyticsUncaughtExceptionHandler.this.logFileManager.writeToLog(timestamp, msg);
                return null;
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void writeNonFatalException(final Thread thread, final Throwable ex) {
        final Date now = new Date();
        this.executorServiceWrapper.executeAsync((Runnable) new Runnable() {
            public void run() {
                if (!CrashlyticsUncaughtExceptionHandler.this.isHandlingException.get()) {
                    CrashlyticsUncaughtExceptionHandler.this.doWriteNonFatal(now, thread, ex);
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void cacheUserData(final String userId, final String userName, final String userEmail) {
        this.executorServiceWrapper.executeAsync(new Callable<Void>() {
            public Void call() throws Exception {
                new MetaDataStore(CrashlyticsUncaughtExceptionHandler.this.getFilesDir()).writeUserData(CrashlyticsUncaughtExceptionHandler.this.getCurrentSessionId(), new UserMetaData(userId, userName, userEmail));
                return null;
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void cacheKeyData(final Map<String, String> keyData) {
        this.executorServiceWrapper.executeAsync(new Callable<Void>() {
            public Void call() throws Exception {
                new MetaDataStore(CrashlyticsUncaughtExceptionHandler.this.getFilesDir()).writeKeyData(CrashlyticsUncaughtExceptionHandler.this.getCurrentSessionId(), keyData);
                return null;
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void openSession() {
        this.executorServiceWrapper.executeAsync(new Callable<Void>() {
            public Void call() throws Exception {
                CrashlyticsUncaughtExceptionHandler.this.doOpenSession();
                return null;
            }
        });
    }

    /* access modifiers changed from: private */
    public String getCurrentSessionId() {
        File[] sessionBeginFiles = listSortedSessionBeginFiles();
        if (sessionBeginFiles.length > 0) {
            return getSessionIdFromSessionFile(sessionBeginFiles[0]);
        }
        return null;
    }

    private String getPreviousSessionId() {
        File[] sessionBeginFiles = listSortedSessionBeginFiles();
        if (sessionBeginFiles.length > 1) {
            return getSessionIdFromSessionFile(sessionBeginFiles[1]);
        }
        return null;
    }

    private String getSessionIdFromSessionFile(File sessionFile) {
        return sessionFile.getName().substring(0, 35);
    }

    /* access modifiers changed from: package-private */
    public boolean hasOpenSession() {
        return listSessionBeginFiles().length > 0;
    }

    /* access modifiers changed from: package-private */
    public boolean finalizeSessions() {
        return ((Boolean) this.executorServiceWrapper.executeSyncLoggingException(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                if (CrashlyticsUncaughtExceptionHandler.this.isHandlingException.get()) {
                    Fabric.getLogger().d(CrashlyticsCore.TAG, "Skipping session finalization because a crash has already occurred.");
                    return Boolean.FALSE;
                }
                Fabric.getLogger().d(CrashlyticsCore.TAG, "Finalizing previously open sessions.");
                SessionEventData crashEventData = CrashlyticsUncaughtExceptionHandler.this.crashlyticsCore.getExternalCrashEventData();
                if (crashEventData != null) {
                    CrashlyticsUncaughtExceptionHandler.this.writeExternalCrashEvent(crashEventData);
                }
                CrashlyticsUncaughtExceptionHandler.this.doCloseSessions(true);
                Fabric.getLogger().d(CrashlyticsCore.TAG, "Closed all previously open sessions");
                return Boolean.TRUE;
            }
        })).booleanValue();
    }

    /* access modifiers changed from: private */
    public void doOpenSession() throws Exception {
        Date startedAt = new Date();
        String sessionIdentifier = new CLSUUID(this.idManager).toString();
        Fabric.getLogger().d(CrashlyticsCore.TAG, "Opening an new session with ID " + sessionIdentifier);
        writeBeginSession(sessionIdentifier, startedAt);
        writeSessionApp(sessionIdentifier);
        writeSessionOS(sessionIdentifier);
        writeSessionDevice(sessionIdentifier);
        this.logFileManager.setCurrentSession(sessionIdentifier);
    }

    /* access modifiers changed from: package-private */
    public void doCloseSessions() throws Exception {
        doCloseSessions(false);
    }

    /* access modifiers changed from: private */
    public void doCloseSessions(boolean excludeCurrent) throws Exception {
        int offset = excludeCurrent ? 1 : 0;
        trimOpenSessions(offset + 8);
        File[] sessionBeginFiles = listSortedSessionBeginFiles();
        if (sessionBeginFiles.length <= offset) {
            Fabric.getLogger().d(CrashlyticsCore.TAG, "No open sessions to be closed.");
            return;
        }
        writeSessionUser(getSessionIdFromSessionFile(sessionBeginFiles[offset]));
        CrashlyticsCore crashlyticsCore2 = this.crashlyticsCore;
        SessionSettingsData settingsData = CrashlyticsCore.getSessionSettingsData();
        if (settingsData == null) {
            Fabric.getLogger().d(CrashlyticsCore.TAG, "Unable to close session. Settings are not loaded.");
        } else {
            closeOpenSessions(sessionBeginFiles, offset, settingsData.maxCustomExceptionEvents);
        }
    }

    private void closeOpenSessions(File[] sessionBeginFiles, int beginIndex, int maxLoggedExceptionsCount) {
        Fabric.getLogger().d(CrashlyticsCore.TAG, "Closing open sessions.");
        for (int i = beginIndex; i < sessionBeginFiles.length; i++) {
            File sessionBeginFile = sessionBeginFiles[i];
            String sessionIdentifier = getSessionIdFromSessionFile(sessionBeginFile);
            Fabric.getLogger().d(CrashlyticsCore.TAG, "Closing session: " + sessionIdentifier);
            writeSessionPartsToSessionFile(sessionBeginFile, sessionIdentifier, maxLoggedExceptionsCount);
        }
    }

    private void closeWithoutRenamingOrLog(ClsFileOutputStream fos) {
        if (fos != null) {
            try {
                fos.closeInProgressStream();
            } catch (IOException ex) {
                Fabric.getLogger().e(CrashlyticsCore.TAG, "Error closing session file stream in the presence of an exception", ex);
            }
        }
    }

    private void deleteSessionPartFilesFor(String sessionId) {
        for (File file : listSessionPartFilesFor(sessionId)) {
            file.delete();
        }
    }

    private File[] listSessionPartFilesFor(String sessionId) {
        return listFilesMatching(new SessionPartFileFilter(sessionId));
    }

    private File[] listCompleteSessionFiles() {
        return listFilesMatching(SESSION_FILE_FILTER);
    }

    /* access modifiers changed from: package-private */
    public File[] listSessionBeginFiles() {
        return listFilesMatching(new FileNameContainsFilter(SESSION_BEGIN_TAG));
    }

    private File[] listSortedSessionBeginFiles() {
        File[] sessionBeginFiles = listSessionBeginFiles();
        Arrays.sort(sessionBeginFiles, LARGEST_FILE_NAME_FIRST);
        return sessionBeginFiles;
    }

    /* access modifiers changed from: private */
    public File[] listFilesMatching(FilenameFilter filter) {
        return ensureFileArrayNotNull(getFilesDir().listFiles(filter));
    }

    private File[] ensureFileArrayNotNull(File[] files) {
        return files == null ? new File[0] : files;
    }

    private void trimSessionEventFiles(String sessionId, int limit) {
        Utils.capFileCount(getFilesDir(), new FileNameContainsFilter(sessionId + SESSION_NON_FATAL_TAG), limit, SMALLEST_FILE_NAME_FIRST);
    }

    /* access modifiers changed from: package-private */
    public void trimSessionFiles() {
        Utils.capFileCount(getFilesDir(), SESSION_FILE_FILTER, 4, SMALLEST_FILE_NAME_FIRST);
    }

    private void trimOpenSessions(int maxOpenSessionCount) {
        Set<String> sessionIdsToKeep = new HashSet<>();
        File[] beginSessionFiles = listSortedSessionBeginFiles();
        int count = Math.min(maxOpenSessionCount, beginSessionFiles.length);
        for (int i = 0; i < count; i++) {
            sessionIdsToKeep.add(getSessionIdFromSessionFile(beginSessionFiles[i]));
        }
        this.logFileManager.discardOldLogFiles(sessionIdsToKeep);
        for (File sessionPartFile : listFilesMatching(new AnySessionPartFileFilter())) {
            String fileName = sessionPartFile.getName();
            Matcher matcher = SESSION_FILE_PATTERN.matcher(fileName);
            matcher.matches();
            if (!sessionIdsToKeep.contains(matcher.group(1))) {
                Fabric.getLogger().d(CrashlyticsCore.TAG, "Trimming open session file: " + fileName);
                sessionPartFile.delete();
            }
        }
    }

    private File[] getTrimmedNonFatalFiles(String sessionId, File[] nonFatalFiles, int maxLoggedExceptionsCount) {
        if (nonFatalFiles.length <= maxLoggedExceptionsCount) {
            return nonFatalFiles;
        }
        Fabric.getLogger().d(CrashlyticsCore.TAG, String.format(Locale.US, "Trimming down to %d logged exceptions.", new Object[]{Integer.valueOf(maxLoggedExceptionsCount)}));
        trimSessionEventFiles(sessionId, maxLoggedExceptionsCount);
        return listFilesMatching(new FileNameContainsFilter(sessionId + SESSION_NON_FATAL_TAG));
    }

    /* access modifiers changed from: package-private */
    public void cleanInvalidTempFiles() {
        this.executorServiceWrapper.executeAsync((Runnable) new Runnable() {
            public void run() {
                CrashlyticsUncaughtExceptionHandler.this.doCleanInvalidTempFiles(CrashlyticsUncaughtExceptionHandler.this.listFilesMatching(ClsFileOutputStream.TEMP_FILENAME_FILTER));
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void doCleanInvalidTempFiles(File[] invalidFiles) {
        deleteLegacyInvalidCacheDir();
        for (File invalidFile : invalidFiles) {
            Fabric.getLogger().d(CrashlyticsCore.TAG, "Found invalid session part file: " + invalidFile);
            final String sessionId = getSessionIdFromSessionFile(invalidFile);
            FilenameFilter sessionFilter = new FilenameFilter() {
                public boolean accept(File f, String name) {
                    return name.startsWith(sessionId);
                }
            };
            Fabric.getLogger().d(CrashlyticsCore.TAG, "Deleting all part files for invalid session: " + sessionId);
            for (File sessionFile : listFilesMatching(sessionFilter)) {
                Fabric.getLogger().d(CrashlyticsCore.TAG, "Deleting session file: " + sessionFile);
                sessionFile.delete();
            }
        }
    }

    private void deleteLegacyInvalidCacheDir() {
        File cacheDir = new File(this.crashlyticsCore.getSdkDirectory(), INVALID_CLS_CACHE_DIR);
        if (cacheDir.exists()) {
            if (cacheDir.isDirectory()) {
                for (File cacheFile : cacheDir.listFiles()) {
                    cacheFile.delete();
                }
            }
            cacheDir.delete();
        }
    }

    private void writeFatal(Date time, Thread thread, Throwable ex) {
        ClsFileOutputStream fos = null;
        CodedOutputStream cos = null;
        try {
            String currentSessionId = getCurrentSessionId();
            if (currentSessionId == null) {
                Fabric.getLogger().e(CrashlyticsCore.TAG, "Tried to write a fatal exception while no session was open.", (Throwable) null);
                CommonUtils.flushOrLog((Flushable) null, "Failed to flush to session begin file.");
                CommonUtils.closeOrLog((Closeable) null, "Failed to close fatal exception file output stream.");
                return;
            }
            CrashlyticsCore.recordFatalExceptionEvent(currentSessionId, ex.getClass().getName());
            ClsFileOutputStream fos2 = new ClsFileOutputStream(getFilesDir(), currentSessionId + SESSION_FATAL_TAG);
            try {
                cos = CodedOutputStream.newInstance((OutputStream) fos2);
                writeSessionEvent(cos, time, thread, ex, EVENT_TYPE_CRASH, true);
                CommonUtils.flushOrLog(cos, "Failed to flush to session begin file.");
                CommonUtils.closeOrLog(fos2, "Failed to close fatal exception file output stream.");
                ClsFileOutputStream clsFileOutputStream = fos2;
            } catch (Exception e) {
                e = e;
                fos = fos2;
                try {
                    Fabric.getLogger().e(CrashlyticsCore.TAG, "An error occurred in the fatal exception logger", e);
                    CommonUtils.flushOrLog(cos, "Failed to flush to session begin file.");
                    CommonUtils.closeOrLog(fos, "Failed to close fatal exception file output stream.");
                } catch (Throwable th) {
                    th = th;
                    CommonUtils.flushOrLog(cos, "Failed to flush to session begin file.");
                    CommonUtils.closeOrLog(fos, "Failed to close fatal exception file output stream.");
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                fos = fos2;
                CommonUtils.flushOrLog(cos, "Failed to flush to session begin file.");
                CommonUtils.closeOrLog(fos, "Failed to close fatal exception file output stream.");
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            Fabric.getLogger().e(CrashlyticsCore.TAG, "An error occurred in the fatal exception logger", e);
            CommonUtils.flushOrLog(cos, "Failed to flush to session begin file.");
            CommonUtils.closeOrLog(fos, "Failed to close fatal exception file output stream.");
        }
    }

    /* access modifiers changed from: private */
    public void writeExternalCrashEvent(SessionEventData crashEventData) throws IOException {
        ClsFileOutputStream fos = null;
        CodedOutputStream cos = null;
        try {
            String previousSessionId = getPreviousSessionId();
            if (previousSessionId == null) {
                Fabric.getLogger().e(CrashlyticsCore.TAG, "Tried to write a native crash while no session was open.", (Throwable) null);
                CommonUtils.flushOrLog((Flushable) null, "Failed to flush to session begin file.");
                CommonUtils.closeOrLog((Closeable) null, "Failed to close fatal exception file output stream.");
                return;
            }
            CrashlyticsCore.recordFatalExceptionEvent(previousSessionId, String.format(Locale.US, "<native-crash [%s (%s)]>", new Object[]{crashEventData.signal.code, crashEventData.signal.name}));
            ClsFileOutputStream fos2 = new ClsFileOutputStream(getFilesDir(), previousSessionId + SESSION_FATAL_TAG);
            try {
                cos = CodedOutputStream.newInstance((OutputStream) fos2);
                NativeCrashWriter.writeNativeCrash(crashEventData, new LogFileManager(this.crashlyticsCore.getContext(), this.fileStore, previousSessionId), new MetaDataStore(getFilesDir()).readKeyData(previousSessionId), cos);
                CommonUtils.flushOrLog(cos, "Failed to flush to session begin file.");
                CommonUtils.closeOrLog(fos2, "Failed to close fatal exception file output stream.");
                ClsFileOutputStream clsFileOutputStream = fos2;
            } catch (Exception e) {
                e = e;
                fos = fos2;
                try {
                    Fabric.getLogger().e(CrashlyticsCore.TAG, "An error occurred in the native crash logger", e);
                    CommonUtils.flushOrLog(cos, "Failed to flush to session begin file.");
                    CommonUtils.closeOrLog(fos, "Failed to close fatal exception file output stream.");
                } catch (Throwable th) {
                    th = th;
                    CommonUtils.flushOrLog(cos, "Failed to flush to session begin file.");
                    CommonUtils.closeOrLog(fos, "Failed to close fatal exception file output stream.");
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                fos = fos2;
                CommonUtils.flushOrLog(cos, "Failed to flush to session begin file.");
                CommonUtils.closeOrLog(fos, "Failed to close fatal exception file output stream.");
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            Fabric.getLogger().e(CrashlyticsCore.TAG, "An error occurred in the native crash logger", e);
            CommonUtils.flushOrLog(cos, "Failed to flush to session begin file.");
            CommonUtils.closeOrLog(fos, "Failed to close fatal exception file output stream.");
        }
    }

    /* access modifiers changed from: private */
    public void doWriteNonFatal(Date time, Thread thread, Throwable ex) {
        String currentSessionId = getCurrentSessionId();
        if (currentSessionId == null) {
            Fabric.getLogger().e(CrashlyticsCore.TAG, "Tried to write a non-fatal exception while no session was open.", (Throwable) null);
            return;
        }
        CrashlyticsCore.recordLoggedExceptionEvent(currentSessionId, ex.getClass().getName());
        ClsFileOutputStream fos = null;
        CodedOutputStream cos = null;
        try {
            Fabric.getLogger().d(CrashlyticsCore.TAG, "Crashlytics is logging non-fatal exception \"" + ex + "\" from thread " + thread.getName());
            ClsFileOutputStream fos2 = new ClsFileOutputStream(getFilesDir(), currentSessionId + SESSION_NON_FATAL_TAG + CommonUtils.padWithZerosToMaxIntWidth(this.eventCounter.getAndIncrement()));
            try {
                cos = CodedOutputStream.newInstance((OutputStream) fos2);
                writeSessionEvent(cos, time, thread, ex, EVENT_TYPE_LOGGED, false);
                CommonUtils.flushOrLog(cos, "Failed to flush to non-fatal file.");
                CommonUtils.closeOrLog(fos2, "Failed to close non-fatal file output stream.");
                ClsFileOutputStream clsFileOutputStream = fos2;
            } catch (Exception e) {
                e = e;
                fos = fos2;
                try {
                    Fabric.getLogger().e(CrashlyticsCore.TAG, "An error occurred in the non-fatal exception logger", e);
                    CommonUtils.flushOrLog(cos, "Failed to flush to non-fatal file.");
                    CommonUtils.closeOrLog(fos, "Failed to close non-fatal file output stream.");
                    trimSessionEventFiles(currentSessionId, 64);
                } catch (Throwable th) {
                    th = th;
                    CommonUtils.flushOrLog(cos, "Failed to flush to non-fatal file.");
                    CommonUtils.closeOrLog(fos, "Failed to close non-fatal file output stream.");
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                fos = fos2;
                CommonUtils.flushOrLog(cos, "Failed to flush to non-fatal file.");
                CommonUtils.closeOrLog(fos, "Failed to close non-fatal file output stream.");
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            Fabric.getLogger().e(CrashlyticsCore.TAG, "An error occurred in the non-fatal exception logger", e);
            CommonUtils.flushOrLog(cos, "Failed to flush to non-fatal file.");
            CommonUtils.closeOrLog(fos, "Failed to close non-fatal file output stream.");
            trimSessionEventFiles(currentSessionId, 64);
        }
        try {
            trimSessionEventFiles(currentSessionId, 64);
        } catch (Exception e3) {
            Fabric.getLogger().e(CrashlyticsCore.TAG, "An error occurred when trimming non-fatal files.", e3);
        }
    }

    private void writeBeginSession(String sessionId, Date startedAt) throws Exception {
        FileOutputStream fos = null;
        CodedOutputStream cos = null;
        try {
            FileOutputStream fos2 = new ClsFileOutputStream(getFilesDir(), sessionId + SESSION_BEGIN_TAG);
            try {
                cos = CodedOutputStream.newInstance((OutputStream) fos2);
                SessionProtobufHelper.writeBeginSession(cos, sessionId, String.format(Locale.US, GENERATOR_FORMAT, new Object[]{this.crashlyticsCore.getVersion()}), startedAt.getTime() / 1000);
                CommonUtils.flushOrLog(cos, "Failed to flush to session begin file.");
                CommonUtils.closeOrLog(fos2, "Failed to close begin session file.");
            } catch (Throwable th) {
                th = th;
                fos = fos2;
                CommonUtils.flushOrLog(cos, "Failed to flush to session begin file.");
                CommonUtils.closeOrLog(fos, "Failed to close begin session file.");
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            CommonUtils.flushOrLog(cos, "Failed to flush to session begin file.");
            CommonUtils.closeOrLog(fos, "Failed to close begin session file.");
            throw th;
        }
    }

    private void writeSessionApp(String sessionId) throws Exception {
        FileOutputStream fos = null;
        CodedOutputStream cos = null;
        try {
            FileOutputStream fos2 = new ClsFileOutputStream(getFilesDir(), sessionId + SESSION_APP_TAG);
            try {
                cos = CodedOutputStream.newInstance((OutputStream) fos2);
                SessionProtobufHelper.writeSessionApp(cos, this.idManager.getAppIdentifier(), this.crashlyticsCore.getApiKey(), this.crashlyticsCore.getVersionCode(), this.crashlyticsCore.getVersionName(), this.idManager.getAppInstallIdentifier(), DeliveryMechanism.determineFrom(this.crashlyticsCore.getInstallerPackageName()).getId(), this.unityVersion);
                CommonUtils.flushOrLog(cos, "Failed to flush to session app file.");
                CommonUtils.closeOrLog(fos2, "Failed to close session app file.");
            } catch (Throwable th) {
                th = th;
                fos = fos2;
                CommonUtils.flushOrLog(cos, "Failed to flush to session app file.");
                CommonUtils.closeOrLog(fos, "Failed to close session app file.");
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            CommonUtils.flushOrLog(cos, "Failed to flush to session app file.");
            CommonUtils.closeOrLog(fos, "Failed to close session app file.");
            throw th;
        }
    }

    private void writeSessionOS(String sessionId) throws Exception {
        FileOutputStream fos = null;
        CodedOutputStream cos = null;
        try {
            FileOutputStream fos2 = new ClsFileOutputStream(getFilesDir(), sessionId + SESSION_OS_TAG);
            try {
                cos = CodedOutputStream.newInstance((OutputStream) fos2);
                SessionProtobufHelper.writeSessionOS(cos, CommonUtils.isRooted(this.crashlyticsCore.getContext()));
                CommonUtils.flushOrLog(cos, "Failed to flush to session OS file.");
                CommonUtils.closeOrLog(fos2, "Failed to close session OS file.");
            } catch (Throwable th) {
                th = th;
                fos = fos2;
                CommonUtils.flushOrLog(cos, "Failed to flush to session OS file.");
                CommonUtils.closeOrLog(fos, "Failed to close session OS file.");
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            CommonUtils.flushOrLog(cos, "Failed to flush to session OS file.");
            CommonUtils.closeOrLog(fos, "Failed to close session OS file.");
            throw th;
        }
    }

    private void writeSessionDevice(String sessionId) throws Exception {
        ClsFileOutputStream clsFileOutputStream = null;
        CodedOutputStream cos = null;
        try {
            ClsFileOutputStream clsFileOutputStream2 = new ClsFileOutputStream(getFilesDir(), sessionId + SESSION_DEVICE_TAG);
            try {
                cos = CodedOutputStream.newInstance((OutputStream) clsFileOutputStream2);
                Context context = this.crashlyticsCore.getContext();
                StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
                boolean isEmulator = CommonUtils.isEmulator(context);
                Map<IdManager.DeviceIdentifierType, String> ids = this.idManager.getDeviceIdentifiers();
                int state = CommonUtils.getDeviceState(context);
                SessionProtobufHelper.writeSessionDevice(cos, this.idManager.getDeviceUUID(), CommonUtils.getCpuArchitectureInt(), Build.MODEL, Runtime.getRuntime().availableProcessors(), CommonUtils.getTotalRamInBytes(), ((long) statFs.getBlockCount()) * ((long) statFs.getBlockSize()), isEmulator, ids, state, Build.MANUFACTURER, Build.PRODUCT);
                CommonUtils.flushOrLog(cos, "Failed to flush session device info.");
                CommonUtils.closeOrLog(clsFileOutputStream2, "Failed to close session device file.");
            } catch (Throwable th) {
                th = th;
                clsFileOutputStream = clsFileOutputStream2;
                CommonUtils.flushOrLog(cos, "Failed to flush session device info.");
                CommonUtils.closeOrLog(clsFileOutputStream, "Failed to close session device file.");
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            CommonUtils.flushOrLog(cos, "Failed to flush session device info.");
            CommonUtils.closeOrLog(clsFileOutputStream, "Failed to close session device file.");
            throw th;
        }
    }

    private void writeSessionUser(String sessionId) throws Exception {
        FileOutputStream fos = null;
        CodedOutputStream cos = null;
        try {
            FileOutputStream fos2 = new ClsFileOutputStream(getFilesDir(), sessionId + SESSION_USER_TAG);
            try {
                cos = CodedOutputStream.newInstance((OutputStream) fos2);
                UserMetaData userMetaData = getUserMetaData(sessionId);
                if (userMetaData.isEmpty()) {
                    CommonUtils.flushOrLog(cos, "Failed to flush session user file.");
                    CommonUtils.closeOrLog(fos2, "Failed to close session user file.");
                    return;
                }
                SessionProtobufHelper.writeSessionUser(cos, userMetaData.id, userMetaData.name, userMetaData.email);
                CommonUtils.flushOrLog(cos, "Failed to flush session user file.");
                CommonUtils.closeOrLog(fos2, "Failed to close session user file.");
            } catch (Throwable th) {
                th = th;
                fos = fos2;
            }
        } catch (Throwable th2) {
            th = th2;
            CommonUtils.flushOrLog(cos, "Failed to flush session user file.");
            CommonUtils.closeOrLog(fos, "Failed to close session user file.");
            throw th;
        }
    }

    private void writeSessionEvent(CodedOutputStream cos, Date time, Thread thread, Throwable ex, String eventType, boolean includeAllThreads) throws Exception {
        Thread[] threads;
        Map<String, String> attributes;
        Context context = this.crashlyticsCore.getContext();
        long eventTime = time.getTime() / 1000;
        float batteryLevel = CommonUtils.getBatteryLevel(context);
        int batteryVelocity = CommonUtils.getBatteryVelocity(context, this.devicePowerStateListener.isPowerConnected());
        boolean proximityEnabled = CommonUtils.getProximitySensorEnabled(context);
        int orientation = context.getResources().getConfiguration().orientation;
        long usedRamBytes = CommonUtils.getTotalRamInBytes() - CommonUtils.calculateFreeRamInBytes(context);
        long diskUsedBytes = CommonUtils.calculateUsedDiskSpaceInBytes(Environment.getDataDirectory().getPath());
        ActivityManager.RunningAppProcessInfo runningAppProcessInfo = CommonUtils.getAppProcessInfo(context.getPackageName(), context);
        List<StackTraceElement[]> stacks = new LinkedList<>();
        StackTraceElement[] exceptionStack = ex.getStackTrace();
        String buildId = this.crashlyticsCore.getBuildId();
        String appIdentifier = this.idManager.getAppIdentifier();
        if (includeAllThreads) {
            Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
            threads = new Thread[allStackTraces.size()];
            int i = 0;
            for (Map.Entry<Thread, StackTraceElement[]> entry : allStackTraces.entrySet()) {
                threads[i] = entry.getKey();
                stacks.add(entry.getValue());
                i++;
            }
        } else {
            threads = new Thread[0];
        }
        if (!CommonUtils.getBooleanResourceValue(context, "com.crashlytics.CollectCustomKeys", true)) {
            attributes = new TreeMap<>();
        } else {
            attributes = this.crashlyticsCore.getAttributes();
            if (attributes != null && attributes.size() > 1) {
                attributes = new TreeMap<>(attributes);
            }
        }
        SessionProtobufHelper.writeSessionEvent(cos, eventTime, eventType, ex, thread, exceptionStack, threads, stacks, attributes, this.logFileManager, runningAppProcessInfo, orientation, appIdentifier, buildId, batteryLevel, batteryVelocity, proximityEnabled, usedRamBytes, diskUsedBytes);
    }

    private void writeSessionPartsToSessionFile(File sessionBeginFile, String sessionId, int maxLoggedExceptionsCount) {
        Fabric.getLogger().d(CrashlyticsCore.TAG, "Collecting session parts for ID " + sessionId);
        File[] fatalFiles = listFilesMatching(new FileNameContainsFilter(sessionId + SESSION_FATAL_TAG));
        boolean hasFatal = fatalFiles != null && fatalFiles.length > 0;
        Fabric.getLogger().d(CrashlyticsCore.TAG, String.format(Locale.US, "Session %s has fatal exception: %s", new Object[]{sessionId, Boolean.valueOf(hasFatal)}));
        File[] nonFatalFiles = listFilesMatching(new FileNameContainsFilter(sessionId + SESSION_NON_FATAL_TAG));
        boolean hasNonFatal = nonFatalFiles != null && nonFatalFiles.length > 0;
        Fabric.getLogger().d(CrashlyticsCore.TAG, String.format(Locale.US, "Session %s has non-fatal exceptions: %s", new Object[]{sessionId, Boolean.valueOf(hasNonFatal)}));
        if (hasFatal || hasNonFatal) {
            synthesizeSessionFile(sessionBeginFile, sessionId, getTrimmedNonFatalFiles(sessionId, nonFatalFiles, maxLoggedExceptionsCount), hasFatal ? fatalFiles[0] : null);
        } else {
            Fabric.getLogger().d(CrashlyticsCore.TAG, "No events present for session ID " + sessionId);
        }
        Fabric.getLogger().d(CrashlyticsCore.TAG, "Removing session part files for ID " + sessionId);
        deleteSessionPartFilesFor(sessionId);
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x00a5  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x00a9  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x00b7  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x00bb  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void synthesizeSessionFile(java.io.File r15, java.lang.String r16, java.io.File[] r17, java.io.File r18) {
        /*
            r14 = this;
            if (r18 == 0) goto L_0x0075
            r7 = 1
        L_0x0003:
            r4 = 0
            r5 = 0
            r2 = 0
            com.crashlytics.android.core.ClsFileOutputStream r6 = new com.crashlytics.android.core.ClsFileOutputStream     // Catch:{ Exception -> 0x007e }
            java.io.File r8 = r14.getFilesDir()     // Catch:{ Exception -> 0x007e }
            r0 = r16
            r6.<init>((java.io.File) r8, (java.lang.String) r0)     // Catch:{ Exception -> 0x007e }
            com.crashlytics.android.core.CodedOutputStream r2 = com.crashlytics.android.core.CodedOutputStream.newInstance((java.io.OutputStream) r6)     // Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            io.fabric.sdk.android.Logger r8 = io.fabric.sdk.android.Fabric.getLogger()     // Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            java.lang.String r9 = "CrashlyticsCore"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            r10.<init>()     // Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            java.lang.String r11 = "Collecting SessionStart data for session ID "
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            r0 = r16
            java.lang.StringBuilder r10 = r10.append(r0)     // Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            java.lang.String r10 = r10.toString()     // Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            r8.d(r9, r10)     // Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            writeToCosFromFile(r2, r15)     // Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            r8 = 4
            java.util.Date r9 = new java.util.Date     // Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            r9.<init>()     // Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            long r10 = r9.getTime()     // Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            r12 = 1000(0x3e8, double:4.94E-321)
            long r10 = r10 / r12
            r2.writeUInt64(r8, r10)     // Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            r8 = 5
            r2.writeBool(r8, r7)     // Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            r8 = 11
            r9 = 1
            r2.writeUInt32(r8, r9)     // Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            r8 = 12
            r9 = 3
            r2.writeEnum(r8, r9)     // Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            r0 = r16
            r14.writeInitialPartsTo(r2, r0)     // Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            r0 = r17
            r1 = r16
            writeNonFatalEventsTo(r2, r0, r1)     // Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
            if (r7 == 0) goto L_0x0069
            r0 = r18
            writeToCosFromFile(r2, r0)     // Catch:{ Exception -> 0x00c4, all -> 0x00c1 }
        L_0x0069:
            java.lang.String r8 = "Error flushing session file stream"
            io.fabric.sdk.android.services.common.CommonUtils.flushOrLog(r2, r8)
            if (r4 == 0) goto L_0x0077
            r14.closeWithoutRenamingOrLog(r6)
            r5 = r6
        L_0x0074:
            return
        L_0x0075:
            r7 = 0
            goto L_0x0003
        L_0x0077:
            java.lang.String r8 = "Failed to close CLS file"
            io.fabric.sdk.android.services.common.CommonUtils.closeOrLog(r6, r8)
            r5 = r6
            goto L_0x0074
        L_0x007e:
            r3 = move-exception
        L_0x007f:
            io.fabric.sdk.android.Logger r8 = io.fabric.sdk.android.Fabric.getLogger()     // Catch:{ all -> 0x00af }
            java.lang.String r9 = "CrashlyticsCore"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ all -> 0x00af }
            r10.<init>()     // Catch:{ all -> 0x00af }
            java.lang.String r11 = "Failed to write session file for session ID: "
            java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ all -> 0x00af }
            r0 = r16
            java.lang.StringBuilder r10 = r10.append(r0)     // Catch:{ all -> 0x00af }
            java.lang.String r10 = r10.toString()     // Catch:{ all -> 0x00af }
            r8.e(r9, r10, r3)     // Catch:{ all -> 0x00af }
            r4 = 1
            java.lang.String r8 = "Error flushing session file stream"
            io.fabric.sdk.android.services.common.CommonUtils.flushOrLog(r2, r8)
            if (r4 == 0) goto L_0x00a9
            r14.closeWithoutRenamingOrLog(r5)
            goto L_0x0074
        L_0x00a9:
            java.lang.String r8 = "Failed to close CLS file"
            io.fabric.sdk.android.services.common.CommonUtils.closeOrLog(r5, r8)
            goto L_0x0074
        L_0x00af:
            r8 = move-exception
        L_0x00b0:
            java.lang.String r9 = "Error flushing session file stream"
            io.fabric.sdk.android.services.common.CommonUtils.flushOrLog(r2, r9)
            if (r4 == 0) goto L_0x00bb
            r14.closeWithoutRenamingOrLog(r5)
        L_0x00ba:
            throw r8
        L_0x00bb:
            java.lang.String r9 = "Failed to close CLS file"
            io.fabric.sdk.android.services.common.CommonUtils.closeOrLog(r5, r9)
            goto L_0x00ba
        L_0x00c1:
            r8 = move-exception
            r5 = r6
            goto L_0x00b0
        L_0x00c4:
            r3 = move-exception
            r5 = r6
            goto L_0x007f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.crashlytics.android.core.CrashlyticsUncaughtExceptionHandler.synthesizeSessionFile(java.io.File, java.lang.String, java.io.File[], java.io.File):void");
    }

    private static void writeNonFatalEventsTo(CodedOutputStream cos, File[] nonFatalFiles, String sessionId) {
        Arrays.sort(nonFatalFiles, CommonUtils.FILE_MODIFIED_COMPARATOR);
        for (File nonFatalFile : nonFatalFiles) {
            try {
                Fabric.getLogger().d(CrashlyticsCore.TAG, String.format(Locale.US, "Found Non Fatal for session ID %s in %s ", new Object[]{sessionId, nonFatalFile.getName()}));
                writeToCosFromFile(cos, nonFatalFile);
            } catch (Exception e) {
                Fabric.getLogger().e(CrashlyticsCore.TAG, "Error writting non-fatal to session.", e);
            }
        }
    }

    private void writeInitialPartsTo(CodedOutputStream cos, String sessionId) throws IOException {
        for (String tag : INITIAL_SESSION_PART_TAGS) {
            File[] sessionPartFiles = listFilesMatching(new FileNameContainsFilter(sessionId + tag));
            if (sessionPartFiles.length == 0) {
                Fabric.getLogger().e(CrashlyticsCore.TAG, "Can't find " + tag + " data for session ID " + sessionId, (Throwable) null);
            } else {
                Fabric.getLogger().d(CrashlyticsCore.TAG, "Collecting " + tag + " data for session ID " + sessionId);
                writeToCosFromFile(cos, sessionPartFiles[0]);
            }
        }
    }

    private static void writeToCosFromFile(CodedOutputStream cos, File file) throws IOException {
        if (!file.exists()) {
            Fabric.getLogger().e(CrashlyticsCore.TAG, "Tried to include a file that doesn't exist: " + file.getName(), (Throwable) null);
            return;
        }
        FileInputStream fis = null;
        try {
            FileInputStream fis2 = new FileInputStream(file);
            try {
                copyToCodedOutputStream(fis2, cos, (int) file.length());
                CommonUtils.closeOrLog(fis2, "Failed to close file input stream.");
            } catch (Throwable th) {
                th = th;
                fis = fis2;
                CommonUtils.closeOrLog(fis, "Failed to close file input stream.");
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            CommonUtils.closeOrLog(fis, "Failed to close file input stream.");
            throw th;
        }
    }

    private static void copyToCodedOutputStream(InputStream inStream, CodedOutputStream cos, int bufferLength) throws IOException {
        int numRead;
        byte[] buffer = new byte[bufferLength];
        int offset = 0;
        while (offset < buffer.length && (numRead = inStream.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        cos.writeRawBytes(buffer);
    }

    private UserMetaData getUserMetaData(String sessionId) {
        return isHandlingException() ? new UserMetaData(this.crashlyticsCore.getUserIdentifier(), this.crashlyticsCore.getUserName(), this.crashlyticsCore.getUserEmail()) : new MetaDataStore(getFilesDir()).readUserData(sessionId);
    }

    private void sendSessionReports() {
        for (File finishedSessionFile : listCompleteSessionFiles()) {
            this.executorServiceWrapper.executeAsync((Runnable) new SendSessionRunnable(this.crashlyticsCore, finishedSessionFile));
        }
    }

    /* access modifiers changed from: private */
    public File getFilesDir() {
        return this.fileStore.getFilesDir();
    }

    private static final class SendSessionRunnable implements Runnable {
        private final CrashlyticsCore crashlyticsCore;
        private final File fileToSend;

        public SendSessionRunnable(CrashlyticsCore crashlyticsCore2, File fileToSend2) {
            this.crashlyticsCore = crashlyticsCore2;
            this.fileToSend = fileToSend2;
        }

        public void run() {
            if (CommonUtils.canTryConnection(this.crashlyticsCore.getContext())) {
                Fabric.getLogger().d(CrashlyticsCore.TAG, "Attempting to send crash report at time of crash...");
                CreateReportSpiCall call = this.crashlyticsCore.getCreateReportSpiCall(Settings.getInstance().awaitSettingsData());
                if (call != null) {
                    new ReportUploader(call).forceUpload(new SessionReport(this.fileToSend, CrashlyticsUncaughtExceptionHandler.SEND_AT_CRASHTIME_HEADER));
                }
            }
        }
    }
}
