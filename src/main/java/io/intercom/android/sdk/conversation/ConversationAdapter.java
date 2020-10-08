package io.intercom.android.sdk.conversation;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.models.Part;
import io.intercom.android.sdk.models.Participant;
import io.intercom.android.sdk.utilities.AvatarUtils;
import io.intercom.android.sdk.utilities.BackgroundUtils;
import io.intercom.android.sdk.utilities.ScreenUtils;
import io.intercom.android.sdk.utilities.TimeUtils;
import io.intercom.android.sdk.views.AdminIsTypingView;
import java.util.List;
import java.util.concurrent.TimeUnit;

@TargetApi(14)
public class ConversationAdapter extends ArrayAdapter<Part> {
    private static final int ADMIN_IS_TYPING = 7;
    private static final int ADMIN_MESSAGE = 1;
    private static final int ADMIN_MESSAGE_FIRST = 2;
    private static final int ALT_USER_MESSAGE = 6;
    private static final int ANNOUNCEMENT_TYPE = 3;
    private static final int MAX_TYPE = 8;
    private static final int POWERED_BY = 4;
    private static final int UPLOAD_LEFT_PADDING_DP = 14;
    private static final int UPLOAD_RIGHT_PADDING_DP = 13;
    private static final int USER_MESSAGE = 0;
    private static final int WELCOME = 5;

    public ConversationAdapter(Context context, int layoutResourceId, List<Part> conversationParts) {
        super(context, layoutResourceId, conversationParts);
    }

    static class ConversationHolder {
        LinearLayout blocks;
        LinearLayout cellLayout;
        ImageView networkAvatar;
        TextView time;

        ConversationHolder() {
        }
    }

    public View getBlockView(int position, View row, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (isWithinBounds(position)) {
            int type = getItemViewType(position);
            Part conversationPart = (Part) getItem(position);
            if (type == 4) {
                row = inflater.inflate(R.layout.intercomsdk_row_poweredby, parent, false);
                checkForEntranceAnimation(row, type, conversationPart);
            } else if (type == 5) {
                row = inflater.inflate(R.layout.intercomsdk_row_welcome, parent, false);
                setUpWelcomeRow(row, conversationPart);
            } else {
                boolean shouldConcatenate = shouldConcatenate(conversationPart, position);
                conversationPart.setShowCreatedAt(conversationPart.showCreatedAt() && !shouldConcatenate);
                ConversationHolder holder = new ConversationHolder();
                if (row == null || !(row.getTag() instanceof ConversationHolder)) {
                    row = setUpRow(inflater, parent, type, shouldConcatenate);
                    holder.time = (TextView) row.findViewById(R.id.rowTime);
                    holder.networkAvatar = (ImageView) row.findViewById(R.id.avatarView);
                    holder.cellLayout = (LinearLayout) row.findViewById(R.id.cellLayout);
                    holder.blocks = conversationPart.getLayout();
                    if (holder.blocks.getParent() != null) {
                        ((LinearLayout) holder.blocks.getParent()).removeView(holder.blocks);
                    }
                    holder.cellLayout.addView(holder.blocks, 0);
                    row.setFocusable(false);
                    row.setTag(holder);
                } else {
                    holder = (ConversationHolder) row.getTag();
                }
                setUpHolderBlocks(holder, type, row, conversationPart, shouldConcatenate);
                setupHolderBackground(holder, type, conversationPart, position, shouldConcatenate);
                checkForEntranceAnimation(holder, type, conversationPart);
            }
        }
        if (row == null) {
            return inflater.inflate(R.layout.intercomsdk_row_welcome, parent, false);
        }
        return row;
    }

    public View getView(int position, View row, ViewGroup parent) {
        return getBlockView(position, row, parent);
    }

    public int getViewTypeCount() {
        return 8;
    }

    public int getItemViewType(int position) {
        int type = 0;
        if (isWithinBounds(position)) {
            Part conversationPart = (Part) getItem(position);
            if (Part.POWERED_BY_STYLE.equals(conversationPart.getMessageStyle())) {
                return 4;
            }
            if (Part.WELCOME_MESSAGE_STYLE.equals(conversationPart.getMessageStyle())) {
                return 5;
            }
            if (Part.ADMIN_IS_TYPING_STYLE.equals(conversationPart.getMessageStyle())) {
                return 7;
            }
            Participant user = conversationPart.getParticipant();
            if (Participant.ADMIN_TYPE.equals(user.getType())) {
                type = (Part.ANNOUNCEMENT_MESSAGE_STYLE.equals(conversationPart.getMessageStyle()) || Part.SMALL_ANNOUNCEMENT_MESSAGE_STYLE.equals(conversationPart.getMessageStyle())) ? 3 : conversationPart.isFirstChatPart() ? 2 : 1;
            } else if (!user.getId().equals(Bridge.getIdentityStore().getIntercomId())) {
                type = 6;
            }
        }
        return type;
    }

