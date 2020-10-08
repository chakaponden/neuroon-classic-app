package com.raizlabs.android.dbflow.sql.trigger;

import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.Model;

public class Trigger implements Query {
    public static final String AFTER = "AFTER";
    public static final String BEFORE = "BEFORE";
    public static final String INSTEAD_OF = "INSTEAD OF";
    String beforeOrAfter;
    boolean temporary;
    final String triggerName;

    public static Trigger create(String triggerName2) {
        return new Trigger(triggerName2);
    }

    private Trigger(String triggerName2) {
        this.triggerName = triggerName2;
    }

    public Trigger temporary() {
        this.temporary = true;
        return this;
    }

    public Trigger after() {
        this.beforeOrAfter = AFTER;
        return this;
    }

    public Trigger before() {
        this.beforeOrAfter = BEFORE;
        return this;
    }

    public Trigger insteadOf() {
        this.beforeOrAfter = INSTEAD_OF;
        return this;
    }

    public <TModel extends Model> TriggerMethod<TModel> delete(Class<TModel> onTable) {
        return new TriggerMethod<>(this, "DELETE", onTable, new IProperty[0]);
    }

    public <TModel extends Model> TriggerMethod<TModel> insert(Class<TModel> onTable) {
        return new TriggerMethod<>(this, TriggerMethod.INSERT, onTable, new IProperty[0]);
    }

    public <TModel extends Model> TriggerMethod<TModel> update(Class<TModel> onTable, IProperty... properties) {
        return new TriggerMethod<>(this, TriggerMethod.UPDATE, onTable, properties);
    }

    public String getName() {
        return this.triggerName;
    }

    public String getQuery() {
        QueryBuilder queryBuilder = new QueryBuilder("CREATE ");
        if (this.temporary) {
            queryBuilder.append("TEMP ");
        }
        queryBuilder.append("TRIGGER IF NOT EXISTS ").appendQuotedIfNeeded(this.triggerName).appendSpace().appendOptional(" " + this.beforeOrAfter + " ");
        return queryBuilder.getQuery();
    }
}
