package com.inteliclinic.neuroon.managers.account.events;

import com.inteliclinic.neuroon.models.network.Flight;
import java.util.List;

public class FlightReceivedEvent {
    private final List<Flight> flights;

    public FlightReceivedEvent(List<Flight> flights2) {
        this.flights = flights2;
    }

    public List<Flight> getFlights() {
        return this.flights;
    }
}
