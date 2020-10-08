package com.inteliclinic.neuroon.fragments.jetlag;

import android.app.Activity;
import android.os.Bundle;
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
import com.inteliclinic.neuroon.managers.therapy.TherapyManager;
import com.inteliclinic.neuroon.managers.therapy.models.CurrentTherapy;
import com.inteliclinic.neuroon.utils.DateUtils;
import com.inteliclinic.neuroon.views.BoldTextView;
import com.inteliclinic.neuroon.views.ThinTextView;
import com.inteliclinic.neuroon.views.calendar.PartialMonthView;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JetLagTherapySummaryFragment extends BaseFragment {
    @InjectView(2131755308)
    PartialMonthView firstMonth;
    @InjectView(2131755306)
    BoldTextView firstMonthName;
    private OnFragmentInteractionListener mListener;
    @InjectView(2131755310)
    LinearLayout secondDaysHeader;
    @InjectView(2131755311)
    PartialMonthView secondMonth;
    @InjectView(2131755309)
    BoldTextView secondMonthName;
    @InjectView(2131755318)
    ThinTextView therapyEnd;
    @InjectView(2131755286)
    ThinTextView therapyLength;
    @InjectView(2131755316)
    ThinTextView therapyStart;
    @InjectView(2131755295)
    ThinTextView timezones;

    public interface OnFragmentInteractionListener {
        void onStartTherapy();
    }

    public static JetLagTherapySummaryFragment newInstance() {
        return new JetLagTherapySummaryFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jet_lag_therapy_summary, container, false);
        ButterKnife.inject((Object) this, view);
        setContent();
        return view;
    }

    private void setContent() {
        CurrentTherapy currentTherapy = TherapyManager.getInstance().getCurrentTherapy();
        if (currentTherapy != null) {
            int dayCount = currentTherapy.getTherapyDuration();
            this.therapyLength.setText(getResources().getQuantityString(R.plurals.some_days, dayCount, new Object[]{Integer.valueOf(dayCount)}));
            this.timezones.setText(getString(R.string.some_hours, new Object[]{currentTherapy.getTimeDifferenceAsString()}));
            this.therapyStart.setText(new SimpleDateFormat("dd MMM. yyyy").format(currentTherapy.getStartTherapyDate()));
            this.therapyEnd.setText(new SimpleDateFormat("dd MMM. yyyy").format(currentTherapy.getEndTherapyDate()));
            setCalendarContent(currentTherapy);
        }
    }

    private void setCalendarContent(CurrentTherapy currentTherapy) {
        int i = 0;
        this.firstMonthName.setText(new SimpleDateFormat("MMMM yyyy").format(currentTherapy.getStartTherapyDate()));
        this.firstMonth.setSelectedDays(currentTherapy.getStartTherapyDate(), currentTherapy.getTherapyDaysLeft(), DateUtils.sameMonth(currentTherapy.getStartTherapyDate(), currentTherapy.getFlightDate()) ? DateUtils.getDayOfMonth(currentTherapy.getFlightDate()) : 0);
        if (DateUtils.sameMonth(currentTherapy.getStartTherapyDate(), currentTherapy.getEndTherapyDate())) {
            this.secondMonthName.setVisibility(8);
            this.secondDaysHeader.setVisibility(8);
            this.secondMonth.setVisibility(8);
            return;
        }
        this.secondMonthName.setText(new SimpleDateFormat("MMMM yyyy").format(currentTherapy.getEndTherapyDate()));
        PartialMonthView partialMonthView = this.secondMonth;
        Date endTherapyDate = currentTherapy.getEndTherapyDate();
        int i2 = -currentTherapy.getTherapyDaysLeft();
        if (DateUtils.sameMonth(currentTherapy.getEndTherapyDate(), currentTherapy.getFlightDate())) {
            i = DateUtils.getDayOfMonth(currentTherapy.getFlightDate());
        }
        partialMonthView.setSelectedDays(endTherapyDate, i2, i);
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

    @OnClick({2131755120})
    public void onNextClick() {
        getTracker().send(new HitBuilders.EventBuilder().setCategory("Jet Lag View").setAction("Start therapy pressed (after planning)").setLabel("Start therapy pressed (after planning)").build());
        if (this.mListener != null) {
            this.mListener.onStartTherapy();
        }
    }
}
