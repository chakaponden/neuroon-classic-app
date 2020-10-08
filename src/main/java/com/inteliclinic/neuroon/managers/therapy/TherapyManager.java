package com.inteliclinic.neuroon.managers.therapy;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.inteliclinic.neuroon.managers.BaseManager;
import com.inteliclinic.neuroon.managers.DataManager;
import com.inteliclinic.neuroon.managers.ManagerStartedEvent;
import com.inteliclinic.neuroon.managers.therapy.models.CurrentTherapy;
import com.inteliclinic.neuroon.managers.tip.Tags;
import com.inteliclinic.neuroon.mask.MaskManager;
import com.inteliclinic.neuroon.models.data.Airport;
import com.inteliclinic.neuroon.models.data.Event;
import com.inteliclinic.neuroon.models.network.Flight;
import com.inteliclinic.neuroon.settings.UserConfig;
import com.inteliclinic.neuroon.utils.DateUtils;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TherapyManager extends BaseManager implements ITherapyManager {
    private static TherapyManager mInstance;
    private final Context mContext;
    private CurrentTherapy mCurrentTherapy;
    private ITherapyTracker mTherapyTracker;

    public TherapyManager(Context context, ITherapyTracker therapyTracker) {
        this.mContext = context;
        this.mTherapyTracker = therapyTracker;
        EventBus.getDefault().post(new ManagerStartedEvent(this));
    }

    public static ITherapyManager getInstance(Context context, ITherapyTracker therapyTracker) {
        if (mInstance == null) {
            mInstance = new TherapyManager(context, therapyTracker);
        }
        return mInstance;
    }

    public static ITherapyManager getInstance() {
        if (mInstance != null) {
            return mInstance;
        }
        throw new NullPointerException();
    }

    private void createTherapyForTimeDifference(float updatedTimeDiff, CurrentTherapy currentTherapy) {
        createTherapyForTimeDifference(updatedTimeDiff, currentTherapy.getFlightDate(), currentTherapy.isReturn(), currentTherapy.getStayLength(), currentTherapy.getOrigin(), currentTherapy.getDestination(), currentTherapy.getFlights());
    }

    public void planTherapy(float timeDiff, Date flightDate, boolean isReturn, float stayLength, Airport origin, Airport destination, List<Flight> flights) {
        float f;
        if (stayLength > Math.abs(timeDiff)) {
            createTherapyForTimeDifference(timeDiff, flightDate, isReturn, stayLength, origin, destination, flights);
            return;
        }
        float val = (timeDiff > 0.0f ? 1.0f : -1.0f) * (1.0f + (0.5f * stayLength));
        if (Math.abs(val) > Math.abs(timeDiff)) {
            f = timeDiff;
        } else {
            f = val;
        }
        createTherapyForTimeDifference(f, flightDate, isReturn, stayLength, origin, destination, flights);
    }

    private void createTherapyForTimeDifference(float hoursDiff, Date startDate, boolean isReturn, float stayLength, Airport origin, Airport destination, List<Flight> flights) {
        Date nadir;
        ArrayList<Event> arrayList = new ArrayList<>();
        float timeDiffValue = hoursDiff;
        if (timeDiffValue > 7.0f) {
            timeDiffValue -= 0.1875f;
        }
        double interval = 0.0d;
        if (timeDiffValue < 0.0f) {
            interval = -(Math.ceil((double) (-timeDiffValue)) * 86400.0d);
        } else if (timeDiffValue > 0.0f) {
            interval = -(Math.ceil((double) timeDiffValue) * 86400.0d);
        }
        if (interval < 518400.0d) {
            interval = -518400.0d;
        }
        Date therapyStartDate = DateUtils.dateAddSeconds(startDate, (int) interval);
        if (DateUtils.compareToNow(therapyStartDate) < 0) {
            therapyStartDate = new Date();
        }
        if (DateUtils.compareTo(therapyStartDate, startDate) > -1) {
            lucidBroadcast(Tags.AT_DESTINATION, 1.0f);
        } else {
            lucidBroadcast(Tags.AT_DESTINATION, 0.0f);
        }
        CurrentTherapy currentTherapy = getCurrentTherapy();
        Date savedNadir = MaskManager.getInstance().getSavedNadir();
        if (savedNadir != null) {
            savedNadir = DateUtils.dateAddSeconds(savedNadir, (DateUtils.secondsToNow(savedNadir) / 86400) * 86400);
        }
        if (currentTherapy == null || currentTherapy.getNadir() == null) {
            nadir = savedNadir;
        } else {
            nadir = currentTherapy.getNadir();
        }
        Date nadir2 = DateUtils.dateAddSeconds(nadir, (int) (Math.floor((double) ((float) (DateUtils.secondsInterval(therapyStartDate, new Date()) / 86400))) * 86400.0d));
        if (timeDiffValue < 0.0f) {
            int diffBeforeStart = DateUtils.secondsInterval(startDate, therapyStartDate) / 86400;
            int n = (int) Math.ceil(((double) diffBeforeStart) + ((((double) (-timeDiffValue)) - (0.6666666666666666d * ((double) diffBeforeStart))) / 2.0d));
            Date nadirForTherapyDay = nadir2;
            for (int i = 0; i <= n; i++) {
                arrayList.add(Event.seekLightEvent(nadirForTherapyDay.getTime() - 25200000, nadirForTherapyDay.getTime() - 3600000));
                arrayList.add(Event.avoidLightEvent(nadirForTherapyDay.getTime() + 3600000, nadirForTherapyDay.getTime() + 25200000));
                arrayList.add(Event.feelingSleepyEvent(nadirForTherapyDay.getTime() - 14400000, nadirForTherapyDay.getTime() - 7200000));
                if (DateUtils.compareTo(nadirForTherapyDay, startDate) < 0) {
                    nadirForTherapyDay = DateUtils.dateAddSeconds(nadirForTherapyDay, 88800);
                } else {
                    nadirForTherapyDay = DateUtils.dateAddSeconds(nadirForTherapyDay, 93600);
                }
            }
        } else if (timeDiffValue > 0.0f) {
            int diffBeforeStart2 = DateUtils.secondsInterval(startDate, therapyStartDate) / 86400;
            int n2 = (int) Math.ceil(((double) diffBeforeStart2) + (((double) timeDiffValue) - (0.6666666666666666d * ((double) diffBeforeStart2))));
            Date nadirForTherapyDay2 = nadir2;
            for (int i2 = 0; i2 <= n2; i2++) {
                arrayList.add(Event.seekLightEvent(nadirForTherapyDay2.getTime() + 3600000, nadirForTherapyDay2.getTime() + 25200000));
                arrayList.add(Event.avoidLightEvent(nadirForTherapyDay2.getTime() - 25200000, nadirForTherapyDay2.getTime() - 3600000));
                arrayList.add(Event.feelingSleepyEvent(nadirForTherapyDay2.getTime() - 14400000, nadirForTherapyDay2.getTime() - 7200000));
                if (DateUtils.compareTo(nadirForTherapyDay2, startDate) < 0) {
                    nadirForTherapyDay2 = DateUtils.dateAddSeconds(nadirForTherapyDay2, 84000);
                } else {
                    nadirForTherapyDay2 = DateUtils.dateAddSeconds(nadirForTherapyDay2, 82800);
                }
            }
        }
        List<Event> eventsToSave = new ArrayList<>();
        for (Event event : arrayList) {
            if (event.getEndDate().after(new Date())) {
                eventsToSave.add(event);
            }
        }
        if (eventsToSave.size() > 0) {
            if (currentTherapy == null) {
                setCurrentTherapy(new CurrentTherapy(startDate, nadir2, hoursDiff, 0.0f, eventsToSave, origin, destination, flights, isReturn, stayLength));
                lucidBroadcast(Tags.JET_LAG, 1.0f);
            } else {
                DataManager.getInstance().deleteEvents(currentTherapy.getPendingEvents());
                setCurrentTherapy(new CurrentTherapy(startDate, nadir2, currentTherapy.getTimeDifference(), currentTherapy.getProgress(), eventsToSave, origin, destination, flights, currentTherapy.getCompletedEvents(), isReturn, stayLength));
            }
            EventBus.getDefault().post(new TherapyCreatedEvent());
            return;
        }
        completeCurrentTherapy();
    }

    private void completeCurrentTherapy() {
        CurrentTherapy currentTherapy = getCurrentTherapy();
        if (currentTherapy != null) {
            currentTherapy.clearEvents();
            currentTherapy.setProgress(currentTherapy.getTimeDifference());
            setCurrentTherapy(currentTherapy);
        }
        EventBus.getDefault().post(new TherapyUpdatedEvent());
    }

    public void updateTherapy() {
        CurrentTherapy currentTherapy = getCurrentTherapy();
        if (currentTherapy != null) {
            float therapyProgress = getTherapyProgress();
            List<Event> therapyEvents = DataManager.getInstance().getIncompleteTherapyEventsBefore(new Date());
            if (!therapyEvents.isEmpty()) {
                float timeDiff = currentTherapy.getTimeDifference();
                Date flightDate = currentTherapy.getFlightDate();
                float progressHourValue = 0.0f;
                List<Event> changedEvents = new ArrayList<>();
                for (Event event : therapyEvents) {
                    if (event.getType() != Event.EventType.ETSleepy) {
                        if (DateUtils.compareTo(event.getEndDate(), flightDate) < 0) {
                            progressHourValue = (float) ((timeDiff < 0.0f ? -0.6666666666666666d : 6.666666666666667d) + ((double) progressHourValue));
                        } else {
                            progressHourValue = (float) ((timeDiff < 0.0f ? -2.0d : 1.0d) + ((double) progressHourValue));
                        }
                    }
                    event.setCompleted(true);
                    changedEvents.add(event);
                }
                float progressHourValue2 = (float) (((double) progressHourValue) / 2.0d);
                float progress = currentTherapy.getProgress() + progressHourValue2;
                DataManager.getInstance().saveEvents(changedEvents);
                Double nadir = (Double) lucidRead(Double.class, UserConfig.USER_NADIR);
                if (nadir == null) {
                    nadir = Double.valueOf(0.0d);
                }
                lucidRead(Object.class, String.format("(set-global! %s %s)", new Object[]{UserConfig.USER_NADIR, String.valueOf(nadir.doubleValue() + ((double) progressHourValue2))}));
                DataManager.getInstance().deleteEvents(DataManager.getInstance().getIncompleteTherapyEvents());
                currentTherapy.addCompletedEvents(changedEvents);
                currentTherapy.clearEvents();
                if (Math.abs(progress) < Math.abs(timeDiff)) {
                    currentTherapy.setProgress(progress);
                    setCurrentTherapy(currentTherapy);
                    createTherapyForTimeDifference(timeDiff - progress, currentTherapy);
                    if (((double) getTherapyProgress()) > 0.5d && ((double) therapyProgress) < 0.5d && this.mTherapyTracker != null) {
                        this.mTherapyTracker.setHalfTherapyProgress();
                        return;
                    }
                    return;
                }
                if (this.mTherapyTracker != null) {
                    this.mTherapyTracker.setEndTherapyProgress();
                }
                completeCurrentTherapy();
            }
        }
    }

    public float getTherapyProgress() {
        return getCurrentTherapy().getProgress() / getCurrentTherapy().getTimeDifference();
    }

    public void deleteTherapy() {
        List<Event> therapyEventsIterator = DataManager.getInstance().getTherapyEvents();
        List<Event> therapyEvents = new ArrayList<>();
        for (Event event : therapyEventsIterator) {
            therapyEvents.add(event);
        }
        DataManager.getInstance().deleteEvents(therapyEvents);
        setCurrentTherapy((CurrentTherapy) null);
        EventBus.getDefault().post(new TherapyDeletedEvent());
    }

    public CurrentTherapy getCurrentTherapy() {
        if (this.mCurrentTherapy == null) {
            this.mCurrentTherapy = (CurrentTherapy) new Gson().fromJson(getTherapyManagerPreferences().getString("current_therapy", (String) null), CurrentTherapy.class);
        }
        return this.mCurrentTherapy;
    }

    private void setCurrentTherapy(CurrentTherapy currentTherapy) {
        this.mCurrentTherapy = currentTherapy;
        saveCurrentTherapy();
    }

    private void saveCurrentTherapy() {
        if (this.mCurrentTherapy != null) {
            DataManager.getInstance().saveEvents(this.mCurrentTherapy.getPendingEvents());
            lucidBroadcast(Tags.JET_LAG, 1.0f);
            getTherapyManagerPreferences().edit().putString("current_therapy", new Gson().toJson((Object) this.mCurrentTherapy)).commit();
            return;
        }
        lucidBroadcast(Tags.JET_LAG, 0.0f);
        getTherapyManagerPreferences().edit().remove("current_therapy").commit();
    }

    private SharedPreferences getTherapyManagerPreferences() {
        return this.mContext.getSharedPreferences("TherapyManager", 0);
    }

    public String getLucidDelegateKey() {
        return "therapy-manager";
    }
}
