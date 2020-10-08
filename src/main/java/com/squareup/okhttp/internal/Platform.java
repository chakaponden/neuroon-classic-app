package com.squareup.okhttp.internal;

import android.util.Log;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.internal.tls.AndroidTrustRootIndex;
import com.squareup.okhttp.internal.tls.RealTrustRootIndex;
import com.squareup.okhttp.internal.tls.TrustRootIndex;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import okio.Buffer;

public class Platform {
    private static final Platform PLATFORM = findPlatform();

    public static Platform get() {
        return PLATFORM;
    }

    public String getPrefix() {
        return "OkHttp";
    }

    public void logW(String warning) {
        System.out.println(warning);
    }

    public void tagSocket(Socket socket) throws SocketException {
    }

    public void untagSocket(Socket socket) throws SocketException {
    }

    public X509TrustManager trustManager(SSLSocketFactory sslSocketFactory) {
        return null;
    }

    public TrustRootIndex trustRootIndex(X509TrustManager trustManager) {
        return new RealTrustRootIndex(trustManager.getAcceptedIssuers());
    }

    public void configureTlsExtensions(SSLSocket sslSocket, String hostname, List<Protocol> list) {
    }

    public void afterHandshake(SSLSocket sslSocket) {
    }

    public String getSelectedProtocol(SSLSocket socket) {
        return null;
    }

    public void connectSocket(Socket socket, InetSocketAddress address, int connectTimeout) throws IOException {
        socket.connect(address, connectTimeout);
    }

