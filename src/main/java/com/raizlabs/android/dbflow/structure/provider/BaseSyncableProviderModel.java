package com.raizlabs.android.dbflow.structure.provider;

import android.database.Cursor;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.structure.BaseModel;

public abstract class BaseSyncableProviderModel extends BaseModel implements ModelProvider {
    public void insert() {
        super.insert();
        ContentUtils.insert(getInsertUri(), this);
    }

    public void save() {
        super.save();
        if (exists()) {
            ContentUtils.update(getUpdateUri(), this);
        } else {
            ContentUtils.insert(getInsertUri(), this);
        }
    }

    public void delete() {
        super.delete();
        ContentUtils.delete(getDeleteUri(), this);
    }

    public void update() {
        super.update();
        ContentUtils.update(getUpdateUri(), this);
    }

    public void load(ConditionGroup whereConditionGroup, String orderBy, String... columns) {
        Cursor cursor = ContentUtils.query(FlowManager.getContext().getContentResolver(), getQueryUri(), whereConditionGroup, orderBy, columns);
        if (cursor != null && cursor.moveToFirst()) {
            getModelAdapter().loadFromCursor(cursor, this);
            cursor.close();
        }
    }

    public void load() {
        load(getModelAdapter().getPrimaryConditionClause(this), "", new String[0]);
    }
}
