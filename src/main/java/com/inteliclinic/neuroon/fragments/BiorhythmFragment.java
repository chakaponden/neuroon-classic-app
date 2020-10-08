package com.inteliclinic.neuroon.fragments;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import com.google.android.gms.analytics.HitBuilders;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.managers.account.AccountManager;
import com.inteliclinic.neuroon.utils.FontUtils;
import com.inteliclinic.neuroon.views.LightTextView;
import com.inteliclinic.neuroon.views.TimeDisplayBarView;
import java.text.DateFormatSymbols;
import java.util.ArrayList;

public class BiorhythmFragment extends BaseFragment implements IDataStorableFragment, TimeDisplayBarView.OnTimeChangedListener {
    private static final String ARG_STANDALONE = "arg_is_standalone";
    @InjectView(2131755164)
    ImageView closeButton;
    @InjectView(2131755165)
    LightTextView groupedBtn;
    private ArrayList<Integer> mAlarms;
    private boolean mDaysSeparated;
    private boolean mIsStandalone;
    @InjectView(2131755166)
    LightTextView separatedBtn;
    @InjectViews({2131755167, 2131755168, 2131755169, 2131755170, 2131755171, 2131755172, 2131755173})
    TimeDisplayBarView[] timers;

    public static BiorhythmFragment newInstance() {
        return newInstance(true);
    }

    public static BiorhythmFragment newInstance(boolean isStandalone) {
        BiorhythmFragment fragment = new BiorhythmFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_STANDALONE, isStandalone);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mIsStandalone = getArguments().getBoolean(ARG_STANDALONE);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_biorythm, container, false);
        ButterKnife.inject((Object) this, view);
        setView(view);
        this.mDaysSeparated = AccountManager.getInstance().isBiorhythmDaysSeparated();
        setupDestinationButton();
        setItemsNumber();
        this.mAlarms = AccountManager.getInstance().getBiorhythmAlarms();
        setItemsValue();
        setListeners();
        return view;
    }

    public void onResume() {
        super.onResume();
        getTracker().setScreenName("biorhythm");
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void setListeners() {
        for (TimeDisplayBarView timer : this.timers) {
            timer.setListener(this);
        }
    }

    private void setItemsValue() {
        for (int i = 0; i < 7; i++) {
            this.timers[i].setTime(this.mAlarms.get(i));
        }
    }

    private void setView(View view) {
        if (!this.mIsStandalone) {
            view.setBackground((Drawable) null);
        } else {
            this.closeButton.setVisibility(8);
        }
    }

    private void setupDestinationButton() {
        if (this.mDaysSeparated) {
            this.separatedBtn.setEnabled(false);
            this.groupedBtn.setEnabled(true);
            return;
        }
        this.separatedBtn.setEnabled(true);
        this.groupedBtn.setEnabled(false);
    }

    @OnClick({2131755166})
    public void onSeparatedClick() {
        this.mDaysSeparated = true;
        AccountManager.getInstance().setBiorhythmDaysSeparated(this.mDaysSeparated);
        setupDestinationButton();
        setItemsNumber();
        setItemsValue();
        if (!this.mDaysSeparated) {
            for (int i = 1; i < 5; i++) {
                this.mAlarms.set(i, this.mAlarms.get(0));
            }
            this.mAlarms.set(6, this.mAlarms.get(5));
        }
        saveData();
    }

    @OnClick({2131755165})
    public void onGroupedClick() {
        this.mDaysSeparated = false;
        AccountManager.getInstance().setBiorhythmDaysSeparated(this.mDaysSeparated);
        setupDestinationButton();
        setItemsNumber();
        setItemsValue();
        if (!this.mDaysSeparated) {
            for (int i = 1; i < 5; i++) {
                this.mAlarms.set(i, this.mAlarms.get(0));
            }
            this.mAlarms.set(6, this.mAlarms.get(5));
        }
        saveData();
    }

    private void setItemsNumber() {
        if (this.mDaysSeparated) {
            String[] weekdays = DateFormatSymbols.getInstance().getWeekdays();
            this.timers[0].setDay(weekdays[2].toUpperCase());
            this.timers[1].setDay(weekdays[3].toUpperCase());
            this.timers[2].setDay(weekdays[4].toUpperCase());
            this.timers[3].setDay(weekdays[5].toUpperCase());
            this.timers[4].setDay(weekdays[6].toUpperCase());
            this.timers[5].setDay(weekdays[7].toUpperCase());
            this.timers[6].setDay(weekdays[1].toUpperCase());
            setViewVisible(this.timers[1]);
            setViewVisible(this.timers[2]);
            setViewVisible(this.timers[3]);
            setViewVisible(this.timers[4]);
            setViewVisible(this.timers[6]);
            checkTimersFont(weekdays);
            return;
        }
        this.timers[0].setDay(getString(R.string.workdays));
        this.timers[5].setDay(getString(R.string.weekend));
        setViewGone(this.timers[1]);
        setViewGone(this.timers[2]);
        setViewGone(this.timers[3]);
        setViewGone(this.timers[4]);
        setViewGone(this.timers[6]);
        checkTimersFont((String[]) null);
    }

    private void checkTimersFont(String[] weekdays) {
        int i = 0;
        if (weekdays != null) {
            for (String weekday : weekdays) {
                if (!weekday.matches("[A-Za-z]+")) {
                    TimeDisplayBarView[] timeDisplayBarViewArr = this.timers;
                    int length = timeDisplayBarViewArr.length;
                    while (i < length) {
                        timeDisplayBarViewArr[i].setDayFont(Typeface.DEFAULT);
                        i++;
                    }
                    return;
                }
            }
        }
        TimeDisplayBarView[] timeDisplayBarViewArr2 = this.timers;
        int length2 = timeDisplayBarViewArr2.length;
        while (i < length2) {
            timeDisplayBarViewArr2[i].setDayFont(FontUtils.getFont(getActivity(), 2));
            i++;
        }
    }

    private void setViewVisible(View view) {
        view.setVisibility(0);
    }

    private void setViewGone(View view) {
        view.setVisibility(8);
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void saveData() {
        AccountManager.getInstance().setBiorhythmAlarms(this.mAlarms);
    }

    public void onTimeChanged(TimeDisplayBarView view, int hourOfDay, int minutes) {
        if (this.mDaysSeparated) {
            for (int i = 0; i < 7; i++) {
                if (view.equals(this.timers[i])) {
                    this.mAlarms.set(i, Integer.valueOf(((hourOfDay * 60) + minutes) * 60));
                }
            }
        } else if (view.equals(this.timers[0])) {
            for (int i2 = 0; i2 < 5; i2++) {
                this.mAlarms.set(i2, Integer.valueOf(((hourOfDay * 60) + minutes) * 60));
            }
        } else if (view.equals(this.timers[5])) {
            for (int i3 = 5; i3 < 7; i3++) {
                this.mAlarms.set(i3, Integer.valueOf(((hourOfDay * 60) + minutes) * 60));
            }
        }
        setItemsValue();
        saveData();
    }
}
