package io.intercom.android.sdk.blocks;

import android.graphics.Color;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.blocks.blockInterfaces.ParagraphBlock;
import io.intercom.android.sdk.utilities.BlockUtils;
import io.intercom.android.sdk.utilities.TrackingLinkMovementMethod;

public class Paragraph implements ParagraphBlock {
    private final LayoutInflater inflater;
    private final StyleType style;

    public Paragraph(LayoutInflater inflater2, StyleType style2) {
        this.style = style2;
        this.inflater = inflater2;
    }

    public View addParagraph(Spanned text, BlockAlignment alignment, boolean isFirstObject, boolean isLastObject, ViewGroup parent) {
        TextView textview;
        switch (this.style) {
            case ADMIN:
            case ANNOUNCEMENT:
                textview = (TextView) this.inflater.inflate(R.layout.intercomsdk_blocks_admin_paragraph, parent, false);
                break;
            case WELCOME:
                textview = (TextView) this.inflater.inflate(R.layout.intercomsdk_blocks_welcome_paragraph, parent, false);
                break;
            default:
                textview = (TextView) this.inflater.inflate(R.layout.intercomsdk_blocks_user_paragraph, parent, false);
                break;
        }
        textview.setClickable(true);
        textview.setMovementMethod(new TrackingLinkMovementMethod());
        if (StyleType.ADMIN == this.style || StyleType.ANNOUNCEMENT == this.style) {
            textview.setLinkTextColor(Color.parseColor(Bridge.getIdentityStore().getAppConfig().getBaseColor()));
        }
        if (StyleType.PREVIEW == this.style) {
            textview.setText(text.toString());
        } else {
            textview.setText(text);
        }
        textview.setGravity(alignment.getGravity());
        BlockUtils.setLayoutMarginsAndGravity(textview, alignment.getGravity(), isLastObject);
        return textview;
    }
}
