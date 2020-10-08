package com.raizlabs.android.dbflow.structure.container;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.raizlabs.android.dbflow.structure.Model;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MapModelContainer<TModel extends Model> extends SimpleModelContainer<TModel, Map<String, Object>> implements Model {
    public MapModelContainer(Class<TModel> table) {
        this(new HashMap(), table);
    }

    public MapModelContainer(@NonNull ModelContainer<TModel, ?> existingContainer) {
        super(existingContainer);
    }

    public BaseModelContainer getInstance(Object inValue, Class<? extends Model> columnClass) {
        if (inValue instanceof ModelContainer) {
            return new MapModelContainer((ModelContainer) inValue);
        }
        return new MapModelContainer((Map) inValue, columnClass);
    }

    public boolean containsValue(String key) {
        return (getData() == null || !((Map) getData()).containsKey(key) || ((Map) getData()).get(key) == null) ? false : true;
    }

    @NonNull
    public Map<String, Object> newDataInstance() {
        return new HashMap();
    }

    public MapModelContainer(Map<String, Object> map, Class<TModel> table) {
        super(table, map);
    }

    public Object getValue(String key) {
        if (getData() != null) {
            return ((Map) getData()).get(key);
        }
        return null;
    }

    public void put(String columnName, Object value) {
        if (getData() == null) {
            setData(newDataInstance());
        }
        ((Map) getData()).put(columnName, value);
    }

    @Nullable
    public Iterator<String> iterator() {
        if (this.data != null) {
            return ((Map) this.data).keySet().iterator();
        }
        return null;
    }
}
