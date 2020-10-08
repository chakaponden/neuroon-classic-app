package com.inteliclinic.neuroon.fragments.mask;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.BaseFragment$$ViewInjector;
import com.inteliclinic.neuroon.views.ThinTextView;

public class MaskFragment$$ViewInjector {
    public static void inject(ButterKnife.Finder finder, final MaskFragment target, Object source) {
        BaseFragment$$ViewInjector.inject(finder, target, source);
        target.deviceName = (ThinTextView) finder.findRequiredView(source, R.id.device_name, "field 'deviceName'");
        target.devicePairDay = (ThinTextView) finder.findRequiredView(source, R.id.device_pair_day, "field 'devicePairDay'");
        target.batteryLevelProgress = (ProgressBar) finder.findRequiredView(source, R.id.battery_level_progress, "field 'batteryLevelProgress'");
        target.batteryLevel = (ThinTextView) finder.findRequiredView(source, R.id.battery_level, "field 'batteryLevel'");
        target.softwareNotification = (ThinTextView) finder.findRequiredView(source, R.id.software_notification, "field 'softwareNotification'");
        target.maskGuide = (RelativeLayout) finder.findRequiredView(source, R.id.mask_guide, "field 'maskGuide'");
        View view = finder.findRequiredView(source, R.id.software, "field 'software' and method 'onSoftwareClick'");
        target.software = (RelativeLayout) view;
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onSoftwareClick();
            }
        });
        target.connectionState = (ThinTextView) finder.findRequiredView(source, R.id.connection_state, "field 'connectionState'");
        View view2 = finder.findRequiredView(source, R.id.reset_mask_data, "field 'resetMaskData' and method 'onResetMaskData'");
        target.resetMaskData = (ThinTextView) view2;
        view2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onResetMaskData();
            }
        });
        target.divider = finder.findRequiredView(source, R.id.divider5, "field 'divider'");
        View view3 = finder.findRequiredView(source, R.id.reset_raw_data, "field 'resetRawData' and method 'onResetRawMask'");
        target.resetRawData = (ThinTextView) view3;
        view3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onResetRawMask();
            }
        });
        target.divider2 = finder.findRequiredView(source, R.id.divider6, "field 'divider2'");
        View view4 = finder.findRequiredView(source, R.id.export_sleep_data, "field 'exportSleepData' and method 'onExportSleepDataClick'");
        target.exportSleepData = (ThinTextView) view4;
        view4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onExportSleepDataClick();
            }
        });
        target.divider7 = finder.findRequiredView(source, R.id.divider7, "field 'divider7'");
        View view5 = finder.findRequiredView(source, R.id.open_logs, "field 'openLogs' and method 'onOpenLogsClick'");
        target.openLogs = (ThinTextView) view5;
        view5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onOpenLogsClick();
            }
        });
        target.divider8 = finder.findRequiredView(source, R.id.divider8, "field 'divider8'");
        View view6 = finder.findRequiredView(source, R.id.load_user_sleeps, "field 'loadUserSleeps' and method 'onLoadUserSleepsClick'");
        target.loadUserSleeps = (ThinTextView) view6;
        view6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onLoadUserSleepsClick();
            }
        });
        finder.findRequiredView(source, R.id.sync_mask, "method 'onSyncMaskClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onSyncMaskClick();
            }
        });
        finder.findRequiredView(source, R.id.force_reconnect_mask, "method 'onUserReconnectMask'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onUserReconnectMask();
            }
        });
        finder.findRequiredView(source, R.id.unpair_change_mask, "method 'onUnpairMaskClick'").setOnClickListener(new View.OnClickListener() {
            public void onClick(View p0) {
                target.onUnpairMaskClick();
            }
        });
    }

    public static void reset(MaskFragment target) {
        BaseFragment$$ViewInjector.reset(target);
        target.deviceName = null;
        target.devicePairDay = null;
        target.batteryLevelProgress = null;
        target.batteryLevel = null;
        target.softwareNotification = null;
        target.maskGuide = null;
        target.software = null;
        target.connectionState = null;
        target.resetMaskData = null;
        target.divider = null;
        target.resetRawData = null;
        target.divider2 = null;
        target.exportSleepData = null;
        target.divider7 = null;
        target.openLogs = null;
        target.divider8 = null;
        target.loadUserSleeps = null;
    }
}
