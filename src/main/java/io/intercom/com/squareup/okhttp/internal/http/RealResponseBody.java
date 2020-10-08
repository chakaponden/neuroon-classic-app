package io.intercom.com.squareup.okhttp.internal.http;

import io.fabric.sdk.android.services.network.HttpRequest;
import io.intercom.com.squareup.okhttp.Headers;
import io.intercom.com.squareup.okhttp.MediaType;
import io.intercom.com.squareup.okhttp.ResponseBody;
import io.intercom.okio.BufferedSource;

public final class RealResponseBody extends ResponseBody {
    private final Headers headers;
    private final BufferedSource source;

    public RealResponseBody(Headers headers2, BufferedSource source2) {
        this.headers = headers2;
        this.source = source2;
    }

    public MediaType contentType() {
        String contentType = this.headers.get(HttpRequest.HEADER_CONTENT_TYPE);
        if (contentType != null) {
            return MediaType.parse(contentType);
        }
        return null;
    }

    public long contentLength() {
        return OkHeaders.contentLength(this.headers);
    }

    public BufferedSource source() {
        return this.source;
    }
}
