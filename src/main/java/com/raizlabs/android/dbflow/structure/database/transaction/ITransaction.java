package com.raizlabs.android.dbflow.structure.database.transaction;

import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

public interface ITransaction {
    void execute(DatabaseWrapper databaseWrapper);
}
