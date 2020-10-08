package com.inteliclinic.neuroon.views.pickers;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.SwitchView;

public class PickerView$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final PickerView target, Object source) {
        target.container = (LinearLayout) finder.findRequiredView(source, R.id.container, "field 'container'");
        View view = finder.findRequiredView(source, R.id.done, "field 'done' and method 'doneClick'");
        target.done = (ImageView) view;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.doneClick();
            }
        });
        target.customSwitch = (SwitchView) finder.findRequiredView(source, R.id.custom_switch, "field 'customSwitch'");
        target.indicator = (ImageView) finder.findRequiredView(source, R.id.indicator, "field 'indicator'");
        finder.findRequiredView(source, R.id.bottom, "method 'hide'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.hide();
            }
        });
    }

    public static void reset(PickerView target) {
        target.container = null;
        target.done = null;
        target.customSwitch = null;
        target.indicator = null;
    }
}
