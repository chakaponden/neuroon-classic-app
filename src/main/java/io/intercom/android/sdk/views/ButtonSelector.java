package io.intercom.android.sdk.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.StateListDrawable;
import io.intercom.android.sdk.Bridge;

public class ButtonSelector extends StateListDrawable {
    private int color = Color.parseColor(Bridge.getIdentityStore().getAppConfig().getBaseColor());

    public ButtonSelector(Context c, int imageResource) {
        addState(new int[]{16842910}, c.getResources().getDrawable(imageResource));
        addState(new int[]{16842908}, c.getResources().getDrawable(imageResource));
        addState(new int[]{16842919}, c.getResources().getDrawable(imageResource));
    }

    /* access modifiers changed from: protected */
    public boolean onStateChange(int[] states) {
        boolean isClicked = false;
        for (int state : states) {
            if (state == 16842919 || state == 16842908) {
                isClicked = true;
            }
        }
        if (isClicked) {
            setColorFilter(darken(this.color, 0.9d), PorterDuff.Mode.SRC);
        } else {
            setColorFilter(this.color, PorterDuff.Mode.SRC);
        }
        return super.onStateChange(states);
    }

    private static int darken(int color2, double fraction) {
        return Color.argb(255, (int) (((double) Color.red(color2)) * fraction), (int) (((double) Color.green(color2)) * fraction), (int) (((double) Color.blue(color2)) * fraction));
    }
}
