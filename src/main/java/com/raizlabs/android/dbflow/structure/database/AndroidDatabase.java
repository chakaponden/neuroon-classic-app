package com.raizlabs.android.dbflow.structure.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class AndroidDatabase implements DatabaseWrapper {
    private final SQLiteDatabase database;

    public static AndroidDatabase from(SQLiteDatabase database2) {
        return new AndroidDatabase(database2);
    }

    AndroidDatabase(@NonNull SQLiteDatabase database2) {
        this.database = database2;
    }

    public void execSQL(String query) {
        this.database.execSQL(query);
    }

    public void beginTransaction() {
        this.database.beginTransaction();
    }

    public void setTransactionSuccessful() {
        this.database.setTransactionSuccessful();
    }

    public void endTransaction() {
        this.database.endTransaction();
    }

    public int getVersion() {
        return this.database.getVersion();
    }

    public SQLiteDatabase getDatabase() {
        return this.database;
    }

    public DatabaseStatement compileStatement(String rawQuery) {
        return AndroidDatabaseStatement.from(this.database.compileStatement(rawQuery), this.database);
    }

    public Cursor rawQuery(String query, String[] selectionArgs) {
        return this.database.rawQuery(query, selectionArgs);
    }

    public long updateWithOnConflict(String tableName, ContentValues contentValues, String where, String[] whereArgs, int conflictAlgorithm) {
        if (Build.VERSION.SDK_INT >= 8) {
            return (long) this.database.updateWithOnConflict(tableName, contentValues, where, whereArgs, conflictAlgorithm);
        }
        return (long) this.database.update(tableName, contentValues, where, whereArgs);
    }

    public long insertWithOnConflict(String tableName, String nullColumnHack, ContentValues values, int sqLiteDatabaseAlgorithmInt) {
        if (Build.VERSION.SDK_INT >= 8) {
            return this.database.insertWithOnConflict(tableName, nullColumnHack, values, sqLiteDatabaseAlgorithmInt);
        }
        return this.database.insert(tableName, nullColumnHack, values);
    }

    public Cursor query(@NonNull String tableName, @Nullable String[] columns, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String groupBy, @Nullable String having, @Nullable String orderBy) {
        return this.database.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    public int delete(@NonNull String tableName, @Nullable String whereClause, @Nullable String[] whereArgs) {
        return this.database.delete(tableName, whereClause, whereArgs);
    }
}
