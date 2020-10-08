package io.fabric.sdk.android.services.settings;

public class SettingsRequest {
    public final String advertisingId;
    public final String androidId;
    public final String apiKey;
    public final String buildVersion;
    public final String deviceModel;
    public final String displayVersion;
    public final String iconHash;
    public final String installationId;
    public final String instanceId;
    public final String osBuildVersion;
    public final String osDisplayVersion;
    public final int source;

    public SettingsRequest(String apiKey2, String deviceModel2, String osBuildVersion2, String osDisplayVersion2, String advertisingId2, String installationId2, String androidId2, String instanceId2, String displayVersion2, String buildVersion2, int source2, String iconHash2) {
        this.apiKey = apiKey2;
        this.deviceModel = deviceModel2;
        this.osBuildVersion = osBuildVersion2;
        this.osDisplayVersion = osDisplayVersion2;
        this.advertisingId = advertisingId2;
        this.installationId = installationId2;
        this.androidId = androidId2;
        this.instanceId = instanceId2;
        this.displayVersion = displayVersion2;
        this.buildVersion = buildVersion2;
        this.source = source2;
        this.iconHash = iconHash2;
    }
}
