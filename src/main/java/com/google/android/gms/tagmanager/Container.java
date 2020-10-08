package com.google.android.gms.tagmanager;

import android.content.Context;
import com.google.android.gms.internal.zzaf;
import com.google.android.gms.internal.zzrs;
import com.google.android.gms.tagmanager.zzcb;
import com.google.android.gms.tagmanager.zzt;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Container {
    private final Context mContext;
    private final String zzbhM;
    private final DataLayer zzbhN;
    private zzcp zzbhO;
    private Map<String, FunctionCallMacroCallback> zzbhP = new HashMap();
    private Map<String, FunctionCallTagCallback> zzbhQ = new HashMap();
    private volatile long zzbhR;
    private volatile String zzbhS = "";

    public interface FunctionCallMacroCallback {
        Object getValue(String str, Map<String, Object> map);
    }

    public interface FunctionCallTagCallback {
        void execute(String str, Map<String, Object> map);
    }

    private class zza implements zzt.zza {
        private zza() {
        }

        public Object zzc(String str, Map<String, Object> map) {
            FunctionCallMacroCallback zzfP = Container.this.zzfP(str);
            if (zzfP == null) {
                return null;
            }
            return zzfP.getValue(str, map);
        }
    }

    private class zzb implements zzt.zza {
        private zzb() {
        }

        public Object zzc(String str, Map<String, Object> map) {
            FunctionCallTagCallback zzfQ = Container.this.zzfQ(str);
            if (zzfQ != null) {
                zzfQ.execute(str, map);
            }
            return zzdf.zzHE();
        }
    }

    Container(Context context, DataLayer dataLayer, String containerId, long lastRefreshTime, zzaf.zzj resource) {
        this.mContext = context;
        this.zzbhN = dataLayer;
        this.zzbhM = containerId;
        this.zzbhR = lastRefreshTime;
        zza(resource.zzju);
        if (resource.zzjt != null) {
            zza(resource.zzjt);
        }
    }

    Container(Context context, DataLayer dataLayer, String containerId, long lastRefreshTime, zzrs.zzc resource) {
        this.mContext = context;
        this.zzbhN = dataLayer;
        this.zzbhM = containerId;
        this.zzbhR = lastRefreshTime;
        zza(resource);
    }

    private synchronized zzcp zzGc() {
        return this.zzbhO;
    }

    private void zza(zzaf.zzf zzf) {
        if (zzf == null) {
            throw new NullPointerException();
        }
        try {
            zza(zzrs.zzb(zzf));
        } catch (zzrs.zzg e) {
            zzbg.e("Not loading resource: " + zzf + " because it is invalid: " + e.toString());
        }
    }

    private void zza(zzrs.zzc zzc) {
        this.zzbhS = zzc.getVersion();
        zzrs.zzc zzc2 = zzc;
        zza(new zzcp(this.mContext, zzc2, this.zzbhN, new zza(), new zzb(), zzfS(this.zzbhS)));
        if (getBoolean("_gtm.loadEventEnabled")) {
            this.zzbhN.pushEvent("gtm.load", DataLayer.mapOf("gtm.id", this.zzbhM));
        }
    }

    private synchronized void zza(zzcp zzcp) {
        this.zzbhO = zzcp;
    }

    private void zza(zzaf.zzi[] zziArr) {
        ArrayList arrayList = new ArrayList();
        for (zzaf.zzi add : zziArr) {
            arrayList.add(add);
        }
        zzGc().zzF(arrayList);
    }

    public boolean getBoolean(String key) {
        zzcp zzGc = zzGc();
        if (zzGc == null) {
            zzbg.e("getBoolean called for closed container.");
            return zzdf.zzHC().booleanValue();
        }
        try {
            return zzdf.zzk(zzGc.zzgn(key).getObject()).booleanValue();
        } catch (Exception e) {
            zzbg.e("Calling getBoolean() threw an exception: " + e.getMessage() + " Returning default value.");
            return zzdf.zzHC().booleanValue();
        }
    }

    public String getContainerId() {
        return this.zzbhM;
    }

    public double getDouble(String key) {
        zzcp zzGc = zzGc();
        if (zzGc == null) {
            zzbg.e("getDouble called for closed container.");
            return zzdf.zzHB().doubleValue();
        }
        try {
            return zzdf.zzj(zzGc.zzgn(key).getObject()).doubleValue();
        } catch (Exception e) {
            zzbg.e("Calling getDouble() threw an exception: " + e.getMessage() + " Returning default value.");
            return zzdf.zzHB().doubleValue();
        }
    }

    public long getLastRefreshTime() {
        return this.zzbhR;
    }

    public long getLong(String key) {
        zzcp zzGc = zzGc();
        if (zzGc == null) {
            zzbg.e("getLong called for closed container.");
            return zzdf.zzHA().longValue();
        }
        try {
            return zzdf.zzi(zzGc.zzgn(key).getObject()).longValue();
        } catch (Exception e) {
            zzbg.e("Calling getLong() threw an exception: " + e.getMessage() + " Returning default value.");
            return zzdf.zzHA().longValue();
        }
    }

    public String getString(String key) {
        zzcp zzGc = zzGc();
        if (zzGc == null) {
            zzbg.e("getString called for closed container.");
            return zzdf.zzHE();
        }
        try {
            return zzdf.zzg(zzGc.zzgn(key).getObject());
        } catch (Exception e) {
            zzbg.e("Calling getString() threw an exception: " + e.getMessage() + " Returning default value.");
            return zzdf.zzHE();
        }
    }

    public boolean isDefault() {
        return getLastRefreshTime() == 0;
    }

    public void registerFunctionCallMacroCallback(String customMacroName, FunctionCallMacroCallback customMacroCallback) {
        if (customMacroCallback == null) {
            throw new NullPointerException("Macro handler must be non-null");
        }
        synchronized (this.zzbhP) {
            this.zzbhP.put(customMacroName, customMacroCallback);
        }
    }

    public void registerFunctionCallTagCallback(String customTagName, FunctionCallTagCallback customTagCallback) {
        if (customTagCallback == null) {
            throw new NullPointerException("Tag callback must be non-null");
        }
        synchronized (this.zzbhQ) {
            this.zzbhQ.put(customTagName, customTagCallback);
        }
    }

    /* access modifiers changed from: package-private */
    public void release() {
        this.zzbhO = null;
    }

    public void unregisterFunctionCallMacroCallback(String customMacroName) {
        synchronized (this.zzbhP) {
            this.zzbhP.remove(customMacroName);
        }
    }

    public void unregisterFunctionCallTagCallback(String customTagName) {
        synchronized (this.zzbhQ) {
            this.zzbhQ.remove(customTagName);
        }
    }

    public String zzGb() {
        return this.zzbhS;
    }

    /* access modifiers changed from: package-private */
    public FunctionCallMacroCallback zzfP(String str) {
        FunctionCallMacroCallback functionCallMacroCallback;
        synchronized (this.zzbhP) {
            functionCallMacroCallback = this.zzbhP.get(str);
        }
        return functionCallMacroCallback;
    }

    public FunctionCallTagCallback zzfQ(String str) {
        FunctionCallTagCallback functionCallTagCallback;
        synchronized (this.zzbhQ) {
            functionCallTagCallback = this.zzbhQ.get(str);
        }
        return functionCallTagCallback;
    }

    public void zzfR(String str) {
        zzGc().zzfR(str);
    }

    /* access modifiers changed from: package-private */
    public zzah zzfS(String str) {
        if (zzcb.zzGU().zzGV().equals(zzcb.zza.CONTAINER_DEBUG)) {
        }
        return new zzbo();
    }
}
