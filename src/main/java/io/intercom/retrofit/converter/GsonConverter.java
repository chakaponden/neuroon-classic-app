package io.intercom.retrofit.converter;

import io.fabric.sdk.android.services.network.HttpRequest;
import io.intercom.com.google.gson.Gson;
import io.intercom.retrofit.mime.TypedOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class GsonConverter implements Converter {
    private String charset;
    private final Gson gson;

    public GsonConverter(Gson gson2) {
        this(gson2, HttpRequest.CHARSET_UTF8);
    }

    public GsonConverter(Gson gson2, String charset2) {
        this.gson = gson2;
        this.charset = charset2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0030 A[SYNTHETIC, Splitter:B:18:0x0030] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:22:0x0035=Splitter:B:22:0x0035, B:13:0x0027=Splitter:B:13:0x0027} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object fromBody(io.intercom.retrofit.mime.TypedInput r7, java.lang.reflect.Type r8) throws io.intercom.retrofit.converter.ConversionException {
        /*
            r6 = this;
            java.lang.String r0 = r6.charset
            java.lang.String r4 = r7.mimeType()
            if (r4 == 0) goto L_0x0010
            java.lang.String r4 = r7.mimeType()
            java.lang.String r0 = io.intercom.retrofit.mime.MimeUtil.parseCharset(r4, r0)
        L_0x0010:
            r2 = 0
            java.io.InputStreamReader r3 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x0026, JsonParseException -> 0x0034 }
            java.io.InputStream r4 = r7.in()     // Catch:{ IOException -> 0x0026, JsonParseException -> 0x0034 }
            r3.<init>(r4, r0)     // Catch:{ IOException -> 0x0026, JsonParseException -> 0x0034 }
            io.intercom.com.google.gson.Gson r4 = r6.gson     // Catch:{ IOException -> 0x0045, JsonParseException -> 0x0042, all -> 0x003f }
            java.lang.Object r4 = r4.fromJson((java.io.Reader) r3, (java.lang.reflect.Type) r8)     // Catch:{ IOException -> 0x0045, JsonParseException -> 0x0042, all -> 0x003f }
            if (r3 == 0) goto L_0x0025
            r3.close()     // Catch:{ IOException -> 0x003b }
        L_0x0025:
            return r4
        L_0x0026:
            r1 = move-exception
        L_0x0027:
            io.intercom.retrofit.converter.ConversionException r4 = new io.intercom.retrofit.converter.ConversionException     // Catch:{ all -> 0x002d }
            r4.<init>((java.lang.Throwable) r1)     // Catch:{ all -> 0x002d }
            throw r4     // Catch:{ all -> 0x002d }
        L_0x002d:
            r4 = move-exception
        L_0x002e:
            if (r2 == 0) goto L_0x0033
            r2.close()     // Catch:{ IOException -> 0x003d }
        L_0x0033:
            throw r4
        L_0x0034:
            r1 = move-exception
        L_0x0035:
            io.intercom.retrofit.converter.ConversionException r4 = new io.intercom.retrofit.converter.ConversionException     // Catch:{ all -> 0x002d }
            r4.<init>((java.lang.Throwable) r1)     // Catch:{ all -> 0x002d }
            throw r4     // Catch:{ all -> 0x002d }
        L_0x003b:
            r5 = move-exception
            goto L_0x0025
        L_0x003d:
            r5 = move-exception
            goto L_0x0033
        L_0x003f:
            r4 = move-exception
            r2 = r3
            goto L_0x002e
        L_0x0042:
            r1 = move-exception
            r2 = r3
            goto L_0x0035
        L_0x0045:
            r1 = move-exception
            r2 = r3
            goto L_0x0027
        */
        throw new UnsupportedOperationException("Method not decompiled: io.intercom.retrofit.converter.GsonConverter.fromBody(io.intercom.retrofit.mime.TypedInput, java.lang.reflect.Type):java.lang.Object");
    }

    public TypedOutput toBody(Object object) {
        try {
            return new JsonTypedOutput(this.gson.toJson(object).getBytes(this.charset), this.charset);
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    private static class JsonTypedOutput implements TypedOutput {
        private final byte[] jsonBytes;
        private final String mimeType;

        JsonTypedOutput(byte[] jsonBytes2, String encode) {
            this.jsonBytes = jsonBytes2;
            this.mimeType = "application/json; charset=" + encode;
        }

        public String fileName() {
            return null;
        }

        public String mimeType() {
            return this.mimeType;
        }

        public long length() {
            return (long) this.jsonBytes.length;
        }

        public void writeTo(OutputStream out) throws IOException {
            out.write(this.jsonBytes);
        }
    }
}
