package io.intercom.android.sdk.blocks;

import android.content.Context;
import android.view.LayoutInflater;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.interfaces.LWRListener;

public class ViewHolderGenerator {
    private final Context context;

    public ViewHolderGenerator(Context context2) {
        this.context = context2;
    }

    public BlocksViewHolder getAdminHolder(LWRListener listener) {
        return generateHolder(R.layout.intercomsdk_blocks_admin_layout, StyleType.ADMIN, listener);
    }

    public BlocksViewHolder getAnnouncementLWRHolder(LWRListener listener) {
        return generateHolder(R.layout.intercomsdk_blocks_announcement_lwr_layout, StyleType.ANNOUNCEMENT, listener);
    }

    public BlocksViewHolder getUserHolder() {
        return generateHolder(R.layout.intercomsdk_blocks_user_layout, StyleType.USER);
    }

    public BlocksViewHolder getPreviewHolder() {
        return generateHolder(R.layout.intercomsdk_blocks_user_layout, StyleType.PREVIEW);
    }

    public BlocksViewHolder getAnnouncementHolder() {
        return generateHolder(R.layout.intercomsdk_blocks_announcement_layout, StyleType.ANNOUNCEMENT);
    }

    public BlocksViewHolder getAnnouncementPreviewHolder(LWRListener listener) {
        return generateHolder(R.layout.intercomsdk_blocks_announcement_preview_layout, StyleType.ANNOUNCEMENT, listener);
    }

    public BlocksViewHolder getWelcomeHolder() {
        return generateHolder(R.layout.intercomsdk_blocks_welcome_layout, StyleType.WELCOME);
    }

    private BlocksViewHolder generateHolder(int layoutId, StyleType style) {
        return generateHolder(layoutId, style, (LWRListener) null);
    }

    private BlocksViewHolder generateHolder(int layoutId, StyleType style, LWRListener listener) {
        BlocksViewHolder holder = new BlocksViewHolder();
        holder.setLayout(layoutId);
        holder.setParagraph(new Paragraph(LayoutInflater.from(this.context), style));
        holder.setHeading(new Heading(this.context, style));
        holder.setSubheading(new Subheading(this.context, style));
        holder.setCode(new Code(this.context, style));
        holder.setUnorderedList(new UnorderedList(this.context, style));
        holder.setOrderedList(new OrderedList(this.context, style));
        holder.setImage(new NetworkImage(this.context, style));
        holder.setLocalImage(new LocalImage(this.context, style));
        holder.setButton(new Button(this.context, style));
        holder.setAttachmentList(new Attachment(this.context, style));
        holder.setLocalAttachment(new LocalAttachment(this.context, style));
        holder.setTwitterButton(new Twitter(this.context, style));
        holder.setFacebookButton(new Facebook(this.context, style));
        holder.setVideo(new Video(this.context, style));
        holder.setLwr(new LightWeightReply(this.context, style, listener));
        return holder;
    }
}