    public void log(String message) {
        System.out.println(message);
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static com.squareup.okhttp.internal.Platform findPlatform() {
        /*
            java.lang.String r2 = "com.android.org.conscrypt.SSLParametersImpl"
            java.lang.Class r3 = java.lang.Class.forName(r2)     // Catch:{ ClassNotFoundException -> 0x00a1 }
        L_0x0006:
            com.squareup.okhttp.internal.OptionalMethod r4 = new com.squareup.okhttp.internal.OptionalMethod     // Catch:{ ClassNotFoundException -> 0x00aa }
            r2 = 0
            java.lang.String r10 = "setUseSessionTickets"
            r24 = 1
            r0 = r24
            java.lang.Class[] r0 = new java.lang.Class[r0]     // Catch:{ ClassNotFoundException -> 0x00aa }
            r24 = r0
            r25 = 0
            java.lang.Class r26 = java.lang.Boolean.TYPE     // Catch:{ ClassNotFoundException -> 0x00aa }
            r24[r25] = r26     // Catch:{ ClassNotFoundException -> 0x00aa }
            r0 = r24
            r4.<init>(r2, r10, r0)     // Catch:{ ClassNotFoundException -> 0x00aa }
            com.squareup.okhttp.internal.OptionalMethod r5 = new com.squareup.okhttp.internal.OptionalMethod     // Catch:{ ClassNotFoundException -> 0x00aa }
            r2 = 0
            java.lang.String r10 = "setHostname"
            r24 = 1
            r0 = r24
            java.lang.Class[] r0 = new java.lang.Class[r0]     // Catch:{ ClassNotFoundException -> 0x00aa }
            r24 = r0
            r25 = 0
            java.lang.Class<java.lang.String> r26 = java.lang.String.class
            r24[r25] = r26     // Catch:{ ClassNotFoundException -> 0x00aa }
            r0 = r24
            r5.<init>(r2, r10, r0)     // Catch:{ ClassNotFoundException -> 0x00aa }
            r6 = 0
            r7 = 0
            r8 = 0
            r9 = 0
            java.lang.String r2 = "android.net.TrafficStats"
            java.lang.Class r23 = java.lang.Class.forName(r2)     // Catch:{ ClassNotFoundException -> 0x0152, NoSuchMethodException -> 0x0155 }
            java.lang.String r2 = "tagSocket"
            r10 = 1
            java.lang.Class[] r10 = new java.lang.Class[r10]     // Catch:{ ClassNotFoundException -> 0x0152, NoSuchMethodException -> 0x0155 }
            r24 = 0
            java.lang.Class<java.net.Socket> r25 = java.net.Socket.class
            r10[r24] = r25     // Catch:{ ClassNotFoundException -> 0x0152, NoSuchMethodException -> 0x0155 }
            r0 = r23
            java.lang.reflect.Method r6 = r0.getMethod(r2, r10)     // Catch:{ ClassNotFoundException -> 0x0152, NoSuchMethodException -> 0x0155 }
            java.lang.String r2 = "untagSocket"
            r10 = 1
            java.lang.Class[] r10 = new java.lang.Class[r10]     // Catch:{ ClassNotFoundException -> 0x0152, NoSuchMethodException -> 0x0155 }
            r24 = 0
            java.lang.Class<java.net.Socket> r25 = java.net.Socket.class
            r10[r24] = r25     // Catch:{ ClassNotFoundException -> 0x0152, NoSuchMethodException -> 0x0155 }
            r0 = r23
            java.lang.reflect.Method r7 = r0.getMethod(r2, r10)     // Catch:{ ClassNotFoundException -> 0x0152, NoSuchMethodException -> 0x0155 }
            java.lang.String r2 = "android.net.Network"
            java.lang.Class.forName(r2)     // Catch:{ ClassNotFoundException -> 0x015d, NoSuchMethodException -> 0x0155 }
            com.squareup.okhttp.internal.OptionalMethod r18 = new com.squareup.okhttp.internal.OptionalMethod     // Catch:{ ClassNotFoundException -> 0x015d, NoSuchMethodException -> 0x0155 }
            java.lang.Class<byte[]> r2 = byte[].class
            java.lang.String r10 = "getAlpnSelectedProtocol"
            r24 = 0
            r0 = r24
            java.lang.Class[] r0 = new java.lang.Class[r0]     // Catch:{ ClassNotFoundException -> 0x015d, NoSuchMethodException -> 0x0155 }
            r24 = r0
            r0 = r18
            r1 = r24
            r0.<init>(r2, r10, r1)     // Catch:{ ClassNotFoundException -> 0x015d, NoSuchMethodException -> 0x0155 }
            com.squareup.okhttp.internal.OptionalMethod r22 = new com.squareup.okhttp.internal.OptionalMethod     // Catch:{ ClassNotFoundException -> 0x0160, NoSuchMethodException -> 0x0158 }
            r2 = 0
            java.lang.String r10 = "setAlpnProtocols"
            r24 = 1
            r0 = r24
            java.lang.Class[] r0 = new java.lang.Class[r0]     // Catch:{ ClassNotFoundException -> 0x0160, NoSuchMethodException -> 0x0158 }
            r24 = r0
            r25 = 0
            java.lang.Class<byte[]> r26 = byte[].class
            r24[r25] = r26     // Catch:{ ClassNotFoundException -> 0x0160, NoSuchMethodException -> 0x0158 }
            r0 = r22
            r1 = r24
            r0.<init>(r2, r10, r1)     // Catch:{ ClassNotFoundException -> 0x0160, NoSuchMethodException -> 0x0158 }
            r9 = r22
            r8 = r18
        L_0x009a:
            com.squareup.okhttp.internal.Platform$Android r2 = new com.squareup.okhttp.internal.Platform$Android     // Catch:{ ClassNotFoundException -> 0x00aa }
            r2.<init>(r3, r4, r5, r6, r7, r8, r9)     // Catch:{ ClassNotFoundException -> 0x00aa }
            r10 = r2
        L_0x00a0:
            return r10
        L_0x00a1:
            r17 = move-exception
            java.lang.String r2 = "org.apache.harmony.xnet.provider.jsse.SSLParametersImpl"
            java.lang.Class r3 = java.lang.Class.forName(r2)     // Catch:{ ClassNotFoundException -> 0x00aa }
            goto L_0x0006
        L_0x00aa:
            r2 = move-exception
            java.lang.String r2 = "sun.security.ssl.SSLContextImpl"
            java.lang.Class r11 = java.lang.Class.forName(r2)     // Catch:{ ClassNotFoundException -> 0x0148 }
            java.lang.String r20 = "org.eclipse.jetty.alpn.ALPN"
            java.lang.Class r19 = java.lang.Class.forName(r20)     // Catch:{ ClassNotFoundException -> 0x0140, NoSuchMethodException -> 0x0150 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ ClassNotFoundException -> 0x0140, NoSuchMethodException -> 0x0150 }
            r2.<init>()     // Catch:{ ClassNotFoundException -> 0x0140, NoSuchMethodException -> 0x0150 }
            r0 = r20
            java.lang.StringBuilder r2 = r2.append(r0)     // Catch:{ ClassNotFoundException -> 0x0140, NoSuchMethodException -> 0x0150 }
            java.lang.String r10 = "$Provider"
            java.lang.StringBuilder r2 = r2.append(r10)     // Catch:{ ClassNotFoundException -> 0x0140, NoSuchMethodException -> 0x0150 }
            java.lang.String r2 = r2.toString()     // Catch:{ ClassNotFoundException -> 0x0140, NoSuchMethodException -> 0x0150 }
            java.lang.Class r21 = java.lang.Class.forName(r2)     // Catch:{ ClassNotFoundException -> 0x0140, NoSuchMethodException -> 0x0150 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ ClassNotFoundException -> 0x0140, NoSuchMethodException -> 0x0150 }
            r2.<init>()     // Catch:{ ClassNotFoundException -> 0x0140, NoSuchMethodException -> 0x0150 }
            r0 = r20
            java.lang.StringBuilder r2 = r2.append(r0)     // Catch:{ ClassNotFoundException -> 0x0140, NoSuchMethodException -> 0x0150 }
            java.lang.String r10 = "$ClientProvider"
            java.lang.StringBuilder r2 = r2.append(r10)     // Catch:{ ClassNotFoundException -> 0x0140, NoSuchMethodException -> 0x0150 }
            java.lang.String r2 = r2.toString()     // Catch:{ ClassNotFoundException -> 0x0140, NoSuchMethodException -> 0x0150 }
            java.lang.Class r15 = java.lang.Class.forName(r2)     // Catch:{ ClassNotFoundException -> 0x0140, NoSuchMethodException -> 0x0150 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ ClassNotFoundException -> 0x0140, NoSuchMethodException -> 0x0150 }
            r2.<init>()     // Catch:{ ClassNotFoundException -> 0x0140, NoSuchMethodException -> 0x0150 }
            r0 = r20
            java.lang.StringBuilder r2 = r2.append(r0)     // Catch:{ ClassNotFoundException -> 0x0140, NoSuchMethodException -> 0x0150 }
            java.lang.String r10 = "$ServerProvider"
            java.lang.StringBuilder r2 = r2.append(r10)     // Catch:{ ClassNotFoundException -> 0x0140, NoSuchMethodException -> 0x0150 }
            java.lang.String r2 = r2.toString()     // Catch:{ ClassNotFoundException -> 0x0140, NoSuchMethodException -> 0x0150 }
            java.lang.Class r16 = java.lang.Class.forName(r2)     // Catch:{ ClassNotFoundException -> 0x0140, NoSuchMethodException -> 0x0150 }
            java.lang.String r2 = "put"
            r10 = 2
            java.lang.Class[] r10 = new java.lang.Class[r10]     // Catch:{ ClassNotFoundException -> 0x0140, NoSuchMethodException -> 0x0150 }
            r24 = 0
            java.lang.Class<javax.net.ssl.SSLSocket> r25 = javax.net.ssl.SSLSocket.class
            r10[r24] = r25     // Catch:{ ClassNotFoundException -> 0x0140, NoSuchMethodException -> 0x0150 }
            r24 = 1
            r10[r24] = r21     // Catch:{ ClassNotFoundException -> 0x0140, NoSuchMethodException -> 0x0150 }
            r0 = r19
            java.lang.reflect.Method r12 = r0.getMethod(r2, r10)     // Catch:{ ClassNotFoundException -> 0x0140, NoSuchMethodException -> 0x0150 }
            java.lang.String r2 = "get"
            r10 = 1
            java.lang.Class[] r10 = new java.lang.Class[r10]     // Catch:{ ClassNotFoundException -> 0x0140, NoSuchMethodException -> 0x0150 }
            r24 = 0
            java.lang.Class<javax.net.ssl.SSLSocket> r25 = javax.net.ssl.SSLSocket.class
            r10[r24] = r25     // Catch:{ ClassNotFoundException -> 0x0140, NoSuchMethodException -> 0x0150 }
            r0 = r19
            java.lang.reflect.Method r13 = r0.getMethod(r2, r10)     // Catch:{ ClassNotFoundException -> 0x0140, NoSuchMethodException -> 0x0150 }
            java.lang.String r2 = "remove"
            r10 = 1
            java.lang.Class[] r10 = new java.lang.Class[r10]     // Catch:{ ClassNotFoundException -> 0x0140, NoSuchMethodException -> 0x0150 }
            r24 = 0
            java.lang.Class<javax.net.ssl.SSLSocket> r25 = javax.net.ssl.SSLSocket.class
            r10[r24] = r25     // Catch:{ ClassNotFoundException -> 0x0140, NoSuchMethodException -> 0x0150 }
            r0 = r19
            java.lang.reflect.Method r14 = r0.getMethod(r2, r10)     // Catch:{ ClassNotFoundException -> 0x0140, NoSuchMethodException -> 0x0150 }
            com.squareup.okhttp.internal.Platform$JdkWithJettyBootPlatform r10 = new com.squareup.okhttp.internal.Platform$JdkWithJettyBootPlatform     // Catch:{ ClassNotFoundException -> 0x0140, NoSuchMethodException -> 0x0150 }
            r10.<init>(r11, r12, r13, r14, r15, r16)     // Catch:{ ClassNotFoundException -> 0x0140, NoSuchMethodException -> 0x0150 }
            goto L_0x00a0
        L_0x0140:
            r2 = move-exception
        L_0x0141:
            com.squareup.okhttp.internal.Platform$JdkPlatform r10 = new com.squareup.okhttp.internal.Platform$JdkPlatform     // Catch:{ ClassNotFoundException -> 0x0148 }
            r10.<init>(r11)     // Catch:{ ClassNotFoundException -> 0x0148 }
            goto L_0x00a0
        L_0x0148:
            r2 = move-exception
            com.squareup.okhttp.internal.Platform r10 = new com.squareup.okhttp.internal.Platform
            r10.<init>()
            goto L_0x00a0
        L_0x0150:
            r2 = move-exception
            goto L_0x0141
        L_0x0152:
            r2 = move-exception
            goto L_0x009a
        L_0x0155:
            r2 = move-exception
            goto L_0x009a
        L_0x0158:
            r2 = move-exception
            r8 = r18
            goto L_0x009a
        L_0x015d:
            r2 = move-exception
            goto L_0x009a
        L_0x0160:
            r2 = move-exception
            r8 = r18
            goto L_0x009a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.squareup.okhttp.internal.Platform.findPlatform():com.squareup.okhttp.internal.Platform");
    }

    private static class Android extends Platform {
        private static final int MAX_LOG_LENGTH = 4000;
        private final OptionalMethod<Socket> getAlpnSelectedProtocol;
        private final OptionalMethod<Socket> setAlpnProtocols;
        private final OptionalMethod<Socket> setHostname;
        private final OptionalMethod<Socket> setUseSessionTickets;
        private final Class<?> sslParametersClass;
        private final Method trafficStatsTagSocket;
        private final Method trafficStatsUntagSocket;

        public Android(Class<?> sslParametersClass2, OptionalMethod<Socket> setUseSessionTickets2, OptionalMethod<Socket> setHostname2, Method trafficStatsTagSocket2, Method trafficStatsUntagSocket2, OptionalMethod<Socket> getAlpnSelectedProtocol2, OptionalMethod<Socket> setAlpnProtocols2) {
            this.sslParametersClass = sslParametersClass2;
            this.setUseSessionTickets = setUseSessionTickets2;
            this.setHostname = setHostname2;
            this.trafficStatsTagSocket = trafficStatsTagSocket2;
            this.trafficStatsUntagSocket = trafficStatsUntagSocket2;
            this.getAlpnSelectedProtocol = getAlpnSelectedProtocol2;
            this.setAlpnProtocols = setAlpnProtocols2;
        }

        public void connectSocket(Socket socket, InetSocketAddress address, int connectTimeout) throws IOException {
            try {
                socket.connect(address, connectTimeout);
            } catch (AssertionError e) {
                if (Util.isAndroidGetsocknameError(e)) {
                    throw new IOException(e);
                }
                throw e;
            } catch (SecurityException e2) {
                IOException ioException = new IOException("Exception in connect");
                ioException.initCause(e2);
                throw ioException;
            }
        }

        public X509TrustManager trustManager(SSLSocketFactory sslSocketFactory) {
            Object context = readFieldOrNull(sslSocketFactory, this.sslParametersClass, "sslParameters");
            if (context == null) {
                try {
                    context = readFieldOrNull(sslSocketFactory, Class.forName("com.google.android.gms.org.conscrypt.SSLParametersImpl", false, sslSocketFactory.getClass().getClassLoader()), "sslParameters");
                } catch (ClassNotFoundException e) {
                    return null;
                }
            }
            X509TrustManager x509TrustManager = (X509TrustManager) readFieldOrNull(context, X509TrustManager.class, "x509TrustManager");
            return x509TrustManager != null ? x509TrustManager : (X509TrustManager) readFieldOrNull(context, X509TrustManager.class, "trustManager");
        }

        public TrustRootIndex trustRootIndex(X509TrustManager trustManager) {
            TrustRootIndex result = AndroidTrustRootIndex.get(trustManager);
            return result != null ? result : Platform.super.trustRootIndex(trustManager);
        }

        public void configureTlsExtensions(SSLSocket sslSocket, String hostname, List<Protocol> protocols) {
            if (hostname != null) {
                this.setUseSessionTickets.invokeOptionalWithoutCheckedException(sslSocket, true);
                this.setHostname.invokeOptionalWithoutCheckedException(sslSocket, hostname);
            }
            if (this.setAlpnProtocols != null && this.setAlpnProtocols.isSupported(sslSocket)) {
                this.setAlpnProtocols.invokeWithoutCheckedException(sslSocket, concatLengthPrefixed(protocols));
            }
        }

        public String getSelectedProtocol(SSLSocket socket) {
            if (this.getAlpnSelectedProtocol == null || !this.getAlpnSelectedProtocol.isSupported(socket)) {
                return null;
            }
            byte[] alpnResult = (byte[]) this.getAlpnSelectedProtocol.invokeWithoutCheckedException(socket, new Object[0]);
            return alpnResult != null ? new String(alpnResult, Util.UTF_8) : null;
        }

        public void tagSocket(Socket socket) throws SocketException {
            if (this.trafficStatsTagSocket != null) {
                try {
                    this.trafficStatsTagSocket.invoke((Object) null, new Object[]{socket});
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e2) {
                    throw new RuntimeException(e2.getCause());
                }
            }
        }

        public void untagSocket(Socket socket) throws SocketException {
            if (this.trafficStatsUntagSocket != null) {
                try {
                    this.trafficStatsUntagSocket.invoke((Object) null, new Object[]{socket});
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e2) {
                    throw new RuntimeException(e2.getCause());
                }
            }
        }

        public void log(String message) {
            int i = 0;
            int length = message.length();
            while (i < length) {
                int newline = message.indexOf(10, i);
                if (newline == -1) {
                    newline = length;
                }
                do {
                    int end = Math.min(newline, i + MAX_LOG_LENGTH);
                    Log.d("OkHttp", message.substring(i, end));
                    i = end;
                } while (i < newline);
                i++;
            }
        }
    }

    private static class JdkPlatform extends Platform {
        private final Class<?> sslContextClass;

        public JdkPlatform(Class<?> sslContextClass2) {
            this.sslContextClass = sslContextClass2;
        }

        public X509TrustManager trustManager(SSLSocketFactory sslSocketFactory) {
            Object context = readFieldOrNull(sslSocketFactory, this.sslContextClass, "context");
            if (context == null) {
                return null;
            }
            return (X509TrustManager) readFieldOrNull(context, X509TrustManager.class, "trustManager");
        }
    }

    private static class JdkWithJettyBootPlatform extends JdkPlatform {
        private final Class<?> clientProviderClass;
        private final Method getMethod;
        private final Method putMethod;
        private final Method removeMethod;
        private final Class<?> serverProviderClass;

        public JdkWithJettyBootPlatform(Class<?> sslContextClass, Method putMethod2, Method getMethod2, Method removeMethod2, Class<?> clientProviderClass2, Class<?> serverProviderClass2) {
            super(sslContextClass);
            this.putMethod = putMethod2;
            this.getMethod = getMethod2;
            this.removeMethod = removeMethod2;
            this.clientProviderClass = clientProviderClass2;
            this.serverProviderClass = serverProviderClass2;
        }

        public void configureTlsExtensions(SSLSocket sslSocket, String hostname, List<Protocol> protocols) {
            List<String> names = new ArrayList<>(protocols.size());
            int size = protocols.size();
            for (int i = 0; i < size; i++) {
                Protocol protocol = protocols.get(i);
                if (protocol != Protocol.HTTP_1_0) {
                    names.add(protocol.toString());
                }
            }
            try {
                Object provider = Proxy.newProxyInstance(Platform.class.getClassLoader(), new Class[]{this.clientProviderClass, this.serverProviderClass}, new JettyNegoProvider(names));
                this.putMethod.invoke((Object) null, new Object[]{sslSocket, provider});
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new AssertionError(e);
            }
        }

        public void afterHandshake(SSLSocket sslSocket) {
            try {
                this.removeMethod.invoke((Object) null, new Object[]{sslSocket});
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new AssertionError();
            }
        }

        public String getSelectedProtocol(SSLSocket socket) {
            try {
                JettyNegoProvider provider = (JettyNegoProvider) Proxy.getInvocationHandler(this.getMethod.invoke((Object) null, new Object[]{socket}));
                if (!provider.unsupported && provider.selected == null) {
                    Internal.logger.log(Level.INFO, "ALPN callback dropped: SPDY and HTTP/2 are disabled. Is alpn-boot on the boot class path?");
                    return null;
                } else if (!provider.unsupported) {
                    return provider.selected;
                } else {
                    return null;
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new AssertionError();
            }
        }
    }

    private static class JettyNegoProvider implements InvocationHandler {
        private final List<String> protocols;
        /* access modifiers changed from: private */
        public String selected;
        /* access modifiers changed from: private */
        public boolean unsupported;

        public JettyNegoProvider(List<String> protocols2) {
            this.protocols = protocols2;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            Class<?> returnType = method.getReturnType();
            if (args == null) {
                args = Util.EMPTY_STRING_ARRAY;
            }
            if (methodName.equals("supports") && Boolean.TYPE == returnType) {
                return true;
            }
            if (methodName.equals("unsupported") && Void.TYPE == returnType) {
                this.unsupported = true;
                return null;
            } else if (methodName.equals("protocols") && args.length == 0) {
                return this.protocols;
            } else {
                if ((methodName.equals("selectProtocol") || methodName.equals("select")) && String.class == returnType && args.length == 1 && (args[0] instanceof List)) {
                    List<String> peerProtocols = (List) args[0];
                    int size = peerProtocols.size();
                    for (int i = 0; i < size; i++) {
                        if (this.protocols.contains(peerProtocols.get(i))) {
                            String str = peerProtocols.get(i);
                            this.selected = str;
                            return str;
                        }
                    }
                    String str2 = this.protocols.get(0);
                    this.selected = str2;
                    return str2;
                } else if ((!methodName.equals("protocolSelected") && !methodName.equals("selected")) || args.length != 1) {
                    return method.invoke(this, args);
                } else {
                    this.selected = (String) args[0];
                    return null;
                }
            }
        }
    }

    static byte[] concatLengthPrefixed(List<Protocol> protocols) {
        Buffer result = new Buffer();
        int size = protocols.size();
        for (int i = 0; i < size; i++) {
            Protocol protocol = protocols.get(i);
            if (protocol != Protocol.HTTP_1_0) {
                result.writeByte(protocol.toString().length());
                result.writeUtf8(protocol.toString());
            }
        }
        return result.readByteArray();
    }

    static <T> T readFieldOrNull(Object instance, Class<T> fieldType, String fieldName) {
        Object delegate;
        Class cls = instance.getClass();
        while (cls != Object.class) {
            try {
                Field field = cls.getDeclaredField(fieldName);
                field.setAccessible(true);
                Object value = field.get(instance);
                if (value == null || !fieldType.isInstance(value)) {
                    return null;
                }
                return fieldType.cast(value);
            } catch (NoSuchFieldException e) {
                cls = cls.getSuperclass();
            } catch (IllegalAccessException e2) {
                throw new AssertionError();
            }
        }
        if (fieldName.equals("delegate") || (delegate = readFieldOrNull(instance, Object.class, "delegate")) == null) {
            return null;
        }
        return readFieldOrNull(delegate, fieldType, fieldName);
    }
}
