package com.squareup.okhttp.internal.io;

import android.support.v7.widget.ActivityChooserView;
import com.squareup.okhttp.Connection;
import com.squareup.okhttp.Handshake;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.Route;
import com.squareup.okhttp.internal.ConnectionSpecSelector;
import com.squareup.okhttp.internal.Platform;
import com.squareup.okhttp.internal.Util;
import com.squareup.okhttp.internal.Version;
import com.squareup.okhttp.internal.framed.FramedConnection;
import com.squareup.okhttp.internal.http.Http1xStream;
import com.squareup.okhttp.internal.http.OkHeaders;
import com.squareup.okhttp.internal.http.StreamAllocation;
import com.squareup.okhttp.internal.tls.TrustRootIndex;
import java.io.IOException;
import java.lang.ref.Reference;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLSocketFactory;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;

public final class RealConnection implements Connection {
    private static SSLSocketFactory lastSslSocketFactory;
    private static TrustRootIndex lastTrustRootIndex;
    public final List<Reference<StreamAllocation>> allocations = new ArrayList();
    public volatile FramedConnection framedConnection;
    private Handshake handshake;
    public long idleAtNanos = Long.MAX_VALUE;
    public boolean noNewStreams;
    private Protocol protocol;
    private Socket rawSocket;
    private final Route route;
    public BufferedSink sink;
    public Socket socket;
    public BufferedSource source;
    public int streamCount;

    public RealConnection(Route route2) {
        this.route = route2;
    }

