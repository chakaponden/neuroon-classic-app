package com.inteliclinic.neuroon.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;
import com.inteliclinic.neuroon.utils.FontUtils;

public class LightEditText extends EditText {
    public LightEditText(Context context) {
        super(context);
        init();
    }

    public LightEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LightEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public LightEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setTypeface(FontUtils.getFont(getContext(), 1));
    }
}
