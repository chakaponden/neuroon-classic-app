package com.crashlytics.android.core;

class CreateReportRequest {
    public final String apiKey;
    public final Report report;

    public CreateReportRequest(String apiKey2, Report report2) {
        this.apiKey = apiKey2;
        this.report = report2;
    }
}
