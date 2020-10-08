package com.inteliclinic.neuroon.fragments;

import android.view.View;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.views.BottomToolbar;
import com.inteliclinic.neuroon.views.dashboard.NapView;

public class EnergyPlusFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final EnergyPlusFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.mBottomToolbar = (BottomToolbar) finder.findRequiredView(source, R.id.bottom_toolbar, "field 'mBottomToolbar'");
        View view = finder.findRequiredView(source, R.id.light_boost, "field 'lightBoost' and method 'onLightBoostClick'");
        target.lightBoost = (NapView) view;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onLightBoostClick();
            }
        });
        View view2 = finder.findRequiredView(source, R.id.recharge_nap, "field 'rechargeNap' and method 'onPowerNapClick'");
        target.rechargeNap = (NapView) view2;
        view2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onPowerNapClick();
            }
        });
        View view3 = finder.findRequiredView(source, R.id.body_nap, "field 'bodyNap' and method 'onBodyNapClick'");
        target.bodyNap = (NapView) view3;
        view3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onBodyNapClick();
            }
        });
        View view4 = finder.findRequiredView(source, R.id.mind_nap, "field 'remNap' and method 'onMindNapClick'");
        target.remNap = (NapView) view4;
        view4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onMindNapClick();
            }
        });
        View view5 = finder.findRequiredView(source, R.id.ultimate_nap, "field 'ultimateNap' and method 'onUltimateNapClick'");
        target.ultimateNap = (NapView) view5;
        view5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onUltimateNapClick();
            }
        });
        finder.findRequiredView(source, R.id.menu, "method 'onMenuClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onMenuClick();
            }
        });
    }

    public static void reset(EnergyPlusFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.mBottomToolbar = null;
        target.lightBoost = null;
        target.rechargeNap = null;
        target.bodyNap = null;
        target.remNap = null;
        target.ultimateNap = null;
    }
}
