package com.inteliclinic.neuroon.fragments.dashboard;

import android.view.View;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;
import com.inteliclinic.neuroon.views.DaysChooserView;
import com.inteliclinic.neuroon.views.LightTextView;
import com.inteliclinic.neuroon.views.SleepScoreView;
import com.inteliclinic.neuroon.views.charts.LineChartView;

public class ProgressFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final ProgressFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.sleepDurationChart = (LineChartView) finder.findRequiredView(source, R.id.sleep_duration_chart, "field 'sleepDurationChart'");
        target.daysChooser = (DaysChooserView) finder.findRequiredView(source, R.id.days_chooser, "field 'daysChooser'");
        target.timeChart = (LineChartView) finder.findRequiredView(source, R.id.time_chart, "field 'timeChart'");
        target.awakeningsChart = (LineChartView) finder.findRequiredView(source, R.id.awakenings_chart, "field 'awakeningsChart'");
        target.sleepScoreMinimum = (SleepScoreView) finder.findRequiredView(source, R.id.sleep_score_minimum, "field 'sleepScoreMinimum'");
        target.sleepScoreMinimumText = (LightTextView) finder.findRequiredView(source, R.id.sleep_score_minimum_text, "field 'sleepScoreMinimumText'");
        target.sleepScoreAverage = (SleepScoreView) finder.findRequiredView(source, R.id.sleep_score_average, "field 'sleepScoreAverage'");
        target.sleepScoreAverageText = (LightTextView) finder.findRequiredView(source, R.id.sleep_score_average_text, "field 'sleepScoreAverageText'");
        target.sleepScoreMaximum = (SleepScoreView) finder.findRequiredView(source, R.id.sleep_score_maximum, "field 'sleepScoreMaximum'");
        target.sleepScoreMaximumText = (LightTextView) finder.findRequiredView(source, R.id.sleep_score_maximum_text, "field 'sleepScoreMaximumText'");
        target.sleepScoreChart = (LineChartView) finder.findRequiredView(source, R.id.sleep_score_chart, "field 'sleepScoreChart'");
        target.sleepDurationMinimumText = (LightTextView) finder.findRequiredView(source, R.id.sleep_duration_minimum_text, "field 'sleepDurationMinimumText'");
        target.sleepDurationAverageText = (LightTextView) finder.findRequiredView(source, R.id.sleep_duration_average_text, "field 'sleepDurationAverageText'");
        target.sleepDurationMaximumText = (LightTextView) finder.findRequiredView(source, R.id.sleep_duration_maximum_text, "field 'sleepDurationMaximumText'");
        target.timeToFallMinimumText = (LightTextView) finder.findRequiredView(source, R.id.time_to_fall_minimum_text, "field 'timeToFallMinimumText'");
        target.timeToFallAverageText = (LightTextView) finder.findRequiredView(source, R.id.time_to_fall_average_text, "field 'timeToFallAverageText'");
        target.timeToFallMaximumText = (LightTextView) finder.findRequiredView(source, R.id.time_to_fall_maximum_text, "field 'timeToFallMaximumText'");
        target.awakeningsMinimumText = (LightTextView) finder.findRequiredView(source, R.id.awakenings_minimum_text, "field 'awakeningsMinimumText'");
        target.awakeningsAverageText = (LightTextView) finder.findRequiredView(source, R.id.awakenings_average_text, "field 'awakeningsAverageText'");
        target.awakeningsMaximumText = (LightTextView) finder.findRequiredView(source, R.id.awakenings_maximum_text, "field 'awakeningsMaximumText'");
        finder.findRequiredView(source, R.id.back_button, "method 'onBackButtonClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onBackButtonClick();
            }
        });
    }

    public static void reset(ProgressFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.sleepDurationChart = null;
        target.daysChooser = null;
        target.timeChart = null;
        target.awakeningsChart = null;
        target.sleepScoreMinimum = null;
        target.sleepScoreMinimumText = null;
        target.sleepScoreAverage = null;
        target.sleepScoreAverageText = null;
        target.sleepScoreMaximum = null;
        target.sleepScoreMaximumText = null;
        target.sleepScoreChart = null;
        target.sleepDurationMinimumText = null;
        target.sleepDurationAverageText = null;
        target.sleepDurationMaximumText = null;
        target.timeToFallMinimumText = null;
        target.timeToFallAverageText = null;
        target.timeToFallMaximumText = null;
        target.awakeningsMinimumText = null;
        target.awakeningsAverageText = null;
        target.awakeningsMaximumText = null;
    }
}
