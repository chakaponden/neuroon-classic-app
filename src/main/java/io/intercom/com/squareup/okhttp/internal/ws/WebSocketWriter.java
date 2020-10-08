package io.intercom.com.squareup.okhttp.internal.ws;

import android.support.v4.media.TransportMediator;
import io.intercom.okio.Buffer;
import io.intercom.okio.BufferedSink;
import io.intercom.okio.BufferedSource;
import io.intercom.okio.Sink;
import io.intercom.okio.Timeout;
import java.io.IOException;
import java.util.Random;

public final class WebSocketWriter {
    static final /* synthetic */ boolean $assertionsDisabled = (!WebSocketWriter.class.desiredAssertionStatus());
    /* access modifiers changed from: private */
    public boolean activeWriter;
    /* access modifiers changed from: private */
    public final Buffer buffer = new Buffer();
    private final FrameSink frameSink = new FrameSink();
    private final boolean isClient;
    private final byte[] maskBuffer;
    private final byte[] maskKey;
    private final Random random;
    /* access modifiers changed from: private */
    public final BufferedSink sink;
    private boolean writerClosed;

    public WebSocketWriter(boolean isClient2, BufferedSink sink2, Random random2) {
        byte[] bArr;
        byte[] bArr2 = null;
        if (sink2 == null) {
            throw new NullPointerException("sink == null");
        } else if (random2 == null) {
            throw new NullPointerException("random == null");
        } else {
            this.isClient = isClient2;
            this.sink = sink2;
            this.random = random2;
            if (isClient2) {
                bArr = new byte[4];
            } else {
                bArr = null;
            }
            this.maskKey = bArr;
            this.maskBuffer = isClient2 ? new byte[2048] : bArr2;
        }
    }

    public void writePing(Buffer payload) throws IOException {
        synchronized (this) {
            writeControlFrameSynchronized(9, payload);
        }
    }

    public void writePong(Buffer payload) throws IOException {
        synchronized (this) {
            writeControlFrameSynchronized(10, payload);
        }
    }

    public void writeClose(int code, String reason) throws IOException {
        Buffer payload = null;
        if (!(code == 0 && reason == null)) {
            if (code == 0 || (code >= 1000 && code < 5000)) {
                payload = new Buffer();
                payload.writeShort(code);
                if (reason != null) {
                    payload.writeUtf8(reason);
                }
            } else {
                throw new IllegalArgumentException("Code must be in range [1000,5000).");
            }
        }
        synchronized (this) {
            writeControlFrameSynchronized(8, payload);
            this.writerClosed = true;
        }
    }

    private void writeControlFrameSynchronized(int opcode, Buffer payload) throws IOException {
        if (!$assertionsDisabled && !Thread.holdsLock(this)) {
            throw new AssertionError();
        } else if (this.writerClosed) {
            throw new IOException("closed");
        } else {
            int length = 0;
            if (payload != null) {
                length = (int) payload.size();
                if (((long) length) > 125) {
                    throw new IllegalArgumentException("Payload size must be less than or equal to 125");
                }
            }
            this.sink.writeByte(opcode | 128);
            int b1 = length;
            if (this.isClient) {
                this.sink.writeByte(b1 | 128);
                this.random.nextBytes(this.maskKey);
                this.sink.write(this.maskKey);
                if (payload != null) {
                    writeMaskedSynchronized(payload, (long) length);
                }
            } else {
                this.sink.writeByte(b1);
                if (payload != null) {
                    this.sink.writeAll(payload);
                }
            }
            this.sink.emit();
        }
    }

    public Sink newMessageSink(int formatOpcode) {
        if (this.activeWriter) {
            throw new IllegalStateException("Another message writer is active. Did you call close()?");
        }
        this.activeWriter = true;
        int unused = this.frameSink.formatOpcode = formatOpcode;
        boolean unused2 = this.frameSink.isFirstFrame = true;
        boolean unused3 = this.frameSink.closed = false;
        return this.frameSink;
    }

    /* access modifiers changed from: private */
    public void writeMessageFrameSynchronized(int formatOpcode, long byteCount, boolean isFirstFrame, boolean isFinal) throws IOException {
        if (!$assertionsDisabled && !Thread.holdsLock(this)) {
            throw new AssertionError();
        } else if (this.writerClosed) {
            throw new IOException("closed");
        } else {
            int b0 = isFirstFrame ? formatOpcode : 0;
            if (isFinal) {
                b0 |= 128;
            }
            this.sink.writeByte(b0);
            int b1 = 0;
            if (this.isClient) {
                b1 = 0 | 128;
                this.random.nextBytes(this.maskKey);
            }
            if (byteCount <= 125) {
                this.sink.writeByte(b1 | ((int) byteCount));
            } else if (byteCount <= 65535) {
                this.sink.writeByte(b1 | TransportMediator.KEYCODE_MEDIA_PLAY);
                this.sink.writeShort((int) byteCount);
            } else {
                this.sink.writeByte(b1 | TransportMediator.KEYCODE_MEDIA_PAUSE);
                this.sink.writeLong(byteCount);
            }
            if (this.isClient) {
                this.sink.write(this.maskKey);
                writeMaskedSynchronized(this.buffer, byteCount);
            } else {
                this.sink.write(this.buffer, byteCount);
            }
            this.sink.emit();
        }
    }

    private void writeMaskedSynchronized(BufferedSource source, long byteCount) throws IOException {
        if ($assertionsDisabled || Thread.holdsLock(this)) {
            long written = 0;
            while (written < byteCount) {
                int read = source.read(this.maskBuffer, 0, (int) Math.min(byteCount, (long) this.maskBuffer.length));
                if (read == -1) {
                    throw new AssertionError();
                }
                WebSocketProtocol.toggleMask(this.maskBuffer, (long) read, this.maskKey, written);
                this.sink.write(this.maskBuffer, 0, read);
                written += (long) read;
            }
            return;
        }
        throw new AssertionError();
    }

    private final class FrameSink implements Sink {
        /* access modifiers changed from: private */
        public boolean closed;
        /* access modifiers changed from: private */
        public int formatOpcode;
        /* access modifiers changed from: private */
        public boolean isFirstFrame;

        private FrameSink() {
        }

        public void write(Buffer source, long byteCount) throws IOException {
            if (this.closed) {
                throw new IOException("closed");
            }
            WebSocketWriter.this.buffer.write(source, byteCount);
            long emitCount = WebSocketWriter.this.buffer.completeSegmentByteCount();
            if (emitCount > 0) {
                synchronized (WebSocketWriter.this) {
                    WebSocketWriter.this.writeMessageFrameSynchronized(this.formatOpcode, emitCount, this.isFirstFrame, false);
                }
                this.isFirstFrame = false;
            }
        }

        public void flush() throws IOException {
            if (this.closed) {
                throw new IOException("closed");
            }
            synchronized (WebSocketWriter.this) {
                WebSocketWriter.this.writeMessageFrameSynchronized(this.formatOpcode, WebSocketWriter.this.buffer.size(), this.isFirstFrame, false);
            }
            this.isFirstFrame = false;
        }

        public Timeout timeout() {
            return WebSocketWriter.this.sink.timeout();
        }

        public void close() throws IOException {
            if (this.closed) {
                throw new IOException("closed");
            }
            synchronized (WebSocketWriter.this) {
                WebSocketWriter.this.writeMessageFrameSynchronized(this.formatOpcode, WebSocketWriter.this.buffer.size(), this.isFirstFrame, true);
            }
            this.closed = true;
            boolean unused = WebSocketWriter.this.activeWriter = false;
        }
    }
}
