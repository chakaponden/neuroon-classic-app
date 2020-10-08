package com.raizlabs.android.dbflow;

import android.content.ContentValues;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

@Deprecated
public class SQLiteCompatibilityUtils {
    @Deprecated
    public static long executeUpdateDelete(DatabaseWrapper database, String rawQuery) {
        return database.compileStatement(rawQuery).executeUpdateDelete();
    }

    @Deprecated
    public static long updateWithOnConflict(DatabaseWrapper database, String tableName, ContentValues contentValues, String where, String[] whereArgs, int conflictAlgorithm) {
        return database.updateWithOnConflict(tableName, contentValues, where, whereArgs, conflictAlgorithm);
    }
}
