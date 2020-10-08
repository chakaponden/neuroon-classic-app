package com.inteliclinic.neuroon.fragments.first_time;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import io.fabric.sdk.android.services.settings.SettingsJsonConstants;

public class HeightFragment extends BaseFragment {
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
    private UnitAdapter mMetricsAdapter;
    @InjectView(2131755264)
    FrameLayout mMetricsContainer;
    @InjectView(2131755265)
    ImageView mMetricsIndicator;
    @InjectView(2131755266)
    RecyclerView mMetricsPicker;
    private RecyclerView mMetricsRecyclerView;
    /* access modifiers changed from: private */
    public double mMetricsStartValue = UnitConverter.cmToInch((double) this.mNormalStartValue);
    /* access modifiers changed from: private */
    public int mMetricsValue;
    @InjectView(2131755132)
    ThinButton mNext;
    private UnitAdapter mNormalAdapter;
    @InjectView(2131755260)
    FrameLayout mNormalContainer;
    private RecyclerView mNormalRecyclerView;
    /* access modifiers changed from: private */
    public int mNormalStartValue = 30;
    /* access modifiers changed from: private */
    public int mNormalValue;
    @InjectView(2131755261)
    RecyclerView mPicker;
    @InjectView(2131755134)
    LinearLayout mStepIndicator;

    public interface OnFragmentInteractionListener {
        void openWeight();
    }

    public static HeightFragment newInstance() {
        return new HeightFragment();
    }

    public static HeightFragment newInstance(boolean isStandalone) {
        HeightFragment fragment = new HeightFragment();
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
        View view = inflater.inflate(R.layout.fragment_height, container, false);
        ButterKnife.inject((Object) this, view);
        initPickers();
        setStartValue();
        return view;
    }

    private void setStartValue() {
        this.mConfigValue = AccountManager.getInstance().getHeight();
        this.mNormalValue = ((int) this.mConfigValue) - this.mNormalStartValue;
        setNormalValue();
        this.mMetricsValue = (int) UnitConverter.cmToInch((double) this.mNormalValue);
        setMetricsValue();
    }

    public void onResume() {
        super.onResume();
        setValue();
        getTracker().setScreenName(SettingsJsonConstants.ICON_HEIGHT_KEY);
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
        AccountManager.getInstance().saveHeight(this.mConfigValue);
        if (this.mListener != null) {
            this.mListener.openWeight();
        } else if (this.mIsStandalone) {
            getFragmentManager().popBackStack();
        }
    }

    private void initPickers() {
        this.mNormalAdapter = new UnitAdapter(this.mIndicator, "", this.mNormalStartValue, ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION);
        this.mNormalRecyclerView = initPicker(this.mPicker, this.mNormalContainer, this.mIndicator, this.mNormalAdapter, false, new PickerView.OnItemSelectedListener() {
            public void onItemSelected(RecyclerView recyclerView, View centerView, int position) {
                double unused = HeightFragment.this.mConfigValue = (double) (HeightFragment.this.mNormalStartValue + position);
                int unused2 = HeightFragment.this.mNormalValue = position;
                if (HeightFragment.this.mHeightNormal != null) {
                    HeightFragment.this.mHeightNormal.setText(HeightFragment.this.getString(R.string.some_cm, new Object[]{Integer.valueOf(HeightFragment.this.mNormalValue + HeightFragment.this.mNormalStartValue)}));
                }
                int unused3 = HeightFragment.this.mMetricsValue = (int) (UnitConverter.cmToInch((double) (HeightFragment.this.mNormalValue + HeightFragment.this.mNormalStartValue)) - HeightFragment.this.mMetricsStartValue);
                HeightFragment.this.setMetricsValue();
            }
        });
        this.mMetricsAdapter = new HeightAdapter(this.mMetricsIndicator, "", (int) this.mMetricsStartValue, 98);
        this.mMetricsRecyclerView = initPicker(this.mMetricsPicker, this.mMetricsContainer, this.mMetricsIndicator, this.mMetricsAdapter, false, new PickerView.OnItemSelectedListener() {
            public void onItemSelected(RecyclerView recyclerView, View centerView, int position) {
                double unused = HeightFragment.this.mConfigValue = UnitConverter.inchToCm(HeightFragment.this.mMetricsStartValue + ((double) position));
                int unused2 = HeightFragment.this.mMetricsValue = position;
                if (HeightFragment.this.mHeightMetrics != null) {
                    HeightFragment.this.mHeightMetrics.setText(HeightFragment.this.getString(R.string.some_height, new Object[]{Integer.valueOf((int) ((HeightFragment.this.mMetricsStartValue + ((double) HeightFragment.this.mMetricsValue)) / 12.0d)), Integer.valueOf((int) ((HeightFragment.this.mMetricsStartValue + ((double) HeightFragment.this.mMetricsValue)) % 12.0d))}));
                }
                int unused3 = HeightFragment.this.mNormalValue = ((int) UnitConverter.inchToCm(((double) HeightFragment.this.mMetricsValue) + HeightFragment.this.mMetricsStartValue)) - HeightFragment.this.mNormalStartValue;
                HeightFragment.this.setNormalValue();
            }
        });
        this.mNormalContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                HeightFragment.this.setValue();
                HeightFragment.this.mNormalContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    /* access modifiers changed from: private */
    public void setNormalValue() {
        this.mNormalRecyclerView.scrollToPosition(this.mNormalValue);
        if (this.mHeightNormal != null) {
            this.mHeightNormal.setText(getString(R.string.some_cm, new Object[]{Integer.valueOf(this.mNormalValue + this.mNormalStartValue)}));
        }
    }

    /* access modifiers changed from: private */
    public void setMetricsValue() {
        this.mMetricsRecyclerView.scrollToPosition(this.mMetricsValue);
        if (this.mHeightMetrics != null) {
            this.mHeightMetrics.setText(getString(R.string.some_height, new Object[]{Integer.valueOf((int) ((this.mMetricsStartValue + ((double) this.mMetricsValue)) / 12.0d)), Integer.valueOf((int) ((this.mMetricsStartValue + ((double) this.mMetricsValue)) % 12.0d))}));
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

    public class HeightAdapter extends UnitAdapter {
        public HeightAdapter(View indicator, String unit, int startValue, int endValue) {
            super(indicator, unit, startValue, endValue);
        }

        public void onBindViewHolder(UnitAdapter.ViewHolder holder, int position) {
            holder.getTextView().setText(HeightFragment.this.getString(R.string.some_height, new Object[]{Integer.valueOf((getStartValue() + position) / 12), Integer.valueOf((getStartValue() + position) % 12)}));
        }

        public int getItemCount() {
            return (getEndValue() - getStartValue()) + 1;
        }
    }
}
