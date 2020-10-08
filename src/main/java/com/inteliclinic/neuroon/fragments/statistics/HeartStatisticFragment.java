package com.inteliclinic.neuroon.fragments.statistics;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.managers.DataManager;
import com.inteliclinic.neuroon.models.data.Sleep;
import com.inteliclinic.neuroon.utils.DateUtils;
import com.inteliclinic.neuroon.views.LightTextView;
import com.inteliclinic.neuroon.views.charts.LineChartView;
import java.text.DateFormat;

public class HeartStatisticFragment extends BaseFragment implements ISleepPresentable {
    private static final String ARG_SLEEP_ID = "param1";
    @InjectView(2131755180)
    LightTextView endTime;
    @InjectView(2131755258)
    LightTextView highestRate;
    @InjectView(2131755256)
    LightTextView lowestRateTime;
    @InjectView(2131755254)
    LineChartView mChart;
    private OnFragmentInteractionListener mListener;
    private long mSleepId;
    @InjectView(2131755252)
    LightTextView numberOfAwakenings;
    @InjectView(2131755246)
    LightTextView sleepDuration;
    @InjectView(2131755248)
    LightTextView sleepTimeSpan;
    @InjectView(2131755179)
    LightTextView startTime;
    @InjectView(2131755250)
    LightTextView timeToFall;

    public interface OnFragmentInteractionListener {
    }

    public static HeartStatisticFragment newInstance(long sleepId) {
        HeartStatisticFragment fragment = new HeartStatisticFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_SLEEP_ID, sleepId);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mSleepId = getArguments().getLong(ARG_SLEEP_ID);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_heart_statistic, container, false);
        ButterKnife.inject((Object) this, view);
        fillWithData();
        return view;
    }

    private void fillWithData() {
        Sleep lastSleep = DataManager.getInstance().getSleepById(this.mSleepId);
        if (lastSleep != null) {
            this.sleepDuration.setText(String.valueOf(lastSleep.getAwakePulseAverage()));
            this.sleepTimeSpan.setText(String.valueOf(lastSleep.getSleepPulseAverage()));
            this.timeToFall.setText(String.valueOf(lastSleep.getHighestPulse()));
            this.numberOfAwakenings.setText(String.valueOf(lastSleep.getLowestPulse()));
            this.startTime.setText(DateFormat.getTimeInstance(3).format(lastSleep.getStartDate()));
            this.endTime.setText(DateFormat.getTimeInstance(3).format(lastSleep.getEndDate()));
            setPulseChartAndHighestLowestRate(this.mChart, lastSleep.getPulseArray(), lastSleep);
        }
    }

    private void setPulseChartAndHighestLowestRate(LineChartView chart, int[] pulseArray, Sleep lastSleep) {
        chart.setMinValue(0.0f);
        chart.setMaxValue(100.0f);
        if (pulseArray != null && pulseArray.length != 0) {
            int indexOfMax = 0;
            int indexOfMin = 0;
            float[] array = new float[pulseArray.length];
            for (int j = 0; j < pulseArray.length; j++) {
                array[j] = (float) pulseArray[j];
                if (pulseArray[indexOfMax] < pulseArray[j]) {
                    indexOfMax = j;
                }
                if (pulseArray[indexOfMin] > pulseArray[j]) {
                    indexOfMin = j;
                }
            }
            this.lowestRateTime.setText(DateFormat.getTimeInstance(3).format(DateUtils.dateAddSeconds(lastSleep.getStartDate(), indexOfMin * 30)));
            this.highestRate.setText(DateFormat.getTimeInstance(3).format(DateUtils.dateAddSeconds(lastSleep.getStartDate(), indexOfMax * 30)));
            chart.setValues(array);
            chart.setMinPointColor(R.color.dark_blue_3F62A8);
            chart.setMaxPointColor(R.color.light_blue_93CEFF);
        }
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

    public void presentSleep(long sleepId) {
        getArguments().putLong(ARG_SLEEP_ID, sleepId);
        this.mSleepId = sleepId;
        fillWithData();
    }
}
