package com.raizlabs.android.dbflow.config;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class FlowConfig {
    private final Context context;
    private final Map<Class<?>, DatabaseConfig> databaseConfigMap;
    private final Set<Class<? extends DatabaseHolder>> databaseHolders;
    private final boolean openDatabasesOnInit;

    FlowConfig(Builder builder) {
        this.databaseHolders = Collections.unmodifiableSet(builder.databaseHolders);
        this.databaseConfigMap = builder.databaseConfigMap;
        this.context = builder.context;
        this.openDatabasesOnInit = builder.openDatabasesOnInit;
    }

    public Set<Class<? extends DatabaseHolder>> databaseHolders() {
        return this.databaseHolders;
    }

    public Map<Class<?>, DatabaseConfig> databaseConfigMap() {
        return this.databaseConfigMap;
    }

    @Nullable
    public DatabaseConfig getConfigForDatabase(Class<?> databaseClass) {
        return databaseConfigMap().get(databaseClass);
    }

    @NonNull
    public Context getContext() {
        return this.context;
    }

    public boolean openDatabasesOnInit() {
        return this.openDatabasesOnInit;
    }

    public static class Builder {
        final Context context;
        final Map<Class<?>, DatabaseConfig> databaseConfigMap = new HashMap();
        Set<Class<? extends DatabaseHolder>> databaseHolders = new HashSet();
        boolean openDatabasesOnInit;

        public Builder(Context context2) {
            this.context = context2;
        }

        public Builder addDatabaseHolder(Class<? extends DatabaseHolder> databaseHolderClass) {
            this.databaseHolders.add(databaseHolderClass);
            return this;
        }

        public Builder addDatabaseConfig(DatabaseConfig databaseConfig) {
            this.databaseConfigMap.put(databaseConfig.databaseClass(), databaseConfig);
            return this;
        }

        public Builder openDatabasesOnInit(boolean openDatabasesOnInit2) {
            this.openDatabasesOnInit = openDatabasesOnInit2;
            return this;
        }

        public FlowConfig build() {
            return new FlowConfig(this);
        }
    }
}
