package io.fabric.sdk.android.services.events;

import android.content.Context;
import io.fabric.sdk.android.services.common.CommonUtils;
import java.util.concurrent.ScheduledExecutorService;

public abstract class EventsHandler<T> implements EventsStorageListener {
    protected final Context context;
    protected final ScheduledExecutorService executor;
    protected EventsStrategy<T> strategy;

    /* access modifiers changed from: protected */
    public abstract EventsStrategy<T> getDisabledEventsStrategy();

    public EventsHandler(Context context2, EventsStrategy<T> strategy2, EventsFilesManager filesManager, ScheduledExecutorService executor2) {
        this.context = context2.getApplicationContext();
        this.executor = executor2;
        this.strategy = strategy2;
        filesManager.registerRollOverListener(this);
    }

    public void recordEventAsync(final T event, final boolean sendImmediately) {
        executeAsync(new Runnable() {
            public void run() {
                try {
                    EventsHandler.this.strategy.recordEvent(event);
                    if (sendImmediately) {
                        EventsHandler.this.strategy.rollFileOver();
                    }
                } catch (Exception e) {
                    CommonUtils.logControlledError(EventsHandler.this.context, "Failed to record event.", e);
                }
            }
        });
    }

    public void recordEventSync(final T event) {
        executeSync(new Runnable() {
            public void run() {
                try {
                    EventsHandler.this.strategy.recordEvent(event);
                } catch (Exception e) {
                    CommonUtils.logControlledError(EventsHandler.this.context, "Crashlytics failed to record event", e);
                }
            }
        });
    }

    public void onRollOver(String rolledOverFile) {
        executeAsync(new Runnable() {
            public void run() {
                try {
                    EventsHandler.this.strategy.sendEvents();
                } catch (Exception e) {
                    CommonUtils.logControlledError(EventsHandler.this.context, "Failed to send events files.", e);
                }
            }
        });
    }

    public void disable() {
        executeAsync(new Runnable() {
            public void run() {
                try {
                    EventsStrategy<T> prevStrategy = EventsHandler.this.strategy;
                    EventsHandler.this.strategy = EventsHandler.this.getDisabledEventsStrategy();
                    prevStrategy.deleteAllEvents();
                } catch (Exception e) {
                    CommonUtils.logControlledError(EventsHandler.this.context, "Failed to disable events.", e);
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void executeSync(Runnable runnable) {
        try {
            this.executor.submit(runnable).get();
        } catch (Exception e) {
            CommonUtils.logControlledError(this.context, "Failed to run events task", e);
        }
    }

    /* access modifiers changed from: protected */
    public void executeAsync(Runnable runnable) {
        try {
            this.executor.submit(runnable);
        } catch (Exception e) {
            CommonUtils.logControlledError(this.context, "Failed to submit events task", e);
        }
    }
}
