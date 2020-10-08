package com.inteliclinic.neuroon.models.data;

import com.google.gson.Gson;
import com.inteliclinic.neuroon.models.bluetooth.BleDataFrame;
import com.inteliclinic.neuroon.models.stats.StagingData;
import com.raizlabs.android.dbflow.data.Blob;
import com.raizlabs.android.dbflow.structure.BaseModel;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;

public class OldSleep extends BaseModel {
    public static final int BLT = 3;
    public static final int NAP = 4;
    public static final int SLEEP = 1;
    public static final int SLEEP_JET_LAG = 2;
    public static final int UNDEFINED = 0;
    String acceleremeterArray;
    String acceleremeterStdArray;
    String alphaHigherArray;
    String alphaLowerArray;
    int awakeDuration;
    int awakePulseAverage;
    int awakenings;
    String betaHigherArray;
    String betaLowerArray;
    int deepDuration;
    String deltaHigherArray;
    String deltaLowerArray;
    Date endDate;
    int fallingAsleep;
    int highestPulse;
    String hypnogram;
    long id;
    int lightDuration;
    int lowestPulse;
    StagingData mHypnogram;
    String powerArray;
    String powerNoDeltaArray;
    String pulseArray;
    String pulseStdArray;
    Blob rawData;
    int remDuration;
    String signalQualityArray;
    int sleepPulseAverage;
    int sleepScore;
    int sleepType;
    String spindlesArray;
    String spindlesStdArray;
    Date startDate;
    String temperatureArray;
    String thetaArray;
    int timeFallAsleep;
    boolean toDelete;

    @Retention(RetentionPolicy.SOURCE)
    public @interface SleepType {
    }

    public OldSleep() {
    }

