package io.intercom.android.sdk.conversation;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.blocks.BlockType;
import io.intercom.android.sdk.blocks.Blocks;
import io.intercom.android.sdk.blocks.BlocksViewHolder;
import io.intercom.android.sdk.blocks.LightWeightReply;
import io.intercom.android.sdk.blocks.ViewHolderGenerator;
import io.intercom.android.sdk.blocks.models.Block;
import io.intercom.android.sdk.blocks.models.BlockAttachment;
import io.intercom.android.sdk.interfaces.LWRListener;
import io.intercom.android.sdk.interfaces.OnConversationInteractionListener;
import io.intercom.android.sdk.models.Attachments;
import io.intercom.android.sdk.models.Events.CloseIAMEvent;
import io.intercom.android.sdk.models.LWR;
import io.intercom.android.sdk.models.Part;
import io.intercom.android.sdk.models.Participant;
import java.util.ArrayList;
import java.util.List;

@TargetApi(15)
public abstract class BaseConversationFragment extends Fragment implements LWRListener {
    protected ConversationAdapter adapter;
    protected boolean allowTextReply;
    protected Blocks blocks;
    private BlocksViewHolder blocksAdminViewHolder;
    private BlocksViewHolder blocksAnnouncementViewHolder;
    private BlocksViewHolder blocksLWRViewHolder;
    private BlocksViewHolder blocksUserViewHolder;
    protected String conversationId;
    protected List<Part> conversationParts;
    protected ViewHolderGenerator generator;
    protected OnConversationInteractionListener listener;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.listener = (OnConversationInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity + " must implement OnConversationInteractionListener");
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bridge.init(getActivity().getApplication());
        this.conversationParts = new ArrayList();
        this.generator = new ViewHolderGenerator(getActivity().getApplicationContext());
        this.blocksUserViewHolder = this.generator.getUserHolder();
        this.blocksAnnouncementViewHolder = this.generator.getAnnouncementHolder();
        this.blocksAdminViewHolder = this.generator.getAdminHolder(this);
        this.blocksLWRViewHolder = this.generator.getAnnouncementLWRHolder(this);
    }

    /* access modifiers changed from: protected */
    public Part createMessageUI(Part part) {
        Participant user = part.getParticipant();
        List<Block> blockParts = part.getBlocks();
        if (!part.getAttachments().isEmpty()) {
            List<BlockAttachment> blockAttachments = new ArrayList<>();
            for (Attachments attachment : part.getAttachments()) {
                blockAttachments.add(new BlockAttachment.Builder().withName(attachment.getName()).withUrl(attachment.getUrl()).withContentType(attachment.getContentType()).build());
            }
            blockParts.add(new Block.Builder().withType(BlockType.ATTACHMENTLIST.name()).withAttachments(blockAttachments).build());
        }
        if (!(part.getLightweightReply() instanceof LWR.NullLWR)) {
            blockParts.add(new Block.Builder().withType(BlockType.LWR.name()).withText(part.getLightweightReply().getType()).build());
            ((LightWeightReply) this.blocksLWRViewHolder.getLwr()).setLwrObject(part.getLightweightReply());
            ((LightWeightReply) this.blocksAdminViewHolder.getLwr()).setLwrObject(part.getLightweightReply());
            this.allowTextReply = false;
        } else {
            this.allowTextReply = true;
        }
        if (Participant.USER_TYPE.equals(user.getType()) && user.getId().equals(Bridge.getIdentityStore().getIntercomId())) {
            part.setLayout(this.blocks.createBlocks(blockParts, this.blocksUserViewHolder));
        } else if (!Part.ANNOUNCEMENT_MESSAGE_STYLE.equals(part.getMessageStyle()) && !Part.SMALL_ANNOUNCEMENT_MESSAGE_STYLE.equals(part.getMessageStyle())) {
            part.setLayout(this.blocks.createBlocks(blockParts, this.blocksAdminViewHolder));
        } else if (this.allowTextReply) {
            part.setLayout(this.blocks.createBlocks(blockParts, this.blocksAnnouncementViewHolder));
        } else {
            part.setLayout(this.blocks.createBlocks(blockParts, this.blocksLWRViewHolder));
        }
        return part;
    }

    public void sendLWRResponse(LWR lwr) {
        Bridge.getApi().sendLWRResponse(this.conversationId, lwr.getType(), lwr.getOption());
    }

    /* access modifiers changed from: protected */
    public void markAsRead() {
        Bridge.getApi().markConversationAsRead(this.conversationId);
    }

    /* access modifiers changed from: protected */
    public void closeTapped() {
        Bridge.getBus().post(new CloseIAMEvent());
    }
}
