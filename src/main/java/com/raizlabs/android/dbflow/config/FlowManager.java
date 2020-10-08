package com.raizlabs.android.dbflow.config;

import android.content.Context;
import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.converter.TypeConverter;
import com.raizlabs.android.dbflow.sql.migration.Migration;
import com.raizlabs.android.dbflow.structure.BaseModelView;
import com.raizlabs.android.dbflow.structure.BaseQueryModel;
import com.raizlabs.android.dbflow.structure.InstanceAdapter;
import com.raizlabs.android.dbflow.structure.InvalidDBConfiguration;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.ModelViewAdapter;
import com.raizlabs.android.dbflow.structure.QueryModelAdapter;
import com.raizlabs.android.dbflow.structure.container.ModelContainerAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class FlowManager {
    private static final String DEFAULT_DATABASE_HOLDER_CLASSNAME = (DEFAULT_DATABASE_HOLDER_PACKAGE_NAME + "." + DEFAULT_DATABASE_HOLDER_NAME);
    private static final String DEFAULT_DATABASE_HOLDER_NAME = "GeneratedDatabaseHolder";
    private static final String DEFAULT_DATABASE_HOLDER_PACKAGE_NAME = FlowManager.class.getPackage().getName();
    static FlowConfig config;
    private static GlobalDatabaseHolder globalDatabaseHolder = new GlobalDatabaseHolder();
    private static HashSet<Class<? extends DatabaseHolder>> loadedModules = new HashSet<>();

    private static class GlobalDatabaseHolder extends DatabaseHolder {
        private GlobalDatabaseHolder() {
        }

        public void add(DatabaseHolder holder) {
            this.databaseDefinitionMap.putAll(holder.databaseDefinitionMap);
            this.databaseNameMap.putAll(holder.databaseNameMap);
            this.typeConverters.putAll(holder.typeConverters);
            this.databaseClassLookupMap.putAll(holder.databaseClassLookupMap);
        }
    }

    public static String getTableName(Class<? extends Model> table) {
        ModelAdapter modelAdapter = getModelAdapter(table);
        if (modelAdapter != null) {
            return modelAdapter.getTableName();
        }
        ModelViewAdapter modelViewAdapter = getDatabaseForTable(table).getModelViewAdapterForTable(table);
        if (modelViewAdapter != null) {
            return modelViewAdapter.getViewName();
        }
        return null;
    }

    public static Class<? extends Model> getTableClassForName(String databaseName, String tableName) {
        DatabaseDefinition databaseDefinition = getDatabase(databaseName);
        if (databaseDefinition == null) {
            throw new IllegalArgumentException(String.format("The specified database %1s was not found. Did you forget to add the @Database?", new Object[]{databaseName}));
        }
        Class<? extends Model> modelClass = databaseDefinition.getModelClassForName(tableName);
        if (modelClass != null) {
            return modelClass;
        }
        throw new IllegalArgumentException(String.format("The specified table %1s was not found. Did you forget to add the @Table annotation and point it to %1s?", new Object[]{tableName, databaseName}));
    }

    public static DatabaseDefinition getDatabaseForTable(Class<? extends Model> table) {
        DatabaseDefinition databaseDefinition = globalDatabaseHolder.getDatabaseForTable(table);
        if (databaseDefinition != null) {
            return databaseDefinition;
        }
        throw new InvalidDBConfiguration("Table: " + table.getName() + " is not registered with a Database. " + "Did you forget the @Table annotation?");
    }

    public static DatabaseDefinition getDatabase(Class<?> databaseClass) {
        DatabaseDefinition databaseDefinition = globalDatabaseHolder.getDatabase(databaseClass);
        if (databaseDefinition != null) {
            return databaseDefinition;
        }
        throw new InvalidDBConfiguration("Database: " + databaseClass.getName() + " is not a registered Database. " + "Did you forget the @Database annotation?");
    }

    public static DatabaseWrapper getWritableDatabaseForTable(Class<? extends Model> table) {
        return getDatabaseForTable(table).getWritableDatabase();
    }

    public static DatabaseDefinition getDatabase(String databaseName) {
        DatabaseDefinition database = globalDatabaseHolder.getDatabase(databaseName);
        if (database != null) {
            return database;
        }
        throw new InvalidDBConfiguration("The specified database" + databaseName + " was not found. " + "Did you forget the @Database annotation?");
    }

    public static DatabaseWrapper getWritableDatabase(String databaseName) {
        return getDatabase(databaseName).getWritableDatabase();
    }

    public static DatabaseWrapper getWritableDatabase(Class<?> databaseClass) {
        return getDatabase(databaseClass).getWritableDatabase();
    }

    public static void initModule(Class<? extends DatabaseHolder> generatedClassName) {
        loadDatabaseHolder(generatedClassName);
    }

    public static FlowConfig getConfig() {
        if (config != null) {
            return config;
        }
        throw new IllegalStateException("Configuration is not initialized. Please call init(FlowConfig) in your application class.");
    }

    protected static void loadDatabaseHolder(Class<? extends DatabaseHolder> holderClass) {
        if (!loadedModules.contains(holderClass)) {
            try {
                DatabaseHolder dbHolder = (DatabaseHolder) holderClass.newInstance();
                if (dbHolder != null) {
                    globalDatabaseHolder.add(dbHolder);
                    loadedModules.add(holderClass);
                }
            } catch (Throwable e) {
                e.printStackTrace();
                throw new ModuleNotFoundException("Cannot load " + holderClass, e);
            }
        }
    }

    public static void reset() {
        for (Map.Entry<Class<?>, DatabaseDefinition> value : globalDatabaseHolder.databaseClassLookupMap.entrySet()) {
            value.getValue().reset(getContext());
        }
        globalDatabaseHolder.reset();
        loadedModules.clear();
    }

    @NonNull
    public static Context getContext() {
        if (config != null) {
            return config.getContext();
        }
        throw new IllegalStateException("You must provide a valid FlowConfig instance. We recommend calling init() in your application class.");
    }

    public static void init(@NonNull FlowConfig flowConfig) {
        config = flowConfig;
        try {
            loadDatabaseHolder(Class.forName(DEFAULT_DATABASE_HOLDER_CLASSNAME));
        } catch (ModuleNotFoundException e) {
            FlowLog.log(FlowLog.Level.W, e.getMessage());
        } catch (ClassNotFoundException e2) {
            FlowLog.log(FlowLog.Level.W, "Could not find the default GeneratedDatabaseHolder");
        }
        if (flowConfig.databaseHolders() != null && !flowConfig.databaseHolders().isEmpty()) {
            for (Class<? extends DatabaseHolder> holder : flowConfig.databaseHolders()) {
                loadDatabaseHolder(holder);
            }
        }
        if (flowConfig.openDatabasesOnInit()) {
            for (DatabaseDefinition databaseDefinition : globalDatabaseHolder.getDatabaseDefinitions()) {
                databaseDefinition.getWritableDatabase();
            }
        }
    }

    public static TypeConverter getTypeConverterForClass(Class<?> objectClass) {
        return globalDatabaseHolder.getTypeConverterForClass(objectClass);
    }

    public static synchronized void destroy() {
        synchronized (FlowManager.class) {
            config = null;
            globalDatabaseHolder = new GlobalDatabaseHolder();
            loadedModules.clear();
        }
    }

    public static InstanceAdapter getInstanceAdapter(Class<? extends Model> modelClass) {
        InstanceAdapter internalAdapter = getModelAdapter(modelClass);
        if (internalAdapter != null) {
            return internalAdapter;
        }
        if (BaseModelView.class.isAssignableFrom(modelClass)) {
            return getModelViewAdapter(modelClass);
        }
        if (BaseQueryModel.class.isAssignableFrom(modelClass)) {
            return getQueryModelAdapter(modelClass);
        }
        return internalAdapter;
    }

    public static <TModel extends Model> ModelAdapter<TModel> getModelAdapter(Class<TModel> modelClass) {
        return getDatabaseForTable(modelClass).getModelAdapterForTable(modelClass);
    }

    public static <TModel extends Model> ModelContainerAdapter<TModel> getContainerAdapter(Class<TModel> modelClass) {
        return getDatabaseForTable(modelClass).getModelContainerAdapterForTable(modelClass);
    }

    public static <TModelView extends BaseModelView<? extends Model>> ModelViewAdapter<? extends Model, TModelView> getModelViewAdapter(Class<TModelView> modelViewClass) {
        return getDatabaseForTable(modelViewClass).getModelViewAdapterForTable(modelViewClass);
    }

    public static <TQueryModel extends BaseQueryModel> QueryModelAdapter<TQueryModel> getQueryModelAdapter(Class<TQueryModel> queryModel) {
        return getDatabaseForTable(queryModel).getQueryModelAdapterForQueryClass(queryModel);
    }

    static Map<Integer, List<Migration>> getMigrations(String databaseName) {
        return getDatabase(databaseName).getMigrations();
    }

    public static boolean isDatabaseIntegrityOk(String databaseName) {
        return getDatabase(databaseName).getHelper().isDatabaseIntegrityOk();
    }

    public static class ModuleNotFoundException extends RuntimeException {
        public ModuleNotFoundException() {
        }

        public ModuleNotFoundException(String detailMessage) {
            super(detailMessage);
        }

        public ModuleNotFoundException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
        }

        public ModuleNotFoundException(Throwable throwable) {
            super(throwable);
        }
    }
}
