package com.inteliclinic.neuroon.fragments.mask;

import android.view.View;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;

public class MaskNotFoundFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final MaskNotFoundFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        finder.findRequiredView(source, R.id.cancel, "method 'onCancelClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onCancelClick();
            }
        });
        finder.findRequiredView(source, R.id.try_again, "method 'onTryAgainClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onTryAgainClick();
            }
        });
    }

    public static void reset(MaskNotFoundFragment target) {
        BaseFragment$$ViewInjector.reset(target);
    }
}
