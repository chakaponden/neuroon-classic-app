package com.inteliclinic.neuroon.fragments.loading;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.inteliclinic.neuroon.R;

public class JetLagLoadingFragment extends LoadingFragment {
    public static JetLagLoadingFragment newInstance() {
        return new JetLagLoadingFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_jet_lag_loading, container, false);
    }
}
