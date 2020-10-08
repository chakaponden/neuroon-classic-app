package com.inteliclinic.neuroon.fragments;

import android.view.View;
import android.widget.FrameLayout;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.LightTextView;

public class PairMaskStartFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final PairMaskStartFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.questions = (LightTextView) finder.findRequiredView(source, R.id.questions, "field 'questions'");
        View view = finder.findRequiredView(source, R.id.questions_btn, "field 'questionsBtn' and method 'onQuestionsClick'");
        target.questionsBtn = (FrameLayout) view;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onQuestionsClick();
            }
        });
        finder.findRequiredView(source, R.id.pair, "method 'onPairClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onPairClick();
            }
        });
        finder.findRequiredView(source, R.id.buy_now, "method 'onBuyNowClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onBuyNowClick();
            }
        });
    }

    public static void reset(PairMaskStartFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.questions = null;
        target.questionsBtn = null;
    }
}
