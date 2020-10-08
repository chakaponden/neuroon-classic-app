package com.squareup.leakcanary;

import android.app.Application;

public final class LeakCanary {
    public static RefWatcher install(Application application) {
        return RefWatcher.DISABLED;
    }

    private LeakCanary() {
        throw new AssertionError();
    }
}
