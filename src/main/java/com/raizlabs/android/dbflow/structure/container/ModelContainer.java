package com.raizlabs.android.dbflow.structure.container;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import java.util.Iterator;

public interface ModelContainer<TModel extends Model, DataClass> extends Model {
    boolean containsValue(String str);

    byte[] getBlbValue(String str);

    Byte[] getBlobValue(String str);

    boolean getBoolValue(String str);

    Boolean getBooleanValue(String str);

    byte getBytValue(String str);

    Byte getByteValue(String str);

    @Nullable
    DataClass getData();

    double getDbleValue(String str);

    Double getDoubleValue(String str);

    Float getFloatValue(String str);

    float getFltValue(String str);

    BaseModelContainer getInstance(Object obj, Class<? extends Model> cls);

    int getIntValue(String str);

    Integer getIntegerValue(String str);

    long getLngValue(String str);

    Long getLongValue(String str);

    @Nullable
    TModel getModel();

    ModelAdapter<TModel> getModelAdapter();

    Short getShortValue(String str);

    short getShrtValue(String str);

    String getStringValue(String str);

    Class<TModel> getTable();

    <T> T getTypeConvertedPropertyValue(Class<T> cls, String str);

    Object getValue(IProperty iProperty);

    Object getValue(String str);

    @Nullable
    Iterator<String> iterator();

    @NonNull
    DataClass newDataInstance();

    void put(IProperty iProperty, Object obj);

    void put(String str, Object obj);

    void putDefault(IProperty iProperty);

    void putDefault(String str);

    void setData(DataClass dataclass);

    @Nullable
    TModel toModel();

    @Nullable
    TModel toModelForce();
}
