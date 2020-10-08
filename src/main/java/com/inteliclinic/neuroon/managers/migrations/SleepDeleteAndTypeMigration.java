package com.inteliclinic.neuroon.managers.migrations;

import com.inteliclinic.neuroon.models.data.OldSleep;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;

public class SleepDeleteAndTypeMigration extends AlterTableMigration<OldSleep> {
    public SleepDeleteAndTypeMigration(Class<OldSleep> table) {
        super(table);
    }

    public void onPreMigrate() {
        addColumn(SQLiteType.INTEGER, "TO_DELETE");
        addColumn(SQLiteType.INTEGER, "SLEEP_TYPE");
    }
}
