package io.fabric.sdk.android.services.concurrency.internal;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

public class RetryThreadPoolExecutor extends ScheduledThreadPoolExecutor {
    private final Backoff backoff;
    private final RetryPolicy retryPolicy;

    public RetryThreadPoolExecutor(int corePoolSize, RetryPolicy retryPolicy2, Backoff backoff2) {
        this(corePoolSize, Executors.defaultThreadFactory(), retryPolicy2, backoff2);
    }

    public RetryThreadPoolExecutor(int corePoolSize, ThreadFactory factory, RetryPolicy retryPolicy2, Backoff backoff2) {
        super(corePoolSize, factory);
        if (retryPolicy2 == null) {
            throw new NullPointerException("retry policy must not be null");
        } else if (backoff2 == null) {
            throw new NullPointerException("backoff must not be null");
        } else {
            this.retryPolicy = retryPolicy2;
            this.backoff = backoff2;
        }
    }

    public Future<?> scheduleWithRetry(Runnable task) {
        return scheduleWithRetryInternal(Executors.callable(task));
    }

    public <T> Future<T> scheduleWithRetry(Runnable task, T result) {
        return scheduleWithRetryInternal(Executors.callable(task, result));
    }

    public <T> Future<T> scheduleWithRetry(Callable<T> task) {
        return scheduleWithRetryInternal(task);
    }

    private <T> Future<T> scheduleWithRetryInternal(Callable<T> task) {
        if (task == null) {
            throw new NullPointerException();
        }
        RetryFuture<T> retryFuture = new RetryFuture<>(task, new RetryState(this.backoff, this.retryPolicy), this);
        execute(retryFuture);
        return retryFuture;
    }

    public RetryPolicy getRetryPolicy() {
        return this.retryPolicy;
    }

    public Backoff getBackoff() {
        return this.backoff;
    }
}
