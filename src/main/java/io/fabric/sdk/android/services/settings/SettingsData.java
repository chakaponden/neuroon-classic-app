package io.fabric.sdk.android.services.settings;

public class SettingsData {
    public final AnalyticsSettingsData analyticsSettingsData;
    public final AppSettingsData appData;
    public final BetaSettingsData betaSettingsData;
    public final int cacheDuration;
    public final long expiresAtMillis;
    public final FeaturesSettingsData featuresData;
    public final PromptSettingsData promptData;
    public final SessionSettingsData sessionData;
    public final int settingsVersion;

    public SettingsData(long expiresAtMillis2, AppSettingsData appData2, SessionSettingsData sessionData2, PromptSettingsData promptData2, FeaturesSettingsData featuresData2, AnalyticsSettingsData analyticsSettingsData2, BetaSettingsData betaSettingsData2, int settingsVersion2, int cacheDuration2) {
        this.expiresAtMillis = expiresAtMillis2;
        this.appData = appData2;
        this.sessionData = sessionData2;
        this.promptData = promptData2;
        this.featuresData = featuresData2;
        this.settingsVersion = settingsVersion2;
        this.cacheDuration = cacheDuration2;
        this.analyticsSettingsData = analyticsSettingsData2;
        this.betaSettingsData = betaSettingsData2;
    }

    public boolean isExpired(long currentTimeMillis) {
        return this.expiresAtMillis < currentTimeMillis;
    }
}
