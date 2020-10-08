package com.raizlabs.android.dbflow.structure;

import android.database.Cursor;
import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.property.BaseProperty;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.sql.saveable.ListModelSaver;
import com.raizlabs.android.dbflow.sql.saveable.ModelSaver;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.cache.IMultiKeyCacheConverter;
import com.raizlabs.android.dbflow.structure.cache.ModelCache;
import com.raizlabs.android.dbflow.structure.cache.SimpleMapCache;
import com.raizlabs.android.dbflow.structure.database.DatabaseStatement;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import java.util.Collection;

public abstract class ModelAdapter<TModel extends Model> extends InstanceAdapter<TModel, TModel> implements InternalAdapter<TModel> {
    private String[] cachingColumns;
    private DatabaseStatement compiledStatement;
    private DatabaseStatement insertStatement;
    private ListModelSaver<TModel, TModel, ModelAdapter<TModel>> listModelSaver;
    private ModelCache<TModel, ?> modelCache;
    private ModelSaver<TModel, TModel, ModelAdapter<TModel>> modelSaver;

    public abstract IProperty[] getAllColumnProperties();

    /* access modifiers changed from: protected */
    public abstract String getCompiledStatementQuery();

    public abstract String getCreationQuery();

    /* access modifiers changed from: protected */
    public abstract String getInsertStatementQuery();

    public abstract BaseProperty getProperty(String str);

    public ModelAdapter(DatabaseDefinition databaseDefinition) {
        super(databaseDefinition);
        if (getTableConfig() != null && getTableConfig().modelSaver() != null) {
            this.modelSaver = getTableConfig().modelSaver();
            this.modelSaver.setAdapter(this);
            this.modelSaver.setModelAdapter(this);
        }
    }

    public DatabaseStatement getInsertStatement() {
        if (this.insertStatement == null) {
            this.insertStatement = getInsertStatement(FlowManager.getDatabaseForTable(getModelClass()).getWritableDatabase());
        }
        return this.insertStatement;
    }

    public DatabaseStatement getInsertStatement(DatabaseWrapper databaseWrapper) {
        return databaseWrapper.compileStatement(getInsertStatementQuery());
    }

    public DatabaseStatement getCompiledStatement() {
        if (this.compiledStatement == null) {
            this.compiledStatement = getCompiledStatement(FlowManager.getDatabaseForTable(getModelClass()).getWritableDatabase());
        }
        return this.compiledStatement;
    }

    public DatabaseStatement getCompiledStatement(DatabaseWrapper databaseWrapper) {
        return databaseWrapper.compileStatement(getCompiledStatementQuery());
    }

    public TModel loadFromCursor(Cursor cursor) {
        TModel model = newInstance();
        loadFromCursor(cursor, model);
        return model;
    }

    public void save(TModel model) {
        getModelSaver().save(model);
    }

    public void save(TModel model, DatabaseWrapper databaseWrapper) {
        getModelSaver().save(model, databaseWrapper);
    }

    public void saveAll(Collection<TModel> models) {
        getListModelSaver().saveAll(models);
        if (cachingEnabled()) {
            for (TModel model : models) {
                getModelCache().addModel(getCachingId(model), model);
            }
        }
    }

    public void saveAll(Collection<TModel> models, DatabaseWrapper databaseWrapper) {
        getListModelSaver().saveAll(models, databaseWrapper);
    }

    public void insert(TModel model) {
        getModelSaver().insert(model);
    }

    public void insert(TModel model, DatabaseWrapper databaseWrapper) {
        getModelSaver().insert(model, databaseWrapper);
    }

    public void insertAll(Collection<TModel> models) {
        getListModelSaver().insertAll(models);
    }

    public void insertAll(Collection<TModel> models, DatabaseWrapper databaseWrapper) {
        getListModelSaver().insertAll(models, databaseWrapper);
    }

    public void update(TModel model) {
        getModelSaver().update(model);
    }

    public void update(TModel model, DatabaseWrapper databaseWrapper) {
        getModelSaver().update(model, databaseWrapper);
    }

    public void updateAll(Collection<TModel> models) {
        getListModelSaver().updateAll(models);
    }

    public void updateAll(Collection<TModel> models, DatabaseWrapper databaseWrapper) {
        getListModelSaver().updateAll(models, databaseWrapper);
    }

