package com.inteliclinic.neuroon.managers.account.events;

public class ApplicationConfigUpdatedEvent {
    private final boolean newConfig;

    public ApplicationConfigUpdatedEvent(boolean newConfig2) {
        this.newConfig = newConfig2;
    }
}