    /* JADX WARNING: CFG modification limit reached, blocks count: 134 */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x005f A[SYNTHETIC, Splitter:B:15:0x005f] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void connect(int r10, int r11, int r12, java.util.List<com.squareup.okhttp.ConnectionSpec> r13, boolean r14) throws com.squareup.okhttp.internal.http.RouteException {
        /*
            r9 = this;
            r7 = 0
            com.squareup.okhttp.Protocol r5 = r9.protocol
            if (r5 == 0) goto L_0x000d
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
            java.lang.String r6 = "already connected"
            r5.<init>(r6)
            throw r5
        L_0x000d:
            r4 = 0
            com.squareup.okhttp.internal.ConnectionSpecSelector r1 = new com.squareup.okhttp.internal.ConnectionSpecSelector
            r1.<init>(r13)
            com.squareup.okhttp.Route r5 = r9.route
            java.net.Proxy r3 = r5.getProxy()
            com.squareup.okhttp.Route r5 = r9.route
            com.squareup.okhttp.Address r0 = r5.getAddress()
            com.squareup.okhttp.Route r5 = r9.route
            com.squareup.okhttp.Address r5 = r5.getAddress()
            javax.net.ssl.SSLSocketFactory r5 = r5.getSslSocketFactory()
            if (r5 != 0) goto L_0x005b
            com.squareup.okhttp.ConnectionSpec r5 = com.squareup.okhttp.ConnectionSpec.CLEARTEXT
            boolean r5 = r13.contains(r5)
            if (r5 != 0) goto L_0x005b
            com.squareup.okhttp.internal.http.RouteException r5 = new com.squareup.okhttp.internal.http.RouteException
            java.net.UnknownServiceException r6 = new java.net.UnknownServiceException
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.String r8 = "CLEARTEXT communication not supported: "
            java.lang.StringBuilder r7 = r7.append(r8)
            java.lang.StringBuilder r7 = r7.append(r13)
            java.lang.String r7 = r7.toString()
            r6.<init>(r7)
            r5.<init>(r6)
            throw r5
        L_0x0051:
            java.net.Socket r5 = new java.net.Socket     // Catch:{ IOException -> 0x0078 }
            r5.<init>(r3)     // Catch:{ IOException -> 0x0078 }
        L_0x0056:
            r9.rawSocket = r5     // Catch:{ IOException -> 0x0078 }
            r9.connectSocket(r10, r11, r12, r1)     // Catch:{ IOException -> 0x0078 }
        L_0x005b:
            com.squareup.okhttp.Protocol r5 = r9.protocol
            if (r5 != 0) goto L_0x00a3
            java.net.Proxy$Type r5 = r3.type()     // Catch:{ IOException -> 0x0078 }
            java.net.Proxy$Type r6 = java.net.Proxy.Type.DIRECT     // Catch:{ IOException -> 0x0078 }
            if (r5 == r6) goto L_0x006f
            java.net.Proxy$Type r5 = r3.type()     // Catch:{ IOException -> 0x0078 }
            java.net.Proxy$Type r6 = java.net.Proxy.Type.HTTP     // Catch:{ IOException -> 0x0078 }
            if (r5 != r6) goto L_0x0051
        L_0x006f:
            javax.net.SocketFactory r5 = r0.getSocketFactory()     // Catch:{ IOException -> 0x0078 }
            java.net.Socket r5 = r5.createSocket()     // Catch:{ IOException -> 0x0078 }
            goto L_0x0056
        L_0x0078:
            r2 = move-exception
            java.net.Socket r5 = r9.socket
            com.squareup.okhttp.internal.Util.closeQuietly((java.net.Socket) r5)
            java.net.Socket r5 = r9.rawSocket
            com.squareup.okhttp.internal.Util.closeQuietly((java.net.Socket) r5)
            r9.socket = r7
            r9.rawSocket = r7
            r9.source = r7
            r9.sink = r7
            r9.handshake = r7
            r9.protocol = r7
            if (r4 != 0) goto L_0x009f
            com.squareup.okhttp.internal.http.RouteException r4 = new com.squareup.okhttp.internal.http.RouteException
            r4.<init>(r2)
        L_0x0096:
            if (r14 == 0) goto L_0x009e
            boolean r5 = r1.connectionFailed(r2)
            if (r5 != 0) goto L_0x005b
        L_0x009e:
            throw r4
        L_0x009f:
            r4.addConnectException(r2)
            goto L_0x0096
        L_0x00a3:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.squareup.okhttp.internal.io.RealConnection.connect(int, int, int, java.util.List, boolean):void");
    }

    private void connectSocket(int connectTimeout, int readTimeout, int writeTimeout, ConnectionSpecSelector connectionSpecSelector) throws IOException {
        this.rawSocket.setSoTimeout(readTimeout);
        try {
            Platform.get().connectSocket(this.rawSocket, this.route.getSocketAddress(), connectTimeout);
            this.source = Okio.buffer(Okio.source(this.rawSocket));
            this.sink = Okio.buffer(Okio.sink(this.rawSocket));
            if (this.route.getAddress().getSslSocketFactory() != null) {
                connectTls(readTimeout, writeTimeout, connectionSpecSelector);
            } else {
                this.protocol = Protocol.HTTP_1_1;
                this.socket = this.rawSocket;
            }
            if (this.protocol == Protocol.SPDY_3 || this.protocol == Protocol.HTTP_2) {
                this.socket.setSoTimeout(0);
                FramedConnection framedConnection2 = new FramedConnection.Builder(true).socket(this.socket, this.route.getAddress().url().host(), this.source, this.sink).protocol(this.protocol).build();
                framedConnection2.sendConnectionPreface();
                this.framedConnection = framedConnection2;
            }
        } catch (ConnectException e) {
            throw new ConnectException("Failed to connect to " + this.route.getSocketAddress());
        }
    }

    /* JADX WARNING: type inference failed for: r12v7, types: [java.net.Socket] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void connectTls(int r17, int r18, com.squareup.okhttp.internal.ConnectionSpecSelector r19) throws java.io.IOException {
        /*
            r16 = this;
            r0 = r16
            com.squareup.okhttp.Route r12 = r0.route
            boolean r12 = r12.requiresTunnel()
            if (r12 == 0) goto L_0x000d
            r16.createTunnel(r17, r18)
        L_0x000d:
            r0 = r16
            com.squareup.okhttp.Route r12 = r0.route
            com.squareup.okhttp.Address r1 = r12.getAddress()
            javax.net.ssl.SSLSocketFactory r8 = r1.getSslSocketFactory()
            r9 = 0
            r7 = 0
            r0 = r16
            java.net.Socket r12 = r0.rawSocket     // Catch:{ AssertionError -> 0x00c4 }
            java.lang.String r13 = r1.getUriHost()     // Catch:{ AssertionError -> 0x00c4 }
            int r14 = r1.getUriPort()     // Catch:{ AssertionError -> 0x00c4 }
            r15 = 1
            java.net.Socket r12 = r8.createSocket(r12, r13, r14, r15)     // Catch:{ AssertionError -> 0x00c4 }
            r0 = r12
            javax.net.ssl.SSLSocket r0 = (javax.net.ssl.SSLSocket) r0     // Catch:{ AssertionError -> 0x00c4 }
            r7 = r0
            r0 = r19
            com.squareup.okhttp.ConnectionSpec r4 = r0.configureSecureSocket(r7)     // Catch:{ AssertionError -> 0x00c4 }
            boolean r12 = r4.supportsTlsExtensions()     // Catch:{ AssertionError -> 0x00c4 }
            if (r12 == 0) goto L_0x004b
            com.squareup.okhttp.internal.Platform r12 = com.squareup.okhttp.internal.Platform.get()     // Catch:{ AssertionError -> 0x00c4 }
            java.lang.String r13 = r1.getUriHost()     // Catch:{ AssertionError -> 0x00c4 }
            java.util.List r14 = r1.getProtocols()     // Catch:{ AssertionError -> 0x00c4 }
            r12.configureTlsExtensions(r7, r13, r14)     // Catch:{ AssertionError -> 0x00c4 }
        L_0x004b:
            r7.startHandshake()     // Catch:{ AssertionError -> 0x00c4 }
            javax.net.ssl.SSLSession r12 = r7.getSession()     // Catch:{ AssertionError -> 0x00c4 }
            com.squareup.okhttp.Handshake r11 = com.squareup.okhttp.Handshake.get(r12)     // Catch:{ AssertionError -> 0x00c4 }
            javax.net.ssl.HostnameVerifier r12 = r1.getHostnameVerifier()     // Catch:{ AssertionError -> 0x00c4 }
            java.lang.String r13 = r1.getUriHost()     // Catch:{ AssertionError -> 0x00c4 }
            javax.net.ssl.SSLSession r14 = r7.getSession()     // Catch:{ AssertionError -> 0x00c4 }
            boolean r12 = r12.verify(r13, r14)     // Catch:{ AssertionError -> 0x00c4 }
            if (r12 != 0) goto L_0x00e1
            java.util.List r12 = r11.peerCertificates()     // Catch:{ AssertionError -> 0x00c4 }
            r13 = 0
            java.lang.Object r2 = r12.get(r13)     // Catch:{ AssertionError -> 0x00c4 }
            java.security.cert.X509Certificate r2 = (java.security.cert.X509Certificate) r2     // Catch:{ AssertionError -> 0x00c4 }
            javax.net.ssl.SSLPeerUnverifiedException r12 = new javax.net.ssl.SSLPeerUnverifiedException     // Catch:{ AssertionError -> 0x00c4 }
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ AssertionError -> 0x00c4 }
            r13.<init>()     // Catch:{ AssertionError -> 0x00c4 }
            java.lang.String r14 = "Hostname "
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ AssertionError -> 0x00c4 }
            java.lang.String r14 = r1.getUriHost()     // Catch:{ AssertionError -> 0x00c4 }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ AssertionError -> 0x00c4 }
            java.lang.String r14 = " not verified:"
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ AssertionError -> 0x00c4 }
            java.lang.String r14 = "\n    certificate: "
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ AssertionError -> 0x00c4 }
            java.lang.String r14 = com.squareup.okhttp.CertificatePinner.pin(r2)     // Catch:{ AssertionError -> 0x00c4 }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ AssertionError -> 0x00c4 }
            java.lang.String r14 = "\n    DN: "
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ AssertionError -> 0x00c4 }
            java.security.Principal r14 = r2.getSubjectDN()     // Catch:{ AssertionError -> 0x00c4 }
            java.lang.String r14 = r14.getName()     // Catch:{ AssertionError -> 0x00c4 }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ AssertionError -> 0x00c4 }
            java.lang.String r14 = "\n    subjectAltNames: "
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ AssertionError -> 0x00c4 }
            java.util.List r14 = com.squareup.okhttp.internal.tls.OkHostnameVerifier.allSubjectAltNames(r2)     // Catch:{ AssertionError -> 0x00c4 }
            java.lang.StringBuilder r13 = r13.append(r14)     // Catch:{ AssertionError -> 0x00c4 }
            java.lang.String r13 = r13.toString()     // Catch:{ AssertionError -> 0x00c4 }
            r12.<init>(r13)     // Catch:{ AssertionError -> 0x00c4 }
            throw r12     // Catch:{ AssertionError -> 0x00c4 }
        L_0x00c4:
            r5 = move-exception
            boolean r12 = com.squareup.okhttp.internal.Util.isAndroidGetsocknameError(r5)     // Catch:{ all -> 0x00d1 }
            if (r12 == 0) goto L_0x015e
            java.io.IOException r12 = new java.io.IOException     // Catch:{ all -> 0x00d1 }
            r12.<init>(r5)     // Catch:{ all -> 0x00d1 }
            throw r12     // Catch:{ all -> 0x00d1 }
        L_0x00d1:
            r12 = move-exception
            if (r7 == 0) goto L_0x00db
            com.squareup.okhttp.internal.Platform r13 = com.squareup.okhttp.internal.Platform.get()
            r13.afterHandshake(r7)
        L_0x00db:
            if (r9 != 0) goto L_0x00e0
            com.squareup.okhttp.internal.Util.closeQuietly((java.net.Socket) r7)
        L_0x00e0:
            throw r12
        L_0x00e1:
            com.squareup.okhttp.CertificatePinner r12 = r1.getCertificatePinner()     // Catch:{ AssertionError -> 0x00c4 }
            com.squareup.okhttp.CertificatePinner r13 = com.squareup.okhttp.CertificatePinner.DEFAULT     // Catch:{ AssertionError -> 0x00c4 }
            if (r12 == r13) goto L_0x0109
            javax.net.ssl.SSLSocketFactory r12 = r1.getSslSocketFactory()     // Catch:{ AssertionError -> 0x00c4 }
            com.squareup.okhttp.internal.tls.TrustRootIndex r10 = trustRootIndex(r12)     // Catch:{ AssertionError -> 0x00c4 }
            com.squareup.okhttp.internal.tls.CertificateChainCleaner r12 = new com.squareup.okhttp.internal.tls.CertificateChainCleaner     // Catch:{ AssertionError -> 0x00c4 }
            r12.<init>(r10)     // Catch:{ AssertionError -> 0x00c4 }
            java.util.List r13 = r11.peerCertificates()     // Catch:{ AssertionError -> 0x00c4 }
            java.util.List r3 = r12.clean(r13)     // Catch:{ AssertionError -> 0x00c4 }
            com.squareup.okhttp.CertificatePinner r12 = r1.getCertificatePinner()     // Catch:{ AssertionError -> 0x00c4 }
            java.lang.String r13 = r1.getUriHost()     // Catch:{ AssertionError -> 0x00c4 }
            r12.check((java.lang.String) r13, (java.util.List<java.security.cert.Certificate>) r3)     // Catch:{ AssertionError -> 0x00c4 }
        L_0x0109:
            boolean r12 = r4.supportsTlsExtensions()     // Catch:{ AssertionError -> 0x00c4 }
            if (r12 == 0) goto L_0x0159
            com.squareup.okhttp.internal.Platform r12 = com.squareup.okhttp.internal.Platform.get()     // Catch:{ AssertionError -> 0x00c4 }
            java.lang.String r6 = r12.getSelectedProtocol(r7)     // Catch:{ AssertionError -> 0x00c4 }
        L_0x0117:
            r0 = r16
            r0.socket = r7     // Catch:{ AssertionError -> 0x00c4 }
            r0 = r16
            java.net.Socket r12 = r0.socket     // Catch:{ AssertionError -> 0x00c4 }
            okio.Source r12 = okio.Okio.source((java.net.Socket) r12)     // Catch:{ AssertionError -> 0x00c4 }
            okio.BufferedSource r12 = okio.Okio.buffer((okio.Source) r12)     // Catch:{ AssertionError -> 0x00c4 }
            r0 = r16
            r0.source = r12     // Catch:{ AssertionError -> 0x00c4 }
            r0 = r16
            java.net.Socket r12 = r0.socket     // Catch:{ AssertionError -> 0x00c4 }
            okio.Sink r12 = okio.Okio.sink((java.net.Socket) r12)     // Catch:{ AssertionError -> 0x00c4 }
            okio.BufferedSink r12 = okio.Okio.buffer((okio.Sink) r12)     // Catch:{ AssertionError -> 0x00c4 }
            r0 = r16
            r0.sink = r12     // Catch:{ AssertionError -> 0x00c4 }
            r0 = r16
            r0.handshake = r11     // Catch:{ AssertionError -> 0x00c4 }
            if (r6 == 0) goto L_0x015b
            com.squareup.okhttp.Protocol r12 = com.squareup.okhttp.Protocol.get(r6)     // Catch:{ AssertionError -> 0x00c4 }
        L_0x0145:
            r0 = r16
            r0.protocol = r12     // Catch:{ AssertionError -> 0x00c4 }
            r9 = 1
            if (r7 == 0) goto L_0x0153
            com.squareup.okhttp.internal.Platform r12 = com.squareup.okhttp.internal.Platform.get()
            r12.afterHandshake(r7)
        L_0x0153:
            if (r9 != 0) goto L_0x0158
            com.squareup.okhttp.internal.Util.closeQuietly((java.net.Socket) r7)
        L_0x0158:
            return
        L_0x0159:
            r6 = 0
            goto L_0x0117
        L_0x015b:
            com.squareup.okhttp.Protocol r12 = com.squareup.okhttp.Protocol.HTTP_1_1     // Catch:{ AssertionError -> 0x00c4 }
            goto L_0x0145
        L_0x015e:
            throw r5     // Catch:{ all -> 0x00d1 }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.squareup.okhttp.internal.io.RealConnection.connectTls(int, int, com.squareup.okhttp.internal.ConnectionSpecSelector):void");
    }

    private static synchronized TrustRootIndex trustRootIndex(SSLSocketFactory sslSocketFactory) {
        TrustRootIndex trustRootIndex;
        synchronized (RealConnection.class) {
            if (sslSocketFactory != lastSslSocketFactory) {
                lastTrustRootIndex = Platform.get().trustRootIndex(Platform.get().trustManager(sslSocketFactory));
                lastSslSocketFactory = sslSocketFactory;
            }
            trustRootIndex = lastTrustRootIndex;
        }
        return trustRootIndex;
    }

    private void createTunnel(int readTimeout, int writeTimeout) throws IOException {
        Request tunnelRequest = createTunnelRequest();
        HttpUrl url = tunnelRequest.httpUrl();
        String requestLine = "CONNECT " + url.host() + ":" + url.port() + " HTTP/1.1";
        do {
            Http1xStream tunnelConnection = new Http1xStream((StreamAllocation) null, this.source, this.sink);
            this.source.timeout().timeout((long) readTimeout, TimeUnit.MILLISECONDS);
            this.sink.timeout().timeout((long) writeTimeout, TimeUnit.MILLISECONDS);
            tunnelConnection.writeRequest(tunnelRequest.headers(), requestLine);
            tunnelConnection.finishRequest();
            Response response = tunnelConnection.readResponse().request(tunnelRequest).build();
            long contentLength = OkHeaders.contentLength(response);
            if (contentLength == -1) {
                contentLength = 0;
            }
            Source body = tunnelConnection.newFixedLengthSource(contentLength);
            Util.skipAll(body, ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED, TimeUnit.MILLISECONDS);
            body.close();
            switch (response.code()) {
                case 200:
                    if (!this.source.buffer().exhausted() || !this.sink.buffer().exhausted()) {
                        throw new IOException("TLS tunnel buffered too many bytes!");
                    }
                    return;
                case 407:
                    tunnelRequest = OkHeaders.processAuthHeader(this.route.getAddress().getAuthenticator(), response, this.route.getProxy());
                    break;
                default:
                    throw new IOException("Unexpected response code for CONNECT: " + response.code());
            }
        } while (tunnelRequest != null);
        throw new IOException("Failed to authenticate with proxy");
    }

    private Request createTunnelRequest() throws IOException {
        return new Request.Builder().url(this.route.getAddress().url()).header("Host", Util.hostHeader(this.route.getAddress().url())).header("Proxy-Connection", "Keep-Alive").header("User-Agent", Version.userAgent()).build();
    }

    /* access modifiers changed from: package-private */
    public boolean isConnected() {
        return this.protocol != null;
    }

    public Route getRoute() {
        return this.route;
    }

    public void cancel() {
        Util.closeQuietly(this.rawSocket);
    }

    public Socket getSocket() {
        return this.socket;
    }

    public int allocationLimit() {
        FramedConnection framedConnection2 = this.framedConnection;
        if (framedConnection2 != null) {
            return framedConnection2.maxConcurrentStreams();
        }
        return 1;
    }

    public boolean isHealthy(boolean doExtensiveChecks) {
        int readTimeout;
        if (this.socket.isClosed() || this.socket.isInputShutdown() || this.socket.isOutputShutdown()) {
            return false;
        }
        if (this.framedConnection != null || !doExtensiveChecks) {
            return true;
        }
        try {
            readTimeout = this.socket.getSoTimeout();
            this.socket.setSoTimeout(1);
            if (this.source.exhausted()) {
                this.socket.setSoTimeout(readTimeout);
                return false;
            }
            this.socket.setSoTimeout(readTimeout);
            return true;
        } catch (SocketTimeoutException e) {
            return true;
        } catch (IOException e2) {
            return false;
        } catch (Throwable th) {
            this.socket.setSoTimeout(readTimeout);
            throw th;
        }
    }

    public Handshake getHandshake() {
        return this.handshake;
    }

    public boolean isMultiplexed() {
        return this.framedConnection != null;
    }

    public Protocol getProtocol() {
        return this.protocol != null ? this.protocol : Protocol.HTTP_1_1;
    }

    public String toString() {
        return "Connection{" + this.route.getAddress().url().host() + ":" + this.route.getAddress().url().port() + ", proxy=" + this.route.getProxy() + " hostAddress=" + this.route.getSocketAddress() + " cipherSuite=" + (this.handshake != null ? this.handshake.cipherSuite() : "none") + " protocol=" + this.protocol + '}';
    }
}
