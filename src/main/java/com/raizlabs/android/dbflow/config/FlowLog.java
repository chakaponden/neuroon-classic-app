package com.raizlabs.android.dbflow.config;

import android.os.Build;
import android.util.Log;

public class FlowLog {
    public static final String TAG = FlowLog.class.getSimpleName();
    private static Level level = Level.E;

    public enum Level {
        V {
            /* access modifiers changed from: package-private */
            public void call(String tag, String message, Throwable throwable) {
                Log.v(tag, message, throwable);
            }
        },
        D {
            /* access modifiers changed from: package-private */
            public void call(String tag, String message, Throwable throwable) {
                Log.d(tag, message, throwable);
            }
        },
        I {
            /* access modifiers changed from: package-private */
            public void call(String tag, String message, Throwable throwable) {
                Log.i(tag, message, throwable);
            }
        },
        W {
            /* access modifiers changed from: package-private */
            public void call(String tag, String message, Throwable throwable) {
                Log.w(tag, message, throwable);
            }
        },
        E {
            /* access modifiers changed from: package-private */
            public void call(String tag, String message, Throwable throwable) {
                Log.e(tag, message, throwable);
            }
        },
        WTF {
            /* access modifiers changed from: package-private */
            public void call(String tag, String message, Throwable throwable) {
                if (Build.VERSION.SDK_INT >= 8) {
                    Log.wtf(tag, message, throwable);
                } else {
                    Log.e(tag, "!!!!!!!!*******" + message + "********!!!!!!", throwable);
                }
            }
        };

        /* access modifiers changed from: package-private */
        public abstract void call(String str, String str2, Throwable th);
    }

    public static void setMinimumLoggingLevel(Level level2) {
        level = level2;
    }

    public static void log(Level level2, String message) {
        log(level2, message, (Throwable) null);
    }

    public static void log(Level level2, String message, Throwable throwable) {
        log(level2, TAG, message, throwable);
    }

    public static void log(Level level2, String tag, String message, Throwable throwable) {
        if (isEnabled(level2)) {
            level2.call(tag, message, throwable);
        }
    }

    public static boolean isEnabled(Level level2) {
        return level2.ordinal() >= level.ordinal();
    }

    public static void logError(Throwable throwable) {
        log(Level.E, throwable);
    }

    public static void log(Level level2, Throwable throwable) {
        log(level2, TAG, "", throwable);
    }
}
