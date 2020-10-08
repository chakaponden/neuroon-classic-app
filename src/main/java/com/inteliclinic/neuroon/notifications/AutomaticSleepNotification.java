package com.inteliclinic.neuroon.notifications;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.activities.MainActivity;
import com.inteliclinic.neuroon.managers.account.AccountManager;
import com.inteliclinic.neuroon.mask.MaskManager;
import com.inteliclinic.neuroon.models.data.Event;
import java.text.DateFormat;

public final class AutomaticSleepNotification {
    private static final String NOTIFICATION_TAG = "AutomaticSleep";

    private AutomaticSleepNotification() {
    }

    public static void notify(Context context) {
        String text;
        Resources resources = context.getResources();
        Event activeEvent = MaskManager.getInstance().getActiveEvent();
        String title = context.getString(R.string.automatic_sleep_title);
        String ticker = context.getString(R.string.automatic_sleep_start);
        if (activeEvent != null) {
            text = context.getString(R.string.automatic_sleep_text, new Object[]{DateFormat.getTimeInstance(3).format(activeEvent.getEndDate())});
        } else {
            text = context.getString(R.string.automatic_sleep_text, new Object[]{DateFormat.getTimeInstance(3).format(AccountManager.getInstance().getNextAlarmTime())});
        }
        notify(context, new NotificationCompat.Builder(context).setDefaults(-1).setSmallIcon(R.drawable.ic_neuroon_notif).setContentTitle(title).setContentText(text).setPriority(0).setTicker(ticker).setContentIntent(PendingIntent.getActivity(context, 0, MainActivity.openAlarmsIntent(context), 134217728)).setAutoCancel(true).build());
    }

    @TargetApi(5)
    private static void notify(Context context, Notification notification) {
        NotificationManager nm = (NotificationManager) context.getSystemService("notification");
        if (Build.VERSION.SDK_INT >= 5) {
            nm.notify(NOTIFICATION_TAG, 0, notification);
        } else {
            nm.notify(NOTIFICATION_TAG.hashCode(), notification);
        }
    }

    @TargetApi(5)
    public static void cancel(Context context) {
        NotificationManager nm = (NotificationManager) context.getSystemService("notification");
        if (Build.VERSION.SDK_INT >= 5) {
            nm.cancel(NOTIFICATION_TAG, 0);
        } else {
            nm.cancel(NOTIFICATION_TAG.hashCode());
        }
    }
}
