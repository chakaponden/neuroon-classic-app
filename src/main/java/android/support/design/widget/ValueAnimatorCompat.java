package android.support.design.widget;

import android.view.animation.Interpolator;

class ValueAnimatorCompat {
    private final Impl mImpl;

    interface AnimatorListener {
        void onAnimationCancel(ValueAnimatorCompat valueAnimatorCompat);

        void onAnimationEnd(ValueAnimatorCompat valueAnimatorCompat);

        void onAnimationStart(ValueAnimatorCompat valueAnimatorCompat);
    }

    interface AnimatorUpdateListener {
        void onAnimationUpdate(ValueAnimatorCompat valueAnimatorCompat);
    }

    interface Creator {
        ValueAnimatorCompat createAnimator();
    }

    static class AnimatorListenerAdapter implements AnimatorListener {
        AnimatorListenerAdapter() {
        }

        public void onAnimationStart(ValueAnimatorCompat animator) {
        }

        public void onAnimationEnd(ValueAnimatorCompat animator) {
        }

        public void onAnimationCancel(ValueAnimatorCompat animator) {
        }
    }

    static abstract class Impl {

        interface AnimatorListenerProxy {
            void onAnimationCancel();

            void onAnimationEnd();

            void onAnimationStart();
        }

        interface AnimatorUpdateListenerProxy {
            void onAnimationUpdate();
        }

        /* access modifiers changed from: package-private */
        public abstract void cancel();

        /* access modifiers changed from: package-private */
        public abstract void end();

        /* access modifiers changed from: package-private */
        public abstract float getAnimatedFloatValue();

        /* access modifiers changed from: package-private */
        public abstract float getAnimatedFraction();

        /* access modifiers changed from: package-private */
        public abstract int getAnimatedIntValue();

        /* access modifiers changed from: package-private */
        public abstract long getDuration();

        /* access modifiers changed from: package-private */
        public abstract boolean isRunning();

        /* access modifiers changed from: package-private */
        public abstract void setDuration(int i);

        /* access modifiers changed from: package-private */
        public abstract void setFloatValues(float f, float f2);

        /* access modifiers changed from: package-private */
        public abstract void setIntValues(int i, int i2);

        /* access modifiers changed from: package-private */
        public abstract void setInterpolator(Interpolator interpolator);

        /* access modifiers changed from: package-private */
        public abstract void setListener(AnimatorListenerProxy animatorListenerProxy);

        /* access modifiers changed from: package-private */
        public abstract void setUpdateListener(AnimatorUpdateListenerProxy animatorUpdateListenerProxy);

        /* access modifiers changed from: package-private */
        public abstract void start();

        Impl() {
        }
    }

    ValueAnimatorCompat(Impl impl) {
        this.mImpl = impl;
    }

    public void start() {
        this.mImpl.start();
    }

    public boolean isRunning() {
        return this.mImpl.isRunning();
    }

    public void setInterpolator(Interpolator interpolator) {
        this.mImpl.setInterpolator(interpolator);
    }

    public void setUpdateListener(final AnimatorUpdateListener updateListener) {
        if (updateListener != null) {
            this.mImpl.setUpdateListener(new Impl.AnimatorUpdateListenerProxy() {
                public void onAnimationUpdate() {
                    updateListener.onAnimationUpdate(ValueAnimatorCompat.this);
                }
            });
        } else {
            this.mImpl.setUpdateListener((Impl.AnimatorUpdateListenerProxy) null);
        }
    }

    public void setListener(final AnimatorListener listener) {
        if (listener != null) {
            this.mImpl.setListener(new Impl.AnimatorListenerProxy() {
                public void onAnimationStart() {
                    listener.onAnimationStart(ValueAnimatorCompat.this);
                }

                public void onAnimationEnd() {
                    listener.onAnimationEnd(ValueAnimatorCompat.this);
                }

                public void onAnimationCancel() {
                    listener.onAnimationCancel(ValueAnimatorCompat.this);
                }
            });
        } else {
            this.mImpl.setListener((Impl.AnimatorListenerProxy) null);
        }
    }

    public void setIntValues(int from, int to) {
        this.mImpl.setIntValues(from, to);
    }

    public int getAnimatedIntValue() {
        return this.mImpl.getAnimatedIntValue();
    }

    public void setFloatValues(float from, float to) {
        this.mImpl.setFloatValues(from, to);
    }

    public float getAnimatedFloatValue() {
        return this.mImpl.getAnimatedFloatValue();
    }

    public void setDuration(int duration) {
        this.mImpl.setDuration(duration);
    }

    public void cancel() {
        this.mImpl.cancel();
    }

    public float getAnimatedFraction() {
        return this.mImpl.getAnimatedFraction();
    }

    public void end() {
        this.mImpl.end();
    }

    public long getDuration() {
        return this.mImpl.getDuration();
    }
}
