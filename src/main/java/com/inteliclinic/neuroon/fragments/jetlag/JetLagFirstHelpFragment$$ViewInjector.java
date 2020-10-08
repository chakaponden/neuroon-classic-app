package com.inteliclinic.neuroon.fragments.jetlag;

import android.view.View;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;

public class JetLagFirstHelpFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final JetLagFirstHelpFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        finder.findRequiredView(source, R.id.close_hint, "method 'onCloseFragment'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onCloseFragment();
            }
        });
    }

    public static void reset(JetLagFirstHelpFragment target) {
        BaseFragment$$ViewInjector.reset(target);
    }
}
