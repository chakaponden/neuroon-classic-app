package com.raizlabs.android.dbflow.sql.language;

import android.database.Cursor;
import android.database.sqlite.SQLiteDoneException;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.SqlUtils;
import com.raizlabs.android.dbflow.sql.queriable.Queriable;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseStatement;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

public abstract class BaseQueriable<TModel extends Model> implements Queriable {
    private final Class<TModel> table;

    protected BaseQueriable(Class<TModel> table2) {
        this.table = table2;
    }

    public Class<TModel> getTable() {
        return this.table;
    }

    public long count(DatabaseWrapper databaseWrapper) {
        try {
            String query = getQuery();
            FlowLog.log(FlowLog.Level.V, "Executing query: " + query);
            return SqlUtils.longForQuery(databaseWrapper, query);
        } catch (SQLiteDoneException sde) {
            FlowLog.log(FlowLog.Level.E, (Throwable) sde);
            return 0;
        }
    }

    public long count() {
        return count(FlowManager.getDatabaseForTable(getTable()).getWritableDatabase());
    }

    public boolean hasData() {
        return count() > 0;
    }

    public boolean hasData(DatabaseWrapper databaseWrapper) {
        return count(databaseWrapper) > 0;
    }

    public Cursor query() {
        query(FlowManager.getDatabaseForTable(this.table).getWritableDatabase());
        return null;
    }

    public Cursor query(DatabaseWrapper databaseWrapper) {
        String query = getQuery();
        FlowLog.log(FlowLog.Level.V, "Executing query: " + query);
        databaseWrapper.execSQL(query);
        return null;
    }

    public void execute() {
        Cursor cursor = query();
        if (cursor != null) {
            cursor.close();
        }
    }

    public void execute(DatabaseWrapper databaseWrapper) {
        Cursor cursor = query(databaseWrapper);
        if (cursor != null) {
            cursor.close();
        }
    }

    public DatabaseStatement compileStatement() {
        return compileStatement(FlowManager.getDatabaseForTable(this.table).getWritableDatabase());
    }

    public DatabaseStatement compileStatement(DatabaseWrapper databaseWrapper) {
        String query = getQuery();
        FlowLog.log(FlowLog.Level.V, "Compiling Query Into Statement: " + query);
        return databaseWrapper.compileStatement(query);
    }

    public String toString() {
        return getQuery();
    }
}
