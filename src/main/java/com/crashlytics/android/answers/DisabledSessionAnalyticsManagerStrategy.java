package com.crashlytics.android.answers;

import com.crashlytics.android.answers.SessionEvent;
import io.fabric.sdk.android.services.settings.AnalyticsSettingsData;
import java.io.IOException;

class DisabledSessionAnalyticsManagerStrategy implements SessionAnalyticsManagerStrategy {
    DisabledSessionAnalyticsManagerStrategy() {
    }

    public void setAnalyticsSettingsData(AnalyticsSettingsData analyticsSettingsData, String protocolAndHostOverride) {
    }

    public void processEvent(SessionEvent.Builder builder) {
    }

    public void sendEvents() {
    }

    public void deleteAllEvents() {
    }

    public boolean rollFileOver() throws IOException {
        return false;
    }

    public void scheduleTimeBasedRollOverIfNeeded() {
    }

    public void cancelTimeBasedFileRollOver() {
    }
}
