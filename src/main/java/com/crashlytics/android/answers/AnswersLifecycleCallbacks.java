package com.crashlytics.android.answers;

import android.app.Activity;
import android.os.Bundle;
import com.crashlytics.android.answers.SessionEvent;
import io.fabric.sdk.android.ActivityLifecycleManager;

class AnswersLifecycleCallbacks extends ActivityLifecycleManager.Callbacks {
    private final SessionAnalyticsManager analyticsManager;
    private final BackgroundManager backgroundManager;

    public AnswersLifecycleCallbacks(SessionAnalyticsManager analyticsManager2, BackgroundManager backgroundManager2) {
        this.analyticsManager = analyticsManager2;
        this.backgroundManager = backgroundManager2;
    }

    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    public void onActivityStarted(Activity activity) {
        this.analyticsManager.onLifecycle(activity, SessionEvent.Type.START);
    }

    public void onActivityResumed(Activity activity) {
        this.analyticsManager.onLifecycle(activity, SessionEvent.Type.RESUME);
        this.backgroundManager.onActivityResumed();
    }

    public void onActivityPaused(Activity activity) {
        this.analyticsManager.onLifecycle(activity, SessionEvent.Type.PAUSE);
        this.backgroundManager.onActivityPaused();
    }

    public void onActivityStopped(Activity activity) {
        this.analyticsManager.onLifecycle(activity, SessionEvent.Type.STOP);
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public void onActivityDestroyed(Activity activity) {
    }
}
