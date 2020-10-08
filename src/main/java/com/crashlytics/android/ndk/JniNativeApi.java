package com.crashlytics.android.ndk;

import android.content.res.AssetManager;
import com.crashlytics.android.BuildConfig;

class JniNativeApi implements NativeApi {
    private native boolean nativeInit(String str, Object obj);

    JniNativeApi() {
    }

    static {
        System.loadLibrary(BuildConfig.ARTIFACT_ID);
    }

    public boolean initialize(String crashFilePath, AssetManager assetManager) {
        return nativeInit(crashFilePath, assetManager);
    }
}
