package io.intercom.android.sdk.blocks;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.blocks.blockInterfaces.OrderedListBlock;
import io.intercom.android.sdk.spans.OrderedListSpan;
import io.intercom.android.sdk.utilities.BlockUtils;
import io.intercom.android.sdk.utilities.TrackingLinkMovementMethod;
import java.util.List;

public class OrderedList implements OrderedListBlock {
    private final Context context;
    private final LayoutInflater inflater;
    private final StyleType style;

    public OrderedList(Context context2, StyleType style2) {
        this.context = context2;
        this.style = style2;
        this.inflater = LayoutInflater.from(context2);
    }

    public View addOrderedList(List<String> items, boolean isFirstObject, boolean isLastObject, ViewGroup parent) {
        TextView textview;
        LinearLayout layout = new LinearLayout(this.context);
        switch (this.style) {
            case ADMIN:
            case ANNOUNCEMENT:
                textview = (TextView) this.inflater.inflate(R.layout.intercomsdk_blocks_admin_orderedlist, parent, false);
                break;
            case WELCOME:
                layout = (LinearLayout) this.inflater.inflate(R.layout.intercomsdk_blocks_welcome_list, parent, false);
                textview = (TextView) layout.findViewById(R.id.list);
                break;
            default:
                textview = (TextView) this.inflater.inflate(R.layout.intercomsdk_blocks_user_orderedlist, parent, false);
                break;
        }
        int lineIndent = (int) this.context.getResources().getDimension(R.dimen.intercomsdk_list_indentation);
        CharSequence chars = "";
        int i = 0;
        int size = items.size();
        while (i < size) {
            String item = items.get(i);
            if (!item.isEmpty()) {
                Spanned source = Html.fromHtml(item + (i < size + -1 ? "<br />" : ""));
                SpannableString spannableString = new SpannableString(source);
                spannableString.setSpan(new OrderedListSpan(lineIndent, (i + 1) + "."), 0, source.length(), 0);
                chars = TextUtils.concat(new CharSequence[]{chars, spannableString});
            }
            i++;
        }
        textview.setClickable(true);
        textview.setMovementMethod(new TrackingLinkMovementMethod());
        textview.setText(chars);
        BlockUtils.setLayoutMarginsAndGravity(textview, 3, isLastObject);
        if (StyleType.WELCOME == this.style) {
            return layout;
        }
        textview.setLinkTextColor(Color.parseColor(Bridge.getIdentityStore().getAppConfig().getBaseColor()));
        return textview;
    }
}
