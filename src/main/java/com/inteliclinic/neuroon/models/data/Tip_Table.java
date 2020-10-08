package com.inteliclinic.neuroon.models.data;

import com.raizlabs.android.dbflow.runtime.BaseContentProvider;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.language.property.BaseProperty;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.sql.language.property.IntProperty;
import com.raizlabs.android.dbflow.sql.language.property.LongProperty;
import com.raizlabs.android.dbflow.sql.language.property.Property;
import com.raizlabs.android.dbflow.structure.Model;
import java.util.Date;

public final class Tip_Table {
    public static final Property<String> CONTENT = new Property<>((Class<? extends Model>) Tip.class, "CONTENT");
    public static final IntProperty FREQUENCY = new IntProperty((Class<? extends Model>) Tip.class, "FREQUENCY");
    public static final LongProperty ID = new LongProperty((Class<? extends Model>) Tip.class, "ID");
    public static final Property<Date> LAST_USE_DATE = new Property<>((Class<? extends Model>) Tip.class, "LAST_USE_DATE");
    public static final Property<String> LINK = new Property<>((Class<? extends Model>) Tip.class, "LINK");
    public static final Property<String> PARAMETERS = new Property<>((Class<? extends Model>) Tip.class, "PARAMETERS");
    public static final BaseContentProvider.PropertyConverter PROPERTY_CONVERTER = new BaseContentProvider.PropertyConverter() {
        public IProperty fromName(String columnName) {
            return Tip_Table.getProperty(columnName);
        }
    };
    public static final IntProperty SERVER_ID = new IntProperty((Class<? extends Model>) Tip.class, "SERVER_ID");
    public static final Property<String> TAGS = new Property<>((Class<? extends Model>) Tip.class, "TAGS");
    public static final Property<String> TITLE = new Property<>((Class<? extends Model>) Tip.class, "TITLE");
    public static final IntProperty VERSION = new IntProperty((Class<? extends Model>) Tip.class, "VERSION");

    public static final IProperty[] getAllColumnProperties() {
        return new IProperty[]{ID, TITLE, CONTENT, LINK, TAGS, PARAMETERS, LAST_USE_DATE, FREQUENCY, VERSION, SERVER_ID};
    }

    public static BaseProperty getProperty(String columnName) {
        String columnName2 = QueryBuilder.quoteIfNeeded(columnName);
        char c = 65535;
        switch (columnName2.hashCode()) {
            case -2048189431:
                if (columnName2.equals("`SERVER_ID`")) {
                    c = 9;
                    break;
                }
                break;
            case -1501631609:
                if (columnName2.equals("`CONTENT`")) {
                    c = 2;
                    break;
                }
                break;
            case -1474129082:
                if (columnName2.equals("`LINK`")) {
                    c = 3;
                    break;
                }
                break;
            case -1466985721:
                if (columnName2.equals("`TAGS`")) {
                    c = 4;
                    break;
                }
                break;
            case -1299049642:
                if (columnName2.equals("`PARAMETERS`")) {
                    c = 5;
                    break;
                }
                break;
            case 2932293:
                if (columnName2.equals("`ID`")) {
                    c = 0;
                    break;
                }
                break;
            case 554331025:
                if (columnName2.equals("`LAST_USE_DATE`")) {
                    c = 6;
                    break;
                }
                break;
            case 1375604488:
                if (columnName2.equals("`VERSION`")) {
                    c = 8;
                    break;
                }
                break;
            case 1775850888:
                if (columnName2.equals("`TITLE`")) {
                    c = 1;
                    break;
                }
                break;
            case 1900681988:
                if (columnName2.equals("`FREQUENCY`")) {
                    c = 7;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return ID;
            case 1:
                return TITLE;
            case 2:
                return CONTENT;
            case 3:
                return LINK;
            case 4:
                return TAGS;
            case 5:
                return PARAMETERS;
            case 6:
                return LAST_USE_DATE;
            case 7:
                return FREQUENCY;
            case 8:
                return VERSION;
            case 9:
                return SERVER_ID;
            default:
                throw new IllegalArgumentException("Invalid column name passed. Ensure you are calling the correct table's column");
        }
    }
}
