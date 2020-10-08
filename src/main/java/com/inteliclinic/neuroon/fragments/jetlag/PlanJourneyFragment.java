package com.inteliclinic.neuroon.fragments.jetlag;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.google.android.gms.analytics.HitBuilders;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.fragments.OnProgressListener;
import com.inteliclinic.neuroon.loaders.AirportLoader;
import com.inteliclinic.neuroon.managers.account.events.ReservationReceivingErrorEvent;
import com.inteliclinic.neuroon.managers.therapy.TherapyManager;
import com.inteliclinic.neuroon.managers.trip.NoReservationFound;
import com.inteliclinic.neuroon.managers.trip.TripManager;
import com.inteliclinic.neuroon.managers.trip.TripScheduledEvent;
import com.inteliclinic.neuroon.managers.trip.TripTooShortEvent;
import com.inteliclinic.neuroon.models.data.Airport;
import com.inteliclinic.neuroon.old_guava.Strings;
import com.inteliclinic.neuroon.utils.KeyboardUtils;
import com.inteliclinic.neuroon.views.LightAutoCompleteEditText;
import com.inteliclinic.neuroon.views.LightEditText;
import com.inteliclinic.neuroon.views.ThinButton;
import com.inteliclinic.neuroon.views.ThinTextView;
import com.inteliclinic.neuroon.views.calendar.CalendarPickerView;
import de.greenrobot.event.EventBus;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PlanJourneyFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<List<Airport>> {
    private AirportAdapter mAirportsAdapter;
    /* access modifiers changed from: private */
    public Date mArivalDate;
    @InjectView(2131755120)
    ThinButton mButton;
    @InjectView(2131755415)
    LightEditText mDate;
    @InjectView(2131755421)
    ThinTextView mDestinationBtn;
    private int mDestinationButtonSel;
    @InjectView(2131755412)
    LinearLayout mDestinationSection;
    /* access modifiers changed from: private */
    public String mFilterText;
    @InjectView(2131755413)
    LightAutoCompleteEditText mFrom;
    /* access modifiers changed from: private */
    public Airport mFromAirport;
    private OnFragmentInteractionListener mListener;
    @InjectView(2131755420)
    ThinTextView mReservationBtn;
    @InjectView(2131755418)
    LightEditText mReservationName;
    @InjectView(2131755417)
    LightAutoCompleteEditText mReservationNumber;
    @InjectView(2131755416)
    LinearLayout mReservationSection;
    @InjectView(2131755414)
    LightAutoCompleteEditText mTo;
    /* access modifiers changed from: private */
    public Airport mToAirport;

    public interface OnFragmentInteractionListener extends OnProgressListener {
        void destinationButtonClicked();

        void openBookingReferenceHelp();

        void tripScheduled();
    }

    public static PlanJourneyFragment newInstance() {
        return new PlanJourneyFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_plan_journey, container, false);
        ButterKnife.inject((Object) this, view);
        setupButton();
        setupDestinationButton();
        setupDestinationView();
        setupAutocomplete();
        setupReservation();
        TripManager.getInstance().clearTrip();
        return view;
    }

    public void onBackButtonClick() {
        super.onBackButtonClick();
        TherapyManager.getInstance().deleteTherapy();
    }

    private void setupReservation() {
        this.mReservationName.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                PlanJourneyFragment.this.checkFirstStep();
            }
        });
        this.mReservationNumber.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                PlanJourneyFragment.this.checkFirstStep();
            }
        });
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mAirportsAdapter = new AirportAdapter(getActivity());
        this.mFrom.setAdapter(this.mAirportsAdapter);
        this.mTo.setAdapter(this.mAirportsAdapter);
        Calendar instance = Calendar.getInstance();
        instance.add(5, 1);
        this.mArivalDate = instance.getTime();
        setDate();
        getLoaderManager().initLoader(0, (Bundle) null, this);
    }

    /* access modifiers changed from: private */
    public void setDate() {
        if (this.mArivalDate != null) {
            this.mDate.setText(new SimpleDateFormat("dd MMM. yyyy").format(this.mArivalDate));
        } else {
            this.mDate.setText("");
        }
    }

    private void setupAutocomplete() {
        this.mFrom.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                String unused = PlanJourneyFragment.this.mFilterText = s.toString();
                PlanJourneyFragment.this.getLoaderManager().restartLoader(0, (Bundle) null, PlanJourneyFragment.this);
            }
        });
        this.mTo.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                String unused = PlanJourneyFragment.this.mFilterText = s.toString();
                PlanJourneyFragment.this.getLoaderManager().restartLoader(0, (Bundle) null, PlanJourneyFragment.this);
            }
        });
        this.mFrom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Airport itemAtPosition = (Airport) parent.getItemAtPosition(position);
                Airport unused = PlanJourneyFragment.this.mFromAirport = itemAtPosition;
                PlanJourneyFragment.this.mFrom.setText(itemAtPosition.getCity() + ", " + itemAtPosition.getCountry());
                PlanJourneyFragment.this.checkFirstStep();
                KeyboardUtils.hideKeyboard(PlanJourneyFragment.this.getActivity());
            }
        });
        this.mTo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Airport itemAtPosition = (Airport) parent.getItemAtPosition(position);
                Airport unused = PlanJourneyFragment.this.mToAirport = itemAtPosition;
                PlanJourneyFragment.this.mTo.setText(itemAtPosition.getCity() + ", " + itemAtPosition.getCountry());
                PlanJourneyFragment.this.checkFirstStep();
                KeyboardUtils.hideKeyboard(PlanJourneyFragment.this.getActivity());
            }
        });
    }

    /* access modifiers changed from: private */
    public void checkFirstStep() {
        switch (this.mDestinationButtonSel) {
            case 1:
                if (!(this.mFromAirport == null || this.mToAirport == null)) {
                    this.mButton.setEnabled(true);
                    return;
                }
            default:
                if (!Strings.isNullOrEmpty(this.mReservationName.getText().toString()) && !Strings.isNullOrEmpty(this.mReservationNumber.getText().toString()) && this.mArivalDate != null) {
                    this.mButton.setEnabled(true);
                    return;
                }
        }
        this.mButton.setEnabled(false);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        EventBus.getDefault().registerSticky(this);
        try {
            this.mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
        this.mListener = null;
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @OnClick({2131755120})
    public void onNextFinishClick() {
        KeyboardUtils.hideKeyboard(getActivity());
        if (this.mDestinationButtonSel == 0) {
            if (this.mListener != null) {
                this.mListener.setProgress(true);
            }
            TripManager.getInstance().updateTrip(this.mReservationName.getText().toString(), this.mReservationNumber.getText().toString());
        } else if (this.mDestinationButtonSel == 1) {
            TripManager.getInstance().updateTrip(this.mFromAirport, this.mToAirport, this.mArivalDate);
        }
        setupButton();
    }

    /* access modifiers changed from: private */
    public void setupButton() {
        checkFirstStep();
    }

    public void onEvent(NoReservationFound event) {
        if (this.mListener != null) {
            this.mListener.setProgress(false);
            Snackbar.make(getView(), (CharSequence) getString(R.string.no_reservation_for_name, new Object[]{this.mReservationNumber.getText().toString(), this.mReservationName.getText().toString()}), 0).show();
        }
    }

    public void onEvent(TripTooShortEvent event) {
        if (this.mListener != null) {
            this.mListener.setProgress(false);
            Snackbar.make(getView(), (int) R.string.no_need_for_therapy, 0).show();
        }
    }

    public void onEvent(TripScheduledEvent event) {
        if (this.mListener != null) {
            this.mListener.setProgress(false);
            this.mListener.tripScheduled();
        }
    }

    public void onEvent(ReservationReceivingErrorEvent event) {
        if (this.mListener != null) {
            this.mListener.setProgress(false);
            Snackbar.make(getView(), (int) R.string.internet_seems_offline, 0).show();
        }
    }

    @OnClick({2131755421})
    public void onDestinationBtnClick() {
        getTracker().send(new HitBuilders.EventBuilder().setCategory("jet_lag").setAction("Reservation mode chosen").setLabel("Reservation mode chosen").build());
        TripManager.getInstance().clearTrip();
        this.mDestinationButtonSel = 1;
        setupDestinationButton();
        setupDestinationView();
        setupButton();
        if (this.mListener != null) {
            this.mListener.destinationButtonClicked();
        }
    }

    @OnClick({2131755420})
    public void onReservationBtnClick() {
        getTracker().send(new HitBuilders.EventBuilder().setCategory("jet_lag").setAction("Destination mode chosen").setLabel("Destination mode chosen").build());
        TripManager.getInstance().clearTrip();
        this.mDestinationButtonSel = 0;
        setupDestinationButton();
        setupDestinationView();
        setupButton();
    }

    @OnClick({2131755415})
    public void onCalendarBtnClick() {
        KeyboardUtils.hideKeyboard(getActivity());
        CalendarPickerView.show(getView(), 1, getString(R.string.departure_date), this.mArivalDate, 1, new CalendarPickerView.OnDatePickerListener() {
            public void pickDate(Date time) {
                Date unused = PlanJourneyFragment.this.mArivalDate = time;
                PlanJourneyFragment.this.setDate();
                PlanJourneyFragment.this.setupButton();
            }
        });
    }

    @OnClick({2131755333})
    public void onBookingReferenceCodeHelp() {
        KeyboardUtils.hideKeyboard(getActivity());
        if (this.mListener != null) {
            this.mListener.openBookingReferenceHelp();
        }
    }

    private void setupDestinationButton() {
        switch (this.mDestinationButtonSel) {
            case 1:
                this.mDestinationBtn.setEnabled(false);
                this.mReservationBtn.setEnabled(true);
                return;
            default:
                this.mDestinationBtn.setEnabled(true);
                this.mReservationBtn.setEnabled(false);
                return;
        }
    }

    private void setupDestinationView() {
        switch (this.mDestinationButtonSel) {
            case 1:
                this.mDestinationSection.setVisibility(0);
                this.mReservationSection.setVisibility(8);
                return;
            default:
                this.mDestinationSection.setVisibility(8);
                this.mReservationSection.setVisibility(0);
                return;
        }
    }

    public Loader<List<Airport>> onCreateLoader(int i, Bundle bundle) {
        return new AirportLoader(getActivity(), 5, this.mFilterText);
    }

    public void onLoadFinished(Loader<List<Airport>> loader, List<Airport> data) {
        this.mAirportsAdapter.setData(new ArrayList(data));
    }

    public void onLoaderReset(Loader<List<Airport>> loader) {
        this.mAirportsAdapter.setData((List<Airport>) null);
    }

    public class AirportAdapter extends ArrayAdapter<Airport> {
        private LayoutInflater mInflater;

        public AirportAdapter(Context ctx) {
            super(ctx, 17367050, new ArrayList());
            this.mInflater = (LayoutInflater) ctx.getSystemService("layout_inflater");
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = this.mInflater.inflate(R.layout.suggestion_item, parent, false);
            } else {
                view = convertView;
            }
            Airport item = (Airport) getItem(position);
            ((ThinTextView) view.findViewById(16908308)).setText(item.getCity() + ", " + item.getCountry() + " (" + item.getIata() + ")");
            return view;
        }

        public Filter getFilter() {
            return new Filter() {
                /* access modifiers changed from: protected */
                public Filter.FilterResults performFiltering(CharSequence constraint) {
                    return null;
                }

                /* access modifiers changed from: protected */
                public void publishResults(CharSequence constraint, Filter.FilterResults results) {
                }
            };
        }

        public void setData(List<Airport> data) {
            clear();
            if (data != null) {
                for (int i = 0; i < data.size(); i++) {
                    add(data.get(i));
                }
            }
        }
    }
}
