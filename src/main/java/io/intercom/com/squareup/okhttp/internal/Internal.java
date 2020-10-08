package io.intercom.com.squareup.okhttp.internal;

import io.intercom.com.squareup.okhttp.Address;
import io.intercom.com.squareup.okhttp.Call;
import io.intercom.com.squareup.okhttp.Callback;
import io.intercom.com.squareup.okhttp.ConnectionPool;
import io.intercom.com.squareup.okhttp.ConnectionSpec;
import io.intercom.com.squareup.okhttp.Headers;
import io.intercom.com.squareup.okhttp.HttpUrl;
import io.intercom.com.squareup.okhttp.OkHttpClient;
import io.intercom.com.squareup.okhttp.internal.http.StreamAllocation;
import io.intercom.com.squareup.okhttp.internal.io.RealConnection;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;

public abstract class Internal {
    public static Internal instance;
    public static final Logger logger = Logger.getLogger(OkHttpClient.class.getName());

    public abstract void addLenient(Headers.Builder builder, String str);

    public abstract void addLenient(Headers.Builder builder, String str, String str2);

    public abstract void apply(ConnectionSpec connectionSpec, SSLSocket sSLSocket, boolean z);

    public abstract StreamAllocation callEngineGetStreamAllocation(Call call);

    public abstract void callEnqueue(Call call, Callback callback, boolean z);

    public abstract boolean connectionBecameIdle(ConnectionPool connectionPool, RealConnection realConnection);

    public abstract RealConnection get(ConnectionPool connectionPool, Address address, StreamAllocation streamAllocation);

    public abstract HttpUrl getHttpUrlChecked(String str) throws MalformedURLException, UnknownHostException;

    public abstract InternalCache internalCache(OkHttpClient okHttpClient);

    public abstract void put(ConnectionPool connectionPool, RealConnection realConnection);

    public abstract RouteDatabase routeDatabase(ConnectionPool connectionPool);

    public abstract void setCache(OkHttpClient okHttpClient, InternalCache internalCache);

    public static void initializeInstanceForTests() {
        new OkHttpClient();
    }
}
