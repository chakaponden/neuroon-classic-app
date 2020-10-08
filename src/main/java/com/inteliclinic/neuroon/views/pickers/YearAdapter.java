package com.inteliclinic.neuroon.views.pickers;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.inteliclinic.neuroon.views.ThinTextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class YearAdapter extends RecyclerView.Adapter<ViewHolder> {
    private final Calendar mCalendar = Calendar.getInstance();
    private final SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy");
    private View mIndicator;

    public YearAdapter(View indicator) {
        this.mIndicator = indicator;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ThinTextView thinTextView = new ThinTextView(parent.getContext());
        thinTextView.setLayoutParams(new FrameLayout.LayoutParams(-1, this.mIndicator.getMeasuredHeight()));
        thinTextView.setGravity(17);
        thinTextView.setTextSize(22.0f);
        return new ViewHolder(thinTextView);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        this.mCalendar.add(1, -position);
        holder.mTextView.setText(this.mDateFormat.format(this.mCalendar.getTime()));
        this.mCalendar.add(1, position);
    }

    public int getItemCount() {
        return 120;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ThinTextView mTextView;

        public ViewHolder(ThinTextView v) {
            super(v);
            this.mTextView = v;
        }
    }
}
