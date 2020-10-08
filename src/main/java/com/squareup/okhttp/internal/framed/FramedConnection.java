package com.squareup.okhttp.internal.framed;

import android.support.v4.internal.view.SupportMenu;
import android.support.v7.widget.ActivityChooserView;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.internal.NamedRunnable;
import com.squareup.okhttp.internal.Util;
import com.squareup.okhttp.internal.framed.FrameReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;

public final class FramedConnection implements Closeable {
    static final /* synthetic */ boolean $assertionsDisabled;
    private static final int OKHTTP_CLIENT_WINDOW_SIZE = 16777216;
    /* access modifiers changed from: private */
    public static final ExecutorService executor = new ThreadPoolExecutor(0, ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED, 60, TimeUnit.SECONDS, new SynchronousQueue(), Util.threadFactory("OkHttp FramedConnection", true));
    long bytesLeftInWriteWindow;
    final boolean client;
    /* access modifiers changed from: private */
    public final Set<Integer> currentPushRequests;
    final FrameWriter frameWriter;
    /* access modifiers changed from: private */
    public final String hostName;
    private long idleStartTimeNs;
    /* access modifiers changed from: private */
    public int lastGoodStreamId;
    /* access modifiers changed from: private */
    public final Listener listener;
    private int nextPingId;
    /* access modifiers changed from: private */
    public int nextStreamId;
    Settings okHttpSettings;
    final Settings peerSettings;
    private Map<Integer, Ping> pings;
    final Protocol protocol;
    private final ExecutorService pushExecutor;
    /* access modifiers changed from: private */
    public final PushObserver pushObserver;
    final Reader readerRunnable;
    /* access modifiers changed from: private */
    public boolean receivedInitialPeerSettings;
    /* access modifiers changed from: private */
    public boolean shutdown;
    final Socket socket;
    /* access modifiers changed from: private */
    public final Map<Integer, FramedStream> streams;
    long unacknowledgedBytesRead;
    final Variant variant;

    static {
        boolean z;
        if (!FramedConnection.class.desiredAssertionStatus()) {
            z = true;
        } else {
            z = false;
        }
        $assertionsDisabled = z;
    }

