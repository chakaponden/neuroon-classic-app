package io.intercom.android.sdk.nexus;

import android.util.Log;

final class NexusLogger {
    private static final String TAG = "intercom-nexus";
    private static boolean loggingEnabled = false;

    NexusLogger() {
    }

    protected static void d(String log) {
        if (loggingEnabled) {
            Log.d(TAG, log);
        }
    }

    protected static void v(String log) {
        if (loggingEnabled) {
            Log.v(TAG, log);
        }
    }

    static void errorLog(String log) {
        if (loggingEnabled) {
            Log.e(TAG, log);
        }
    }

    static void errorLog(String log, Exception e) {
        if (loggingEnabled) {
            Log.e(TAG, log);
            e.printStackTrace();
        }
    }

    static void setLoggingEnabled(boolean loggingEnabled2) {
        loggingEnabled = loggingEnabled2;
    }
}
