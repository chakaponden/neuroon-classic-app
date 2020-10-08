package com.crashlytics.android.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.ScrollView;
import android.widget.TextView;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.core.internal.CrashEventDataProvider;
import com.crashlytics.android.core.internal.models.SessionEventData;
import com.raizlabs.android.dbflow.sql.language.Condition;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;
import io.fabric.sdk.android.services.common.ApiKey;
import io.fabric.sdk.android.services.common.CommonUtils;
import io.fabric.sdk.android.services.common.Crash;
import io.fabric.sdk.android.services.common.ExecutorUtils;
import io.fabric.sdk.android.services.common.IdManager;
import io.fabric.sdk.android.services.concurrency.DependsOn;
import io.fabric.sdk.android.services.concurrency.Priority;
import io.fabric.sdk.android.services.concurrency.PriorityCallable;
import io.fabric.sdk.android.services.concurrency.Task;
import io.fabric.sdk.android.services.concurrency.UnmetDependencyException;
import io.fabric.sdk.android.services.network.DefaultHttpRequestFactory;
import io.fabric.sdk.android.services.network.HttpMethod;
import io.fabric.sdk.android.services.network.HttpRequest;
import io.fabric.sdk.android.services.network.HttpRequestFactory;
import io.fabric.sdk.android.services.persistence.FileStore;
import io.fabric.sdk.android.services.persistence.FileStoreImpl;
import io.fabric.sdk.android.services.persistence.PreferenceStore;
import io.fabric.sdk.android.services.persistence.PreferenceStoreImpl;
import io.fabric.sdk.android.services.settings.PromptSettingsData;
import io.fabric.sdk.android.services.settings.SessionSettingsData;
import io.fabric.sdk.android.services.settings.Settings;
import io.fabric.sdk.android.services.settings.SettingsData;
import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.net.ssl.HttpsURLConnection;

@DependsOn({CrashEventDataProvider.class})
public class CrashlyticsCore extends Kit<Void> {
    static final float CLS_DEFAULT_PROCESS_DELAY = 1.0f;
    static final String COLLECT_CUSTOM_KEYS = "com.crashlytics.CollectCustomKeys";
    static final String COLLECT_CUSTOM_LOGS = "com.crashlytics.CollectCustomLogs";
    static final String CRASHLYTICS_API_ENDPOINT = "com.crashlytics.ApiEndpoint";
    static final String CRASHLYTICS_REQUIRE_BUILD_ID = "com.crashlytics.RequireBuildId";
    static final boolean CRASHLYTICS_REQUIRE_BUILD_ID_DEFAULT = true;
    static final String CRASH_MARKER_FILE_NAME = "crash_marker";
    static final int DEFAULT_MAIN_HANDLER_TIMEOUT_SEC = 4;
    private static final String INITIALIZATION_MARKER_FILE_NAME = "initialization_marker";
    static final int MAX_ATTRIBUTES = 64;
    static final int MAX_ATTRIBUTE_SIZE = 1024;
    private static final String PREF_ALWAYS_SEND_REPORTS_KEY = "always_send_reports_opt_in";
    private static final boolean SHOULD_PROMPT_BEFORE_SENDING_REPORTS_DEFAULT = false;
    public static final String TAG = "CrashlyticsCore";
    private String apiKey;
    private final ConcurrentHashMap<String, String> attributes;
    private String buildId;
    private CrashlyticsFileMarker crashMarker;
    private float delay;
    private boolean disabled;
    private CrashlyticsExecutorServiceWrapper executorServiceWrapper;
    private CrashEventDataProvider externalCrashEventDataProvider;
    private FileStore fileStore;
    private CrashlyticsUncaughtExceptionHandler handler;
    private HttpRequestFactory httpRequestFactory;
    /* access modifiers changed from: private */
    public CrashlyticsFileMarker initializationMarker;
    private String installerPackageName;
    private CrashlyticsListener listener;
    private String packageName;
    private final PinningInfoProvider pinningInfo;
    private File sdkDir;
    private final long startTime;
    private String userEmail;
    private String userId;
    private String userName;
    private String versionCode;
    private String versionName;

