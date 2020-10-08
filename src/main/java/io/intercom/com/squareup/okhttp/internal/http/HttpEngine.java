package io.intercom.com.squareup.okhttp.internal.http;

import io.fabric.sdk.android.services.network.HttpRequest;
import io.intercom.com.squareup.okhttp.Address;
import io.intercom.com.squareup.okhttp.CertificatePinner;
import io.intercom.com.squareup.okhttp.Connection;
import io.intercom.com.squareup.okhttp.Headers;
import io.intercom.com.squareup.okhttp.HttpUrl;
import io.intercom.com.squareup.okhttp.Interceptor;
import io.intercom.com.squareup.okhttp.MediaType;
import io.intercom.com.squareup.okhttp.OkHttpClient;
import io.intercom.com.squareup.okhttp.Protocol;
import io.intercom.com.squareup.okhttp.Request;
import io.intercom.com.squareup.okhttp.Response;
import io.intercom.com.squareup.okhttp.ResponseBody;
import io.intercom.com.squareup.okhttp.internal.Internal;
import io.intercom.com.squareup.okhttp.internal.InternalCache;
import io.intercom.com.squareup.okhttp.internal.Util;
import io.intercom.com.squareup.okhttp.internal.Version;
import io.intercom.com.squareup.okhttp.internal.http.CacheStrategy;
import io.intercom.okio.Buffer;
import io.intercom.okio.BufferedSink;
import io.intercom.okio.BufferedSource;
import io.intercom.okio.GzipSource;
import io.intercom.okio.Okio;
import io.intercom.okio.Sink;
import io.intercom.okio.Source;
import io.intercom.okio.Timeout;
import java.io.Closeable;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.ProtocolException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

public final class HttpEngine {
    private static final ResponseBody EMPTY_BODY = new ResponseBody() {
        public MediaType contentType() {
            return null;
        }

        public long contentLength() {
            return 0;
        }

        public BufferedSource source() {
            return new Buffer();
        }
    };
    public static final int MAX_FOLLOW_UPS = 20;
    public final boolean bufferRequestBody;
    private BufferedSink bufferedRequestBody;
    private Response cacheResponse;
    private CacheStrategy cacheStrategy;
    private final boolean callerWritesRequestBody;
    final OkHttpClient client;
    private final boolean forWebSocket;
    /* access modifiers changed from: private */
    public HttpStream httpStream;
    /* access modifiers changed from: private */
    public Request networkRequest;
    private final Response priorResponse;
    private Sink requestBodyOut;
    long sentRequestMillis = -1;
    private CacheRequest storeRequest;
    public final StreamAllocation streamAllocation;
    private boolean transparentGzip;
    private final Request userRequest;
    private Response userResponse;

    public HttpEngine(OkHttpClient client2, Request request, boolean bufferRequestBody2, boolean callerWritesRequestBody2, boolean forWebSocket2, StreamAllocation streamAllocation2, RetryableSink requestBodyOut2, Response priorResponse2) {
        this.client = client2;
        this.userRequest = request;
        this.bufferRequestBody = bufferRequestBody2;
        this.callerWritesRequestBody = callerWritesRequestBody2;
        this.forWebSocket = forWebSocket2;
        this.streamAllocation = streamAllocation2 == null ? new StreamAllocation(client2.getConnectionPool(), createAddress(client2, request)) : streamAllocation2;
        this.requestBodyOut = requestBodyOut2;
        this.priorResponse = priorResponse2;
    }

