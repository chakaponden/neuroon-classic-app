package com.google.android.gms.analytics.internal;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import com.google.android.gms.common.internal.zzx;
import com.raizlabs.android.dbflow.sql.language.Condition;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

class zzah extends zzd {
    /* access modifiers changed from: private */
    public static final byte[] zzTd = "\n".getBytes();
    private final zzaj zzTc;
    private final String zzzN = zza("GoogleAnalytics", zze.VERSION, Build.VERSION.RELEASE, zzam.zza(Locale.getDefault()), Build.MODEL, Build.ID);

    private class zza {
        private int zzTe;
        private ByteArrayOutputStream zzTf = new ByteArrayOutputStream();

        public zza() {
        }

        public byte[] getPayload() {
            return this.zzTf.toByteArray();
        }

        public boolean zzj(zzab zzab) {
            zzx.zzz(zzab);
            if (this.zzTe + 1 > zzah.this.zzjn().zzkD()) {
                return false;
            }
            String zza = zzah.this.zza(zzab, false);
            if (zza == null) {
                zzah.this.zzjm().zza(zzab, "Error formatting hit");
                return true;
            }
            byte[] bytes = zza.getBytes();
            int length = bytes.length;
            if (length > zzah.this.zzjn().zzkv()) {
                zzah.this.zzjm().zza(zzab, "Hit size exceeds the maximum size limit");
                return true;
            }
            if (this.zzTf.size() > 0) {
                length++;
            }
            if (length + this.zzTf.size() > zzah.this.zzjn().zzkx()) {
                return false;
            }
            try {
                if (this.zzTf.size() > 0) {
                    this.zzTf.write(zzah.zzTd);
                }
                this.zzTf.write(bytes);
                this.zzTe++;
                return true;
            } catch (IOException e) {
                zzah.this.zze("Failed to write payload when batching hits", e);
                return true;
            }
        }

        public int zzlE() {
            return this.zzTe;
        }
    }

