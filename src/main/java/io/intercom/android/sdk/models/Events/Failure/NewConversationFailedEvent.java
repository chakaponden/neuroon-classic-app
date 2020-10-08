package io.intercom.android.sdk.models.Events.Failure;

public class NewConversationFailedEvent {
    private final String partId;
    private final int position;

    public NewConversationFailedEvent(int position2, String partId2) {
        this.position = position2;
        this.partId = partId2;
    }

    public String getPartId() {
        return this.partId;
    }

    public int getPosition() {
        return this.position;
    }
}
