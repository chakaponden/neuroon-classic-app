package io.intercom.com.squareup.okhttp.internal.io;

import android.support.v7.widget.ActivityChooserView;
import io.intercom.com.squareup.okhttp.Connection;
import io.intercom.com.squareup.okhttp.Handshake;
import io.intercom.com.squareup.okhttp.HttpUrl;
import io.intercom.com.squareup.okhttp.Protocol;
import io.intercom.com.squareup.okhttp.Request;
import io.intercom.com.squareup.okhttp.Response;
import io.intercom.com.squareup.okhttp.Route;
import io.intercom.com.squareup.okhttp.internal.ConnectionSpecSelector;
import io.intercom.com.squareup.okhttp.internal.Platform;
import io.intercom.com.squareup.okhttp.internal.Util;
import io.intercom.com.squareup.okhttp.internal.Version;
import io.intercom.com.squareup.okhttp.internal.framed.FramedConnection;
import io.intercom.com.squareup.okhttp.internal.http.Http1xStream;
import io.intercom.com.squareup.okhttp.internal.http.OkHeaders;
import io.intercom.com.squareup.okhttp.internal.http.StreamAllocation;
import io.intercom.okio.BufferedSink;
import io.intercom.okio.BufferedSource;
import io.intercom.okio.Okio;
import io.intercom.okio.Source;
import java.io.IOException;
import java.lang.ref.Reference;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class RealConnection implements Connection {
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
    public void connect(int r10, int r11, int r12, java.util.List<io.intercom.com.squareup.okhttp.ConnectionSpec> r13, boolean r14) throws io.intercom.com.squareup.okhttp.internal.http.RouteException {
        /*
            r9 = this;
            r7 = 0
            io.intercom.com.squareup.okhttp.Protocol r5 = r9.protocol
            if (r5 == 0) goto L_0x000d
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
            java.lang.String r6 = "already connected"
            r5.<init>(r6)
            throw r5
        L_0x000d:
            r4 = 0
            io.intercom.com.squareup.okhttp.internal.ConnectionSpecSelector r1 = new io.intercom.com.squareup.okhttp.internal.ConnectionSpecSelector
            r1.<init>(r13)
            io.intercom.com.squareup.okhttp.Route r5 = r9.route
            java.net.Proxy r3 = r5.getProxy()
            io.intercom.com.squareup.okhttp.Route r5 = r9.route
            io.intercom.com.squareup.okhttp.Address r0 = r5.getAddress()
            io.intercom.com.squareup.okhttp.Route r5 = r9.route
            io.intercom.com.squareup.okhttp.Address r5 = r5.getAddress()
            javax.net.ssl.SSLSocketFactory r5 = r5.getSslSocketFactory()
            if (r5 != 0) goto L_0x005b
            io.intercom.com.squareup.okhttp.ConnectionSpec r5 = io.intercom.com.squareup.okhttp.ConnectionSpec.CLEARTEXT
            boolean r5 = r13.contains(r5)
            if (r5 != 0) goto L_0x005b
            io.intercom.com.squareup.okhttp.internal.http.RouteException r5 = new io.intercom.com.squareup.okhttp.internal.http.RouteException
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
            io.intercom.com.squareup.okhttp.Protocol r5 = r9.protocol
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
            io.intercom.com.squareup.okhttp.internal.Util.closeQuietly((java.net.Socket) r5)
            java.net.Socket r5 = r9.rawSocket
            io.intercom.com.squareup.okhttp.internal.Util.closeQuietly((java.net.Socket) r5)
            r9.socket = r7
            r9.rawSocket = r7
            r9.source = r7
            r9.sink = r7
            r9.handshake = r7
            r9.protocol = r7
            if (r4 != 0) goto L_0x009f
            io.intercom.com.squareup.okhttp.internal.http.RouteException r4 = new io.intercom.com.squareup.okhttp.internal.http.RouteException
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
        throw new UnsupportedOperationException("Method not decompiled: io.intercom.com.squareup.okhttp.internal.io.RealConnection.connect(int, int, int, java.util.List, boolean):void");
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

    /* JADX WARNING: type inference failed for: r10v7, types: [java.net.Socket] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void connectTls(int r15, int r16, io.intercom.com.squareup.okhttp.internal.ConnectionSpecSelector r17) throws java.io.IOException {
        /*
            r14 = this;
            io.intercom.com.squareup.okhttp.Route r10 = r14.route
            boolean r10 = r10.requiresTunnel()
            if (r10 == 0) goto L_0x000b
            r14.createTunnel(r15, r16)
        L_0x000b:
            io.intercom.com.squareup.okhttp.Route r10 = r14.route
            io.intercom.com.squareup.okhttp.Address r1 = r10.getAddress()
            javax.net.ssl.SSLSocketFactory r7 = r1.getSslSocketFactory()
            r8 = 0
            r6 = 0
            java.net.Socket r10 = r14.rawSocket     // Catch:{ AssertionError -> 0x00be }
            java.lang.String r11 = r1.getUriHost()     // Catch:{ AssertionError -> 0x00be }
            int r12 = r1.getUriPort()     // Catch:{ AssertionError -> 0x00be }
            r13 = 1
            java.net.Socket r10 = r7.createSocket(r10, r11, r12, r13)     // Catch:{ AssertionError -> 0x00be }
            r0 = r10
            javax.net.ssl.SSLSocket r0 = (javax.net.ssl.SSLSocket) r0     // Catch:{ AssertionError -> 0x00be }
            r6 = r0
            r0 = r17
            io.intercom.com.squareup.okhttp.ConnectionSpec r3 = r0.configureSecureSocket(r6)     // Catch:{ AssertionError -> 0x00be }
            boolean r10 = r3.supportsTlsExtensions()     // Catch:{ AssertionError -> 0x00be }
            if (r10 == 0) goto L_0x0045
            io.intercom.com.squareup.okhttp.internal.Platform r10 = io.intercom.com.squareup.okhttp.internal.Platform.get()     // Catch:{ AssertionError -> 0x00be }
            java.lang.String r11 = r1.getUriHost()     // Catch:{ AssertionError -> 0x00be }
            java.util.List r12 = r1.getProtocols()     // Catch:{ AssertionError -> 0x00be }
            r10.configureTlsExtensions(r6, r11, r12)     // Catch:{ AssertionError -> 0x00be }
        L_0x0045:
            r6.startHandshake()     // Catch:{ AssertionError -> 0x00be }
            javax.net.ssl.SSLSession r10 = r6.getSession()     // Catch:{ AssertionError -> 0x00be }
            io.intercom.com.squareup.okhttp.Handshake r9 = io.intercom.com.squareup.okhttp.Handshake.get(r10)     // Catch:{ AssertionError -> 0x00be }
            javax.net.ssl.HostnameVerifier r10 = r1.getHostnameVerifier()     // Catch:{ AssertionError -> 0x00be }
            java.lang.String r11 = r1.getUriHost()     // Catch:{ AssertionError -> 0x00be }
            javax.net.ssl.SSLSession r12 = r6.getSession()     // Catch:{ AssertionError -> 0x00be }
            boolean r10 = r10.verify(r11, r12)     // Catch:{ AssertionError -> 0x00be }
            if (r10 != 0) goto L_0x00db
            java.util.List r10 = r9.peerCertificates()     // Catch:{ AssertionError -> 0x00be }
            r11 = 0
            java.lang.Object r2 = r10.get(r11)     // Catch:{ AssertionError -> 0x00be }
            java.security.cert.X509Certificate r2 = (java.security.cert.X509Certificate) r2     // Catch:{ AssertionError -> 0x00be }
            javax.net.ssl.SSLPeerUnverifiedException r10 = new javax.net.ssl.SSLPeerUnverifiedException     // Catch:{ AssertionError -> 0x00be }
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ AssertionError -> 0x00be }
            r11.<init>()     // Catch:{ AssertionError -> 0x00be }
            java.lang.String r12 = "Hostname "
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ AssertionError -> 0x00be }
            java.lang.String r12 = r1.getUriHost()     // Catch:{ AssertionError -> 0x00be }
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ AssertionError -> 0x00be }
            java.lang.String r12 = " not verified:"
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ AssertionError -> 0x00be }
            java.lang.String r12 = "\n    certificate: "
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ AssertionError -> 0x00be }
            java.lang.String r12 = io.intercom.com.squareup.okhttp.CertificatePinner.pin(r2)     // Catch:{ AssertionError -> 0x00be }
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ AssertionError -> 0x00be }
            java.lang.String r12 = "\n    DN: "
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ AssertionError -> 0x00be }
            java.security.Principal r12 = r2.getSubjectDN()     // Catch:{ AssertionError -> 0x00be }
            java.lang.String r12 = r12.getName()     // Catch:{ AssertionError -> 0x00be }
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ AssertionError -> 0x00be }
            java.lang.String r12 = "\n    subjectAltNames: "
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ AssertionError -> 0x00be }
            java.util.List r12 = io.intercom.com.squareup.okhttp.internal.tls.OkHostnameVerifier.allSubjectAltNames(r2)     // Catch:{ AssertionError -> 0x00be }
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ AssertionError -> 0x00be }
            java.lang.String r11 = r11.toString()     // Catch:{ AssertionError -> 0x00be }
            r10.<init>(r11)     // Catch:{ AssertionError -> 0x00be }
            throw r10     // Catch:{ AssertionError -> 0x00be }
        L_0x00be:
            r4 = move-exception
            boolean r10 = io.intercom.com.squareup.okhttp.internal.Util.isAndroidGetsocknameError(r4)     // Catch:{ all -> 0x00cb }
            if (r10 == 0) goto L_0x0131
            java.io.IOException r10 = new java.io.IOException     // Catch:{ all -> 0x00cb }
            r10.<init>(r4)     // Catch:{ all -> 0x00cb }
            throw r10     // Catch:{ all -> 0x00cb }
        L_0x00cb:
            r10 = move-exception
            if (r6 == 0) goto L_0x00d5
            io.intercom.com.squareup.okhttp.internal.Platform r11 = io.intercom.com.squareup.okhttp.internal.Platform.get()
            r11.afterHandshake(r6)
        L_0x00d5:
            if (r8 != 0) goto L_0x00da
            io.intercom.com.squareup.okhttp.internal.Util.closeQuietly((java.net.Socket) r6)
        L_0x00da:
            throw r10
        L_0x00db:
            io.intercom.com.squareup.okhttp.CertificatePinner r10 = r1.getCertificatePinner()     // Catch:{ AssertionError -> 0x00be }
            java.lang.String r11 = r1.getUriHost()     // Catch:{ AssertionError -> 0x00be }
            java.util.List r12 = r9.peerCertificates()     // Catch:{ AssertionError -> 0x00be }
            r10.check((java.lang.String) r11, (java.util.List<java.security.cert.Certificate>) r12)     // Catch:{ AssertionError -> 0x00be }
            boolean r10 = r3.supportsTlsExtensions()     // Catch:{ AssertionError -> 0x00be }
            if (r10 == 0) goto L_0x012c
            io.intercom.com.squareup.okhttp.internal.Platform r10 = io.intercom.com.squareup.okhttp.internal.Platform.get()     // Catch:{ AssertionError -> 0x00be }
            java.lang.String r5 = r10.getSelectedProtocol(r6)     // Catch:{ AssertionError -> 0x00be }
        L_0x00f8:
            r14.socket = r6     // Catch:{ AssertionError -> 0x00be }
            java.net.Socket r10 = r14.socket     // Catch:{ AssertionError -> 0x00be }
            io.intercom.okio.Source r10 = io.intercom.okio.Okio.source((java.net.Socket) r10)     // Catch:{ AssertionError -> 0x00be }
            io.intercom.okio.BufferedSource r10 = io.intercom.okio.Okio.buffer((io.intercom.okio.Source) r10)     // Catch:{ AssertionError -> 0x00be }
            r14.source = r10     // Catch:{ AssertionError -> 0x00be }
            java.net.Socket r10 = r14.socket     // Catch:{ AssertionError -> 0x00be }
            io.intercom.okio.Sink r10 = io.intercom.okio.Okio.sink((java.net.Socket) r10)     // Catch:{ AssertionError -> 0x00be }
            io.intercom.okio.BufferedSink r10 = io.intercom.okio.Okio.buffer((io.intercom.okio.Sink) r10)     // Catch:{ AssertionError -> 0x00be }
            r14.sink = r10     // Catch:{ AssertionError -> 0x00be }
            r14.handshake = r9     // Catch:{ AssertionError -> 0x00be }
            if (r5 == 0) goto L_0x012e
            io.intercom.com.squareup.okhttp.Protocol r10 = io.intercom.com.squareup.okhttp.Protocol.get(r5)     // Catch:{ AssertionError -> 0x00be }
        L_0x011a:
            r14.protocol = r10     // Catch:{ AssertionError -> 0x00be }
            r8 = 1
            if (r6 == 0) goto L_0x0126
            io.intercom.com.squareup.okhttp.internal.Platform r10 = io.intercom.com.squareup.okhttp.internal.Platform.get()
            r10.afterHandshake(r6)
        L_0x0126:
            if (r8 != 0) goto L_0x012b
            io.intercom.com.squareup.okhttp.internal.Util.closeQuietly((java.net.Socket) r6)
        L_0x012b:
            return
        L_0x012c:
            r5 = 0
            goto L_0x00f8
        L_0x012e:
            io.intercom.com.squareup.okhttp.Protocol r10 = io.intercom.com.squareup.okhttp.Protocol.HTTP_1_1     // Catch:{ AssertionError -> 0x00be }
            goto L_0x011a
        L_0x0131:
            throw r4     // Catch:{ all -> 0x00cb }
        */
        throw new UnsupportedOperationException("Method not decompiled: io.intercom.com.squareup.okhttp.internal.io.RealConnection.connectTls(int, int, io.intercom.com.squareup.okhttp.internal.ConnectionSpecSelector):void");
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