    zzah(zzf zzf) {
        super(zzf);
        this.zzTc = new zzaj(zzf.zzjl());
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v9, resolved type: java.net.HttpURLConnection} */
    /* JADX WARNING: type inference failed for: r2v2, types: [java.net.HttpURLConnection] */
    /* JADX WARNING: type inference failed for: r2v3 */
    /* JADX WARNING: type inference failed for: r2v4, types: [java.net.HttpURLConnection] */
    /* JADX WARNING: type inference failed for: r2v5 */
    /* JADX WARNING: type inference failed for: r2v8 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x006e A[SYNTHETIC, Splitter:B:26:0x006e] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0073  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0082 A[SYNTHETIC, Splitter:B:35:0x0082] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0087  */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int zza(java.net.URL r6, byte[] r7) {
        /*
            r5 = this;
            r1 = 0
            com.google.android.gms.common.internal.zzx.zzz(r6)
            com.google.android.gms.common.internal.zzx.zzz(r7)
            java.lang.String r0 = "POST bytes, url"
            int r2 = r7.length
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            r5.zzb(r0, r2, r6)
            boolean r0 = r5.zzhp()
            if (r0 == 0) goto L_0x0021
            java.lang.String r0 = "Post payload\n"
            java.lang.String r2 = new java.lang.String
            r2.<init>(r7)
            r5.zza(r0, r2)
        L_0x0021:
            java.net.HttpURLConnection r2 = r5.zzc(r6)     // Catch:{ IOException -> 0x0064, all -> 0x007e }
            r0 = 1
            r2.setDoOutput(r0)     // Catch:{ IOException -> 0x0094 }
            int r0 = r7.length     // Catch:{ IOException -> 0x0094 }
            r2.setFixedLengthStreamingMode(r0)     // Catch:{ IOException -> 0x0094 }
            r2.connect()     // Catch:{ IOException -> 0x0094 }
            java.io.OutputStream r1 = r2.getOutputStream()     // Catch:{ IOException -> 0x0094 }
            r1.write(r7)     // Catch:{ IOException -> 0x0094 }
            r5.zzb((java.net.HttpURLConnection) r2)     // Catch:{ IOException -> 0x0094 }
            int r0 = r2.getResponseCode()     // Catch:{ IOException -> 0x0094 }
            r3 = 200(0xc8, float:2.8E-43)
            if (r0 != r3) goto L_0x0049
            com.google.android.gms.analytics.internal.zzb r3 = r5.zziH()     // Catch:{ IOException -> 0x0094 }
            r3.zzjh()     // Catch:{ IOException -> 0x0094 }
        L_0x0049:
            java.lang.String r3 = "POST status"
            java.lang.Integer r4 = java.lang.Integer.valueOf(r0)     // Catch:{ IOException -> 0x0094 }
            r5.zzb(r3, r4)     // Catch:{ IOException -> 0x0094 }
            if (r1 == 0) goto L_0x0057
            r1.close()     // Catch:{ IOException -> 0x005d }
        L_0x0057:
            if (r2 == 0) goto L_0x005c
            r2.disconnect()
        L_0x005c:
            return r0
        L_0x005d:
            r1 = move-exception
            java.lang.String r3 = "Error closing http post connection output stream"
            r5.zze(r3, r1)
            goto L_0x0057
        L_0x0064:
            r0 = move-exception
            r2 = r1
        L_0x0066:
            java.lang.String r3 = "Network POST connection error"
            r5.zzd(r3, r0)     // Catch:{ all -> 0x0092 }
            r0 = 0
            if (r1 == 0) goto L_0x0071
            r1.close()     // Catch:{ IOException -> 0x0077 }
        L_0x0071:
            if (r2 == 0) goto L_0x005c
            r2.disconnect()
            goto L_0x005c
        L_0x0077:
            r1 = move-exception
            java.lang.String r3 = "Error closing http post connection output stream"
            r5.zze(r3, r1)
            goto L_0x0071
        L_0x007e:
            r0 = move-exception
            r2 = r1
        L_0x0080:
            if (r1 == 0) goto L_0x0085
            r1.close()     // Catch:{ IOException -> 0x008b }
        L_0x0085:
            if (r2 == 0) goto L_0x008a
            r2.disconnect()
        L_0x008a:
            throw r0
        L_0x008b:
            r1 = move-exception
            java.lang.String r3 = "Error closing http post connection output stream"
            r5.zze(r3, r1)
            goto L_0x0085
        L_0x0092:
            r0 = move-exception
            goto L_0x0080
        L_0x0094:
            r0 = move-exception
            goto L_0x0066
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.analytics.internal.zzah.zza(java.net.URL, byte[]):int");
    }

    private static String zza(String str, String str2, String str3, String str4, String str5, String str6) {
        return String.format("%s/%s (Linux; U; Android %s; %s; %s Build/%s)", new Object[]{str, str2, str3, str4, str5, str6});
    }

    private void zza(StringBuilder sb, String str, String str2) throws UnsupportedEncodingException {
        if (sb.length() != 0) {
            sb.append('&');
        }
        sb.append(URLEncoder.encode(str, HttpRequest.CHARSET_UTF8));
        sb.append('=');
        sb.append(URLEncoder.encode(str2, HttpRequest.CHARSET_UTF8));
    }

    private int zzb(URL url) {
        int i;
        zzx.zzz(url);
        zzb("GET request", url);
        HttpURLConnection httpURLConnection = null;
        try {
            HttpURLConnection zzc = zzc(url);
            zzc.connect();
            zzb(zzc);
            i = zzc.getResponseCode();
            if (i == 200) {
                zziH().zzjh();
            }
            zzb("GET status", Integer.valueOf(i));
            if (zzc != null) {
                zzc.disconnect();
            }
        } catch (IOException e) {
            zzd("Network GET connection error", e);
            i = 0;
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        } catch (Throwable th) {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            throw th;
        }
        return i;
    }

    /* JADX WARNING: Removed duplicated region for block: B:35:0x00af A[SYNTHETIC, Splitter:B:35:0x00af] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00b4  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00c3 A[SYNTHETIC, Splitter:B:44:0x00c3] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00c8  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int zzb(java.net.URL r9, byte[] r10) {
        /*
            r8 = this;
            r1 = 0
            com.google.android.gms.common.internal.zzx.zzz(r9)
            com.google.android.gms.common.internal.zzx.zzz(r10)
            byte[] r0 = zzg((byte[]) r10)     // Catch:{ IOException -> 0x00a5, all -> 0x00bf }
            java.lang.String r2 = "POST compressed size, ratio %, url"
            int r3 = r0.length     // Catch:{ IOException -> 0x00a5, all -> 0x00bf }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ IOException -> 0x00a5, all -> 0x00bf }
            r4 = 100
            int r6 = r0.length     // Catch:{ IOException -> 0x00a5, all -> 0x00bf }
            long r6 = (long) r6     // Catch:{ IOException -> 0x00a5, all -> 0x00bf }
            long r4 = r4 * r6
            int r6 = r10.length     // Catch:{ IOException -> 0x00a5, all -> 0x00bf }
            long r6 = (long) r6     // Catch:{ IOException -> 0x00a5, all -> 0x00bf }
            long r4 = r4 / r6
            java.lang.Long r4 = java.lang.Long.valueOf(r4)     // Catch:{ IOException -> 0x00a5, all -> 0x00bf }
            r8.zza(r2, r3, r4, r9)     // Catch:{ IOException -> 0x00a5, all -> 0x00bf }
            int r2 = r0.length     // Catch:{ IOException -> 0x00a5, all -> 0x00bf }
            int r3 = r10.length     // Catch:{ IOException -> 0x00a5, all -> 0x00bf }
            if (r2 <= r3) goto L_0x0034
            java.lang.String r2 = "Compressed payload is larger then uncompressed. compressed, uncompressed"
            int r3 = r0.length     // Catch:{ IOException -> 0x00a5, all -> 0x00bf }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ IOException -> 0x00a5, all -> 0x00bf }
            int r4 = r10.length     // Catch:{ IOException -> 0x00a5, all -> 0x00bf }
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch:{ IOException -> 0x00a5, all -> 0x00bf }
            r8.zzc(r2, r3, r4)     // Catch:{ IOException -> 0x00a5, all -> 0x00bf }
        L_0x0034:
            boolean r2 = r8.zzhp()     // Catch:{ IOException -> 0x00a5, all -> 0x00bf }
            if (r2 == 0) goto L_0x0057
            java.lang.String r2 = "Post payload"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00a5, all -> 0x00bf }
            r3.<init>()     // Catch:{ IOException -> 0x00a5, all -> 0x00bf }
            java.lang.String r4 = "\n"
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ IOException -> 0x00a5, all -> 0x00bf }
            java.lang.String r4 = new java.lang.String     // Catch:{ IOException -> 0x00a5, all -> 0x00bf }
            r4.<init>(r10)     // Catch:{ IOException -> 0x00a5, all -> 0x00bf }
            java.lang.StringBuilder r3 = r3.append(r4)     // Catch:{ IOException -> 0x00a5, all -> 0x00bf }
            java.lang.String r3 = r3.toString()     // Catch:{ IOException -> 0x00a5, all -> 0x00bf }
            r8.zza(r2, r3)     // Catch:{ IOException -> 0x00a5, all -> 0x00bf }
        L_0x0057:
            java.net.HttpURLConnection r3 = r8.zzc(r9)     // Catch:{ IOException -> 0x00a5, all -> 0x00bf }
            r2 = 1
            r3.setDoOutput(r2)     // Catch:{ IOException -> 0x00db, all -> 0x00d3 }
            java.lang.String r2 = "Content-Encoding"
            java.lang.String r4 = "gzip"
            r3.addRequestProperty(r2, r4)     // Catch:{ IOException -> 0x00db, all -> 0x00d3 }
            int r2 = r0.length     // Catch:{ IOException -> 0x00db, all -> 0x00d3 }
            r3.setFixedLengthStreamingMode(r2)     // Catch:{ IOException -> 0x00db, all -> 0x00d3 }
            r3.connect()     // Catch:{ IOException -> 0x00db, all -> 0x00d3 }
            java.io.OutputStream r2 = r3.getOutputStream()     // Catch:{ IOException -> 0x00db, all -> 0x00d3 }
            r2.write(r0)     // Catch:{ IOException -> 0x00de, all -> 0x00d5 }
            r2.close()     // Catch:{ IOException -> 0x00de, all -> 0x00d5 }
            r2 = 0
            r8.zzb((java.net.HttpURLConnection) r3)     // Catch:{ IOException -> 0x00db, all -> 0x00d3 }
            int r0 = r3.getResponseCode()     // Catch:{ IOException -> 0x00db, all -> 0x00d3 }
            r4 = 200(0xc8, float:2.8E-43)
            if (r0 != r4) goto L_0x008a
            com.google.android.gms.analytics.internal.zzb r4 = r8.zziH()     // Catch:{ IOException -> 0x00db, all -> 0x00d3 }
            r4.zzjh()     // Catch:{ IOException -> 0x00db, all -> 0x00d3 }
        L_0x008a:
            java.lang.String r4 = "POST status"
            java.lang.Integer r5 = java.lang.Integer.valueOf(r0)     // Catch:{ IOException -> 0x00db, all -> 0x00d3 }
            r8.zzb(r4, r5)     // Catch:{ IOException -> 0x00db, all -> 0x00d3 }
            if (r1 == 0) goto L_0x0098
            r2.close()     // Catch:{ IOException -> 0x009e }
        L_0x0098:
            if (r3 == 0) goto L_0x009d
            r3.disconnect()
        L_0x009d:
            return r0
        L_0x009e:
            r1 = move-exception
            java.lang.String r2 = "Error closing http compressed post connection output stream"
            r8.zze(r2, r1)
            goto L_0x0098
        L_0x00a5:
            r0 = move-exception
            r2 = r1
        L_0x00a7:
            java.lang.String r3 = "Network compressed POST connection error"
            r8.zzd(r3, r0)     // Catch:{ all -> 0x00d8 }
            r0 = 0
            if (r1 == 0) goto L_0x00b2
            r1.close()     // Catch:{ IOException -> 0x00b8 }
        L_0x00b2:
            if (r2 == 0) goto L_0x009d
            r2.disconnect()
            goto L_0x009d
        L_0x00b8:
            r1 = move-exception
            java.lang.String r3 = "Error closing http compressed post connection output stream"
            r8.zze(r3, r1)
            goto L_0x00b2
        L_0x00bf:
            r0 = move-exception
            r3 = r1
        L_0x00c1:
            if (r1 == 0) goto L_0x00c6
            r1.close()     // Catch:{ IOException -> 0x00cc }
        L_0x00c6:
            if (r3 == 0) goto L_0x00cb
            r3.disconnect()
        L_0x00cb:
            throw r0
        L_0x00cc:
            r1 = move-exception
            java.lang.String r2 = "Error closing http compressed post connection output stream"
            r8.zze(r2, r1)
            goto L_0x00c6
        L_0x00d3:
            r0 = move-exception
            goto L_0x00c1
        L_0x00d5:
            r0 = move-exception
            r1 = r2
            goto L_0x00c1
        L_0x00d8:
            r0 = move-exception
            r3 = r2
            goto L_0x00c1
        L_0x00db:
            r0 = move-exception
            r2 = r3
            goto L_0x00a7
        L_0x00de:
            r0 = move-exception
            r1 = r2
            r2 = r3
            goto L_0x00a7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.analytics.internal.zzah.zzb(java.net.URL, byte[]):int");
    }

    private URL zzb(zzab zzab, String str) {
        try {
            return new URL(zzab.zzlt() ? zzjn().zzkF() + zzjn().zzkH() + Condition.Operation.EMPTY_PARAM + str : zzjn().zzkG() + zzjn().zzkH() + Condition.Operation.EMPTY_PARAM + str);
        } catch (MalformedURLException e) {
            zze("Error trying to parse the hardcoded host url", e);
            return null;
        }
    }

    private void zzb(HttpURLConnection httpURLConnection) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = httpURLConnection.getInputStream();
            do {
            } while (inputStream.read(new byte[1024]) > 0);
            if (inputStream != null) {
                try {
                } catch (IOException e) {
                    zze("Error closing http connection input stream", e);
                }
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e2) {
                    zze("Error closing http connection input stream", e2);
                }
            }
        }
    }

    private boolean zzg(zzab zzab) {
        zzx.zzz(zzab);
        String zza2 = zza(zzab, !zzab.zzlt());
        if (zza2 == null) {
            zzjm().zza(zzab, "Error formatting hit for upload");
            return true;
        } else if (zza2.length() <= zzjn().zzku()) {
            URL zzb = zzb(zzab, zza2);
            if (zzb != null) {
                return zzb(zzb) == 200;
            }
            zzbh("Failed to build collect GET endpoint url");
            return false;
        } else {
            String zza3 = zza(zzab, false);
            if (zza3 == null) {
                zzjm().zza(zzab, "Error formatting hit for POST upload");
                return true;
            }
            byte[] bytes = zza3.getBytes();
            if (bytes.length > zzjn().zzkw()) {
                zzjm().zza(zzab, "Hit payload exceeds size limit");
                return true;
            }
            URL zzh = zzh(zzab);
            if (zzh != null) {
                return zza(zzh, bytes) == 200;
            }
            zzbh("Failed to build collect POST endpoint url");
            return false;
        }
    }

    private static byte[] zzg(byte[] bArr) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        gZIPOutputStream.write(bArr);
        gZIPOutputStream.close();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    private URL zzh(zzab zzab) {
        try {
            return new URL(zzab.zzlt() ? zzjn().zzkF() + zzjn().zzkH() : zzjn().zzkG() + zzjn().zzkH());
        } catch (MalformedURLException e) {
            zze("Error trying to parse the hardcoded host url", e);
            return null;
        }
    }

    private String zzi(zzab zzab) {
        return String.valueOf(zzab.zzlq());
    }

    private URL zzlC() {
        try {
            return new URL(zzjn().zzkF() + zzjn().zzkI());
        } catch (MalformedURLException e) {
            zze("Error trying to parse the hardcoded host url", e);
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public String zza(zzab zzab, boolean z) {
        zzx.zzz(zzab);
        StringBuilder sb = new StringBuilder();
        try {
            for (Map.Entry next : zzab.zzn().entrySet()) {
                String str = (String) next.getKey();
                if (!"ht".equals(str) && !"qt".equals(str) && !"AppUID".equals(str) && !"z".equals(str) && !"_gmsv".equals(str)) {
                    zza(sb, str, (String) next.getValue());
                }
            }
            zza(sb, "ht", String.valueOf(zzab.zzlr()));
            zza(sb, "qt", String.valueOf(zzjl().currentTimeMillis() - zzab.zzlr()));
            if (zzjn().zzkr()) {
                zza(sb, "_gmsv", zze.VERSION);
            }
            if (z) {
                long zzlu = zzab.zzlu();
                zza(sb, "z", zzlu != 0 ? String.valueOf(zzlu) : zzi(zzab));
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            zze("Failed to encode name or value", e);
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public List<Long> zza(List<zzab> list, boolean z) {
        zzx.zzac(!list.isEmpty());
        zza("Uploading batched hits. compression, count", Boolean.valueOf(z), Integer.valueOf(list.size()));
        zza zza2 = new zza();
        ArrayList arrayList = new ArrayList();
        for (zzab next : list) {
            if (!zza2.zzj(next)) {
                break;
            }
            arrayList.add(Long.valueOf(next.zzlq()));
        }
        if (zza2.zzlE() == 0) {
            return arrayList;
        }
        URL zzlC = zzlC();
        if (zzlC == null) {
            zzbh("Failed to build batching endpoint url");
            return Collections.emptyList();
        }
        int zzb = z ? zzb(zzlC, zza2.getPayload()) : zza(zzlC, zza2.getPayload());
        if (200 == zzb) {
            zza("Batched upload completed. Hits batched", Integer.valueOf(zza2.zzlE()));
            return arrayList;
        }
        zza("Network error uploading hits. status code", Integer.valueOf(zzb));
        if (zzjn().zzkL().contains(Integer.valueOf(zzb))) {
            zzbg("Server instructed the client to stop batching");
            this.zzTc.start();
        }
        return Collections.emptyList();
    }

    /* access modifiers changed from: package-private */
    public HttpURLConnection zzc(URL url) throws IOException {
        URLConnection openConnection = url.openConnection();
        if (!(openConnection instanceof HttpURLConnection)) {
            throw new IOException("Failed to obtain http connection");
        }
        HttpURLConnection httpURLConnection = (HttpURLConnection) openConnection;
        httpURLConnection.setDefaultUseCaches(false);
        httpURLConnection.setConnectTimeout(zzjn().zzkU());
        httpURLConnection.setReadTimeout(zzjn().zzkV());
        httpURLConnection.setInstanceFollowRedirects(false);
        httpURLConnection.setRequestProperty("User-Agent", this.zzzN);
        httpURLConnection.setDoInput(true);
        return httpURLConnection;
    }

    /* access modifiers changed from: protected */
    public void zziJ() {
        zza("Network initialized. User agent", this.zzzN);
    }

    public boolean zzlB() {
        NetworkInfo networkInfo;
        zzjk();
        zzjv();
        try {
            networkInfo = ((ConnectivityManager) getContext().getSystemService("connectivity")).getActiveNetworkInfo();
        } catch (SecurityException e) {
            networkInfo = null;
        }
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        zzbd("No network connectivity");
        return false;
    }

    public List<Long> zzq(List<zzab> list) {
        boolean z;
        boolean z2 = true;
        zzjk();
        zzjv();
        zzx.zzz(list);
        if (zzjn().zzkL().isEmpty() || !this.zzTc.zzv(zzjn().zzkE() * 1000)) {
            z2 = false;
            z = false;
        } else {
            z = zzjn().zzkJ() != zzm.zzRk;
            if (zzjn().zzkK() != zzo.GZIP) {
                z2 = false;
            }
        }
        return z ? zza(list, z2) : zzr(list);
    }

    /* access modifiers changed from: package-private */
    public List<Long> zzr(List<zzab> list) {
        ArrayList arrayList = new ArrayList(list.size());
        for (zzab next : list) {
            if (zzg(next)) {
                arrayList.add(Long.valueOf(next.zzlq()));
                if (arrayList.size() >= zzjn().zzkC()) {
                    break;
                }
            } else {
                break;
            }
        }
        return arrayList;
    }
}
