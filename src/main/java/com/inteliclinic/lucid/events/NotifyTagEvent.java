package com.inteliclinic.lucid.events;

public class NotifyTagEvent {
    private String mKey;
    private Float mValue;

    public NotifyTagEvent(String key, Float value) {
        this.mKey = key;
        this.mValue = value;
    }

    public String getKey() {
        return this.mKey;
    }

    public Float getValue() {
        return this.mValue;
    }
}
