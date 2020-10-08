package com.raizlabs.android.dbflow.sql.queriable;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.container.ModelContainer;
import com.raizlabs.android.dbflow.structure.container.ModelContainerAdapter;

public class ModelContainerLoader<TModel extends Model> extends ModelLoader<TModel, ModelContainer<TModel, ?>> {
    private ModelContainerAdapter<TModel> modelContainerAdapter;

    public ModelContainerLoader(Class<TModel> modelClass) {
        super(modelClass);
        this.modelContainerAdapter = FlowManager.getContainerAdapter(modelClass);
    }

    public ModelContainer<TModel, ?> convertToData(@NonNull Cursor cursor, @Nullable ModelContainer<TModel, ?> data) {
        if (data == null) {
            return null;
        }
        if (!cursor.moveToFirst()) {
            return data;
        }
        this.modelContainerAdapter.loadFromCursor(cursor, data);
        return data;
    }
}
