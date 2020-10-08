package com.raizlabs.android.dbflow.converter;

public class BooleanConverter extends TypeConverter<Integer, Boolean> {
    public Integer getDBValue(Boolean model) {
        if (model == null) {
            return null;
        }
        return Integer.valueOf(model.booleanValue() ? 1 : 0);
    }

    public Boolean getModelValue(Integer data) {
        boolean z = true;
        if (data == null) {
            return null;
        }
        if (data.intValue() != 1) {
            z = false;
        }
        return Boolean.valueOf(z);
    }
}
