package com.inteliclinic.neuroon.managers;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.Log;
import com.inteliclinic.neuroon.core.R;
import com.inteliclinic.neuroon.events.DbEventsUpdatedEvent;
import com.inteliclinic.neuroon.events.DbSleepsUpdatedEvent;
import com.inteliclinic.neuroon.events.DbTipsUpdatedEvent;
import com.inteliclinic.neuroon.models.data.Airport;
import com.inteliclinic.neuroon.models.data.Airport_Table;
import com.inteliclinic.neuroon.models.data.Event;
import com.inteliclinic.neuroon.models.data.Event_Table;
import com.inteliclinic.neuroon.models.data.OldSleep;
import com.inteliclinic.neuroon.models.data.Sleep;
import com.inteliclinic.neuroon.models.data.Sleep_Table;
import com.inteliclinic.neuroon.models.data.Tip;
import com.inteliclinic.neuroon.models.data.Tip_Table;
import com.inteliclinic.neuroon.settings.UserConfig;
import com.inteliclinic.neuroon.utils.BytesUtils;
import com.inteliclinic.neuroon.utils.DateUtils;
import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.structure.BaseModel;
import de.greenrobot.event.EventBus;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public final class DataManager extends BaseManager implements IDataManager {
    private static final String TAG = DataManager.class.getSimpleName();
    private static IDataManager mInstance;

    private DataManager(Context context) {
        loadInitData(context);
        EventBus.getDefault().post(new ManagerStartedEvent(this));
    }

    public static IDataManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DataManager(context);
        }
        return mInstance;
    }

    public static IDataManager getInstance() {
        if (mInstance != null) {
            return mInstance;
        }
        throw new NullPointerException();
    }

    public static void temporaryFixForRestoreAirportDB(Context context) {
        File dbPath = context.getDatabasePath("Airports.db");
        AssetManager assets = context.getAssets();
        try {
            if (!dbPath.exists() || dbPath.length() <= 700000) {
                dbPath.getParentFile().mkdirs();
                File databasePath = context.getDatabasePath("Airports.db");
                InputStream inputStream = assets.open("Airports.db");
                OutputStream output = new FileOutputStream(dbPath);
                byte[] buffer = new byte[1024];
                while (true) {
                    int length = inputStream.read(buffer);
                    if (length > 0) {
                        output.write(buffer, 0, length);
                    } else {
                        output.flush();
                        output.close();
                        inputStream.close();
                        return;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadLocalSleeps(Context context, String userId) {
        getInstance().deleteAllSleeps();
        ((DataManager) mInstance).lucidGlobalSet(Integer.class, UserConfig.DEEP_TRESHOLD_PROCESSED_SLEEP_COUNT, 0);
        ((DataManager) mInstance).lucidGlobalSet(Integer.class, UserConfig.DEEP_TRESHOLD_SUM, 0);
        try {
            for (String s : context.getAssets().list("sleeps/" + userId)) {
                getInstance().saveSleeps(new Sleep[]{Sleep.fromData(BytesUtils.extract(context.getAssets().open("sleeps/" + userId + Condition.Operation.DIVISION + s)), 9)});
            }
        } catch (IOException e) {
            Log.d(TAG, "Sleeps not loaded locally");
        }
    }

    private void loadInitData(Context context) {
    }

    public void saveSleeps(Sleep[] sleeps) {
        saveRecords((BaseModel[]) sleeps);
        EventBus.getDefault().post(new DbSleepsUpdatedEvent());
    }

    public void saveEvent(Event event) {
        saveRecords((BaseModel[]) new Event[]{event});
        EventBus.getDefault().post(new DbEventsUpdatedEvent());
    }

    public void saveEvents(Event[] events) {
        saveRecords((BaseModel[]) events);
        EventBus.getDefault().post(new DbEventsUpdatedEvent());
    }

    public void saveEvents(List<Event> events) {
        saveRecords((List<? extends BaseModel>) events);
        EventBus.getDefault().post(new DbEventsUpdatedEvent());
    }

    public void saveTips(Tip[] tips) {
        saveRecords((BaseModel[]) tips);
        EventBus.getDefault().post(new DbTipsUpdatedEvent());
    }

    public void saveTips(List<Tip> tips) {
        saveRecords((List<? extends BaseModel>) tips);
        EventBus.getDefault().post(new DbTipsUpdatedEvent());
    }

    public void saveTip(Tip tip, boolean notify) {
        saveRecord(tip);
        if (notify) {
            EventBus.getDefault().post(new DbTipsUpdatedEvent());
        }
    }

    public Sleep getSleepById(long id) {
        return (Sleep) SQLite.select(new IProperty[0]).from(Sleep.class).where(Sleep_Table.ID.eq(id)).querySingle();
    }

    private void saveRecord(BaseModel record) {
        record.save();
    }

    private void saveRecords(BaseModel[] records) {
        for (BaseModel record : records) {
            record.save();
        }
    }

    private void saveRecords(List<? extends BaseModel> records) {
        for (BaseModel record : records) {
            try {
                record.save();
            } catch (Exception e) {
            }
        }
    }

    public List<Sleep> getSleepsByDateDescending() {
        return SQLite.select(Sleep_Table.ID, Sleep_Table.START_DATE, Sleep_Table.SLEEP_TYPE, Sleep_Table.RAW_DATA).from(Sleep.class).where(Sleep_Table.TO_DELETE.eq(false)).and(Sleep_Table.HYPNOGRAM.isNotNull()).orderBy((IProperty) Sleep_Table.START_DATE, false).queryList();
    }

    private List<Sleep> getSleepsFromLastDaysP(int days, IProperty... properties) {
        Calendar instance = Calendar.getInstance();
        instance.add(5, -days);
        return SQLite.select(properties).from(Sleep.class).where(Sleep_Table.TO_DELETE.eq(false), Sleep_Table.START_DATE.greaterThanOrEq(instance.getTime())).queryList();
    }

    public List<Sleep> getSleepsFromLastDays(int days) {
        return getSleepsFromLastDaysP(days, new IProperty[0]);
    }

    public List<Sleep> getSleepScoreSleepsFromLastDays(int days) {
        return getSleepsFromLastDaysP(days, Sleep_Table.SLEEP_SCORE);
    }

    public List<Sleep> getSleepDurationSleepsFromLastDays(int days) {
        return getSleepsFromLastDaysP(days, Sleep_Table.START_DATE, Sleep_Table.END_DATE);
    }

    public List<Sleep> getTimeToFallAsleepSleepsFromLastDays(int days) {
        return getSleepsFromLastDaysP(days, Sleep_Table.TIME_FALL_ASLEEP);
    }

    public List<Sleep> getNumberOfAwakeningsSleepsFromLastDays(int days) {
        return getSleepsFromLastDaysP(days, Sleep_Table.AWAKENINGS);
    }

    public List<Sleep> getSleepsFromMonth(Date date) {
        Date date2 = DateUtils.dateWithoutTime(date);
        Calendar instance = Calendar.getInstance();
        instance.setTime(date2);
        instance.set(5, 1);
        Date firstDayDate = instance.getTime();
        instance.add(2, 1);
        Date lastDayDate = instance.getTime();
        return SQLite.select(new IProperty[0]).from(Sleep.class).where(Sleep_Table.TO_DELETE.eq(false), Sleep_Table.START_DATE.between(firstDayDate).and(lastDayDate)).queryList();
    }

    public List<Sleep> getSleepsWithNoType() {
        return SQLite.select(new IProperty[0]).from(Sleep.class).where(Sleep_Table.TO_DELETE.eq(false), Sleep_Table.SLEEP_TYPE.eq(0)).queryList();
    }

    public List<Sleep> getAllSleepsWithoutHypnogram() {
        return SQLite.select(new IProperty[0]).from(Sleep.class).where(Sleep_Table.TO_DELETE.eq(false), Sleep_Table.HYPNOGRAM.isNull()).queryList();
    }

    public boolean isSleepStartDateExist(Sleep sleep) {
        if (SQLite.select(Sleep_Table.ID).from(Sleep.class).where(Sleep_Table.START_DATE.eq(sleep.getStartDate())).limit(1).querySingle() != null) {
            return true;
        }
        return false;
    }

    public Event getEventById(long eventId) {
        return (Event) SQLite.select(new IProperty[0]).from(Event.class).where(Event_Table.ID.eq(eventId)).querySingle();
    }

    public Event getEventOfSleep(Sleep sleep) {
        return (Event) SQLite.select(new IProperty[0]).from(Event.class).where(Event_Table.START_DATE.eq(sleep.getStartDate())).querySingle();
    }

    public Sleep getLastSleep() {
        return (Sleep) SQLite.select(new IProperty[0]).from(Sleep.class).where().orderBy((IProperty) Sleep_Table.ID, false).querySingle();
    }

    public Sleep getLastSleepByDate() {
        return getLastSleepByDateP(new IProperty[0]);
    }

    private Sleep getLastSleepByDateP(IProperty... properties) {
        return (Sleep) SQLite.select(properties).from(Sleep.class).where(Sleep_Table.TO_DELETE.eq(false), Sleep_Table.SLEEP_TYPE.in(1, 2), Sleep_Table.HYPNOGRAM.isNotNull()).orderBy((IProperty) Sleep_Table.START_DATE, false).querySingle();
    }

    public String getParameterForKey(String key) {
        Sleep lastSleepByDate = getInstance().getLastSleepByDate();
        if (lastSleepByDate != null) {
            char c = 65535;
            switch (key.hashCode()) {
                case -1460698173:
                    if (key.equals("p-nrem-deep-length")) {
                        c = 6;
                        break;
                    }
                    break;
                case -1359561386:
                    if (key.equals("p-rem-tendency")) {
                        c = 9;
                        break;
                    }
                    break;
                case -272639745:
                    if (key.equals("p-rem-count")) {
                        c = 1;
                        break;
                    }
                    break;
                case 386314486:
                    if (key.equals("p-rem-length")) {
                        c = 2;
                        break;
                    }
                    break;
                case 584141345:
                    if (key.equals("p-nrem-light-length")) {
                        c = 5;
                        break;
                    }
                    break;
                case 675736360:
                    if (key.equals("p-fall-asleep-length")) {
                        c = 7;
                        break;
                    }
                    break;
                case 988587146:
                    if (key.equals("p-nrem-length")) {
                        c = 4;
                        break;
                    }
                    break;
                case 1547903723:
                    if (key.equals("p-nrem-count")) {
                        c = 3;
                        break;
                    }
                    break;
                case 1616691417:
                    if (key.equals("p-sleep-length")) {
                        c = 0;
                        break;
                    }
                    break;
                case 1944502082:
                    if (key.equals("p-sleep-cycles-count")) {
                        c = 8;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    return Resources.getSystem().getString(R.string.some_minutes, new Object[]{Integer.valueOf(lastSleepByDate.getSleepDuration())});
                case 1:
                    return String.valueOf(lastSleepByDate.getRemCount());
                case 2:
                    return String.valueOf(lastSleepByDate.getRemDuration());
                case 3:
                    return String.valueOf(lastSleepByDate.getNRemCount());
                case 4:
                    return String.valueOf(lastSleepByDate.getNRemDuration());
                case 5:
                    return String.valueOf(lastSleepByDate.getLightDuration());
                case 6:
                    return String.valueOf(lastSleepByDate.getDeepDuration());
                case 7:
                    return String.valueOf(lastSleepByDate.getTimeFallAsleep());
            }
        }
        return "";
    }

    public Sleep getLastSleepByDateForDashboard() {
        return getLastSleepByDateP(Sleep_Table.ID, Sleep_Table.SLEEP_PULSE_AVERAGE, Sleep_Table.SLEEP_SCORE, Sleep_Table.START_DATE, Sleep_Table.END_DATE, Sleep_Table.REM_DURATION, Sleep_Table.PULSE_ARRAY);
    }

    public Sleep getLastSleepByDateForNaps() {
        return getLastSleepByDateP(Sleep_Table.END_DATE);
    }

    public long getLastSleepByDateId() {
        Sleep sleep = getLastSleepByDateP(Sleep_Table.ID);
        if (sleep != null) {
            return sleep.getId();
        }
        return -1;
    }

    public long getSleepIdByDate(Date time) {
        Date date = DateUtils.dateWithoutTime(time);
        Sleep sleep = (Sleep) SQLite.select(new IProperty[0]).from(Sleep.class).where(Sleep_Table.START_DATE.between(date).and(DateUtils.dateAddSeconds(date, 86400))).querySingle();
        if (sleep == null) {
            return -1;
        }
        return sleep.getId();
    }

    public List<OldSleep> getAllOldSleeps() {
        return SQLite.select(new IProperty[0]).from(OldSleep.class).queryList();
    }

    public List<Sleep> getSleepsWithoutUpload() {
        return SQLite.select(new IProperty[0]).from(Sleep.class).where(Sleep_Table.SYNC_STATE.eq(1)).queryList();
    }

    public List<Sleep> getSleepsToSend() {
        return SQLite.select(new IProperty[0]).from(Sleep.class).where(Sleep_Table.SYNC_STATE.eq(0)).queryList();
    }

    public List<Event> getEvents(int offset, int elements) {
        return SQLite.select(new IProperty[0]).from(Event.class).where(Event_Table.END_DATE.isNotNull(), Event_Table.START_DATE.isNotNull()).orderBy((IProperty) Event_Table.START_DATE, false).limit(elements).offset(offset).queryList();
    }

    public List<Event> getIncompleteEvents() {
        return SQLite.select(new IProperty[0]).from(Event.class).where(Event_Table.COMPLETED.eq(false)).queryList();
    }

    public List<Event> getTherapyEvents() {
        return SQLite.select(new IProperty[0]).from(Event.class).where(Event_Table.START_DATE.greaterThanOrEq(new Date()), ConditionGroup.clause().orAll(Event_Table.TYPE.eq(Event.EventType.ETAvoidLight), Event_Table.TYPE.eq(Event.EventType.ETSeekLight), Event_Table.TYPE.eq(Event.EventType.ETSleepy), Event_Table.TYPE.eq(Event.EventType.ETJetLagTreatment))).queryList();
    }

    public List<Event> getIncompleteTherapyEvents() {
        return SQLite.select(new IProperty[0]).from(Event.class).where(Event_Table.COMPLETED.eq(false), ConditionGroup.clause().orAll(Event_Table.TYPE.eq(Event.EventType.ETAvoidLight), Event_Table.TYPE.eq(Event.EventType.ETSeekLight), Event_Table.TYPE.eq(Event.EventType.ETSleepy), Event_Table.TYPE.eq(Event.EventType.ETJetLagTreatment))).queryList();
    }

    public List<Event> getIncompleteTherapyEventsBefore(Date date) {
        return SQLite.select(new IProperty[0]).from(Event.class).where(Event_Table.COMPLETED.eq(false), Event_Table.END_DATE.lessThanOrEq(date), ConditionGroup.clause().orAll(Event_Table.TYPE.eq(Event.EventType.ETAvoidLight), Event_Table.TYPE.eq(Event.EventType.ETSeekLight), Event_Table.TYPE.eq(Event.EventType.ETSleepy), Event_Table.TYPE.eq(Event.EventType.ETJetLagTreatment))).queryList();
    }

    public List<Airport> getAirports() {
        return SQLite.select(new IProperty[0]).from(Airport.class).queryList();
    }

    public List<Airport> getAirports(int limit, String filter) {
        return SQLite.select(new IProperty[0]).from(Airport.class).where(Airport_Table.COUNTRY.like(filter + Condition.Operation.MOD)).or(Airport_Table.CITY.like(filter + Condition.Operation.MOD)).or(Airport_Table.IATA.like(filter + Condition.Operation.MOD)).limit(limit).queryList();
    }

    public List<Airport> getAirportsWithIata(int limit, String filter) {
        return SQLite.select(new IProperty[0]).from(Airport.class).where(Airport_Table.IATA.isNotNull()).and(ConditionGroup.clause().orAll(Airport_Table.COUNTRY.like(filter + Condition.Operation.MOD), Airport_Table.CITY.like(filter + Condition.Operation.MOD), Airport_Table.IATA.like(filter + Condition.Operation.MOD))).limit(limit).queryList();
    }

    public Airport getAirportByIata(String iata) {
        return (Airport) SQLite.select(new IProperty[0]).from(Airport.class).where(Airport_Table.IATA.eq(iata)).querySingle();
    }

    public Airport getAirportByIcao(String icao) {
        return (Airport) SQLite.select(new IProperty[0]).from(Airport.class).where(Airport_Table.ICAO.eq(icao)).querySingle();
    }

    public List<Tip> getTips() {
        return SQLite.select(new IProperty[0]).from(Tip.class).queryList();
    }

    public Tip getTipById(long id) {
        return (Tip) SQLite.select(new IProperty[0]).from(Tip.class).where().orderBy((IProperty) Tip_Table.ID, false).querySingle();
    }

    public void deleteAllSleeps() {
        SQLite.delete().from(OldSleep.class).execute();
        SQLite.delete().from(Sleep.class).execute();
        EventBus.getDefault().post(new DbSleepsUpdatedEvent());
    }

    public void deleteEvent(Event event) {
        deleteRecords((BaseModel[]) new Event[]{event});
        EventBus.getDefault().post(new DbEventsUpdatedEvent());
    }

    public void deleteEvents(Event[] events) {
        deleteRecords((BaseModel[]) events);
        EventBus.getDefault().post(new DbEventsUpdatedEvent());
    }

    public void deleteEvents(List<Event> events) {
        deleteRecords((List<? extends BaseModel>) events);
        EventBus.getDefault().post(new DbEventsUpdatedEvent());
    }

    public void deleteAllEvents() {
        SQLite.delete().from(Event.class).execute();
    }

    public void deleteTips(Tip[] tips) {
        deleteRecords((BaseModel[]) tips);
        EventBus.getDefault().post(new DbTipsUpdatedEvent());
    }

    public void deleteAllTips() {
        SQLite.delete().from(Tip.class).execute();
    }

    public int getMaxTipsVersion() {
        Tip tip = (Tip) SQLite.select(new IProperty[0]).from(Tip.class).orderBy((IProperty) Tip_Table.VERSION, false).querySingle();
        if (tip != null) {
            return tip.getVersion();
        }
        return -1;
    }

    public Tip getTipByServerId(int serverId) {
        return (Tip) SQLite.select(new IProperty[0]).from(Tip.class).where(Tip_Table.SERVER_ID.eq(serverId)).querySingle();
    }

    public long countTips() {
        return SQLite.selectCountOf(new IProperty[0]).from(Tip.class).count();
    }

    public void deleteAllOldSleeps() {
        SQLite.delete().from(OldSleep.class).execute();
    }

    private void deleteRecords(BaseModel[] records) {
        for (BaseModel record : records) {
            record.delete();
        }
    }

    private void deleteRecords(List<? extends BaseModel> records) {
        for (BaseModel record : records) {
            record.delete();
        }
    }

    public void revalidateMaskEvents() {
        List<TModel> queryList = SQLite.select(Sleep_Table.START_DATE).from(Sleep.class).queryList();
        for (TModel sleep : queryList) {
            if (!sleep.isDeleted()) {
                Event first = getEventOfSleep(sleep);
                if (first == null) {
                    first = new Event();
                    first.setStartDate(sleep.getStartDate());
                }
                first.setEndDate(sleep.getEndDate());
                first.setType(sleep);
                first.setCompleted(true);
                first.save();
            }
        }
        queryList.clear();
        Date lastSleepDate = new Date(0);
        Sleep lastSavedSleep = (Sleep) SQLite.select(Sleep_Table.START_DATE).from(Sleep.class).orderBy((IProperty) Sleep_Table.START_DATE, false).querySingle();
        if (lastSavedSleep != null) {
            lastSleepDate = lastSavedSleep.getStartDate();
        }
        for (Event next : getSleepEvents()) {
            if (((Sleep) SQLite.select(Sleep_Table.ID).from(Sleep.class).where(Sleep_Table.TO_DELETE.eq(false), Sleep_Table.START_DATE.eq(next.getStartDate())).querySingle()) == null && (next.getStartDate().before(lastSleepDate) || new Date().before(next.getStartDate()))) {
                next.delete();
            }
        }
        EventBus.getDefault().post(new DbEventsUpdatedEvent());
    }

    public String getLucidDelegateKey() {
        return "data-manager";
    }

    @NonNull
    private List<Event> getSleepEvents() {
        return SQLite.select(new IProperty[0]).from(Event.class).where(Event_Table.TYPE.eq(Event.EventType.ETSleep)).or(Event_Table.TYPE.eq(Event.EventType.ETNapBody)).or(Event_Table.TYPE.eq(Event.EventType.ETNapPower)).or(Event_Table.TYPE.eq(Event.EventType.ETNapRem)).or(Event_Table.TYPE.eq(Event.EventType.ETNapUltimate)).or(Event_Table.TYPE.eq(Event.EventType.ETBLT)).queryList();
    }
}
