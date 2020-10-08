package com.inteliclinic.neuroon.models.stats;

public class SignalQuality {
    private final double mQuality;
    private int mQualityLength;

    public SignalQuality(double quality, int qualityLength) {
        this.mQuality = quality;
        this.mQualityLength = qualityLength;
    }

    public void increaseLength() {
        this.mQualityLength++;
    }

    public double getQuality() {
        return this.mQuality;
    }

    public int getLength() {
        return this.mQualityLength;
    }
}
