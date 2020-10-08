package com.inteliclinic.neuroon.managers.therapy.models;

import com.inteliclinic.neuroon.models.data.Airport;
import com.inteliclinic.neuroon.models.data.Event;
import com.inteliclinic.neuroon.models.network.Flight;
import com.inteliclinic.neuroon.utils.DateUtils;
import com.raizlabs.android.dbflow.sql.language.Condition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class CurrentTherapy {
    private final List<Event> completedEvents;
    private final Airport destination;
    private final List<Event> eventsToSave;
    private final List<Flight> flights;
    private final float hoursDiff;
    private final boolean mIsReturn;
    private final float mStayLength;
    private final Date nadir;
    private final Airport origin;
    private float progress;
    private final Date startDate;

    public CurrentTherapy(Date startDate2, Date nadir2, float hoursDiff2, float progress2, List<Event> eventsToSave2, Airport origin2, Airport destination2, List<Flight> flights2, boolean isReturn, float stayLength) {
        this(startDate2, nadir2, hoursDiff2, progress2, eventsToSave2, origin2, destination2, flights2, new ArrayList(), isReturn, stayLength);
    }

    public CurrentTherapy(Date startDate2, Date nadir2, float hoursDiff2, float progress2, List<Event> eventsToSave2, Airport origin2, Airport destination2, List<Flight> flights2, List<Event> completedEvents2, boolean isReturn, float stayLength) {
        this.startDate = startDate2;
        this.nadir = nadir2;
        this.hoursDiff = hoursDiff2;
        this.progress = progress2;
        this.eventsToSave = eventsToSave2;
        this.origin = origin2;
        this.destination = destination2;
        this.flights = flights2;
        this.completedEvents = completedEvents2;
        this.mIsReturn = isReturn;
        this.mStayLength = stayLength;
    }

    public float getProgress() {
        return this.progress;
    }

    public void setProgress(float progress2) {
        this.progress = progress2;
    }

    public Date getNadir() {
        return this.nadir;
    }

    public Airport getOrigin() {
        return this.origin;
    }

    public Airport getDestination() {
        return this.destination;
    }

    public List<Event> getPendingEvents() {
        return this.eventsToSave;
    }

    public int getTherapyDuration() {
        List<Event> events = getEvents();
        if (events == null || events.size() == 0) {
            return 0;
        }
        return (((int) (events.get(events.size() - 1).getStartDate().getTime() / 86400000)) - ((int) (events.get(0).getStartDate().getTime() / 86400000))) + 1;
    }

    public int getTherapyDaysLeft() {
        if (this.eventsToSave == null || this.eventsToSave.size() == 0) {
            return 0;
        }
        return (((int) (this.eventsToSave.get(this.eventsToSave.size() - 1).getEndDate().getTime() / 86400000)) - ((int) (new Date().getTime() / 86400000))) + 1;
    }

    public String getTimeDifferenceAsString() {
        if (((float) ((int) getTimeDifference())) == getTimeDifference()) {
            return (getTimeDifference() > 0.0f ? Condition.Operation.PLUS : "") + ((int) getTimeDifference());
        }
        return (getTimeDifference() > 0.0f ? Condition.Operation.PLUS : "") + String.format("%.1f", new Object[]{Float.valueOf(getTimeDifference())});
    }

    public float getTimeDifference() {
        return this.hoursDiff;
    }

    public List<Event> getTodayEvents() {
        List<Event> events = new ArrayList<>();
        Date date = new Date();
        for (Event event : this.eventsToSave) {
            if (DateUtils.sameDate(event.getStartDate(), date)) {
                events.add(event);
            }
        }
        return events;
    }

    public List<Flight> getFlights() {
        return this.flights;
    }

    public Date getFlightDate() {
        return this.startDate;
    }

    public Date getStartTherapyDate() {
        return getEvents().get(0).getStartDate();
    }

    public Date getEndTherapyDate() {
        return getPendingEvents().get(getPendingEvents().size() - 1).getStartDate();
    }

    public void clearEvents() {
        this.eventsToSave.clear();
    }

    public void addCompletedEvents(List<Event> events) {
        getCompletedEvents().addAll(events);
    }

    public List<Event> getCompletedEvents() {
        if (this.completedEvents == null) {
            return new ArrayList();
        }
        return this.completedEvents;
    }

    public List<Event> getEvents() {
        List<Event> events = new ArrayList<>();
        events.addAll(getCompletedEvents());
        events.addAll(this.eventsToSave);
        Collections.sort(events, new Comparator<Event>() {
            public int compare(Event lhs, Event rhs) {
                return lhs.getStartDate().compareTo(rhs.getStartDate());
            }
        });
        return events;
    }

    public boolean isReturn() {
        return this.mIsReturn;
    }

    public float getStayLength() {
        return this.mStayLength;
    }
}
