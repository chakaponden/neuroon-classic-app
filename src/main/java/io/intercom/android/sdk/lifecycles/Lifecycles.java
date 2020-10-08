package io.intercom.android.sdk.lifecycles;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.KeyguardManager;
import android.os.Bundle;
import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.Gcm;
import io.intercom.android.sdk.api.Api;
import io.intercom.android.sdk.api.DeDuper;
import io.intercom.android.sdk.logger.IntercomLogger;
import java.util.HashSet;
import java.util.Set;

@TargetApi(15)
public class Lifecycles implements Application.ActivityLifecycleCallbacks {
    private static final int SESSION_TIMEOUT = 20000;
    private static boolean backgrounded = true;
    private final Api api;
    private final Set<String> attachedActivities = new HashSet();
    private final DeDuper deDuper;
    private long enteredBackgroundAt = 0;

    public Lifecycles(Api api2, DeDuper deDuper2) {
        this.deDuper = deDuper2;
        this.api = api2;
    }

    public static boolean isAppBackgrounded() {
        return backgrounded;
    }

    private static boolean isScreenLocked() {
        return ((KeyguardManager) Bridge.getContext().getSystemService("keyguard")).inKeyguardRestrictedInputMode();
    }

    private void attachActivity(Activity activity) {
        this.attachedActivities.add(activity.getClass().getName());
    }

    private void detachActivity(Activity activity) {
        this.attachedActivities.remove(activity.getClass().getName());
    }

    private boolean isApplicationInBackground(Activity activity) {
        if (isScreenLocked()) {
            return true;
        }
        boolean configurationChanged = activity.isChangingConfigurations();
        if (!this.attachedActivities.isEmpty() || configurationChanged) {
            return false;
        }
        return true;
    }

    private void appEnteredBackground(Activity activity) {
        IntercomLogger.INTERNAL("lifecycles", "backgrounded");
        Bridge.getNexusClient().disconnect();
        this.enteredBackgroundAt = System.currentTimeMillis();
        this.deDuper.reset();
    }

    private void appEnteredForeground(Activity activity) {
        IntercomLogger.INTERNAL("lifecycles", "foregrounded");
        if (System.currentTimeMillis() - this.enteredBackgroundAt > 20000) {
            this.api.ping();
            IntercomLogger.INTERNAL("sdk ping", "backgrounded ping from lifecycles");
        }
    }

    public void onActivityCreated(Activity activity, Bundle bundle) {
        IntercomLogger.INTERNAL("lifecycles", "on create");
        Bridge.init(activity.getApplication());
    }

    public void onActivityStarted(Activity activity) {
        IntercomLogger.INTERNAL("lifecycles", "on start");
        Bridge.init(activity.getApplication());
        attachActivity(activity);
        if (backgrounded) {
            backgrounded = false;
            appEnteredForeground(activity);
        }
        Bridge.getNexusClient().connect(Bridge.getIdentityStore().getAppConfig().getRealTimeConfig(), true);
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public void onActivityResumed(Activity activity) {
        IntercomLogger.INTERNAL("lifecycles", "on resume");
        Bridge.init(activity.getApplication());
        attachActivity(activity);
        Gcm.clearNotifications(activity);
    }

    public void onActivityPaused(Activity activity) {
        IntercomLogger.INTERNAL("lifecycles", "on pause");
        detachActivity(activity);
    }

    public void onActivityStopped(Activity activity) {
        IntercomLogger.INTERNAL("lifecycles", "on stop");
        if (isApplicationInBackground(activity)) {
            backgrounded = true;
            appEnteredBackground(activity);
            return;
        }
        detachActivity(activity);
    }

    public void onActivityDestroyed(Activity activity) {
        IntercomLogger.INTERNAL("lifecycles", "on destroy");
    }
}
