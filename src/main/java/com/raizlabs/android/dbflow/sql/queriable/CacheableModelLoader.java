package com.raizlabs.android.dbflow.sql.queriable;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.cache.ModelCache;

public class CacheableModelLoader<TModel extends Model> extends SingleModelLoader<TModel> {
    private ModelAdapter<TModel> modelAdapter;

    public CacheableModelLoader(Class<TModel> modelClass) {
        super(modelClass);
    }

    public ModelAdapter<TModel> getModelAdapter() {
        if (this.modelAdapter == null) {
            if (!(getInstanceAdapter() instanceof ModelAdapter)) {
                throw new IllegalArgumentException("A non-Table type was used.");
            }
            this.modelAdapter = (ModelAdapter) getInstanceAdapter();
            if (!this.modelAdapter.cachingEnabled()) {
                throw new IllegalArgumentException("You cannot call this method for a table that has no caching id. Eitheruse one Primary Key or use the MultiCacheKeyConverter");
            }
        }
        return this.modelAdapter;
    }

    @Nullable
    public TModel convertToData(@NonNull Cursor cursor, @Nullable TModel data, boolean moveToFirst) {
        TModel model;
        if (moveToFirst && !cursor.moveToFirst()) {
            return null;
        }
        ModelCache<TModel, ?> modelCache = getModelAdapter().getModelCache();
        Object[] values = getModelAdapter().getCachingColumnValuesFromCursor(new Object[getModelAdapter().getCachingColumns().length], cursor);
        TModel model2 = modelCache.get(getModelAdapter().getCachingId(values));
        if (model2 == null) {
            if (data == null) {
                model = getModelAdapter().newInstance();
            } else {
                model = data;
            }
            getModelAdapter().loadFromCursor(cursor, model);
            modelCache.addModel(getModelAdapter().getCachingId(values), model);
            return model;
        }
        getModelAdapter().reloadRelationships(model2, cursor);
        return model2;
    }
}
