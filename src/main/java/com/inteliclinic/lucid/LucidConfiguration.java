package com.inteliclinic.lucid;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.inteliclinic.lucid.data.Configuration;
import com.inteliclinic.neuroon.settings.UserConfig;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;
import java.util.Map;

public class LucidConfiguration implements ILucidConfiguration {
    private static final String LUCID_CONFIG_FILE = "lucid_file_name";
    private static final Configuration RSL = ((Configuration) new Gson().fromJson("{\"version\":1,\"configuration\":{\"debug-print\":\"(fn (s) (do (print s) s))\", \"false?\":\"(. not true?)\",\"count\":\"(fn (p xs) (if (empty? xs) 0 (+ (if (p (head xs)) 1 0) (count p (tail xs)))))\",\"any?\":\"(fn (p xs) (if (empty? xs) #f (if (p (head xs)) #t (any? p (tail xs)))))\",\"none?\":\"(. not any?)\",\"all?\":\"(fn (p xs) (if (empty? xs) #t (if (p (head xs)) (all? p (tail xs)) #f)))\",\"<=\":\"(fn (x y) (or (= x y) (< x y)))\",\">\":\"(fn (x y)  (not (<= x y)))\",\">=\":\"(fn (x y) (not (< x y)))\",\"bigger\":\"(fn (x y) (if (< x y) y x))\",\"lesser\":\"(fn (x y) (if (< x y) x y))\",\"fold\":\"(fn (f s xs) (if (empty? xs) s (fold f (f s (head xs)) (tail xs))))\",\"sum\":\"(fn (xs) (fold + 0 xs))\",\"product\":\"(fn (xs) (fold * 1 xs))\",\"maximum\":\"(fn (xs) (fold bigger -922337203686 xs))\",\"minimum\":\"(fn (xs) (fold lesser 922337203685 xs))\",\"flip\":\"(fn (f) (fn (x y) (f y x)))\",\"const\":\"(fn (x) (fn (y) x))\",\"id\":\"(fn (x) x)\",\"zip\":\"(fn (l1 l2) (if (or (empty? l1) (empty? l2)) () (cons (list (head l1) (head l2)) (zip (tail l1) (tail l2)))))\",\"zip-with\":\"(fn (f l1 l2) (if (or (empty? l1) (empty? l2)) () (cons (f (head l1) (head l2)) (zip-with f (tail l1) (tail l2)))))\",\"map\":\"(fn (f xs) (if (empty? xs) () (cons (f (head xs)) (map f (tail xs)))))\",\"filter\":\"(fn (p xs) (if (empty? xs) () (if (p (head xs)) (cons (head xs) (filter p (tail xs))) (filter p (tail xs)))))\",\"inc\":\"(fn (x) (+ 1 x))\",\"dec\":\"(fn (x) (- x 1))\",\"range-from-to\":\"(fn (fr to) (if (< to fr) () (cons fr (range-from-to (inc fr) to))))\",\"range\":\"(fn (x) (range-from-to 0 (dec x)))\",\"fib\":\"(fn (n) (if (< n 2) 1 (+ (fib (- n 1)) (fib (- n 2)))))\",\"sequence\":\"(fn (f s n) (let ((worker (fn (f1 s1 n1) (if (< 0 n1) (let ((new (f1 s1))) (cons new (worker f1 new (dec n1)))) ())))) (cons s (worker f s n))))\",\"iterate\":\"(fn (f s n) (if (< 0 n) (iterate f (f s) (dec n)) s))\",\"otherwise\":\"#t\",\"arr-zeros\":\"(fn (c) (arr-init-with 0 c))\"}}", Configuration.class));
    private Configuration app;
    private Configuration user;

    public LucidConfiguration(Map<String, Object> app2, Map<String, Object> user2) {
        this(new Configuration(app2), new Configuration(user2));
    }

