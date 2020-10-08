package com.inteliclinic.neuroon.fragments;

import android.view.View;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.ThinTextView;

public class NavigationDrawerFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final NavigationDrawerFragment target, Object source) {
        target.talkWithUsDivider = finder.findRequiredView(source, R.id.talk_with_us_divider, "field 'talkWithUsDivider'");
        View view = finder.findRequiredView(source, R.id.talk_with_us, "field 'talkWithUs' and method 'onTalkWithUsClick'");
        target.talkWithUs = (ThinTextView) view;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onTalkWithUsClick();
            }
        });
        finder.findRequiredView(source, R.id.neuroon_mask, "method 'onNeuroonClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onNeuroonClick();
            }
        });
        finder.findRequiredView(source, R.id.profile, "method 'onProfileClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onProfileClick();
            }
        });
        finder.findRequiredView(source, R.id.alarms, "method 'onAlarmsClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onAlarmsClick();
            }
        });
        finder.findRequiredView(source, R.id.science_of_sleep, "method 'onScienceOfSleepClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onScienceOfSleepClick();
            }
        });
        finder.findRequiredView(source, R.id.terms_and_conditions, "method 'onTermsAndConditionsClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onTermsAndConditionsClick();
            }
        });
        finder.findRequiredView(source, R.id.about, "method 'onAboutClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onAboutClick();
            }
        });
        finder.findRequiredView(source, R.id.privacy_policy, "method 'onPrivacyPolicyClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onPrivacyPolicyClick();
            }
        });
        finder.findRequiredView(source, R.id.helpdesk, "method 'onFAQClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onFAQClick();
            }
        });
        finder.findRequiredView(source, R.id.report_a_problem, "method 'onReportAProblemClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onReportAProblemClick();
            }
        });
    }

    public static void reset(NavigationDrawerFragment target) {
        target.talkWithUsDivider = null;
        target.talkWithUs = null;
    }
}
