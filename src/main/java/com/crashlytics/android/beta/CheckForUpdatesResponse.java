package com.crashlytics.android.beta;

class CheckForUpdatesResponse {
    public final String buildVersion;
    public final String displayVersion;
    public final String instanceId;
    public final String packageName;
    public final String url;
    public final String versionString;

    public CheckForUpdatesResponse(String url2, String versionString2, String displayVersion2, String buildVersion2, String packageName2, String instanceId2) {
        this.url = url2;
        this.versionString = versionString2;
        this.displayVersion = displayVersion2;
        this.buildVersion = buildVersion2;
        this.packageName = packageName2;
        this.instanceId = instanceId2;
    }
}
