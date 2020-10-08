package com.inteliclinic.neuroon.models.data;

import com.raizlabs.android.dbflow.structure.BaseModel;

public class Airport extends BaseModel {
    String city;
    String cityCode;
    String country;
    String iata;
    String icao;
    long id;
    float latitude;
    float longitude;
    String name;
    float utcOffsetHours;

    public String getIcao() {
        return this.icao;
    }

    public float getUTFOffset() {
        return this.utcOffsetHours;
    }

    public String getCity() {
        return this.city;
    }

    public String getIata() {
        return this.iata;
    }

    public String getCountry() {
        return this.country;
    }

    public float getLongitude() {
        return this.longitude;
    }

    public float getLatitude() {
        return this.latitude;
    }
}
