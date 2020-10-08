package com.raizlabs.android.dbflow.structure.container;

import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.TableConfig;
import com.raizlabs.android.dbflow.sql.queriable.ModelContainerLoader;
import com.raizlabs.android.dbflow.sql.saveable.ListModelSaver;
import com.raizlabs.android.dbflow.sql.saveable.ModelSaver;
import com.raizlabs.android.dbflow.structure.InternalAdapter;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.RetrievalAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseStatement;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class ModelContainerAdapter<TModel extends Model> extends RetrievalAdapter<ModelContainer<TModel, ?>, TModel> implements InternalAdapter<ModelContainer<TModel, ?>> {
    protected final Map<String, Class> columnMap = new HashMap();
    private ListModelSaver<TModel, ModelContainer<TModel, ?>, ModelContainerAdapter<TModel>> listModelSaver;
    private ModelAdapter<TModel> modelAdapter;
    private ModelContainerLoader<TModel> modelContainerLoader;
    private ModelSaver<TModel, ModelContainer<TModel, ?>, ModelContainerAdapter<TModel>> modelSaver;

    public abstract ForeignKeyContainer<TModel> toForeignKeyContainer(TModel tmodel);

    public abstract TModel toModel(ModelContainer<TModel, ?> modelContainer);

    public ModelContainerAdapter(DatabaseDefinition databaseDefinition) {
        super(databaseDefinition);
        TableConfig<TModel> tableConfig = getTableConfig();
        if (tableConfig != null) {
            if (tableConfig.modelContainerLoader() != null) {
                this.modelContainerLoader = tableConfig.modelContainerLoader();
            }
            if (tableConfig.modelContainerModelSaver() != null) {
                this.modelSaver = tableConfig.modelContainerModelSaver();
                this.modelSaver.setAdapter(this);
                this.modelSaver.setModelAdapter(getModelAdapter());
            }
        }
    }

    public void save(ModelContainer<TModel, ?> modelContainer) {
        getModelSaver().save(modelContainer);
    }

    public void save(ModelContainer<TModel, ?> model, DatabaseWrapper databaseWrapper) {
        getModelSaver().save(model, databaseWrapper);
    }

    public void saveAll(Collection<ModelContainer<TModel, ?>> modelContainers) {
        getListModelSaver().saveAll(modelContainers);
    }

    public void saveAll(Collection<ModelContainer<TModel, ?>> modelContainers, DatabaseWrapper databaseWrapper) {
        getListModelSaver().saveAll(modelContainers, databaseWrapper);
    }

    public void insert(ModelContainer<TModel, ?> modelContainer) {
        getModelSaver().insert(modelContainer);
    }

    public void insert(ModelContainer<TModel, ?> model, DatabaseWrapper databaseWrapper) {
        getModelSaver().insert(model, databaseWrapper);
    }

    public void insertAll(Collection<ModelContainer<TModel, ?>> modelContainers) {
        getListModelSaver().insertAll(modelContainers);
    }

    public void insertAll(Collection<ModelContainer<TModel, ?>> modelContainers, DatabaseWrapper databaseWrapper) {
        getListModelSaver().insertAll(modelContainers, databaseWrapper);
    }

    public void update(ModelContainer<TModel, ?> modelContainer) {
        getModelSaver().update(modelContainer);
    }

    public void update(ModelContainer<TModel, ?> model, DatabaseWrapper databaseWrapper) {
        getModelSaver().update(model, databaseWrapper);
    }

    public void updateAll(Collection<ModelContainer<TModel, ?>> modelContainers) {
        getListModelSaver().updateAll(modelContainers);
    }

    public void updateAll(Collection<ModelContainer<TModel, ?>> modelContainers, DatabaseWrapper databaseWrapper) {
        getListModelSaver().updateAll(modelContainers, databaseWrapper);
    }

    public void delete(ModelContainer<TModel, ?> modelContainer) {
        getModelSaver().delete(modelContainer);
    }

    public void delete(ModelContainer<TModel, ?> model, DatabaseWrapper databaseWrapper) {
        getModelSaver().delete(model, databaseWrapper);
    }

    public ModelSaver<TModel, ModelContainer<TModel, ?>, ModelContainerAdapter<TModel>> getModelSaver() {
        if (this.modelSaver == null) {
            this.modelSaver = new ModelSaver<>();
            this.modelSaver.setModelAdapter(getModelAdapter());
            this.modelSaver.setAdapter(this);
        }
        return this.modelSaver;
    }

    public ListModelSaver<TModel, ModelContainer<TModel, ?>, ModelContainerAdapter<TModel>> getListModelSaver() {
        if (this.listModelSaver == null) {
            this.listModelSaver = new ListModelSaver<>(getModelSaver());
        }
        return this.listModelSaver;
    }

    public ModelAdapter<TModel> getModelAdapter() {
        if (this.modelAdapter == null) {
            this.modelAdapter = FlowManager.getModelAdapter(getModelClass());
        }
        return this.modelAdapter;
    }

    public void setModelSaver(ModelSaver<TModel, ModelContainer<TModel, ?>, ModelContainerAdapter<TModel>> modelSaver2) {
        this.modelSaver = modelSaver2;
    }

    public void updateAutoIncrement(ModelContainer<TModel, ?> modelContainer, Number id) {
    }

    public Number getAutoIncrementingId(ModelContainer<TModel, ?> modelContainer) {
        return 0;
    }

    public boolean cachingEnabled() {
        return false;
    }

    public void bindToInsertStatement(DatabaseStatement sqLiteStatement, ModelContainer<TModel, ?> model) {
        bindToInsertStatement(sqLiteStatement, model, 0);
    }

    @NonNull
    public Map<String, Class> getColumnMap() {
        return this.columnMap;
    }

    public Class<?> getClassForColumn(String columnName) {
        return this.columnMap.get(columnName);
    }

    public ModelContainerLoader<TModel> getModelContainerLoader() {
        if (this.modelContainerLoader == null) {
            this.modelContainerLoader = createModelContainerLoader();
        }
        return this.modelContainerLoader;
    }

    /* access modifiers changed from: protected */
    public ModelContainerLoader<TModel> createModelContainerLoader() {
        return new ModelContainerLoader<>(getModelClass());
    }

    public void setModelContainerLoader(ModelContainerLoader<TModel> modelContainerLoader2) {
        this.modelContainerLoader = modelContainerLoader2;
    }
}
