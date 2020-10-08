package io.fabric.sdk.android.services.settings;

public class AppSettingsData {
    public static final String STATUS_ACTIVATED = "activated";
    public static final String STATUS_CONFIGURED = "configured";
    public static final String STATUS_NEW = "new";
    public final AppIconSettingsData icon;
    public final String identifier;
    public final String reportsUrl;
    public final String status;
    public final boolean updateRequired;
    public final String url;

    public AppSettingsData(String identifier2, String status2, String url2, String reportsUrl2, boolean updateRequired2, AppIconSettingsData icon2) {
        this.identifier = identifier2;
        this.status = status2;
        this.url = url2;
        this.reportsUrl = reportsUrl2;
        this.updateRequired = updateRequired2;
        this.icon = icon2;
    }
}
