package com.inteliclinic.neuroon.models.data;

import com.inteliclinic.neuroon.models.bluetooth.BleDataFrame;
import com.inteliclinic.neuroon.models.network.SleepRecordingMeta;
import com.inteliclinic.neuroon.models.stats.SleepStage;
import com.inteliclinic.neuroon.models.stats.StagingData;
import com.inteliclinic.neuroon.utils.DateUtils;
import com.raizlabs.android.dbflow.data.Blob;
import com.raizlabs.android.dbflow.structure.BaseModel;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.Date;

public class Sleep extends BaseModel {
    public static final int BLT = 3;
    public static final int NAP = 4;
    public static final int SLEEP = 1;
    public static final int SLEEP_JET_LAG = 2;
    public static final int SYNC_STATE_DONE = 2;
    public static final int SYNC_STATE_META = 1;
    public static final int SYNC_STATE_NO = 0;
    public static final int SYNC_STATE_UPDATE_META = 3;
    public static final int UNDEFINED = 0;
    int[] acceleremeterArray;
    int[] acceleremeterStdArray;
    int[] alphaHigherArray;
    int[] alphaLowerArray;
    int awakePulseAverage;
    int[] betaHigherArray;
    int[] betaLowerArray;
    long computingAppConfig;
    int[] deltaHigherArray;
    int[] deltaLowerArray;
    long downloadAppVersion;
    int fallingAsleep;
    int highestPulse;
    double[][] hypnogram;
    int lowestPulse;
    int mAwakeDuration;
    int mAwakenings;
    int mDeepDuration;
    Date mEndDate;
    StagingData mHypnogram;
    long mId;
    int mLightDuration;
    int[] mPulseArray;
    Blob mRawData;
    int mRemDuration;
    long mServerId;
    int mSleepScore;
    int mSleepType;
    Date mStartDate;
    int mTimeFallAsleep;
    long mUserId;
    int[] powerArray;
    int[] powerNoDeltaArray;
    int[] pulseStdArray;
    int[] signalQualityArray;
    int sleepPulseAverage;
    int[] spindlesArray;
    int[] spindlesStdArray;
    int syncState;
    int[] temperatureArray;
    int[] thetaArray;
    boolean toDelete;

    @Retention(RetentionPolicy.SOURCE)
    public @interface SleepType {
    }

    public Sleep() {
    }

    public Sleep(byte[] rawData, Date startDate, Date endDate, int sleepTypeRaw, int maskAppVersion) {
        this.mRawData = new Blob(rawData);
        this.mStartDate = startDate;
        this.mEndDate = endDate;
        this.mSleepType = sleepTypeFromRaw(sleepTypeRaw);
        this.downloadAppVersion = (long) maskAppVersion;
    }

    public static Sleep fromData(SleepRecordingMeta meta, byte[] data) {
        BleDataFrame bleDataFrame = new BleDataFrame(Arrays.copyOfRange(data, 0, 20));
        if (bleDataFrame.getFrameNumber() == 0) {
            long timeStamp = ((long) bleDataFrame.getTimeStamp().intValue()) * 1000;
            return new Sleep(data, new Date(timeStamp), new Date(((long) (((data.length / 20) - 2) * 15000)) + timeStamp), bleDataFrame.getSleepType(), meta.getAppConfigVersion());
        } else if (bleDataFrame.getFrameNumber() == 2) {
            return new Sleep(data, meta.getSleepDate(), DateUtils.dateAddSeconds(meta.getSleepDate(), (data.length * 15000) / 20), meta.getSleepType(), meta.getAppConfigVersion());
        } else {
            throw new UnsupportedOperationException("SLEEP is empty or invalid");
        }
    }

    public static Sleep fromData(byte[] sleepDataByte, int appVersion) {
        BleDataFrame bleDataFrame = new BleDataFrame(Arrays.copyOfRange(sleepDataByte, 0, 20));
        if (bleDataFrame.getFrameNumber() == 0) {
            long timeStamp = ((long) bleDataFrame.getTimeStamp().intValue()) * 1000;
            return new Sleep(sleepDataByte, new Date(timeStamp), new Date(((long) (((sleepDataByte.length / 20) - 2) * 15000)) + timeStamp), bleDataFrame.getSleepType(), appVersion);
        }
        throw new UnsupportedOperationException("SLEEP is empty or invalid");
    }

