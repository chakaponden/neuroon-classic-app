package com.inteliclinic.neuroon.managers.account;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.StringRes;
import android.util.Pair;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedHashTreeMap;
import com.inteliclinic.lucid.IMaskUserManager;
import com.inteliclinic.lucid.LucidConfiguration;
import com.inteliclinic.lucid.LucidEnv;
import com.inteliclinic.lucid.LucidManager;
import com.inteliclinic.lucid.data.Configuration;
import com.inteliclinic.neuroon.lib.R;
import com.inteliclinic.neuroon.managers.BaseManager;
import com.inteliclinic.neuroon.managers.DataManager;
import com.inteliclinic.neuroon.managers.account.events.AccessTokenUpdatedEvent;
import com.inteliclinic.neuroon.managers.account.events.ApplicationConfigUpdatedEvent;
import com.inteliclinic.neuroon.managers.account.events.ConfigsUpdatedEvent;
import com.inteliclinic.neuroon.managers.account.events.FlightReceivedEvent;
import com.inteliclinic.neuroon.managers.account.events.InvalidRefreshTokenEvent;
import com.inteliclinic.neuroon.managers.account.events.NetworkErrorEvent;
import com.inteliclinic.neuroon.managers.account.events.ReservationReceivedEvent;
import com.inteliclinic.neuroon.managers.account.events.ReservationReceivingErrorEvent;
import com.inteliclinic.neuroon.managers.account.events.UserConfigUpdatedEvent;
import com.inteliclinic.neuroon.managers.account.events.UserPasswordUpdateErrorEvent;
import com.inteliclinic.neuroon.managers.context.ContextManager;
import com.inteliclinic.neuroon.managers.network.MaskSoftwareDownloadErrorEvent;
import com.inteliclinic.neuroon.managers.network.MaskSoftwareDownloadProgressEvent;
import com.inteliclinic.neuroon.managers.network.MaskSoftwareDownloadedEvent;
import com.inteliclinic.neuroon.managers.network.NetworkManager;
import com.inteliclinic.neuroon.managers.network.callbacks.BaseTokenCallback;
import com.inteliclinic.neuroon.managers.network.callbacks.LoginCallback;
import com.inteliclinic.neuroon.managers.network.callbacks.RegisterCallback;
import com.inteliclinic.neuroon.mask.IUserAlarmManager;
import com.inteliclinic.neuroon.mask.MaskFirmwareCheck;
import com.inteliclinic.neuroon.mask.MaskManager;
import com.inteliclinic.neuroon.models.data.Airport;
import com.inteliclinic.neuroon.models.data.Tip;
import com.inteliclinic.neuroon.models.network.AccessToken;
import com.inteliclinic.neuroon.models.network.ChangePasswordModel;
import com.inteliclinic.neuroon.models.network.Flight;
import com.inteliclinic.neuroon.models.network.MaskFirmwareMeta;
import com.inteliclinic.neuroon.models.network.RegisterUser;
import com.inteliclinic.neuroon.models.network.Reservation;
import com.inteliclinic.neuroon.models.network.SleepRecordingMeta;
import com.inteliclinic.neuroon.models.network.User;
import com.inteliclinic.neuroon.settings.AppConfig;
import com.inteliclinic.neuroon.settings.UserConfig;
import com.inteliclinic.neuroon.utils.DateUtils;
import de.greenrobot.event.EventBus;
import io.fabric.sdk.android.services.common.CommonUtils;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

public final class AccountManager extends BaseManager implements IMaskUserManager, IUserAlarmManager, IIntercomSettings {
    private static final String ACCOUNT_MANAGER_ACCESS_TOKEN = "account_manager_access_token";
    private static final String ACCOUNT_MANAGER_FILE = "account_manager_preferences";
    private static final String ACCOUNT_MANAGER_USERNAME = "account_manager_username";
    private static AccountManager mInstance;
    /* access modifiers changed from: private */
    public AccessToken mAccessToken;
    /* access modifiers changed from: private */
    public final Context mContext;
    private boolean mSynchronizeConfigs;
    /* access modifiers changed from: private */
    public String mUsername;

    private AccountManager(Context context) {
        this.mContext = context;
        EventBus.getDefault().register(this);
        readManagerFromFile(context);
    }

    public static AccountManager getInstance() {
        if (mInstance != null) {
            return mInstance;
        }
        throw new UnsupportedOperationException("Account Manager should be firstly created by Supervisor");
    }

