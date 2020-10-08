package com.inteliclinic.neuroon.fragments.statistics;

import android.app.Activity;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.managers.DataManager;
import com.inteliclinic.neuroon.models.data.Sleep;
import com.inteliclinic.neuroon.models.stats.SleepStage;
import com.inteliclinic.neuroon.models.stats.StagingData;
import com.inteliclinic.neuroon.views.LightTextView;
import com.inteliclinic.neuroon.views.charts.BarChartView;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class TimeStatisticFragment extends BaseFragment implements ISleepPresentable {
    private static final String ARG_SLEEP_ID = "param1";
    @InjectView(2131755254)
    BarChartView chart;
    @InjectView(2131755180)
    LightTextView endTime;
    private OnFragmentInteractionListener mListener;
    private long mSleepId;
    @InjectView(2131755252)
    LightTextView numberOfAwakenings;
    @InjectView(2131755246)
    LightTextView sleepDuration;
    @InjectView(2131755211)
    LightTextView sleepScore;
    @InjectView(2131755248)
    LightTextView sleepTimeSpan;
    @InjectView(2131755179)
    LightTextView startTime;
    @InjectView(2131755480)
    LightTextView timeAwake;
    @InjectView(2131755250)
    LightTextView timeToFall;

    public interface OnFragmentInteractionListener {
    }

    public static TimeStatisticFragment newInstance(long sleepId) {
        TimeStatisticFragment fragment = new TimeStatisticFragment();
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
        View view = inflater.inflate(R.layout.fragment_time_statistic, container, false);
        ButterKnife.inject((Object) this, view);
        fillWithData();
        return view;
    }

    private void fillWithData() {
        Sleep lastSleep = DataManager.getInstance().getSleepById(this.mSleepId);
        if (lastSleep != null) {
            if (this.sleepScore != null) {
                this.sleepScore.setText(String.valueOf(lastSleep.getSleepScore()));
            }
            if (this.sleepDuration != null) {
                this.sleepDuration.setText(String.format("%02d:%02d", new Object[]{Integer.valueOf(lastSleep.getSleepDuration() / 3600), Integer.valueOf((lastSleep.getSleepDuration() / 60) % 60)}));
            }
            if (this.timeAwake != null) {
                this.timeAwake.setText(String.format("%02d:%02d", new Object[]{Integer.valueOf(lastSleep.getAwakeDuration() / 3600), Integer.valueOf((lastSleep.getAwakeDuration() / 60) % 60)}));
            }
            if (this.sleepTimeSpan != null) {
                this.sleepTimeSpan.setText(getResources().getString(R.string.time_dash_time, new Object[]{DateFormat.getTimeInstance(3).format(lastSleep.getStartDate()), DateFormat.getTimeInstance(3).format(lastSleep.getEndDate())}));
            }
            if (this.timeToFall != null) {
                this.timeToFall.setText(String.format("%02d:%02d", new Object[]{Integer.valueOf(lastSleep.getTimeFallAsleep() / 3600), Integer.valueOf((lastSleep.getTimeFallAsleep() / 60) % 60)}));
            }
            if (this.numberOfAwakenings != null) {
                this.numberOfAwakenings.setText(String.valueOf(lastSleep.getAwakenings()));
            }
            if (this.startTime != null) {
                this.startTime.setText(DateFormat.getTimeInstance(3).format(lastSleep.getStartDate()));
            }
            if (this.endTime != null) {
                this.endTime.setText(DateFormat.getTimeInstance(3).format(lastSleep.getEndDate()));
            }
            if (this.chart != null) {
                int[] colors = {getResources().getColor(R.color.dark_blue_3F62A8), getResources().getColor(R.color.light_blue_93CEFF)};
                StagingData hypnogram = lastSleep.getHypnogram();
                if (hypnogram != null && hypnogram.getHypnogram() != null) {
                    List<Pair<Integer, Integer>> valuesList = new ArrayList<>();
                    int[] colorsList = new int[hypnogram.getHypnogram().size()];
                    int i = 0;
                    for (SleepStage stage : hypnogram.getHypnogram()) {
                        switch (stage.getStage()) {
                            case 1:
                                valuesList.add(new Pair(Integer.valueOf(stage.getStageLength()), -1));
                                colorsList[i] = colors[1];
                                i++;
                                break;
                            case 2:
                                valuesList.add(new Pair(Integer.valueOf(stage.getStageLength()), 1));
                                colorsList[i] = colors[0];
                                i++;
                                break;
                            case 3:
                                valuesList.add(new Pair(Integer.valueOf(stage.getStageLength()), 1));
                                colorsList[i] = colors[0];
                                i++;
                                break;
                            case 4:
                                valuesList.add(new Pair(Integer.valueOf(stage.getStageLength()), 1));
                                colorsList[i] = colors[0];
                                i++;
                                break;
                        }
                    }
                    this.chart.setMaxYValue(1);
                    this.chart.setMinYValue(-1);
                    this.chart.setAnimateValues(valuesList, colorsList);
                }
            }
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
