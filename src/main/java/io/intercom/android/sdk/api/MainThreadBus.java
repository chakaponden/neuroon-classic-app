package io.intercom.android.sdk.api;

import android.os.Handler;
import android.os.Looper;
import io.intercom.com.squareup.otto.Bus;
import io.intercom.com.squareup.otto.ThreadEnforcer;

public class MainThreadBus extends Bus {
    private final Handler mainThread = new Handler(Looper.getMainLooper());

    public MainThreadBus(ThreadEnforcer threadEnforcer) {
        super(threadEnforcer);
    }

    public void post(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
        } else {
            this.mainThread.post(new Runnable() {
                public void run() {
                    MainThreadBus.super.post(event);
                }
            });
        }
    }

    public void register(Object object) {
        try {
            super.register(object);
        } catch (Exception e) {
        }
    }

    public void unregister(Object object) {
        try {
            super.unregister(object);
        } catch (Exception e) {
        }
    }
}
