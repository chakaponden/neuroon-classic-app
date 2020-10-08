package io.fabric.sdk.android.services.settings;

public class FeaturesSettingsData {
    public final boolean collectAnalytics;
    public final boolean collectLoggedException;
    public final boolean collectReports;
    public final boolean promptEnabled;

    public FeaturesSettingsData(boolean promptEnabled2, boolean collectLoggedException2, boolean collectReports2, boolean collectAnalytics2) {
        this.promptEnabled = promptEnabled2;
        this.collectLoggedException = collectLoggedException2;
        this.collectReports = collectReports2;
        this.collectAnalytics = collectAnalytics2;
    }
}