    public void sendRequest() throws RequestException, RouteException, IOException {
        if (this.cacheStrategy == null) {
            if (this.httpStream != null) {
                throw new IllegalStateException();
            }
            Request request = networkRequest(this.userRequest);
            InternalCache responseCache = Internal.instance.internalCache(this.client);
            Response cacheCandidate = responseCache != null ? responseCache.get(request) : null;
            this.cacheStrategy = new CacheStrategy.Factory(System.currentTimeMillis(), request, cacheCandidate).get();
            this.networkRequest = this.cacheStrategy.networkRequest;
            this.cacheResponse = this.cacheStrategy.cacheResponse;
            if (responseCache != null) {
                responseCache.trackResponse(this.cacheStrategy);
            }
            if (cacheCandidate != null && this.cacheResponse == null) {
                Util.closeQuietly((Closeable) cacheCandidate.body());
            }
            if (this.networkRequest != null) {
                this.httpStream = connect();
                this.httpStream.setHttpEngine(this);
                if (this.callerWritesRequestBody && permitsRequestBody(this.networkRequest) && this.requestBodyOut == null) {
                    long contentLength = OkHeaders.contentLength(request);
                    if (!this.bufferRequestBody) {
                        this.httpStream.writeRequestHeaders(this.networkRequest);
                        this.requestBodyOut = this.httpStream.createRequestBody(this.networkRequest, contentLength);
                    } else if (contentLength > 2147483647L) {
                        throw new IllegalStateException("Use setFixedLengthStreamingMode() or setChunkedStreamingMode() for requests larger than 2 GiB.");
                    } else if (contentLength != -1) {
                        this.httpStream.writeRequestHeaders(this.networkRequest);
                        this.requestBodyOut = new RetryableSink((int) contentLength);
                    } else {
                        this.requestBodyOut = new RetryableSink();
                    }
                }
            } else {
                if (this.cacheResponse != null) {
                    this.userResponse = this.cacheResponse.newBuilder().request(this.userRequest).priorResponse(stripBody(this.priorResponse)).cacheResponse(stripBody(this.cacheResponse)).build();
                } else {
                    this.userResponse = new Response.Builder().request(this.userRequest).priorResponse(stripBody(this.priorResponse)).protocol(Protocol.HTTP_1_1).code(504).message("Unsatisfiable Request (only-if-cached)").body(EMPTY_BODY).build();
                }
                this.userResponse = unzip(this.userResponse);
            }
        }
    }

    private HttpStream connect() throws RouteException, RequestException, IOException {
        return this.streamAllocation.newStream(this.client.getConnectTimeout(), this.client.getReadTimeout(), this.client.getWriteTimeout(), this.client.getRetryOnConnectionFailure(), !this.networkRequest.method().equals(HttpRequest.METHOD_GET));
    }

    private static Response stripBody(Response response) {
        return (response == null || response.body() == null) ? response : response.newBuilder().body((ResponseBody) null).build();
    }

    public void writingRequestHeaders() {
        if (this.sentRequestMillis != -1) {
            throw new IllegalStateException();
        }
        this.sentRequestMillis = System.currentTimeMillis();
    }

    /* access modifiers changed from: package-private */
    public boolean permitsRequestBody(Request request) {
        return HttpMethod.permitsRequestBody(request.method());
    }

    public Sink getRequestBody() {
        if (this.cacheStrategy != null) {
            return this.requestBodyOut;
        }
        throw new IllegalStateException();
    }

    public BufferedSink getBufferedRequestBody() {
        BufferedSink result;
        BufferedSink result2 = this.bufferedRequestBody;
        if (result2 != null) {
            return result2;
        }
        Sink requestBody = getRequestBody();
        if (requestBody != null) {
            result = Okio.buffer(requestBody);
            this.bufferedRequestBody = result;
        } else {
            result = null;
        }
        return result;
    }

    public boolean hasResponse() {
        return this.userResponse != null;
    }

    public Request getRequest() {
        return this.userRequest;
    }

    public Response getResponse() {
        if (this.userResponse != null) {
            return this.userResponse;
        }
        throw new IllegalStateException();
    }

    public Connection getConnection() {
        return this.streamAllocation.connection();
    }

    public HttpEngine recover(RouteException e) {
        if (!this.streamAllocation.recover(e) || !this.client.getRetryOnConnectionFailure()) {
            return null;
        }
        return new HttpEngine(this.client, this.userRequest, this.bufferRequestBody, this.callerWritesRequestBody, this.forWebSocket, close(), (RetryableSink) this.requestBodyOut, this.priorResponse);
    }

    public HttpEngine recover(IOException e, Sink requestBodyOut2) {
        if (!this.streamAllocation.recover(e, requestBodyOut2) || !this.client.getRetryOnConnectionFailure()) {
            return null;
        }
        return new HttpEngine(this.client, this.userRequest, this.bufferRequestBody, this.callerWritesRequestBody, this.forWebSocket, close(), (RetryableSink) requestBodyOut2, this.priorResponse);
    }

    public HttpEngine recover(IOException e) {
        return recover(e, this.requestBodyOut);
    }

