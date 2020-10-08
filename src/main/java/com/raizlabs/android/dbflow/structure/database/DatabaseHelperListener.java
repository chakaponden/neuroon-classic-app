package com.raizlabs.android.dbflow.structure.database;

public interface DatabaseHelperListener {
    void onCreate(DatabaseWrapper databaseWrapper);

    void onOpen(DatabaseWrapper databaseWrapper);

    void onUpgrade(DatabaseWrapper databaseWrapper, int i, int i2);
}
