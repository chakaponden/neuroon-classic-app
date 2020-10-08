package com.raizlabs.android.dbflow.converter;

import java.util.Calendar;

public class CalendarConverter extends TypeConverter<Long, Calendar> {
    public Long getDBValue(Calendar model) {
        if (model == null) {
            return null;
        }
        return Long.valueOf(model.getTimeInMillis());
    }

    public Calendar getModelValue(Long data) {
        if (data == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(data.longValue());
        return calendar;
    }
}
