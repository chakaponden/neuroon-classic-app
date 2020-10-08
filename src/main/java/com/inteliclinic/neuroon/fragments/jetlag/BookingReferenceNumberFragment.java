package com.inteliclinic.neuroon.fragments.jetlag;

import android.os.Bundle;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.views.BaseTextView;
import com.inteliclinic.neuroon.views.ThinTextView;

public class BookingReferenceNumberFragment extends BaseFragment {
    @InjectView(2131755174)
    ThinTextView bookingDescription;

    public static BookingReferenceNumberFragment newInstance() {
        return new BookingReferenceNumberFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_reference_number, container, false);
        ButterKnife.inject((Object) this, view);
        setColor(this.bookingDescription, getString(R.string.booking_reference_number_description), getString(R.string.booking_reference_number_highlight), getResources().getColor(R.color.jet_lag_color));
        return view;
    }

    private void setColor(BaseTextView view, String fulltext, String subtext, int color) {
        view.setText(fulltext, TextView.BufferType.SPANNABLE);
        Spannable str = (Spannable) view.getText();
        int i = fulltext.indexOf(subtext);
        str.setSpan(new ForegroundColorSpan(color), i, subtext.length() + i, 33);
        str.setSpan(new StyleSpan(1), i, subtext.length() + i, 33);
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
