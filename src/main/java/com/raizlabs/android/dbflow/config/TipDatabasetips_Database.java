package com.raizlabs.android.dbflow.config;

import com.inteliclinic.neuroon.models.data.Tip;
import com.inteliclinic.neuroon.models.data.TipDatabase;
import com.inteliclinic.neuroon.models.data.Tip_Adapter;

public final class TipDatabasetips_Database extends DatabaseDefinition {
    public TipDatabasetips_Database(DatabaseHolder holder) {
        holder.putDatabaseForTable(Tip.class, this);
        this.models.add(Tip.class);
        this.modelTableNames.put("TIP", Tip.class);
        this.modelAdapters.put(Tip.class, new Tip_Adapter(holder, this));
    }

    public final Class getAssociatedDatabaseClassFile() {
        return TipDatabase.class;
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
        return TipDatabase.NAME;
    }
}