    public OldSleep(byte[] rawData2, Date startDate2, Date endDate2, int sleepTypeRaw) {
        this.rawData = new Blob(rawData2);
        this.startDate = startDate2;
        this.endDate = endDate2;
        this.sleepType = sleepTypeFromRaw(sleepTypeRaw);
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
        if (this.rawData != null && this.rawData.getBlob().length > 20) {
            this.sleepType = sleepTypeFromRaw(new BleDataFrame(Arrays.copyOf(this.rawData.getBlob(), 20)).getSleepType());
        }
        if (this.sleepType != 0) {
            save();
        }
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public byte[] getRawData() {
        return this.rawData.getBlob();
    }

    public void setRemDuration(int remDuration2) {
        this.remDuration = remDuration2;
    }

    public void setLightDuration(int lightDuration2) {
        this.lightDuration = lightDuration2;
    }

    public void setDeepDuration(int deepDuration2) {
        this.deepDuration = deepDuration2;
    }

    public StagingData getHypnogram() {
        if (this.mHypnogram == null) {
            this.mHypnogram = (StagingData) new Gson().fromJson(this.hypnogram, StagingData.class);
        }
        return this.mHypnogram;
    }

    public void setHypnogram(StagingData hypnogram2) {
        this.hypnogram = new Gson().toJson((Object) hypnogram2);
    }

    public int getSleepScore() {
        return this.sleepScore;
    }

    public void setSleepScore(int sleepScore2) {
        this.sleepScore = sleepScore2;
    }

    public int getSleepDuration() {
        if (this.endDate == null || this.startDate == null) {
            return 0;
        }
        return (int) ((this.endDate.getTime() - this.startDate.getTime()) / 1000);
    }

    public int getAwakenings() {
        return this.awakenings;
    }

    public void setAwakenings(int awakenings2) {
        this.awakenings = awakenings2;
    }

    public void setAcceleremeterArray(int[] acceleremeterArray2) {
        this.acceleremeterArray = new Gson().toJson((Object) acceleremeterArray2, (Type) int[].class);
    }

    public void setAcceleremeterStdArray(int[] acceleremeterStdArray2) {
        this.acceleremeterStdArray = new Gson().toJson((Object) acceleremeterStdArray2, (Type) int[].class);
    }

    public int[] getPulseArray() {
        return (int[]) new Gson().fromJson(this.pulseArray, int[].class);
    }

    public void setPulseArray(int[] pulseArray2) {
        this.pulseArray = new Gson().toJson((Object) pulseArray2, (Type) int[].class);
    }

    public int[] getPulseStdArray() {
        return (int[]) new Gson().fromJson(this.pulseStdArray, int[].class);
    }

    public void setPulseStdArray(int[] pulseStdArray2) {
        this.pulseStdArray = new Gson().toJson((Object) pulseStdArray2, (Type) int[].class);
    }

    public int[] getAccelerometerArray() {
        return (int[]) new Gson().fromJson(this.acceleremeterArray, int[].class);
    }

    public int[] getAccelerometerStdArray() {
        return (int[]) new Gson().fromJson(this.acceleremeterStdArray, int[].class);
    }

    public int[] getDeltaLowerArray() {
        return (int[]) new Gson().fromJson(this.deltaLowerArray, int[].class);
    }

    public void setDeltaLowerArray(int[] deltaLowerArray2) {
        this.deltaLowerArray = new Gson().toJson((Object) deltaLowerArray2, (Type) int[].class);
    }

    public int[] getDeltaHigherArray() {
        return (int[]) new Gson().fromJson(this.deltaHigherArray, int[].class);
    }

    public void setDeltaHigherArray(int[] deltaHigherArray2) {
        this.deltaHigherArray = new Gson().toJson((Object) deltaHigherArray2, (Type) int[].class);
    }

    public int[] getThetaArray() {
        return (int[]) new Gson().fromJson(this.thetaArray, int[].class);
    }

    public void setThetaArray(int[] thetaArray2) {
        this.thetaArray = new Gson().toJson((Object) thetaArray2, (Type) int[].class);
    }

    public int[] getAlphaLowerArray() {
        return (int[]) new Gson().fromJson(this.alphaLowerArray, int[].class);
    }

    public void setAlphaLowerArray(int[] alphaLowerArray2) {
        this.alphaLowerArray = new Gson().toJson((Object) alphaLowerArray2, (Type) int[].class);
    }

    public int[] getSpindlesArray() {
        return (int[]) new Gson().fromJson(this.spindlesArray, int[].class);
    }

    public void setSpindlesArray(int[] spindlesArray2) {
        this.spindlesArray = new Gson().toJson((Object) spindlesArray2, (Type) int[].class);
    }

    public int[] getSpindlesStdArray() {
        return (int[]) new Gson().fromJson(this.spindlesStdArray, int[].class);
    }

    public void setSpindlesStdArray(int[] spindlesStdArray2) {
        this.spindlesStdArray = new Gson().toJson((Object) spindlesStdArray2, (Type) int[].class);
    }

    public int[] getSignalQualityArray() {
        return (int[]) new Gson().fromJson(this.signalQualityArray, int[].class);
    }

    public void setSignalQualityArray(int[] signalQualityArray2) {
        this.signalQualityArray = new Gson().toJson((Object) signalQualityArray2, (Type) int[].class);
    }

    public int[] getAlphaHigherArray() {
        return (int[]) new Gson().fromJson(this.alphaHigherArray, int[].class);
    }

    public void setAlphaHigherArray(int[] alphaHigherArray2) {
        this.alphaHigherArray = new Gson().toJson((Object) alphaHigherArray2, (Type) int[].class);
    }

    public int[] getBetaLowerArray() {
        return (int[]) new Gson().fromJson(this.betaLowerArray, int[].class);
    }

    public void setBetaLowerArray(int[] betaLowerArray2) {
        this.betaLowerArray = new Gson().toJson((Object) betaLowerArray2, (Type) int[].class);
    }

    public int[] getBetaHigherArray() {
        return (int[]) new Gson().fromJson(this.betaHigherArray, int[].class);
    }

    public void setBetaHigherArray(int[] betaHigherArray2) {
        this.betaHigherArray = new Gson().toJson((Object) betaHigherArray2, (Type) int[].class);
    }

    public int[] getPowerArray() {
        return (int[]) new Gson().fromJson(this.powerArray, int[].class);
    }

    public void setPowerArray(int[] powerArray2) {
        this.powerArray = new Gson().toJson((Object) powerArray2, (Type) int[].class);
    }

    public int[] getPowerNoDeltaArray() {
        return (int[]) new Gson().fromJson(this.powerNoDeltaArray, int[].class);
    }

    public void setPowerNoDeltaArray(int[] powerNoDeltaArray2) {
        this.powerNoDeltaArray = new Gson().toJson((Object) powerNoDeltaArray2, (Type) int[].class);
    }

    public int[] getTemperatureArray() {
        return (int[]) new Gson().fromJson(this.temperatureArray, int[].class);
    }

    public void setTemperatureArray(int[] temperatureArray2) {
        this.temperatureArray = new Gson().toJson((Object) temperatureArray2, (Type) int[].class);
    }

    public int getSleepPulseAverage() {
        return this.sleepPulseAverage;
    }

    public void setSleepPulseAverage(int sleepPulseAverage2) {
        this.sleepPulseAverage = sleepPulseAverage2;
    }

    public int getRemDurationProcentage() {
        int[] array = getPulseArray();
        if (array == null || array.length <= 0) {
            return 0;
        }
        return (this.remDuration * 100) / getPulseArray().length;
    }

    public int getDeepDurationProcentage() {
        int[] array = getPulseArray();
        if (array == null || array.length <= 0) {
            return 0;
        }
        return (this.deepDuration * 100) / getPulseArray().length;
    }

    public int getLightDurationProcentage() {
        int[] array = getPulseArray();
        if (array == null || array.length <= 0) {
            return 0;
        }
        return (this.lightDuration * 100) / getPulseArray().length;
    }

    public int getAwakeDuration() {
        return (int) (((float) getSleepDuration()) * (((float) getWakeDurationProcentage()) / 100.0f));
    }

    public void setAwakeDuration(int awakeDuration2) {
        this.awakeDuration = awakeDuration2;
    }

    public int getWakeDurationProcentage() {
        int[] array = getPulseArray();
        if (array == null || array.length <= 0) {
            return 0;
        }
        return (this.awakeDuration * 100) / getPulseArray().length;
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
        return this.timeFallAsleep * 30;
    }

    public void setTimeFallAsleep(int timeFallAsleep2) {
        this.timeFallAsleep = timeFallAsleep2;
    }

    public boolean hasHypnogram() {
        return this.hypnogram != null;
    }

    public int getSleepType() {
        if (this.sleepType == 0) {
            computeSleepType();
        }
        return this.sleepType;
    }

    public void setAsDeleted() {
        this.toDelete = true;
    }

    public boolean isDeleted() {
        return this.toDelete;
    }

    public long getId() {
        return this.id;
    }
}
