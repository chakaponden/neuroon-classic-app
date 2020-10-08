package com.inteliclinic.neuroon.fragments.mask;

import android.view.View;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;
import com.inteliclinic.neuroon.views.LightTextView;
import com.inteliclinic.neuroon.views.ThinButton;

public class TurnOnMaskFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final TurnOnMaskFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.text2 = (LightTextView) finder.findRequiredView(source, R.id.text2, "field 'text2'");
        View view = finder.findRequiredView(source, R.id.start_button, "field 'startButton' and method 'onCancelClick'");
        target.startButton = (ThinButton) view;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onCancelClick();
            }
        });
        target.title = (LightTextView) finder.findRequiredView(source, R.id.title, "field 'title'");
    }

    public static void reset(TurnOnMaskFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.text2 = null;
        target.startButton = null;
        target.title = null;
    }
}
