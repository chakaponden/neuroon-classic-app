package com.inteliclinic.neuroon.models.data;

import android.content.ContentValues;
import android.database.Cursor;
import com.inteliclinic.neuroon.models.data.converters.HypnogramConverter;
import com.inteliclinic.neuroon.models.data.converters.IntArrayConverter;
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

public final class Sleep_Adapter extends ModelAdapter<Sleep> {
    private final DateConverter global_typeConverterDateConverter;
    private final HypnogramConverter typeConverterHypnogramConverter = new HypnogramConverter();
    private final IntArrayConverter typeConverterIntArrayConverter = new IntArrayConverter();

    public Sleep_Adapter(DatabaseHolder holder, DatabaseDefinition databaseDefinition) {
        super(databaseDefinition);
        this.global_typeConverterDateConverter = (DateConverter) holder.getTypeConverterForClass(Date.class);
    }

    public final Class<Sleep> getModelClass() {
        return Sleep.class;
    }

    public final String getTableName() {
        return "`SLEEP`";
    }

    public final void updateAutoIncrement(Sleep model, Number id) {
        model.mId = id.longValue();
    }

    public final Number getAutoIncrementingId(Sleep model) {
        return Long.valueOf(model.mId);
    }

    public final String getAutoIncrementingColumnName() {
        return "ID";
    }

    public final IProperty[] getAllColumnProperties() {
        return Sleep_Table.getAllColumnProperties();
    }

