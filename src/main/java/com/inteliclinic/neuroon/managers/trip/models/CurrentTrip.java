package com.inteliclinic.neuroon.managers.trip.models;

import com.inteliclinic.neuroon.models.data.Airport;
import com.inteliclinic.neuroon.models.network.Flight;
import java.util.Date;
import java.util.List;

public class CurrentTrip {
    private Date date;
    private Airport destination;
    private String[] flightCodes;
    private List<Flight> flights;
    private boolean mReturn;
    private float mStayLength;
    private Airport origin;
    private String reservationName;
    private String reservationNumber;

    public List<Flight> getFlights() {
        return this.flights;
    }

    public void setFlights(List<Flight> flights2) {
        this.flights = flights2;
    }

    public void setDate(Date date2) {
        this.date = date2;
    }

    public void setAirports(Airport from, Airport to) {
        this.origin = from;
        this.destination = to;
    }

    public Airport getOrigin() {
        if (this.origin == null && this.flights != null && this.flights.size() > 0) {
            this.origin = this.flights.get(0).getOriginAirport();
        }
        return this.origin;
    }

    public Airport getDestination() {
        if (this.destination == null && this.flights != null && this.flights.size() > 0) {
            this.destination = this.flights.get(this.flights.size() - 1).getDestinationAirport();
        }
        return this.destination;
    }

    public void setReservation(String name, String number) {
        this.reservationName = name;
        this.reservationNumber = number;
    }

    public String getReservationName() {
        return this.reservationName;
    }

    public String getReservationNumber() {
        return this.reservationNumber;
    }

    public String[] getFlightCodes() {
        return this.flightCodes;
    }

    public void setFlightCodes(String[] flightCodes2) {
        this.flightCodes = flightCodes2;
    }

    public Date getFlightDate() {
        return this.date;
    }

    public float getTimeDiff() {
        if (getDestination() == null || getOrigin() == null) {
            return 0.0f;
        }
        return getDestination().getUTFOffset() - getOrigin().getUTFOffset();
    }

    public boolean isReturn() {
        return this.mReturn;
    }

    public float getStayLength() {
        return this.mStayLength;
    }

    public void setIsReturn(boolean isReturn) {
        this.mReturn = isReturn;
    }

    public void setStayLength(int stayLength) {
        this.mStayLength = (float) stayLength;
    }
}
