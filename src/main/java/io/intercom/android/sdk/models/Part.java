package io.intercom.android.sdk.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.LinearLayout;
import io.intercom.android.sdk.annotations.Exclude;
import io.intercom.android.sdk.blocks.BlockType;
import io.intercom.android.sdk.blocks.models.Block;
import io.intercom.android.sdk.models.Attachments;
import io.intercom.android.sdk.models.LWR;
import io.intercom.android.sdk.models.Participant;
import java.util.ArrayList;
import java.util.List;

public class Part implements Parcelable {
    public static final String ADMIN_IS_TYPING_STYLE = "admin_is_typing_style";
    public static final String ANNOUNCEMENT_MESSAGE_STYLE = "announcement";
    public static final String CHAT_MESSAGE_STYLE = "chat";
    public static final Parcelable.Creator<Part> CREATOR = new Parcelable.Creator<Part>() {
        public Part createFromParcel(Parcel in) {
            return new Part(in);
        }

        public Part[] newArray(int size) {
            return new Part[size];
        }
    };
    public static final String POWERED_BY_STYLE = "intercom_powered_by";
    public static final String SMALL_ANNOUNCEMENT_MESSAGE_STYLE = "small-announcement";
    public static final String WELCOME_MESSAGE_STYLE = "intercom_welcome_message";
    private List<Attachments> attachments;
    private List<Block> blocks;
    private long createdAt;
    private boolean displayDelivered;
    private boolean entranceAnimation;
    private Uri fileUri;
    private boolean firstChatPart;
    private String footer;
    private String id;
    private boolean isImageOnly;
    @Exclude
    private LinearLayout layout;
    private LWR lightweightReply;
    private MessageState messageState;
    private String messageStyle;
    private Participant participant;
    private String participantId;
    private boolean participantIsAdmin;
    private boolean showCreatedAt;
    private String summary;

    public enum MessageState {
        SENDING,
        FAILED,
        UPLOAD_FAILED,
        NORMAL
    }

    public Part() {
        this(new Builder());
    }

    private Part(Builder builder) {
        boolean z;
        this.id = builder.id == null ? "" : builder.id;
        this.participantId = builder.participant_id == null ? "" : builder.participant_id;
        this.participantIsAdmin = builder.participant_is_admin;
        this.showCreatedAt = builder.show_created_at;
        this.summary = builder.summary == null ? "" : builder.summary;
        this.createdAt = builder.created_at;
        this.messageStyle = builder.message_style == null ? "" : builder.message_style;
        this.blocks = new ArrayList();
        if (builder.body != null) {
            for (Block.Builder blockBuilder : builder.body) {
                this.blocks.add(blockBuilder.build());
            }
        }
        this.lightweightReply = builder.lightweight_reply == null ? new LWR.NullLWR() : builder.lightweight_reply.build();
        this.attachments = new ArrayList();
        if (builder.attachments != null) {
            for (Attachments.Builder attachmentBuilder : builder.attachments) {
                this.attachments.add(attachmentBuilder.build());
            }
        }
        this.footer = "";
        this.participant = new Participant.NullParticipant();
        this.footer = "";
        this.displayDelivered = false;
        this.entranceAnimation = false;
        this.firstChatPart = false;
        this.messageState = MessageState.NORMAL;
        if (this.blocks.size() == 1 && this.blocks.get(0).getType() == BlockType.IMAGE) {
            z = true;
        } else {
            z = false;
        }
        this.isImageOnly = z;
        this.fileUri = new Uri.Builder().build();
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id2) {
        this.id = id2;
    }

    public String getParticipantId() {
        return this.participantId;
    }

    public boolean showCreatedAt() {
        return this.showCreatedAt;
    }

    public void setShowCreatedAt(boolean showCreatedAt2) {
        this.showCreatedAt = showCreatedAt2;
    }

    public boolean isAdmin() {
        return this.participantIsAdmin;
    }

    public List<Block> getBlocks() {
        return this.blocks;
    }

    public void setBlocks(List<Block> blocks2) {
        this.blocks = blocks2;
    }

    public List<Attachments> getAttachments() {
        return this.attachments;
    }

    public void setAttachments(List<Attachments> attachments2) {
        this.attachments = attachments2;
    }

    public String getMessageStyle() {
        return this.messageStyle;
    }

    public void setMessageStyle(String messageStyle2) {
        this.messageStyle = messageStyle2;
    }

    public long getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(long createdAt2) {
        this.createdAt = createdAt2;
    }

