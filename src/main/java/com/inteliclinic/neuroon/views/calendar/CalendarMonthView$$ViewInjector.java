package com.inteliclinic.neuroon.views.calendar;

import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.BoldTextView;

public class CalendarMonthView$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, CalendarMonthView target, Object source) {
        target.monthName = (BoldTextView) finder.findRequiredView(source, R.id.month_name, "field 'monthName'");
        target.mMonthView = (MonthView) finder.findRequiredView(source, R.id.month, "field 'mMonthView'");
    }

    public static void reset(CalendarMonthView target) {
        target.monthName = null;
        target.mMonthView = null;
    }
}
