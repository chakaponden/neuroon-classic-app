package io.intercom.android.sdk.models.Events.Failure;

public class ReplyFailedEvent {
    private final boolean isUpload;
    private final String partId;
    private final int position;

    public ReplyFailedEvent(int position2, boolean isUpload2, String partId2) {
        this.position = position2;
        this.partId = partId2;
        this.isUpload = isUpload2;
    }

    public String getPartId() {
        return this.partId;
    }

    public int getPosition() {
        return this.position;
    }

    public boolean isUpload() {
        return this.isUpload;
    }
}
