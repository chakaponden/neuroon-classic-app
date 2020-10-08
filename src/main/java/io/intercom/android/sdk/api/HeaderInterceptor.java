package io.intercom.android.sdk.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Base64;
import io.intercom.android.sdk.Bridge;
import io.intercom.com.squareup.okhttp.Interceptor;
import io.intercom.com.squareup.okhttp.Request;
import io.intercom.com.squareup.okhttp.Response;
import java.io.IOException;

public class HeaderInterceptor implements Interceptor {
    private static final String ANDROID_HEADER = "intercom-android-sdk/";
    private static final String AUTHORIZATION = "Authorization";
    private static final String CONTENT_TYPE_KEY = "Content-Type";
    private static final String CONTENT_TYPE_VALUE = "application/json";
    private static final String CORDOVA_HEADER = "intercom-sdk-cordova/";
    private static final String CORDOVA_PREFS = "intercomsdk_cordova_prefs";
    private static final String CORDOVA_VERSION = "cordova_version";
    private static final String INTERCOM_AGENT = "X-INTERCOM-AGENT";
    private static final String INTERCOM_AGENT_WRAPPER = "X-INTERCOM-AGENT-WRAPPER";
    private final String cordovaVersion;

    public HeaderInterceptor(Context context) {
        this.cordovaVersion = context.getSharedPreferences(CORDOVA_PREFS, 0).getString(CORDOVA_VERSION, "");
    }

    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request.Builder requestBuilder = chain.request().newBuilder().header("Content-Type", "application/json").header("Authorization", getBasicAuth()).header(INTERCOM_AGENT, "intercom-android-sdk/1.1.17");
        if (!this.cordovaVersion.isEmpty()) {
            requestBuilder.header(INTERCOM_AGENT_WRAPPER, CORDOVA_HEADER + this.cordovaVersion);
        }
        return chain.proceed(requestBuilder.build());
    }

    private String getBasicAuth() {
        return "Basic " + Base64.encodeToString((Bridge.getIdentityStore().getAppId() + ":" + Bridge.getIdentityStore().getApiKey()).getBytes(), 2);
    }

    @SuppressLint({"CommitPrefEdits"})
    public static void setCordovaVersion(Context context, String cordovaVersion2) {
        context.getSharedPreferences(CORDOVA_PREFS, 0).edit().putString(CORDOVA_VERSION, cordovaVersion2).commit();
    }
}
