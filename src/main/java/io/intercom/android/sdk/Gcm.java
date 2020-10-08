package io.intercom.android.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import io.intercom.android.sdk.logger.IntercomLogger;

public class Gcm {
    public static final String ACTION_REMOVE_NOTIFICATION = "intercom_sdk_remove_notification";
    public static final String MULTIPLE_NOTIFICATIONS = "multiple_notifications";
    public static final String PUSH_ONLY_ID = "push_only_convo_id";

    public static void clearNotifications(Context context) {
        Class intentClass = getGcmIntentClass();
        if (intentClass != null) {
            Intent intent = new Intent(context, intentClass);
            intent.setAction(ACTION_REMOVE_NOTIFICATION);
            context.startService(intent);
        }
    }

    public static void changeComponentState(int newState, Context context) {
        boolean z;
        boolean z2 = true;
        Class broadcastReceiverClass = getGcmBroadcastReceiverClass();
        Class intentClass = getGcmIntentClass();
        if (broadcastReceiverClass != null && intentClass != null) {
            ComponentName gcmBroadcastReceiverComponent = new ComponentName(context, broadcastReceiverClass);
            ComponentName gcmServiceComponent = new ComponentName(context, intentClass);
            PackageManager packageManager = context.getPackageManager();
            packageManager.setComponentEnabledSetting(gcmBroadcastReceiverComponent, newState, 1);
            packageManager.setComponentEnabledSetting(gcmServiceComponent, newState, 1);
            StringBuilder append = new StringBuilder().append("broadcast receiver is enabled: ");
            if (packageManager.getComponentEnabledSetting(gcmBroadcastReceiverComponent) == 1) {
                z = true;
            } else {
                z = false;
            }
            IntercomLogger.INTERNAL("component state", append.append(z).toString());
            StringBuilder append2 = new StringBuilder().append("gcm service is enabled: ");
            if (packageManager.getComponentEnabledSetting(gcmServiceComponent) != 1) {
                z2 = false;
            }
            IntercomLogger.INTERNAL("component state", append2.append(z2).toString());
        }
    }

    public static boolean gcmModuleInstalled() {
        return (getGcmIntentClass() == null || getGcmBroadcastReceiverClass() == null) ? false : true;
    }

    public static boolean isGcmEnabled(Context context) {
        Class broadcastReceiverClass = getGcmBroadcastReceiverClass();
        Class intentClass = getGcmIntentClass();
        if (broadcastReceiverClass == null || intentClass == null) {
            return false;
        }
        PackageManager packageManager = context.getPackageManager();
        ComponentName gcmBroadcastReceiverComponent = new ComponentName(context, broadcastReceiverClass);
        ComponentName gcmServiceComponent = new ComponentName(context, intentClass);
        if (packageManager.getComponentEnabledSetting(gcmBroadcastReceiverComponent) == 1 && packageManager.getComponentEnabledSetting(gcmServiceComponent) == 1) {
            return true;
        }
        return false;
    }

    private static Class getGcmIntentClass() {
        try {
            return Class.forName("io.intercom.android.sdk.gcm.GcmIntentService");
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private static Class getGcmBroadcastReceiverClass() {
        try {
            return Class.forName("io.intercom.android.sdk.gcm.GcmBroadcastReceiver");
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
