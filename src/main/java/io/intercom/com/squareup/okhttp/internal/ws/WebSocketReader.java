package io.intercom.com.squareup.okhttp.internal.ws;

import android.support.v4.media.TransportMediator;
import io.intercom.com.squareup.okhttp.MediaType;
import io.intercom.com.squareup.okhttp.ResponseBody;
import io.intercom.com.squareup.okhttp.ws.WebSocket;
import io.intercom.okio.Buffer;
import io.intercom.okio.BufferedSource;
import io.intercom.okio.Okio;
import io.intercom.okio.Source;
import io.intercom.okio.Timeout;
import java.io.EOFException;
import java.io.IOException;
import java.net.ProtocolException;

public final class WebSocketReader {
    /* access modifiers changed from: private */
    public boolean closed;
    /* access modifiers changed from: private */
    public long frameBytesRead;
    private final FrameCallback frameCallback;
    /* access modifiers changed from: private */
    public long frameLength;
    private final Source framedMessageSource = new FramedMessageSource();
    private final boolean isClient;
    private boolean isControlFrame;
    /* access modifiers changed from: private */
    public boolean isFinalFrame;
    /* access modifiers changed from: private */
    public boolean isMasked;
    /* access modifiers changed from: private */
    public final byte[] maskBuffer = new byte[2048];
    /* access modifiers changed from: private */
    public final byte[] maskKey = new byte[4];
    /* access modifiers changed from: private */
    public boolean messageClosed;
    /* access modifiers changed from: private */
    public int opcode;
    /* access modifiers changed from: private */
    public final BufferedSource source;

    public interface FrameCallback {
        void onClose(int i, String str);

        void onMessage(ResponseBody responseBody) throws IOException;

        void onPing(Buffer buffer);

        void onPong(Buffer buffer);
    }

    public WebSocketReader(boolean isClient2, BufferedSource source2, FrameCallback frameCallback2) {
        if (source2 == null) {
            throw new NullPointerException("source == null");
        } else if (frameCallback2 == null) {
            throw new NullPointerException("frameCallback == null");
        } else {
            this.isClient = isClient2;
            this.source = source2;
            this.frameCallback = frameCallback2;
        }
    }

    public void processNextFrame() throws IOException {
        readHeader();
        if (this.isControlFrame) {
            readControlFrame();
        } else {
            readMessageFrame();
        }
    }

