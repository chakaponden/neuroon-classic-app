package com.crashlytics.android.answers;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import com.crashlytics.android.answers.BackgroundManager;
import com.crashlytics.android.answers.SessionEvent;
import io.fabric.sdk.android.ActivityLifecycleManager;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;
import io.fabric.sdk.android.services.common.ExecutorUtils;
import io.fabric.sdk.android.services.common.IdManager;
import io.fabric.sdk.android.services.network.DefaultHttpRequestFactory;
import io.fabric.sdk.android.services.persistence.FileStoreImpl;
import io.fabric.sdk.android.services.settings.AnalyticsSettingsData;
import java.util.concurrent.ScheduledExecutorService;

class SessionAnalyticsManager implements BackgroundManager.Listener {
    static final String EXECUTOR_SERVICE = "Answers Events Handler";
    static final long FIRST_LAUNCH_INTERVAL_IN_MS = 3600000;
    static final String ON_CRASH_ERROR_MSG = "onCrash called from main thread!!!";
    final BackgroundManager backgroundManager;
    final AnswersEventsHandler eventsHandler;
    private final long installedAt;
    final ActivityLifecycleManager lifecycleManager;
    final AnswersPreferenceManager preferenceManager;

    public static SessionAnalyticsManager build(Kit kit, Context context, IdManager idManager, String versionCode, String versionName, long installedAt2) {
        SessionMetadataCollector metadataCollector = new SessionMetadataCollector(context, idManager, versionCode, versionName);
        AnswersFilesManagerProvider filesManagerProvider = new AnswersFilesManagerProvider(context, new FileStoreImpl(kit));
        DefaultHttpRequestFactory httpRequestFactory = new DefaultHttpRequestFactory(Fabric.getLogger());
        ActivityLifecycleManager lifecycleManager2 = new ActivityLifecycleManager(context);
        ScheduledExecutorService executorService = ExecutorUtils.buildSingleThreadScheduledExecutorService(EXECUTOR_SERVICE);
        BackgroundManager backgroundManager2 = new BackgroundManager(executorService);
        return new SessionAnalyticsManager(new AnswersEventsHandler(kit, context, filesManagerProvider, metadataCollector, httpRequestFactory, executorService), lifecycleManager2, backgroundManager2, AnswersPreferenceManager.build(context), installedAt2);
    }

    SessionAnalyticsManager(AnswersEventsHandler eventsHandler2, ActivityLifecycleManager lifecycleManager2, BackgroundManager backgroundManager2, AnswersPreferenceManager preferenceManager2, long installedAt2) {
        this.eventsHandler = eventsHandler2;
        this.lifecycleManager = lifecycleManager2;
        this.backgroundManager = backgroundManager2;
        this.preferenceManager = preferenceManager2;
        this.installedAt = installedAt2;
    }

    public void enable() {
        this.eventsHandler.enable();
        this.lifecycleManager.registerCallbacks(new AnswersLifecycleCallbacks(this, this.backgroundManager));
        this.backgroundManager.registerListener(this);
        if (isFirstLaunch(this.installedAt)) {
            onInstall();
            this.preferenceManager.setAnalyticsLaunched();
        }
    }

    public void disable() {
        this.lifecycleManager.resetCallbacks();
        this.eventsHandler.disable();
    }

    public void onCustom(CustomEvent event) {
        Fabric.getLogger().d(Answers.TAG, "Logged custom event: " + event);
        this.eventsHandler.processEventAsync(SessionEvent.customEventBuilder(event));
    }

    public void onPredefined(PredefinedEvent event) {
        Fabric.getLogger().d(Answers.TAG, "Logged predefined event: " + event);
        this.eventsHandler.processEventAsync(SessionEvent.predefinedEventBuilder(event));
    }

    public void onCrash(String sessionId, String exceptionName) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException(ON_CRASH_ERROR_MSG);
        }
        Fabric.getLogger().d(Answers.TAG, "Logged crash");
        this.eventsHandler.processEventSync(SessionEvent.crashEventBuilder(sessionId, exceptionName));
    }

    public void onError(String sessionId) {
    }

    public void onInstall() {
        Fabric.getLogger().d(Answers.TAG, "Logged install");
        this.eventsHandler.processEventAsyncAndFlush(SessionEvent.installEventBuilder());
    }

    public void onLifecycle(Activity activity, SessionEvent.Type type) {
        Fabric.getLogger().d(Answers.TAG, "Logged lifecycle event: " + type.name());
        this.eventsHandler.processEventAsync(SessionEvent.lifecycleEventBuilder(type, activity));
    }

    public void onBackground() {
        Fabric.getLogger().d(Answers.TAG, "Flush events when app is backgrounded");
        this.eventsHandler.flushEvents();
    }

    public void setAnalyticsSettingsData(AnalyticsSettingsData analyticsSettingsData, String protocolAndHostOverride) {
        this.backgroundManager.setFlushOnBackground(analyticsSettingsData.flushOnBackground);
        this.eventsHandler.setAnalyticsSettingsData(analyticsSettingsData, protocolAndHostOverride);
    }

    /* access modifiers changed from: package-private */
    public boolean isFirstLaunch(long installedAt2) {
        return !this.preferenceManager.hasAnalyticsLaunched() && installedRecently(installedAt2);
    }

    /* access modifiers changed from: package-private */
    public boolean installedRecently(long installedAt2) {
        return System.currentTimeMillis() - installedAt2 < FIRST_LAUNCH_INTERVAL_IN_MS;
    }
}
