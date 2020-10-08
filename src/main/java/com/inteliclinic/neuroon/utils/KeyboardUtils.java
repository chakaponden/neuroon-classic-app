package com.inteliclinic.neuroon.utils;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public final class KeyboardUtils {
    private KeyboardUtils() {
    }

    public static void hideKeyboard(Activity activity) {
        View v = activity.getWindow().getCurrentFocus();
        if (v != null) {
            ((InputMethodManager) activity.getSystemService("input_method")).hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
