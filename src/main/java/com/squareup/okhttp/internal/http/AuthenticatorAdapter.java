package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Challenge;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.IOException;
import java.net.Authenticator;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.List;

public final class AuthenticatorAdapter implements Authenticator {
    public static final Authenticator INSTANCE = new AuthenticatorAdapter();

    public Request authenticate(Proxy proxy, Response response) throws IOException {
        PasswordAuthentication auth;
        List<Challenge> challenges = response.challenges();
        Request request = response.request();
        HttpUrl url = request.httpUrl();
        int size = challenges.size();
        for (int i = 0; i < size; i++) {
            Challenge challenge = challenges.get(i);
            if ("Basic".equalsIgnoreCase(challenge.getScheme()) && (auth = java.net.Authenticator.requestPasswordAuthentication(url.host(), getConnectToInetAddress(proxy, url), url.port(), url.scheme(), challenge.getRealm(), challenge.getScheme(), url.url(), Authenticator.RequestorType.SERVER)) != null) {
                return request.newBuilder().header(HttpRequest.HEADER_AUTHORIZATION, Credentials.basic(auth.getUserName(), new String(auth.getPassword()))).build();
            }
        }
        return null;
    }

    public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
        List<Challenge> challenges = response.challenges();
        Request request = response.request();
        HttpUrl url = request.httpUrl();
        int size = challenges.size();
        for (int i = 0; i < size; i++) {
            Challenge challenge = challenges.get(i);
            if ("Basic".equalsIgnoreCase(challenge.getScheme())) {
                InetSocketAddress proxyAddress = (InetSocketAddress) proxy.address();
                PasswordAuthentication auth = java.net.Authenticator.requestPasswordAuthentication(proxyAddress.getHostName(), getConnectToInetAddress(proxy, url), proxyAddress.getPort(), url.scheme(), challenge.getRealm(), challenge.getScheme(), url.url(), Authenticator.RequestorType.PROXY);
                if (auth != null) {
                    return request.newBuilder().header(HttpRequest.HEADER_PROXY_AUTHORIZATION, Credentials.basic(auth.getUserName(), new String(auth.getPassword()))).build();
                }
            }
        }
        return null;
    }

    private InetAddress getConnectToInetAddress(Proxy proxy, HttpUrl url) throws IOException {
        if (proxy == null || proxy.type() == Proxy.Type.DIRECT) {
            return InetAddress.getByName(url.host());
        }
        return ((InetSocketAddress) proxy.address()).getAddress();
    }
}
