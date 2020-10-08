package com.raizlabs.android.dbflow.sql.saveable;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.SqlUtils;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.InternalAdapter;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.RetrievalAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseStatement;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

public class ModelSaver<TModel extends Model, TTable extends Model, TAdapter extends RetrievalAdapter & InternalAdapter> {
    private static final int INSERT_FAILED = -1;
    private TAdapter adapter;
    private ModelAdapter<TModel> modelAdapter;

    public void setModelAdapter(ModelAdapter<TModel> modelAdapter2) {
        this.modelAdapter = modelAdapter2;
    }

    public void setAdapter(TAdapter adapter2) {
        this.adapter = adapter2;
    }

    public synchronized boolean save(@NonNull TTable model) {
        return save(model, getWritableDatabase(), this.modelAdapter.getInsertStatement(), new ContentValues());
    }

    public synchronized boolean save(@NonNull TTable model, DatabaseWrapper wrapper) {
        return save(model, getWritableDatabase(), this.modelAdapter.getInsertStatement(wrapper), new ContentValues());
    }

    public synchronized boolean save(@NonNull TTable model, DatabaseWrapper wrapper, DatabaseStatement insertStatement, ContentValues contentValues) {
        boolean exists;
        exists = this.adapter.exists(model, wrapper);
        if (exists) {
            exists = update(model, wrapper, contentValues);
        }
        if (!exists) {
            exists = insert(model, insertStatement) > -1;
        }
        if (exists) {
            SqlUtils.notifyModelChanged(model, this.adapter, this.modelAdapter, BaseModel.Action.SAVE);
        }
        return exists;
    }

    public synchronized boolean update(@NonNull TTable model) {
        return update(model, getWritableDatabase(), new ContentValues());
    }

    public synchronized boolean update(@NonNull TTable model, @NonNull DatabaseWrapper wrapper) {
        return update(model, wrapper, new ContentValues());
    }

    public synchronized boolean update(@NonNull TTable model, @NonNull DatabaseWrapper wrapper, @NonNull ContentValues contentValues) {
        boolean successful;
        ((InternalAdapter) this.adapter).bindToContentValues(contentValues, model);
        successful = wrapper.updateWithOnConflict(this.modelAdapter.getTableName(), contentValues, this.adapter.getPrimaryConditionClause(model).getQuery(), (String[]) null, ConflictAction.getSQLiteDatabaseAlgorithmInt(this.modelAdapter.getUpdateOnConflictAction())) != 0;
        if (successful) {
            SqlUtils.notifyModelChanged(model, this.adapter, this.modelAdapter, BaseModel.Action.UPDATE);
        }
        return successful;
    }

    public synchronized long insert(@NonNull TTable model) {
        return insert(model, this.modelAdapter.getInsertStatement());
    }

    public synchronized long insert(@NonNull TTable model, @NonNull DatabaseWrapper wrapper) {
        long result;
        DatabaseStatement insertStatement = this.modelAdapter.getInsertStatement(wrapper);
        try {
            result = insert(model, insertStatement);
            insertStatement.close();
        } catch (Throwable th) {
            insertStatement.close();
            throw th;
        }
        return result;
    }

    public synchronized long insert(@NonNull TTable model, @NonNull DatabaseStatement insertStatement) {
        long id;
        ((InternalAdapter) this.adapter).bindToInsertStatement(insertStatement, model);
        id = insertStatement.executeInsert();
        if (id > -1) {
            ((InternalAdapter) this.adapter).updateAutoIncrement(model, Long.valueOf(id));
            SqlUtils.notifyModelChanged(model, this.adapter, this.modelAdapter, BaseModel.Action.INSERT);
        }
        return id;
    }

    public synchronized boolean delete(@NonNull TTable model) {
        return delete(model, getWritableDatabase());
    }

    public synchronized boolean delete(@NonNull TTable model, @NonNull DatabaseWrapper wrapper) {
        boolean successful = true;
        synchronized (this) {
            if (SQLite.delete(this.modelAdapter.getModelClass()).where(this.adapter.getPrimaryConditionClause(model)).count(wrapper) == 0) {
                successful = false;
            }
            if (successful) {
                SqlUtils.notifyModelChanged(model, this.adapter, this.modelAdapter, BaseModel.Action.DELETE);
            }
            ((InternalAdapter) this.adapter).updateAutoIncrement(model, 0);
        }
        return successful;
    }

    /* access modifiers changed from: protected */
    public DatabaseWrapper getWritableDatabase() {
        return FlowManager.getDatabaseForTable(this.modelAdapter.getModelClass()).getWritableDatabase();
    }

    public TAdapter getAdapter() {
        return this.adapter;
    }

    public ModelAdapter<TModel> getModelAdapter() {
        return this.modelAdapter;
    }
}
