package io.intercom.com.squareup.otto;

public class DeadEvent {
    public final Object event;
    public final Object source;

    public DeadEvent(Object source2, Object event2) {
        this.source = source2;
        this.event = event2;
    }
}
