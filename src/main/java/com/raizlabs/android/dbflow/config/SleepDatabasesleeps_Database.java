package com.raizlabs.android.dbflow.config;

import com.inteliclinic.neuroon.models.data.Sleep;
import com.inteliclinic.neuroon.models.data.SleepDatabase;
import com.inteliclinic.neuroon.models.data.Sleep_Adapter;

public final class SleepDatabasesleeps_Database extends DatabaseDefinition {
    public SleepDatabasesleeps_Database(DatabaseHolder holder) {
        holder.putDatabaseForTable(Sleep.class, this);
        this.models.add(Sleep.class);
        this.modelTableNames.put("SLEEP", Sleep.class);
        this.modelAdapters.put(Sleep.class, new Sleep_Adapter(holder, this));
    }

    public final Class getAssociatedDatabaseClassFile() {
        return SleepDatabase.class;
    }

    public final boolean isForeignKeysSupported() {
        return false;
    }

    public final boolean isInMemory() {
        return false;
    }

    public final boolean backupEnabled() {
        return false;
    }

    public final boolean areConsistencyChecksEnabled() {
        return false;
    }

    public final int getDatabaseVersion() {
        return 1;
    }

    public final String getDatabaseName() {
        return SleepDatabase.NAME;
    }
}
