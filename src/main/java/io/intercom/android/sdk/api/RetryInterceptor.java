package io.intercom.android.sdk.api;

import io.intercom.com.squareup.okhttp.Interceptor;
import io.intercom.com.squareup.okhttp.Response;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RetryInterceptor implements Interceptor {
    private static final int MAX_RETRIES = 3;
    private final Sleeper sleeper;

    public RetryInterceptor(Sleeper sleeper2) {
        this.sleeper = sleeper2;
    }

    public Response intercept(Interceptor.Chain chain) throws IOException {
        int i = 0;
        while (i <= 3) {
            try {
                return chain.proceed(chain.request());
            } catch (IOException e) {
                if (i == 3) {
                    throw e;
                }
                this.sleeper.sleep(getRetryTimer(i + 1));
                i++;
            }
        }
        return null;
    }

    static int getRetryTimer(int count) {
        return (int) Math.pow(2.0d, (double) count);
    }

    public static class Sleeper {
        public void sleep(int time) {
            try {
                TimeUnit.SECONDS.sleep((long) time);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
