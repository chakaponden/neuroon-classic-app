package io.intercom.android.sdk.nexus;

import android.os.Handler;
import android.os.HandlerThread;
import io.intercom.com.squareup.okhttp.OkHttpClient;
import io.intercom.com.squareup.okhttp.Request;
import io.intercom.com.squareup.okhttp.RequestBody;
import io.intercom.com.squareup.okhttp.Response;
import io.intercom.com.squareup.okhttp.ResponseBody;
import io.intercom.com.squareup.okhttp.ws.WebSocket;
import io.intercom.com.squareup.okhttp.ws.WebSocketCall;
import io.intercom.com.squareup.okhttp.ws.WebSocketListener;
import io.intercom.okio.Buffer;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;

class NexusSocket implements WebSocketListener {
    private static final String HEADER = "?X-Nexus-Version=android.1.0.11";
    private static final int MAX_RECONNECT_TIME = 256;
    private static final int N_TIMEOUT_DISCONNECT = 4001;
    private static final int OK_CLIENT_DISCONNECT = 4000;
    private final Handler backgroundHandler;
    private final long connectionTimeout;
    private final NexusListener eventNotifier;
    private long lastReconnectAt = 0;
    /* access modifiers changed from: private */
    public final String nexusUrl;
    private int reconnectAttempts = 0;
    private final boolean shouldSendPresence;
    /* access modifiers changed from: private */
    public WebSocket socket = new ClosedSocket();
    private Runnable timeoutRunnable = new Runnable() {
        public void run() {
            NexusSocket.this.timedOut();
        }
    };

    public NexusSocket(String nexusUrl2, int connectionTimeout2, boolean shouldSendPresence2, NexusListener eventNotifier2) {
        this.nexusUrl = nexusUrl2;
        this.connectionTimeout = TimeUnit.SECONDS.toMillis((long) connectionTimeout2);
        this.shouldSendPresence = shouldSendPresence2;
        this.eventNotifier = eventNotifier2;
        HandlerThread bgThread = new HandlerThread("background-handler");
        bgThread.start();
        this.backgroundHandler = new Handler(bgThread.getLooper());
        connect(nexusUrl2);
    }

    /* access modifiers changed from: private */
    public void connect(String url) {
        NexusLogger.d("connecting to a socket...");
        this.socket = new ConnectingSocket();
        final OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(20, TimeUnit.SECONDS);
        client.setReadTimeout(130, TimeUnit.SECONDS);
        client.setWriteTimeout(130, TimeUnit.SECONDS);
        final Request request = new Request.Builder().url(url + HEADER).build();
        this.backgroundHandler.post(new Runnable() {
            public void run() {
                WebSocketCall.create(client, request).enqueue(NexusSocket.this);
                client.getDispatcher().getExecutorService().shutdown();
            }
        });
        this.backgroundHandler.postDelayed(this.timeoutRunnable, this.connectionTimeout);
    }

    public void fire(final String data) {
        if (!data.isEmpty()) {
            this.backgroundHandler.post(new Runnable() {
                public void run() {
                    try {
                        NexusLogger.v("firing: " + data);
                        NexusSocket.this.socket.sendMessage(RequestBody.create(WebSocket.TEXT, data));
                    } catch (IOException | IllegalStateException e) {
                        NexusLogger.errorLog("fire: " + data, e);
                    }
                }
            });
        }
    }

    public void disconnect() {
        disconnect(OK_CLIENT_DISCONNECT);
    }

    public boolean isConnected() {
        return !(this.socket instanceof ClosedSocket);
    }

