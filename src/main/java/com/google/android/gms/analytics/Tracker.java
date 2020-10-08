package com.google.android.gms.analytics;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.ActivityChooserView;
import android.text.TextUtils;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.internal.zzab;
import com.google.android.gms.analytics.internal.zzad;
import com.google.android.gms.analytics.internal.zzal;
import com.google.android.gms.analytics.internal.zzam;
import com.google.android.gms.analytics.internal.zzd;
import com.google.android.gms.analytics.internal.zze;
import com.google.android.gms.analytics.internal.zzf;
import com.google.android.gms.analytics.internal.zzh;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzpq;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class Tracker extends zzd {
    private boolean zzPs;
    private final Map<String, String> zzPt = new HashMap();
    /* access modifiers changed from: private */
    public final zzad zzPu;
    /* access modifiers changed from: private */
    public final zza zzPv;
    private ExceptionReporter zzPw;
    /* access modifiers changed from: private */
    public zzal zzPx;
    private final Map<String, String> zzxA = new HashMap();

    private class zza extends zzd implements GoogleAnalytics.zza {
        private boolean zzPG;
        private int zzPH;
        private long zzPI = -1;
        private boolean zzPJ;
        private long zzPK;

        protected zza(zzf zzf) {
            super(zzf);
        }

        private void zziN() {
            if (this.zzPI >= 0 || this.zzPG) {
                zziC().zza(Tracker.this.zzPv);
            } else {
                zziC().zzb(Tracker.this.zzPv);
            }
        }

        public void enableAutoActivityTracking(boolean enabled) {
            this.zzPG = enabled;
            zziN();
        }

        public void setSessionTimeout(long sessionTimeout) {
            this.zzPI = sessionTimeout;
            zziN();
        }

        /* access modifiers changed from: protected */
        public void zziJ() {
        }

        public synchronized boolean zziM() {
            boolean z;
            z = this.zzPJ;
            this.zzPJ = false;
            return z;
        }

        /* access modifiers changed from: package-private */
        public boolean zziO() {
            return zzjl().elapsedRealtime() >= this.zzPK + Math.max(1000, this.zzPI);
        }

        public void zzl(Activity activity) {
            if (this.zzPH == 0 && zziO()) {
                this.zzPJ = true;
            }
            this.zzPH++;
            if (this.zzPG) {
                Intent intent = activity.getIntent();
                if (intent != null) {
                    Tracker.this.setCampaignParamsOnNextHit(intent.getData());
                }
                HashMap hashMap = new HashMap();
                hashMap.put("&t", "screenview");
                Tracker.this.set("&cd", Tracker.this.zzPx != null ? Tracker.this.zzPx.zzo(activity) : activity.getClass().getCanonicalName());
                if (TextUtils.isEmpty((CharSequence) hashMap.get("&dr"))) {
                    String zzn = Tracker.zzn(activity);
                    if (!TextUtils.isEmpty(zzn)) {
                        hashMap.put("&dr", zzn);
                    }
                }
                Tracker.this.send(hashMap);
            }
        }

        public void zzm(Activity activity) {
            this.zzPH--;
            this.zzPH = Math.max(0, this.zzPH);
            if (this.zzPH == 0) {
                this.zzPK = zzjl().elapsedRealtime();
            }
        }
    }

    Tracker(zzf analytics, String trackingId, zzad rateLimiter) {
        super(analytics);
        if (trackingId != null) {
            this.zzxA.put("&tid", trackingId);
        }
        this.zzxA.put("useSecure", "1");
        this.zzxA.put("&a", Integer.toString(new Random().nextInt(ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED) + 1));
        if (rateLimiter == null) {
            this.zzPu = new zzad("tracking", zzjl());
        } else {
            this.zzPu = rateLimiter;
        }
        this.zzPv = new zza(analytics);
    }

    private static boolean zza(Map.Entry<String, String> entry) {
        String key = entry.getKey();
        String value = entry.getValue();
        return key.startsWith("&") && key.length() >= 2;
    }

    private static String zzb(Map.Entry<String, String> entry) {
        if (!zza(entry)) {
            return null;
        }
        return entry.getKey().substring(1);
    }

    private static void zzb(Map<String, String> map, Map<String, String> map2) {
        zzx.zzz(map2);
        if (map != null) {
            for (Map.Entry next : map.entrySet()) {
                String zzb = zzb((Map.Entry<String, String>) next);
                if (zzb != null) {
                    map2.put(zzb, next.getValue());
                }
            }
        }
    }

    private static void zzc(Map<String, String> map, Map<String, String> map2) {
        zzx.zzz(map2);
        if (map != null) {
            for (Map.Entry next : map.entrySet()) {
                String zzb = zzb((Map.Entry<String, String>) next);
                if (zzb != null && !map2.containsKey(zzb)) {
                    map2.put(zzb, next.getValue());
                }
            }
        }
    }

    private boolean zziK() {
        return this.zzPw != null;
    }

    static String zzn(Activity activity) {
        zzx.zzz(activity);
        Intent intent = activity.getIntent();
        if (intent == null) {
            return null;
        }
        String stringExtra = intent.getStringExtra("android.intent.extra.REFERRER_NAME");
        if (!TextUtils.isEmpty(stringExtra)) {
            return stringExtra;
        }
        return null;
    }

    public void enableAdvertisingIdCollection(boolean enabled) {
        this.zzPs = enabled;
    }

    public void enableAutoActivityTracking(boolean enabled) {
        this.zzPv.enableAutoActivityTracking(enabled);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void enableExceptionReporting(boolean r4) {
        /*
            r3 = this;
            monitor-enter(r3)
            boolean r0 = r3.zziK()     // Catch:{ all -> 0x0026 }
            if (r0 != r4) goto L_0x0009
            monitor-exit(r3)     // Catch:{ all -> 0x0026 }
        L_0x0008:
            return
        L_0x0009:
            if (r4 == 0) goto L_0x0029
            android.content.Context r0 = r3.getContext()     // Catch:{ all -> 0x0026 }
            java.lang.Thread$UncaughtExceptionHandler r1 = java.lang.Thread.getDefaultUncaughtExceptionHandler()     // Catch:{ all -> 0x0026 }
            com.google.android.gms.analytics.ExceptionReporter r2 = new com.google.android.gms.analytics.ExceptionReporter     // Catch:{ all -> 0x0026 }
            r2.<init>(r3, r1, r0)     // Catch:{ all -> 0x0026 }
            r3.zzPw = r2     // Catch:{ all -> 0x0026 }
            com.google.android.gms.analytics.ExceptionReporter r0 = r3.zzPw     // Catch:{ all -> 0x0026 }
            java.lang.Thread.setDefaultUncaughtExceptionHandler(r0)     // Catch:{ all -> 0x0026 }
            java.lang.String r0 = "Uncaught exceptions will be reported to Google Analytics"
            r3.zzbd(r0)     // Catch:{ all -> 0x0026 }
        L_0x0024:
            monitor-exit(r3)     // Catch:{ all -> 0x0026 }
            goto L_0x0008
        L_0x0026:
            r0 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x0026 }
            throw r0
        L_0x0029:
            com.google.android.gms.analytics.ExceptionReporter r0 = r3.zzPw     // Catch:{ all -> 0x0026 }
            java.lang.Thread$UncaughtExceptionHandler r0 = r0.zziD()     // Catch:{ all -> 0x0026 }
            java.lang.Thread.setDefaultUncaughtExceptionHandler(r0)     // Catch:{ all -> 0x0026 }
            java.lang.String r0 = "Uncaught exceptions will not be reported to Google Analytics"
            r3.zzbd(r0)     // Catch:{ all -> 0x0026 }
            goto L_0x0024
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.analytics.Tracker.enableExceptionReporting(boolean):void");
    }

    public String get(String key) {
        zzjv();
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        if (this.zzxA.containsKey(key)) {
            return this.zzxA.get(key);
        }
        if (key.equals("&ul")) {
            return zzam.zza(Locale.getDefault());
        }
        if (key.equals("&cid")) {
            return zzjr().zzkk();
        }
        if (key.equals("&sr")) {
            return zzju().zzla();
        }
        if (key.equals("&aid")) {
            return zzjt().zzjS().zzwK();
        }
        if (key.equals("&an")) {
            return zzjt().zzjS().zzlg();
        }
        if (key.equals("&av")) {
            return zzjt().zzjS().zzli();
        }
        if (key.equals("&aiid")) {
            return zzjt().zzjS().zzAJ();
        }
        return null;
    }

    public void send(Map<String, String> params) {
        final long currentTimeMillis = zzjl().currentTimeMillis();
        if (zziC().getAppOptOut()) {
            zzbe("AppOptOut is set to true. Not sending Google Analytics hit");
            return;
        }
        final boolean isDryRunEnabled = zziC().isDryRunEnabled();
        final HashMap hashMap = new HashMap();
        zzb(this.zzxA, hashMap);
        zzb(params, hashMap);
        final boolean zzh = zzam.zzh(this.zzxA.get("useSecure"), true);
        zzc(this.zzPt, hashMap);
        this.zzPt.clear();
        final String str = (String) hashMap.get("t");
        if (TextUtils.isEmpty(str)) {
            zzjm().zzh(hashMap, "Missing hit type parameter");
            return;
        }
        final String str2 = (String) hashMap.get("tid");
        if (TextUtils.isEmpty(str2)) {
            zzjm().zzh(hashMap, "Missing tracking id parameter");
            return;
        }
        final boolean zziL = zziL();
        synchronized (this) {
            if ("screenview".equalsIgnoreCase(str) || "pageview".equalsIgnoreCase(str) || "appview".equalsIgnoreCase(str) || TextUtils.isEmpty(str)) {
                int parseInt = Integer.parseInt(this.zzxA.get("&a")) + 1;
                if (parseInt >= Integer.MAX_VALUE) {
                    parseInt = 1;
                }
                this.zzxA.put("&a", Integer.toString(parseInt));
            }
        }
        zzjo().zzf(new Runnable() {
            public void run() {
                boolean z = true;
                if (Tracker.this.zzPv.zziM()) {
                    hashMap.put("sc", "start");
                }
                zzam.zzd(hashMap, "cid", Tracker.this.zziC().getClientId());
                String str = (String) hashMap.get("sf");
                if (str != null) {
                    double zza = zzam.zza(str, 100.0d);
                    if (zzam.zza(zza, (String) hashMap.get("cid"))) {
                        Tracker.this.zzb("Sampling enabled. Hit sampled out. sample rate", Double.valueOf(zza));
                        return;
                    }
                }
                com.google.android.gms.analytics.internal.zza zzb = Tracker.this.zzjs();
                if (zziL) {
                    zzam.zzb(hashMap, "ate", zzb.zziU());
                    zzam.zzc(hashMap, "adid", zzb.zziY());
                } else {
                    hashMap.remove("ate");
                    hashMap.remove("adid");
                }
                zzpq zzjS = Tracker.this.zzjt().zzjS();
                zzam.zzc(hashMap, "an", zzjS.zzlg());
                zzam.zzc(hashMap, "av", zzjS.zzli());
                zzam.zzc(hashMap, "aid", zzjS.zzwK());
                zzam.zzc(hashMap, "aiid", zzjS.zzAJ());
                hashMap.put("v", "1");
                hashMap.put("_v", zze.zzQm);
                zzam.zzc(hashMap, "ul", Tracker.this.zzju().zzkZ().getLanguage());
                zzam.zzc(hashMap, "sr", Tracker.this.zzju().zzla());
                if ((str.equals("transaction") || str.equals("item")) || Tracker.this.zzPu.zzlw()) {
                    long zzbt = zzam.zzbt((String) hashMap.get("ht"));
                    if (zzbt == 0) {
                        zzbt = currentTimeMillis;
                    }
                    if (isDryRunEnabled) {
                        Tracker.this.zzjm().zzc("Dry run enabled. Would have sent hit", new zzab(Tracker.this, hashMap, zzbt, zzh));
                        return;
                    }
                    String str2 = (String) hashMap.get("cid");
                    HashMap hashMap = new HashMap();
                    zzam.zza((Map<String, String>) hashMap, "uid", (Map<String, String>) hashMap);
                    zzam.zza((Map<String, String>) hashMap, "an", (Map<String, String>) hashMap);
                    zzam.zza((Map<String, String>) hashMap, "aid", (Map<String, String>) hashMap);
                    zzam.zza((Map<String, String>) hashMap, "av", (Map<String, String>) hashMap);
                    zzam.zza((Map<String, String>) hashMap, "aiid", (Map<String, String>) hashMap);
                    String str3 = str2;
                    if (TextUtils.isEmpty((CharSequence) hashMap.get("adid"))) {
                        z = false;
                    }
                    hashMap.put("_s", String.valueOf(Tracker.this.zziH().zza(new zzh(0, str2, str3, z, 0, hashMap))));
                    Tracker.this.zziH().zza(new zzab(Tracker.this, hashMap, zzbt, zzh));
                    return;
                }
                Tracker.this.zzjm().zzh(hashMap, "Too many hits sent too quickly, rate limiting invoked");
            }
        });
    }

    public void set(String key, String value) {
        zzx.zzb(key, (Object) "Key should be non-null");
        if (!TextUtils.isEmpty(key)) {
            this.zzxA.put(key, value);
        }
    }

    public void setAnonymizeIp(boolean anonymize) {
        set("&aip", zzam.zzK(anonymize));
    }

    public void setAppId(String appId) {
        set("&aid", appId);
    }

    public void setAppInstallerId(String appInstallerId) {
        set("&aiid", appInstallerId);
    }

    public void setAppName(String appName) {
        set("&an", appName);
    }

    public void setAppVersion(String appVersion) {
        set("&av", appVersion);
    }

    public void setCampaignParamsOnNextHit(Uri uri) {
        if (uri != null && !uri.isOpaque()) {
            String queryParameter = uri.getQueryParameter("referrer");
            if (!TextUtils.isEmpty(queryParameter)) {
                Uri parse = Uri.parse("http://hostname/?" + queryParameter);
                String queryParameter2 = parse.getQueryParameter("utm_id");
                if (queryParameter2 != null) {
                    this.zzPt.put("&ci", queryParameter2);
                }
                String queryParameter3 = parse.getQueryParameter("anid");
                if (queryParameter3 != null) {
                    this.zzPt.put("&anid", queryParameter3);
                }
                String queryParameter4 = parse.getQueryParameter("utm_campaign");
                if (queryParameter4 != null) {
                    this.zzPt.put("&cn", queryParameter4);
                }
                String queryParameter5 = parse.getQueryParameter("utm_content");
                if (queryParameter5 != null) {
                    this.zzPt.put("&cc", queryParameter5);
                }
                String queryParameter6 = parse.getQueryParameter("utm_medium");
                if (queryParameter6 != null) {
                    this.zzPt.put("&cm", queryParameter6);
                }
                String queryParameter7 = parse.getQueryParameter("utm_source");
                if (queryParameter7 != null) {
                    this.zzPt.put("&cs", queryParameter7);
                }
                String queryParameter8 = parse.getQueryParameter("utm_term");
                if (queryParameter8 != null) {
                    this.zzPt.put("&ck", queryParameter8);
                }
                String queryParameter9 = parse.getQueryParameter("dclid");
                if (queryParameter9 != null) {
                    this.zzPt.put("&dclid", queryParameter9);
                }
                String queryParameter10 = parse.getQueryParameter("gclid");
                if (queryParameter10 != null) {
                    this.zzPt.put("&gclid", queryParameter10);
                }
                String queryParameter11 = parse.getQueryParameter("aclid");
                if (queryParameter11 != null) {
                    this.zzPt.put("&aclid", queryParameter11);
                }
            }
        }
    }

    public void setClientId(String clientId) {
        set("&cid", clientId);
    }

    public void setEncoding(String encoding) {
        set("&de", encoding);
    }

    public void setHostname(String hostname) {
        set("&dh", hostname);
    }

    public void setLanguage(String language) {
        set("&ul", language);
    }

    public void setLocation(String location) {
        set("&dl", location);
    }

    public void setPage(String page) {
        set("&dp", page);
    }

    public void setReferrer(String referrer) {
        set("&dr", referrer);
    }

    public void setSampleRate(double sampleRate) {
        set("&sf", Double.toString(sampleRate));
    }

    public void setScreenColors(String screenColors) {
        set("&sd", screenColors);
    }

    public void setScreenName(String screenName) {
        set("&cd", screenName);
    }

    public void setScreenResolution(int width, int height) {
        if (width >= 0 || height >= 0) {
            set("&sr", width + "x" + height);
        } else {
            zzbg("Invalid width or height. The values should be non-negative.");
        }
    }

    public void setSessionTimeout(long sessionTimeout) {
        this.zzPv.setSessionTimeout(1000 * sessionTimeout);
    }

    public void setTitle(String title) {
        set("&dt", title);
    }

    public void setUseSecure(boolean useSecure) {
        set("useSecure", zzam.zzK(useSecure));
    }

    public void setViewportSize(String viewportSize) {
        set("&vp", viewportSize);
    }

    /* access modifiers changed from: package-private */
    public void zza(zzal zzal) {
        zzbd("Loading Tracker config values");
        this.zzPx = zzal;
        if (this.zzPx.zzlT()) {
            String trackingId = this.zzPx.getTrackingId();
            set("&tid", trackingId);
            zza("trackingId loaded", trackingId);
        }
        if (this.zzPx.zzlU()) {
            String d = Double.toString(this.zzPx.zzlV());
            set("&sf", d);
            zza("Sample frequency loaded", d);
        }
        if (this.zzPx.zzlW()) {
            int sessionTimeout = this.zzPx.getSessionTimeout();
            setSessionTimeout((long) sessionTimeout);
            zza("Session timeout loaded", Integer.valueOf(sessionTimeout));
        }
        if (this.zzPx.zzlX()) {
            boolean zzlY = this.zzPx.zzlY();
            enableAutoActivityTracking(zzlY);
            zza("Auto activity tracking loaded", Boolean.valueOf(zzlY));
        }
        if (this.zzPx.zzlZ()) {
            boolean zzma = this.zzPx.zzma();
            if (zzma) {
                set("&aip", "1");
            }
            zza("Anonymize ip loaded", Boolean.valueOf(zzma));
        }
        enableExceptionReporting(this.zzPx.zzmb());
    }

    /* access modifiers changed from: protected */
    public void zziJ() {
        this.zzPv.zza();
        String zzlg = zziI().zzlg();
        if (zzlg != null) {
            set("&an", zzlg);
        }
        String zzli = zziI().zzli();
        if (zzli != null) {
            set("&av", zzli);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean zziL() {
        return this.zzPs;
    }
}
