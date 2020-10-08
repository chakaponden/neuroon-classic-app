package com.raizlabs.android.dbflow.structure.cache;

import android.util.SparseArray;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.structure.Model;

public class SparseArrayBasedCache<TModel extends Model> extends ModelCache<TModel, SparseArray<TModel>> {
    public SparseArrayBasedCache() {
        super(new SparseArray());
    }

    public SparseArrayBasedCache(int initialCapacity) {
        super(new SparseArray(initialCapacity));
    }

    public SparseArrayBasedCache(SparseArray<TModel> sparseArray) {
        super(sparseArray);
    }

    public void addModel(Object id, TModel model) {
        if (id instanceof Number) {
            synchronized (((SparseArray) getCache())) {
                ((SparseArray) getCache()).put(((Number) id).intValue(), model);
            }
            return;
        }
        throw new IllegalArgumentException("A SparseArrayBasedCache must use an id that can cast to a Number to convert it into a int");
    }

    public TModel removeModel(Object id) {
        TModel model = get(id);
        synchronized (((SparseArray) getCache())) {
            ((SparseArray) getCache()).remove(((Number) id).intValue());
        }
        return model;
    }

    public void clear() {
        synchronized (((SparseArray) getCache())) {
            ((SparseArray) getCache()).clear();
        }
    }

    public void setCacheSize(int size) {
        FlowLog.log(FlowLog.Level.I, "The cache size for " + SparseArrayBasedCache.class.getSimpleName() + " is not re-configurable.");
    }

    public TModel get(Object id) {
        if (id instanceof Number) {
            return (Model) ((SparseArray) getCache()).get(((Number) id).intValue());
        }
        throw new IllegalArgumentException("A SparseArrayBasedCache uses an id that can cast to a Number to convert it into a int");
    }
}
