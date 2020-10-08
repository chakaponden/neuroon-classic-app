package com.google.android.gms.measurement.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.common.internal.safeparcel.zzb;

public class zzk implements Parcelable.Creator<EventParcel> {
    static void zza(EventParcel eventParcel, Parcel parcel, int i) {
        int zzav = zzb.zzav(parcel);
        zzb.zzc(parcel, 1, eventParcel.versionCode);
        zzb.zza(parcel, 2, eventParcel.name, false);
        zzb.zza(parcel, 3, (Parcelable) eventParcel.zzaVV, i, false);
        zzb.zza(parcel, 4, eventParcel.zzaVW, false);
        zzb.zza(parcel, 5, eventParcel.zzaVX);
        zzb.zzI(parcel, zzav);
    }

    /* renamed from: zzfN */
    public EventParcel createFromParcel(Parcel parcel) {
        String str = null;
        int zzau = zza.zzau(parcel);
        int i = 0;
        long j = 0;
        EventParams eventParams = null;
        String str2 = null;
        while (parcel.dataPosition() < zzau) {
            int zzat = zza.zzat(parcel);
            switch (zza.zzca(zzat)) {
                case 1:
                    i = zza.zzg(parcel, zzat);
                    break;
                case 2:
                    str2 = zza.zzp(parcel, zzat);
                    break;
                case 3:
                    eventParams = (EventParams) zza.zza(parcel, zzat, EventParams.CREATOR);
                    break;
                case 4:
                    str = zza.zzp(parcel, zzat);
                    break;
                case 5:
                    j = zza.zzi(parcel, zzat);
                    break;
                default:
                    zza.zzb(parcel, zzat);
                    break;
            }
        }
        if (parcel.dataPosition() == zzau) {
            return new EventParcel(i, str2, eventParams, str, j);
        }
        throw new zza.C0004zza("Overread allowed size end=" + zzau, parcel);
    }

    /* renamed from: zziJ */
    public EventParcel[] newArray(int i) {
        return new EventParcel[i];
    }
}
