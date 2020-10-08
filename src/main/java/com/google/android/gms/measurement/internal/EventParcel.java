package com.google.android.gms.measurement.internal;

import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public final class EventParcel implements SafeParcelable {
    public static final zzk CREATOR = new zzk();
    public final String name;
    public final int versionCode;
    public final EventParams zzaVV;
    public final String zzaVW;
    public final long zzaVX;

    EventParcel(int versionCode2, String name2, EventParams params, String origin, long eventTimeInMilliseconds) {
        this.versionCode = versionCode2;
        this.name = name2;
        this.zzaVV = params;
        this.zzaVW = origin;
        this.zzaVX = eventTimeInMilliseconds;
    }

    public EventParcel(String name2, EventParams params, String origin, long eventTimeInMilliseconds) {
        this.versionCode = 1;
        this.name = name2;
        this.zzaVV = params;
        this.zzaVW = origin;
        this.zzaVX = eventTimeInMilliseconds;
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "origin=" + this.zzaVW + ",name=" + this.name + ",params=" + this.zzaVV;
    }

    public void writeToParcel(Parcel out, int flags) {
        zzk.zza(this, out, flags);
    }
}
