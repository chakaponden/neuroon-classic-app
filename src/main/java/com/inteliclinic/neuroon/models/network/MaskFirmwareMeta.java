package com.inteliclinic.neuroon.models.network;

import com.google.gson.annotations.SerializedName;

public class MaskFirmwareMeta {
    @SerializedName("build")
    private int appVersion;
    @SerializedName("crc")
    private String crc;
    @SerializedName("length")
    private int length;
    @SerializedName("max_app_config")
    private int maxAppConfig;
    @SerializedName("min_app_config")
    private int minAppConfig;
    @SerializedName("version")
    private int version;

    public int getAppVersion() {
        return this.appVersion;
    }

    public void setAppVersion(int appVersion2) {
        this.appVersion = appVersion2;
    }

    public int getCrc() {
        return Integer.parseInt(this.crc);
    }

    public int getLength() {
        return this.length;
    }

    public int getMinAppConfig() {
        return this.minAppConfig;
    }

    public int getMaxAppConfig() {
        return this.maxAppConfig;
    }

    public int getVersion() {
        return this.version;
    }
}
