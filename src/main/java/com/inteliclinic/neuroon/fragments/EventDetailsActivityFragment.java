package com.inteliclinic.neuroon.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.managers.DataManager;
import com.inteliclinic.neuroon.models.data.Event;

public class EventDetailsActivityFragment extends BaseFragment {
    private static final String ARG_EVENT_ID = "arg_event_id";
    private Event mEvent;

    public static EventDetailsActivityFragment newInstance(Event event) {
        EventDetailsActivityFragment fragment = new EventDetailsActivityFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_EVENT_ID, event.getId());
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mEvent = DataManager.getInstance().getEventById(getArguments().getLong(ARG_EVENT_ID));
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
