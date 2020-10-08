package com.inteliclinic.neuroon.fragments;

import android.view.View;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;

public class SleepSurveyFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final SleepSurveyFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        finder.findRequiredView(source, R.id.refreshed_btn, "method 'onCurrentFeelClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onCurrentFeelClick(p0);
            }
        });
        finder.findRequiredView(source, R.id.tired_btn, "method 'onCurrentFeelClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onCurrentFeelClick(p0);
            }
        });
        finder.findRequiredView(source, R.id.too_cold_btn, "method 'onNightFeelClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onNightFeelClick(p0);
            }
        });
        finder.findRequiredView(source, R.id.normal_btn, "method 'onNightFeelClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onNightFeelClick(p0);
            }
        });
        finder.findRequiredView(source, R.id.too_hot_btn, "method 'onNightFeelClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onNightFeelClick(p0);
            }
        });
        finder.findRequiredView(source, R.id.comfortable_btn, "method 'onNightFeel2Click'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onNightFeel2Click(p0);
            }
        });
        finder.findRequiredView(source, R.id.pain_btn, "method 'onNightFeel2Click'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onNightFeel2Click(p0);
            }
        });
        finder.findRequiredView(source, R.id.dreams_no_btn, "method 'onBadDreamsClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onBadDreamsClick(p0);
            }
        });
        finder.findRequiredView(source, R.id.dreams_yes_btn, "method 'onBadDreamsClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onBadDreamsClick(p0);
            }
        });
        finder.findRequiredView(source, R.id.do_button, "method 'onSaveClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onSaveClick(p0);
            }
        });
    }

    public static void reset(SleepSurveyFragment target) {
        BaseFragment$$ViewInjector.reset(target);
    }
}
