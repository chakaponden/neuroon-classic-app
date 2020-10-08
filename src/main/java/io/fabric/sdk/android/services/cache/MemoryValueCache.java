package io.fabric.sdk.android.services.cache;

import android.content.Context;

public class MemoryValueCache<T> extends AbstractValueCache<T> {
    private T value;

    public MemoryValueCache() {
        this((ValueCache) null);
    }

    public MemoryValueCache(ValueCache<T> childCache) {
        super(childCache);
    }

    /* access modifiers changed from: protected */
    public void doInvalidate(Context context) {
        this.value = null;
    }

    /* access modifiers changed from: protected */
    public T getCached(Context context) {
        return this.value;
    }

    /* access modifiers changed from: protected */
    public void cacheValue(Context context, T value2) {
        this.value = value2;
    }
}