    private void disconnect(final int code) {
        this.backgroundHandler.post(new Runnable() {
            public void run() {
                try {
                    NexusSocket.this.socket.close(code, "Goodbye, world!");
                } catch (IOException e) {
                    NexusLogger.errorLog("disconnect: failed " + e.getMessage());
                    NexusLogger.errorLog("disconnect: failed", e);
                } catch (IllegalStateException ex) {
                    NexusLogger.errorLog("disconnect: socket already closed", ex);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void timedOut() {
        if (this.socket == null) {
            connect(this.nexusUrl);
        } else {
            disconnect(N_TIMEOUT_DISCONNECT);
        }
        this.eventNotifier.onConnectFailed();
    }

    private void resetTimeout() {
        this.backgroundHandler.removeCallbacks(this.timeoutRunnable);
        this.backgroundHandler.postDelayed(this.timeoutRunnable, this.connectionTimeout);
    }

    private void scheduleReconnect() {
        modifyReconnectAttempts();
        this.backgroundHandler.postDelayed(new Runnable() {
            public void run() {
                NexusSocket.this.connect(NexusSocket.this.nexusUrl);
            }
        }, calculateReconnectTimerInMS(this.reconnectAttempts));
    }

    private void modifyReconnectAttempts() {
        if (System.currentTimeMillis() - this.lastReconnectAt > TimeUnit.SECONDS.toMillis(256) * 2) {
            NexusLogger.d("resetting reconnection attempts");
            this.reconnectAttempts = 1;
        } else {
            NexusLogger.d("incrementing reconnection attempts");
            this.reconnectAttempts++;
        }
        this.lastReconnectAt = System.currentTimeMillis();
    }

    public void onOpen(WebSocket webSocket, Response response) {
        NexusLogger.d("onOpen: " + response.message());
        this.socket = webSocket;
        resetTimeout();
        if (this.shouldSendPresence) {
            fire(NexusEvent.UserPresence.toJsonFormattedString());
        }
        this.eventNotifier.onConnect();
    }

    public void onMessage(ResponseBody message) throws IOException {
        resetTimeout();
        if (message.contentType() == WebSocket.TEXT) {
            String s = message.string();
            if (!s.isEmpty() && !s.equals(" ")) {
                try {
                    JSONObject jObject = new JSONObject(s);
                    String eventName = jObject.optString("eventName");
                    if (eventName.isEmpty() || eventName.equals("ACK")) {
                        NexusLogger.d("onMessage ACK: " + s);
                    } else {
                        NexusLogger.v("onMessage TEXT: " + s + " length: " + s.length());
                        this.eventNotifier.notifyEvent(NexusEvent.parse(jObject));
                    }
                } catch (JSONException e) {
                    NexusLogger.errorLog("onMessage: json parse exception for message: " + s);
                    NexusLogger.errorLog("onMessage: json parse exception", e);
                }
            }
        }
        message.close();
    }

    public void onPong(Buffer payload) {
        payload.close();
    }

    public void onClose(int code, String reason) {
        switch (code) {
            case OK_CLIENT_DISCONNECT /*4000*/:
                shutdown();
                break;
            default:
                scheduleReconnect();
                break;
        }
        NexusLogger.d("onClose code: " + code + " reason: " + reason);
    }

    public void onFailure(IOException e, Response response) {
        if (shouldReconnectFromFailure(e.getMessage())) {
            scheduleReconnect();
        } else {
            shutdown();
        }
        NexusLogger.errorLog("onFailure: " + e.getMessage(), e);
        NexusLogger.errorLog("onFailure: " + response, e);
        this.eventNotifier.onConnectFailed();
    }

    private void shutdown() {
        this.socket = new ClosedSocket();
        this.backgroundHandler.removeCallbacksAndMessages((Object) null);
        this.backgroundHandler.getLooper().quit();
    }

    public static long calculateReconnectTimerInMS(int reconnectAttempts2) {
        int minimumBackoffSeconds = (int) Math.min(Math.pow(2.0d, (double) reconnectAttempts2), 256.0d);
        long millis = TimeUnit.SECONDS.toMillis((long) (minimumBackoffSeconds + new Random().nextInt(minimumBackoffSeconds + 1)));
        NexusLogger.d("Scheduling reconnect in: " + millis + " for attempt: " + reconnectAttempts2);
        return millis;
    }

    static boolean shouldReconnectFromFailure(String exception) {
        if (exception != null) {
            if (exception.equals("closed")) {
                return false;
            }
            if (exception.startsWith("Expected HTTP 101 response but was")) {
                return exception.substring("Expected HTTP 101 response but was".length()).startsWith(" '5");
            }
        }
        return true;
    }

    private class DummySocket implements WebSocket {
        private DummySocket() {
        }

        public void sendMessage(RequestBody message) throws IOException {
        }

        public void sendPing(Buffer payload) throws IOException {
        }

        public void close(int code, String reason) throws IOException {
        }
    }

    private class ConnectingSocket extends DummySocket {
        private ConnectingSocket() {
            super();
        }
    }

    private class ClosedSocket extends DummySocket {
        private ClosedSocket() {
            super();
        }
    }
}
