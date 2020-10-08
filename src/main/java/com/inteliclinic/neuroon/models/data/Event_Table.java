package com.inteliclinic.neuroon.models.data;

import com.inteliclinic.neuroon.models.data.Event;
import com.raizlabs.android.dbflow.runtime.BaseContentProvider;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.language.property.BaseProperty;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.sql.language.property.LongProperty;
import com.raizlabs.android.dbflow.sql.language.property.Property;
import com.raizlabs.android.dbflow.structure.Model;
import java.util.Date;

public final class Event_Table {
    public static final Property<Boolean> COMPLETED = new Property<>((Class<? extends Model>) Event.class, "COMPLETED");
    public static final Property<Date> END_DATE = new Property<>((Class<? extends Model>) Event.class, "END_DATE");
    public static final LongProperty ID = new LongProperty((Class<? extends Model>) Event.class, "ID");
    public static final BaseContentProvider.PropertyConverter PROPERTY_CONVERTER = new BaseContentProvider.PropertyConverter() {
        public IProperty fromName(String columnName) {
            return Event_Table.getProperty(columnName);
        }
    };
    public static final Property<Date> START_DATE = new Property<>((Class<? extends Model>) Event.class, "START_DATE");
    public static final Property<Event.EventType> TYPE = new Property<>((Class<? extends Model>) Event.class, "TYPE");

    public static final IProperty[] getAllColumnProperties() {
        return new IProperty[]{ID, START_DATE, END_DATE, TYPE, COMPLETED};
    }

    public static BaseProperty getProperty(String columnName) {
        String columnName2 = QueryBuilder.quoteIfNeeded(columnName);
        char c = 65535;
        switch (columnName2.hashCode()) {
            case -1466262522:
                if (columnName2.equals("`TYPE`")) {
                    c = 3;
                    break;
                }
                break;
            case -764753931:
                if (columnName2.equals("`COMPLETED`")) {
                    c = 4;
                    break;
                }
                break;
            case -340184690:
                if (columnName2.equals("`END_DATE`")) {
                    c = 2;
                    break;
                }
                break;
            case 2932293:
                if (columnName2.equals("`ID`")) {
                    c = 0;
                    break;
                }
                break;
            case 1038256085:
                if (columnName2.equals("`START_DATE`")) {
                    c = 1;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return ID;
            case 1:
                return START_DATE;
            case 2:
                return END_DATE;
            case 3:
                return TYPE;
            case 4:
                return COMPLETED;
            default:
                throw new IllegalArgumentException("Invalid column name passed. Ensure you are calling the correct table's column");
        }
    }
}
