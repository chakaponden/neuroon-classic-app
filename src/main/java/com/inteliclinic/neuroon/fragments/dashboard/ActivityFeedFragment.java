package com.inteliclinic.neuroon.fragments.dashboard;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.eowise.recyclerview.stickyheaders.StickyHeadersAdapter;
import com.eowise.recyclerview.stickyheaders.StickyHeadersBuilder;
import com.eowise.recyclerview.stickyheaders.StickyHeadersItemDecoration;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.adapters.EndlessRecyclerOnScrollListener;
import com.inteliclinic.neuroon.events.DbEventsUpdatedEvent;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.managers.DataManager;
import com.inteliclinic.neuroon.models.data.Event;
import com.inteliclinic.neuroon.utils.DateUtils;
import com.inteliclinic.neuroon.views.ActivityEntryView;
import com.inteliclinic.neuroon.views.ThinTextView;
import de.greenrobot.event.EventBus;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class ActivityFeedFragment extends BaseFragment {
    /* access modifiers changed from: private */
    public ActivityFeedAdapter mAdapter;
    private StickyHeadersItemDecoration mItemDecoration;
    /* access modifiers changed from: private */
    public OnFragmentInteractionListener mListener;
    @InjectView(2131755133)
    RecyclerView mRecyclerView;

    public interface OnFragmentInteractionListener {
        void goToDashboard();

        void showEventDetails(Event event);
    }

    public static ActivityFeedFragment newInstance() {
        return new ActivityFeedFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_feed, container, false);
        ButterKnife.inject((Object) this, view);
        setupView();
        return view;
    }

    public void onResume() {
        super.onResume();
        this.mAdapter.checkAdapter();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    public void onPause() {
        super.onPause();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    public void onEventMainThread(DbEventsUpdatedEvent event) {
        if (this.mItemDecoration != null) {
            this.mRecyclerView.removeItemDecoration(this.mItemDecoration);
        }
        this.mAdapter = new ActivityFeedAdapter();
        this.mAdapter.setHasStableIds(true);
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mItemDecoration = new StickyHeadersBuilder().setAdapter(this.mAdapter).setRecyclerView(this.mRecyclerView).setStickyHeadersAdapter(new HeaderAdapter(this.mAdapter)).build();
        this.mRecyclerView.addItemDecoration(this.mItemDecoration);
    }

    private void setupView() {
        this.mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.mRecyclerView.getContext(), 1, false);
        this.mRecyclerView.setLayoutManager(mLayoutManager);
        this.mAdapter = new ActivityFeedAdapter();
        this.mAdapter.setHasStableIds(true);
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mItemDecoration = new StickyHeadersBuilder().setAdapter(this.mAdapter).setRecyclerView(this.mRecyclerView).setStickyHeadersAdapter(new HeaderAdapter(this.mAdapter)).build();
        this.mRecyclerView.addItemDecoration(this.mItemDecoration);
        this.mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            public void onLoadMore(int currentPage) {
                ActivityFeedFragment.this.mAdapter.loadMore(50);
            }
        });
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void onBackButtonClick() {
        if (this.mListener != null) {
            this.mListener.goToDashboard();
        }
    }

    public static class ActivityEntryViewHolder extends RecyclerView.ViewHolder {
        ActivityEntryView mEntry;

        public ActivityEntryViewHolder(ActivityEntryView v) {
            super(v);
            this.mEntry = v;
        }
    }

    public final class ActivityFeedAdapter extends RecyclerView.Adapter<ActivityEntryViewHolder> {
        private static final int TYPE_EXTENDED = 2;
        private static final int TYPE_NORMAL = 0;
        private List<Event> mEventsDataSet = new ArrayList();

        public ActivityFeedAdapter() {
            loadMore(20);
        }

        public void loadMore(int count) {
            Iterator<Event> dbEvents = DataManager.getInstance().getEvents(this.mEventsDataSet.size(), count).iterator();
            int i = 0;
            while (i < count) {
                if (dbEvents.hasNext()) {
                    this.mEventsDataSet.add(dbEvents.next());
                    Collections.sort(this.mEventsDataSet, new Comparator<Event>() {
                        public int compare(Event lhs, Event rhs) {
                            return rhs.getStartDate().compareTo(lhs.getStartDate());
                        }
                    });
                    i++;
                } else if (i > 0) {
                    notifyDataSetChanged();
                    return;
                } else {
                    return;
                }
            }
            notifyDataSetChanged();
        }

        public ActivityEntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case 2:
                    return new ActivityEntryViewHolder((ActivityEntryView) LayoutInflater.from(parent.getContext()).inflate(R.layout.view_activity_feed_item_extended, parent, false));
                default:
                    return new ActivityEntryViewHolder((ActivityEntryView) LayoutInflater.from(parent.getContext()).inflate(R.layout.view_activity_feed_item_normal, parent, false));
            }
        }

        public void onBindViewHolder(ActivityEntryViewHolder holder, int position) {
            final Event itemAtPosition = getItemAtPosition(position);
            holder.mEntry.setEvent(itemAtPosition);
            holder.mEntry.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (ActivityFeedFragment.this.mListener != null) {
                        ActivityFeedFragment.this.mListener.showEventDetails(itemAtPosition);
                    }
                }
            });
        }

        public int getItemViewType(int position) {
            return 0;
        }

        @Nullable
        public Event getItemAtPosition(int position) {
            return this.mEventsDataSet.get(position);
        }

        public int getItemCount() {
            return this.mEventsDataSet.size();
        }

        public long getItemId(int position) {
            return this.mEventsDataSet.get(position).getId();
        }

        public void checkAdapter() {
            int i;
            List<Integer> toDelete = new ArrayList<>();
            for (Event next : this.mEventsDataSet) {
                if (DataManager.getInstance().getEventById(next.getId()) == null && (i = this.mEventsDataSet.indexOf(next)) != -1) {
                    toDelete.add(Integer.valueOf(i));
                }
            }
            Collections.reverse(toDelete);
            for (Integer intValue : toDelete) {
                this.mEventsDataSet.remove(intValue.intValue());
            }
            if (toDelete.size() > 0) {
                ActivityFeedFragment.this.mRecyclerView.setItemAnimator((RecyclerView.ItemAnimator) null);
                notifyDataSetChanged();
            }
        }
    }

    public final class HeaderAdapter implements StickyHeadersAdapter<RecyclerView.ViewHolder> {
        private DateFormat dateFormat = new SimpleDateFormat("MMM. dd yyyy");
        private ActivityFeedAdapter mAdapter;

        public HeaderAdapter(ActivityFeedAdapter adapter) {
            this.mAdapter = adapter;
        }

        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
            return new RecyclerView.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_activity_feed_item_header, viewGroup, false)) {
            };
        }

        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            ThinTextView itemView = (ThinTextView) viewHolder.itemView.findViewById(R.id.header);
            Event itemAtPosition = this.mAdapter.getItemAtPosition(position);
            if (itemAtPosition == null) {
                return;
            }
            if (DateUtils.isToday(itemAtPosition.getStartDate())) {
                itemView.setText(R.string.today);
            } else {
                itemView.setText(this.dateFormat.format(itemAtPosition.getStartDate()));
            }
        }

        public long getHeaderId(int position) {
            Event itemAtPosition = this.mAdapter.getItemAtPosition(position);
            if (itemAtPosition != null) {
                return DateUtils.dateWithoutTime(itemAtPosition.getStartDate()).getTime();
            }
            return 0;
        }
    }
}
