package com.inteliclinic.neuroon.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;
import com.inteliclinic.neuroon.utils.FontUtils;

public class LightAutoCompleteEditText extends AutoCompleteTextView {
    public LightAutoCompleteEditText(Context context) {
        super(context);
        init();
    }

    public LightAutoCompleteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LightAutoCompleteEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public LightAutoCompleteEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setTypeface(FontUtils.getFont(getContext(), 1));
    }
}
