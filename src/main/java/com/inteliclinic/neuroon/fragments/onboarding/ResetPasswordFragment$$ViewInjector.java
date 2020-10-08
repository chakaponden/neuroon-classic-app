package com.inteliclinic.neuroon.fragments.onboarding;

import android.view.View;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;
import com.inteliclinic.neuroon.views.LightEditText;

public class ResetPasswordFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final ResetPasswordFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.email = (LightEditText) finder.findRequiredView(source, R.id.email, "field 'email'");
        finder.findRequiredView(source, R.id.password_reset, "method 'onRequestResetClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onRequestResetClick();
            }
        });
    }

    public static void reset(ResetPasswordFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.email = null;
    }
}
