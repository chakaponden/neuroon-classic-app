package com.inteliclinic.neuroon.views.calendar;

import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.BoldTextView;

public class SleepDatesCalendarMonthView$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, SleepDatesCalendarMonthView target, Object source) {
        target.monthName = (BoldTextView) finder.findRequiredView(source, R.id.month_name, "field 'monthName'");
        target.mMonthView = (SleepDatesMonthView) finder.findRequiredView(source, R.id.month, "field 'mMonthView'");
    }

    public static void reset(SleepDatesCalendarMonthView target) {
        target.monthName = null;
        target.mMonthView = null;
    }
}
