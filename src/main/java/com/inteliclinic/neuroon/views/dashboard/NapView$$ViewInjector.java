package com.inteliclinic.neuroon.views.dashboard;

import android.widget.ImageView;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.BoldTextView;
import com.inteliclinic.neuroon.views.ThinTextView;

public class NapView$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, NapView target, Object source) {
        target.mNapImage = (ImageView) finder.findRequiredView(source, R.id.nap_image, "field 'mNapImage'");
        target.mNapTitle = (BoldTextView) finder.findRequiredView(source, R.id.nap_title, "field 'mNapTitle'");
        target.mNapBestTake = (ThinTextView) finder.findRequiredView(source, R.id.nap_best_take, "field 'mNapBestTake'");
        target.mNapBestTime = (ThinTextView) finder.findRequiredView(source, R.id.nap_best_time, "field 'mNapBestTime'");
        target.mNapTime = (ThinTextView) finder.findRequiredView(source, R.id.nap_time, "field 'mNapTime'");
        target.mNapTimeUnit = (ThinTextView) finder.findRequiredView(source, R.id.nap_time_unit, "field 'mNapTimeUnit'");
        target.mNapTimeWhenever = (ThinTextView) finder.findRequiredView(source, R.id.nap_time_whenever, "field 'mNapTimeWhenever'");
    }

    public static void reset(NapView target) {
        target.mNapImage = null;
        target.mNapTitle = null;
        target.mNapBestTake = null;
        target.mNapBestTime = null;
        target.mNapTime = null;
        target.mNapTimeUnit = null;
        target.mNapTimeWhenever = null;
    }
}
