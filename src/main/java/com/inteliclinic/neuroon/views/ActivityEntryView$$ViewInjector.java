package com.inteliclinic.neuroon.views;

import android.widget.ImageView;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;

public class ActivityEntryView$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, ActivityEntryView target, Object source) {
        target.eventBar = (ImageView) finder.findRequiredView(source, R.id.event_bar, "field 'eventBar'");
        target.eventIcon = (ImageView) finder.findRequiredView(source, R.id.event_icon, "field 'eventIcon'");
        target.eventTitle = (LightTextView) finder.findRequiredView(source, R.id.event_title, "field 'eventTitle'");
        target.mEventTime = (ThinTextView) finder.findRequiredView(source, R.id.event_time, "field 'mEventTime'");
    }

    public static void reset(ActivityEntryView target) {
        target.eventBar = null;
        target.eventIcon = null;
        target.eventTitle = null;
        target.mEventTime = null;
    }
}
