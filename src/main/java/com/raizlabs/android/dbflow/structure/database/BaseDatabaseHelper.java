package com.raizlabs.android.dbflow.structure.database;

import android.database.sqlite.SQLiteException;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.config.NaturalOrderComparator;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.migration.Migration;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.ModelViewAdapter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseDatabaseHelper {
    public static final String MIGRATION_PATH = "migrations";
    private final DatabaseDefinition databaseDefinition;

    public BaseDatabaseHelper(DatabaseDefinition databaseDefinition2) {
        this.databaseDefinition = databaseDefinition2;
    }

    public DatabaseDefinition getDatabaseDefinition() {
        return this.databaseDefinition;
    }

    public void onCreate(DatabaseWrapper db) {
        checkForeignKeySupport(db);
        executeCreations(db);
        executeMigrations(db, -1, db.getVersion());
    }

    public void onUpgrade(DatabaseWrapper db, int oldVersion, int newVersion) {
        checkForeignKeySupport(db);
        executeCreations(db);
        executeMigrations(db, oldVersion, newVersion);
    }

    public void onOpen(DatabaseWrapper db) {
        checkForeignKeySupport(db);
    }

    /* access modifiers changed from: protected */
    public void checkForeignKeySupport(DatabaseWrapper database) {
        if (this.databaseDefinition.isForeignKeysSupported()) {
            database.execSQL("PRAGMA foreign_keys=ON;");
            FlowLog.log(FlowLog.Level.I, "Foreign Keys supported. Enabling foreign key features.");
        }
    }

    /* access modifiers changed from: protected */
    public void executeCreations(DatabaseWrapper database) {
        try {
            database.beginTransaction();
            for (ModelAdapter modelAdapter : this.databaseDefinition.getModelAdapters()) {
                database.execSQL(modelAdapter.getCreationQuery());
            }
            for (ModelViewAdapter modelView : this.databaseDefinition.getModelViewAdapters()) {
                database.execSQL(new QueryBuilder().append("CREATE VIEW IF NOT EXISTS").appendSpaceSeparated(modelView.getViewName()).append("AS ").append(modelView.getCreationQuery()).getQuery());
            }
            database.setTransactionSuccessful();
        } catch (SQLiteException e) {
            FlowLog.logError(e);
        } finally {
            database.endTransaction();
        }
    }

    /* access modifiers changed from: protected */
    public void executeMigrations(DatabaseWrapper db, int oldVersion, int newVersion) {
        try {
            List<String> files = Arrays.asList(FlowManager.getContext().getAssets().list("migrations/" + this.databaseDefinition.getDatabaseName()));
            Collections.sort(files, new NaturalOrderComparator());
            Map<Integer, List<String>> migrationFileMap = new HashMap<>();
            for (String file : files) {
                try {
                    Integer version = Integer.valueOf(file.replace(".sql", ""));
                    List<String> fileList = migrationFileMap.get(version);
                    if (fileList == null) {
                        fileList = new ArrayList<>();
                        migrationFileMap.put(version, fileList);
                    }
                    fileList.add(file);
                } catch (NumberFormatException e) {
                    FlowLog.log(FlowLog.Level.W, "Skipping invalidly named file: " + file, e);
                }
            }
            Map<Integer, List<Migration>> migrationMap = this.databaseDefinition.getMigrations();
            int curVersion = oldVersion + 1;
            db.beginTransaction();
            for (int i = curVersion; i <= newVersion; i++) {
                List<String> migrationFiles = migrationFileMap.get(Integer.valueOf(i));
                if (migrationFiles != null) {
                    for (String migrationFile : migrationFiles) {
                        executeSqlScript(db, migrationFile);
                        FlowLog.log(FlowLog.Level.I, migrationFile + " executed successfully.");
                    }
                }
                if (migrationMap != null) {
                    List<Migration> migrationsList = migrationMap.get(Integer.valueOf(i));
                    if (migrationsList != null) {
                        for (Migration migration : migrationsList) {
                            migration.onPreMigrate();
                            migration.migrate(db);
                            migration.onPostMigrate();
                            FlowLog.log(FlowLog.Level.I, migration.getClass() + " executed successfully.");
                        }
                    }
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (IOException e2) {
            FlowLog.log(FlowLog.Level.E, "Failed to execute migrations.", e2);
        } catch (Throwable th) {
            db.endTransaction();
            throw th;
        }
    }

    private void executeSqlScript(DatabaseWrapper db, String file) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(FlowManager.getContext().getAssets().open("migrations/" + getDatabaseDefinition().getDatabaseName() + Condition.Operation.DIVISION + file)));
            StringBuffer query = new StringBuffer();
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                String line2 = line.trim();
                boolean isEndOfQuery = line2.endsWith(";");
                if (!line2.startsWith("--")) {
                    if (isEndOfQuery) {
                        line2 = line2.substring(0, line2.length() - ";".length());
                    }
                    query.append(" ").append(line2);
                    if (isEndOfQuery) {
                        db.execSQL(query.toString());
                        query = new StringBuffer();
                    }
                }
            }
            if (query.length() > 0) {
                db.execSQL(query.toString());
            }
        } catch (IOException e) {
            FlowLog.log(FlowLog.Level.E, "Failed to execute " + file, e);
        }
    }
}