    private FramedConnection(Builder builder) throws IOException {
        int i = 2;
        this.streams = new HashMap();
        this.idleStartTimeNs = System.nanoTime();
        this.unacknowledgedBytesRead = 0;
        this.okHttpSettings = new Settings();
        this.peerSettings = new Settings();
        this.receivedInitialPeerSettings = false;
        this.currentPushRequests = new LinkedHashSet();
        this.protocol = builder.protocol;
        this.pushObserver = builder.pushObserver;
        this.client = builder.client;
        this.listener = builder.listener;
        this.nextStreamId = builder.client ? 1 : 2;
        if (builder.client && this.protocol == Protocol.HTTP_2) {
            this.nextStreamId += 2;
        }
        this.nextPingId = builder.client ? 1 : i;
        if (builder.client) {
            this.okHttpSettings.set(7, 0, 16777216);
        }
        this.hostName = builder.hostName;
        if (this.protocol == Protocol.HTTP_2) {
            this.variant = new Http2();
            this.pushExecutor = new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue(), Util.threadFactory(String.format("OkHttp %s Push Observer", new Object[]{this.hostName}), true));
            this.peerSettings.set(7, 0, SupportMenu.USER_MASK);
            this.peerSettings.set(5, 0, 16384);
        } else if (this.protocol == Protocol.SPDY_3) {
            this.variant = new Spdy3();
            this.pushExecutor = null;
        } else {
            throw new AssertionError(this.protocol);
        }
        this.bytesLeftInWriteWindow = (long) this.peerSettings.getInitialWindowSize(65536);
        this.socket = builder.socket;
        this.frameWriter = this.variant.newWriter(builder.sink, this.client);
        this.readerRunnable = new Reader(this.variant.newReader(builder.source, this.client));
        new Thread(this.readerRunnable).start();
    }

    public Protocol getProtocol() {
        return this.protocol;
    }

    public synchronized int openStreamCount() {
        return this.streams.size();
    }

    /* access modifiers changed from: package-private */
    public synchronized FramedStream getStream(int id) {
        return this.streams.get(Integer.valueOf(id));
    }

    /* access modifiers changed from: package-private */
    public synchronized FramedStream removeStream(int streamId) {
        FramedStream stream;
        stream = this.streams.remove(Integer.valueOf(streamId));
        if (stream != null && this.streams.isEmpty()) {
            setIdle(true);
        }
        notifyAll();
        return stream;
    }

    private synchronized void setIdle(boolean value) {
        this.idleStartTimeNs = value ? System.nanoTime() : Long.MAX_VALUE;
    }

    public synchronized boolean isIdle() {
        return this.idleStartTimeNs != Long.MAX_VALUE;
    }

    public synchronized int maxConcurrentStreams() {
        return this.peerSettings.getMaxConcurrentStreams(ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
    }

    public synchronized long getIdleStartTimeNs() {
        return this.idleStartTimeNs;
    }

    public FramedStream pushStream(int associatedStreamId, List<Header> requestHeaders, boolean out) throws IOException {
        if (this.client) {
            throw new IllegalStateException("Client cannot push requests.");
        } else if (this.protocol == Protocol.HTTP_2) {
            return newStream(associatedStreamId, requestHeaders, out, false);
        } else {
            throw new IllegalStateException("protocol != HTTP_2");
        }
    }

    public FramedStream newStream(List<Header> requestHeaders, boolean out, boolean in) throws IOException {
        return newStream(0, requestHeaders, out, in);
    }

    private FramedStream newStream(int associatedStreamId, List<Header> requestHeaders, boolean out, boolean in) throws IOException {
        boolean outFinished;
        int streamId;
        FramedStream stream;
        boolean inFinished = true;
        if (!out) {
            outFinished = true;
        } else {
            outFinished = false;
        }
        if (in) {
            inFinished = false;
        }
        synchronized (this.frameWriter) {
            synchronized (this) {
                if (this.shutdown) {
                    throw new IOException("shutdown");
                }
                streamId = this.nextStreamId;
                this.nextStreamId += 2;
                stream = new FramedStream(streamId, this, outFinished, inFinished, requestHeaders);
                if (stream.isOpen()) {
                    this.streams.put(Integer.valueOf(streamId), stream);
                    setIdle(false);
                }
            }
            if (associatedStreamId == 0) {
                this.frameWriter.synStream(outFinished, inFinished, streamId, associatedStreamId, requestHeaders);
            } else if (this.client) {
                throw new IllegalArgumentException("client streams shouldn't have associated stream IDs");
            } else {
                this.frameWriter.pushPromise(associatedStreamId, streamId, requestHeaders);
            }
        }
        if (!out) {
            this.frameWriter.flush();
        }
        return stream;
    }

    /* access modifiers changed from: package-private */
    public void writeSynReply(int streamId, boolean outFinished, List<Header> alternating) throws IOException {
        this.frameWriter.synReply(outFinished, streamId, alternating);
    }

    public void writeData(int streamId, boolean outFinished, Buffer buffer, long byteCount) throws IOException {
        int toWrite;
        boolean z;
        if (byteCount == 0) {
            this.frameWriter.data(outFinished, streamId, buffer, 0);
            return;
        }
        while (byteCount > 0) {
            synchronized (this) {
                while (this.bytesLeftInWriteWindow <= 0) {
                    try {
                        if (!this.streams.containsKey(Integer.valueOf(streamId))) {
                            throw new IOException("stream closed");
                        }
                        wait();
                    } catch (InterruptedException e) {
                        throw new InterruptedIOException();
                    }
                }
                toWrite = Math.min((int) Math.min(byteCount, this.bytesLeftInWriteWindow), this.frameWriter.maxDataLength());
                this.bytesLeftInWriteWindow -= (long) toWrite;
            }
            byteCount -= (long) toWrite;
            FrameWriter frameWriter2 = this.frameWriter;
            if (!outFinished || byteCount != 0) {
                z = false;
            } else {
                z = true;
            }
            frameWriter2.data(z, streamId, buffer, toWrite);
        }
    }

    /* access modifiers changed from: package-private */
    public void addBytesToWriteWindow(long delta) {
        this.bytesLeftInWriteWindow += delta;
        if (delta > 0) {
            notifyAll();
        }
    }

    /* access modifiers changed from: package-private */
    public void writeSynResetLater(int streamId, ErrorCode errorCode) {
        final int i = streamId;
        final ErrorCode errorCode2 = errorCode;
        executor.submit(new NamedRunnable("OkHttp %s stream %d", new Object[]{this.hostName, Integer.valueOf(streamId)}) {
            public void execute() {
                try {
                    FramedConnection.this.writeSynReset(i, errorCode2);
                } catch (IOException e) {
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void writeSynReset(int streamId, ErrorCode statusCode) throws IOException {
        this.frameWriter.rstStream(streamId, statusCode);
    }

    /* access modifiers changed from: package-private */
    public void writeWindowUpdateLater(int streamId, long unacknowledgedBytesRead2) {
        final int i = streamId;
        final long j = unacknowledgedBytesRead2;
        executor.execute(new NamedRunnable("OkHttp Window Update %s stream %d", new Object[]{this.hostName, Integer.valueOf(streamId)}) {
            public void execute() {
                try {
                    FramedConnection.this.frameWriter.windowUpdate(i, j);
                } catch (IOException e) {
                }
            }
        });
    }

    public Ping ping() throws IOException {
        int pingId;
        Ping ping = new Ping();
        synchronized (this) {
            if (this.shutdown) {
                throw new IOException("shutdown");
            }
            pingId = this.nextPingId;
            this.nextPingId += 2;
            if (this.pings == null) {
                this.pings = new HashMap();
            }
            this.pings.put(Integer.valueOf(pingId), ping);
        }
        writePing(false, pingId, 1330343787, ping);
        return ping;
    }

    /* access modifiers changed from: private */
    public void writePingLater(boolean reply, int payload1, int payload2, Ping ping) {
        final boolean z = reply;
        final int i = payload1;
        final int i2 = payload2;
        final Ping ping2 = ping;
        executor.execute(new NamedRunnable("OkHttp %s ping %08x%08x", new Object[]{this.hostName, Integer.valueOf(payload1), Integer.valueOf(payload2)}) {
            public void execute() {
                try {
                    FramedConnection.this.writePing(z, i, i2, ping2);
                } catch (IOException e) {
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void writePing(boolean reply, int payload1, int payload2, Ping ping) throws IOException {
        synchronized (this.frameWriter) {
            if (ping != null) {
                ping.send();
            }
            this.frameWriter.ping(reply, payload1, payload2);
        }
    }

    /* access modifiers changed from: private */
    public synchronized Ping removePing(int id) {
        return this.pings != null ? this.pings.remove(Integer.valueOf(id)) : null;
    }

    public void flush() throws IOException {
        this.frameWriter.flush();
    }

    public void shutdown(ErrorCode statusCode) throws IOException {
        synchronized (this.frameWriter) {
            synchronized (this) {
                if (!this.shutdown) {
                    this.shutdown = true;
                    int lastGoodStreamId2 = this.lastGoodStreamId;
                    this.frameWriter.goAway(lastGoodStreamId2, statusCode, Util.EMPTY_BYTE_ARRAY);
                }
            }
        }
    }

    public void close() throws IOException {
        close(ErrorCode.NO_ERROR, ErrorCode.CANCEL);
    }

    /* JADX WARNING: type inference failed for: r7v15, types: [java.lang.Object[]] */
    /* JADX WARNING: type inference failed for: r7v19, types: [java.lang.Object[]] */
    /* access modifiers changed from: private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void close(com.squareup.okhttp.internal.framed.ErrorCode r11, com.squareup.okhttp.internal.framed.ErrorCode r12) throws java.io.IOException {
        /*
            r10 = this;
            r8 = 0
            boolean r7 = $assertionsDisabled
            if (r7 != 0) goto L_0x0011
            boolean r7 = java.lang.Thread.holdsLock(r10)
            if (r7 == 0) goto L_0x0011
            java.lang.AssertionError r7 = new java.lang.AssertionError
            r7.<init>()
            throw r7
        L_0x0011:
            r6 = 0
            r10.shutdown(r11)     // Catch:{ IOException -> 0x006b }
        L_0x0015:
            r5 = 0
            r3 = 0
            monitor-enter(r10)
            java.util.Map<java.lang.Integer, com.squareup.okhttp.internal.framed.FramedStream> r7 = r10.streams     // Catch:{ all -> 0x006e }
            boolean r7 = r7.isEmpty()     // Catch:{ all -> 0x006e }
            if (r7 != 0) goto L_0x003f
            java.util.Map<java.lang.Integer, com.squareup.okhttp.internal.framed.FramedStream> r7 = r10.streams     // Catch:{ all -> 0x006e }
            java.util.Collection r7 = r7.values()     // Catch:{ all -> 0x006e }
            java.util.Map<java.lang.Integer, com.squareup.okhttp.internal.framed.FramedStream> r9 = r10.streams     // Catch:{ all -> 0x006e }
            int r9 = r9.size()     // Catch:{ all -> 0x006e }
            com.squareup.okhttp.internal.framed.FramedStream[] r9 = new com.squareup.okhttp.internal.framed.FramedStream[r9]     // Catch:{ all -> 0x006e }
            java.lang.Object[] r7 = r7.toArray(r9)     // Catch:{ all -> 0x006e }
            r0 = r7
            com.squareup.okhttp.internal.framed.FramedStream[] r0 = (com.squareup.okhttp.internal.framed.FramedStream[]) r0     // Catch:{ all -> 0x006e }
            r5 = r0
            java.util.Map<java.lang.Integer, com.squareup.okhttp.internal.framed.FramedStream> r7 = r10.streams     // Catch:{ all -> 0x006e }
            r7.clear()     // Catch:{ all -> 0x006e }
            r7 = 0
            r10.setIdle(r7)     // Catch:{ all -> 0x006e }
        L_0x003f:
            java.util.Map<java.lang.Integer, com.squareup.okhttp.internal.framed.Ping> r7 = r10.pings     // Catch:{ all -> 0x006e }
            if (r7 == 0) goto L_0x005c
            java.util.Map<java.lang.Integer, com.squareup.okhttp.internal.framed.Ping> r7 = r10.pings     // Catch:{ all -> 0x006e }
            java.util.Collection r7 = r7.values()     // Catch:{ all -> 0x006e }
            java.util.Map<java.lang.Integer, com.squareup.okhttp.internal.framed.Ping> r9 = r10.pings     // Catch:{ all -> 0x006e }
            int r9 = r9.size()     // Catch:{ all -> 0x006e }
            com.squareup.okhttp.internal.framed.Ping[] r9 = new com.squareup.okhttp.internal.framed.Ping[r9]     // Catch:{ all -> 0x006e }
            java.lang.Object[] r7 = r7.toArray(r9)     // Catch:{ all -> 0x006e }
            r0 = r7
            com.squareup.okhttp.internal.framed.Ping[] r0 = (com.squareup.okhttp.internal.framed.Ping[]) r0     // Catch:{ all -> 0x006e }
            r3 = r0
            r7 = 0
            r10.pings = r7     // Catch:{ all -> 0x006e }
        L_0x005c:
            monitor-exit(r10)     // Catch:{ all -> 0x006e }
            if (r5 == 0) goto L_0x0076
            int r9 = r5.length
            r7 = r8
        L_0x0061:
            if (r7 >= r9) goto L_0x0076
            r4 = r5[r7]
            r4.close(r12)     // Catch:{ IOException -> 0x0071 }
        L_0x0068:
            int r7 = r7 + 1
            goto L_0x0061
        L_0x006b:
            r1 = move-exception
            r6 = r1
            goto L_0x0015
        L_0x006e:
            r7 = move-exception
            monitor-exit(r10)     // Catch:{ all -> 0x006e }
            throw r7
        L_0x0071:
            r1 = move-exception
            if (r6 == 0) goto L_0x0068
            r6 = r1
            goto L_0x0068
        L_0x0076:
            if (r3 == 0) goto L_0x0084
            int r9 = r3.length
            r7 = r8
        L_0x007a:
            if (r7 >= r9) goto L_0x0084
            r2 = r3[r7]
            r2.cancel()
            int r7 = r7 + 1
            goto L_0x007a
        L_0x0084:
            com.squareup.okhttp.internal.framed.FrameWriter r7 = r10.frameWriter     // Catch:{ IOException -> 0x0091 }
            r7.close()     // Catch:{ IOException -> 0x0091 }
        L_0x0089:
            java.net.Socket r7 = r10.socket     // Catch:{ IOException -> 0x0096 }
            r7.close()     // Catch:{ IOException -> 0x0096 }
        L_0x008e:
            if (r6 == 0) goto L_0x0099
            throw r6
        L_0x0091:
            r1 = move-exception
            if (r6 != 0) goto L_0x0089
            r6 = r1
            goto L_0x0089
        L_0x0096:
            r1 = move-exception
            r6 = r1
            goto L_0x008e
        L_0x0099:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.squareup.okhttp.internal.framed.FramedConnection.close(com.squareup.okhttp.internal.framed.ErrorCode, com.squareup.okhttp.internal.framed.ErrorCode):void");
    }

    public void sendConnectionPreface() throws IOException {
        this.frameWriter.connectionPreface();
        this.frameWriter.settings(this.okHttpSettings);
        int windowSize = this.okHttpSettings.getInitialWindowSize(65536);
        if (windowSize != 65536) {
            this.frameWriter.windowUpdate(0, (long) (windowSize - 65536));
        }
    }

    public void setSettings(Settings settings) throws IOException {
        synchronized (this.frameWriter) {
            synchronized (this) {
                if (this.shutdown) {
                    throw new IOException("shutdown");
                }
                this.okHttpSettings.merge(settings);
                this.frameWriter.settings(settings);
            }
        }
    }

    public static class Builder {
        /* access modifiers changed from: private */
        public boolean client;
        /* access modifiers changed from: private */
        public String hostName;
        /* access modifiers changed from: private */
        public Listener listener = Listener.REFUSE_INCOMING_STREAMS;
        /* access modifiers changed from: private */
        public Protocol protocol = Protocol.SPDY_3;
        /* access modifiers changed from: private */
        public PushObserver pushObserver = PushObserver.CANCEL;
        /* access modifiers changed from: private */
        public BufferedSink sink;
        /* access modifiers changed from: private */
        public Socket socket;
        /* access modifiers changed from: private */
        public BufferedSource source;

        public Builder(boolean client2) throws IOException {
            this.client = client2;
        }

        public Builder socket(Socket socket2) throws IOException {
            return socket(socket2, ((InetSocketAddress) socket2.getRemoteSocketAddress()).getHostName(), Okio.buffer(Okio.source(socket2)), Okio.buffer(Okio.sink(socket2)));
        }

        public Builder socket(Socket socket2, String hostName2, BufferedSource source2, BufferedSink sink2) {
            this.socket = socket2;
            this.hostName = hostName2;
            this.source = source2;
            this.sink = sink2;
            return this;
        }

        public Builder listener(Listener listener2) {
            this.listener = listener2;
            return this;
        }

        public Builder protocol(Protocol protocol2) {
            this.protocol = protocol2;
            return this;
        }

        public Builder pushObserver(PushObserver pushObserver2) {
            this.pushObserver = pushObserver2;
            return this;
        }

        public FramedConnection build() throws IOException {
            return new FramedConnection(this);
        }
    }

    class Reader extends NamedRunnable implements FrameReader.Handler {
        final FrameReader frameReader;

        private Reader(FrameReader frameReader2) {
            super("OkHttp %s", FramedConnection.this.hostName);
            this.frameReader = frameReader2;
        }

        /* access modifiers changed from: protected */
        public void execute() {
            ErrorCode connectionErrorCode = ErrorCode.INTERNAL_ERROR;
            ErrorCode streamErrorCode = ErrorCode.INTERNAL_ERROR;
            try {
                if (!FramedConnection.this.client) {
                    this.frameReader.readConnectionPreface();
                }
                do {
                } while (this.frameReader.nextFrame(this));
                try {
                    FramedConnection.this.close(ErrorCode.NO_ERROR, ErrorCode.CANCEL);
                } catch (IOException e) {
                }
                Util.closeQuietly((Closeable) this.frameReader);
            } catch (IOException e2) {
                connectionErrorCode = ErrorCode.PROTOCOL_ERROR;
                try {
                    FramedConnection.this.close(connectionErrorCode, ErrorCode.PROTOCOL_ERROR);
                } catch (IOException e3) {
                }
                Util.closeQuietly((Closeable) this.frameReader);
            } catch (Throwable th) {
                try {
                    FramedConnection.this.close(connectionErrorCode, streamErrorCode);
                } catch (IOException e4) {
                }
                Util.closeQuietly((Closeable) this.frameReader);
                throw th;
            }
        }

        public void data(boolean inFinished, int streamId, BufferedSource source, int length) throws IOException {
            if (FramedConnection.this.pushedStream(streamId)) {
                FramedConnection.this.pushDataLater(streamId, source, length, inFinished);
                return;
            }
            FramedStream dataStream = FramedConnection.this.getStream(streamId);
            if (dataStream == null) {
                FramedConnection.this.writeSynResetLater(streamId, ErrorCode.INVALID_STREAM);
                source.skip((long) length);
                return;
            }
            dataStream.receiveData(source, length);
            if (inFinished) {
                dataStream.receiveFin();
            }
        }

        /* JADX WARNING: Code restructure failed: missing block: B:34:0x0092, code lost:
            if (r15.failIfStreamPresent() == false) goto L_0x00a0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x0094, code lost:
            r6.closeLater(com.squareup.okhttp.internal.framed.ErrorCode.PROTOCOL_ERROR);
            r9.this$0.removeStream(r12);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x00a0, code lost:
            r6.receiveHeaders(r14, r15);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x00a3, code lost:
            if (r11 == false) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x00a5, code lost:
            r6.receiveFin();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:46:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:47:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void headers(boolean r10, boolean r11, int r12, int r13, java.util.List<com.squareup.okhttp.internal.framed.Header> r14, com.squareup.okhttp.internal.framed.HeadersMode r15) {
            /*
                r9 = this;
                com.squareup.okhttp.internal.framed.FramedConnection r1 = com.squareup.okhttp.internal.framed.FramedConnection.this
                boolean r1 = r1.pushedStream(r12)
                if (r1 == 0) goto L_0x000e
                com.squareup.okhttp.internal.framed.FramedConnection r1 = com.squareup.okhttp.internal.framed.FramedConnection.this
                r1.pushHeadersLater(r12, r14, r11)
            L_0x000d:
                return
            L_0x000e:
                com.squareup.okhttp.internal.framed.FramedConnection r7 = com.squareup.okhttp.internal.framed.FramedConnection.this
                monitor-enter(r7)
                com.squareup.okhttp.internal.framed.FramedConnection r1 = com.squareup.okhttp.internal.framed.FramedConnection.this     // Catch:{ all -> 0x001b }
                boolean r1 = r1.shutdown     // Catch:{ all -> 0x001b }
                if (r1 == 0) goto L_0x001e
                monitor-exit(r7)     // Catch:{ all -> 0x001b }
                goto L_0x000d
            L_0x001b:
                r1 = move-exception
                monitor-exit(r7)     // Catch:{ all -> 0x001b }
                throw r1
            L_0x001e:
                com.squareup.okhttp.internal.framed.FramedConnection r1 = com.squareup.okhttp.internal.framed.FramedConnection.this     // Catch:{ all -> 0x001b }
                com.squareup.okhttp.internal.framed.FramedStream r6 = r1.getStream(r12)     // Catch:{ all -> 0x001b }
                if (r6 != 0) goto L_0x008d
                boolean r1 = r15.failIfStreamAbsent()     // Catch:{ all -> 0x001b }
                if (r1 == 0) goto L_0x0035
                com.squareup.okhttp.internal.framed.FramedConnection r1 = com.squareup.okhttp.internal.framed.FramedConnection.this     // Catch:{ all -> 0x001b }
                com.squareup.okhttp.internal.framed.ErrorCode r2 = com.squareup.okhttp.internal.framed.ErrorCode.INVALID_STREAM     // Catch:{ all -> 0x001b }
                r1.writeSynResetLater(r12, r2)     // Catch:{ all -> 0x001b }
                monitor-exit(r7)     // Catch:{ all -> 0x001b }
                goto L_0x000d
            L_0x0035:
                com.squareup.okhttp.internal.framed.FramedConnection r1 = com.squareup.okhttp.internal.framed.FramedConnection.this     // Catch:{ all -> 0x001b }
                int r1 = r1.lastGoodStreamId     // Catch:{ all -> 0x001b }
                if (r12 > r1) goto L_0x003f
                monitor-exit(r7)     // Catch:{ all -> 0x001b }
                goto L_0x000d
            L_0x003f:
                int r1 = r12 % 2
                com.squareup.okhttp.internal.framed.FramedConnection r2 = com.squareup.okhttp.internal.framed.FramedConnection.this     // Catch:{ all -> 0x001b }
                int r2 = r2.nextStreamId     // Catch:{ all -> 0x001b }
                int r2 = r2 % 2
                if (r1 != r2) goto L_0x004d
                monitor-exit(r7)     // Catch:{ all -> 0x001b }
                goto L_0x000d
            L_0x004d:
                com.squareup.okhttp.internal.framed.FramedStream r0 = new com.squareup.okhttp.internal.framed.FramedStream     // Catch:{ all -> 0x001b }
                com.squareup.okhttp.internal.framed.FramedConnection r2 = com.squareup.okhttp.internal.framed.FramedConnection.this     // Catch:{ all -> 0x001b }
                r1 = r12
                r3 = r10
                r4 = r11
                r5 = r14
                r0.<init>(r1, r2, r3, r4, r5)     // Catch:{ all -> 0x001b }
                com.squareup.okhttp.internal.framed.FramedConnection r1 = com.squareup.okhttp.internal.framed.FramedConnection.this     // Catch:{ all -> 0x001b }
                int unused = r1.lastGoodStreamId = r12     // Catch:{ all -> 0x001b }
                com.squareup.okhttp.internal.framed.FramedConnection r1 = com.squareup.okhttp.internal.framed.FramedConnection.this     // Catch:{ all -> 0x001b }
                java.util.Map r1 = r1.streams     // Catch:{ all -> 0x001b }
                java.lang.Integer r2 = java.lang.Integer.valueOf(r12)     // Catch:{ all -> 0x001b }
                r1.put(r2, r0)     // Catch:{ all -> 0x001b }
                java.util.concurrent.ExecutorService r1 = com.squareup.okhttp.internal.framed.FramedConnection.executor     // Catch:{ all -> 0x001b }
                com.squareup.okhttp.internal.framed.FramedConnection$Reader$1 r2 = new com.squareup.okhttp.internal.framed.FramedConnection$Reader$1     // Catch:{ all -> 0x001b }
                java.lang.String r3 = "OkHttp %s stream %d"
                r4 = 2
                java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch:{ all -> 0x001b }
                r5 = 0
                com.squareup.okhttp.internal.framed.FramedConnection r8 = com.squareup.okhttp.internal.framed.FramedConnection.this     // Catch:{ all -> 0x001b }
                java.lang.String r8 = r8.hostName     // Catch:{ all -> 0x001b }
                r4[r5] = r8     // Catch:{ all -> 0x001b }
                r5 = 1
                java.lang.Integer r8 = java.lang.Integer.valueOf(r12)     // Catch:{ all -> 0x001b }
                r4[r5] = r8     // Catch:{ all -> 0x001b }
                r2.<init>(r3, r4, r0)     // Catch:{ all -> 0x001b }
                r1.execute(r2)     // Catch:{ all -> 0x001b }
                monitor-exit(r7)     // Catch:{ all -> 0x001b }
                goto L_0x000d
            L_0x008d:
                monitor-exit(r7)     // Catch:{ all -> 0x001b }
                boolean r1 = r15.failIfStreamPresent()
                if (r1 == 0) goto L_0x00a0
                com.squareup.okhttp.internal.framed.ErrorCode r1 = com.squareup.okhttp.internal.framed.ErrorCode.PROTOCOL_ERROR
                r6.closeLater(r1)
                com.squareup.okhttp.internal.framed.FramedConnection r1 = com.squareup.okhttp.internal.framed.FramedConnection.this
                r1.removeStream(r12)
                goto L_0x000d
            L_0x00a0:
                r6.receiveHeaders(r14, r15)
                if (r11 == 0) goto L_0x000d
                r6.receiveFin()
                goto L_0x000d
            */
            throw new UnsupportedOperationException("Method not decompiled: com.squareup.okhttp.internal.framed.FramedConnection.Reader.headers(boolean, boolean, int, int, java.util.List, com.squareup.okhttp.internal.framed.HeadersMode):void");
        }

        public void rstStream(int streamId, ErrorCode errorCode) {
            if (FramedConnection.this.pushedStream(streamId)) {
                FramedConnection.this.pushResetLater(streamId, errorCode);
                return;
            }
            FramedStream rstStream = FramedConnection.this.removeStream(streamId);
            if (rstStream != null) {
                rstStream.receiveRstStream(errorCode);
            }
        }

        /* JADX WARNING: type inference failed for: r8v26, types: [java.lang.Object[]] */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void settings(boolean r16, com.squareup.okhttp.internal.framed.Settings r17) {
            /*
                r15 = this;
                r2 = 0
                r7 = 0
                com.squareup.okhttp.internal.framed.FramedConnection r9 = com.squareup.okhttp.internal.framed.FramedConnection.this
                monitor-enter(r9)
                com.squareup.okhttp.internal.framed.FramedConnection r8 = com.squareup.okhttp.internal.framed.FramedConnection.this     // Catch:{ all -> 0x00b1 }
                com.squareup.okhttp.internal.framed.Settings r8 = r8.peerSettings     // Catch:{ all -> 0x00b1 }
                r10 = 65536(0x10000, float:9.18355E-41)
                int r5 = r8.getInitialWindowSize(r10)     // Catch:{ all -> 0x00b1 }
                if (r16 == 0) goto L_0x0019
                com.squareup.okhttp.internal.framed.FramedConnection r8 = com.squareup.okhttp.internal.framed.FramedConnection.this     // Catch:{ all -> 0x00b1 }
                com.squareup.okhttp.internal.framed.Settings r8 = r8.peerSettings     // Catch:{ all -> 0x00b1 }
                r8.clear()     // Catch:{ all -> 0x00b1 }
            L_0x0019:
                com.squareup.okhttp.internal.framed.FramedConnection r8 = com.squareup.okhttp.internal.framed.FramedConnection.this     // Catch:{ all -> 0x00b1 }
                com.squareup.okhttp.internal.framed.Settings r8 = r8.peerSettings     // Catch:{ all -> 0x00b1 }
                r0 = r17
                r8.merge(r0)     // Catch:{ all -> 0x00b1 }
                com.squareup.okhttp.internal.framed.FramedConnection r8 = com.squareup.okhttp.internal.framed.FramedConnection.this     // Catch:{ all -> 0x00b1 }
                com.squareup.okhttp.Protocol r8 = r8.getProtocol()     // Catch:{ all -> 0x00b1 }
                com.squareup.okhttp.Protocol r10 = com.squareup.okhttp.Protocol.HTTP_2     // Catch:{ all -> 0x00b1 }
                if (r8 != r10) goto L_0x0031
                r0 = r17
                r15.ackSettingsLater(r0)     // Catch:{ all -> 0x00b1 }
            L_0x0031:
                com.squareup.okhttp.internal.framed.FramedConnection r8 = com.squareup.okhttp.internal.framed.FramedConnection.this     // Catch:{ all -> 0x00b1 }
                com.squareup.okhttp.internal.framed.Settings r8 = r8.peerSettings     // Catch:{ all -> 0x00b1 }
                r10 = 65536(0x10000, float:9.18355E-41)
                int r4 = r8.getInitialWindowSize(r10)     // Catch:{ all -> 0x00b1 }
                r8 = -1
                if (r4 == r8) goto L_0x0080
                if (r4 == r5) goto L_0x0080
                int r8 = r4 - r5
                long r2 = (long) r8     // Catch:{ all -> 0x00b1 }
                com.squareup.okhttp.internal.framed.FramedConnection r8 = com.squareup.okhttp.internal.framed.FramedConnection.this     // Catch:{ all -> 0x00b1 }
                boolean r8 = r8.receivedInitialPeerSettings     // Catch:{ all -> 0x00b1 }
                if (r8 != 0) goto L_0x0056
                com.squareup.okhttp.internal.framed.FramedConnection r8 = com.squareup.okhttp.internal.framed.FramedConnection.this     // Catch:{ all -> 0x00b1 }
                r8.addBytesToWriteWindow(r2)     // Catch:{ all -> 0x00b1 }
                com.squareup.okhttp.internal.framed.FramedConnection r8 = com.squareup.okhttp.internal.framed.FramedConnection.this     // Catch:{ all -> 0x00b1 }
                r10 = 1
                boolean unused = r8.receivedInitialPeerSettings = r10     // Catch:{ all -> 0x00b1 }
            L_0x0056:
                com.squareup.okhttp.internal.framed.FramedConnection r8 = com.squareup.okhttp.internal.framed.FramedConnection.this     // Catch:{ all -> 0x00b1 }
                java.util.Map r8 = r8.streams     // Catch:{ all -> 0x00b1 }
                boolean r8 = r8.isEmpty()     // Catch:{ all -> 0x00b1 }
                if (r8 != 0) goto L_0x0080
                com.squareup.okhttp.internal.framed.FramedConnection r8 = com.squareup.okhttp.internal.framed.FramedConnection.this     // Catch:{ all -> 0x00b1 }
                java.util.Map r8 = r8.streams     // Catch:{ all -> 0x00b1 }
                java.util.Collection r8 = r8.values()     // Catch:{ all -> 0x00b1 }
                com.squareup.okhttp.internal.framed.FramedConnection r10 = com.squareup.okhttp.internal.framed.FramedConnection.this     // Catch:{ all -> 0x00b1 }
                java.util.Map r10 = r10.streams     // Catch:{ all -> 0x00b1 }
                int r10 = r10.size()     // Catch:{ all -> 0x00b1 }
                com.squareup.okhttp.internal.framed.FramedStream[] r10 = new com.squareup.okhttp.internal.framed.FramedStream[r10]     // Catch:{ all -> 0x00b1 }
                java.lang.Object[] r8 = r8.toArray(r10)     // Catch:{ all -> 0x00b1 }
                r0 = r8
                com.squareup.okhttp.internal.framed.FramedStream[] r0 = (com.squareup.okhttp.internal.framed.FramedStream[]) r0     // Catch:{ all -> 0x00b1 }
                r7 = r0
            L_0x0080:
                java.util.concurrent.ExecutorService r8 = com.squareup.okhttp.internal.framed.FramedConnection.executor     // Catch:{ all -> 0x00b1 }
                com.squareup.okhttp.internal.framed.FramedConnection$Reader$2 r10 = new com.squareup.okhttp.internal.framed.FramedConnection$Reader$2     // Catch:{ all -> 0x00b1 }
                java.lang.String r11 = "OkHttp %s settings"
                r12 = 1
                java.lang.Object[] r12 = new java.lang.Object[r12]     // Catch:{ all -> 0x00b1 }
                r13 = 0
                com.squareup.okhttp.internal.framed.FramedConnection r14 = com.squareup.okhttp.internal.framed.FramedConnection.this     // Catch:{ all -> 0x00b1 }
                java.lang.String r14 = r14.hostName     // Catch:{ all -> 0x00b1 }
                r12[r13] = r14     // Catch:{ all -> 0x00b1 }
                r10.<init>(r11, r12)     // Catch:{ all -> 0x00b1 }
                r8.execute(r10)     // Catch:{ all -> 0x00b1 }
                monitor-exit(r9)     // Catch:{ all -> 0x00b1 }
                if (r7 == 0) goto L_0x00b7
                r8 = 0
                int r8 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1))
                if (r8 == 0) goto L_0x00b7
                int r9 = r7.length
                r8 = 0
            L_0x00a5:
                if (r8 >= r9) goto L_0x00b7
                r6 = r7[r8]
                monitor-enter(r6)
                r6.addBytesToWriteWindow(r2)     // Catch:{ all -> 0x00b4 }
                monitor-exit(r6)     // Catch:{ all -> 0x00b4 }
                int r8 = r8 + 1
                goto L_0x00a5
            L_0x00b1:
                r8 = move-exception
                monitor-exit(r9)     // Catch:{ all -> 0x00b1 }
                throw r8
            L_0x00b4:
                r8 = move-exception
                monitor-exit(r6)     // Catch:{ all -> 0x00b4 }
                throw r8
            L_0x00b7:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.squareup.okhttp.internal.framed.FramedConnection.Reader.settings(boolean, com.squareup.okhttp.internal.framed.Settings):void");
        }

        private void ackSettingsLater(final Settings peerSettings) {
            FramedConnection.executor.execute(new NamedRunnable("OkHttp %s ACK Settings", new Object[]{FramedConnection.this.hostName}) {
                public void execute() {
                    try {
                        FramedConnection.this.frameWriter.ackSettings(peerSettings);
                    } catch (IOException e) {
                    }
                }
            });
        }

        public void ackSettings() {
        }

        public void ping(boolean reply, int payload1, int payload2) {
            if (reply) {
                Ping ping = FramedConnection.this.removePing(payload1);
                if (ping != null) {
                    ping.receive();
                    return;
                }
                return;
            }
            FramedConnection.this.writePingLater(true, payload1, payload2, (Ping) null);
        }

        public void goAway(int lastGoodStreamId, ErrorCode errorCode, ByteString debugData) {
            FramedStream[] streamsCopy;
            if (debugData.size() > 0) {
            }
            synchronized (FramedConnection.this) {
                streamsCopy = (FramedStream[]) FramedConnection.this.streams.values().toArray(new FramedStream[FramedConnection.this.streams.size()]);
                boolean unused = FramedConnection.this.shutdown = true;
            }
            for (FramedStream framedStream : streamsCopy) {
                if (framedStream.getId() > lastGoodStreamId && framedStream.isLocallyInitiated()) {
                    framedStream.receiveRstStream(ErrorCode.REFUSED_STREAM);
                    FramedConnection.this.removeStream(framedStream.getId());
                }
            }
        }

        public void windowUpdate(int streamId, long windowSizeIncrement) {
            if (streamId == 0) {
                synchronized (FramedConnection.this) {
                    FramedConnection.this.bytesLeftInWriteWindow += windowSizeIncrement;
                    FramedConnection.this.notifyAll();
                }
                return;
            }
            FramedStream stream = FramedConnection.this.getStream(streamId);
            if (stream != null) {
                synchronized (stream) {
                    stream.addBytesToWriteWindow(windowSizeIncrement);
                }
            }
        }

        public void priority(int streamId, int streamDependency, int weight, boolean exclusive) {
        }

        public void pushPromise(int streamId, int promisedStreamId, List<Header> requestHeaders) {
            FramedConnection.this.pushRequestLater(promisedStreamId, requestHeaders);
        }

        public void alternateService(int streamId, String origin, ByteString protocol, String host, int port, long maxAge) {
        }
    }

    /* access modifiers changed from: private */
    public boolean pushedStream(int streamId) {
        return this.protocol == Protocol.HTTP_2 && streamId != 0 && (streamId & 1) == 0;
    }

    /* access modifiers changed from: private */
    public void pushRequestLater(int streamId, List<Header> requestHeaders) {
        synchronized (this) {
            if (this.currentPushRequests.contains(Integer.valueOf(streamId))) {
                writeSynResetLater(streamId, ErrorCode.PROTOCOL_ERROR);
                return;
            }
            this.currentPushRequests.add(Integer.valueOf(streamId));
            final int i = streamId;
            final List<Header> list = requestHeaders;
            this.pushExecutor.execute(new NamedRunnable("OkHttp %s Push Request[%s]", new Object[]{this.hostName, Integer.valueOf(streamId)}) {
                public void execute() {
                    if (FramedConnection.this.pushObserver.onRequest(i, list)) {
                        try {
                            FramedConnection.this.frameWriter.rstStream(i, ErrorCode.CANCEL);
                            synchronized (FramedConnection.this) {
                                FramedConnection.this.currentPushRequests.remove(Integer.valueOf(i));
                            }
                        } catch (IOException e) {
                        }
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void pushHeadersLater(int streamId, List<Header> requestHeaders, boolean inFinished) {
        final int i = streamId;
        final List<Header> list = requestHeaders;
        final boolean z = inFinished;
        this.pushExecutor.execute(new NamedRunnable("OkHttp %s Push Headers[%s]", new Object[]{this.hostName, Integer.valueOf(streamId)}) {
            public void execute() {
                boolean cancel = FramedConnection.this.pushObserver.onHeaders(i, list, z);
                if (cancel) {
                    try {
                        FramedConnection.this.frameWriter.rstStream(i, ErrorCode.CANCEL);
                    } catch (IOException e) {
                        return;
                    }
                }
                if (cancel || z) {
                    synchronized (FramedConnection.this) {
                        FramedConnection.this.currentPushRequests.remove(Integer.valueOf(i));
                    }
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void pushDataLater(int streamId, BufferedSource source, int byteCount, boolean inFinished) throws IOException {
        final Buffer buffer = new Buffer();
        source.require((long) byteCount);
        source.read(buffer, (long) byteCount);
        if (buffer.size() != ((long) byteCount)) {
            throw new IOException(buffer.size() + " != " + byteCount);
        }
        final int i = streamId;
        final int i2 = byteCount;
        final boolean z = inFinished;
        this.pushExecutor.execute(new NamedRunnable("OkHttp %s Push Data[%s]", new Object[]{this.hostName, Integer.valueOf(streamId)}) {
            public void execute() {
                try {
                    boolean cancel = FramedConnection.this.pushObserver.onData(i, buffer, i2, z);
                    if (cancel) {
                        FramedConnection.this.frameWriter.rstStream(i, ErrorCode.CANCEL);
                    }
                    if (cancel || z) {
                        synchronized (FramedConnection.this) {
                            FramedConnection.this.currentPushRequests.remove(Integer.valueOf(i));
                        }
                    }
                } catch (IOException e) {
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void pushResetLater(int streamId, ErrorCode errorCode) {
        final int i = streamId;
        final ErrorCode errorCode2 = errorCode;
        this.pushExecutor.execute(new NamedRunnable("OkHttp %s Push Reset[%s]", new Object[]{this.hostName, Integer.valueOf(streamId)}) {
            public void execute() {
                FramedConnection.this.pushObserver.onReset(i, errorCode2);
                synchronized (FramedConnection.this) {
                    FramedConnection.this.currentPushRequests.remove(Integer.valueOf(i));
                }
            }
        });
    }

    public static abstract class Listener {
        public static final Listener REFUSE_INCOMING_STREAMS = new Listener() {
            public void onStream(FramedStream stream) throws IOException {
                stream.close(ErrorCode.REFUSED_STREAM);
            }
        };

        public abstract void onStream(FramedStream framedStream) throws IOException;

        public void onSettings(FramedConnection connection) {
        }
    }
}
