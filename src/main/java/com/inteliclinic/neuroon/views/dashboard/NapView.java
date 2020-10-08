package com.inteliclinic.neuroon.views.dashboard;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.models.data.Event;
import com.inteliclinic.neuroon.views.BaseTextView;
import com.inteliclinic.neuroon.views.BoldTextView;
import com.inteliclinic.neuroon.views.ThinTextView;
import java.text.DateFormat;

public class NapView extends RelativeLayout {
    private String mBetween;
    private Drawable mIcon;
    @InjectView(2131755610)
    ThinTextView mNapBestTake;
    @InjectView(2131755611)
    ThinTextView mNapBestTime;
    @InjectView(2131755608)
    ImageView mNapImage;
    @InjectView(2131755613)
    ThinTextView mNapTime;
    @InjectView(2131755614)
    ThinTextView mNapTimeUnit;
    @InjectView(2131755612)
    ThinTextView mNapTimeWhenever;
    @InjectView(2131755609)
    BoldTextView mNapTitle;
    private int mTheme;
    private int mTime;
    private String mTitle;

    public NapView(Context context) {
        this(context, (AttributeSet) null);
    }

    public NapView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initStyle(context, attrs, defStyleAttr, 0);
        setData();
    }

    @TargetApi(21)
    public NapView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
        initStyle(context, attrs, defStyleAttr, defStyleRes);
        setData();
    }

    private void init() {
        inflate(getContext(), R.layout.view_nap, this);
        ButterKnife.inject((View) this);
    }

    private void setData() {
        this.mNapImage.setImageDrawable(this.mIcon);
        this.mNapTitle.setText(this.mTitle);
        this.mNapTime.setText(String.valueOf(this.mTime));
        this.mNapBestTime.setText(this.mBetween);
    }

    public void setTheme(int theme) {
        this.mTheme = theme;
        if (this.mTheme == 1) {
            this.mNapBestTake.setTextColor(-1);
            this.mNapBestTime.setTextColor(-1);
            this.mNapTime.setTextColor(-1);
            this.mNapTimeUnit.setTextColor(-1);
        }
    }

    public void setNapImage(@DrawableRes int res) {
        this.mNapImage.setImageResource(res);
    }

    public void setNapTitle(String title) {
        this.mNapTitle.setText(title);
    }

    public void setNapTitleColor(@ColorInt int colorInt) {
        this.mNapTitle.setTextColor(colorInt);
    }

    public void setNapTime(int time) {
        this.mNapTime.setText(time);
    }

    public void setNapBetween(String between) {
        this.mNapBestTime.setText(between);
    }

    public void setNapUnit(String unit) {
        this.mNapTimeUnit.setText(unit);
    }

    private void initStyle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NapView, defStyleAttr, defStyleRes);
        try {
            this.mIcon = a.getDrawable(3);
            this.mTitle = a.getString(0);
            this.mBetween = a.getString(2);
            this.mTime = a.getInteger(1, 0);
        } finally {
            a.recycle();
        }
    }

    public void fillWithEvent(Event event) {
        setEventIcon(this.mNapImage, event.getType());
        setEventTitle(this.mNapTitle, event.getType());
        switch (event.getType()) {
            case ETBLT:
            case ETNapPower:
                this.mNapBestTake.setVisibility(8);
                this.mNapBestTime.setVisibility(8);
                this.mNapTimeWhenever.setVisibility(0);
                break;
            default:
                this.mNapBestTake.setVisibility(0);
                this.mNapBestTime.setVisibility(0);
                this.mNapTimeWhenever.setVisibility(8);
                DateFormat timeInstance = DateFormat.getTimeInstance(3);
                this.mNapBestTime.setText(getResources().getString(R.string.time_dash_time, new Object[]{timeInstance.format(event.getStartDate()), timeInstance.format(event.getEndDate())}));
                break;
        }
        switch (event.getType()) {
            case ETBLT:
                this.mNapTime.setTextColor(getResources().getColor(R.color.light_boost_color));
                this.mNapTimeUnit.setTextColor(getResources().getColor(R.color.light_boost_color));
                break;
            case ETNapPower:
            case ETNapBody:
            case ETNapRem:
            case ETNapUltimate:
                break;
            default:
                this.mNapTime.setText(String.valueOf(event.getDuration() / 3600));
                this.mNapTimeUnit.setText(R.string.h);
                return;
        }
        this.mNapTime.setText(String.valueOf(event.getDuration() / 60));
        this.mNapTimeUnit.setText(R.string.min);
    }

    /* access modifiers changed from: protected */
    public void setEventIcon(ImageView imageView, Event.EventType eventType) {
        switch (eventType) {
            case ETBLT:
                imageView.setImageResource(R.drawable.nap_light);
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
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void setEventTitle(BaseTextView textView, Event.EventType eventType) {
        switch (eventType) {
            case ETBLT:
                textView.setText(R.string.light_boost);
                textView.setTextColor(getResources().getColor(R.color.light_boost_color));
                return;
            case ETNapPower:
                textView.setText(R.string.power_nap);
                return;
            case ETNapBody:
                textView.setText(R.string.body_nap);
                return;
            case ETNapRem:
                textView.setText(R.string.rem_nap);
                return;
            case ETNapUltimate:
                textView.setText(R.string.ultimate_nap);
                return;
            case ETSeekLight:
                textView.setText(R.string.seek_light);
                textView.setTextColor(getResources().getColor(R.color.seek_light_color));
                return;
            case ETSleepy:
                textView.setText(R.string.feeling_sleepy);
                textView.setTextColor(getResources().getColor(R.color.feeling_sleepy_color));
                return;
            case ETAvoidLight:
                textView.setText(R.string.avoid_light);
                textView.setTextColor(getResources().getColor(R.color.avoid_light_color));
                return;
            default:
                return;
        }
    }
}
