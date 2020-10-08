package com.inteliclinic.neuroon.views.pickers;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.inteliclinic.neuroon.views.ThinTextView;

public class UnitAdapter extends RecyclerView.Adapter<ViewHolder> {
    private final int endValue;
    private View mIndicator;
    private String mUnit;
    private final int startValue;

    public UnitAdapter(View indicator, String unit, int startValue2, int endValue2) {
        this.mIndicator = indicator;
        this.mUnit = unit;
        this.startValue = startValue2;
        this.endValue = endValue2;
    }

    public int getStartValue() {
        return this.startValue;
    }

    public int getEndValue() {
        return this.endValue;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ThinTextView thinTextView = new ThinTextView(parent.getContext());
        thinTextView.setLayoutParams(new FrameLayout.LayoutParams(-1, this.mIndicator.getMeasuredHeight()));
        thinTextView.setGravity(17);
        thinTextView.setTextSize(22.0f);
        return new ViewHolder(thinTextView);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText((this.startValue + position) + this.mUnit);
    }

    public int getItemCount() {
        return (this.endValue - this.startValue) + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ThinTextView mTextView;

        public ViewHolder(ThinTextView v) {
            super(v);
            this.mTextView = v;
        }

        public ThinTextView getTextView() {
            return this.mTextView;
        }
    }
}
