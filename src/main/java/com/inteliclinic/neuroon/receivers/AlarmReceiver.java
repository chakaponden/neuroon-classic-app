package com.inteliclinic.neuroon.receivers;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.activities.AlarmDialogActivity;
import com.inteliclinic.neuroon.managers.DataManager;
import com.inteliclinic.neuroon.managers.therapy.TherapyManager;
import com.inteliclinic.neuroon.models.data.Event;
import com.inteliclinic.neuroon.utils.DateUtils;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String ALARM_CODE = "alarm_code";
    private static final int ALARM_MASK = 16777215;
    private static final int ALARM_REQUEST_CODE = -1742527628;
    private static final int ALARM_THERAPY_NOTIFICATION_PREFIX = -1761607680;
    private static final String TAG = AlarmReceiver.class.getSimpleName();
    private static final String THERAPY_EVENT_ID = "event_id";

    public static void scheduleTherapyNotification(Long id, Date date, Context context) {
        Intent myIntent = new Intent(context, AlarmReceiver.class);
        myIntent.putExtra(ALARM_CODE, ALARM_THERAPY_NOTIFICATION_PREFIX);
        myIntent.putExtra(THERAPY_EVENT_ID, id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) ((-1761607680 + id.longValue()) & 16777215), myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService("alarm");
        if (Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(0, date.getTime(), pendingIntent);
        } else {
            alarmManager.set(0, date.getTime(), pendingIntent);
        }
    }

    public static void scheduleAlarm(long millis, Context context) {
        Intent myIntent = new Intent(context, AlarmReceiver.class);
        myIntent.putExtra(ALARM_CODE, ALARM_REQUEST_CODE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ALARM_REQUEST_CODE, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService("alarm");
        if (Build.VERSION.SDK_INT >= 19) {
            alarmManager.setExact(0, millis, pendingIntent);
        } else {
            alarmManager.set(0, millis, pendingIntent);
        }
    }

    public static void removePendingTherapyNotifications(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService("alarm");
        Intent updateServiceIntent = new Intent(context, AlarmReceiver.class);
        for (int i = 0; i < 10; i++) {
            try {
                alarmManager.cancel(PendingIntent.getBroadcast(context, ALARM_THERAPY_NOTIFICATION_PREFIX + i, updateServiceIntent, 0));
            } catch (Exception e) {
                Log.e(TAG, "AlarmManager update was not canceled.", e);
            }
        }
    }

    public static void removeAlarm(Context mContext) {
        try {
            ((AlarmManager) mContext.getSystemService("alarm")).cancel(PendingIntent.getBroadcast(mContext, ALARM_REQUEST_CODE, new Intent(mContext, AlarmReceiver.class), 0));
        } catch (Exception e) {
            Log.e(TAG, "AlarmManager update was not canceled.", e);
        }
    }

    public void onReceive(Context context, Intent intent) {
        Event eventById;
        Log.d(TAG, "Alarm Received");
        if (intent != null) {
            int intExtra = intent.getIntExtra(ALARM_CODE, -1);
            if (intExtra == ALARM_REQUEST_CODE) {
                playAlarmSound(context);
            } else if (intExtra == ALARM_THERAPY_NOTIFICATION_PREFIX) {
                TherapyManager.getInstance().updateTherapy();
                long eventId = intent.getLongExtra(THERAPY_EVENT_ID, -1);
                if (eventId != -1 && (eventById = DataManager.getInstance().getEventById(eventId)) != null) {
                    showTherapyNotification(context, eventById);
                }
            }
        }
    }

    private void showTherapyNotification(Context context, Event event) {
        Notification.Builder builder = new Notification.Builder(context).setSmallIcon(R.drawable.ic_neuroon_notif);
        switch (event.getType()) {
            case ETAvoidLight:
                builder.setContentTitle(context.getString(R.string.title_jet_lag_blocker)).setContentText(context.getString(R.string.jet_lag_notif_avoid_body, new Object[]{Integer.valueOf(DateUtils.getHoursTo(event.getEndDate()))}));
                break;
            case ETSeekLight:
                builder.setContentTitle(context.getString(R.string.title_jet_lag_blocker)).setContentText(context.getString(R.string.jet_lag_notif_seek_body, new Object[]{Integer.valueOf(DateUtils.getHoursTo(event.getEndDate()))}));
                break;
            case ETSleepy:
                builder.setContentTitle(context.getString(R.string.title_jet_lag_blocker)).setContentText(context.getString(R.string.jet_lag_notif_sleepy_body, new Object[]{Integer.valueOf(DateUtils.getHoursTo(event.getEndDate()))}));
                break;
        }
        ((NotificationManager) context.getSystemService("notification")).notify(ALARM_THERAPY_NOTIFICATION_PREFIX, builder.build());
    }

    private void playAlarmSound(Context context) {
        Intent i = new Intent(context, AlarmDialogActivity.class);
        i.addFlags(268435456);
        context.startActivity(i);
    }
}
