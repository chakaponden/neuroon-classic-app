package io.fabric.sdk.android.services.cache;

import android.content.Context;

public abstract class AbstractValueCache<T> implements ValueCache<T> {
    private final ValueCache<T> childCache;

    /* access modifiers changed from: protected */
    public abstract void cacheValue(Context context, T t);

    /* access modifiers changed from: protected */
    public abstract void doInvalidate(Context context);

    /* access modifiers changed from: protected */
    public abstract T getCached(Context context);

    public AbstractValueCache() {
        this((ValueCache) null);
    }

    public AbstractValueCache(ValueCache<T> childCache2) {
        this.childCache = childCache2;
    }

    public final synchronized T get(Context context, ValueLoader<T> loader) throws Exception {
        T value;
        value = getCached(context);
        if (value == null) {
            value = this.childCache != null ? this.childCache.get(context, loader) : loader.load(context);
            cache(context, value);
        }
        return value;
    }

    public final synchronized void invalidate(Context context) {
        doInvalidate(context);
    }

    private void cache(Context context, T value) {
        if (value == null) {
            throw new NullPointerException();
        }
        cacheValue(context, value);
    }
}
