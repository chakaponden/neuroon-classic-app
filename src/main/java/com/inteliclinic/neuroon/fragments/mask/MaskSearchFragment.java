package com.inteliclinic.neuroon.fragments.mask;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.gms.analytics.HitBuilders;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;

public class MaskSearchFragment extends BaseFragment {
    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onCancelSearch();
    }

    public static MaskSearchFragment newInstance() {
        return new MaskSearchFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mask_search, container, false);
        ButterKnife.inject((Object) this, view);
        return view;
    }

    public void onResume() {
        super.onResume();
        getTracker().setScreenName("searching");
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
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

    @OnClick({2131755374})
    public void onCancelClick() {
        if (this.mListener != null) {
            this.mListener.onCancelSearch();
        }
    }
}
