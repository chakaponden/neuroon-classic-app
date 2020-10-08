package com.inteliclinic.neuroon.fragments;

import android.view.View;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.LightTextView;

public class AboutAppFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final AboutAppFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.mVersion = (LightTextView) finder.findRequiredView(source, R.id.version, "field 'mVersion'");
        finder.findRequiredView(source, R.id.licenses, "method 'onLicensesClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onLicensesClick();
            }
        });
    }

    public static void reset(AboutAppFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.mVersion = null;
    }
}
