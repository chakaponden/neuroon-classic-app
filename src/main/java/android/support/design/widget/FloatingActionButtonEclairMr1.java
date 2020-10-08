package android.support.design.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.Nullable;
import android.support.design.R;
import android.support.design.widget.AnimationUtils;
import android.support.design.widget.FloatingActionButtonImpl;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;

class FloatingActionButtonEclairMr1 extends FloatingActionButtonImpl {
    private int mAnimationDuration;
    CircularBorderDrawable mBorderDrawable;
    /* access modifiers changed from: private */
    public float mElevation;
    /* access modifiers changed from: private */
    public boolean mIsHiding;
    /* access modifiers changed from: private */
    public float mPressedTranslationZ;
    Drawable mRippleDrawable;
    ShadowDrawableWrapper mShadowDrawable;
    Drawable mShapeDrawable;
    private StateListAnimator mStateListAnimator = new StateListAnimator();

    FloatingActionButtonEclairMr1(View view, ShadowViewDelegate shadowViewDelegate) {
        super(view, shadowViewDelegate);
        this.mAnimationDuration = view.getResources().getInteger(17694720);
        this.mStateListAnimator.setTarget(view);
        this.mStateListAnimator.addState(PRESSED_ENABLED_STATE_SET, setupAnimation(new ElevateToTranslationZAnimation()));
        this.mStateListAnimator.addState(FOCUSED_ENABLED_STATE_SET, setupAnimation(new ElevateToTranslationZAnimation()));
        this.mStateListAnimator.addState(EMPTY_STATE_SET, setupAnimation(new ResetElevationAnimation()));
    }

    /* access modifiers changed from: package-private */
    public void setBackgroundDrawable(ColorStateList backgroundTint, PorterDuff.Mode backgroundTintMode, int rippleColor, int borderWidth) {
        Drawable[] layers;
        this.mShapeDrawable = DrawableCompat.wrap(createShapeDrawable());
        DrawableCompat.setTintList(this.mShapeDrawable, backgroundTint);
        if (backgroundTintMode != null) {
            DrawableCompat.setTintMode(this.mShapeDrawable, backgroundTintMode);
        }
        this.mRippleDrawable = DrawableCompat.wrap(createShapeDrawable());
        DrawableCompat.setTintList(this.mRippleDrawable, createColorStateList(rippleColor));
        DrawableCompat.setTintMode(this.mRippleDrawable, PorterDuff.Mode.MULTIPLY);
        if (borderWidth > 0) {
            this.mBorderDrawable = createBorderDrawable(borderWidth, backgroundTint);
            layers = new Drawable[]{this.mBorderDrawable, this.mShapeDrawable, this.mRippleDrawable};
        } else {
            this.mBorderDrawable = null;
            layers = new Drawable[]{this.mShapeDrawable, this.mRippleDrawable};
        }
        this.mShadowDrawable = new ShadowDrawableWrapper(this.mView.getResources(), new LayerDrawable(layers), this.mShadowViewDelegate.getRadius(), this.mElevation, this.mElevation + this.mPressedTranslationZ);
        this.mShadowDrawable.setAddPaddingForCorners(false);
        this.mShadowViewDelegate.setBackgroundDrawable(this.mShadowDrawable);
        updatePadding();
    }

    /* access modifiers changed from: package-private */
    public void setBackgroundTintList(ColorStateList tint) {
        DrawableCompat.setTintList(this.mShapeDrawable, tint);
        if (this.mBorderDrawable != null) {
            this.mBorderDrawable.setBorderTint(tint);
        }
    }

    /* access modifiers changed from: package-private */
    public void setBackgroundTintMode(PorterDuff.Mode tintMode) {
        DrawableCompat.setTintMode(this.mShapeDrawable, tintMode);
    }

    /* access modifiers changed from: package-private */
    public void setRippleColor(int rippleColor) {
        DrawableCompat.setTintList(this.mRippleDrawable, createColorStateList(rippleColor));
    }

    /* access modifiers changed from: package-private */
    public void setElevation(float elevation) {
        if (this.mElevation != elevation && this.mShadowDrawable != null) {
            this.mShadowDrawable.setShadowSize(elevation, this.mPressedTranslationZ + elevation);
            this.mElevation = elevation;
            updatePadding();
        }
    }

    /* access modifiers changed from: package-private */
    public void setPressedTranslationZ(float translationZ) {
        if (this.mPressedTranslationZ != translationZ && this.mShadowDrawable != null) {
            this.mPressedTranslationZ = translationZ;
            this.mShadowDrawable.setMaxShadowSize(this.mElevation + translationZ);
            updatePadding();
        }
    }

