package com.inteliclinic.neuroon.models.network;

import java.util.ArrayList;
import java.util.List;

public class Reservation {
    private ReservationData data;

    public ReservationData getData() {
        return this.data;
    }

    public void setData(ReservationData data2) {
        this.data = data2;
    }

    public class ReservationData {
        private String name;
        private String reservation;
        private List<List<Flight>> trips = new ArrayList();

        public ReservationData() {
        }

        public String getReservation() {
            return this.reservation;
        }

        public void setReservation(String reservation2) {
            this.reservation = reservation2;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name2) {
            this.name = name2;
        }

        public List<List<Flight>> getTrips() {
            return this.trips;
        }
    }
}
