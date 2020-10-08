package io.intercom.com.squareup.okhttp.ws;

import io.intercom.com.squareup.okhttp.Response;
import io.intercom.com.squareup.okhttp.ResponseBody;
import io.intercom.okio.Buffer;
import java.io.IOException;

public interface WebSocketListener {
    void onClose(int i, String str);

    void onFailure(IOException iOException, Response response);

    void onMessage(ResponseBody responseBody) throws IOException;

    void onOpen(WebSocket webSocket, Response response);

    void onPong(Buffer buffer);
}