    /* access modifiers changed from: package-private */
    public void onDrawableStateChanged(int[] state) {
        this.mStateListAnimator.setState(state);
    }

    /* access modifiers changed from: package-private */
    public void jumpDrawableToCurrentState() {
        this.mStateListAnimator.jumpToCurrentState();
    }

    /* access modifiers changed from: package-private */
    public void hide(@Nullable final FloatingActionButtonImpl.InternalVisibilityChangedListener listener) {
        if (!this.mIsHiding && this.mView.getVisibility() == 0) {
            Animation anim = AnimationUtils.loadAnimation(this.mView.getContext(), R.anim.design_fab_out);
            anim.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            anim.setDuration(200);
            anim.setAnimationListener(new AnimationUtils.AnimationListenerAdapter() {
                public void onAnimationStart(Animation animation) {
                    boolean unused = FloatingActionButtonEclairMr1.this.mIsHiding = true;
                }

                public void onAnimationEnd(Animation animation) {
                    boolean unused = FloatingActionButtonEclairMr1.this.mIsHiding = false;
                    FloatingActionButtonEclairMr1.this.mView.setVisibility(8);
                    if (listener != null) {
                        listener.onHidden();
                    }
                }
            });
            this.mView.startAnimation(anim);
        } else if (listener != null) {
            listener.onHidden();
        }
    }

    /* access modifiers changed from: package-private */
    public void show(@Nullable final FloatingActionButtonImpl.InternalVisibilityChangedListener listener) {
        if (this.mView.getVisibility() != 0 || this.mIsHiding) {
            this.mView.clearAnimation();
            this.mView.setVisibility(0);
            Animation anim = android.view.animation.AnimationUtils.loadAnimation(this.mView.getContext(), R.anim.design_fab_in);
            anim.setDuration(200);
            anim.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            anim.setAnimationListener(new AnimationUtils.AnimationListenerAdapter() {
                public void onAnimationEnd(Animation animation) {
                    if (listener != null) {
                        listener.onShown();
                    }
                }
            });
            this.mView.startAnimation(anim);
        } else if (listener != null) {
            listener.onShown();
        }
    }

    private void updatePadding() {
        Rect rect = new Rect();
        this.mShadowDrawable.getPadding(rect);
        this.mShadowViewDelegate.setShadowPadding(rect.left, rect.top, rect.right, rect.bottom);
    }

    private Animation setupAnimation(Animation animation) {
        animation.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        animation.setDuration((long) this.mAnimationDuration);
        return animation;
    }

    private abstract class BaseShadowAnimation extends Animation {
        private float mShadowSizeDiff;
        private float mShadowSizeStart;

        /* access modifiers changed from: protected */
        public abstract float getTargetShadowSize();

        private BaseShadowAnimation() {
        }

        public void reset() {
            super.reset();
            this.mShadowSizeStart = FloatingActionButtonEclairMr1.this.mShadowDrawable.getShadowSize();
            this.mShadowSizeDiff = getTargetShadowSize() - this.mShadowSizeStart;
        }

        /* access modifiers changed from: protected */
        public void applyTransformation(float interpolatedTime, Transformation t) {
            FloatingActionButtonEclairMr1.this.mShadowDrawable.setShadowSize(this.mShadowSizeStart + (this.mShadowSizeDiff * interpolatedTime));
        }
    }

    private class ResetElevationAnimation extends BaseShadowAnimation {
        private ResetElevationAnimation() {
            super();
        }

        /* access modifiers changed from: protected */
        public float getTargetShadowSize() {
            return FloatingActionButtonEclairMr1.this.mElevation;
        }
    }

    private class ElevateToTranslationZAnimation extends BaseShadowAnimation {
        private ElevateToTranslationZAnimation() {
            super();
        }

        /* access modifiers changed from: protected */
        public float getTargetShadowSize() {
            return FloatingActionButtonEclairMr1.this.mElevation + FloatingActionButtonEclairMr1.this.mPressedTranslationZ;
        }
    }

    private static ColorStateList createColorStateList(int selectedColor) {
        int[][] states = new int[3][];
        int[] colors = new int[3];
        states[0] = FOCUSED_ENABLED_STATE_SET;
        colors[0] = selectedColor;
        int i = 0 + 1;
        states[i] = PRESSED_ENABLED_STATE_SET;
        colors[i] = selectedColor;
        int i2 = i + 1;
        states[i2] = new int[0];
        colors[i2] = 0;
        int i3 = i2 + 1;
        return new ColorStateList(states, colors);
    }
}