    public static Sleep convertFromOld(OldSleep sleep, int appVersion) {
        Sleep sleep1 = new Sleep();
        sleep1.setHypnogram(sleep.getHypnogram());
        sleep1.mRawData = sleep.rawData;
        sleep1.mSleepScore = sleep.sleepScore;
        sleep1.mStartDate = sleep.startDate;
        sleep1.mEndDate = sleep.endDate;
        sleep1.mSleepType = sleep.sleepType;
        sleep1.mTimeFallAsleep = sleep.timeFallAsleep;
        sleep1.mRemDuration = sleep.remDuration;
        sleep1.mLightDuration = sleep.lightDuration;
        sleep1.mDeepDuration = sleep.deepDuration;
        sleep1.mAwakeDuration = sleep.awakeDuration;
        sleep1.mAwakenings = sleep.awakenings;
        sleep1.sleepPulseAverage = sleep.sleepPulseAverage;
        sleep1.awakePulseAverage = sleep.awakePulseAverage;
        sleep1.highestPulse = sleep.highestPulse;
        sleep1.lowestPulse = sleep.lowestPulse;
        sleep1.fallingAsleep = sleep.fallingAsleep;
        sleep1.mPulseArray = sleep.getPulseArray();
        sleep1.pulseStdArray = sleep.getPulseStdArray();
        sleep1.acceleremeterArray = sleep.getAccelerometerArray();
        sleep1.acceleremeterStdArray = sleep.getAccelerometerStdArray();
        sleep1.deltaLowerArray = sleep.getDeltaLowerArray();
        sleep1.deltaHigherArray = sleep.getDeltaHigherArray();
        sleep1.thetaArray = sleep.getThetaArray();
        sleep1.alphaLowerArray = sleep.getAlphaLowerArray();
        sleep1.alphaHigherArray = sleep.getAlphaHigherArray();
        sleep1.spindlesArray = sleep.getSpindlesArray();
        sleep1.spindlesStdArray = sleep.getSpindlesStdArray();
        sleep1.signalQualityArray = sleep.getSignalQualityArray();
        sleep1.betaLowerArray = sleep.getBetaLowerArray();
        sleep1.betaHigherArray = sleep.getBetaHigherArray();
        sleep1.powerArray = sleep.getPowerArray();
        sleep1.powerNoDeltaArray = sleep.getPowerNoDeltaArray();
        sleep1.temperatureArray = sleep.getTemperatureArray();
        sleep1.downloadAppVersion = (long) appVersion;
        sleep1.computingAppConfig = sleep.getHypnogram() != null ? 2 : 0;
        sleep1.toDelete = sleep.toDelete;
        return sleep1;
    }

    private int sleepTypeFromRaw(int sleepTypeRaw) {
        switch (sleepTypeRaw) {
            case 16:
                return 1;
            case 17:
                return 2;
            case 18:
                return 4;
            case 19:
                return 3;
            default:
                return 0;
        }
    }

    private void computeSleepType() {
        if (this.mRawData != null && this.mRawData.getBlob().length > 20) {
            this.mSleepType = sleepTypeFromRaw(new BleDataFrame(Arrays.copyOf(this.mRawData.getBlob(), 20)).getSleepType());
        }
        if (this.mSleepType != 0) {
            save();
        }
    }

    public Date getStartDate() {
        return this.mStartDate;
    }

    public Date getEndDate() {
        return this.mEndDate;
    }

    public byte[] getRawData() {
        return this.mRawData.getBlob();
    }

    public double[][] getRawHypnogram() {
        return this.hypnogram;
    }

    public StagingData getHypnogram() {
        if (this.mHypnogram == null) {
            this.mHypnogram = StagingData.convertFromDb(this.hypnogram);
        }
        return this.mHypnogram;
    }

    public void setHypnogram(StagingData hypnogram2) {
        this.mHypnogram = hypnogram2;
        this.hypnogram = StagingData.convertToDB(hypnogram2);
    }

    public int getSleepScore() {
        return this.mSleepScore;
    }

    public void setSleepScore(int sleepScore) {
        this.mSleepScore = sleepScore;
    }

