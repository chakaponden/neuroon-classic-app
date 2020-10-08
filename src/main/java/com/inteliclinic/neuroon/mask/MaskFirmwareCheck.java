package com.inteliclinic.neuroon.mask;

import com.inteliclinic.neuroon.models.network.MaskFirmwareMeta;

public class MaskFirmwareCheck {
    private final MaskFirmwareMeta firmwareMeta;
    private boolean isAvailable;

    public MaskFirmwareCheck(boolean isAvailable2) {
        this.isAvailable = isAvailable2;
        this.firmwareMeta = null;
    }

    public MaskFirmwareCheck(MaskFirmwareMeta firmwareMeta2) {
        this.firmwareMeta = firmwareMeta2;
        this.isAvailable = true;
    }

    public boolean isNewAvailable() {
        if (this.isAvailable && this.firmwareMeta.getAppVersion() > MaskManager.getInstance().getAppVersion()) {
            return true;
        }
        return false;
    }

    public int getAppVersion() {
        return this.firmwareMeta.getAppVersion();
    }

    public MaskFirmwareMeta getUpdateInfo() {
        return this.firmwareMeta;
    }
}
