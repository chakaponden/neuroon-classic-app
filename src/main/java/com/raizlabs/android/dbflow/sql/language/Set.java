package com.raizlabs.android.dbflow.sql.language;

import android.content.ContentValues;
import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.SqlUtils;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.sql.queriable.Queriable;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

public class Set<TModel extends Model> extends BaseQueriable<TModel> implements WhereBase<TModel>, Queriable, Transformable<TModel> {
    private ConditionGroup conditionGroup = new ConditionGroup();
    private Query update;

    Set(Query update2, Class<TModel> table) {
        super(table);
        this.update = update2;
        this.conditionGroup.setAllCommaSeparated(true);
    }

    public Set<TModel> conditions(SQLCondition... conditions) {
        this.conditionGroup.andAll(conditions);
        return this;
    }

    public Set<TModel> conditionValues(ContentValues contentValues) {
        SqlUtils.addContentValues(contentValues, this.conditionGroup);
        return this;
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

    public long count() {
        return where(new SQLCondition[0]).count();
    }

    public long count(DatabaseWrapper databaseWrapper) {
        return where(new SQLCondition[0]).count(databaseWrapper);
    }

    public String getQuery() {
        return new QueryBuilder(this.update.getQuery()).append("SET ").append(this.conditionGroup.getQuery()).appendSpace().getQuery();
    }

    public Query getQueryBuilderBase() {
        return this.update;
    }
}
