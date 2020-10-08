package com.raizlabs.android.dbflow.structure;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

public class BaseModel implements Model {
    private transient ModelAdapter modelAdapter;

    public enum Action {
        SAVE,
        INSERT,
        UPDATE,
        DELETE,
        CHANGE
    }

    public void save() {
        getModelAdapter().save(this);
    }

    public void save(DatabaseWrapper databaseWrapper) {
        getModelAdapter().save(this, databaseWrapper);
    }

    public void delete() {
        getModelAdapter().delete(this);
    }

    public void delete(DatabaseWrapper databaseWrapper) {
        getModelAdapter().delete(this, databaseWrapper);
    }

    public void update() {
        getModelAdapter().update(this);
    }

    public void update(DatabaseWrapper databaseWrapper) {
        getModelAdapter().update(this, databaseWrapper);
    }

    public void insert() {
        getModelAdapter().insert(this);
    }

    public void insert(DatabaseWrapper databaseWrapper) {
        getModelAdapter().insert(this, databaseWrapper);
    }

    public boolean exists() {
        return getModelAdapter().exists(this);
    }

    public boolean exists(DatabaseWrapper databaseWrapper) {
        return getModelAdapter().exists(this, databaseWrapper);
    }

    public AsyncModel<BaseModel> async() {
        return new AsyncModel<>(this);
    }

    public ModelAdapter getModelAdapter() {
        if (this.modelAdapter == null) {
            this.modelAdapter = FlowManager.getModelAdapter(getClass());
        }
        return this.modelAdapter;
    }
}
