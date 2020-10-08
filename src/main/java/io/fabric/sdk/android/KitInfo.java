package io.fabric.sdk.android;

public class KitInfo {
    private final String buildType;
    private final String identifier;
    private final String version;

    public KitInfo(String identifier2, String version2, String buildType2) {
        this.identifier = identifier2;
        this.version = version2;
        this.buildType = buildType2;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String getVersion() {
        return this.version;
    }

    public String getBuildType() {
        return this.buildType;
    }
}
