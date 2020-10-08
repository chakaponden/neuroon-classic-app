package io.intercom.uk.co.senab.photoview.gestures;

import android.content.Context;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import io.intercom.uk.co.senab.photoview.log.LogManager;

public class CupcakeGestureDetector implements GestureDetector {
    private static final String LOG_TAG = "CupcakeGestureDetector";
    private boolean mIsDragging;
    float mLastTouchX;
    float mLastTouchY;
    protected OnGestureListener mListener;
    final float mMinimumVelocity;
    final float mTouchSlop;
    private VelocityTracker mVelocityTracker;

    public void setOnGestureListener(OnGestureListener listener) {
        this.mListener = listener;
    }

    public CupcakeGestureDetector(Context context) {
        ViewConfiguration configuration = ViewConfiguration.get(context);
        this.mMinimumVelocity = (float) configuration.getScaledMinimumFlingVelocity();
        this.mTouchSlop = (float) configuration.getScaledTouchSlop();
    }

    /* access modifiers changed from: package-private */
    public float getActiveX(MotionEvent ev) {
        return ev.getX();
    }

    /* access modifiers changed from: package-private */
    public float getActiveY(MotionEvent ev) {
        return ev.getY();
    }

    public boolean isScaling() {
        return false;
    }

    public boolean isDragging() {
        return this.mIsDragging;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        boolean z = false;
        switch (ev.getAction()) {
            case 0:
                this.mVelocityTracker = VelocityTracker.obtain();
                if (this.mVelocityTracker != null) {
                    this.mVelocityTracker.addMovement(ev);
                } else {
                    LogManager.getLogger().i(LOG_TAG, "Velocity tracker is null");
                }
                this.mLastTouchX = getActiveX(ev);
                this.mLastTouchY = getActiveY(ev);
                this.mIsDragging = false;
                break;
            case 1:
                if (this.mIsDragging && this.mVelocityTracker != null) {
                    this.mLastTouchX = getActiveX(ev);
                    this.mLastTouchY = getActiveY(ev);
                    this.mVelocityTracker.addMovement(ev);
                    this.mVelocityTracker.computeCurrentVelocity(1000);
                    float vX = this.mVelocityTracker.getXVelocity();
                    float vY = this.mVelocityTracker.getYVelocity();
                    if (Math.max(Math.abs(vX), Math.abs(vY)) >= this.mMinimumVelocity) {
                        this.mListener.onFling(this.mLastTouchX, this.mLastTouchY, -vX, -vY);
                    }
                }
                if (this.mVelocityTracker != null) {
                    this.mVelocityTracker.recycle();
                    this.mVelocityTracker = null;
                    break;
                }
                break;
            case 2:
                float x = getActiveX(ev);
                float y = getActiveY(ev);
                float dx = x - this.mLastTouchX;
                float dy = y - this.mLastTouchY;
                if (!this.mIsDragging) {
                    if (Math.sqrt((double) ((dx * dx) + (dy * dy))) >= ((double) this.mTouchSlop)) {
                        z = true;
                    }
                    this.mIsDragging = z;
                }
                if (this.mIsDragging) {
                    this.mListener.onDrag(dx, dy);
                    this.mLastTouchX = x;
                    this.mLastTouchY = y;
                    if (this.mVelocityTracker != null) {
                        this.mVelocityTracker.addMovement(ev);
                        break;
                    }
                }
                break;
            case 3:
                if (this.mVelocityTracker != null) {
                    this.mVelocityTracker.recycle();
                    this.mVelocityTracker = null;
                    break;
                }
                break;
        }
        return true;
    }
}
