package io.intercom.com.squareup.picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.v4.internal.view.SupportMenu;
import android.widget.ImageView;
import android.widget.RemoteViews;
import io.intercom.com.squareup.picasso.Action;
import io.intercom.com.squareup.picasso.RemoteViewsAction;
import java.io.File;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;

public class Picasso {
    static final Handler HANDLER = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 3:
                    Action action = (Action) msg.obj;
                    if (action.getPicasso().loggingEnabled) {
                        Utils.log("Main", "canceled", action.request.logId(), "target got garbage collected");
                    }
                    action.picasso.cancelExistingRequest(action.getTarget());
                    return;
                case 8:
                    List<BitmapHunter> batch = (List) msg.obj;
                    int n = batch.size();
                    for (int i = 0; i < n; i++) {
                        BitmapHunter hunter = batch.get(i);
                        hunter.picasso.complete(hunter);
                    }
                    return;
                case 13:
                    List<Action> batch2 = (List) msg.obj;
                    int n2 = batch2.size();
                    for (int i2 = 0; i2 < n2; i2++) {
                        Action action2 = batch2.get(i2);
                        action2.picasso.resumeAction(action2);
                    }
                    return;
                default:
                    throw new AssertionError("Unknown handler message received: " + msg.what);
            }
        }
    };
    static final String TAG = "Picasso";
    static volatile Picasso singleton = null;
    final Cache cache;
    private final CleanupThread cleanupThread;
    final Context context;
    final Bitmap.Config defaultBitmapConfig;
    final Dispatcher dispatcher;
    boolean indicatorsEnabled;
    private final Listener listener;
    volatile boolean loggingEnabled;
    final ReferenceQueue<Object> referenceQueue;
    private final List<RequestHandler> requestHandlers;
    private final RequestTransformer requestTransformer;
    boolean shutdown;
    final Stats stats;
    final Map<Object, Action> targetToAction;
    final Map<ImageView, DeferredRequestCreator> targetToDeferredRequestCreator;

    public interface Listener {
        void onImageLoadFailed(Picasso picasso, Uri uri, Exception exc);
    }

    public enum Priority {
        LOW,
        NORMAL,
        HIGH
    }

    public interface RequestTransformer {
        public static final RequestTransformer IDENTITY = new RequestTransformer() {
            public Request transformRequest(Request request) {
                return request;
            }
        };

        Request transformRequest(Request request);
    }

    Picasso(Context context2, Dispatcher dispatcher2, Cache cache2, Listener listener2, RequestTransformer requestTransformer2, List<RequestHandler> extraRequestHandlers, Stats stats2, Bitmap.Config defaultBitmapConfig2, boolean indicatorsEnabled2, boolean loggingEnabled2) {
        this.context = context2;
        this.dispatcher = dispatcher2;
        this.cache = cache2;
        this.listener = listener2;
        this.requestTransformer = requestTransformer2;
        this.defaultBitmapConfig = defaultBitmapConfig2;
        List<RequestHandler> allRequestHandlers = new ArrayList<>(7 + (extraRequestHandlers != null ? extraRequestHandlers.size() : 0));
        allRequestHandlers.add(new ResourceRequestHandler(context2));
        if (extraRequestHandlers != null) {
            allRequestHandlers.addAll(extraRequestHandlers);
        }
        allRequestHandlers.add(new ContactsPhotoRequestHandler(context2));
        allRequestHandlers.add(new MediaStoreRequestHandler(context2));
        allRequestHandlers.add(new ContentStreamRequestHandler(context2));
        allRequestHandlers.add(new AssetRequestHandler(context2));
        allRequestHandlers.add(new FileRequestHandler(context2));
        allRequestHandlers.add(new NetworkRequestHandler(dispatcher2.downloader, stats2));
        this.requestHandlers = Collections.unmodifiableList(allRequestHandlers);
        this.stats = stats2;
        this.targetToAction = new WeakHashMap();
        this.targetToDeferredRequestCreator = new WeakHashMap();
        this.indicatorsEnabled = indicatorsEnabled2;
        this.loggingEnabled = loggingEnabled2;
        this.referenceQueue = new ReferenceQueue<>();
        this.cleanupThread = new CleanupThread(this.referenceQueue, HANDLER);
        this.cleanupThread.start();
    }

    public void cancelRequest(ImageView view) {
        cancelExistingRequest(view);
    }

    public void cancelRequest(Target target) {
        cancelExistingRequest(target);
    }

    public void cancelRequest(RemoteViews remoteViews, int viewId) {
        cancelExistingRequest(new RemoteViewsAction.RemoteViewsTarget(remoteViews, viewId));
    }

    public void cancelTag(Object tag) {
        Utils.checkMain();
        List<Action> actions = new ArrayList<>(this.targetToAction.values());
        int n = actions.size();
        for (int i = 0; i < n; i++) {
            Action action = actions.get(i);
            if (action.getTag().equals(tag)) {
                cancelExistingRequest(action.getTarget());
            }
        }
    }

    public void pauseTag(Object tag) {
        this.dispatcher.dispatchPauseTag(tag);
    }

    public void resumeTag(Object tag) {
        this.dispatcher.dispatchResumeTag(tag);
    }

    public RequestCreator load(Uri uri) {
        return new RequestCreator(this, uri, 0);
    }

    public RequestCreator load(String path) {
        if (path == null) {
            return new RequestCreator(this, (Uri) null, 0);
        }
        if (path.trim().length() != 0) {
            return load(Uri.parse(path));
        }
        throw new IllegalArgumentException("Path must not be empty.");
    }

    public RequestCreator load(File file) {
        if (file == null) {
            return new RequestCreator(this, (Uri) null, 0);
        }
        return load(Uri.fromFile(file));
    }

    public RequestCreator load(int resourceId) {
        if (resourceId != 0) {
            return new RequestCreator(this, (Uri) null, resourceId);
        }
        throw new IllegalArgumentException("Resource ID must not be zero.");
    }

    public void invalidate(Uri uri) {
        if (uri == null) {
            throw new IllegalArgumentException("uri == null");
        }
        this.cache.clearKeyUri(uri.toString());
    }

    public void invalidate(String path) {
        if (path == null) {
            throw new IllegalArgumentException("path == null");
        }
        invalidate(Uri.parse(path));
    }

    public void invalidate(File file) {
        if (file == null) {
            throw new IllegalArgumentException("file == null");
        }
        invalidate(Uri.fromFile(file));
    }

    @Deprecated
    public boolean isDebugging() {
        return areIndicatorsEnabled() && isLoggingEnabled();
    }

    @Deprecated
    public void setDebugging(boolean debugging) {
        setIndicatorsEnabled(debugging);
    }

    public void setIndicatorsEnabled(boolean enabled) {
        this.indicatorsEnabled = enabled;
    }

    public boolean areIndicatorsEnabled() {
        return this.indicatorsEnabled;
    }

    public void setLoggingEnabled(boolean enabled) {
        this.loggingEnabled = enabled;
    }

    public boolean isLoggingEnabled() {
        return this.loggingEnabled;
    }

    public StatsSnapshot getSnapshot() {
        return this.stats.createSnapshot();
    }

    public void shutdown() {
        if (this == singleton) {
            throw new UnsupportedOperationException("Default singleton instance cannot be shutdown.");
        } else if (!this.shutdown) {
            this.cache.clear();
            this.cleanupThread.shutdown();
            this.stats.shutdown();
            this.dispatcher.shutdown();
            for (DeferredRequestCreator deferredRequestCreator : this.targetToDeferredRequestCreator.values()) {
                deferredRequestCreator.cancel();
            }
            this.targetToDeferredRequestCreator.clear();
            this.shutdown = true;
        }
    }

    /* access modifiers changed from: package-private */
    public List<RequestHandler> getRequestHandlers() {
        return this.requestHandlers;
    }

    /* access modifiers changed from: package-private */
    public Request transformRequest(Request request) {
        Request transformed = this.requestTransformer.transformRequest(request);
        if (transformed != null) {
            return transformed;
        }
        throw new IllegalStateException("Request transformer " + this.requestTransformer.getClass().getCanonicalName() + " returned null for " + request);
    }

    /* access modifiers changed from: package-private */
    public void defer(ImageView view, DeferredRequestCreator request) {
        this.targetToDeferredRequestCreator.put(view, request);
    }

    /* access modifiers changed from: package-private */
    public void enqueueAndSubmit(Action action) {
        Object target = action.getTarget();
        if (!(target == null || this.targetToAction.get(target) == action)) {
            cancelExistingRequest(target);
            this.targetToAction.put(target, action);
        }
        submit(action);
    }

    /* access modifiers changed from: package-private */
    public void submit(Action action) {
        this.dispatcher.dispatchSubmit(action);
    }

    /* access modifiers changed from: package-private */
    public Bitmap quickMemoryCacheCheck(String key) {
        Bitmap cached = this.cache.get(key);
        if (cached != null) {
            this.stats.dispatchCacheHit();
        } else {
            this.stats.dispatchCacheMiss();
        }
        return cached;
    }

    /* access modifiers changed from: package-private */
    public void complete(BitmapHunter hunter) {
        boolean hasMultiple;
        boolean shouldDeliver = false;
        Action single = hunter.getAction();
        List<Action> joined = hunter.getActions();
        if (joined == null || joined.isEmpty()) {
            hasMultiple = false;
        } else {
            hasMultiple = true;
        }
        if (single != null || hasMultiple) {
            shouldDeliver = true;
        }
        if (shouldDeliver) {
            Uri uri = hunter.getData().uri;
            Exception exception = hunter.getException();
            Bitmap result = hunter.getResult();
            LoadedFrom from = hunter.getLoadedFrom();
            if (single != null) {
                deliverAction(result, from, single);
            }
            if (hasMultiple) {
                int n = joined.size();
                for (int i = 0; i < n; i++) {
                    deliverAction(result, from, joined.get(i));
                }
            }
            if (this.listener != null && exception != null) {
                this.listener.onImageLoadFailed(this, uri, exception);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void resumeAction(Action action) {
        Bitmap bitmap = null;
        if (MemoryPolicy.shouldReadFromMemoryCache(action.memoryPolicy)) {
            bitmap = quickMemoryCacheCheck(action.getKey());
        }
        if (bitmap != null) {
            deliverAction(bitmap, LoadedFrom.MEMORY, action);
            if (this.loggingEnabled) {
                Utils.log("Main", "completed", action.request.logId(), "from " + LoadedFrom.MEMORY);
                return;
            }
            return;
        }
        enqueueAndSubmit(action);
        if (this.loggingEnabled) {
            Utils.log("Main", "resumed", action.request.logId());
        }
    }

    private void deliverAction(Bitmap result, LoadedFrom from, Action action) {
        if (!action.isCancelled()) {
            if (!action.willReplay()) {
                this.targetToAction.remove(action.getTarget());
            }
            if (result == null) {
                action.error();
                if (this.loggingEnabled) {
                    Utils.log("Main", "errored", action.request.logId());
                }
            } else if (from == null) {
                throw new AssertionError("LoadedFrom cannot be null.");
            } else {
                action.complete(result, from);
                if (this.loggingEnabled) {
                    Utils.log("Main", "completed", action.request.logId(), "from " + from);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void cancelExistingRequest(Object target) {
        DeferredRequestCreator deferredRequestCreator;
        Utils.checkMain();
        Action action = this.targetToAction.remove(target);
        if (action != null) {
            action.cancel();
            this.dispatcher.dispatchCancel(action);
        }
        if ((target instanceof ImageView) && (deferredRequestCreator = this.targetToDeferredRequestCreator.remove((ImageView) target)) != null) {
            deferredRequestCreator.cancel();
        }
    }

    private static class CleanupThread extends Thread {
        private final Handler handler;
        private final ReferenceQueue<Object> referenceQueue;

        CleanupThread(ReferenceQueue<Object> referenceQueue2, Handler handler2) {
            this.referenceQueue = referenceQueue2;
            this.handler = handler2;
            setDaemon(true);
            setName("Picasso-refQueue");
        }

        public void run() {
            Process.setThreadPriority(10);
            while (true) {
                try {
                    Action.RequestWeakReference<?> remove = (Action.RequestWeakReference) this.referenceQueue.remove(1000);
                    Message message = this.handler.obtainMessage();
                    if (remove != null) {
                        message.what = 3;
                        message.obj = remove.action;
                        this.handler.sendMessage(message);
                    } else {
                        message.recycle();
                    }
                } catch (InterruptedException e) {
                    return;
                } catch (Exception e2) {
                    this.handler.post(new Runnable() {
                        public void run() {
                            throw new RuntimeException(e2);
                        }
                    });
                    return;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void shutdown() {
            interrupt();
        }
    }

    public static Picasso with(Context context2) {
        if (singleton == null) {
            synchronized (Picasso.class) {
                if (singleton == null) {
                    singleton = new Builder(context2).build();
                }
            }
        }
        return singleton;
    }

    public static void setSingletonInstance(Picasso picasso) {
        synchronized (Picasso.class) {
            if (singleton != null) {
                throw new IllegalStateException("Singleton instance already exists.");
            }
            singleton = picasso;
        }
    }

    public static class Builder {
        private Cache cache;
        private final Context context;
        private Bitmap.Config defaultBitmapConfig;
        private Downloader downloader;
        private boolean indicatorsEnabled;
        private Listener listener;
        private boolean loggingEnabled;
        private List<RequestHandler> requestHandlers;
        private ExecutorService service;
        private RequestTransformer transformer;

        public Builder(Context context2) {
            if (context2 == null) {
                throw new IllegalArgumentException("Context must not be null.");
            }
            this.context = context2.getApplicationContext();
        }

        public Builder defaultBitmapConfig(Bitmap.Config bitmapConfig) {
            if (bitmapConfig == null) {
                throw new IllegalArgumentException("Bitmap config must not be null.");
            }
            this.defaultBitmapConfig = bitmapConfig;
            return this;
        }

        public Builder downloader(Downloader downloader2) {
            if (downloader2 == null) {
                throw new IllegalArgumentException("Downloader must not be null.");
            } else if (this.downloader != null) {
                throw new IllegalStateException("Downloader already set.");
            } else {
                this.downloader = downloader2;
                return this;
            }
        }

        public Builder executor(ExecutorService executorService) {
            if (executorService == null) {
                throw new IllegalArgumentException("Executor service must not be null.");
            } else if (this.service != null) {
                throw new IllegalStateException("Executor service already set.");
            } else {
                this.service = executorService;
                return this;
            }
        }

        public Builder memoryCache(Cache memoryCache) {
            if (memoryCache == null) {
                throw new IllegalArgumentException("Memory cache must not be null.");
            } else if (this.cache != null) {
                throw new IllegalStateException("Memory cache already set.");
            } else {
                this.cache = memoryCache;
                return this;
            }
        }

        public Builder listener(Listener listener2) {
            if (listener2 == null) {
                throw new IllegalArgumentException("Listener must not be null.");
            } else if (this.listener != null) {
                throw new IllegalStateException("Listener already set.");
            } else {
                this.listener = listener2;
                return this;
            }
        }

        public Builder requestTransformer(RequestTransformer transformer2) {
            if (transformer2 == null) {
                throw new IllegalArgumentException("Transformer must not be null.");
            } else if (this.transformer != null) {
                throw new IllegalStateException("Transformer already set.");
            } else {
                this.transformer = transformer2;
                return this;
            }
        }

        public Builder addRequestHandler(RequestHandler requestHandler) {
            if (requestHandler == null) {
                throw new IllegalArgumentException("RequestHandler must not be null.");
            }
            if (this.requestHandlers == null) {
                this.requestHandlers = new ArrayList();
            }
            if (this.requestHandlers.contains(requestHandler)) {
                throw new IllegalStateException("RequestHandler already registered.");
            }
            this.requestHandlers.add(requestHandler);
            return this;
        }

        @Deprecated
        public Builder debugging(boolean debugging) {
            return indicatorsEnabled(debugging);
        }

        public Builder indicatorsEnabled(boolean enabled) {
            this.indicatorsEnabled = enabled;
            return this;
        }

        public Builder loggingEnabled(boolean enabled) {
            this.loggingEnabled = enabled;
            return this;
        }

        public Picasso build() {
            Context context2 = this.context;
            if (this.downloader == null) {
                this.downloader = Utils.createDefaultDownloader(context2);
            }
            if (this.cache == null) {
                this.cache = new LruCache(context2);
            }
            if (this.service == null) {
                this.service = new PicassoExecutorService();
            }
            if (this.transformer == null) {
                this.transformer = RequestTransformer.IDENTITY;
            }
            Stats stats = new Stats(this.cache);
            return new Picasso(context2, new Dispatcher(context2, this.service, Picasso.HANDLER, this.downloader, this.cache, stats), this.cache, this.listener, this.transformer, this.requestHandlers, stats, this.defaultBitmapConfig, this.indicatorsEnabled, this.loggingEnabled);
        }
    }

    public enum LoadedFrom {
        MEMORY(-16711936),
        DISK(-16776961),
        NETWORK(SupportMenu.CATEGORY_MASK);
        
        final int debugColor;

        private LoadedFrom(int debugColor2) {
            this.debugColor = debugColor2;
        }
    }
}
