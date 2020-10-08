package com.google.android.gms.tagmanager;

import android.annotation.TargetApi;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RawRes;
import android.support.annotation.RequiresPermission;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.tagmanager.DataLayer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TagManager {
    private static TagManager zzblm;
    private final Context mContext;
    private final DataLayer zzbhN;
    private final zzs zzbkh;
    private final zza zzblj;
    private final zzct zzblk;
    private final ConcurrentMap<zzo, Boolean> zzbll;

    public interface zza {
        zzp zza(Context context, TagManager tagManager, Looper looper, String str, int i, zzs zzs);
    }

    TagManager(Context context, zza containerHolderLoaderProvider, DataLayer dataLayer, zzct serviceManager) {
        if (context == null) {
            throw new NullPointerException("context cannot be null");
        }
        this.mContext = context.getApplicationContext();
        this.zzblk = serviceManager;
        this.zzblj = containerHolderLoaderProvider;
        this.zzbll = new ConcurrentHashMap();
        this.zzbhN = dataLayer;
        this.zzbhN.zza((DataLayer.zzb) new DataLayer.zzb() {
            public void zzQ(Map<String, Object> map) {
                Object obj = map.get("event");
                if (obj != null) {
                    TagManager.this.zzgp(obj.toString());
                }
            }
        });
        this.zzbhN.zza((DataLayer.zzb) new zzd(this.mContext));
        this.zzbkh = new zzs();
        zzHt();
    }

    @RequiresPermission(allOf = {"android.permission.INTERNET", "android.permission.ACCESS_NETWORK_STATE"})
    public static TagManager getInstance(Context context) {
        TagManager tagManager;
        synchronized (TagManager.class) {
            if (zzblm == null) {
                if (context == null) {
                    zzbg.e("TagManager.getInstance requires non-null context.");
                    throw new NullPointerException();
                }
                zzblm = new TagManager(context, new zza() {
                    public zzp zza(Context context, TagManager tagManager, Looper looper, String str, int i, zzs zzs) {
                        return new zzp(context, tagManager, looper, str, i, zzs);
                    }
                }, new DataLayer(new zzw(context)), zzcu.zzHo());
            }
            tagManager = zzblm;
        }
        return tagManager;
    }

    @TargetApi(14)
    private void zzHt() {
        if (Build.VERSION.SDK_INT >= 14) {
            this.mContext.registerComponentCallbacks(new ComponentCallbacks2() {
                public void onConfigurationChanged(Configuration configuration) {
                }

                public void onLowMemory() {
                }

                public void onTrimMemory(int i) {
                    if (i == 20) {
                        TagManager.this.dispatch();
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void zzgp(String str) {
        for (zzo zzfR : this.zzbll.keySet()) {
            zzfR.zzfR(str);
        }
    }

    public void dispatch() {
        this.zzblk.dispatch();
    }

    public DataLayer getDataLayer() {
        return this.zzbhN;
    }

    public PendingResult<ContainerHolder> loadContainerDefaultOnly(String containerId, @RawRes int defaultContainerResourceId) {
        zzp zza2 = this.zzblj.zza(this.mContext, this, (Looper) null, containerId, defaultContainerResourceId, this.zzbkh);
        zza2.zzGg();
        return zza2;
    }

    public PendingResult<ContainerHolder> loadContainerDefaultOnly(String containerId, @RawRes int defaultContainerResourceId, Handler handler) {
        zzp zza2 = this.zzblj.zza(this.mContext, this, handler.getLooper(), containerId, defaultContainerResourceId, this.zzbkh);
        zza2.zzGg();
        return zza2;
    }

    public PendingResult<ContainerHolder> loadContainerPreferFresh(String containerId, @RawRes int defaultContainerResourceId) {
        zzp zza2 = this.zzblj.zza(this.mContext, this, (Looper) null, containerId, defaultContainerResourceId, this.zzbkh);
        zza2.zzGi();
        return zza2;
    }

    public PendingResult<ContainerHolder> loadContainerPreferFresh(String containerId, @RawRes int defaultContainerResourceId, Handler handler) {
        zzp zza2 = this.zzblj.zza(this.mContext, this, handler.getLooper(), containerId, defaultContainerResourceId, this.zzbkh);
        zza2.zzGi();
        return zza2;
    }

    public PendingResult<ContainerHolder> loadContainerPreferNonDefault(String containerId, @RawRes int defaultContainerResourceId) {
        zzp zza2 = this.zzblj.zza(this.mContext, this, (Looper) null, containerId, defaultContainerResourceId, this.zzbkh);
        zza2.zzGh();
        return zza2;
    }

    public PendingResult<ContainerHolder> loadContainerPreferNonDefault(String containerId, @RawRes int defaultContainerResourceId, Handler handler) {
        zzp zza2 = this.zzblj.zza(this.mContext, this, handler.getLooper(), containerId, defaultContainerResourceId, this.zzbkh);
        zza2.zzGh();
        return zza2;
    }

    public void setVerboseLoggingEnabled(boolean enableVerboseLogging) {
        zzbg.setLogLevel(enableVerboseLogging ? 2 : 5);
    }

    public void zza(zzo zzo) {
        this.zzbll.put(zzo, true);
    }

    public boolean zzb(zzo zzo) {
        return this.zzbll.remove(zzo) != null;
    }

    /* access modifiers changed from: package-private */
    public synchronized boolean zzp(Uri uri) {
        boolean z;
        zzcb zzGU = zzcb.zzGU();
        if (zzGU.zzp(uri)) {
            String containerId = zzGU.getContainerId();
            switch (zzGU.zzGV()) {
                case NONE:
                    for (zzo zzo : this.zzbll.keySet()) {
                        if (zzo.getContainerId().equals(containerId)) {
                            zzo.zzfT((String) null);
                            zzo.refresh();
                        }
                    }
                    break;
                case CONTAINER:
                case CONTAINER_DEBUG:
                    for (zzo zzo2 : this.zzbll.keySet()) {
                        if (zzo2.getContainerId().equals(containerId)) {
                            zzo2.zzfT(zzGU.zzGW());
                            zzo2.refresh();
                        } else if (zzo2.zzGd() != null) {
                            zzo2.zzfT((String) null);
                            zzo2.refresh();
                        }
                    }
                    break;
            }
            z = true;
        } else {
            z = false;
        }
        return z;
    }
}