    public void delete(TModel model) {
        getModelSaver().delete(model);
    }

    public void delete(TModel model, DatabaseWrapper databaseWrapper) {
        getModelSaver().delete(model, databaseWrapper);
    }

    public void bindToInsertStatement(DatabaseStatement sqLiteStatement, TModel model) {
        bindToInsertStatement(sqLiteStatement, model, 0);
    }

    public void updateAutoIncrement(TModel tmodel, Number id) {
    }

    public Number getAutoIncrementingId(TModel tmodel) {
        throw new InvalidDBConfiguration(String.format("This method may have been called in error. The model class %1s must containa single primary key (if used in a ModelCache, this method may be called)", new Object[]{getModelClass()}));
    }

    public String getAutoIncrementingColumnName() {
        throw new InvalidDBConfiguration(String.format("This method may have been called in error. The model class %1s must contain an autoincrementing or single int/long primary key (if used in a ModelCache, this method may be called)", new Object[]{getModelClass()}));
    }

    public String[] createCachingColumns() {
        return new String[]{getAutoIncrementingColumnName()};
    }

    public String[] getCachingColumns() {
        if (this.cachingColumns == null) {
            this.cachingColumns = createCachingColumns();
        }
        return this.cachingColumns;
    }

    public Object[] getCachingColumnValuesFromCursor(Object[] inValues, Cursor cursor) {
        throwCachingError();
        return null;
    }

    public Object[] getCachingColumnValuesFromModel(Object[] inValues, TModel tmodel) {
        throwCachingError();
        return null;
    }

    public void storeModelInCache(@NonNull TModel model) {
        getModelCache().addModel(getCachingId(model), model);
    }

    public ModelCache<TModel, ?> getModelCache() {
        if (this.modelCache == null) {
            this.modelCache = createModelCache();
        }
        return this.modelCache;
    }

    public Object getCachingId(@NonNull Object[] inValues) {
        if (inValues.length == 1) {
            return inValues[0];
        }
        return getCacheConverter().getCachingKey(inValues);
    }

    public Object getCachingId(@NonNull TModel model) {
        return getCachingId(getCachingColumnValuesFromModel(new Object[getCachingColumns().length], model));
    }

    public ModelSaver<TModel, TModel, ModelAdapter<TModel>> getModelSaver() {
        if (this.modelSaver == null) {
            this.modelSaver = new ModelSaver<>();
            this.modelSaver.setAdapter(this);
            this.modelSaver.setModelAdapter(this);
        }
        return this.modelSaver;
    }

    public ListModelSaver<TModel, TModel, ModelAdapter<TModel>> getListModelSaver() {
        if (this.listModelSaver == null) {
            this.listModelSaver = createListModelSaver();
        }
        return this.listModelSaver;
    }

    /* access modifiers changed from: protected */
    public ListModelSaver<TModel, TModel, ModelAdapter<TModel>> createListModelSaver() {
        return new ListModelSaver<>(getModelSaver());
    }

    public void setModelSaver(ModelSaver<TModel, TModel, ModelAdapter<TModel>> modelSaver2) {
        this.modelSaver = modelSaver2;
    }

    public void reloadRelationships(@NonNull TModel tmodel, Cursor cursor) {
        throwCachingError();
    }

    public boolean cachingEnabled() {
        return false;
    }

    public int getCacheSize() {
        return 25;
    }

    public IMultiKeyCacheConverter<?> getCacheConverter() {
        throw new InvalidDBConfiguration("For multiple primary keys, a public static IMultiKeyCacheConverter field mustbe  marked with @MultiCacheField in the corresponding model class. The resulting keymust be a unique combination of the multiple keys, otherwise inconsistencies may occur.");
    }

    public ModelCache<TModel, ?> createModelCache() {
        return new SimpleMapCache(getCacheSize());
    }

    public ConflictAction getUpdateOnConflictAction() {
        return ConflictAction.ABORT;
    }

    public ConflictAction getInsertOnConflictAction() {
        return ConflictAction.ABORT;
    }

    private void throwCachingError() {
        throw new InvalidDBConfiguration(String.format("This method may have been called in error. The model class %1s must containan auto-incrementing or at least one primary key (if used in a ModelCache, this method may be called)", new Object[]{getModelClass()}));
    }
}
