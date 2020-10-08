package com.raizlabs.android.dbflow.sql.queriable;

import android.database.Cursor;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.language.BaseModelQueriable;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

public class StringQuery<TModel extends Model> extends BaseModelQueriable<TModel> implements Query, ModelQueriable<TModel> {
    private final String query;

    public StringQuery(Class<TModel> table, String sql) {
        super(table);
        this.query = sql;
    }

    public String getQuery() {
        return this.query;
    }

    public Cursor query() {
        return query(FlowManager.getDatabaseForTable(getTable()).getWritableDatabase());
    }

    public Cursor query(DatabaseWrapper databaseWrapper) {
        return databaseWrapper.rawQuery(this.query, (String[]) null);
    }
}
