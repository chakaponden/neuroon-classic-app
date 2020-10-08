package com.inteliclinic.neuroon.fragments.first_time;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;
import com.inteliclinic.neuroon.views.LightTextView;
import com.inteliclinic.neuroon.views.ThinButton;

public class DateOfBirthFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final DateOfBirthFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.datePicker = (RecyclerView) finder.findRequiredView(source, R.id.date_picker, "field 'datePicker'");
        target.monthPicker = (RecyclerView) finder.findRequiredView(source, R.id.month_picker, "field 'monthPicker'");
        target.yearPicker = (RecyclerView) finder.findRequiredView(source, R.id.year_picker, "field 'yearPicker'");
        target.indicator = (ImageView) finder.findRequiredView(source, R.id.indicator, "field 'indicator'");
        target.datePickerContainer = (LinearLayout) finder.findRequiredView(source, R.id.date_picker_container, "field 'datePickerContainer'");
        target.calendar = (ImageView) finder.findRequiredView(source, R.id.calendar, "field 'calendar'");
        target.birthDate = (LightTextView) finder.findRequiredView(source, R.id.birth_date, "field 'birthDate'");
        View view = finder.findRequiredView(source, R.id.next, "field 'next' and method 'onNextClick'");
        target.next = (ThinButton) view;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onNextClick();
            }
        });
        target.stepIndicator = (LinearLayout) finder.findRequiredView(source, R.id.step_indicator, "field 'stepIndicator'");
    }

    public static void reset(DateOfBirthFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.datePicker = null;
        target.monthPicker = null;
        target.yearPicker = null;
        target.indicator = null;
        target.datePickerContainer = null;
        target.calendar = null;
        target.birthDate = null;
        target.next = null;
        target.stepIndicator = null;
    }
}
