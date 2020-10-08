package io.intercom.android.sdk.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Attachments implements Parcelable {
    public static final Parcelable.Creator<Attachments> CREATOR = new Parcelable.Creator<Attachments>() {
        public Attachments createFromParcel(Parcel in) {
            return new Attachments(in);
        }

        public Attachments[] newArray(int size) {
            return new Attachments[size];
        }
    };
    private final String contentType;
    private final String name;
    private final String url;

    private Attachments(Builder builder) {
        this.name = builder.name == null ? "" : builder.name;
        this.url = builder.url == null ? "" : builder.url;
        this.contentType = builder.content_type == null ? "" : builder.content_type;
    }

    public Attachments(String name2, String url2, String contentType2) {
        this.name = name2;
        this.url = url2;
        this.contentType = contentType2;
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
        public String content_type;
        /* access modifiers changed from: private */
        public String name;
        /* access modifiers changed from: private */
        public String url;

        public Attachments build() {
            return new Attachments(this);
        }
    }

    protected Attachments(Parcel in) {
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