    public static class Builder {
        private float delay = -1.0f;
        private boolean disabled = false;
        private CrashlyticsListener listener;
        private PinningInfoProvider pinningInfoProvider;

        public Builder delay(float delay2) {
            if (delay2 <= 0.0f) {
                throw new IllegalArgumentException("delay must be greater than 0");
            } else if (this.delay > 0.0f) {
                throw new IllegalStateException("delay already set.");
            } else {
                this.delay = delay2;
                return this;
            }
        }

        public Builder listener(CrashlyticsListener listener2) {
            if (listener2 == null) {
                throw new IllegalArgumentException("listener must not be null.");
            } else if (this.listener != null) {
                throw new IllegalStateException("listener already set.");
            } else {
                this.listener = listener2;
                return this;
            }
        }

        @Deprecated
        public Builder pinningInfo(PinningInfoProvider pinningInfoProvider2) {
            if (pinningInfoProvider2 == null) {
                throw new IllegalArgumentException("pinningInfoProvider must not be null.");
            } else if (this.pinningInfoProvider != null) {
                throw new IllegalStateException("pinningInfoProvider already set.");
            } else {
                this.pinningInfoProvider = pinningInfoProvider2;
                return this;
            }
        }

        public Builder disabled(boolean isDisabled) {
            this.disabled = isDisabled;
            return this;
        }

        public CrashlyticsCore build() {
            if (this.delay < 0.0f) {
                this.delay = 1.0f;
            }
            return new CrashlyticsCore(this.delay, this.listener, this.pinningInfoProvider, this.disabled);
        }
    }

    public CrashlyticsCore() {
        this(1.0f, (CrashlyticsListener) null, (PinningInfoProvider) null, false);
    }

    CrashlyticsCore(float delay2, CrashlyticsListener listener2, PinningInfoProvider pinningInfo2, boolean disabled2) {
        this(delay2, listener2, pinningInfo2, disabled2, ExecutorUtils.buildSingleThreadExecutorService("Crashlytics Exception Handler"));
    }

    CrashlyticsCore(float delay2, CrashlyticsListener listener2, PinningInfoProvider pinningInfo2, boolean disabled2, ExecutorService crashHandlerExecutor) {
        this.userId = null;
        this.userEmail = null;
        this.userName = null;
        this.delay = delay2;
        this.listener = listener2 == null ? new NoOpListener() : listener2;
        this.pinningInfo = pinningInfo2;
        this.disabled = disabled2;
        this.executorServiceWrapper = new CrashlyticsExecutorServiceWrapper(crashHandlerExecutor);
        this.attributes = new ConcurrentHashMap<>();
        this.startTime = System.currentTimeMillis();
    }

    /* access modifiers changed from: protected */
    public boolean onPreExecute() {
        return onPreExecute(super.getContext());
    }

    /* access modifiers changed from: package-private */
    public boolean onPreExecute(Context context) {
        if (this.disabled) {
            return false;
        }
        this.apiKey = new ApiKey().getValue(context);
        if (this.apiKey == null) {
            return false;
        }
        Fabric.getLogger().i(TAG, "Initializing Crashlytics " + getVersion());
        this.fileStore = new FileStoreImpl(this);
        this.crashMarker = new CrashlyticsFileMarker(CRASH_MARKER_FILE_NAME, this.fileStore);
        this.initializationMarker = new CrashlyticsFileMarker(INITIALIZATION_MARKER_FILE_NAME, this.fileStore);
        try {
            setAndValidateKitProperties(context, this.apiKey);
            UnityVersionProvider unityVersionProvider = new ManifestUnityVersionProvider(context, getPackageName());
            boolean initializeSynchronously = didPreviousInitializationFail();
            checkForPreviousCrash();
            installExceptionHandler(unityVersionProvider);
            if (!initializeSynchronously || !CommonUtils.canTryConnection(context)) {
                return true;
            }
            finishInitSynchronously();
            return false;
        } catch (CrashlyticsMissingDependencyException e) {
            throw new UnmetDependencyException((Throwable) e);
        } catch (Exception e2) {
            Fabric.getLogger().e(TAG, "Crashlytics was not started due to an exception during initialization", e2);
            return false;
        }
    }

