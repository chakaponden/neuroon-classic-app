package io.intercom.com.squareup.okhttp.internal.ws;

import com.raizlabs.android.dbflow.sql.language.Condition;
import io.intercom.com.squareup.okhttp.MediaType;
import io.intercom.com.squareup.okhttp.RequestBody;
import io.intercom.com.squareup.okhttp.Response;
import io.intercom.com.squareup.okhttp.ResponseBody;
import io.intercom.com.squareup.okhttp.internal.NamedRunnable;
import io.intercom.com.squareup.okhttp.internal.ws.WebSocketReader;
import io.intercom.com.squareup.okhttp.ws.WebSocket;
import io.intercom.com.squareup.okhttp.ws.WebSocketListener;
import io.intercom.okio.Buffer;
import io.intercom.okio.BufferedSink;
import io.intercom.okio.BufferedSource;
import io.intercom.okio.Okio;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class RealWebSocket implements WebSocket {
    private static final int CLOSE_PROTOCOL_EXCEPTION = 1002;
    private final AtomicBoolean connectionClosed = new AtomicBoolean();
    private final WebSocketListener listener;
    private final WebSocketReader reader;
    /* access modifiers changed from: private */
    public boolean readerSentClose;
    /* access modifiers changed from: private */
    public final WebSocketWriter writer;
    private volatile boolean writerSentClose;
    private boolean writerWantsClose;

    /* access modifiers changed from: protected */
    public abstract void close() throws IOException;

    public RealWebSocket(boolean isClient, BufferedSource source, BufferedSink sink, Random random, final Executor replyExecutor, final WebSocketListener listener2, final String url) {
        this.listener = listener2;
        this.writer = new WebSocketWriter(isClient, sink, random);
        this.reader = new WebSocketReader(isClient, source, new WebSocketReader.FrameCallback() {
            public void onMessage(ResponseBody message) throws IOException {
                listener2.onMessage(message);
            }

            public void onPing(final Buffer buffer) {
                replyExecutor.execute(new NamedRunnable("OkHttp %s WebSocket Pong Reply", new Object[]{url}) {
                    /* access modifiers changed from: protected */
                    public void execute() {
                        try {
                            RealWebSocket.this.writer.writePong(buffer);
                        } catch (IOException e) {
                        }
                    }
                });
            }

            public void onPong(Buffer buffer) {
                listener2.onPong(buffer);
            }

            public void onClose(int code, String reason) {
                boolean unused = RealWebSocket.this.readerSentClose = true;
                final int i = code;
                final String str = reason;
                replyExecutor.execute(new NamedRunnable("OkHttp %s WebSocket Close Reply", new Object[]{url}) {
                    /* access modifiers changed from: protected */
                    public void execute() {
                        RealWebSocket.this.peerClose(i, str);
                    }
                });
            }
        });
    }

    public boolean readMessage() {
        try {
            this.reader.processNextFrame();
            if (!this.readerSentClose) {
                return true;
            }
            return false;
        } catch (IOException e) {
            readerErrorClose(e);
            return false;
        }
    }

    public void sendMessage(RequestBody message) throws IOException {
        int formatOpcode;
        if (message == null) {
            throw new NullPointerException("message == null");
        } else if (this.writerSentClose) {
            throw new IllegalStateException("closed");
        } else if (this.writerWantsClose) {
            throw new IllegalStateException("must call close()");
        } else {
            MediaType contentType = message.contentType();
            if (contentType == null) {
                throw new IllegalArgumentException("Message content type was null. Must use WebSocket.TEXT or WebSocket.BINARY.");
            }
            String contentSubtype = contentType.subtype();
            if (WebSocket.TEXT.subtype().equals(contentSubtype)) {
                formatOpcode = 1;
            } else if (WebSocket.BINARY.subtype().equals(contentSubtype)) {
                formatOpcode = 2;
            } else {
                throw new IllegalArgumentException("Unknown message content type: " + contentType.type() + Condition.Operation.DIVISION + contentType.subtype() + ". Must use WebSocket.TEXT or WebSocket.BINARY.");
            }
            BufferedSink sink = Okio.buffer(this.writer.newMessageSink(formatOpcode));
            try {
                message.writeTo(sink);
                sink.close();
            } catch (IOException e) {
                this.writerWantsClose = true;
                throw e;
            }
        }
    }

    public void sendPing(Buffer payload) throws IOException {
        if (this.writerSentClose) {
            throw new IllegalStateException("closed");
        } else if (this.writerWantsClose) {
            throw new IllegalStateException("must call close()");
        } else {
            try {
                this.writer.writePing(payload);
            } catch (IOException e) {
                this.writerWantsClose = true;
                throw e;
            }
        }
    }

    public void sendPong(Buffer payload) throws IOException {
        if (this.writerSentClose) {
            throw new IllegalStateException("closed");
        } else if (this.writerWantsClose) {
            throw new IllegalStateException("must call close()");
        } else {
            try {
                this.writer.writePong(payload);
            } catch (IOException e) {
                this.writerWantsClose = true;
                throw e;
            }
        }
    }

    public void close(int code, String reason) throws IOException {
        if (this.writerSentClose) {
            throw new IllegalStateException("closed");
        }
        this.writerSentClose = true;
        try {
            this.writer.writeClose(code, reason);
        } catch (IOException e) {
            if (this.connectionClosed.compareAndSet(false, true)) {
                try {
                    close();
                } catch (IOException e2) {
                }
            }
            throw e;
        }
    }

    /* access modifiers changed from: private */
    public void peerClose(int code, String reason) {
        if (!this.writerSentClose) {
            try {
                this.writer.writeClose(code, reason);
            } catch (IOException e) {
            }
        }
        if (this.connectionClosed.compareAndSet(false, true)) {
            try {
                close();
            } catch (IOException e2) {
            }
        }
        this.listener.onClose(code, reason);
    }

    private void readerErrorClose(IOException e) {
        if (!this.writerSentClose && (e instanceof ProtocolException)) {
            try {
                this.writer.writeClose(CLOSE_PROTOCOL_EXCEPTION, (String) null);
            } catch (IOException e2) {
            }
        }
        if (this.connectionClosed.compareAndSet(false, true)) {
            try {
                close();
            } catch (IOException e3) {
            }
        }
        this.listener.onFailure(e, (Response) null);
    }
}
