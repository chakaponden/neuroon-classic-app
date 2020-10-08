package com.inteliclinic.neuroon.views.calendar;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.crashlytics.android.Crashlytics;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.events.DbSleepsUpdatedEvent;
import com.inteliclinic.neuroon.managers.DataManager;
import com.inteliclinic.neuroon.models.data.Sleep;
import com.inteliclinic.neuroon.views.ThinTextView;
import de.greenrobot.event.EventBus;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SleepDatesHorizontalListView extends RecyclerView {
    /* access modifiers changed from: private */
    public static DateFormat dateFormat = new SimpleDateFormat("MMM. dd");
    /* access modifiers changed from: private */
    public CalendarListAdapter mAdapter;
    /* access modifiers changed from: private */
    public OnSleepClickListener onItemClickListener;

    public interface OnSleepClickListener {
        void onSleepClick(long j);
    }

    public SleepDatesHorizontalListView(Context context) {
        super(context);
        addOnScrollListener(new CalendarScrollListener());
        addOnItemTouchListener(new RecyclerItemClickListener(getContext()));
    }

    public SleepDatesHorizontalListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        addOnScrollListener(new CalendarScrollListener());
        addOnItemTouchListener(new RecyclerItemClickListener(getContext()));
    }

    public SleepDatesHorizontalListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addOnScrollListener(new CalendarScrollListener());
        addOnItemTouchListener(new RecyclerItemClickListener(getContext()));
    }

    public void initDateList() {
        setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(), 0, false);
        mLayoutManager.setStackFromEnd(true);
        setLayoutManager(mLayoutManager);
        this.mAdapter = new CalendarListAdapter();
        setAdapter(this.mAdapter);
        super.scrollToPosition(this.mAdapter.getItemCount() - 1);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDetachedFromWindow();
    }

    public void onEventMainThread(DbSleepsUpdatedEvent event) {
        if (this.mAdapter != null) {
            this.mAdapter.updateAdapter();
        }
    }

    public void smoothScrollToPosition(int position) {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
        int lastVisibleView = linearLayoutManager.findLastVisibleItemPosition();
        int elemsCount = lastVisibleView - linearLayoutManager.findFirstVisibleItemPosition();
        if (lastVisibleView == position) {
            if (this.onItemClickListener != null) {
                this.onItemClickListener.onSleepClick(this.mAdapter.getSleepAtPosition(position));
            }
        } else if (position > elemsCount || position >= lastVisibleView) {
            super.smoothScrollToPosition(position - elemsCount);
        } else {
            if (this.onItemClickListener != null) {
                this.onItemClickListener.onSleepClick(this.mAdapter.getSleepAtPosition(position));
            }
            scrollBy((position - lastVisibleView) * linearLayoutManager.findViewByPosition(lastVisibleView).getMeasuredWidth(), 0);
        }
    }

    public void scrollToPosition(int position) {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
        int lastVisibleView = linearLayoutManager.findLastVisibleItemPosition();
        int findFirstVisibleItemPosition = lastVisibleView - linearLayoutManager.findFirstVisibleItemPosition();
        if (lastVisibleView != position) {
            if (this.onItemClickListener != null) {
                this.onItemClickListener.onSleepClick(this.mAdapter.getSleepAtPosition(position));
            }
            scrollBy((position - lastVisibleView) * linearLayoutManager.findViewByPosition(lastVisibleView).getMeasuredWidth(), 0);
        } else if (this.onItemClickListener != null) {
            this.onItemClickListener.onSleepClick(this.mAdapter.getSleepAtPosition(position));
        }
    }

    public void smoothScrollToSleep(long sleepId) {
        smoothScrollToPosition(this.mAdapter.getPositionOf(sleepId));
    }

    public void scrollToSleep(long sleepId) {
        scrollToPosition(this.mAdapter.getPositionOf(sleepId));
    }

    public void setOnItemClickListener(OnSleepClickListener onItemClickListener2) {
        this.onItemClickListener = onItemClickListener2;
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
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) SleepDatesHorizontalListView.this.getLayoutManager();
            int lastVisibleView = linearLayoutManager.findLastVisibleItemPosition();
            View lastView = linearLayoutManager.findViewByPosition(lastVisibleView);
            if (lastView != null) {
                int scrollDistanceLeft = lastView.getLeft() - SleepDatesHorizontalListView.this.getWidth();
                int scrollDistanceRight = lastView.getRight() - SleepDatesHorizontalListView.this.getWidth();
                if (Math.abs(scrollDistanceLeft) <= Math.abs(scrollDistanceRight)) {
                    SleepDatesHorizontalListView.this.smoothScrollBy(scrollDistanceLeft, 0);
                } else if (scrollDistanceRight != 0) {
                    SleepDatesHorizontalListView.this.smoothScrollBy(scrollDistanceRight, 0);
                } else if (SleepDatesHorizontalListView.this.onItemClickListener != null) {
                    SleepDatesHorizontalListView.this.onItemClickListener.onSleepClick(SleepDatesHorizontalListView.this.mAdapter.getSleepAtPosition(lastVisibleView));
                }
            }
        }
    }

    public final class CalendarListAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
        private ArrayList<Pair<String, Long>> mDataset;

        public CalendarListAdapter() {
            if (this.mDataset == null) {
                fillDays();
            }
        }

        public void updateAdapter() {
            fillDays();
            notifyDataSetChanged();
            click();
        }

        private void click() {
            if (SleepDatesHorizontalListView.this.onItemClickListener != null) {
                int lastVisibleItemPosition = ((LinearLayoutManager) SleepDatesHorizontalListView.this.getLayoutManager()).findLastVisibleItemPosition();
                if (this.mDataset.size() <= 0 || lastVisibleItemPosition <= -1) {
                    SleepDatesHorizontalListView.this.onItemClickListener.onSleepClick(-1);
                } else if (this.mDataset.size() <= lastVisibleItemPosition) {
                    SleepDatesHorizontalListView.this.onItemClickListener.onSleepClick(((Long) this.mDataset.get(lastVisibleItemPosition - 1).second).longValue());
                } else {
                    SleepDatesHorizontalListView.this.onItemClickListener.onSleepClick(((Long) this.mDataset.get(lastVisibleItemPosition).second).longValue());
                }
            }
        }

        private void fillDays() {
            List<Sleep> sleepsDescending = DataManager.getInstance().getSleepsByDateDescending();
            this.mDataset = new ArrayList<>();
            for (Sleep next : sleepsDescending) {
                if (next.getSleepType() == 1) {
                    this.mDataset.add(0, new Pair(SleepDatesHorizontalListView.dateFormat.format(Long.valueOf(next.getStartDate().getTime())), Long.valueOf(next.getId())));
                }
            }
        }

        public CalendarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CalendarViewHolder((ThinTextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_list_item, parent, false));
        }

        public void onBindViewHolder(CalendarViewHolder holder, int position) {
            holder.mTextView.setText(((String) this.mDataset.get(position).first).toUpperCase());
        }

        public int getItemCount() {
            return this.mDataset.size();
        }

        public int getPositionOf(long sleepId) {
            int position = 0;
            Iterator<Pair<String, Long>> it = this.mDataset.iterator();
            while (it.hasNext()) {
                if (((Long) it.next().second).longValue() == sleepId) {
                    return position;
                }
                position++;
            }
            return 0;
        }

        public long getSleepAtPosition(int position) {
            if (position != -1) {
                return ((Long) this.mDataset.get(position).second).longValue();
            }
            Crashlytics.logException(new UnsupportedOperationException("wrong sleep position"));
            return ((Long) this.mDataset.get(0).second).longValue();
        }
    }

    private class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        GestureDetector mGestureDetector;

        RecyclerItemClickListener(Context context) {
            this.mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(SleepDatesHorizontalListView.this) {
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
