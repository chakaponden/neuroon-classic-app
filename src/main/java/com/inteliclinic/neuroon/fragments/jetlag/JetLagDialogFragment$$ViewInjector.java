package com.inteliclinic.neuroon.fragments.jetlag;

import android.view.View;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;
import com.inteliclinic.neuroon.views.ThinCheckbox;

public class JetLagDialogFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final JetLagDialogFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.dontShow = (ThinCheckbox) finder.findRequiredView(source, R.id.dont_show, "field 'dontShow'");
        finder.findRequiredView(source, R.id.button, "method 'onClickClose'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onClickClose();
            }
        });
    }

    public static void reset(JetLagDialogFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.dontShow = null;
    }
}
