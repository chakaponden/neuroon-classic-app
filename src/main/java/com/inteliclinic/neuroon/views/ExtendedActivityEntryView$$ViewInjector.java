package com.inteliclinic.neuroon.views;

import android.widget.ImageView;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;

public class ExtendedActivityEntryView$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, ExtendedActivityEntryView target, Object source) {
        ActivityEntryView$$ViewInjector.inject(finder, target, source);
        target.eventExtendedBar = (ImageView) finder.findRequiredView(source, R.id.event_extended_bar, "field 'eventExtendedBar'");
        target.eventExtendedIcon = (ImageView) finder.findRequiredView(source, R.id.event_extended_icon, "field 'eventExtendedIcon'");
        target.eventExtendedTitle = (LightTextView) finder.findRequiredView(source, R.id.event_extended_title, "field 'eventExtendedTitle'");
        target.eventExtendedTime = (ThinTextView) finder.findRequiredView(source, R.id.event_extended_time, "field 'eventExtendedTime'");
    }

    public static void reset(ExtendedActivityEntryView target) {
        ActivityEntryView$$ViewInjector.reset(target);
        target.eventExtendedBar = null;
        target.eventExtendedIcon = null;
        target.eventExtendedTitle = null;
        target.eventExtendedTime = null;
    }
}
