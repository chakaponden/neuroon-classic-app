package io.intercom.android.sdk.preview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.SoundPool;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.activities.MainActivity;
import io.intercom.android.sdk.conversation.ConversationFragment;
import io.intercom.android.sdk.logger.IntercomLogger;
import io.intercom.android.sdk.models.Conversation;
import io.intercom.android.sdk.models.Part;
import io.intercom.android.sdk.utilities.AvatarUtils;
import io.intercom.android.sdk.utilities.FontUtils;
import io.intercom.android.sdk.utilities.IntercomUtils;
import io.intercom.android.sdk.utilities.ScreenUtils;
import java.util.List;

@TargetApi(11)
public class PreviewDisplayManager {
    private static final int PREVIEW_OPEN_DELAY_MILLIS = 200;
    private static final int PREVIEW_ORIGINAL_SCALE = 1;
    private static final float PREVIEW_TOUCHED_SCALE = 0.9f;
    protected Conversation currentlyDisplayedConversation;
    private int notificationPreviewVisible = 0;
    private IntercomPreviewPosition previewPosition = IntercomPreviewPosition.BOTTOM_LEFT;

    public int getVisibility() {
        return this.notificationPreviewVisible;
    }

    public IntercomPreviewPosition getPreviewPosition() {
        return this.previewPosition;
    }

    public void setVisibility(int visibility) {
        this.notificationPreviewVisible = visibility;
    }

    public void setPreviewPosition(IntercomPreviewPosition previewPosition2) {
        this.previewPosition = previewPosition2;
    }

    public void conditionallyAddPreview(Activity activity, List<Conversation> conversations, int unreadCount) throws Exception {
        if (shouldDisplayPreview(activity, conversations)) {
            evaluatePreviewType(activity, conversations.get(0), unreadCount);
        }
    }

    public void conditionallyRemovePreview(Activity activity) throws Exception {
        View root = activity.findViewById(R.id.previewRoot);
        if (root != null) {
            ((ViewGroup) root.getParent()).removeView(root);
        }
    }

    public void displayFirstMessageVisuals(Activity activity) throws Exception {
        View view = activity.getLayoutInflater().inflate(R.layout.intercomsdk_onboarding_layout, (ViewGroup) null);
        Toast toast = new Toast(Bridge.getContext());
        toast.setGravity(17, 0, 0);
        toast.setDuration(1);
        toast.setView(view);
        toast.show();
    }

    public void reset(Activity activity) {
        this.currentlyDisplayedConversation = null;
        try {
            conditionallyRemovePreview(activity);
        } catch (Exception e) {
        }
    }

    private synchronized void evaluatePreviewType(Activity activity, Conversation conversation, int unreadCount) {
        String messageStyle = conversation.getLastPart().getMessageStyle();
        if (Part.ANNOUNCEMENT_MESSAGE_STYLE.equals(messageStyle) || Part.SMALL_ANNOUNCEMENT_MESSAGE_STYLE.equals(messageStyle)) {
            goToConversation(conversation, unreadCount);
        } else if (Part.CHAT_MESSAGE_STYLE.equals(messageStyle)) {
            displayNotificationPreview(activity, conversation, unreadCount);
            this.currentlyDisplayedConversation = conversation;
        }
    }

    private void displayNotificationPreview(Activity activity, Conversation conversation, int unreadCount) {
        boolean shouldFadeInPreview = shouldFadeInPreview(activity);
        View root = initViewIfNecessary(activity);
        displayChatHead(root, conversation, unreadCount);
        if (shouldPlaySound(conversation)) {
            IntercomLogger.INTERNAL("preview", "playing preview sound");
            playSound(activity);
        }
        if (shouldFadeInPreview) {
            IntercomLogger.INTERNAL("preview", "fading in preview");
            PreviewAnimation.fadeInPreview(root.findViewById(R.id.avatar), root.findViewById(R.id.indicator_text), root.findViewById(R.id.avatar_shadow));
        }
        if (shouldShowTextPreview(conversation)) {
            IntercomLogger.INTERNAL("preview", "showing text preview");
            displayTextPreview(activity, (TextView) root.findViewById(R.id.notification_text), conversation, unreadCount);
        }
    }

