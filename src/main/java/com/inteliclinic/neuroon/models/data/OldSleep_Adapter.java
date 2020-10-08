package com.inteliclinic.neuroon.models.data;

import android.content.ContentValues;
import android.database.Cursor;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.DatabaseHolder;
import com.raizlabs.android.dbflow.converter.DateConverter;
import com.raizlabs.android.dbflow.data.Blob;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.Method;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.sql.language.property.BaseProperty;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseStatement;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import java.util.Date;

public final class OldSleep_Adapter extends ModelAdapter<OldSleep> {
    private final DateConverter global_typeConverterDateConverter;

    public OldSleep_Adapter(DatabaseHolder holder, DatabaseDefinition databaseDefinition) {
        super(databaseDefinition);
        this.global_typeConverterDateConverter = (DateConverter) holder.getTypeConverterForClass(Date.class);
    }

    public final Class<OldSleep> getModelClass() {
        return OldSleep.class;
    }

    public final String getTableName() {
        return "`SLEEP`";
    }

    public final void updateAutoIncrement(OldSleep model, Number id) {
        model.id = id.longValue();
    }

    public final Number getAutoIncrementingId(OldSleep model) {
        return Long.valueOf(model.id);
    }

    public final String getAutoIncrementingColumnName() {
        return "ID";
    }

    public final IProperty[] getAllColumnProperties() {
        return OldSleep_Table.getAllColumnProperties();
    }

