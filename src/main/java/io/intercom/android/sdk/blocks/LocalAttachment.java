package io.intercom.android.sdk.blocks;

import android.annotation.TargetApi;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.blocks.blockInterfaces.LocalAttachmentBlock;
import io.intercom.android.sdk.blocks.models.BlockAttachment;
import io.intercom.android.sdk.utilities.BlockUtils;
import io.intercom.android.sdk.views.ProgressLinearLayout;
import io.intercom.android.sdk.views.UploadProgressBar;

@TargetApi(11)
public class LocalAttachment implements LocalAttachmentBlock {
    private final LayoutInflater inflater;
    private final int spinnerSize;
    private final StyleType style;

    public LocalAttachment(Context context, StyleType style2) {
        this.style = style2;
        this.inflater = LayoutInflater.from(context);
        this.spinnerSize = context.getResources().getDimensionPixelSize(R.dimen.intercomsdk_local_attachment_upload_size);
    }

    public View addAttachment(BlockAttachment blockAttachment, boolean isFirstObject, boolean isLastObject, ViewGroup parent) {
        ProgressLinearLayout rootView = (ProgressLinearLayout) this.inflater.inflate(R.layout.intercomsdk_blocks_local_attachment, parent, false);
        TextView textView = (TextView) rootView.findViewById(R.id.local_attachment);
        textView.setText(blockAttachment.getName());
        textView.setTextColor(-1);
        rootView.setUploadProgressBar((UploadProgressBar) rootView.findViewById(R.id.upload_wheel));
        BlockUtils.setLayoutMarginsAndGravity(rootView, 3, isLastObject);
        return rootView;
    }
}
