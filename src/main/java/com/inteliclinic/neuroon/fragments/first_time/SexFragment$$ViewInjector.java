package com.inteliclinic.neuroon.fragments.first_time;

import android.view.View;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;
import com.inteliclinic.neuroon.views.ThinButton;

public class SexFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final SexFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        View view = finder.findRequiredView(source, R.id.men, "field 'men' and method 'onMenClick'");
        target.men = (LinearLayout) view;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onMenClick();
            }
        });
        View view2 = finder.findRequiredView(source, R.id.women, "field 'women' and method 'onWomenClick'");
        target.women = (LinearLayout) view2;
        view2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onWomenClick();
            }
        });
        View view3 = finder.findRequiredView(source, R.id.next, "field 'next' and method 'onNextClick'");
        target.next = (ThinButton) view3;
        view3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onNextClick();
            }
        });
        target.stepIndicator = (LinearLayout) finder.findRequiredView(source, R.id.step_indicator, "field 'stepIndicator'");
    }

    public static void reset(SexFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.men = null;
        target.women = null;
        target.next = null;
        target.stepIndicator = null;
    }
}
