package com.inteliclinic.neuroon.managers.account.events;

import com.inteliclinic.neuroon.managers.network.NetworkManager;
import retrofit.RetrofitError;

public class InvalidRefreshTokenEvent {
    public InvalidRefreshTokenEvent(NetworkManager.RequestKey requestKey, RetrofitError error) {
    }
}
