package com.google.android.gms.tagmanager;

import android.util.Base64;
import com.google.android.gms.internal.zzae;
import com.google.android.gms.internal.zzag;
import io.intercom.android.sdk.utilities.AttachmentUtils;
import java.util.Map;

class zzad extends zzak {
    private static final String ID = com.google.android.gms.internal.zzad.ENCODE.toString();
    private static final String zzbiQ = zzae.ARG0.toString();
    private static final String zzbiR = zzae.NO_PADDING.toString();
    private static final String zzbiS = zzae.INPUT_FORMAT.toString();
    private static final String zzbiT = zzae.OUTPUT_FORMAT.toString();

    public zzad() {
        super(ID, zzbiQ);
    }

    public boolean zzFW() {
        return true;
    }

    public zzag.zza zzP(Map<String, zzag.zza> map) {
        byte[] decode;
        String encodeToString;
        zzag.zza zza = map.get(zzbiQ);
        if (zza == null || zza == zzdf.zzHF()) {
            return zzdf.zzHF();
        }
        String zzg = zzdf.zzg(zza);
        zzag.zza zza2 = map.get(zzbiS);
        String zzg2 = zza2 == null ? AttachmentUtils.MIME_TYPE_TEXT : zzdf.zzg(zza2);
        zzag.zza zza3 = map.get(zzbiT);
        String zzg3 = zza3 == null ? "base16" : zzdf.zzg(zza3);
        zzag.zza zza4 = map.get(zzbiR);
        int i = (zza4 == null || !zzdf.zzk(zza4).booleanValue()) ? 2 : 3;
        try {
            if (AttachmentUtils.MIME_TYPE_TEXT.equals(zzg2)) {
                decode = zzg.getBytes();
            } else if ("base16".equals(zzg2)) {
                decode = zzk.zzfO(zzg);
            } else if ("base64".equals(zzg2)) {
                decode = Base64.decode(zzg, i);
            } else if ("base64url".equals(zzg2)) {
                decode = Base64.decode(zzg, i | 8);
            } else {
                zzbg.e("Encode: unknown input format: " + zzg2);
                return zzdf.zzHF();
            }
            if ("base16".equals(zzg3)) {
                encodeToString = zzk.zzj(decode);
            } else if ("base64".equals(zzg3)) {
                encodeToString = Base64.encodeToString(decode, i);
            } else if ("base64url".equals(zzg3)) {
                encodeToString = Base64.encodeToString(decode, i | 8);
            } else {
                zzbg.e("Encode: unknown output format: " + zzg3);
                return zzdf.zzHF();
            }
            return zzdf.zzR(encodeToString);
        } catch (IllegalArgumentException e) {
            zzbg.e("Encode: invalid input:");
            return zzdf.zzHF();
        }
    }
}
