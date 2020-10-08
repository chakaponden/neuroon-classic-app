package io.intercom.retrofit.client;

import io.fabric.sdk.android.services.network.HttpRequest;
import io.intercom.retrofit.mime.TypedInput;
import io.intercom.retrofit.mime.TypedOutput;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UrlConnectionClient implements Client {
    private static final int CHUNK_SIZE = 4096;

    public Response execute(Request request) throws IOException {
        HttpURLConnection connection = openConnection(request);
        prepareRequest(connection, request);
        return readResponse(connection);
    }

    /* access modifiers changed from: protected */
    public HttpURLConnection openConnection(Request request) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(request.getUrl()).openConnection();
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(20000);
        return connection;
    }

    /* access modifiers changed from: package-private */
    public void prepareRequest(HttpURLConnection connection, Request request) throws IOException {
        connection.setRequestMethod(request.getMethod());
        connection.setDoInput(true);
        for (Header header : request.getHeaders()) {
            connection.addRequestProperty(header.getName(), header.getValue());
        }
        TypedOutput body = request.getBody();
        if (body != null) {
            connection.setDoOutput(true);
            connection.addRequestProperty(HttpRequest.HEADER_CONTENT_TYPE, body.mimeType());
            long length = body.length();
            if (length != -1) {
                connection.setFixedLengthStreamingMode((int) length);
                connection.addRequestProperty(HttpRequest.HEADER_CONTENT_LENGTH, String.valueOf(length));
            } else {
                connection.setChunkedStreamingMode(4096);
            }
            body.writeTo(connection.getOutputStream());
        }
    }

    /* access modifiers changed from: package-private */
    public Response readResponse(HttpURLConnection connection) throws IOException {
        InputStream stream;
        int status = connection.getResponseCode();
        String reason = connection.getResponseMessage();
        if (reason == null) {
            reason = "";
        }
        List<Header> headers = new ArrayList<>();
        for (Map.Entry<String, List<String>> field : connection.getHeaderFields().entrySet()) {
            String name = field.getKey();
            for (String value : field.getValue()) {
                headers.add(new Header(name, value));
            }
        }
        String mimeType = connection.getContentType();
        int length = connection.getContentLength();
        if (status >= 400) {
            stream = connection.getErrorStream();
        } else {
            stream = connection.getInputStream();
        }
        return new Response(connection.getURL().toString(), status, reason, headers, new TypedInputStream(mimeType, (long) length, stream));
    }

    private static class TypedInputStream implements TypedInput {
        private final long length;
        private final String mimeType;
        private final InputStream stream;

        private TypedInputStream(String mimeType2, long length2, InputStream stream2) {
            this.mimeType = mimeType2;
            this.length = length2;
            this.stream = stream2;
        }

        public String mimeType() {
            return this.mimeType;
        }

        public long length() {
            return this.length;
        }

        public InputStream in() throws IOException {
            return this.stream;
        }
    }
}
