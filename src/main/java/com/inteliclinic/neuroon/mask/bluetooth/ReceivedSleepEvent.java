package com.inteliclinic.neuroon.mask.bluetooth;

import com.inteliclinic.neuroon.mask.MaskManager;
import com.inteliclinic.neuroon.models.bluetooth.BleDataFrame;
import com.inteliclinic.neuroon.models.data.Sleep;
import java.util.List;

public class ReceivedSleepEvent {
    private static final String TAG = ReceivedSleepEvent.class.getSimpleName();
    private final boolean mSkip;
    private List<BleDataFrame> sleepData;
    private final int sleepNum;

    public ReceivedSleepEvent(int sleepNum2, List<BleDataFrame> sleepData2, boolean skip) {
        this.sleepNum = sleepNum2;
        this.sleepData = sleepData2;
        this.mSkip = skip;
    }

    public Sleep getSleep() {
        byte[] sleepDataByte = new byte[(this.sleepData.size() * 20)];
        int i = 0;
        for (BleDataFrame data : this.sleepData) {
            System.arraycopy(data.getRawData(), 0, sleepDataByte, i * 20, 20);
            i++;
        }
        return Sleep.fromData(sleepDataByte, MaskManager.getInstance().getAppVersion());
    }

    public boolean shouldSkip() {
        return this.mSkip;
    }

    public int getSleepNum() {
        return this.sleepNum;
    }
}
