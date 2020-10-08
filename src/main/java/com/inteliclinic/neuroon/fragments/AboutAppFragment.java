package com.inteliclinic.neuroon.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.inteliclinic.neuroon.BuildConfig;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.LightTextView;

public class AboutAppFragment extends BaseFragment {
    private OnFragmentInteractionListener mListener;
    @InjectView(2131755137)
    LightTextView mVersion;

    public interface OnFragmentInteractionListener {
    }

    public static AboutAppFragment newInstance() {
        return new AboutAppFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_app, container, false);
        ButterKnife.inject((Object) this, view);
        this.mVersion.setText(getString(R.string.version_number, new Object[]{BuildConfig.VERSION_NAME}));
        return view;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnFragmentInteractionListener) {
            this.mListener = (OnFragmentInteractionListener) activity;
            return;
        }
        throw new RuntimeException(activity.toString() + " must implement OnFragmentInteractionListener");
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({2131755138})
    public void onLicensesClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.licenses);
        builder.setMessage(R.string.licenses_body).setPositiveButton(R.string.close, (DialogInterface.OnClickListener) null);
        builder.show();
    }
}
