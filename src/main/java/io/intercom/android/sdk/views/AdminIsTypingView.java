package io.intercom.android.sdk.views;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.conversation.events.AdminIsTypingEvent;
import io.intercom.android.sdk.conversation.events.AdminTypingEndedEvent;
import io.intercom.android.sdk.conversation.events.CancelAdminTypingEvent;
import io.intercom.android.sdk.logger.IntercomLogger;
import io.intercom.com.squareup.otto.Subscribe;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@TargetApi(14)
public class AdminIsTypingView extends LinearLayout {
    private static final int ANIMATION_DELAY = 100;
    private static final int IS_TYPING_DURATION = 10;
    private final String adminId;
    private boolean animating;
    private final String conversationId;
    /* access modifiers changed from: private */
    public final ImageView[] dots;
    /* access modifiers changed from: private */
    public final AnimatorSet[] dotsAnimations;
    private ScheduledFuture future;
    /* access modifiers changed from: private */
    public int iterationCount;
    private final String partId;

    static /* synthetic */ int access$008(AdminIsTypingView x0) {
        int i = x0.iterationCount;
        x0.iterationCount = i + 1;
        return i;
    }

    public AdminIsTypingView(Context context) {
        this(context, "", "", "");
    }

    public AdminIsTypingView(Context context, AttributeSet attrs) {
        this(context, "", "", "");
    }

    public AdminIsTypingView(Context context, String adminId2, String conversationId2, String partId2) {
        super(context);
        this.dots = new ImageView[3];
        this.dotsAnimations = new AnimatorSet[3];
        this.animating = false;
        this.iterationCount = 0;
        inflate(getContext(), R.layout.intercomsdk_admin_is_typing, this);
        this.adminId = adminId2;
        this.conversationId = conversationId2;
        this.partId = partId2;
        this.dots[0] = (ImageView) findViewById(R.id.dot1);
        this.dots[1] = (ImageView) findViewById(R.id.dot2);
        this.dots[2] = (ImageView) findViewById(R.id.dot3);
        for (int i = 0; i < this.dotsAnimations.length; i++) {
            this.dotsAnimations[i] = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.intercomsdk_admin_is_typing);
            this.dotsAnimations[i].setStartDelay((long) (i * 100));
        }
        Bridge.getBus().register(this);
    }

    public void beginAnimation() {
        if (!this.animating) {
            this.animating = true;
            this.future = Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
                public void run() {
                    if (AdminIsTypingView.this.iterationCount < 10) {
                        AdminIsTypingView.access$008(AdminIsTypingView.this);
                        for (int i = 0; i < AdminIsTypingView.this.dots.length; i++) {
                            final int j = i;
                            AdminIsTypingView.this.dots[i].post(new Runnable() {
                                public void run() {
                                    AdminIsTypingView.this.dotsAnimations[j].setTarget(AdminIsTypingView.this.dots[j]);
                                    AdminIsTypingView.this.dotsAnimations[j].start();
                                }
                            });
                        }
                        return;
                    }
                    AdminIsTypingView.this.endAnimation();
                }
            }, 0, 1, TimeUnit.SECONDS);
        }
    }

    /* access modifiers changed from: private */
    public void endAnimation() {
        if (this.animating) {
            this.animating = false;
            Bridge.getBus().unregister(this);
            Bridge.getBus().post(new AdminTypingEndedEvent(this.adminId, this.conversationId, this.partId));
            IntercomLogger.INTERNAL("isTyping", "ending animation");
            if (!this.future.isCancelled()) {
                this.future.cancel(true);
            }
            for (AnimatorSet set : this.dotsAnimations) {
                set.cancel();
            }
        }
    }

    @Subscribe
    public void renewTypingAnimation(AdminIsTypingEvent event) {
        if (event.getAdminId().equals(this.adminId) && event.getConversationId().equals(this.conversationId)) {
            this.iterationCount = 0;
        }
    }

    @Subscribe
    public void cancelTypingAnimation(CancelAdminTypingEvent ignored) {
        endAnimation();
    }
}
