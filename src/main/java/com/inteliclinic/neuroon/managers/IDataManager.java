package com.inteliclinic.neuroon.managers;

import com.inteliclinic.neuroon.models.data.Airport;
import com.inteliclinic.neuroon.models.data.Event;
import com.inteliclinic.neuroon.models.data.OldSleep;
import com.inteliclinic.neuroon.models.data.Sleep;
import com.inteliclinic.neuroon.models.data.Tip;
import java.util.Date;
import java.util.List;

public interface IDataManager {
    long countTips();

    void deleteAllEvents();

    void deleteAllOldSleeps();

    void deleteAllSleeps();

    void deleteAllTips();

    void deleteEvent(Event event);

    void deleteEvents(List<Event> list);

    void deleteEvents(Event[] eventArr);

    void deleteTips(Tip[] tipArr);

    Airport getAirportByIata(String str);

    Airport getAirportByIcao(String str);

    List<Airport> getAirports();

    List<Airport> getAirports(int i, String str);

    List<Airport> getAirportsWithIata(int i, String str);

    List<OldSleep> getAllOldSleeps();

    List<Sleep> getAllSleepsWithoutHypnogram();

    Event getEventById(long j);

    Event getEventOfSleep(Sleep sleep);

    List<Event> getEvents(int i, int i2);

    List<Event> getIncompleteEvents();

    List<Event> getIncompleteTherapyEvents();

    List<Event> getIncompleteTherapyEventsBefore(Date date);

    Sleep getLastSleep();

    Sleep getLastSleepByDate();

    Sleep getLastSleepByDateForDashboard();

    Sleep getLastSleepByDateForNaps();

    long getLastSleepByDateId();

    int getMaxTipsVersion();

    List<Sleep> getNumberOfAwakeningsSleepsFromLastDays(int i);

    String getParameterForKey(String str);

    Sleep getSleepById(long j);

    List<Sleep> getSleepDurationSleepsFromLastDays(int i);

    long getSleepIdByDate(Date date);

    List<Sleep> getSleepScoreSleepsFromLastDays(int i);

    List<Sleep> getSleepsByDateDescending();

    List<Sleep> getSleepsFromLastDays(int i);

    List<Sleep> getSleepsFromMonth(Date date);

    List<Sleep> getSleepsToSend();

    List<Sleep> getSleepsWithNoType();

    List<Sleep> getSleepsWithoutUpload();

    List<Event> getTherapyEvents();

    List<Sleep> getTimeToFallAsleepSleepsFromLastDays(int i);

    Tip getTipById(long j);

    Tip getTipByServerId(int i);

    List<Tip> getTips();

    boolean isSleepStartDateExist(Sleep sleep);

    void revalidateMaskEvents();

    void saveEvent(Event event);

    void saveEvents(List<Event> list);

    void saveEvents(Event[] eventArr);

    void saveSleeps(Sleep[] sleepArr);

    void saveTip(Tip tip, boolean z);

    void saveTips(List<Tip> list);

    void saveTips(Tip[] tipArr);
}
