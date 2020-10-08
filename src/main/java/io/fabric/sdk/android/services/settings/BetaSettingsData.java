package io.fabric.sdk.android.services.settings;

public class BetaSettingsData {
    public final int updateSuspendDurationSeconds;
    public final String updateUrl;

    public BetaSettingsData(String updateUrl2, int updateSuspendDurationSeconds2) {
        this.updateUrl = updateUrl2;
        this.updateSuspendDurationSeconds = updateSuspendDurationSeconds2;
    }
}
