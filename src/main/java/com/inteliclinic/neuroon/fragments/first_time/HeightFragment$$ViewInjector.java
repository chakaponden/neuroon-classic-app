package com.inteliclinic.neuroon.fragments.first_time;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;
import com.inteliclinic.neuroon.views.LightTextView;
import com.inteliclinic.neuroon.views.ThinButton;

public class HeightFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final HeightFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.mPicker = (RecyclerView) finder.findRequiredView(source, R.id.picker, "field 'mPicker'");
        target.mIndicator = (ImageView) finder.findRequiredView(source, R.id.indicator, "field 'mIndicator'");
        target.mNormalContainer = (FrameLayout) finder.findRequiredView(source, R.id.normal_container, "field 'mNormalContainer'");
        target.mHeightNormal = (LightTextView) finder.findRequiredView(source, R.id.height_normal, "field 'mHeightNormal'");
        target.mHeightMetrics = (LightTextView) finder.findRequiredView(source, R.id.height_metrics, "field 'mHeightMetrics'");
        target.mMetricsPicker = (RecyclerView) finder.findRequiredView(source, R.id.metrics_picker, "field 'mMetricsPicker'");
        target.mMetricsIndicator = (ImageView) finder.findRequiredView(source, R.id.metrics_indicator, "field 'mMetricsIndicator'");
        target.mMetricsContainer = (FrameLayout) finder.findRequiredView(source, R.id.metrics_container, "field 'mMetricsContainer'");
        View view = finder.findRequiredView(source, R.id.next, "field 'mNext' and method 'onNextClick'");
        target.mNext = (ThinButton) view;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onNextClick();
            }
        });
        target.mStepIndicator = (LinearLayout) finder.findRequiredView(source, R.id.step_indicator, "field 'mStepIndicator'");
    }

    public static void reset(HeightFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.mPicker = null;
        target.mIndicator = null;
        target.mNormalContainer = null;
        target.mHeightNormal = null;
        target.mHeightMetrics = null;
        target.mMetricsPicker = null;
        target.mMetricsIndicator = null;
        target.mMetricsContainer = null;
        target.mNext = null;
        target.mStepIndicator = null;
    }
}
