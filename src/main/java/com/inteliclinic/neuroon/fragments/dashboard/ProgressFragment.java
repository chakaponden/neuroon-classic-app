package com.inteliclinic.neuroon.fragments.dashboard;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.managers.StatsManager;
import com.inteliclinic.neuroon.views.DaysChooserView;
import com.inteliclinic.neuroon.views.LightTextView;
import com.inteliclinic.neuroon.views.SleepScoreView;
import com.inteliclinic.neuroon.views.charts.LineChartView;
import java.util.List;

public class ProgressFragment extends BaseFragment {
    @InjectView(2131755445)
    LightTextView awakeningsAverageText;
    @InjectView(2131755447)
    LineChartView awakeningsChart;
    @InjectView(2131755446)
    LightTextView awakeningsMaximumText;
    @InjectView(2131755444)
    LightTextView awakeningsMinimumText;
    @InjectView(2131755428)
    DaysChooserView daysChooser;
    private OnFragmentInteractionListener mListener;
    @InjectView(2131755437)
    LightTextView sleepDurationAverageText;
    @InjectView(2131755439)
    LineChartView sleepDurationChart;
    @InjectView(2131755438)
    LightTextView sleepDurationMaximumText;
    @InjectView(2131755436)
    LightTextView sleepDurationMinimumText;
    @InjectView(2131755431)
    SleepScoreView sleepScoreAverage;
    @InjectView(2131755432)
    LightTextView sleepScoreAverageText;
    @InjectView(2131755435)
    LineChartView sleepScoreChart;
    @InjectView(2131755433)
    SleepScoreView sleepScoreMaximum;
    @InjectView(2131755434)
    LightTextView sleepScoreMaximumText;
    @InjectView(2131755429)
    SleepScoreView sleepScoreMinimum;
    @InjectView(2131755430)
    LightTextView sleepScoreMinimumText;
    @InjectView(2131755443)
    LineChartView timeChart;
    @InjectView(2131755441)
    LightTextView timeToFallAverageText;
    @InjectView(2131755442)
    LightTextView timeToFallMaximumText;
    @InjectView(2131755440)
    LightTextView timeToFallMinimumText;

    public interface OnFragmentInteractionListener {
        void goToDashboard();
    }

    public static ProgressFragment newInstance() {
        return new ProgressFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        ButterKnife.inject((Object) this, view);
        fillSleepScoreProgress(StatsManager.getInstance().getSleepScoreProgressForWeek());
        fillSleepDurationProgress(StatsManager.getInstance().getSleepDurationProgressForWeek());
        fillTimeToFallAsleepProgress(StatsManager.getInstance().getTimeToFallAsleepProgressForWeek());
        fillNumberOfAwakeningsProgress(StatsManager.getInstance().getNumberOfAwakeningsProgressForWeek());
        this.daysChooser.setListener(new DaysChooserView.OnDaysChooserStateChangedListener() {
            public void onStateChange(DaysChooserView.DaysChooserState newState) {
                if (newState == DaysChooserView.DaysChooserState.DAYS30) {
                    ProgressFragment.this.fillSleepScoreProgress(StatsManager.getInstance().getSleepScoreProgressForMonth());
                    ProgressFragment.this.fillSleepDurationProgress(StatsManager.getInstance().getSleepDurationProgressForMonth());
                    ProgressFragment.this.fillTimeToFallAsleepProgress(StatsManager.getInstance().getTimeToFallAsleepProgressForMonth());
                    ProgressFragment.this.fillNumberOfAwakeningsProgress(StatsManager.getInstance().getNumberOfAwakeningsProgressForMonth());
                    return;
                }
                ProgressFragment.this.fillSleepScoreProgress(StatsManager.getInstance().getSleepScoreProgressForWeek());
                ProgressFragment.this.fillSleepDurationProgress(StatsManager.getInstance().getSleepDurationProgressForWeek());
                ProgressFragment.this.fillTimeToFallAsleepProgress(StatsManager.getInstance().getTimeToFallAsleepProgressForWeek());
                ProgressFragment.this.fillNumberOfAwakeningsProgress(StatsManager.getInstance().getNumberOfAwakeningsProgressForWeek());
            }
        });
        return view;
    }

    /* access modifiers changed from: private */
    public void fillSleepScoreProgress(StatsManager.Progress sleepScoreProgressForWeek) {
        this.sleepScoreMinimum.setProgress(sleepScoreProgressForWeek.min());
        this.sleepScoreMinimumText.setProgress(sleepScoreProgressForWeek.min());
        this.sleepScoreAverage.setProgress(sleepScoreProgressForWeek.average());
        this.sleepScoreAverageText.setProgress(sleepScoreProgressForWeek.average());
        this.sleepScoreMaximum.setProgress(sleepScoreProgressForWeek.max());
        this.sleepScoreMaximumText.setProgress(sleepScoreProgressForWeek.max());
        List<Integer> raw = sleepScoreProgressForWeek.raw();
        if (raw.size() > 0) {
            float[] sleepVal = new float[raw.size()];
            for (int i = 0; i < raw.size(); i++) {
                sleepVal[i] = (float) raw.get(i).intValue();
            }
            if (sleepScoreProgressForWeek.min() != sleepScoreProgressForWeek.max()) {
                this.sleepScoreChart.setMinValue(-1.0f);
                this.sleepScoreChart.setMaxValue(-1.0f);
            } else if (sleepScoreProgressForWeek.min() == 0) {
                this.sleepScoreChart.setMaxValue(10.0f);
            } else {
                this.sleepScoreChart.setMinValue(0.0f);
            }
            this.sleepScoreChart.setValues(sleepVal);
            return;
        }
        this.sleepScoreMinimum.setProgress(100);
        this.sleepScoreAverage.setProgress(100);
        this.sleepScoreMaximum.setProgress(100);
        setEmptyChart(this.sleepScoreChart);
    }