    public final void bindToInsertValues(ContentValues values, OldSleep model) {
        byte[] refrawData;
        Long refstartDate;
        Long refendDate = null;
        if (model.rawData != null) {
            refrawData = model.rawData.getBlob();
        } else {
            refrawData = null;
        }
        if (refrawData != null) {
            values.put(OldSleep_Table.RAW_DATA.getCursorKey(), refrawData);
        } else {
            values.putNull(OldSleep_Table.RAW_DATA.getCursorKey());
        }
        values.put(OldSleep_Table.SLEEP_SCORE.getCursorKey(), Integer.valueOf(model.sleepScore));
        if (model.startDate != null) {
            refstartDate = this.global_typeConverterDateConverter.getDBValue(model.startDate);
        } else {
            refstartDate = null;
        }
        if (refstartDate != null) {
            values.put(OldSleep_Table.START_DATE.getCursorKey(), refstartDate);
        } else {
            values.putNull(OldSleep_Table.START_DATE.getCursorKey());
        }
        if (model.endDate != null) {
            refendDate = this.global_typeConverterDateConverter.getDBValue(model.endDate);
        }
        if (refendDate != null) {
            values.put(OldSleep_Table.END_DATE.getCursorKey(), refendDate);
        } else {
            values.putNull(OldSleep_Table.END_DATE.getCursorKey());
        }
        values.put(OldSleep_Table.SLEEP_TYPE.getCursorKey(), Integer.valueOf(model.sleepType));
        values.put(OldSleep_Table.TIME_FALL_ASLEEP.getCursorKey(), Integer.valueOf(model.timeFallAsleep));
        values.put(OldSleep_Table.REM_DURATION.getCursorKey(), Integer.valueOf(model.remDuration));
        values.put(OldSleep_Table.LIGHT_DURATION.getCursorKey(), Integer.valueOf(model.lightDuration));
        values.put(OldSleep_Table.DEEP_DURATION.getCursorKey(), Integer.valueOf(model.deepDuration));
        values.put(OldSleep_Table.AWAKE_DURATION.getCursorKey(), Integer.valueOf(model.awakeDuration));
        values.put(OldSleep_Table.AWAKENINGS.getCursorKey(), Integer.valueOf(model.awakenings));
        if (model.hypnogram != null) {
            values.put(OldSleep_Table.HYPNOGRAM.getCursorKey(), model.hypnogram);
        } else {
            values.putNull(OldSleep_Table.HYPNOGRAM.getCursorKey());
        }
        values.put(OldSleep_Table.SLEEP_PULSE_AVERAGE.getCursorKey(), Integer.valueOf(model.sleepPulseAverage));
        values.put(OldSleep_Table.AWAKE_PULSE_AVERAGE.getCursorKey(), Integer.valueOf(model.awakePulseAverage));
        values.put(OldSleep_Table.HIGHEST_PULSE.getCursorKey(), Integer.valueOf(model.highestPulse));
        values.put(OldSleep_Table.LOWEST_PULSE.getCursorKey(), Integer.valueOf(model.lowestPulse));
        values.put(OldSleep_Table.FALLING_ASLEEP.getCursorKey(), Integer.valueOf(model.fallingAsleep));
        if (model.pulseArray != null) {
            values.put(OldSleep_Table.PULSE_ARRAY.getCursorKey(), model.pulseArray);
        } else {
            values.putNull(OldSleep_Table.PULSE_ARRAY.getCursorKey());
        }
        if (model.pulseStdArray != null) {
            values.put(OldSleep_Table.PULSE_STD_ARRAY.getCursorKey(), model.pulseStdArray);
        } else {
            values.putNull(OldSleep_Table.PULSE_STD_ARRAY.getCursorKey());
        }
        if (model.acceleremeterArray != null) {
            values.put(OldSleep_Table.ACCELEREMETER_ARRAY.getCursorKey(), model.acceleremeterArray);
        } else {
            values.putNull(OldSleep_Table.ACCELEREMETER_ARRAY.getCursorKey());
        }
        if (model.acceleremeterStdArray != null) {
            values.put(OldSleep_Table.ACCELEREMETER_STD_ARRAY.getCursorKey(), model.acceleremeterStdArray);
        } else {
            values.putNull(OldSleep_Table.ACCELEREMETER_STD_ARRAY.getCursorKey());
        }
        if (model.deltaLowerArray != null) {
            values.put(OldSleep_Table.DELTA_LOWER_ARRAY.getCursorKey(), model.deltaLowerArray);
        } else {
            values.putNull(OldSleep_Table.DELTA_LOWER_ARRAY.getCursorKey());
        }
        if (model.deltaHigherArray != null) {
            values.put(OldSleep_Table.DELTA_HIGHER_ARRAY.getCursorKey(), model.deltaHigherArray);
        } else {
            values.putNull(OldSleep_Table.DELTA_HIGHER_ARRAY.getCursorKey());
        }
        if (model.thetaArray != null) {
            values.put(OldSleep_Table.THETA_ARRAY.getCursorKey(), model.thetaArray);
        } else {
            values.putNull(OldSleep_Table.THETA_ARRAY.getCursorKey());
        }
        if (model.alphaLowerArray != null) {
            values.put(OldSleep_Table.ALPHA_LOWER_ARRAY.getCursorKey(), model.alphaLowerArray);
        } else {
            values.putNull(OldSleep_Table.ALPHA_LOWER_ARRAY.getCursorKey());
        }
        if (model.alphaHigherArray != null) {
            values.put(OldSleep_Table.ALPHA_HIGHER_ARRAY.getCursorKey(), model.alphaHigherArray);
        } else {
            values.putNull(OldSleep_Table.ALPHA_HIGHER_ARRAY.getCursorKey());
        }
        if (model.spindlesArray != null) {
            values.put(OldSleep_Table.SPINDLES_ARRAY.getCursorKey(), model.spindlesArray);
        } else {
            values.putNull(OldSleep_Table.SPINDLES_ARRAY.getCursorKey());
        }
        if (model.spindlesStdArray != null) {
            values.put(OldSleep_Table.SPINDLES_STD_ARRAY.getCursorKey(), model.spindlesStdArray);
        } else {
            values.putNull(OldSleep_Table.SPINDLES_STD_ARRAY.getCursorKey());
        }
        if (model.signalQualityArray != null) {
            values.put(OldSleep_Table.SIGNAL_QUALITY_ARRAY.getCursorKey(), model.signalQualityArray);
        } else {
            values.putNull(OldSleep_Table.SIGNAL_QUALITY_ARRAY.getCursorKey());
        }
        if (model.betaLowerArray != null) {
            values.put(OldSleep_Table.BETA_LOWER_ARRAY.getCursorKey(), model.betaLowerArray);
        } else {
            values.putNull(OldSleep_Table.BETA_LOWER_ARRAY.getCursorKey());
        }
        if (model.betaHigherArray != null) {
            values.put(OldSleep_Table.BETA_HIGHER_ARRAY.getCursorKey(), model.betaHigherArray);
        } else {
            values.putNull(OldSleep_Table.BETA_HIGHER_ARRAY.getCursorKey());
        }
        if (model.powerArray != null) {
            values.put(OldSleep_Table.POWER_ARRAY.getCursorKey(), model.powerArray);
        } else {
            values.putNull(OldSleep_Table.POWER_ARRAY.getCursorKey());
        }
        if (model.powerNoDeltaArray != null) {
            values.put(OldSleep_Table.POWER_NO_DELTA_ARRAY.getCursorKey(), model.powerNoDeltaArray);
        } else {
            values.putNull(OldSleep_Table.POWER_NO_DELTA_ARRAY.getCursorKey());
        }
        if (model.temperatureArray != null) {
            values.put(OldSleep_Table.TEMPERATURE_ARRAY.getCursorKey(), model.temperatureArray);
        } else {
            values.putNull(OldSleep_Table.TEMPERATURE_ARRAY.getCursorKey());
        }
        values.put(OldSleep_Table.TO_DELETE.getCursorKey(), Integer.valueOf(model.toDelete ? 1 : 0));
    }

