package com.google.android.gms.tagmanager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import com.raizlabs.android.dbflow.sql.language.Condition;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

class zzcx implements zzac {
    private final Context mContext;
    private final zzb zzblh;
    private final zza zzbli;
    private final String zzzN;

    public interface zza {
        void zza(zzaq zzaq);

        void zzb(zzaq zzaq);

        void zzc(zzaq zzaq);
    }

    interface zzb {
        HttpURLConnection zzd(URL url) throws IOException;
    }

    zzcx(Context context, zza zza2) {
        this(new zzb() {
            public HttpURLConnection zzd(URL url) throws IOException {
                return (HttpURLConnection) url.openConnection();
            }
        }, context, zza2);
    }

    zzcx(zzb zzb2, Context context, zza zza2) {
        this.zzblh = zzb2;
        this.mContext = context.getApplicationContext();
        this.zzbli = zza2;
        this.zzzN = zza("GoogleTagManager", "4.00", Build.VERSION.RELEASE, zzc(Locale.getDefault()), Build.MODEL, Build.ID);
    }

    static String zzc(Locale locale) {
        if (locale == null || locale.getLanguage() == null || locale.getLanguage().length() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(locale.getLanguage().toLowerCase());
        if (!(locale.getCountry() == null || locale.getCountry().length() == 0)) {
            sb.append(Condition.Operation.MINUS).append(locale.getCountry().toLowerCase());
        }
        return sb.toString();
    }

    public void zzE(List<zzaq> list) {
        boolean z;
        int min = Math.min(list.size(), 40);
        boolean z2 = true;
        int i = 0;
        while (i < min) {
            zzaq zzaq = list.get(i);
            URL zzd = zzd(zzaq);
            if (zzd == null) {
                zzbg.zzaK("No destination: discarding hit.");
                this.zzbli.zzb(zzaq);
            } else {
                try {
                    HttpURLConnection zzd2 = this.zzblh.zzd(zzd);
                    if (z2) {
                        try {
                            zzbl.zzbb(this.mContext);
                            z2 = false;
                        } catch (IOException e) {
                            IOException iOException = e;
                            z2 = z;
                            e = iOException;
                            zzbg.zzaK("Exception sending hit: " + e.getClass().getSimpleName());
                            zzbg.zzaK(e.getMessage());
                            this.zzbli.zzc(zzaq);
                            i++;
                            z2 = z2;
                        } catch (Throwable th) {
                            Throwable th2 = th;
                            z = z2;
                            Throwable th3 = th2;
                            zzd2.disconnect();
                            throw th3;
                        }
                    }
                    zzd2.setRequestProperty("User-Agent", this.zzzN);
                    int responseCode = zzd2.getResponseCode();
                    if (responseCode != 200) {
                        zzbg.zzaK("Bad response: " + responseCode);
                        this.zzbli.zzc(zzaq);
                    } else {
                        this.zzbli.zza(zzaq);
                    }
                    zzd2.disconnect();
                } catch (IOException e2) {
                    e = e2;
                }
            }
            i++;
            z2 = z2;
        }
    }

    public boolean zzGw() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) this.mContext.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            return true;
        }
        zzbg.v("...no network connectivity");
        return false;
    }

    /* access modifiers changed from: package-private */
    public String zza(String str, String str2, String str3, String str4, String str5, String str6) {
        return String.format("%s/%s (Linux; U; Android %s; %s; %s Build/%s)", new Object[]{str, str2, str3, str4, str5, str6});
    }

    /* access modifiers changed from: package-private */
    public URL zzd(zzaq zzaq) {
        try {
            return new URL(zzaq.zzGF());
        } catch (MalformedURLException e) {
            zzbg.e("Error trying to parse the GTM url.");
            return null;
        }
    }
}
