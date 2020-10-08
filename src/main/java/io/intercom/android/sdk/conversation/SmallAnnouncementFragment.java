package io.intercom.android.sdk.conversation;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.blocks.BlockType;
import io.intercom.android.sdk.blocks.Blocks;
import io.intercom.android.sdk.blocks.BlocksViewHolder;
import io.intercom.android.sdk.blocks.LightWeightReply;
import io.intercom.android.sdk.blocks.ViewHolderGenerator;
import io.intercom.android.sdk.blocks.models.Block;
import io.intercom.android.sdk.blocks.models.BlockAttachment;
import io.intercom.android.sdk.interfaces.LWRListener;
import io.intercom.android.sdk.interfaces.OnSmallAnnouncementInteractionListener;
import io.intercom.android.sdk.models.Attachments;
import io.intercom.android.sdk.models.Events.CloseIAMEvent;
import io.intercom.android.sdk.models.Events.ReadEvent;
import io.intercom.android.sdk.models.LWR;
import io.intercom.android.sdk.models.Part;
import io.intercom.android.sdk.models.Participant;
import io.intercom.android.sdk.utilities.AvatarUtils;
import io.intercom.android.sdk.utilities.ScreenUtils;
import java.util.ArrayList;
import java.util.List;

@TargetApi(15)
public class SmallAnnouncementFragment extends Fragment implements View.OnClickListener, LWRListener {
    private static final int ANIMATION_OFFSET_DP = 200;
    private static final int ANIMATION_TIME = 300;
    private static final String ARG_CONVERSATION_ID = "conversationArg";
    private static final String ARG_PART = "partArg";
    private static final int LWR_HEIGHT_DP = 55;
    private static final int MAX_HEIGHT_DP = 290;
    private LinearLayout announcementView;
    private Blocks blocks;
    private BlocksViewHolder blocksAnnouncementViewHolder;
    private ImageButton closeButton;
    private String conversationId;
    private List<Part> conversationParts;
    private View fadeView;
    private ViewHolderGenerator generator;
    private OnSmallAnnouncementInteractionListener mListener;
    private View rootView;
    private View roundBottom;
    /* access modifiers changed from: private */
    public Part smallAnnouncement;
    private View touchInterceptor;