    public final void bindToContentValues(ContentValues values, OldSleep model) {
        values.put(OldSleep_Table.ID.getCursorKey(), Long.valueOf(model.id));
        bindToInsertValues(values, model);
    }

    public final void bindToInsertStatement(DatabaseStatement statement, OldSleep model, int start) {
        byte[] refrawData;
        Long refstartDate;
        Long refendDate = null;
        if (model.rawData != null) {
            refrawData = model.rawData.getBlob();
        } else {
            refrawData = null;
        }
        if (refrawData != null) {
            statement.bindBlob(start + 1, refrawData);
        } else {
            statement.bindNull(start + 1);
        }
        statement.bindLong(start + 2, (long) model.sleepScore);
        if (model.startDate != null) {
            refstartDate = this.global_typeConverterDateConverter.getDBValue(model.startDate);
        } else {
            refstartDate = null;
        }
        if (refstartDate != null) {
            statement.bindLong(start + 3, refstartDate.longValue());
        } else {
            statement.bindNull(start + 3);
        }
        if (model.endDate != null) {
            refendDate = this.global_typeConverterDateConverter.getDBValue(model.endDate);
        }
        if (refendDate != null) {
            statement.bindLong(start + 4, refendDate.longValue());
        } else {
            statement.bindNull(start + 4);
        }
        statement.bindLong(start + 5, (long) model.sleepType);
        statement.bindLong(start + 6, (long) model.timeFallAsleep);
        statement.bindLong(start + 7, (long) model.remDuration);
        statement.bindLong(start + 8, (long) model.lightDuration);
        statement.bindLong(start + 9, (long) model.deepDuration);
        statement.bindLong(start + 10, (long) model.awakeDuration);
        statement.bindLong(start + 11, (long) model.awakenings);
        if (model.hypnogram != null) {
            statement.bindString(start + 12, model.hypnogram);
        } else {
            statement.bindNull(start + 12);
        }
        statement.bindLong(start + 13, (long) model.sleepPulseAverage);
        statement.bindLong(start + 14, (long) model.awakePulseAverage);
        statement.bindLong(start + 15, (long) model.highestPulse);
        statement.bindLong(start + 16, (long) model.lowestPulse);
        statement.bindLong(start + 17, (long) model.fallingAsleep);
        if (model.pulseArray != null) {
            statement.bindString(start + 18, model.pulseArray);
        } else {
            statement.bindNull(start + 18);
        }
        if (model.pulseStdArray != null) {
            statement.bindString(start + 19, model.pulseStdArray);
        } else {
            statement.bindNull(start + 19);
        }
        if (model.acceleremeterArray != null) {
            statement.bindString(start + 20, model.acceleremeterArray);
        } else {
            statement.bindNull(start + 20);
        }
        if (model.acceleremeterStdArray != null) {
            statement.bindString(start + 21, model.acceleremeterStdArray);
        } else {
            statement.bindNull(start + 21);
        }
        if (model.deltaLowerArray != null) {
            statement.bindString(start + 22, model.deltaLowerArray);
        } else {
            statement.bindNull(start + 22);
        }
        if (model.deltaHigherArray != null) {
            statement.bindString(start + 23, model.deltaHigherArray);
        } else {
            statement.bindNull(start + 23);
        }
        if (model.thetaArray != null) {
            statement.bindString(start + 24, model.thetaArray);
        } else {
            statement.bindNull(start + 24);
        }
        if (model.alphaLowerArray != null) {
            statement.bindString(start + 25, model.alphaLowerArray);
        } else {
            statement.bindNull(start + 25);
        }
        if (model.alphaHigherArray != null) {
            statement.bindString(start + 26, model.alphaHigherArray);
        } else {
            statement.bindNull(start + 26);
        }
        if (model.spindlesArray != null) {
            statement.bindString(start + 27, model.spindlesArray);
        } else {
            statement.bindNull(start + 27);
        }
        if (model.spindlesStdArray != null) {
            statement.bindString(start + 28, model.spindlesStdArray);
        } else {
            statement.bindNull(start + 28);
        }
        if (model.signalQualityArray != null) {
            statement.bindString(start + 29, model.signalQualityArray);
        } else {
            statement.bindNull(start + 29);
        }
        if (model.betaLowerArray != null) {
            statement.bindString(start + 30, model.betaLowerArray);
        } else {
            statement.bindNull(start + 30);
        }
        if (model.betaHigherArray != null) {
            statement.bindString(start + 31, model.betaHigherArray);
        } else {
            statement.bindNull(start + 31);
        }
        if (model.powerArray != null) {
            statement.bindString(start + 32, model.powerArray);
        } else {
            statement.bindNull(start + 32);
        }
        if (model.powerNoDeltaArray != null) {
            statement.bindString(start + 33, model.powerNoDeltaArray);
        } else {
            statement.bindNull(start + 33);
        }
        if (model.temperatureArray != null) {
            statement.bindString(start + 34, model.temperatureArray);
        } else {
            statement.bindNull(start + 34);
        }
        statement.bindLong(start + 35, model.toDelete ? 1 : 0);
    }

