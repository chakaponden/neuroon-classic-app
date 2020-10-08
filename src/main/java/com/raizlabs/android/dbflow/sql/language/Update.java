package com.raizlabs.android.dbflow.sql.language;

import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.structure.Model;

public class Update<TModel extends Model> implements Query {
    private ConflictAction conflictAction = ConflictAction.NONE;
    private final Class<TModel> table;

    public Update(Class<TModel> table2) {
        this.table = table2;
    }

    public Update conflictAction(ConflictAction conflictAction2) {
        this.conflictAction = conflictAction2;
        return this;
    }

    public Update orRollback() {
        return conflictAction(ConflictAction.ROLLBACK);
    }

    public Update orAbort() {
        return conflictAction(ConflictAction.ABORT);
    }

    public Update orReplace() {
        return conflictAction(ConflictAction.REPLACE);
    }

    public Update orFail() {
        return conflictAction(ConflictAction.FAIL);
    }

    public Update orIgnore() {
        return conflictAction(ConflictAction.IGNORE);
    }

    public Set<TModel> set(SQLCondition... conditions) {
        return new Set(this, this.table).conditions(conditions);
    }

    public String getQuery() {
        QueryBuilder queryBuilder = new QueryBuilder("UPDATE ");
        if (this.conflictAction != null && !this.conflictAction.equals(ConflictAction.NONE)) {
            queryBuilder.append(Condition.Operation.OR).appendSpaceSeparated(this.conflictAction.name());
        }
        queryBuilder.append(FlowManager.getTableName(this.table)).appendSpace();
        return queryBuilder.getQuery();
    }
}
