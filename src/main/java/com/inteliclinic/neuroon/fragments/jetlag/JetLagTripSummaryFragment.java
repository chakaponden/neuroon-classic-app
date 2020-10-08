package com.inteliclinic.neuroon.fragments.jetlag;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.managers.therapy.TherapyCreatedEvent;
import com.inteliclinic.neuroon.managers.therapy.TherapyManager;
import com.inteliclinic.neuroon.managers.trip.TripManager;
import com.inteliclinic.neuroon.managers.trip.models.CurrentTrip;
import com.inteliclinic.neuroon.models.network.Flight;
import com.inteliclinic.neuroon.views.BaseTextView;
import com.inteliclinic.neuroon.views.BoldTextView;
import com.inteliclinic.neuroon.views.LightTextView;
import com.inteliclinic.neuroon.views.ThinButton;
import com.inteliclinic.neuroon.views.ThinTextView;
import com.inteliclinic.neuroon.views.WorldMapRoadView;
import de.greenrobot.event.EventBus;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class JetLagTripSummaryFragment extends BaseFragment {
    @InjectView(2131755120)
    ThinButton button;
    @InjectView(2131755267)
    FrameLayout buttonContainer;
    @InjectView(2131755329)
    BoldTextView destinationCity;
    @InjectView(2131755330)
    LightTextView destinationCityCode;
    @InjectView(2131755331)
    ThinTextView destinationStartDate;
    @InjectView(2131755332)
    BoldTextView destinationStartTime;
    @InjectView(2131755320)
    ThinTextView journeyDuration;
    @InjectView(2131755328)
    LinearLayout mContainer;
    private OnFragmentInteractionListener mListener;
    @InjectView(2131755284)
    WorldMapRoadView map;
    @InjectView(2131755322)
    ThinTextView numberOfFlights;
    @InjectView(2131755323)
    BoldTextView originCity;
    @InjectView(2131755324)
    LightTextView originCityCode;
    @InjectView(2131755327)
    LightTextView originFlightNumber;
    @InjectView(2131755325)
    ThinTextView originStartDate;
    @InjectView(2131755326)
    BoldTextView originStartTime;
    @InjectView(2131755134)
    LinearLayout stepIndicator;
    @InjectViews({2131755319, 2131755321})
    LightTextView[] topTitles;

    public interface OnFragmentInteractionListener {
        void therapyCreated();
    }

    public static JetLagTripSummaryFragment newInstance() {
        return new JetLagTripSummaryFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jet_lag_trip_summary, container, false);
        ButterKnife.inject((Object) this, view);
        setContent(view);
        return view;
    }

    private void setContent(View view) {
        CurrentTrip currentTrip = TripManager.getInstance().getCurrentTrip();
        if (currentTrip != null) {
            currentTrip.setStayLength(100);
            if (currentTrip.getFlights().size() <= 0 || currentTrip.getFlights().get(0).getFlightTime() == null) {
                setTopHeaderVisibility(8);
                this.originCity.setText(currentTrip.getOrigin().getCity().toUpperCase());
                if (currentTrip.getOrigin().getIata() != null) {
                    this.originCityCode.setText(getString(R.string.in_parenth, new Object[]{currentTrip.getOrigin().getIata().toUpperCase()}));
                }
                this.originStartDate.setText(new SimpleDateFormat("MMM dd, yyyy").format(currentTrip.getFlightDate()));
                this.originFlightNumber.setVisibility(4);
                this.destinationCity.setText(currentTrip.getDestination().getCity().toUpperCase());
                if (currentTrip.getOrigin().getIata() != null) {
                    this.destinationCityCode.setText(getString(R.string.in_parenth, new Object[]{currentTrip.getDestination().getIata().toUpperCase()}));
                }
                this.destinationStartDate.setText(new SimpleDateFormat("MMM dd, yyyy").format(currentTrip.getFlightDate()));
                return;
            }
            this.mContainer.removeAllViews();
            int journeyDurationTime = 0;
            for (int i = 0; i < currentTrip.getFlights().size(); i++) {
                Flight flight = currentTrip.getFlights().get(i);
                journeyDurationTime += flight.getFlightTime().intValue();
                if (i == 0) {
                    this.originCity.setText(flight.getOriginAirport().getCity().toUpperCase());
                    this.originCityCode.setText(getString(R.string.in_parenth, new Object[]{flight.getOriginAirport().getIata().toUpperCase()}));
                    this.originStartDate.setText(new SimpleDateFormat("MMM dd, yyyy").format(flight.getDepartureDate()));
                    this.originStartTime.setText(DateFormat.getTimeInstance(3).format(flight.getDepartureDate()));
                    this.originFlightNumber.setVisibility(0);
                    this.originFlightNumber.setText(flight.getFlightNumber());
                    addFlightTime(view, flight, this.mContainer);
                }
                if (i == currentTrip.getFlights().size() - 1) {
                    this.destinationCity.setText(flight.getDestinationAirport().getCity().toUpperCase());
                    this.destinationCityCode.setText(getString(R.string.in_parenth, new Object[]{flight.getDestinationAirport().getIata().toUpperCase()}));
                    this.destinationStartDate.setText(new SimpleDateFormat("MMM dd, yyyy").format(flight.getArrivalDate()));
                    this.destinationStartTime.setText(DateFormat.getTimeInstance(3).format(flight.getArrivalDate()));
                } else if (i != 0) {
                    addLayover(view, flight, currentTrip.getFlights().get(i - 1), this.mContainer);
                    addFlightTime(view, flight, this.mContainer);
                }
            }
            this.journeyDuration.setText(getString(R.string.some_minutes, new Object[]{Integer.valueOf(journeyDurationTime)}));
            this.numberOfFlights.setText(String.valueOf(currentTrip.getFlights().size()));
            setTopHeaderVisibility(0);
        }
    }

    private void addFlightTime(View view, Flight flight, LinearLayout container) {
        View inflate = View.inflate(view.getContext(), R.layout.layout_flight_time, (ViewGroup) null);
        ((BaseTextView) inflate.findViewById(R.id.flight_time)).setText(getString(R.string.some_minutes, new Object[]{flight.getFlightTime()}));
        container.addView(inflate);
    }

    private void addLayover(View view, Flight flight, Flight oldFlight, LinearLayout container) {
        View inflate = View.inflate(view.getContext(), R.layout.layout_layover, (ViewGroup) null);
        ((BaseTextView) inflate.findViewById(R.id.layover_city)).setText(flight.getOriginAirport().getCity());
        ((BaseTextView) inflate.findViewById(R.id.layover_city_code)).setText(getString(R.string.in_parenth, new Object[]{flight.getOriginAirport().getIata().toUpperCase()}));
        ((BaseTextView) inflate.findViewById(R.id.layover_time)).setText(getString(R.string.some_minutes, new Object[]{Integer.valueOf((int) (((flight.getDepartureDate().getTime() - oldFlight.getDepartureDate().getTime()) / 60000) - ((long) oldFlight.getFlightTime().intValue())))}));
        container.addView(inflate);
    }

    private void setTopHeaderVisibility(int visibility) {
        for (LightTextView title : this.topTitles) {
            title.setVisibility(visibility);
        }
        this.journeyDuration.setVisibility(visibility);
        this.numberOfFlights.setVisibility(visibility);
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

    public void onEvent(TherapyCreatedEvent event) {
        EventBus.getDefault().removeStickyEvent((Object) event);
        if (this.mListener != null) {
            this.mListener.therapyCreated();
        }
    }

    @OnClick({2131755120})
    public void onNextClick() {
        CurrentTrip currentTrip = TripManager.getInstance().getCurrentTrip();
        TherapyManager.getInstance().planTherapy(currentTrip.getTimeDiff(), currentTrip.getFlightDate(), currentTrip.isReturn(), currentTrip.getStayLength(), currentTrip.getOrigin(), currentTrip.getDestination(), currentTrip.getFlights());
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
