package com.inteliclinic.neuroon.models.data;

import com.raizlabs.android.dbflow.data.Blob;
import com.raizlabs.android.dbflow.runtime.BaseContentProvider;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.language.property.BaseProperty;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.sql.language.property.IntProperty;
import com.raizlabs.android.dbflow.sql.language.property.LongProperty;
import com.raizlabs.android.dbflow.sql.language.property.Property;
import com.raizlabs.android.dbflow.structure.Model;
import java.util.Date;

public final class Sleep_Table {
    public static final Property<int[]> ACCELEREMETER_ARRAY = new Property<>((Class<? extends Model>) Sleep.class, "ACCELEREMETER_ARRAY");
    public static final Property<int[]> ACCELEREMETER_STD_ARRAY = new Property<>((Class<? extends Model>) Sleep.class, "ACCELEREMETER_STD_ARRAY");
    public static final Property<int[]> ALPHA_HIGHER_ARRAY = new Property<>((Class<? extends Model>) Sleep.class, "ALPHA_HIGHER_ARRAY");
    public static final Property<int[]> ALPHA_LOWER_ARRAY = new Property<>((Class<? extends Model>) Sleep.class, "ALPHA_LOWER_ARRAY");
    public static final IntProperty AWAKENINGS = new IntProperty((Class<? extends Model>) Sleep.class, "AWAKENINGS");
    public static final IntProperty AWAKE_DURATION = new IntProperty((Class<? extends Model>) Sleep.class, "AWAKE_DURATION");
    public static final IntProperty AWAKE_PULSE_AVERAGE = new IntProperty((Class<? extends Model>) Sleep.class, "AWAKE_PULSE_AVERAGE");
    public static final Property<int[]> BETA_HIGHER_ARRAY = new Property<>((Class<? extends Model>) Sleep.class, "BETA_HIGHER_ARRAY");
    public static final Property<int[]> BETA_LOWER_ARRAY = new Property<>((Class<? extends Model>) Sleep.class, "BETA_LOWER_ARRAY");
    public static final LongProperty COMPUTING_APP_CONFIG = new LongProperty((Class<? extends Model>) Sleep.class, "COMPUTING_APP_CONFIG");
    public static final IntProperty DEEP_DURATION = new IntProperty((Class<? extends Model>) Sleep.class, "DEEP_DURATION");
    public static final Property<int[]> DELTA_HIGHER_ARRAY = new Property<>((Class<? extends Model>) Sleep.class, "DELTA_HIGHER_ARRAY");
    public static final Property<int[]> DELTA_LOWER_ARRAY = new Property<>((Class<? extends Model>) Sleep.class, "DELTA_LOWER_ARRAY");
    public static final LongProperty DOWNLOAD_APP_VERSION = new LongProperty((Class<? extends Model>) Sleep.class, "DOWNLOAD_APP_VERSION");
    public static final Property<Date> END_DATE = new Property<>((Class<? extends Model>) Sleep.class, "END_DATE");
    public static final IntProperty FALLING_ASLEEP = new IntProperty((Class<? extends Model>) Sleep.class, "FALLING_ASLEEP");
    public static final IntProperty HIGHEST_PULSE = new IntProperty((Class<? extends Model>) Sleep.class, "HIGHEST_PULSE");
    public static final Property<double[][]> HYPNOGRAM = new Property<>((Class<? extends Model>) Sleep.class, "HYPNOGRAM");
    public static final LongProperty ID = new LongProperty((Class<? extends Model>) Sleep.class, "ID");
    public static final IntProperty LIGHT_DURATION = new IntProperty((Class<? extends Model>) Sleep.class, "LIGHT_DURATION");
    public static final IntProperty LOWEST_PULSE = new IntProperty((Class<? extends Model>) Sleep.class, "LOWEST_PULSE");
    public static final Property<int[]> POWER_ARRAY = new Property<>((Class<? extends Model>) Sleep.class, "POWER_ARRAY");
    public static final Property<int[]> POWER_NO_DELTA_ARRAY = new Property<>((Class<? extends Model>) Sleep.class, "POWER_NO_DELTA_ARRAY");
    public static final BaseContentProvider.PropertyConverter PROPERTY_CONVERTER = new BaseContentProvider.PropertyConverter() {
        public IProperty fromName(String columnName) {
            return Sleep_Table.getProperty(columnName);
        }
    };
    public static final Property<int[]> PULSE_ARRAY = new Property<>((Class<? extends Model>) Sleep.class, "PULSE_ARRAY");
    public static final Property<int[]> PULSE_STD_ARRAY = new Property<>((Class<? extends Model>) Sleep.class, "PULSE_STD_ARRAY");
    public static final Property<Blob> RAW_DATA = new Property<>((Class<? extends Model>) Sleep.class, "RAW_DATA");
    public static final IntProperty REM_DURATION = new IntProperty((Class<? extends Model>) Sleep.class, "REM_DURATION");
    public static final LongProperty SERVER_ID = new LongProperty((Class<? extends Model>) Sleep.class, "SERVER_ID");
    public static final Property<int[]> SIGNAL_QUALITY_ARRAY = new Property<>((Class<? extends Model>) Sleep.class, "SIGNAL_QUALITY_ARRAY");
    public static final IntProperty SLEEP_PULSE_AVERAGE = new IntProperty((Class<? extends Model>) Sleep.class, "SLEEP_PULSE_AVERAGE");
    public static final IntProperty SLEEP_SCORE = new IntProperty((Class<? extends Model>) Sleep.class, "SLEEP_SCORE");
    public static final IntProperty SLEEP_TYPE = new IntProperty((Class<? extends Model>) Sleep.class, "SLEEP_TYPE");
    public static final Property<int[]> SPINDLES_ARRAY = new Property<>((Class<? extends Model>) Sleep.class, "SPINDLES_ARRAY");
    public static final Property<int[]> SPINDLES_STD_ARRAY = new Property<>((Class<? extends Model>) Sleep.class, "SPINDLES_STD_ARRAY");
    public static final Property<Date> START_DATE = new Property<>((Class<? extends Model>) Sleep.class, "START_DATE");
    public static final IntProperty SYNC_STATE = new IntProperty((Class<? extends Model>) Sleep.class, "SYNC_STATE");
    public static final Property<int[]> TEMPERATURE_ARRAY = new Property<>((Class<? extends Model>) Sleep.class, "TEMPERATURE_ARRAY");
    public static final Property<int[]> THETA_ARRAY = new Property<>((Class<? extends Model>) Sleep.class, "THETA_ARRAY");
    public static final IntProperty TIME_FALL_ASLEEP = new IntProperty((Class<? extends Model>) Sleep.class, "TIME_FALL_ASLEEP");
    public static final Property<Boolean> TO_DELETE = new Property<>((Class<? extends Model>) Sleep.class, "TO_DELETE");
    public static final LongProperty USER_ID = new LongProperty((Class<? extends Model>) Sleep.class, "USER_ID");

