package io.intercom.android.sdk.blocks.models;

import android.os.Parcel;
import android.os.Parcelable;

public class BlockAttachment implements Parcelable {
    public static final Parcelable.Creator<BlockAttachment> CREATOR = new Parcelable.Creator<BlockAttachment>() {
        public BlockAttachment createFromParcel(Parcel in) {
            return new BlockAttachment(in);
        }

        public BlockAttachment[] newArray(int size) {
            return new BlockAttachment[size];
        }
    };
    private final String contentType;
    private final String name;
    private final String url;

    public BlockAttachment() {
        this(new Builder());
    }

    public BlockAttachment(Builder builder) {
        this.name = builder.name == null ? "" : builder.name;
        this.url = builder.url == null ? "" : builder.url;
        this.contentType = builder.contentType == null ? "" : builder.contentType;
    }

    public String getName() {
        return this.name;
    }

    public String getUrl() {
        return this.url;
    }

    public String getContentType() {
        return this.contentType;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public String contentType;
        /* access modifiers changed from: private */
        public String name;
        /* access modifiers changed from: private */
        public String url;

        public Builder withName(String name2) {
            this.name = name2;
            return this;
        }

        public Builder withUrl(String url2) {
            this.url = url2;
            return this;
        }

        public Builder withContentType(String contentType2) {
            this.contentType = contentType2;
            return this;
        }

        public BlockAttachment build() {
            return new BlockAttachment(this);
        }
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BlockAttachment that = (BlockAttachment) o;
        if (this.name != null) {
            if (!this.name.equals(that.name)) {
                return false;
            }
        } else if (that.name != null) {
            return false;
        }
        if (this.contentType != null) {
            if (!this.contentType.equals(that.contentType)) {
                return false;
            }
        } else if (that.contentType != null) {
            return false;
        }
        if (this.url == null ? that.url != null : !this.url.equals(that.url)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i;
        int i2 = 0;
        if (this.name != null) {
            result = this.name.hashCode();
        } else {
            result = 0;
        }
        int i3 = result * 31;
        if (this.contentType != null) {
            i = this.contentType.hashCode();
        } else {
            i = 0;
        }
        int i4 = (i3 + i) * 31;
        if (this.url != null) {
            i2 = this.url.hashCode();
        }
        return i4 + i2;
    }

    protected BlockAttachment(Parcel in) {
        this.name = in.readString();
        this.url = in.readString();
        this.contentType = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.url);
        dest.writeString(this.contentType);
    }
}
