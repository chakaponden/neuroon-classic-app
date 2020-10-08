package com.google.android.gms.measurement.internal;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import com.google.android.gms.common.internal.zzx;
import com.google.android.gms.internal.zzmq;
import com.google.android.gms.internal.zzqb;
import com.google.android.gms.internal.zzsn;
import io.fabric.sdk.android.services.common.CommonUtils;
import io.fabric.sdk.android.services.events.EventsFilesManager;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class zzaj extends zzy {
    zzaj(zzw zzw) {
        super(zzw);
    }

    public static boolean zzI(Bundle bundle) {
        return bundle.getLong("_c") == 1;
    }

    public static boolean zzQ(String str, String str2) {
        if (str == null && str2 == null) {
            return true;
        }
        if (str == null) {
            return false;
        }
        return str.equals(str2);
    }

    private Object zza(int i, Object obj, boolean z) {
        if (obj == null) {
            return null;
        }
        if ((obj instanceof Long) || (obj instanceof Float)) {
            return obj;
        }
        if (obj instanceof Integer) {
            return Long.valueOf((long) ((Integer) obj).intValue());
        }
        if (obj instanceof Byte) {
            return Long.valueOf((long) ((Byte) obj).byteValue());
        }
        if (obj instanceof Short) {
            return Long.valueOf((long) ((Short) obj).shortValue());
        }
        if (obj instanceof Boolean) {
            return Long.valueOf(((Boolean) obj).booleanValue() ? 1 : 0);
        } else if (obj instanceof Double) {
            return Float.valueOf((float) ((Double) obj).doubleValue());
        } else {
            if (!(obj instanceof String) && !(obj instanceof Character) && !(obj instanceof CharSequence)) {
                return null;
            }
            String valueOf = String.valueOf(obj);
            if (valueOf.length() <= i) {
                return valueOf;
            }
            if (z) {
                return valueOf.substring(0, i);
            }
            return null;
        }
    }

    private void zza(String str, String str2, int i, Object obj) {
        if (obj == null) {
            zzAo().zzCH().zzj(str + " value can't be null. Ignoring " + str, str2);
        } else if (!(obj instanceof Long) && !(obj instanceof Float) && !(obj instanceof Integer) && !(obj instanceof Byte) && !(obj instanceof Short) && !(obj instanceof Boolean) && !(obj instanceof Double)) {
            if ((obj instanceof String) || (obj instanceof Character) || (obj instanceof CharSequence)) {
                String valueOf = String.valueOf(obj);
                if (valueOf.length() > i) {
                    zzAo().zzCH().zze("Ignoring " + str + ". Value is too long. name, value length", str2, Integer.valueOf(valueOf.length()));
                }
            }
        }
    }

    private static void zza(StringBuilder sb, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            sb.append("  ");
        }
    }

    private static void zza(StringBuilder sb, int i, zzqb.zze zze) {
        if (zze != null) {
            zza(sb, i);
            sb.append("bundle {\n");
            zza(sb, i, "protocol_version", (Object) zze.zzbal);
            zza(sb, i, "platform", (Object) zze.zzbat);
            zza(sb, i, "gmp_version", (Object) zze.zzbax);
            zza(sb, i, "uploading_gmp_version", (Object) zze.zzbay);
            zza(sb, i, "gmp_app_id", (Object) zze.zzaVt);
            zza(sb, i, "app_id", (Object) zze.appId);
            zza(sb, i, "app_version", (Object) zze.zzaMV);
            zza(sb, i, "dev_cert_hash", (Object) zze.zzbaC);
            zza(sb, i, "app_store", (Object) zze.zzaVu);
            zza(sb, i, "upload_timestamp_millis", (Object) zze.zzbao);
            zza(sb, i, "start_timestamp_millis", (Object) zze.zzbap);
            zza(sb, i, "end_timestamp_millis", (Object) zze.zzbaq);
            zza(sb, i, "previous_bundle_start_timestamp_millis", (Object) zze.zzbar);
            zza(sb, i, "previous_bundle_end_timestamp_millis", (Object) zze.zzbas);
            zza(sb, i, "app_instance_id", (Object) zze.zzbaB);
            zza(sb, i, "resettable_device_id", (Object) zze.zzbaz);
            zza(sb, i, "limited_ad_tracking", (Object) zze.zzbaA);
            zza(sb, i, "os_version", (Object) zze.osVersion);
            zza(sb, i, "device_model", (Object) zze.zzbau);
            zza(sb, i, "user_default_language", (Object) zze.zzbav);
            zza(sb, i, "time_zone_offset_minutes", (Object) zze.zzbaw);
            zza(sb, i, "bundle_sequential_index", (Object) zze.zzbaD);
            zza(sb, i, "service_upload", (Object) zze.zzbaE);
            zza(sb, i, "health_monitor", (Object) zze.zzaVx);
            zza(sb, i, zze.zzban);
            zza(sb, i, zze.zzbaF);
            zza(sb, i, zze.zzbam);
            zza(sb, i);
            sb.append("}\n");
        }
    }

    private static void zza(StringBuilder sb, int i, String str, zzqb.zzf zzf) {
        int i2 = 0;
        if (zzf != null) {
            int i3 = i + 1;
            zza(sb, i3);
            sb.append(str);
            sb.append(" {\n");
            if (zzf.zzbaH != null) {
                zza(sb, i3 + 1);
                sb.append("results: ");
                long[] jArr = zzf.zzbaH;
                int length = jArr.length;
                int i4 = 0;
                int i5 = 0;
                while (i4 < length) {
                    Long valueOf = Long.valueOf(jArr[i4]);
                    int i6 = i5 + 1;
                    if (i5 != 0) {
                        sb.append(", ");
                    }
                    sb.append(valueOf);
                    i4++;
                    i5 = i6;
                }
                sb.append(10);
            }
            if (zzf.zzbaG != null) {
                zza(sb, i3 + 1);
                sb.append("status: ");
                long[] jArr2 = zzf.zzbaG;
                int length2 = jArr2.length;
                int i7 = 0;
                while (i2 < length2) {
                    Long valueOf2 = Long.valueOf(jArr2[i2]);
                    int i8 = i7 + 1;
                    if (i7 != 0) {
                        sb.append(", ");
                    }
                    sb.append(valueOf2);
                    i2++;
                    i7 = i8;
                }
                sb.append(10);
            }
            zza(sb, i3);
            sb.append("}\n");
        }
    }

    private static void zza(StringBuilder sb, int i, String str, Object obj) {
        if (obj != null) {
            zza(sb, i + 1);
            sb.append(str);
            sb.append(": ");
            sb.append(obj);
            sb.append(10);
        }
    }

    private static void zza(StringBuilder sb, int i, zzqb.zza[] zzaArr) {
        if (zzaArr != null) {
            int i2 = i + 1;
            for (zzqb.zza zza : zzaArr) {
                if (zza != null) {
                    zza(sb, i2);
                    sb.append("audience_membership {\n");
                    zza(sb, i2, "audience_id", (Object) zza.zzaZr);
                    zza(sb, i2, "new_audience", (Object) zza.zzbac);
                    zza(sb, i2, "current_data", zza.zzbaa);
                    zza(sb, i2, "previous_data", zza.zzbab);
                    zza(sb, i2);
                    sb.append("}\n");
                }
            }
        }
    }

    private static void zza(StringBuilder sb, int i, zzqb.zzb[] zzbArr) {
        if (zzbArr != null) {
            int i2 = i + 1;
            for (zzqb.zzb zzb : zzbArr) {
                if (zzb != null) {
                    zza(sb, i2);
                    sb.append("event {\n");
                    zza(sb, i2, "name", (Object) zzb.name);
                    zza(sb, i2, "timestamp_millis", (Object) zzb.zzbaf);
                    zza(sb, i2, "previous_timestamp_millis", (Object) zzb.zzbag);
                    zza(sb, i2, "count", (Object) zzb.count);
                    zza(sb, i2, zzb.zzbae);
                    zza(sb, i2);
                    sb.append("}\n");
                }
            }
        }
    }

    private static void zza(StringBuilder sb, int i, zzqb.zzc[] zzcArr) {
        if (zzcArr != null) {
            int i2 = i + 1;
            for (zzqb.zzc zzc : zzcArr) {
                if (zzc != null) {
                    zza(sb, i2);
                    sb.append("event {\n");
                    zza(sb, i2, "name", (Object) zzc.name);
                    zza(sb, i2, "string_value", (Object) zzc.zzamJ);
                    zza(sb, i2, "int_value", (Object) zzc.zzbai);
                    zza(sb, i2, "float_value", (Object) zzc.zzaZo);
                    zza(sb, i2);
                    sb.append("}\n");
                }
            }
        }
    }

    private static void zza(StringBuilder sb, int i, zzqb.zzg[] zzgArr) {
        if (zzgArr != null) {
            int i2 = i + 1;
            for (zzqb.zzg zzg : zzgArr) {
                if (zzg != null) {
                    zza(sb, i2);
                    sb.append("user_property {\n");
                    zza(sb, i2, "set_timestamp_millis", (Object) zzg.zzbaJ);
                    zza(sb, i2, "name", (Object) zzg.name);
                    zza(sb, i2, "string_value", (Object) zzg.zzamJ);
                    zza(sb, i2, "int_value", (Object) zzg.zzbai);
                    zza(sb, i2, "float_value", (Object) zzg.zzaZo);
                    zza(sb, i2);
                    sb.append("}\n");
                }
            }
        }
    }

    public static boolean zza(Context context, Class<? extends Service> cls) {
        try {
            ServiceInfo serviceInfo = context.getPackageManager().getServiceInfo(new ComponentName(context, cls), 4);
            return serviceInfo != null && serviceInfo.enabled;
        } catch (PackageManager.NameNotFoundException e) {
        }
    }

    public static boolean zza(Context context, Class<? extends BroadcastReceiver> cls, boolean z) {
        try {
            ActivityInfo receiverInfo = context.getPackageManager().getReceiverInfo(new ComponentName(context, cls), 2);
            return receiverInfo != null && receiverInfo.enabled && (!z || receiverInfo.exported);
        } catch (PackageManager.NameNotFoundException e) {
        }
    }

    public static boolean zza(long[] jArr, int i) {
        return i < jArr.length * 64 && (jArr[i / 64] & (1 << (i % 64))) != 0;
    }

    public static long[] zza(BitSet bitSet) {
        int length = (bitSet.length() + 63) / 64;
        long[] jArr = new long[length];
        int i = 0;
        while (i < length) {
            jArr[i] = 0;
            int i2 = 0;
            while (i2 < 64 && (i * 64) + i2 < bitSet.length()) {
                if (bitSet.get((i * 64) + i2)) {
                    jArr[i] = jArr[i] | (1 << i2);
                }
                i2++;
            }
            i++;
        }
        return jArr;
    }

    public static String zzb(zzqb.zzd zzd) {
        if (zzd == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\nbatch {\n");
        if (zzd.zzbaj != null) {
            for (zzqb.zze zze : zzd.zzbaj) {
                if (zze != null) {
                    zza(sb, 1, zze);
                }
            }
        }
        sb.append("}\n");
        return sb.toString();
    }

    static MessageDigest zzbv(String str) {
        int i = 0;
        while (true) {
            int i2 = i;
            if (i2 >= 2) {
                return null;
            }
            try {
                MessageDigest instance = MessageDigest.getInstance(str);
                if (instance != null) {
                    return instance;
                }
                i = i2 + 1;
            } catch (NoSuchAlgorithmException e) {
            }
        }
    }

    static boolean zzfq(String str) {
        zzx.zzcM(str);
        return str.charAt(0) != '_';
    }

    private int zzfu(String str) {
        return "_ldl".equals(str) ? zzCp().zzBG() : zzCp().zzBF();
    }

    public static boolean zzfv(String str) {
        return !TextUtils.isEmpty(str) && str.startsWith(EventsFilesManager.ROLL_OVER_FILE_NAME_SEPARATOR);
    }

    static long zzq(byte[] bArr) {
        int i = 0;
        zzx.zzz(bArr);
        zzx.zzab(bArr.length > 0);
        long j = 0;
        int length = bArr.length - 1;
        while (length >= 0 && length >= bArr.length - 8) {
            j += (((long) bArr[length]) & 255) << i;
            i += 8;
            length--;
        }
        return j;
    }

    public /* bridge */ /* synthetic */ Context getContext() {
        return super.getContext();
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

    public void zza(Bundle bundle, String str, Object obj) {
        if (obj instanceof Long) {
            bundle.putLong(str, ((Long) obj).longValue());
        } else if (obj instanceof Float) {
            bundle.putFloat(str, ((Float) obj).floatValue());
        } else if (obj instanceof String) {
            bundle.putString(str, String.valueOf(obj));
        } else if (str != null) {
            zzAo().zzCH().zze("Not putting event parameter. Invalid value type. name, type", str, obj.getClass().getSimpleName());
        }
    }

    public void zza(zzqb.zzc zzc, Object obj) {
        zzx.zzz(obj);
        zzc.zzamJ = null;
        zzc.zzbai = null;
        zzc.zzaZo = null;
        if (obj instanceof String) {
            zzc.zzamJ = (String) obj;
        } else if (obj instanceof Long) {
            zzc.zzbai = (Long) obj;
        } else if (obj instanceof Float) {
            zzc.zzaZo = (Float) obj;
        } else {
            zzAo().zzCE().zzj("Ignoring invalid (type) event param value", obj);
        }
    }

    public void zza(zzqb.zzg zzg, Object obj) {
        zzx.zzz(obj);
        zzg.zzamJ = null;
        zzg.zzbai = null;
        zzg.zzaZo = null;
        if (obj instanceof String) {
            zzg.zzamJ = (String) obj;
        } else if (obj instanceof Long) {
            zzg.zzbai = (Long) obj;
        } else if (obj instanceof Float) {
            zzg.zzaZo = (Float) obj;
        } else {
            zzAo().zzCE().zzj("Ignoring invalid (type) user attribute value", obj);
        }
    }

    public byte[] zza(zzqb.zzd zzd) {
        try {
            byte[] bArr = new byte[zzd.getSerializedSize()];
            zzsn zzE = zzsn.zzE(bArr);
            zzd.writeTo(zzE);
            zzE.zzJo();
            return bArr;
        } catch (IOException e) {
            zzAo().zzCE().zzj("Data loss. Failed to serialize batch", e);
            return null;
        }
    }

    @WorkerThread
    public boolean zzbk(String str) {
        zzjk();
        if (getContext().checkCallingOrSelfPermission(str) == 0) {
            return true;
        }
        zzAo().zzCJ().zzj("Permission not granted", str);
        return false;
    }

    /* access modifiers changed from: package-private */
    public void zzc(String str, int i, String str2) {
        if (str2 == null) {
            throw new IllegalArgumentException(str + " name is required and can't be null");
        } else if (str2.length() == 0) {
            throw new IllegalArgumentException(str + " name is required and can't be empty");
        } else {
            char charAt = str2.charAt(0);
            if (Character.isLetter(charAt) || charAt == '_') {
                int i2 = 1;
                while (i2 < str2.length()) {
                    char charAt2 = str2.charAt(i2);
                    if (charAt2 == '_' || Character.isLetterOrDigit(charAt2)) {
                        i2++;
                    } else {
                        throw new IllegalArgumentException(str + " name must consist of letters, digits or _ (underscores)");
                    }
                }
                if (str2.length() > i) {
                    throw new IllegalArgumentException(str + " name is too long. The maximum supported length is " + i);
                }
                return;
            }
            throw new IllegalArgumentException(str + " name must start with a letter or _");
        }
    }

    public boolean zzc(long j, long j2) {
        return j == 0 || j2 <= 0 || Math.abs(zzjl().currentTimeMillis() - j) > j2;
    }

    public void zzfr(String str) {
        zzc("event", zzCp().zzBB(), str);
    }

    public void zzfs(String str) {
        zzc("user attribute", zzCp().zzBC(), str);
    }

    public void zzft(String str) {
        zzc("event param", zzCp().zzBC(), str);
    }

    public byte[] zzg(byte[] bArr) throws IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
            gZIPOutputStream.write(bArr);
            gZIPOutputStream.close();
            byteArrayOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            zzAo().zzCE().zzj("Failed to gzip content", e);
            throw e;
        }
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

    public Object zzk(String str, Object obj) {
        return zza(zzfv(str) ? zzCp().zzBE() : zzCp().zzBD(), obj, false);
    }

    public void zzl(String str, Object obj) {
        if ("_ldl".equals(str)) {
            zza("user attribute referrer", str, zzfu(str), obj);
        } else {
            zza("user attribute", str, zzfu(str), obj);
        }
    }

    public Object zzm(String str, Object obj) {
        return "_ldl".equals(str) ? zza(zzfu(str), obj, true) : zza(zzfu(str), obj, false);
    }

    public byte[] zzp(byte[] bArr) throws IOException {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
            GZIPInputStream gZIPInputStream = new GZIPInputStream(byteArrayInputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr2 = new byte[1024];
            while (true) {
                int read = gZIPInputStream.read(bArr2);
                if (read <= 0) {
                    gZIPInputStream.close();
                    byteArrayInputStream.close();
                    return byteArrayOutputStream.toByteArray();
                }
                byteArrayOutputStream.write(bArr2, 0, read);
            }
        } catch (IOException e) {
            zzAo().zzCE().zzj("Failed to ungzip content", e);
            throw e;
        }
    }

    public long zzr(byte[] bArr) {
        zzx.zzz(bArr);
        MessageDigest zzbv = zzbv(CommonUtils.MD5_INSTANCE);
        if (zzbv != null) {
            return zzq(zzbv.digest(bArr));
        }
        zzAo().zzCE().zzfg("Failed to get MD5");
        return 0;
    }
}
