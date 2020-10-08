package io.intercom.android.sdk.models.Events;

public class UploadEvent {
    private final String tempPartId;
    private final int tempPartPosition;
    private final int uploadId;

    public UploadEvent(int uploadId2, int tempPartPosition2, String tempPartId2) {
        this.uploadId = uploadId2;
        this.tempPartPosition = tempPartPosition2;
        this.tempPartId = tempPartId2;
    }

    public int getUploadId() {
        return this.uploadId;
    }

    public int getTempPartPosition() {
        return this.tempPartPosition;
    }

    public String getTempPartId() {
        return this.tempPartId;
    }
}
