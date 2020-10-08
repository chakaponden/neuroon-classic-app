package io.fabric.sdk.android.services.persistence;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import io.fabric.sdk.android.Kit;

public class PreferenceStoreImpl implements PreferenceStore {
    private final Context context;
    private final String preferenceName;
    private final SharedPreferences sharedPreferences;

    public PreferenceStoreImpl(Context context2, String name) {
        if (context2 == null) {
            throw new IllegalStateException("Cannot get directory before context has been set. Call Fabric.with() first");
        }
        this.context = context2;
        this.preferenceName = name;
        this.sharedPreferences = this.context.getSharedPreferences(this.preferenceName, 0);
    }

    @Deprecated
    public PreferenceStoreImpl(Kit kit) {
        this(kit.getContext(), kit.getClass().getName());
    }

    public SharedPreferences get() {
        return this.sharedPreferences;
    }

    public SharedPreferences.Editor edit() {
        return this.sharedPreferences.edit();
    }

    @TargetApi(9)
    public boolean save(SharedPreferences.Editor editor) {
        if (Build.VERSION.SDK_INT < 9) {
            return editor.commit();
        }
        editor.apply();
        return true;
    }
}
