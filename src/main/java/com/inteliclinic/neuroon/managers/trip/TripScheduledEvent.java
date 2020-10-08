package com.inteliclinic.neuroon.managers.trip;

import com.inteliclinic.neuroon.managers.trip.models.CurrentTrip;
import com.inteliclinic.neuroon.models.data.Airport;
import com.inteliclinic.neuroon.models.network.Flight;
import java.util.Date;
import java.util.List;

public class TripScheduledEvent {
    private final Airport destination;
    private final List<Flight> flights;
    private final Airport origin;
    private final Date startDate;
    private final float timeDiff;

    public TripScheduledEvent(Date startDate2, CurrentTrip trip) {
        this.timeDiff = trip.getTimeDiff();
        this.startDate = startDate2;
        this.origin = trip.getOrigin();
        this.destination = trip.getDestination();
        this.flights = trip.getFlights();
    }

    public float getTimeDiff() {
        return this.timeDiff;
    }

    public List<Flight> getFlights() {
        return this.flights;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public Airport getOrigin() {
        return this.origin;
    }

    public Airport getDestination() {
        return this.destination;
    }
}
