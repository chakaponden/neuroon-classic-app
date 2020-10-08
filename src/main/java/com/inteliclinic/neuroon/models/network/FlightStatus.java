package com.inteliclinic.neuroon.models.network;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class FlightStatus {
    @SerializedName("airplane_name")
    private String airplaneName;
    @SerializedName("arrival_time_local")
    private Date arrivalTimeLocal;
    @SerializedName("departure_gate")
    private String departureGate;
    @SerializedName("departure_terminal")
    private String departureTerminal;
    @SerializedName("departure_time_local")
    private Date departureTimeLocal;
    @SerializedName("departure_time_utc")
    private Date departureTimeUtc;
    @SerializedName("flight_time")
    private int flightTime;
    @SerializedName("flight_type")
    private String flightType;
    @SerializedName("status")
    private String status;
}
