package io.intercom.android.sdk.models;

import io.intercom.android.sdk.models.BaseResponse;
import io.intercom.android.sdk.models.ConversationList;

public class UsersResponse extends BaseResponse {
    private final ConversationList unreadConversations;

    private UsersResponse(Builder builder) {
        super(builder);
        this.unreadConversations = builder.unread_conversations == null ? new ConversationList() : builder.unread_conversations.build();
    }

    public ConversationList getUnreadConversations() {
        return this.unreadConversations;
    }

    public static final class Builder extends BaseResponse.Builder {
        /* access modifiers changed from: private */
        public ConversationList.Builder unread_conversations;

        public UsersResponse build() {
            return new UsersResponse(this);
        }
    }
}
