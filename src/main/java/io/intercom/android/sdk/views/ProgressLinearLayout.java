package io.intercom.android.sdk.views;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.conversation.UploadProgressListener;
import io.intercom.android.sdk.logger.IntercomLogger;

@TargetApi(11)
public class ProgressLinearLayout extends LinearLayout implements UploadProgressListener {
    /* access modifiers changed from: private */
    public UploadProgressBar uploadProgressBar;

    public ProgressLinearLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public ProgressLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setUploadProgressBar(UploadProgressBar uploadProgressBar2) {
        this.uploadProgressBar = uploadProgressBar2;
    }

    public void uploadNotice(final byte percentUploaded) {
        IntercomLogger.INTERNAL("progress", "" + percentUploaded);
        post(new Runnable() {
            public void run() {
                if (ProgressLinearLayout.this.uploadProgressBar != null) {
                    ProgressLinearLayout.this.uploadProgressBar.setProgress(percentUploaded);
                    if (percentUploaded == 90) {
                        ProgressLinearLayout.this.uploadProgressBar.smoothEndAnimation(new Animator.AnimatorListener() {
                            public void onAnimationStart(Animator animation) {
                            }

                            public void onAnimationEnd(Animator animation) {
                                ProgressLinearLayout.this.uploadProgressBar.hideBar();
                                ImageView attachmentIcon = (ImageView) ProgressLinearLayout.this.findViewById(R.id.attachment_icon);
                                if (attachmentIcon != null) {
                                    attachmentIcon.setVisibility(0);
                                    ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(attachmentIcon, "alpha", new float[]{0.0f, 1.0f});
                                    objectAnimator.setDuration(300);
                                    objectAnimator.setInterpolator(new DecelerateInterpolator());
                                    objectAnimator.start();
                                }
                            }

                            public void onAnimationCancel(Animator animation) {
                            }

                            public void onAnimationRepeat(Animator animation) {
                            }
                        });
                    }
                }
            }
        });
    }
}
