package io.intercom.android.sdk.utilities;

import android.view.View;
import android.widget.LinearLayout;

public class BlockUtils {
    public static void setLayoutMarginsAndGravity(View view, int gravity, boolean shouldStripBottomPadding) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (shouldStripBottomPadding) {
            params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, 0);
        }
        params.gravity = gravity;
    }
}
