package com.inteliclinic.neuroon.fragments.mask;

import android.view.View;
import android.widget.FrameLayout;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;

public class TurnOnBluetoothFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final TurnOnBluetoothFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        View view = finder.findRequiredView(source, R.id.cancel, "field 'cancel' and method 'onCancelClick'");
        target.cancel = (FrameLayout) view;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onCancelClick();
            }
        });
        View view2 = finder.findRequiredView(source, R.id.try_again, "field 'tryAgain' and method 'onEnableClick'");
        target.tryAgain = (FrameLayout) view2;
        view2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onEnableClick();
            }
        });
    }

    public static void reset(TurnOnBluetoothFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.cancel = null;
        target.tryAgain = null;
    }
}
