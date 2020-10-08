package io.intercom.android.sdk.utilities;

import android.text.Layout;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.widget.TextView;
import io.intercom.android.sdk.Bridge;

public class TrackingLinkMovementMethod extends LinkMovementMethod {
    public boolean onTouchEvent(TextView textView, Spannable buffer, MotionEvent event) {
        if (event.getAction() == 1) {
            int position = getPosition(event, textView);
            URLSpan[] link = (URLSpan[]) buffer.getSpans(position, position, URLSpan.class);
            if (link.length != 0) {
                LinkUtil.openUrl(link[0].getURL(), Bridge.getContext());
            }
        }
        return true;
    }

    private int getPosition(MotionEvent event, TextView textView) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int x2 = x - textView.getTotalPaddingLeft();
        int y2 = y - textView.getTotalPaddingTop();
        int x3 = x2 + textView.getScrollX();
        Layout layout = textView.getLayout();
        return layout.getOffsetForHorizontal(layout.getLineForVertical(y2 + textView.getScrollY()), (float) x3);
    }
}
