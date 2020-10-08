package com.raizlabs.android.dbflow.sql.language;

import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.sql.language.property.Property;
import com.raizlabs.android.dbflow.structure.Model;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Select implements Query {
    public static final int ALL = 1;
    public static final int DISTINCT = 0;
    public static final int NONE = -1;
    private int mSelectQualifier = -1;
    private final List<IProperty> propertyList = new ArrayList();

    public Select(IProperty... properties) {
        Collections.addAll(this.propertyList, properties);
        if (this.propertyList.isEmpty()) {
            this.propertyList.add(Property.ALL_PROPERTY);
        }
    }

    public <TModel extends Model> From<TModel> from(Class<TModel> table) {
        return new From<>(this, table);
    }

    public Select distinct() {
        return selectQualifier(0);
    }

    public String toString() {
        return getQuery();
    }

    public String getQuery() {
        QueryBuilder queryBuilder = new QueryBuilder("SELECT ");
        if (this.mSelectQualifier != -1) {
            if (this.mSelectQualifier == 0) {
                queryBuilder.append("DISTINCT");
            } else if (this.mSelectQualifier == 1) {
                queryBuilder.append("ALL");
            }
            queryBuilder.appendSpace();
        }
        queryBuilder.append(QueryBuilder.join((CharSequence) ",", (Iterable) this.propertyList));
        queryBuilder.appendSpace();
        return queryBuilder.getQuery();
    }

    private Select selectQualifier(int qualifierInt) {
        this.mSelectQualifier = qualifierInt;
        return this;
    }
}
