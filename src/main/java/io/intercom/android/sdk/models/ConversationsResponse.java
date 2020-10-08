package io.intercom.android.sdk.models;

import io.intercom.android.sdk.models.BaseResponse;
import io.intercom.android.sdk.models.ConversationList;

public class ConversationsResponse extends BaseResponse {
    private final ConversationList conversationPage;

    private ConversationsResponse(Builder builder) {
        super(builder);
        this.conversationPage = builder.conversation_page == null ? new ConversationList() : builder.conversation_page.build();
    }

    public ConversationList getConversationPage() {
        return this.conversationPage;
    }

    public static final class Builder extends BaseResponse.Builder {
        /* access modifiers changed from: private */
        public ConversationList.Builder conversation_page;

        public ConversationsResponse build() {
            return new ConversationsResponse(this);
        }
    }
}
