package com.raizlabs.android.dbflow.structure.database;

public interface OpenHelper {
    void backupDB();

    void closeDB();

    DatabaseWrapper getDatabase();

    DatabaseHelperDelegate getDelegate();

    boolean isDatabaseIntegrityOk();

    void performRestoreFromBackup();

    void setDatabaseListener(DatabaseHelperListener databaseHelperListener);
}
