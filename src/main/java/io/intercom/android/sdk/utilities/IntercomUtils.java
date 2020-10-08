package io.intercom.android.sdk.utilities;

import android.app.Activity;
import android.content.Context;
import io.intercom.android.sdk.Gcm;
import io.intercom.android.sdk.Intercom;

public class IntercomUtils {
    public static final String CS;

    static {
        try {
            CS = Intercom.class.getPackage().getName();
        } catch (Exception e) {
            CS = "io.intercom.android";
        }
    }

    public static boolean isMessagingSDKSupported(int androidOsVersion) {
        return androidOsVersion > 14;
    }

    public static void changeComponentState(int newState, Context context) {
        Gcm.changeComponentState(newState, context);
    }

    public static boolean isIntercomActivity(Activity activity) {
        return (activity == null || CS == null || !activity.getClass().getName().contains(CS)) ? false : true;
    }
}
