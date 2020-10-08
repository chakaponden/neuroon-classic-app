package io.intercom.android.sdk.blocks;

import android.content.Context;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.blocks.blockInterfaces.CodeBlock;
import io.intercom.android.sdk.utilities.BlockUtils;

public class Code implements CodeBlock {
    private final LayoutInflater inflater;

    public Code(Context context, StyleType style) {
        this.inflater = LayoutInflater.from(context);
    }

    public View addCode(Spanned code, boolean isFirstObject, boolean isLastObject, ViewGroup parent) {
        TextView textview = (TextView) this.inflater.inflate(R.layout.intercomsdk_blocks_code, parent, false);
        textview.setText(code);
        BlockUtils.setLayoutMarginsAndGravity(textview, 3, isLastObject);
        return textview;
    }
}
