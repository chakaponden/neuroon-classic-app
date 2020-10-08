package io.intercom.android.sdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class LWR implements Parcelable {
    public static final Parcelable.Creator<LWR> CREATOR = new Parcelable.Creator<LWR>() {
        public LWR createFromParcel(Parcel in) {
            return new LWR(in);
        }

        public LWR[] newArray(int size) {
            return new LWR[size];
        }
    };
    private String option;
    private final List<LWROption> options;
    private final String type;

    public LWR() {
        this(new Builder());
    }

    private LWR(Builder builder) {
        this.type = builder.type == null ? "" : builder.type;
        this.options = builder.options == null ? new ArrayList<>() : builder.options;
        this.option = builder.option == null ? "" : builder.option;
    }

    public String getType() {
        return this.type;
    }

    public List<LWROption> getOptions() {
        return this.options;
    }

    public String getOption() {
        return this.option;
    }

    public void setOption(String option2) {
        this.option = option2;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LWR lwr = (LWR) o;
        if (this.type != null) {
            if (!this.type.equals(lwr.type)) {
                return false;
            }
        } else if (lwr.type != null) {
            return false;
        }
        if (this.options != null) {
            if (!this.options.equals(lwr.options)) {
                return false;
            }
        } else if (lwr.options != null) {
            return false;
        }
        if (this.option == null ? lwr.option != null : !this.option.equals(lwr.option)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i;
        int i2 = 0;
        if (this.type != null) {
            result = this.type.hashCode();
        } else {
            result = 0;
        }
        int i3 = result * 31;
        if (this.options != null) {
            i = this.options.hashCode();
        } else {
            i = 0;
        }
        int i4 = (i3 + i) * 31;
        if (this.option != null) {
            i2 = this.option.hashCode();
        }
        return i4 + i2;
    }

    protected LWR(Parcel in) {
        this.type = in.readString();
        if (in.readByte() == 1) {
            this.options = new ArrayList();
            in.readList(this.options, LWROption.class.getClassLoader());
        } else {
            this.options = null;
        }
        this.option = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        if (this.options == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeList(this.options);
        }
        dest.writeString(this.option);
    }

    public static class Builder {
        /* access modifiers changed from: private */
        public String option;
        /* access modifiers changed from: private */
        public List<LWROption> options;
        /* access modifiers changed from: private */
        public String type;

        public Builder withType(String type2) {
            this.type = type2;
            return this;
        }

        public Builder withOptions(List<LWROption> options2) {
            this.options = options2;
            return this;
        }

        public Builder withOption(String option2) {
            this.option = option2;
            return this;
        }

        public LWR build() {
            return new LWR(this);
        }
    }

    public static final class NullLWR extends LWR {
        public NullLWR() {
            super(new Builder());
        }
    }
}
