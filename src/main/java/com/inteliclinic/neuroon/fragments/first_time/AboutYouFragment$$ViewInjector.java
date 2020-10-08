package com.inteliclinic.neuroon.fragments.first_time;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;
import com.inteliclinic.neuroon.views.ThinButton;

public class AboutYouFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final AboutYouFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.recyclerView = (RecyclerView) finder.findRequiredView(source, R.id.recycler_view, "field 'recyclerView'");
        View view = finder.findRequiredView(source, R.id.next, "field 'next' and method 'onNextClick'");
        target.next = (ThinButton) view;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onNextClick();
            }
        });
        target.stepIndicator = (LinearLayout) finder.findRequiredView(source, R.id.step_indicator, "field 'stepIndicator'");
    }

    public static void reset(AboutYouFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.recyclerView = null;
        target.next = null;
        target.stepIndicator = null;
    }
}
