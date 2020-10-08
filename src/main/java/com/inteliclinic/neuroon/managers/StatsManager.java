package com.inteliclinic.neuroon.managers;

import android.content.Context;
import android.support.v7.widget.ActivityChooserView;
import com.inteliclinic.lucid.LucidConfiguration;
import com.inteliclinic.neuroon.models.bluetooth.BleDataFrame;
import com.inteliclinic.neuroon.models.data.Event;
import com.inteliclinic.neuroon.models.data.Sleep;
import com.inteliclinic.neuroon.models.stats.SleepStage;
import com.inteliclinic.neuroon.models.stats.StagingData;
import com.inteliclinic.neuroon.utils.DateUtils;
import de.greenrobot.event.EventBus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class StatsManager extends BaseManager implements IStatsManager {
    private static final int TIME_HOUR = 3600;
    private static Context mContext;
    private static StatsManager mInstance;

    private StatsManager() {
        EventBus.getDefault().post(new ManagerStartedEvent(this));
    }

    public static StatsManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new StatsManager();
            mContext = context;
        }
        return mInstance;
    }

    public static StatsManager getInstance() {
        if (mInstance != null) {
            return mInstance;
        }
        throw new NullPointerException("Manager is not initialized");
    }

    public static void analyzeSleep(Sleep sleep) {
        StagingData stagingData;
        Analytics.processFeaturesFromSleep(sleep);
        int[] pulseArray = sleep.getPulseArray();
        if (!(pulseArray.length <= 2 || (stagingData = AnalyticsManager.getInstance().computeHypnogramForData(sleep)) == null || stagingData.getHypnogram() == null)) {
            int sumRem = 0;
            int sumLight = 0;
            int sumDeep = 0;
            int sumAwake = 0;
            int countRem = 0;
            int countNRem = 0;
            int awakenings = 0;
            for (SleepStage stage : stagingData.getHypnogram()) {
                switch (stage.getStage()) {
                    case 1:
                        sumAwake += stage.getStageLength();
                        if (!stage.equals(stagingData.getHypnogram().get(0))) {
                            awakenings++;
                            break;
                        } else {
                            sleep.setTimeFallAsleep(sumAwake);
                            break;
                        }
                    case 2:
                        countNRem++;
                        sumLight += stage.getStageLength();
                        break;
                    case 3:
                        countRem++;
                        sumRem += stage.getStageLength();
                        break;
                    case 4:
                        countNRem++;
                        sumDeep += stage.getStageLength();
                        break;
                }
            }
            sleep.setRemDuration(sumRem);
            sleep.setLightDuration(sumLight);
            sleep.setDeepDuration(sumDeep);
            sleep.setAwakeDuration(sumAwake);
            sleep.setAwakenings(awakenings);
            sleep.setHypnogram(stagingData);
            getInstance().lucidBroadcast("rem-count-few", countRem < 4 ? 1.0f : -1.0f);
            getInstance().lucidBroadcast("rem-count-many", countRem >= 4 ? 1.0f : -1.0f);
            getInstance().lucidBroadcast("nrem-count-few", countNRem < 3 ? 1.0f : -1.0f);
            getInstance().lucidBroadcast("nrem-count-average", (countNRem < 3 || countNRem >= 6) ? -1.0f : 1.0f);
            getInstance().lucidBroadcast("nrem-count-many", countNRem >= 6 ? 1.0f : -1.0f);
            int timeToFallingAsleep = sleep.getTimeFallAsleep() / 30;
            getInstance().lucidBroadcast("latency-short", timeToFallingAsleep < 5 ? 1.0f : -1.0f);
            getInstance().lucidBroadcast("latency-average", (timeToFallingAsleep < 5 || timeToFallingAsleep >= 30) ? -1.0f : 1.0f);
            getInstance().lucidBroadcast("latency-long", (timeToFallingAsleep < 30 || timeToFallingAsleep >= 60) ? -1.0f : 1.0f);
            getInstance().lucidBroadcast("latency-very-long", timeToFallingAsleep >= 60 ? 1.0f : -1.0f);
            getInstance().lucidBroadcast("deep-shortage", ((double) (((float) sleep.getDeepDurationProcentage()) / 100.0f)) < 0.15d ? 1.0f : -1.0f);
            getInstance().lucidBroadcast("light-shortage", ((double) (((float) sleep.getLightDurationProcentage()) / 100.0f)) < 0.4d ? 1.0f : -1.0f);
        }
        int pulseAverageSleep = 0;
        int pulseAverageAwake = 0;
        int pulseHighest = 0;
        int pulseLowest = ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        int length = pulseArray.length;
        for (int i = 0; i < length; i++) {
            int pulseValue = pulseArray[i];
            if (pulseValue > pulseHighest) {
                pulseHighest = pulseValue;
            }
            if (pulseValue < pulseLowest) {
                pulseLowest = pulseValue;
            }
            pulseAverageSleep += pulseValue;
            pulseAverageAwake += pulseValue;
        }
        if (pulseArray.length > 0) {
            sleep.setSleepPulseAverage(pulseAverageSleep / pulseArray.length);
            sleep.setAwakePulseAverage(pulseAverageAwake / pulseArray.length);
        }
        sleep.setHighestPulse(pulseHighest);
        sleep.setLowestPulse(pulseLowest);
        sleep.setPulseArray(pulseArray);
        int sleepScore = sleep.getSleepScore();
        sleep.setComputingAppConfig((long) LucidConfiguration.getAppConfigVersion(mContext));
        getInstance().lucidBroadcast("sleep-score", ((float) sleepScore) / 100.0f);
        getInstance().lucidBroadcast("sleep-good", sleepScore >= 80 ? 1.0f : -1.0f);
        getInstance().lucidBroadcast("sleep-average", (sleepScore < 60 || sleepScore >= 80) ? -1.0f : 1.0f);
        getInstance().lucidBroadcast("sleep-bad", (sleepScore < 40 || sleepScore >= 60) ? -1.0f : 1.0f);
        getInstance().lucidBroadcast("sleep-none", sleepScore < 40 ? 1.0f : -1.0f);
        int duration = sleep.getSleepDuration();
        getInstance().lucidBroadcast("sleep-length-extremely-short", duration < 14400 ? 1.0f : -1.0f);
        getInstance().lucidBroadcast("sleep-length-very-short", (duration < 14400 || duration >= 21600) ? -1.0f : 1.0f);
        getInstance().lucidBroadcast("sleep-length-short", (duration < 21600 || duration >= 25200) ? -1.0f : 1.0f);
        getInstance().lucidBroadcast("sleep-length-medium", (duration < 25200 || duration >= 28800) ? -1.0f : 1.0f);
        getInstance().lucidBroadcast("sleep-length-long", (duration < 28800 || duration >= 36000) ? -1.0f : 1.0f);
        getInstance().lucidBroadcast("sleep-length-very-long", duration >= 36000 ? 1.0f : -1.0f);
        sleep.setSyncState(-1, false);
    }

    public Map<String, Progress> getProgressForWeek() {
        return createProgressWithData(DataManager.getInstance().getSleepsFromLastDays(7));
    }

    public Map<String, Progress> getProgressForMonth() {
        return createProgressWithData(DataManager.getInstance().getSleepsFromLastDays(30));
    }

    private Map<String, Progress> createProgressWithData(List<Sleep> sleepsFromLastDays) {
        Map<String, Progress> res = new HashMap<>(4);
        Progress sleepScoreProgress = new Progress();
        Progress durationProgress = new Progress();
        Progress fallingProgress = new Progress();
        Progress awakeningsProgress = new Progress();
        for (Sleep sleep : sleepsFromLastDays) {
            sleepScoreProgress.add(sleep.getSleepScore());
            int sleepDuration = sleep.getSleepDuration();
            if (sleepDuration != 0) {
                durationProgress.add(sleepDuration);
            }
            fallingProgress.add(sleep.getTimeFallAsleep());
            awakeningsProgress.add(sleep.getAwakenings());
        }
        res.put("SleepScore", sleepScoreProgress);
        res.put("Duration", durationProgress);
        res.put("Falling", fallingProgress);
        res.put("Awakenings", awakeningsProgress);
        return res;
    }

    public Progress getSleepScoreProgressForWeek() {
        return createSleepScoreProgressWithData(DataManager.getInstance().getSleepScoreSleepsFromLastDays(7));
    }

    public Progress getSleepScoreProgressForMonth() {
        return createSleepScoreProgressWithData(DataManager.getInstance().getSleepScoreSleepsFromLastDays(30));
    }

    private Progress createSleepScoreProgressWithData(List<Sleep> sleepsFromLastDays) {
        Progress progress = new Progress();
        for (Sleep sleep : sleepsFromLastDays) {
            progress.add(sleep.getSleepScore());
        }
        return progress;
    }

    public Progress getSleepDurationProgressForWeek() {
        return createSleepDurationProgressWithData(DataManager.getInstance().getSleepDurationSleepsFromLastDays(7));
    }

    public Progress getSleepDurationProgressForMonth() {
        return createSleepDurationProgressWithData(DataManager.getInstance().getSleepDurationSleepsFromLastDays(30));
    }

    private Progress createSleepDurationProgressWithData(List<Sleep> sleepsFromLastDays) {
        Progress progress = new Progress();
        for (Sleep sleep : sleepsFromLastDays) {
            int sleepDuration = sleep.getSleepDuration();
            if (sleepDuration != 0) {
                progress.add(sleepDuration);
            }
        }
        return progress;
    }

    public Progress getTimeToFallAsleepProgressForWeek() {
        return createTimeToFallAsleepProgressWithData(DataManager.getInstance().getTimeToFallAsleepSleepsFromLastDays(7));
    }

    public Progress getTimeToFallAsleepProgressForMonth() {
        return createTimeToFallAsleepProgressWithData(DataManager.getInstance().getTimeToFallAsleepSleepsFromLastDays(30));
    }

    private Progress createTimeToFallAsleepProgressWithData(List<Sleep> sleepsFromLastDays) {
        Progress progress = new Progress();
        for (Sleep sleep : sleepsFromLastDays) {
            progress.add(sleep.getTimeFallAsleep());
        }
        return progress;
    }

    public Progress getNumberOfAwakeningsProgressForWeek() {
        return createNumberOfAwakeningsProgressWithData(DataManager.getInstance().getNumberOfAwakeningsSleepsFromLastDays(7));
    }

    public Progress getNumberOfAwakeningsProgressForMonth() {
        return createNumberOfAwakeningsProgressWithData(DataManager.getInstance().getNumberOfAwakeningsSleepsFromLastDays(30));
    }

    private Progress createNumberOfAwakeningsProgressWithData(List<Sleep> sleepsFromLastDays) {
        Progress progress = new Progress();
        for (Sleep sleep : sleepsFromLastDays) {
            progress.add(sleep.getAwakenings());
        }
        return progress;
    }

    public List<Event> getNapRecommendations() {
        List<Event> events = new ArrayList<>();
        events.add(Event.lightBoostTherapy());
        events.add(Event.powerNapTherapy(1200000));
        Sleep lastSleep = DataManager.getInstance().getLastSleepByDateForNaps();
        if (lastSleep != null) {
            events.add(Event.bodyNapTherapy(DateUtils.dateAddSeconds(lastSleep.getEndDate(), 28800), DateUtils.dateAddSeconds(lastSleep.getEndDate(), 36000)));
            events.add(Event.remNapTherapy(DateUtils.dateAddSeconds(lastSleep.getEndDate(), 7200), DateUtils.dateAddSeconds(lastSleep.getEndDate(), 14400)));
            events.add(Event.ultimateNapTherapy(DateUtils.dateAddSeconds(lastSleep.getEndDate(), 21600), DateUtils.dateAddSeconds(lastSleep.getEndDate(), 28800)));
        }
        return events;
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object methodWithLucidKey(java.lang.String r11, java.util.List<java.lang.Object> r12) {
        /*
            r10 = this;
            r6 = 0
            r5 = 0
            r7 = -1
            int r8 = r11.hashCode()     // Catch:{ Exception -> 0x00b3 }
            switch(r8) {
                case 10558197: goto L_0x001c;
                case 678196629: goto L_0x0013;
                default: goto L_0x000a;
            }     // Catch:{ Exception -> 0x00b3 }
        L_0x000a:
            r5 = r7
        L_0x000b:
            switch(r5) {
                case 0: goto L_0x0026;
                case 1: goto L_0x004f;
                default: goto L_0x000e;
            }     // Catch:{ Exception -> 0x00b3 }
        L_0x000e:
            java.lang.Object r5 = super.methodWithLucidKey(r11, r12)     // Catch:{ Exception -> 0x00b3 }
        L_0x0012:
            return r5
        L_0x0013:
            java.lang.String r8 = "get-sleeps-count"
            boolean r8 = r11.equals(r8)     // Catch:{ Exception -> 0x00b3 }
            if (r8 == 0) goto L_0x000a
            goto L_0x000b
        L_0x001c:
            java.lang.String r5 = "get-nth-sleep"
            boolean r5 = r11.equals(r5)     // Catch:{ Exception -> 0x00b3 }
            if (r5 == 0) goto L_0x000a
            r5 = 1
            goto L_0x000b
        L_0x0026:
            com.inteliclinic.neuroon.managers.IDataManager r5 = com.inteliclinic.neuroon.managers.DataManager.getInstance()     // Catch:{ Exception -> 0x00b3 }
            java.util.List r4 = r5.getSleepsByDateDescending()     // Catch:{ Exception -> 0x00b3 }
            int r2 = r4.size()     // Catch:{ Exception -> 0x00b3 }
            java.lang.String r5 = "StatsManager"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00b3 }
            r7.<init>()     // Catch:{ Exception -> 0x00b3 }
            java.lang.String r8 = "sleeps-count: "
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ Exception -> 0x00b3 }
            java.lang.StringBuilder r7 = r7.append(r2)     // Catch:{ Exception -> 0x00b3 }
            java.lang.String r7 = r7.toString()     // Catch:{ Exception -> 0x00b3 }
            android.util.Log.d(r5, r7)     // Catch:{ Exception -> 0x00b3 }
            java.lang.Integer r5 = java.lang.Integer.valueOf(r2)     // Catch:{ Exception -> 0x00b3 }
            goto L_0x0012
        L_0x004f:
            r1 = 0
            r5 = 0
            java.lang.Object r5 = r12.get(r5)     // Catch:{ Exception -> 0x00b3 }
            boolean r5 = r5 instanceof java.lang.Integer     // Catch:{ Exception -> 0x00b3 }
            if (r5 == 0) goto L_0x0064
            r5 = 0
            java.lang.Object r5 = r12.get(r5)     // Catch:{ Exception -> 0x00b3 }
            java.lang.Integer r5 = (java.lang.Integer) r5     // Catch:{ Exception -> 0x00b3 }
            int r1 = r5.intValue()     // Catch:{ Exception -> 0x00b3 }
        L_0x0064:
            r5 = 0
            java.lang.Object r5 = r12.get(r5)     // Catch:{ Exception -> 0x00b3 }
            boolean r5 = r5 instanceof java.lang.Double     // Catch:{ Exception -> 0x00b3 }
            if (r5 == 0) goto L_0x0078
            r5 = 0
            java.lang.Object r5 = r12.get(r5)     // Catch:{ Exception -> 0x00b3 }
            java.lang.Double r5 = (java.lang.Double) r5     // Catch:{ Exception -> 0x00b3 }
            int r1 = r5.intValue()     // Catch:{ Exception -> 0x00b3 }
        L_0x0078:
            com.inteliclinic.neuroon.managers.IDataManager r5 = com.inteliclinic.neuroon.managers.DataManager.getInstance()     // Catch:{ Exception -> 0x00b3 }
            java.util.List r3 = r5.getSleepsByDateDescending()     // Catch:{ Exception -> 0x00b3 }
            if (r3 == 0) goto L_0x00a9
            int r5 = r3.size()     // Catch:{ Exception -> 0x00b3 }
            if (r1 >= r5) goto L_0x00a9
            if (r1 < 0) goto L_0x00a9
            java.lang.String r5 = "StatsManager"
            java.lang.String r7 = "nth-sleep:return sleep"
            android.util.Log.d(r5, r7)     // Catch:{ Exception -> 0x00b3 }
            com.inteliclinic.neuroon.managers.IDataManager r7 = com.inteliclinic.neuroon.managers.DataManager.getInstance()     // Catch:{ Exception -> 0x00b3 }
            java.lang.Object r5 = r3.get(r1)     // Catch:{ Exception -> 0x00b3 }
            com.inteliclinic.neuroon.models.data.Sleep r5 = (com.inteliclinic.neuroon.models.data.Sleep) r5     // Catch:{ Exception -> 0x00b3 }
            long r8 = r5.getId()     // Catch:{ Exception -> 0x00b3 }
            com.inteliclinic.neuroon.models.data.Sleep r5 = r7.getSleepById(r8)     // Catch:{ Exception -> 0x00b3 }
            java.util.Map r5 = com.inteliclinic.neuroon.managers.AnalyticsManager.sleepDictionary(r5)     // Catch:{ Exception -> 0x00b3 }
            goto L_0x0012
        L_0x00a9:
            java.lang.String r5 = "StatsManager"
            java.lang.String r7 = "nth-sleep:return null"
            android.util.Log.d(r5, r7)     // Catch:{ Exception -> 0x00b3 }
            r5 = r6
            goto L_0x0012
        L_0x00b3:
            r0 = move-exception
            r0.printStackTrace()
            r5 = r6
            goto L_0x0012
        */
        throw new UnsupportedOperationException("Method not decompiled: com.inteliclinic.neuroon.managers.StatsManager.methodWithLucidKey(java.lang.String, java.util.List):java.lang.Object");
    }

    public String getLucidDelegateKey() {
        return "stats-manager";
    }

    static class Analytics {
        Analytics() {
        }

        public static int computeSleepScoreForData(Sleep data) {
            float durationsMins = ((float) data.getSleepDuration()) / 60.0f;
            float durationBase = Math.max(0.15f * ((float) 100), (Math.min(420.0f, durationsMins) / 420.0f) * 0.85f * ((float) 100));
            float durationBonus = Math.max(0.0f, Math.min(1.0f, (durationsMins - 420.0f) / (540.0f - 420.0f)) * (1.0f - 0.85f) * ((float) 100));
            return (int) ((durationBase + durationBonus) * ((durationsMins - Math.min(durationsMins, ((float) data.getAwakeDuration()) / 60.0f)) / durationsMins));
        }

        public static List<SleepStage> computeHypnogramForData(byte[] data) {
            List<SleepStage> values = new ArrayList<>(15);
            Random random = new Random(0);
            values.add(new SleepStage(1, random.nextInt() / 20));
            values.add(new SleepStage(2, random.nextInt() / 20));
            values.add(new SleepStage(4, random.nextInt() / 20));
            values.add(new SleepStage(3, random.nextInt() / 20));
            values.add(new SleepStage(2, random.nextInt() / 20));
            values.add(new SleepStage(4, random.nextInt() / 20));
            values.add(new SleepStage(1, random.nextInt() / 20));
            values.add(new SleepStage(2, random.nextInt() / 20));
            values.add(new SleepStage(4, random.nextInt() / 20));
            values.add(new SleepStage(2, random.nextInt() / 20));
            values.add(new SleepStage(3, random.nextInt() / 20));
            values.add(new SleepStage(2, random.nextInt() / 20));
            values.add(new SleepStage(4, random.nextInt() / 20));
            values.add(new SleepStage(1, random.nextInt() / 20));
            return values;
        }

        public static void processFeaturesFromSleep(Sleep sleep) {
            byte[] rawData = sleep.getRawData();
            int len = rawData.length / 20;
            byte[] frame = new byte[20];
            int[] pulseArray = new int[((len - 2) / 2)];
            int[] pulseStdArray = new int[((len - 2) / 2)];
            int[] accelArray = new int[((len - 2) / 2)];
            int[] accelStdArray = new int[((len - 2) / 2)];
            int[] deltaLowerArray = new int[((len - 2) / 2)];
            int[] deltaHigherArray = new int[((len - 2) / 2)];
            int[] thetaArray = new int[((len - 2) / 2)];
            int[] alphaLowerArray = new int[((len - 2) / 2)];
            int[] spindlesArray = new int[((len - 2) / 2)];
            int[] spindlesStdArray = new int[((len - 2) / 2)];
            int[] signalQualityArray = new int[((len - 2) / 2)];
            int[] alphaHigherArray = new int[((len - 2) / 2)];
            int[] betaLowerArray = new int[((len - 2) / 2)];
            int[] betaHigherArray = new int[((len - 2) / 2)];
            int[] powerArray = new int[((len - 2) / 2)];
            int[] powerNoDeltaArray = new int[((len - 2) / 2)];
            int[] temperatureArray = new int[((len - 2) / 2)];
            for (int i = 0; i < len - 3; i++) {
                System.arraycopy(rawData, (i + 2) * 20, frame, 0, 20);
                BleDataFrame dataFrame = new BleDataFrame(frame);
                if (dataFrame.getFrameNumber() % 2 == 0) {
                    pulseArray[i / 2] = dataFrame.getPulse().intValue();
                    pulseStdArray[i / 2] = dataFrame.getPrv().intValue();
                    accelArray[i / 2] = dataFrame.getAccEvents().intValue();
                    accelStdArray[i / 2] = dataFrame.getAccMax().intValue();
                    deltaLowerArray[i / 2] = dataFrame.getLowDelta().intValue();
                    deltaHigherArray[i / 2] = dataFrame.getHighDelta().intValue();
                    thetaArray[i / 2] = dataFrame.getTheta().intValue();
                    alphaLowerArray[i / 2] = dataFrame.getLowAlpha().intValue();
                    spindlesArray[i / 2] = dataFrame.getSpindles().intValue();
                    spindlesStdArray[i / 2] = dataFrame.getSpindlesStd().intValue();
                } else {
                    signalQualityArray[i / 2] = dataFrame.getSignalQuality().intValue();
                    alphaHigherArray[i / 2] = dataFrame.getHighAlpha().intValue();
                    betaLowerArray[i / 2] = dataFrame.getLowBeta().intValue();
                    betaHigherArray[i / 2] = dataFrame.getHighBeta().intValue();
                    powerArray[i / 2] = dataFrame.getPower().intValue();
                    powerNoDeltaArray[i / 2] = dataFrame.getPowNoDelta().intValue();
                    temperatureArray[i / 2] = dataFrame.getTemperature().intValue();
                }
            }
            sleep.setPulseArray(pulseArray);
            sleep.setPulseStdArray(pulseStdArray);
            sleep.setAcceleremeterArray(accelArray);
            sleep.setAcceleremeterStdArray(accelArray);
            sleep.setDeltaLowerArray(deltaLowerArray);
            sleep.setDeltaHigherArray(deltaHigherArray);
            sleep.setThetaArray(thetaArray);
            sleep.setAlphaLowerArray(alphaLowerArray);
            sleep.setSpindlesArray(spindlesArray);
            sleep.setSpindlesStdArray(spindlesStdArray);
            sleep.setSignalQualityArray(signalQualityArray);
            sleep.setAlphaHigherArray(alphaHigherArray);
            sleep.setBetaLowerArray(betaLowerArray);
            sleep.setBetaHigherArray(betaHigherArray);
            sleep.setPowerArray(powerArray);
            sleep.setPowerNoDeltaArray(powerNoDeltaArray);
            sleep.setTemperatureArray(temperatureArray);
        }
    }

    public final class Progress {
        private int maximum;
        private int minimum;
        private List<Integer> raw;
        private int sum;

        private Progress() {
            this.minimum = ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
            this.sum = 0;
            this.maximum = 0;
            this.raw = new ArrayList();
        }

        public void add(int value) {
            if (value < this.minimum) {
                this.minimum = value;
            }
            this.sum += value;
            if (value > this.maximum) {
                this.maximum = value;
            }
            this.raw.add(Integer.valueOf(value));
        }

        public int min() {
            if (this.minimum == Integer.MAX_VALUE) {
                return 0;
            }
            return this.minimum;
        }

        public int max() {
            return this.maximum;
        }

        public int average() {
            int size = this.raw.size();
            if (size != 0) {
                return this.sum / size;
            }
            return 0;
        }

        public List<Integer> raw() {
            return this.raw;
        }
    }
}
