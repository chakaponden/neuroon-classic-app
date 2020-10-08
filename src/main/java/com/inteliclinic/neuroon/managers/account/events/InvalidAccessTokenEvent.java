package com.inteliclinic.neuroon.managers.account.events;

import com.inteliclinic.neuroon.managers.network.NetworkManager;

public class InvalidAccessTokenEvent {
    private final NetworkManager.RequestKey config;

    public InvalidAccessTokenEvent(NetworkManager.RequestKey config2) {
        this.config = config2;
    }

    public NetworkManager.RequestKey getRequestKey() {
        return this.config;
    }
}
