package com.inteliclinic.neuroon.fragments.jetlag;

import android.view.View;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;
import com.inteliclinic.neuroon.views.LightAutoCompleteEditText;
import com.inteliclinic.neuroon.views.LightEditText;
import com.inteliclinic.neuroon.views.ThinButton;
import com.inteliclinic.neuroon.views.ThinTextView;

public class PlanJourneyFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final PlanJourneyFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.mFrom = (LightAutoCompleteEditText) finder.findRequiredView(source, R.id.from, "field 'mFrom'");
        target.mTo = (LightAutoCompleteEditText) finder.findRequiredView(source, R.id.to, "field 'mTo'");
        View view = finder.findRequiredView(source, R.id.date, "field 'mDate' and method 'onCalendarBtnClick'");
        target.mDate = (LightEditText) view;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onCalendarBtnClick();
            }
        });
        target.mDestinationSection = (LinearLayout) finder.findRequiredView(source, R.id.destination_section, "field 'mDestinationSection'");
        target.mReservationNumber = (LightAutoCompleteEditText) finder.findRequiredView(source, R.id.reservation_number, "field 'mReservationNumber'");
        target.mReservationName = (LightEditText) finder.findRequiredView(source, R.id.reservation_name, "field 'mReservationName'");
        target.mReservationSection = (LinearLayout) finder.findRequiredView(source, R.id.reservation_section, "field 'mReservationSection'");
        View view2 = finder.findRequiredView(source, R.id.reservation_btn, "field 'mReservationBtn' and method 'onReservationBtnClick'");
        target.mReservationBtn = (ThinTextView) view2;
        view2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onReservationBtnClick();
            }
        });
        View view3 = finder.findRequiredView(source, R.id.destination_btn, "field 'mDestinationBtn' and method 'onDestinationBtnClick'");
        target.mDestinationBtn = (ThinTextView) view3;
        view3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onDestinationBtnClick();
            }
        });
        View view4 = finder.findRequiredView(source, R.id.button, "field 'mButton' and method 'onNextFinishClick'");
        target.mButton = (ThinButton) view4;
        view4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onNextFinishClick();
            }
        });
        finder.findRequiredView(source, R.id.help_icon, "method 'onBookingReferenceCodeHelp'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onBookingReferenceCodeHelp();
            }
        });
    }

    public static void reset(PlanJourneyFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.mFrom = null;
        target.mTo = null;
        target.mDate = null;
        target.mDestinationSection = null;
        target.mReservationNumber = null;
        target.mReservationName = null;
        target.mReservationSection = null;
        target.mReservationBtn = null;
        target.mDestinationBtn = null;
        target.mButton = null;
    }
}
