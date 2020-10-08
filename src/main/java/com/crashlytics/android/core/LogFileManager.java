package com.crashlytics.android.core;

import android.content.Context;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.common.CommonUtils;
import io.fabric.sdk.android.services.persistence.FileStore;
import java.io.File;
import java.util.Set;

class LogFileManager {
    private static final String DIRECTORY_NAME = "log-files";
    private static final String LOGFILE_EXT = ".temp";
    private static final String LOGFILE_PREFIX = "crashlytics-userlog-";
    static final int MAX_LOG_SIZE = 65536;
    private static final NoopLogStore NOOP_LOG_STORE = new NoopLogStore();
    private final Context context;
    private FileLogStore currentLog;
    private final FileStore fileStore;

    public LogFileManager(Context context2, FileStore fileStore2) {
        this(context2, fileStore2, (String) null);
    }

    public LogFileManager(Context context2, FileStore fileStore2, String currentSessionId) {
        this.context = context2;
        this.fileStore = fileStore2;
        this.currentLog = NOOP_LOG_STORE;
        setCurrentSession(currentSessionId);
    }

    public final void setCurrentSession(String sessionId) {
        this.currentLog.closeLogFile();
        this.currentLog = NOOP_LOG_STORE;
        if (sessionId != null) {
            if (!isLoggingEnabled()) {
                Fabric.getLogger().d(CrashlyticsCore.TAG, "Preferences requested no custom logs. Aborting log file creation.");
            } else {
                setLogFile(getWorkingFileForSession(sessionId), 65536);
            }
        }
    }

    public void writeToLog(long timestamp, String msg) {
        this.currentLog.writeToLog(timestamp, msg);
    }

    public ByteString getByteStringForLog() {
        return this.currentLog.getLogAsByteString();
    }

    public void clearLog() {
        this.currentLog.deleteLogFile();
    }

    public void discardOldLogFiles(Set<String> sessionIdsToKeep) {
        File[] logFiles = getLogFileDir().listFiles();
        if (logFiles != null) {
            for (File file : logFiles) {
                if (!sessionIdsToKeep.contains(getSessionIdForFile(file))) {
                    file.delete();
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setLogFile(File workingFile, int maxLogSize) {
        this.currentLog = new QueueFileLogStore(workingFile, maxLogSize);
    }

    private File getWorkingFileForSession(String sessionId) {
        return new File(getLogFileDir(), LOGFILE_PREFIX + sessionId + LOGFILE_EXT);
    }

    private String getSessionIdForFile(File workingFile) {
        String filename = workingFile.getName();
        int indexOfExtension = filename.lastIndexOf(LOGFILE_EXT);
        return indexOfExtension == -1 ? filename : filename.substring(LOGFILE_PREFIX.length(), indexOfExtension);
    }

    private boolean isLoggingEnabled() {
        return CommonUtils.getBooleanResourceValue(this.context, "com.crashlytics.CollectCustomLogs", true);
    }

    private File getLogFileDir() {
        File logFileDir = new File(this.fileStore.getFilesDir(), DIRECTORY_NAME);
        if (!logFileDir.exists()) {
            logFileDir.mkdirs();
        }
        return logFileDir;
    }

    private static final class NoopLogStore implements FileLogStore {
        private NoopLogStore() {
        }

        public void writeToLog(long timestamp, String msg) {
        }

        public ByteString getLogAsByteString() {
            return null;
        }

        public void closeLogFile() {
        }

        public void deleteLogFile() {
        }
    }
}
