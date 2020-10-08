package com.inteliclinic.neuroon.fragments.first_time;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.google.android.gms.analytics.HitBuilders;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.managers.account.AccountManager;
import com.inteliclinic.neuroon.utils.UnitConverter;
import com.inteliclinic.neuroon.views.LightTextView;
import com.inteliclinic.neuroon.views.ThinButton;
import com.inteliclinic.neuroon.views.ThinTextView;
import com.inteliclinic.neuroon.views.pickers.CenterScrollListener;
import com.inteliclinic.neuroon.views.pickers.DaysAdapter;
import com.inteliclinic.neuroon.views.pickers.IIndicatorable;
import com.inteliclinic.neuroon.views.pickers.MonthAdapter;
import com.inteliclinic.neuroon.views.pickers.PickerItemDecoration;
import com.inteliclinic.neuroon.views.pickers.PickerView;
import com.inteliclinic.neuroon.views.pickers.YearAdapter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateOfBirthFragment extends BaseFragment implements IIndicatorable {
    private static final String ARG_STANDALONE = "is_standalone";
    @InjectView(2131755237)
    LightTextView birthDate;
    @InjectView(2131755234)
    ImageView calendar;
    @InjectView(2131755238)
    RecyclerView datePicker;
    @InjectView(2131755235)
    LinearLayout datePickerContainer;
    @InjectView(2131755236)
    ImageView indicator;
    /* access modifiers changed from: private */
    public Calendar mCalendar;
    private DaysAdapter mDaysAdapter;
    private RecyclerView mDaysRecyclerView;
    private boolean mIsStandalone;
    private OnFragmentInteractionListener mListener;
    private MonthAdapter mMonthAdapter;
    private RecyclerView mMonthRecyclerView;
    private YearAdapter mYearAdapter;
    private RecyclerView mYearRecyclerView;
    @InjectView(2131755239)
    RecyclerView monthPicker;
    @InjectView(2131755132)
    ThinButton next;
    @InjectView(2131755134)
    LinearLayout stepIndicator;
    @InjectView(2131755240)
    RecyclerView yearPicker;

    public interface OnFragmentInteractionListener {
        void openHeight();
    }

    public static DateOfBirthFragment newInstance(boolean isStandalone) {
        DateOfBirthFragment fragment = new DateOfBirthFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_STANDALONE, isStandalone);
        fragment.setArguments(args);
        return fragment;
    }

    public static DateOfBirthFragment newInstance() {
        return new DateOfBirthFragment();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mIsStandalone = getArguments().getBoolean(ARG_STANDALONE);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_date_of_birth, container, false);
        ButterKnife.inject((Object) this, view);
        this.mCalendar = Calendar.getInstance();
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                DateOfBirthFragment.this.setDateContainerMargins();
                DateOfBirthFragment.this.initPickers();
                view.postDelayed(new Runnable() {
                    public void run() {
                        DateOfBirthFragment.this.setDate();
                    }
                }, 100);
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        Integer date = AccountManager.getInstance().getBirthDate();
        if (date != null) {
            this.mCalendar.setTime(new Date(((long) date.intValue()) * 1000));
        }
        return view;
    }

    public void onResume() {
        super.onResume();
        getTracker().setScreenName("date_of_birth");
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
        if (this.mIsStandalone) {
            this.next.setText(R.string.save);
            this.stepIndicator.setVisibility(4);
        }
    }

    /* access modifiers changed from: private */
    public void setDateContainerMargins() {
        int width = this.calendar.getWidth();
        int height = this.calendar.getHeight();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.datePickerContainer.getLayoutParams();
        layoutParams.leftMargin = (int) ((((double) width) / 899.0d) * 60.0d);
        layoutParams.rightMargin = (int) ((((double) width) / 899.0d) * 60.0d);
        layoutParams.topMargin = (int) (((float) ((int) ((((double) height) / 1112.0d) * 305.0d))) + UnitConverter.convertDpToPixel(16.0f, this.calendar.getContext()));
        layoutParams.bottomMargin = (int) ((((double) height) / 1112.0d) * 62.0d);
        this.datePickerContainer.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.birthDate.getLayoutParams();
        layoutParams2.leftMargin = (int) ((((double) width) / 899.0d) * 50.0d);
        layoutParams2.rightMargin = (int) ((((double) width) / 899.0d) * 50.0d);
        layoutParams2.topMargin = (int) ((((double) height) / 1112.0d) * 137.0d);
        layoutParams2.bottomMargin = (int) ((((double) height) / 1112.0d) * 813.0d);
        this.birthDate.setLayoutParams(layoutParams2);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnFragmentInteractionListener) {
            this.mListener = (OnFragmentInteractionListener) activity;
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

    @OnClick({2131755132})
    public void onNextClick() {
        AccountManager.getInstance().setBirthDate(this.mCalendar.getTime());
        if (this.mListener != null) {
            this.mListener.openHeight();
        } else if (this.mIsStandalone) {
            getFragmentManager().popBackStack();
        }
    }

    /* access modifiers changed from: private */
    public void initPickers() {
        this.mDaysAdapter = new DaysAdapter(this.indicator, new Date());
        this.mDaysRecyclerView = initPicker(this.datePicker, this.mDaysAdapter, false, new PickerView.OnItemSelectedListener() {
            public void onItemSelected(RecyclerView recyclerView, View centerView, int position) {
                DateOfBirthFragment.this.mCalendar.set(5, position + 1);
            }
        });
        this.mMonthAdapter = new MonthAdapter(this.indicator);
        this.mMonthRecyclerView = initPicker(this.monthPicker, this.mMonthAdapter, false, new PickerView.OnItemSelectedListener() {
            public void onItemSelected(RecyclerView recyclerView, View centerView, int position) {
                DateOfBirthFragment.this.mCalendar.set(2, position);
                if (DateOfBirthFragment.this.mCalendar.get(2) != position) {
                    DateOfBirthFragment.this.mCalendar.set(2, position);
                    DateOfBirthFragment.this.mCalendar.set(5, DateOfBirthFragment.this.mCalendar.getActualMaximum(5));
                }
                DateOfBirthFragment.this.setDay();
            }
        });
        this.mYearAdapter = new YearAdapter(this.indicator);
        this.mYearRecyclerView = initPicker(this.yearPicker, this.mYearAdapter, true, new PickerView.OnItemSelectedListener() {
            public void onItemSelected(RecyclerView recyclerView, View centerView, int position) {
                int month = DateOfBirthFragment.this.mCalendar.get(2);
                if (!(centerView == null || !(centerView instanceof ThinTextView) || ((ThinTextView) centerView).getText() == null)) {
                    DateOfBirthFragment.this.mCalendar.set(1, Integer.parseInt(((ThinTextView) centerView).getText().toString()));
                }
                if (DateOfBirthFragment.this.mCalendar.get(2) != month) {
                    DateOfBirthFragment.this.mCalendar.set(2, month);
                    DateOfBirthFragment.this.mCalendar.set(5, DateOfBirthFragment.this.mCalendar.getActualMaximum(5));
                }
                DateOfBirthFragment.this.setDay();
            }
        });
        this.datePickerContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                DateOfBirthFragment.this.setDate();
                DateOfBirthFragment.this.datePickerContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
    }

    /* access modifiers changed from: protected */
    public RecyclerView initPicker(RecyclerView recyclerView, RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter, boolean reverseAdapter, PickerView.OnItemSelectedListener listener) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), 1, reverseAdapter) {
            public void scrollToPosition(int position) {
                if (DateOfBirthFragment.this.indicator != null) {
                    super.scrollToPositionWithOffset(position, DateOfBirthFragment.this.indicator.getTop());
                }
            }
        });
        recyclerView.addItemDecoration(new PickerItemDecoration(this.indicator, this.datePickerContainer, reverseAdapter));
        recyclerView.addOnScrollListener(new CenterScrollListener(this, listener));
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

    public void setDate() {
        setDay();
        setMonth();
        setYear();
    }

    private void setYear() {
        this.mYearRecyclerView.scrollToPosition(Calendar.getInstance().get(1) - this.mCalendar.get(1));
    }

    private void setMonth() {
        this.mMonthRecyclerView.scrollToPosition(this.mCalendar.get(2));
    }

    /* access modifiers changed from: private */
    public void setDay() {
        this.mDaysAdapter.setDate(this.mCalendar.getTime());
        this.mDaysRecyclerView.scrollToPosition(this.mCalendar.get(5) - 1);
        setDateText();
    }

    private void setDateText() {
        if (this.birthDate != null) {
            this.birthDate.setText(new SimpleDateFormat("dd MMM yyyy").format(this.mCalendar.getTime()));
        }
    }

    public View getIndicator() {
        return this.indicator;
    }
}