    public final void bindToInsertValues(ContentValues values, Sleep model) {
        values.put(Sleep_Table.USER_ID.getCursorKey(), Long.valueOf(model.mUserId));
        byte[] refmRawData = model.mRawData != null ? model.mRawData.getBlob() : null;
        if (refmRawData != null) {
            values.put(Sleep_Table.RAW_DATA.getCursorKey(), refmRawData);
        } else {
            values.putNull(Sleep_Table.RAW_DATA.getCursorKey());
        }
        values.put(Sleep_Table.SLEEP_SCORE.getCursorKey(), Integer.valueOf(model.mSleepScore));
        Long refmStartDate = model.mStartDate != null ? this.global_typeConverterDateConverter.getDBValue(model.mStartDate) : null;
        if (refmStartDate != null) {
            values.put(Sleep_Table.START_DATE.getCursorKey(), refmStartDate);
        } else {
            values.putNull(Sleep_Table.START_DATE.getCursorKey());
        }
        Long refmEndDate = model.mEndDate != null ? this.global_typeConverterDateConverter.getDBValue(model.mEndDate) : null;
        if (refmEndDate != null) {
            values.put(Sleep_Table.END_DATE.getCursorKey(), refmEndDate);
        } else {
            values.putNull(Sleep_Table.END_DATE.getCursorKey());
        }
        values.put(Sleep_Table.SLEEP_TYPE.getCursorKey(), Integer.valueOf(model.mSleepType));
        values.put(Sleep_Table.TIME_FALL_ASLEEP.getCursorKey(), Integer.valueOf(model.mTimeFallAsleep));
        values.put(Sleep_Table.REM_DURATION.getCursorKey(), Integer.valueOf(model.mRemDuration));
        values.put(Sleep_Table.LIGHT_DURATION.getCursorKey(), Integer.valueOf(model.mLightDuration));
        values.put(Sleep_Table.DEEP_DURATION.getCursorKey(), Integer.valueOf(model.mDeepDuration));
        values.put(Sleep_Table.AWAKE_DURATION.getCursorKey(), Integer.valueOf(model.mAwakeDuration));
        values.put(Sleep_Table.AWAKENINGS.getCursorKey(), Integer.valueOf(model.mAwakenings));
        byte[] refhypnogram = model.hypnogram != null ? this.typeConverterHypnogramConverter.getDBValue(model.hypnogram) : null;
        if (refhypnogram != null) {
            values.put(Sleep_Table.HYPNOGRAM.getCursorKey(), refhypnogram);
        } else {
            values.putNull(Sleep_Table.HYPNOGRAM.getCursorKey());
        }
        values.put(Sleep_Table.COMPUTING_APP_CONFIG.getCursorKey(), Long.valueOf(model.computingAppConfig));
        values.put(Sleep_Table.DOWNLOAD_APP_VERSION.getCursorKey(), Long.valueOf(model.downloadAppVersion));
        values.put(Sleep_Table.SLEEP_PULSE_AVERAGE.getCursorKey(), Integer.valueOf(model.sleepPulseAverage));
        values.put(Sleep_Table.AWAKE_PULSE_AVERAGE.getCursorKey(), Integer.valueOf(model.awakePulseAverage));
        values.put(Sleep_Table.HIGHEST_PULSE.getCursorKey(), Integer.valueOf(model.highestPulse));
        values.put(Sleep_Table.LOWEST_PULSE.getCursorKey(), Integer.valueOf(model.lowestPulse));
        values.put(Sleep_Table.FALLING_ASLEEP.getCursorKey(), Integer.valueOf(model.fallingAsleep));
        byte[] refmPulseArray = model.mPulseArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.mPulseArray) : null;
        if (refmPulseArray != null) {
            values.put(Sleep_Table.PULSE_ARRAY.getCursorKey(), refmPulseArray);
        } else {
            values.putNull(Sleep_Table.PULSE_ARRAY.getCursorKey());
        }
        byte[] refpulseStdArray = model.pulseStdArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.pulseStdArray) : null;
        if (refpulseStdArray != null) {
            values.put(Sleep_Table.PULSE_STD_ARRAY.getCursorKey(), refpulseStdArray);
        } else {
            values.putNull(Sleep_Table.PULSE_STD_ARRAY.getCursorKey());
        }
        byte[] refacceleremeterArray = model.acceleremeterArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.acceleremeterArray) : null;
        if (refacceleremeterArray != null) {
            values.put(Sleep_Table.ACCELEREMETER_ARRAY.getCursorKey(), refacceleremeterArray);
        } else {
            values.putNull(Sleep_Table.ACCELEREMETER_ARRAY.getCursorKey());
        }
        byte[] refacceleremeterStdArray = model.acceleremeterStdArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.acceleremeterStdArray) : null;
        if (refacceleremeterStdArray != null) {
            values.put(Sleep_Table.ACCELEREMETER_STD_ARRAY.getCursorKey(), refacceleremeterStdArray);
        } else {
            values.putNull(Sleep_Table.ACCELEREMETER_STD_ARRAY.getCursorKey());
        }
        byte[] refdeltaLowerArray = model.deltaLowerArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.deltaLowerArray) : null;
        if (refdeltaLowerArray != null) {
            values.put(Sleep_Table.DELTA_LOWER_ARRAY.getCursorKey(), refdeltaLowerArray);
        } else {
            values.putNull(Sleep_Table.DELTA_LOWER_ARRAY.getCursorKey());
        }
        byte[] refdeltaHigherArray = model.deltaHigherArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.deltaHigherArray) : null;
        if (refdeltaHigherArray != null) {
            values.put(Sleep_Table.DELTA_HIGHER_ARRAY.getCursorKey(), refdeltaHigherArray);
        } else {
            values.putNull(Sleep_Table.DELTA_HIGHER_ARRAY.getCursorKey());
        }
        byte[] refthetaArray = model.thetaArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.thetaArray) : null;
        if (refthetaArray != null) {
            values.put(Sleep_Table.THETA_ARRAY.getCursorKey(), refthetaArray);
        } else {
            values.putNull(Sleep_Table.THETA_ARRAY.getCursorKey());
        }
        byte[] refalphaLowerArray = model.alphaLowerArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.alphaLowerArray) : null;
        if (refalphaLowerArray != null) {
            values.put(Sleep_Table.ALPHA_LOWER_ARRAY.getCursorKey(), refalphaLowerArray);
        } else {
            values.putNull(Sleep_Table.ALPHA_LOWER_ARRAY.getCursorKey());
        }
        byte[] refalphaHigherArray = model.alphaHigherArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.alphaHigherArray) : null;
        if (refalphaHigherArray != null) {
            values.put(Sleep_Table.ALPHA_HIGHER_ARRAY.getCursorKey(), refalphaHigherArray);
        } else {
            values.putNull(Sleep_Table.ALPHA_HIGHER_ARRAY.getCursorKey());
        }
        byte[] refspindlesArray = model.spindlesArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.spindlesArray) : null;
        if (refspindlesArray != null) {
            values.put(Sleep_Table.SPINDLES_ARRAY.getCursorKey(), refspindlesArray);
        } else {
            values.putNull(Sleep_Table.SPINDLES_ARRAY.getCursorKey());
        }
        byte[] refspindlesStdArray = model.spindlesStdArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.spindlesStdArray) : null;
        if (refspindlesStdArray != null) {
            values.put(Sleep_Table.SPINDLES_STD_ARRAY.getCursorKey(), refspindlesStdArray);
        } else {
            values.putNull(Sleep_Table.SPINDLES_STD_ARRAY.getCursorKey());
        }
        byte[] refsignalQualityArray = model.signalQualityArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.signalQualityArray) : null;
        if (refsignalQualityArray != null) {
            values.put(Sleep_Table.SIGNAL_QUALITY_ARRAY.getCursorKey(), refsignalQualityArray);
        } else {
            values.putNull(Sleep_Table.SIGNAL_QUALITY_ARRAY.getCursorKey());
        }
        byte[] refbetaLowerArray = model.betaLowerArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.betaLowerArray) : null;
        if (refbetaLowerArray != null) {
            values.put(Sleep_Table.BETA_LOWER_ARRAY.getCursorKey(), refbetaLowerArray);
        } else {
            values.putNull(Sleep_Table.BETA_LOWER_ARRAY.getCursorKey());
        }
        byte[] refbetaHigherArray = model.betaHigherArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.betaHigherArray) : null;
        if (refbetaHigherArray != null) {
            values.put(Sleep_Table.BETA_HIGHER_ARRAY.getCursorKey(), refbetaHigherArray);
        } else {
            values.putNull(Sleep_Table.BETA_HIGHER_ARRAY.getCursorKey());
        }
        byte[] refpowerArray = model.powerArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.powerArray) : null;
        if (refpowerArray != null) {
            values.put(Sleep_Table.POWER_ARRAY.getCursorKey(), refpowerArray);
        } else {
            values.putNull(Sleep_Table.POWER_ARRAY.getCursorKey());
        }
        byte[] refpowerNoDeltaArray = model.powerNoDeltaArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.powerNoDeltaArray) : null;
        if (refpowerNoDeltaArray != null) {
            values.put(Sleep_Table.POWER_NO_DELTA_ARRAY.getCursorKey(), refpowerNoDeltaArray);
        } else {
            values.putNull(Sleep_Table.POWER_NO_DELTA_ARRAY.getCursorKey());
        }
        byte[] reftemperatureArray = model.temperatureArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.temperatureArray) : null;
        if (reftemperatureArray != null) {
            values.put(Sleep_Table.TEMPERATURE_ARRAY.getCursorKey(), reftemperatureArray);
        } else {
            values.putNull(Sleep_Table.TEMPERATURE_ARRAY.getCursorKey());
        }
        values.put(Sleep_Table.TO_DELETE.getCursorKey(), Integer.valueOf(model.toDelete ? 1 : 0));
        values.put(Sleep_Table.SYNC_STATE.getCursorKey(), Integer.valueOf(model.syncState));
        values.put(Sleep_Table.SERVER_ID.getCursorKey(), Long.valueOf(model.mServerId));
    }

    public final void bindToContentValues(ContentValues values, Sleep model) {
        values.put(Sleep_Table.ID.getCursorKey(), Long.valueOf(model.mId));
        bindToInsertValues(values, model);
    }

    public final void bindToInsertStatement(DatabaseStatement statement, Sleep model, int start) {
        statement.bindLong(start + 1, model.mUserId);
        byte[] refmRawData = model.mRawData != null ? model.mRawData.getBlob() : null;
        if (refmRawData != null) {
            statement.bindBlob(start + 2, refmRawData);
        } else {
            statement.bindNull(start + 2);
        }
        statement.bindLong(start + 3, (long) model.mSleepScore);
        Long refmStartDate = model.mStartDate != null ? this.global_typeConverterDateConverter.getDBValue(model.mStartDate) : null;
        if (refmStartDate != null) {
            statement.bindLong(start + 4, refmStartDate.longValue());
        } else {
            statement.bindNull(start + 4);
        }
        Long refmEndDate = model.mEndDate != null ? this.global_typeConverterDateConverter.getDBValue(model.mEndDate) : null;
        if (refmEndDate != null) {
            statement.bindLong(start + 5, refmEndDate.longValue());
        } else {
            statement.bindNull(start + 5);
        }
        statement.bindLong(start + 6, (long) model.mSleepType);
        statement.bindLong(start + 7, (long) model.mTimeFallAsleep);
        statement.bindLong(start + 8, (long) model.mRemDuration);
        statement.bindLong(start + 9, (long) model.mLightDuration);
        statement.bindLong(start + 10, (long) model.mDeepDuration);
        statement.bindLong(start + 11, (long) model.mAwakeDuration);
        statement.bindLong(start + 12, (long) model.mAwakenings);
        byte[] refhypnogram = model.hypnogram != null ? this.typeConverterHypnogramConverter.getDBValue(model.hypnogram) : null;
        if (refhypnogram != null) {
            statement.bindBlob(start + 13, refhypnogram);
        } else {
            statement.bindNull(start + 13);
        }
        statement.bindLong(start + 14, model.computingAppConfig);
        statement.bindLong(start + 15, model.downloadAppVersion);
        statement.bindLong(start + 16, (long) model.sleepPulseAverage);
        statement.bindLong(start + 17, (long) model.awakePulseAverage);
        statement.bindLong(start + 18, (long) model.highestPulse);
        statement.bindLong(start + 19, (long) model.lowestPulse);
        statement.bindLong(start + 20, (long) model.fallingAsleep);
        byte[] refmPulseArray = model.mPulseArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.mPulseArray) : null;
        if (refmPulseArray != null) {
            statement.bindBlob(start + 21, refmPulseArray);
        } else {
            statement.bindNull(start + 21);
        }
        byte[] refpulseStdArray = model.pulseStdArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.pulseStdArray) : null;
        if (refpulseStdArray != null) {
            statement.bindBlob(start + 22, refpulseStdArray);
        } else {
            statement.bindNull(start + 22);
        }
        byte[] refacceleremeterArray = model.acceleremeterArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.acceleremeterArray) : null;
        if (refacceleremeterArray != null) {
            statement.bindBlob(start + 23, refacceleremeterArray);
        } else {
            statement.bindNull(start + 23);
        }
        byte[] refacceleremeterStdArray = model.acceleremeterStdArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.acceleremeterStdArray) : null;
        if (refacceleremeterStdArray != null) {
            statement.bindBlob(start + 24, refacceleremeterStdArray);
        } else {
            statement.bindNull(start + 24);
        }
        byte[] refdeltaLowerArray = model.deltaLowerArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.deltaLowerArray) : null;
        if (refdeltaLowerArray != null) {
            statement.bindBlob(start + 25, refdeltaLowerArray);
        } else {
            statement.bindNull(start + 25);
        }
        byte[] refdeltaHigherArray = model.deltaHigherArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.deltaHigherArray) : null;
        if (refdeltaHigherArray != null) {
            statement.bindBlob(start + 26, refdeltaHigherArray);
        } else {
            statement.bindNull(start + 26);
        }
        byte[] refthetaArray = model.thetaArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.thetaArray) : null;
        if (refthetaArray != null) {
            statement.bindBlob(start + 27, refthetaArray);
        } else {
            statement.bindNull(start + 27);
        }
        byte[] refalphaLowerArray = model.alphaLowerArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.alphaLowerArray) : null;
        if (refalphaLowerArray != null) {
            statement.bindBlob(start + 28, refalphaLowerArray);
        } else {
            statement.bindNull(start + 28);
        }
        byte[] refalphaHigherArray = model.alphaHigherArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.alphaHigherArray) : null;
        if (refalphaHigherArray != null) {
            statement.bindBlob(start + 29, refalphaHigherArray);
        } else {
            statement.bindNull(start + 29);
        }
        byte[] refspindlesArray = model.spindlesArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.spindlesArray) : null;
        if (refspindlesArray != null) {
            statement.bindBlob(start + 30, refspindlesArray);
        } else {
            statement.bindNull(start + 30);
        }
        byte[] refspindlesStdArray = model.spindlesStdArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.spindlesStdArray) : null;
        if (refspindlesStdArray != null) {
            statement.bindBlob(start + 31, refspindlesStdArray);
        } else {
            statement.bindNull(start + 31);
        }
        byte[] refsignalQualityArray = model.signalQualityArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.signalQualityArray) : null;
        if (refsignalQualityArray != null) {
            statement.bindBlob(start + 32, refsignalQualityArray);
        } else {
            statement.bindNull(start + 32);
        }
        byte[] refbetaLowerArray = model.betaLowerArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.betaLowerArray) : null;
        if (refbetaLowerArray != null) {
            statement.bindBlob(start + 33, refbetaLowerArray);
        } else {
            statement.bindNull(start + 33);
        }
        byte[] refbetaHigherArray = model.betaHigherArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.betaHigherArray) : null;
        if (refbetaHigherArray != null) {
            statement.bindBlob(start + 34, refbetaHigherArray);
        } else {
            statement.bindNull(start + 34);
        }
        byte[] refpowerArray = model.powerArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.powerArray) : null;
        if (refpowerArray != null) {
            statement.bindBlob(start + 35, refpowerArray);
        } else {
            statement.bindNull(start + 35);
        }
        byte[] refpowerNoDeltaArray = model.powerNoDeltaArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.powerNoDeltaArray) : null;
        if (refpowerNoDeltaArray != null) {
            statement.bindBlob(start + 36, refpowerNoDeltaArray);
        } else {
            statement.bindNull(start + 36);
        }
        byte[] reftemperatureArray = model.temperatureArray != null ? this.typeConverterIntArrayConverter.getDBValue(model.temperatureArray) : null;
        if (reftemperatureArray != null) {
            statement.bindBlob(start + 37, reftemperatureArray);
        } else {
            statement.bindNull(start + 37);
        }
        statement.bindLong(start + 38, model.toDelete ? 1 : 0);
        statement.bindLong(start + 39, (long) model.syncState);
        statement.bindLong(start + 40, model.mServerId);
    }

    public final void bindToStatement(DatabaseStatement statement, Sleep model) {
        statement.bindLong(1, model.mId);
        bindToInsertStatement(statement, model, 1);
    }

    public final String getInsertStatementQuery() {
        return "INSERT INTO `SLEEP`(`USER_ID`,`RAW_DATA`,`SLEEP_SCORE`,`START_DATE`,`END_DATE`,`SLEEP_TYPE`,`TIME_FALL_ASLEEP`,`REM_DURATION`,`LIGHT_DURATION`,`DEEP_DURATION`,`AWAKE_DURATION`,`AWAKENINGS`,`HYPNOGRAM`,`COMPUTING_APP_CONFIG`,`DOWNLOAD_APP_VERSION`,`SLEEP_PULSE_AVERAGE`,`AWAKE_PULSE_AVERAGE`,`HIGHEST_PULSE`,`LOWEST_PULSE`,`FALLING_ASLEEP`,`PULSE_ARRAY`,`PULSE_STD_ARRAY`,`ACCELEREMETER_ARRAY`,`ACCELEREMETER_STD_ARRAY`,`DELTA_LOWER_ARRAY`,`DELTA_HIGHER_ARRAY`,`THETA_ARRAY`,`ALPHA_LOWER_ARRAY`,`ALPHA_HIGHER_ARRAY`,`SPINDLES_ARRAY`,`SPINDLES_STD_ARRAY`,`SIGNAL_QUALITY_ARRAY`,`BETA_LOWER_ARRAY`,`BETA_HIGHER_ARRAY`,`POWER_ARRAY`,`POWER_NO_DELTA_ARRAY`,`TEMPERATURE_ARRAY`,`TO_DELETE`,`SYNC_STATE`,`SERVER_ID`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }

    public final String getCompiledStatementQuery() {
        return "INSERT INTO `SLEEP`(`ID`,`USER_ID`,`RAW_DATA`,`SLEEP_SCORE`,`START_DATE`,`END_DATE`,`SLEEP_TYPE`,`TIME_FALL_ASLEEP`,`REM_DURATION`,`LIGHT_DURATION`,`DEEP_DURATION`,`AWAKE_DURATION`,`AWAKENINGS`,`HYPNOGRAM`,`COMPUTING_APP_CONFIG`,`DOWNLOAD_APP_VERSION`,`SLEEP_PULSE_AVERAGE`,`AWAKE_PULSE_AVERAGE`,`HIGHEST_PULSE`,`LOWEST_PULSE`,`FALLING_ASLEEP`,`PULSE_ARRAY`,`PULSE_STD_ARRAY`,`ACCELEREMETER_ARRAY`,`ACCELEREMETER_STD_ARRAY`,`DELTA_LOWER_ARRAY`,`DELTA_HIGHER_ARRAY`,`THETA_ARRAY`,`ALPHA_LOWER_ARRAY`,`ALPHA_HIGHER_ARRAY`,`SPINDLES_ARRAY`,`SPINDLES_STD_ARRAY`,`SIGNAL_QUALITY_ARRAY`,`BETA_LOWER_ARRAY`,`BETA_HIGHER_ARRAY`,`POWER_ARRAY`,`POWER_NO_DELTA_ARRAY`,`TEMPERATURE_ARRAY`,`TO_DELETE`,`SYNC_STATE`,`SERVER_ID`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    }

    public final String getCreationQuery() {
        return "CREATE TABLE IF NOT EXISTS `SLEEP`(`ID` INTEGER PRIMARY KEY AUTOINCREMENT,`USER_ID` INTEGER,`RAW_DATA` BLOB,`SLEEP_SCORE` INTEGER,`START_DATE` INTEGER,`END_DATE` INTEGER,`SLEEP_TYPE` INTEGER,`TIME_FALL_ASLEEP` INTEGER,`REM_DURATION` INTEGER,`LIGHT_DURATION` INTEGER,`DEEP_DURATION` INTEGER,`AWAKE_DURATION` INTEGER,`AWAKENINGS` INTEGER,`HYPNOGRAM` BLOB,`COMPUTING_APP_CONFIG` INTEGER,`DOWNLOAD_APP_VERSION` INTEGER,`SLEEP_PULSE_AVERAGE` INTEGER,`AWAKE_PULSE_AVERAGE` INTEGER,`HIGHEST_PULSE` INTEGER,`LOWEST_PULSE` INTEGER,`FALLING_ASLEEP` INTEGER,`PULSE_ARRAY` BLOB,`PULSE_STD_ARRAY` BLOB,`ACCELEREMETER_ARRAY` BLOB,`ACCELEREMETER_STD_ARRAY` BLOB,`DELTA_LOWER_ARRAY` BLOB,`DELTA_HIGHER_ARRAY` BLOB,`THETA_ARRAY` BLOB,`ALPHA_LOWER_ARRAY` BLOB,`ALPHA_HIGHER_ARRAY` BLOB,`SPINDLES_ARRAY` BLOB,`SPINDLES_STD_ARRAY` BLOB,`SIGNAL_QUALITY_ARRAY` BLOB,`BETA_LOWER_ARRAY` BLOB,`BETA_HIGHER_ARRAY` BLOB,`POWER_ARRAY` BLOB,`POWER_NO_DELTA_ARRAY` BLOB,`TEMPERATURE_ARRAY` BLOB,`TO_DELETE` INTEGER,`SYNC_STATE` INTEGER,`SERVER_ID` INTEGER);";
    }

    public final void loadFromCursor(Cursor cursor, Sleep model) {
        int indexID = cursor.getColumnIndex("ID");
        if (indexID == -1 || cursor.isNull(indexID)) {
            model.mId = 0;
        } else {
            model.mId = cursor.getLong(indexID);
        }
        int indexUSER_ID = cursor.getColumnIndex("USER_ID");
        if (indexUSER_ID == -1 || cursor.isNull(indexUSER_ID)) {
            model.mUserId = 0;
        } else {
            model.mUserId = cursor.getLong(indexUSER_ID);
        }
        int indexRAW_DATA = cursor.getColumnIndex("RAW_DATA");
        if (indexRAW_DATA == -1 || cursor.isNull(indexRAW_DATA)) {
            model.mRawData = null;
        } else {
            model.mRawData = new Blob(cursor.getBlob(indexRAW_DATA));
        }
        int indexSLEEP_SCORE = cursor.getColumnIndex("SLEEP_SCORE");
        if (indexSLEEP_SCORE == -1 || cursor.isNull(indexSLEEP_SCORE)) {
            model.mSleepScore = 0;
        } else {
            model.mSleepScore = cursor.getInt(indexSLEEP_SCORE);
        }
        int indexSTART_DATE = cursor.getColumnIndex("START_DATE");
        if (indexSTART_DATE == -1 || cursor.isNull(indexSTART_DATE)) {
            model.mStartDate = null;
        } else {
            model.mStartDate = this.global_typeConverterDateConverter.getModelValue(Long.valueOf(cursor.getLong(indexSTART_DATE)));
        }
        int indexEND_DATE = cursor.getColumnIndex("END_DATE");
        if (indexEND_DATE == -1 || cursor.isNull(indexEND_DATE)) {
            model.mEndDate = null;
        } else {
            model.mEndDate = this.global_typeConverterDateConverter.getModelValue(Long.valueOf(cursor.getLong(indexEND_DATE)));
        }
        int indexSLEEP_TYPE = cursor.getColumnIndex("SLEEP_TYPE");
        if (indexSLEEP_TYPE == -1 || cursor.isNull(indexSLEEP_TYPE)) {
            model.mSleepType = 0;
        } else {
            model.mSleepType = cursor.getInt(indexSLEEP_TYPE);
        }
        int indexTIME_FALL_ASLEEP = cursor.getColumnIndex("TIME_FALL_ASLEEP");
        if (indexTIME_FALL_ASLEEP == -1 || cursor.isNull(indexTIME_FALL_ASLEEP)) {
            model.mTimeFallAsleep = 0;
        } else {
            model.mTimeFallAsleep = cursor.getInt(indexTIME_FALL_ASLEEP);
        }
        int indexREM_DURATION = cursor.getColumnIndex("REM_DURATION");
        if (indexREM_DURATION == -1 || cursor.isNull(indexREM_DURATION)) {
            model.mRemDuration = 0;
        } else {
            model.mRemDuration = cursor.getInt(indexREM_DURATION);
        }
        int indexLIGHT_DURATION = cursor.getColumnIndex("LIGHT_DURATION");
        if (indexLIGHT_DURATION == -1 || cursor.isNull(indexLIGHT_DURATION)) {
            model.mLightDuration = 0;
        } else {
            model.mLightDuration = cursor.getInt(indexLIGHT_DURATION);
        }
        int indexDEEP_DURATION = cursor.getColumnIndex("DEEP_DURATION");
        if (indexDEEP_DURATION == -1 || cursor.isNull(indexDEEP_DURATION)) {
            model.mDeepDuration = 0;
        } else {
            model.mDeepDuration = cursor.getInt(indexDEEP_DURATION);
        }
        int indexAWAKE_DURATION = cursor.getColumnIndex("AWAKE_DURATION");
        if (indexAWAKE_DURATION == -1 || cursor.isNull(indexAWAKE_DURATION)) {
            model.mAwakeDuration = 0;
        } else {
            model.mAwakeDuration = cursor.getInt(indexAWAKE_DURATION);
        }
        int indexAWAKENINGS = cursor.getColumnIndex("AWAKENINGS");
        if (indexAWAKENINGS == -1 || cursor.isNull(indexAWAKENINGS)) {
            model.mAwakenings = 0;
        } else {
            model.mAwakenings = cursor.getInt(indexAWAKENINGS);
        }
        int indexHYPNOGRAM = cursor.getColumnIndex("HYPNOGRAM");
        if (indexHYPNOGRAM == -1 || cursor.isNull(indexHYPNOGRAM)) {
            model.hypnogram = null;
        } else {
            model.hypnogram = this.typeConverterHypnogramConverter.getModelValue(cursor.getBlob(indexHYPNOGRAM));
        }
        int indexCOMPUTING_APP_CONFIG = cursor.getColumnIndex("COMPUTING_APP_CONFIG");
        if (indexCOMPUTING_APP_CONFIG == -1 || cursor.isNull(indexCOMPUTING_APP_CONFIG)) {
            model.computingAppConfig = 0;
        } else {
            model.computingAppConfig = cursor.getLong(indexCOMPUTING_APP_CONFIG);
        }
        int indexDOWNLOAD_APP_VERSION = cursor.getColumnIndex("DOWNLOAD_APP_VERSION");
        if (indexDOWNLOAD_APP_VERSION == -1 || cursor.isNull(indexDOWNLOAD_APP_VERSION)) {
            model.downloadAppVersion = 0;
        } else {
            model.downloadAppVersion = cursor.getLong(indexDOWNLOAD_APP_VERSION);
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
            model.mPulseArray = null;
        } else {
            model.mPulseArray = this.typeConverterIntArrayConverter.getModelValue(cursor.getBlob(indexPULSE_ARRAY));
        }
        int indexPULSE_STD_ARRAY = cursor.getColumnIndex("PULSE_STD_ARRAY");
        if (indexPULSE_STD_ARRAY == -1 || cursor.isNull(indexPULSE_STD_ARRAY)) {
            model.pulseStdArray = null;
        } else {
            model.pulseStdArray = this.typeConverterIntArrayConverter.getModelValue(cursor.getBlob(indexPULSE_STD_ARRAY));
        }
        int indexACCELEREMETER_ARRAY = cursor.getColumnIndex("ACCELEREMETER_ARRAY");
        if (indexACCELEREMETER_ARRAY == -1 || cursor.isNull(indexACCELEREMETER_ARRAY)) {
            model.acceleremeterArray = null;
        } else {
            model.acceleremeterArray = this.typeConverterIntArrayConverter.getModelValue(cursor.getBlob(indexACCELEREMETER_ARRAY));
        }
        int indexACCELEREMETER_STD_ARRAY = cursor.getColumnIndex("ACCELEREMETER_STD_ARRAY");
        if (indexACCELEREMETER_STD_ARRAY == -1 || cursor.isNull(indexACCELEREMETER_STD_ARRAY)) {
            model.acceleremeterStdArray = null;
        } else {
            model.acceleremeterStdArray = this.typeConverterIntArrayConverter.getModelValue(cursor.getBlob(indexACCELEREMETER_STD_ARRAY));
        }
        int indexDELTA_LOWER_ARRAY = cursor.getColumnIndex("DELTA_LOWER_ARRAY");
        if (indexDELTA_LOWER_ARRAY == -1 || cursor.isNull(indexDELTA_LOWER_ARRAY)) {
            model.deltaLowerArray = null;
        } else {
            model.deltaLowerArray = this.typeConverterIntArrayConverter.getModelValue(cursor.getBlob(indexDELTA_LOWER_ARRAY));
        }
        int indexDELTA_HIGHER_ARRAY = cursor.getColumnIndex("DELTA_HIGHER_ARRAY");
        if (indexDELTA_HIGHER_ARRAY == -1 || cursor.isNull(indexDELTA_HIGHER_ARRAY)) {
            model.deltaHigherArray = null;
        } else {
            model.deltaHigherArray = this.typeConverterIntArrayConverter.getModelValue(cursor.getBlob(indexDELTA_HIGHER_ARRAY));
        }
        int indexTHETA_ARRAY = cursor.getColumnIndex("THETA_ARRAY");
        if (indexTHETA_ARRAY == -1 || cursor.isNull(indexTHETA_ARRAY)) {
            model.thetaArray = null;
        } else {
            model.thetaArray = this.typeConverterIntArrayConverter.getModelValue(cursor.getBlob(indexTHETA_ARRAY));
        }
        int indexALPHA_LOWER_ARRAY = cursor.getColumnIndex("ALPHA_LOWER_ARRAY");
        if (indexALPHA_LOWER_ARRAY == -1 || cursor.isNull(indexALPHA_LOWER_ARRAY)) {
            model.alphaLowerArray = null;
        } else {
            model.alphaLowerArray = this.typeConverterIntArrayConverter.getModelValue(cursor.getBlob(indexALPHA_LOWER_ARRAY));
        }
        int indexALPHA_HIGHER_ARRAY = cursor.getColumnIndex("ALPHA_HIGHER_ARRAY");
        if (indexALPHA_HIGHER_ARRAY == -1 || cursor.isNull(indexALPHA_HIGHER_ARRAY)) {
            model.alphaHigherArray = null;
        } else {
            model.alphaHigherArray = this.typeConverterIntArrayConverter.getModelValue(cursor.getBlob(indexALPHA_HIGHER_ARRAY));
        }
        int indexSPINDLES_ARRAY = cursor.getColumnIndex("SPINDLES_ARRAY");
        if (indexSPINDLES_ARRAY == -1 || cursor.isNull(indexSPINDLES_ARRAY)) {
            model.spindlesArray = null;
        } else {
            model.spindlesArray = this.typeConverterIntArrayConverter.getModelValue(cursor.getBlob(indexSPINDLES_ARRAY));
        }
        int indexSPINDLES_STD_ARRAY = cursor.getColumnIndex("SPINDLES_STD_ARRAY");
        if (indexSPINDLES_STD_ARRAY == -1 || cursor.isNull(indexSPINDLES_STD_ARRAY)) {
            model.spindlesStdArray = null;
        } else {
            model.spindlesStdArray = this.typeConverterIntArrayConverter.getModelValue(cursor.getBlob(indexSPINDLES_STD_ARRAY));
        }
        int indexSIGNAL_QUALITY_ARRAY = cursor.getColumnIndex("SIGNAL_QUALITY_ARRAY");
        if (indexSIGNAL_QUALITY_ARRAY == -1 || cursor.isNull(indexSIGNAL_QUALITY_ARRAY)) {
            model.signalQualityArray = null;
        } else {
            model.signalQualityArray = this.typeConverterIntArrayConverter.getModelValue(cursor.getBlob(indexSIGNAL_QUALITY_ARRAY));
        }
        int indexBETA_LOWER_ARRAY = cursor.getColumnIndex("BETA_LOWER_ARRAY");
        if (indexBETA_LOWER_ARRAY == -1 || cursor.isNull(indexBETA_LOWER_ARRAY)) {
            model.betaLowerArray = null;
        } else {
            model.betaLowerArray = this.typeConverterIntArrayConverter.getModelValue(cursor.getBlob(indexBETA_LOWER_ARRAY));
        }
        int indexBETA_HIGHER_ARRAY = cursor.getColumnIndex("BETA_HIGHER_ARRAY");
        if (indexBETA_HIGHER_ARRAY == -1 || cursor.isNull(indexBETA_HIGHER_ARRAY)) {
            model.betaHigherArray = null;
        } else {
            model.betaHigherArray = this.typeConverterIntArrayConverter.getModelValue(cursor.getBlob(indexBETA_HIGHER_ARRAY));
        }
        int indexPOWER_ARRAY = cursor.getColumnIndex("POWER_ARRAY");
        if (indexPOWER_ARRAY == -1 || cursor.isNull(indexPOWER_ARRAY)) {
            model.powerArray = null;
        } else {
            model.powerArray = this.typeConverterIntArrayConverter.getModelValue(cursor.getBlob(indexPOWER_ARRAY));
        }
        int indexPOWER_NO_DELTA_ARRAY = cursor.getColumnIndex("POWER_NO_DELTA_ARRAY");
        if (indexPOWER_NO_DELTA_ARRAY == -1 || cursor.isNull(indexPOWER_NO_DELTA_ARRAY)) {
            model.powerNoDeltaArray = null;
        } else {
            model.powerNoDeltaArray = this.typeConverterIntArrayConverter.getModelValue(cursor.getBlob(indexPOWER_NO_DELTA_ARRAY));
        }
        int indexTEMPERATURE_ARRAY = cursor.getColumnIndex("TEMPERATURE_ARRAY");
        if (indexTEMPERATURE_ARRAY == -1 || cursor.isNull(indexTEMPERATURE_ARRAY)) {
            model.temperatureArray = null;
        } else {
            model.temperatureArray = this.typeConverterIntArrayConverter.getModelValue(cursor.getBlob(indexTEMPERATURE_ARRAY));
        }
        int indexTO_DELETE = cursor.getColumnIndex("TO_DELETE");
        if (indexTO_DELETE == -1 || cursor.isNull(indexTO_DELETE)) {
            model.toDelete = false;
        } else {
            model.toDelete = cursor.getInt(indexTO_DELETE) == 1;
        }
        int indexSYNC_STATE = cursor.getColumnIndex("SYNC_STATE");
        if (indexSYNC_STATE == -1 || cursor.isNull(indexSYNC_STATE)) {
            model.syncState = 0;
        } else {
            model.syncState = cursor.getInt(indexSYNC_STATE);
        }
        int indexSERVER_ID = cursor.getColumnIndex("SERVER_ID");
        if (indexSERVER_ID == -1 || cursor.isNull(indexSERVER_ID)) {
            model.mServerId = 0;
        } else {
            model.mServerId = cursor.getLong(indexSERVER_ID);
        }
    }

    public final boolean exists(Sleep model, DatabaseWrapper wrapper) {
        if (model.mId > 0) {
            if (new Select(Method.count(new IProperty[0])).from(Sleep.class).where(getPrimaryConditionClause(model)).count(wrapper) > 0) {
                return true;
            }
        }
        return false;
    }

    public final ConditionGroup getPrimaryConditionClause(Sleep model) {
        ConditionGroup clause = ConditionGroup.clause();
        clause.and(Sleep_Table.ID.eq(model.mId));
        return clause;
    }

    public final Sleep newInstance() {
        return new Sleep();
    }

    public final BaseProperty getProperty(String name) {
        return Sleep_Table.getProperty(name);
    }
}
