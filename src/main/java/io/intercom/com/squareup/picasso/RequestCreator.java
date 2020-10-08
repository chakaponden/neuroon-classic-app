package io.intercom.com.squareup.picasso;

import android.app.Notification;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.RemoteViews;
import io.intercom.com.squareup.picasso.Picasso;
import io.intercom.com.squareup.picasso.RemoteViewsAction;
import io.intercom.com.squareup.picasso.Request;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RequestCreator {
    private static final AtomicInteger nextId = new AtomicInteger();
    private final Request.Builder data;
    private boolean deferred;
    private Drawable errorDrawable;
    private int errorResId;
    private int memoryPolicy;
    private int networkPolicy;
    private boolean noFade;
    private final Picasso picasso;
    private Drawable placeholderDrawable;
    private int placeholderResId;
    private boolean setPlaceholder;
    private Object tag;

    RequestCreator(Picasso picasso2, Uri uri, int resourceId) {
        this.setPlaceholder = true;
        if (picasso2.shutdown) {
            throw new IllegalStateException("Picasso instance already shut down. Cannot submit new requests.");
        }
        this.picasso = picasso2;
        this.data = new Request.Builder(uri, resourceId, picasso2.defaultBitmapConfig);
    }

    RequestCreator() {
        this.setPlaceholder = true;
        this.picasso = null;
        this.data = new Request.Builder((Uri) null, 0, (Bitmap.Config) null);
    }

    public RequestCreator noPlaceholder() {
        if (this.placeholderResId != 0) {
            throw new IllegalStateException("Placeholder resource already set.");
        } else if (this.placeholderDrawable != null) {
            throw new IllegalStateException("Placeholder image already set.");
        } else {
            this.setPlaceholder = false;
            return this;
        }
    }

    public RequestCreator placeholder(int placeholderResId2) {
        if (!this.setPlaceholder) {
            throw new IllegalStateException("Already explicitly declared as no placeholder.");
        } else if (placeholderResId2 == 0) {
            throw new IllegalArgumentException("Placeholder image resource invalid.");
        } else if (this.placeholderDrawable != null) {
            throw new IllegalStateException("Placeholder image already set.");
        } else {
            this.placeholderResId = placeholderResId2;
            return this;
        }
    }

    public RequestCreator placeholder(Drawable placeholderDrawable2) {
        if (!this.setPlaceholder) {
            throw new IllegalStateException("Already explicitly declared as no placeholder.");
        } else if (this.placeholderResId != 0) {
            throw new IllegalStateException("Placeholder image already set.");
        } else {
            this.placeholderDrawable = placeholderDrawable2;
            return this;
        }
    }

    public RequestCreator error(int errorResId2) {
        if (errorResId2 == 0) {
            throw new IllegalArgumentException("Error image resource invalid.");
        } else if (this.errorDrawable != null) {
            throw new IllegalStateException("Error image already set.");
        } else {
            this.errorResId = errorResId2;
            return this;
        }
    }

    public RequestCreator error(Drawable errorDrawable2) {
        if (errorDrawable2 == null) {
            throw new IllegalArgumentException("Error image may not be null.");
        } else if (this.errorResId != 0) {
            throw new IllegalStateException("Error image already set.");
        } else {
            this.errorDrawable = errorDrawable2;
            return this;
        }
    }

    public RequestCreator tag(Object tag2) {
        if (tag2 == null) {
            throw new IllegalArgumentException("Tag invalid.");
        } else if (this.tag != null) {
            throw new IllegalStateException("Tag already set.");
        } else {
            this.tag = tag2;
            return this;
        }
    }

    public RequestCreator fit() {
        this.deferred = true;
        return this;
    }

    /* access modifiers changed from: package-private */
    public RequestCreator unfit() {
        this.deferred = false;
        return this;
    }

    public RequestCreator resizeDimen(int targetWidthResId, int targetHeightResId) {
        Resources resources = this.picasso.context.getResources();
        return resize(resources.getDimensionPixelSize(targetWidthResId), resources.getDimensionPixelSize(targetHeightResId));
    }

    public RequestCreator resize(int targetWidth, int targetHeight) {
        this.data.resize(targetWidth, targetHeight);
        return this;
    }

    public RequestCreator centerCrop() {
        this.data.centerCrop();
        return this;
    }

    public RequestCreator centerInside() {
        this.data.centerInside();
        return this;
    }

    public RequestCreator onlyScaleDown() {
        this.data.onlyScaleDown();
        return this;
    }

    public RequestCreator rotate(float degrees) {
        this.data.rotate(degrees);
        return this;
    }

    public RequestCreator rotate(float degrees, float pivotX, float pivotY) {
        this.data.rotate(degrees, pivotX, pivotY);
        return this;
    }

    public RequestCreator config(Bitmap.Config config) {
        this.data.config(config);
        return this;
    }

    public RequestCreator stableKey(String stableKey) {
        this.data.stableKey(stableKey);
        return this;
    }

    public RequestCreator priority(Picasso.Priority priority) {
        this.data.priority(priority);
        return this;
    }

    public RequestCreator transform(Transformation transformation) {
        this.data.transform(transformation);
        return this;
    }

    public RequestCreator transform(List<? extends Transformation> transformations) {
        this.data.transform(transformations);
        return this;
    }

    @Deprecated
    public RequestCreator skipMemoryCache() {
        return memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE);
    }

    public RequestCreator memoryPolicy(MemoryPolicy policy, MemoryPolicy... additional) {
        if (policy == null) {
            throw new IllegalArgumentException("Memory policy cannot be null.");
        }
        this.memoryPolicy |= policy.index;
        if (additional == null) {
            throw new IllegalArgumentException("Memory policy cannot be null.");
        }
        if (additional.length > 0) {
            for (MemoryPolicy memoryPolicy2 : additional) {
                if (memoryPolicy2 == null) {
                    throw new IllegalArgumentException("Memory policy cannot be null.");
                }
                this.memoryPolicy |= memoryPolicy2.index;
            }
        }
        return this;
    }

    public RequestCreator networkPolicy(NetworkPolicy policy, NetworkPolicy... additional) {
        if (policy == null) {
            throw new IllegalArgumentException("Network policy cannot be null.");
        }
        this.networkPolicy |= policy.index;
        if (additional == null) {
            throw new IllegalArgumentException("Network policy cannot be null.");
        }
        if (additional.length > 0) {
            for (NetworkPolicy networkPolicy2 : additional) {
                if (networkPolicy2 == null) {
                    throw new IllegalArgumentException("Network policy cannot be null.");
                }
                this.networkPolicy |= networkPolicy2.index;
            }
        }
        return this;
    }

    public RequestCreator noFade() {
        this.noFade = true;
        return this;
    }

    public Bitmap get() throws IOException {
        long started = System.nanoTime();
        Utils.checkNotMain();
        if (this.deferred) {
            throw new IllegalStateException("Fit cannot be used with get.");
        } else if (!this.data.hasImage()) {
            return null;
        } else {
            Request finalData = createRequest(started);
            return BitmapHunter.forRequest(this.picasso, this.picasso.dispatcher, this.picasso.cache, this.picasso.stats, new GetAction(this.picasso, finalData, this.memoryPolicy, this.networkPolicy, this.tag, Utils.createKey(finalData, new StringBuilder()))).hunt();
        }
    }

    public void fetch() {
        fetch((Callback) null);
    }

    public void fetch(Callback callback) {
        long started = System.nanoTime();
        if (this.deferred) {
            throw new IllegalStateException("Fit cannot be used with fetch.");
        } else if (this.data.hasImage()) {
            if (!this.data.hasPriority()) {
                this.data.priority(Picasso.Priority.LOW);
            }
            Request request = createRequest(started);
            String key = Utils.createKey(request, new StringBuilder());
            if (this.picasso.quickMemoryCacheCheck(key) != null) {
                if (this.picasso.loggingEnabled) {
                    Utils.log("Main", "completed", request.plainId(), "from " + Picasso.LoadedFrom.MEMORY);
                }
                if (callback != null) {
                    callback.onSuccess();
                    return;
                }
                return;
            }
            this.picasso.submit(new FetchAction(this.picasso, request, this.memoryPolicy, this.networkPolicy, this.tag, key, callback));
        }
    }

    public void into(Target target) {
        Bitmap bitmap;
        Drawable drawable = null;
        long started = System.nanoTime();
        Utils.checkMain();
        if (target == null) {
            throw new IllegalArgumentException("Target must not be null.");
        } else if (this.deferred) {
            throw new IllegalStateException("Fit cannot be used with a Target.");
        } else if (!this.data.hasImage()) {
            this.picasso.cancelRequest(target);
            if (this.setPlaceholder) {
                drawable = getPlaceholderDrawable();
            }
            target.onPrepareLoad(drawable);
        } else {
            Request request = createRequest(started);
            String requestKey = Utils.createKey(request);
            if (!MemoryPolicy.shouldReadFromMemoryCache(this.memoryPolicy) || (bitmap = this.picasso.quickMemoryCacheCheck(requestKey)) == null) {
                if (this.setPlaceholder) {
                    drawable = getPlaceholderDrawable();
                }
                target.onPrepareLoad(drawable);
                this.picasso.enqueueAndSubmit(new TargetAction(this.picasso, target, request, this.memoryPolicy, this.networkPolicy, this.errorDrawable, requestKey, this.tag, this.errorResId));
                return;
            }
            this.picasso.cancelRequest(target);
            target.onBitmapLoaded(bitmap, Picasso.LoadedFrom.MEMORY);
        }
    }

    public void into(RemoteViews remoteViews, int viewId, int notificationId, Notification notification) {
        long started = System.nanoTime();
        if (remoteViews == null) {
            throw new IllegalArgumentException("RemoteViews must not be null.");
        } else if (notification == null) {
            throw new IllegalArgumentException("Notification must not be null.");
        } else if (this.deferred) {
            throw new IllegalStateException("Fit cannot be used with RemoteViews.");
        } else if (this.placeholderDrawable == null && this.placeholderResId == 0 && this.errorDrawable == null) {
            Request request = createRequest(started);
            RemoteViews remoteViews2 = remoteViews;
            int i = viewId;
            int i2 = notificationId;
            Notification notification2 = notification;
            performRemoteViewInto(new RemoteViewsAction.NotificationAction(this.picasso, request, remoteViews2, i, i2, notification2, this.memoryPolicy, this.networkPolicy, Utils.createKey(request, new StringBuilder()), this.tag, this.errorResId));
        } else {
            throw new IllegalArgumentException("Cannot use placeholder or error drawables with remote views.");
        }
    }

    public void into(RemoteViews remoteViews, int viewId, int[] appWidgetIds) {
        long started = System.nanoTime();
        if (remoteViews == null) {
            throw new IllegalArgumentException("remoteViews must not be null.");
        } else if (appWidgetIds == null) {
            throw new IllegalArgumentException("appWidgetIds must not be null.");
        } else if (this.deferred) {
            throw new IllegalStateException("Fit cannot be used with remote views.");
        } else if (this.placeholderDrawable == null && this.placeholderResId == 0 && this.errorDrawable == null) {
            Request request = createRequest(started);
            RemoteViews remoteViews2 = remoteViews;
            int i = viewId;
            int[] iArr = appWidgetIds;
            performRemoteViewInto(new RemoteViewsAction.AppWidgetAction(this.picasso, request, remoteViews2, i, iArr, this.memoryPolicy, this.networkPolicy, Utils.createKey(request, new StringBuilder()), this.tag, this.errorResId));
        } else {
            throw new IllegalArgumentException("Cannot use placeholder or error drawables with remote views.");
        }
    }

    public void into(ImageView target) {
        into(target, (Callback) null);
    }

    public void into(ImageView target, Callback callback) {
        Bitmap bitmap;
        long started = System.nanoTime();
        Utils.checkMain();
        if (target == null) {
            throw new IllegalArgumentException("Target must not be null.");
        } else if (!this.data.hasImage()) {
            this.picasso.cancelRequest(target);
            if (this.setPlaceholder) {
                PicassoDrawable.setPlaceholder(target, getPlaceholderDrawable());
            }
        } else {
            if (this.deferred) {
                if (this.data.hasSize()) {
                    throw new IllegalStateException("Fit cannot be used with resize.");
                }
                int width = target.getWidth();
                int height = target.getHeight();
                if (width == 0 || height == 0) {
                    if (this.setPlaceholder) {
                        PicassoDrawable.setPlaceholder(target, getPlaceholderDrawable());
                    }
                    this.picasso.defer(target, new DeferredRequestCreator(this, target, callback));
                    return;
                }
                this.data.resize(width, height);
            }
            Request request = createRequest(started);
            String requestKey = Utils.createKey(request);
            if (!MemoryPolicy.shouldReadFromMemoryCache(this.memoryPolicy) || (bitmap = this.picasso.quickMemoryCacheCheck(requestKey)) == null) {
                if (this.setPlaceholder) {
                    PicassoDrawable.setPlaceholder(target, getPlaceholderDrawable());
                }
                this.picasso.enqueueAndSubmit(new ImageViewAction(this.picasso, target, request, this.memoryPolicy, this.networkPolicy, this.errorResId, this.errorDrawable, requestKey, this.tag, callback, this.noFade));
                return;
            }
            this.picasso.cancelRequest(target);
            PicassoDrawable.setBitmap(target, this.picasso.context, bitmap, Picasso.LoadedFrom.MEMORY, this.noFade, this.picasso.indicatorsEnabled);
            if (this.picasso.loggingEnabled) {
                Utils.log("Main", "completed", request.plainId(), "from " + Picasso.LoadedFrom.MEMORY);
            }
            if (callback != null) {
                callback.onSuccess();
            }
        }
    }

    private Drawable getPlaceholderDrawable() {
        if (this.placeholderResId != 0) {
            return this.picasso.context.getResources().getDrawable(this.placeholderResId);
        }
        return this.placeholderDrawable;
    }

    private Request createRequest(long started) {
        int id = nextId.getAndIncrement();
        Request request = this.data.build();
        request.id = id;
        request.started = started;
        boolean loggingEnabled = this.picasso.loggingEnabled;
        if (loggingEnabled) {
            Utils.log("Main", "created", request.plainId(), request.toString());
        }
        Request transformed = this.picasso.transformRequest(request);
        if (transformed != request) {
            transformed.id = id;
            transformed.started = started;
            if (loggingEnabled) {
                Utils.log("Main", "changed", transformed.logId(), "into " + transformed);
            }
        }
        return transformed;
    }

    private void performRemoteViewInto(RemoteViewsAction action) {
        Bitmap bitmap;
        if (!MemoryPolicy.shouldReadFromMemoryCache(this.memoryPolicy) || (bitmap = this.picasso.quickMemoryCacheCheck(action.getKey())) == null) {
            if (this.placeholderResId != 0) {
                action.setImageResource(this.placeholderResId);
            }
            this.picasso.enqueueAndSubmit(action);
            return;
        }
        action.complete(bitmap, Picasso.LoadedFrom.MEMORY);
    }
}
