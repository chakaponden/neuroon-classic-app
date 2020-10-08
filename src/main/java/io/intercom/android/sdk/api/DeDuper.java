package io.intercom.android.sdk.api;

import io.intercom.android.sdk.Bridge;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DeDuper {
    private long earliestUpdateAt = 0;
    private final Map<String, Object> userUpdateAttributes = new HashMap();

    public void setAttributes(Map<String, ?> newAttributes) {
        this.userUpdateAttributes.clear();
        this.userUpdateAttributes.putAll(newAttributes);
        this.earliestUpdateAt = hasExpiredCache() ? System.currentTimeMillis() : this.earliestUpdateAt;
    }

    public boolean shouldUpdateUser(Map<String, ?> newAttributes) {
        return !isDuplicateUpdate(newAttributes) || hasExpiredCache();
    }

    public void reset() {
        this.userUpdateAttributes.clear();
        this.earliestUpdateAt = 0;
    }

    /* access modifiers changed from: package-private */
    public boolean hasExpiredCache() {
        return System.currentTimeMillis() - this.earliestUpdateAt > TimeUnit.SECONDS.toMillis((long) Bridge.getIdentityStore().getAppConfig().getUserUpdateCacheMaxAge());
    }

    /* access modifiers changed from: package-private */
    public boolean isDuplicateUpdate(Map<String, ?> newAttributes) {
        Map<String, Object> allAttributes = new HashMap<>();
        allAttributes.putAll(this.userUpdateAttributes);
        allAttributes.putAll(newAttributes);
        return allAttributes.equals(this.userUpdateAttributes);
    }

    /* access modifiers changed from: package-private */
    public Map<String, Object> getMap() {
        return this.userUpdateAttributes;
    }

    /* access modifiers changed from: package-private */
    public void setEarliestUpdateAt(long time) {
        this.earliestUpdateAt = time;
    }

    /* access modifiers changed from: package-private */
    public long getEarliestUpdateAt() {
        return this.earliestUpdateAt;
    }
}
