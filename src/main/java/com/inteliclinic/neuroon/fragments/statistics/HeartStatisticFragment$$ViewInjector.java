package com.inteliclinic.neuroon.fragments.statistics;

import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;
import com.inteliclinic.neuroon.views.LightTextView;
import com.inteliclinic.neuroon.views.charts.LineChartView;

public class HeartStatisticFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, HeartStatisticFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.sleepDuration = (LightTextView) finder.findRequiredView(source, R.id.sleep_duration, "field 'sleepDuration'");
        target.sleepTimeSpan = (LightTextView) finder.findRequiredView(source, R.id.sleep_time_span, "field 'sleepTimeSpan'");
        target.timeToFall = (LightTextView) finder.findRequiredView(source, R.id.time_to_fall, "field 'timeToFall'");
        target.numberOfAwakenings = (LightTextView) finder.findRequiredView(source, R.id.number_of_awakenings, "field 'numberOfAwakenings'");
        target.startTime = (LightTextView) finder.findRequiredView(source, R.id.start_time, "field 'startTime'");
        target.endTime = (LightTextView) finder.findRequiredView(source, R.id.end_time, "field 'endTime'");
        target.mChart = (LineChartView) finder.findRequiredView(source, R.id.chart, "field 'mChart'");
        target.lowestRateTime = (LightTextView) finder.findRequiredView(source, R.id.lowest_rate_time, "field 'lowestRateTime'");
        target.highestRate = (LightTextView) finder.findRequiredView(source, R.id.highest_rate, "field 'highestRate'");
    }

    public static void reset(HeartStatisticFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.sleepDuration = null;
        target.sleepTimeSpan = null;
        target.timeToFall = null;
        target.numberOfAwakenings = null;
        target.startTime = null;
        target.endTime = null;
        target.mChart = null;
        target.lowestRateTime = null;
        target.highestRate = null;
    }
}
