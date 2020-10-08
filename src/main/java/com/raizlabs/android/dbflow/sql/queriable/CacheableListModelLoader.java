package com.raizlabs.android.dbflow.sql.queriable;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.cache.ModelCache;
import java.util.ArrayList;
import java.util.List;

public class CacheableListModelLoader<TModel extends Model> extends ListModelLoader<TModel> {
    private ModelAdapter<TModel> modelAdapter;
    private ModelCache<TModel, ?> modelCache;

    public CacheableListModelLoader(Class<TModel> modelClass) {
        super(modelClass);
    }

    public ModelCache<TModel, ?> getModelCache() {
        if (this.modelCache == null) {
            this.modelCache = this.modelAdapter.getModelCache();
            if (this.modelCache == null) {
                throw new IllegalArgumentException("ModelCache specified in convertToCacheableList() must not be null.");
            }
        }
        return this.modelCache;
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

    public List<TModel> convertToData(@NonNull Cursor cursor, @Nullable List<TModel> data) {
        if (data == null) {
            data = new ArrayList<>();
        }
        Object[] cacheValues = new Object[getModelAdapter().getCachingColumns().length];
        if (cursor.moveToFirst()) {
            do {
                Object[] values = getModelAdapter().getCachingColumnValuesFromCursor(cacheValues, cursor);
                TModel model = getModelCache().get(getModelAdapter().getCachingId(values));
                if (model != null) {
                    getModelAdapter().reloadRelationships(model, cursor);
                    data.add(model);
                } else {
                    TModel model2 = getModelAdapter().newInstance();
                    getModelAdapter().loadFromCursor(cursor, model2);
                    getModelCache().addModel(getModelAdapter().getCachingId(values), model2);
                    data.add(model2);
                }
            } while (cursor.moveToNext());
        }
        return data;
    }
}
