package com.raizlabs.android.dbflow.structure;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.structure.Model;

public abstract class InstanceAdapter<TModel extends Model, TTable extends Model> extends RetrievalAdapter<TModel, TTable> {
    public abstract TModel newInstance();

    public InstanceAdapter(DatabaseDefinition databaseDefinition) {
        super(databaseDefinition);
    }
}
