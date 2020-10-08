package io.fabric.sdk.android.services.network;

import com.inteliclinic.neuroon.mask.bluetooth.SettingsCharacteristic;
import com.raizlabs.android.dbflow.sql.language.Condition;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.GZIPInputStream;

public class HttpRequest {
    private static final String BOUNDARY = "00content0boundary00";
    public static final String CHARSET_UTF8 = "UTF-8";
    private static ConnectionFactory CONNECTION_FACTORY = ConnectionFactory.DEFAULT;
    public static final String CONTENT_TYPE_FORM = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_JSON = "application/json";
    private static final String CONTENT_TYPE_MULTIPART = "multipart/form-data; boundary=00content0boundary00";
    private static final String CRLF = "\r\n";
    private static final String[] EMPTY_STRINGS = new String[0];
    public static final String ENCODING_GZIP = "gzip";
    public static final String HEADER_ACCEPT = "Accept";
    public static final String HEADER_ACCEPT_CHARSET = "Accept-Charset";
    public static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_CACHE_CONTROL = "Cache-Control";
    public static final String HEADER_CONTENT_ENCODING = "Content-Encoding";
    public static final String HEADER_CONTENT_LENGTH = "Content-Length";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_DATE = "Date";
    public static final String HEADER_ETAG = "ETag";
    public static final String HEADER_EXPIRES = "Expires";
    public static final String HEADER_IF_NONE_MATCH = "If-None-Match";
    public static final String HEADER_LAST_MODIFIED = "Last-Modified";
    public static final String HEADER_LOCATION = "Location";
    public static final String HEADER_PROXY_AUTHORIZATION = "Proxy-Authorization";
    public static final String HEADER_REFERER = "Referer";
    public static final String HEADER_SERVER = "Server";
    public static final String HEADER_USER_AGENT = "User-Agent";
    public static final String METHOD_DELETE = "DELETE";
    public static final String METHOD_GET = "GET";
    public static final String METHOD_HEAD = "HEAD";
    public static final String METHOD_OPTIONS = "OPTIONS";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_TRACE = "TRACE";
    public static final String PARAM_CHARSET = "charset";
    /* access modifiers changed from: private */
    public int bufferSize = 8192;
    private HttpURLConnection connection = null;
    private boolean form;
    private String httpProxyHost;
    private int httpProxyPort;
    private boolean ignoreCloseExceptions = true;
    private boolean multipart;
    private RequestOutputStream output;
    private final String requestMethod;
    private boolean uncompress = false;
    public final URL url;

    public interface ConnectionFactory {
        public static final ConnectionFactory DEFAULT = new ConnectionFactory() {
            public HttpURLConnection create(URL url) throws IOException {
                return (HttpURLConnection) url.openConnection();
            }

            public HttpURLConnection create(URL url, Proxy proxy) throws IOException {
                return (HttpURLConnection) url.openConnection(proxy);
            }
        };

        HttpURLConnection create(URL url) throws IOException;

        HttpURLConnection create(URL url, Proxy proxy) throws IOException;
    }

    /* access modifiers changed from: private */
    public static String getValidCharset(String charset) {
        return (charset == null || charset.length() <= 0) ? CHARSET_UTF8 : charset;
    }

    private static StringBuilder addPathSeparator(String baseUrl, StringBuilder result) {
        if (baseUrl.indexOf(58) + 2 == baseUrl.lastIndexOf(47)) {
            result.append('/');
        }
        return result;
    }

    private static StringBuilder addParamPrefix(String baseUrl, StringBuilder result) {
        int queryStart = baseUrl.indexOf(63);
        int lastChar = result.length() - 1;
        if (queryStart == -1) {
            result.append('?');
        } else if (queryStart < lastChar && baseUrl.charAt(lastChar) != '&') {
            result.append('&');
        }
        return result;
    }

    public static void setConnectionFactory(ConnectionFactory connectionFactory) {
        if (connectionFactory == null) {
            CONNECTION_FACTORY = ConnectionFactory.DEFAULT;
        } else {
            CONNECTION_FACTORY = connectionFactory;
        }
    }

    public static class Base64 {
        private static final byte EQUALS_SIGN = 61;
        private static final String PREFERRED_ENCODING = "US-ASCII";
        private static final byte[] _STANDARD_ALPHABET = {SettingsCharacteristic.BLE_CMD_RAW_STOP, SettingsCharacteristic.BLE_CMD_RAW_CLEAR, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, SettingsCharacteristic.BLE_CMD_GOTO_TEST, SettingsCharacteristic.BLE_CMD_GOTO_DFU, 50, SettingsCharacteristic.BLE_CMD_DEVSEL, SettingsCharacteristic.BLE_CMD_UART_TEST, 53, 54, 55, 56, 57, 43, 47};

        private Base64() {
        }

