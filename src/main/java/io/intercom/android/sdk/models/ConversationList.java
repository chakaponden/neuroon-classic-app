package io.intercom.android.sdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import io.intercom.android.sdk.models.Conversation;
import java.util.ArrayList;
import java.util.List;

public class ConversationList implements Parcelable {
    public static final Parcelable.Creator<ConversationList> CREATOR = new Parcelable.Creator<ConversationList>() {
        public ConversationList createFromParcel(Parcel in) {
            return new ConversationList(in);
        }

        public ConversationList[] newArray(int size) {
            return new ConversationList[size];
        }
    };
    private final List<Conversation> conversations;
    private final boolean morePagesAvailable;
    private final int totalUnreadCount;

    public ConversationList() {
        this(new Builder());
    }

    private ConversationList(Builder builder) {
        this.conversations = new ArrayList();
        if (builder.conversations != null) {
            for (Conversation.Builder conversationBuilder : builder.conversations) {
                this.conversations.add(conversationBuilder.build());
            }
        }
        this.totalUnreadCount = Math.max(builder.total_count, builder.total_unread_count);
        this.morePagesAvailable = builder.more_pages_available;
    }

    public List<Conversation> getConversations() {
        return this.conversations;
    }

    public int getTotalUnreadCount() {
        return this.totalUnreadCount;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConversationList that = (ConversationList) o;
        if (this.totalUnreadCount != that.totalUnreadCount || this.morePagesAvailable != that.morePagesAvailable) {
            return false;
        }
        if (this.conversations == null ? that.conversations != null : !this.conversations.equals(that.conversations)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i = 0;
        if (this.conversations != null) {
            result = this.conversations.hashCode();
        } else {
            result = 0;
        }
        int i2 = ((result * 31) + this.totalUnreadCount) * 31;
        if (this.morePagesAvailable) {
            i = 1;
        }
        return i2 + i;
    }

    protected ConversationList(Parcel in) {
        boolean z = true;
        this.conversations = new ArrayList();
        if (in.readByte() == 1) {
            in.readList(this.conversations, Conversation.class.getClassLoader());
        }
        this.totalUnreadCount = in.readInt();
        this.morePagesAvailable = in.readByte() == 0 ? false : z;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i = 1;
        if (this.conversations == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeList(this.conversations);
        }
        dest.writeInt(this.totalUnreadCount);
        if (!this.morePagesAvailable) {
            i = 0;
        }
        dest.writeByte((byte) i);
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public List<Conversation.Builder> conversations;
        /* access modifiers changed from: private */
        public boolean more_pages_available;
        /* access modifiers changed from: private */
        public int total_count;
        /* access modifiers changed from: private */
        public int total_unread_count;

        public Builder withConversations(List<Conversation.Builder> conversations2) {
            this.conversations = conversations2;
            return this;
        }

        public Builder withTotalUnreadCount(int unreadCount) {
            this.total_unread_count = unreadCount;
            return this;
        }

        public Builder withMorePagesAvailable(boolean morePagesAvailable) {
            this.more_pages_available = morePagesAvailable;
            return this;
        }

        public ConversationList build() {
            return new ConversationList(this);
        }
    }
}
