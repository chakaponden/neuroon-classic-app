package com.raizlabs.android.dbflow.sql.language;

import com.raizlabs.android.dbflow.sql.QueryBuilder;

public interface SQLCondition {
    void appendConditionToQuery(QueryBuilder queryBuilder);

    String columnName();

    boolean hasSeparator();

    String operation();

    SQLCondition separator(String str);

    String separator();

    Object value();
}
