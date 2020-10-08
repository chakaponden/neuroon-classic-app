package com.inteliclinic.neuroon.fragments.mask;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;

public class MaskPickerFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final MaskPickerFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.mRecyclerView = (RecyclerView) finder.findRequiredView(source, R.id.my_recycler_view, "field 'mRecyclerView'");
        View view = finder.findRequiredView(source, R.id.pair, "field 'pair' and method 'onPairClick'");
        target.pair = (FrameLayout) view;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onPairClick();
            }
        });
        target.buttonBackground = (ImageView) finder.findRequiredView(source, R.id.button_background, "field 'buttonBackground'");
    }

    public static void reset(MaskPickerFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.mRecyclerView = null;
        target.pair = null;
        target.buttonBackground = null;
    }
}
