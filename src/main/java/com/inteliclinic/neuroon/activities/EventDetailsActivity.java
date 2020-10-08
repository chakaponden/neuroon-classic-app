package com.inteliclinic.neuroon.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.inteliclinic.neuroon.R;
import com.inteliclinic.neuroon.fragments.EventDetailsActivityFragment;
import com.inteliclinic.neuroon.fragments.statistics.BrainStatisticFragment;
import com.inteliclinic.neuroon.fragments.statistics.HeartStatisticFragment;
import com.inteliclinic.neuroon.fragments.statistics.StatsDetailFragment;
import com.inteliclinic.neuroon.fragments.statistics.TimeStatisticFragment;
import com.inteliclinic.neuroon.managers.DataManager;
import com.inteliclinic.neuroon.models.data.Event;
import com.inteliclinic.neuroon.models.data.Sleep;

public class EventDetailsActivity extends BaseActivity implements StatsDetailFragment.OnFragmentInteractionListener, BrainStatisticFragment.OnFragmentInteractionListener, HeartStatisticFragment.OnFragmentInteractionListener, TimeStatisticFragment.OnFragmentInteractionListener {
    private static final String BUNDLE_EVENT_ID = "bundle_event_id";
    private static final String BUNDLE_SLEEP_ID = "bundle_sleep_id";
    private static final String BUNDLE_SLEEP_PAGE = "bundle_sleep_page";

    public static void openEvent(Context context, Event event) {
        Intent intent = new Intent(context, EventDetailsActivity.class);
        intent.putExtra(BUNDLE_EVENT_ID, event.getId());
        context.startActivity(intent);
    }

    public static void openSleep(Context context, long sleepId, StatsDetailFragment.StatsPage page) {
        Intent intent = new Intent(context, EventDetailsActivity.class);
        intent.putExtra(BUNDLE_SLEEP_ID, sleepId);
        intent.putExtra(BUNDLE_SLEEP_PAGE, page);
        context.startActivity(intent);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_event_details);
        if (!handleIntent()) {
            finish();
        }
    }

    private boolean handleIntent() {
        if (getIntent() != null) {
            if (getIntent().getLongExtra(BUNDLE_EVENT_ID, -1) != -1) {
                Event event = DataManager.getInstance().getEventById(getIntent().getLongExtra(BUNDLE_EVENT_ID, -1));
                if (event != null) {
                    switch (event.getType()) {
                        case ETSleep:
                            getFragmentManager().beginTransaction().add(R.id.container, StatsDetailFragment.newInstance(DataManager.getInstance().getSleepIdByDate(event.getStartDate()), StatsDetailFragment.StatsPage.BRAIN)).commit();
                            return true;
                    }
                }
            } else if (getIntent().getLongExtra(BUNDLE_SLEEP_ID, -1) != -1) {
                getFragmentManager().beginTransaction().add(R.id.container, StatsDetailFragment.newInstance(getIntent().getLongExtra(BUNDLE_SLEEP_ID, -1), (StatsDetailFragment.StatsPage) getIntent().getSerializableExtra(BUNDLE_SLEEP_PAGE))).commit();
                return true;
            }
        }
        return false;
    }

    public void closeFragments() {
        finish();
    }

    public void deleteSleep(long sleepId) {
        Sleep lastSleep = DataManager.getInstance().getSleepById(sleepId);
        if (lastSleep != null) {
            lastSleep.setAsDeleted();
            DataManager.getInstance().saveSleeps(new Sleep[]{lastSleep});
            Event eventOfSleep = DataManager.getInstance().getEventOfSleep(lastSleep);
            if (eventOfSleep != null) {
                DataManager.getInstance().deleteEvent(eventOfSleep);
                return;
            }
            return;
        }
        finish();
    }

    public boolean goBack() {
        if (!(getFragmentManager().findFragmentById(R.id.container) instanceof EventDetailsActivityFragment)) {
            return super.goBack();
        }
        finish();
        return true;
    }
}
