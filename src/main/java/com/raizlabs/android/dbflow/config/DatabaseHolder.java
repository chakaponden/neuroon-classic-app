package com.raizlabs.android.dbflow.config;

import com.raizlabs.android.dbflow.converter.TypeConverter;
import com.raizlabs.android.dbflow.structure.Model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class DatabaseHolder {
    protected final Map<Class<?>, DatabaseDefinition> databaseClassLookupMap = new HashMap();
    protected final Map<Class<? extends Model>, DatabaseDefinition> databaseDefinitionMap = new HashMap();
    protected final Map<String, DatabaseDefinition> databaseNameMap = new HashMap();
    protected final Map<Class<?>, TypeConverter> typeConverters = new HashMap();

    public TypeConverter getTypeConverterForClass(Class<?> clazz) {
        return this.typeConverters.get(clazz);
    }

    public DatabaseDefinition getDatabaseForTable(Class<? extends Model> table) {
        return this.databaseDefinitionMap.get(table);
    }

    public DatabaseDefinition getDatabase(Class<?> databaseClass) {
        return this.databaseClassLookupMap.get(databaseClass);
    }

    public DatabaseDefinition getDatabase(String databaseName) {
        return this.databaseNameMap.get(databaseName);
    }

    public void putDatabaseForTable(Class<? extends Model> table, DatabaseDefinition databaseDefinition) {
        this.databaseDefinitionMap.put(table, databaseDefinition);
        this.databaseNameMap.put(databaseDefinition.getDatabaseName(), databaseDefinition);
        this.databaseClassLookupMap.put(databaseDefinition.getAssociatedDatabaseClassFile(), databaseDefinition);
    }

    public void reset() {
        this.databaseDefinitionMap.clear();
        this.databaseNameMap.clear();
        this.databaseClassLookupMap.clear();
        this.typeConverters.clear();
    }

    public List<DatabaseDefinition> getDatabaseDefinitions() {
        return new ArrayList(this.databaseNameMap.values());
    }
}
