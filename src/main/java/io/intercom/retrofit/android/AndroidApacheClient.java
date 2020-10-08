package io.intercom.retrofit.android;

import android.net.http.AndroidHttpClient;
import io.intercom.retrofit.client.ApacheClient;

public final class AndroidApacheClient extends ApacheClient {
    public AndroidApacheClient() {
        super(AndroidHttpClient.newInstance("Retrofit"));
    }
}
