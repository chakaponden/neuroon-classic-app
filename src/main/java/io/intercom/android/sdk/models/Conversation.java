package io.intercom.android.sdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import io.intercom.android.sdk.models.Part;
import io.intercom.android.sdk.models.Participant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class Conversation implements Parcelable {
    public static final Parcelable.Creator<Conversation> CREATOR = new Parcelable.Creator<Conversation>() {
        public Conversation createFromParcel(Parcel in) {
            return new Conversation(in);
        }

        public Conversation[] newArray(int size) {
            return new Conversation[size];
        }
    };
    private final List<Part> conversationParts;
    private final String id;
    private final Map<String, Participant> participants;
    private boolean read;

    public Conversation() {
        this(new Builder());
    }

    private Conversation(Builder builder) {
        this.id = builder.id == null ? "" : builder.id;
        this.read = builder.read;
        this.participants = new LinkedHashMap();
        if (builder.participants != null) {
            for (Participant.Builder participantBuilder : builder.participants) {
                Participant participant = participantBuilder.build();
                this.participants.put(participant.getId(), participant);
            }
        }
        this.conversationParts = new ArrayList();
        if (builder.conversation_parts != null) {
            for (Part.Builder partBuilder : builder.conversation_parts) {
                Part part = partBuilder.build();
                part.setParticipant(getParticipant(part.getParticipantId()));
                this.conversationParts.add(part);
            }
        }
    }

    public List<Part> getParts() {
        return this.conversationParts;
    }

    public Part getLastPart() {
        return this.conversationParts.isEmpty() ? new Part.NullPart() : this.conversationParts.get(this.conversationParts.size() - 1);
    }

    public Part getLastAdminPart() {
        Part lastAdminPart = getLastPart();
        return lastAdminPart.isAdmin() ? lastAdminPart : new Part.NullPart();
    }

    public Participant getParticipant(String id2) {
        Participant participant = this.participants.get(id2);
        if (participant == null) {
            return new Participant.NullParticipant();
        }
        return participant;
    }

    public String getId() {
        return this.id;
    }

    public Boolean getRead() {
        return Boolean.valueOf(this.read);
    }

    public void setRead(Boolean read2) {
        this.read = read2.booleanValue();
    }

    public Participant getLastAdmin() {
        Participant lastAdmin = getParticipant(getLastAdminPart().getParticipantId());
        if (!(lastAdmin instanceof Participant.NullParticipant) || this.participants.size() <= 1) {
            return lastAdmin;
        }
        ListIterator<Map.Entry<String, Participant>> iterator = new ArrayList(this.participants.entrySet()).listIterator(this.participants.size());
        while (iterator.hasPrevious()) {
            Participant participant = this.participants.get(iterator.previous().getKey());
            if (participant.isAdmin()) {
                return participant;
            }
        }
        return lastAdmin;
    }

    public Map<String, Participant> getParticipants() {
        return this.participants;
    }

    protected Conversation(Parcel in) {
        this.id = in.readString();
        this.read = in.readByte() != 0;
        this.conversationParts = new ArrayList();
        if (in.readByte() == 1) {
            in.readList(this.conversationParts, Part.class.getClassLoader());
        }
        this.participants = new LinkedHashMap();
        if (in.readByte() == 1) {
            in.readHashMap(Participant.class.getClassLoader());
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeByte((byte) (this.read ? 1 : 0));
        if (this.conversationParts == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeList(this.conversationParts);
        }
        if (this.participants == null) {
            dest.writeByte((byte) 0);
            return;
        }
        dest.writeByte((byte) 1);
        dest.writeMap(this.participants);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Conversation that = (Conversation) o;
        if (this.read != that.read) {
            return false;
        }
        if (this.conversationParts == null ? that.conversationParts != null : !this.conversationParts.equals(that.conversationParts)) {
            return false;
        }
        if (this.id != null) {
            if (this.id.equals(that.id)) {
                return true;
            }
        } else if (that.id == null) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int result;
        int i;
        int i2 = 0;
        if (this.id != null) {
            result = this.id.hashCode();
        } else {
            result = 0;
        }
        int i3 = result * 31;
        if (this.read) {
            i = 1;
        } else {
            i = 0;
        }
        int i4 = (i3 + i) * 31;
        if (this.conversationParts != null) {
            i2 = this.conversationParts.hashCode();
        }
        return i4 + i2;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public List<Part.Builder> conversation_parts;
        /* access modifiers changed from: private */
        public String id;
        /* access modifiers changed from: private */
        public List<Participant.Builder> participants;
        /* access modifiers changed from: private */
        public boolean read;

        public Builder withId(String id2) {
            this.id = id2;
            return this;
        }

        public Builder withRead(boolean read2) {
            this.read = read2;
            return this;
        }

        public Builder withParticipants(List<Participant.Builder> participants2) {
            this.participants = participants2;
            return this;
        }

        public Builder withParts(List<Part.Builder> parts) {
            this.conversation_parts = parts;
            return this;
        }

        public Conversation build() {
            return new Conversation(this);
        }
    }

    public static final class Loading extends Conversation {
        public Loading() {
            super(new Builder());
        }
    }
}
