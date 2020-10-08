package com.inteliclinic.neuroon.fragments.jetlag;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.managers.therapy.TherapyManager;
import com.inteliclinic.neuroon.managers.therapy.models.CurrentTherapy;
import com.inteliclinic.neuroon.views.ThinTextView;
import com.inteliclinic.neuroon.views.WorldMapRoadView;
import com.inteliclinic.neuroon.views.charts.BiorhythmSynchronizationChartView;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JetLagProgressFragment extends BaseFragment {
    @InjectView(2131755297)
    BiorhythmSynchronizationChartView biorhythmSyncChart;
    private OnFragmentInteractionListener mListener;
    @InjectView(2131755284)
    WorldMapRoadView map;
    @InjectView(2131755291)
    ThinTextView therapyFrom;
    @InjectView(2131755286)
    ThinTextView therapyLength;
    @InjectView(2131755289)
    ThinTextView therapyStartsBefore;
    @InjectView(2131755288)
    ThinTextView therapyStartsLength;
    @InjectView(2131755293)
    ThinTextView therapyTo;
    @InjectView(2131755295)
    ThinTextView timezones;

    public interface OnFragmentInteractionListener {
        void goToJetLagMain();
    }

    public static JetLagProgressFragment newInstance() {
        return new JetLagProgressFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jet_lag_progress, container, false);
        ButterKnife.inject((Object) this, view);
        fillTherapy();
        return view;
    }

    private void fillTherapy() {
        CurrentTherapy currentTherapy = TherapyManager.getInstance().getCurrentTherapy();
        if (currentTherapy != null) {
            this.therapyLength.setText(getResources().getQuantityString(R.plurals.some_days, currentTherapy.getTherapyDuration(), new Object[]{Integer.valueOf(currentTherapy.getTherapyDuration())}));
            if (currentTherapy.getEvents().size() > 0) {
                Date startTherapyDate = currentTherapy.getEvents().get(0).getStartDate();
                this.therapyStartsLength.setText(new SimpleDateFormat("dd MMM. yyyy").format(startTherapyDate));
                int daysBeforeFlight = ((int) ((startTherapyDate.getTime() - currentTherapy.getFlightDate().getTime()) / 86400000)) + 1;
                if (daysBeforeFlight > 0) {
                    this.therapyStartsBefore.setText(getResources().getQuantityString(R.plurals.some_days_before_the_flight, daysBeforeFlight, new Object[]{Integer.valueOf(daysBeforeFlight)}));
                } else if (daysBeforeFlight < 0) {
                    this.therapyStartsBefore.setText(getResources().getQuantityString(R.plurals.some_days_after_the_flight, daysBeforeFlight, new Object[]{Integer.valueOf(daysBeforeFlight)}));
                }
            }
            this.therapyFrom.setText(currentTherapy.getOrigin().getCity());
            this.therapyTo.setText(currentTherapy.getDestination().getCity());
            this.map.setTimeZones(currentTherapy.getOrigin().getUTFOffset(), currentTherapy.getDestination().getUTFOffset(), currentTherapy.getProgress());
            this.timezones.setText(getString(R.string.some_hours, new Object[]{currentTherapy.getTimeDifferenceAsString()}));
            this.biorhythmSyncChart.setHoursDifference(currentTherapy.getTimeDifference());
        }
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

    @OnClick({2131755283})
    public void onBackButtonClick() {
        if (this.mListener != null) {
            this.mListener.goToJetLagMain();
        }
    }
}
