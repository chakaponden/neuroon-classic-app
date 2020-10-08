package com.google.android.gms.tagmanager;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

class zzcu extends zzct {
    /* access modifiers changed from: private */
    public static final Object zzbkP = new Object();
    private static zzcu zzbla;
    /* access modifiers changed from: private */
    public boolean connected = true;
    /* access modifiers changed from: private */
    public Handler handler;
    private Context zzbkQ;
    /* access modifiers changed from: private */
    public zzau zzbkR;
    private volatile zzas zzbkS;
    /* access modifiers changed from: private */
    public int zzbkT = 1800000;
    private boolean zzbkU = true;
    private boolean zzbkV = false;
    private boolean zzbkW = true;
    private zzav zzbkX = new zzav() {
        public void zzax(boolean z) {
            zzcu.this.zzd(z, zzcu.this.connected);
        }
    };
    private zzbl zzbkY;
    /* access modifiers changed from: private */
    public boolean zzbkZ = false;

    private zzcu() {
    }

    public static zzcu zzHo() {
        if (zzbla == null) {
            zzbla = new zzcu();
        }
        return zzbla;
    }

    private void zzHp() {
        this.zzbkY = new zzbl(this);
        this.zzbkY.zzba(this.zzbkQ);
    }

    private void zzHq() {
        this.handler = new Handler(this.zzbkQ.getMainLooper(), new Handler.Callback() {
            public boolean handleMessage(Message msg) {
                if (1 == msg.what && zzcu.zzbkP.equals(msg.obj)) {
                    zzcu.this.dispatch();
                    if (zzcu.this.zzbkT > 0 && !zzcu.this.zzbkZ) {
                        zzcu.this.handler.sendMessageDelayed(zzcu.this.handler.obtainMessage(1, zzcu.zzbkP), (long) zzcu.this.zzbkT);
                    }
                }
                return true;
            }
        });
        if (this.zzbkT > 0) {
            this.handler.sendMessageDelayed(this.handler.obtainMessage(1, zzbkP), (long) this.zzbkT);
        }
    }

    public synchronized void dispatch() {
        if (!this.zzbkV) {
            zzbg.v("Dispatch call queued. Dispatch will run once initialization is complete.");
            this.zzbkU = true;
        } else {
            this.zzbkS.zzj(new Runnable() {
                public void run() {
                    zzcu.this.zzbkR.dispatch();
                }
            });
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized zzau zzHr() {
        if (this.zzbkR == null) {
            if (this.zzbkQ == null) {
                throw new IllegalStateException("Cant get a store unless we have a context");
            }
            this.zzbkR = new zzby(this.zzbkX, this.zzbkQ);
        }
        if (this.handler == null) {
            zzHq();
        }
        this.zzbkV = true;
        if (this.zzbkU) {
            dispatch();
            this.zzbkU = false;
        }
        if (this.zzbkY == null && this.zzbkW) {
            zzHp();
        }
        return this.zzbkR;
    }

    /* access modifiers changed from: package-private */
    public synchronized void zza(Context context, zzas zzas) {
        if (this.zzbkQ == null) {
            this.zzbkQ = context.getApplicationContext();
            if (this.zzbkS == null) {
                this.zzbkS = zzas;
            }
        }
    }

    public synchronized void zzay(boolean z) {
        zzd(this.zzbkZ, z);
    }

    /* access modifiers changed from: package-private */
    public synchronized void zzd(boolean z, boolean z2) {
        if (!(this.zzbkZ == z && this.connected == z2)) {
            if (z || !z2) {
                if (this.zzbkT > 0) {
                    this.handler.removeMessages(1, zzbkP);
                }
            }
            if (!z && z2 && this.zzbkT > 0) {
                this.handler.sendMessageDelayed(this.handler.obtainMessage(1, zzbkP), (long) this.zzbkT);
            }
            zzbg.v("PowerSaveMode " + ((z || !z2) ? "initiated." : "terminated."));
            this.zzbkZ = z;
            this.connected = z2;
        }
    }

    public synchronized void zzjg() {
        if (!this.zzbkZ && this.connected && this.zzbkT > 0) {
            this.handler.removeMessages(1, zzbkP);
            this.handler.sendMessage(this.handler.obtainMessage(1, zzbkP));
        }
    }
}
