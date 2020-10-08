package com.inteliclinic.neuroon.models.stats;

import android.util.Pair;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class StagingData {
    private static final double MASK_SIGNAL_AVERAGE = 100.0d;
    private static final double MASK_SIGNAL_GOOD = 30.0d;
    public static final int NO_SIGNAL = -1;
    public static final int SIGNAL_BAD = 2;
    public static final int SIGNAL_GOOD = 0;
    public static final int SIGNAL_MEDIUM = 1;
    private List<SleepStage> hypnogram = new ArrayList();
    private List<SignalQuality> signalQuality2 = new ArrayList();

    @Retention(RetentionPolicy.SOURCE)
    public @interface SignalQualityLevel {
    }

    public static StagingData createStagingFrom(Double[] hypnogram2, Double[] signalQuality) {
        StagingData data = new StagingData();
        SleepStage lastStage = null;
        for (Double hypno : hypnogram2) {
            if (lastStage == null || lastStage.getStage() != hypno.intValue()) {
                lastStage = new SleepStage(hypno.intValue(), 1);
                data.hypnogram.add(lastStage);
            } else {
                lastStage.increaseLength();
            }
        }
        SignalQuality lastSignalQuality = null;
        for (Double quality : signalQuality) {
            if (lastSignalQuality == null || lastSignalQuality.getQuality() != quality.doubleValue()) {
                lastSignalQuality = new SignalQuality(quality.doubleValue(), 1);
                data.signalQuality2.add(lastSignalQuality);
            } else {
                lastSignalQuality.increaseLength();
            }
        }
        return data;
    }

    public static double[][] convertToDB(StagingData stagingData) {
        if (stagingData == null || stagingData.hypnogram == null || stagingData.signalQuality2 == null) {
            return null;
        }
        double[][] data = new double[4][];
        int size1 = stagingData.hypnogram.size();
        data[0] = new double[size1];
        data[1] = new double[size1];
        int i = 0;
        for (SleepStage sleepStage : stagingData.hypnogram) {
            data[0][i] = (double) sleepStage.getStage();
            data[1][i] = (double) sleepStage.getStageLength();
            i++;
        }
        int size2 = stagingData.signalQuality2.size();
        data[2] = new double[size2];
        data[3] = new double[size2];
        int j = 0;
        for (SignalQuality signalQuality : stagingData.signalQuality2) {
            data[2][j] = signalQuality.getQuality();
            data[3][j] = (double) signalQuality.getLength();
            j++;
        }
        return data;
    }

    public static StagingData convertFromDb(double[][] hypnogram2) {
        if (hypnogram2 == null) {
            return null;
        }
        StagingData stagingData = new StagingData();
        for (int i = 0; i < hypnogram2[0].length; i++) {
            stagingData.hypnogram.add(new SleepStage((int) hypnogram2[0][i], (int) hypnogram2[1][i]));
        }
        for (int i2 = 0; i2 < hypnogram2[2].length; i2++) {
            stagingData.signalQuality2.add(new SignalQuality(hypnogram2[2][i2], (int) hypnogram2[3][i2]));
        }
        return stagingData;
    }

    public List<SleepStage> getHypnogram() {
        return this.hypnogram;
    }

    public int getSleepSignalQualityLevel() {
        if (this.signalQuality2 != null) {
            long signalLength = 0;
            long signalBad = 0;
            for (SignalQuality quality : this.signalQuality2) {
                if (quality.getQuality() > MASK_SIGNAL_AVERAGE) {
                    signalBad += (long) quality.getLength();
                }
                signalLength += (long) quality.getLength();
            }
            if (signalLength != 0) {
                float ratioQuality = ((float) signalBad) / ((float) signalLength);
                if (((double) ratioQuality) > 0.5d) {
                    return 2;
                }
                if (((double) ratioQuality) > 0.2d) {
                    return 1;
                }
                return 0;
            }
        }
        return -1;
    }

    public List<Pair<Integer, Integer>> getSleepSignalQuality() {
        List<Pair<Integer, Integer>> ret = new ArrayList<>();
        if (this.signalQuality2 != null) {
            for (SignalQuality quality : this.signalQuality2) {
                if (quality.getQuality() > MASK_SIGNAL_AVERAGE) {
                    ret.add(new Pair(Integer.valueOf(quality.getLength()), 2));
                } else if (quality.getQuality() > MASK_SIGNAL_GOOD) {
                    ret.add(new Pair(Integer.valueOf(quality.getLength()), 1));
                } else {
                    ret.add(new Pair(Integer.valueOf(quality.getLength()), 0));
                }
            }
        }
        return ret;
    }
}
