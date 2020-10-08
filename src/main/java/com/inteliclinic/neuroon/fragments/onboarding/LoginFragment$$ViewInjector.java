package com.inteliclinic.neuroon.fragments.onboarding;

import android.view.View;
import android.widget.Button;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;
import com.inteliclinic.neuroon.views.LightEditText;
import com.inteliclinic.neuroon.views.LightTextView;
import com.inteliclinic.neuroon.views.ThinTextView;

public class LoginFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final LoginFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.mUsername = (LightEditText) finder.findRequiredView(source, R.id.username, "field 'mUsername'");
        target.mPassword = (LightEditText) finder.findRequiredView(source, R.id.password, "field 'mPassword'");
        View view = finder.findRequiredView(source, R.id.log_in, "field 'mLogIn' and method 'onLogInClick'");
        target.mLogIn = (Button) view;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onLogInClick();
            }
        });
        View view2 = finder.findRequiredView(source, R.id.password_reset, "field 'mPasswordReset' and method 'onResetPasswordClick'");
        target.mPasswordReset = (LightTextView) view2;
        view2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onResetPasswordClick();
            }
        });
        target.mError = (ThinTextView) finder.findRequiredView(source, R.id.error, "field 'mError'");
        finder.findRequiredView(source, R.id.back_button, "method 'onBackButtonClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onBackButtonClick();
            }
        });
    }

    public static void reset(LoginFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.mUsername = null;
        target.mPassword = null;
        target.mLogIn = null;
        target.mPasswordReset = null;
        target.mError = null;
    }
}
