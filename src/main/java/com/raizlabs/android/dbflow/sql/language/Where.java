package com.raizlabs.android.dbflow.sql.language;

import android.database.Cursor;
import android.database.sqlite.SQLiteDoneException;
import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.SqlUtils;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.sql.queriable.ModelQueriable;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Where<TModel extends Model> extends BaseModelQueriable<TModel> implements Query, ModelQueriable<TModel>, Transformable<TModel> {
    private static final int VALUE_UNSET = -1;
    private ConditionGroup conditionGroup;
    private final List<NameAlias> groupByList = new ArrayList();
    private ConditionGroup havingGroup;
    private int limit = -1;
    private int offset = -1;
    private final List<OrderBy> orderByList = new ArrayList();
    private final WhereBase<TModel> whereBase;

    Where(WhereBase<TModel> whereBase2, SQLCondition... conditions) {
        super(whereBase2.getTable());
        this.whereBase = whereBase2;
        this.conditionGroup = new ConditionGroup();
        this.havingGroup = new ConditionGroup();
        this.conditionGroup.andAll(conditions);
    }

    public Where<TModel> and(SQLCondition condition) {
        this.conditionGroup.and(condition);
        return this;
    }

    public Where<TModel> or(SQLCondition condition) {
        this.conditionGroup.or(condition);
        return this;
    }

    public Where<TModel> andAll(List<SQLCondition> conditions) {
        this.conditionGroup.andAll(conditions);
        return this;
    }

    public Where<TModel> andAll(SQLCondition... conditions) {
        this.conditionGroup.andAll(conditions);
        return this;
    }

    public Where<TModel> groupBy(NameAlias... columns) {
        Collections.addAll(this.groupByList, columns);
        return this;
    }

    public Where<TModel> groupBy(IProperty... properties) {
        for (IProperty property : properties) {
            this.groupByList.add(property.getNameAlias());
        }
        return this;
    }

    public Where<TModel> having(SQLCondition... conditions) {
        this.havingGroup.andAll(conditions);
        return this;
    }

    public Where<TModel> orderBy(NameAlias nameAlias, boolean ascending) {
        this.orderByList.add(new OrderBy(nameAlias, ascending));
        return this;
    }

    public Where<TModel> orderBy(IProperty property, boolean ascending) {
        this.orderByList.add(new OrderBy(property.getNameAlias(), ascending));
        return this;
    }

    public Where<TModel> orderBy(OrderBy orderBy) {
        this.orderByList.add(orderBy);
        return this;
    }

    public Where<TModel> orderByAll(List<OrderBy> orderBies) {
        if (orderBies != null) {
            this.orderByList.addAll(orderBies);
        }
        return this;
    }

    public Where<TModel> limit(int count) {
        this.limit = count;
        return this;
    }

    public Where<TModel> offset(int offset2) {
        this.offset = offset2;
        return this;
    }

    public Where<TModel> exists(@NonNull Where where) {
        this.conditionGroup.and(new ExistenceCondition().where(where));
        return this;
    }

    public long count(DatabaseWrapper databaseWrapper) {
        if ((this.whereBase instanceof Set) || (this.whereBase.getQueryBuilderBase() instanceof Delete)) {
            return databaseWrapper.compileStatement(getQuery()).executeUpdateDelete();
        }
        try {
            return SqlUtils.longForQuery(databaseWrapper, getQuery());
        } catch (SQLiteDoneException sde) {
            FlowLog.log(FlowLog.Level.E, (Throwable) sde);
            return 0;
        }
    }

    public String getQuery() {
        QueryBuilder queryBuilder = new QueryBuilder().append(this.whereBase.getQuery().trim()).appendSpace().appendQualifier("WHERE", this.conditionGroup.getQuery()).appendQualifier("GROUP BY", QueryBuilder.join((CharSequence) ",", (Iterable) this.groupByList)).appendQualifier("HAVING", this.havingGroup.getQuery()).appendQualifier("ORDER BY", QueryBuilder.join((CharSequence) ",", (Iterable) this.orderByList));
        if (this.limit > -1) {
            queryBuilder.appendQualifier("LIMIT", String.valueOf(this.limit));
        }
        if (this.offset > -1) {
            queryBuilder.appendQualifier("OFFSET", String.valueOf(this.offset));
        }
        return queryBuilder.getQuery();
    }

    public Cursor query(DatabaseWrapper wrapper) {
        String query = getQuery();
        if (this.whereBase.getQueryBuilderBase() instanceof Select) {
            return wrapper.rawQuery(query, (String[]) null);
        }
        wrapper.execSQL(query);
        return null;
    }

    public Cursor query() {
        return query(FlowManager.getDatabaseForTable(getTable()).getWritableDatabase());
    }

    public List<TModel> queryList() {
        checkSelect("query");
        return super.queryList();
    }

    /* access modifiers changed from: protected */
    public void checkSelect(String methodName) {
        if (!(this.whereBase.getQueryBuilderBase() instanceof Select)) {
            throw new IllegalArgumentException("Please use " + methodName + "(). The beginning is not a Select");
        }
    }

    public TModel querySingle() {
        checkSelect("query");
        limit(1);
        return super.querySingle();
    }
}
