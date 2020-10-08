package com.inteliclinic.neuroon.fragments.statistics;

import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;
import com.inteliclinic.neuroon.views.LightTextView;
import com.inteliclinic.neuroon.views.charts.BarChartView;

public class TimeStatisticFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, TimeStatisticFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.sleepDuration = (LightTextView) finder.findRequiredView(source, R.id.sleep_duration, "field 'sleepDuration'");
        target.sleepTimeSpan = (LightTextView) finder.findRequiredView(source, R.id.sleep_time_span, "field 'sleepTimeSpan'");
        target.timeToFall = (LightTextView) finder.findRequiredView(source, R.id.time_to_fall, "field 'timeToFall'");
        target.numberOfAwakenings = (LightTextView) finder.findRequiredView(source, R.id.number_of_awakenings, "field 'numberOfAwakenings'");
        target.startTime = (LightTextView) finder.findRequiredView(source, R.id.start_time, "field 'startTime'");
        target.endTime = (LightTextView) finder.findRequiredView(source, R.id.end_time, "field 'endTime'");
        target.chart = (BarChartView) finder.findRequiredView(source, R.id.chart, "field 'chart'");
        target.timeAwake = (LightTextView) finder.findRequiredView(source, R.id.time_awake, "field 'timeAwake'");
        target.sleepScore = (LightTextView) finder.findRequiredView(source, R.id.sleep_score, "field 'sleepScore'");
    }

    public static void reset(TimeStatisticFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.sleepDuration = null;
        target.sleepTimeSpan = null;
        target.timeToFall = null;
        target.numberOfAwakenings = null;
        target.startTime = null;
        target.endTime = null;
        target.chart = null;
        target.timeAwake = null;
        target.sleepScore = null;
    }
}