    public LucidConfiguration(Configuration app2, Configuration user2) {
        Map<String, Object> conf;
        this.app = app2;
        if (!(user2 == null || (conf = user2.getConfiguration()) == null || !conf.containsKey("AccountManager"))) {
            conf.put("account-manager", conf.get("AccountManager"));
            conf.remove("AccountManager");
        }
        this.user = user2;
    }

    public static void saveAppConfig(Context context, Configuration configuration) {
        getSharedPreferences(context).edit().putString("AppConfig", new Gson().toJson((Object) configuration)).putInt("AppConfigVersion", configuration.getVersion()).commit();
    }

    public static int getAppConfigVersion(Context context) {
        return getSharedPreferences(context).getInt("AppConfigVersion", 0);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(LUCID_CONFIG_FILE, 4);
    }

    public static void saveUserConfig(Context context, Configuration configuration, boolean updateTime) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit().putString("UserConfig", new Gson().toJson((Object) configuration));
        if (configuration != null && configuration.getVersion() > 0) {
            editor.putInt(UserConfig.CONFIG_VER, configuration.getVersion());
        }
        if (updateTime) {
            editor.putInt("UserConfigLastUpdate", (int) (new Date().getTime() / 1000));
        }
        editor.commit();
    }

    public static void removeUserConfig(Context context) {
        getSharedPreferences(context).edit().remove("UserConfig").remove(UserConfig.CONFIG_VER).remove("UserConfigLastUpdate").commit();
    }

    public static int getUserConfigurationVersion(Context context) {
        return getSharedPreferences(context).getInt(UserConfig.CONFIG_VER, 0);
    }

    public static int getUserConfigurationLastUpdate(Context context) {
        return getSharedPreferences(context).getInt("UserConfigLastUpdate", 0);
    }

    public static LucidConfiguration readConfig(Context context) {
        Configuration appConfiguration;
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        if (sharedPreferences.getString("AppConfig", (String) null) == null) {
            appConfiguration = readDefaults().getApp();
        } else {
            appConfiguration = (Configuration) new Gson().fromJson(sharedPreferences.getString("AppConfig", ""), Configuration.class);
        }
        Configuration userConfiguration = (Configuration) new Gson().fromJson(sharedPreferences.getString("UserConfig", ""), Configuration.class);
        if (userConfiguration != null && userConfiguration.getVersion() == 0) {
            userConfiguration = new Configuration(userConfiguration.getConfiguration(), sharedPreferences.getInt(UserConfig.CONFIG_VER, 0));
        }
        return new LucidConfiguration(appConfiguration, userConfiguration);
    }

    private static LucidConfiguration readDefaults() {
        return new LucidConfiguration((Configuration) new Gson().fromJson("{\"version\":1,\"configuration\":{\"stats-manager\":{},\n\"analytics-manager\":{\n\"bottomize\":\"(fn (signal) (arr-subtract signal (arr-min signal)))\",\n\"acc-modifier\":\"(fn (sleep-alist) (let ((bfun (fn (sig) (bottomize (arr-normalize (arr-rolling-mean sig 4) 50))))) (arr-mult (bfun (get :accel-max sleep-alist)) (bfun (get :accel-events sleep-alist)))))\",\n\"clean-with-beta\":\"(fn (signal beta-lower modifier) (arr-subtract signal (arr-mult beta-lower modifier)))\",\n\"get-clean-spindles\":\"(fn (sleep-alist) (arr-clip (clean-with-beta (get :spindles sleep-alist) (get :beta-lower sleep-alist) 0.5) 0 50000))\",\n\"get-clean-spindles-std\":\"(fn (sleep-alist) (arr-clip (clean-with-beta (get :spindles-std sleep-alist) (get :beta-lower sleep-alist) 0.3334) 0 80000))\",\n\"get-clean-delta-higher\":\"(fn (sleep-alist) (arr-clip (clean-with-beta (get :delta-higher sleep-alist) (get :beta-lower sleep-alist) 0.3) 0 80000))\",\n\"get-arousals\":\"(fn (sleep-alist pow_t accv_t acce_t) (let ((s (fn (x) (get x sleep-alist)))) (staging-arousal (s :power) pow_t (s :accel-max) accv_t (s :accel-events) acce_t))) \",\n\"get-asleep-moment\":\"(fn (sleep-alist) (let ((spins (get-clean-spindles-std sleep-alist)) (arousals (get-arousals sleep-alist 6e6 800 20)) (moff (get-mask-off (get :power sleep-alist)))) (let ((asleep (staging-asleep-moment spins 4 130 arousals 7 moff))) (let ((lighter? (>= asleep (arr-len spins)))) (if lighter? (lesser 200 (staging-asleep-moment spins 4 95 arousals 6 moff)) asleep)))))\",\n\"get-lows-smooth\":\"(fn (sleep-alist) (arr-rolling-min (get-lows sleep-alist) 3)) \",\n\"get-lows\":\"(fn (sleep-alist) (let ((s (fn (x) (get x sleep-alist)))) (arr-subtract (arr-add (s :delta-higher) (arr-add (s :delta-lower) (s :theta))) (s :beta-lower))))\",\n\"compute-deep-discriminant-treshold\":\" (fn (n psc tsum) (let ((last-sleep (if (< psc n) (call :stats-manager :get-nth-sleep (list psc)) '()))) (if last-sleep (compute-deep-discriminant-treshold n (set-global! deep-treshold-processed-sleeps-count (+ psc 1)) (set-global! deep-treshold-sum (+ tsum (deep-discriminant-treshold-for-sleep last-sleep deep-sleep-quantile)))) (if (= n 0) 0 (/ deep-treshold-sum deep-treshold-processed-sleeps-count))))) \",\n\"deep-sleep-quantile\":\" 0.90 \",\n\"deep-discriminant-treshold-for-sleep\":\" (fn (sleep-alist quant) (let ((lows (get-lows-smooth sleep-alist)) (bsq (better-sleep-quality (get :power sleep-alist) (get-arousals sleep-alist 0 800 20) 1 1))) (arr-quantile (arr-treshold-filter lows bsq 20 :lt) quant))) \",\n\"get-deep-discriminant-treshold\":\" (fn (sleep-alist current-sleep-weight) (do (if (not (symbol-bound? 'deep-treshold-processed-sleeps-count)) (set-global! deep-treshold-processed-sleeps-count 0) ()) (if (not (symbol-bound? 'deep-treshold-sum)) (set-global! deep-treshold-sum 0.0) ()) (let ((sleep-count (call :stats-manager :get-sleeps-count '()))) (let ((this-sleep-treshold (deep-discriminant-treshold-for-sleep sleep-alist deep-sleep-quantile)) (other-sleeps-treshold (compute-deep-discriminant-treshold sleep-count deep-treshold-processed-sleeps-count deep-treshold-sum))) (if (= 0 other-sleeps-treshold) this-sleep-treshold (+ (* current-sleep-weight this-sleep-treshold) (* (- 1.0 current-sleep-weight) other-sleeps-treshold)))))))\",\n\"get-deep-discriminant\":\"(fn (sleep-alist) (staging-get-deep-discriminant (get-lows-smooth sleep-alist) (get-arousals sleep-alist 6e6 800 20) (acc-modifier sleep-alist) 20 10))\",\n\"get-is-deep\":\" (fn (sleep-alist) (let ((dd (get-deep-discriminant sleep-alist))) (arr-treshold-binary dd dd (get-deep-discriminant-treshold sleep-alist 0.55) :gt))) \",\n\"get-is-rem-smooth\":\" (fn (sleep-alist) (let ((r (get-is-rem sleep-alist))) (let ((rs (arr-rolling-median r 30))) (fix-median-smoothing rs r)))) \",\n\"get-is-deep-smooth\":\" (fn (sleep-alist) (let ((d (get-is-deep sleep-alist))) (let ((ds (arr-rolling-median d 30))) (fix-median-smoothing ds d)))) \",\n\"get-mask-off-smooth\":\" (fn (power) (arr-rolling-median (get-mask-off power) 12)) \",\n\"get-is-rem\":\" (fn (sleep-alist) (let ((dd (get-rem-discriminant sleep-alist))) (arr-treshold-binary dd dd 0.007 :lt))) \",\n\"get-rem-discriminant\":\"(fn (sleep-alist) (staging-get-rem (get-clean-spindles sleep-alist) (get-clean-spindles-std sleep-alist) (get-clean-delta-higher sleep-alist) (get-arousals sleep-alist 6e6 800 20) 0.01))\",\n\"get-hypnogram-old\":\"(fn (data) (if (and (alist? data) (not (empty? data))) (get-staging (get-asleep-moment data) (get-arousals data 1e7 3000 20) (get-mask-off (get :power data)) (get-is-rem data) (get-is-deep data)) (error \\\"argument must be non-empty alist\\\")))\",\n\"get-hypnogram\":\"(fn (data) (if (and (alist? data) (not (empty? data))) (get-staging (get-asleep-moment data) (get-arousals data 2e7 4000 40) (get-mask-off-smooth (get :power data)) (get-is-rem-smooth data) (get-is-deep-smooth data)) (error \\\"argument must be non-empty alist\\\")))\",\n\"sleep-score-length-mod\":\" (fn (sleep-length) (clip 0.4 1.0 (let ((sl (/ sleep-length 120.0))) (- (+ (+ (* (pow sl 2) -0.0323559) (* sl 0.517694)) -1.06888) (* 0.033 (abs (- 8 sl))))))) \",\n\"get-sleep-efficiency\":\"(fn (data hypno) (let ((is-awake (arr-treshold-binary hypno hypno 0 :eq)) (is-restless (get-arousals data 6e6 800 20))) (let ((is-awake-or-restless (arr-or is-awake is-restless))) (* 100 (/ (->double (arr-len hypno)) (+ (arr-len hypno) (arr-sum is-awake-or-restless))))))) \",\n\"compute-staging\":\"(fn (data) (let ((hypno (get-hypnogram data))) (list :hypnogram hypno :signal-quality (better-sleep-quality (get :power data) (get-arousals data 0 800 20) 1 1) :sleep-score (* (clip 0.0 100.0 (* 1.1764705882352942 (get-sleep-efficiency data hypno))) (sleep-score-length-mod (arr-len hypno))))))\"\n},\n\"trip-manager\":{\n\"timezone-diff-min\":3\n},\n\"mask-manager\":{\n\"alarm-min-gap\":300,\n\"alarm-max-gap\":600,\n\"nap-contact-level\":25,\n\"nap-acc-event-start-level\":18,\n\"nap-acc-event-stop-level\":25,\n\"nap-acc-event-tail-e-time\":8,\n\"nap-spindles-start-level\":105,\n\"nap-spindles-stop-level\":85,\n\"nap-spindles-tail-e-time\":5,\n\"nap-asleep-wnd-e-start\":20,\n\"nap-asleep-wnd-e-stop\":40,\n\"nap-asleep-max-wnd-timer-delay\":10,\n\"firmware-version\":\"33557248\"\n},\n\"account-manager\":{\n\"download-tips\":true,\n\"synchronize-sleeps\":true,\n\"intercom-enabled\":false\n},\n\"app-config-version\":9\n}}", Configuration.class), (Configuration) new Gson().fromJson("", Configuration.class));
    }

    public static Configuration getTestAppConfigBase(Context context, String version) {
        try {
            return (Configuration) new Gson().fromJson((Reader) new InputStreamReader(context.getAssets().open("test-configs/" + version + ".json")), Configuration.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Configuration getTestAppConfig103005000(Context context) {
        return getTestAppConfigBase(context, "103005000");
    }

    public static Configuration getTestAppConfig103001001() {
        return (Configuration) new Gson().fromJson("{   \"version\":103001001,    \"configuration\": {      \"stats-manager\": {},      \"analytics-manager\": {        \"bottomize\": \"(fn (signal)(arr-subtract signal (arr-min signal)))\",        \"acc-modifier\": \"(fn (sleep-alist)(let ((bfun (fn (sig) (bottomize (arr-normalize (arr-rolling-mean sig 4) 50)))))(arr-mult (bfun (get :accel-max sleep-alist))(bfun (get :accel-events sleep-alist)))))\",        \"clean-with-beta\": \"(fn (signal beta-lower modifier)(arr-subtract signal (arr-mult beta-lower modifier)))\",        \"get-clean-spindles\": \"(fn (sleep-alist)(arr-clip (clean-with-beta (get :spindles sleep-alist)(get :beta-lower sleep-alist)0.5)050000))\",        \"get-clean-spindles-std\": \"(fn (sleep-alist)(arr-clip (clean-with-beta (get :spindles-std sleep-alist)(get :beta-lower sleep-alist)0.3334)080000))\",        \"get-clean-delta-higher\": \"(fn (sleep-alist)(arr-clip (clean-with-beta (get :delta-higher sleep-alist)(get :beta-lower sleep-alist)0.3)080000))\",        \"get-arousals\": \"(fn (sleep-alist pow_t accv_t acce_t)(let ((s (fn (x) (get x sleep-alist))))(staging-arousal(s :power) pow_t(s :accel-max) accv_t(s :accel-events) acce_t)))\",        \"get-asleep-moment\": \"(fn (sleep-alist)(let ((spins    (get-clean-spindles-std sleep-alist))(arousals (get-arousals sleep-alist 6e6 800 20))(moff     (get-mask-off (get :power sleep-alist))))(let ((asleep (staging-asleep-moment spins 4 130 arousals 7 moff)))(let ((lighter? (>= asleep (arr-len spins))))(if lighter?(lesser 200 (staging-asleep-moment spins 4 95 arousals 6 moff))asleep)))))\",        \"get-lows-smooth\": \"(fn (sleep-alist)(arr-rolling-min (get-lows sleep-alist) 3))\",        \"get-lows\": \"(fn (sleep-alist)(let ((s (fn (x) (get x sleep-alist))))(arr-subtract (arr-add (s :delta-higher)(arr-add (s :delta-lower)(s :theta)))(s :beta-lower))))\",        \"compute-deep-discriminant-treshold\": \"(fn (n psc tsum)(let ((last-sleep (if (< psc n)(call :stats-manager :get-nth-sleep (list psc))'())))(if last-sleep(compute-deep-discriminant-tresholdn(set-global! deep-treshold-processed-sleeps-count (+ psc 1))(set-global! deep-treshold-sum (+ tsum(deep-discriminant-treshold-for-sleeplast-sleepdeep-sleep-quantile))))(if (= n 0)0(/ deep-treshold-sumdeep-treshold-processed-sleeps-count)))))\",        \"deep-sleep-quantile\": \"0.88\",        \"deep-discriminant-treshold-for-sleep\": \"(fn (sleep-alist quant)(let ((lows (get-lows-smooth sleep-alist))(bsq  (better-sleep-quality(get :power sleep-alist)(get-arousals sleep-alist 0 800 20)11)))(arr-quantile (arr-treshold-filter lows bsq 20 :lt) quant)))\",        \"get-deep-discriminant-treshold\": \"(fn (sleep-alist current-sleep-weight)(do(if (not (symbol-bound? 'deep-treshold-processed-sleeps-count))(set-global! deep-treshold-processed-sleeps-count 0)())(if (not (symbol-bound? 'deep-treshold-sum))(set-global! deep-treshold-sum 0.0)())(let ((sleep-count (call :stats-manager :get-sleeps-count '())))(let ((this-sleep-treshold (deep-discriminant-treshold-for-sleep sleep-alist deep-sleep-quantile))(other-sleeps-treshold (compute-deep-discriminant-treshold sleep-count deep-treshold-processed-sleeps-count deep-treshold-sum)))(if (= 0 other-sleeps-treshold)this-sleep-treshold(+ (* current-sleep-weight this-sleep-treshold)(* (- 1.0 current-sleep-weight) other-sleeps-treshold)))))))\"get-deep-discriminant\": \"(fn (sleep-alist)                           (staging-get-deep-discriminant                            (get-lows sleep-alist)                            (get-arousals sleep-alist 6e6 800 20)                            (acc-modifier sleep-alist)                            20                            10))\",\"get-is-deep\": \"          (fn (sleep-alist)           (let ((dd (get-deep-discriminant sleep-alist)))            (arr-treshold-binary dd dd (get-deep-discriminant-treshold sleep-alist 0.55) :gt)))          \",\"get-is-rem-smooth\": \"          (fn (sleep-alist)           (arr-rolling-median (get-is-rem sleep-alist) 30))          \",\"get-is-deep-smooth\": \"          (fn (sleep-alist)           (arr-rolling-median (get-is-deep sleep-alist) 30))          \",\"get-mask-off-smooth\": \"          (fn (power)           (arr-rolling-median (get-mask-off power) 12))          \",\"get-is-rem\": \"          (fn (sleep-alist)           (let ((dd (get-rem-discriminant sleep-alist)))            (arr-treshold-binary dd dd 0.007 :lt)))          \",\"get-rem-discriminant\": \"(fn (sleep-alist)                          (staging-get-rem (get-clean-spindles sleep-alist)                           (get-clean-spindles-std sleep-alist)                           (get-clean-delta-higher sleep-alist)                           (get-arousals sleep-alist 6e6 800 20)                           0.01))\",\"get-hypnogram-old\": \"(fn (data)                       (if (and (alist? data) (not (empty? data)))                           (get-staging  (get-asleep-moment data)                            (get-arousals data 1e7 3000 20)                            (get-mask-off (get :power data))                            (get-is-rem data)                            (get-is-deep data))                        (error \\\"argument must be non-empty alist\\\")))\",\"get-hypnogram\": \"(fn (data)                   (if (and (alist? data) (not (empty? data)))                       (get-staging  (get-asleep-moment data)                        (get-arousals data 2e7 4000 40)                        (get-mask-off-smooth (get :power data))                        (get-is-rem-smooth data)                        (get-is-deep-smooth data))                    (error \\\"argument must be non-empty alist\\\")))\",\"compute-staging\": \"(fn (data)                     (list :hypnogram      (get-hypnogram data)                      :signal-quality (better-sleep-quality (get :power data)                                       (get-arousals data 0 800 20)                                       1                                       1)))\"      },      \"trip-manager\": {        \"timezone-diff-min\": 3      },      \"mask-manager\": {        \"alarm-min-gap\": 300,        \"alarm-max-gap\": 600,        \"nap-contact-level\": 25,        \"nap-acc-event-start-level\": 18,        \"nap-acc-event-stop-level\": 25,        \"nap-acc-event-tail-e-time\": 8,        \"nap-spindles-start-level\": 105,        \"nap-spindles-stop-level\": 85,        \"nap-spindles-tail-e-time\": 5,        \"nap-asleep-wnd-e-start\": 20,        \"nap-asleep-wnd-e-stop\": 40,        \"nap-asleep-max-wnd-timer-delay\": 10,        \"firmware-version\": \"33557248\"      },      \"account-manager\": {},      \"app-config-version\": 0    },    \"User\": {      \"trip-manager\": {},      \"analytics-manager\": {},      \"mask-manager\": {        \"artificial-dawn-active\": \"#t\"      },      \"account-manager\": {}    }  }]", Configuration.class);
    }

    public Configuration getRsl() {
        return RSL;
    }

    public Map<String, Object> getRslMap() {
        return RSL.getConfiguration();
    }

    public Configuration getApp() {
        return this.app;
    }

    public Map<String, Object> getAppMap() {
        if (this.app != null) {
            return this.app.getConfiguration();
        }
        return null;
    }

    public Configuration getUser() {
        return this.user;
    }

    public Map<String, Object> getUserMap() {
        if (this.user != null) {
            return this.user.getConfiguration();
        }
        return null;
    }

    public void save(Context context) {
        saveUserConfig(context, getUser(), false);
    }
}
