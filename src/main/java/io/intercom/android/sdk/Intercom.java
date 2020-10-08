package io.intercom.android.sdk;

import android.app.Application;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import io.intercom.android.sdk.identity.Registration;
import io.intercom.android.sdk.logger.IntercomLogger;
import io.intercom.android.sdk.preview.IntercomPreviewPosition;
import io.intercom.android.sdk.utilities.IntercomUtils;
import java.util.Map;

public abstract class Intercom {
    public static final String GCM_RECEIVER = "intercom_sdk";
    public static final int GONE = 8;
    public static final int VISIBLE = 0;
    private static Intercom instance;

    public abstract void displayConversationsList();

    public abstract void displayMessageComposer();

    public abstract void logEvent(String str);

    public abstract void logEvent(String str, Map<String, ?> map);

    public abstract void openGCMMessage(Intent intent);

    public abstract void openGCMMessage(Intent intent, TaskStackBuilder taskStackBuilder);

    @Deprecated
    public abstract boolean openGCMMessage(Uri uri);

    public abstract void registerIdentifiedUser(Registration registration);

    public abstract void registerUnidentifiedUser();

    public abstract void reset();

    @Deprecated
    public abstract void setMessagesHidden(boolean z);

    public abstract void setPreviewPosition(IntercomPreviewPosition intercomPreviewPosition);

    public abstract void setSecureMode(String str, String str2);

    public abstract void setVisibility(int i);

    public abstract void setupGCM(String str, int i);

    public abstract void updateUser(Map<String, ?> map);

    public static synchronized void initialize(Application application, String apiKey, String appId) {
        synchronized (Intercom.class) {
            if (instance != null) {
                IntercomLogger.INFO("Intercom has already been initialized");
            } else if (IntercomUtils.isMessagingSDKSupported(Build.VERSION.SDK_INT)) {
                instance = new RealIntercom(application, apiKey, appId);
            } else {
                instance = new Gingercom(application.getApplicationContext());
                IntercomLogger.INFO("Intercom has no functionality on " + Build.VERSION.CODENAME + " devices");
            }
        }
    }

    public static synchronized Intercom client() {
        Intercom intercom;
        synchronized (Intercom.class) {
            if (instance == null) {
                throw new IllegalStateException("Please call Intercom.initialize() before requesting the client.");
            }
            intercom = instance;
        }
        return intercom;
    }

    public static void setLogLevel(int logLevel) {
        IntercomLogger.setLogLevel(logLevel);
    }
}