    public static final IProperty[] getAllColumnProperties() {
        return new IProperty[]{ID, USER_ID, RAW_DATA, SLEEP_SCORE, START_DATE, END_DATE, SLEEP_TYPE, TIME_FALL_ASLEEP, REM_DURATION, LIGHT_DURATION, DEEP_DURATION, AWAKE_DURATION, AWAKENINGS, HYPNOGRAM, COMPUTING_APP_CONFIG, DOWNLOAD_APP_VERSION, SLEEP_PULSE_AVERAGE, AWAKE_PULSE_AVERAGE, HIGHEST_PULSE, LOWEST_PULSE, FALLING_ASLEEP, PULSE_ARRAY, PULSE_STD_ARRAY, ACCELEREMETER_ARRAY, ACCELEREMETER_STD_ARRAY, DELTA_LOWER_ARRAY, DELTA_HIGHER_ARRAY, THETA_ARRAY, ALPHA_LOWER_ARRAY, ALPHA_HIGHER_ARRAY, SPINDLES_ARRAY, SPINDLES_STD_ARRAY, SIGNAL_QUALITY_ARRAY, BETA_LOWER_ARRAY, BETA_HIGHER_ARRAY, POWER_ARRAY, POWER_NO_DELTA_ARRAY, TEMPERATURE_ARRAY, TO_DELETE, SYNC_STATE, SERVER_ID};
    }

