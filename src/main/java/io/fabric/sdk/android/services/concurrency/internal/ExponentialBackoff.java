package io.fabric.sdk.android.services.concurrency.internal;

public class ExponentialBackoff implements Backoff {
    private static final int DEFAULT_POWER = 2;
    private final long baseTimeMillis;
    private final int power;

    public ExponentialBackoff(long baseTimeMillis2) {
        this(baseTimeMillis2, 2);
    }

    public ExponentialBackoff(long baseTimeMillis2, int power2) {
        this.baseTimeMillis = baseTimeMillis2;
        this.power = power2;
    }

    public long getDelayMillis(int retries) {
        return (long) (((double) this.baseTimeMillis) * Math.pow((double) this.power, (double) retries));
    }
}
