package com.inteliclinic.neuroon.fragments;

import android.view.View;
import android.widget.ImageView;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.LightTextView;
import com.inteliclinic.neuroon.views.TimeDisplayBarView;

public class BiorhythmFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final BiorhythmFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.closeButton = (ImageView) finder.findRequiredView(source, R.id.close_button, "field 'closeButton'");
        View view = finder.findRequiredView(source, R.id.grouped_btn, "field 'groupedBtn' and method 'onGroupedClick'");
        target.groupedBtn = (LightTextView) view;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onGroupedClick();
            }
        });
        View view2 = finder.findRequiredView(source, R.id.separated_btn, "field 'separatedBtn' and method 'onSeparatedClick'");
        target.separatedBtn = (LightTextView) view2;
        view2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onSeparatedClick();
            }
        });
        target.timers = (TimeDisplayBarView[]) ButterKnife.Finder.arrayOf((TimeDisplayBarView) finder.findRequiredView(source, R.id.one, "timers"), (TimeDisplayBarView) finder.findRequiredView(source, R.id.two, "timers"), (TimeDisplayBarView) finder.findRequiredView(source, R.id.three, "timers"), (TimeDisplayBarView) finder.findRequiredView(source, R.id.four, "timers"), (TimeDisplayBarView) finder.findRequiredView(source, R.id.five, "timers"), (TimeDisplayBarView) finder.findRequiredView(source, R.id.six, "timers"), (TimeDisplayBarView) finder.findRequiredView(source, R.id.seven, "timers"));
    }

    public static void reset(BiorhythmFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.closeButton = null;
        target.groupedBtn = null;
        target.separatedBtn = null;
        target.timers = null;
    }
}
