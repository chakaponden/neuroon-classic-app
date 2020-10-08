package io.intercom.android.sdk.blocks;

import android.content.Context;
import android.graphics.Color;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.blocks.blockInterfaces.HeadingBlock;
import io.intercom.android.sdk.utilities.BlockUtils;
import io.intercom.android.sdk.utilities.FontUtils;
import io.intercom.android.sdk.utilities.TrackingLinkMovementMethod;

public class Heading implements HeadingBlock {
    private final Context context;
    private final LayoutInflater inflater;
    private final StyleType style;

    public Heading(Context context2, StyleType style2) {
        this.style = style2;
        this.context = context2;
        this.inflater = LayoutInflater.from(context2);
    }

    public View addHeading(Spanned text, BlockAlignment alignment, boolean isFirstObject, boolean isLastObject, ViewGroup parent) {
        TextView textview;
        switch (this.style) {
            case ADMIN:
                textview = (TextView) this.inflater.inflate(R.layout.intercomsdk_blocks_admin_heading, parent, false);
                break;
            case ANNOUNCEMENT:
                textview = (TextView) this.inflater.inflate(R.layout.intercomsdk_blocks_announcement_heading, parent, false);
                int color = Color.parseColor(Bridge.getIdentityStore().getAppConfig().getBaseColor());
                textview.setTextColor(color);
                textview.setLinkTextColor(color);
                FontUtils.setTypeface(textview, FontUtils.ROBOTO_LIGHT, this.context);
                break;
            case WELCOME:
                textview = (TextView) this.inflater.inflate(R.layout.intercomsdk_blocks_welcome_heading, parent, false);
                break;
            default:
                textview = (TextView) this.inflater.inflate(R.layout.intercomsdk_blocks_user_heading, parent, false);
                break;
        }
        textview.setClickable(true);
        textview.setMovementMethod(new TrackingLinkMovementMethod());
        textview.setText(text);
        textview.setGravity(alignment.getGravity());
        BlockUtils.setLayoutMarginsAndGravity(textview, alignment.getGravity(), isLastObject);
        return textview;
    }
}
