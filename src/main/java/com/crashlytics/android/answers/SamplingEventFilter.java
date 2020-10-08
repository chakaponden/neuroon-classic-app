package com.crashlytics.android.answers;

import com.crashlytics.android.answers.SessionEvent;
import java.util.HashSet;
import java.util.Set;

class SamplingEventFilter implements EventFilter {
    static final Set<SessionEvent.Type> EVENTS_TYPE_TO_SAMPLE = new HashSet<SessionEvent.Type>() {
        {
            add(SessionEvent.Type.START);
            add(SessionEvent.Type.RESUME);
            add(SessionEvent.Type.PAUSE);
            add(SessionEvent.Type.STOP);
        }
    };
    final int samplingRate;

    public SamplingEventFilter(int samplingRate2) {
        this.samplingRate = samplingRate2;
    }

    public boolean skipEvent(SessionEvent sessionEvent) {
        boolean canBeSampled;
        boolean isSampledId;
        if (!EVENTS_TYPE_TO_SAMPLE.contains(sessionEvent.type) || sessionEvent.sessionEventMetadata.betaDeviceToken != null) {
            canBeSampled = false;
        } else {
            canBeSampled = true;
        }
        if (Math.abs(sessionEvent.sessionEventMetadata.installationId.hashCode() % this.samplingRate) != 0) {
            isSampledId = true;
        } else {
            isSampledId = false;
        }
        if (!canBeSampled || !isSampledId) {
            return false;
        }
        return true;
    }
}
