package com.inteliclinic.neuroon.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.inteliclinic.neuroon.R;

public class SleepSurveyFragment extends BaseFragment {
    private static final String ARG_SLEEP_ID = "param1";
    private OnFragmentInteractionListener mListener;
    private long mSleepId;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public static SleepSurveyFragment newInstance(Long sleepId) {
        SleepSurveyFragment fragment = new SleepSurveyFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_SLEEP_ID, sleepId.longValue());
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mSleepId = getArguments().getLong(ARG_SLEEP_ID);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sleep_survey, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            this.mListener = (OnFragmentInteractionListener) context;
            return;
        }
        throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({2131755456, 2131755457})
    public void onCurrentFeelClick(View view) {
        switch (view.getId()) {
        }
    }

    @OnClick({2131755458, 2131755154, 2131755459})
    public void onNightFeelClick(View view) {
        switch (view.getId()) {
        }
    }

    @OnClick({2131755460, 2131755461})
    public void onNightFeel2Click(View view) {
        switch (view.getId()) {
        }
    }

    @OnClick({2131755462, 2131755463})
    public void onBadDreamsClick(View view) {
        switch (view.getId()) {
        }
    }

    @OnClick({2131755464})
    public void onSaveClick(View view) {
    }
}
