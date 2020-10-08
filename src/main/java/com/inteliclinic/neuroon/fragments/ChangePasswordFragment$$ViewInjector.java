package com.inteliclinic.neuroon.fragments;

import android.view.View;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.LightEditText;

public class ChangePasswordFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final ChangePasswordFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.mOldPassword = (LightEditText) finder.findRequiredView(source, R.id.old_password, "field 'mOldPassword'");
        target.mPassword = (LightEditText) finder.findRequiredView(source, R.id.password, "field 'mPassword'");
        target.mRepeatPassword = (LightEditText) finder.findRequiredView(source, R.id.repeat_password, "field 'mRepeatPassword'");
        finder.findRequiredView(source, R.id.change, "method 'onChangePasswordClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onChangePasswordClick();
            }
        });
    }

    public static void reset(ChangePasswordFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.mOldPassword = null;
        target.mPassword = null;
        target.mRepeatPassword = null;
    }
}