    public boolean isEnabled(int position) {
        if (!isWithinBounds(position)) {
            return false;
        }
        Part conversationPart = (Part) getItem(position);
        if (Part.MessageState.FAILED == conversationPart.getMessageState() || Part.MessageState.UPLOAD_FAILED == conversationPart.getMessageState() || Part.POWERED_BY_STYLE.equals(conversationPart.getMessageStyle())) {
            return true;
        }
        return false;
    }

    private boolean shouldConcatenate(Part conversationPart, int position) {
        if (position + 1 >= getCount()) {
            return false;
        }
        Part nextPart = (Part) getItem(position + 1);
        if (Math.abs(nextPart.getCreatedAt() - conversationPart.getCreatedAt()) >= TimeUnit.MINUTES.toSeconds(1) || !conversationPart.getParticipantId().equals(nextPart.getParticipantId()) || nextPart.getCreatedAt() == 0) {
            return false;
        }
        return true;
    }

    private View setUpRow(LayoutInflater inflater, ViewGroup parent, int type, boolean isConcat) {
        if (type == 1 || type == 6 || type == 7) {
            return inflater.inflate(R.layout.intercomsdk_row_admin_part, parent, false);
        }
        if (type == 2) {
            return inflater.inflate(R.layout.intercomsdk_row_admin_part_first, parent, false);
        }
        if (type == 3) {
            return inflater.inflate(R.layout.intercomsdk_row_big_announcement, parent, false);
        }
        if (isConcat) {
            return inflater.inflate(R.layout.intercomsdk_row_user_part_concat, parent, false);
        }
        return inflater.inflate(R.layout.intercomsdk_row_user_part, parent, false);
    }

    private void setUpHolderBlocks(ConversationHolder holder, int type, View row, Part conversationPart, boolean shouldConcatenate) {
        holder.cellLayout.setVisibility(0);
        holder.blocks = conversationPart.getLayout();
        if (holder.blocks.getParent() != null) {
            ((LinearLayout) holder.blocks.getParent()).removeView(holder.blocks);
        }
        if (holder.cellLayout.getChildCount() > 0) {
            holder.cellLayout.removeAllViews();
        }
        holder.cellLayout.addView(holder.blocks, 0);
        setUpRowFocusRules(type, holder, row, conversationPart);
        setUpRowTime(type, holder, conversationPart, shouldConcatenate);
    }

    private void setUpRowFocusRules(int type, ConversationHolder holder, View row, Part conversationPart) {
        holder.blocks.setAlpha(1.0f);
        if (Part.MessageState.FAILED == conversationPart.getMessageState() || Part.MessageState.UPLOAD_FAILED == conversationPart.getMessageState()) {
            ((ViewGroup) row).setDescendantFocusability(393216);
            holder.blocks.setAlpha(0.5f);
        } else if (Part.MessageState.SENDING == conversationPart.getMessageState()) {
            ((ViewGroup) row).setDescendantFocusability(393216);
        } else {
            ((ViewGroup) row).setDescendantFocusability(262144);
        }
    }

    private void setUpRowTime(int type, ConversationHolder holder, Part conversationPart, boolean shouldConcatenate) {
        if (holder.time == null) {
            return;
        }
        if (conversationPart.showCreatedAt()) {
            holder.time.setVisibility(0);
            holder.time.setPadding(0, 0, 0, ScreenUtils.convertDpToPixel(10.0f, getContext()));
            if (Part.MessageState.SENDING == conversationPart.getMessageState()) {
                holder.time.setText(conversationPart.getFooter());
                holder.time.setTextColor(getContext().getResources().getColor(R.color.intercomsdk_light_grey_colour));
            } else if (Part.MessageState.FAILED == conversationPart.getMessageState() || Part.MessageState.UPLOAD_FAILED == conversationPart.getMessageState()) {
                holder.time.setText(conversationPart.getFooter());
                holder.time.setTextColor(getContext().getResources().getColor(R.color.intercomsdk_red));
            } else {
                displayDelivered(holder, conversationPart);
                holder.time.setTextColor(getContext().getResources().getColor(R.color.intercomsdk_light_grey_colour));
            }
        } else if (shouldConcatenate || !(type == 3 || type == 2)) {
            holder.time.setVisibility(8);
        } else {
            holder.time.setVisibility(4);
            holder.time.setHeight(ScreenUtils.convertDpToPixel(10.0f, getContext()));
        }
    }

