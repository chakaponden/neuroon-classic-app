package com.raizlabs.android.dbflow.sql.migration;

import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

public abstract class BaseMigration implements Migration {
    public abstract void migrate(DatabaseWrapper databaseWrapper);

    public void onPreMigrate() {
    }

    public void onPostMigrate() {
    }
}
