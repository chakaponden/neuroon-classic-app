package com.inteliclinic.neuroon.fragments.jetlag;

import android.view.View;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;
import com.inteliclinic.neuroon.views.BoldTextView;
import com.inteliclinic.neuroon.views.ThinTextView;
import com.inteliclinic.neuroon.views.calendar.PartialMonthView;

public class JetLagTherapySummaryFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final JetLagTherapySummaryFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.therapyLength = (ThinTextView) finder.findRequiredView(source, R.id.therapy_length, "field 'therapyLength'");
        target.therapyStart = (ThinTextView) finder.findRequiredView(source, R.id.therapy_start, "field 'therapyStart'");
        target.therapyEnd = (ThinTextView) finder.findRequiredView(source, R.id.therapy_end, "field 'therapyEnd'");
        target.timezones = (ThinTextView) finder.findRequiredView(source, R.id.timezones, "field 'timezones'");
        target.firstMonthName = (BoldTextView) finder.findRequiredView(source, R.id.first_month_name, "field 'firstMonthName'");
        target.firstMonth = (PartialMonthView) finder.findRequiredView(source, R.id.first_month, "field 'firstMonth'");
        target.secondMonthName = (BoldTextView) finder.findRequiredView(source, R.id.second_month_name, "field 'secondMonthName'");
        target.secondDaysHeader = (LinearLayout) finder.findRequiredView(source, R.id.second_days_header, "field 'secondDaysHeader'");
        target.secondMonth = (PartialMonthView) finder.findRequiredView(source, R.id.second_month, "field 'secondMonth'");
        finder.findRequiredView(source, R.id.button, "method 'onNextClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onNextClick();
            }
        });
    }

    public static void reset(JetLagTherapySummaryFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.therapyLength = null;
        target.therapyStart = null;
        target.therapyEnd = null;
        target.timezones = null;
        target.firstMonthName = null;
        target.firstMonth = null;
        target.secondMonthName = null;
        target.secondDaysHeader = null;
        target.secondMonth = null;
    }
}
