package com.inteliclinic.neuroon.views;

import android.widget.ImageView;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;

public class SwipeToCloseView$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, SwipeToCloseView target, Object source) {
        target.slider = (ImageView) finder.findRequiredView(source, R.id.swipe_extra_slider, "field 'slider'");
        target.slideToClose = (LightTextView) finder.findRequiredView(source, R.id.slide_to_close, "field 'slideToClose'");
    }

    public static void reset(SwipeToCloseView target) {
        target.slider = null;
        target.slideToClose = null;
    }
}
