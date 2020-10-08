package com.google.android.gms.analytics.internal;

import android.text.TextUtils;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import io.fabric.sdk.android.services.common.CommonUtils;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Locale;

public class zza extends zzd {
    public static boolean zzPV;
    private AdvertisingIdClient.Info zzPW;
    private final zzaj zzPX;
    private String zzPY;
    private boolean zzPZ = false;
    private Object zzQa = new Object();

    zza(zzf zzf) {
        super(zzf);
        this.zzPX = new zzaj(zzf.zzjl());
    }

    private boolean zza(AdvertisingIdClient.Info info, AdvertisingIdClient.Info info2) {
        String str;
        String str2 = null;
        String id = info2 == null ? null : info2.getId();
        if (TextUtils.isEmpty(id)) {
            return true;
        }
        String zzkk = zzjr().zzkk();
        synchronized (this.zzQa) {
            if (!this.zzPZ) {
                this.zzPY = zzjb();
                this.zzPZ = true;
            } else if (TextUtils.isEmpty(this.zzPY)) {
                if (info != null) {
                    str2 = info.getId();
                }
                if (str2 == null) {
                    boolean zzbc = zzbc(id + zzkk);
                    return zzbc;
                }
                this.zzPY = zzbb(str2 + zzkk);
            }
            String zzbb = zzbb(id + zzkk);
            if (TextUtils.isEmpty(zzbb)) {
                return false;
            }
            if (zzbb.equals(this.zzPY)) {
                return true;
            }
            if (!TextUtils.isEmpty(this.zzPY)) {
                zzbd("Resetting the client id because Advertising Id changed.");
                str = zzjr().zzkl();
                zza("New client Id", str);
            } else {
                str = zzkk;
            }
            boolean zzbc2 = zzbc(id + str);
            return zzbc2;
        }
    }

    private static String zzbb(String str) {
        MessageDigest zzbv = zzam.zzbv(CommonUtils.MD5_INSTANCE);
        if (zzbv == null) {
            return null;
        }
        return String.format(Locale.US, "%032X", new Object[]{new BigInteger(1, zzbv.digest(str.getBytes()))});
    }

    private boolean zzbc(String str) {
        try {
            String zzbb = zzbb(str);
            zzbd("Storing hashed adid.");
            FileOutputStream openFileOutput = getContext().openFileOutput("gaClientIdData", 0);
            openFileOutput.write(zzbb.getBytes());
            openFileOutput.close();
            this.zzPY = zzbb;
            return true;
        } catch (IOException e) {
            zze("Error creating hash file", e);
            return false;
        }
    }

    private synchronized AdvertisingIdClient.Info zziZ() {
        if (this.zzPX.zzv(1000)) {
            this.zzPX.start();
            AdvertisingIdClient.Info zzja = zzja();
            if (zza(this.zzPW, zzja)) {
                this.zzPW = zzja;
            } else {
                zzbh("Failed to reset client id on adid change. Not using adid");
                this.zzPW = new AdvertisingIdClient.Info("", false);
            }
        }
        return this.zzPW;
    }

    /* access modifiers changed from: protected */
    public void zziJ() {
    }

    public boolean zziU() {
        zzjv();
        AdvertisingIdClient.Info zziZ = zziZ();
        return zziZ != null && !zziZ.isLimitAdTrackingEnabled();
    }

    public String zziY() {
        zzjv();
        AdvertisingIdClient.Info zziZ = zziZ();
        String id = zziZ != null ? zziZ.getId() : null;
        if (TextUtils.isEmpty(id)) {
            return null;
        }
        return id;
    }

    /* access modifiers changed from: protected */
    public AdvertisingIdClient.Info zzja() {
        try {
            return AdvertisingIdClient.getAdvertisingIdInfo(getContext());
        } catch (IllegalStateException e) {
            zzbg("IllegalStateException getting Ad Id Info. If you would like to see Audience reports, please ensure that you have added '<meta-data android:name=\"com.google.android.gms.version\" android:value=\"@integer/google_play_services_version\" />' to your application manifest file. See http://goo.gl/naFqQk for details.");
            return null;
        } catch (Throwable th) {
            if (zzPV) {
                return null;
            }
            zzPV = true;
            zzd("Error getting advertiser id", th);
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public String zzjb() {
        String str = null;
        try {
            FileInputStream openFileInput = getContext().openFileInput("gaClientIdData");
            byte[] bArr = new byte[128];
            int read = openFileInput.read(bArr, 0, 128);
            if (openFileInput.available() > 0) {
                zzbg("Hash file seems corrupted, deleting it.");
                openFileInput.close();
                getContext().deleteFile("gaClientIdData");
                return null;
            } else if (read <= 0) {
                zzbd("Hash file is empty.");
                openFileInput.close();
                return null;
            } else {
                String str2 = new String(bArr, 0, read);
                try {
                    openFileInput.close();
                    return str2;
                } catch (FileNotFoundException e) {
                    return str2;
                } catch (IOException e2) {
                    IOException iOException = e2;
                    str = str2;
                    e = iOException;
                    zzd("Error reading Hash file, deleting it", e);
                    getContext().deleteFile("gaClientIdData");
                    return str;
                }
            }
        } catch (FileNotFoundException e3) {
            return null;
        } catch (IOException e4) {
            e = e4;
            zzd("Error reading Hash file, deleting it", e);
            getContext().deleteFile("gaClientIdData");
            return str;
        }
    }
}