    private void setAndValidateKitProperties(Context context, String apiKey2) throws PackageManager.NameNotFoundException {
        CrashlyticsPinningInfoProvider infoProvider = this.pinningInfo != null ? new CrashlyticsPinningInfoProvider(this.pinningInfo) : null;
        this.httpRequestFactory = new DefaultHttpRequestFactory(Fabric.getLogger());
        this.httpRequestFactory.setPinningInfoProvider(infoProvider);
        this.packageName = context.getPackageName();
        this.installerPackageName = getIdManager().getInstallerPackageName();
        Fabric.getLogger().d(TAG, "Installer package name is: " + this.installerPackageName);
        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(this.packageName, 0);
        this.versionCode = Integer.toString(packageInfo.versionCode);
        this.versionName = packageInfo.versionName == null ? IdManager.DEFAULT_VERSION_NAME : packageInfo.versionName;
        this.buildId = CommonUtils.resolveBuildId(context);
        getBuildIdValidator(this.buildId, isRequiringBuildId(context)).validate(apiKey2, this.packageName);
    }

    private void installExceptionHandler(UnityVersionProvider unityVersionProvider) {
        try {
            Fabric.getLogger().d(TAG, "Installing exception handler...");
            this.handler = new CrashlyticsUncaughtExceptionHandler(Thread.getDefaultUncaughtExceptionHandler(), this.executorServiceWrapper, getIdManager(), unityVersionProvider, this.fileStore, this);
            this.handler.openSession();
            Thread.setDefaultUncaughtExceptionHandler(this.handler);
            Fabric.getLogger().d(TAG, "Successfully installed exception handler.");
        } catch (Exception e) {
            Fabric.getLogger().e(TAG, "There was a problem installing the exception handler.", e);
        }
    }

    /* access modifiers changed from: protected */
    public Void doInBackground() {
        markInitializationStarted();
        this.handler.cleanInvalidTempFiles();
        try {
            SettingsData settingsData = Settings.getInstance().awaitSettingsData();
            if (settingsData == null) {
                Fabric.getLogger().w(TAG, "Received null settings, skipping initialization!");
            } else if (!settingsData.featuresData.collectReports) {
                Fabric.getLogger().d(TAG, "Collection of crash reports disabled in Crashlytics settings.");
                markInitializationComplete();
            } else {
                this.handler.finalizeSessions();
                CreateReportSpiCall call = getCreateReportSpiCall(settingsData);
                if (call == null) {
                    Fabric.getLogger().w(TAG, "Unable to create a call to upload reports.");
                    markInitializationComplete();
                } else {
                    new ReportUploader(call).uploadReports(this.delay);
                    markInitializationComplete();
                }
            }
        } catch (Exception e) {
            Fabric.getLogger().e(TAG, "Crashlytics encountered a problem during asynchronous initialization.", e);
        } finally {
            markInitializationComplete();
        }
        return null;
    }

    public String getIdentifier() {
        return "com.crashlytics.sdk.android.crashlytics-core";
    }

    public String getVersion() {
        return "2.3.10.127";
    }

    public static CrashlyticsCore getInstance() {
        return (CrashlyticsCore) Fabric.getKit(CrashlyticsCore.class);
    }

    public PinningInfoProvider getPinningInfoProvider() {
        if (!this.disabled) {
            return this.pinningInfo;
        }
        return null;
    }

    public void logException(Throwable throwable) {
        if (this.disabled || !ensureFabricWithCalled("prior to logging exceptions.")) {
            return;
        }
        if (throwable == null) {
            Fabric.getLogger().log(5, TAG, "Crashlytics is ignoring a request to log a null exception.");
        } else {
            this.handler.writeNonFatalException(Thread.currentThread(), throwable);
        }
    }

