package com.inteliclinic.neuroon.managers.trip;

import com.inteliclinic.neuroon.managers.trip.models.CurrentTrip;

public class TripCreatedEvent {
    private final CurrentTrip currentTrip;
    private final TripManager tripManager;

    public TripCreatedEvent(TripManager tripManager2, CurrentTrip currentTrip2) {
        this.tripManager = tripManager2;
        this.currentTrip = currentTrip2;
    }
}
