package com.inteliclinic.neuroon.models.network;

import com.google.gson.annotations.SerializedName;
import com.inteliclinic.neuroon.managers.DataManager;
import com.inteliclinic.neuroon.models.data.Airport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Flight {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS");
    private transient Airport arrivalAirport;
    @SerializedName("arrival_iata")
    String arrivalIata;
    @SerializedName("arrival_icao")
    String arrivalIcao;
    @SerializedName("arrival_terminal")
    String arrivalTerminal;
    @SerializedName("carrier_icao")
    String carrierIcao;
    private transient Airport departureAirport;
    @SerializedName("departure_gate")
    String departureGate;
    @SerializedName("departure_iata")
    String departureIata;
    @SerializedName("departure_icao")
    String departureIcao;
    @SerializedName("departure_terminal")
    String departureTerminal;
    @SerializedName("departure_time")
    String departureTime;
    @SerializedName("flight_number")
    String flightNumber;
    @SerializedName("flight_time")
    Integer flightTime;
    String id;

    public Flight() {
    }

    public Flight(Airport departureAirport2, Airport arrivalAirport2, Date departureTimeUTC) {
        this.departureAirport = departureAirport2;
        this.arrivalAirport = arrivalAirport2;
        this.departureTime = DATE_FORMAT.format(departureTimeUTC);
    }

    public Airport getOriginAirport() {
        if (this.departureAirport == null && this.departureIcao != null) {
            this.departureAirport = DataManager.getInstance().getAirportByIcao(this.departureIcao);
        }
        if (this.departureAirport == null && this.departureIata != null) {
            this.departureAirport = DataManager.getInstance().getAirportByIata(this.departureIata);
        }
        return this.departureAirport;
    }

    public Airport getDestinationAirport() {
        if (this.arrivalAirport == null && this.arrivalIcao != null) {
            this.arrivalAirport = DataManager.getInstance().getAirportByIcao(this.arrivalIcao);
        }
        if (this.arrivalAirport == null && this.arrivalIata != null) {
            this.arrivalAirport = DataManager.getInstance().getAirportByIata(this.arrivalIata);
        }
        return this.arrivalAirport;
    }

    public Date getDepartureDate() {
        try {
            return DATE_FORMAT.parse(this.departureTime);
        } catch (ParseException e) {
            throw new UnsupportedOperationException("departure date wrong format");
        }
    }

    public Date getArrivalDate() {
        try {
            Date parse = DATE_FORMAT.parse(this.departureTime);
            if (parse == null || this.flightTime.intValue() <= 0) {
                return parse;
            }
            return new Date(parse.getTime() + ((long) (this.flightTime.intValue() * 60000)));
        } catch (ParseException e) {
            throw new UnsupportedOperationException("departure date wrong format");
        }
    }

    public Integer getFlightTime() {
        return this.flightTime;
    }

    public String getFlightNumber() {
        return this.flightNumber;
    }
}