    private void maybeCache() throws IOException {
        InternalCache responseCache = Internal.instance.internalCache(this.client);
        if (responseCache != null) {
            if (CacheStrategy.isCacheable(this.userResponse, this.networkRequest)) {
                this.storeRequest = responseCache.put(stripBody(this.userResponse));
            } else if (HttpMethod.invalidatesCache(this.networkRequest.method())) {
                try {
                    responseCache.remove(this.networkRequest);
                } catch (IOException e) {
                }
            }
        }
    }

    public void releaseStreamAllocation() throws IOException {
        this.streamAllocation.release();
    }

    public void cancel() {
        this.streamAllocation.cancel();
    }

    public StreamAllocation close() {
        if (this.bufferedRequestBody != null) {
            Util.closeQuietly((Closeable) this.bufferedRequestBody);
        } else if (this.requestBodyOut != null) {
            Util.closeQuietly((Closeable) this.requestBodyOut);
        }
        if (this.userResponse != null) {
            Util.closeQuietly((Closeable) this.userResponse.body());
        } else {
            this.streamAllocation.connectionFailed();
        }
        return this.streamAllocation;
    }

    private Response unzip(Response response) throws IOException {
        if (!this.transparentGzip || !HttpRequest.ENCODING_GZIP.equalsIgnoreCase(this.userResponse.header(HttpRequest.HEADER_CONTENT_ENCODING)) || response.body() == null) {
            return response;
        }
        GzipSource responseBody = new GzipSource(response.body().source());
        Headers strippedHeaders = response.headers().newBuilder().removeAll(HttpRequest.HEADER_CONTENT_ENCODING).removeAll(HttpRequest.HEADER_CONTENT_LENGTH).build();
        return response.newBuilder().headers(strippedHeaders).body(new RealResponseBody(strippedHeaders, Okio.buffer((Source) responseBody))).build();
    }

    public static boolean hasBody(Response response) {
        if (response.request().method().equals(HttpRequest.METHOD_HEAD)) {
            return false;
        }
        int responseCode = response.code();
        if ((responseCode < 100 || responseCode >= 200) && responseCode != 204 && responseCode != 304) {
            return true;
        }
        if (OkHeaders.contentLength(response) != -1 || "chunked".equalsIgnoreCase(response.header("Transfer-Encoding"))) {
            return true;
        }
        return false;
    }

    private Request networkRequest(Request request) throws IOException {
        Request.Builder result = request.newBuilder();
        if (request.header("Host") == null) {
            result.header("Host", Util.hostHeader(request.httpUrl()));
        }
        if (request.header("Connection") == null) {
            result.header("Connection", "Keep-Alive");
        }
        if (request.header(HttpRequest.HEADER_ACCEPT_ENCODING) == null) {
            this.transparentGzip = true;
            result.header(HttpRequest.HEADER_ACCEPT_ENCODING, HttpRequest.ENCODING_GZIP);
        }
        CookieHandler cookieHandler = this.client.getCookieHandler();
        if (cookieHandler != null) {
            OkHeaders.addCookies(result, cookieHandler.get(request.uri(), OkHeaders.toMultimap(result.build().headers(), (String) null)));
        }
        if (request.header("User-Agent") == null) {
            result.header("User-Agent", Version.userAgent());
        }
        return result.build();
    }

