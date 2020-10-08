package com.raizlabs.android.dbflow.converter;

import java.sql.Date;

public class SqlDateConverter extends TypeConverter<Long, Date> {
    public Long getDBValue(Date model) {
        if (model == null) {
            return null;
        }
        return Long.valueOf(model.getTime());
    }

    public Date getModelValue(Long data) {
        if (data == null) {
            return null;
        }
        return new Date(data.longValue());
    }
}
