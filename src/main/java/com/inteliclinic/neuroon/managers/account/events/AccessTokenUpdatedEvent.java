package com.inteliclinic.neuroon.managers.account.events;

import com.inteliclinic.neuroon.managers.network.NetworkManager;

public class AccessTokenUpdatedEvent {
    private final NetworkManager.RequestKey requestKey;

    public AccessTokenUpdatedEvent(NetworkManager.RequestKey requestKey2) {
        this.requestKey = requestKey2;
    }

    public NetworkManager.RequestKey getRequestKey() {
        return this.requestKey;
    }
}
