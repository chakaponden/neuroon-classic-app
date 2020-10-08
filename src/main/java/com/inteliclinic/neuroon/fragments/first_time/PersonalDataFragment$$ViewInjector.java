package com.inteliclinic.neuroon.fragments.first_time;

import android.view.View;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;

public class PersonalDataFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final PersonalDataFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        finder.findRequiredView(source, R.id.start, "method 'onStartClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onStartClick();
            }
        });
    }

    public static void reset(PersonalDataFragment target) {
        BaseFragment$$ViewInjector.reset(target);
    }
}
