package io.fabric.sdk.android.services.network;

import io.fabric.sdk.android.DefaultLogger;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Logger;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

public class DefaultHttpRequestFactory implements HttpRequestFactory {
    private static final String HTTPS = "https";
    private boolean attemptedSslInit;
    private final Logger logger;
    private PinningInfoProvider pinningInfo;
    private SSLSocketFactory sslSocketFactory;

    public DefaultHttpRequestFactory() {
        this(new DefaultLogger());
    }

    public DefaultHttpRequestFactory(Logger logger2) {
        this.logger = logger2;
    }

    public PinningInfoProvider getPinningInfoProvider() {
        return this.pinningInfo;
    }

    public void setPinningInfoProvider(PinningInfoProvider pinningInfo2) {
        if (this.pinningInfo != pinningInfo2) {
            this.pinningInfo = pinningInfo2;
            resetSSLSocketFactory();
        }
    }

    private synchronized void resetSSLSocketFactory() {
        this.attemptedSslInit = false;
        this.sslSocketFactory = null;
    }

    public HttpRequest buildHttpRequest(HttpMethod method, String url) {
        return buildHttpRequest(method, url, Collections.emptyMap());
    }

    public HttpRequest buildHttpRequest(HttpMethod method, String url, Map<String, String> queryParams) {
        HttpRequest httpRequest;
        SSLSocketFactory sslSocketFactory2;
        switch (method) {
            case GET:
                httpRequest = HttpRequest.get((CharSequence) url, (Map<?, ?>) queryParams, true);
                break;
            case POST:
                httpRequest = HttpRequest.post((CharSequence) url, (Map<?, ?>) queryParams, true);
                break;
            case PUT:
                httpRequest = HttpRequest.put((CharSequence) url);
                break;
            case DELETE:
                httpRequest = HttpRequest.delete((CharSequence) url);
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP method!");
        }
        if (!(!isHttps(url) || this.pinningInfo == null || (sslSocketFactory2 = getSSLSocketFactory()) == null)) {
            ((HttpsURLConnection) httpRequest.getConnection()).setSSLSocketFactory(sslSocketFactory2);
        }
        return httpRequest;
    }

    private boolean isHttps(String url) {
        return url != null && url.toLowerCase(Locale.US).startsWith(HTTPS);
    }

    private synchronized SSLSocketFactory getSSLSocketFactory() {
        if (this.sslSocketFactory == null && !this.attemptedSslInit) {
            this.sslSocketFactory = initSSLSocketFactory();
        }
        return this.sslSocketFactory;
    }

    private synchronized SSLSocketFactory initSSLSocketFactory() {
        SSLSocketFactory sslSocketFactory2;
        this.attemptedSslInit = true;
        try {
            sslSocketFactory2 = NetworkUtils.getSSLSocketFactory(this.pinningInfo);
            this.logger.d(Fabric.TAG, "Custom SSL pinning enabled");
        } catch (Exception e) {
            this.logger.e(Fabric.TAG, "Exception while validating pinned certs", e);
            sslSocketFactory2 = null;
        }
        return sslSocketFactory2;
    }
}
