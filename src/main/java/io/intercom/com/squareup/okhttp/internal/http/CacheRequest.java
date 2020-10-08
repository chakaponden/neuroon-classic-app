package io.intercom.com.squareup.okhttp.internal.http;

import io.intercom.okio.Sink;
import java.io.IOException;

public interface CacheRequest {
    void abort();

    Sink body() throws IOException;
}
