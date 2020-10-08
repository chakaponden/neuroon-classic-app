package com.inteliclinic.neuroon.fragments.jetlag;

import android.view.View;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;
import com.inteliclinic.neuroon.views.ThinTextView;

public class BookingReferenceNumberFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final BookingReferenceNumberFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.bookingDescription = (ThinTextView) finder.findRequiredView(source, R.id.booking_description, "field 'bookingDescription'");
        finder.findRequiredView(source, R.id.close, "method 'onCloseButtonClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onCloseButtonClick();
            }
        });
    }

    public static void reset(BookingReferenceNumberFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.bookingDescription = null;
    }
}
