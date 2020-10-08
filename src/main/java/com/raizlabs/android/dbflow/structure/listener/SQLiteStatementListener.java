package com.raizlabs.android.dbflow.structure.listener;

import com.raizlabs.android.dbflow.structure.database.DatabaseStatement;

public interface SQLiteStatementListener {
    void onBindToInsertStatement(DatabaseStatement databaseStatement);

    void onBindToStatement(DatabaseStatement databaseStatement);
}
