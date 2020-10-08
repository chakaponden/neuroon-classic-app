package com.raizlabs.android.dbflow.structure.provider;

import android.database.Cursor;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.provider.BaseProviderModel;

public abstract class BaseProviderModel<TProviderModel extends BaseProviderModel> extends BaseModel implements ModelProvider {
    public void delete() {
        ContentUtils.delete(getDeleteUri(), this);
    }

    public void save() {
        if (ContentUtils.update(getUpdateUri(), this) == 0) {
            ContentUtils.insert(getInsertUri(), this);
        }
    }

    public void update() {
        ContentUtils.update(getUpdateUri(), this);
    }

    public void insert() {
        ContentUtils.insert(getInsertUri(), this);
    }

    public boolean exists() {
        boolean exists = false;
        Cursor cursor = ContentUtils.query(FlowManager.getContext().getContentResolver(), getQueryUri(), getModelAdapter().getPrimaryConditionClause(this), "", new String[0]);
        if (cursor != null && cursor.getCount() > 0) {
            exists = true;
        }
        if (cursor != null) {
            cursor.close();
        }
        return exists;
    }

    public void load(ConditionGroup whereConditions, String orderBy, String... columns) {
        Cursor cursor = ContentUtils.query(FlowManager.getContext().getContentResolver(), getQueryUri(), whereConditions, orderBy, columns);
        if (cursor != null && cursor.moveToFirst()) {
            getModelAdapter().loadFromCursor(cursor, this);
            cursor.close();
        }
    }

    public void load() {
        load(getModelAdapter().getPrimaryConditionClause(this), "", new String[0]);
    }
}
