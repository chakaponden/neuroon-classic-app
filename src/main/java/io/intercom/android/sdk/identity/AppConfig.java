package io.intercom.android.sdk.identity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.ActivityChooserView;
import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.blocks.models.Block;
import io.intercom.android.sdk.logger.IntercomLogger;
import io.intercom.android.sdk.models.Config;
import io.intercom.android.sdk.models.Events.FirstMessageEvent;
import io.intercom.android.sdk.nexus.NexusConfig;
import io.intercom.android.sdk.utilities.Constants;
import io.intercom.com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

public class AppConfig {
    private static final String APP_AUDIO_ENABLED = "app_audio_enabled";
    private static final String APP_COLOR = "app_color";
    private static final String APP_FIRST_REQUEST = "app_is_first_request";
    private static final String APP_INBOUND_MESSAGES = "app_inbound_messages";
    private static final String APP_MESSAGE_RESPONSE = "app_message_response";
    private static final String APP_NO_REAL_TIME_THROTTLE = "app_no_real_time_throttle";
    private static final String APP_POLLING_INTERVAL = "app_polling_interval";
    private static final String APP_POWERED_BY = "app_powered_by";
    private static final String APP_RATE_LIMIT_COUNT = "app_rate_limit_count";
    private static final String APP_RATE_LIMIT_PERIOD = "app_rate_limit_period";
    private static final String APP_REAL_TIME = "app_real_time";
    private static final String APP_USER_UPDATE_CACHE_MAX_AGE = "app_user_update_cache_max_age";
    private static final String APP_WELCOME_MESSAGE = "app_welcome_message";
    private boolean audioEnabled;
    private String baseColor = "";
    private Drawable baseColorAvatar;
    private final Gson gson;
    private boolean inboundMessages;
    private boolean isFirstRequest;
    private String messageResponse = "";
    private int noRealtimeThrottle;
    private int pollingInterval;
    private final SharedPreferences prefs;
    private int rateLimitCount;
    private int rateLimitPeriod;
    private boolean realTime;
    private NexusConfig realTimeConfig = new NexusConfig();
    private boolean showPoweredBy;
    private int userUpdateCacheMaxAge;
    private List<Block> welcomeMessage = new ArrayList();
    private String welcomeMessageJson;

