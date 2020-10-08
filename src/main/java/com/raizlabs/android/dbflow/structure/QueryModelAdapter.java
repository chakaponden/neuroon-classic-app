package com.raizlabs.android.dbflow.structure;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.structure.BaseQueryModel;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

public abstract class QueryModelAdapter<TQueryModel extends BaseQueryModel> extends InstanceAdapter<TQueryModel, TQueryModel> {
    public QueryModelAdapter(DatabaseDefinition databaseDefinition) {
        super(databaseDefinition);
    }

    public ConditionGroup getPrimaryConditionClause(TQueryModel tquerymodel) {
        throw new UnsupportedOperationException("QueryModels cannot check for existence");
    }

    public boolean exists(TQueryModel tquerymodel) {
        throw new UnsupportedOperationException("QueryModels cannot check for existence");
    }

    public boolean exists(TQueryModel tquerymodel, DatabaseWrapper databaseWrapper) {
        throw new UnsupportedOperationException("QueryModels cannot check for existence");
    }
}