    public final void bindToStatement(DatabaseStatement statement, OldSleep model) {
        statement.bindLong(1, model.id);
        bindToInsertStatement(statement, model, 1);
    }

    public final String getInsertStatementQuery() {
        return "INSERT INTO `SLEEP`(`RAW_DATA`,`SLEEP_SCORE`,`START_DATE`,`END_DATE`,`SLEEP_TYPE`,`TIME_FALL_ASLEEP`,`REM_DURATION`,`LIGHT_DURATION`,`DEEP_DURATION`,`AWAKE_DURATION`,`AWAKENINGS`,`HYPNOGRAM`,`SLEEP_PULSE_AVERAGE`,`AWAKE_PULSE_AVERAGE`,`HIGHEST_PULSE`,`LOWEST_PULSE`,`FALLING_ASLEEP`,`PULSE_ARRAY`,`PULSE_STD_ARRAY`,`ACCELEREMETER_ARRAY`,`ACCELEREMETER_STD_ARRAY`,`DELTA_LOWER_ARRAY`,`DELTA_HIGHER_ARRAY`,`THETA_ARRAY`,`ALPHA_LOWER_ARRAY`,`ALPHA_HIGHER_ARRAY`,`SPINDLES_ARRAY`,`SPINDLES_STD_ARRAY`,`SIGNAL_QUALITY_ARRAY`,`BETA_LOWER_ARRAY`,`BETA_HIGHER_ARRAY`,`POWER_ARRAY`,`POWER_NO_DELTA_ARRAY`,`TEMPERATURE_ARRAY`,`TO_DELETE`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }

    public final String getCompiledStatementQuery() {
        return "INSERT INTO `SLEEP`(`ID`,`RAW_DATA`,`SLEEP_SCORE`,`START_DATE`,`END_DATE`,`SLEEP_TYPE`,`TIME_FALL_ASLEEP`,`REM_DURATION`,`LIGHT_DURATION`,`DEEP_DURATION`,`AWAKE_DURATION`,`AWAKENINGS`,`HYPNOGRAM`,`SLEEP_PULSE_AVERAGE`,`AWAKE_PULSE_AVERAGE`,`HIGHEST_PULSE`,`LOWEST_PULSE`,`FALLING_ASLEEP`,`PULSE_ARRAY`,`PULSE_STD_ARRAY`,`ACCELEREMETER_ARRAY`,`ACCELEREMETER_STD_ARRAY`,`DELTA_LOWER_ARRAY`,`DELTA_HIGHER_ARRAY`,`THETA_ARRAY`,`ALPHA_LOWER_ARRAY`,`ALPHA_HIGHER_ARRAY`,`SPINDLES_ARRAY`,`SPINDLES_STD_ARRAY`,`SIGNAL_QUALITY_ARRAY`,`BETA_LOWER_ARRAY`,`BETA_HIGHER_ARRAY`,`POWER_ARRAY`,`POWER_NO_DELTA_ARRAY`,`TEMPERATURE_ARRAY`,`TO_DELETE`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }

    public final String getCreationQuery() {
        return "CREATE TABLE IF NOT EXISTS `SLEEP`(`ID` INTEGER PRIMARY KEY AUTOINCREMENT,`RAW_DATA` BLOB,`SLEEP_SCORE` INTEGER,`START_DATE` INTEGER,`END_DATE` INTEGER,`SLEEP_TYPE` INTEGER,`TIME_FALL_ASLEEP` INTEGER,`REM_DURATION` INTEGER,`LIGHT_DURATION` INTEGER,`DEEP_DURATION` INTEGER,`AWAKE_DURATION` INTEGER,`AWAKENINGS` INTEGER,`HYPNOGRAM` TEXT,`SLEEP_PULSE_AVERAGE` INTEGER,`AWAKE_PULSE_AVERAGE` INTEGER,`HIGHEST_PULSE` INTEGER,`LOWEST_PULSE` INTEGER,`FALLING_ASLEEP` INTEGER,`PULSE_ARRAY` TEXT,`PULSE_STD_ARRAY` TEXT,`ACCELEREMETER_ARRAY` TEXT,`ACCELEREMETER_STD_ARRAY` TEXT,`DELTA_LOWER_ARRAY` TEXT,`DELTA_HIGHER_ARRAY` TEXT,`THETA_ARRAY` TEXT,`ALPHA_LOWER_ARRAY` TEXT,`ALPHA_HIGHER_ARRAY` TEXT,`SPINDLES_ARRAY` TEXT,`SPINDLES_STD_ARRAY` TEXT,`SIGNAL_QUALITY_ARRAY` TEXT,`BETA_LOWER_ARRAY` TEXT,`BETA_HIGHER_ARRAY` TEXT,`POWER_ARRAY` TEXT,`POWER_NO_DELTA_ARRAY` TEXT,`TEMPERATURE_ARRAY` TEXT,`TO_DELETE` INTEGER);";
    }

    public final void loadFromCursor(Cursor cursor, OldSleep model) {
        int indexID = cursor.getColumnIndex("ID");
        if (indexID == -1 || cursor.isNull(indexID)) {
            model.id = 0;
        } else {
            model.id = cursor.getLong(indexID);
        }
        int indexRAW_DATA = cursor.getColumnIndex("RAW_DATA");
        if (indexRAW_DATA == -1 || cursor.isNull(indexRAW_DATA)) {
            model.rawData = null;
        } else {
            model.rawData = new Blob(cursor.getBlob(indexRAW_DATA));
        }
        int indexSLEEP_SCORE = cursor.getColumnIndex("SLEEP_SCORE");
        if (indexSLEEP_SCORE == -1 || cursor.isNull(indexSLEEP_SCORE)) {
            model.sleepScore = 0;
        } else {
            model.sleepScore = cursor.getInt(indexSLEEP_SCORE);
        }
        int indexSTART_DATE = cursor.getColumnIndex("START_DATE");
        if (indexSTART_DATE == -1 || cursor.isNull(indexSTART_DATE)) {
            model.startDate = null;
        } else {
            model.startDate = this.global_typeConverterDateConverter.getModelValue(Long.valueOf(cursor.getLong(indexSTART_DATE)));
        }
        int indexEND_DATE = cursor.getColumnIndex("END_DATE");
        if (indexEND_DATE == -1 || cursor.isNull(indexEND_DATE)) {
            model.endDate = null;
        } else {
            model.endDate = this.global_typeConverterDateConverter.getModelValue(Long.valueOf(cursor.getLong(indexEND_DATE)));
        }
        int indexSLEEP_TYPE = cursor.getColumnIndex("SLEEP_TYPE");
        if (indexSLEEP_TYPE == -1 || cursor.isNull(indexSLEEP_TYPE)) {
            model.sleepType = 0;
        } else {
            model.sleepType = cursor.getInt(indexSLEEP_TYPE);
        }
        int indexTIME_FALL_ASLEEP = cursor.getColumnIndex("TIME_FALL_ASLEEP");
        if (indexTIME_FALL_ASLEEP == -1 || cursor.isNull(indexTIME_FALL_ASLEEP)) {
            model.timeFallAsleep = 0;
        } else {
            model.timeFallAsleep = cursor.getInt(indexTIME_FALL_ASLEEP);
        }
        int indexREM_DURATION = cursor.getColumnIndex("REM_DURATION");
        if (indexREM_DURATION == -1 || cursor.isNull(indexREM_DURATION)) {
            model.remDuration = 0;
        } else {
            model.remDuration = cursor.getInt(indexREM_DURATION);
        }
        int indexLIGHT_DURATION = cursor.getColumnIndex("LIGHT_DURATION");
        if (indexLIGHT_DURATION == -1 || cursor.isNull(indexLIGHT_DURATION)) {
            model.lightDuration = 0;
        } else {
            model.lightDuration = cursor.getInt(indexLIGHT_DURATION);
        }
        int indexDEEP_DURATION = cursor.getColumnIndex("DEEP_DURATION");
        if (indexDEEP_DURATION == -1 || cursor.isNull(indexDEEP_DURATION)) {
            model.deepDuration = 0;
        } else {
            model.deepDuration = cursor.getInt(indexDEEP_DURATION);
        }
        int indexAWAKE_DURATION = cursor.getColumnIndex("AWAKE_DURATION");
        if (indexAWAKE_DURATION == -1 || cursor.isNull(indexAWAKE_DURATION)) {
            model.awakeDuration = 0;
        } else {
            model.awakeDuration = cursor.getInt(indexAWAKE_DURATION);
        }
        int indexAWAKENINGS = cursor.getColumnIndex("AWAKENINGS");
        if (indexAWAKENINGS == -1 || cursor.isNull(indexAWAKENINGS)) {
            model.awakenings = 0;
        } else {
            model.awakenings = cursor.getInt(indexAWAKENINGS);
        }
        int indexHYPNOGRAM = cursor.getColumnIndex("HYPNOGRAM");
        if (indexHYPNOGRAM == -1 || cursor.isNull(indexHYPNOGRAM)) {
            model.hypnogram = null;
        } else {
            model.hypnogram = cursor.getString(indexHYPNOGRAM);
        }
        int indexSLEEP_PULSE_AVERAGE = cursor.getColumnIndex("SLEEP_PULSE_AVERAGE");
        if (indexSLEEP_PULSE_AVERAGE == -1 || cursor.isNull(indexSLEEP_PULSE_AVERAGE)) {
            model.sleepPulseAverage = 0;
        } else {
            model.sleepPulseAverage = cursor.getInt(indexSLEEP_PULSE_AVERAGE);
        }
        int indexAWAKE_PULSE_AVERAGE = cursor.getColumnIndex("AWAKE_PULSE_AVERAGE");
        if (indexAWAKE_PULSE_AVERAGE == -1 || cursor.isNull(indexAWAKE_PULSE_AVERAGE)) {
            model.awakePulseAverage = 0;
        } else {
            model.awakePulseAverage = cursor.getInt(indexAWAKE_PULSE_AVERAGE);
        }
        int indexHIGHEST_PULSE = cursor.getColumnIndex("HIGHEST_PULSE");
        if (indexHIGHEST_PULSE == -1 || cursor.isNull(indexHIGHEST_PULSE)) {
            model.highestPulse = 0;
        } else {
            model.highestPulse = cursor.getInt(indexHIGHEST_PULSE);
        }
        int indexLOWEST_PULSE = cursor.getColumnIndex("LOWEST_PULSE");
        if (indexLOWEST_PULSE == -1 || cursor.isNull(indexLOWEST_PULSE)) {
            model.lowestPulse = 0;
        } else {
            model.lowestPulse = cursor.getInt(indexLOWEST_PULSE);
        }
        int indexFALLING_ASLEEP = cursor.getColumnIndex("FALLING_ASLEEP");
        if (indexFALLING_ASLEEP == -1 || cursor.isNull(indexFALLING_ASLEEP)) {
            model.fallingAsleep = 0;
        } else {
            model.fallingAsleep = cursor.getInt(indexFALLING_ASLEEP);
        }
        int indexPULSE_ARRAY = cursor.getColumnIndex("PULSE_ARRAY");
        if (indexPULSE_ARRAY == -1 || cursor.isNull(indexPULSE_ARRAY)) {
            model.pulseArray = null;
        } else {
            model.pulseArray = cursor.getString(indexPULSE_ARRAY);
        }
        int indexPULSE_STD_ARRAY = cursor.getColumnIndex("PULSE_STD_ARRAY");
        if (indexPULSE_STD_ARRAY == -1 || cursor.isNull(indexPULSE_STD_ARRAY)) {
            model.pulseStdArray = null;
        } else {
            model.pulseStdArray = cursor.getString(indexPULSE_STD_ARRAY);
        }
        int indexACCELEREMETER_ARRAY = cursor.getColumnIndex("ACCELEREMETER_ARRAY");
        if (indexACCELEREMETER_ARRAY == -1 || cursor.isNull(indexACCELEREMETER_ARRAY)) {
            model.acceleremeterArray = null;
        } else {
            model.acceleremeterArray = cursor.getString(indexACCELEREMETER_ARRAY);
        }
        int indexACCELEREMETER_STD_ARRAY = cursor.getColumnIndex("ACCELEREMETER_STD_ARRAY");
        if (indexACCELEREMETER_STD_ARRAY == -1 || cursor.isNull(indexACCELEREMETER_STD_ARRAY)) {
            model.acceleremeterStdArray = null;
        } else {
            model.acceleremeterStdArray = cursor.getString(indexACCELEREMETER_STD_ARRAY);
        }
        int indexDELTA_LOWER_ARRAY = cursor.getColumnIndex("DELTA_LOWER_ARRAY");
        if (indexDELTA_LOWER_ARRAY == -1 || cursor.isNull(indexDELTA_LOWER_ARRAY)) {
            model.deltaLowerArray = null;
        } else {
            model.deltaLowerArray = cursor.getString(indexDELTA_LOWER_ARRAY);
        }
        int indexDELTA_HIGHER_ARRAY = cursor.getColumnIndex("DELTA_HIGHER_ARRAY");
        if (indexDELTA_HIGHER_ARRAY == -1 || cursor.isNull(indexDELTA_HIGHER_ARRAY)) {
            model.deltaHigherArray = null;
        } else {
            model.deltaHigherArray = cursor.getString(indexDELTA_HIGHER_ARRAY);
        }
        int indexTHETA_ARRAY = cursor.getColumnIndex("THETA_ARRAY");
        if (indexTHETA_ARRAY == -1 || cursor.isNull(indexTHETA_ARRAY)) {
            model.thetaArray = null;
        } else {
            model.thetaArray = cursor.getString(indexTHETA_ARRAY);
        }
        int indexALPHA_LOWER_ARRAY = cursor.getColumnIndex("ALPHA_LOWER_ARRAY");
        if (indexALPHA_LOWER_ARRAY == -1 || cursor.isNull(indexALPHA_LOWER_ARRAY)) {
            model.alphaLowerArray = null;
        } else {
            model.alphaLowerArray = cursor.getString(indexALPHA_LOWER_ARRAY);
        }
        int indexALPHA_HIGHER_ARRAY = cursor.getColumnIndex("ALPHA_HIGHER_ARRAY");
        if (indexALPHA_HIGHER_ARRAY == -1 || cursor.isNull(indexALPHA_HIGHER_ARRAY)) {
            model.alphaHigherArray = null;
        } else {
            model.alphaHigherArray = cursor.getString(indexALPHA_HIGHER_ARRAY);
        }
        int indexSPINDLES_ARRAY = cursor.getColumnIndex("SPINDLES_ARRAY");
        if (indexSPINDLES_ARRAY == -1 || cursor.isNull(indexSPINDLES_ARRAY)) {
            model.spindlesArray = null;
        } else {
            model.spindlesArray = cursor.getString(indexSPINDLES_ARRAY);
        }
        int indexSPINDLES_STD_ARRAY = cursor.getColumnIndex("SPINDLES_STD_ARRAY");
        if (indexSPINDLES_STD_ARRAY == -1 || cursor.isNull(indexSPINDLES_STD_ARRAY)) {
            model.spindlesStdArray = null;
        } else {
            model.spindlesStdArray = cursor.getString(indexSPINDLES_STD_ARRAY);
        }
        int indexSIGNAL_QUALITY_ARRAY = cursor.getColumnIndex("SIGNAL_QUALITY_ARRAY");
        if (indexSIGNAL_QUALITY_ARRAY == -1 || cursor.isNull(indexSIGNAL_QUALITY_ARRAY)) {
            model.signalQualityArray = null;
        } else {
            model.signalQualityArray = cursor.getString(indexSIGNAL_QUALITY_ARRAY);
        }
        int indexBETA_LOWER_ARRAY = cursor.getColumnIndex("BETA_LOWER_ARRAY");
        if (indexBETA_LOWER_ARRAY == -1 || cursor.isNull(indexBETA_LOWER_ARRAY)) {
            model.betaLowerArray = null;
        } else {
            model.betaLowerArray = cursor.getString(indexBETA_LOWER_ARRAY);
        }
        int indexBETA_HIGHER_ARRAY = cursor.getColumnIndex("BETA_HIGHER_ARRAY");
        if (indexBETA_HIGHER_ARRAY == -1 || cursor.isNull(indexBETA_HIGHER_ARRAY)) {
            model.betaHigherArray = null;
        } else {
            model.betaHigherArray = cursor.getString(indexBETA_HIGHER_ARRAY);
        }
        int indexPOWER_ARRAY = cursor.getColumnIndex("POWER_ARRAY");
        if (indexPOWER_ARRAY == -1 || cursor.isNull(indexPOWER_ARRAY)) {
            model.powerArray = null;
        } else {
            model.powerArray = cursor.getString(indexPOWER_ARRAY);
        }
        int indexPOWER_NO_DELTA_ARRAY = cursor.getColumnIndex("POWER_NO_DELTA_ARRAY");
        if (indexPOWER_NO_DELTA_ARRAY == -1 || cursor.isNull(indexPOWER_NO_DELTA_ARRAY)) {
            model.powerNoDeltaArray = null;
        } else {
            model.powerNoDeltaArray = cursor.getString(indexPOWER_NO_DELTA_ARRAY);
        }
        int indexTEMPERATURE_ARRAY = cursor.getColumnIndex("TEMPERATURE_ARRAY");
        if (indexTEMPERATURE_ARRAY == -1 || cursor.isNull(indexTEMPERATURE_ARRAY)) {
            model.temperatureArray = null;
        } else {
            model.temperatureArray = cursor.getString(indexTEMPERATURE_ARRAY);
        }
        int indexTO_DELETE = cursor.getColumnIndex("TO_DELETE");
        if (indexTO_DELETE == -1 || cursor.isNull(indexTO_DELETE)) {
            model.toDelete = false;
        } else {
            model.toDelete = cursor.getInt(indexTO_DELETE) == 1;
        }
    }

    public final boolean exists(OldSleep model, DatabaseWrapper wrapper) {
        if (model.id > 0) {
            if (new Select(Method.count(new IProperty[0])).from(OldSleep.class).where(getPrimaryConditionClause(model)).count(wrapper) > 0) {
                return true;
            }
        }
        return false;
    }

    public final ConditionGroup getPrimaryConditionClause(OldSleep model) {
        ConditionGroup clause = ConditionGroup.clause();
        clause.and(OldSleep_Table.ID.eq(model.id));
        return clause;
    }

    public final OldSleep newInstance() {
        return new OldSleep();
    }

    public final BaseProperty getProperty(String name) {
        return OldSleep_Table.getProperty(name);
    }
}
