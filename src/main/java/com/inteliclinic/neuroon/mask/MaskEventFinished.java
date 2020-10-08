package com.inteliclinic.neuroon.mask;

import com.inteliclinic.neuroon.models.data.Event;

public class MaskEventFinished {
    private Event event;

    public MaskEventFinished(Event event2) {
        this.event = event2;
    }

    public Event getEvent() {
        return this.event;
    }
}
