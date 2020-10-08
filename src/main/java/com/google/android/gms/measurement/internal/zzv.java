package com.google.android.gms.measurement.internal;

import android.content.Context;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzmq;
import java.lang.Thread;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

public class zzv extends zzz {
    /* access modifiers changed from: private */
    public zzc zzaXI;
    /* access modifiers changed from: private */
    public zzc zzaXJ;
    private final BlockingQueue<FutureTask<?>> zzaXK = new LinkedBlockingQueue();
    private final BlockingQueue<FutureTask<?>> zzaXL = new LinkedBlockingQueue();
    private final Thread.UncaughtExceptionHandler zzaXM = new zzb("Thread death: Uncaught exception on worker thread");
    private final Thread.UncaughtExceptionHandler zzaXN = new zzb("Thread death: Uncaught exception on network thread");
    /* access modifiers changed from: private */
    public final Object zzaXO = new Object();
    /* access modifiers changed from: private */
    public final Semaphore zzaXP = new Semaphore(2);
    /* access modifiers changed from: private */
    public volatile boolean zzaXQ;

    private final class zza<V> extends FutureTask<V> {
        private final String zzaXR;

        zza(Runnable runnable, String str) {
            super(runnable, (Object) null);
            zzx.zzz(str);
            this.zzaXR = str;
        }

        zza(Callable<V> callable, String str) {
            super(callable);
            zzx.zzz(str);
            this.zzaXR = str;
        }

        /* access modifiers changed from: protected */
        public void setException(Throwable error) {
            zzv.this.zzAo().zzCE().zzj(this.zzaXR, error);
            super.setException(error);
        }
    }

    private final class zzb implements Thread.UncaughtExceptionHandler {
        private final String zzaXR;

        public zzb(String str) {
            zzx.zzz(str);
            this.zzaXR = str;
        }

        public synchronized void uncaughtException(Thread thread, Throwable error) {
            zzv.this.zzAo().zzCE().zzj(this.zzaXR, error);
        }
    }

    private final class zzc extends Thread {
        private final Object zzaXT = new Object();
        private final BlockingQueue<FutureTask<?>> zzaXU;

        public zzc(String str, BlockingQueue<FutureTask<?>> blockingQueue) {
            zzx.zzz(str);
            this.zzaXU = blockingQueue;
            setName(str);
        }

        private void zza(InterruptedException interruptedException) {
            zzv.this.zzAo().zzCF().zzj(getName() + " was interrupted", interruptedException);
        }

