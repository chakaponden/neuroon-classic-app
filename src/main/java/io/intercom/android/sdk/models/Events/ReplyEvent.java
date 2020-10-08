package io.intercom.android.sdk.models.Events;

import io.intercom.android.sdk.models.Part;

public class ReplyEvent {
    private final String conversationId;
    private final String partId;
    private final int position;
    private final Part response;

    public ReplyEvent(Part response2, int position2, String partId2, String conversationId2) {
        this.response = response2;
        this.partId = partId2;
        this.position = position2;
        this.conversationId = conversationId2;
    }

    public Part getResponse() {
        return this.response;
    }

    public String getPartId() {
        return this.partId;
    }

    public int getPosition() {
        return this.position;
    }

    public String getConversationId() {
        return this.conversationId;
    }
}
