package io.intercom.android.sdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import io.intercom.android.sdk.utilities.Constants;

public class Avatar implements Parcelable {
    public static final Parcelable.Creator<Avatar> CREATOR = new Parcelable.Creator<Avatar>() {
        public Avatar createFromParcel(Parcel in) {
            return new Avatar(in);
        }

        public Avatar[] newArray(int size) {
            return new Avatar[size];
        }
    };
    private final String color;
    private final String imageUrl;
    private final String initials;

    public Avatar() {
        this(new Builder());
    }

    private Avatar(Builder builder) {
        this.imageUrl = builder.image_url == null ? "" : builder.image_url;
        this.initials = builder.initials == null ? "" : builder.initials;
        this.color = builder.color == null ? Constants.DEFAULT_COLOR : builder.color;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public String getColor() {
        return this.color;
    }

    public String getInitials() {
        return this.initials;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Avatar avatar = (Avatar) o;
        if (this.imageUrl != null) {
            if (!this.imageUrl.equals(avatar.imageUrl)) {
                return false;
            }
        } else if (avatar.imageUrl != null) {
            return false;
        }
        if (this.initials != null) {
            if (!this.initials.equals(avatar.initials)) {
                return false;
            }
        } else if (avatar.initials != null) {
            return false;
        }
        if (this.color == null ? avatar.color != null : !this.color.equals(avatar.color)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i;
        int i2 = 0;
        if (this.imageUrl != null) {
            result = this.imageUrl.hashCode();
        } else {
            result = 0;
        }
        int i3 = result * 31;
        if (this.initials != null) {
            i = this.initials.hashCode();
        } else {
            i = 0;
        }
        int i4 = (i3 + i) * 31;
        if (this.color != null) {
            i2 = this.color.hashCode();
        }
        return i4 + i2;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageUrl);
        dest.writeString(this.initials);
        dest.writeString(this.color);
    }

    private Avatar(Parcel in) {
        this.imageUrl = in.readString();
        this.initials = in.readString();
        this.color = in.readString();
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public String color;
        /* access modifiers changed from: private */
        public String image_url;
        /* access modifiers changed from: private */
        public String initials;

        public Builder withImageUrl(String imageUrl) {
            this.image_url = imageUrl;
            return this;
        }

        public Builder withInitials(String initials2) {
            this.initials = initials2;
            return this;
        }

        public Builder withColor(String color2) {
            this.color = color2;
            return this;
        }

        public Avatar build() {
            return new Avatar(this);
        }
    }
}
