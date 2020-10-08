package io.intercom.android.sdk.spans;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.Spanned;
import android.text.style.LeadingMarginSpan;

public class OrderedListSpan implements LeadingMarginSpan {
    private final int gapWidth;
    private final String number;

    public OrderedListSpan(int gapWidth2, String number2) {
        this.gapWidth = gapWidth2;
        this.number = number2;
    }

    public int getLeadingMargin(boolean first) {
        return (int) (new Paint().measureText(this.number) + ((float) this.gapWidth));
    }

    public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout l) {
        if (((Spanned) text).getSpanStart(this) == start) {
            Paint.Style style = p.getStyle();
            p.setStyle(Paint.Style.FILL);
            c.drawText(this.number + " ", (float) (x + dir), (float) baseline, p);
            p.setStyle(style);
        }
    }
}
