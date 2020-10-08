package io.intercom.android.sdk.api;

import io.intercom.android.sdk.identity.AppConfig;
import java.util.concurrent.TimeUnit;

public class RateLimiter {
    private final AppConfig appConfig;
    private int limitedRequestCount;
    private long periodStartTimestamp;

    public RateLimiter(AppConfig appConfig2) {
        this.appConfig = appConfig2;
    }

    public boolean isLimited() {
        return isInsideCurrentTimePeriod() && hasExceededMaxCount();
    }

    public void increment() {
        if (!isInsideCurrentTimePeriod()) {
            this.periodStartTimestamp = System.currentTimeMillis();
            this.limitedRequestCount = 0;
        }
        this.limitedRequestCount++;
    }

    /* access modifiers changed from: package-private */
    public boolean hasExceededMaxCount() {
        return this.limitedRequestCount >= this.appConfig.getRateLimitCount();
    }

    /* access modifiers changed from: package-private */
    public boolean isInsideCurrentTimePeriod() {
        return System.currentTimeMillis() - this.periodStartTimestamp < TimeUnit.SECONDS.toMillis((long) this.appConfig.getRateLimitPeriod());
    }

    /* access modifiers changed from: package-private */
    public long getPeriodStartTimestamp() {
        return this.periodStartTimestamp;
    }

    /* access modifiers changed from: package-private */
    public void setPeriodStartTimestamp(long periodStartTimestamp2) {
        this.periodStartTimestamp = periodStartTimestamp2;
    }

    /* access modifiers changed from: package-private */
    public int getLimitedRequestCount() {
        return this.limitedRequestCount;
    }

    /* access modifiers changed from: package-private */
    public void setLimitedRequestCount(int limitedRequestCount2) {
        this.limitedRequestCount = limitedRequestCount2;
    }
}
