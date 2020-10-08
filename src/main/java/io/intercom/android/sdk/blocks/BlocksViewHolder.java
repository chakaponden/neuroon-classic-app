package io.intercom.android.sdk.blocks;

import io.intercom.android.sdk.blocks.blockInterfaces.AttachmentListBlock;
import io.intercom.android.sdk.blocks.blockInterfaces.ButtonBlock;
import io.intercom.android.sdk.blocks.blockInterfaces.CodeBlock;
import io.intercom.android.sdk.blocks.blockInterfaces.FacebookBlock;
import io.intercom.android.sdk.blocks.blockInterfaces.HeadingBlock;
import io.intercom.android.sdk.blocks.blockInterfaces.ImageBlock;
import io.intercom.android.sdk.blocks.blockInterfaces.LightWeightReplyBlock;
import io.intercom.android.sdk.blocks.blockInterfaces.LocalAttachmentBlock;
import io.intercom.android.sdk.blocks.blockInterfaces.LocalImageBlock;
import io.intercom.android.sdk.blocks.blockInterfaces.OrderedListBlock;
import io.intercom.android.sdk.blocks.blockInterfaces.ParagraphBlock;
import io.intercom.android.sdk.blocks.blockInterfaces.SubheadngBlock;
import io.intercom.android.sdk.blocks.blockInterfaces.TwitterBlock;
import io.intercom.android.sdk.blocks.blockInterfaces.UnorderedListBlock;
import io.intercom.android.sdk.blocks.blockInterfaces.VideoBlock;

public class BlocksViewHolder {
    private AttachmentListBlock attachmentList;
    private ButtonBlock button;
    private CodeBlock code;
    private FacebookBlock facebookButton;
    private HeadingBlock heading;
    private ImageBlock image;
    private int layout;
    private LocalAttachmentBlock localAttachment;
    private LocalImageBlock localImage;
    private LightWeightReplyBlock lwr;
    private OrderedListBlock orderedList;
    private ParagraphBlock paragraph;
    private SubheadngBlock subheading;
    private TwitterBlock twitterButton;
    private UnorderedListBlock unorderedList;
    private VideoBlock video;

    public int getLayout() {
        return this.layout;
    }

    public void setLayout(int layout2) {
        this.layout = layout2;
    }

    public ParagraphBlock getParagraph() {
        return this.paragraph;
    }

    public void setParagraph(ParagraphBlock paragraph2) {
        this.paragraph = paragraph2;
    }

    public HeadingBlock getHeading() {
        return this.heading;
    }

    public void setHeading(HeadingBlock heading2) {
        this.heading = heading2;
    }

    public SubheadngBlock getSubheading() {
        return this.subheading;
    }

    public void setSubheading(SubheadngBlock subheading2) {
        this.subheading = subheading2;
    }

    public CodeBlock getCode() {
        return this.code;
    }

    public void setCode(CodeBlock code2) {
        this.code = code2;
    }

    public UnorderedListBlock getUnorderedList() {
        return this.unorderedList;
    }

    public void setUnorderedList(UnorderedListBlock unorderedList2) {
        this.unorderedList = unorderedList2;
    }

    public OrderedListBlock getOrderedList() {
        return this.orderedList;
    }

    public void setOrderedList(OrderedListBlock orderedList2) {
        this.orderedList = orderedList2;
    }

    public ImageBlock getImage() {
        return this.image;
    }

    public void setImage(ImageBlock image2) {
        this.image = image2;
    }

    public LocalImageBlock getLocalImage() {
        return this.localImage;
    }

    public void setLocalImage(LocalImageBlock localImage2) {
        this.localImage = localImage2;
    }

    public ButtonBlock getButton() {
        return this.button;
    }

    public void setButton(ButtonBlock button2) {
        this.button = button2;
    }

    public FacebookBlock getFacebookButton() {
        return this.facebookButton;
    }

    public void setFacebookButton(FacebookBlock facebookButton2) {
        this.facebookButton = facebookButton2;
    }

    public TwitterBlock getTwitterButton() {
        return this.twitterButton;
    }

    public void setTwitterButton(TwitterBlock twitterButton2) {
        this.twitterButton = twitterButton2;
    }

    public VideoBlock getVideo() {
        return this.video;
    }

    public void setVideo(VideoBlock video2) {
        this.video = video2;
    }

    public AttachmentListBlock getAttachmentList() {
        return this.attachmentList;
    }

    public void setAttachmentList(AttachmentListBlock attachmentList2) {
        this.attachmentList = attachmentList2;
    }

    public LocalAttachmentBlock getLocalAttachment() {
        return this.localAttachment;
    }

    public void setLocalAttachment(LocalAttachmentBlock localAttachment2) {
        this.localAttachment = localAttachment2;
    }

    public LightWeightReplyBlock getLwr() {
        return this.lwr;
    }

    public void setLwr(LightWeightReplyBlock lwr2) {
        this.lwr = lwr2;
    }
}
