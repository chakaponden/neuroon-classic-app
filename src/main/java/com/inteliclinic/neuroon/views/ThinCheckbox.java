package com.inteliclinic.neuroon.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import com.inteliclinic.neuroon.utils.FontUtils;

public class ThinCheckbox extends CheckBox {
    public ThinCheckbox(Context context) {
        super(context);
        init();
    }

    public ThinCheckbox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ThinCheckbox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setTypeface(FontUtils.getFont(getContext(), 2));
    }
}
