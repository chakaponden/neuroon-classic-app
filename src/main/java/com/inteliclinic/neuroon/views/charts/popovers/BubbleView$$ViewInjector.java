package com.inteliclinic.neuroon.views.charts.popovers;

import android.widget.FrameLayout;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.BaseTextView;

public class BubbleView$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, BubbleView target, Object source) {
        target.hightlight = (FrameLayout) finder.findRequiredView(source, R.id.highlight, "field 'hightlight'");
        target.mTitle = (BaseTextView) finder.findRequiredView(source, R.id.title, "field 'mTitle'");
        target.time = (BaseTextView) finder.findRequiredView(source, R.id.time, "field 'time'");
        target.mContainer = (LinearLayout) finder.findRequiredView(source, R.id.container, "field 'mContainer'");
    }

    public static void reset(BubbleView target) {
        target.hightlight = null;
        target.mTitle = null;
        target.time = null;
        target.mContainer = null;
    }
}
