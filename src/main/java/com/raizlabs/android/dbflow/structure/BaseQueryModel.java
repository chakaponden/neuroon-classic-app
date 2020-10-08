package com.raizlabs.android.dbflow.structure;

import android.database.Cursor;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.NoModificationModel;

public class BaseQueryModel extends NoModificationModel {
    private transient QueryModelAdapter adapter;

    public /* bridge */ /* synthetic */ void delete() {
        super.delete();
    }

    public /* bridge */ /* synthetic */ void insert() {
        super.insert();
    }

    public /* bridge */ /* synthetic */ void save() {
        super.save();
    }

    public /* bridge */ /* synthetic */ void update() {
        super.update();
    }

    public boolean exists() {
        throw new NoModificationModel.InvalidSqlViewOperationException("Query " + getClass().getName() + " does not exist as a table." + "It's a convenient representation of a complex SQLite query.");
    }

    public QueryModelAdapter getQueryModelAdapter() {
        if (this.adapter == null) {
            this.adapter = FlowManager.getQueryModelAdapter(getClass());
        }
        return this.adapter;
    }

    public void loadFromCursor(Cursor cursor) {
        if (cursor != null && cursor.moveToFirst()) {
            getQueryModelAdapter().loadFromCursor(cursor, this);
        }
        if (cursor != null) {
            cursor.close();
        }
    }
}
