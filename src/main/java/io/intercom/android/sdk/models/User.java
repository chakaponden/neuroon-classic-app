package io.intercom.android.sdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import io.intercom.android.sdk.models.Avatar;
import io.intercom.com.google.gson.annotations.SerializedName;

public class User implements Parcelable {
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
    @SerializedName("anonymous_id")
    private final String anonymousId;
    private final String email;
    @SerializedName("intercom_id")
    private final String intercomId;
    private final String name;
    private final String type;
    @SerializedName("user_id")
    private final String userId;

    public User() {
        this("");
    }

    public User(String intercomId2) {
        this.type = Participant.USER_TYPE;
        this.intercomId = intercomId2;
        this.anonymousId = "";
        this.userId = "";
        this.name = "";
        this.email = "";
    }

    private User(Builder builder) {
        this.type = builder.type == null ? Participant.USER_TYPE : builder.type;
        this.intercomId = builder.intercom_id == null ? "" : builder.intercom_id;
        this.anonymousId = builder.anonymous_id == null ? "" : builder.anonymous_id;
        this.userId = builder.user_id == null ? "" : builder.user_id;
        this.name = builder.name == null ? "" : builder.name;
        this.email = builder.email == null ? "" : builder.email;
    }

    protected User(Parcel in) {
        this.type = in.readString();
        this.intercomId = in.readString();
        this.anonymousId = in.readString();
        this.userId = in.readString();
        this.name = in.readString();
        this.email = in.readString();
    }

    public String getType() {
        return this.type;
    }

    public String getIntercomId() {
        return this.intercomId;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getAnonymousId() {
        return this.anonymousId;
    }

    public boolean equals(Object obj) {
        if (obj instanceof User) {
            return this.intercomId.equals(((User) obj).intercomId);
        }
        return false;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.intercomId);
        dest.writeString(this.anonymousId);
        dest.writeString(this.userId);
        dest.writeString(this.name);
        dest.writeString(this.email);
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public String anonymous_id;
        private Avatar.Builder avatar;
        /* access modifiers changed from: private */
        public String email;
        /* access modifiers changed from: private */
        public String intercom_id;
        /* access modifiers changed from: private */
        public String name;
        /* access modifiers changed from: private */
        public String type;
        /* access modifiers changed from: private */
        public String user_id;

        public Builder withType(String type2) {
            this.type = type2;
            return this;
        }

        public Builder withIntercomId(String intercomId) {
            this.intercom_id = intercomId;
            return this;
        }

        public Builder withAnonymousId(String anonymousId) {
            this.anonymous_id = anonymousId;
            return this;
        }

        public Builder withUserId(String userId) {
            this.user_id = userId;
            return this;
        }

        public Builder withName(String name2) {
            this.name = name2;
            return this;
        }

        public Builder withEmail(String email2) {
            this.email = email2;
            return this;
        }

        public Builder withAvatar(Avatar.Builder avatar2) {
            this.avatar = avatar2;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
