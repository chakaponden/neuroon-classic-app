package com.inteliclinic.neuroon.service;

import android.content.Context;
import com.inteliclinic.neuroon.managers.therapy.TherapyManager;
import com.inteliclinic.neuroon.managers.therapy.models.CurrentTherapy;
import com.inteliclinic.neuroon.mask.AutomaticSleepStartedEvent;
import com.inteliclinic.neuroon.mask.MaskDownloadStoppedEvent;
import com.inteliclinic.neuroon.mask.MaskManager;
import com.inteliclinic.neuroon.mask.NewSleepAvailableEvent;
import com.inteliclinic.neuroon.mask.bluetooth.ReceivedSleepEvent;
import com.inteliclinic.neuroon.mask.bluetooth.ReceivingSleepEvent;
import com.inteliclinic.neuroon.models.data.Event;
import com.inteliclinic.neuroon.notifications.AutomaticSleepNotification;
import com.inteliclinic.neuroon.notifications.SleepDownloadNotification;
import com.inteliclinic.neuroon.receivers.AlarmReceiver;
import com.inteliclinic.neuroon.utils.DateUtils;
import de.greenrobot.event.EventBus;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public final class NotificationsWatcher extends Thread {
    private static final int MASK_DOWNLOAD_PREFIX = 3841;
    private static final String TAG = NotificationsWatcher.class.getSimpleName();
    private static Queue<NotifTask> mTasks = new LinkedBlockingQueue();
    private static NotificationsWatcher watcher;
    private Context mContext;
    private int mProgress = 0;
    private int mSleepStart = 0;

    private NotificationsWatcher(Context context) {
        this.mContext = context;
        start();
        check();
    }

    public static void startWatcher(Context context) {
        if (watcher == null) {
            watcher = new NotificationsWatcher(context);
        }
    }

    public static synchronized void check() {
        synchronized (NotificationsWatcher.class) {
            if (watcher != null) {
                synchronized (watcher) {
                    watcher.notify();
                }
            }
        }
    }

    public static void scheduleAlarm(Date date) {
        addTask(new ScheduleAlarm(date.getTime() + 30000));
        check();
    }

    public static void removeScheduledAlarm() {
        addTask(new RemoveScheduledAlarm());
        check();
    }

    public static Object getTask() {
        return mTasks.poll();
    }

    public static void addTask(NotifTask task) {
        mTasks.add(task);
    }

    public void run() {
        super.run();
        while (true) {
            synchronized (this) {
                try {
                    if (!EventBus.getDefault().isRegistered(this)) {
                        EventBus.getDefault().register(this);
                    }
                    removeJetLagNotifications();
                    prepareJetLagNotifications();
                    checkTasks();
                    wait();
                } catch (InterruptedException e) {
                    EventBus.getDefault().unregister(this);
                    return;
                }
            }
        }
    }

    public void onEvent(NewSleepAvailableEvent event) {
        this.mSleepStart = event.getLastSavedSleep();
        if (this.mSleepStart == -1) {
            this.mSleepStart = 0;
        }
        this.mProgress = -1;
    }

    public void onEvent(ReceivingSleepEvent event) {
        float sleepPercent = ((float) event.getActualFrame()) / ((float) event.getAllFrames());
        int i = (int) ((((float) ((event.getSleepNum() - this.mSleepStart) * 100)) - (100.0f - (sleepPercent * 100.0f))) / ((float) (MaskManager.getInstance().getMaskSleepCount() - this.mSleepStart)));
        if (this.mProgress != i) {
            this.mProgress = i;
            SleepDownloadNotification.notify(this.mContext, "e", i, 100);
        }
    }

    public void onEventBackgroundThread(ReceivedSleepEvent event) {
        if (event.getSleepNum() == MaskManager.getInstance().getMaskSleepCount()) {
            SleepDownloadNotification.cancel(this.mContext);
        }
    }

    public void onEvent(MaskDownloadStoppedEvent event) {
        this.mProgress = 100;
        SleepDownloadNotification.cancel(this.mContext);
    }

    private void checkTasks() {
        for (Object task = getTask(); task != null; task = getTask()) {
            if (task instanceof ScheduleAlarm) {
                scheduleAlarmInt(((ScheduleAlarm) task).getMillis());
            }
            if (task instanceof RemoveScheduledAlarm) {
                removeAlarmInt();
            }
            if (task instanceof ScheduleTherapyNotification) {
                scheduleTherapyNotification(((ScheduleTherapyNotification) task).getId(), ((ScheduleTherapyNotification) task).getType(), ((ScheduleTherapyNotification) task).getStartDate());
            }
        }
    }

    public void onEvent(AutomaticSleepStartedEvent event) {
        AutomaticSleepNotification.notify(this.mContext);
    }

    private void scheduleTherapyNotification(Long id, Event.EventType type, Date date) {
        AlarmReceiver.scheduleTherapyNotification(id, date, this.mContext);
    }

    private void prepareJetLagNotifications() {
        CurrentTherapy currentTherapy = TherapyManager.getInstance().getCurrentTherapy();
        if (currentTherapy != null) {
            for (Event event : currentTherapy.getPendingEvents()) {
                if (DateUtils.in24hFromNow(event.getStartDate())) {
                    addTask(new ScheduleTherapyNotification(Long.valueOf(event.getId()), event.getType(), event.getStartDate()));
                }
            }
        }
    }

    private void removeJetLagNotifications() {
        AlarmReceiver.removePendingTherapyNotifications(this.mContext);
    }

    private void scheduleAlarmInt(long millis) {
        AlarmReceiver.scheduleAlarm(millis, this.mContext);
    }

    private void removeAlarmInt() {
        AlarmReceiver.removeAlarm(this.mContext);
    }

    private static class ScheduleAlarm extends NotifTask {
        private final long millis;

        ScheduleAlarm(long millis2) {
            super();
            this.millis = millis2;
        }

        public long getMillis() {
            return this.millis;
        }
    }

    private static class RemoveScheduledAlarm extends NotifTask {
        private RemoveScheduledAlarm() {
            super();
        }
    }

    private static abstract class NotifTask {
        private NotifTask() {
        }
    }

    private class ScheduleTherapyNotification extends NotifTask {
        private final Long id;
        private final Date startDate;
        private final Event.EventType type;

        ScheduleTherapyNotification(Long id2, Event.EventType type2, Date startDate2) {
            super();
            this.id = id2;
            this.type = type2;
            this.startDate = startDate2;
        }

        public Event.EventType getType() {
            return this.type;
        }

        public Date getStartDate() {
            return this.startDate;
        }

        public Long getId() {
            return this.id;
        }
    }
}
