package io.intercom.android.sdk.blocks;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.blocks.blockInterfaces.ButtonBlock;
import io.intercom.android.sdk.utilities.BackgroundUtils;
import io.intercom.android.sdk.utilities.BlockUtils;
import io.intercom.android.sdk.utilities.FontUtils;
import io.intercom.android.sdk.utilities.LinkUtil;
import io.intercom.android.sdk.views.ButtonSelector;

public class Button implements ButtonBlock {
    /* access modifiers changed from: private */
    public final Context context;
    private final LayoutInflater inflater;
    private final StyleType style;

    public Button(Context context2, StyleType style2) {
        this.context = context2;
        this.style = style2;
        this.inflater = LayoutInflater.from(context2);
    }

    public View addButton(String text, final String buttonUrl, BlockAlignment alignment, boolean isFirstObject, boolean isLastObject, ViewGroup parent) {
        TextView button;
        View.OnClickListener clickListener = new View.OnClickListener() {
            public void onClick(View view) {
                LinkUtil.openUrl(buttonUrl, Button.this.context);
            }
        };
        switch (this.style) {
            case ANNOUNCEMENT:
                button = (android.widget.Button) this.inflater.inflate(R.layout.intercomsdk_blocks_button, parent, false);
                FontUtils.setTypeface(button, FontUtils.ROBOTO_MEDIUM, this.context);
                button.setGravity(17);
                BackgroundUtils.setBackground(button, new ButtonSelector(this.context, R.drawable.intercomsdk_button_shape));
                break;
            default:
                if (StyleType.WELCOME == this.style) {
                    button = (TextView) this.inflater.inflate(R.layout.intercomsdk_blocks_welcome_button, parent, false);
                } else {
                    button = (TextView) this.inflater.inflate(R.layout.intercomsdk_blocks_admin_paragraph, parent, false);
                }
                button.setTextColor(Color.parseColor(Bridge.getIdentityStore().getAppConfig().getBaseColor()));
                button.setPaintFlags(button.getPaintFlags() | 8);
                button.setGravity(alignment.getGravity());
                break;
        }
        button.setText(text);
        button.setOnClickListener(clickListener);
        BlockUtils.setLayoutMarginsAndGravity(button, alignment.getGravity(), isLastObject);
        return button;
    }
}
