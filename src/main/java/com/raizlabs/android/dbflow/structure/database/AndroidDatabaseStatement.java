package com.raizlabs.android.dbflow.structure.database;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;

public class AndroidDatabaseStatement implements DatabaseStatement {
    private final SQLiteDatabase database;
    private final SQLiteStatement statement;

    public static AndroidDatabaseStatement from(SQLiteStatement sqLiteStatement, SQLiteDatabase database2) {
        return new AndroidDatabaseStatement(sqLiteStatement, database2);
    }

    AndroidDatabaseStatement(SQLiteStatement statement2, SQLiteDatabase database2) {
        this.statement = statement2;
        this.database = database2;
    }

    public SQLiteStatement getStatement() {
        return this.statement;
    }

    public long executeUpdateDelete() {
        long count = 0;
        if (Build.VERSION.SDK_INT >= 11) {
            return (long) this.statement.executeUpdateDelete();
        }
        this.statement.execute();
        Cursor cursor = null;
        try {
            Cursor cursor2 = this.database.rawQuery("SELECT changes() AS affected_row_count", (String[]) null);
            if (cursor2 != null && cursor2.getCount() > 0 && cursor2.moveToFirst()) {
                count = cursor2.getLong(cursor2.getColumnIndex("affected_row_count"));
            }
            if (cursor2 == null) {
                return count;
            }
            cursor2.close();
            return count;
        } catch (SQLException e) {
            if (cursor == null) {
                return 0;
            }
            cursor.close();
            return 0;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    public void execute() {
        this.statement.execute();
    }

    public void close() {
        this.statement.close();
    }

    public long simpleQueryForLong() {
        return this.statement.simpleQueryForLong();
    }

    public String simpleQueryForString() {
        return this.statement.simpleQueryForString();
    }

    public long executeInsert() {
        return this.statement.executeInsert();
    }

    public void bindString(int index, String name) {
        this.statement.bindString(index, name);
    }

    public void bindNull(int index) {
        this.statement.bindNull(index);
    }

    public void bindLong(int index, long aLong) {
        this.statement.bindLong(index, aLong);
    }

    public void bindDouble(int index, double aDouble) {
        this.statement.bindDouble(index, aDouble);
    }

    public void bindBlob(int index, byte[] bytes) {
        this.statement.bindBlob(index, bytes);
    }
}
