package io.intercom.android.sdk.api;

import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.identity.IdentityStore;
import io.intercom.android.sdk.logger.IntercomLogger;
import io.intercom.com.squareup.okhttp.Interceptor;
import io.intercom.com.squareup.okhttp.Response;
import java.io.IOException;

public class IdentityInterceptor implements Interceptor {
    public Response intercept(Interceptor.Chain chain) throws IOException {
        IdentityStore identityStore = Bridge.getIdentityStore();
        if (!identityStore.userIdentityExists() || !identityStore.appIdentityExists()) {
            IntercomLogger.INTERNAL("interceptor", "halting: no user or app identity");
            return null;
        }
        String fingerprint = identityStore.getUserIdentityFingerprint();
        Response response = chain.proceed(chain.request());
        if (fingerprint.equals(identityStore.getUserIdentityFingerprint())) {
            IntercomLogger.INTERNAL("interceptor", "proceeding");
            return response;
        }
        IntercomLogger.INTERNAL("interceptor", "halting: user identity changed");
        if (response != null) {
            response.body().close();
        }
        return null;
    }
}
