package com.inteliclinic.neuroon.managers;

import com.crashlytics.android.Crashlytics;
import com.inteliclinic.neuroon.models.data.Sleep;
import com.inteliclinic.neuroon.models.stats.StagingData;
import java.util.HashMap;
import java.util.Map;

public class AnalyticsManager extends BaseManager {
    private static final String TAG = AnalyticsManager.class.getSimpleName();
    private static AnalyticsManager mInstance;

    public static AnalyticsManager getInstance() {
        if (mInstance == null) {
            mInstance = new AnalyticsManager();
        }
        return mInstance;
    }

    public static Map<String, Object> sleepDictionary(Sleep sleep) {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        sleepDictionaryPut(stringObjectHashMap, "pulse", sleep.getPulseArray());
        sleepDictionaryPut(stringObjectHashMap, "pulse-std", sleep.getPulseStdArray());
        sleepDictionaryPut(stringObjectHashMap, "accel-events", sleep.getAccelerometerArray());
        sleepDictionaryPut(stringObjectHashMap, "accel-max", sleep.getAccelerometerStdArray());
        sleepDictionaryPut(stringObjectHashMap, "delta-lower", sleep.getDeltaLowerArray());
        sleepDictionaryPut(stringObjectHashMap, "delta-higher", sleep.getDeltaHigherArray());
        sleepDictionaryPut(stringObjectHashMap, "theta", sleep.getThetaArray());
        sleepDictionaryPut(stringObjectHashMap, "alpha-lower", sleep.getAlphaLowerArray());
        sleepDictionaryPut(stringObjectHashMap, "spindles", sleep.getSpindlesArray());
        sleepDictionaryPut(stringObjectHashMap, "spindles-std", sleep.getSpindlesStdArray());
        sleepDictionaryPut(stringObjectHashMap, "signal-quality", sleep.getSignalQualityArray());
        sleepDictionaryPut(stringObjectHashMap, "alpha-higher", sleep.getAlphaHigherArray());
        sleepDictionaryPut(stringObjectHashMap, "beta-lower", sleep.getBetaLowerArray());
        sleepDictionaryPut(stringObjectHashMap, "beta-higher", sleep.getBetaHigherArray());
        sleepDictionaryPut(stringObjectHashMap, "power", sleep.getPowerArray());
        sleepDictionaryPut(stringObjectHashMap, "power-no-delta", sleep.getPowerNoDeltaArray());
        sleepDictionaryPut(stringObjectHashMap, "temperature", sleep.getTemperatureArray());
        return stringObjectHashMap;
    }

    private static void sleepDictionaryPut(Map<String, Object> map, String name, int[] value) {
        if (value == null) {
            map.put(name, new int[0]);
        } else {
            map.put(name, value);
        }
    }

    private static String sleepDictionaryToAlist(Sleep sleep) {
        StringBuilder builder = new StringBuilder("'(");
        sleepItemToAList(builder, "pulse", sleep.getPulseArray());
        sleepItemToAList(builder, "pulse-std", sleep.getPulseStdArray());
        sleepItemToAList(builder, "accel-events", sleep.getAccelerometerArray());
        sleepItemToAList(builder, "accel-max", sleep.getAccelerometerStdArray());
        sleepItemToAList(builder, "delta-lower", sleep.getDeltaLowerArray());
        sleepItemToAList(builder, "delta-higher", sleep.getDeltaHigherArray());
        sleepItemToAList(builder, "theta", sleep.getThetaArray());
        sleepItemToAList(builder, "alpha-lower", sleep.getAlphaLowerArray());
        sleepItemToAList(builder, "spindles", sleep.getSpindlesArray());
        sleepItemToAList(builder, "spindles-std", sleep.getSpindlesStdArray());
        sleepItemToAList(builder, "signal-quality", sleep.getSignalQualityArray());
        sleepItemToAList(builder, "alpha-higher", sleep.getAlphaHigherArray());
        sleepItemToAList(builder, "beta-lower", sleep.getBetaLowerArray());
        sleepItemToAList(builder, "beta-higher", sleep.getBetaHigherArray());
        sleepItemToAList(builder, "power", sleep.getPowerArray());
        sleepItemToAList(builder, "power-no-delta", sleep.getPowerNoDeltaArray());
        sleepItemToAList(builder, "temperature", sleep.getTemperatureArray());
        builder.append(")");
        return builder.toString();
    }

    private static void sleepItemToAList(StringBuilder builder, String name, int[] items) {
        builder.append(String.format(":%s [", new Object[]{name}));
        for (int item : items) {
            builder.append(item).append(" ");
        }
        builder.append("]");
    }

    public String getLucidDelegateKey() {
        return "analytics-manager";
    }

    public StagingData computeHypnogramForData(Sleep sleep) {
        String runStagingKey = String.format("(compute-staging %s)", new Object[]{sleepDictionaryToAlist(sleep)});
        try {
            Map hypnogram = (Map) lucidRead(Map.class, runStagingKey);
            if (hypnogram == null || hypnogram.size() != 3) {
                throw new UnsupportedOperationException("Staging not calculated");
            }
            sleep.setSleepScore(((Double) hypnogram.get("sleep-score")).intValue());
            return StagingData.createStagingFrom((Double[]) hypnogram.get("hypnogram"), (Double[]) hypnogram.get("signal-quality"));
        } catch (Exception ex) {
            if (Crashlytics.getInstance() == null) {
                Crashlytics.log(runStagingKey);
                Crashlytics.logException(ex);
            }
            return null;
        }
    }
}