        /* JADX WARNING: Code restructure failed: missing block: B:39:0x0078, code lost:
            r1 = com.google.android.gms.measurement.internal.zzv.zzc(r4.zzaXS);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:0x007e, code lost:
            monitor-enter(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:42:?, code lost:
            com.google.android.gms.measurement.internal.zzv.zza(r4.zzaXS).release();
            com.google.android.gms.measurement.internal.zzv.zzc(r4.zzaXS).notifyAll();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:43:0x0097, code lost:
            if (r4 != com.google.android.gms.measurement.internal.zzv.zzd(r4.zzaXS)) goto L_0x00a9;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:0x0099, code lost:
            com.google.android.gms.measurement.internal.zzv.zza(r4.zzaXS, (com.google.android.gms.measurement.internal.zzv.zzc) null);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x009f, code lost:
            monitor-exit(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:46:0x00a0, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:56:0x00af, code lost:
            if (r4 != com.google.android.gms.measurement.internal.zzv.zze(r4.zzaXS)) goto L_0x00bb;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:57:0x00b1, code lost:
            com.google.android.gms.measurement.internal.zzv.zzb(r4.zzaXS, (com.google.android.gms.measurement.internal.zzv.zzc) null);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:62:?, code lost:
            r4.zzaXS.zzAo().zzCE().zzfg("Current scheduler thread is neither worker nor network");
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
                r4 = this;
                r0 = 0
                r1 = r0
            L_0x0002:
                if (r1 != 0) goto L_0x0015
                com.google.android.gms.measurement.internal.zzv r0 = com.google.android.gms.measurement.internal.zzv.this     // Catch:{ InterruptedException -> 0x0010 }
                java.util.concurrent.Semaphore r0 = r0.zzaXP     // Catch:{ InterruptedException -> 0x0010 }
                r0.acquire()     // Catch:{ InterruptedException -> 0x0010 }
                r0 = 1
                r1 = r0
                goto L_0x0002
            L_0x0010:
                r0 = move-exception
                r4.zza(r0)
                goto L_0x0002
            L_0x0015:
                java.util.concurrent.BlockingQueue<java.util.concurrent.FutureTask<?>> r0 = r4.zzaXU     // Catch:{ all -> 0x0023 }
                java.lang.Object r0 = r0.poll()     // Catch:{ all -> 0x0023 }
                java.util.concurrent.FutureTask r0 = (java.util.concurrent.FutureTask) r0     // Catch:{ all -> 0x0023 }
                if (r0 == 0) goto L_0x004d
                r0.run()     // Catch:{ all -> 0x0023 }
                goto L_0x0015
            L_0x0023:
                r0 = move-exception
                com.google.android.gms.measurement.internal.zzv r1 = com.google.android.gms.measurement.internal.zzv.this
                java.lang.Object r1 = r1.zzaXO
                monitor-enter(r1)
                com.google.android.gms.measurement.internal.zzv r2 = com.google.android.gms.measurement.internal.zzv.this     // Catch:{ all -> 0x00e1 }
                java.util.concurrent.Semaphore r2 = r2.zzaXP     // Catch:{ all -> 0x00e1 }
                r2.release()     // Catch:{ all -> 0x00e1 }
                com.google.android.gms.measurement.internal.zzv r2 = com.google.android.gms.measurement.internal.zzv.this     // Catch:{ all -> 0x00e1 }
                java.lang.Object r2 = r2.zzaXO     // Catch:{ all -> 0x00e1 }
                r2.notifyAll()     // Catch:{ all -> 0x00e1 }
                com.google.android.gms.measurement.internal.zzv r2 = com.google.android.gms.measurement.internal.zzv.this     // Catch:{ all -> 0x00e1 }
                com.google.android.gms.measurement.internal.zzv$zzc r2 = r2.zzaXI     // Catch:{ all -> 0x00e1 }
                if (r4 != r2) goto L_0x00d1
                com.google.android.gms.measurement.internal.zzv r2 = com.google.android.gms.measurement.internal.zzv.this     // Catch:{ all -> 0x00e1 }
                r3 = 0
                com.google.android.gms.measurement.internal.zzv.zzc unused = r2.zzaXI = r3     // Catch:{ all -> 0x00e1 }
            L_0x004b:
                monitor-exit(r1)     // Catch:{ all -> 0x00e1 }
                throw r0
            L_0x004d:
                java.lang.Object r1 = r4.zzaXT     // Catch:{ all -> 0x0023 }
                monitor-enter(r1)     // Catch:{ all -> 0x0023 }
                java.util.concurrent.BlockingQueue<java.util.concurrent.FutureTask<?>> r0 = r4.zzaXU     // Catch:{ all -> 0x00a6 }
                java.lang.Object r0 = r0.peek()     // Catch:{ all -> 0x00a6 }
                if (r0 != 0) goto L_0x0067
                com.google.android.gms.measurement.internal.zzv r0 = com.google.android.gms.measurement.internal.zzv.this     // Catch:{ all -> 0x00a6 }
                boolean r0 = r0.zzaXQ     // Catch:{ all -> 0x00a6 }
                if (r0 != 0) goto L_0x0067
                java.lang.Object r0 = r4.zzaXT     // Catch:{ InterruptedException -> 0x00a1 }
                r2 = 30000(0x7530, double:1.4822E-319)
                r0.wait(r2)     // Catch:{ InterruptedException -> 0x00a1 }
            L_0x0067:
                monitor-exit(r1)     // Catch:{ all -> 0x00a6 }
                com.google.android.gms.measurement.internal.zzv r0 = com.google.android.gms.measurement.internal.zzv.this     // Catch:{ all -> 0x0023 }
                java.lang.Object r1 = r0.zzaXO     // Catch:{ all -> 0x0023 }
                monitor-enter(r1)     // Catch:{ all -> 0x0023 }
                java.util.concurrent.BlockingQueue<java.util.concurrent.FutureTask<?>> r0 = r4.zzaXU     // Catch:{ all -> 0x00ce }
                java.lang.Object r0 = r0.peek()     // Catch:{ all -> 0x00ce }
                if (r0 != 0) goto L_0x00cb
                monitor-exit(r1)     // Catch:{ all -> 0x00ce }
                com.google.android.gms.measurement.internal.zzv r0 = com.google.android.gms.measurement.internal.zzv.this
                java.lang.Object r1 = r0.zzaXO
                monitor-enter(r1)
                com.google.android.gms.measurement.internal.zzv r0 = com.google.android.gms.measurement.internal.zzv.this     // Catch:{ all -> 0x00b8 }
                java.util.concurrent.Semaphore r0 = r0.zzaXP     // Catch:{ all -> 0x00b8 }
                r0.release()     // Catch:{ all -> 0x00b8 }
                com.google.android.gms.measurement.internal.zzv r0 = com.google.android.gms.measurement.internal.zzv.this     // Catch:{ all -> 0x00b8 }
                java.lang.Object r0 = r0.zzaXO     // Catch:{ all -> 0x00b8 }
                r0.notifyAll()     // Catch:{ all -> 0x00b8 }
                com.google.android.gms.measurement.internal.zzv r0 = com.google.android.gms.measurement.internal.zzv.this     // Catch:{ all -> 0x00b8 }
                com.google.android.gms.measurement.internal.zzv$zzc r0 = r0.zzaXI     // Catch:{ all -> 0x00b8 }
                if (r4 != r0) goto L_0x00a9
                com.google.android.gms.measurement.internal.zzv r0 = com.google.android.gms.measurement.internal.zzv.this     // Catch:{ all -> 0x00b8 }
                r2 = 0
                com.google.android.gms.measurement.internal.zzv.zzc unused = r0.zzaXI = r2     // Catch:{ all -> 0x00b8 }
            L_0x009f:
                monitor-exit(r1)     // Catch:{ all -> 0x00b8 }
                return
            L_0x00a1:
                r0 = move-exception
                r4.zza(r0)     // Catch:{ all -> 0x00a6 }
                goto L_0x0067
            L_0x00a6:
                r0 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x00a6 }
                throw r0     // Catch:{ all -> 0x0023 }
            L_0x00a9:
                com.google.android.gms.measurement.internal.zzv r0 = com.google.android.gms.measurement.internal.zzv.this     // Catch:{ all -> 0x00b8 }
                com.google.android.gms.measurement.internal.zzv$zzc r0 = r0.zzaXJ     // Catch:{ all -> 0x00b8 }
                if (r4 != r0) goto L_0x00bb
                com.google.android.gms.measurement.internal.zzv r0 = com.google.android.gms.measurement.internal.zzv.this     // Catch:{ all -> 0x00b8 }
                r2 = 0
                com.google.android.gms.measurement.internal.zzv.zzc unused = r0.zzaXJ = r2     // Catch:{ all -> 0x00b8 }
                goto L_0x009f
            L_0x00b8:
                r0 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x00b8 }
                throw r0
            L_0x00bb:
                com.google.android.gms.measurement.internal.zzv r0 = com.google.android.gms.measurement.internal.zzv.this     // Catch:{ all -> 0x00b8 }
                com.google.android.gms.measurement.internal.zzp r0 = r0.zzAo()     // Catch:{ all -> 0x00b8 }
                com.google.android.gms.measurement.internal.zzp$zza r0 = r0.zzCE()     // Catch:{ all -> 0x00b8 }
                java.lang.String r2 = "Current scheduler thread is neither worker nor network"
                r0.zzfg(r2)     // Catch:{ all -> 0x00b8 }
                goto L_0x009f
            L_0x00cb:
                monitor-exit(r1)     // Catch:{ all -> 0x00ce }
                goto L_0x0015
            L_0x00ce:
                r0 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x00ce }
                throw r0     // Catch:{ all -> 0x0023 }
            L_0x00d1:
                com.google.android.gms.measurement.internal.zzv r2 = com.google.android.gms.measurement.internal.zzv.this     // Catch:{ all -> 0x00e1 }
                com.google.android.gms.measurement.internal.zzv$zzc r2 = r2.zzaXJ     // Catch:{ all -> 0x00e1 }
                if (r4 != r2) goto L_0x00e4
                com.google.android.gms.measurement.internal.zzv r2 = com.google.android.gms.measurement.internal.zzv.this     // Catch:{ all -> 0x00e1 }
                r3 = 0
                com.google.android.gms.measurement.internal.zzv.zzc unused = r2.zzaXJ = r3     // Catch:{ all -> 0x00e1 }
                goto L_0x004b
            L_0x00e1:
                r0 = move-exception
                monitor-exit(r1)     // Catch:{ all -> 0x00e1 }
                throw r0
            L_0x00e4:
                com.google.android.gms.measurement.internal.zzv r2 = com.google.android.gms.measurement.internal.zzv.this     // Catch:{ all -> 0x00e1 }
                com.google.android.gms.measurement.internal.zzp r2 = r2.zzAo()     // Catch:{ all -> 0x00e1 }
                com.google.android.gms.measurement.internal.zzp$zza r2 = r2.zzCE()     // Catch:{ all -> 0x00e1 }
                java.lang.String r3 = "Current scheduler thread is neither worker nor network"
                r2.zzfg(r3)     // Catch:{ all -> 0x00e1 }
                goto L_0x004b
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zzv.zzc.run():void");
        }

        public void zzfb() {
            synchronized (this.zzaXT) {
                this.zzaXT.notifyAll();
            }
        }
    }

    zzv(zzw zzw) {
        super(zzw);
    }

    private void zza(FutureTask<?> futureTask) {
        synchronized (this.zzaXO) {
            this.zzaXK.add(futureTask);
            if (this.zzaXI == null) {
                this.zzaXI = new zzc("Measurement Worker", this.zzaXK);
                this.zzaXI.setUncaughtExceptionHandler(this.zzaXM);
                this.zzaXI.start();
            } else {
                this.zzaXI.zzfb();
            }
        }
    }

    private void zzb(FutureTask<?> futureTask) {
        synchronized (this.zzaXO) {
            this.zzaXL.add(futureTask);
            if (this.zzaXJ == null) {
                this.zzaXJ = new zzc("Measurement Network", this.zzaXL);
                this.zzaXJ.setUncaughtExceptionHandler(this.zzaXN);
                this.zzaXJ.start();
            } else {
                this.zzaXJ.zzfb();
            }
        }
    }

    public /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    public /* bridge */ /* synthetic */ zzp zzAo() {
        return super.zzAo();
    }

