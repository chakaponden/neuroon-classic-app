package io.intercom.android.sdk.models;

import android.os.Parcel;
import android.os.Parcelable;

public class LWROption implements Parcelable {
    public static final Parcelable.Creator<LWROption> CREATOR = new Parcelable.Creator<LWROption>() {
        public LWROption createFromParcel(Parcel in) {
            return new LWROption(in);
        }

        public LWROption[] newArray(int size) {
            return new LWROption[size];
        }
    };
    private final String id;

    public LWROption() {
        this(new Builder());
    }

    private LWROption(Builder builder) {
        this.id = builder.id == null ? "" : builder.id;
    }

    public String getId() {
        return this.id;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LWROption lwrOption = (LWROption) o;
        if (this.id != null) {
            if (this.id.equals(lwrOption.id)) {
                return true;
            }
        } else if (lwrOption.id == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        if (this.id != null) {
            return this.id.hashCode();
        }
        return 0;
    }

    protected LWROption(Parcel in) {
        this.id = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public String id;

        public Builder withId(String id2) {
            this.id = id2;
            return this;
        }

        public LWROption build() {
            return new LWROption(this);
        }
    }
}
