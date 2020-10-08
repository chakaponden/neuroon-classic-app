package com.raizlabs.android.dbflow.converter;

public abstract class TypeConverter<DataClass, ModelClass> {
    public abstract DataClass getDBValue(ModelClass modelclass);

    public abstract ModelClass getModelValue(DataClass dataclass);
}
