package com.inteliclinic.neuroon.models.network;

import com.google.gson.annotations.SerializedName;
import com.inteliclinic.neuroon.models.data.Sleep;
import com.inteliclinic.neuroon.old_guava.Ints;
import com.inteliclinic.neuroon.utils.DateUtils;
import java.text.ParseException;
import java.util.Date;

public class SleepRecordingMeta {
    @SerializedName("app_config_version")
    private int mAppConfigVersion;
    @SerializedName("firmware_version")
    private String mFirmwareVersion;
    @SerializedName("hypnogram")
    private SleepRecordingMetaInnerObject mHypnogram;
    @SerializedName("id")
    private long mId;
    @SerializedName("is_deleted")
    private boolean mIsDeleted;
    @SerializedName("datafile_uploaded")
    private Boolean mIsFileUploaded;
    @SerializedName("recorded_at")
    private String mRecordedAt;
    @SerializedName("sleep_score")
    private int mSleepScore;
    @SerializedName("type")
    private String mType;

    public static SleepRecordingMeta fromSleep(Sleep sleep, String maskFirmwareVer, int appConfigVer) {
        SleepRecordingMeta sleepRecordingMeta = new SleepRecordingMeta();
        sleepRecordingMeta.mRecordedAt = DateUtils.formatISO8601(sleep.getStartDate());
        sleepRecordingMeta.mFirmwareVersion = maskFirmwareVer;
        sleepRecordingMeta.mAppConfigVersion = appConfigVer;
        double[][] rawHypnogram = sleep.getRawHypnogram();
        if (rawHypnogram != null) {
            sleepRecordingMeta.mHypnogram = new SleepRecordingMetaInnerObject(Ints.toArray(rawHypnogram[0]), Ints.toArray(rawHypnogram[1]), rawHypnogram[2], Ints.toArray(rawHypnogram[3]));
        }
        sleepRecordingMeta.setType(sleep);
        sleepRecordingMeta.mSleepScore = sleep.getSleepScore();
        sleepRecordingMeta.mIsDeleted = sleep.isDeleted();
        return sleepRecordingMeta;
    }

    public int getAppConfigVersion() {
        return this.mAppConfigVersion;
    }

    public long getId() {
        return this.mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public boolean isDataUploaded() {
        return this.mIsFileUploaded != null && this.mIsFileUploaded.booleanValue();
    }

    public void setType(Sleep sleep) {
        switch (sleep.getSleepType()) {
            case 0:
                this.mType = "-1";
                return;
            case 1:
                this.mType = "0";
                return;
            case 2:
                this.mType = "2";
                return;
            case 3:
                this.mType = "3";
                return;
            case 4:
                this.mType = "1";
                return;
            default:
                return;
        }
    }

    public int getSleepType() {
        String str = this.mType;
        char c = 65535;
        switch (str.hashCode()) {
            case 48:
                if (str.equals("0")) {
                    c = 0;
                    break;
                }
                break;
            case 49:
                if (str.equals("1")) {
                    c = 1;
                    break;
                }
                break;
            case 50:
                if (str.equals("2")) {
                    c = 2;
                    break;
                }
                break;
            case 51:
                if (str.equals("3")) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return 1;
            case 1:
                return 4;
            case 2:
                return 2;
            case 3:
                return 3;
            default:
                return 0;
        }
    }

    public Date getSleepDate() {
        try {
            return DateUtils.parseISO8601(this.mRecordedAt);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class SleepRecordingMetaInnerObject {
        private int[] hypnogramDurations;
        private int[] hypnogramPhases;
        private double[] hypnogramSignalQuality;
        private int[] hypnogramSignalQualityDurations;

        SleepRecordingMetaInnerObject(int[] hypnogramPhases2, int[] hypnogramDurations2, double[] hypnogramSignalQuality2, int[] hypnogramSignalQualityDurations2) {
            this.hypnogramPhases = hypnogramPhases2;
            this.hypnogramDurations = hypnogramDurations2;
            this.hypnogramSignalQuality = hypnogramSignalQuality2;
            this.hypnogramSignalQualityDurations = hypnogramSignalQualityDurations2;
        }
    }
}
