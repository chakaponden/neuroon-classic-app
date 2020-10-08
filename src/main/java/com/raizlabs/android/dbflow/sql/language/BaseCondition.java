package com.raizlabs.android.dbflow.sql.language;

import android.database.DatabaseUtils;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.converter.TypeConverter;
import com.raizlabs.android.dbflow.data.Blob;
import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.SqlUtils;
import com.raizlabs.android.dbflow.sql.language.Condition;

public abstract class BaseCondition implements SQLCondition {
    protected boolean isValueSet;
    protected NameAlias nameAlias;
    protected String operation = "";
    protected String postArg;
    protected String separator;
    protected Object value;

    public static String convertValueToString(Object value2, boolean appendInnerQueryParenthesis) {
        byte[] bytes;
        if (value2 == null) {
            return "NULL";
        }
        TypeConverter typeConverter = FlowManager.getTypeConverterForClass(value2.getClass());
        if (typeConverter != null) {
            value2 = typeConverter.getDBValue(value2);
        }
        if (value2 instanceof Number) {
            return String.valueOf(value2);
        }
        if (appendInnerQueryParenthesis && (value2 instanceof BaseModelQueriable)) {
            return String.format("(%1s)", new Object[]{((BaseModelQueriable) value2).getQuery().trim()});
        } else if (value2 instanceof NameAlias) {
            return ((NameAlias) value2).getQuery();
        } else {
            if (value2 instanceof SQLCondition) {
                QueryBuilder queryBuilder = new QueryBuilder();
                ((SQLCondition) value2).appendConditionToQuery(queryBuilder);
                return queryBuilder.toString();
            } else if (value2 instanceof Query) {
                return ((Query) value2).getQuery();
            } else {
                if ((value2 instanceof Blob) || (value2 instanceof byte[])) {
                    if (value2 instanceof Blob) {
                        bytes = ((Blob) value2).getBlob();
                    } else {
                        bytes = (byte[]) value2;
                    }
                    return "X" + DatabaseUtils.sqlEscapeString(SqlUtils.byteArrayToHexString(bytes));
                }
                String stringVal = String.valueOf(value2);
                if (!stringVal.equals(Condition.Operation.EMPTY_PARAM)) {
                    return DatabaseUtils.sqlEscapeString(stringVal);
                }
                return stringVal;
            }
        }
    }

    public static String joinArguments(CharSequence delimiter, Object[] tokens) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object token : tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(convertValueToString(token, false));
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
            sb.append(convertValueToString(token, false));
        }
        return sb.toString();
    }

    BaseCondition(NameAlias nameAlias2) {
        this.nameAlias = nameAlias2;
    }

    public Object value() {
        return this.value;
    }

    public String columnName() {
        return this.nameAlias.getQuery();
    }

    public SQLCondition separator(String separator2) {
        this.separator = separator2;
        return this;
    }

    public String separator() {
        return this.separator;
    }

    public boolean hasSeparator() {
        return this.separator != null && this.separator.length() > 0;
    }

    public String operation() {
        return this.operation;
    }

    public String postArgument() {
        return this.postArg;
    }

    /* access modifiers changed from: package-private */
    public NameAlias columnAlias() {
        return this.nameAlias;
    }
}
