package com.inteliclinic.neuroon.managers.account.events;

import com.inteliclinic.neuroon.managers.network.NetworkManager;
import retrofit.RetrofitError;

public class NetworkErrorEvent {
    private final RetrofitError error;
    private NetworkManager.RequestKey requestKey;

    public NetworkErrorEvent(NetworkManager.RequestKey requestKey2, RetrofitError error2) {
        this.requestKey = requestKey2;
        this.error = error2;
    }

    public NetworkManager.RequestKey getRequestKey() {
        return this.requestKey;
    }
}
