package com.inteliclinic.neuroon.fragments.first_time;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.inteliclinic.neuroon.views.pickers.CenterScrollListener;
import com.inteliclinic.neuroon.views.pickers.IIndicatorable;
import com.inteliclinic.neuroon.views.pickers.PickerItemDecoration;
import com.inteliclinic.neuroon.views.pickers.PickerView;
import com.inteliclinic.neuroon.views.pickers.UnitAdapter;

public class WeightFragment extends BaseFragment {
    private static final String ARG_STANDALONE = "is_standalone";
    /* access modifiers changed from: private */
    public double mConfigValue;
    @InjectView(2131755263)
    LightTextView mHeightMetrics;
    @InjectView(2131755262)
    LightTextView mHeightNormal;
    @InjectView(2131755236)
    ImageView mIndicator;
    private boolean mIsStandalone;
    private OnFragmentInteractionListener mListener;
    private RecyclerView.Adapter<? extends RecyclerView.ViewHolder> mMetricsAdapter;
    @InjectView(2131755264)
    FrameLayout mMetricsContainer;
    @InjectView(2131755265)
    ImageView mMetricsIndicator;
    @InjectView(2131755266)
    RecyclerView mMetricsPicker;
    private RecyclerView mMetricsRecyclerView;
    /* access modifiers changed from: private */
    public int mMetricsValue;
    @InjectView(2131755132)
    ThinButton mNext;
    private RecyclerView.Adapter<? extends RecyclerView.ViewHolder> mNormalAdapter;
    @InjectView(2131755260)
    FrameLayout mNormalContainer;
    private RecyclerView mNormalRecyclerView;
    /* access modifiers changed from: private */
    public int mNormalValue;
    @InjectView(2131755261)
    RecyclerView mPicker;
    @InjectView(2131755134)
    LinearLayout mStepIndicator;

    public interface OnFragmentInteractionListener {
        void openAboutYou();
    }

    public static WeightFragment newInstance() {
        return newInstance(false);
    }

    public static WeightFragment newInstance(boolean isStandalone) {
        WeightFragment fragment = new WeightFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_STANDALONE, isStandalone);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mIsStandalone = getArguments().getBoolean(ARG_STANDALONE);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weight, container, false);
        ButterKnife.inject((Object) this, view);
        this.mNormalValue = 1;
        this.mMetricsValue = 2;
        initPickers();
        setStartValue();
        return view;
    }

    private void setStartValue() {
        this.mConfigValue = AccountManager.getInstance().getWeight();
        this.mNormalValue = (int) this.mConfigValue;
        setNormalValue();
        this.mMetricsValue = UnitConverter.kgToLbs(this.mConfigValue);
        setMetricsValue();
    }

    public void onResume() {
        super.onResume();
        getTracker().setScreenName("weight");
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
        if (this.mIsStandalone) {
            this.mNext.setText(R.string.save);
            this.mStepIndicator.setVisibility(4);
        }
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
        AccountManager.getInstance().saveWeight(this.mConfigValue);
        if (this.mListener != null) {
            this.mListener.openAboutYou();
        } else if (this.mIsStandalone) {
            getFragmentManager().popBackStack();
        }
    }

    private void initPickers() {
        this.mNormalAdapter = new UnitAdapter(this.mIndicator, "", 1, 200);
        this.mNormalRecyclerView = initPicker(this.mPicker, this.mNormalContainer, this.mIndicator, this.mNormalAdapter, false, new PickerView.OnItemSelectedListener() {
            public void onItemSelected(RecyclerView recyclerView, View centerView, int position) {
                double unused = WeightFragment.this.mConfigValue = (double) (position + 1);
                int unused2 = WeightFragment.this.mNormalValue = (int) WeightFragment.this.mConfigValue;
                if (WeightFragment.this.mHeightNormal != null) {
                    WeightFragment.this.mHeightNormal.setText(WeightFragment.this.getString(R.string.some_kg, new Object[]{Integer.valueOf(WeightFragment.this.mNormalValue)}));
                }
                int unused3 = WeightFragment.this.mMetricsValue = (int) (((double) WeightFragment.this.mNormalValue) * 2.20462d);
                WeightFragment.this.setMetricsValue();
            }
        });
        this.mMetricsAdapter = new UnitAdapter(this.mMetricsIndicator, "", 1, 440);
        this.mMetricsRecyclerView = initPicker(this.mMetricsPicker, this.mMetricsContainer, this.mMetricsIndicator, this.mMetricsAdapter, false, new PickerView.OnItemSelectedListener() {
            public void onItemSelected(RecyclerView recyclerView, View centerView, int position) {
                int unused = WeightFragment.this.mMetricsValue = position + 1;
                if (WeightFragment.this.mHeightMetrics != null) {
                    WeightFragment.this.mHeightMetrics.setText(WeightFragment.this.getString(R.string.some_lbs, new Object[]{Integer.valueOf(WeightFragment.this.mMetricsValue)}));
                }
                double unused2 = WeightFragment.this.mConfigValue = UnitConverter.lbsToKg(WeightFragment.this.mMetricsValue);
                int unused3 = WeightFragment.this.mNormalValue = (int) WeightFragment.this.mConfigValue;
                WeightFragment.this.setNormalValue();
            }
        });
        this.mNormalContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                WeightFragment.this.setValue();
                WeightFragment.this.mNormalContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });
    }

    /* access modifiers changed from: private */
    public void setNormalValue() {
        this.mNormalRecyclerView.scrollToPosition(this.mNormalValue - 1);
        if (this.mHeightNormal != null) {
            this.mHeightNormal.setText(getString(R.string.some_kg, new Object[]{Integer.valueOf(this.mNormalValue)}));
        }
    }

    /* access modifiers changed from: private */
    public void setMetricsValue() {
        this.mMetricsRecyclerView.scrollToPosition(this.mMetricsValue - 1);
        if (this.mHeightMetrics != null) {
            this.mHeightMetrics.setText(getString(R.string.some_lbs, new Object[]{Integer.valueOf(this.mMetricsValue)}));
        }
    }

    /* access modifiers changed from: private */
    public void setValue() {
        setNormalValue();
        setMetricsValue();
    }

    /* access modifiers changed from: protected */
    public RecyclerView initPicker(RecyclerView recyclerView, View container, final View indicator, RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter, boolean reverseAdapter, PickerView.OnItemSelectedListener listener) {
        recyclerView.setHasFixedSize(true);
        final View view = indicator;
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), 1, reverseAdapter) {
            public void scrollToPosition(int position) {
                super.scrollToPositionWithOffset(position, view.getTop());
            }
        });
        recyclerView.addItemDecoration(new PickerItemDecoration(indicator, container, reverseAdapter));
        recyclerView.addOnScrollListener(new CenterScrollListener(new IIndicatorable() {
            public View getIndicator() {
                return indicator;
            }
        }, listener));
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }
}
