package io.intercom.android.sdk.preview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.view.View;

@TargetApi(11)
public class PreviewAnimation {
    private static final int OFFSET_MS = 500;

    static void fadeInPreview(View notificationAvatar, View indicatorTextView, View shadow) {
        notificationAvatar.setAlpha(0.0f);
        indicatorTextView.setAlpha(0.0f);
        shadow.setAlpha(0.0f);
        ObjectAnimator avatarAnimation = ObjectAnimator.ofPropertyValuesHolder(notificationAvatar, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat("alpha", new float[]{0.0f, 1.0f}), PropertyValuesHolder.ofFloat("scaleX", new float[]{0.6f, 1.0f}), PropertyValuesHolder.ofFloat("scaleY", new float[]{0.6f, 1.0f})});
        avatarAnimation.setDuration(200);
        avatarAnimation.setStartDelay(500);
        ObjectAnimator shadowAnimation = avatarAnimation.clone();
        shadowAnimation.setTarget(shadow);
        ObjectAnimator indicatorAnimation = avatarAnimation.clone();
        indicatorAnimation.setStartDelay(680);
        indicatorAnimation.setTarget(indicatorTextView);
        avatarAnimation.start();
        indicatorAnimation.start();
        shadowAnimation.start();
    }

    static AnimatorSet animateTextPreview(View target, boolean isLeftAligned) {
        if (isLeftAligned) {
            return animateTextPreview(target, target.getTranslationX() - 120.0f);
        }
        return animateTextPreview(target, target.getTranslationX() + 120.0f);
    }

    private static AnimatorSet animateTextPreview(View target, float offset) {
        ObjectAnimator entranceAnim = ObjectAnimator.ofPropertyValuesHolder(target, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat("alpha", new float[]{0.0f, 1.0f}), PropertyValuesHolder.ofFloat("scaleX", new float[]{0.6f, 1.0f}), PropertyValuesHolder.ofFloat("scaleY", new float[]{0.6f, 1.0f}), PropertyValuesHolder.ofFloat("translationX", new float[]{offset, target.getTranslationX()})});
        entranceAnim.setDuration(200);
        entranceAnim.setStartDelay(720);
        ObjectAnimator exit = ObjectAnimator.ofPropertyValuesHolder(target, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat("alpha", new float[]{1.0f, 0.0f}), PropertyValuesHolder.ofFloat("scaleX", new float[]{1.0f, 0.6f}), PropertyValuesHolder.ofFloat("scaleY", new float[]{1.0f, 0.6f}), PropertyValuesHolder.ofFloat("translationX", new float[]{target.getTranslationX(), offset})});
        exit.setDuration(50);
        exit.setStartDelay(6500);
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(new Animator[]{entranceAnim, exit});
        return set;
    }
}
