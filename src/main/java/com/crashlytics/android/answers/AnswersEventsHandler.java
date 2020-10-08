package com.crashlytics.android.answers;

import android.content.Context;
import com.crashlytics.android.answers.SessionEvent;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;
import io.fabric.sdk.android.services.events.EventsStorageListener;
import io.fabric.sdk.android.services.network.HttpRequestFactory;
import io.fabric.sdk.android.services.settings.AnalyticsSettingsData;
import java.util.concurrent.ScheduledExecutorService;

class AnswersEventsHandler implements EventsStorageListener {
    /* access modifiers changed from: private */
    public final Context context;
    final ScheduledExecutorService executor;
    /* access modifiers changed from: private */
    public final AnswersFilesManagerProvider filesManagerProvider;
    /* access modifiers changed from: private */
    public final Kit kit;
    /* access modifiers changed from: private */
    public final SessionMetadataCollector metadataCollector;
    /* access modifiers changed from: private */
    public final HttpRequestFactory requestFactory;
    SessionAnalyticsManagerStrategy strategy = new DisabledSessionAnalyticsManagerStrategy();

    public AnswersEventsHandler(Kit kit2, Context context2, AnswersFilesManagerProvider filesManagerProvider2, SessionMetadataCollector metadataCollector2, HttpRequestFactory requestFactory2, ScheduledExecutorService executor2) {
        this.kit = kit2;
        this.context = context2;
        this.filesManagerProvider = filesManagerProvider2;
        this.metadataCollector = metadataCollector2;
        this.requestFactory = requestFactory2;
        this.executor = executor2;
    }

    public void processEventAsync(SessionEvent.Builder eventBuilder) {
        processEvent(eventBuilder, false, false);
    }

    public void processEventAsyncAndFlush(SessionEvent.Builder eventBuilder) {
        processEvent(eventBuilder, false, true);
    }

    public void processEventSync(SessionEvent.Builder eventBuilder) {
        processEvent(eventBuilder, true, false);
    }

    public void setAnalyticsSettingsData(final AnalyticsSettingsData analyticsSettingsData, final String protocolAndHostOverride) {
        executeAsync(new Runnable() {
            public void run() {
                try {
                    AnswersEventsHandler.this.strategy.setAnalyticsSettingsData(analyticsSettingsData, protocolAndHostOverride);
                } catch (Exception e) {
                    Fabric.getLogger().e(Answers.TAG, "Failed to set analytics settings data", e);
                }
            }
        });
    }

    public void disable() {
        executeAsync(new Runnable() {
            public void run() {
                try {
                    SessionAnalyticsManagerStrategy prevStrategy = AnswersEventsHandler.this.strategy;
                    AnswersEventsHandler.this.strategy = new DisabledSessionAnalyticsManagerStrategy();
                    prevStrategy.deleteAllEvents();
                } catch (Exception e) {
                    Fabric.getLogger().e(Answers.TAG, "Failed to disable events", e);
                }
            }
        });
    }

    public void onRollOver(String rolledOverFile) {
        executeAsync(new Runnable() {
            public void run() {
                try {
                    AnswersEventsHandler.this.strategy.sendEvents();
                } catch (Exception e) {
                    Fabric.getLogger().e(Answers.TAG, "Failed to send events files", e);
                }
            }
        });
    }

    public void enable() {
        executeAsync(new Runnable() {
            public void run() {
                try {
                    SessionEventMetadata metadata = AnswersEventsHandler.this.metadataCollector.getMetadata();
                    SessionAnalyticsFilesManager filesManager = AnswersEventsHandler.this.filesManagerProvider.getAnalyticsFilesManager();
                    filesManager.registerRollOverListener(AnswersEventsHandler.this);
                    AnswersEventsHandler.this.strategy = new EnabledSessionAnalyticsManagerStrategy(AnswersEventsHandler.this.kit, AnswersEventsHandler.this.context, AnswersEventsHandler.this.executor, filesManager, AnswersEventsHandler.this.requestFactory, metadata);
                } catch (Exception e) {
                    Fabric.getLogger().e(Answers.TAG, "Failed to enable events", e);
                }
            }
        });
    }

    public void flushEvents() {
        executeAsync(new Runnable() {
            public void run() {
                try {
                    AnswersEventsHandler.this.strategy.rollFileOver();
                } catch (Exception e) {
                    Fabric.getLogger().e(Answers.TAG, "Failed to flush events", e);
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void processEvent(final SessionEvent.Builder eventBuilder, boolean sync, final boolean flush) {
        Runnable runnable = new Runnable() {
            public void run() {
                try {
                    AnswersEventsHandler.this.strategy.processEvent(eventBuilder);
                    if (flush) {
                        AnswersEventsHandler.this.strategy.rollFileOver();
                    }
                } catch (Exception e) {
                    Fabric.getLogger().e(Answers.TAG, "Failed to process event", e);
                }
            }
        };
        if (sync) {
            executeSync(runnable);
        } else {
            executeAsync(runnable);
        }
    }

    private void executeSync(Runnable runnable) {
        try {
            this.executor.submit(runnable).get();
        } catch (Exception e) {
            Fabric.getLogger().e(Answers.TAG, "Failed to run events task", e);
        }
    }

    private void executeAsync(Runnable runnable) {
        try {
            this.executor.submit(runnable);
        } catch (Exception e) {
            Fabric.getLogger().e(Answers.TAG, "Failed to submit events task", e);
        }
    }
}
