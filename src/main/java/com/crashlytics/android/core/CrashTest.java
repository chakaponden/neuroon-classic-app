package com.crashlytics.android.core;

import android.os.AsyncTask;
import io.fabric.sdk.android.Fabric;

public class CrashTest {
    public void throwRuntimeException(String message) {
        throw new RuntimeException(message);
    }

    public int stackOverflow() {
        return stackOverflow() + ((int) Math.random());
    }

    public void indexOutOfBounds() {
        Fabric.getLogger().d(CrashlyticsCore.TAG, "Out of bounds value: " + new int[2][10]);
    }

    public void crashAsyncTask(final long delayMs) {
        new AsyncTask<Void, Void, Void>() {
            /* access modifiers changed from: protected */
            public Void doInBackground(Void... params) {
                try {
                    Thread.sleep(delayMs);
                } catch (InterruptedException e) {
                }
                CrashTest.this.throwRuntimeException("Background thread crash");
                return null;
            }
        }.execute(new Void[]{null});
    }

    public void throwFiveChainedExceptions() {
        try {
            privateMethodThatThrowsException("1");
        } catch (Exception ex) {
            throw new RuntimeException("2", ex);
        } catch (Exception ex2) {
            try {
                throw new RuntimeException("3", ex2);
            } catch (Exception ex3) {
                try {
                    throw new RuntimeException("4", ex3);
                } catch (Exception ex4) {
                    throw new RuntimeException("5", ex4);
                }
            }
        }
    }

    private void privateMethodThatThrowsException(String message) {
        throw new RuntimeException(message);
    }
}
