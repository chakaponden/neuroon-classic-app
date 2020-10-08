package com.raizlabs.android.dbflow.sql.saveable;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.structure.InternalAdapter;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.RetrievalAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseStatement;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import java.util.Collection;

public class ListModelSaver<TModel extends Model, TTable extends Model, TAdapter extends RetrievalAdapter & InternalAdapter> {
    private final ModelSaver<TModel, TTable, TAdapter> modelSaver;

    public ListModelSaver(ModelSaver<TModel, TTable, TAdapter> modelSaver2) {
        this.modelSaver = modelSaver2;
    }

    public synchronized void saveAll(@NonNull Collection<TTable> tableCollection) {
        saveAll(tableCollection, this.modelSaver.getWritableDatabase());
    }

    public synchronized void saveAll(@NonNull Collection<TTable> tableCollection, DatabaseWrapper wrapper) {
        if (!tableCollection.isEmpty()) {
            DatabaseStatement statement = this.modelSaver.getModelAdapter().getInsertStatement(wrapper);
            ContentValues contentValues = new ContentValues();
            try {
                for (TTable model : tableCollection) {
                    this.modelSaver.save(model, wrapper, statement, contentValues);
                }
                statement.close();
            } catch (Throwable th) {
                statement.close();
                throw th;
            }
        }
    }

    public synchronized void insertAll(@NonNull Collection<TTable> tableCollection) {
        insertAll(tableCollection, this.modelSaver.getWritableDatabase());
    }

    public synchronized void insertAll(@NonNull Collection<TTable> tableCollection, DatabaseWrapper wrapper) {
        if (!tableCollection.isEmpty()) {
            DatabaseStatement statement = this.modelSaver.getModelAdapter().getInsertStatement(wrapper);
            try {
                for (TTable model : tableCollection) {
                    this.modelSaver.insert(model, statement);
                }
                statement.close();
            } catch (Throwable th) {
                statement.close();
                throw th;
            }
        }
    }

    public synchronized void updateAll(@NonNull Collection<TTable> tableCollection) {
        saveAll(tableCollection, this.modelSaver.getWritableDatabase());
    }

    public synchronized void updateAll(@NonNull Collection<TTable> tableCollection, DatabaseWrapper wrapper) {
        if (!tableCollection.isEmpty()) {
            ContentValues contentValues = new ContentValues();
            for (TTable model : tableCollection) {
                this.modelSaver.update(model, wrapper, contentValues);
            }
        }
    }

    public ModelSaver<TModel, TTable, TAdapter> getModelSaver() {
        return this.modelSaver;
    }
}
