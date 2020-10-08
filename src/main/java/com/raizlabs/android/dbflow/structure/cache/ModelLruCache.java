package com.raizlabs.android.dbflow.structure.cache;

import com.raizlabs.android.dbflow.structure.Model;

public class ModelLruCache<TModel extends Model> extends ModelCache<TModel, LruCache<Long, TModel>> {
    public static <TModel extends Model> ModelLruCache<TModel> newInstance(int size) {
        if (size <= 0) {
            size = 25;
        }
        return new ModelLruCache<>(size);
    }

    protected ModelLruCache(int size) {
        super(new LruCache(size));
    }

    public void addModel(Object id, TModel model) {
        if (id instanceof Number) {
            synchronized (((LruCache) getCache())) {
                ((LruCache) getCache()).put(Long.valueOf(((Number) id).longValue()), model);
            }
            return;
        }
        throw new IllegalArgumentException("A ModelLruCache must use an id that can cast toa Number to convert it into a long");
    }

    public TModel removeModel(Object id) {
        TModel model;
        if (id instanceof Number) {
            synchronized (((LruCache) getCache())) {
                model = (Model) ((LruCache) getCache()).remove(Long.valueOf(((Number) id).longValue()));
            }
            return model;
        }
        throw new IllegalArgumentException("A ModelLruCache uses an id that can cast toa Number to convert it into a long");
    }

    public void clear() {
        synchronized (((LruCache) getCache())) {
            ((LruCache) getCache()).evictAll();
        }
    }

    public void setCacheSize(int size) {
        ((LruCache) getCache()).resize(size);
    }

    public TModel get(Object id) {
        if (id instanceof Number) {
            return (Model) ((LruCache) getCache()).get(Long.valueOf(((Number) id).longValue()));
        }
        throw new IllegalArgumentException("A ModelLruCache must use an id that can cast toa Number to convert it into a long");
    }
}
