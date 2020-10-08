package com.inteliclinic.neuroon.fragments.jetlag;

import android.os.Bundle;
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
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.views.ThinTextView;

public class JetLagTherapyEventsDescriptionFragment extends BaseFragment {
    @InjectView(2131755300)
    ThinTextView asleep;
    @InjectView(2131755299)
    ThinTextView awake;
    @InjectView(2131755298)
    ImageView imageBrainwaves;
    @InjectView(2131755301)
    ThinTextView startTherapy;

    public static JetLagTherapyEventsDescriptionFragment newInstance() {
        return new JetLagTherapyEventsDescriptionFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_jet_lag_therapy_events_description, container, false);
        ButterKnife.inject((Object) this, view);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                JetLagTherapyEventsDescriptionFragment.this.setTexts();
            }
        });
        return view;
    }

    /* access modifiers changed from: private */
    public void setTexts() {
        double imageWidth = ((double) this.imageBrainwaves.getWidth()) / 961.0d;
        double h = (((double) this.imageBrainwaves.getHeight()) / 535.0d) * 342.0d;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.awake.getLayoutParams();
        layoutParams.leftMargin = (int) ((541.0d * imageWidth) - ((double) (this.awake.getWidth() / 2)));
        layoutParams.topMargin = (int) h;
        this.awake.setLayoutParams(layoutParams);
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) this.asleep.getLayoutParams();
        layoutParams2.leftMargin = (int) ((846.0d * imageWidth) - ((double) (this.asleep.getWidth() / 2)));
        layoutParams2.topMargin = (int) h;
        this.asleep.setLayoutParams(layoutParams2);
        LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) this.startTherapy.getLayoutParams();
        layoutParams3.leftMargin = (int) (((846.0d * imageWidth) - ((double) (this.startTherapy.getWidth() / 2))) + ((double) ((ViewGroup.MarginLayoutParams) ((FrameLayout) this.asleep.getParent()).getLayoutParams()).leftMargin));
        this.startTherapy.setLayoutParams(layoutParams3);
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void onCloseButtonClick() {
        getFragmentManager().popBackStack();
    }

    @OnClick({2131755120})
    public void onCloseClick() {
        onCloseButtonClick();
    }
}
