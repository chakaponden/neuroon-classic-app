package com.inteliclinic.neuroon.fragments.jetlag;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;
import com.inteliclinic.neuroon.views.BoldTextView;
import com.inteliclinic.neuroon.views.LightTextView;
import com.inteliclinic.neuroon.views.ThinButton;
import com.inteliclinic.neuroon.views.ThinTextView;
import com.inteliclinic.neuroon.views.WorldMapRoadView;

public class JetLagTripSummaryFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final JetLagTripSummaryFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.journeyDuration = (ThinTextView) finder.findRequiredView(source, R.id.journey_duration, "field 'journeyDuration'");
        target.numberOfFlights = (ThinTextView) finder.findRequiredView(source, R.id.number_of_flights, "field 'numberOfFlights'");
        target.originCity = (BoldTextView) finder.findRequiredView(source, R.id.origin_city, "field 'originCity'");
        target.originCityCode = (LightTextView) finder.findRequiredView(source, R.id.origin_city_code, "field 'originCityCode'");
        target.originStartDate = (ThinTextView) finder.findRequiredView(source, R.id.origin_start_date, "field 'originStartDate'");
        target.originStartTime = (BoldTextView) finder.findRequiredView(source, R.id.origin_start_time, "field 'originStartTime'");
        target.originFlightNumber = (LightTextView) finder.findRequiredView(source, R.id.origin_flight_number, "field 'originFlightNumber'");
        target.mContainer = (LinearLayout) finder.findRequiredView(source, R.id.flights_container, "field 'mContainer'");
        target.destinationCity = (BoldTextView) finder.findRequiredView(source, R.id.destination_city, "field 'destinationCity'");
        target.destinationCityCode = (LightTextView) finder.findRequiredView(source, R.id.destination_city_code, "field 'destinationCityCode'");
        target.destinationStartDate = (ThinTextView) finder.findRequiredView(source, R.id.destination_start_date, "field 'destinationStartDate'");
        target.destinationStartTime = (BoldTextView) finder.findRequiredView(source, R.id.destination_start_time, "field 'destinationStartTime'");
        View view = finder.findRequiredView(source, R.id.button, "field 'button' and method 'onNextClick'");
        target.button = (ThinButton) view;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onNextClick();
            }
        });
        target.buttonContainer = (FrameLayout) finder.findRequiredView(source, R.id.button_container, "field 'buttonContainer'");
        target.stepIndicator = (LinearLayout) finder.findRequiredView(source, R.id.step_indicator, "field 'stepIndicator'");
        target.map = (WorldMapRoadView) finder.findRequiredView(source, R.id.map, "field 'map'");
        target.topTitles = (LightTextView[]) ButterKnife.Finder.arrayOf((LightTextView) finder.findRequiredView(source, R.id.journey_duration_title, "topTitles"), (LightTextView) finder.findRequiredView(source, R.id.number_of_flights_title, "topTitles"));
    }

    public static void reset(JetLagTripSummaryFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.journeyDuration = null;
        target.numberOfFlights = null;
        target.originCity = null;
        target.originCityCode = null;
        target.originStartDate = null;
        target.originStartTime = null;
        target.originFlightNumber = null;
        target.mContainer = null;
        target.destinationCity = null;
        target.destinationCityCode = null;
        target.destinationStartDate = null;
        target.destinationStartTime = null;
        target.button = null;
        target.buttonContainer = null;
        target.stepIndicator = null;
        target.map = null;
        target.topTitles = null;
    }
}