    public static SmallAnnouncementFragment newInstance(String conversationId2, Part part) {
        SmallAnnouncementFragment fragment = new SmallAnnouncementFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CONVERSATION_ID, conversationId2);
        args.putParcelable(ARG_PART, part);
        fragment.setArguments(args);
        return fragment;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnSmallAnnouncementInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnSmallAnnouncementInteractionListener");
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bridge.init(getActivity().getApplication());
        this.conversationParts = new ArrayList();
        Bundle args = getArguments();
        if (args != null) {
            this.generator = new ViewHolderGenerator(getActivity());
            this.blocksAnnouncementViewHolder = this.generator.getAnnouncementPreviewHolder(this);
            this.smallAnnouncement = (Part) args.getParcelable(ARG_PART);
            this.conversationId = args.getString(ARG_CONVERSATION_ID);
            this.conversationParts.add(this.smallAnnouncement);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.rootView == null) {
            this.rootView = inflater.inflate(R.layout.intercomsdk_fragment_small_announcement, container, false);
        } else {
            ViewGroup parent = (ViewGroup) this.rootView.getParent();
            if (parent != null) {
                parent.removeView(this.rootView);
            }
        }
        this.touchInterceptor = this.rootView.findViewById(R.id.touchInterceptor);
        this.blocks = new Blocks(getActivity());
        this.closeButton = (ImageButton) this.rootView.findViewById(R.id.close_button);
        this.closeButton.setOnClickListener(this);
        this.announcementView = (LinearLayout) this.rootView.findViewById(R.id.announcement);
        this.fadeView = this.rootView.findViewById(R.id.white_fade);
        this.roundBottom = this.rootView.findViewById(R.id.round_bottom);
        displaySmallAnnouncement();
        return this.rootView;
    }

    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        if (enter) {
            triggerEntranceAnimation();
        }
        return super.onCreateAnimator(transit, enter, nextAnim);
    }

    private void displaySmallAnnouncement() {
        this.rootView.setOnClickListener(this);
        createSmallAnnouncementUI();
        ((ImageButton) this.rootView.findViewById(R.id.close_button)).setOnClickListener(this);
        final View view = getBlockView(this.announcementView);
        this.announcementView.addView(view);
        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                SmallAnnouncementFragment.this.addBottomBorder(SmallAnnouncementFragment.this.smallAnnouncement, view);
                SmallAnnouncementFragment.this.setUpTouchInterceptor(view);
            }
        });
        Bridge.getApi().markConversationAsRead(this.conversationId);
        Bridge.getBus().post(new ReadEvent(this.conversationId));
    }

    private void createSmallAnnouncementUI() {
        List<Block> blockParts = this.smallAnnouncement.getBlocks();
        if (!this.smallAnnouncement.getAttachments().isEmpty()) {
            List<BlockAttachment> blockAttachments = new ArrayList<>();
            for (Attachments attachment : this.smallAnnouncement.getAttachments()) {
                blockAttachments.add(new BlockAttachment.Builder().withName(attachment.getName()).withUrl(attachment.getUrl()).build());
            }
            blockParts.add(new Block.Builder().withType(BlockType.ATTACHMENTLIST.name()).withAttachments(blockAttachments).build());
        }
        if (!(this.smallAnnouncement.getLightweightReply() instanceof LWR.NullLWR)) {
            blockParts.add(new Block.Builder().withType(BlockType.LWR.name()).withText(this.smallAnnouncement.getLightweightReply().getType()).build());
            ((LightWeightReply) this.blocksAnnouncementViewHolder.getLwr()).setLwrObject(this.smallAnnouncement.getLightweightReply());
        }
        if (!blockParts.isEmpty()) {
            this.smallAnnouncement.setLayout(this.blocks.createBlocks(blockParts, this.blocksAnnouncementViewHolder));
        }
    }

    private View getBlockView(ViewGroup parent) {
        View row = LayoutInflater.from(getActivity()).inflate(R.layout.intercomsdk_row_small_announcement, parent, false);
        ImageView networkAvatar = (ImageView) row.findViewById(R.id.avatarView);
        LinearLayout cellLayout = (LinearLayout) row.findViewById(R.id.cellLayout);
        LinearLayout blocks2 = this.smallAnnouncement.getLayout();
        if (blocks2.getParent() != null) {
            ((LinearLayout) blocks2.getParent()).removeView(blocks2);
        }
        if (cellLayout.getChildCount() > 0) {
            cellLayout.removeAllViews();
        }
        cellLayout.addView(blocks2, 0);
        row.setFocusable(false);
        cellLayout.setVisibility(0);
        Participant user = this.smallAnnouncement.getParticipant();
        networkAvatar.setVisibility(0);
        AvatarUtils.createAvatar(user.isAdmin(), user.getAvatar(), networkAvatar, getActivity());
        return row;
    }

    /* access modifiers changed from: private */
    public void addBottomBorder(Part smallAnnouncement2, View messageView) {
        LinearLayout content = (LinearLayout) messageView.findViewById(R.id.cell_content);
        if (this.fadeView.getVisibility() != 0) {
            if (ScreenUtils.convertPixelsToDp((float) content.getMeasuredHeight(), getActivity()) == MAX_HEIGHT_DP) {
                this.fadeView.setVisibility(0);
                this.roundBottom.setVisibility(0);
            } else {
                this.fadeView.setVisibility(4);
                this.roundBottom.setVisibility(4);
            }
            messageView.findViewById(R.id.buttonLayout).bringToFront();
        }
    }

    /* access modifiers changed from: private */
    public void setUpTouchInterceptor(View messageView) {
        View fadeView2 = this.rootView.findViewById(R.id.white_fade);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.touchInterceptor.getLayoutParams();
        params.height = ((LinearLayout) messageView.findViewById(R.id.cell_content)).getMeasuredHeight();
        if (fadeView2.getVisibility() != 0 && !(this.smallAnnouncement.getLightweightReply() instanceof LWR.NullLWR)) {
            params.height -= ScreenUtils.convertDpToPixel(55.0f, getActivity());
        }
        this.touchInterceptor.setLayoutParams(params);
        this.touchInterceptor.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.touchInterceptor) {
            transitionToFullConversation();
        } else if (v.getId() == R.id.close_button || v.getId() == R.id.announcementRootView) {
            Bridge.getBus().post(new CloseIAMEvent());
        }
    }

    private void transitionToFullConversation() {
        this.mListener.transitionToConversation(this.conversationId);
    }

    public void sendLWRResponse(LWR lwr) {
        Bridge.getApi().sendLWRResponse(this.conversationId, lwr.getType(), lwr.getOption());
    }

    private void triggerEntranceAnimation() {
        ObjectAnimator.ofFloat(this.rootView.findViewById(R.id.tinted_background), "alpha", new float[]{0.0f, 1.0f}).setDuration(300).start();
        animateIn(this.announcementView);
        animateIn(this.fadeView);
        animateIn(this.roundBottom);
        animateIn(this.closeButton);
    }

    private void animateIn(View view) {
        ObjectAnimator translateAnimator = ObjectAnimator.ofFloat(view, "translationY", new float[]{view.getY() + ((float) ScreenUtils.convertDpToPixel(200.0f, getActivity())), view.getY()});
        translateAnimator.setDuration(300);
        translateAnimator.start();
    }
}
