package com.crashlytics.android.answers;

final class SessionEventMetadata {
    public final String advertisingId;
    public final String androidId;
    public final String appBundleId;
    public final String appVersionCode;
    public final String appVersionName;
    public final String betaDeviceToken;
    public final String buildId;
    public final String deviceModel;
    public final String executionId;
    public final String installationId;
    public final Boolean limitAdTrackingEnabled;
    public final String osVersion;
    private String stringRepresentation;

    public SessionEventMetadata(String appBundleId2, String executionId2, String installationId2, String androidId2, String advertisingId2, Boolean limitAdTrackingEnabled2, String betaDeviceToken2, String buildId2, String osVersion2, String deviceModel2, String appVersionCode2, String appVersionName2) {
        this.appBundleId = appBundleId2;
        this.executionId = executionId2;
        this.installationId = installationId2;
        this.androidId = androidId2;
        this.advertisingId = advertisingId2;
        this.limitAdTrackingEnabled = limitAdTrackingEnabled2;
        this.betaDeviceToken = betaDeviceToken2;
        this.buildId = buildId2;
        this.osVersion = osVersion2;
        this.deviceModel = deviceModel2;
        this.appVersionCode = appVersionCode2;
        this.appVersionName = appVersionName2;
    }

    public String toString() {
        if (this.stringRepresentation == null) {
            this.stringRepresentation = "appBundleId=" + this.appBundleId + ", executionId=" + this.executionId + ", installationId=" + this.installationId + ", androidId=" + this.androidId + ", advertisingId=" + this.advertisingId + ", limitAdTrackingEnabled=" + this.limitAdTrackingEnabled + ", betaDeviceToken=" + this.betaDeviceToken + ", buildId=" + this.buildId + ", osVersion=" + this.osVersion + ", deviceModel=" + this.deviceModel + ", appVersionCode=" + this.appVersionCode + ", appVersionName=" + this.appVersionName;
        }
        return this.stringRepresentation;
    }
}