    public int getSleepDuration() {
        if (this.mEndDate == null || this.mStartDate == null) {
            return 0;
        }
        return (int) ((this.mEndDate.getTime() - this.mStartDate.getTime()) / 1000);
    }

    public int getAwakenings() {
        return this.mAwakenings;
    }

    public void setAwakenings(int awakenings) {
        this.mAwakenings = awakenings;
    }

    public void setAcceleremeterArray(int[] accelerometerArray) {
        this.acceleremeterArray = accelerometerArray;
    }

    public void setAcceleremeterStdArray(int[] acceleremeterStdArray2) {
        this.acceleremeterStdArray = acceleremeterStdArray2;
    }

    public int[] getPulseArray() {
        return this.mPulseArray;
    }

    public void setPulseArray(int[] pulseArray) {
        this.mPulseArray = pulseArray;
    }

    public int[] getPulseStdArray() {
        return this.pulseStdArray;
    }

    public void setPulseStdArray(int[] pulseStdArray2) {
        this.pulseStdArray = pulseStdArray2;
    }

    public int[] getAccelerometerArray() {
        return this.acceleremeterArray;
    }

    public int[] getAccelerometerStdArray() {
        return this.acceleremeterStdArray;
    }

    public int[] getDeltaLowerArray() {
        return this.deltaLowerArray;
    }

    public void setDeltaLowerArray(int[] deltaLowerArray2) {
        this.deltaLowerArray = deltaLowerArray2;
    }

    public int[] getDeltaHigherArray() {
        return this.deltaHigherArray;
    }

    public void setDeltaHigherArray(int[] deltaHigherArray2) {
        this.deltaHigherArray = deltaHigherArray2;
    }

    public int[] getThetaArray() {
        return this.thetaArray;
    }

    public void setThetaArray(int[] thetaArray2) {
        this.thetaArray = thetaArray2;
    }

    public int[] getAlphaLowerArray() {
        return this.alphaLowerArray;
    }

    public void setAlphaLowerArray(int[] alphaLowerArray2) {
        this.alphaLowerArray = alphaLowerArray2;
    }

    public int[] getSpindlesArray() {
        return this.spindlesArray;
    }

    public void setSpindlesArray(int[] spindlesArray2) {
        this.spindlesArray = spindlesArray2;
    }

    public int[] getSpindlesStdArray() {
        return this.spindlesStdArray;
    }

    public void setSpindlesStdArray(int[] spindlesStdArray2) {
        this.spindlesStdArray = spindlesStdArray2;
    }

    public int[] getSignalQualityArray() {
        return this.signalQualityArray;
    }

    public void setSignalQualityArray(int[] signalQualityArray2) {
        this.signalQualityArray = signalQualityArray2;
    }

    public int[] getAlphaHigherArray() {
        return this.alphaHigherArray;
    }

    public void setAlphaHigherArray(int[] alphaHigherArray2) {
        this.alphaHigherArray = alphaHigherArray2;
    }

    public int[] getBetaLowerArray() {
        return this.betaLowerArray;
    }

    public void setBetaLowerArray(int[] betaLowerArray2) {
        this.betaLowerArray = betaLowerArray2;
    }

    public int[] getBetaHigherArray() {
        return this.betaHigherArray;
    }

    public void setBetaHigherArray(int[] betaHigherArray2) {
        this.betaHigherArray = betaHigherArray2;
    }

    public int[] getPowerArray() {
        return this.powerArray;
    }

    public void setPowerArray(int[] powerArray2) {
        this.powerArray = powerArray2;
    }

    public int[] getPowerNoDeltaArray() {
        return this.powerNoDeltaArray;
    }

    public void setPowerNoDeltaArray(int[] powerNoDeltaArray2) {
        this.powerNoDeltaArray = powerNoDeltaArray2;
    }

    public int[] getTemperatureArray() {
        return this.temperatureArray;
    }

    public void setTemperatureArray(int[] temperatureArray2) {
        this.temperatureArray = temperatureArray2;
    }

    public int getSleepPulseAverage() {
        return this.sleepPulseAverage;
    }

    public void setSleepPulseAverage(int sleepPulseAverage2) {
        this.sleepPulseAverage = sleepPulseAverage2;
    }

    public int getRemDurationProcentage() {
        int[] pulseArray = getPulseArray();
        if (pulseArray == null || pulseArray.length <= 0) {
            return 0;
        }
        return (this.mRemDuration * 100) / getPulseArray().length;
    }

