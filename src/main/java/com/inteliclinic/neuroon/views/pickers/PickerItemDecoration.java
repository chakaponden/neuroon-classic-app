package com.inteliclinic.neuroon.views.pickers;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class PickerItemDecoration extends RecyclerView.ItemDecoration {
    private final View container;
    private final View indicator;
    private int mBottomOffset;
    private boolean mReversedAdapter;
    private int mTopOffset;

    public PickerItemDecoration(View indicator2, View container2, boolean reverseAdapter) {
        this.indicator = indicator2;
        this.container = container2;
        this.mReversedAdapter = reverseAdapter;
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (this.mTopOffset == 0 || this.mBottomOffset == 0) {
            this.mTopOffset = (this.container.getMeasuredHeight() / 2) - (this.indicator.getMeasuredHeight() / 2);
            this.mBottomOffset = (this.container.getMeasuredHeight() / 2) - (this.indicator.getMeasuredHeight() / 2);
        }
        int childAdapterPosition = parent.getChildAdapterPosition(view);
        if (!this.mReversedAdapter) {
            if (childAdapterPosition == 0) {
                outRect.set(0, this.mTopOffset, 0, 0);
                return;
            } else if (childAdapterPosition == parent.getAdapter().getItemCount() - 1) {
                outRect.set(0, 0, 0, this.mBottomOffset);
                return;
            }
        } else if (childAdapterPosition == 0) {
            outRect.set(0, 0, 0, this.mBottomOffset);
            return;
        } else if (childAdapterPosition == parent.getAdapter().getItemCount() - 1) {
            outRect.set(0, this.mTopOffset, 0, 0);
            return;
        }
        outRect.set(0, 0, 0, 0);
    }
}
