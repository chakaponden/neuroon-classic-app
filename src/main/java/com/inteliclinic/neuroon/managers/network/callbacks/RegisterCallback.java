package com.inteliclinic.neuroon.managers.network.callbacks;

import com.inteliclinic.neuroon.models.network.User;
import retrofit.RetrofitError;
import retrofit.client.Response;

public abstract class RegisterCallback extends BaseCallback<User> {
    /* access modifiers changed from: protected */
    public abstract void handleBadData(RetrofitError retrofitError);

    /* access modifiers changed from: protected */
    public abstract void handleBadRequest(RetrofitError retrofitError);

    /* access modifiers changed from: protected */
    public abstract void handleConflict(RetrofitError retrofitError);

    /* access modifiers changed from: protected */
    public abstract void handleUnauthorizedClient(RetrofitError retrofitError);

    /* access modifiers changed from: protected */
    public abstract void handleUnexpectedOperation();

    /* access modifiers changed from: protected */
    public abstract void unexpectedError(RetrofitError retrofitError);

    public void failure(RetrofitError error) {
        super.failure(error);
        switch (error.getKind()) {
            case HTTP:
                handleHttpError(error);
                return;
            default:
                handleUnexpectedOperation();
                return;
        }
    }

    /* access modifiers changed from: protected */
    public boolean handleHttpError(RetrofitError error) {
        Response response = error.getResponse();
        if (response == null) {
            throw new UnsupportedOperationException();
        }
        switch (response.getStatus()) {
            case 400:
                handleBadRequest(error);
                return true;
            case 401:
                handleUnauthorizedClient(error);
                return true;
            case 409:
                handleConflict(error);
                return true;
            case 422:
                handleBadData(error);
                return true;
            default:
                unexpectedError(error);
                return true;
        }
    }
}
