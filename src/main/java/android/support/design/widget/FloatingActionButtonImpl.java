package android.support.design.widget;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.design.R;
import android.view.View;
import android.view.ViewTreeObserver;

abstract class FloatingActionButtonImpl {
    static final int[] EMPTY_STATE_SET = new int[0];
    static final int[] FOCUSED_ENABLED_STATE_SET = {16842908, 16842910};
    static final int[] PRESSED_ENABLED_STATE_SET = {16842919, 16842910};
    static final int SHOW_HIDE_ANIM_DURATION = 200;
    private ViewTreeObserver.OnPreDrawListener mPreDrawListener;
    final ShadowViewDelegate mShadowViewDelegate;
    final View mView;

    interface InternalVisibilityChangedListener {
        void onHidden();

        void onShown();
    }

    /* access modifiers changed from: package-private */
    public abstract void hide(@Nullable InternalVisibilityChangedListener internalVisibilityChangedListener);

    /* access modifiers changed from: package-private */
    public abstract void jumpDrawableToCurrentState();

    /* access modifiers changed from: package-private */
    public abstract void onDrawableStateChanged(int[] iArr);

    /* access modifiers changed from: package-private */
    public abstract void setBackgroundDrawable(ColorStateList colorStateList, PorterDuff.Mode mode, int i, int i2);

    /* access modifiers changed from: package-private */
    public abstract void setBackgroundTintList(ColorStateList colorStateList);

    /* access modifiers changed from: package-private */
    public abstract void setBackgroundTintMode(PorterDuff.Mode mode);

    /* access modifiers changed from: package-private */
    public abstract void setElevation(float f);

    /* access modifiers changed from: package-private */
    public abstract void setPressedTranslationZ(float f);

    /* access modifiers changed from: package-private */
    public abstract void setRippleColor(int i);

    /* access modifiers changed from: package-private */
    public abstract void show(@Nullable InternalVisibilityChangedListener internalVisibilityChangedListener);

    FloatingActionButtonImpl(View view, ShadowViewDelegate shadowViewDelegate) {
        this.mView = view;
        this.mShadowViewDelegate = shadowViewDelegate;
    }

    /* access modifiers changed from: package-private */
    public void onAttachedToWindow() {
        if (requirePreDrawListener()) {
            ensurePreDrawListener();
            this.mView.getViewTreeObserver().addOnPreDrawListener(this.mPreDrawListener);
        }
    }

    /* access modifiers changed from: package-private */
    public void onDetachedFromWindow() {
        if (this.mPreDrawListener != null) {
            this.mView.getViewTreeObserver().removeOnPreDrawListener(this.mPreDrawListener);
            this.mPreDrawListener = null;
        }
    }

    /* access modifiers changed from: package-private */
    public boolean requirePreDrawListener() {
        return false;
    }

    /* access modifiers changed from: package-private */
    public CircularBorderDrawable createBorderDrawable(int borderWidth, ColorStateList backgroundTint) {
        Resources resources = this.mView.getResources();
        CircularBorderDrawable borderDrawable = newCircularDrawable();
        borderDrawable.setGradientColors(resources.getColor(R.color.design_fab_stroke_top_outer_color), resources.getColor(R.color.design_fab_stroke_top_inner_color), resources.getColor(R.color.design_fab_stroke_end_inner_color), resources.getColor(R.color.design_fab_stroke_end_outer_color));
        borderDrawable.setBorderWidth((float) borderWidth);
        borderDrawable.setBorderTint(backgroundTint);
        return borderDrawable;
    }

    /* access modifiers changed from: package-private */
    public CircularBorderDrawable newCircularDrawable() {
        return new CircularBorderDrawable();
    }

    /* access modifiers changed from: package-private */
    public GradientDrawable createShapeDrawable() {
        GradientDrawable d = new GradientDrawable();
        d.setShape(1);
        d.setColor(-1);
        return d;
    }

    /* access modifiers changed from: package-private */
    public void onPreDraw() {
    }

    private void ensurePreDrawListener() {
        if (this.mPreDrawListener == null) {
            this.mPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    FloatingActionButtonImpl.this.onPreDraw();
                    return true;
                }
            };
        }
    }
}
