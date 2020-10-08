package com.raizlabs.android.dbflow.sql.language;

import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.SqlUtils;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import java.util.ArrayList;
import java.util.List;

public class Index<TModel extends Model> implements Query {
    private List<NameAlias> columns;
    private final String indexName;
    private boolean isUnique = false;
    private Class<TModel> table;

    public Index(@NonNull String indexName2) {
        this.indexName = indexName2;
        this.columns = new ArrayList();
    }

    public Index<TModel> unique(boolean unique) {
        this.isUnique = unique;
        return this;
    }

    public Index<TModel> on(@NonNull Class<TModel> table2, IProperty... properties) {
        this.table = table2;
        for (IProperty property : properties) {
            and(property);
        }
        return this;
    }

    public Index<TModel> on(@NonNull Class<TModel> table2, NameAlias firstAlias, NameAlias... columns2) {
        this.table = table2;
        and(firstAlias);
        for (NameAlias column : columns2) {
            and(column);
        }
        return this;
    }

    public Index<TModel> and(IProperty property) {
        if (!this.columns.contains(property.getNameAlias())) {
            this.columns.add(property.getNameAlias());
        }
        return this;
    }

    public Index<TModel> and(NameAlias columnName) {
        if (!this.columns.contains(columnName)) {
            this.columns.add(columnName);
        }
        return this;
    }

    public String getIndexName() {
        return this.indexName;
    }

    public Class<TModel> getTable() {
        return this.table;
    }

    public boolean isUnique() {
        return this.isUnique;
    }

    public void enable(DatabaseWrapper databaseWrapper) {
        if (this.table == null) {
            throw new IllegalStateException("Please call on() to set a table to use this index on.");
        } else if (this.columns == null || this.columns.isEmpty()) {
            throw new IllegalStateException("There should be at least one column in this index");
        } else {
            databaseWrapper.execSQL(getQuery());
        }
    }

    public void enable() {
        enable(FlowManager.getDatabaseForTable(this.table).getWritableDatabase());
    }

    public void disable() {
        SqlUtils.dropIndex(FlowManager.getDatabaseForTable(this.table).getWritableDatabase(), this.indexName);
    }

    public void disable(DatabaseWrapper databaseWrapper) {
        SqlUtils.dropIndex(databaseWrapper, this.indexName);
    }

    public String getQuery() {
        return new QueryBuilder("CREATE ").append(this.isUnique ? "UNIQUE " : "").append("INDEX IF NOT EXISTS ").appendQuotedIfNeeded(this.indexName).append(" ON ").append(FlowManager.getTableName(this.table)).append("(").appendList(this.columns).append(")").getQuery();
    }
}
