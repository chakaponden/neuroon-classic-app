package com.raizlabs.android.dbflow.structure;

import android.database.Cursor;
import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.config.DatabaseConfig;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.TableConfig;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.queriable.ListModelLoader;
import com.raizlabs.android.dbflow.sql.queriable.SingleModelLoader;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

public abstract class RetrievalAdapter<TModel extends Model, TTable extends Model> {
    private ListModelLoader<TTable> listModelLoader;
    private SingleModelLoader<TTable> singleModelLoader;
    private TableConfig<TTable> tableConfig;

    public abstract boolean exists(TModel tmodel, DatabaseWrapper databaseWrapper);

    public abstract Class<TTable> getModelClass();

    public abstract ConditionGroup getPrimaryConditionClause(TModel tmodel);

    public abstract void loadFromCursor(Cursor cursor, TModel tmodel);

    public RetrievalAdapter(DatabaseDefinition databaseDefinition) {
        DatabaseConfig databaseConfig = FlowManager.getConfig().getConfigForDatabase(databaseDefinition.getAssociatedDatabaseClassFile());
        if (databaseConfig != null) {
            this.tableConfig = databaseConfig.getTableConfigForTable(getModelClass());
            if (this.tableConfig != null) {
                if (this.tableConfig.singleModelLoader() != null) {
                    this.singleModelLoader = this.tableConfig.singleModelLoader();
                }
                if (this.tableConfig.listModelLoader() != null) {
                    this.listModelLoader = this.tableConfig.listModelLoader();
                }
            }
        }
    }

    public boolean exists(TModel model) {
        return exists(model, FlowManager.getDatabaseForTable(getModelClass()).getWritableDatabase());
    }

    /* access modifiers changed from: protected */
    public TableConfig<TTable> getTableConfig() {
        return this.tableConfig;
    }

    public ListModelLoader<TTable> getListModelLoader() {
        if (this.listModelLoader == null) {
            this.listModelLoader = createListModelLoader();
        }
        return this.listModelLoader;
    }

    /* access modifiers changed from: protected */
    public ListModelLoader<TTable> createListModelLoader() {
        return new ListModelLoader<>(getModelClass());
    }

    public SingleModelLoader<TTable> getSingleModelLoader() {
        if (this.singleModelLoader == null) {
            this.singleModelLoader = createSingleModelLoader();
        }
        return this.singleModelLoader;
    }

    public void setSingleModelLoader(@NonNull SingleModelLoader<TTable> singleModelLoader2) {
        this.singleModelLoader = singleModelLoader2;
    }

    public void setListModelLoader(@NonNull ListModelLoader<TTable> listModelLoader2) {
        this.listModelLoader = listModelLoader2;
    }

    /* access modifiers changed from: protected */
    public SingleModelLoader<TTable> createSingleModelLoader() {
        return new SingleModelLoader<>(getModelClass());
    }
}
