package com.inteliclinic.neuroon.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class LockableViewPager extends ViewPager {
    private boolean mLocked;

    public LockableViewPager(Context context) {
        super(context);
    }

    public LockableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!this.mLocked) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (!this.mLocked) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    public void setLocked(boolean locked) {
        this.mLocked = locked;
    }
}
