package com.raizlabs.android.dbflow.structure;

import android.database.Cursor;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.structure.BaseModelView;
import com.raizlabs.android.dbflow.structure.Model;

public abstract class ModelViewAdapter<TModel extends Model, TModelView extends BaseModelView<TModel>> extends InstanceAdapter<TModelView, TModelView> {
    public abstract String getCreationQuery();

    public abstract String getViewName();

    public ModelViewAdapter(DatabaseDefinition databaseDefinition) {
        super(databaseDefinition);
    }

    public TModelView loadFromCursor(Cursor cursor) {
        TModelView TModelView = (BaseModelView) newInstance();
        loadFromCursor(cursor, TModelView);
        return TModelView;
    }
}
