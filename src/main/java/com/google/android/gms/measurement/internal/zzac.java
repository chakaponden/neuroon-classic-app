package com.google.android.gms.measurement.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.zzf;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.common.stats.zzb;
import com.google.android.gms.common.zzc;
import com.google.android.gms.internal.zzmq;
import com.google.android.gms.measurement.AppMeasurementService;
import com.google.android.gms.measurement.internal.zzm;
import java.util.ArrayList;
import java.util.List;

public class zzac extends zzz {
    /* access modifiers changed from: private */
    public final zza zzaYN;
    /* access modifiers changed from: private */
    public zzm zzaYO;
    private Boolean zzaYP;
    private final zzf zzaYQ;
    private final zzaf zzaYR;
    private final List<Runnable> zzaYS = new ArrayList();
    private final zzf zzaYT;

    protected class zza implements ServiceConnection, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
        /* access modifiers changed from: private */
        public volatile boolean zzaYV;
        private volatile zzo zzaYW;

        protected zza() {
        }

        @MainThread
        public void onConnected(@Nullable Bundle connectionHint) {
            zzx.zzcD("MeasurementServiceConnection.onConnected");
            synchronized (this) {
                try {
                    final zzm zzm = (zzm) this.zzaYW.zzqJ();
                    this.zzaYW = null;
                    zzac.this.zzCn().zzg(new Runnable() {
                        public void run() {
                            synchronized (zza.this) {
                                boolean unused = zza.this.zzaYV = false;
                                if (!zzac.this.isConnected()) {
                                    zzac.this.zzAo().zzCJ().zzfg("Connected to remote service");
                                    zzac.this.zza(zzm);
                                }
                            }
                        }
                    });
                } catch (DeadObjectException | IllegalStateException e) {
                    this.zzaYW = null;
                    this.zzaYV = false;
                }
            }
        }

        @MainThread
        public void onConnectionFailed(@NonNull ConnectionResult result) {
            zzx.zzcD("MeasurementServiceConnection.onConnectionFailed");
            zzp zzCT = zzac.this.zzaTV.zzCT();
            if (zzCT != null) {
                zzCT.zzCF().zzj("Service connection failed", result);
            }
            synchronized (this) {
                this.zzaYV = false;
                this.zzaYW = null;
            }
        }

        @MainThread
        public void onConnectionSuspended(int cause) {
            zzx.zzcD("MeasurementServiceConnection.onConnectionSuspended");
            zzac.this.zzAo().zzCJ().zzfg("Service connection suspended");
            zzac.this.zzCn().zzg(new Runnable() {
                public void run() {
                    zzac.this.onServiceDisconnected(new ComponentName(zzac.this.getContext(), AppMeasurementService.class));
                }
            });
        }

        @MainThread
        public void onServiceConnected(ComponentName name, IBinder binder) {
            zzx.zzcD("MeasurementServiceConnection.onServiceConnected");
            synchronized (this) {
                if (binder == null) {
                    this.zzaYV = false;
                    zzac.this.zzAo().zzCE().zzfg("Service connected with null binder");
                    return;
                }
                final zzm zzm = null;
                try {
                    String interfaceDescriptor = binder.getInterfaceDescriptor();
                    if ("com.google.android.gms.measurement.internal.IMeasurementService".equals(interfaceDescriptor)) {
                        zzm = zzm.zza.zzdn(binder);
                        zzac.this.zzAo().zzCK().zzfg("Bound to IMeasurementService interface");
                    } else {
                        zzac.this.zzAo().zzCE().zzj("Got binder with a wrong descriptor", interfaceDescriptor);
                    }
                } catch (RemoteException e) {
                    zzac.this.zzAo().zzCE().zzfg("Service connect failed to get IMeasurementService");
                }
                if (zzm == null) {
                    this.zzaYV = false;
                    try {
                        zzb.zzrP().zza(zzac.this.getContext(), zzac.this.zzaYN);
                    } catch (IllegalArgumentException e2) {
                    }
                } else {
                    zzac.this.zzCn().zzg(new Runnable() {
                        public void run() {
                            synchronized (zza.this) {
                                boolean unused = zza.this.zzaYV = false;
                                if (!zzac.this.isConnected()) {
                                    zzac.this.zzAo().zzCK().zzfg("Connected to service");
                                    zzac.this.zza(zzm);
                                }
                            }
                        }
                    });
                }
            }
        }

        @MainThread
        public void onServiceDisconnected(final ComponentName name) {
            zzx.zzcD("MeasurementServiceConnection.onServiceDisconnected");
            zzac.this.zzAo().zzCJ().zzfg("Service disconnected");
            zzac.this.zzCn().zzg(new Runnable() {
                public void run() {
                    zzac.this.onServiceDisconnected(name);
                }
            });
        }

