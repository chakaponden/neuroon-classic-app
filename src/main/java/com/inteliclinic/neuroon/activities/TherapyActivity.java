package com.inteliclinic.neuroon.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.gms.analytics.HitBuilders;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.events.DbEventsUpdatedEvent;
import com.inteliclinic.neuroon.fragments.TherapyFragment;
import com.inteliclinic.neuroon.fragments.mask.TurnOnMaskFragment;
import com.inteliclinic.neuroon.managers.DataManager;
import com.inteliclinic.neuroon.mask.MaskConnectedEvent;
import com.inteliclinic.neuroon.mask.MaskManager;
import com.inteliclinic.neuroon.models.data.Event;
import com.inteliclinic.neuroon.utils.DateUtils;
import de.greenrobot.event.EventBus;
import java.util.Date;

public class TherapyActivity extends BaseActivity implements TherapyFragment.OnFragmentInteractionListener, TurnOnMaskFragment.OnFragmentInteractionListener {
    private static final String ARG_THERAPY_DURATION = "therapy_duration";
    private static final String ARG_THERAPY_NAME = "therapy_name";
    private static final String ARG_THERAPY_TYPE = "therapy_type";
    private Long mEventId;
    private TherapyFragment mFragment;
    private int mTherapyDuration;
    private String mTherapyName;
    private Event.EventType mTherapyType;
    private TurnOnMaskFragment mTurnOnMaskFragment;
    private boolean mWaitingForDevice;

    public static void startActivity(Context context, String name, int seconds, Event.EventType type) {
        Intent i = new Intent(context, TherapyActivity.class);
        i.putExtra(ARG_THERAPY_NAME, name);
        i.putExtra(ARG_THERAPY_DURATION, seconds);
        i.putExtra(ARG_THERAPY_TYPE, String.valueOf(type));
        context.startActivity(i);
    }

    public void onEvent(MaskConnectedEvent event) {
        if (MaskManager.getInstance().isFullyConnected() && this.mWaitingForDevice) {
            this.mWaitingForDevice = false;
            if (this.mTurnOnMaskFragment != null) {
                getFragmentManager().beginTransaction().remove(this.mTurnOnMaskFragment).commit();
                this.mTurnOnMaskFragment = null;
            }
            sendTherapyToMask();
        }
    }

    public void onEvent(DbEventsUpdatedEvent event) {
        if (this.mEventId != null && DataManager.getInstance().getEventById(this.mEventId.longValue()).isCompleted()) {
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_therapy);
        this.mFragment = (TherapyFragment) getFragmentManager().findFragmentById(R.id.fragment);
        if (getIntent() != null) {
            this.mTherapyName = getIntent().getStringExtra(ARG_THERAPY_NAME);
            this.mTherapyDuration = getIntent().getIntExtra(ARG_THERAPY_DURATION, -1);
            this.mTherapyType = Event.EventType.valueOf(getIntent().getStringExtra(ARG_THERAPY_TYPE));
            this.mFragment.setTherapy(this.mTherapyName, this.mTherapyDuration, this.mTherapyType);
        }
        if (!MaskManager.getInstance().isFullyConnected()) {
            this.mWaitingForDevice = true;
            this.mTurnOnMaskFragment = TurnOnMaskFragment.newInstance(this.mTherapyName);
            getFragmentManager().beginTransaction().add(R.id.container, this.mTurnOnMaskFragment).commit();
        } else {
            sendTherapyToMask();
        }
        EventBus.getDefault().registerSticky(this);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void closeFragments() {
        finish();
    }

    public void onCancelTherapy() {
        new AlertDialog.Builder(this).setTitle(R.string.app_name).setMessage(R.string.turn_off_therapy_mode_on_mask).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MaskManager.getInstance().cancelEvent();
                dialog.dismiss();
            }
        }).setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                TherapyActivity.this.closeFragments();
            }
        }).show();
    }

    private void sendTherapyToMask() {
        Event event = null;
        switch (this.mTherapyType) {
            case ETSleep:
                event = Event.sleepEvent(DateUtils.dateAddSeconds(new Date(), this.mTherapyDuration));
                break;
            case ETNapPower:
                getTracker().send(new HitBuilders.EventBuilder().setCategory("personalPause").setAction("personal_pause_started").setLabel("personal_pause_started").build());
                event = Event.powerNapTherapy(this.mTherapyDuration * 1000);
                break;
            case ETNapBody:
                getTracker().send(new HitBuilders.EventBuilder().setCategory("personalPause").setAction("personal_pause_started").setLabel("personal_pause_started").build());
                event = Event.bodyNapTherapy(this.mTherapyDuration * 1000);
                break;
            case ETNapRem:
                getTracker().send(new HitBuilders.EventBuilder().setCategory("personalPause").setAction("personal_pause_started").setLabel("personal_pause_started").build());
                event = Event.remNapTherapy(this.mTherapyDuration * 1000);
                break;
            case ETNapUltimate:
                getTracker().send(new HitBuilders.EventBuilder().setCategory("personalPause").setAction("personal_pause_started").setLabel("personal_pause_started").build());
                event = Event.ultimateNapTherapy(this.mTherapyDuration * 1000);
                break;
            case ETBLT:
                getTracker().send(new HitBuilders.EventBuilder().setCategory("personalPause").setAction("light_boost_started").setLabel("light_boost_started").build());
                event = Event.lightBoostTherapy();
                break;
        }
        if (event != null) {
            MaskManager.getInstance().startEvent(event);
            DataManager.getInstance().saveEvents(new Event[]{event});
            this.mEventId = Long.valueOf(event.getId());
        }
    }

    public void onCancelTherapyWithoutStart() {
        finish();
    }
}
