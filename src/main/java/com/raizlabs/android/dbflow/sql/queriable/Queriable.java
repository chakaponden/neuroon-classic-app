package com.raizlabs.android.dbflow.sql.queriable;

import android.database.Cursor;
import android.support.annotation.Nullable;
import com.raizlabs.android.dbflow.sql.Query;
import com.raizlabs.android.dbflow.structure.database.DatabaseStatement;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

public interface Queriable extends Query {
    DatabaseStatement compileStatement();

    DatabaseStatement compileStatement(DatabaseWrapper databaseWrapper);

    long count();

    long count(DatabaseWrapper databaseWrapper);

    void execute();

    void execute(DatabaseWrapper databaseWrapper);

    boolean hasData();

    boolean hasData(DatabaseWrapper databaseWrapper);

    @Nullable
    Cursor query();

    @Nullable
    Cursor query(DatabaseWrapper databaseWrapper);
}
