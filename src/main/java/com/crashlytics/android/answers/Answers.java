package com.crashlytics.android.answers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;
import io.fabric.sdk.android.services.common.CommonUtils;
import io.fabric.sdk.android.services.common.Crash;
import io.fabric.sdk.android.services.common.IdManager;
import io.fabric.sdk.android.services.settings.Settings;
import io.fabric.sdk.android.services.settings.SettingsData;
import java.io.File;

public class Answers extends Kit<Boolean> {
    static final String CRASHLYTICS_API_ENDPOINT = "com.crashlytics.ApiEndpoint";
    public static final String TAG = "Answers";
    SessionAnalyticsManager analyticsManager;

    public static Answers getInstance() {
        return (Answers) Fabric.getKit(Answers.class);
    }

    public void logCustom(CustomEvent event) {
        if (event == null) {
            throw new NullPointerException("event must not be null");
        } else if (this.analyticsManager != null) {
            this.analyticsManager.onCustom(event);
        }
    }

    public void logPurchase(PurchaseEvent event) {
        if (event == null) {
            throw new NullPointerException("event must not be null");
        } else if (this.analyticsManager != null) {
            this.analyticsManager.onPredefined(event);
        }
    }

    public void logLogin(LoginEvent event) {
        if (event == null) {
            throw new NullPointerException("event must not be null");
        } else if (this.analyticsManager != null) {
            this.analyticsManager.onPredefined(event);
        }
    }

    public void logShare(ShareEvent event) {
        if (event == null) {
            throw new NullPointerException("event must not be null");
        } else if (this.analyticsManager != null) {
            this.analyticsManager.onPredefined(event);
        }
    }

    public void logInvite(InviteEvent event) {
        if (event == null) {
            throw new NullPointerException("event must not be null");
        } else if (this.analyticsManager != null) {
            this.analyticsManager.onPredefined(event);
        }
    }

    public void logSignUp(SignUpEvent event) {
        if (event == null) {
            throw new NullPointerException("event must not be null");
        } else if (this.analyticsManager != null) {
            this.analyticsManager.onPredefined(event);
        }
    }

    public void logLevelStart(LevelStartEvent event) {
        if (event == null) {
            throw new NullPointerException("event must not be null");
        } else if (this.analyticsManager != null) {
            this.analyticsManager.onPredefined(event);
        }
    }

    public void logLevelEnd(LevelEndEvent event) {
        if (event == null) {
            throw new NullPointerException("event must not be null");
        } else if (this.analyticsManager != null) {
            this.analyticsManager.onPredefined(event);
        }
    }

    public void logAddToCart(AddToCartEvent event) {
        if (event == null) {
            throw new NullPointerException("event must not be null");
        } else if (this.analyticsManager != null) {
            this.analyticsManager.onPredefined(event);
        }
    }

    public void logStartCheckout(StartCheckoutEvent event) {
        if (event == null) {
            throw new NullPointerException("event must not be null");
        } else if (this.analyticsManager != null) {
            this.analyticsManager.onPredefined(event);
        }
    }

    public void logRating(RatingEvent event) {
        if (event == null) {
            throw new NullPointerException("event must not be null");
        } else if (this.analyticsManager != null) {
            this.analyticsManager.onPredefined(event);
        }
    }

    public void logContentView(ContentViewEvent event) {
        if (event == null) {
            throw new NullPointerException("event must not be null");
        } else if (this.analyticsManager != null) {
            this.analyticsManager.onPredefined(event);
        }
    }

    public void logSearch(SearchEvent event) {
        if (event == null) {
            throw new NullPointerException("event must not be null");
        } else if (this.analyticsManager != null) {
            this.analyticsManager.onPredefined(event);
        }
    }

    public void onException(Crash.LoggedException exception) {
        if (this.analyticsManager != null) {
            this.analyticsManager.onError(exception.getSessionId());
        }
    }

    public void onException(Crash.FatalException exception) {
        if (this.analyticsManager != null) {
            this.analyticsManager.onCrash(exception.getSessionId(), exception.getExceptionName());
        }
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"NewApi"})
    public boolean onPreExecute() {
        long installedAt;
        try {
            Context context = getContext();
            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getPackageName();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            String versionCode = Integer.toString(packageInfo.versionCode);
            String versionName = packageInfo.versionName == null ? IdManager.DEFAULT_VERSION_NAME : packageInfo.versionName;
            if (Build.VERSION.SDK_INT >= 9) {
                installedAt = packageInfo.firstInstallTime;
            } else {
                installedAt = new File(packageManager.getApplicationInfo(packageName, 0).sourceDir).lastModified();
            }
            this.analyticsManager = SessionAnalyticsManager.build(this, context, getIdManager(), versionCode, versionName, installedAt);
            this.analyticsManager.enable();
            return true;
        } catch (Exception e) {
            Fabric.getLogger().e(TAG, "Error retrieving app properties", e);
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public Boolean doInBackground() {
        try {
            SettingsData settingsData = Settings.getInstance().awaitSettingsData();
            if (settingsData == null) {
                Fabric.getLogger().e(TAG, "Failed to retrieve settings");
                return false;
            } else if (settingsData.featuresData.collectAnalytics) {
                Fabric.getLogger().d(TAG, "Analytics collection enabled");
                this.analyticsManager.setAnalyticsSettingsData(settingsData.analyticsSettingsData, getOverridenSpiEndpoint());
                return true;
            } else {
                Fabric.getLogger().d(TAG, "Analytics collection disabled");
                this.analyticsManager.disable();
                return false;
            }
        } catch (Exception e) {
            Fabric.getLogger().e(TAG, "Error dealing with settings", e);
            return false;
        }
    }

    public String getIdentifier() {
        return "com.crashlytics.sdk.android:answers";
    }

    public String getVersion() {
        return "1.3.8.127";
    }

    /* access modifiers changed from: package-private */
    public String getOverridenSpiEndpoint() {
        return CommonUtils.getStringsFileValue(getContext(), CRASHLYTICS_API_ENDPOINT);
    }
}
