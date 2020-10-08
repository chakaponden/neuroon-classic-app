package com.inteliclinic.neuroon.notifications;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.activities.MaskSynchronizationActivity;

public final class SleepDownloadNotification {
    private static final String NOTIFICATION_TAG = "SleepDownload";

    private SleepDownloadNotification() {
    }

    public static void notify(Context context, String exampleString, int progress, int maxProgress) {
        Resources res = context.getResources();
        String str = exampleString;
        String title = res.getString(R.string.sleep_download_notification_title_template);
        ((NotificationManager) context.getSystemService("notification")).notify(NOTIFICATION_TAG, 0, new NotificationCompat.Builder(context).setDefaults(-1).setSmallIcon(R.drawable.ic_neuroon_notif).setContentTitle(title).setOngoing(true).setOnlyAlertOnce(true).setPriority(0).setTicker(title).setProgress(maxProgress, progress, false).setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MaskSynchronizationActivity.class), 134217728)).addAction(17301594, res.getString(R.string.stop), PendingIntent.getActivity(context, 0, new Intent(context, MaskSynchronizationActivity.class).setAction(MaskSynchronizationActivity.ACTION_STOP), 134217728)).setAutoCancel(false).build());
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