    private View initViewIfNecessary(Activity activity) {
        View root = activity.findViewById(R.id.previewRoot);
        if (root == null) {
            root = activity.getLayoutInflater().inflate(R.layout.intercomsdk_preview, (ViewGroup) null, false);
            if (this.previewPosition.isLeftAligned()) {
                View v = root.findViewById(R.id.notification_text);
                v.setBackgroundResource(R.drawable.intercomsdk_preview_bubble_left);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v.getLayoutParams();
                layoutParams.leftMargin = ScreenUtils.convertDpToPixel(8.0f, activity);
                layoutParams.addRule(1, R.id.avatar_container);
            } else {
                View v2 = root.findViewById(R.id.notification_text);
                v2.setBackgroundResource(R.drawable.intercomsdk_preview_bubble_right);
                RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) v2.getLayoutParams();
                layoutParams2.rightMargin = ScreenUtils.convertDpToPixel(8.0f, activity);
                layoutParams2.addRule(0, R.id.avatar_container);
            }
            activity.addContentView(root, new RelativeLayout.LayoutParams(-1, -1));
        }
        return root;
    }

    private void displayChatHead(View root, final Conversation conversation, final int unreadCount) {
        View avatarContainer = root.findViewById(R.id.avatar_container);
        TextView indicatorText = (TextView) root.findViewById(R.id.indicator_text);
        this.previewPosition.setAlignment((RelativeLayout.LayoutParams) avatarContainer.getLayoutParams());
        AvatarUtils.createAvatar(conversation.getLastAdmin().isAdmin(), conversation.getLastAdmin().getAvatar(), (ImageView) root.findViewById(R.id.avatar), root.getContext());
        indicatorText.setText(String.valueOf(unreadCount));
        FontUtils.setTypeface(indicatorText, FontUtils.ROBOTO_MEDIUM, root.getContext());
        avatarContainer.setOnTouchListener(new View.OnTouchListener() {
            private int initialHeight;
            private int initialWidth;
            private int initialX;
            private int initialY;

            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case 0:
                        int[] location = new int[2];
                        view.getLocationOnScreen(location);
                        this.initialX = location[0];
                        this.initialY = location[1];
                        this.initialWidth = view.getWidth();
                        this.initialHeight = view.getHeight();
                        view.setScaleX(PreviewDisplayManager.PREVIEW_TOUCHED_SCALE);
                        view.setScaleY(PreviewDisplayManager.PREVIEW_TOUCHED_SCALE);
                        break;
                    case 1:
                        if (PreviewDisplayManager.isTouchInViewBounds(event.getRawX(), event.getRawY(), this.initialX, this.initialY, this.initialWidth, this.initialHeight)) {
                            view.setScaleX(1.0f);
                            view.setScaleY(1.0f);
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    PreviewDisplayManager.this.goToConversation(conversation, unreadCount);
                                }
                            }, 200);
                            break;
                        }
                        break;
                    default:
                        if (!PreviewDisplayManager.isTouchInViewBounds(event.getRawX(), event.getRawY(), this.initialX, this.initialY, this.initialWidth, this.initialHeight)) {
                            view.setScaleX(1.0f);
                            view.setScaleY(1.0f);
                            break;
                        } else {
                            view.setScaleX(PreviewDisplayManager.PREVIEW_TOUCHED_SCALE);
                            view.setScaleY(PreviewDisplayManager.PREVIEW_TOUCHED_SCALE);
                            break;
                        }
                }
                return true;
            }
        });
        IntercomLogger.INTERNAL("preview", "displayed preview");
    }

    private void displayTextPreview(Activity activity, final TextView notificationTextView, final Conversation conversation, final int unreadCount) {
        String lastAdminText = conversation.getLastAdminPart().getSummary();
        if (!lastAdminText.isEmpty()) {
            notificationTextView.setText(lastAdminText);
        } else {
            notificationTextView.setText(R.string.intercomsdk_image_attached);
        }
        FontUtils.setTypeface(notificationTextView, FontUtils.ROBOTO_MEDIUM, activity);
        notificationTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PreviewDisplayManager.this.goToConversation(conversation, unreadCount);
            }
        });
        notificationTextView.setVisibility(0);
        notificationTextView.getBackground().setColorFilter(Color.parseColor(Bridge.getIdentityStore().getAppConfig().getBaseColor()), PorterDuff.Mode.MULTIPLY);
        final float initialTranslationX = notificationTextView.getTranslationX();
        AnimatorSet set = PreviewAnimation.animateTextPreview((View) notificationTextView, this.previewPosition.isLeftAligned());
        set.addListener(new Animator.AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationCancel(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                IntercomLogger.INTERNAL("anim", "target visibility before: " + notificationTextView.getVisibility());
                notificationTextView.setVisibility(8);
                IntercomLogger.INTERNAL("anim", "target visibility after: " + notificationTextView.getVisibility());
                IntercomLogger.INTERNAL("anim", "target translationX before: " + notificationTextView.getTranslationX());
                notificationTextView.setTranslationX(initialTranslationX);
                IntercomLogger.INTERNAL("anim", "target translationX after: " + notificationTextView.getTranslationX());
            }
        });
        set.start();
    }

    private void playSound(Context context) {
        SoundPool soundPool = new SoundPool(1, 5, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(sampleId, 1.0f, 1.0f, 1, 0, 1.0f);
            }
        });
        soundPool.load(context, R.raw.intercomsdk_birdy_done_1, 1);
    }

    /* access modifiers changed from: package-private */
    public boolean shouldDisplayPreview(Activity activity, List<Conversation> conversations) {
        boolean result = false;
        if (this.notificationPreviewVisible == 0 && activity != null && conversations != null && !conversations.isEmpty() && ((!conversations.get(0).equals(this.currentlyDisplayedConversation) || activity.findViewById(R.id.previewRoot) == null) && !IntercomUtils.isIntercomActivity(activity))) {
            result = true;
        }
        IntercomLogger.INTERNAL("preview", "should display preview: " + result);
        return result;
    }

    /* access modifiers changed from: package-private */
    public boolean shouldFadeInPreview(Activity activity) {
        return activity.findViewById(R.id.previewRoot) == null;
    }

    private boolean shouldPlaySound(Conversation conversation) {
        return !conversation.equals(this.currentlyDisplayedConversation) && Bridge.getIdentityStore().getAppConfig().isAudioEnabled();
    }

    private boolean shouldShowTextPreview(Conversation conversation) {
        return !conversation.equals(this.currentlyDisplayedConversation);
    }

    static boolean isTouchInViewBounds(float touchX, float touchY, int viewX, int viewY, int viewWidth, int viewHeight) {
        return touchX > ((float) viewX) && touchX < ((float) (viewX + viewWidth)) && touchY > ((float) viewY) && touchY < ((float) (viewY + viewHeight));
    }

    /* access modifiers changed from: private */
    public void goToConversation(Conversation conversation, int unreadCount) {
        Context context = Bridge.getContext();
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MainActivity.OPEN_CONVERSATION, conversation);
        intent.putExtra(ConversationFragment.ARG_UNREAD_COUNT, unreadCount);
        intent.putExtra(ConversationFragment.ARG_IS_READ, false);
        intent.setFlags(268435456);
        context.startActivity(intent);
    }
}
