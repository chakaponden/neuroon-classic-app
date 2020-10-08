package com.inteliclinic.neuroon.fragments.onboarding;

import android.view.View;
import android.widget.EditText;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;
import com.inteliclinic.neuroon.views.LightEditText;
import com.inteliclinic.neuroon.views.ThinCheckbox;
import com.inteliclinic.neuroon.views.ThinTextView;

public class RegisterFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final RegisterFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.username = (EditText) finder.findRequiredView(source, R.id.username, "field 'username'");
        target.password = (EditText) finder.findRequiredView(source, R.id.password, "field 'password'");
        target.repeatPassword = (LightEditText) finder.findRequiredView(source, R.id.repeat_password, "field 'repeatPassword'");
        target.acceptTerms = (ThinCheckbox) finder.findRequiredView(source, R.id.accept_terms, "field 'acceptTerms'");
        target.mError = (ThinTextView) finder.findRequiredView(source, R.id.error, "field 'mError'");
        target.acceptTermsText = (ThinTextView) finder.findRequiredView(source, R.id.accept_terms_text, "field 'acceptTermsText'");
        finder.findRequiredView(source, R.id.back_button, "method 'onBackButtonClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onBackButtonClick();
            }
        });
        finder.findRequiredView(source, R.id.sign_up, "method 'signUp'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.signUp();
            }
        });
    }

    public static void reset(RegisterFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.username = null;
        target.password = null;
        target.repeatPassword = null;
        target.acceptTerms = null;
        target.mError = null;
        target.acceptTermsText = null;
    }
}
