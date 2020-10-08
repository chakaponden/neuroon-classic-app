package com.inteliclinic.neuroon.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import butterknife.InjectView;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.models.data.Event;

public class ExtendedActivityEntryView extends ActivityEntryView {
    @InjectView(2131755602)
    ImageView eventExtendedBar;
    @InjectView(2131755603)
    ImageView eventExtendedIcon;
    @InjectView(2131755605)
    ThinTextView eventExtendedTime;
    @InjectView(2131755604)
    LightTextView eventExtendedTitle;

    public ExtendedActivityEntryView(Context context) {
        this(context, (AttributeSet) null);
    }

    public ExtendedActivityEntryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExtendedActivityEntryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public ExtendedActivityEntryView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /* access modifiers changed from: protected */
    public int getViewId() {
        return R.layout.view_extended_activity_entry;
    }

    public void setExtendedEvent(Event event) {
        this.eventBar.setImageResource(R.drawable.belka_sen_dluga);
        this.eventIcon.setImageResource(R.drawable.sleep_feed);
        this.eventTitle.setText(R.string.sleep);
        setEventTime(this.mEventTime, event);
        setEventBar(this.eventExtendedBar, event.getType());
        setEventIcon(this.eventExtendedIcon, event.getType());
        setEventTitle(this.eventExtendedTitle, event.getType());
        setExtendedEventTime(event);
    }

    private void setExtendedEventTime(Event event) {
        setEventTime(this.eventExtendedTime, event);
    }
}
