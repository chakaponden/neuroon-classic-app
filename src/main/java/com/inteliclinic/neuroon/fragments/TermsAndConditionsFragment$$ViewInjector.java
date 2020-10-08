package com.inteliclinic.neuroon.fragments;

import android.webkit.WebView;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;

public class TermsAndConditionsFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, TermsAndConditionsFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.webView = (WebView) finder.findRequiredView(source, R.id.webView, "field 'webView'");
    }

    public static void reset(TermsAndConditionsFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.webView = null;
    }
}
