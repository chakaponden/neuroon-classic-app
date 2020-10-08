package com.raizlabs.android.dbflow.sql.language.property;

import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Index;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.Model;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

public class IndexProperty<T extends Model> {
    private final Index<T> index;

    public IndexProperty(String indexName, boolean unique, Class<T> table, IProperty... properties) {
        this.index = SQLite.index(indexName);
        this.index.on(table, properties).unique(unique);
    }

    public void createIfNotExists(DatabaseWrapper wrapper) {
        this.index.enable(wrapper);
    }

    public void createIfNotExists() {
        this.index.enable();
    }

    public void drop() {
        this.index.disable();
    }

    public void drop(DatabaseWrapper writableDatabase) {
        this.index.disable(writableDatabase);
    }

    public Index<T> getIndex() {
        return this.index;
    }

    public String getIndexName() {
        return QueryBuilder.quoteIfNeeded(this.index.getIndexName());
    }
}
