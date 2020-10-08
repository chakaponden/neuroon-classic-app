package com.raizlabs.android.dbflow.sql.language;

import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.sql.language.property.IndexProperty;
import com.raizlabs.android.dbflow.structure.Model;

public class IndexedBy<TModel extends Model> implements WhereBase<TModel>, Transformable<TModel> {
    private final IndexProperty<TModel> indexProperty;
    private final WhereBase<TModel> whereBase;

    IndexedBy(IndexProperty<TModel> indexProperty2, WhereBase<TModel> whereBase2) {
        this.indexProperty = indexProperty2;
        this.whereBase = whereBase2;
    }

    public Where<TModel> where(SQLCondition... conditions) {
        return new Where<>(this, conditions);
    }

    public Where<TModel> groupBy(NameAlias... nameAliases) {
        return where(new SQLCondition[0]).groupBy(nameAliases);
    }

    public Where<TModel> groupBy(IProperty... properties) {
        return where(new SQLCondition[0]).groupBy(properties);
    }

    public Where<TModel> orderBy(NameAlias nameAlias, boolean ascending) {
        return where(new SQLCondition[0]).orderBy(nameAlias, ascending);
    }

    public Where<TModel> orderBy(IProperty property, boolean ascending) {
        return where(new SQLCondition[0]).orderBy(property, ascending);
    }

    public Where<TModel> orderBy(OrderBy orderBy) {
        return where(new SQLCondition[0]).orderBy(orderBy);
    }

    public Where<TModel> limit(int count) {
        return where(new SQLCondition[0]).limit(count);
    }

    public Where<TModel> offset(int offset) {
        return where(new SQLCondition[0]).offset(offset);
    }

    public Where<TModel> having(SQLCondition... conditions) {
        return where(new SQLCondition[0]).having(conditions);
    }

    public Class<TModel> getTable() {
        return this.whereBase.getTable();
    }

    public Query getQueryBuilderBase() {
        return this.whereBase.getQueryBuilderBase();
    }

    public String getQuery() {
        return new QueryBuilder(this.whereBase.getQuery()).append("INDEXED BY ").append(QueryBuilder.quoteIfNeeded(this.indexProperty.getIndexName())).appendSpace().getQuery();
    }
}
