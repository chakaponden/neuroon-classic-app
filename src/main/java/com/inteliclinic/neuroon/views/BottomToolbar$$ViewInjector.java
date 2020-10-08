package com.inteliclinic.neuroon.views;

import android.view.View;
import android.widget.ImageView;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;

public class BottomToolbar$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final BottomToolbar target, Object source) {
        target.personalPauseText = (LightTextView) finder.findRequiredView(source, R.id.personal_pause_text, "field 'personalPauseText'");
        target.sleepScoreText = (LightTextView) finder.findRequiredView(source, R.id.sleep_score_text, "field 'sleepScoreText'");
        target.lightboostText = (LightTextView) finder.findRequiredView(source, R.id.lightboost_text, "field 'lightboostText'");
        target.toolbarBackground = (ImageView) finder.findRequiredView(source, R.id.toolbar_background, "field 'toolbarBackground'");
        View view = finder.findRequiredView(source, R.id.naps_button, "field 'napsButton' and method 'onNapClick'");
        target.napsButton = (ImageView) view;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onNapClick();
            }
        });
        View view2 = finder.findRequiredView(source, R.id.sleep_score_button, "field 'sleepScoreButton' and method 'onSleepScoreClick'");
        target.sleepScoreButton = (ImageView) view2;
        view2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onSleepScoreClick();
            }
        });
        View view3 = finder.findRequiredView(source, R.id.lightboost_button, "field 'lightboostButton' and method 'onLightBoostClick'");
        target.lightboostButton = (ImageView) view3;
        view3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onLightBoostClick();
            }
        });
    }

    public static void reset(BottomToolbar target) {
        target.personalPauseText = null;
        target.sleepScoreText = null;
        target.lightboostText = null;
        target.toolbarBackground = null;
        target.napsButton = null;
        target.sleepScoreButton = null;
        target.lightboostButton = null;
    }
}
