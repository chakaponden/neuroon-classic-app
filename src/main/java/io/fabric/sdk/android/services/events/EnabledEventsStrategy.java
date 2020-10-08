package io.fabric.sdk.android.services.events;

import android.content.Context;
import io.fabric.sdk.android.services.common.CommonUtils;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public abstract class EnabledEventsStrategy<T> implements EventsStrategy<T> {
    static final int UNDEFINED_ROLLOVER_INTERVAL_SECONDS = -1;
    protected final Context context;
    final ScheduledExecutorService executorService;
    protected final EventsFilesManager<T> filesManager;
    volatile int rolloverIntervalSeconds = -1;
    final AtomicReference<ScheduledFuture<?>> scheduledRolloverFutureRef;

    public EnabledEventsStrategy(Context context2, ScheduledExecutorService executorService2, EventsFilesManager<T> filesManager2) {
        this.context = context2;
        this.executorService = executorService2;
        this.filesManager = filesManager2;
        this.scheduledRolloverFutureRef = new AtomicReference<>();
    }

    public void scheduleTimeBasedRollOverIfNeeded() {
        if (this.rolloverIntervalSeconds != -1) {
            scheduleTimeBasedFileRollOver((long) this.rolloverIntervalSeconds, (long) this.rolloverIntervalSeconds);
        }
    }

    public void sendEvents() {
        sendAndCleanUpIfSuccess();
    }

    public void cancelTimeBasedFileRollOver() {
        if (this.scheduledRolloverFutureRef.get() != null) {
            CommonUtils.logControlled(this.context, "Cancelling time-based rollover because no events are currently being generated.");
            this.scheduledRolloverFutureRef.get().cancel(false);
            this.scheduledRolloverFutureRef.set((Object) null);
        }
    }

    public void deleteAllEvents() {
        this.filesManager.deleteAllEventsFiles();
    }

    public void recordEvent(T event) {
        CommonUtils.logControlled(this.context, event.toString());
        try {
            this.filesManager.writeEvent(event);
        } catch (IOException e) {
            CommonUtils.logControlledError(this.context, "Failed to write event.", e);
        }
        scheduleTimeBasedRollOverIfNeeded();
    }

    public boolean rollFileOver() {
        try {
            return this.filesManager.rollFileOver();
        } catch (IOException e) {
            CommonUtils.logControlledError(this.context, "Failed to roll file over.", e);
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public void configureRollover(int rolloverIntervalSeconds2) {
        this.rolloverIntervalSeconds = rolloverIntervalSeconds2;
        scheduleTimeBasedFileRollOver(0, (long) this.rolloverIntervalSeconds);
    }

    public int getRollover() {
        return this.rolloverIntervalSeconds;
    }

    /* access modifiers changed from: package-private */
    public void scheduleTimeBasedFileRollOver(long initialDelaySecs, long frequencySecs) {
        if (this.scheduledRolloverFutureRef.get() == null) {
            Runnable rollOverRunnable = new TimeBasedFileRollOverRunnable(this.context, this);
            CommonUtils.logControlled(this.context, "Scheduling time based file roll over every " + frequencySecs + " seconds");
            try {
                this.scheduledRolloverFutureRef.set(this.executorService.scheduleAtFixedRate(rollOverRunnable, initialDelaySecs, frequencySecs, TimeUnit.SECONDS));
            } catch (RejectedExecutionException e) {
                CommonUtils.logControlledError(this.context, "Failed to schedule time based file roll over", e);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void sendAndCleanUpIfSuccess() {
        FilesSender filesSender = getFilesSender();
        if (filesSender == null) {
            CommonUtils.logControlled(this.context, "skipping files send because we don't yet know the target endpoint");
            return;
        }
        CommonUtils.logControlled(this.context, "Sending all files");
        int filesSent = 0;
        List<File> batch = this.filesManager.getBatchOfFilesToSend();
        while (batch.size() > 0) {
            try {
                CommonUtils.logControlled(this.context, String.format(Locale.US, "attempt to send batch of %d files", new Object[]{Integer.valueOf(batch.size())}));
                boolean cleanup = filesSender.send(batch);
                if (cleanup) {
                    filesSent += batch.size();
                    this.filesManager.deleteSentFiles(batch);
                }
                if (!cleanup) {
                    break;
                }
                batch = this.filesManager.getBatchOfFilesToSend();
            } catch (Exception e) {
                CommonUtils.logControlledError(this.context, "Failed to send batch of analytics files to server: " + e.getMessage(), e);
            }
        }
        if (filesSent == 0) {
            this.filesManager.deleteOldestInRollOverIfOverMax();
        }
    }
}
