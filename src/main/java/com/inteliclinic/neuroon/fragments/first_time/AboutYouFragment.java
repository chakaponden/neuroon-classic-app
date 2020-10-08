package com.inteliclinic.neuroon.fragments.first_time;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.google.android.gms.analytics.HitBuilders;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.managers.account.AccountManager;
import com.inteliclinic.neuroon.views.ThinButton;
import java.util.List;

public class AboutYouFragment extends BaseFragment {
    private static final String ARG_STANDALONE = "is_standalone";
    private SurveyAdapter mAdapter;
    private boolean mIsStandalone;
    private OnFragmentInteractionListener mListener;
    @InjectView(2131755132)
    ThinButton next;
    @InjectView(2131755133)
    RecyclerView recyclerView;
    @InjectView(2131755134)
    LinearLayout stepIndicator;

    public interface OnFragmentInteractionListener {
        void finish();
    }

    public static AboutYouFragment newInstance() {
        return new AboutYouFragment();
    }

    public static AboutYouFragment newInstance(boolean isStandalone) {
        AboutYouFragment fragment = new AboutYouFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_STANDALONE, isStandalone);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mIsStandalone = getArguments().getBoolean(ARG_STANDALONE);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.inject((Object) this, view);
        initList();
        return view;
    }

    public void onResume() {
        super.onResume();
        getTracker().setScreenName("about_you");
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
        if (this.mIsStandalone) {
            this.next.setText(R.string.save);
            this.stepIndicator.setVisibility(4);
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnFragmentInteractionListener) {
            this.mListener = (OnFragmentInteractionListener) activity;
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

    @OnClick({2131755132})
    public void onNextClick() {
        List<Boolean> results = this.mAdapter.getResults();
        getTracker().send(new HitBuilders.EventBuilder().setCategory("about_you").setAction("flyer").setLabel(results.get(3).booleanValue() ? "1" : "0").build());
        getTracker().send(new HitBuilders.EventBuilder().setCategory("about_you").setAction("shift-worker").setLabel(results.get(0).booleanValue() ? "1" : "0").build());
        getTracker().send(new HitBuilders.EventBuilder().setCategory("about_you").setAction("diagnosed").setLabel(results.get(5).booleanValue() ? "1" : "0").build());
        AccountManager.getInstance().setUserHabits(results);
        if (this.mListener != null) {
            this.mListener.finish();
        } else if (this.mIsStandalone) {
            getFragmentManager().popBackStack();
        }
    }

    private void initList() {
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.recyclerView.getContext(), 1, false));
        this.recyclerView.setHasFixedSize(false);
        this.mAdapter = new SurveyAdapter(AccountManager.getInstance().getSleepHabits());
        this.recyclerView.setAdapter(this.mAdapter);
    }
}
