package com.inteliclinic.neuroon.views.pickers;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.inteliclinic.neuroon.views.ThinTextView;
import java.util.Calendar;
import java.util.Date;

public class DaysAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Calendar mCalendar = Calendar.getInstance();
    private Date mDate;
    private int mDaysCount;
    private View mIndicator;

    public DaysAdapter(View indicator, Date date) {
        this.mIndicator = indicator;
        setDateInt(date);
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ThinTextView thinTextView = new ThinTextView(parent.getContext());
        thinTextView.setLayoutParams(new FrameLayout.LayoutParams(-1, this.mIndicator.getMeasuredHeight()));
        thinTextView.setGravity(17);
        thinTextView.setTextSize(22.0f);
        return new ViewHolder(thinTextView);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(String.valueOf(position + 1));
    }

    public int getItemCount() {
        return this.mDaysCount;
    }

    public void setDate(Date date) {
        setDateInt(date);
        notifyDataSetChanged();
    }

    private void setDateInt(Date date) {
        this.mDate = date;
        this.mCalendar.setTime(this.mDate);
        this.mDaysCount = this.mCalendar.getActualMaximum(5);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ThinTextView mTextView;

        public ViewHolder(ThinTextView v) {
            super(v);
            this.mTextView = v;
        }
    }
}
