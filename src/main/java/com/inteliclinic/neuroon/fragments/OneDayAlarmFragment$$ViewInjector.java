package com.inteliclinic.neuroon.fragments;

import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.SwitchView;
import com.inteliclinic.neuroon.views.TimeDisplayBarView;

public class OneDayAlarmFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, OneDayAlarmFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.one = (TimeDisplayBarView) finder.findRequiredView(source, R.id.one, "field 'one'");
        target.onSwitch = (SwitchView) finder.findRequiredView(source, R.id.on_switch, "field 'onSwitch'");
    }

    public static void reset(OneDayAlarmFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.one = null;
        target.onSwitch = null;
    }
}
