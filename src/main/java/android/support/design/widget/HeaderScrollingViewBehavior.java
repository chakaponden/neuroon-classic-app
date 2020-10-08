package android.support.design.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import java.util.List;

abstract class HeaderScrollingViewBehavior extends ViewOffsetBehavior<View> {
    /* access modifiers changed from: package-private */
    public abstract View findFirstDependency(List<View> list);

    public HeaderScrollingViewBehavior() {
    }

    public HeaderScrollingViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onMeasureChild(CoordinatorLayout parent, View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        View header;
        int childLpHeight = child.getLayoutParams().height;
        if (childLpHeight == -1 || childLpHeight == -2) {
            List<View> dependencies = parent.getDependencies(child);
            if (!dependencies.isEmpty() && (header = findFirstDependency(dependencies)) != null && ViewCompat.isLaidOut(header)) {
                if (ViewCompat.getFitsSystemWindows(header)) {
                    ViewCompat.setFitsSystemWindows(child, true);
                }
                int availableHeight = View.MeasureSpec.getSize(parentHeightMeasureSpec);
                if (availableHeight == 0) {
                    availableHeight = parent.getHeight();
                }
                parent.onMeasureChild(child, parentWidthMeasureSpec, widthUsed, View.MeasureSpec.makeMeasureSpec((availableHeight - header.getMeasuredHeight()) + getScrollRange(header), childLpHeight == -1 ? 1073741824 : Integer.MIN_VALUE), heightUsed);
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public int getScrollRange(View v) {
        return v.getMeasuredHeight();
    }
}
