package com.inteliclinic.neuroon.managers.account.events;

import com.inteliclinic.neuroon.models.network.Reservation;

public class ReservationReceivedEvent {
    private Reservation reservation;

    public ReservationReceivedEvent(Reservation reservation2) {
        this.reservation = reservation2;
    }

    public Reservation.ReservationData getReservation() {
        if (this.reservation == null) {
            return null;
        }
        return this.reservation.getData();
    }
}
