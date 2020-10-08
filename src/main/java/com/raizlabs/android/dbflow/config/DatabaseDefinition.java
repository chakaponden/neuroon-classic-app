package com.raizlabs.android.dbflow.config;

import android.content.Context;
import com.raizlabs.android.dbflow.runtime.BaseTransactionManager;
import com.raizlabs.android.dbflow.sql.migration.Migration;
import com.raizlabs.android.dbflow.structure.BaseModelView;
import com.raizlabs.android.dbflow.structure.BaseQueryModel;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.ModelViewAdapter;
import com.raizlabs.android.dbflow.structure.QueryModelAdapter;
import com.raizlabs.android.dbflow.structure.container.ModelContainerAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseHelperListener;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.FlowSQLiteOpenHelper;
import com.raizlabs.android.dbflow.structure.database.OpenHelper;
import com.raizlabs.android.dbflow.structure.database.transaction.DefaultTransactionManager;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class DatabaseDefinition {
    private DatabaseConfig databaseConfig = FlowManager.getConfig().databaseConfigMap().get(getAssociatedDatabaseClassFile());
    private DatabaseHelperListener helperListener;
    private boolean isResetting = false;
    final Map<Integer, List<Migration>> migrationMap = new HashMap();
    final Map<Class<? extends Model>, ModelAdapter> modelAdapters = new HashMap();
    final Map<Class<? extends Model>, ModelContainerAdapter> modelContainerAdapters = new HashMap();
    final Map<String, Class<? extends Model>> modelTableNames = new HashMap();
    final Map<Class<? extends BaseModelView>, ModelViewAdapter> modelViewAdapterMap = new LinkedHashMap();
    final List<Class<? extends BaseModelView>> modelViews = new ArrayList();
    final List<Class<? extends Model>> models = new ArrayList();
    private OpenHelper openHelper;
    final Map<Class<? extends BaseQueryModel>, QueryModelAdapter> queryModelAdapterMap = new LinkedHashMap();
    private BaseTransactionManager transactionManager;

    public abstract boolean areConsistencyChecksEnabled();

    public abstract boolean backupEnabled();

    public abstract Class<?> getAssociatedDatabaseClassFile();

    public abstract String getDatabaseName();

    public abstract int getDatabaseVersion();

    public abstract boolean isForeignKeysSupported();

    public abstract boolean isInMemory();

    public DatabaseDefinition() {
        if (this.databaseConfig != null) {
            for (TableConfig tableConfig : this.databaseConfig.tableConfigMap().values()) {
                ModelAdapter modelAdapter = this.modelAdapters.get(tableConfig.tableClass());
                if (modelAdapter != null) {
                    if (tableConfig.listModelLoader() != null) {
                        modelAdapter.setListModelLoader(tableConfig.listModelLoader());
                    }
                    if (tableConfig.singleModelLoader() != null) {
                        modelAdapter.setSingleModelLoader(tableConfig.singleModelLoader());
                    }
                    if (tableConfig.modelSaver() != null) {
                        modelAdapter.setModelSaver(tableConfig.modelSaver());
                    }
                }
            }
            this.helperListener = this.databaseConfig.helperListener();
        }
        if (this.databaseConfig == null || this.databaseConfig.transactionManagerCreator() == null) {
            this.transactionManager = new DefaultTransactionManager(this);
        } else {
            this.transactionManager = this.databaseConfig.transactionManagerCreator().createManager(this);
        }
    }

    public List<Class<? extends Model>> getModelClasses() {
        return this.models;
    }

    public BaseTransactionManager getTransactionManager() {
        return this.transactionManager;
    }

    public List<ModelAdapter> getModelAdapters() {
        return new ArrayList(this.modelAdapters.values());
    }

    public ModelAdapter getModelAdapterForTable(Class<? extends Model> table) {
        return this.modelAdapters.get(table);
    }

    public Class<? extends Model> getModelClassForName(String tableName) {
        return this.modelTableNames.get(tableName);
    }

    public ModelContainerAdapter getModelContainerAdapterForTable(Class<? extends Model> table) {
        return this.modelContainerAdapters.get(table);
    }

    public List<Class<? extends BaseModelView>> getModelViews() {
        return this.modelViews;
    }

    public ModelViewAdapter getModelViewAdapterForTable(Class<? extends BaseModelView> table) {
        return this.modelViewAdapterMap.get(table);
    }

    public List<ModelViewAdapter> getModelViewAdapters() {
        return new ArrayList(this.modelViewAdapterMap.values());
    }

    public List<QueryModelAdapter> getModelQueryAdapters() {
        return new ArrayList(this.queryModelAdapterMap.values());
    }

    public QueryModelAdapter getQueryModelAdapterForQueryClass(Class<? extends BaseQueryModel> queryModel) {
        return this.queryModelAdapterMap.get(queryModel);
    }

    public Map<Integer, List<Migration>> getMigrations() {
        return this.migrationMap;
    }

    public synchronized OpenHelper getHelper() {
        if (this.openHelper == null) {
            DatabaseConfig config = FlowManager.getConfig().databaseConfigMap().get(getAssociatedDatabaseClassFile());
            if (config == null || config.helperCreator() == null) {
                this.openHelper = new FlowSQLiteOpenHelper(this, this.helperListener);
            } else {
                this.openHelper = config.helperCreator().createHelper(this, this.helperListener);
            }
            this.openHelper.performRestoreFromBackup();
        }
        return this.openHelper;
    }

    public DatabaseWrapper getWritableDatabase() {
        return getHelper().getDatabase();
    }

    public Transaction.Builder beginTransactionAsync(ITransaction transaction) {
        return new Transaction.Builder(transaction, this);
    }

    public void executeTransaction(ITransaction transaction) {
        DatabaseWrapper database = getWritableDatabase();
        try {
            database.beginTransaction();
            transaction.execute(database);
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public String getDatabaseFileName() {
        return getDatabaseName() + ".db";
    }

    public void reset(Context context) {
        if (!this.isResetting) {
            this.isResetting = true;
            getTransactionManager().stopQueue();
            getHelper().closeDB();
            context.deleteDatabase(getDatabaseFileName());
            if (this.databaseConfig == null || this.databaseConfig.transactionManagerCreator() == null) {
                this.transactionManager = new DefaultTransactionManager(this);
            } else {
                this.transactionManager = this.databaseConfig.transactionManagerCreator().createManager(this);
            }
            this.openHelper = null;
            this.isResetting = false;
            getHelper().getDatabase();
        }
    }

    public boolean isDatabaseIntegrityOk() {
        return getHelper().isDatabaseIntegrityOk();
    }

    public void backupDatabase() {
        getHelper().backupDB();
    }
}
