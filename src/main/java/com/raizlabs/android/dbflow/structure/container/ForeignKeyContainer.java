package com.raizlabs.android.dbflow.structure.container;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.Model;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ForeignKeyContainer<TModel extends Model> extends SimpleModelContainer<TModel, Map<String, Object>> {
    private TModel relationshipModel;

    public ForeignKeyContainer(Class<TModel> table) {
        this(table, new LinkedHashMap());
    }

    public ForeignKeyContainer(Class<TModel> table, Map<String, Object> data) {
        super(table, data);
    }

    public ForeignKeyContainer(@NonNull ModelContainer<TModel, ?> existingContainer) {
        super(existingContainer);
    }

    @NonNull
    public Map<String, Object> newDataInstance() {
        return new LinkedHashMap();
    }

    public BaseModelContainer getInstance(Object inValue, Class<? extends Model> columnClass) {
        if (inValue instanceof ModelContainer) {
            return new ForeignKeyContainer((ModelContainer) inValue);
        }
        return new ForeignKeyContainer(columnClass, (Map) inValue);
    }

    public boolean containsValue(String key) {
        Map<String, Object> data = (Map) getData();
        return (data == null || !data.containsKey(key) || data.get(key) == null) ? false : true;
    }

    public Object getValue(String key) {
        Map<String, Object> data = (Map) getData();
        if (data != null) {
            return data.get(key);
        }
        return null;
    }

    public void put(String columnName, Object value) {
        Map<String, Object> data = (Map) getData();
        if (data == null) {
            data = new LinkedHashMap<>();
            setData(data);
        }
        data.put(columnName, value);
    }

    @Nullable
    public Iterator<String> iterator() {
        if (this.data != null) {
            return ((Map) this.data).keySet().iterator();
        }
        return null;
    }

    @Nullable
    public TModel load() {
        if (this.relationshipModel == null && getData() != null) {
            this.relationshipModel = new Select(new IProperty[0]).from(this.modelAdapter.getModelClass()).where(this.modelContainerAdapter.getPrimaryConditionClause(this)).querySingle();
        }
        return this.relationshipModel;
    }

    @Nullable
    public TModel reload() {
        this.relationshipModel = null;
        return load();
    }

    public void save() {
        throw new InvalidMethodCallException("Cannot call save() on a foreign key container. Call load() and then save() instead");
    }

    public void delete() {
        throw new InvalidMethodCallException("Cannot call delete() on a foreign key container. Call load() and then delete() instead");
    }

    public void update() {
        throw new InvalidMethodCallException("Cannot call update() on a foreign key container. Call load() and then update() instead");
    }

    public void insert() {
        throw new InvalidMethodCallException("Cannot call insert() on a foreign key container. Call load() and then insert() instead");
    }

    private static class InvalidMethodCallException extends RuntimeException {
        public InvalidMethodCallException(String detailMessage) {
            super(detailMessage);
        }
    }
}