    /* access modifiers changed from: private */
    public void fillSleepDurationProgress(StatsManager.Progress sleepScoreProgressForWeek) {
        this.sleepDurationMinimumText.setText(String.format("%02d:%02d", new Object[]{Integer.valueOf(sleepScoreProgressForWeek.min() / 3600), Integer.valueOf((sleepScoreProgressForWeek.min() / 60) % 60)}));
        this.sleepDurationAverageText.setText(String.format("%02d:%02d", new Object[]{Integer.valueOf(sleepScoreProgressForWeek.average() / 3600), Integer.valueOf((sleepScoreProgressForWeek.average() / 60) % 60)}));
        this.sleepDurationMaximumText.setText(String.format("%02d:%02d", new Object[]{Integer.valueOf(sleepScoreProgressForWeek.max() / 3600), Integer.valueOf((sleepScoreProgressForWeek.max() / 60) % 60)}));
        List<Integer> raw = sleepScoreProgressForWeek.raw();
        if (raw.size() > 0) {
            float[] sleepVal = new float[raw.size()];
            for (int i = 0; i < raw.size(); i++) {
                sleepVal[i] = (float) raw.get(i).intValue();
            }
            if (sleepScoreProgressForWeek.min() != sleepScoreProgressForWeek.max()) {
                this.sleepDurationChart.setMinValue(-1.0f);
                this.sleepDurationChart.setMaxValue(-1.0f);
            } else if (sleepScoreProgressForWeek.min() == 0) {
                this.sleepDurationChart.setMaxValue(10.0f);
            } else {
                this.sleepDurationChart.setMinValue(0.0f);
            }
            this.sleepDurationChart.setValues(sleepVal);
            return;
        }
        setEmptyChart(this.sleepDurationChart);
    }

    /* access modifiers changed from: private */
    public void fillTimeToFallAsleepProgress(StatsManager.Progress sleepScoreProgressForWeek) {
        this.timeToFallMinimumText.setText(String.format("%02d:%02d", new Object[]{Integer.valueOf(sleepScoreProgressForWeek.min() / 3600), Integer.valueOf((sleepScoreProgressForWeek.min() / 60) % 60)}));
        this.timeToFallAverageText.setText(String.format("%02d:%02d", new Object[]{Integer.valueOf(sleepScoreProgressForWeek.average() / 3600), Integer.valueOf((sleepScoreProgressForWeek.average() / 60) % 60)}));
        this.timeToFallMaximumText.setText(String.format("%02d:%02d", new Object[]{Integer.valueOf(sleepScoreProgressForWeek.max() / 3600), Integer.valueOf((sleepScoreProgressForWeek.max() / 60) % 60)}));
        List<Integer> raw = sleepScoreProgressForWeek.raw();
        if (raw.size() > 0) {
            float[] sleepVal = new float[raw.size()];
            for (int i = 0; i < raw.size(); i++) {
                sleepVal[i] = (float) raw.get(i).intValue();
            }
            if (sleepScoreProgressForWeek.min() != sleepScoreProgressForWeek.max()) {
                this.timeChart.setMinValue(-1.0f);
                this.timeChart.setMaxValue(-1.0f);
            } else if (sleepScoreProgressForWeek.min() == 0) {
                this.timeChart.setMaxValue(10.0f);
            } else {
                this.timeChart.setMinValue(0.0f);
            }
            this.timeChart.setValues(sleepVal);
            return;
        }
        setEmptyChart(this.timeChart);
    }

    /* access modifiers changed from: private */
    public void fillNumberOfAwakeningsProgress(StatsManager.Progress sleepScoreProgressForWeek) {
        this.awakeningsMinimumText.setText(String.valueOf(sleepScoreProgressForWeek.min()));
        this.awakeningsAverageText.setText(String.valueOf(sleepScoreProgressForWeek.average()));
        this.awakeningsMaximumText.setText(String.valueOf(sleepScoreProgressForWeek.max()));
        List<Integer> raw = sleepScoreProgressForWeek.raw();
        if (raw.size() > 0) {
            float[] sleepVal = new float[raw.size()];
            for (int i = 0; i < raw.size(); i++) {
                sleepVal[i] = (float) raw.get(i).intValue();
            }
            if (sleepScoreProgressForWeek.min() != sleepScoreProgressForWeek.max()) {
                this.awakeningsChart.setMinValue(-1.0f);
                this.awakeningsChart.setMaxValue(-1.0f);
            } else if (sleepScoreProgressForWeek.min() == 0) {
                this.awakeningsChart.setMaxValue(10.0f);
            } else {
                this.awakeningsChart.setMinValue(0.0f);
            }
            this.awakeningsChart.setValues(sleepVal);
            return;
        }
        setEmptyChart(this.awakeningsChart);
    }

    private void setEmptyChart(LineChartView chart) {
        chart.setMinValue(0.0f);
        chart.setMaxValue(10.0f);
        chart.setValues(new float[]{0.0f, 0.0f, 0.0f});
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({2131755131})
    public void onBackButtonClick() {
        if (this.mListener != null) {
            this.mListener.goToDashboard();
        }
    }
}