        private static byte[] encode3to4(byte[] source, int srcOffset, int numSigBytes, byte[] destination, int destOffset) {
            int i;
            int i2 = 0;
            byte[] ALPHABET = _STANDARD_ALPHABET;
            if (numSigBytes > 0) {
                i = (source[srcOffset] << 24) >>> 8;
            } else {
                i = 0;
            }
            int i3 = (numSigBytes > 1 ? (source[srcOffset + 1] << 24) >>> 16 : 0) | i;
            if (numSigBytes > 2) {
                i2 = (source[srcOffset + 2] << 24) >>> 24;
            }
            int inBuff = i3 | i2;
            switch (numSigBytes) {
                case 1:
                    destination[destOffset] = ALPHABET[inBuff >>> 18];
                    destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 63];
                    destination[destOffset + 2] = EQUALS_SIGN;
                    destination[destOffset + 3] = EQUALS_SIGN;
                    break;
                case 2:
                    destination[destOffset] = ALPHABET[inBuff >>> 18];
                    destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 63];
                    destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 63];
                    destination[destOffset + 3] = EQUALS_SIGN;
                    break;
                case 3:
                    destination[destOffset] = ALPHABET[inBuff >>> 18];
                    destination[destOffset + 1] = ALPHABET[(inBuff >>> 12) & 63];
                    destination[destOffset + 2] = ALPHABET[(inBuff >>> 6) & 63];
                    destination[destOffset + 3] = ALPHABET[inBuff & 63];
                    break;
            }
            return destination;
        }

        public static String encode(String string) {
            byte[] bytes;
            try {
                bytes = string.getBytes(PREFERRED_ENCODING);
            } catch (UnsupportedEncodingException e) {
                bytes = string.getBytes();
            }
            return encodeBytes(bytes);
        }

        public static String encodeBytes(byte[] source) {
            return encodeBytes(source, 0, source.length);
        }

        public static String encodeBytes(byte[] source, int off, int len) {
            byte[] encoded = encodeBytesToBytes(source, off, len);
            try {
                return new String(encoded, PREFERRED_ENCODING);
            } catch (UnsupportedEncodingException e) {
                return new String(encoded);
            }
        }

        public static byte[] encodeBytesToBytes(byte[] source, int off, int len) {
            int i;
            if (source == null) {
                throw new NullPointerException("Cannot serialize a null array.");
            } else if (off < 0) {
                throw new IllegalArgumentException("Cannot have negative offset: " + off);
            } else if (len < 0) {
                throw new IllegalArgumentException("Cannot have length offset: " + len);
            } else if (off + len > source.length) {
                throw new IllegalArgumentException(String.format(Locale.ENGLISH, "Cannot have offset of %d and length of %d with array of length %d", new Object[]{Integer.valueOf(off), Integer.valueOf(len), Integer.valueOf(source.length)}));
            } else {
                int i2 = (len / 3) * 4;
                if (len % 3 > 0) {
                    i = 4;
                } else {
                    i = 0;
                }
                byte[] outBuff = new byte[(i2 + i)];
                int d = 0;
                int e = 0;
                int len2 = len - 2;
                while (d < len2) {
                    encode3to4(source, d + off, 3, outBuff, e);
                    d += 3;
                    e += 4;
                }
                if (d < len) {
                    encode3to4(source, d + off, len - d, outBuff, e);
                    e += 4;
                }
                if (e > outBuff.length - 1) {
                    return outBuff;
                }
                byte[] finalOut = new byte[e];
                System.arraycopy(outBuff, 0, finalOut, 0, e);
                return finalOut;
            }
        }
    }

    public static class HttpRequestException extends RuntimeException {
        private static final long serialVersionUID = -1170466989781746231L;

        protected HttpRequestException(IOException cause) {
            super(cause);
        }

        public IOException getCause() {
            return (IOException) super.getCause();
        }
    }

    protected static abstract class Operation<V> implements Callable<V> {
        /* access modifiers changed from: protected */
        public abstract void done() throws IOException;

        /* access modifiers changed from: protected */
        public abstract V run() throws HttpRequestException, IOException;

        protected Operation() {
        }

        public V call() throws HttpRequestException {
            boolean thrown;
            try {
                V run = run();
                try {
                    done();
                } catch (IOException e) {
                    if (0 == 0) {
                        throw new HttpRequestException(e);
                    }
                }
                return run;
            } catch (HttpRequestException e2) {
                thrown = true;
                throw e2;
            } catch (IOException e3) {
                thrown = true;
                throw new HttpRequestException(e3);
            } catch (Throwable th) {
                try {
                    done();
                } catch (IOException e4) {
                    if (!thrown) {
                        throw new HttpRequestException(e4);
                    }
                }
                throw th;
            }
        }
    }

    protected static abstract class CloseOperation<V> extends Operation<V> {
        private final Closeable closeable;
        private final boolean ignoreCloseExceptions;

        protected CloseOperation(Closeable closeable2, boolean ignoreCloseExceptions2) {
            this.closeable = closeable2;
            this.ignoreCloseExceptions = ignoreCloseExceptions2;
        }

        /* access modifiers changed from: protected */
        public void done() throws IOException {
            if (this.closeable instanceof Flushable) {
                ((Flushable) this.closeable).flush();
            }
            if (this.ignoreCloseExceptions) {
                try {
                    this.closeable.close();
                } catch (IOException e) {
                }
            } else {
                this.closeable.close();
            }
        }
    }

    protected static abstract class FlushOperation<V> extends Operation<V> {
        private final Flushable flushable;

        protected FlushOperation(Flushable flushable2) {
            this.flushable = flushable2;
        }

        /* access modifiers changed from: protected */
        public void done() throws IOException {
            this.flushable.flush();
        }
    }

    public static class RequestOutputStream extends BufferedOutputStream {
        /* access modifiers changed from: private */
        public final CharsetEncoder encoder;

        public RequestOutputStream(OutputStream stream, String charset, int bufferSize) {
            super(stream, bufferSize);
            this.encoder = Charset.forName(HttpRequest.getValidCharset(charset)).newEncoder();
        }

        public RequestOutputStream write(String value) throws IOException {
            ByteBuffer bytes = this.encoder.encode(CharBuffer.wrap(value));
            super.write(bytes.array(), 0, bytes.limit());
            return this;
        }
    }

    public static String encode(CharSequence url2) throws HttpRequestException {
        try {
            URL parsed = new URL(url2.toString());
            String host = parsed.getHost();
            int port = parsed.getPort();
            if (port != -1) {
                host = host + ':' + Integer.toString(port);
            }
            try {
                String encoded = new URI(parsed.getProtocol(), host, parsed.getPath(), parsed.getQuery(), (String) null).toASCIIString();
                int paramsStart = encoded.indexOf(63);
                if (paramsStart <= 0 || paramsStart + 1 >= encoded.length()) {
                    return encoded;
                }
                return encoded.substring(0, paramsStart + 1) + encoded.substring(paramsStart + 1).replace(Condition.Operation.PLUS, "%2B");
            } catch (URISyntaxException e) {
                IOException io2 = new IOException("Parsing URI failed");
                io2.initCause(e);
                throw new HttpRequestException(io2);
            }
        } catch (IOException e2) {
            throw new HttpRequestException(e2);
        }
    }

    public static String append(CharSequence url2, Map<?, ?> params) {
        String baseUrl = url2.toString();
        if (params == null || params.isEmpty()) {
            return baseUrl;
        }
        StringBuilder result = new StringBuilder(baseUrl);
        addPathSeparator(baseUrl, result);
        addParamPrefix(baseUrl, result);
        Iterator<Map.Entry<?, ?>> it = params.entrySet().iterator();
        Map.Entry<?, ?> entry = it.next();
        result.append(entry.getKey().toString());
        result.append('=');
        Object value = entry.getValue();
        if (value != null) {
            result.append(value);
        }
        while (it.hasNext()) {
            result.append('&');
            Map.Entry<?, ?> entry2 = it.next();
            result.append(entry2.getKey().toString());
            result.append('=');
            Object value2 = entry2.getValue();
            if (value2 != null) {
                result.append(value2);
            }
        }
        return result.toString();
    }

    public static String append(CharSequence url2, Object... params) {
        String baseUrl = url2.toString();
        if (params == null || params.length == 0) {
            return baseUrl;
        }
        if (params.length % 2 != 0) {
            throw new IllegalArgumentException("Must specify an even number of parameter names/values");
        }
        StringBuilder result = new StringBuilder(baseUrl);
        addPathSeparator(baseUrl, result);
        addParamPrefix(baseUrl, result);
        result.append(params[0]);
        result.append('=');
        Object value = params[1];
        if (value != null) {
            result.append(value);
        }
        for (int i = 2; i < params.length; i += 2) {
            result.append('&');
            result.append(params[i]);
            result.append('=');
            Object value2 = params[i + 1];
            if (value2 != null) {
                result.append(value2);
            }
        }
        return result.toString();
    }

    public static HttpRequest get(CharSequence url2) throws HttpRequestException {
        return new HttpRequest(url2, METHOD_GET);
    }

    public static HttpRequest get(URL url2) throws HttpRequestException {
        return new HttpRequest(url2, METHOD_GET);
    }

    public static HttpRequest get(CharSequence baseUrl, Map<?, ?> params, boolean encode) {
        String url2 = append(baseUrl, params);
        if (encode) {
            url2 = encode(url2);
        }
        return get((CharSequence) url2);
    }

    public static HttpRequest get(CharSequence baseUrl, boolean encode, Object... params) {
        String url2 = append(baseUrl, params);
        if (encode) {
            url2 = encode(url2);
        }
        return get((CharSequence) url2);
    }

    public static HttpRequest post(CharSequence url2) throws HttpRequestException {
        return new HttpRequest(url2, METHOD_POST);
    }

    public static HttpRequest post(URL url2) throws HttpRequestException {
        return new HttpRequest(url2, METHOD_POST);
    }

    public static HttpRequest post(CharSequence baseUrl, Map<?, ?> params, boolean encode) {
        String url2 = append(baseUrl, params);
        if (encode) {
            url2 = encode(url2);
        }
        return post((CharSequence) url2);
    }

    public static HttpRequest post(CharSequence baseUrl, boolean encode, Object... params) {
        String url2 = append(baseUrl, params);
        if (encode) {
            url2 = encode(url2);
        }
        return post((CharSequence) url2);
    }

    public static HttpRequest put(CharSequence url2) throws HttpRequestException {
        return new HttpRequest(url2, METHOD_PUT);
    }

    public static HttpRequest put(URL url2) throws HttpRequestException {
        return new HttpRequest(url2, METHOD_PUT);
    }

    public static HttpRequest put(CharSequence baseUrl, Map<?, ?> params, boolean encode) {
        String url2 = append(baseUrl, params);
        if (encode) {
            url2 = encode(url2);
        }
        return put((CharSequence) url2);
    }

    public static HttpRequest put(CharSequence baseUrl, boolean encode, Object... params) {
        String url2 = append(baseUrl, params);
        if (encode) {
            url2 = encode(url2);
        }
        return put((CharSequence) url2);
    }

    public static HttpRequest delete(CharSequence url2) throws HttpRequestException {
        return new HttpRequest(url2, "DELETE");
    }

    public static HttpRequest delete(URL url2) throws HttpRequestException {
        return new HttpRequest(url2, "DELETE");
    }

    public static HttpRequest delete(CharSequence baseUrl, Map<?, ?> params, boolean encode) {
        String url2 = append(baseUrl, params);
        if (encode) {
            url2 = encode(url2);
        }
        return delete((CharSequence) url2);
    }

    public static HttpRequest delete(CharSequence baseUrl, boolean encode, Object... params) {
        String url2 = append(baseUrl, params);
        if (encode) {
            url2 = encode(url2);
        }
        return delete((CharSequence) url2);
    }

    public static HttpRequest head(CharSequence url2) throws HttpRequestException {
        return new HttpRequest(url2, METHOD_HEAD);
    }

    public static HttpRequest head(URL url2) throws HttpRequestException {
        return new HttpRequest(url2, METHOD_HEAD);
    }

    public static HttpRequest head(CharSequence baseUrl, Map<?, ?> params, boolean encode) {
        String url2 = append(baseUrl, params);
        if (encode) {
            url2 = encode(url2);
        }
        return head((CharSequence) url2);
    }

    public static HttpRequest head(CharSequence baseUrl, boolean encode, Object... params) {
        String url2 = append(baseUrl, params);
        if (encode) {
            url2 = encode(url2);
        }
        return head((CharSequence) url2);
    }

    public static HttpRequest options(CharSequence url2) throws HttpRequestException {
        return new HttpRequest(url2, METHOD_OPTIONS);
    }

    public static HttpRequest options(URL url2) throws HttpRequestException {
        return new HttpRequest(url2, METHOD_OPTIONS);
    }

    public static HttpRequest trace(CharSequence url2) throws HttpRequestException {
        return new HttpRequest(url2, METHOD_TRACE);
    }

    public static HttpRequest trace(URL url2) throws HttpRequestException {
        return new HttpRequest(url2, METHOD_TRACE);
    }

    public static void keepAlive(boolean keepAlive) {
        setProperty("http.keepAlive", Boolean.toString(keepAlive));
    }

    public static void proxyHost(String host) {
        setProperty("http.proxyHost", host);
        setProperty("https.proxyHost", host);
    }

    public static void proxyPort(int port) {
        String portValue = Integer.toString(port);
        setProperty("http.proxyPort", portValue);
        setProperty("https.proxyPort", portValue);
    }

    public static void nonProxyHosts(String... hosts) {
        if (hosts == null || hosts.length <= 0) {
            setProperty("http.nonProxyHosts", (String) null);
            return;
        }
        StringBuilder separated = new StringBuilder();
        int last = hosts.length - 1;
        for (int i = 0; i < last; i++) {
            separated.append(hosts[i]).append('|');
        }
        separated.append(hosts[last]);
        setProperty("http.nonProxyHosts", separated.toString());
    }

    private static String setProperty(final String name, final String value) {
        PrivilegedAction r0;
        if (value != null) {
            r0 = new PrivilegedAction<String>() {
                public String run() {
                    return System.setProperty(name, value);
                }
            };
        } else {
            r0 = new PrivilegedAction<String>() {
                public String run() {
                    return System.clearProperty(name);
                }
            };
        }
        return (String) AccessController.doPrivileged(r0);
    }

    public HttpRequest(CharSequence url2, String method) throws HttpRequestException {
        try {
            this.url = new URL(url2.toString());
            this.requestMethod = method;
        } catch (MalformedURLException e) {
            throw new HttpRequestException(e);
        }
    }

    public HttpRequest(URL url2, String method) throws HttpRequestException {
        this.url = url2;
        this.requestMethod = method;
    }

    private Proxy createProxy() {
        return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(this.httpProxyHost, this.httpProxyPort));
    }

    private HttpURLConnection createConnection() {
        HttpURLConnection connection2;
        try {
            if (this.httpProxyHost != null) {
                connection2 = CONNECTION_FACTORY.create(this.url, createProxy());
            } else {
                connection2 = CONNECTION_FACTORY.create(this.url);
            }
            connection2.setRequestMethod(this.requestMethod);
            return connection2;
        } catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }

    public String toString() {
        return method() + ' ' + url();
    }

    public HttpURLConnection getConnection() {
        if (this.connection == null) {
            this.connection = createConnection();
        }
        return this.connection;
    }

    public HttpRequest ignoreCloseExceptions(boolean ignore) {
        this.ignoreCloseExceptions = ignore;
        return this;
    }

    public boolean ignoreCloseExceptions() {
        return this.ignoreCloseExceptions;
    }

    public int code() throws HttpRequestException {
        try {
            closeOutput();
            return getConnection().getResponseCode();
        } catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }

    public HttpRequest code(AtomicInteger output2) throws HttpRequestException {
        output2.set(code());
        return this;
    }

    public boolean ok() throws HttpRequestException {
        return 200 == code();
    }

    public boolean created() throws HttpRequestException {
        return 201 == code();
    }

    public boolean serverError() throws HttpRequestException {
        return 500 == code();
    }

    public boolean badRequest() throws HttpRequestException {
        return 400 == code();
    }

    public boolean notFound() throws HttpRequestException {
        return 404 == code();
    }

    public boolean notModified() throws HttpRequestException {
        return 304 == code();
    }

    public String message() throws HttpRequestException {
        try {
            closeOutput();
            return getConnection().getResponseMessage();
        } catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }

    public HttpRequest disconnect() {
        getConnection().disconnect();
        return this;
    }

    public HttpRequest chunk(int size) {
        getConnection().setChunkedStreamingMode(size);
        return this;
    }

    public HttpRequest bufferSize(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Size must be greater than zero");
        }
        this.bufferSize = size;
        return this;
    }

    public int bufferSize() {
        return this.bufferSize;
    }

    public HttpRequest uncompress(boolean uncompress2) {
        this.uncompress = uncompress2;
        return this;
    }

    /* access modifiers changed from: protected */
    public ByteArrayOutputStream byteStream() {
        int size = contentLength();
        if (size > 0) {
            return new ByteArrayOutputStream(size);
        }
        return new ByteArrayOutputStream();
    }

    public String body(String charset) throws HttpRequestException {
        ByteArrayOutputStream output2 = byteStream();
        try {
            copy((InputStream) buffer(), (OutputStream) output2);
            return output2.toString(getValidCharset(charset));
        } catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }

    public String body() throws HttpRequestException {
        return body(charset());
    }

    public HttpRequest body(AtomicReference<String> output2) throws HttpRequestException {
        output2.set(body());
        return this;
    }

    public HttpRequest body(AtomicReference<String> output2, String charset) throws HttpRequestException {
        output2.set(body(charset));
        return this;
    }

    public boolean isBodyEmpty() throws HttpRequestException {
        return contentLength() == 0;
    }

    public byte[] bytes() throws HttpRequestException {
        ByteArrayOutputStream output2 = byteStream();
        try {
            copy((InputStream) buffer(), (OutputStream) output2);
            return output2.toByteArray();
        } catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }

    public BufferedInputStream buffer() throws HttpRequestException {
        return new BufferedInputStream(stream(), this.bufferSize);
    }

    public InputStream stream() throws HttpRequestException {
        InputStream stream;
        if (code() < 400) {
            try {
                stream = getConnection().getInputStream();
            } catch (IOException e) {
                throw new HttpRequestException(e);
            }
        } else {
            stream = getConnection().getErrorStream();
            if (stream == null) {
                try {
                    stream = getConnection().getInputStream();
                } catch (IOException e2) {
                    throw new HttpRequestException(e2);
                }
            }
        }
        if (!this.uncompress || !ENCODING_GZIP.equals(contentEncoding())) {
            return stream;
        }
        try {
            return new GZIPInputStream(stream);
        } catch (IOException e3) {
            throw new HttpRequestException(e3);
        }
    }

    public InputStreamReader reader(String charset) throws HttpRequestException {
        try {
            return new InputStreamReader(stream(), getValidCharset(charset));
        } catch (UnsupportedEncodingException e) {
            throw new HttpRequestException(e);
        }
    }

    public InputStreamReader reader() throws HttpRequestException {
        return reader(charset());
    }

    public BufferedReader bufferedReader(String charset) throws HttpRequestException {
        return new BufferedReader(reader(charset), this.bufferSize);
    }

    public BufferedReader bufferedReader() throws HttpRequestException {
        return bufferedReader(charset());
    }

    public HttpRequest receive(File file) throws HttpRequestException {
        try {
            final OutputStream output2 = new BufferedOutputStream(new FileOutputStream(file), this.bufferSize);
            return (HttpRequest) new CloseOperation<HttpRequest>(this.ignoreCloseExceptions, output2) {
                /* access modifiers changed from: protected */
                public HttpRequest run() throws HttpRequestException, IOException {
                    return HttpRequest.this.receive(output2);
                }
            }.call();
        } catch (FileNotFoundException e) {
            throw new HttpRequestException(e);
        }
    }

    public HttpRequest receive(OutputStream output2) throws HttpRequestException {
        try {
            return copy((InputStream) buffer(), output2);
        } catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }

    public HttpRequest receive(PrintStream output2) throws HttpRequestException {
        return receive((OutputStream) output2);
    }

    public HttpRequest receive(Appendable appendable) throws HttpRequestException {
        BufferedReader reader = bufferedReader();
        final BufferedReader bufferedReader = reader;
        final Appendable appendable2 = appendable;
        return (HttpRequest) new CloseOperation<HttpRequest>(reader, this.ignoreCloseExceptions) {
            public HttpRequest run() throws IOException {
                CharBuffer buffer = CharBuffer.allocate(HttpRequest.this.bufferSize);
                while (true) {
                    int read = bufferedReader.read(buffer);
                    if (read == -1) {
                        return HttpRequest.this;
                    }
                    buffer.rewind();
                    appendable2.append(buffer, 0, read);
                    buffer.rewind();
                }
            }
        }.call();
    }

    public HttpRequest receive(Writer writer) throws HttpRequestException {
        BufferedReader reader = bufferedReader();
        final BufferedReader bufferedReader = reader;
        final Writer writer2 = writer;
        return (HttpRequest) new CloseOperation<HttpRequest>(reader, this.ignoreCloseExceptions) {
            public HttpRequest run() throws IOException {
                return HttpRequest.this.copy((Reader) bufferedReader, writer2);
            }
        }.call();
    }

    public HttpRequest readTimeout(int timeout) {
        getConnection().setReadTimeout(timeout);
        return this;
    }

    public HttpRequest connectTimeout(int timeout) {
        getConnection().setConnectTimeout(timeout);
        return this;
    }

    public HttpRequest header(String name, String value) {
        getConnection().setRequestProperty(name, value);
        return this;
    }

    public HttpRequest header(String name, Number value) {
        return header(name, value != null ? value.toString() : null);
    }

    public HttpRequest headers(Map<String, String> headers) {
        if (!headers.isEmpty()) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                header(header);
            }
        }
        return this;
    }

    public HttpRequest header(Map.Entry<String, String> header) {
        return header(header.getKey(), header.getValue());
    }

    public String header(String name) throws HttpRequestException {
        closeOutputQuietly();
        return getConnection().getHeaderField(name);
    }

    public Map<String, List<String>> headers() throws HttpRequestException {
        closeOutputQuietly();
        return getConnection().getHeaderFields();
    }

    public long dateHeader(String name) throws HttpRequestException {
        return dateHeader(name, -1);
    }

    public long dateHeader(String name, long defaultValue) throws HttpRequestException {
        closeOutputQuietly();
        return getConnection().getHeaderFieldDate(name, defaultValue);
    }

    public int intHeader(String name) throws HttpRequestException {
        return intHeader(name, -1);
    }

    public int intHeader(String name, int defaultValue) throws HttpRequestException {
        closeOutputQuietly();
        return getConnection().getHeaderFieldInt(name, defaultValue);
    }

    public String[] headers(String name) {
        Map<String, List<String>> headers = headers();
        if (headers == null || headers.isEmpty()) {
            return EMPTY_STRINGS;
        }
        List<String> values = headers.get(name);
        if (values == null || values.isEmpty()) {
            return EMPTY_STRINGS;
        }
        return (String[]) values.toArray(new String[values.size()]);
    }

    public String parameter(String headerName, String paramName) {
        return getParam(header(headerName), paramName);
    }

    public Map<String, String> parameters(String headerName) {
        return getParams(header(headerName));
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x004b, code lost:
        r7 = r14.substring(r4 + 1, r0).trim();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.Map<java.lang.String, java.lang.String> getParams(java.lang.String r14) {
        /*
            r13 = this;
            r12 = 34
            r11 = 59
            r10 = -1
            if (r14 == 0) goto L_0x000d
            int r8 = r14.length()
            if (r8 != 0) goto L_0x0012
        L_0x000d:
            java.util.Map r5 = java.util.Collections.emptyMap()
        L_0x0011:
            return r5
        L_0x0012:
            int r1 = r14.length()
            int r8 = r14.indexOf(r11)
            int r6 = r8 + 1
            if (r6 == 0) goto L_0x0020
            if (r6 != r1) goto L_0x0025
        L_0x0020:
            java.util.Map r5 = java.util.Collections.emptyMap()
            goto L_0x0011
        L_0x0025:
            int r0 = r14.indexOf(r11, r6)
            if (r0 != r10) goto L_0x002c
            r0 = r1
        L_0x002c:
            java.util.LinkedHashMap r5 = new java.util.LinkedHashMap
            r5.<init>()
        L_0x0031:
            if (r6 >= r0) goto L_0x0011
            r8 = 61
            int r4 = r14.indexOf(r8, r6)
            if (r4 == r10) goto L_0x0077
            if (r4 >= r0) goto L_0x0077
            java.lang.String r8 = r14.substring(r6, r4)
            java.lang.String r3 = r8.trim()
            int r8 = r3.length()
            if (r8 <= 0) goto L_0x0077
            int r8 = r4 + 1
            java.lang.String r8 = r14.substring(r8, r0)
            java.lang.String r7 = r8.trim()
            int r2 = r7.length()
            if (r2 == 0) goto L_0x0077
            r8 = 2
            if (r2 <= r8) goto L_0x0081
            r8 = 0
            char r8 = r7.charAt(r8)
            if (r12 != r8) goto L_0x0081
            int r8 = r2 + -1
            char r8 = r7.charAt(r8)
            if (r12 != r8) goto L_0x0081
            r8 = 1
            int r9 = r2 + -1
            java.lang.String r8 = r7.substring(r8, r9)
            r5.put(r3, r8)
        L_0x0077:
            int r6 = r0 + 1
            int r0 = r14.indexOf(r11, r6)
            if (r0 != r10) goto L_0x0031
            r0 = r1
            goto L_0x0031
        L_0x0081:
            r5.put(r3, r7)
            goto L_0x0077
        */
        throw new UnsupportedOperationException("Method not decompiled: io.fabric.sdk.android.services.network.HttpRequest.getParams(java.lang.String):java.util.Map");
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0041, code lost:
        r3 = r12.substring(r2 + 1, r0).trim();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getParam(java.lang.String r12, java.lang.String r13) {
        /*
            r11 = this;
            r10 = 34
            r6 = 0
            r9 = 59
            r8 = -1
            if (r12 == 0) goto L_0x000e
            int r7 = r12.length()
            if (r7 != 0) goto L_0x0010
        L_0x000e:
            r3 = r6
        L_0x000f:
            return r3
        L_0x0010:
            int r1 = r12.length()
            int r7 = r12.indexOf(r9)
            int r4 = r7 + 1
            if (r4 == 0) goto L_0x001e
            if (r4 != r1) goto L_0x0020
        L_0x001e:
            r3 = r6
            goto L_0x000f
        L_0x0020:
            int r0 = r12.indexOf(r9, r4)
            if (r0 != r8) goto L_0x0027
            r0 = r1
        L_0x0027:
            if (r4 >= r0) goto L_0x0075
            r7 = 61
            int r2 = r12.indexOf(r7, r4)
            if (r2 == r8) goto L_0x006b
            if (r2 >= r0) goto L_0x006b
            java.lang.String r7 = r12.substring(r4, r2)
            java.lang.String r7 = r7.trim()
            boolean r7 = r13.equals(r7)
            if (r7 == 0) goto L_0x006b
            int r7 = r2 + 1
            java.lang.String r7 = r12.substring(r7, r0)
            java.lang.String r3 = r7.trim()
            int r5 = r3.length()
            if (r5 == 0) goto L_0x006b
            r6 = 2
            if (r5 <= r6) goto L_0x000f
            r6 = 0
            char r6 = r3.charAt(r6)
            if (r10 != r6) goto L_0x000f
            int r6 = r5 + -1
            char r6 = r3.charAt(r6)
            if (r10 != r6) goto L_0x000f
            r6 = 1
            int r7 = r5 + -1
            java.lang.String r3 = r3.substring(r6, r7)
            goto L_0x000f
        L_0x006b:
            int r4 = r0 + 1
            int r0 = r12.indexOf(r9, r4)
            if (r0 != r8) goto L_0x0027
            r0 = r1
            goto L_0x0027
        L_0x0075:
            r3 = r6
            goto L_0x000f
        */
        throw new UnsupportedOperationException("Method not decompiled: io.fabric.sdk.android.services.network.HttpRequest.getParam(java.lang.String, java.lang.String):java.lang.String");
    }

    public String charset() {
        return parameter(HEADER_CONTENT_TYPE, PARAM_CHARSET);
    }

    public HttpRequest userAgent(String userAgent) {
        return header("User-Agent", userAgent);
    }

    public HttpRequest referer(String referer) {
        return header(HEADER_REFERER, referer);
    }

    public HttpRequest useCaches(boolean useCaches) {
        getConnection().setUseCaches(useCaches);
        return this;
    }

    public HttpRequest acceptEncoding(String acceptEncoding) {
        return header(HEADER_ACCEPT_ENCODING, acceptEncoding);
    }

    public HttpRequest acceptGzipEncoding() {
        return acceptEncoding(ENCODING_GZIP);
    }

    public HttpRequest acceptCharset(String acceptCharset) {
        return header(HEADER_ACCEPT_CHARSET, acceptCharset);
    }

    public String contentEncoding() {
        return header(HEADER_CONTENT_ENCODING);
    }

    public String server() {
        return header(HEADER_SERVER);
    }

    public long date() {
        return dateHeader(HEADER_DATE);
    }

    public String cacheControl() {
        return header(HEADER_CACHE_CONTROL);
    }

    public String eTag() {
        return header(HEADER_ETAG);
    }

    public long expires() {
        return dateHeader(HEADER_EXPIRES);
    }

    public long lastModified() {
        return dateHeader(HEADER_LAST_MODIFIED);
    }

    public String location() {
        return header(HEADER_LOCATION);
    }

    public HttpRequest authorization(String authorization) {
        return header(HEADER_AUTHORIZATION, authorization);
    }

    public HttpRequest proxyAuthorization(String proxyAuthorization) {
        return header(HEADER_PROXY_AUTHORIZATION, proxyAuthorization);
    }

    public HttpRequest basic(String name, String password) {
        return authorization("Basic " + Base64.encode(name + ':' + password));
    }

    public HttpRequest proxyBasic(String name, String password) {
        return proxyAuthorization("Basic " + Base64.encode(name + ':' + password));
    }

    public HttpRequest ifModifiedSince(long ifModifiedSince) {
        getConnection().setIfModifiedSince(ifModifiedSince);
        return this;
    }

    public HttpRequest ifNoneMatch(String ifNoneMatch) {
        return header(HEADER_IF_NONE_MATCH, ifNoneMatch);
    }

    public HttpRequest contentType(String contentType) {
        return contentType(contentType, (String) null);
    }

    public HttpRequest contentType(String contentType, String charset) {
        if (charset == null || charset.length() <= 0) {
            return header(HEADER_CONTENT_TYPE, contentType);
        }
        return header(HEADER_CONTENT_TYPE, contentType + "; charset=" + charset);
    }

    public String contentType() {
        return header(HEADER_CONTENT_TYPE);
    }

    public int contentLength() {
        return intHeader(HEADER_CONTENT_LENGTH);
    }

    public HttpRequest contentLength(String contentLength) {
        return contentLength(Integer.parseInt(contentLength));
    }

    public HttpRequest contentLength(int contentLength) {
        getConnection().setFixedLengthStreamingMode(contentLength);
        return this;
    }

    public HttpRequest accept(String accept) {
        return header("Accept", accept);
    }

    public HttpRequest acceptJson() {
        return accept("application/json");
    }

    /* access modifiers changed from: protected */
    public HttpRequest copy(InputStream input, OutputStream output2) throws IOException {
        final InputStream inputStream = input;
        final OutputStream outputStream = output2;
        return (HttpRequest) new CloseOperation<HttpRequest>(input, this.ignoreCloseExceptions) {
            public HttpRequest run() throws IOException {
                byte[] buffer = new byte[HttpRequest.this.bufferSize];
                while (true) {
                    int read = inputStream.read(buffer);
                    if (read == -1) {
                        return HttpRequest.this;
                    }
                    outputStream.write(buffer, 0, read);
                }
            }
        }.call();
    }

    /* access modifiers changed from: protected */
    public HttpRequest copy(Reader input, Writer output2) throws IOException {
        final Reader reader = input;
        final Writer writer = output2;
        return (HttpRequest) new CloseOperation<HttpRequest>(input, this.ignoreCloseExceptions) {
            public HttpRequest run() throws IOException {
                char[] buffer = new char[HttpRequest.this.bufferSize];
                while (true) {
                    int read = reader.read(buffer);
                    if (read == -1) {
                        return HttpRequest.this;
                    }
                    writer.write(buffer, 0, read);
                }
            }
        }.call();
    }

    /* access modifiers changed from: protected */
    public HttpRequest closeOutput() throws IOException {
        if (this.output != null) {
            if (this.multipart) {
                this.output.write("\r\n--00content0boundary00--\r\n");
            }
            if (this.ignoreCloseExceptions) {
                try {
                    this.output.close();
                } catch (IOException e) {
                }
            } else {
                this.output.close();
            }
            this.output = null;
        }
        return this;
    }

    /* access modifiers changed from: protected */
    public HttpRequest closeOutputQuietly() throws HttpRequestException {
        try {
            return closeOutput();
        } catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }

    /* access modifiers changed from: protected */
    public HttpRequest openOutput() throws IOException {
        if (this.output == null) {
            getConnection().setDoOutput(true);
            this.output = new RequestOutputStream(getConnection().getOutputStream(), getParam(getConnection().getRequestProperty(HEADER_CONTENT_TYPE), PARAM_CHARSET), this.bufferSize);
        }
        return this;
    }

    /* access modifiers changed from: protected */
    public HttpRequest startPart() throws IOException {
        if (!this.multipart) {
            this.multipart = true;
            contentType(CONTENT_TYPE_MULTIPART).openOutput();
            this.output.write("--00content0boundary00\r\n");
        } else {
            this.output.write("\r\n--00content0boundary00\r\n");
        }
        return this;
    }

    /* access modifiers changed from: protected */
    public HttpRequest writePartHeader(String name, String filename) throws IOException {
        return writePartHeader(name, filename, (String) null);
    }

    /* access modifiers changed from: protected */
    public HttpRequest writePartHeader(String name, String filename, String contentType) throws IOException {
        StringBuilder partBuffer = new StringBuilder();
        partBuffer.append("form-data; name=\"").append(name);
        if (filename != null) {
            partBuffer.append("\"; filename=\"").append(filename);
        }
        partBuffer.append('\"');
        partHeader("Content-Disposition", partBuffer.toString());
        if (contentType != null) {
            partHeader(HEADER_CONTENT_TYPE, contentType);
        }
        return send((CharSequence) CRLF);
    }

    public HttpRequest part(String name, String part) {
        return part(name, (String) null, part);
    }

    public HttpRequest part(String name, String filename, String part) throws HttpRequestException {
        return part(name, filename, (String) null, part);
    }

    public HttpRequest part(String name, String filename, String contentType, String part) throws HttpRequestException {
        try {
            startPart();
            writePartHeader(name, filename, contentType);
            this.output.write(part);
            return this;
        } catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }

    public HttpRequest part(String name, Number part) throws HttpRequestException {
        return part(name, (String) null, part);
    }

    public HttpRequest part(String name, String filename, Number part) throws HttpRequestException {
        return part(name, filename, part != null ? part.toString() : null);
    }

    public HttpRequest part(String name, File part) throws HttpRequestException {
        return part(name, (String) null, part);
    }

    public HttpRequest part(String name, String filename, File part) throws HttpRequestException {
        return part(name, filename, (String) null, part);
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x001f A[SYNTHETIC, Splitter:B:15:0x001f] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public io.fabric.sdk.android.services.network.HttpRequest part(java.lang.String r6, java.lang.String r7, java.lang.String r8, java.io.File r9) throws io.fabric.sdk.android.services.network.HttpRequest.HttpRequestException {
        /*
            r5 = this;
            r1 = 0
            java.io.BufferedInputStream r2 = new java.io.BufferedInputStream     // Catch:{ IOException -> 0x0015 }
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ IOException -> 0x0015 }
            r3.<init>(r9)     // Catch:{ IOException -> 0x0015 }
            r2.<init>(r3)     // Catch:{ IOException -> 0x0015 }
            io.fabric.sdk.android.services.network.HttpRequest r3 = r5.part((java.lang.String) r6, (java.lang.String) r7, (java.lang.String) r8, (java.io.InputStream) r2)     // Catch:{ IOException -> 0x002a, all -> 0x0027 }
            if (r2 == 0) goto L_0x0014
            r2.close()     // Catch:{ IOException -> 0x0023 }
        L_0x0014:
            return r3
        L_0x0015:
            r0 = move-exception
        L_0x0016:
            io.fabric.sdk.android.services.network.HttpRequest$HttpRequestException r3 = new io.fabric.sdk.android.services.network.HttpRequest$HttpRequestException     // Catch:{ all -> 0x001c }
            r3.<init>(r0)     // Catch:{ all -> 0x001c }
            throw r3     // Catch:{ all -> 0x001c }
        L_0x001c:
            r3 = move-exception
        L_0x001d:
            if (r1 == 0) goto L_0x0022
            r1.close()     // Catch:{ IOException -> 0x0025 }
        L_0x0022:
            throw r3
        L_0x0023:
            r4 = move-exception
            goto L_0x0014
        L_0x0025:
            r4 = move-exception
            goto L_0x0022
        L_0x0027:
            r3 = move-exception
            r1 = r2
            goto L_0x001d
        L_0x002a:
            r0 = move-exception
            r1 = r2
            goto L_0x0016
        */
        throw new UnsupportedOperationException("Method not decompiled: io.fabric.sdk.android.services.network.HttpRequest.part(java.lang.String, java.lang.String, java.lang.String, java.io.File):io.fabric.sdk.android.services.network.HttpRequest");
    }

    public HttpRequest part(String name, InputStream part) throws HttpRequestException {
        return part(name, (String) null, (String) null, part);
    }

    public HttpRequest part(String name, String filename, String contentType, InputStream part) throws HttpRequestException {
        try {
            startPart();
            writePartHeader(name, filename, contentType);
            copy(part, (OutputStream) this.output);
            return this;
        } catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }

    public HttpRequest partHeader(String name, String value) throws HttpRequestException {
        return send((CharSequence) name).send((CharSequence) ": ").send((CharSequence) value).send((CharSequence) CRLF);
    }

    public HttpRequest send(File input) throws HttpRequestException {
        try {
            return send(new BufferedInputStream(new FileInputStream(input)));
        } catch (FileNotFoundException e) {
            throw new HttpRequestException(e);
        }
    }

    public HttpRequest send(byte[] input) throws HttpRequestException {
        return send((InputStream) new ByteArrayInputStream(input));
    }

    public HttpRequest send(InputStream input) throws HttpRequestException {
        try {
            openOutput();
            copy(input, (OutputStream) this.output);
            return this;
        } catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }

    public HttpRequest send(final Reader input) throws HttpRequestException {
        try {
            openOutput();
            final Writer writer = new OutputStreamWriter(this.output, this.output.encoder.charset());
            return (HttpRequest) new FlushOperation<HttpRequest>(writer) {
                /* access modifiers changed from: protected */
                public HttpRequest run() throws IOException {
                    return HttpRequest.this.copy(input, writer);
                }
            }.call();
        } catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }

    public HttpRequest send(CharSequence value) throws HttpRequestException {
        try {
            openOutput();
            this.output.write(value.toString());
            return this;
        } catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }

    public OutputStreamWriter writer() throws HttpRequestException {
        try {
            openOutput();
            return new OutputStreamWriter(this.output, this.output.encoder.charset());
        } catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }

    public HttpRequest form(Map<?, ?> values) throws HttpRequestException {
        return form(values, CHARSET_UTF8);
    }

    public HttpRequest form(Map.Entry<?, ?> entry) throws HttpRequestException {
        return form(entry, CHARSET_UTF8);
    }

    public HttpRequest form(Map.Entry<?, ?> entry, String charset) throws HttpRequestException {
        return form(entry.getKey(), entry.getValue(), charset);
    }

    public HttpRequest form(Object name, Object value) throws HttpRequestException {
        return form(name, value, CHARSET_UTF8);
    }

    public HttpRequest form(Object name, Object value, String charset) throws HttpRequestException {
        boolean first = !this.form;
        if (first) {
            contentType(CONTENT_TYPE_FORM, charset);
            this.form = true;
        }
        String charset2 = getValidCharset(charset);
        try {
            openOutput();
            if (!first) {
                this.output.write(38);
            }
            this.output.write(URLEncoder.encode(name.toString(), charset2));
            this.output.write(61);
            if (value != null) {
                this.output.write(URLEncoder.encode(value.toString(), charset2));
            }
            return this;
        } catch (IOException e) {
            throw new HttpRequestException(e);
        }
    }

    public HttpRequest form(Map<?, ?> values, String charset) throws HttpRequestException {
        if (!values.isEmpty()) {
            for (Map.Entry<?, ?> entry : values.entrySet()) {
                form(entry, charset);
            }
        }
        return this;
    }

    public HttpRequest trustAllCerts() throws HttpRequestException {
        return this;
    }

    public HttpRequest trustAllHosts() {
        return this;
    }

    public URL url() {
        return getConnection().getURL();
    }

    public String method() {
        return getConnection().getRequestMethod();
    }

    public HttpRequest useProxy(String proxyHost, int proxyPort) {
        if (this.connection != null) {
            throw new IllegalStateException("The connection has already been created. This method must be called before reading or writing to the request.");
        }
        this.httpProxyHost = proxyHost;
        this.httpProxyPort = proxyPort;
        return this;
    }

    public HttpRequest followRedirects(boolean followRedirects) {
        getConnection().setInstanceFollowRedirects(followRedirects);
        return this;
    }
}
