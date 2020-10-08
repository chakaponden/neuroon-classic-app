package com.raizlabs.android.dbflow.config;

import com.inteliclinic.neuroon.models.data.converters.HypnogramConverter;
import com.inteliclinic.neuroon.models.data.converters.IntArrayConverter;
import com.raizlabs.android.dbflow.converter.BooleanConverter;
import com.raizlabs.android.dbflow.converter.CalendarConverter;
import com.raizlabs.android.dbflow.converter.DateConverter;
import com.raizlabs.android.dbflow.converter.SqlDateConverter;
import java.util.Calendar;
import java.util.Date;

public final class GeneratedDatabaseHolder extends DatabaseHolder {
    public GeneratedDatabaseHolder() {
        this.typeConverters.put(Calendar.class, new CalendarConverter());
        this.typeConverters.put(Date.class, new DateConverter());
        this.typeConverters.put(Boolean.class, new BooleanConverter());
        this.typeConverters.put(double[][].class, new HypnogramConverter());
        this.typeConverters.put(int[].class, new IntArrayConverter());
        this.typeConverters.put(java.sql.Date.class, new SqlDateConverter());
        new SugarDatabasedata_Database(this);
        new SleepDatabasesleeps_Database(this);
        new TipDatabasetips_Database(this);
        new AirportDatabaseAirports_Database(this);
    }
}
