package com.inteliclinic.neuroon.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.models.data.Event;
import java.text.DateFormat;

public class ActivityEntryView extends RelativeLayout {
    @InjectView(2131755582)
    ImageView eventBar;
    @InjectView(2131755303)
    ImageView eventIcon;
    @InjectView(2131755304)
    LightTextView eventTitle;
    @InjectView(2131755305)
    ThinTextView mEventTime;

    public ActivityEntryView(Context context) {
        this(context, (AttributeSet) null);
    }

    public ActivityEntryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActivityEntryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public ActivityEntryView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setEvent(Event event) {
        setEventBar(event);
        setEventIcon(event);
        setEventTitle(event);
        setEventTime(event);
    }

    private void init() {
        inflate(getContext(), getViewId(), this);
        ButterKnife.inject((View) this);
    }

    /* access modifiers changed from: protected */
    @LayoutRes
    public int getViewId() {
        return R.layout.view_activity_entry;
    }

    /* access modifiers changed from: protected */
    public void setEventBar(Event event) {
        setEventBar(this.eventBar, event.getType());
    }

    /* access modifiers changed from: protected */
    public void setEventBar(ImageView imageView, Event.EventType eventType) {
        switch (eventType) {
            case ETJetLagTreatment:
                imageView.setImageResource(R.drawable.belka_jetlag);
                return;
            case ETNapUltimate:
            case ETNapRem:
            case ETNapBody:
            case ETNapPower:
                imageView.setImageResource(R.drawable.belka_naps);
                return;
            case ETBLT:
                imageView.setImageResource(R.drawable.belka_boost);
                return;
            case ETSleep:
                imageView.setImageResource(R.drawable.belka_sen);
                return;
            case ETSeekLight:
                imageView.setImageResource(R.drawable.belka_seek_light);
                return;
            case ETSleepy:
                imageView.setImageResource(R.drawable.belka_feeling_sleepy);
                return;
            case ETAvoidLight:
                imageView.setImageResource(R.drawable.belka_avoid_light);
                return;
            case ETPaired:
                imageView.setImageResource(R.drawable.belka_paired);
                return;
            default:
                return;
        }
    }

    private void setEventIcon(Event event) {
        setEventIcon(this.eventIcon, event.getType());
    }

    /* access modifiers changed from: protected */
    public void setEventIcon(ImageView imageView, Event.EventType eventType) {
        switch (eventType) {
            case ETJetLagTreatment:
                imageView.setImageResource(R.drawable.jetlag_feed);
                return;
            case ETNapUltimate:
            case ETNapRem:
            case ETNapBody:
            case ETNapPower:
                imageView.setImageResource(R.drawable.naps_feed);
                return;
            case ETBLT:
                imageView.setImageResource(R.drawable.boost_feed);
                return;
            case ETSleep:
                imageView.setImageResource(R.drawable.sleep_feed);
                return;
            case ETSeekLight:
                imageView.setImageResource(R.drawable.seek_light_feed);
                return;
            case ETSleepy:
                imageView.setImageResource(R.drawable.feeling_sleepy_feed);
                return;
            case ETAvoidLight:
                imageView.setImageResource(R.drawable.avoid_light_feed);
                return;
            case ETPaired:
                imageView.setImageResource(R.drawable.paired_feed);
                return;
            default:
                return;
        }
    }

    private void setEventTitle(Event event) {
        setEventTitle(this.eventTitle, event.getType());
    }

    /* access modifiers changed from: protected */
    public void setEventTitle(BaseTextView textView, Event.EventType eventType) {
        switch (eventType) {
            case ETJetLagTreatment:
                textView.setTextColor(getResources().getColor(R.color.text_normal_color));
                textView.setText(R.string.jet_lag_treatment);
                return;
            case ETNapUltimate:
            case ETNapRem:
            case ETNapBody:
            case ETNapPower:
                textView.setTextColor(getResources().getColor(R.color.text_normal_color));
                textView.setText(R.string.personal_pause);
                return;
            case ETBLT:
                textView.setTextColor(getResources().getColor(R.color.text_normal_color));
                textView.setText(R.string.light_boost);
                return;
            case ETSleep:
                textView.setTextColor(getResources().getColor(R.color.text_normal_color));
                textView.setText(R.string.sleep);
                return;
            case ETSeekLight:
                textView.setTextColor(getResources().getColor(R.color.seek_light_color));
                textView.setText(R.string.seek_light);
                return;
            case ETSleepy:
                textView.setTextColor(getResources().getColor(R.color.feeling_sleepy_color));
                textView.setText(R.string.feeling_sleepy);
                return;
            case ETAvoidLight:
                textView.setTextColor(getResources().getColor(R.color.avoid_light_color));
                textView.setText(R.string.avoid_light);
                return;
            case ETPaired:
                textView.setTextColor(getResources().getColor(R.color.mask_paired_color));
                textView.setText(R.string.neuroon_mask_paired);
                return;
            default:
                return;
        }
    }

    private void setEventTime(Event event) {
        setEventTime(this.mEventTime, event);
    }

    /* access modifiers changed from: protected */
    public void setEventTime(BaseTextView eventTime, Event event) {
        DateFormat timeInstance = DateFormat.getTimeInstance(3);
        switch (event.getType()) {
            case ETPaired:
                eventTime.setText(timeInstance.format(event.getStartDate()));
                return;
            default:
                eventTime.setText(getResources().getString(R.string.time_dash_time, new Object[]{timeInstance.format(event.getStartDate()), timeInstance.format(event.getEndDate())}));
                return;
        }
    }
}
