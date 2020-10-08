package io.intercom.android.sdk.conversation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.TargetApi;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ImageButton;
import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.nexus.NexusEvent;

@TargetApi(14)
class ComposerTextWatcher implements TextWatcher {
    private static final long IS_TYPING_DELAY = 1000;
    private final ImageButton attachmentButton;
    private String conversationId = "";
    private final Handler handler;
    private ObjectAnimator hideAttach;
    private ObjectAnimator hideSend;
    private final ImageButton sendButton;
    /* access modifiers changed from: private */
    public boolean shouldSendIsTyping = true;
    private ObjectAnimator showAttach;
    private ObjectAnimator showSend;
    private final String userId;

    public ComposerTextWatcher(final ImageButton sendButton2, final ImageButton attachmentButton2, String userId2) {
        this.sendButton = sendButton2;
        this.attachmentButton = attachmentButton2;
        this.userId = userId2;
        HandlerThread bgThread = new HandlerThread("composer-background-handler");
        bgThread.start();
        this.handler = new Handler(bgThread.getLooper());
        this.showAttach = ObjectAnimator.ofPropertyValuesHolder(attachmentButton2, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat("alpha", new float[]{0.0f, 1.0f}), PropertyValuesHolder.ofFloat("scaleX", new float[]{0.6f, 1.0f}), PropertyValuesHolder.ofFloat("scaleY", new float[]{0.6f, 1.0f})}).setDuration(100);
        this.showAttach.setStartDelay(50);
        this.showAttach.addListener(new Animator.AnimatorListener() {
            public void onAnimationStart(Animator animation) {
                attachmentButton2.setVisibility(0);
            }

            public void onAnimationEnd(Animator animation) {
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        this.hideAttach = ObjectAnimator.ofPropertyValuesHolder(attachmentButton2, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat("alpha", new float[]{1.0f, 0.0f}), PropertyValuesHolder.ofFloat("scaleX", new float[]{1.0f, 0.6f}), PropertyValuesHolder.ofFloat("scaleY", new float[]{1.0f, 0.6f})}).setDuration(100);
        this.hideAttach.addListener(new Animator.AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                attachmentButton2.setVisibility(8);
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        this.showSend = ObjectAnimator.ofPropertyValuesHolder(sendButton2, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat("alpha", new float[]{0.0f, 1.0f}), PropertyValuesHolder.ofFloat("scaleX", new float[]{0.6f, 1.0f}), PropertyValuesHolder.ofFloat("scaleY", new float[]{0.6f, 1.0f})}).setDuration(100);
        this.showSend.setStartDelay(50);
        this.showSend.addListener(new Animator.AnimatorListener() {
            public void onAnimationStart(Animator animation) {
                sendButton2.setVisibility(0);
            }

            public void onAnimationEnd(Animator animation) {
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
        this.hideSend = ObjectAnimator.ofPropertyValuesHolder(sendButton2, new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat("alpha", new float[]{1.0f, 0.0f}), PropertyValuesHolder.ofFloat("scaleX", new float[]{1.0f, 0.6f}), PropertyValuesHolder.ofFloat("scaleY", new float[]{1.0f, 0.6f})}).setDuration(100);
        this.hideSend.addListener(new Animator.AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                sendButton2.setVisibility(8);
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    /* access modifiers changed from: protected */
    public void setConversationId(String conversationId2) {
        this.conversationId = conversationId2;
    }

    /* access modifiers changed from: protected */
    public void cleanup() {
        this.handler.removeCallbacksAndMessages((Object) null);
        this.handler.getLooper().quit();
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    public void afterTextChanged(Editable editable) {
        if (this.shouldSendIsTyping) {
            this.shouldSendIsTyping = false;
            this.handler.postDelayed(new Runnable() {
                public void run() {
                    boolean unused = ComposerTextWatcher.this.shouldSendIsTyping = true;
                }
            }, IS_TYPING_DELAY);
            if (!this.conversationId.isEmpty()) {
                Bridge.getNexusClient().fire(NexusEvent.getUserIsTypingEvent(this.conversationId, this.userId));
            }
        }
        if (containsText(editable.toString())) {
            if (this.sendButton.getVisibility() == 8) {
                this.hideAttach.start();
                this.showSend.start();
            }
        } else if (this.attachmentButton.getVisibility() == 8) {
            this.hideSend.start();
            this.showAttach.start();
        }
    }

    protected static boolean containsText(String text) {
        return !text.trim().isEmpty();
    }
}