    public static BaseProperty getProperty(String columnName) {
        String columnName2 = QueryBuilder.quoteIfNeeded(columnName);
        char c = 65535;
        switch (columnName2.hashCode()) {
            case -2147312355:
                if (columnName2.equals("`DOWNLOAD_APP_VERSION`")) {
                    c = 15;
                    break;
                }
                break;
            case -2122709479:
                if (columnName2.equals("`DEEP_DURATION`")) {
                    c = 10;
                    break;
                }
                break;
            case -2048189431:
                if (columnName2.equals("`SERVER_ID`")) {
                    c = '(';
                    break;
                }
                break;
            case -1790024858:
                if (columnName2.equals("`AWAKENINGS`")) {
                    c = 12;
                    break;
                }
                break;
            case -1692200170:
                if (columnName2.equals("`SLEEP_SCORE`")) {
                    c = 3;
                    break;
                }
                break;
            case -1596688706:
                if (columnName2.equals("`SIGNAL_QUALITY_ARRAY`")) {
                    c = ' ';
                    break;
                }
                break;
            case -1494901805:
                if (columnName2.equals("`SYNC_STATE`")) {
                    c = '\'';
                    break;
                }
                break;
            case -1446951696:
                if (columnName2.equals("`DELTA_HIGHER_ARRAY`")) {
                    c = 26;
                    break;
                }
                break;
            case -1438658860:
                if (columnName2.equals("`LOWEST_PULSE`")) {
                    c = 19;
                    break;
                }
                break;
            case -1378236974:
                if (columnName2.equals("`POWER_NO_DELTA_ARRAY`")) {
                    c = '$';
                    break;
                }
                break;
            case -1199509359:
                if (columnName2.equals("`USER_ID`")) {
                    c = 1;
                    break;
                }
                break;
            case -1005869853:
                if (columnName2.equals("`LIGHT_DURATION`")) {
                    c = 9;
                    break;
                }
                break;
            case -916923896:
                if (columnName2.equals("`BETA_HIGHER_ARRAY`")) {
                    c = '\"';
                    break;
                }
                break;
            case -886041976:
                if (columnName2.equals("`THETA_ARRAY`")) {
                    c = 27;
                    break;
                }
                break;
            case -800158743:
                if (columnName2.equals("`PULSE_STD_ARRAY`")) {
                    c = 22;
                    break;
                }
                break;
            case -759182671:
                if (columnName2.equals("`TO_DELETE`")) {
                    c = '&';
                    break;
                }
                break;
            case -745744258:
                if (columnName2.equals("`SLEEP_TYPE`")) {
                    c = 6;
                    break;
                }
                break;
            case -512919705:
                if (columnName2.equals("`REM_DURATION`")) {
                    c = 8;
                    break;
                }
                break;
            case -349628648:
                if (columnName2.equals("`TIME_FALL_ASLEEP`")) {
                    c = 7;
                    break;
                }
                break;
            case -340184690:
                if (columnName2.equals("`END_DATE`")) {
                    c = 5;
                    break;
                }
                break;
            case -256957821:
                if (columnName2.equals("`AWAKE_PULSE_AVERAGE`")) {
                    c = 17;
                    break;
                }
                break;
            case -179770067:
                if (columnName2.equals("`PULSE_ARRAY`")) {
                    c = 21;
                    break;
                }
                break;
            case -175722996:
                if (columnName2.equals("`DELTA_LOWER_ARRAY`")) {
                    c = 25;
                    break;
                }
                break;
            case 2932293:
                if (columnName2.equals("`ID`")) {
                    c = 0;
                    break;
                }
                break;
            case 140208786:
                if (columnName2.equals("`AWAKE_DURATION`")) {
                    c = 11;
                    break;
                }
                break;
            case 266711794:
                if (columnName2.equals("`TEMPERATURE_ARRAY`")) {
                    c = '%';
                    break;
                }
                break;
            case 433713823:
                if (columnName2.equals("`RAW_DATA`")) {
                    c = 2;
                    break;
                }
                break;
            case 566611521:
                if (columnName2.equals("`POWER_ARRAY`")) {
                    c = '#';
                    break;
                }
                break;
            case 690882002:
                if (columnName2.equals("`FALLING_ASLEEP`")) {
                    c = 20;
                    break;
                }
                break;
            case 739533845:
                if (columnName2.equals("`COMPUTING_APP_CONFIG`")) {
                    c = 14;
                    break;
                }
                break;
            case 745838118:
                if (columnName2.equals("`ALPHA_LOWER_ARRAY`")) {
                    c = 28;
                    break;
                }
                break;
            case 795295434:
                if (columnName2.equals("`SPINDLES_STD_ARRAY`")) {
                    c = 31;
                    break;
                }
                break;
            case 824896873:
                if (columnName2.equals("`ACCELEREMETER_STD_ARRAY`")) {
                    c = 24;
                    break;
                }
                break;
            case 899098089:
                if (columnName2.equals("`HYPNOGRAM`")) {
                    c = 13;
                    break;
                }
                break;
            case 949753332:
                if (columnName2.equals("`BETA_LOWER_ARRAY`")) {
                    c = '!';
                    break;
                }
                break;
            case 1038256085:
                if (columnName2.equals("`START_DATE`")) {
                    c = 4;
                    break;
                }
                break;
            case 1129242353:
                if (columnName2.equals("`SLEEP_PULSE_AVERAGE`")) {
                    c = 16;
                    break;
                }
                break;
            case 1251085997:
                if (columnName2.equals("`ACCELEREMETER_ARRAY`")) {
                    c = 23;
                    break;
                }
                break;
            case 1351639062:
                if (columnName2.equals("`ALPHA_HIGHER_ARRAY`")) {
                    c = 29;
                    break;
                }
                break;
            case 1832913026:
                if (columnName2.equals("`HIGHEST_PULSE`")) {
                    c = 18;
                    break;
                }
                break;
            case 1973958798:
                if (columnName2.equals("`SPINDLES_ARRAY`")) {
                    c = 30;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return ID;
            case 1:
                return USER_ID;
            case 2:
                return RAW_DATA;
            case 3:
                return SLEEP_SCORE;
            case 4:
                return START_DATE;
            case 5:
                return END_DATE;
            case 6:
                return SLEEP_TYPE;
            case 7:
                return TIME_FALL_ASLEEP;
            case 8:
                return REM_DURATION;
            case 9:
                return LIGHT_DURATION;
            case 10:
                return DEEP_DURATION;
            case 11:
                return AWAKE_DURATION;
            case 12:
                return AWAKENINGS;
            case 13:
                return HYPNOGRAM;
            case 14:
                return COMPUTING_APP_CONFIG;
            case 15:
                return DOWNLOAD_APP_VERSION;
            case 16:
                return SLEEP_PULSE_AVERAGE;
            case 17:
                return AWAKE_PULSE_AVERAGE;
            case 18:
                return HIGHEST_PULSE;
            case 19:
                return LOWEST_PULSE;
            case 20:
                return FALLING_ASLEEP;
            case 21:
                return PULSE_ARRAY;
            case 22:
                return PULSE_STD_ARRAY;
            case 23:
                return ACCELEREMETER_ARRAY;
            case 24:
                return ACCELEREMETER_STD_ARRAY;
            case 25:
                return DELTA_LOWER_ARRAY;
            case 26:
                return DELTA_HIGHER_ARRAY;
            case 27:
                return THETA_ARRAY;
            case 28:
                return ALPHA_LOWER_ARRAY;
            case 29:
                return ALPHA_HIGHER_ARRAY;
            case 30:
                return SPINDLES_ARRAY;
            case 31:
                return SPINDLES_STD_ARRAY;
            case ' ':
                return SIGNAL_QUALITY_ARRAY;
            case '!':
                return BETA_LOWER_ARRAY;
            case '\"':
                return BETA_HIGHER_ARRAY;
            case '#':
                return POWER_ARRAY;
            case '$':
                return POWER_NO_DELTA_ARRAY;
            case '%':
                return TEMPERATURE_ARRAY;
            case '&':
                return TO_DELETE;
            case '\'':
                return SYNC_STATE;
            case '(':
                return SERVER_ID;
            default:
                throw new IllegalArgumentException("Invalid column name passed. Ensure you are calling the correct table's column");
        }
    }
}
