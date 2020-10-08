package io.intercom.android.sdk.api;

import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.logger.IntercomLogger;
import io.intercom.retrofit.Callback;
import io.intercom.retrofit.RetrofitError;
import io.intercom.retrofit.client.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class BaseCallback<T> implements Callback<T> {
    /* access modifiers changed from: package-private */
    public abstract void onSuccess(T t);

    public final void success(T t, Response response) {
        if (t == null) {
            failure(RetrofitError.unexpectedError("", new IllegalStateException("no body found")));
        } else {
            onSuccess(t);
        }
    }

    public final void failure(RetrofitError error) {
        logFailure("Api call failed", error);
        onFailure(error);
    }

    /* access modifiers changed from: package-private */
    public void onFailure(RetrofitError error) {
    }

    /* access modifiers changed from: package-private */
    public void logFailure(String reason, RetrofitError error) {
        IntercomLogger.ERROR(reason + ": " + getDetails(error));
    }

    static String getDetails(RetrofitError error) {
        InputStream is;
        BufferedReader bis;
        String reason = null;
        if (error != null) {
            Response response = error.getResponse();
            if (response != null) {
                if (response.getBody() != null) {
                    String errorBody = "";
                    try {
                        is = response.getBody().in();
                        bis = new BufferedReader(new InputStreamReader(is));
                        StringBuilder stringBuilder = new StringBuilder();
                        while (true) {
                            String line = bis.readLine();
                            if (line == null) {
                                break;
                            }
                            stringBuilder.append(line);
                        }
                        errorBody = stringBuilder.toString();
                        is.close();
                        bis.close();
                    } catch (IOException e) {
                    } catch (Throwable th) {
                        is.close();
                        bis.close();
                        throw th;
                    }
                    reason = " Status: " + response.getStatus() + " Body: " + errorBody;
                } else {
                    reason = response.getReason();
                }
            } else if ("Canceled".equalsIgnoreCase(error.getMessage())) {
                if (!Bridge.getIdentityStore().userIdentityExists()) {
                    reason = "no user currently registered";
                }
                if (!Bridge.getIdentityStore().appIdentityExists()) {
                    reason = "the apiKey or appId used to initialise Intercom are invalid";
                }
            } else {
                reason = error.getMessage();
            }
        }
        if (reason == null) {
            return "unknown error";
        }
        return reason;
    }
}
