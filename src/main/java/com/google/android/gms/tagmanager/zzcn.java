package com.google.android.gms.tagmanager;

import android.content.Context;
import android.content.res.Resources;
import com.google.android.gms.internal.zzaf;
import com.google.android.gms.internal.zzrq;
import com.google.android.gms.internal.zzrs;
import com.google.android.gms.internal.zzst;
import com.google.android.gms.internal.zzsu;
import com.google.android.gms.tagmanager.zzbf;
import com.google.android.gms.tagmanager.zzcb;
import com.google.android.gms.tagmanager.zzp;
import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.JSONException;

class zzcn implements zzp.zzf {
    private final Context mContext;
    private final String zzbhM;
    private zzbf<zzrq.zza> zzbkg;
    private final ExecutorService zzbkn = Executors.newSingleThreadExecutor();

    zzcn(Context context, String str) {
        this.mContext = context;
        this.zzbhM = str;
    }

    private zzrs.zzc zza(ByteArrayOutputStream byteArrayOutputStream) {
        try {
            return zzaz.zzgi(byteArrayOutputStream.toString(HttpRequest.CHARSET_UTF8));
        } catch (UnsupportedEncodingException e) {
            zzbg.zzaI("Failed to convert binary resource to string for JSON parsing; the file format is not UTF-8 format.");
            return null;
        } catch (JSONException e2) {
            zzbg.zzaK("Failed to extract the container from the resource file. Resource is a UTF-8 encoded string but doesn't contain a JSON container");
            return null;
        }
    }

    private void zzd(zzrq.zza zza) throws IllegalArgumentException {
        if (zza.zzju == null && zza.zzbme == null) {
            throw new IllegalArgumentException("Resource and SupplementedResource are NULL.");
        }
    }

    private zzrs.zzc zzx(byte[] bArr) {
        try {
            zzrs.zzc zzb = zzrs.zzb(zzaf.zzf.zzc(bArr));
            if (zzb == null) {
                return zzb;
            }
            zzbg.v("The container was successfully loaded from the resource (using binary file)");
            return zzb;
        } catch (zzst e) {
            zzbg.e("The resource file is corrupted. The container cannot be extracted from the binary file");
            return null;
        } catch (zzrs.zzg e2) {
            zzbg.zzaK("The resource file is invalid. The container from the binary file is invalid");
            return null;
        }
    }

    public synchronized void release() {
        this.zzbkn.shutdown();
    }

    public void zzGl() {
        this.zzbkn.execute(new Runnable() {
            public void run() {
                zzcn.this.zzHc();
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void zzHc() {
        if (this.zzbkg == null) {
            throw new IllegalStateException("Callback must be set before execute");
        }
        this.zzbkg.zzGk();
        zzbg.v("Attempting to load resource from disk");
        if ((zzcb.zzGU().zzGV() == zzcb.zza.CONTAINER || zzcb.zzGU().zzGV() == zzcb.zza.CONTAINER_DEBUG) && this.zzbhM.equals(zzcb.zzGU().getContainerId())) {
            this.zzbkg.zza(zzbf.zza.NOT_AVAILABLE);
            return;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(zzHd());
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                zzrs.zzb(fileInputStream, byteArrayOutputStream);
                zzrq.zza zzy = zzrq.zza.zzy(byteArrayOutputStream.toByteArray());
                zzd(zzy);
                this.zzbkg.zzI(zzy);
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    zzbg.zzaK("Error closing stream for reading resource from disk");
                }
            } catch (IOException e2) {
                this.zzbkg.zza(zzbf.zza.IO_ERROR);
                zzbg.zzaK("Failed to read the resource from disk");
                try {
                    fileInputStream.close();
                } catch (IOException e3) {
                    zzbg.zzaK("Error closing stream for reading resource from disk");
                }
            } catch (IllegalArgumentException e4) {
                this.zzbkg.zza(zzbf.zza.IO_ERROR);
                zzbg.zzaK("Failed to read the resource from disk. The resource is inconsistent");
                try {
                    fileInputStream.close();
                } catch (IOException e5) {
                    zzbg.zzaK("Error closing stream for reading resource from disk");
                }
            } catch (Throwable th) {
                try {
                    fileInputStream.close();
                } catch (IOException e6) {
                    zzbg.zzaK("Error closing stream for reading resource from disk");
                }
                throw th;
            }
            zzbg.v("The Disk resource was successfully read.");
        } catch (FileNotFoundException e7) {
            zzbg.zzaI("Failed to find the resource in the disk");
            this.zzbkg.zza(zzbf.zza.NOT_AVAILABLE);
        }
    }

    /* access modifiers changed from: package-private */
    public File zzHd() {
        return new File(this.mContext.getDir("google_tagmanager", 0), "resource_" + this.zzbhM);
    }

    public void zza(zzbf<zzrq.zza> zzbf) {
        this.zzbkg = zzbf;
    }

    public void zzb(final zzrq.zza zza) {
        this.zzbkn.execute(new Runnable() {
            public void run() {
                zzcn.this.zzc(zza);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public boolean zzc(zzrq.zza zza) {
        boolean z = false;
        File zzHd = zzHd();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(zzHd);
            try {
                fileOutputStream.write(zzsu.toByteArray(zza));
                z = true;
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    zzbg.zzaK("error closing stream for writing resource to disk");
                }
            } catch (IOException e2) {
                zzbg.zzaK("Error writing resource to disk. Removing resource from disk.");
                zzHd.delete();
                try {
                    fileOutputStream.close();
                } catch (IOException e3) {
                    zzbg.zzaK("error closing stream for writing resource to disk");
                }
            } catch (Throwable th) {
                try {
                    fileOutputStream.close();
                } catch (IOException e4) {
                    zzbg.zzaK("error closing stream for writing resource to disk");
                }
                throw th;
            }
        } catch (FileNotFoundException e5) {
            zzbg.e("Error opening resource file for writing");
        }
        return z;
    }

    public zzrs.zzc zzke(int i) {
        try {
            InputStream openRawResource = this.mContext.getResources().openRawResource(i);
            zzbg.v("Attempting to load a container from the resource ID " + i + " (" + this.mContext.getResources().getResourceName(i) + ")");
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                zzrs.zzb(openRawResource, byteArrayOutputStream);
                zzrs.zzc zza = zza(byteArrayOutputStream);
                if (zza == null) {
                    return zzx(byteArrayOutputStream.toByteArray());
                }
                zzbg.v("The container was successfully loaded from the resource (using JSON file format)");
                return zza;
            } catch (IOException e) {
                zzbg.zzaK("Error reading the default container with resource ID " + i + " (" + this.mContext.getResources().getResourceName(i) + ")");
                return null;
            }
        } catch (Resources.NotFoundException e2) {
            zzbg.zzaK("Failed to load the container. No default container resource found with the resource ID " + i);
            return null;
        }
    }
}
