package com.raizlabs.android.dbflow.sql;

import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Condition;
import java.util.List;
import java.util.regex.Pattern;

public class QueryBuilder<QueryClass extends QueryBuilder> implements Query {
    private static final char QUOTE = '`';
    private static final Pattern QUOTE_PATTERN = Pattern.compile("`.*`");
    protected StringBuilder query = new StringBuilder();

    public QueryBuilder() {
    }

    public QueryBuilder(Object object) {
        append(object);
    }

    public QueryClass appendSpace() {
        return append(" ");
    }

    public QueryClass appendSpaceSeparated(Object object) {
        return appendSpace().append(object).appendSpace();
    }

    public QueryClass appendParenthesisEnclosed(Object string) {
        return append("(").append(string).append(")");
    }

    public QueryClass append(Object object) {
        this.query.append(object);
        return castThis();
    }

    public QueryClass appendOptional(Object object) {
        if (object != null) {
            append(object);
        }
        return castThis();
    }

    /* access modifiers changed from: protected */
    public QueryClass castThis() {
        return this;
    }

    public QueryClass appendType(String type) {
        return appendSQLiteType(SQLiteType.get(type));
    }

    public QueryClass appendSQLiteType(SQLiteType sqLiteType) {
        return append(sqLiteType.name());
    }

    public QueryClass appendArray(Object... objects) {
        return append(join((CharSequence) ", ", objects));
    }

    public QueryClass appendList(List<?> objects) {
        return append(join((CharSequence) ", ", (Iterable) objects));
    }

    public QueryClass appendQualifier(String name, String value) {
        if (value != null && value.length() > 0) {
            if (name != null) {
                append(name);
            }
            appendSpaceSeparated(value);
        }
        return castThis();
    }

    public QueryClass appendNotEmpty(String string) {
        if (string != null && !string.isEmpty()) {
            append(string);
        }
        return castThis();
    }

    public QueryClass appendQuoted(String string) {
        if (string.equals(Condition.Operation.MULTIPLY)) {
            return append(string);
        }
        append(quote(string));
        return castThis();
    }

    public QueryClass appendQuotedIfNeeded(String string) {
        if (string.equals(Condition.Operation.MULTIPLY)) {
            return append(string);
        }
        append(quoteIfNeeded(string));
        return castThis();
    }

    public QueryClass appendQuotedList(List<?> objects) {
        return appendQuoted(join((CharSequence) "`, `", (Iterable) objects));
    }

    public QueryClass appendQuotedArray(Object... objects) {
        return appendQuoted(join((CharSequence) "`, `", objects));
    }

    public String toString() {
        return getQuery();
    }

    public String getQuery() {
        return this.query.toString();
    }

    public static String quote(String columnName) {
        return QUOTE + columnName.replace(".", "`.`") + QUOTE;
    }

    public static String quoteIfNeeded(String name) {
        if (name == null || isQuoted(name)) {
            return name;
        }
        return quote(name);
    }

    public static boolean isQuoted(String name) {
        return QUOTE_PATTERN.matcher(name).find();
    }

    public static String stripQuotes(String name) {
        String ret = name;
        if (ret == null || !isQuoted(ret)) {
            return ret;
        }
        return ret.replace("`", "");
    }

    public static String join(CharSequence delimiter, Object[] tokens) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object token : tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(token);
        }
        return sb.toString();
    }

    public static String join(CharSequence delimiter, Iterable tokens) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object token : tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(token);
        }
        return sb.toString();
    }
}