    public static AccountManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new AccountManager(context);
        }
        return mInstance;
    }

    public void destroy(Context context) {
        saveManagerToFile(context);
    }

    private void readManagerFromFile(Context context) {
        this.mAccessToken = (AccessToken) new Gson().fromJson(getSharedPreferences(context).getString(ACCOUNT_MANAGER_ACCESS_TOKEN, ""), AccessToken.class);
        this.mUsername = (String) new Gson().fromJson(getSharedPreferences(context).getString(ACCOUNT_MANAGER_USERNAME, ""), String.class);
    }

    private SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(ACCOUNT_MANAGER_FILE, 4);
    }

    private void saveManagerToFile(Context context) {
        getSharedPreferences(context).edit().putString(ACCOUNT_MANAGER_ACCESS_TOKEN, new Gson().toJson((Object) this.mAccessToken)).commit();
        getSharedPreferences(context).edit().putString(ACCOUNT_MANAGER_USERNAME, new Gson().toJson((Object) this.mUsername)).commit();
    }

    public boolean isLogged() {
        return (this.mAccessToken == null || this.mAccessToken.getAccessToken() == null) ? false : true;
    }

    public void login(final String username, String password, final LoginCallback loginCallback) {
        NetworkManager.getInstance().login(username, password, new LoginCallback() {
            public void success(AccessToken accessToken, Response response) {
                AccessToken unused = AccountManager.this.mAccessToken = AccessToken.changeInstance(AccountManager.this.mAccessToken, accessToken);
                String unused2 = AccountManager.this.mUsername = username;
                AccountManager.this.saveToFile();
                loginCallback.success(accessToken, response);
                EventBus.getDefault().post(new AccessTokenUpdatedEvent((NetworkManager.RequestKey) null));
            }

            public void failure(RetrofitError error) {
                loginCallback.failure(error);
            }
        });
    }

    public synchronized AccessToken refreshToken() {
        checkAccessToken();
        try {
            this.mAccessToken = AccessToken.changeInstance(this.mAccessToken, NetworkManager.getInstance().refreshToken(this.mAccessToken));
            saveToFile();
            EventBus.getDefault().post(new AccessTokenUpdatedEvent((NetworkManager.RequestKey) null));
        } catch (RetrofitError error) {
            if (error.getKind() == RetrofitError.Kind.HTTP) {
                EventBus.getDefault().post(new InvalidRefreshTokenEvent((NetworkManager.RequestKey) null, error));
            }
        }
        return this.mAccessToken;
    }

    public void register(RegisterUser registerUser, RegisterCallback registerCallback) {
        NetworkManager.getInstance().register(registerUser, registerCallback);
    }

    public void signOut() {
        this.mAccessToken = null;
        saveToFile();
        LucidConfiguration.removeUserConfig(this.mContext);
        getLucid().reloadLucid(this.mContext);
    }

    public void getFlights(String reservationName, String reservationNumber) {
        checkAccessToken();
        NetworkManager.getInstance().getReservation(this.mAccessToken, reservationName.toUpperCase(), reservationNumber.toUpperCase(), new BaseTokenCallback<Reservation>() {
            public void failure(RetrofitError error) {
                if (!handleFailure(error)) {
                    EventBus.getDefault().post(new ReservationReceivingErrorEvent());
                }
            }

            public void success(Reservation reservation, Response response) {
                EventBus.getDefault().post(new ReservationReceivedEvent(reservation));
            }
        });
    }

    public void getFlights(Airport origin, Airport destination, Date startDate) {
        String month;
        String day;
        checkAccessToken();
        Calendar instance = Calendar.getInstance();
        instance.setTime(startDate);
        int i = instance.get(2) + 1;
        if (i < 10) {
            month = "0" + i;
        } else {
            month = String.valueOf(i);
        }
        int i2 = instance.get(5);
        if (i2 < 10) {
            day = "0" + i2;
        } else {
            day = String.valueOf(i2);
        }
        NetworkManager.getInstance().getFlights(this.mAccessToken, instance.get(1), month, day, origin.getIcao(), destination.getIcao(), new BaseTokenCallback<List<Flight>>() {
            public void success(List<Flight> flights, Response response) {
                EventBus.getDefault().post(new FlightReceivedEvent(flights));
            }
        });
    }

    public void getFlights(String[] flightCodes, Date startDate) {
        String month;
        String day;
        checkAccessToken();
        Calendar instance = Calendar.getInstance();
        instance.setTime(startDate);
        int i = instance.get(2) + 1;
        if (i < 10) {
            month = "0" + i;
        } else {
            month = String.valueOf(i);
        }
        int i2 = instance.get(5);
        if (i2 < 10) {
            day = "0" + i2;
        } else {
            day = String.valueOf(i2);
        }
        NetworkManager.getInstance().getFlights(this.mAccessToken, flightCodes[0], instance.get(1), month, day, new BaseTokenCallback<List<Flight>>() {
            public void success(List<Flight> flights, Response response) {
                EventBus.getDefault().post(new FlightReceivedEvent(flights));
            }
        });
    }

    public String getLucidDelegateKey() {
        return "account-manager";
    }

    /* access modifiers changed from: private */
    public void saveToFile() {
        saveToFile(this.mContext);
    }

    public void saveToFile(Context context) {
        saveManagerToFile(context);
    }

    public void synchronizeTips(final Callback callback) {
        checkAccessToken();
        NetworkManager.getInstance().getTips(this.mAccessToken, DataManager.getInstance().getMaxTipsVersion(), Locale.getDefault().getLanguage(), ContextManager.getInstance().getTags(), new BaseTokenCallback<List<Tip>>() {
            public void success(List<Tip> tipNetworks, Response response) {
                DataManager.getInstance().saveTips(Tip.filterNotInDb(tipNetworks));
                if (callback != null) {
                    callback.success(null, (Response) null);
                }
            }

            public void failure(RetrofitError error) {
                super.failure(error);
                if (callback != null) {
                    callback.failure(error);
                }
            }
        });
    }

    private void checkAccessToken() {
        if (this.mAccessToken == null) {
            throw new UnsupportedOperationException("User not logged in");
        }
    }

    private void synchronizeAppConfig() {
        checkAccessToken();
        NetworkManager.getInstance().getAppConfig(this.mAccessToken, LucidConfiguration.getAppConfigVersion(this.mContext) + 1, LucidEnv.getVersion(), new BaseTokenCallback<List<Configuration>>() {
            public void success(List<Configuration> configurations, Response response) {
                if (configurations == null || configurations.size() <= 0) {
                    EventBus.getDefault().post(new ApplicationConfigUpdatedEvent(false));
                    return;
                }
                AccountManager.this.saveAppConfig(configurations.get(0));
                EventBus.getDefault().post(new ApplicationConfigUpdatedEvent(true));
            }

            /* access modifiers changed from: protected */
            public boolean handleFailure(RetrofitError error) {
                if (!super.handleFailure(error)) {
                    EventBus.getDefault().post(new NetworkErrorEvent(NetworkManager.RequestKey.APP_CONFIG, error));
                }
                return true;
            }
        });
    }

    private void synchronizeUserConfig() {
        checkAccessToken();
        int version = LucidConfiguration.getUserConfigurationVersion(this.mContext);
        if (!hasConfig() || version == 0) {
            NetworkManager.getInstance().getUserConfig(this.mAccessToken, 0, new BaseTokenCallback<List<Object>>() {
                public void success(List<Object> configurations, Response response) {
                    if (configurations != null && configurations.size() > 0) {
                        if (AccountManager.this.hasConfig()) {
                            AccountManager.this.saveUserConfig(new Configuration(AccountManager.this.getLucid().getUserConfig(), ((Double) ((Map) configurations.get(0)).get("version")).intValue()));
                        } else {
                            String s = new Gson().toJson(configurations.get(0), (Type) Map.class);
                            try {
                                AccountManager.this.saveUserConfig((Configuration) new Gson().fromJson(s, Configuration.class));
                            } catch (JsonSyntaxException e) {
                                if (AccountManager.this.getLucid() == null || AccountManager.this.getLucid().getUserConfig() == null || AccountManager.this.getLucid().getUserConfig() == null) {
                                    AccountManager.this.saveUserConfig(new Configuration(new LinkedHashTreeMap(), ((Double) ((Map) configurations.get(0)).get("version")).intValue()));
                                } else {
                                    AccountManager.this.saveUserConfig(new Configuration(AccountManager.this.getLucid().getUserConfig(), ((Double) ((Map) configurations.get(0)).get("version")).intValue()));
                                }
                            }
                        }
                        Object unused = AccountManager.this.lucidGlobalSet(Integer.class, UserConfig.DEEP_TRESHOLD_PROCESSED_SLEEP_COUNT, 0);
                        Object unused2 = AccountManager.this.lucidGlobalSet(Integer.class, UserConfig.DEEP_TRESHOLD_SUM, 0);
                        MaskManager.getInstance().setSavedSleep(0);
                    }
                    EventBus.getDefault().post(new UserConfigUpdatedEvent());
                }
            });
            return;
        }
        final Configuration userConfig = new Configuration(getLucid().getUserConfig(), version);
        NetworkManager.getInstance().postUserConfig(this.mAccessToken, userConfig, version, new BaseTokenCallback<Configuration>() {
            public void success(Configuration configuration, Response response) {
                AccountManager.this.saveUserConfig(userConfig);
                EventBus.getDefault().post(new UserConfigUpdatedEvent());
            }

            /* access modifiers changed from: protected */
            public boolean handleFailure(RetrofitError error) {
                if (!super.handleFailure(error)) {
                    EventBus.getDefault().post(new NetworkErrorEvent(NetworkManager.RequestKey.USER_CONFIG, error));
                }
                return true;
            }
        });
    }

    public void checkMaskFirmware() {
        checkAccessToken();
        NetworkManager.getInstance().getMaskFirmwareMeta(this.mAccessToken, new BaseTokenCallback<List<MaskFirmwareMeta>>() {
            public void success(List<MaskFirmwareMeta> maskFirmwareMetas, Response response) {
                EventBus.getDefault().removeStickyEvent(MaskFirmwareCheck.class);
                if (maskFirmwareMetas == null || maskFirmwareMetas.size() <= 0) {
                    EventBus.getDefault().postSticky(new MaskFirmwareCheck(false));
                } else {
                    EventBus.getDefault().postSticky(new MaskFirmwareCheck(maskFirmwareMetas.get(0)));
                }
            }

            /* access modifiers changed from: protected */
            public boolean handleFailure(RetrofitError error) {
                if (!super.handleFailure(error)) {
                    EventBus.getDefault().removeStickyEvent(MaskFirmwareCheck.class);
                    EventBus.getDefault().postSticky(new MaskFirmwareCheck(false));
                }
                return true;
            }
        });
    }

    public void onEvent(ApplicationConfigUpdatedEvent event) {
        if (this.mSynchronizeConfigs) {
            synchronizeUserConfig();
        } else {
            EventBus.getDefault().post(new ConfigsUpdatedEvent());
        }
    }

    public void onEvent(UserConfigUpdatedEvent event) {
        if (this.mSynchronizeConfigs) {
            EventBus.getDefault().post(new ConfigsUpdatedEvent());
        }
        this.mSynchronizeConfigs = false;
    }

    public void synchronizeConfigs() {
        this.mSynchronizeConfigs = true;
        synchronizeAppConfig();
    }

    /* access modifiers changed from: private */
    public void saveAppConfig(Configuration configuration) {
        LucidConfiguration.saveAppConfig(this.mContext, configuration);
        if (!this.mSynchronizeConfigs) {
            getLucid().reloadLucid(this.mContext);
        }
    }

    /* access modifiers changed from: private */
    public void saveUserConfig(Configuration configuration) {
        LucidConfiguration.saveUserConfig(this.mContext, configuration, true);
        getLucid().reloadLucid(this.mContext);
    }

    public String getUsername() {
        return this.mUsername;
    }

    public boolean hasConfig() {
        return LucidConfiguration.getUserConfigurationLastUpdate(this.mContext) > 0;
    }

    public boolean shouldConfigBeUpdated() {
        int integer = LucidConfiguration.getUserConfigurationLastUpdate(this.mContext);
        if (integer == 0 || ((long) integer) < (new Date().getTime() / 1000) - 86000) {
            return true;
        }
        Double integer1 = (Double) lucidRead(Double.class, LucidManager.LAST_CONFIG_CHANGE_TIME);
        if (integer1 == null || integer1.doubleValue() <= ((double) integer)) {
            return false;
        }
        return true;
    }

    public boolean hasToShowJetLagDialog() {
        Boolean a = (Boolean) lucidRead(Boolean.class, UserConfig.DONT_SHOW_JET_LAG);
        return a == null || !a.booleanValue();
    }

    public void dontShowJetLagDialog(boolean checked) {
        lucidSet(Boolean.class, UserConfig.DONT_SHOW_JET_LAG, Boolean.valueOf(checked));
        lucidSave(this.mContext);
    }

    public boolean isOneDayAlarmTurned() {
        Boolean a = (Boolean) lucidRead(Boolean.class, UserConfig.ONE_DAY_ALARM_ON);
        if (a == null) {
            return false;
        }
        return a.booleanValue();
    }

    public void setOneDayAlarmTurned(boolean oneDayAlarmTurned) {
        lucidSet(Boolean.class, UserConfig.ONE_DAY_ALARM_ON, Boolean.valueOf(oneDayAlarmTurned));
    }

    public Integer getOneDayAlarmTimeAsTimestamp() {
        Double date = (Double) lucidRead(Double.class, UserConfig.ONE_DAY_ALARM_TIME);
        if (date == null) {
            date = Double.valueOf(25200.0d);
        }
        return Integer.valueOf(date.intValue());
    }

    public Date getOneDayAlarmTime() {
        Date date = DateUtils.resetDateToToday(DateUtils.timeInUtc(getOneDayAlarmTimeAsTimestamp()));
        return date.after(new Date()) ? date : DateUtils.dateAddSeconds(date, 86400);
    }

    public void setOneDayAlarmTime(int hourOfDay, int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis((long) (((hourOfDay * 60) + minutes) * 60 * 1000));
        lucidSet(Date.class, UserConfig.ONE_DAY_ALARM_TIME, cal.getTime());
        lucidSave(this.mContext);
    }

    public boolean isBiorhythmDaysSeparated() {
        Boolean a = (Boolean) lucidRead(Boolean.class, UserConfig.BIORHYTHM_DAYS_SEPARATED);
        return a != null && a.booleanValue();
    }

    public void setBiorhythmDaysSeparated(boolean state) {
        lucidSet(Boolean.class, UserConfig.BIORHYTHM_DAYS_SEPARATED, Boolean.valueOf(state));
        lucidSave(this.mContext);
    }

    public ArrayList<Integer> getBiorhythmAlarms() {
        ArrayList<Integer> arrayList = (ArrayList) lucidRead(ArrayList.class, UserConfig.BIORHYTHM_ALARMS);
        if (arrayList == null) {
            arrayList = new ArrayList<>(7);
            for (int i = 0; i < 7; i++) {
                arrayList.add(25200);
            }
        }
        return arrayList;
    }

    public void setBiorhythmAlarms(ArrayList<Integer> alarms) {
        lucidSet(ArrayList.class, UserConfig.BIORHYTHM_ALARMS, alarms);
        lucidSave(this.mContext);
    }

    public long getNextEventTimeMillis() {
        if (getInstance().isOneDayAlarmTurned()) {
            return getInstance().getOneDayAlarmTime().getTime();
        }
        return getInstance().getNextBiorhythmAlarm().getTime();
    }

    public Date getNextBiorhythmAlarm() {
        Date date = DateUtils.resetDateToToday(DateUtils.timeInUtc(getBiorhythmAlarms().get(DateUtils.getDayOfWeekAsIndex())));
        if (date.after(new Date())) {
            return date;
        }
        return DateUtils.dateAddSeconds(DateUtils.resetDateToToday(DateUtils.timeInUtc(getBiorhythmAlarms().get(DateUtils.getNextDayOfWeekAsIndex()))), 86400);
    }

    public boolean isJetLagMainScreenHelpShown() {
        Boolean a = (Boolean) lucidRead(Boolean.class, UserConfig.JET_LAG_MAIN_SCREEN_HELP_SHOWN);
        if (a == null) {
            return false;
        }
        return a.booleanValue();
    }

    public void setJetLagMainScreenHelpShown() {
        lucidSet(Boolean.class, UserConfig.JET_LAG_MAIN_SCREEN_HELP_SHOWN, true);
        lucidSave(this.mContext);
    }

    public boolean hasToShowReservationNumberDialog() {
        Boolean a = (Boolean) lucidRead(Boolean.class, UserConfig.DONT_SHOW_RESERVATION_NUMBER);
        return a == null || !a.booleanValue();
    }

    public void dontShowReservationNumberDialog(boolean checked) {
        lucidSet(Boolean.class, UserConfig.DONT_SHOW_RESERVATION_NUMBER, Boolean.valueOf(checked));
        lucidSave(this.mContext);
    }

    public boolean getSex() {
        Boolean aBoolean = (Boolean) lucidRead(Boolean.class, UserConfig.USER_SEX);
        if (aBoolean == null) {
            return false;
        }
        return aBoolean.booleanValue();
    }

    public void setSex(boolean isWomen) {
        lucidSet(Boolean.class, UserConfig.USER_SEX, Boolean.valueOf(isWomen));
        lucidSave(this.mContext);
    }

    @StringRes
    public int getSexAsStringRes() {
        if (!getSex()) {
            return R.string.male;
        }
        return R.string.female;
    }

    public Integer getBirthDate() {
        return (Integer) lucidRead(Integer.class, UserConfig.USER_BIRTH_DATE);
    }

    public void setBirthDate(Date date) {
        lucidSet(Integer.class, UserConfig.USER_BIRTH_DATE, Integer.valueOf((int) (date.getTime() / 1000)));
        lucidSave(this.mContext);
    }

    public boolean hasPersonalData() {
        return lucidRead(Integer.class, UserConfig.USER_BIRTH_DATE) != null;
    }

    public double getHeight() {
        Double integer = (Double) lucidRead(Double.class, UserConfig.USER_HEIGHT);
        if (integer == null) {
            return 160.0d;
        }
        return integer.doubleValue();
    }

    public void saveHeight(double value) {
        lucidSet(Double.class, UserConfig.USER_HEIGHT, Double.valueOf(value));
        lucidSave(this.mContext);
    }

    public double getWeight() {
        Double integer = (Double) lucidRead(Double.class, UserConfig.USER_WEIGHT);
        if (integer == null) {
            return 70.0d;
        }
        return integer.doubleValue();
    }

    public void saveWeight(double value) {
        lucidSet(Double.class, UserConfig.USER_WEIGHT, Double.valueOf(value));
        lucidSave(this.mContext);
    }

    public List<Pair<String, Boolean>> getSleepHabits() {
        List<Pair<String, Boolean>> ret = new ArrayList<>(7);
        Object obj = lucidRead(Object.class, UserConfig.USER_SLEEP_HABITS);
        ArrayList list = null;
        if (obj instanceof List) {
            list = (ArrayList) obj;
        }
        String[] stringArray = this.mContext.getResources().getStringArray(R.array.sleep_habits);
        for (int i = 0; i < 10; i++) {
            sleepHabit(ret, list, i + 1, stringArray[i]);
        }
        return ret;
    }

    private void sleepHabit(List<Pair<String, Boolean>> ret, ArrayList list, int i, String habbit) {
        if (list == null || list.size() < i) {
            ret.add(new Pair(habbit, false));
        } else {
            ret.add(new Pair(habbit, (Boolean) list.get(i - 1)));
        }
    }

    public void setUserHabits(List<Boolean> values) {
        lucidSet(List.class, Object.class, UserConfig.USER_SLEEP_HABITS, values);
        lucidSave(this.mContext);
    }

    /* access modifiers changed from: private */
    public void saveUser(User user) {
        lucidSet(Integer.class, UserConfig.USER_ID, Integer.valueOf(user.getId()));
        lucidSet(String.class, UserConfig.USER_EMAIL, user.getUsername());
        lucidSave(this.mContext);
    }

    public int getUserId() {
        Integer integer = (Integer) lucidRead(Integer.class, UserConfig.USER_ID);
        if (integer == null) {
            return -1;
        }
        return integer.intValue();
    }

    public byte[] getUserMaskHash() {
        Integer integer = (Integer) lucidRead(Integer.class, UserConfig.USER_ID);
        if (integer == null) {
            return new byte[]{-86, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        }
        return md5(String.valueOf(integer));
    }

    private byte[] md5(String in) {
        try {
            MessageDigest digest = MessageDigest.getInstance(CommonUtils.MD5_INSTANCE);
            digest.reset();
            digest.update(in.getBytes());
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void getMaskFirmware(int version) {
        EventBus.getDefault().postSticky(new MaskSoftwareDownloadProgressEvent(false, 0, 1));
        NetworkManager.getInstance().getMaskFirmware(this.mAccessToken, version, new BaseTokenCallback<Response>() {
            public void success(Response o, Response response) {
                try {
                    FileOutputStream outputStream = AccountManager.this.mContext.openFileOutput("mask_firmware", 0);
                    InputStream is = response.getBody().in();
                    byte[] buff = new byte[4096];
                    long downloaded = 0;
                    long toDownload = response.getBody().length();
                    EventBus.getDefault().removeStickyEvent(MaskSoftwareDownloadProgressEvent.class);
                    EventBus.getDefault().postSticky(new MaskSoftwareDownloadProgressEvent(false, 0, toDownload));
                    while (true) {
                        int readed = is.read(buff);
                        if (readed == -1) {
                            outputStream.close();
                            EventBus.getDefault().removeStickyEvent(MaskSoftwareDownloadProgressEvent.class);
                            EventBus.getDefault().postSticky(new MaskSoftwareDownloadedEvent());
                            return;
                        }
                        outputStream.write(buff);
                        downloaded += (long) readed;
                        EventBus.getDefault().removeStickyEvent(MaskSoftwareDownloadProgressEvent.class);
                        EventBus.getDefault().postSticky(new MaskSoftwareDownloadProgressEvent(false, downloaded, toDownload));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    EventBus.getDefault().postSticky(new MaskSoftwareDownloadErrorEvent());
                }
            }
        });
    }

    public void changePassword(final String oldPassword, final String newPassword, final BaseTokenCallback<Object> callback) {
        checkAccessToken();
        if (getUserId() != -1) {
            NetworkManager.getInstance().changePassword(this.mAccessToken, getUserId(), new ChangePasswordModel(oldPassword, newPassword), callback);
        } else {
            NetworkManager.getInstance().getUser(this.mAccessToken, new BaseTokenCallback<User>() {
                public void success(User user, Response response) {
                    AccountManager.this.saveUser(user);
                    NetworkManager.getInstance().changePassword(AccountManager.this.mAccessToken, AccountManager.this.getUserId(), new ChangePasswordModel(oldPassword, newPassword), callback);
                }

                public void failure(RetrofitError error) {
                    EventBus.getDefault().postSticky(new UserPasswordUpdateErrorEvent());
                }
            });
        }
    }

    public void syncUserId() {
        checkAccessToken();
        NetworkManager.getInstance().getUser(this.mAccessToken, new BaseTokenCallback<User>() {
            public void success(User user, Response response) {
                AccountManager.this.saveUser(user);
            }

            public void failure(RetrofitError error) {
            }
        });
    }

    public Date getNextAlarmTime() {
        if (getInstance().isOneDayAlarmTurned()) {
            return getInstance().getOneDayAlarmTime();
        }
        return getInstance().getNextBiorhythmAlarm();
    }

    public List<SleepRecordingMeta> getSleepMetas() {
        checkAccessToken();
        return NetworkManager.getInstance().getSleepMetas(this.mAccessToken);
    }

    public SleepRecordingMeta getSleepMeta(long id) {
        checkAccessToken();
        return NetworkManager.getInstance().getSleepMeta(this.mAccessToken, id);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x005a, code lost:
        r7.setId(-1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        return r7;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.inteliclinic.neuroon.models.network.SleepRecordingMeta uploadSleepMeta(com.inteliclinic.neuroon.models.network.SleepRecordingMeta r7) {
        /*
            r6 = this;
            if (r7 != 0) goto L_0x0004
            r7 = 0
        L_0x0003:
            return r7
        L_0x0004:
            com.inteliclinic.neuroon.managers.network.INetworkManager r3 = com.inteliclinic.neuroon.managers.network.NetworkManager.getInstance()     // Catch:{ RetrofitError -> 0x000f, Exception -> 0x0060 }
            com.inteliclinic.neuroon.models.network.AccessToken r4 = r6.mAccessToken     // Catch:{ RetrofitError -> 0x000f, Exception -> 0x0060 }
            com.inteliclinic.neuroon.models.network.SleepRecordingMeta r7 = r3.postSleepMeta(r4, r7)     // Catch:{ RetrofitError -> 0x000f, Exception -> 0x0060 }
            goto L_0x0003
        L_0x000f:
            r1 = move-exception
            retrofit.client.Response r3 = r1.getResponse()
            if (r3 == 0) goto L_0x005a
            retrofit.client.Response r3 = r1.getResponse()
            int r3 = r3.getStatus()
            r4 = 409(0x199, float:5.73E-43)
            if (r3 != r4) goto L_0x005a
            com.google.gson.Gson r3 = new com.google.gson.Gson     // Catch:{ IOException -> 0x0050 }
            r3.<init>()     // Catch:{ IOException -> 0x0050 }
            java.io.InputStreamReader r4 = new java.io.InputStreamReader     // Catch:{ IOException -> 0x0050 }
            retrofit.client.Response r5 = r1.getResponse()     // Catch:{ IOException -> 0x0050 }
            retrofit.mime.TypedInput r5 = r5.getBody()     // Catch:{ IOException -> 0x0050 }
            java.io.InputStream r5 = r5.in()     // Catch:{ IOException -> 0x0050 }
            r4.<init>(r5)     // Catch:{ IOException -> 0x0050 }
            java.lang.Class<com.inteliclinic.neuroon.models.network.SleepRecordingMeta> r5 = com.inteliclinic.neuroon.models.network.SleepRecordingMeta.class
            java.lang.Object r2 = r3.fromJson((java.io.Reader) r4, r5)     // Catch:{ IOException -> 0x0050 }
            com.inteliclinic.neuroon.models.network.SleepRecordingMeta r2 = (com.inteliclinic.neuroon.models.network.SleepRecordingMeta) r2     // Catch:{ IOException -> 0x0050 }
            long r4 = r2.getId()     // Catch:{ IOException -> 0x0050 }
            r7.setId(r4)     // Catch:{ IOException -> 0x0050 }
            long r4 = r2.getId()     // Catch:{ IOException -> 0x0050 }
            com.inteliclinic.neuroon.models.network.SleepRecordingMeta r7 = r6.patchSleepMeta(r4, r7)     // Catch:{ IOException -> 0x0050 }
            goto L_0x0003
        L_0x0050:
            r0 = move-exception
            com.crashlytics.android.Crashlytics r3 = com.crashlytics.android.Crashlytics.getInstance()
            if (r3 == 0) goto L_0x005a
            com.crashlytics.android.Crashlytics.logException(r0)
        L_0x005a:
            r4 = -1
            r7.setId(r4)
            goto L_0x0003
        L_0x0060:
            r1 = move-exception
            com.crashlytics.android.Crashlytics r3 = com.crashlytics.android.Crashlytics.getInstance()
            if (r3 == 0) goto L_0x005a
            com.crashlytics.android.Crashlytics.logException(r1)
            goto L_0x005a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.inteliclinic.neuroon.managers.account.AccountManager.uploadSleepMeta(com.inteliclinic.neuroon.models.network.SleepRecordingMeta):com.inteliclinic.neuroon.models.network.SleepRecordingMeta");
    }

    public SleepRecordingMeta patchSleepMeta(long id, SleepRecordingMeta sleepRecordingMeta) {
        if (sleepRecordingMeta == null) {
            return null;
        }
        try {
            return NetworkManager.getInstance().patchSleepMeta(this.mAccessToken, id, sleepRecordingMeta);
        } catch (RetrofitError e) {
            return null;
        } catch (Exception ex) {
            if (Crashlytics.getInstance() == null) {
                return null;
            }
            Crashlytics.logException(ex);
            return null;
        }
    }

    public byte[] getSleepData(long id) {
        checkAccessToken();
        try {
            Response response = NetworkManager.getInstance().getSleepData(this.mAccessToken, id);
            if (response.getStatus() == 200) {
                return ((TypedByteArray) response.getBody()).getBytes();
            }
        } catch (RetrofitError ex) {
            if (ex.getResponse() != null && ex.getResponse().getStatus() == 404) {
                return null;
            }
        } catch (Exception ex2) {
            if (Crashlytics.getInstance() != null) {
                Crashlytics.logException(ex2);
            }
        }
        return null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0044 A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int uploadSleepData(com.inteliclinic.neuroon.models.data.Sleep r11) {
        /*
            r10 = this;
            com.inteliclinic.neuroon.managers.network.INetworkManager r2 = com.inteliclinic.neuroon.managers.network.NetworkManager.getInstance()     // Catch:{ RetrofitError -> 0x0025, Exception -> 0x003a }
            com.inteliclinic.neuroon.models.network.AccessToken r3 = r10.mAccessToken     // Catch:{ RetrofitError -> 0x0025, Exception -> 0x003a }
            long r4 = r11.getServerId()     // Catch:{ RetrofitError -> 0x0025, Exception -> 0x003a }
            com.inteliclinic.neuroon.managers.network.TypedFileFromBytes r6 = new com.inteliclinic.neuroon.managers.network.TypedFileFromBytes     // Catch:{ RetrofitError -> 0x0025, Exception -> 0x003a }
            java.lang.String r7 = "application/octet-stream"
            java.lang.String r8 = "data.bin"
            byte[] r9 = r11.getRawData()     // Catch:{ RetrofitError -> 0x0025, Exception -> 0x003a }
            r6.<init>(r7, r8, r9)     // Catch:{ RetrofitError -> 0x0025, Exception -> 0x003a }
            retrofit.client.Response r1 = r2.postSleepData(r3, r4, r6)     // Catch:{ RetrofitError -> 0x0025, Exception -> 0x003a }
            int r2 = r1.getStatus()     // Catch:{ RetrofitError -> 0x0025, Exception -> 0x003a }
            r3 = 201(0xc9, float:2.82E-43)
            if (r2 != r3) goto L_0x0044
            r2 = 1
        L_0x0024:
            return r2
        L_0x0025:
            r0 = move-exception
            retrofit.client.Response r2 = r0.getResponse()
            if (r2 == 0) goto L_0x0044
            retrofit.client.Response r2 = r0.getResponse()
            int r2 = r2.getStatus()
            r3 = 404(0x194, float:5.66E-43)
            if (r2 != r3) goto L_0x0044
            r2 = -1
            goto L_0x0024
        L_0x003a:
            r0 = move-exception
            com.crashlytics.android.Crashlytics r2 = com.crashlytics.android.Crashlytics.getInstance()
            if (r2 == 0) goto L_0x0044
            com.crashlytics.android.Crashlytics.logException(r0)
        L_0x0044:
            r2 = 0
            goto L_0x0024
        */
        throw new UnsupportedOperationException("Method not decompiled: com.inteliclinic.neuroon.managers.account.AccountManager.uploadSleepData(com.inteliclinic.neuroon.models.data.Sleep):int");
    }

    public boolean isIntercomEnabled() {
        Boolean enabled = (Boolean) lucidRead(Boolean.class, AppConfig.INTERCOM_ENABLED);
        return enabled != null && enabled.booleanValue();
    }

    public void checkMask(String serial) {
        checkAccessToken();
        try {
            Response response = NetworkManager.getInstance().getMaskStatus(this.mAccessToken, serial);
            if (response.getStatus() != 200 && Crashlytics.getInstance() != null) {
                Crashlytics.logException(new UnsupportedOperationException("Check mask returned unexpected code:" + response.getStatus()));
            }
        } catch (RetrofitError ex) {
            if (ex.getResponse() != null && ex.getResponse().getStatus() == 417) {
                getInstance().setThiefMarker();
                EventBus.getDefault().post(new UserAccountStatusChangedEvent());
            }
        } catch (Exception ex2) {
            if (Crashlytics.getInstance() != null) {
                Crashlytics.logException(ex2);
            }
        }
    }

    private void setThiefMarker() {
        lucidSet(Boolean.class, UserConfig.IS_THIEF, true);
    }

    public int canUserUseApp() {
        Boolean thief = (Boolean) lucidRead(Boolean.class, UserConfig.IS_THIEF);
        if (thief == null || !thief.booleanValue()) {
            return 0;
        }
        return 1;
    }
}
