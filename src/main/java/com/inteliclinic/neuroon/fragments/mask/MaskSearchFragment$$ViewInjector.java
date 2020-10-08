package com.inteliclinic.neuroon.fragments.mask;

import android.view.View;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;

public class MaskSearchFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final MaskSearchFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        finder.findRequiredView(source, R.id.cancel, "method 'onCancelClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onCancelClick();
            }
        });
    }

    public static void reset(MaskSearchFragment target) {
        BaseFragment$$ViewInjector.reset(target);
    }
}
