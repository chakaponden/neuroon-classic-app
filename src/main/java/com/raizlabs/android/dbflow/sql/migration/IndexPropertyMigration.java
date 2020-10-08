package com.raizlabs.android.dbflow.sql.migration;

import android.support.annotation.NonNull;
import com.raizlabs.android.dbflow.sql.language.property.IndexProperty;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

public abstract class IndexPropertyMigration extends BaseMigration {
    @NonNull
    public abstract IndexProperty getIndexProperty();

    public boolean shouldCreate() {
        return true;
    }

    public void migrate(DatabaseWrapper database) {
        if (shouldCreate()) {
            getIndexProperty().createIfNotExists(database);
        } else {
            getIndexProperty().drop(database);
        }
    }
}