    public void log(String msg) {
        doLog(3, TAG, msg);
    }

    private void doLog(int priority, String tag, String msg) {
        if (!this.disabled && ensureFabricWithCalled("prior to logging messages.")) {
            this.handler.writeToLog(System.currentTimeMillis() - this.startTime, formatLogMessage(priority, tag, msg));
        }
    }

    public void log(int priority, String tag, String msg) {
        doLog(priority, tag, msg);
        Fabric.getLogger().log(priority, "" + tag, "" + msg, true);
    }

    public void setUserIdentifier(String identifier) {
        if (!this.disabled) {
            this.userId = sanitizeAttribute(identifier);
            this.handler.cacheUserData(this.userId, this.userName, this.userEmail);
        }
    }

    public void setUserName(String name) {
        if (!this.disabled) {
            this.userName = sanitizeAttribute(name);
            this.handler.cacheUserData(this.userId, this.userName, this.userEmail);
        }
    }

    public void setUserEmail(String email) {
        if (!this.disabled) {
            this.userEmail = sanitizeAttribute(email);
            this.handler.cacheUserData(this.userId, this.userName, this.userEmail);
        }
    }

    public void setString(String key, String value) {
        if (!this.disabled) {
            if (key == null) {
                Context context = getContext();
                if (context == null || !CommonUtils.isAppDebuggable(context)) {
                    Fabric.getLogger().e(TAG, "Attempting to set custom attribute with null key, ignoring.", (Throwable) null);
                    return;
                }
                throw new IllegalArgumentException("Custom attribute key must not be null.");
            }
            String key2 = sanitizeAttribute(key);
            if (this.attributes.size() < 64 || this.attributes.containsKey(key2)) {
                this.attributes.put(key2, value == null ? "" : sanitizeAttribute(value));
                this.handler.cacheKeyData(this.attributes);
                return;
            }
            Fabric.getLogger().d(TAG, "Exceeded maximum number of custom attributes (64)");
        }
    }

    public void setBool(String key, boolean value) {
        setString(key, Boolean.toString(value));
    }

    public void setDouble(String key, double value) {
        setString(key, Double.toString(value));
    }

    public void setFloat(String key, float value) {
        setString(key, Float.toString(value));
    }

    public void setInt(String key, int value) {
        setString(key, Integer.toString(value));
    }

    public void setLong(String key, long value) {
        setString(key, Long.toString(value));
    }

    public void crash() {
        new CrashTest().indexOutOfBounds();
    }

    public boolean verifyPinning(URL url) {
        try {
            return internalVerifyPinning(url);
        } catch (Exception e) {
            Fabric.getLogger().e(TAG, "Could not verify SSL pinning", e);
            return false;
        }
    }

    @Deprecated
    public synchronized void setListener(CrashlyticsListener listener2) {
        Fabric.getLogger().w(TAG, "Use of setListener is deprecated.");
        if (listener2 == null) {
            throw new IllegalArgumentException("listener must not be null.");
        }
        this.listener = listener2;
    }

    static void recordLoggedExceptionEvent(String sessionId, String exceptionName) {
        Answers answers = (Answers) Fabric.getKit(Answers.class);
        if (answers != null) {
            answers.onException(new Crash.LoggedException(sessionId, exceptionName));
        }
    }

    static void recordFatalExceptionEvent(String sessionId, String exceptionName) {
        Answers answers = (Answers) Fabric.getKit(Answers.class);
        if (answers != null) {
            answers.onException(new Crash.FatalException(sessionId, exceptionName));
        }
    }

    /* access modifiers changed from: package-private */
    public Map<String, String> getAttributes() {
        return Collections.unmodifiableMap(this.attributes);
    }

    /* access modifiers changed from: package-private */
    public BuildIdValidator getBuildIdValidator(String buildId2, boolean requireBuildId) {
        return new BuildIdValidator(buildId2, requireBuildId);
    }

    /* access modifiers changed from: package-private */
    public String getPackageName() {
        return this.packageName;
    }

