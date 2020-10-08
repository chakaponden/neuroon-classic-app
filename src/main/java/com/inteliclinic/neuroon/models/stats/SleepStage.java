package com.inteliclinic.neuroon.models.stats;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class SleepStage {
    public static final int STAGE_AWAKE = 1;
    public static final int STAGE_DEEP = 4;
    public static final int STAGE_LIGHT = 2;
    public static final int STAGE_REM = 3;
    private int mStage;
    private int mStageValue;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Stage {
    }

    public SleepStage(int stage, int stageLength) {
        this.mStage = stage;
        this.mStageValue = stageLength;
    }

    public void increaseLength() {
        this.mStageValue++;
    }

    public int getStage() {
        return this.mStage;
    }

    public int getStageLength() {
        return this.mStageValue;
    }
}
