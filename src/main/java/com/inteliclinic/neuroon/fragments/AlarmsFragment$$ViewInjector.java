package com.inteliclinic.neuroon.fragments;

import android.view.View;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.LightTextView;

public class AlarmsFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final AlarmsFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        View view = finder.findRequiredView(source, R.id.use_artificial_btn, "field 'mUseArtificialDawn' and method 'onUseArtificialClick'");
        target.mUseArtificialDawn = (LightTextView) view;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onUseArtificialClick();
            }
        });
        View view2 = finder.findRequiredView(source, R.id.dont_use_artificial_btn, "field 'mDontUseArtificialDawn' and method 'onDontUseArtificialClick'");
        target.mDontUseArtificialDawn = (LightTextView) view2;
        view2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onDontUseArtificialClick();
            }
        });
        View view3 = finder.findRequiredView(source, R.id.dimmed_btn, "field 'mDimmedDawn' and method 'onDimmedArtificialDawn'");
        target.mDimmedDawn = (LightTextView) view3;
        view3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onDimmedArtificialDawn();
            }
        });
        View view4 = finder.findRequiredView(source, R.id.normal_btn, "field 'mNormalDawn' and method 'onNormalArtificialDawn'");
        target.mNormalDawn = (LightTextView) view4;
        view4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onNormalArtificialDawn();
            }
        });
        View view5 = finder.findRequiredView(source, R.id.bright_btn, "field 'mBrightDawn' and method 'onBrightArtificialDawn'");
        target.mBrightDawn = (LightTextView) view5;
        view5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onBrightArtificialDawn();
            }
        });
        View view6 = finder.findRequiredView(source, R.id.automatic_sleep_on_btn, "field 'mAutoSleepOn' and method 'onAutoSleepStartOnClick'");
        target.mAutoSleepOn = (LightTextView) view6;
        view6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onAutoSleepStartOnClick();
            }
        });
        View view7 = finder.findRequiredView(source, R.id.automatic_sleep_off_btn, "field 'mAutoSleepOff' and method 'onAutoSleepStartOffClick'");
        target.mAutoSleepOff = (LightTextView) view7;
        view7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onAutoSleepStartOffClick();
            }
        });
        finder.findRequiredView(source, R.id.biorhythm, "method 'onBiorhythmClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onBiorhythmClick();
            }
        });
        finder.findRequiredView(source, R.id.one_day_alarm, "method 'onOneDayAlarmClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onOneDayAlarmClick();
            }
        });
    }

    public static void reset(AlarmsFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.mUseArtificialDawn = null;
        target.mDontUseArtificialDawn = null;
        target.mDimmedDawn = null;
        target.mNormalDawn = null;
        target.mBrightDawn = null;
        target.mAutoSleepOn = null;
        target.mAutoSleepOff = null;
    }
}
