package io.intercom.com.squareup.okhttp.ws;

import io.fabric.sdk.android.services.network.HttpRequest;
import io.intercom.com.squareup.okhttp.Call;
import io.intercom.com.squareup.okhttp.Callback;
import io.intercom.com.squareup.okhttp.OkHttpClient;
import io.intercom.com.squareup.okhttp.Protocol;
import io.intercom.com.squareup.okhttp.Request;
import io.intercom.com.squareup.okhttp.Response;
import io.intercom.com.squareup.okhttp.internal.Internal;
import io.intercom.com.squareup.okhttp.internal.Util;
import io.intercom.com.squareup.okhttp.internal.http.StreamAllocation;
import io.intercom.com.squareup.okhttp.internal.ws.RealWebSocket;
import io.intercom.com.squareup.okhttp.internal.ws.WebSocketProtocol;
import io.intercom.okio.ByteString;
import java.io.Closeable;
import java.io.IOException;
import java.net.ProtocolException;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class WebSocketCall {
    private final Call call;
    private final String key;
    private final Random random;

    public static WebSocketCall create(OkHttpClient client, Request request) {
        return new WebSocketCall(client, request);
    }

    WebSocketCall(OkHttpClient client, Request request) {
        this(client, request, new SecureRandom());
    }

    WebSocketCall(OkHttpClient client, Request request, Random random2) {
        if (!HttpRequest.METHOD_GET.equals(request.method())) {
            throw new IllegalArgumentException("Request must be GET: " + request.method());
        }
        this.random = random2;
        byte[] nonce = new byte[16];
        random2.nextBytes(nonce);
        this.key = ByteString.of(nonce).base64();
        OkHttpClient client2 = client.clone();
        client2.setProtocols(Collections.singletonList(Protocol.HTTP_1_1));
        this.call = client2.newCall(request.newBuilder().header("Upgrade", "websocket").header("Connection", "Upgrade").header("Sec-WebSocket-Key", this.key).header("Sec-WebSocket-Version", "13").build());
    }

    public void enqueue(final WebSocketListener listener) {
        Internal.instance.callEnqueue(this.call, new Callback() {
            public void onResponse(Response response) throws IOException {
                try {
                    WebSocketCall.this.createWebSocket(response, listener);
                } catch (IOException e) {
                    listener.onFailure(e, response);
                }
            }

            public void onFailure(Request request, IOException e) {
                listener.onFailure(e, (Response) null);
            }
        }, true);
    }

    public void cancel() {
        this.call.cancel();
    }

    /* access modifiers changed from: private */
    public void createWebSocket(Response response, WebSocketListener listener) throws IOException {
        if (response.code() != 101) {
            Util.closeQuietly((Closeable) response.body());
            throw new ProtocolException("Expected HTTP 101 response but was '" + response.code() + " " + response.message() + "'");
        }
        String headerConnection = response.header("Connection");
        if (!"Upgrade".equalsIgnoreCase(headerConnection)) {
            throw new ProtocolException("Expected 'Connection' header value 'Upgrade' but was '" + headerConnection + "'");
        }
        String headerUpgrade = response.header("Upgrade");
        if (!"websocket".equalsIgnoreCase(headerUpgrade)) {
            throw new ProtocolException("Expected 'Upgrade' header value 'websocket' but was '" + headerUpgrade + "'");
        }
        String headerAccept = response.header("Sec-WebSocket-Accept");
        String acceptExpected = Util.shaBase64(this.key + WebSocketProtocol.ACCEPT_MAGIC);
        if (!acceptExpected.equals(headerAccept)) {
            throw new ProtocolException("Expected 'Sec-WebSocket-Accept' header value '" + acceptExpected + "' but was '" + headerAccept + "'");
        }
        RealWebSocket webSocket = StreamWebSocket.create(Internal.instance.callEngineGetStreamAllocation(this.call), response, this.random, listener);
        listener.onOpen(webSocket, response);
        do {
        } while (webSocket.readMessage());
    }

    private static class StreamWebSocket extends RealWebSocket {
        private final ExecutorService replyExecutor;
        private final StreamAllocation streamAllocation;

        static RealWebSocket create(StreamAllocation streamAllocation2, Response response, Random random, WebSocketListener listener) {
            String url = response.request().urlString();
            ThreadPoolExecutor replyExecutor2 = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, new LinkedBlockingDeque(), Util.threadFactory(String.format("OkHttp %s WebSocket", new Object[]{url}), true));
            replyExecutor2.allowCoreThreadTimeOut(true);
            return new StreamWebSocket(streamAllocation2, random, replyExecutor2, listener, url);
        }

        private StreamWebSocket(StreamAllocation streamAllocation2, Random random, ExecutorService replyExecutor2, WebSocketListener listener, String url) {
            super(true, streamAllocation2.connection().source, streamAllocation2.connection().sink, random, replyExecutor2, listener, url);
            this.streamAllocation = streamAllocation2;
            this.replyExecutor = replyExecutor2;
        }

        /* access modifiers changed from: protected */
        public void close() throws IOException {
            this.replyExecutor.shutdown();
            this.streamAllocation.noNewStreams();
            this.streamAllocation.streamFinished(this.streamAllocation.stream());
        }
    }
}
