package io.intercom.android.sdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import io.intercom.android.sdk.models.Avatar;
import io.intercom.android.sdk.utilities.NameUtils;

public class Participant implements Parcelable {
    public static final String ADMIN_TYPE = "admin";
    public static final Parcelable.Creator<Participant> CREATOR = new Parcelable.Creator<Participant>() {
        public Participant createFromParcel(Parcel in) {
            return new Participant(in);
        }

        public Participant[] newArray(int size) {
            return new Participant[size];
        }
    };
    public static final String USER_TYPE = "user";
    private final Avatar avatar;
    private final String email;
    private final String id;
    private final String name;
    private final String type;

    public Participant() {
        this(new Builder());
    }

    private Participant(Builder builder) {
        this.id = builder.id == null ? "" : builder.id;
        this.name = builder.name == null ? "" : builder.name;
        this.type = builder.type == null ? USER_TYPE : builder.type;
        this.email = builder.email == null ? "" : builder.email;
        this.avatar = builder.avatar == null ? new Avatar.Builder().withInitials(NameUtils.getInitials(getDisplayName())).build() : builder.avatar.withInitials(NameUtils.getInitials(getDisplayName())).build();
    }

    public String getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public String getEmail() {
        return this.email;
    }

    public Avatar getAvatar() {
        return this.avatar;
    }

    public boolean isAdmin() {
        return ADMIN_TYPE.equals(this.type);
    }

    public String getDisplayName() {
        return this.name.isEmpty() ? this.email : this.name;
    }

    public boolean hasDisplayName() {
        return !this.name.isEmpty() || !this.email.isEmpty();
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public Avatar.Builder avatar;
        /* access modifiers changed from: private */
        public String email;
        /* access modifiers changed from: private */
        public String id;
        /* access modifiers changed from: private */
        public String name;
        /* access modifiers changed from: private */
        public String type;

        public Participant build() {
            return new Participant(this);
        }

        public Builder withId(String id2) {
            this.id = id2;
            return this;
        }

        public Builder withAvatar(Avatar.Builder avatar2) {
            this.avatar = avatar2;
            return this;
        }

        public Builder withName(String name2) {
            this.name = name2;
            return this;
        }

        public Builder withType(String type2) {
            this.type = type2;
            return this;
        }
    }

    public static final class NullParticipant extends Participant {
        public NullParticipant() {
            super(new Builder());
        }
    }

    protected Participant(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.type = in.readString();
        this.email = in.readString();
        this.avatar = (Avatar) in.readValue(Avatar.class.getClassLoader());
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.type);
        dest.writeString(this.email);
        dest.writeValue(this.avatar);
    }
}
