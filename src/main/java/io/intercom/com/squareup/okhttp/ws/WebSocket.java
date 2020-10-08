package io.intercom.com.squareup.okhttp.ws;

import io.intercom.com.squareup.okhttp.MediaType;
import io.intercom.com.squareup.okhttp.RequestBody;
import io.intercom.okio.Buffer;
import java.io.IOException;

public interface WebSocket {
    public static final MediaType BINARY = MediaType.parse("application/vnd.okhttp.websocket+binary");
    public static final MediaType TEXT = MediaType.parse("application/vnd.okhttp.websocket+text; charset=utf-8");

    void close(int i, String str) throws IOException;

    void sendMessage(RequestBody requestBody) throws IOException;

    void sendPing(Buffer buffer) throws IOException;
}