    public void readResponse() throws IOException {
        Response networkResponse;
        if (this.userResponse == null) {
            if (this.networkRequest == null && this.cacheResponse == null) {
                throw new IllegalStateException("call sendRequest() first!");
            } else if (this.networkRequest != null) {
                if (this.forWebSocket) {
                    this.httpStream.writeRequestHeaders(this.networkRequest);
                    networkResponse = readNetworkResponse();
                } else if (!this.callerWritesRequestBody) {
                    networkResponse = new NetworkInterceptorChain(0, this.networkRequest).proceed(this.networkRequest);
                } else {
                    if (this.bufferedRequestBody != null && this.bufferedRequestBody.buffer().size() > 0) {
                        this.bufferedRequestBody.emit();
                    }
                    if (this.sentRequestMillis == -1) {
                        if (OkHeaders.contentLength(this.networkRequest) == -1 && (this.requestBodyOut instanceof RetryableSink)) {
                            this.networkRequest = this.networkRequest.newBuilder().header(HttpRequest.HEADER_CONTENT_LENGTH, Long.toString(((RetryableSink) this.requestBodyOut).contentLength())).build();
                        }
                        this.httpStream.writeRequestHeaders(this.networkRequest);
                    }
                    if (this.requestBodyOut != null) {
                        if (this.bufferedRequestBody != null) {
                            this.bufferedRequestBody.close();
                        } else {
                            this.requestBodyOut.close();
                        }
                        if (this.requestBodyOut instanceof RetryableSink) {
                            this.httpStream.writeRequestBody((RetryableSink) this.requestBodyOut);
                        }
                    }
                    networkResponse = readNetworkResponse();
                }
                receiveHeaders(networkResponse.headers());
                if (this.cacheResponse != null) {
                    if (validate(this.cacheResponse, networkResponse)) {
                        this.userResponse = this.cacheResponse.newBuilder().request(this.userRequest).priorResponse(stripBody(this.priorResponse)).headers(combine(this.cacheResponse.headers(), networkResponse.headers())).cacheResponse(stripBody(this.cacheResponse)).networkResponse(stripBody(networkResponse)).build();
                        networkResponse.body().close();
                        releaseStreamAllocation();
                        InternalCache responseCache = Internal.instance.internalCache(this.client);
                        responseCache.trackConditionalCacheHit();
                        responseCache.update(this.cacheResponse, stripBody(this.userResponse));
                        this.userResponse = unzip(this.userResponse);
                        return;
                    }
                    Util.closeQuietly((Closeable) this.cacheResponse.body());
                }
                this.userResponse = networkResponse.newBuilder().request(this.userRequest).priorResponse(stripBody(this.priorResponse)).cacheResponse(stripBody(this.cacheResponse)).networkResponse(stripBody(networkResponse)).build();
                if (hasBody(this.userResponse)) {
                    maybeCache();
                    this.userResponse = unzip(cacheWritingResponse(this.storeRequest, this.userResponse));
                }
            }
        }
    }

    class NetworkInterceptorChain implements Interceptor.Chain {
        private int calls;
        private final int index;
        private final Request request;

        NetworkInterceptorChain(int index2, Request request2) {
            this.index = index2;
            this.request = request2;
        }

        public Connection connection() {
            return HttpEngine.this.streamAllocation.connection();
        }

        public Request request() {
            return this.request;
        }

        public Response proceed(Request request2) throws IOException {
            this.calls++;
            if (this.index > 0) {
                Interceptor caller = HttpEngine.this.client.networkInterceptors().get(this.index - 1);
                Address address = connection().getRoute().getAddress();
                if (!request2.httpUrl().host().equals(address.getUriHost()) || request2.httpUrl().port() != address.getUriPort()) {
                    throw new IllegalStateException("network interceptor " + caller + " must retain the same host and port");
                } else if (this.calls > 1) {
                    throw new IllegalStateException("network interceptor " + caller + " must call proceed() exactly once");
                }
            }
            if (this.index < HttpEngine.this.client.networkInterceptors().size()) {
                NetworkInterceptorChain chain = new NetworkInterceptorChain(this.index + 1, request2);
                Interceptor interceptor = HttpEngine.this.client.networkInterceptors().get(this.index);
                Response interceptedResponse = interceptor.intercept(chain);
                if (chain.calls != 1) {
                    throw new IllegalStateException("network interceptor " + interceptor + " must call proceed() exactly once");
                } else if (interceptedResponse != null) {
                    return interceptedResponse;
                } else {
                    throw new NullPointerException("network interceptor " + interceptor + " returned null");
                }
            } else {
                HttpEngine.this.httpStream.writeRequestHeaders(request2);
                Request unused = HttpEngine.this.networkRequest = request2;
                if (HttpEngine.this.permitsRequestBody(request2) && request2.body() != null) {
                    BufferedSink bufferedRequestBody = Okio.buffer(HttpEngine.this.httpStream.createRequestBody(request2, request2.body().contentLength()));
                    request2.body().writeTo(bufferedRequestBody);
                    bufferedRequestBody.close();
                }
                Response response = HttpEngine.this.readNetworkResponse();
                int code = response.code();
                if ((code != 204 && code != 205) || response.body().contentLength() <= 0) {
                    return response;
                }
                throw new ProtocolException("HTTP " + code + " had non-zero Content-Length: " + response.body().contentLength());
            }
        }
    }

