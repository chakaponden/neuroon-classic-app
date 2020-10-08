package com.inteliclinic.neuroon.models.data;

import com.raizlabs.android.dbflow.runtime.BaseContentProvider;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.language.property.BaseProperty;
import com.raizlabs.android.dbflow.sql.language.property.FloatProperty;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.sql.language.property.LongProperty;
import com.raizlabs.android.dbflow.sql.language.property.Property;
import com.raizlabs.android.dbflow.structure.Model;

public final class Airport_Table {
    public static final Property<String> CITY = new Property<>((Class<? extends Model>) Airport.class, "CITY");
    public static final Property<String> CITY_CODE = new Property<>((Class<? extends Model>) Airport.class, "CITY_CODE");
    public static final Property<String> COUNTRY = new Property<>((Class<? extends Model>) Airport.class, "COUNTRY");
    public static final Property<String> IATA = new Property<>((Class<? extends Model>) Airport.class, "IATA");
    public static final Property<String> ICAO = new Property<>((Class<? extends Model>) Airport.class, "ICAO");
    public static final LongProperty ID = new LongProperty((Class<? extends Model>) Airport.class, "ID");
    public static final FloatProperty LATITUDE = new FloatProperty((Class<? extends Model>) Airport.class, "LATITUDE");
    public static final FloatProperty LONGITUDE = new FloatProperty((Class<? extends Model>) Airport.class, "LONGITUDE");
    public static final Property<String> NAME = new Property<>((Class<? extends Model>) Airport.class, "NAME");
    public static final BaseContentProvider.PropertyConverter PROPERTY_CONVERTER = new BaseContentProvider.PropertyConverter() {
        public IProperty fromName(String columnName) {
            return Airport_Table.getProperty(columnName);
        }
    };
    public static final FloatProperty UTC_OFFSET_HOURS = new FloatProperty((Class<? extends Model>) Airport.class, "UTC_OFFSET_HOURS");

    public static final IProperty[] getAllColumnProperties() {
        return new IProperty[]{ID, NAME, CITY, CITY_CODE, COUNTRY, IATA, ICAO, LATITUDE, LONGITUDE, UTC_OFFSET_HOURS};
    }

    public static BaseProperty getProperty(String columnName) {
        String columnName2 = QueryBuilder.quoteIfNeeded(columnName);
        char c = 65535;
        switch (columnName2.hashCode()) {
            case -1523554142:
                if (columnName2.equals("`UTC_OFFSET_HOURS`")) {
                    c = 9;
                    break;
                }
                break;
            case -1482434571:
                if (columnName2.equals("`CITY`")) {
                    c = 2;
                    break;
                }
                break;
            case -1477132517:
                if (columnName2.equals("`IATA`")) {
                    c = 5;
                    break;
                }
                break;
            case -1477090760:
                if (columnName2.equals("`ICAO`")) {
                    c = 6;
                    break;
                }
                break;
            case -1472521515:
                if (columnName2.equals("`NAME`")) {
                    c = 1;
                    break;
                }
                break;
            case -1306317814:
                if (columnName2.equals("`COUNTRY`")) {
                    c = 4;
                    break;
                }
                break;
            case -588489452:
                if (columnName2.equals("`LATITUDE`")) {
                    c = 7;
                    break;
                }
                break;
            case -260169455:
                if (columnName2.equals("`LONGITUDE`")) {
                    c = 8;
                    break;
                }
                break;
            case 2932293:
                if (columnName2.equals("`ID`")) {
                    c = 0;
                    break;
                }
                break;
            case 744364191:
                if (columnName2.equals("`CITY_CODE`")) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return ID;
            case 1:
                return NAME;
            case 2:
                return CITY;
            case 3:
                return CITY_CODE;
            case 4:
                return COUNTRY;
            case 5:
                return IATA;
            case 6:
                return ICAO;
            case 7:
                return LATITUDE;
            case 8:
                return LONGITUDE;
            case 9:
                return UTC_OFFSET_HOURS;
            default:
                throw new IllegalArgumentException("Invalid column name passed. Ensure you are calling the correct table's column");
        }
    }
}
