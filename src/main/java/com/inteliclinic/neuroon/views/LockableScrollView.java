package com.inteliclinic.neuroon.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class LockableScrollView extends ScrollView {
    private boolean mScrollable = false;

    public LockableScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LockableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LockableScrollView(Context context) {
        super(context);
    }

    public void setScrollingEnabled(boolean enabled) {
        this.mScrollable = enabled;
    }

    public boolean isScrollable() {
        return this.mScrollable;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case 0:
                if (this.mScrollable) {
                    return super.onTouchEvent(ev);
                }
                return this.mScrollable;
            default:
                return super.onTouchEvent(ev);
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case 0:
                if (this.mScrollable) {
                    return super.onInterceptTouchEvent(ev);
                }
                return this.mScrollable;
            default:
                return super.onInterceptTouchEvent(ev);
        }
    }
}
