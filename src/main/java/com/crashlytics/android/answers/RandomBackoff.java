package com.crashlytics.android.answers;

import io.fabric.sdk.android.services.concurrency.internal.Backoff;
import java.util.Random;

class RandomBackoff implements Backoff {
    final Backoff backoff;
    final double jitterPercent;
    final Random random;

    public RandomBackoff(Backoff backoff2, double jitterPercent2) {
        this(backoff2, jitterPercent2, new Random());
    }

    public RandomBackoff(Backoff backoff2, double jitterPercent2, Random random2) {
        if (jitterPercent2 < 0.0d || jitterPercent2 > 1.0d) {
            throw new IllegalArgumentException("jitterPercent must be between 0.0 and 1.0");
        } else if (backoff2 == null) {
            throw new NullPointerException("backoff must not be null");
        } else if (random2 == null) {
            throw new NullPointerException("random must not be null");
        } else {
            this.backoff = backoff2;
            this.jitterPercent = jitterPercent2;
            this.random = random2;
        }
    }

    public long getDelayMillis(int retries) {
        return (long) (randomJitter() * ((double) this.backoff.getDelayMillis(retries)));
    }

    /* access modifiers changed from: package-private */
    public double randomJitter() {
        double minJitter = 1.0d - this.jitterPercent;
        double maxJitter = 1.0d + this.jitterPercent;
        return ((maxJitter - minJitter) * this.random.nextDouble()) + minJitter;
    }
}
