package com.raizlabs.android.dbflow.sql.trigger;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.Model;

public class TriggerMethod<TModel extends Model> implements Query {
    public static final String DELETE = "DELETE";
    public static final String INSERT = "INSERT";
    public static final String UPDATE = "UPDATE";
    boolean forEachRow = false;
    private final String methodName;
    Class<TModel> onTable;
    private IProperty[] properties;
    final Trigger trigger;
    private SQLCondition whenCondition;

    TriggerMethod(Trigger trigger2, String methodName2, Class<TModel> onTable2, IProperty... properties2) {
        this.trigger = trigger2;
        this.methodName = methodName2;
        this.onTable = onTable2;
        if (properties2 != null && properties2.length > 0 && properties2[0] != null) {
            if (!methodName2.equals(UPDATE)) {
                throw new IllegalArgumentException("An Trigger OF can only be used with an UPDATE method");
            }
            this.properties = properties2;
        }
    }

    public TriggerMethod<TModel> forEachRow() {
        this.forEachRow = true;
        return this;
    }

    public TriggerMethod<TModel> when(SQLCondition condition) {
        this.whenCondition = condition;
        return this;
    }

    public CompletedTrigger<TModel> begin(Query triggerLogicQuery) {
        return new CompletedTrigger<>(this, triggerLogicQuery);
    }

    public String getQuery() {
        QueryBuilder queryBuilder = new QueryBuilder(this.trigger.getQuery()).append(this.methodName);
        if (this.properties != null && this.properties.length > 0) {
            queryBuilder.appendSpaceSeparated("OF").appendArray((Object[]) this.properties);
        }
        queryBuilder.appendSpaceSeparated("ON").append(FlowManager.getTableName(this.onTable));
        if (this.forEachRow) {
            queryBuilder.appendSpaceSeparated("FOR EACH ROW");
        }
        if (this.whenCondition != null) {
            queryBuilder.append(" WHEN ");
            this.whenCondition.appendConditionToQuery(queryBuilder);
            queryBuilder.appendSpace();
        }
        queryBuilder.appendSpace();
        return queryBuilder.getQuery();
    }
}
