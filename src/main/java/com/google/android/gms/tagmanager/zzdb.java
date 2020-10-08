package com.google.android.gms.tagmanager;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.PowerManager;
import android.os.Process;
import android.text.TextUtils;
import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzae;
import com.google.android.gms.internal.zzag;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class zzdb extends zzak {
    private static final String ID = zzad.TIMER_LISTENER.toString();
    private static final String NAME = zzae.NAME.toString();
    private static final String zzblp = zzae.INTERVAL.toString();
    private static final String zzblq = zzae.LIMIT.toString();
    private static final String zzblr = zzae.UNIQUE_TRIGGER_ID.toString();
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public Handler mHandler;
    /* access modifiers changed from: private */
    public DataLayer zzbhN;
    /* access modifiers changed from: private */
    public boolean zzbls;
    /* access modifiers changed from: private */
    public boolean zzblt;
    private final HandlerThread zzblu;
    /* access modifiers changed from: private */
    public final Set<String> zzblv = new HashSet();

    private final class zza implements Runnable {
        private final long zzCv = System.currentTimeMillis();
        private final long zzaNY;
        private final String zzblw;
        private final String zzblx;
        private final long zzbly;
        private long zzblz;

        zza(String str, String str2, long j, long j2) {
            this.zzblw = str;
            this.zzblx = str2;
            this.zzaNY = j;
            this.zzbly = j2;
        }

        public void run() {
            if (this.zzbly <= 0 || this.zzblz < this.zzbly) {
                this.zzblz++;
                if (zzcH()) {
                    long currentTimeMillis = System.currentTimeMillis();
                    zzdb.this.zzbhN.push(DataLayer.mapOf("event", this.zzblw, "gtm.timerInterval", String.valueOf(this.zzaNY), "gtm.timerLimit", String.valueOf(this.zzbly), "gtm.timerStartTime", String.valueOf(this.zzCv), "gtm.timerCurrentTime", String.valueOf(currentTimeMillis), "gtm.timerElapsedTime", String.valueOf(currentTimeMillis - this.zzCv), "gtm.timerEventNumber", String.valueOf(this.zzblz), "gtm.triggers", this.zzblx));
                }
                zzdb.this.mHandler.postDelayed(this, this.zzaNY);
            } else if (!"0".equals(this.zzblx)) {
                zzdb.this.zzblv.remove(this.zzblx);
            }
        }

        /* access modifiers changed from: protected */
        public boolean zzcH() {
            if (zzdb.this.zzblt) {
                return zzdb.this.zzbls;
            }
            KeyguardManager keyguardManager = (KeyguardManager) zzdb.this.mContext.getSystemService("keyguard");
            PowerManager powerManager = (PowerManager) zzdb.this.mContext.getSystemService("power");
            for (ActivityManager.RunningAppProcessInfo next : ((ActivityManager) zzdb.this.mContext.getSystemService("activity")).getRunningAppProcesses()) {
                if (Process.myPid() == next.pid && next.importance == 100 && !keyguardManager.inKeyguardRestrictedInputMode() && powerManager.isScreenOn()) {
                    return true;
                }
            }
            return false;
        }
    }

    public zzdb(Context context, DataLayer dataLayer) {
        super(ID, zzblp, NAME);
        this.mContext = context;
        this.zzbhN = dataLayer;
        this.zzblu = new HandlerThread("Google GTM SDK Timer", 10);
        this.zzblu.start();
        this.mHandler = new Handler(this.zzblu.getLooper());
    }

    public boolean zzFW() {
        return false;
    }

    public zzag.zza zzP(Map<String, zzag.zza> map) {
        long j;
        long j2;
        String zzg = zzdf.zzg(map.get(NAME));
        String zzg2 = zzdf.zzg(map.get(zzblr));
        String zzg3 = zzdf.zzg(map.get(zzblp));
        String zzg4 = zzdf.zzg(map.get(zzblq));
        try {
            j = Long.parseLong(zzg3);
        } catch (NumberFormatException e) {
            j = 0;
        }
        try {
            j2 = Long.parseLong(zzg4);
        } catch (NumberFormatException e2) {
            j2 = 0;
        }
        if (j > 0 && !TextUtils.isEmpty(zzg)) {
            if (zzg2 == null || zzg2.isEmpty()) {
                zzg2 = "0";
            }
            if (!this.zzblv.contains(zzg2)) {
                if (!"0".equals(zzg2)) {
                    this.zzblv.add(zzg2);
                }
                this.mHandler.postDelayed(new zza(zzg, zzg2, j, j2), j);
            }
        }
        return zzdf.zzHF();
    }
}
