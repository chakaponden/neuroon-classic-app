package io.intercom.com.squareup.okhttp.internal;

import io.intercom.com.squareup.okhttp.Request;
import io.intercom.com.squareup.okhttp.Response;
import io.intercom.com.squareup.okhttp.internal.http.CacheRequest;
import io.intercom.com.squareup.okhttp.internal.http.CacheStrategy;
import java.io.IOException;

public interface InternalCache {
    Response get(Request request) throws IOException;

    CacheRequest put(Response response) throws IOException;

    void remove(Request request) throws IOException;

    void trackConditionalCacheHit();

    void trackResponse(CacheStrategy cacheStrategy);

    void update(Response response, Response response2) throws IOException;
}
