package com.raizlabs.android.dbflow.config;

import com.inteliclinic.neuroon.models.data.Airport;
import com.inteliclinic.neuroon.models.data.AirportDatabase;
import com.inteliclinic.neuroon.models.data.Airport_Adapter;

public final class AirportDatabaseAirports_Database extends DatabaseDefinition {
    public AirportDatabaseAirports_Database(DatabaseHolder holder) {
        holder.putDatabaseForTable(Airport.class, this);
        this.models.add(Airport.class);
        this.modelTableNames.put("Airport", Airport.class);
        this.modelAdapters.put(Airport.class, new Airport_Adapter(holder, this));
    }

    public final Class getAssociatedDatabaseClassFile() {
        return AirportDatabase.class;
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
        return AirportDatabase.NAME;
    }
}
