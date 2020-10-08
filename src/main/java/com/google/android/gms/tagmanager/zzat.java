package com.google.android.gms.tagmanager;

import android.content.Context;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.LinkedBlockingQueue;

class zzat extends Thread implements zzas {
    private static zzat zzbjb;
    private volatile boolean mClosed = false;
    /* access modifiers changed from: private */
    public final Context mContext;
    private volatile boolean zzRE = false;
    private final LinkedBlockingQueue<Runnable> zzbja = new LinkedBlockingQueue<>();
    /* access modifiers changed from: private */
    public volatile zzau zzbjc;

    private zzat(Context context) {
        super("GAThread");
        if (context != null) {
            this.mContext = context.getApplicationContext();
        } else {
            this.mContext = context;
        }
        start();
    }

    static zzat zzaZ(Context context) {
        if (zzbjb == null) {
            zzbjb = new zzat(context);
        }
        return zzbjb;
    }

    private String zzd(Throwable th) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);
        th.printStackTrace(printStream);
        printStream.flush();
        return new String(byteArrayOutputStream.toByteArray());
    }

    public void run() {
        while (!this.mClosed) {
            try {
                Runnable take = this.zzbja.take();
                if (!this.zzRE) {
                    take.run();
                }
            } catch (InterruptedException e) {
                zzbg.zzaJ(e.toString());
            } catch (Throwable th) {
                zzbg.e("Error on Google TagManager Thread: " + zzd(th));
                zzbg.e("Google TagManager is shutting down.");
                this.zzRE = true;
            }
        }
    }

    public void zzgg(String str) {
        zzk(str, System.currentTimeMillis());
    }

    public void zzj(Runnable runnable) {
        this.zzbja.add(runnable);
    }

    /* access modifiers changed from: package-private */
    public void zzk(String str, long j) {
        final long j2 = j;
        final String str2 = str;
        zzj(new Runnable() {
            public void run() {
                if (zzat.this.zzbjc == null) {
                    zzcu zzHo = zzcu.zzHo();
                    zzHo.zza(zzat.this.mContext, this);
                    zzau unused = zzat.this.zzbjc = zzHo.zzHr();
                }
                zzat.this.zzbjc.zzg(j2, str2);
            }
        });
    }
}
