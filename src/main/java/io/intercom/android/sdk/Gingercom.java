package io.intercom.android.sdk;

import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import io.intercom.android.sdk.identity.Registration;
import io.intercom.android.sdk.preview.IntercomPreviewPosition;
import io.intercom.android.sdk.utilities.IntercomUtils;
import java.util.Map;

public class Gingercom extends Intercom {
    public Gingercom(Context context) {
        IntercomUtils.changeComponentState(2, context);
    }

    public void registerUnidentifiedUser() {
    }

    public void registerIdentifiedUser(Registration userRegistration) {
    }

    public void setSecureMode(String secureHash, String secureData) {
    }

    public void updateUser(Map<String, ?> map) {
    }

    public void logEvent(String name) {
    }

    public void logEvent(String name, Map<String, ?> map) {
    }

    public void displayMessageComposer() {
    }

    public void displayConversationsList() {
    }

    public void setPreviewPosition(IntercomPreviewPosition previewPosition) {
    }

    public void setVisibility(int visibility) {
    }

    public void setMessagesHidden(boolean visibility) {
    }

    public void setupGCM(String regId, int appLogo) {
    }

    public void openGCMMessage(Intent intent) {
    }

    public void openGCMMessage(Intent intent, TaskStackBuilder customStack) {
    }

    public boolean openGCMMessage(Uri data) {
        return false;
    }

    public void reset() {
    }
}