    public void zzCd() {
        if (Thread.currentThread() != this.zzaXJ) {
            throw new IllegalStateException("Call expected from network thread");
        }
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

    public <V> Future<V> zzd(Callable<V> callable) throws IllegalStateException {
        zzjv();
        zzx.zzz(callable);
        zza zza2 = new zza(callable, "Task exception on worker thread");
        if (Thread.currentThread() == this.zzaXI) {
            zza2.run();
        } else {
            zza((FutureTask<?>) zza2);
        }
        return zza2;
    }

    public void zzg(Runnable runnable) throws IllegalStateException {
        zzjv();
        zzx.zzz(runnable);
        zza((FutureTask<?>) new zza(runnable, "Task exception on worker thread"));
    }

    public void zzh(Runnable runnable) throws IllegalStateException {
        zzjv();
        zzx.zzz(runnable);
        zzb((FutureTask<?>) new zza(runnable, "Task exception on network thread"));
    }

    /* access modifiers changed from: protected */
    public void zziJ() {
    }

    public /* bridge */ /* synthetic */ void zzjj() {
        super.zzjj();
    }

    public void zzjk() {
        if (Thread.currentThread() != this.zzaXI) {
            throw new IllegalStateException("Call expected from worker thread");
        }
    }

    public /* bridge */ /* synthetic */ zzmq zzjl() {
        return super.zzjl();
    }
}
