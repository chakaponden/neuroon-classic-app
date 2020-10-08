package com.inteliclinic.neuroon.managers.trip;

import com.inteliclinic.neuroon.managers.BaseManager;
import com.inteliclinic.neuroon.managers.ManagerStartedEvent;
import com.inteliclinic.neuroon.managers.account.AccountManager;
import com.inteliclinic.neuroon.managers.account.events.FlightReceivedEvent;
import com.inteliclinic.neuroon.managers.account.events.ReservationReceivedEvent;
import com.inteliclinic.neuroon.managers.trip.models.CurrentTrip;
import com.inteliclinic.neuroon.models.data.Airport;
import com.inteliclinic.neuroon.models.network.Flight;
import com.inteliclinic.neuroon.models.network.Reservation;
import com.inteliclinic.neuroon.old_guava.Strings;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class TripManager extends BaseManager implements ITripManager {
    private static TripManager mInstance;
    private CurrentTrip mCurrentTrip;

    public TripManager() {
        EventBus.getDefault().post(new ManagerStartedEvent(this));
        EventBus.getDefault().register(this);
    }

    public static ITripManager getInstance() {
        if (mInstance == null) {
            mInstance = new TripManager();
        }
        return mInstance;
    }

    public String getLucidDelegateKey() {
        return "trip-manager";
    }

    public void updateTrip(Flight flight) {
        getCurrentTripInt();
        this.mCurrentTrip.setFlights(Collections.singletonList(flight));
    }

    public void updateTrip(Airport origin, Airport destination, Date date) {
        getCurrentTripInt();
        this.mCurrentTrip.setAirports(origin, destination);
        this.mCurrentTrip.setDate(date);
        handleUpdateTrip();
    }

    public void updateTrip(String[] flightCodes) {
        getCurrentTripInt();
        this.mCurrentTrip.setFlightCodes(flightCodes);
        handleUpdateTrip();
    }

    public void updateTrip(String reservationName, String reservationNumber) {
        getCurrentTripInt();
        this.mCurrentTrip.setReservation(reservationName, reservationNumber);
        handleUpdateTrip();
    }

    private CurrentTrip getCurrentTripInt() {
        if (this.mCurrentTrip == null) {
            this.mCurrentTrip = new CurrentTrip();
        }
        return this.mCurrentTrip;
    }

    public void updateTrip(boolean isReturn) {
        getCurrentTripInt().setIsReturn(isReturn);
    }

    public void updateTrip(int stayLength) {
        getCurrentTripInt().setStayLength(stayLength);
    }

    public CurrentTrip getCurrentTrip() {
        return this.mCurrentTrip;
    }

    private void handleUpdateTrip() {
        if (!Strings.isNullOrEmpty(this.mCurrentTrip.getReservationName()) && !Strings.isNullOrEmpty(this.mCurrentTrip.getReservationNumber())) {
            AccountManager.getInstance().getFlights(this.mCurrentTrip.getReservationName(), this.mCurrentTrip.getReservationNumber());
        } else if (this.mCurrentTrip.getFlightCodes() != null && this.mCurrentTrip.getFlightCodes().length > 0 && this.mCurrentTrip.getFlightDate() != null) {
            AccountManager.getInstance().getFlights(this.mCurrentTrip.getFlightCodes(), this.mCurrentTrip.getFlightDate());
        } else if (this.mCurrentTrip.getOrigin() != null && this.mCurrentTrip.getDestination() != null && this.mCurrentTrip.getFlightDate() != null) {
            saveTrip();
        }
    }

    public void saveTrip() {
        if (this.mCurrentTrip == null) {
            throw new UnsupportedOperationException("current trip not set");
        }
        List<Flight> flights = this.mCurrentTrip.getFlights();
        if (flights == null || flights.size() == 0) {
            this.mCurrentTrip.setFlights(Collections.singletonList(new Flight(this.mCurrentTrip.getOrigin(), this.mCurrentTrip.getDestination(), this.mCurrentTrip.getFlightDate())));
        }
        List<Flight> flights2 = this.mCurrentTrip.getFlights();
        if (flights2 != null && flights2.size() != 0) {
            Date startDate = flights2.get(0).getDepartureDate();
            if (Math.abs(this.mCurrentTrip.getOrigin().getUTFOffset() - this.mCurrentTrip.getDestination().getUTFOffset()) > 3.0f) {
                EventBus.getDefault().post(new TripScheduledEvent(startDate, this.mCurrentTrip));
            } else {
                EventBus.getDefault().post(new TripTooShortEvent());
            }
        }
    }

    public void clearTrip() {
        this.mCurrentTrip = new CurrentTrip();
    }

    public void onEvent(FlightReceivedEvent event) {
        List<Flight> flights = event.getFlights();
        if (flights != null && flights.size() > 0) {
            if ((!Strings.isNullOrEmpty(this.mCurrentTrip.getReservationName()) && !Strings.isNullOrEmpty(this.mCurrentTrip.getReservationNumber())) || (this.mCurrentTrip.getFlightCodes() != null && this.mCurrentTrip.getFlightCodes().length > 0)) {
                this.mCurrentTrip.setFlights(flights);
                saveTrip();
            }
        }
    }

    public void onEvent(ReservationReceivedEvent event) {
        Reservation.ReservationData reservation = event.getReservation();
        if (reservation == null) {
            EventBus.getDefault().post(new NoReservationFound());
            return;
        }
        List<List<Flight>> trips = reservation.getTrips();
        if (trips == null || trips.size() == 0) {
            EventBus.getDefault().post(new NoReservationFound());
            return;
        }
        List<Flight> flights = new ArrayList<>();
        Iterator<List<Flight>> it = trips.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            for (Flight flight : it.next()) {
                flights.add(flight);
            }
            if (flights.size() > 0) {
                Airport originAirport = flights.get(0).getOriginAirport();
                Airport destinationAirport = flights.get(flights.size() - 1).getDestinationAirport();
                if (!(originAirport == null || destinationAirport == null)) {
                    this.mCurrentTrip.setAirports(originAirport, destinationAirport);
                    if (Math.abs(this.mCurrentTrip.getTimeDiff()) > 3.0f) {
                        this.mCurrentTrip.setDate(flights.get(0).getDepartureDate());
                        break;
                    }
                }
            }
            flights.clear();
        }
        this.mCurrentTrip.setFlights(flights);
        saveTrip();
    }
}
