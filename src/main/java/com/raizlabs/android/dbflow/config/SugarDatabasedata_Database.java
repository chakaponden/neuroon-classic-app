package com.raizlabs.android.dbflow.config;

import com.inteliclinic.neuroon.models.data.Event;
import com.inteliclinic.neuroon.models.data.Event_Adapter;
import com.inteliclinic.neuroon.models.data.OldSleep;
import com.inteliclinic.neuroon.models.data.OldSleep_Adapter;
import com.inteliclinic.neuroon.models.data.SugarDatabase;

public final class SugarDatabasedata_Database extends DatabaseDefinition {
    public SugarDatabasedata_Database(DatabaseHolder holder) {
        holder.putDatabaseForTable(Event.class, this);
        holder.putDatabaseForTable(OldSleep.class, this);
        this.models.add(Event.class);
        this.modelTableNames.put("Event", Event.class);
        this.modelAdapters.put(Event.class, new Event_Adapter(holder, this));
        this.models.add(OldSleep.class);
        this.modelTableNames.put("SLEEP", OldSleep.class);
        this.modelAdapters.put(OldSleep.class, new OldSleep_Adapter(holder, this));
    }

    public final Class getAssociatedDatabaseClassFile() {
        return SugarDatabase.class;
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
        return 2;
    }

    public final String getDatabaseName() {
        return SugarDatabase.NAME;
    }
}