    /* access modifiers changed from: package-private */
    public String getApiKey() {
        return this.apiKey;
    }

    /* access modifiers changed from: package-private */
    public String getInstallerPackageName() {
        return this.installerPackageName;
    }

    /* access modifiers changed from: package-private */
    public String getVersionName() {
        return this.versionName;
    }

    /* access modifiers changed from: package-private */
    public String getVersionCode() {
        return this.versionCode;
    }

    /* access modifiers changed from: package-private */
    public String getOverridenSpiEndpoint() {
        return CommonUtils.getStringsFileValue(getContext(), CRASHLYTICS_API_ENDPOINT);
    }

    /* access modifiers changed from: package-private */
    public String getBuildId() {
        return this.buildId;
    }

    /* access modifiers changed from: package-private */
    public CrashlyticsUncaughtExceptionHandler getHandler() {
        return this.handler;
    }

    /* access modifiers changed from: package-private */
    public String getUserIdentifier() {
        if (getIdManager().canCollectUserIds()) {
            return this.userId;
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public String getUserEmail() {
        if (getIdManager().canCollectUserIds()) {
            return this.userEmail;
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public String getUserName() {
        if (getIdManager().canCollectUserIds()) {
            return this.userName;
        }
        return null;
    }

    private void finishInitSynchronously() {
        PriorityCallable<Void> callable = new PriorityCallable<Void>() {
            public Void call() throws Exception {
                return CrashlyticsCore.this.doInBackground();
            }

            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };
        for (Task task : getDependencies()) {
            callable.addDependency(task);
        }
        Future<Void> future = getFabric().getExecutorService().submit(callable);
        Fabric.getLogger().d(TAG, "Crashlytics detected incomplete initialization on previous app launch. Will initialize synchronously.");
        try {
            future.get(4, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Fabric.getLogger().e(TAG, "Crashlytics was interrupted during initialization.", e);
        } catch (ExecutionException e2) {
            Fabric.getLogger().e(TAG, "Problem encountered during Crashlytics initialization.", e2);
        } catch (TimeoutException e3) {
            Fabric.getLogger().e(TAG, "Crashlytics timed out during initialization.", e3);
        }
    }

    /* access modifiers changed from: package-private */
    public void markInitializationStarted() {
        this.executorServiceWrapper.executeSyncLoggingException(new Callable<Void>() {
            public Void call() throws Exception {
                CrashlyticsCore.this.initializationMarker.create();
                Fabric.getLogger().d(CrashlyticsCore.TAG, "Initialization marker file created.");
                return null;
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void markInitializationComplete() {
        this.executorServiceWrapper.executeAsync(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                try {
                    boolean removed = CrashlyticsCore.this.initializationMarker.remove();
                    Fabric.getLogger().d(CrashlyticsCore.TAG, "Initialization marker file removed: " + removed);
                    return Boolean.valueOf(removed);
                } catch (Exception e) {
                    Fabric.getLogger().e(CrashlyticsCore.TAG, "Problem encountered deleting Crashlytics initialization marker.", e);
                    return false;
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public boolean didPreviousInitializationFail() {
        return ((Boolean) this.executorServiceWrapper.executeSyncLoggingException(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                return Boolean.valueOf(CrashlyticsCore.this.initializationMarker.isPresent());
            }
        })).booleanValue();
    }

    /* access modifiers changed from: package-private */
    public void setExternalCrashEventDataProvider(CrashEventDataProvider provider) {
        this.externalCrashEventDataProvider = provider;
    }

    /* access modifiers changed from: package-private */
    public SessionEventData getExternalCrashEventData() {
        if (this.externalCrashEventDataProvider != null) {
            return this.externalCrashEventDataProvider.getCrashEventData();
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public boolean internalVerifyPinning(URL url) {
        if (getPinningInfoProvider() == null) {
            return false;
        }
        HttpRequest httpRequest = this.httpRequestFactory.buildHttpRequest(HttpMethod.GET, url.toString());
        ((HttpsURLConnection) httpRequest.getConnection()).setInstanceFollowRedirects(false);
        httpRequest.code();
        return true;
    }

    /* access modifiers changed from: package-private */
    public File getSdkDirectory() {
        if (this.sdkDir == null) {
            this.sdkDir = new FileStoreImpl(this).getFilesDir();
        }
        return this.sdkDir;
    }

    /* access modifiers changed from: package-private */
    public boolean shouldPromptUserBeforeSendingCrashReports() {
        return ((Boolean) Settings.getInstance().withSettings(new Settings.SettingsAccess<Boolean>() {
            public Boolean usingSettings(SettingsData settingsData) {
                boolean z = false;
                if (!settingsData.featuresData.promptEnabled) {
                    return false;
                }
                if (!CrashlyticsCore.this.shouldSendReportsWithoutPrompting()) {
                    z = true;
                }
                return Boolean.valueOf(z);
            }
        }, false)).booleanValue();
    }

    /* access modifiers changed from: package-private */
    public boolean shouldSendReportsWithoutPrompting() {
        return new PreferenceStoreImpl(this).get().getBoolean(PREF_ALWAYS_SEND_REPORTS_KEY, false);
    }

    /* access modifiers changed from: package-private */
    @SuppressLint({"CommitPrefEdits"})
    public void setShouldSendUserReportsWithoutPrompting(boolean send) {
        PreferenceStore prefStore = new PreferenceStoreImpl(this);
        prefStore.save(prefStore.edit().putBoolean(PREF_ALWAYS_SEND_REPORTS_KEY, send));
    }

    /* access modifiers changed from: package-private */
    public boolean canSendWithUserApproval() {
        return ((Boolean) Settings.getInstance().withSettings(new Settings.SettingsAccess<Boolean>() {
            public Boolean usingSettings(SettingsData settingsData) {
                boolean send = true;
                Activity activity = CrashlyticsCore.this.getFabric().getCurrentActivity();
                if (activity != null && !activity.isFinishing() && CrashlyticsCore.this.shouldPromptUserBeforeSendingCrashReports()) {
                    send = CrashlyticsCore.this.getSendDecisionFromUser(activity, settingsData.promptData);
                }
                return Boolean.valueOf(send);
            }
        }, true)).booleanValue();
    }

    /* access modifiers changed from: package-private */
    public CreateReportSpiCall getCreateReportSpiCall(SettingsData settingsData) {
        if (settingsData != null) {
            return new DefaultCreateReportSpiCall(this, getOverridenSpiEndpoint(), settingsData.appData.reportsUrl, this.httpRequestFactory);
        }
        return null;
    }

    private void checkForPreviousCrash() {
        if (Boolean.TRUE.equals((Boolean) this.executorServiceWrapper.executeSyncLoggingException(new CrashMarkerCheck(this.crashMarker)))) {
            try {
                this.listener.crashlyticsDidDetectCrashDuringPreviousExecution();
            } catch (Exception e) {
                Fabric.getLogger().e(TAG, "Exception thrown by CrashlyticsListener while notifying of previous crash.", e);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void createCrashMarker() {
        this.crashMarker.create();
    }

    /* access modifiers changed from: private */
    public boolean getSendDecisionFromUser(Activity context, PromptSettingsData promptData) {
        final DialogStringResolver stringResolver = new DialogStringResolver(context, promptData);
        final OptInLatch latch = new OptInLatch();
        final Activity activity = context;
        final PromptSettingsData promptSettingsData = promptData;
        activity.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                DialogInterface.OnClickListener sendClickListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        latch.setOptIn(true);
                        dialog.dismiss();
                    }
                };
                float density = activity.getResources().getDisplayMetrics().density;
                int textViewPadding = CrashlyticsCore.dipsToPixels(density, 5);
                TextView textView = new TextView(activity);
                textView.setAutoLinkMask(15);
                textView.setText(stringResolver.getMessage());
                textView.setTextAppearance(activity, 16973892);
                textView.setPadding(textViewPadding, textViewPadding, textViewPadding, textViewPadding);
                textView.setFocusable(false);
                ScrollView scrollView = new ScrollView(activity);
                scrollView.setPadding(CrashlyticsCore.dipsToPixels(density, 14), CrashlyticsCore.dipsToPixels(density, 2), CrashlyticsCore.dipsToPixels(density, 10), CrashlyticsCore.dipsToPixels(density, 12));
                scrollView.addView(textView);
                builder.setView(scrollView).setTitle(stringResolver.getTitle()).setCancelable(false).setNeutralButton(stringResolver.getSendButtonTitle(), sendClickListener);
                if (promptSettingsData.showCancelButton) {
                    builder.setNegativeButton(stringResolver.getCancelButtonTitle(), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            latch.setOptIn(false);
                            dialog.dismiss();
                        }
                    });
                }
                if (promptSettingsData.showAlwaysSendButton) {
                    builder.setPositiveButton(stringResolver.getAlwaysSendButtonTitle(), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            CrashlyticsCore.this.setShouldSendUserReportsWithoutPrompting(true);
                            latch.setOptIn(true);
                            dialog.dismiss();
                        }
                    });
                }
                builder.show();
            }
        });
        Fabric.getLogger().d(TAG, "Waiting for user opt-in.");
        latch.await();
        return latch.getOptIn();
    }

    static SessionSettingsData getSessionSettingsData() {
        SettingsData settingsData = Settings.getInstance().awaitSettingsData();
        if (settingsData == null) {
            return null;
        }
        return settingsData.sessionData;
    }

    private static boolean isRequiringBuildId(Context context) {
        return CommonUtils.getBooleanResourceValue(context, CRASHLYTICS_REQUIRE_BUILD_ID, true);
    }

    private static String formatLogMessage(int priority, String tag, String msg) {
        return CommonUtils.logPriorityToString(priority) + Condition.Operation.DIVISION + tag + " " + msg;
    }

    private static boolean ensureFabricWithCalled(String msg) {
        CrashlyticsCore instance = getInstance();
        if (instance != null && instance.handler != null) {
            return true;
        }
        Fabric.getLogger().e(TAG, "Crashlytics must be initialized by calling Fabric.with(Context) " + msg, (Throwable) null);
        return false;
    }

    private static String sanitizeAttribute(String input) {
        if (input == null) {
            return input;
        }
        String input2 = input.trim();
        if (input2.length() > 1024) {
            return input2.substring(0, 1024);
        }
        return input2;
    }

    /* access modifiers changed from: private */
    public static int dipsToPixels(float density, int dips) {
        return (int) (((float) dips) * density);
    }

    private static class OptInLatch {
        private final CountDownLatch latch;
        private boolean send;

        private OptInLatch() {
            this.send = false;
            this.latch = new CountDownLatch(1);
        }

        /* access modifiers changed from: package-private */
        public void setOptIn(boolean optIn) {
            this.send = optIn;
            this.latch.countDown();
        }

        /* access modifiers changed from: package-private */
        public boolean getOptIn() {
            return this.send;
        }

        /* access modifiers changed from: package-private */
        public void await() {
            try {
                this.latch.await();
            } catch (InterruptedException e) {
            }
        }
    }

    private static final class CrashMarkerCheck implements Callable<Boolean> {
        private final CrashlyticsFileMarker crashMarker;

        public CrashMarkerCheck(CrashlyticsFileMarker crashMarker2) {
            this.crashMarker = crashMarker2;
        }

        public Boolean call() throws Exception {
            if (!this.crashMarker.isPresent()) {
                return Boolean.FALSE;
            }
            Fabric.getLogger().d(CrashlyticsCore.TAG, "Found previous crash marker.");
            this.crashMarker.remove();
            return Boolean.TRUE;
        }
    }

    private static final class NoOpListener implements CrashlyticsListener {
        private NoOpListener() {
        }

        public void crashlyticsDidDetectCrashDuringPreviousExecution() {
        }
    }
}
