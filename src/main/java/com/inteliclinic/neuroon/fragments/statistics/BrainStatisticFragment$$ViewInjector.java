package com.inteliclinic.neuroon.fragments.statistics;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;
import com.inteliclinic.neuroon.views.LightTextView;
import com.inteliclinic.neuroon.views.ThinTextView;
import com.inteliclinic.neuroon.views.charts.BarChartView;
import com.inteliclinic.neuroon.views.charts.CircleChartView;

public class BrainStatisticFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final BrainStatisticFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.barChart = (BarChartView) finder.findRequiredView(source, R.id.bar_chart, "field 'barChart'");
        target.percentChart = (CircleChartView) finder.findRequiredView(source, R.id.percent_chart, "field 'percentChart'");
        target.deepPercent = (ThinTextView) finder.findRequiredView(source, R.id.deep_percent, "field 'deepPercent'");
        target.remPercent = (ThinTextView) finder.findRequiredView(source, R.id.rem_percent, "field 'remPercent'");
        target.lightPercent = (ThinTextView) finder.findRequiredView(source, R.id.light_percent, "field 'lightPercent'");
        target.wakePercent = (ThinTextView) finder.findRequiredView(source, R.id.wake_percent, "field 'wakePercent'");
        target.startTime = (LightTextView) finder.findRequiredView(source, R.id.start_time, "field 'startTime'");
        target.endTime = (LightTextView) finder.findRequiredView(source, R.id.end_time, "field 'endTime'");
        target.overallSignal = (ThinTextView) finder.findRequiredView(source, R.id.overall_signal, "field 'overallSignal'");
        target.overallSignalContainer = (RelativeLayout) finder.findRequiredView(source, R.id.overall_signal_container, "field 'overallSignalContainer'");
        target.signalBarChart = (BarChartView) finder.findRequiredView(source, R.id.signal_bar_chart, "field 'signalBarChart'");
        target.signalContainer = (LinearLayout) finder.findRequiredView(source, R.id.signal_container, "field 'signalContainer'");
        finder.findRequiredView(source, R.id.delete_sleep, "method 'onDeleteSleepClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onDeleteSleepClick();
            }
        });
    }

    public static void reset(BrainStatisticFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.barChart = null;
        target.percentChart = null;
        target.deepPercent = null;
        target.remPercent = null;
        target.lightPercent = null;
        target.wakePercent = null;
        target.startTime = null;
        target.endTime = null;
        target.overallSignal = null;
        target.overallSignalContainer = null;
        target.signalBarChart = null;
        target.signalContainer = null;
    }
}
