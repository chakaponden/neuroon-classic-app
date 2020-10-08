package io.fabric.sdk.android.services.settings;

import io.fabric.sdk.android.KitInfo;
import java.util.Collection;

public class AppRequestData {
    public final String apiKey;
    public final String appId;
    public final String buildVersion;
    public final String builtSdkVersion;
    public final String displayVersion;
    public final IconRequest icon;
    public final String instanceIdentifier;
    public final String minSdkVersion;
    public final String name;
    public final Collection<KitInfo> sdkKits;
    public final int source;

    public AppRequestData(String apiKey2, String appId2, String displayVersion2, String buildVersion2, String instanceIdentifier2, String name2, int source2, String minSdkVersion2, String builtSdkVersion2, IconRequest icon2, Collection<KitInfo> sdkKits2) {
        this.apiKey = apiKey2;
        this.appId = appId2;
        this.displayVersion = displayVersion2;
        this.buildVersion = buildVersion2;
        this.instanceIdentifier = instanceIdentifier2;
        this.name = name2;
        this.source = source2;
        this.minSdkVersion = minSdkVersion2;
        this.builtSdkVersion = builtSdkVersion2;
        this.icon = icon2;
        this.sdkKits = sdkKits2;
    }
}
