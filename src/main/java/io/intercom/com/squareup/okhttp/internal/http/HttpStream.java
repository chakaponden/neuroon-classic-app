package io.intercom.com.squareup.okhttp.internal.http;

import io.intercom.com.squareup.okhttp.Request;
import io.intercom.com.squareup.okhttp.Response;
import io.intercom.com.squareup.okhttp.ResponseBody;
import io.intercom.okio.Sink;
import java.io.IOException;

public interface HttpStream {
    public static final int DISCARD_STREAM_TIMEOUT_MILLIS = 100;

    void cancel();

    Sink createRequestBody(Request request, long j) throws IOException;

    void finishRequest() throws IOException;

    ResponseBody openResponseBody(Response response) throws IOException;

    Response.Builder readResponseHeaders() throws IOException;

    void setHttpEngine(HttpEngine httpEngine);

    void writeRequestBody(RetryableSink retryableSink) throws IOException;

    void writeRequestHeaders(Request request) throws IOException;
}