    public int getDeepDurationProcentage() {
        int[] pulseArray = getPulseArray();
        if (pulseArray == null || pulseArray.length <= 0) {
            return 0;
        }
        return (this.mDeepDuration * 100) / getPulseArray().length;
    }

    public int getLightDurationProcentage() {
        int[] pulseArray = getPulseArray();
        if (pulseArray == null || pulseArray.length <= 0) {
            return 0;
        }
        return (this.mLightDuration * 100) / getPulseArray().length;
    }

    public int getAwakeDuration() {
        return (int) (((float) getSleepDuration()) * (((float) getWakeDurationProcentage()) / 100.0f));
    }

    public void setAwakeDuration(int awakeDuration) {
        this.mAwakeDuration = awakeDuration;
    }

    public int getWakeDurationProcentage() {
        int[] pulseArray = getPulseArray();
        if (pulseArray == null || pulseArray.length <= 0) {
            return 0;
        }
        return (this.mAwakeDuration * 100) / getPulseArray().length;
    }

    public int getAwakePulseAverage() {
        return this.awakePulseAverage;
    }

    public void setAwakePulseAverage(int awakePulseAverage2) {
        this.awakePulseAverage = awakePulseAverage2;
    }

    public int getHighestPulse() {
        return this.highestPulse;
    }

    public void setHighestPulse(int highestPulse2) {
        this.highestPulse = highestPulse2;
    }

    public int getLowestPulse() {
        return this.lowestPulse;
    }

    public void setLowestPulse(int lowestPulse2) {
        this.lowestPulse = lowestPulse2;
    }

    public int getTimeFallAsleep() {
        return this.mTimeFallAsleep * 30;
    }

    public void setTimeFallAsleep(int timeFallAsleep) {
        this.mTimeFallAsleep = timeFallAsleep;
    }

    public boolean hasHypnogram() {
        return this.hypnogram != null;
    }

    public int getSleepType() {
        if (this.mSleepType == 0) {
            computeSleepType();
        }
        return this.mSleepType;
    }

    public void setAsDeleted() {
        this.toDelete = true;
    }

    public boolean isDeleted() {
        return this.toDelete;
    }

    public long getId() {
        return this.mId;
    }

    public void setAsDataSynced() {
        this.syncState = 2;
    }

    public void setSyncState(long id, boolean updateId) {
        if (id > 0) {
            this.syncState = 1;
            if (updateId) {
                this.mServerId = id;
                return;
            }
            return;
        }
        this.syncState = 0;
        if (updateId) {
            this.mServerId = 0;
        }
    }

    public void setSyncState(long id) {
        setSyncState(id, true);
    }

    public long getServerId() {
        return this.mServerId;
    }

    public void setComputingAppConfig(long computingAppConfig2) {
        this.computingAppConfig = computingAppConfig2;
    }

    public int getRemCount() {
        int remCount = 0;
        StagingData hypno = getHypnogram();
        if (!(hypno == null || hypno.getHypnogram() == null)) {
            for (SleepStage sleepStage : hypno.getHypnogram()) {
                if (sleepStage.getStage() == 3) {
                    remCount++;
                }
            }
        }
        return remCount;
    }

    public int getRemDuration() {
        return this.mRemDuration * 30;
    }

    public void setRemDuration(int remDuration) {
        this.mRemDuration = remDuration;
    }

    public int getNRemCount() {
        int nRemCount = 0;
        StagingData hypno = getHypnogram();
        if (!(hypno == null || hypno.getHypnogram() == null)) {
            for (SleepStage sleepStage : hypno.getHypnogram()) {
                if (sleepStage.getStage() == 2 || sleepStage.getStage() == 4) {
                    nRemCount++;
                }
            }
        }
        return nRemCount;
    }

    public int getNRemDuration() {
        return (this.mDeepDuration + this.mLightDuration) * 30;
    }

    public int getLightDuration() {
        return this.mLightDuration * 30;
    }

    public void setLightDuration(int lightDuration) {
        this.mLightDuration = lightDuration;
    }

    public int getDeepDuration() {
        return this.mDeepDuration * 30;
    }

    public void setDeepDuration(int deepDuration) {
        this.mDeepDuration = deepDuration;
    }
}
