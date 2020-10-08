package com.google.android.gms.tagmanager;

import com.google.android.gms.internal.zzad;
import com.google.android.gms.internal.zzae;
import com.google.android.gms.internal.zzag;
import io.fabric.sdk.android.services.common.CommonUtils;
import io.intercom.android.sdk.utilities.AttachmentUtils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

class zzap extends zzak {
    private static final String ID = zzad.HASH.toString();
    private static final String zzbiQ = zzae.ARG0.toString();
    private static final String zzbiS = zzae.INPUT_FORMAT.toString();
    private static final String zzbiW = zzae.ALGORITHM.toString();

    public zzap() {
        super(ID, zzbiQ);
    }

    private byte[] zzg(String str, byte[] bArr) throws NoSuchAlgorithmException {
        MessageDigest instance = MessageDigest.getInstance(str);
        instance.update(bArr);
        return instance.digest();
    }

    public boolean zzFW() {
        return true;
    }

    public zzag.zza zzP(Map<String, zzag.zza> map) {
        byte[] zzfO;
        zzag.zza zza = map.get(zzbiQ);
        if (zza == null || zza == zzdf.zzHF()) {
            return zzdf.zzHF();
        }
        String zzg = zzdf.zzg(zza);
        zzag.zza zza2 = map.get(zzbiW);
        String zzg2 = zza2 == null ? CommonUtils.MD5_INSTANCE : zzdf.zzg(zza2);
        zzag.zza zza3 = map.get(zzbiS);
        String zzg3 = zza3 == null ? AttachmentUtils.MIME_TYPE_TEXT : zzdf.zzg(zza3);
        if (AttachmentUtils.MIME_TYPE_TEXT.equals(zzg3)) {
            zzfO = zzg.getBytes();
        } else if ("base16".equals(zzg3)) {
            zzfO = zzk.zzfO(zzg);
        } else {
            zzbg.e("Hash: unknown input format: " + zzg3);
            return zzdf.zzHF();
        }
        try {
            return zzdf.zzR(zzk.zzj(zzg(zzg2, zzfO)));
        } catch (NoSuchAlgorithmException e) {
            zzbg.e("Hash: unknown algorithm: " + zzg2);
            return zzdf.zzHF();
        }
    }
}
