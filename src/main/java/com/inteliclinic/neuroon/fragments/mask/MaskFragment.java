package com.inteliclinic.neuroon.fragments.mask;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.google.gson.Gson;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.activities.MaskSynchronizationActivity;
import com.inteliclinic.neuroon.fragments.BaseFragment;
import com.inteliclinic.neuroon.managers.DataManager;
import com.inteliclinic.neuroon.managers.account.AccountManager;
import com.inteliclinic.neuroon.mask.BatteryLevelReceivedEvent;
import com.inteliclinic.neuroon.mask.MaskConnectedEvent;
import com.inteliclinic.neuroon.mask.MaskManager;
import com.inteliclinic.neuroon.mask.bluetooth.BleManager;
import com.inteliclinic.neuroon.mask.bluetooth.DeviceStateEvent;
import com.inteliclinic.neuroon.models.data.Sleep;
import com.inteliclinic.neuroon.views.ThinTextView;
import com.raizlabs.android.dbflow.sql.language.Condition;
import de.greenrobot.event.EventBus;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MaskFragment extends BaseFragment {
    private static final String TAG = MaskFragment.class.getSimpleName();
    @InjectView(2131755348)
    ThinTextView batteryLevel;
    @InjectView(2131755347)
    ProgressBar batteryLevelProgress;
    @InjectView(2131755345)
    ThinTextView connectionState;
    @InjectView(2131755344)
    ThinTextView deviceName;
    @InjectView(2131755343)
    ThinTextView devicePairDay;
    @InjectView(2131755360)
    View divider;
    @InjectView(2131755362)
    View divider2;
    @InjectView(2131755364)
    View divider7;
    @InjectView(2131755366)
    View divider8;
    @InjectView(2131755363)
    ThinTextView exportSleepData;
    @InjectView(2131755367)
    ThinTextView loadUserSleeps;
    private int mBatteryLevel = -1;
    /* access modifiers changed from: private */
    public OnFragmentInteractionListener mListener;
    @InjectView(2131755355)
    RelativeLayout maskGuide;
    @InjectView(2131755365)
    ThinTextView openLogs;
    @InjectView(2131755359)
    ThinTextView resetMaskData;
    @InjectView(2131755361)
    ThinTextView resetRawData;
    @InjectView(2131755351)
    RelativeLayout software;
    @InjectView(2131755353)
    ThinTextView softwareNotification;

    public interface OnFragmentInteractionListener {
        void openLogs();

        void openMaskSoftware();

        void unPairMask();
    }

    public static MaskFragment newInstance() {
        return new MaskFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String username;
        View inflate = inflater.inflate(R.layout.fragment_mask, container, false);
        ButterKnife.inject((Object) this, inflate);
        setMaskData();
        setMaskState();
        setBatteryLevel();
        if (AccountManager.getInstance().isLogged() && (username = AccountManager.getInstance().getUsername()) != null && username.endsWith("@inteliclinic.com")) {
            this.resetMaskData.setVisibility(0);
            this.resetMaskData.setClickable(true);
            this.divider.setVisibility(0);
            this.resetRawData.setVisibility(0);
            this.resetRawData.setClickable(true);
            this.divider2.setVisibility(0);
            this.exportSleepData.setVisibility(0);
            this.exportSleepData.setClickable(true);
            this.divider7.setVisibility(0);
            this.openLogs.setVisibility(0);
            this.openLogs.setClickable(true);
            this.divider8.setVisibility(0);
            this.loadUserSleeps.setVisibility(0);
            this.loadUserSleeps.setClickable(true);
        }
        return inflate;
    }

    private void setMaskData() {
        if (MaskManager.getInstance().pairedWithDevice()) {
            Date pairDate = MaskManager.getInstance().getPairDate();
            if (pairDate == null) {
                pairDate = new Date();
            }
            String name = MaskManager.getInstance().getDeviceName();
            if (name != null) {
                this.deviceName.setText(name);
            }
            this.devicePairDay.setText(getString(R.string.paired_date, new Object[]{new SimpleDateFormat("dd MMM. yyyy").format(pairDate)}));
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        EventBus.getDefault().registerSticky(this);
        try {
            this.mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
        this.mListener = null;
    }

    public void onEventMainThread(DeviceStateEvent device) {
        setMaskState();
    }

    public void onEventMainThread(MaskConnectedEvent event) {
        setMaskState();
    }

    public void onEventMainThread(BatteryLevelReceivedEvent event) {
        this.mBatteryLevel = event.getLevel();
        setBatteryLevel();
    }

    private void setBatteryLevel() {
        if (this.batteryLevelProgress != null && this.batteryLevel != null) {
            if (this.mBatteryLevel == -1) {
                this.batteryLevelProgress.setProgress(0);
                this.batteryLevel.setText(getString(R.string.battery_level, new Object[]{"not connected"}));
                return;
            }
            this.batteryLevelProgress.setProgress(this.mBatteryLevel);
            if (this.mBatteryLevel < 40) {
                this.batteryLevel.setText(getString(R.string.battery_level, new Object[]{getString(R.string.low)}));
            } else if (this.mBatteryLevel < 70) {
                this.batteryLevel.setText(getString(R.string.battery_level, new Object[]{getString(R.string.medium)}));
            } else {
                this.batteryLevel.setText(getString(R.string.battery_level, new Object[]{getString(R.string.high)}));
            }
        }
    }

    private void setMaskState() {
        if (this.connectionState == null) {
            return;
        }
        if (MaskManager.getInstance().isFullyConnected()) {
            this.connectionState.setText(R.string.connected);
        } else if (MaskManager.getInstance().isConnected()) {
            this.connectionState.setText(R.string.connecting);
        } else {
            this.connectionState.setText(R.string.not_connected);
        }
    }

    @OnClick({2131755349})
    public void onSyncMaskClick() {
        if (!MaskManager.getInstance().isFullyConnected()) {
            View view = getView();
            if (view != null) {
                Snackbar.make(view, (int) R.string.please_connect_the_mask, 0).show();
            }
        } else if (MaskManager.getInstance().isDownloadingSleeps()) {
            startActivity(new Intent(getActivity(), MaskSynchronizationActivity.class));
        } else if (MaskManager.getInstance().getMaskSleepCount() > MaskManager.getInstance().getLastSavedSleep()) {
            MaskManager.getInstance().downloadSleep(MaskManager.getInstance().getLastSavedSleep() + 1);
            startActivity(new Intent(getActivity(), MaskSynchronizationActivity.class));
        } else {
            View view2 = getView();
            if (view2 != null) {
                Snackbar.make(view2, (int) R.string.mask_data_up_to_date, 0).show();
            }
        }
    }

    @OnClick({2131755351})
    public void onSoftwareClick() {
        if (this.mListener != null) {
            this.mListener.openMaskSoftware();
        }
    }

    @OnClick({2131755356})
    public void onUserReconnectMask() {
        BleManager.getInstance().disconnectDevice();
        BleManager.getInstance().resetAll();
        BleManager.getInstance().scanForDevices();
    }

    @OnClick({2131755357})
    public void onUnpairMaskClick() {
        new AlertDialog.Builder(getActivity()).setTitle((int) R.string.app_name).setMessage((int) R.string.are_you_sure).setPositiveButton((int) R.string.yes, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (MaskFragment.this.mListener != null) {
                    MaskFragment.this.mListener.unPairMask();
                }
            }
        }).setNegativeButton((int) R.string.no, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    @OnClick({2131755359})
    public void onResetMaskData() {
        if (MaskManager.getInstance().isFullyConnected()) {
            BleManager.getInstance().clearMaskData();
        }
    }

    @OnClick({2131755361})
    public void onResetRawMask() {
        if (MaskManager.getInstance().isFullyConnected()) {
            BleManager.getInstance().clearMaskRawData();
        }
    }

    @OnClick({2131755363})
    public void onExportSleepDataClick() {
        final List<Sleep> sleepsByDateDescending = DataManager.getInstance().getSleepsByDateDescending();
        List<CharSequence> itemsList = new ArrayList<>();
        for (Sleep sleep : sleepsByDateDescending) {
            itemsList.add(DateFormat.getDateTimeInstance().format(sleep.getStartDate()));
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle((CharSequence) "Select sleep to save");
        builder.setItems((CharSequence[]) itemsList.toArray(new CharSequence[itemsList.size()]), (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                MaskFragment.this.checkAndSaveSleep(DataManager.getInstance().getSleepById(((Sleep) sleepsByDateDescending.get(item)).getId()));
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @OnClick({2131755365})
    public void onOpenLogsClick() {
        if (this.mListener != null) {
            this.mListener.openLogs();
        }
    }

    /* access modifiers changed from: package-private */
    @OnClick({2131755367})
    public void onLoadUserSleepsClick() {
    }

    /* access modifiers changed from: private */
    public void checkAndSaveSleep(Sleep sleep) {
        if (ContextCompat.checkSelfPermission(getActivity(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            saveSleepToFile(sleep);
        } else if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), "android.permission.READ_CONTACTS")) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
        }
    }

    public boolean isExternalStorageWritable() {
        if ("mounted".equals(Environment.getExternalStorageState())) {
            return true;
        }
        return false;
    }

    private void saveSleepToFile(Sleep sleep) {
        File myDir = new File(Environment.getExternalStorageDirectory().toString() + "/Sleeps/");
        myDir.mkdirs();
        File file = new File(myDir, "Sleep-" + sleep.getId() + Condition.Operation.MINUS + DateFormat.getDateTimeInstance().format(sleep.getStartDate()) + ".txt");
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(new Gson().toJson((Object) sleep).getBytes());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