        @WorkerThread
        public void zzDt() {
            zzac.this.zzjk();
            Context context = zzac.this.getContext();
            synchronized (this) {
                if (this.zzaYV) {
                    zzac.this.zzAo().zzCK().zzfg("Connection attempt already in progress");
                } else if (this.zzaYW != null) {
                    zzac.this.zzAo().zzCK().zzfg("Already awaiting connection attempt");
                } else {
                    this.zzaYW = new zzo(context, Looper.getMainLooper(), zzf.zzat(context), this, this);
                    zzac.this.zzAo().zzCK().zzfg("Connecting to remote service");
                    this.zzaYV = true;
                    this.zzaYW.zzqG();
                }
            }
        }

        @WorkerThread
        public void zzz(Intent intent) {
            zzac.this.zzjk();
            Context context = zzac.this.getContext();
            zzb zzrP = zzb.zzrP();
            synchronized (this) {
                if (this.zzaYV) {
                    zzac.this.zzAo().zzCK().zzfg("Connection attempt already in progress");
                    return;
                }
                this.zzaYV = true;
                zzrP.zza(context, intent, (ServiceConnection) zzac.this.zzaYN, 129);
            }
        }
    }

    protected zzac(zzw zzw) {
        super(zzw);
        this.zzaYR = new zzaf(zzw.zzjl());
        this.zzaYN = new zza();
        this.zzaYQ = new zzf(zzw) {
            public void run() {
                zzac.this.zzjJ();
            }
        };
        this.zzaYT = new zzf(zzw) {
            public void run() {
                zzac.this.zzAo().zzCF().zzfg("Tasks have been queued for a long time");
            }
        };
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public void onServiceDisconnected(ComponentName name) {
        zzjk();
        if (this.zzaYO != null) {
            this.zzaYO = null;
            zzAo().zzCK().zzj("Disconnected from device MeasurementService", name);
            zzDr();
        }
    }

    private boolean zzDp() {
        List<ResolveInfo> queryIntentServices = getContext().getPackageManager().queryIntentServices(new Intent(getContext(), AppMeasurementService.class), 65536);
        return queryIntentServices != null && queryIntentServices.size() > 0;
    }

    @WorkerThread
    private boolean zzDq() {
        zzjk();
        zzjv();
        if (zzCp().zzkr()) {
            return true;
        }
        zzAo().zzCK().zzfg("Checking service availability");
        switch (zzc.zzoK().isGooglePlayServicesAvailable(getContext())) {
            case 0:
                zzAo().zzCK().zzfg("Service available");
                return true;
            case 1:
                zzAo().zzCK().zzfg("Service missing");
                return false;
            case 2:
                zzAo().zzCK().zzfg("Service version update required");
                return false;
            case 3:
                zzAo().zzCK().zzfg("Service disabled");
                return false;
            case 9:
                zzAo().zzCK().zzfg("Service invalid");
                return false;
            case 18:
                zzAo().zzCK().zzfg("Service updating");
                return true;
            default:
                return false;
        }
    }

    @WorkerThread
    private void zzDr() {
        zzjk();
        zzjX();
    }

    @WorkerThread
    private void zzDs() {
        zzjk();
        zzAo().zzCK().zzj("Processing queued up service tasks", Integer.valueOf(this.zzaYS.size()));
        for (Runnable zzg : this.zzaYS) {
            zzCn().zzg(zzg);
        }
        this.zzaYS.clear();
        this.zzaYT.cancel();
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public void zza(zzm zzm) {
        zzjk();
        zzx.zzz(zzm);
        this.zzaYO = zzm;
        zzjI();
        zzDs();
    }

    @WorkerThread
    private void zzi(Runnable runnable) throws IllegalStateException {
        zzjk();
        if (isConnected()) {
            runnable.run();
        } else if (((long) this.zzaYS.size()) >= zzCp().zzBS()) {
            zzAo().zzCE().zzfg("Discarding data. Max runnable queue size reached");
        } else {
            this.zzaYS.add(runnable);
            if (!this.zzaTV.zzCZ()) {
                this.zzaYT.zzt(60000);
            }
            zzjX();
        }
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public void zzjI() {
        zzjk();
        this.zzaYR.start();
        if (!this.zzaTV.zzCZ()) {
            this.zzaYQ.zzt(zzCp().zzkM());
        }
    }

    /* access modifiers changed from: private */
    @WorkerThread
    public void zzjJ() {
        zzjk();
        if (isConnected()) {
            zzAo().zzCK().zzfg("Inactivity, disconnecting from AppMeasurementService");
            disconnect();
        }
    }

    @WorkerThread
    private void zzjX() {
        zzjk();
        zzjv();
        if (!isConnected()) {
            if (this.zzaYP == null) {
                this.zzaYP = zzCo().zzCP();
                if (this.zzaYP == null) {
                    zzAo().zzCK().zzfg("State of service unknown");
                    this.zzaYP = Boolean.valueOf(zzDq());
                    zzCo().zzar(this.zzaYP.booleanValue());
                }
            }
            if (this.zzaYP.booleanValue()) {
                zzAo().zzCK().zzfg("Using measurement service");
                this.zzaYN.zzDt();
            } else if (zzDp() && !this.zzaTV.zzCZ()) {
                zzAo().zzCK().zzfg("Using local app measurement service");
                Intent intent = new Intent("com.google.android.gms.measurement.START");
                intent.setComponent(new ComponentName(getContext(), AppMeasurementService.class));
                this.zzaYN.zzz(intent);
            } else if (zzCp().zzks()) {
                zzAo().zzCK().zzfg("Using direct local measurement implementation");
                zza((zzm) new zzx(this.zzaTV, true));
            } else {
                zzAo().zzCE().zzfg("Not in main process. Unable to use local measurement implementation. Please register the AppMeasurementService service in the app manifest");
            }
        }
    }

    @WorkerThread
    public void disconnect() {
        zzjk();
        zzjv();
        try {
            zzb.zzrP().zza(getContext(), this.zzaYN);
        } catch (IllegalArgumentException | IllegalStateException e) {
        }
        this.zzaYO = null;
    }

    public /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
    }

    @WorkerThread
    public boolean isConnected() {
        zzjk();
        zzjv();
        return this.zzaYO != null;
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

    /* access modifiers changed from: protected */
    @WorkerThread
    public void zzDl() {
        zzjk();
        zzjv();
        zzi(new Runnable() {
            public void run() {
                zzm zzc = zzac.this.zzaYO;
                if (zzc == null) {
                    zzac.this.zzAo().zzCE().zzfg("Discarding data. Failed to send app launch");
                    return;
                }
                try {
                    zzc.zza(zzac.this.zzCg().zzfe(zzac.this.zzAo().zzCL()));
                    zzac.this.zzjI();
                } catch (RemoteException e) {
                    zzac.this.zzAo().zzCE().zzj("Failed to send app launch to AppMeasurementService", e);
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public void zzDo() {
        zzjk();
        zzjv();
        zzi(new Runnable() {
            public void run() {
                zzm zzc = zzac.this.zzaYO;
                if (zzc == null) {
                    zzac.this.zzAo().zzCE().zzfg("Failed to send measurementEnabled to service");
                    return;
                }
                try {
                    zzc.zzb(zzac.this.zzCg().zzfe(zzac.this.zzAo().zzCL()));
                    zzac.this.zzjI();
                } catch (RemoteException e) {
                    zzac.this.zzAo().zzCE().zzj("Failed to send measurementEnabled to AppMeasurementService", e);
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public void zza(final UserAttributeParcel userAttributeParcel) {
        zzjk();
        zzjv();
        zzi(new Runnable() {
            public void run() {
                zzm zzc = zzac.this.zzaYO;
                if (zzc == null) {
                    zzac.this.zzAo().zzCE().zzfg("Discarding data. Failed to set user attribute");
                    return;
                }
                try {
                    zzc.zza(userAttributeParcel, zzac.this.zzCg().zzfe(zzac.this.zzAo().zzCL()));
                    zzac.this.zzjI();
                } catch (RemoteException e) {
                    zzac.this.zzAo().zzCE().zzj("Failed to send attribute to AppMeasurementService", e);
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    @WorkerThread
    public void zzb(final EventParcel eventParcel, final String str) {
        zzx.zzz(eventParcel);
        zzjk();
        zzjv();
        zzi(new Runnable() {
            public void run() {
                zzm zzc = zzac.this.zzaYO;
                if (zzc == null) {
                    zzac.this.zzAo().zzCE().zzfg("Discarding data. Failed to send event to service");
                    return;
                }
                try {
                    if (TextUtils.isEmpty(str)) {
                        zzc.zza(eventParcel, zzac.this.zzCg().zzfe(zzac.this.zzAo().zzCL()));
                    } else {
                        zzc.zza(eventParcel, str, zzac.this.zzAo().zzCL());
                    }
                    zzac.this.zzjI();
                } catch (RemoteException e) {
                    zzac.this.zzAo().zzCE().zzj("Failed to send event to AppMeasurementService", e);
                }
            }
        });
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
}
