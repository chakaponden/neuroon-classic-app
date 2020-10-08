package com.raizlabs.android.dbflow.sql.language;

import com.raizlabs.android.dbflow.StringUtils;
import com.raizlabs.android.dbflow.sql.QueryBuilder;

public class UnSafeStringCondition implements SQLCondition {
    private final String conditionString;
    private String separator = "";

    public UnSafeStringCondition(String selection, String[] selectionArgs) {
        String newSelection = selection;
        if (newSelection != null) {
            for (String selectionArg : selectionArgs) {
                newSelection = newSelection.replaceFirst("\\?", selectionArg);
            }
        }
        this.conditionString = newSelection;
    }

    public void appendConditionToQuery(QueryBuilder queryBuilder) {
        queryBuilder.append(this.conditionString);
    }

    public String columnName() {
        return "";
    }

    public String separator() {
        return this.separator;
    }

    public SQLCondition separator(String separator2) {
        this.separator = separator2;
        return this;
    }

    public boolean hasSeparator() {
        return StringUtils.isNotNullOrEmpty(this.separator);
    }

    public String operation() {
        return "";
    }

    public Object value() {
        return "";
    }
}
