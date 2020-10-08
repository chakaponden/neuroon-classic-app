package io.intercom.android.sdk.spans;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.Spanned;
import android.text.style.LeadingMarginSpan;
import io.intercom.android.sdk.utilities.ScreenUtils;

public class UnorderedListSpan implements LeadingMarginSpan {
    private static final int BULLET_RADIUS_IN_DP = 2;
    private static final int INDENT_IN_DP = 5;
    private final int bulletRadius;
    private final int gapWidth;
    private final int indent;

    public UnorderedListSpan(int gapWidth2, Context context) {
        this.gapWidth = gapWidth2;
        this.bulletRadius = ScreenUtils.convertDpToPixel(2.0f, context);
        this.indent = ScreenUtils.convertDpToPixel(5.0f, context);
    }

    public int getLeadingMargin(boolean first) {
        return (this.bulletRadius * 2) + this.gapWidth;
    }

    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout l) {
        if (((Spanned) text).getSpanStart(this) == start) {
            Paint.Style style = p.getStyle();
            p.setStyle(Paint.Style.FILL);
            c.drawCircle((float) ((this.bulletRadius * dir) + x + this.indent), ((float) (top + bottom)) / 2.0f, (float) this.bulletRadius, p);
            p.setStyle(style);
        }
    }
}
