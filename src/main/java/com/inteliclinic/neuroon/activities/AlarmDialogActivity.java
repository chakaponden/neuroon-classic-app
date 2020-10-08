package com.inteliclinic.neuroon.activities;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.mask.bluetooth.DeviceStateEvent;
import de.greenrobot.event.EventBus;
import io.intercom.android.sdk.utilities.AttachmentUtils;
import java.io.IOException;

public class AlarmDialogActivity extends AppCompatActivity {
    private static final String TAG = AlarmDialogActivity.class.getSimpleName();
    private KeyguardManager.KeyguardLock mKeyguardLock;
    private MediaPlayer mPlayer;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(6815872);
        this.mKeyguardLock = ((KeyguardManager) getSystemService("keyguard")).newKeyguardLock("App");
        this.mKeyguardLock.disableKeyguard();
        playAlarmSound(this);
        EventBus.getDefault().registerSticky(this);
    }

    public void onEvent(DeviceStateEvent event) {
        if (event.isConnected()) {
            endAlarm();
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void playAlarmSound(Context context) {
        Uri alert = RingtoneManager.getDefaultUri(4);
        if (alert == null && (alert = RingtoneManager.getDefaultUri(2)) == null) {
            alert = RingtoneManager.getDefaultUri(1);
        }
        this.mPlayer = new MediaPlayer();
        try {
            this.mPlayer.setDataSource(context, alert);
            if (((AudioManager) context.getSystemService(AttachmentUtils.MIME_TYPE_AUDIO)).getStreamVolume(4) != 0) {
                this.mPlayer.setAudioStreamType(4);
                this.mPlayer.setLooping(true);
                this.mPlayer.prepare();
                this.mPlayer.start();
            }
            showDialogToCancelAlarm(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showDialogToCancelAlarm(Context context) {
        new AlertDialog.Builder(context).setTitle((int) R.string.app_name).setMessage((CharSequence) "It's time to wake up!").setPositiveButton((CharSequence) "Dismiss", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        }).setCancelable(false).setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                AlarmDialogActivity.this.endAlarm();
            }
        }).show();
    }

    /* access modifiers changed from: private */
    public void endAlarm() {
        if (this.mPlayer != null) {
            this.mPlayer.reset();
            this.mPlayer.release();
            this.mPlayer = null;
        }
        this.mKeyguardLock.reenableKeyguard();
        finish();
    }
}
