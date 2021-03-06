package io.intercom.retrofit;

import android.os.Build;
import android.os.Process;
import io.intercom.com.google.gson.Gson;
import io.intercom.retrofit.RestAdapter;
import io.intercom.retrofit.Utils;
import io.intercom.retrofit.android.AndroidApacheClient;
import io.intercom.retrofit.android.AndroidLog;
import io.intercom.retrofit.android.MainThreadExecutor;
import io.intercom.retrofit.appengine.UrlFetchClient;
import io.intercom.retrofit.client.Client;
import io.intercom.retrofit.client.OkClient;
import io.intercom.retrofit.client.UrlConnectionClient;
import io.intercom.retrofit.converter.Converter;
import io.intercom.retrofit.converter.GsonConverter;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

abstract class Platform {
    static final boolean HAS_RX_JAVA = hasRxJavaOnClasspath();
    private static final Platform PLATFORM = findPlatform();

    /* access modifiers changed from: package-private */
    public abstract Executor defaultCallbackExecutor();

    /* access modifiers changed from: package-private */
    public abstract Client.Provider defaultClient();

    /* access modifiers changed from: package-private */
    public abstract Converter defaultConverter();

    /* access modifiers changed from: package-private */
    public abstract Executor defaultHttpExecutor();

    /* access modifiers changed from: package-private */
    public abstract RestAdapter.Log defaultLog();

    Platform() {
    }

    static Platform get() {
        return PLATFORM;
    }

    private static Platform findPlatform() {
        try {
            Class.forName("android.os.Build");
            if (Build.VERSION.SDK_INT != 0) {
                return new Android();
            }
        } catch (ClassNotFoundException e) {
        }
        if (System.getProperty("com.google.appengine.runtime.version") != null) {
            return new AppEngine();
        }
        return new Base();
    }

    private static class Base extends Platform {
        private Base() {
        }

        /* access modifiers changed from: package-private */
        public Converter defaultConverter() {
            return new GsonConverter(new Gson());
        }

        /* access modifiers changed from: package-private */
        public Client.Provider defaultClient() {
            final Client client;
            if (Platform.hasOkHttpOnClasspath()) {
                client = OkClientInstantiator.instantiate();
            } else {
                client = new UrlConnectionClient();
            }
            return new Client.Provider() {
                public Client get() {
                    return client;
                }
            };
        }

        /* access modifiers changed from: package-private */
        public Executor defaultHttpExecutor() {
            return Executors.newCachedThreadPool(new ThreadFactory() {
                public Thread newThread(final Runnable r) {
                    return new Thread(new Runnable() {
                        public void run() {
                            Thread.currentThread().setPriority(1);
                            r.run();
                        }
                    }, "Retrofit-Idle");
                }
            });
        }

        /* access modifiers changed from: package-private */
        public Executor defaultCallbackExecutor() {
            return new Utils.SynchronousExecutor();
        }

        /* access modifiers changed from: package-private */
        public RestAdapter.Log defaultLog() {
            return new RestAdapter.Log() {
                public void log(String message) {
                    System.out.println(message);
                }
            };
        }
    }

    private static class Android extends Platform {
        private Android() {
        }

        /* access modifiers changed from: package-private */
        public Converter defaultConverter() {
            return new GsonConverter(new Gson());
        }

        /* access modifiers changed from: package-private */
        public Client.Provider defaultClient() {
            final Client client;
            if (Platform.hasOkHttpOnClasspath()) {
                client = OkClientInstantiator.instantiate();
            } else if (Build.VERSION.SDK_INT < 9) {
                client = new AndroidApacheClient();
            } else {
                client = new UrlConnectionClient();
            }
            return new Client.Provider() {
                public Client get() {
                    return client;
                }
            };
        }

        /* access modifiers changed from: package-private */
        public Executor defaultHttpExecutor() {
            return Executors.newCachedThreadPool(new ThreadFactory() {
                public Thread newThread(final Runnable r) {
                    return new Thread(new Runnable() {
                        public void run() {
                            Process.setThreadPriority(10);
                            r.run();
                        }
                    }, "Retrofit-Idle");
                }
            });
        }

        /* access modifiers changed from: package-private */
        public Executor defaultCallbackExecutor() {
            return new MainThreadExecutor();
        }

        /* access modifiers changed from: package-private */
        public RestAdapter.Log defaultLog() {
            return new AndroidLog("Retrofit");
        }
    }

    private static class AppEngine extends Base {
        private AppEngine() {
            super();
        }

        /* access modifiers changed from: package-private */
        public Client.Provider defaultClient() {
            final UrlFetchClient client = new UrlFetchClient();
            return new Client.Provider() {
                public Client get() {
                    return client;
                }
            };
        }
    }

    /* access modifiers changed from: private */
    public static boolean hasOkHttpOnClasspath() {
        try {
            Class.forName("io.intercom.com.squareup.okhttp.OkHttpClient");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    private static class OkClientInstantiator {
        private OkClientInstantiator() {
        }

        static Client instantiate() {
            return new OkClient();
        }
    }

    private static boolean hasRxJavaOnClasspath() {
        try {
            Class.forName("rx.Observable");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
