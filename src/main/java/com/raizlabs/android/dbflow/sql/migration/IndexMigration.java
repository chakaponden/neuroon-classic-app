package com.raizlabs.android.dbflow.sql.migration;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.sql.language.Index;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

public abstract class IndexMigration<TModel extends Model> extends BaseMigration {
    private Index<TModel> index;
    private String name;
    private Class<TModel> onTable;

    @NonNull
    public abstract String getName();

    public IndexMigration(@NonNull Class<TModel> onTable2) {
        this.onTable = onTable2;
    }

    @CallSuper
    public void onPreMigrate() {
        this.index = getIndex();
    }

    public final void migrate(DatabaseWrapper database) {
        database.execSQL(getIndex().getQuery());
    }

    @CallSuper
    public void onPostMigrate() {
        this.onTable = null;
        this.name = null;
        this.index = null;
    }

    public IndexMigration<TModel> addColumn(IProperty property) {
        getIndex().and(property);
        return this;
    }

    public IndexMigration<TModel> unique() {
        getIndex().unique(true);
        return this;
    }

    public Index<TModel> getIndex() {
        if (this.index == null) {
            this.index = new Index(this.name).on(this.onTable, new IProperty[0]);
        }
        return this.index;
    }

    public String getIndexQuery() {
        return getIndex().getQuery();
    }
}
