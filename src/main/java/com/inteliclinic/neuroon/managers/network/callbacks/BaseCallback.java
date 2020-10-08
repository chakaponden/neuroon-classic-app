package com.inteliclinic.neuroon.managers.network.callbacks;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public abstract class BaseCallback<T> implements Callback<T> {
    public void failure(RetrofitError error) {
        handleFailure(error);
    }

    /* access modifiers changed from: protected */
    public boolean handleFailure(RetrofitError error) {
        switch (error.getKind()) {
            case HTTP:
                return handleHttpError(error);
            default:
                return false;
        }
    }

    /* access modifiers changed from: protected */
    public boolean handleHttpError(RetrofitError error) {
        Response response = error.getResponse();
        if (response == null) {
            throw new UnsupportedOperationException();
        }
        switch (response.getStatus()) {
            case 401:
                unauthorized();
                return true;
            case 404:
                if (notFound()) {
                    return true;
                }
                break;
            case 422:
                if (notPassValidation()) {
                    return true;
                }
                break;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean notPassValidation() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean notFound() {
        return false;
    }

    /* access modifiers changed from: protected */
    public void unauthorized() {
    }
}
