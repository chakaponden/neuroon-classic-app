package com.inteliclinic.neuroon.views.calendar;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.ThinTextView;

public class CalendarPickerView$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final CalendarPickerView target, Object source) {
        target.title = (ThinTextView) finder.findRequiredView(source, R.id.title, "field 'title'");
        target.recyclerView = (RecyclerView) finder.findRequiredView(source, R.id.recycler_view, "field 'recyclerView'");
        target.shadow = (ImageView) finder.findRequiredView(source, R.id.shadow, "field 'shadow'");
        View view = finder.findRequiredView(source, R.id.accept, "field 'accept' and method 'onAcceptButtonClick'");
        target.accept = (FrameLayout) view;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onAcceptButtonClick();
            }
        });
        target.buttonImage = (ImageView) finder.findRequiredView(source, R.id.button_image, "field 'buttonImage'");
        finder.findRequiredView(source, R.id.close_button, "method 'onCloseButtonClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onCloseButtonClick();
            }
        });
    }

    public static void reset(CalendarPickerView target) {
        target.title = null;
        target.recyclerView = null;
        target.shadow = null;
        target.accept = null;
        target.buttonImage = null;
    }
}
