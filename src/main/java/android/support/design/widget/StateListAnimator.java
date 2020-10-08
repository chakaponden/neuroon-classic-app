package android.support.design.widget;

import android.util.StateSet;
import android.view.View;
import android.view.animation.Animation;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

final class StateListAnimator {
    private Animation.AnimationListener mAnimationListener = new Animation.AnimationListener() {
        public void onAnimationEnd(Animation animation) {
            if (StateListAnimator.this.mRunningAnimation == animation) {
                Animation unused = StateListAnimator.this.mRunningAnimation = null;
            }
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationRepeat(Animation animation) {
        }
    };
    private Tuple mLastMatch = null;
    /* access modifiers changed from: private */
    public Animation mRunningAnimation = null;
    private final ArrayList<Tuple> mTuples = new ArrayList<>();
    private WeakReference<View> mViewRef;

    StateListAnimator() {
    }

    public void addState(int[] specs, Animation animation) {
        Tuple tuple = new Tuple(specs, animation);
        animation.setAnimationListener(this.mAnimationListener);
        this.mTuples.add(tuple);
    }

    /* access modifiers changed from: package-private */
    public Animation getRunningAnimation() {
        return this.mRunningAnimation;
    }

    /* access modifiers changed from: package-private */
    public View getTarget() {
        if (this.mViewRef == null) {
            return null;
        }
        return (View) this.mViewRef.get();
    }

    /* access modifiers changed from: package-private */
    public void setTarget(View view) {
        View current = getTarget();
        if (current != view) {
            if (current != null) {
                clearTarget();
            }
            if (view != null) {
                this.mViewRef = new WeakReference<>(view);
            }
        }
    }

    private void clearTarget() {
        View view = getTarget();
        int size = this.mTuples.size();
        for (int i = 0; i < size; i++) {
            if (view.getAnimation() == this.mTuples.get(i).mAnimation) {
                view.clearAnimation();
            }
        }
        this.mViewRef = null;
        this.mLastMatch = null;
        this.mRunningAnimation = null;
    }

    /* access modifiers changed from: package-private */
    public void setState(int[] state) {
        Tuple match = null;
        int count = this.mTuples.size();
        int i = 0;
        while (true) {
            if (i >= count) {
                break;
            }
            Tuple tuple = this.mTuples.get(i);
            if (StateSet.stateSetMatches(tuple.mSpecs, state)) {
                match = tuple;
                break;
            }
            i++;
        }
        if (match != this.mLastMatch) {
            if (this.mLastMatch != null) {
                cancel();
            }
            this.mLastMatch = match;
            View view = (View) this.mViewRef.get();
            if (match != null && view != null && view.getVisibility() == 0) {
                start(match);
            }
        }
    }

    private void start(Tuple match) {
        this.mRunningAnimation = match.mAnimation;
        View view = getTarget();
        if (view != null) {
            view.startAnimation(this.mRunningAnimation);
        }
    }

    private void cancel() {
        if (this.mRunningAnimation != null) {
            View view = getTarget();
            if (view != null && view.getAnimation() == this.mRunningAnimation) {
                view.clearAnimation();
            }
            this.mRunningAnimation = null;
        }
    }

    /* access modifiers changed from: package-private */
    public ArrayList<Tuple> getTuples() {
        return this.mTuples;
    }

    public void jumpToCurrentState() {
        View view;
        if (this.mRunningAnimation != null && (view = getTarget()) != null && view.getAnimation() == this.mRunningAnimation) {
            view.clearAnimation();
        }
    }

    static class Tuple {
        final Animation mAnimation;
        final int[] mSpecs;

        private Tuple(int[] specs, Animation Animation) {
            this.mSpecs = specs;
            this.mAnimation = Animation;
        }

        /* access modifiers changed from: package-private */
        public int[] getSpecs() {
            return this.mSpecs;
        }

        /* access modifiers changed from: package-private */
        public Animation getAnimation() {
            return this.mAnimation;
        }
    }
}