    public AppConfig(Context context, Gson gson2) {
        this.gson = gson2;
        this.prefs = context.getSharedPreferences(Constants.INTERCOM_PREFS, 0);
        this.baseColor = this.prefs.getString(APP_COLOR, String.format("#%06X", new Object[]{Integer.valueOf(16777215 & context.getResources().getColor(R.color.intercomsdk_main_blue))}));
        this.baseColorAvatar = createColoredAvatar(context, Color.parseColor(this.baseColor));
        this.showPoweredBy = this.prefs.getBoolean(APP_POWERED_BY, false);
        this.isFirstRequest = this.prefs.getBoolean(APP_FIRST_REQUEST, false);
        this.inboundMessages = this.prefs.getBoolean(APP_INBOUND_MESSAGES, false);
        this.pollingInterval = this.prefs.getInt(APP_POLLING_INTERVAL, 0);
        this.noRealtimeThrottle = this.prefs.getInt(APP_NO_REAL_TIME_THROTTLE, 0);
        this.userUpdateCacheMaxAge = this.prefs.getInt(APP_USER_UPDATE_CACHE_MAX_AGE, 0);
        this.rateLimitPeriod = this.prefs.getInt(APP_RATE_LIMIT_PERIOD, 60);
        this.rateLimitCount = this.prefs.getInt(APP_RATE_LIMIT_COUNT, ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
        this.realTime = this.prefs.getBoolean(APP_REAL_TIME, false);
        this.messageResponse = this.prefs.getString(APP_MESSAGE_RESPONSE, "");
        this.audioEnabled = this.prefs.getBoolean(APP_AUDIO_ENABLED, true);
        this.welcomeMessageJson = this.prefs.getString(APP_WELCOME_MESSAGE, "");
    }

    /* access modifiers changed from: protected */
    public void update(Config config) {
        boolean colourChanged;
        if (isNewConfig(config)) {
            boolean nexusConfigChanged = !config.getRealTimeConfig().equals(this.realTimeConfig);
            if (!config.getBaseColor().equals(this.baseColor)) {
                colourChanged = true;
            } else {
                colourChanged = false;
            }
            this.welcomeMessage = config.getWelcomeMessage();
            this.baseColor = config.getBaseColor();
            this.isFirstRequest = config.isFirstRequest();
            this.inboundMessages = config.isInboundMessages();
            this.showPoweredBy = config.isShowPoweredBy();
            this.audioEnabled = config.isAudioEnabled();
            this.realTime = config.isRealTime();
            this.pollingInterval = config.getPollingInterval();
            this.noRealtimeThrottle = config.getNoRealtimeThrottle();
            this.userUpdateCacheMaxAge = config.getUserUpdateCacheMaxAge();
            this.rateLimitPeriod = config.getRateLimitPeriod();
            this.rateLimitCount = config.getRateLimitCount();
            this.messageResponse = config.getMessageResponse();
            this.realTimeConfig = config.getRealTimeConfig();
            SharedPreferences.Editor editor = this.prefs.edit();
            editor.putString(APP_WELCOME_MESSAGE, convertWelcomeMessageToJson());
            editor.putString(APP_COLOR, this.baseColor);
            editor.putString(APP_MESSAGE_RESPONSE, this.messageResponse);
            editor.putInt(APP_POLLING_INTERVAL, this.pollingInterval);
            editor.putInt(APP_NO_REAL_TIME_THROTTLE, this.noRealtimeThrottle);
            editor.putInt(APP_USER_UPDATE_CACHE_MAX_AGE, this.userUpdateCacheMaxAge);
            editor.putInt(APP_RATE_LIMIT_COUNT, this.rateLimitCount);
            editor.putInt(APP_RATE_LIMIT_PERIOD, this.rateLimitPeriod);
            editor.putBoolean(APP_POWERED_BY, this.showPoweredBy);
            editor.putBoolean(APP_FIRST_REQUEST, this.isFirstRequest);
            editor.putBoolean(APP_INBOUND_MESSAGES, this.inboundMessages);
            editor.putBoolean(APP_AUDIO_ENABLED, this.audioEnabled);
            editor.putBoolean(APP_REAL_TIME, this.realTime);
            editor.apply();
            if (this.isFirstRequest) {
                Bridge.getBus().post(new FirstMessageEvent());
            }
            if (nexusConfigChanged) {
                Bridge.getNexusClient().cycle(this.realTimeConfig, true);
            }
            if (colourChanged) {
                this.baseColorAvatar = createColoredAvatar(Bridge.getContext(), Color.parseColor(this.baseColor));
            }
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v2, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: java.util.List<io.intercom.android.sdk.blocks.models.Block>} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void setUpWelcomeMessage() {
        /*
            r6 = this;
            r1 = 0
            io.intercom.com.google.gson.Gson r3 = r6.gson     // Catch:{ AssertionError -> 0x001b }
            java.lang.String r4 = r6.welcomeMessageJson     // Catch:{ AssertionError -> 0x001b }
            io.intercom.android.sdk.identity.AppConfig$1 r5 = new io.intercom.android.sdk.identity.AppConfig$1     // Catch:{ AssertionError -> 0x001b }
            r5.<init>()     // Catch:{ AssertionError -> 0x001b }
            java.lang.reflect.Type r5 = r5.getType()     // Catch:{ AssertionError -> 0x001b }
            java.lang.Object r3 = r3.fromJson((java.lang.String) r4, (java.lang.reflect.Type) r5)     // Catch:{ AssertionError -> 0x001b }
            r0 = r3
            java.util.List r0 = (java.util.List) r0     // Catch:{ AssertionError -> 0x001b }
            r1 = r0
        L_0x0016:
            if (r1 == 0) goto L_0x001a
            r6.welcomeMessage = r1
        L_0x001a:
            return
        L_0x001b:
            r2 = move-exception
            java.lang.String r3 = "Failed to parse JSON for welcome message"
            io.intercom.android.sdk.logger.IntercomLogger.WARNING(r3)
            goto L_0x0016
        */
        throw new UnsupportedOperationException("Method not decompiled: io.intercom.android.sdk.identity.AppConfig.setUpWelcomeMessage():void");
    }

    private String convertWelcomeMessageToJson() {
        try {
            return this.gson.toJson((Object) this.welcomeMessage);
        } catch (AssertionError e) {
            IntercomLogger.WARNING("Failed to parse JSON for welcome message");
            return "";
        }
    }

    public List<Block> getWelcomeMessage() {
        if (this.welcomeMessage.isEmpty() && !this.welcomeMessageJson.isEmpty()) {
            setUpWelcomeMessage();
        }
        return this.welcomeMessage;
    }

    public String getBaseColor() {
        return this.baseColor;
    }

    public Drawable getBaseColorAvatar() {
        return this.baseColorAvatar;
    }

    public boolean isShowPoweredBy() {
        return this.showPoweredBy;
    }

    public boolean isInboundMessages() {
        return this.inboundMessages;
    }

    public boolean isFirstRequest() {
        return this.isFirstRequest;
    }

    public int getPollingInterval() {
        return this.pollingInterval;
    }

    public int getNoRealtimeThrottle() {
        return this.noRealtimeThrottle;
    }

    public int getUserUpdateCacheMaxAge() {
        return this.userUpdateCacheMaxAge;
    }

    public int getRateLimitPeriod() {
        return this.rateLimitPeriod;
    }

    public int getRateLimitCount() {
        return this.rateLimitCount;
    }

    public boolean isRealTime() {
        return this.realTime && !this.realTimeConfig.getEndpoints().isEmpty();
    }

    public NexusConfig getRealTimeConfig() {
        return this.realTimeConfig;
    }

    public String getMessageResponse() {
        return this.messageResponse;
    }

    public boolean isAudioEnabled() {
        return this.audioEnabled;
    }

    public void resetRealTimeConfig() {
        this.realTimeConfig = new NexusConfig();
    }

    private boolean isNewConfig(Config config) {
        return (config.getMessageResponse().equals(this.messageResponse) && config.getWelcomeMessage().equals(this.welcomeMessage) && config.getRealTimeConfig().equals(this.realTimeConfig) && config.getBaseColor().equals(this.baseColor) && config.getPollingInterval() == this.pollingInterval && config.getNoRealtimeThrottle() == this.noRealtimeThrottle && config.getUserUpdateCacheMaxAge() == this.userUpdateCacheMaxAge && config.isAudioEnabled() == this.audioEnabled && config.isRealTime() == this.realTime && config.isShowPoweredBy() == this.showPoweredBy && config.isInboundMessages() == this.inboundMessages && config.isFirstRequest() == this.isFirstRequest && config.getRateLimitCount() == this.rateLimitCount && config.getRateLimitPeriod() == this.rateLimitPeriod) ? false : true;
    }

    private Drawable createColoredAvatar(Context context, int baseColor2) {
        Bitmap originalAvatar = BitmapFactory.decodeResource(context.getResources(), R.drawable.intercomsdk_avatar);
        if (originalAvatar == null) {
            return context.getResources().getDrawable(R.drawable.intercomsdk_avatar);
        }
        Bitmap newAvatar = originalAvatar;
        int originalColor = context.getResources().getColor(R.color.intercomsdk_main_blue);
        if (originalColor != baseColor2) {
            int width = originalAvatar.getWidth();
            int height = originalAvatar.getHeight();
            if (width > 0 && height > 0) {
                int[] pixelArray = new int[(width * height)];
                originalAvatar.getPixels(pixelArray, 0, width, 0, 0, width, height);
                for (int pixel = 0; pixel < pixelArray.length; pixel++) {
                    pixelArray[pixel] = pixelArray[pixel] == originalColor ? baseColor2 : pixelArray[pixel];
                }
                newAvatar = Bitmap.createBitmap(width, height, originalAvatar.getConfig());
                newAvatar.setPixels(pixelArray, 0, width, 0, 0, width, height);
            }
        }
        return new BitmapDrawable(context.getResources(), newAvatar);
    }
}
