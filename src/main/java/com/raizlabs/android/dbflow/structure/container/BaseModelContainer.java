package com.raizlabs.android.dbflow.structure.container;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.converter.TypeConverter;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.InvalidDBConfiguration;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import java.util.Iterator;

public abstract class BaseModelContainer<TModel extends Model, DataClass> implements ModelContainer<TModel, DataClass>, Model {
    DataClass data;
    TModel model;
    ModelAdapter<TModel> modelAdapter;
    ModelContainerAdapter<TModel> modelContainerAdapter;

    public abstract BaseModelContainer getInstance(Object obj, Class<? extends Model> cls);

    public abstract Object getValue(String str);

    public abstract void put(String str, Object obj);

    public BaseModelContainer(Class<TModel> table) {
        this.modelAdapter = FlowManager.getModelAdapter(table);
        this.modelContainerAdapter = FlowManager.getContainerAdapter(table);
        if (this.modelContainerAdapter == null) {
            throw new InvalidDBConfiguration("The table " + FlowManager.getTableName(table) + " did not specify the " + ModelContainer.class.getSimpleName() + " annotation." + " Please decorate " + table.getName() + " with annotation @" + ModelContainer.class.getSimpleName() + ".");
        }
    }

    public BaseModelContainer(Class<TModel> table, DataClass data2) {
        this(table);
        this.data = data2;
    }

    public BaseModelContainer(@NonNull ModelContainer<TModel, ?> existingContainer) {
        this(existingContainer.getTable());
        Iterator<String> keys = existingContainer.iterator();
        if (keys != null) {
            while (keys.hasNext()) {
                String key = keys.next();
                put(key, existingContainer.getValue(key));
            }
        }
    }

    public <T> T getTypeConvertedPropertyValue(Class<T> type, String key) {
        Object value = getValue(key);
        if (value != null && type.isAssignableFrom(value.getClass())) {
            return value;
        }
        TypeConverter<Object, T> converter = FlowManager.getTypeConverterForClass(type);
        if (converter != null) {
            return converter.getModelValue(value);
        }
        return null;
    }

    @Nullable
    public TModel toModel() {
        if (this.model == null && this.data != null) {
            this.model = this.modelContainerAdapter.toModel(this);
        }
        return this.model;
    }

    @Nullable
    public TModel toModelForce() {
        this.model = null;
        return toModel();
    }

    @Nullable
    public TModel getModel() {
        return this.model;
    }

    public void setModel(TModel model2) {
        this.model = model2;
    }

    public void invalidateModel() {
        setModel((Model) null);
    }

    /* access modifiers changed from: protected */
    public Object getModelValue(Object inValue, String columnName) {
        Class<?> classForColumn = FlowManager.getContainerAdapter(getTable()).getClassForColumn(columnName);
        ModelContainerAdapter<? extends Model> columnAdapter = FlowManager.getContainerAdapter(classForColumn);
        if (columnAdapter != null) {
            return columnAdapter.toModel(getInstance(inValue, classForColumn));
        }
        throw new RuntimeException("Column: " + columnName + "'s class needs to add the @ContainerAdapter annotation");
    }

    @Nullable
    public DataClass getData() {
        return this.data;
    }

    public void setData(DataClass data2) {
        this.data = data2;
        this.model = null;
    }

    public Object getValue(IProperty property) {
        return getValue(property.getContainerKey());
    }

    public void put(IProperty property, Object value) {
        put(property.getContainerKey(), value);
    }

    public void putDefault(String columnName) {
        Class columnClass = this.modelContainerAdapter.getColumnMap().get(columnName);
        if (!columnClass.isPrimitive()) {
            put(columnName, (Object) null);
        } else if (columnClass.equals(Boolean.TYPE)) {
            put(columnName, (Object) false);
        } else if (columnClass.equals(Character.TYPE)) {
            put(columnName, (Object) 0);
        } else {
            put(columnName, (Object) 0);
        }
    }

    public void putDefault(IProperty property) {
        putDefault(property.getContainerKey());
    }

    public ModelAdapter<TModel> getModelAdapter() {
        return this.modelAdapter;
    }

    public Class<TModel> getTable() {
        return this.modelAdapter.getModelClass();
    }

    public void save() {
        this.modelContainerAdapter.save(this);
    }

    public void delete() {
        this.modelContainerAdapter.delete(this);
    }

    public void update() {
        this.modelContainerAdapter.update(this);
    }

    public void insert() {
        this.modelContainerAdapter.insert(this);
    }

    public boolean exists() {
        return this.modelContainerAdapter.exists(this);
    }
}
