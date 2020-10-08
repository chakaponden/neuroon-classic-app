package com.raizlabs.android.dbflow.sql.builder;

import android.database.DatabaseUtils;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.converter.TypeConverter;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.structure.Model;

public class ValueQueryBuilder extends QueryBuilder<ValueQueryBuilder> {
    public static String joinArguments(CharSequence delimiter, Object[] tokens) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object token : tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(convertValueToDatabaseString(token));
        }
        return sb.toString();
    }

    public static String joinArguments(CharSequence delimiter, Iterable tokens) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object token : tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(convertValueToDatabaseString(token));
        }
        return sb.toString();
    }

    public static String convertValueToDatabaseString(Object modelValue) {
        TypeConverter typeConverter;
        Object value = modelValue;
        if (!(value == null || (typeConverter = FlowManager.getTypeConverterForClass(value.getClass())) == null)) {
            value = typeConverter.getDBValue(value);
        }
        if (value instanceof Number) {
            return String.valueOf(value);
        }
        String stringVal = String.valueOf(value);
        if (!stringVal.equals(Condition.Operation.EMPTY_PARAM)) {
            return DatabaseUtils.sqlEscapeString(stringVal);
        }
        return stringVal;
    }

    public ValueQueryBuilder() {
    }

    public ValueQueryBuilder(String string) {
        super(string);
    }

    public ValueQueryBuilder appendTableName(Class<? extends Model> table) {
        return (ValueQueryBuilder) append(FlowManager.getTableName(table));
    }

    public ValueQueryBuilder appendDBValue(Object modelValue) {
        return (ValueQueryBuilder) append(convertValueToDatabaseString(modelValue));
    }

    public ValueQueryBuilder appendModelList(Iterable<?> modelList) {
        if (modelList != null) {
            append(joinArguments((CharSequence) ",", (Iterable) modelList));
        }
        return this;
    }

    public ValueQueryBuilder appendModelArray(Object[] modelList) {
        if (modelList != null) {
            append(joinArguments((CharSequence) ",", modelList));
        }
        return this;
    }
}
