package io.fabric.sdk.android.services.concurrency.internal;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

class RetryFuture<T> extends AbstractFuture<T> implements Runnable {
    private final RetryThreadPoolExecutor executor;
    RetryState retryState;
    private final AtomicReference<Thread> runner = new AtomicReference<>();
    private final Callable<T> task;

    RetryFuture(Callable<T> task2, RetryState retryState2, RetryThreadPoolExecutor executor2) {
        this.task = task2;
        this.retryState = retryState2;
        this.executor = executor2;
    }

    public void run() {
        if (!isDone() && this.runner.compareAndSet((Object) null, Thread.currentThread())) {
            try {
                set(this.task.call());
            } catch (Throwable exception) {
                if (getRetryPolicy().shouldRetry(getRetryCount(), exception)) {
                    long delay = getBackoff().getDelayMillis(getRetryCount());
                    this.retryState = this.retryState.nextRetryState();
                    this.executor.schedule(this, delay, TimeUnit.MILLISECONDS);
                } else {
                    setException(exception);
                }
            } finally {
                this.runner.getAndSet((Object) null);
            }
        }
    }

    private RetryPolicy getRetryPolicy() {
        return this.retryState.getRetryPolicy();
    }

    private Backoff getBackoff() {
        return this.retryState.getBackoff();
    }

    private int getRetryCount() {
        return this.retryState.getRetryCount();
    }

    /* access modifiers changed from: protected */
    public void interruptTask() {
        Thread thread = this.runner.getAndSet((Object) null);
        if (thread != null) {
            thread.interrupt();
        }
    }
}
