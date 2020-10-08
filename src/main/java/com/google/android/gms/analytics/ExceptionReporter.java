package com.google.android.gms.analytics;

import android.content.Context;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.internal.zzae;
import java.lang.Thread;
import java.util.ArrayList;

public class ExceptionReporter implements Thread.UncaughtExceptionHandler {
    private final Context mContext;
    private final Thread.UncaughtExceptionHandler zzPa;
    private final Tracker zzPb;
    private ExceptionParser zzPc;
    private GoogleAnalytics zzPd;

    public ExceptionReporter(Tracker tracker, Thread.UncaughtExceptionHandler originalHandler, Context context) {
        if (tracker == null) {
            throw new NullPointerException("tracker cannot be null");
        } else if (context == null) {
            throw new NullPointerException("context cannot be null");
        } else {
            this.zzPa = originalHandler;
            this.zzPb = tracker;
            this.zzPc = new StandardExceptionParser(context, new ArrayList());
            this.mContext = context.getApplicationContext();
            zzae.v("ExceptionReporter created, original handler is " + (originalHandler == null ? "null" : originalHandler.getClass().getName()));
        }
    }

    public ExceptionParser getExceptionParser() {
        return this.zzPc;
    }

    public void setExceptionParser(ExceptionParser exceptionParser) {
        this.zzPc = exceptionParser;
    }

    public void uncaughtException(Thread t, Throwable e) {
        String str = "UncaughtException";
        if (this.zzPc != null) {
            str = this.zzPc.getDescription(t != null ? t.getName() : null, e);
        }
        zzae.v("Reporting uncaught exception: " + str);
        this.zzPb.send(new HitBuilders.ExceptionBuilder().setDescription(str).setFatal(true).build());
        GoogleAnalytics zziC = zziC();
        zziC.dispatchLocalHits();
        zziC.zziG();
        if (this.zzPa != null) {
            zzae.v("Passing exception to the original handler");
            this.zzPa.uncaughtException(t, e);
        }
    }

    /* access modifiers changed from: package-private */
    public GoogleAnalytics zziC() {
        if (this.zzPd == null) {
            this.zzPd = GoogleAnalytics.getInstance(this.mContext);
        }
        return this.zzPd;
    }

    /* access modifiers changed from: package-private */
    public Thread.UncaughtExceptionHandler zziD() {
        return this.zzPa;
    }
}
