package com.inteliclinic.neuroon.fragments.jetlag;

import android.view.View;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;
import com.inteliclinic.neuroon.views.BottomToolbar;

public class StartJetLagTherapyFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final StartJetLagTherapyFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.mBottomToolbar = (BottomToolbar) finder.findRequiredView(source, R.id.bottom_toolbar, "field 'mBottomToolbar'");
        finder.findRequiredView(source, R.id.menu, "method 'onMenuClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onMenuClick();
            }
        });
        finder.findRequiredView(source, R.id.help_icon, "method 'onHelpClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onHelpClick();
            }
        });
        finder.findRequiredView(source, R.id.start_button, "method 'onStartClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onStartClick();
            }
        });
    }

    public static void reset(StartJetLagTherapyFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.mBottomToolbar = null;
    }
}
