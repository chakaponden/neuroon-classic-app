package com.inteliclinic.neuroon.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.android.gms.analytics.HitBuilders;
import com.inteliclinic.neuroon.R;
import com.pnikosis.materialishprogress.ProgressWheel;

public class LauncherActivityFragment extends BaseFragment {
    @InjectView(2131755336)
    ProgressWheel progressWheel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_launcher, container, false);
        ButterKnife.inject((Object) this, view);
        setDone(true);
        return view;
    }

    public void onResume() {
        super.onResume();
        getTracker().setScreenName("hello");
        getTracker().send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void setDone(boolean isDone) {
        if (isDone) {
            Animation a = new Animation() {
                /* access modifiers changed from: protected */
                public void applyTransformation(float interpolatedTime, Transformation t) {
                }
            };
            a.setDuration(1500);
            a.setStartOffset(1000);
            a.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    if (LauncherActivityFragment.this.progressWheel != null) {
                        LauncherActivityFragment.this.progressWheel.setVisibility(0);
                    }
                }

                public void onAnimationRepeat(Animation animation) {
                }
            });
            this.progressWheel.startAnimation(a);
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
