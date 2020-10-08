package com.raizlabs.android.dbflow.structure.database;

import android.content.Context;
import android.support.annotation.Nullable;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelperDelegate extends BaseDatabaseHelper {
    public static final String TEMP_DB_NAME = "temp-";
    @Nullable
    private final OpenHelper backupHelper;
    private DatabaseHelperListener databaseHelperListener;

    public static String getTempDbFileName(DatabaseDefinition databaseDefinition) {
        return TEMP_DB_NAME + databaseDefinition.getDatabaseName() + ".db";
    }

    public DatabaseHelperDelegate(DatabaseHelperListener databaseHelperListener2, DatabaseDefinition databaseDefinition, @Nullable OpenHelper backupHelper2) {
        super(databaseDefinition);
        this.databaseHelperListener = databaseHelperListener2;
        this.backupHelper = backupHelper2;
    }

    public void performRestoreFromBackup() {
        movePrepackagedDB(getDatabaseDefinition().getDatabaseFileName(), getDatabaseDefinition().getDatabaseFileName(), getWritableDatabase(), this.backupHelper != null ? this.backupHelper.getDatabase() : null);
        if (!getDatabaseDefinition().backupEnabled()) {
            return;
        }
        if (this.backupHelper == null) {
            throw new IllegalStateException("the passed backup helper was null, even though backup is enabled. Ensure that its passed in.");
        }
        restoreDatabase(getTempDbFileName(), getDatabaseDefinition().getDatabaseFileName());
        this.backupHelper.getDatabase();
    }

    public void setDatabaseHelperListener(DatabaseHelperListener databaseHelperListener2) {
        this.databaseHelperListener = databaseHelperListener2;
    }

    public void onCreate(DatabaseWrapper db) {
        if (this.databaseHelperListener != null) {
            this.databaseHelperListener.onCreate(db);
        }
        super.onCreate(db);
    }

    public void onUpgrade(DatabaseWrapper db, int oldVersion, int newVersion) {
        if (this.databaseHelperListener != null) {
            this.databaseHelperListener.onUpgrade(db, oldVersion, newVersion);
        }
        super.onUpgrade(db, oldVersion, newVersion);
    }

    public void onOpen(DatabaseWrapper db) {
        if (this.databaseHelperListener != null) {
            this.databaseHelperListener.onOpen(db);
        }
        super.onOpen(db);
    }

    /* access modifiers changed from: private */
    public String getTempDbFileName() {
        return getTempDbFileName(getDatabaseDefinition());
    }

    public void movePrepackagedDB(String databaseName, String prepackagedName, DatabaseWrapper databaseWrapper, DatabaseWrapper backupDatabaseWrapper) {
        InputStream inputStream;
        File dbPath = FlowManager.getContext().getDatabasePath(databaseName);
        if (dbPath.exists()) {
            if (!getDatabaseDefinition().areConsistencyChecksEnabled()) {
                return;
            }
            if (getDatabaseDefinition().areConsistencyChecksEnabled() && isDatabaseIntegrityOk(databaseWrapper)) {
                return;
            }
        }
        dbPath.getParentFile().mkdirs();
        try {
            File existingDb = FlowManager.getContext().getDatabasePath(getTempDbFileName());
            if (!existingDb.exists() || (getDatabaseDefinition().backupEnabled() && (!getDatabaseDefinition().backupEnabled() || !isDatabaseIntegrityOk(backupDatabaseWrapper)))) {
                inputStream = FlowManager.getContext().getAssets().open(prepackagedName);
            } else {
                inputStream = new FileInputStream(existingDb);
            }
            writeDB(dbPath, inputStream);
        } catch (IOException e) {
            FlowLog.log(FlowLog.Level.W, "Failed to open file", e);
        }
    }

    public boolean isDatabaseIntegrityOk() {
        return isDatabaseIntegrityOk(getWritableDatabase());
    }

    public boolean isDatabaseIntegrityOk(DatabaseWrapper databaseWrapper) {
        boolean integrityOk = true;
        DatabaseStatement prog = null;
        try {
            prog = databaseWrapper.compileStatement("PRAGMA quick_check(1)");
            String rslt = prog.simpleQueryForString();
            if (!rslt.equalsIgnoreCase("ok")) {
                FlowLog.log(FlowLog.Level.E, "PRAGMA integrity_check on " + getDatabaseDefinition().getDatabaseName() + " returned: " + rslt);
                integrityOk = false;
                if (getDatabaseDefinition().backupEnabled()) {
                    integrityOk = restoreBackUp();
                }
            }
            return integrityOk;
        } finally {
            if (prog != null) {
                prog.close();
            }
        }
    }

    public boolean restoreBackUp() {
        File db = FlowManager.getContext().getDatabasePath(TEMP_DB_NAME + getDatabaseDefinition().getDatabaseName());
        File corrupt = FlowManager.getContext().getDatabasePath(getDatabaseDefinition().getDatabaseName());
        if (corrupt.delete()) {
            try {
                writeDB(corrupt, new FileInputStream(db));
                return true;
            } catch (IOException e) {
                FlowLog.logError(e);
                return false;
            }
        } else {
            FlowLog.log(FlowLog.Level.E, "Failed to delete DB");
            return true;
        }
    }

    /* access modifiers changed from: private */
    public void writeDB(File dbPath, InputStream existingDB) throws IOException {
        OutputStream output = new FileOutputStream(dbPath);
        byte[] buffer = new byte[1024];
        while (true) {
            int length = existingDB.read(buffer);
            if (length > 0) {
                output.write(buffer, 0, length);
            } else {
                output.flush();
                output.close();
                existingDB.close();
                return;
            }
        }
    }

    public void restoreDatabase(String databaseName, String prepackagedName) {
        InputStream inputStream;
        File dbPath = FlowManager.getContext().getDatabasePath(databaseName);
        if (!dbPath.exists()) {
            dbPath.getParentFile().mkdirs();
            try {
                File existingDb = FlowManager.getContext().getDatabasePath(getDatabaseDefinition().getDatabaseFileName());
                if (!existingDb.exists() || !getDatabaseDefinition().backupEnabled() || this.backupHelper == null || !isDatabaseIntegrityOk(this.backupHelper.getDatabase())) {
                    inputStream = FlowManager.getContext().getAssets().open(prepackagedName);
                } else {
                    inputStream = new FileInputStream(existingDb);
                }
                writeDB(dbPath, inputStream);
            } catch (IOException e) {
                FlowLog.logError(e);
            }
        }
    }

    public void backupDB() {
        if (!getDatabaseDefinition().backupEnabled() || !getDatabaseDefinition().areConsistencyChecksEnabled()) {
            throw new IllegalStateException("Backups are not enabled for : " + getDatabaseDefinition().getDatabaseName() + ". Please consider adding " + "both backupEnabled and consistency checks enabled to the Database annotation");
        }
        getDatabaseDefinition().beginTransactionAsync(new ITransaction() {
            public void execute(DatabaseWrapper databaseWrapper) {
                Context context = FlowManager.getContext();
                File backup = context.getDatabasePath(DatabaseHelperDelegate.this.getTempDbFileName());
                File temp = context.getDatabasePath("temp--2-" + DatabaseHelperDelegate.this.getDatabaseDefinition().getDatabaseFileName());
                if (temp.exists()) {
                    temp.delete();
                }
                backup.renameTo(temp);
                if (backup.exists()) {
                    backup.delete();
                }
                File existing = context.getDatabasePath(DatabaseHelperDelegate.this.getDatabaseDefinition().getDatabaseFileName());
                try {
                    backup.getParentFile().mkdirs();
                    DatabaseHelperDelegate.this.writeDB(backup, new FileInputStream(existing));
                    temp.delete();
                } catch (Exception e) {
                    FlowLog.logError(e);
                }
            }
        }).build().execute();
    }

    public DatabaseWrapper getWritableDatabase() {
        return getDatabaseDefinition().getWritableDatabase();
    }
}
