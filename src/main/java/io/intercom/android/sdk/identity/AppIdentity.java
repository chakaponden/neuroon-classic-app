package io.intercom.android.sdk.identity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import io.intercom.android.sdk.models.Config;
import io.intercom.android.sdk.utilities.Constants;
import io.intercom.com.google.gson.Gson;

public class AppIdentity {
    private static final String PREFS_API_KEY = "ApiKey";
    private static final String PREFS_APP_ID = "AppId";
    private String apiKey = this.prefs.getString(PREFS_API_KEY, "");
    private final AppConfig appConfig;
    private String appId = this.prefs.getString(PREFS_APP_ID, "");
    private final SharedPreferences prefs;

    public AppIdentity(Context context) {
        this.prefs = context.getSharedPreferences(Constants.INTERCOM_PREFS, 0);
        this.appConfig = new AppConfig(context, new Gson());
    }

    /* access modifiers changed from: protected */
    public void update(String apiKey2, String appId2) {
        SharedPreferences.Editor editor = this.prefs.edit();
        this.apiKey = apiKey2;
        this.appId = appId2;
        editor.putString(PREFS_API_KEY, apiKey2);
        editor.putString(PREFS_APP_ID, appId2);
        editor.apply();
    }

    /* access modifiers changed from: protected */
    public void updateAppConfig(Config config) {
        this.appConfig.update(config);
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public String getAppId() {
        return this.appId;
    }

    public AppConfig getAppConfig() {
        return this.appConfig;
    }

    /* access modifiers changed from: protected */
    public boolean appIdentityExists() {
        return !this.apiKey.isEmpty() && !this.appId.isEmpty();
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"CommitPrefEdits"})
    public void clear() {
        this.prefs.edit().clear().commit();
        this.apiKey = "";
        this.appId = "";
    }
}
