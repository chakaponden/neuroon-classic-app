package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.WorkerThread;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzmq;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class zzq extends zzz {

    @WorkerThread
    interface zza {
        void zza(String str, int i, Throwable th, byte[] bArr);
    }

    @WorkerThread
    private static class zzb implements Runnable {
        private final int zzBc;
        private final String zzTJ;
        private final zza zzaWP;
        private final Throwable zzaWQ;
        private final byte[] zzaWR;

        private zzb(String str, zza zza, int i, Throwable th, byte[] bArr) {
            zzx.zzz(zza);
            this.zzaWP = zza;
            this.zzBc = i;
            this.zzaWQ = th;
            this.zzaWR = bArr;
            this.zzTJ = str;
        }

        public void run() {
            this.zzaWP.zza(this.zzTJ, this.zzBc, this.zzaWQ, this.zzaWR);
        }
    }

    @WorkerThread
    private class zzc implements Runnable {
        private final String zzTJ;
        private final byte[] zzaWS;
        private final zza zzaWT;
        private final Map<String, String> zzaWU;
        private final URL zzzq;

        public zzc(String str, URL url, byte[] bArr, Map<String, String> map, zza zza) {
            zzx.zzcM(str);
            zzx.zzz(url);
            zzx.zzz(zza);
            this.zzzq = url;
            this.zzaWS = bArr;
            this.zzaWT = zza;
            this.zzTJ = str;
            this.zzaWU = map;
        }

        /* JADX WARNING: Removed duplicated region for block: B:40:0x00ee A[SYNTHETIC, Splitter:B:40:0x00ee] */
        /* JADX WARNING: Removed duplicated region for block: B:43:0x00f3  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
                r12 = this;
                r4 = 0
                com.google.android.gms.measurement.internal.zzq r0 = com.google.android.gms.measurement.internal.zzq.this
                r0.zzCd()
                r3 = 0
                com.google.android.gms.measurement.internal.zzq r0 = com.google.android.gms.measurement.internal.zzq.this     // Catch:{ IOException -> 0x0123, all -> 0x00e8 }
                java.net.URL r1 = r12.zzzq     // Catch:{ IOException -> 0x0123, all -> 0x00e8 }
                java.net.HttpURLConnection r2 = r0.zzc((java.net.URL) r1)     // Catch:{ IOException -> 0x0123, all -> 0x00e8 }
                java.util.Map<java.lang.String, java.lang.String> r0 = r12.zzaWU     // Catch:{ IOException -> 0x0039, all -> 0x011c }
                if (r0 == 0) goto L_0x005c
                java.util.Map<java.lang.String, java.lang.String> r0 = r12.zzaWU     // Catch:{ IOException -> 0x0039, all -> 0x011c }
                java.util.Set r0 = r0.entrySet()     // Catch:{ IOException -> 0x0039, all -> 0x011c }
                java.util.Iterator r5 = r0.iterator()     // Catch:{ IOException -> 0x0039, all -> 0x011c }
            L_0x001d:
                boolean r0 = r5.hasNext()     // Catch:{ IOException -> 0x0039, all -> 0x011c }
                if (r0 == 0) goto L_0x005c
                java.lang.Object r0 = r5.next()     // Catch:{ IOException -> 0x0039, all -> 0x011c }
                java.util.Map$Entry r0 = (java.util.Map.Entry) r0     // Catch:{ IOException -> 0x0039, all -> 0x011c }
                java.lang.Object r1 = r0.getKey()     // Catch:{ IOException -> 0x0039, all -> 0x011c }
                java.lang.String r1 = (java.lang.String) r1     // Catch:{ IOException -> 0x0039, all -> 0x011c }
                java.lang.Object r0 = r0.getValue()     // Catch:{ IOException -> 0x0039, all -> 0x011c }
                java.lang.String r0 = (java.lang.String) r0     // Catch:{ IOException -> 0x0039, all -> 0x011c }
                r2.addRequestProperty(r1, r0)     // Catch:{ IOException -> 0x0039, all -> 0x011c }
                goto L_0x001d
            L_0x0039:
                r9 = move-exception
                r8 = r3
                r0 = r4
                r1 = r2
            L_0x003d:
                if (r0 == 0) goto L_0x0042
                r0.close()     // Catch:{ IOException -> 0x00d6 }
            L_0x0042:
                if (r1 == 0) goto L_0x0047
                r1.disconnect()
            L_0x0047:
                com.google.android.gms.measurement.internal.zzq r0 = com.google.android.gms.measurement.internal.zzq.this
                com.google.android.gms.measurement.internal.zzv r0 = r0.zzCn()
                com.google.android.gms.measurement.internal.zzq$zzb r5 = new com.google.android.gms.measurement.internal.zzq$zzb
                java.lang.String r6 = r12.zzTJ
                com.google.android.gms.measurement.internal.zzq$zza r7 = r12.zzaWT
                r10 = r4
                r11 = r4
                r5.<init>(r6, r7, r8, r9, r10)
                r0.zzg(r5)
            L_0x005b:
                return
            L_0x005c:
                byte[] r0 = r12.zzaWS     // Catch:{ IOException -> 0x0039, all -> 0x011c }
                if (r0 == 0) goto L_0x012e
                com.google.android.gms.measurement.internal.zzq r0 = com.google.android.gms.measurement.internal.zzq.this     // Catch:{ IOException -> 0x0039, all -> 0x011c }
                com.google.android.gms.measurement.internal.zzaj r0 = r0.zzCk()     // Catch:{ IOException -> 0x0039, all -> 0x011c }
                byte[] r1 = r12.zzaWS     // Catch:{ IOException -> 0x0039, all -> 0x011c }
                byte[] r1 = r0.zzg(r1)     // Catch:{ IOException -> 0x0039, all -> 0x011c }
                com.google.android.gms.measurement.internal.zzq r0 = com.google.android.gms.measurement.internal.zzq.this     // Catch:{ IOException -> 0x0039, all -> 0x011c }
                com.google.android.gms.measurement.internal.zzp r0 = r0.zzAo()     // Catch:{ IOException -> 0x0039, all -> 0x011c }
                com.google.android.gms.measurement.internal.zzp$zza r0 = r0.zzCK()     // Catch:{ IOException -> 0x0039, all -> 0x011c }
                java.lang.String r5 = "Uploading data. size"
                int r6 = r1.length     // Catch:{ IOException -> 0x0039, all -> 0x011c }
                java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch:{ IOException -> 0x0039, all -> 0x011c }
                r0.zzj(r5, r6)     // Catch:{ IOException -> 0x0039, all -> 0x011c }
                r0 = 1
                r2.setDoOutput(r0)     // Catch:{ IOException -> 0x0039, all -> 0x011c }
                java.lang.String r0 = "Content-Encoding"
                java.lang.String r5 = "gzip"
                r2.addRequestProperty(r0, r5)     // Catch:{ IOException -> 0x0039, all -> 0x011c }
                int r0 = r1.length     // Catch:{ IOException -> 0x0039, all -> 0x011c }
                r2.setFixedLengthStreamingMode(r0)     // Catch:{ IOException -> 0x0039, all -> 0x011c }
                r2.connect()     // Catch:{ IOException -> 0x0039, all -> 0x011c }
                java.io.OutputStream r0 = r2.getOutputStream()     // Catch:{ IOException -> 0x0039, all -> 0x011c }
                r0.write(r1)     // Catch:{ IOException -> 0x0129, all -> 0x0120 }
                r0.close()     // Catch:{ IOException -> 0x0129, all -> 0x0120 }
                r0 = r4
            L_0x009d:
                int r3 = r2.getResponseCode()     // Catch:{ IOException -> 0x0039, all -> 0x011c }
                com.google.android.gms.measurement.internal.zzq r1 = com.google.android.gms.measurement.internal.zzq.this     // Catch:{ IOException -> 0x0039, all -> 0x011c }
                byte[] r5 = r1.zzc((java.net.HttpURLConnection) r2)     // Catch:{ IOException -> 0x0039, all -> 0x011c }
                if (r4 == 0) goto L_0x00ac
                r0.close()     // Catch:{ IOException -> 0x00c5 }
            L_0x00ac:
                if (r2 == 0) goto L_0x00b1
                r2.disconnect()
            L_0x00b1:
                com.google.android.gms.measurement.internal.zzq r0 = com.google.android.gms.measurement.internal.zzq.this
                com.google.android.gms.measurement.internal.zzv r7 = r0.zzCn()
                com.google.android.gms.measurement.internal.zzq$zzb r0 = new com.google.android.gms.measurement.internal.zzq$zzb
                java.lang.String r1 = r12.zzTJ
                com.google.android.gms.measurement.internal.zzq$zza r2 = r12.zzaWT
                r6 = r4
                r0.<init>(r1, r2, r3, r4, r5)
                r7.zzg(r0)
                goto L_0x005b
            L_0x00c5:
                r0 = move-exception
                com.google.android.gms.measurement.internal.zzq r1 = com.google.android.gms.measurement.internal.zzq.this
                com.google.android.gms.measurement.internal.zzp r1 = r1.zzAo()
                com.google.android.gms.measurement.internal.zzp$zza r1 = r1.zzCE()
                java.lang.String r6 = "Error closing HTTP compressed POST connection output stream"
                r1.zzj(r6, r0)
                goto L_0x00ac
            L_0x00d6:
                r0 = move-exception
                com.google.android.gms.measurement.internal.zzq r2 = com.google.android.gms.measurement.internal.zzq.this
                com.google.android.gms.measurement.internal.zzp r2 = r2.zzAo()
                com.google.android.gms.measurement.internal.zzp$zza r2 = r2.zzCE()
                java.lang.String r3 = "Error closing HTTP compressed POST connection output stream"
                r2.zzj(r3, r0)
                goto L_0x0042
            L_0x00e8:
                r0 = move-exception
                r7 = r0
                r2 = r4
                r0 = r4
            L_0x00ec:
                if (r0 == 0) goto L_0x00f1
                r0.close()     // Catch:{ IOException -> 0x010b }
            L_0x00f1:
                if (r2 == 0) goto L_0x00f6
                r2.disconnect()
            L_0x00f6:
                com.google.android.gms.measurement.internal.zzq r0 = com.google.android.gms.measurement.internal.zzq.this
                com.google.android.gms.measurement.internal.zzv r8 = r0.zzCn()
                com.google.android.gms.measurement.internal.zzq$zzb r0 = new com.google.android.gms.measurement.internal.zzq$zzb
                java.lang.String r1 = r12.zzTJ
                com.google.android.gms.measurement.internal.zzq$zza r2 = r12.zzaWT
                r5 = r4
                r6 = r4
                r0.<init>(r1, r2, r3, r4, r5)
                r8.zzg(r0)
                throw r7
            L_0x010b:
                r0 = move-exception
                com.google.android.gms.measurement.internal.zzq r1 = com.google.android.gms.measurement.internal.zzq.this
                com.google.android.gms.measurement.internal.zzp r1 = r1.zzAo()
                com.google.android.gms.measurement.internal.zzp$zza r1 = r1.zzCE()
                java.lang.String r5 = "Error closing HTTP compressed POST connection output stream"
                r1.zzj(r5, r0)
                goto L_0x00f1
            L_0x011c:
                r0 = move-exception
                r7 = r0
                r0 = r4
                goto L_0x00ec
            L_0x0120:
                r1 = move-exception
                r7 = r1
                goto L_0x00ec
            L_0x0123:
                r9 = move-exception
                r8 = r3
                r0 = r4
                r1 = r4
                goto L_0x003d
            L_0x0129:
                r9 = move-exception
                r8 = r3
                r1 = r2
                goto L_0x003d
            L_0x012e:
                r0 = r4
                goto L_0x009d
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzq.zzc.run():void");
        }
    }

    public zzq(zzw zzw) {
        super(zzw);
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public byte[] zzc(HttpURLConnection httpURLConnection) throws IOException {
        InputStream inputStream = null;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            inputStream = httpURLConnection.getInputStream();
            byte[] bArr = new byte[1024];
            while (true) {
                int read = inputStream.read(bArr);
                if (read <= 0) {
                    break;
                }
                byteArrayOutputStream.write(bArr, 0, read);
            }
            return byteArrayOutputStream.toByteArray();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public /* bridge */ /* synthetic */ zzp zzAo() {
        return super.zzAo();
    }

    public /* bridge */ /* synthetic */ void zzCd() {
        super.zzCd();
    }

    public /* bridge */ /* synthetic */ zzc zzCe() {
        return super.zzCe();
    }

    public /* bridge */ /* synthetic */ zzab zzCf() {
        return super.zzCf();
    }

    public /* bridge */ /* synthetic */ zzn zzCg() {
        return super.zzCg();
    }

    public /* bridge */ /* synthetic */ zzg zzCh() {
        return super.zzCh();
    }

    public /* bridge */ /* synthetic */ zzac zzCi() {
        return super.zzCi();
    }

    public /* bridge */ /* synthetic */ zze zzCj() {
        return super.zzCj();
    }

    public /* bridge */ /* synthetic */ zzaj zzCk() {
        return super.zzCk();
    }

    public /* bridge */ /* synthetic */ zzu zzCl() {
        return super.zzCl();
    }

    public /* bridge */ /* synthetic */ zzad zzCm() {
        return super.zzCm();
    }

    public /* bridge */ /* synthetic */ zzv zzCn() {
        return super.zzCn();
    }

    public /* bridge */ /* synthetic */ zzt zzCo() {
        return super.zzCo();
    }

    public /* bridge */ /* synthetic */ zzd zzCp() {
        return super.zzCp();
    }

    @WorkerThread
    public void zza(String str, URL url, Map<String, String> map, zza zza2) {
        zzjk();
        zzjv();
        zzx.zzz(url);
        zzx.zzz(zza2);
        zzCn().zzh(new zzc(str, url, (byte[]) null, map, zza2));
    }

    @WorkerThread
    public void zza(String str, URL url, byte[] bArr, Map<String, String> map, zza zza2) {
        zzjk();
        zzjv();
        zzx.zzz(url);
        zzx.zzz(bArr);
        zzx.zzz(zza2);
        zzCn().zzh(new zzc(str, url, bArr, map, zza2));
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public HttpURLConnection zzc(URL url) throws IOException {
        URLConnection openConnection = url.openConnection();
        if (!(openConnection instanceof HttpURLConnection)) {
            throw new IOException("Failed to obtain HTTP connection");
        }
        HttpURLConnection httpURLConnection = (HttpURLConnection) openConnection;
        httpURLConnection.setDefaultUseCaches(false);
        httpURLConnection.setConnectTimeout((int) zzCp().zzBO());
        httpURLConnection.setReadTimeout((int) zzCp().zzBP());
        httpURLConnection.setInstanceFollowRedirects(false);
        httpURLConnection.setDoInput(true);
        return httpURLConnection;
    }

    /* access modifiers changed from: protected */
    public void zziJ() {
    }

    public /* bridge */ /* synthetic */ void zzjj() {
        super.zzjj();
    }

    public /* bridge */ /* synthetic */ void zzjk() {
        super.zzjk();
    }

    public /* bridge */ /* synthetic */ zzmq zzjl() {
        return super.zzjl();
    }

    public boolean zzlB() {
        NetworkInfo networkInfo;
        zzjv();
        try {
            networkInfo = ((ConnectivityManager) getContext().getSystemService("connectivity")).getActiveNetworkInfo();
        } catch (SecurityException e) {
            networkInfo = null;
        }
        return networkInfo != null && networkInfo.isConnected();
    }
}
