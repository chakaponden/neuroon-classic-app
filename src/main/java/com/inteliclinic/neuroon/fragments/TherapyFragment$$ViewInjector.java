package com.inteliclinic.neuroon.fragments;

import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.LightTextView;
import com.inteliclinic.neuroon.views.SwipeToCloseView;
import com.inteliclinic.neuroon.views.TherapyProgressView;
import com.inteliclinic.neuroon.views.ThinTextView;

public class TherapyFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, TherapyFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.therapyBack = (ViewGroup) finder.findRequiredView(source, R.id.therapy_back, "field 'therapyBack'");
        target.slider = (SwipeToCloseView) finder.findRequiredView(source, R.id.slider, "field 'slider'");
        target.therapyTitle = (ThinTextView) finder.findRequiredView(source, R.id.therapy_title, "field 'therapyTitle'");
        target.progressView = (TherapyProgressView) finder.findRequiredView(source, R.id.progress_view, "field 'progressView'");
        target.timer = (LightTextView) finder.findRequiredView(source, R.id.timer, "field 'timer'");
        target.therapyEnds = (LightTextView) finder.findRequiredView(source, R.id.therapy_ends, "field 'therapyEnds'");
        target.endsTime = (LightTextView) finder.findRequiredView(source, R.id.ends_time, "field 'endsTime'");
    }

    public static void reset(TherapyFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.therapyBack = null;
        target.slider = null;
        target.therapyTitle = null;
        target.progressView = null;
        target.timer = null;
        target.therapyEnds = null;
        target.endsTime = null;
    }
}
