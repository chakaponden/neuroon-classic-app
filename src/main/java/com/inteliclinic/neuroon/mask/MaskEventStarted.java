package com.inteliclinic.neuroon.mask;

import com.inteliclinic.neuroon.models.data.Event;

public class MaskEventStarted {
    private Event event;

    public MaskEventStarted(Event event2) {
        this.event = event2;
    }

    public Event getEvent() {
        return this.event;
    }
}
