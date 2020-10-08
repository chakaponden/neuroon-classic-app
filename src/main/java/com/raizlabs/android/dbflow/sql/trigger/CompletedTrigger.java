package com.raizlabs.android.dbflow.sql.trigger;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.SqlUtils;
import com.raizlabs.android.dbflow.structure.Model;

public class CompletedTrigger<TModel extends Model> implements Query {
    private Query triggerLogicQuery;
    private TriggerMethod<TModel> triggerMethod;

    CompletedTrigger(TriggerMethod<TModel> triggerMethod2, Query triggerLogicQuery2) {
        this.triggerMethod = triggerMethod2;
        this.triggerLogicQuery = triggerLogicQuery2;
    }

    public String getQuery() {
        QueryBuilder queryBuilder = new QueryBuilder(this.triggerMethod.getQuery());
        queryBuilder.append("\nBEGIN").append("\n").append(this.triggerLogicQuery.getQuery()).append(";").append("\nEND");
        return queryBuilder.getQuery();
    }

    public void enable() {
        FlowManager.getDatabaseForTable(this.triggerMethod.onTable).getWritableDatabase().execSQL(getQuery());
    }

    public void disable() {
        SqlUtils.dropTrigger(this.triggerMethod.onTable, this.triggerMethod.trigger.triggerName);
    }
}
