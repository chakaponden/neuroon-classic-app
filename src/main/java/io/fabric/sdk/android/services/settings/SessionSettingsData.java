package io.fabric.sdk.android.services.settings;

public class SessionSettingsData {
    public final int identifierMask;
    public final int logBufferSize;
    public final int maxChainedExceptionDepth;
    public final int maxCustomExceptionEvents;
    public final int maxCustomKeyValuePairs;
    public final boolean sendSessionWithoutCrash;

    public SessionSettingsData(int logBufferSize2, int maxChainedExceptionDepth2, int maxCustomExceptionEvents2, int maxCustomKeyValuePairs2, int identifierMask2, boolean sendSessionWithoutCrash2) {
        this.logBufferSize = logBufferSize2;
        this.maxChainedExceptionDepth = maxChainedExceptionDepth2;
        this.maxCustomExceptionEvents = maxCustomExceptionEvents2;
        this.maxCustomKeyValuePairs = maxCustomKeyValuePairs2;
        this.identifierMask = identifierMask2;
        this.sendSessionWithoutCrash = sendSessionWithoutCrash2;
    }
}
