package io.intercom.android.sdk.metrics;

import android.content.Context;
import android.content.SharedPreferences;
import io.intercom.android.sdk.logger.IntercomLogger;
import java.util.HashMap;
import java.util.Map;

public class MetricsStore {
    private static final String METRICS_PREFS = "INTERCOM_SDK_METRICS_PREFS";
    private final SharedPreferences prefs;

    public MetricsStore(Context context) {
        this.prefs = context.getSharedPreferences(METRICS_PREFS, 0);
    }

    public void increment(MetricType type) {
        String metricName = type.name().toLowerCase();
        int prevValue = this.prefs.getInt(metricName, 0);
        SharedPreferences.Editor editor = this.prefs.edit();
        editor.putInt(metricName, prevValue + 1);
        editor.apply();
    }

    public Map<String, Integer> getMap() {
        Map<String, Integer> metricNamesToValue = new HashMap<>();
        for (MetricType type : MetricType.values()) {
            String metricName = type.name().toLowerCase();
            int value = this.prefs.getInt(metricName, 0);
            if (value != 0) {
                metricNamesToValue.put(metricName, Integer.valueOf(value));
            }
        }
        clear();
        IntercomLogger.INTERNAL("metrics", metricNamesToValue.toString());
        return metricNamesToValue;
    }

    private void clear() {
        this.prefs.edit().clear().commit();
    }
}
