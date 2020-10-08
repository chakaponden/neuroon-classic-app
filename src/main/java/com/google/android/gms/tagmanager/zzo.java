package com.google.android.gms.tagmanager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tagmanager.ContainerHolder;

class zzo implements ContainerHolder {
    private Status zzUX;
    private final Looper zzagr;
    private boolean zzapK;
    private Container zzbhU;
    private Container zzbhV;
    private zzb zzbhW;
    private zza zzbhX;
    private TagManager zzbhY;

    public interface zza {
        String zzGd();

        void zzGf();

        void zzfT(String str);
    }

    private class zzb extends Handler {
        private final ContainerHolder.ContainerAvailableListener zzbhZ;

        public zzb(ContainerHolder.ContainerAvailableListener containerAvailableListener, Looper looper) {
            super(looper);
            this.zzbhZ = containerAvailableListener;
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    zzfV((String) msg.obj);
                    return;
                default:
                    zzbg.e("Don't know how to handle this message.");
                    return;
            }
        }

        public void zzfU(String str) {
            sendMessage(obtainMessage(1, str));
        }

        /* access modifiers changed from: protected */
        public void zzfV(String str) {
            this.zzbhZ.onContainerAvailable(zzo.this, str);
        }
    }

    public zzo(Status status) {
        this.zzUX = status;
        this.zzagr = null;
    }

    public zzo(TagManager tagManager, Looper looper, Container container, zza zza2) {
        this.zzbhY = tagManager;
        this.zzagr = looper == null ? Looper.getMainLooper() : looper;
        this.zzbhU = container;
        this.zzbhX = zza2;
        this.zzUX = Status.zzagC;
        tagManager.zza(this);
    }

    private void zzGe() {
        if (this.zzbhW != null) {
            this.zzbhW.zzfU(this.zzbhV.zzGb());
        }
    }

    public synchronized Container getContainer() {
        Container container = null;
        synchronized (this) {
            if (this.zzapK) {
                zzbg.e("ContainerHolder is released.");
            } else {
                if (this.zzbhV != null) {
                    this.zzbhU = this.zzbhV;
                    this.zzbhV = null;
                }
                container = this.zzbhU;
            }
        }
        return container;
    }

    /* access modifiers changed from: package-private */
    public String getContainerId() {
        if (!this.zzapK) {
            return this.zzbhU.getContainerId();
        }
        zzbg.e("getContainerId called on a released ContainerHolder.");
        return "";
    }

    public Status getStatus() {
        return this.zzUX;
    }

    public synchronized void refresh() {
        if (this.zzapK) {
            zzbg.e("Refreshing a released ContainerHolder.");
        } else {
            this.zzbhX.zzGf();
        }
    }

    public synchronized void release() {
        if (this.zzapK) {
            zzbg.e("Releasing a released ContainerHolder.");
        } else {
            this.zzapK = true;
            this.zzbhY.zzb(this);
            this.zzbhU.release();
            this.zzbhU = null;
            this.zzbhV = null;
            this.zzbhX = null;
            this.zzbhW = null;
        }
    }

    public synchronized void setContainerAvailableListener(ContainerHolder.ContainerAvailableListener listener) {
        if (this.zzapK) {
            zzbg.e("ContainerHolder is released.");
        } else if (listener == null) {
            this.zzbhW = null;
        } else {
            this.zzbhW = new zzb(listener, this.zzagr);
            if (this.zzbhV != null) {
                zzGe();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public String zzGd() {
        if (!this.zzapK) {
            return this.zzbhX.zzGd();
        }
        zzbg.e("setCtfeUrlPathAndQuery called on a released ContainerHolder.");
        return "";
    }

    public synchronized void zza(Container container) {
        if (!this.zzapK) {
            if (container == null) {
                zzbg.e("Unexpected null container.");
            } else {
                this.zzbhV = container;
                zzGe();
            }
        }
    }

    public synchronized void zzfR(String str) {
        if (!this.zzapK) {
            this.zzbhU.zzfR(str);
        }
    }

    /* access modifiers changed from: package-private */
    public void zzfT(String str) {
        if (this.zzapK) {
            zzbg.e("setCtfeUrlPathAndQuery called on a released ContainerHolder.");
        } else {
            this.zzbhX.zzfT(str);
        }
    }
}
