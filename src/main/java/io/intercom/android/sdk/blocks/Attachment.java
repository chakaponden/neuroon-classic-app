package io.intercom.android.sdk.blocks;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.blocks.blockInterfaces.AttachmentListBlock;
import io.intercom.android.sdk.blocks.models.BlockAttachment;
import io.intercom.android.sdk.utilities.BlockUtils;
import io.intercom.android.sdk.utilities.ScreenUtils;
import java.util.List;

@TargetApi(11)
public class Attachment implements AttachmentListBlock {
    private static final int ATTACHMENT_LINE_SPACING_DP = 12;
    /* access modifiers changed from: private */
    public final Context context;
    private final LayoutInflater inflater;
    private final StyleType style;

    public Attachment(Context context2, StyleType style2) {
        this.style = style2;
        this.context = context2;
        this.inflater = LayoutInflater.from(context2);
    }

    public View addAttachmentList(List<BlockAttachment> attachments, boolean isFirstObject, boolean isLastObject, ViewGroup parent) {
        LinearLayout layout = (LinearLayout) this.inflater.inflate(R.layout.intercomsdk_blocks_attachment_list, parent, false);
        int count = attachments.size();
        for (int i = 0; i < count; i++) {
            final BlockAttachment attachment = attachments.get(i);
            LinearLayout attachmentLayout = (LinearLayout) this.inflater.inflate(R.layout.intercomsdk_blocks_attachment, layout, false);
            TextView attachmentText = (TextView) attachmentLayout.findViewById(R.id.attachment_text);
            attachmentText.setText(attachment.getName());
            attachmentText.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Uri uri = Uri.parse(attachment.getUrl());
                    if (!Uri.EMPTY.equals(uri)) {
                        Intent intent = new Intent("android.intent.action.VIEW", uri);
                        intent.setFlags(268435456);
                        Attachment.this.context.startActivity(intent);
                    }
                }
            });
            View attachmentDivider = attachmentLayout.findViewById(R.id.attachment_divider);
            if (this.style == StyleType.WELCOME) {
                attachmentText.setTextColor(this.context.getResources().getColor(R.color.intercomsdk_poweredby_color));
            } else if (this.style == StyleType.USER || this.style == StyleType.PREVIEW) {
                attachmentDivider.setVisibility(0);
                attachmentText.setTextColor(-1);
            } else {
                ((ImageView) attachmentLayout.findViewById(R.id.attachment_icon)).setColorFilter(Color.parseColor(Bridge.getIdentityStore().getAppConfig().getBaseColor()));
                attachmentText.setTextColor(Color.parseColor(Bridge.getIdentityStore().getAppConfig().getBaseColor()));
            }
            if (i < count - 1) {
                attachmentLayout.setPadding(attachmentLayout.getPaddingLeft(), attachmentLayout.getPaddingTop(), attachmentLayout.getPaddingRight(), ScreenUtils.convertDpToPixel(12.0f, this.context));
            }
            layout.addView(attachmentLayout);
        }
        BlockUtils.setLayoutMarginsAndGravity(layout, 3, isLastObject);
        return layout;
    }
}
