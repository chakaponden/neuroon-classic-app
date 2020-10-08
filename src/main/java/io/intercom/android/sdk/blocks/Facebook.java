package io.intercom.android.sdk.blocks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.blocks.blockInterfaces.FacebookBlock;
import io.intercom.android.sdk.utilities.BackgroundUtils;
import io.intercom.android.sdk.utilities.BlockUtils;
import io.intercom.android.sdk.utilities.FontUtils;
import io.intercom.android.sdk.views.ButtonSelector;

public class Facebook implements FacebookBlock {
    /* access modifiers changed from: private */
    public final Context context;
    private final LayoutInflater inflater;
    private final StyleType style;

    public Facebook(Context context2, StyleType style2) {
        this.context = context2;
        this.style = style2;
        this.inflater = LayoutInflater.from(context2);
    }

    public View addFacebookButton(final String url, BlockAlignment alignment, boolean isFirstObject, boolean isLastObject, ViewGroup parent) {
        View view;
        View.OnClickListener clickListener = new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
                intent.setFlags(268435456);
                Facebook.this.context.startActivity(intent);
            }
        };
        switch (this.style) {
            case ANNOUNCEMENT:
                view = this.inflater.inflate(R.layout.intercomsdk_blocks_facebook_button, parent, false);
                TextView buttonText = (TextView) view.findViewById(R.id.title);
                FontUtils.setTypeface(buttonText, FontUtils.ROBOTO_MEDIUM, this.context);
                buttonText.setGravity(17);
                BackgroundUtils.setBackground(view, new ButtonSelector(this.context, R.drawable.intercomsdk_button_shape));
                break;
            default:
                TextView textView = (TextView) this.inflater.inflate(R.layout.intercomsdk_blocks_admin_paragraph, parent, false);
                textView.setText(R.string.intercomsdk_facebook_like);
                textView.setTextColor(Color.parseColor(Bridge.getIdentityStore().getAppConfig().getBaseColor()));
                textView.setPaintFlags(textView.getPaintFlags() | 8);
                textView.setGravity(alignment.getGravity());
                view = textView;
                break;
        }
        view.setOnClickListener(clickListener);
        BlockUtils.setLayoutMarginsAndGravity(view, alignment.getGravity(), isLastObject);
        return view;
    }
}
