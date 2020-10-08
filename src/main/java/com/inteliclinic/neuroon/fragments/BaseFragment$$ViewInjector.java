package com.inteliclinic.neuroon.fragments;

import android.view.View;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;

public class BaseFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final BaseFragment target, Object source) {
        View view = finder.findOptionalView(source, R.id.close_button);
        if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View p0) {
                    target.onCloseButtonClick();
                }
            });
        }
        View view2 = finder.findOptionalView(source, R.id.back_button);
        if (view2 != null) {
            view2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View p0) {
                    target.onBackButtonClick();
                }
            });
        }
    }

    public static void reset(BaseFragment target) {
    }
}
