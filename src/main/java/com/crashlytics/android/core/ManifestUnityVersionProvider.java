package com.crashlytics.android.core;

import android.content.Context;
import android.os.Bundle;

class ManifestUnityVersionProvider implements UnityVersionProvider {
    static final String FABRIC_UNITY_CRASHLYTICS_VERSION_KEY = "io.fabric.unity.crashlytics.version";
    private final Context context;
    private final String packageName;

    public ManifestUnityVersionProvider(Context context2, String packageName2) {
        this.context = context2;
        this.packageName = packageName2;
    }

    public String getUnityVersion() {
        try {
            Bundle metaData = this.context.getPackageManager().getApplicationInfo(this.packageName, 128).metaData;
            if (metaData != null) {
                return metaData.getString(FABRIC_UNITY_CRASHLYTICS_VERSION_KEY);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
