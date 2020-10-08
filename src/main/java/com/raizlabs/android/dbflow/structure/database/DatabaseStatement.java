package com.raizlabs.android.dbflow.structure.database;

public interface DatabaseStatement {
    void bindBlob(int i, byte[] bArr);

    void bindDouble(int i, double d);

    void bindLong(int i, long j);

    void bindNull(int i);

    void bindString(int i, String str);

    void close();

    void execute();

    long executeInsert();

    long executeUpdateDelete();

    long simpleQueryForLong();

    String simpleQueryForString();
}
