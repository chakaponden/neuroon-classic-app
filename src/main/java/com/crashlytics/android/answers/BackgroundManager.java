package com.crashlytics.android.answers;

import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

class BackgroundManager {
    private static final int BACKGROUND_DELAY = 5000;
    final AtomicReference<ScheduledFuture<?>> backgroundFutureRef = new AtomicReference<>();
    private final ScheduledExecutorService executorService;
    private volatile boolean flushOnBackground = true;
    boolean inBackground = true;
    private final List<Listener> listeners = new ArrayList();

    public interface Listener {
        void onBackground();
    }

    public BackgroundManager(ScheduledExecutorService executorService2) {
        this.executorService = executorService2;
    }

    public void setFlushOnBackground(boolean flushOnBackground2) {
        this.flushOnBackground = flushOnBackground2;
    }

    /* access modifiers changed from: private */
    public void notifyBackground() {
        for (Listener listener : this.listeners) {
            listener.onBackground();
        }
    }

    public void registerListener(Listener listener) {
        this.listeners.add(listener);
    }

    public void onActivityResumed() {
        this.inBackground = false;
        ScheduledFuture backgroundFuture = this.backgroundFutureRef.getAndSet((Object) null);
        if (backgroundFuture != null) {
            backgroundFuture.cancel(false);
        }
    }

    public void onActivityPaused() {
        if (this.flushOnBackground && !this.inBackground) {
            this.inBackground = true;
            try {
                this.backgroundFutureRef.compareAndSet((Object) null, this.executorService.schedule(new Runnable() {
                    public void run() {
                        BackgroundManager.this.backgroundFutureRef.set((Object) null);
                        BackgroundManager.this.notifyBackground();
                    }
                }, 5000, TimeUnit.MILLISECONDS));
            } catch (RejectedExecutionException e) {
                Fabric.getLogger().d(Answers.TAG, "Failed to schedule background detector", e);
            }
        }
    }
}
