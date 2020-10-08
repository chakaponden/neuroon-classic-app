package io.intercom.android.sdk.conversation.events;

public class SendingEvent {
    private final boolean isAttachment;

    public SendingEvent(boolean isAttachment2) {
        this.isAttachment = isAttachment2;
    }

    public boolean isAttachment() {
        return this.isAttachment;
    }
}