    private void readHeader() throws IOException {
        boolean z;
        boolean reservedFlag1;
        boolean reservedFlag2;
        boolean reservedFlag3;
        boolean z2 = true;
        if (this.closed) {
            throw new IOException("closed");
        }
        int b0 = this.source.readByte() & 255;
        this.opcode = b0 & 15;
        this.isFinalFrame = (b0 & 128) != 0;
        if ((b0 & 8) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.isControlFrame = z;
        if (!this.isControlFrame || this.isFinalFrame) {
            if ((b0 & 64) != 0) {
                reservedFlag1 = true;
            } else {
                reservedFlag1 = false;
            }
            if ((b0 & 32) != 0) {
                reservedFlag2 = true;
            } else {
                reservedFlag2 = false;
            }
            if ((b0 & 16) != 0) {
                reservedFlag3 = true;
            } else {
                reservedFlag3 = false;
            }
            if (reservedFlag1 || reservedFlag2 || reservedFlag3) {
                throw new ProtocolException("Reserved flags are unsupported.");
            }
            int b1 = this.source.readByte() & 255;
            if ((b1 & 128) == 0) {
                z2 = false;
            }
            this.isMasked = z2;
            if (this.isMasked == this.isClient) {
                throw new ProtocolException("Client-sent frames must be masked. Server sent must not.");
            }
            this.frameLength = (long) (b1 & TransportMediator.KEYCODE_MEDIA_PAUSE);
            if (this.frameLength == 126) {
                this.frameLength = ((long) this.source.readShort()) & 65535;
            } else if (this.frameLength == 127) {
                this.frameLength = this.source.readLong();
                if (this.frameLength < 0) {
                    throw new ProtocolException("Frame length 0x" + Long.toHexString(this.frameLength) + " > 0x7FFFFFFFFFFFFFFF");
                }
            }
            this.frameBytesRead = 0;
            if (this.isControlFrame && this.frameLength > 125) {
                throw new ProtocolException("Control frame must be less than 125B.");
            } else if (this.isMasked) {
                this.source.readFully(this.maskKey);
            }
        } else {
            throw new ProtocolException("Control frames must be final.");
        }
    }

    private void readControlFrame() throws IOException {
        Buffer buffer = null;
        if (this.frameBytesRead < this.frameLength) {
            buffer = new Buffer();
            if (this.isClient) {
                this.source.readFully(buffer, this.frameLength);
            } else {
                while (this.frameBytesRead < this.frameLength) {
                    int read = this.source.read(this.maskBuffer, 0, (int) Math.min(this.frameLength - this.frameBytesRead, (long) this.maskBuffer.length));
                    if (read == -1) {
                        throw new EOFException();
                    }
                    WebSocketProtocol.toggleMask(this.maskBuffer, (long) read, this.maskKey, this.frameBytesRead);
                    buffer.write(this.maskBuffer, 0, read);
                    this.frameBytesRead += (long) read;
                }
            }
        }
        switch (this.opcode) {
            case 8:
                int code = 1000;
                String reason = "";
                if (buffer != null) {
                    long bufferSize = buffer.size();
                    if (bufferSize == 1) {
                        throw new ProtocolException("Malformed close payload length of 1.");
                    } else if (bufferSize != 0) {
                        code = buffer.readShort();
                        if (code < 1000 || code >= 5000) {
                            throw new ProtocolException("Code must be in range [1000,5000): " + code);
                        } else if ((code < 1004 || code > 1006) && (code < 1012 || code > 2999)) {
                            reason = buffer.readUtf8();
                        } else {
                            throw new ProtocolException("Code " + code + " is reserved and may not be used.");
                        }
                    }
                }
                this.frameCallback.onClose(code, reason);
                this.closed = true;
                return;
            case 9:
                this.frameCallback.onPing(buffer);
                return;
            case 10:
                this.frameCallback.onPong(buffer);
                return;
            default:
                throw new ProtocolException("Unknown control opcode: " + Integer.toHexString(this.opcode));
        }
    }

    private void readMessageFrame() throws IOException {
        final MediaType type;
        switch (this.opcode) {
            case 1:
                type = WebSocket.TEXT;
                break;
            case 2:
                type = WebSocket.BINARY;
                break;
            default:
                throw new ProtocolException("Unknown opcode: " + Integer.toHexString(this.opcode));
        }
        final BufferedSource source2 = Okio.buffer(this.framedMessageSource);
        ResponseBody body = new ResponseBody() {
            public MediaType contentType() {
                return type;
            }

            public long contentLength() throws IOException {
                return -1;
            }

            public BufferedSource source() throws IOException {
                return source2;
            }
        };
        this.messageClosed = false;
        this.frameCallback.onMessage(body);
        if (!this.messageClosed) {
            throw new IllegalStateException("Listener failed to call close on message payload.");
        }
    }

    /* access modifiers changed from: private */
    public void readUntilNonControlFrame() throws IOException {
        while (!this.closed) {
            readHeader();
            if (this.isControlFrame) {
                readControlFrame();
            } else {
                return;
            }
        }
    }

    private final class FramedMessageSource implements Source {
        private FramedMessageSource() {
        }

        public long read(Buffer sink, long byteCount) throws IOException {
            long read;
            if (WebSocketReader.this.closed) {
                throw new IOException("closed");
            } else if (WebSocketReader.this.messageClosed) {
                throw new IllegalStateException("closed");
            } else {
                if (WebSocketReader.this.frameBytesRead == WebSocketReader.this.frameLength) {
                    if (WebSocketReader.this.isFinalFrame) {
                        return -1;
                    }
                    WebSocketReader.this.readUntilNonControlFrame();
                    if (WebSocketReader.this.opcode != 0) {
                        throw new ProtocolException("Expected continuation opcode. Got: " + Integer.toHexString(WebSocketReader.this.opcode));
                    } else if (WebSocketReader.this.isFinalFrame && WebSocketReader.this.frameLength == 0) {
                        return -1;
                    }
                }
                long toRead = Math.min(byteCount, WebSocketReader.this.frameLength - WebSocketReader.this.frameBytesRead);
                if (WebSocketReader.this.isMasked) {
                    read = (long) WebSocketReader.this.source.read(WebSocketReader.this.maskBuffer, 0, (int) Math.min(toRead, (long) WebSocketReader.this.maskBuffer.length));
                    if (read == -1) {
                        throw new EOFException();
                    }
                    WebSocketProtocol.toggleMask(WebSocketReader.this.maskBuffer, read, WebSocketReader.this.maskKey, WebSocketReader.this.frameBytesRead);
                    sink.write(WebSocketReader.this.maskBuffer, 0, (int) read);
                } else {
                    read = WebSocketReader.this.source.read(sink, toRead);
                    if (read == -1) {
                        throw new EOFException();
                    }
                }
                long unused = WebSocketReader.this.frameBytesRead = WebSocketReader.this.frameBytesRead + read;
                return read;
            }
        }

        public Timeout timeout() {
            return WebSocketReader.this.source.timeout();
        }

        public void close() throws IOException {
            if (!WebSocketReader.this.messageClosed) {
                boolean unused = WebSocketReader.this.messageClosed = true;
                if (!WebSocketReader.this.closed) {
                    WebSocketReader.this.source.skip(WebSocketReader.this.frameLength - WebSocketReader.this.frameBytesRead);
                    while (!WebSocketReader.this.isFinalFrame) {
                        WebSocketReader.this.readUntilNonControlFrame();
                        WebSocketReader.this.source.skip(WebSocketReader.this.frameLength);
                    }
                }
            }
        }
    }
}