    /* access modifiers changed from: private */
    public Response readNetworkResponse() throws IOException {
        this.httpStream.finishRequest();
        Response networkResponse = this.httpStream.readResponseHeaders().request(this.networkRequest).handshake(this.streamAllocation.connection().getHandshake()).header(OkHeaders.SENT_MILLIS, Long.toString(this.sentRequestMillis)).header(OkHeaders.RECEIVED_MILLIS, Long.toString(System.currentTimeMillis())).build();
        if (!this.forWebSocket) {
            networkResponse = networkResponse.newBuilder().body(this.httpStream.openResponseBody(networkResponse)).build();
        }
        if ("close".equalsIgnoreCase(networkResponse.request().header("Connection")) || "close".equalsIgnoreCase(networkResponse.header("Connection"))) {
            this.streamAllocation.noNewStreams();
        }
        return networkResponse;
    }

    private Response cacheWritingResponse(final CacheRequest cacheRequest, Response response) throws IOException {
        Sink cacheBodyUnbuffered;
        if (cacheRequest == null || (cacheBodyUnbuffered = cacheRequest.body()) == null) {
            return response;
        }
        final BufferedSource source = response.body().source();
        final BufferedSink cacheBody = Okio.buffer(cacheBodyUnbuffered);
        return response.newBuilder().body(new RealResponseBody(response.headers(), Okio.buffer(new Source() {
            boolean cacheRequestClosed;

            public long read(Buffer sink, long byteCount) throws IOException {
                try {
                    long bytesRead = source.read(sink, byteCount);
                    if (bytesRead == -1) {
                        if (!this.cacheRequestClosed) {
                            this.cacheRequestClosed = true;
                            cacheBody.close();
                        }
                        return -1;
                    }
                    sink.copyTo(cacheBody.buffer(), sink.size() - bytesRead, bytesRead);
                    cacheBody.emitCompleteSegments();
                    return bytesRead;
                } catch (IOException e) {
                    if (!this.cacheRequestClosed) {
                        this.cacheRequestClosed = true;
                        cacheRequest.abort();
                    }
                    throw e;
                }
            }

            public Timeout timeout() {
                return source.timeout();
            }

            public void close() throws IOException {
                if (!this.cacheRequestClosed && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
                    this.cacheRequestClosed = true;
                    cacheRequest.abort();
                }
                source.close();
            }
        }))).build();
    }

    private static boolean validate(Response cached, Response network) {
        Date networkLastModified;
        if (network.code() == 304) {
            return true;
        }
        Date lastModified = cached.headers().getDate(HttpRequest.HEADER_LAST_MODIFIED);
        if (lastModified == null || (networkLastModified = network.headers().getDate(HttpRequest.HEADER_LAST_MODIFIED)) == null || networkLastModified.getTime() >= lastModified.getTime()) {
            return false;
        }
        return true;
    }

    private static Headers combine(Headers cachedHeaders, Headers networkHeaders) throws IOException {
        Headers.Builder result = new Headers.Builder();
        int size = cachedHeaders.size();
        for (int i = 0; i < size; i++) {
            String fieldName = cachedHeaders.name(i);
            String value = cachedHeaders.value(i);
            if ((!"Warning".equalsIgnoreCase(fieldName) || !value.startsWith("1")) && (!OkHeaders.isEndToEnd(fieldName) || networkHeaders.get(fieldName) == null)) {
                result.add(fieldName, value);
            }
        }
        int size2 = networkHeaders.size();
        for (int i2 = 0; i2 < size2; i2++) {
            String fieldName2 = networkHeaders.name(i2);
            if (!HttpRequest.HEADER_CONTENT_LENGTH.equalsIgnoreCase(fieldName2) && OkHeaders.isEndToEnd(fieldName2)) {
                result.add(fieldName2, networkHeaders.value(i2));
            }
        }
        return result.build();
    }

