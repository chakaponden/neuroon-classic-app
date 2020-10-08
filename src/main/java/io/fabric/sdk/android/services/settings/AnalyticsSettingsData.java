package io.fabric.sdk.android.services.settings;

public class AnalyticsSettingsData {
    public static final int DEFAULT_SAMPLING_RATE = 1;
    public final String analyticsURL;
    public final int flushIntervalSeconds;
    public final boolean flushOnBackground;
    public final int maxByteSizePerFile;
    public final int maxFileCountPerSend;
    public final int maxPendingSendFileCount;
    public final int samplingRate;
    public final boolean trackCustomEvents;
    public final boolean trackPredefinedEvents;

    public AnalyticsSettingsData(String analyticsURL2, int flushIntervalSeconds2, int maxByteSizePerFile2, int maxFileCountPerSend2, int maxPendingSendFileCount2, boolean trackCustomEvents2, boolean trackPredefinedEvents2, int samplingRate2, boolean flushOnBackground2) {
        this.analyticsURL = analyticsURL2;
        this.flushIntervalSeconds = flushIntervalSeconds2;
        this.maxByteSizePerFile = maxByteSizePerFile2;
        this.maxFileCountPerSend = maxFileCountPerSend2;
        this.maxPendingSendFileCount = maxPendingSendFileCount2;
        this.trackCustomEvents = trackCustomEvents2;
        this.trackPredefinedEvents = trackPredefinedEvents2;
        this.samplingRate = samplingRate2;
        this.flushOnBackground = flushOnBackground2;
    }

    @Deprecated
    public AnalyticsSettingsData(String analyticsURL2, int flushIntervalSeconds2, int maxByteSizePerFile2, int maxFileCountPerSend2, int maxPendingSendFileCount2, boolean trackCustomEvents2, int samplingRate2) {
        this(analyticsURL2, flushIntervalSeconds2, maxByteSizePerFile2, maxFileCountPerSend2, maxPendingSendFileCount2, trackCustomEvents2, true, samplingRate2, true);
    }

    @Deprecated
    public AnalyticsSettingsData(String analyticsURL2, int flushIntervalSeconds2, int maxByteSizePerFile2, int maxFileCountPerSend2, int maxPendingSendFileCount2, boolean trackCustomEvents2) {
        this(analyticsURL2, flushIntervalSeconds2, maxByteSizePerFile2, maxFileCountPerSend2, maxPendingSendFileCount2, trackCustomEvents2, true, 1, true);
    }
}
