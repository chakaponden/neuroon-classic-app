package com.inteliclinic.neuroon.fragments;

import android.view.View;
import android.widget.ImageView;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.BottomToolbar;

public class NapDetailsFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final NapDetailsFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.lightboostIcon = (ImageView) finder.findRequiredView(source, R.id.lightboost_icon, "field 'lightboostIcon'");
        target.mBottomToolbar = (BottomToolbar) finder.findRequiredView(source, R.id.bottom_toolbar, "field 'mBottomToolbar'");
        View view = finder.findRequiredView(source, R.id.start_lightboost_button, "field 'startLightboostButton' and method 'onStartTherapyClick'");
        target.startLightboostButton = (ImageView) view;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onStartTherapyClick();
            }
        });
    }

    public static void reset(NapDetailsFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.lightboostIcon = null;
        target.mBottomToolbar = null;
        target.startLightboostButton = null;
    }
}
