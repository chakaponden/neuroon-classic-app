package com.raizlabs.android.dbflow.sql.queriable;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.InstanceAdapter;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

public abstract class ModelLoader<TModel extends Model, TReturn> {
    private DatabaseDefinition databaseDefinition;
    private InstanceAdapter instanceAdapter;
    private final Class<TModel> modelClass;

    @Nullable
    public abstract TReturn convertToData(@NonNull Cursor cursor, @Nullable TReturn treturn);

    public ModelLoader(Class<TModel> modelClass2) {
        this.modelClass = modelClass2;
    }

    public TReturn load(String query) {
        return load(getDatabaseDefinition().getWritableDatabase(), query);
    }

    public TReturn load(String query, @Nullable TReturn data) {
        return load(getDatabaseDefinition().getWritableDatabase(), query, data);
    }

    @Nullable
    public TReturn load(@NonNull DatabaseWrapper databaseWrapper, String query) {
        return load(databaseWrapper, query, (Object) null);
    }

    @Nullable
    public TReturn load(@NonNull DatabaseWrapper databaseWrapper, String query, @Nullable TReturn data) {
        return load(databaseWrapper.rawQuery(query, (String[]) null), data);
    }

    @Nullable
    public TReturn load(@Nullable Cursor cursor) {
        return load(cursor, (Object) null);
    }

    @Nullable
    public TReturn load(@Nullable Cursor cursor, @Nullable TReturn data) {
        if (cursor != null) {
            try {
                data = convertToData(cursor, data);
            } finally {
                cursor.close();
            }
        }
        return data;
    }

    public Class<TModel> getModelClass() {
        return this.modelClass;
    }

    @NonNull
    public InstanceAdapter getInstanceAdapter() {
        if (this.instanceAdapter == null) {
            this.instanceAdapter = FlowManager.getInstanceAdapter(this.modelClass);
        }
        return this.instanceAdapter;
    }

    @NonNull
    public DatabaseDefinition getDatabaseDefinition() {
        if (this.databaseDefinition == null) {
            this.databaseDefinition = FlowManager.getDatabaseForTable(this.modelClass);
        }
        return this.databaseDefinition;
    }
}
