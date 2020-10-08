package com.inteliclinic.neuroon.mask;

import com.inteliclinic.neuroon.models.data.Event;

public class MaskEventStopped {
    private Event event;

    public MaskEventStopped(Event event2) {
        this.event = event2;
    }

    public Event getEvent() {
        return this.event;
    }
}