    public void receiveHeaders(Headers headers) throws IOException {
        CookieHandler cookieHandler = this.client.getCookieHandler();
        if (cookieHandler != null) {
            cookieHandler.put(this.userRequest.uri(), OkHeaders.toMultimap(headers, (String) null));
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0069, code lost:
        if (r12.client.getFollowRedirects() == false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x006b, code lost:
        r1 = r12.userResponse.header(io.fabric.sdk.android.services.network.HttpRequest.HEADER_LOCATION);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0073, code lost:
        if (r1 == null) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0075, code lost:
        r8 = r12.userRequest.httpUrl().resolve(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x007f, code lost:
        if (r8 == null) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0093, code lost:
        if (r8.scheme().equals(r12.userRequest.httpUrl().scheme()) != false) goto L_0x009d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x009b, code lost:
        if (r12.client.getFollowSslRedirects() == false) goto L_?;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x009d, code lost:
        r3 = r12.userRequest.newBuilder();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00a7, code lost:
        if (io.intercom.com.squareup.okhttp.internal.http.HttpMethod.permitsRequestBody(r2) == false) goto L_0x00c3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:35:0x00ad, code lost:
        if (io.intercom.com.squareup.okhttp.internal.http.HttpMethod.redirectsToGet(r2) == false) goto L_0x00d8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x00af, code lost:
        r3.method(io.fabric.sdk.android.services.network.HttpRequest.METHOD_GET, (io.intercom.com.squareup.okhttp.RequestBody) null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00b4, code lost:
        r3.removeHeader("Transfer-Encoding");
        r3.removeHeader(io.fabric.sdk.android.services.network.HttpRequest.HEADER_CONTENT_LENGTH);
        r3.removeHeader(io.fabric.sdk.android.services.network.HttpRequest.HEADER_CONTENT_TYPE);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00c7, code lost:
        if (sameConnection(r8) != false) goto L_0x00ce;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00c9, code lost:
        r3.removeHeader(io.fabric.sdk.android.services.network.HttpRequest.HEADER_AUTHORIZATION);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x00d8, code lost:
        r3.method(r2, (io.intercom.com.squareup.okhttp.RequestBody) null);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:?, code lost:
        return io.intercom.com.squareup.okhttp.internal.http.OkHeaders.processAuthHeader(r12.client.getAuthenticator(), r12.userResponse, r7);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:?, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:?, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:?, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:?, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:?, code lost:
        return r3.url(r8).build();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public io.intercom.com.squareup.okhttp.Request followUpRequest() throws java.io.IOException {
        /*
            r12 = this;
            r9 = 0
            io.intercom.com.squareup.okhttp.Response r10 = r12.userResponse
            if (r10 != 0) goto L_0x000b
            java.lang.IllegalStateException r9 = new java.lang.IllegalStateException
            r9.<init>()
            throw r9
        L_0x000b:
            io.intercom.com.squareup.okhttp.internal.http.StreamAllocation r10 = r12.streamAllocation
            io.intercom.com.squareup.okhttp.internal.io.RealConnection r0 = r10.connection()
            if (r0 == 0) goto L_0x002d
            io.intercom.com.squareup.okhttp.Route r5 = r0.getRoute()
        L_0x0017:
            if (r5 == 0) goto L_0x002f
            java.net.Proxy r7 = r5.getProxy()
        L_0x001d:
            io.intercom.com.squareup.okhttp.Response r10 = r12.userResponse
            int r4 = r10.code()
            io.intercom.com.squareup.okhttp.Request r10 = r12.userRequest
            java.lang.String r2 = r10.method()
            switch(r4) {
                case 300: goto L_0x0063;
                case 301: goto L_0x0063;
                case 302: goto L_0x0063;
                case 303: goto L_0x0063;
                case 307: goto L_0x0053;
                case 308: goto L_0x0053;
                case 401: goto L_0x0046;
                case 407: goto L_0x0036;
                default: goto L_0x002c;
            }
        L_0x002c:
            return r9
        L_0x002d:
            r5 = r9
            goto L_0x0017
        L_0x002f:
            io.intercom.com.squareup.okhttp.OkHttpClient r10 = r12.client
            java.net.Proxy r7 = r10.getProxy()
            goto L_0x001d
        L_0x0036:
            java.net.Proxy$Type r9 = r7.type()
            java.net.Proxy$Type r10 = java.net.Proxy.Type.HTTP
            if (r9 == r10) goto L_0x0046
            java.net.ProtocolException r9 = new java.net.ProtocolException
            java.lang.String r10 = "Received HTTP_PROXY_AUTH (407) code while not using proxy"
            r9.<init>(r10)
            throw r9
        L_0x0046:
            io.intercom.com.squareup.okhttp.OkHttpClient r9 = r12.client
            io.intercom.com.squareup.okhttp.Authenticator r9 = r9.getAuthenticator()
            io.intercom.com.squareup.okhttp.Response r10 = r12.userResponse
            io.intercom.com.squareup.okhttp.Request r9 = io.intercom.com.squareup.okhttp.internal.http.OkHeaders.processAuthHeader(r9, r10, r7)
            goto L_0x002c
        L_0x0053:
            java.lang.String r10 = "GET"
            boolean r10 = r2.equals(r10)
            if (r10 != 0) goto L_0x0063
            java.lang.String r10 = "HEAD"
            boolean r10 = r2.equals(r10)
            if (r10 == 0) goto L_0x002c
        L_0x0063:
            io.intercom.com.squareup.okhttp.OkHttpClient r10 = r12.client
            boolean r10 = r10.getFollowRedirects()
            if (r10 == 0) goto L_0x002c
            io.intercom.com.squareup.okhttp.Response r10 = r12.userResponse
            java.lang.String r11 = "Location"
            java.lang.String r1 = r10.header(r11)
            if (r1 == 0) goto L_0x002c
            io.intercom.com.squareup.okhttp.Request r10 = r12.userRequest
            io.intercom.com.squareup.okhttp.HttpUrl r10 = r10.httpUrl()
            io.intercom.com.squareup.okhttp.HttpUrl r8 = r10.resolve(r1)
            if (r8 == 0) goto L_0x002c
            java.lang.String r10 = r8.scheme()
            io.intercom.com.squareup.okhttp.Request r11 = r12.userRequest
            io.intercom.com.squareup.okhttp.HttpUrl r11 = r11.httpUrl()
            java.lang.String r11 = r11.scheme()
            boolean r6 = r10.equals(r11)
            if (r6 != 0) goto L_0x009d
            io.intercom.com.squareup.okhttp.OkHttpClient r10 = r12.client
            boolean r10 = r10.getFollowSslRedirects()
            if (r10 == 0) goto L_0x002c
        L_0x009d:
            io.intercom.com.squareup.okhttp.Request r10 = r12.userRequest
            io.intercom.com.squareup.okhttp.Request$Builder r3 = r10.newBuilder()
            boolean r10 = io.intercom.com.squareup.okhttp.internal.http.HttpMethod.permitsRequestBody(r2)
            if (r10 == 0) goto L_0x00c3
            boolean r10 = io.intercom.com.squareup.okhttp.internal.http.HttpMethod.redirectsToGet(r2)
            if (r10 == 0) goto L_0x00d8
            java.lang.String r10 = "GET"
            r3.method(r10, r9)
        L_0x00b4:
            java.lang.String r9 = "Transfer-Encoding"
            r3.removeHeader(r9)
            java.lang.String r9 = "Content-Length"
            r3.removeHeader(r9)
            java.lang.String r9 = "Content-Type"
            r3.removeHeader(r9)
        L_0x00c3:
            boolean r9 = r12.sameConnection(r8)
            if (r9 != 0) goto L_0x00ce
            java.lang.String r9 = "Authorization"
            r3.removeHeader(r9)
        L_0x00ce:
            io.intercom.com.squareup.okhttp.Request$Builder r9 = r3.url((io.intercom.com.squareup.okhttp.HttpUrl) r8)
            io.intercom.com.squareup.okhttp.Request r9 = r9.build()
            goto L_0x002c
        L_0x00d8:
            r3.method(r2, r9)
            goto L_0x00b4
        */
        throw new UnsupportedOperationException("Method not decompiled: io.intercom.com.squareup.okhttp.internal.http.HttpEngine.followUpRequest():io.intercom.com.squareup.okhttp.Request");
    }

    public boolean sameConnection(HttpUrl followUp) {
        HttpUrl url = this.userRequest.httpUrl();
        return url.host().equals(followUp.host()) && url.port() == followUp.port() && url.scheme().equals(followUp.scheme());
    }

    private static Address createAddress(OkHttpClient client2, Request request) {
        SSLSocketFactory sslSocketFactory = null;
        HostnameVerifier hostnameVerifier = null;
        CertificatePinner certificatePinner = null;
        if (request.isHttps()) {
            sslSocketFactory = client2.getSslSocketFactory();
            hostnameVerifier = client2.getHostnameVerifier();
            certificatePinner = client2.getCertificatePinner();
        }
        return new Address(request.httpUrl().host(), request.httpUrl().port(), client2.getDns(), client2.getSocketFactory(), sslSocketFactory, hostnameVerifier, certificatePinner, client2.getAuthenticator(), client2.getProxy(), client2.getProtocols(), client2.getConnectionSpecs(), client2.getProxySelector());
    }
}
