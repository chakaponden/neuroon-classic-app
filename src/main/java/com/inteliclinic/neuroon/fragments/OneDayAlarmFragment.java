package com.inteliclinic.neuroon.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.android.gms.analytics.HitBuilders;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.managers.account.AccountManager;
import com.inteliclinic.neuroon.views.SwitchView;
import com.inteliclinic.neuroon.views.TimeDisplayBarView;

public class OneDayAlarmFragment extends BaseFragment {
    @InjectView(2131755405)
    SwitchView onSwitch;
    @InjectView(2131755167)
    TimeDisplayBarView one;

    public static OneDayAlarmFragment newInstance() {
        return new OneDayAlarmFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one_day_alarm, container, false);
        ButterKnife.inject((Object) this, view);
        this.onSwitch.setOn(AccountManager.getInstance().isOneDayAlarmTurned());
        this.onSwitch.setListener(new SwitchView.SwitchListener() {
            public void onSwitchStateChange(boolean state) {
                AccountManager.getInstance().setOneDayAlarmTurned(state);
            }
        });
        this.one.setTime(AccountManager.getInstance().getOneDayAlarmTimeAsTimestamp());
        this.one.setPickerTheme(2);
        this.one.setListener(new TimeDisplayBarView.OnTimeChangedListener() {
            public void onTimeChanged(TimeDisplayBarView onTimePickerListener, int hourOfDay, int minutes) {
                AccountManager.getInstance().setOneDayAlarmTime(hourOfDay, minutes);
            }
        });
        return view;
    }

    public void onResume() {
        super.onResume();
        getTracker().setScreenName("one_day_alarm");
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
