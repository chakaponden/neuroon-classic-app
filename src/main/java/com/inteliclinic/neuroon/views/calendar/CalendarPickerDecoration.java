package com.inteliclinic.neuroon.views.calendar;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class CalendarPickerDecoration extends RecyclerView.ItemDecoration {
    private final View container;
    private final View indicator;
    private int mLeftOffset;
    private boolean mReversedAdapter;

    public CalendarPickerDecoration(View indicator2, View container2, boolean reverseAdapter) {
        this.indicator = indicator2;
        this.container = container2;
        this.mReversedAdapter = reverseAdapter;
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (this.mLeftOffset == 0) {
            this.mLeftOffset = this.container.getMeasuredWidth() - this.indicator.getMeasuredWidth();
        }
        int childAdapterPosition = parent.getChildAdapterPosition(view);
        if (!this.mReversedAdapter) {
            if (childAdapterPosition == 0) {
                outRect.set(this.mLeftOffset, 0, 0, 0);
                return;
            }
        } else if (childAdapterPosition == parent.getAdapter().getItemCount() - 1) {
            outRect.set(this.mLeftOffset, 0, 0, 0);
            return;
        }
        outRect.set(0, 0, 0, 0);
    }
}
