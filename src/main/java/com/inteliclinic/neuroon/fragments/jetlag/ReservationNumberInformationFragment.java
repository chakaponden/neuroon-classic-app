package com.inteliclinic.neuroon.fragments.jetlag;

import android.content.Context;
import android.os.Bundle;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.managers.account.AccountManager;
import com.inteliclinic.neuroon.views.BaseTextView;
import com.inteliclinic.neuroon.views.ThinCheckbox;
import com.inteliclinic.neuroon.views.ThinTextView;

public class ReservationNumberInformationFragment extends BaseFragment {
    @InjectView(2131755174)
    ThinTextView bookingDescription;
    @InjectView(2131755268)
    ThinCheckbox dontShow;
    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
    }

    public static ReservationNumberInformationFragment newInstance() {
        return new ReservationNumberInformationFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        boolean z = false;
        View view = inflater.inflate(R.layout.fragment_reservation_number_information, container, false);
        ButterKnife.inject((Object) this, view);
        setColor(this.bookingDescription, getString(R.string.reservation_number_description_1), getString(R.string.reservation_number_highlight_1), getResources().getColor(R.color.jet_lag_color));
        ThinCheckbox thinCheckbox = this.dontShow;
        if (!AccountManager.getInstance().hasToShowReservationNumberDialog()) {
            z = true;
        }
        thinCheckbox.setChecked(z);
        this.dontShow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                AccountManager.getInstance().dontShowReservationNumberDialog(isChecked);
            }
        });
        return view;
    }

    private void setColor(BaseTextView view, String fulltext, String subtext, int color) {
        view.setText(fulltext, TextView.BufferType.SPANNABLE);
        Spannable str = (Spannable) view.getText();
        int i = fulltext.indexOf(subtext);
        str.setSpan(new ForegroundColorSpan(color), i, subtext.length() + i, 33);
        str.setSpan(new StyleSpan(1), i, subtext.length() + i, 33);
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

    @OnClick({2131755175})
    public void onCloseButtonClick() {
        onBackButtonClick();
    }
}
