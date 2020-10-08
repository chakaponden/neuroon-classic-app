package com.raizlabs.android.dbflow.structure.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public interface DatabaseWrapper {
    void beginTransaction();

    DatabaseStatement compileStatement(String str);

    int delete(@NonNull String str, @Nullable String str2, @Nullable String[] strArr);

    void endTransaction();

    void execSQL(String str);

    int getVersion();

    long insertWithOnConflict(String str, String str2, ContentValues contentValues, int i);

    Cursor query(@NonNull String str, @Nullable String[] strArr, @Nullable String str2, @Nullable String[] strArr2, @Nullable String str3, @Nullable String str4, @Nullable String str5);

    Cursor rawQuery(String str, String[] strArr);

    void setTransactionSuccessful();

    long updateWithOnConflict(String str, ContentValues contentValues, String str2, String[] strArr, int i);
}
