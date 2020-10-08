package io.fabric.sdk.android.services.settings;

public class PromptSettingsData {
    public final String alwaysSendButtonTitle;
    public final String cancelButtonTitle;
    public final String message;
    public final String sendButtonTitle;
    public final boolean showAlwaysSendButton;
    public final boolean showCancelButton;
    public final String title;

    public PromptSettingsData(String title2, String message2, String sendButtonTitle2, boolean showCancelButton2, String cancelButtonTitle2, boolean showAlwaysSendButton2, String alwaysSendButtonTitle2) {
        this.title = title2;
        this.message = message2;
        this.sendButtonTitle = sendButtonTitle2;
        this.showCancelButton = showCancelButton2;
        this.cancelButtonTitle = cancelButtonTitle2;
        this.showAlwaysSendButton = showAlwaysSendButton2;
        this.alwaysSendButtonTitle = alwaysSendButtonTitle2;
    }
}