    private void setupHolderBackground(ConversationHolder holder, int type, Part conversationPart, int position, boolean shouldConcatenate) {
        int pLeft = holder.blocks.getPaddingLeft();
        int pRight = holder.blocks.getPaddingRight();
        int pTop = holder.blocks.getPaddingTop();
        int pBottom = holder.blocks.getPaddingBottom();
        if (conversationPart.isImageOnly()) {
            holder.blocks.setBackgroundResource(0);
            pLeft = 0;
            pTop = 0;
            pBottom = 0;
            if (conversationPart.isAdmin()) {
                if (!shouldConcatenate) {
                    showAvatar(holder, position, conversationPart.getParticipant());
                }
                pLeft = 14;
            } else {
                pRight = 13;
            }
        } else if ((type == 0 || Part.MessageState.SENDING == conversationPart.getMessageState() || Part.MessageState.FAILED == conversationPart.getMessageState() || Part.MessageState.UPLOAD_FAILED == conversationPart.getMessageState()) && shouldConcatenate) {
            BackgroundUtils.setBackground(holder.blocks, getContext().getResources().getDrawable(R.drawable.intercomsdk_chat_bubble_right_aligned_notail));
            setCellRowColor(holder);
        } else if (type != 1 || !shouldConcatenate) {
            if (type == 2) {
                BackgroundUtils.setBackground(holder.blocks, getContext().getResources().getDrawable(R.drawable.intercomsdk_chat_bubble_left_aligned));
            }
            if (Participant.ADMIN_TYPE.equals(conversationPart.getParticipant().getType()) || type == 6) {
                showAvatar(holder, position, conversationPart.getParticipant());
            } else {
                setCellRowColor(holder);
            }
        } else {
            BackgroundUtils.setBackground(holder.blocks, getContext().getResources().getDrawable(R.drawable.intercomsdk_chat_bubble_left_aligned_notail));
            holder.networkAvatar.setVisibility(4);
        }
        holder.blocks.setPadding(pLeft, pTop, pRight, pBottom);
    }

    private void checkForEntranceAnimation(ConversationHolder holder, int type, Part part) {
        if (!part.hasEntranceAnimation()) {
            return;
        }
        if (type == 7) {
            part.setEntranceAnimation(false);
            holder.networkAvatar.setAlpha(0.0f);
            holder.networkAvatar.setScaleX(0.5f);
            holder.networkAvatar.setScaleY(0.5f);
            holder.networkAvatar.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(300).setStartDelay(100).start();
            holder.cellLayout.setAlpha(0.0f);
            holder.cellLayout.setScaleX(0.5f);
            holder.cellLayout.setScaleY(0.5f);
            holder.cellLayout.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(300).setStartDelay(150).start();
            ((AdminIsTypingView) holder.blocks.getChildAt(0)).beginAnimation();
        } else if (Part.MessageState.SENDING == part.getMessageState()) {
            part.setEntranceAnimation(false);
            holder.cellLayout.setAlpha(0.0f);
            holder.cellLayout.setTranslationY(holder.cellLayout.getTranslationY() + 100.0f);
            holder.time.setAlpha(0.0f);
            holder.time.setTranslationY(holder.time.getTranslationY() + 100.0f);
            holder.cellLayout.animate().setStartDelay(100).alpha(1.0f).translationYBy(-100.0f).start();
            holder.time.animate().setStartDelay(100).alpha(1.0f).translationYBy(-100.0f).start();
        }
    }

    private void checkForEntranceAnimation(View view, int type, Part conversationPart) {
        if (conversationPart.hasEntranceAnimation() && type == 4) {
            ObjectAnimator animationSet = (ObjectAnimator) AnimatorInflater.loadAnimator(getContext(), R.animator.intercomsdk_fade_in_row);
            animationSet.setTarget(view);
            animationSet.start();
            conversationPart.setEntranceAnimation(false);
        }
    }

    private void displayDelivered(ConversationHolder holder, Part conversationPart) {
        if (conversationPart.isDisplayDelivered()) {
            holder.time.setText(getContext().getString(R.string.intercomsdk_delivered));
        } else {
            holder.time.setText(new TimeUtils().getFormattedTime(conversationPart.getCreatedAt(), getContext()));
        }
    }

    private void setUpWelcomeRow(View row, Part conversationPart) {
        LinearLayout cellLayout = (LinearLayout) row.findViewById(R.id.cellLayout);
        LinearLayout blocks = conversationPart.getLayout();
        if (blocks.getParent() != null) {
            ((LinearLayout) blocks.getParent()).removeView(blocks);
        }
        cellLayout.addView(blocks, 0);
        row.setFocusable(false);
    }

    private void setCellRowColor(ConversationHolder holder) {
        Drawable bg = holder.blocks.getBackground();
        bg.setColorFilter(new PorterDuffColorFilter(Color.parseColor(Bridge.getIdentityStore().getAppConfig().getBaseColor()), PorterDuff.Mode.MULTIPLY));
        BackgroundUtils.setBackground(holder.blocks, bg);
    }

    private void showAvatar(ConversationHolder holder, int position, Participant user) {
        if (holder.networkAvatar != null) {
            holder.networkAvatar.setTag(Integer.valueOf(position));
            holder.networkAvatar.setVisibility(0);
            AvatarUtils.createAvatar(user.isAdmin(), user.getAvatar(), holder.networkAvatar, getContext());
        }
    }

    private boolean isWithinBounds(int position) {
        return position >= 0 && position < getCount();
    }
}
