package com.inteliclinic.neuroon.views.pickers;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.SwitchView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class PickerView extends FrameLayout implements IIndicatorable {
    public static final int THEME_BIORHYTHM = 1;
    public static final int THEME_GENERAL = 0;
    public static final int THEME_ONE_DAY_ALARM = 2;
    /* access modifiers changed from: private */
    public static boolean isShowing = false;
    @InjectView(2131755116)
    LinearLayout container;
    @InjectView(2131755617)
    SwitchView customSwitch;
    @InjectView(2131755618)
    ImageView done;
    @InjectView(2131755236)
    ImageView indicator;
    private int mTheme;

    public interface OnItemSelectedListener {
        void onItemSelected(RecyclerView recyclerView, View view, int i);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PickerTheme {
    }

    public PickerView(Context context) {
        this(context, (AttributeSet) null);
    }

    public PickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public PickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public static void setIsShowing(boolean isShowing2) {
        isShowing = isShowing2;
    }

    public static synchronized boolean isShowing() {
        boolean z;
        synchronized (PickerView.class) {
            z = isShowing;
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public void setTheme(int theme) {
        this.mTheme = theme;
        doTheme();
    }

    /* access modifiers changed from: protected */
    public void hideSwitch() {
        this.customSwitch.setVisibility(8);
    }

    /* access modifiers changed from: protected */
    public void init() {
        inflate(getContext(), R.layout.view_picker, this);
        ButterKnife.inject((View) this);
        this.mTheme = 0;
        doTheme();
    }

    private void doTheme() {
        if (this.mTheme == 0) {
            this.indicator.setImageResource(R.drawable.belka_standard);
            this.done.setImageResource(R.drawable.button_maly_standard);
        } else if (this.mTheme == 1) {
            this.indicator.setImageResource(R.drawable.belka_biorythm);
            this.done.setImageResource(R.drawable.button_maly_biorytm);
        } else if (this.mTheme == 2) {
            this.indicator.setImageResource(R.drawable.belka_alarm_one_day);
            this.done.setImageResource(R.drawable.button_maly_alarm);
        }
    }

    @OnClick({2131755618})
    public void doneClick() {
        save();
        dismiss();
    }

    /* access modifiers changed from: protected */
    public void save() {
    }

    @OnClick({2131755037})
    public void hide() {
        dismiss();
    }

    public void dismiss() {
        dismiss(true);
    }

    private synchronized void dismiss(boolean animate) {
        if (isShowing) {
            if (animate) {
                Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.abc_slide_out_bottom);
                anim.setDuration(300);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        ((ViewGroup) PickerView.this.getParent()).removeView(PickerView.this);
                        boolean unused = PickerView.isShowing = false;
                    }

                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                startAnimation(anim);
            } else {
                ((ViewGroup) getParent()).removeView(this);
                isShowing = false;
            }
        }
    }

    public boolean dispatchKeyEventPreIme(KeyEvent event) {
        if (event.getKeyCode() != 4) {
            return super.dispatchKeyEventPreIme(event);
        }
        dismiss();
        return true;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.abc_slide_in_bottom);
        anim.setDuration(300);
        anim.setFillAfter(true);
        anim.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                PickerView.this.requestFocus();
            }

            public void onAnimationRepeat(Animation animation) {
            }
        });
        startAnimation(anim);
    }

    /* access modifiers changed from: protected */
    public RecyclerView initPicker(@IdRes int resource, RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter, boolean reverseAdapter, OnItemSelectedListener listener) {
        RecyclerView recyclerView = (RecyclerView) findViewById(resource);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), 1, reverseAdapter) {
            public void scrollToPosition(int position) {
                super.scrollToPositionWithOffset(position, PickerView.this.indicator.getTop());
            }
        });
        recyclerView.addItemDecoration(new PickerItemDecoration(this.indicator, this.container, reverseAdapter));
        recyclerView.addOnScrollListener(new CenterScrollListener(this, listener));
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    public View getIndicator() {
        return this.indicator;
    }
}
