package com.raizlabs.android.dbflow.structure.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;

public class FlowSQLiteOpenHelper extends SQLiteOpenHelper implements OpenHelper {
    private AndroidDatabase androidDatabase;
    private DatabaseHelperDelegate databaseHelperDelegate;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public FlowSQLiteOpenHelper(DatabaseDefinition databaseDefinition, DatabaseHelperListener listener) {
        super(FlowManager.getContext(), databaseDefinition.isInMemory() ? null : databaseDefinition.getDatabaseFileName(), (SQLiteDatabase.CursorFactory) null, databaseDefinition.getDatabaseVersion());
        this.databaseHelperDelegate = new DatabaseHelperDelegate(listener, databaseDefinition, databaseDefinition.backupEnabled() ? new BackupHelper(FlowManager.getContext(), DatabaseHelperDelegate.getTempDbFileName(databaseDefinition), databaseDefinition.getDatabaseVersion(), databaseDefinition) : null);
    }

    public void performRestoreFromBackup() {
        this.databaseHelperDelegate.performRestoreFromBackup();
    }

    public DatabaseHelperDelegate getDelegate() {
        return this.databaseHelperDelegate;
    }

    public boolean isDatabaseIntegrityOk() {
        return this.databaseHelperDelegate.isDatabaseIntegrityOk();
    }

    public void backupDB() {
        this.databaseHelperDelegate.backupDB();
    }

    public DatabaseWrapper getDatabase() {
        if (this.androidDatabase == null) {
            this.androidDatabase = AndroidDatabase.from(getWritableDatabase());
        }
        return this.androidDatabase;
    }

    public void setDatabaseListener(DatabaseHelperListener listener) {
        this.databaseHelperDelegate.setDatabaseHelperListener(listener);
    }

    public void onCreate(SQLiteDatabase db) {
        this.databaseHelperDelegate.onCreate(AndroidDatabase.from(db));
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.databaseHelperDelegate.onUpgrade(AndroidDatabase.from(db), oldVersion, newVersion);
    }

    public void onOpen(SQLiteDatabase db) {
        this.databaseHelperDelegate.onOpen(AndroidDatabase.from(db));
    }

    public void closeDB() {
        getDatabase();
        this.androidDatabase.getDatabase().close();
    }

    private class BackupHelper extends SQLiteOpenHelper implements OpenHelper {
        private AndroidDatabase androidDatabase;
        private final BaseDatabaseHelper baseDatabaseHelper;

        public BackupHelper(Context context, String name, int version, DatabaseDefinition databaseDefinition) {
            super(context, name, (SQLiteDatabase.CursorFactory) null, version);
            this.baseDatabaseHelper = new BaseDatabaseHelper(databaseDefinition);
        }

        public DatabaseWrapper getDatabase() {
            if (this.androidDatabase == null) {
                this.androidDatabase = AndroidDatabase.from(getWritableDatabase());
            }
            return this.androidDatabase;
        }

        public void performRestoreFromBackup() {
        }

        public DatabaseHelperDelegate getDelegate() {
            return null;
        }

        public boolean isDatabaseIntegrityOk() {
            return false;
        }

        public void backupDB() {
        }

        public void setDatabaseListener(DatabaseHelperListener helperListener) {
        }

        public void onCreate(SQLiteDatabase db) {
            this.baseDatabaseHelper.onCreate(AndroidDatabase.from(db));
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            this.baseDatabaseHelper.onUpgrade(AndroidDatabase.from(db), oldVersion, newVersion);
        }

        public void onOpen(SQLiteDatabase db) {
            this.baseDatabaseHelper.onOpen(AndroidDatabase.from(db));
        }

        public void closeDB() {
        }
    }
}
