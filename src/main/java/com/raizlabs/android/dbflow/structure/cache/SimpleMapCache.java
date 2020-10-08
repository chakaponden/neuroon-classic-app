package com.raizlabs.android.dbflow.structure.cache;

import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.structure.Model;
import java.util.HashMap;
import java.util.Map;

public class SimpleMapCache<TModel extends Model> extends ModelCache<TModel, Map<Object, TModel>> {
    public SimpleMapCache(int capacity) {
        super(new HashMap(capacity));
    }

    public SimpleMapCache(Map<Object, TModel> cache) {
        super(cache);
    }

    public void addModel(Object id, TModel model) {
        ((Map) getCache()).put(id, model);
    }

    public TModel removeModel(Object id) {
        return (Model) ((Map) getCache()).remove(id);
    }

    public void clear() {
        ((Map) getCache()).clear();
    }

    public TModel get(Object id) {
        return (Model) ((Map) getCache()).get(id);
    }

    public void setCacheSize(int size) {
        FlowLog.log(FlowLog.Level.I, "The cache size for " + SimpleMapCache.class.getSimpleName() + " is not re-configurable.");
    }
}
