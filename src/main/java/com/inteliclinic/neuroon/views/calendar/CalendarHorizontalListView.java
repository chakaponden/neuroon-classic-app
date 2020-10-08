package com.inteliclinic.neuroon.views.calendar;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.ThinTextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CalendarHorizontalListView extends RecyclerView {
    private CalendarListAdapter mAdapter;

    public CalendarHorizontalListView(Context context) {
        super(context);
        addOnScrollListener(new CalendarScrollListener());
        addOnItemTouchListener(new RecyclerItemClickListener(getContext()));
    }

    public CalendarHorizontalListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addOnScrollListener(new CalendarScrollListener());
        addOnItemTouchListener(new RecyclerItemClickListener(getContext()));
    }

    public CalendarHorizontalListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addOnScrollListener(new CalendarScrollListener());
        addOnItemTouchListener(new RecyclerItemClickListener(getContext()));
    }

    public void initDateList() {
        setHasFixedSize(true);
        setLayoutManager(new LinearLayoutManager(getContext(), 0, false));
        this.mAdapter = new CalendarListAdapter(getContext());
        setAdapter(this.mAdapter);
        scrollToPosition(this.mAdapter.getItemCount() - 1);
    }

    public void smoothScrollToPosition(int position) {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
        super.smoothScrollToPosition(position - (linearLayoutManager.findLastVisibleItemPosition() - linearLayoutManager.findFirstVisibleItemPosition()));
    }

    public static final class CalendarListAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
        private static DateFormat dateFormat = new SimpleDateFormat("MMM. dd");
        private static ArrayList<String> mDataset;

        public CalendarListAdapter(Context context) {
            if (mDataset == null) {
                fillDays(context);
            }
        }

        private static void fillDays(Context context) {
            mDataset = new ArrayList<>();
            mDataset.add(context.getString(R.string.today));
            Calendar cal = Calendar.getInstance();
            for (int i = 0; i < 363; i++) {
                cal.add(5, -1);
                mDataset.add(0, dateFormat.format(cal.getTime()));
            }
        }

        public CalendarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CalendarViewHolder((ThinTextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_list_item, parent, false));
        }

        public void onBindViewHolder(CalendarViewHolder holder, int position) {
            holder.mTextView.setText(mDataset.get(position).toUpperCase());
        }

        public int getItemCount() {
            return 364;
        }
    }

    public static class CalendarViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        public CalendarViewHolder(TextView v) {
            super(v);
            this.mTextView = v;
        }
    }

    private class CalendarScrollListener extends RecyclerView.OnScrollListener {
        private CalendarScrollListener() {
        }

        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == 0) {
                settle();
            }
        }

        private void settle() {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) CalendarHorizontalListView.this.getLayoutManager();
            View lastView = linearLayoutManager.findViewByPosition(linearLayoutManager.findLastVisibleItemPosition());
            int scrollDistanceLeft = lastView.getLeft() - CalendarHorizontalListView.this.getWidth();
            int scrollDistanceRight = lastView.getRight() - CalendarHorizontalListView.this.getWidth();
            if (Math.abs(scrollDistanceLeft) <= Math.abs(scrollDistanceRight)) {
                CalendarHorizontalListView.this.smoothScrollBy(scrollDistanceLeft, 0);
            } else if (scrollDistanceRight != 0) {
                CalendarHorizontalListView.this.smoothScrollBy(scrollDistanceRight, 0);
            }
        }
    }

    private class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        GestureDetector mGestureDetector;

        RecyclerItemClickListener(Context context) {
            this.mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(CalendarHorizontalListView.this) {
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            if (childView == null || !this.mGestureDetector.onTouchEvent(e)) {
                return false;
            }
            view.smoothScrollToPosition(view.getChildAdapterPosition(childView));
            return true;
        }

        public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
        }

        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }
}
