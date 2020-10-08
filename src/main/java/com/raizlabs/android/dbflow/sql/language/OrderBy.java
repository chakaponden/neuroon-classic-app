package com.raizlabs.android.dbflow.sql.language;

import com.raizlabs.android.dbflow.annotation.Collate;
import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;

public class OrderBy implements Query {
    public static final String ASCENDING = "ASC";
    public static final String DESCENDING = "DESC";
    private Collate collation;
    private NameAlias column;
    private boolean isAscending;
    private String orderByString;

    public static OrderBy fromProperty(IProperty property) {
        return new OrderBy(property.getNameAlias());
    }

    public static OrderBy fromNameAlias(NameAlias nameAlias) {
        return new OrderBy(nameAlias);
    }

    public static OrderBy fromString(String orderByString2) {
        return new OrderBy(orderByString2);
    }

    OrderBy(NameAlias column2) {
        this.column = column2;
    }

    OrderBy(NameAlias column2, boolean isAscending2) {
        this(column2);
        this.isAscending = isAscending2;
    }

    OrderBy(String orderByString2) {
        this.orderByString = orderByString2;
    }

    public OrderBy ascending() {
        this.isAscending = true;
        return this;
    }

    public OrderBy descending() {
        this.isAscending = false;
        return this;
    }

    public OrderBy collate(Collate collate) {
        this.collation = collate;
        return this;
    }

    public String getQuery() {
        if (this.orderByString != null) {
            return this.orderByString;
        }
        StringBuilder query = new StringBuilder().append(this.column).append(" ");
        if (this.collation != null) {
            query.append("COLLATE").append(" ").append(this.collation).append(" ");
        }
        query.append(this.isAscending ? ASCENDING : DESCENDING);
        return query.toString();
    }

    public String toString() {
        return getQuery();
    }
}
