package com.inteliclinic.neuroon.views.pickers;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.inteliclinic.neuroon.views.pickers.PickerView;

public class CenterScrollListener extends RecyclerView.OnScrollListener {
    private PickerView.OnItemSelectedListener mListener;
    private IIndicatorable pickerView;

    public CenterScrollListener(IIndicatorable pickerView2, PickerView.OnItemSelectedListener listener) {
        this.pickerView = pickerView2;
        this.mListener = listener;
    }

    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == 0) {
            settle(recyclerView);
        }
    }

    private void settle(RecyclerView recyclerView) {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int position = (linearLayoutManager.findLastVisibleItemPosition() + linearLayoutManager.findFirstVisibleItemPosition()) / 2;
        View centerView = linearLayoutManager.findViewByPosition(position);
        int indicatorBottom = 0;
        if (!(this.pickerView == null || this.pickerView.getIndicator() == null)) {
            indicatorBottom = this.pickerView.getIndicator().getBottom();
        }
        int scrollDistance = centerView.getBottom() - indicatorBottom;
        if (Math.abs(scrollDistance) < centerView.getHeight() / 2) {
            if (scrollDistance != 0) {
                recyclerView.smoothScrollBy(0, scrollDistance);
            } else if (this.mListener != null) {
                this.mListener.onItemSelected(recyclerView, centerView, position);
            }
        } else if (scrollDistance > 0) {
            recyclerView.smoothScrollBy(0, -(centerView.getHeight() - scrollDistance));
            if (this.mListener != null) {
                this.mListener.onItemSelected(recyclerView, linearLayoutManager.findViewByPosition(position - 1), position - 1);
            }
        } else {
            recyclerView.smoothScrollBy(0, centerView.getHeight() + scrollDistance);
            if (this.mListener != null) {
                this.mListener.onItemSelected(recyclerView, linearLayoutManager.findViewByPosition(position + 1), position + 1);
            }
        }
    }
}