    public String getSummary() {
        return this.summary;
    }

    public LWR getLightweightReply() {
        return this.lightweightReply;
    }

    public Participant getParticipant() {
        return this.participant;
    }

    public void setParticipant(Participant participant2) {
        this.participant = participant2;
        this.participantId = participant2.getId();
    }

    public LinearLayout getLayout() {
        return this.layout;
    }

    public void setLayout(LinearLayout layout2) {
        this.layout = layout2;
    }

    public String getFooter() {
        return this.footer;
    }

    public void setFooter(String footer2) {
        this.footer = footer2;
    }

    public boolean isDisplayDelivered() {
        return this.displayDelivered;
    }

    public void setDisplayDelivered(boolean displayDelivered2) {
        this.displayDelivered = displayDelivered2;
    }

    public boolean hasEntranceAnimation() {
        return this.entranceAnimation;
    }

    public void setEntranceAnimation(boolean entranceAnimation2) {
        this.entranceAnimation = entranceAnimation2;
    }

    public boolean isFirstChatPart() {
        return this.firstChatPart;
    }

    public void setFirstChatPart(boolean firstChatPart2) {
        this.firstChatPart = firstChatPart2;
    }

    public MessageState getMessageState() {
        return this.messageState;
    }

    public void setMessageState(MessageState messageState2) {
        this.messageState = messageState2;
    }

    public boolean isImageOnly() {
        return this.isImageOnly;
    }

    public void isImageOnly(boolean isImageOnly2) {
        this.isImageOnly = isImageOnly2;
    }

    public Uri getFileUri() {
        return this.fileUri;
    }

    public void setFileUri(Uri fileUri2) {
        this.fileUri = fileUri2;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public List<Attachments.Builder> attachments;
        /* access modifiers changed from: private */
        public List<Block.Builder> body;
        /* access modifiers changed from: private */
        public long created_at;
        /* access modifiers changed from: private */
        public String id;
        /* access modifiers changed from: private */
        public LWR.Builder lightweight_reply;
        /* access modifiers changed from: private */
        public String message_style;
        /* access modifiers changed from: private */
        public String participant_id;
        /* access modifiers changed from: private */
        public boolean participant_is_admin;
        /* access modifiers changed from: private */
        public boolean show_created_at;
        /* access modifiers changed from: private */
        public String summary;

        public Part build() {
            return new Part(this);
        }
    }

    public static final class NullPart extends Part {
        public NullPart() {
            super(new Builder());
        }
    }

    protected Part(Parcel in) {
        boolean z;
        boolean isNullLwr;
        boolean z2 = true;
        this.id = in.readString();
        this.participantId = in.readString();
        if (in.readByte() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.participantIsAdmin = z;
        if (in.readByte() == 1) {
            this.blocks = new ArrayList();
            in.readList(this.blocks, Block.class.getClassLoader());
        } else {
            this.blocks = null;
        }
        if (in.readByte() == 1) {
            this.attachments = new ArrayList();
            in.readList(this.attachments, Attachments.class.getClassLoader());
        } else {
            this.attachments = null;
        }
        this.messageStyle = in.readString();
        this.createdAt = in.readLong();
        this.summary = in.readString();
        if (in.readByte() == 0) {
            isNullLwr = true;
        } else {
            isNullLwr = false;
        }
        if (isNullLwr) {
            this.lightweightReply = new LWR.NullLWR();
        } else {
            this.lightweightReply = (LWR) in.readValue(LWR.class.getClassLoader());
        }
        this.participant = (Participant) in.readValue(Participant.class.getClassLoader());
        this.footer = in.readString();
        this.showCreatedAt = in.readByte() == 0 ? false : z2;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i = 1;
        dest.writeString(this.id);
        dest.writeString(this.participantId);
        dest.writeByte((byte) (this.participantIsAdmin ? 1 : 0));
        if (this.blocks == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeList(this.blocks);
        }
        if (this.attachments == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeList(this.attachments);
        }
        dest.writeString(this.messageStyle);
        dest.writeLong(this.createdAt);
        dest.writeString(this.summary);
        if (this.lightweightReply instanceof LWR.NullLWR) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeValue(this.lightweightReply);
        }
        dest.writeValue(this.participant);
        dest.writeString(this.footer);
        if (!this.showCreatedAt) {
            i = 0;
        }
        dest.writeByte((byte) i);
    }

    public int hashCode() {
        return this.id.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof Part) {
            return this.id.equals(((Part) o).id);
        }
        return false;
    }
}
