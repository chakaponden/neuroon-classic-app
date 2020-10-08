package com.raizlabs.android.dbflow.sql.language;

import android.database.Cursor;
import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Join;
import com.raizlabs.android.dbflow.sql.language.NameAlias;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.sql.language.property.IndexProperty;
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import java.util.ArrayList;
import java.util.List;

public class From<TModel extends Model> extends BaseModelQueriable<TModel> implements WhereBase<TModel>, ModelQueriable<TModel>, Transformable<TModel> {
    private List<Join> joins = new ArrayList();
    private Query queryBase;
    private NameAlias tableAlias;

    public From(Query querybase, Class<TModel> table) {
        super(table);
        this.queryBase = querybase;
        this.tableAlias = new NameAlias.Builder(FlowManager.getTableName(table)).build();
    }

    public From<TModel> as(String alias) {
        this.tableAlias = this.tableAlias.newBuilder().as(alias).build();
        return this;
    }

    public <TJoin extends Model> Join<TJoin, TModel> join(Class<TJoin> table, @NonNull Join.JoinType joinType) {
        Join<TJoin, TModel> join = new Join<>(this, table, joinType);
        this.joins.add(join);
        return join;
    }

    public <TJoin extends Model> Join<TJoin, TModel> join(ModelQueriable<TJoin> modelQueriable, @NonNull Join.JoinType joinType) {
        Join<TJoin, TModel> join = new Join<>(this, joinType, modelQueriable);
        this.joins.add(join);
        return join;
    }

    public <TJoin extends Model> Join<TJoin, TModel> crossJoin(Class<TJoin> table) {
        return join(table, Join.JoinType.CROSS);
    }

    public <TJoin extends Model> Join<TJoin, TModel> crossJoin(ModelQueriable<TJoin> modelQueriable) {
        return join(modelQueriable, Join.JoinType.CROSS);
    }

    public <TJoin extends Model> Join<TJoin, TModel> innerJoin(Class<TJoin> table) {
        return join(table, Join.JoinType.INNER);
    }

    public <TJoin extends Model> Join<TJoin, TModel> innerJoin(ModelQueriable<TJoin> modelQueriable) {
        return join(modelQueriable, Join.JoinType.INNER);
    }

    public <TJoin extends Model> Join<TJoin, TModel> leftOuterJoin(Class<TJoin> table) {
        return join(table, Join.JoinType.LEFT_OUTER);
    }

    public <TJoin extends Model> Join<TJoin, TModel> leftOuterJoin(ModelQueriable<TJoin> modelQueriable) {
        return join(modelQueriable, Join.JoinType.LEFT_OUTER);
    }

    public Where<TModel> where() {
        return new Where<>(this, new SQLCondition[0]);
    }

    public Where<TModel> where(SQLCondition... conditions) {
        return where().andAll(conditions);
    }

    public Cursor query() {
        return where().query();
    }

    public Cursor query(DatabaseWrapper databaseWrapper) {
        return where().query(databaseWrapper);
    }

    public long count() {
        return where().count();
    }

    public long count(DatabaseWrapper databaseWrapper) {
        return where().count(databaseWrapper);
    }

    public IndexedBy<TModel> indexedBy(IndexProperty<TModel> indexProperty) {
        return new IndexedBy<>(indexProperty, this);
    }

    public String getQuery() {
        QueryBuilder queryBuilder = new QueryBuilder().append(this.queryBase.getQuery());
        if (!(this.queryBase instanceof Update)) {
            queryBuilder.append("FROM ");
        }
        queryBuilder.append(this.tableAlias);
        if (this.queryBase instanceof Select) {
            for (Join join : this.joins) {
                queryBuilder.appendSpace();
                queryBuilder.append(join.getQuery());
            }
        } else {
            queryBuilder.appendSpace();
        }
        return queryBuilder.getQuery();
    }

    public Query getQueryBuilderBase() {
        return this.queryBase;
    }

    public Where<TModel> groupBy(NameAlias... nameAliases) {
        return where().groupBy(nameAliases);
    }

    public Where<TModel> groupBy(IProperty... properties) {
        return where().groupBy(properties);
    }

    public Where<TModel> orderBy(NameAlias nameAlias, boolean ascending) {
        return where().orderBy(nameAlias, ascending);
    }

    public Where<TModel> orderBy(IProperty property, boolean ascending) {
        return where().orderBy(property, ascending);
    }

    public Where<TModel> orderBy(OrderBy orderBy) {
        return where().orderBy(orderBy);
    }

    public Where<TModel> limit(int count) {
        return where().limit(count);
    }

    public Where<TModel> offset(int offset) {
        return where().offset(offset);
    }

    public Where<TModel> having(SQLCondition... conditions) {
        return where().having(conditions);
    }
}
