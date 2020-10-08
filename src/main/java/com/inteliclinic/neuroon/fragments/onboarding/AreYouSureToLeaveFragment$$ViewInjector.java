package com.inteliclinic.neuroon.fragments.onboarding;

import android.view.View;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;

public class AreYouSureToLeaveFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final AreYouSureToLeaveFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        finder.findRequiredView(source, R.id.leave, "method 'onLeaveButtonClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onLeaveButtonClick();
            }
        });
        finder.findRequiredView(source, R.id.continue_btn, "method 'onContinueButtonClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onContinueButtonClick();
            }
        });
    }

    public static void reset(AreYouSureToLeaveFragment target) {
        BaseFragment$$ViewInjector.reset(target);
    }
}
