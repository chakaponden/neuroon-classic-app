package com.google.android.gms.analytics.internal;

import android.os.Parcel;
import android.os.Parcelable;

public class Command implements Parcelable {
    @Deprecated
    public static final Parcelable.Creator<Command> CREATOR = new Parcelable.Creator<Command>() {
        @Deprecated
        /* renamed from: zzag */
        public Command[] newArray(int i) {
            return new Command[i];
        }

        @Deprecated
        /* renamed from: zzq */
        public Command createFromParcel(Parcel parcel) {
            return new Command(parcel);
        }
    };
    private String mValue;
    private String zzRt;
    private String zzyv;

    @Deprecated
    public Command() {
    }

    @Deprecated
    Command(Parcel in) {
        readFromParcel(in);
    }

    @Deprecated
    private void readFromParcel(Parcel in) {
        this.zzyv = in.readString();
        this.zzRt = in.readString();
        this.mValue = in.readString();
    }

    @Deprecated
    public int describeContents() {
        return 0;
    }

    public String getId() {
        return this.zzyv;
    }

    public String getValue() {
        return this.mValue;
    }

    @Deprecated
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.zzyv);
        out.writeString(this.zzRt);
        out.writeString(this.mValue);
    }
}
