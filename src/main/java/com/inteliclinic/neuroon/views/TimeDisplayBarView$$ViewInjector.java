package com.inteliclinic.neuroon.views;

import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;

public class TimeDisplayBarView$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, TimeDisplayBarView target, Object source) {
        target.time = (ThinTextView) finder.findRequiredView(source, R.id.time, "field 'time'");
        target.day = (ThinTextView) finder.findRequiredView(source, R.id.day, "field 'day'");
    }

    public static void reset(TimeDisplayBarView target) {
        target.time = null;
        target.day = null;
    }
}
