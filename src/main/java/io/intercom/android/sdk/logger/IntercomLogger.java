package io.intercom.android.sdk.logger;

import android.util.Log;

public class IntercomLogger {
    public static final int ASSERT = 7;
    public static final int DEBUG = 3;
    public static final int DISABLED = -1;
    public static final int ERROR = 6;
    public static final int INFO = 4;
    private static final String TAG = "Intercom-Android";
    public static final int VERBOSE = 2;
    public static final int WARN = 5;
    private static int logLevel = 5;

    public static void ERROR(String text) {
        if (logLevel != -1 && logLevel <= 6) {
            Log.e(TAG, "ERROR: " + text);
        }
    }

    public static void WARNING(String text) {
        if (logLevel != -1 && logLevel <= 5) {
            Log.w(TAG, "WARNING: " + text);
        }
    }

    public static void DEBUG(String text) {
        if (logLevel != -1 && logLevel <= 3) {
            Log.d(TAG, "DEBUG: " + text);
        }
    }

    public static void INFO(String text) {
        if (logLevel != -1 && logLevel <= 4) {
            Log.i(TAG, "INFORMATION: " + text);
        }
    }

    public static void INTERNAL(String text) {
        INTERNAL(TAG, text);
    }

    public static void INTERNAL(String tag, String text) {
    }

    public static void setLogLevel(int logLevel2) {
        logLevel = logLevel2;
    }
}
