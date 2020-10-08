package com.inteliclinic.neuroon.fragments.onboarding;

import android.view.View;
import android.widget.FrameLayout;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;
import com.inteliclinic.neuroon.views.ThinTextView;

public class SetUpFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final SetUpFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        View view = finder.findRequiredView(source, R.id.sign_up, "field 'signUp' and method 'signUp'");
        target.signUp = (FrameLayout) view;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.signUp();
            }
        });
        View view2 = finder.findRequiredView(source, R.id.log_in, "field 'logIn' and method 'logIn'");
        target.logIn = (FrameLayout) view2;
        view2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.logIn();
            }
        });
        target.questions = (ThinTextView) finder.findRequiredView(source, R.id.questions, "field 'questions'");
        View view3 = finder.findRequiredView(source, R.id.questions_btn, "field 'questionsBtn' and method 'onQuestionsClick'");
        target.questionsBtn = (FrameLayout) view3;
        view3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onQuestionsClick();
            }
        });
    }

    public static void reset(SetUpFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.signUp = null;
        target.logIn = null;
        target.questions = null;
        target.questionsBtn = null;
    }
}
