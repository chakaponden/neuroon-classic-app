package io.fabric.sdk.android;

import io.fabric.sdk.android.services.common.TimingMetric;
import io.fabric.sdk.android.services.concurrency.Priority;
import io.fabric.sdk.android.services.concurrency.PriorityAsyncTask;
import io.fabric.sdk.android.services.concurrency.UnmetDependencyException;

class InitializationTask<Result> extends PriorityAsyncTask<Void, Void, Result> {
    private static final String TIMING_METRIC_TAG = "KitInitialization";
    final Kit<Result> kit;

    public InitializationTask(Kit<Result> kit2) {
        this.kit = kit2;
    }

    /* access modifiers changed from: protected */
    public void onPreExecute() {
        super.onPreExecute();
        TimingMetric timingMetric = createAndStartTimingMetric("onPreExecute");
        try {
            boolean result = this.kit.onPreExecute();
            timingMetric.stopMeasuring();
            if (!result) {
                cancel(true);
            }
        } catch (UnmetDependencyException ex) {
            throw ex;
        } catch (Exception ex2) {
            Fabric.getLogger().e(Fabric.TAG, "Failure onPreExecute()", ex2);
            timingMetric.stopMeasuring();
            if (0 == 0) {
                cancel(true);
            }
        } catch (Throwable th) {
            timingMetric.stopMeasuring();
            if (0 == 0) {
                cancel(true);
            }
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    public Result doInBackground(Void... voids) {
        TimingMetric timingMetric = createAndStartTimingMetric("doInBackground");
        Result result = null;
        if (!isCancelled()) {
            result = this.kit.doInBackground();
        }
        timingMetric.stopMeasuring();
        return result;
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Result result) {
        this.kit.onPostExecute(result);
        this.kit.initializationCallback.success(result);
    }

    /* access modifiers changed from: protected */
    public void onCancelled(Result result) {
        this.kit.onCancelled(result);
        this.kit.initializationCallback.failure(new InitializationException(this.kit.getIdentifier() + " Initialization was cancelled"));
    }

    public Priority getPriority() {
        return Priority.HIGH;
    }

    private TimingMetric createAndStartTimingMetric(String event) {
        TimingMetric timingMetric = new TimingMetric(this.kit.getIdentifier() + "." + event, TIMING_METRIC_TAG);
        timingMetric.startMeasuring();
        return timingMetric;
    }
}
