package io.intercom.com.squareup.picasso;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.NetworkInfo;
import io.intercom.com.squareup.picasso.Downloader;
import io.intercom.com.squareup.picasso.NetworkRequestHandler;
import io.intercom.com.squareup.picasso.Picasso;
import io.intercom.com.squareup.picasso.RequestHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

class BitmapHunter implements Runnable {
    private static final Object DECODE_LOCK = new Object();
    private static final RequestHandler ERRORING_HANDLER = new RequestHandler() {
        public boolean canHandleRequest(Request data) {
            return true;
        }

        public RequestHandler.Result load(Request request, int networkPolicy) throws IOException {
            throw new IllegalStateException("Unrecognized type of request: " + request);
        }
    };
    private static final ThreadLocal<StringBuilder> NAME_BUILDER = new ThreadLocal<StringBuilder>() {
        /* access modifiers changed from: protected */
        public StringBuilder initialValue() {
            return new StringBuilder("Picasso-");
        }
    };
    private static final AtomicInteger SEQUENCE_GENERATOR = new AtomicInteger();
    Action action;
    List<Action> actions;
    final Cache cache;
    final Request data;
    final Dispatcher dispatcher;
    Exception exception;
    int exifRotation;
    Future<?> future;
    final String key;
    Picasso.LoadedFrom loadedFrom;
    final int memoryPolicy;
    int networkPolicy;
    final Picasso picasso;
    Picasso.Priority priority;
    final RequestHandler requestHandler;
    Bitmap result;
    int retryCount;
    final int sequence = SEQUENCE_GENERATOR.incrementAndGet();
    final Stats stats;

    BitmapHunter(Picasso picasso2, Dispatcher dispatcher2, Cache cache2, Stats stats2, Action action2, RequestHandler requestHandler2) {
        this.picasso = picasso2;
        this.dispatcher = dispatcher2;
        this.cache = cache2;
        this.stats = stats2;
        this.action = action2;
        this.key = action2.getKey();
        this.data = action2.getRequest();
        this.priority = action2.getPriority();
        this.memoryPolicy = action2.getMemoryPolicy();
        this.networkPolicy = action2.getNetworkPolicy();
        this.requestHandler = requestHandler2;
        this.retryCount = requestHandler2.getRetryCount();
    }

    static Bitmap decodeStream(InputStream stream, Request request) throws IOException {
        MarkableInputStream markStream = new MarkableInputStream(stream);
        InputStream stream2 = markStream;
        long mark = markStream.savePosition(65536);
        BitmapFactory.Options options = RequestHandler.createBitmapOptions(request);
        boolean calculateSize = RequestHandler.requiresInSampleSize(options);
        boolean isWebPFile = Utils.isWebPFile(stream2);
        markStream.reset(mark);
        if (isWebPFile) {
            byte[] bytes = Utils.toByteArray(stream2);
            if (calculateSize) {
                BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                RequestHandler.calculateInSampleSize(request.targetWidth, request.targetHeight, options, request);
            }
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        }
        if (calculateSize) {
            BitmapFactory.decodeStream(stream2, (Rect) null, options);
            RequestHandler.calculateInSampleSize(request.targetWidth, request.targetHeight, options, request);
            markStream.reset(mark);
        }
        Bitmap bitmap = BitmapFactory.decodeStream(stream2, (Rect) null, options);
        if (bitmap != null) {
            return bitmap;
        }
        throw new IOException("Failed to decode stream.");
    }

    public void run() {
        String str;
        try {
            updateThreadName(this.data);
            if (this.picasso.loggingEnabled) {
                Utils.log("Hunter", "executing", Utils.getLogIdsForHunter(this));
            }
            this.result = hunt();
            if (this.result == null) {
                this.dispatcher.dispatchFailed(this);
            } else {
                this.dispatcher.dispatchComplete(this);
            }
        } catch (Downloader.ResponseException e) {
            if (!e.localCacheOnly || e.responseCode != 504) {
                this.exception = e;
            }
            this.dispatcher.dispatchFailed(this);
        } catch (NetworkRequestHandler.ContentLengthException e2) {
            this.exception = e2;
            this.dispatcher.dispatchRetry(this);
        } catch (IOException e3) {
            this.exception = e3;
            this.dispatcher.dispatchRetry(this);
        } catch (OutOfMemoryError e4) {
            StringWriter writer = new StringWriter();
            this.stats.createSnapshot().dump(new PrintWriter(writer));
            this.exception = new RuntimeException(writer.toString(), e4);
            this.dispatcher.dispatchFailed(this);
        } catch (Exception e5) {
            this.exception = e5;
            this.dispatcher.dispatchFailed(this);
        } finally {
            str = "Picasso-Idle";
            Thread.currentThread().setName(str);
        }
    }

    /* access modifiers changed from: package-private */
    public Bitmap hunt() throws IOException {
        Bitmap bitmap = null;
        if (!MemoryPolicy.shouldReadFromMemoryCache(this.memoryPolicy) || (bitmap = this.cache.get(this.key)) == null) {
            this.data.networkPolicy = this.retryCount == 0 ? NetworkPolicy.OFFLINE.index : this.networkPolicy;
            RequestHandler.Result result2 = this.requestHandler.load(this.data, this.networkPolicy);
            if (result2 != null) {
                this.loadedFrom = result2.getLoadedFrom();
                this.exifRotation = result2.getExifOrientation();
                bitmap = result2.getBitmap();
                if (bitmap == null) {
                    InputStream is = result2.getStream();
                    try {
                        bitmap = decodeStream(is, this.data);
                    } finally {
                        Utils.closeQuietly(is);
                    }
                }
            }
            if (bitmap != null) {
                if (this.picasso.loggingEnabled) {
                    Utils.log("Hunter", "decoded", this.data.logId());
                }
                this.stats.dispatchBitmapDecoded(bitmap);
                if (this.data.needsTransformation() || this.exifRotation != 0) {
                    synchronized (DECODE_LOCK) {
                        if (this.data.needsMatrixTransform() || this.exifRotation != 0) {
                            bitmap = transformResult(this.data, bitmap, this.exifRotation);
                            if (this.picasso.loggingEnabled) {
                                Utils.log("Hunter", "transformed", this.data.logId());
                            }
                        }
                        if (this.data.hasCustomTransformations()) {
                            bitmap = applyCustomTransformations(this.data.transformations, bitmap);
                            if (this.picasso.loggingEnabled) {
                                Utils.log("Hunter", "transformed", this.data.logId(), "from custom transformations");
                            }
                        }
                    }
                    if (bitmap != null) {
                        this.stats.dispatchBitmapTransformed(bitmap);
                    }
                }
            }
            return bitmap;
        }
        this.stats.dispatchCacheHit();
        this.loadedFrom = Picasso.LoadedFrom.MEMORY;
        if (this.picasso.loggingEnabled) {
            Utils.log("Hunter", "decoded", this.data.logId(), "from cache");
        }
        return bitmap;
    }

    /* access modifiers changed from: package-private */
    public void attach(Action action2) {
        boolean loggingEnabled = this.picasso.loggingEnabled;
        Request request = action2.request;
        if (this.action == null) {
            this.action = action2;
            if (!loggingEnabled) {
                return;
            }
            if (this.actions == null || this.actions.isEmpty()) {
                Utils.log("Hunter", "joined", request.logId(), "to empty hunter");
            } else {
                Utils.log("Hunter", "joined", request.logId(), Utils.getLogIdsForHunter(this, "to "));
            }
        } else {
            if (this.actions == null) {
                this.actions = new ArrayList(3);
            }
            this.actions.add(action2);
            if (loggingEnabled) {
                Utils.log("Hunter", "joined", request.logId(), Utils.getLogIdsForHunter(this, "to "));
            }
            Picasso.Priority actionPriority = action2.getPriority();
            if (actionPriority.ordinal() > this.priority.ordinal()) {
                this.priority = actionPriority;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void detach(Action action2) {
        boolean detached = false;
        if (this.action == action2) {
            this.action = null;
            detached = true;
        } else if (this.actions != null) {
            detached = this.actions.remove(action2);
        }
        if (detached && action2.getPriority() == this.priority) {
            this.priority = computeNewPriority();
        }
        if (this.picasso.loggingEnabled) {
            Utils.log("Hunter", "removed", action2.request.logId(), Utils.getLogIdsForHunter(this, "from "));
        }
    }

    private Picasso.Priority computeNewPriority() {
        boolean hasMultiple;
        boolean hasAny;
        Picasso.Priority newPriority = Picasso.Priority.LOW;
        if (this.actions == null || this.actions.isEmpty()) {
            hasMultiple = false;
        } else {
            hasMultiple = true;
        }
        if (this.action != null || hasMultiple) {
            hasAny = true;
        } else {
            hasAny = false;
        }
        if (!hasAny) {
            return newPriority;
        }
        if (this.action != null) {
            newPriority = this.action.getPriority();
        }
        if (hasMultiple) {
            int n = this.actions.size();
            for (int i = 0; i < n; i++) {
                Picasso.Priority actionPriority = this.actions.get(i).getPriority();
                if (actionPriority.ordinal() > newPriority.ordinal()) {
                    newPriority = actionPriority;
                }
            }
        }
        return newPriority;
    }

    /* access modifiers changed from: package-private */
    public boolean cancel() {
        if (this.action != null) {
            return false;
        }
        if ((this.actions == null || this.actions.isEmpty()) && this.future != null && this.future.cancel(false)) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean isCancelled() {
        return this.future != null && this.future.isCancelled();
    }

    /* access modifiers changed from: package-private */
    public boolean shouldRetry(boolean airplaneMode, NetworkInfo info) {
        boolean hasRetries;
        if (this.retryCount > 0) {
            hasRetries = true;
        } else {
            hasRetries = false;
        }
        if (!hasRetries) {
            return false;
        }
        this.retryCount--;
        return this.requestHandler.shouldRetry(airplaneMode, info);
    }

    /* access modifiers changed from: package-private */
    public boolean supportsReplay() {
        return this.requestHandler.supportsReplay();
    }

    /* access modifiers changed from: package-private */
    public Bitmap getResult() {
        return this.result;
    }

    /* access modifiers changed from: package-private */
    public String getKey() {
        return this.key;
    }

    /* access modifiers changed from: package-private */
    public int getMemoryPolicy() {
        return this.memoryPolicy;
    }

    /* access modifiers changed from: package-private */
    public Request getData() {
        return this.data;
    }

    /* access modifiers changed from: package-private */
    public Action getAction() {
        return this.action;
    }

    /* access modifiers changed from: package-private */
    public Picasso getPicasso() {
        return this.picasso;
    }

    /* access modifiers changed from: package-private */
    public List<Action> getActions() {
        return this.actions;
    }

    /* access modifiers changed from: package-private */
    public Exception getException() {
        return this.exception;
    }

    /* access modifiers changed from: package-private */
    public Picasso.LoadedFrom getLoadedFrom() {
        return this.loadedFrom;
    }

    /* access modifiers changed from: package-private */
    public Picasso.Priority getPriority() {
        return this.priority;
    }

    static void updateThreadName(Request data2) {
        String name = data2.getName();
        StringBuilder builder = NAME_BUILDER.get();
        builder.ensureCapacity("Picasso-".length() + name.length());
        builder.replace("Picasso-".length(), builder.length(), name);
        Thread.currentThread().setName(builder.toString());
    }

    static BitmapHunter forRequest(Picasso picasso2, Dispatcher dispatcher2, Cache cache2, Stats stats2, Action action2) {
        Request request = action2.getRequest();
        List<RequestHandler> requestHandlers = picasso2.getRequestHandlers();
        int count = requestHandlers.size();
        for (int i = 0; i < count; i++) {
            RequestHandler requestHandler2 = requestHandlers.get(i);
            if (requestHandler2.canHandleRequest(request)) {
                return new BitmapHunter(picasso2, dispatcher2, cache2, stats2, action2, requestHandler2);
            }
        }
        return new BitmapHunter(picasso2, dispatcher2, cache2, stats2, action2, ERRORING_HANDLER);
    }

    static Bitmap applyCustomTransformations(List<Transformation> transformations, Bitmap result2) {
        int i = 0;
        int count = transformations.size();
        while (i < count) {
            final Transformation transformation = transformations.get(i);
            try {
                Bitmap newResult = transformation.transform(result2);
                if (newResult == null) {
                    final StringBuilder builder = new StringBuilder().append("Transformation ").append(transformation.key()).append(" returned null after ").append(i).append(" previous transformation(s).\n\nTransformation list:\n");
                    for (Transformation t : transformations) {
                        builder.append(t.key()).append(10);
                    }
                    Picasso.HANDLER.post(new Runnable() {
                        public void run() {
                            throw new NullPointerException(builder.toString());
                        }
                    });
                    return null;
                } else if (newResult == result2 && result2.isRecycled()) {
                    Picasso.HANDLER.post(new Runnable() {
                        public void run() {
                            throw new IllegalStateException("Transformation " + transformation.key() + " returned input Bitmap but recycled it.");
                        }
                    });
                    return null;
                } else if (newResult == result2 || result2.isRecycled()) {
                    result2 = newResult;
                    i++;
                } else {
                    Picasso.HANDLER.post(new Runnable() {
                        public void run() {
                            throw new IllegalStateException("Transformation " + transformation.key() + " mutated input Bitmap but failed to recycle the original.");
                        }
                    });
                    return null;
                }
            } catch (RuntimeException e) {
                Picasso.HANDLER.post(new Runnable() {
                    public void run() {
                        throw new RuntimeException("Transformation " + transformation.key() + " crashed with exception.", e);
                    }
                });
                return null;
            }
        }
        return result2;
    }

    static Bitmap transformResult(Request data2, Bitmap result2, int exifRotation2) {
        float scale;
        float scaleX;
        float scaleY;
        int inWidth = result2.getWidth();
        int inHeight = result2.getHeight();
        boolean onlyScaleDown = data2.onlyScaleDown;
        int drawX = 0;
        int drawY = 0;
        int drawWidth = inWidth;
        int drawHeight = inHeight;
        Matrix matrix = new Matrix();
        if (data2.needsMatrixTransform()) {
            int targetWidth = data2.targetWidth;
            int targetHeight = data2.targetHeight;
            float targetRotation = data2.rotationDegrees;
            if (targetRotation != 0.0f) {
                if (data2.hasRotationPivot) {
                    matrix.setRotate(targetRotation, data2.rotationPivotX, data2.rotationPivotY);
                } else {
                    matrix.setRotate(targetRotation);
                }
            }
            if (data2.centerCrop) {
                float widthRatio = ((float) targetWidth) / ((float) inWidth);
                float heightRatio = ((float) targetHeight) / ((float) inHeight);
                if (widthRatio > heightRatio) {
                    int newSize = (int) Math.ceil((double) (((float) inHeight) * (heightRatio / widthRatio)));
                    drawY = (inHeight - newSize) / 2;
                    drawHeight = newSize;
                    scaleX = widthRatio;
                    scaleY = ((float) targetHeight) / ((float) drawHeight);
                } else {
                    int newSize2 = (int) Math.ceil((double) (((float) inWidth) * (widthRatio / heightRatio)));
                    drawX = (inWidth - newSize2) / 2;
                    drawWidth = newSize2;
                    scaleX = ((float) targetWidth) / ((float) drawWidth);
                    scaleY = heightRatio;
                }
                if (shouldResize(onlyScaleDown, inWidth, inHeight, targetWidth, targetHeight)) {
                    matrix.preScale(scaleX, scaleY);
                }
            } else if (data2.centerInside) {
                float widthRatio2 = ((float) targetWidth) / ((float) inWidth);
                float heightRatio2 = ((float) targetHeight) / ((float) inHeight);
                if (widthRatio2 < heightRatio2) {
                    scale = widthRatio2;
                } else {
                    scale = heightRatio2;
                }
                if (shouldResize(onlyScaleDown, inWidth, inHeight, targetWidth, targetHeight)) {
                    matrix.preScale(scale, scale);
                }
            } else if (!((targetWidth == 0 && targetHeight == 0) || (targetWidth == inWidth && targetHeight == inHeight))) {
                float sx = targetWidth != 0 ? ((float) targetWidth) / ((float) inWidth) : ((float) targetHeight) / ((float) inHeight);
                float sy = targetHeight != 0 ? ((float) targetHeight) / ((float) inHeight) : ((float) targetWidth) / ((float) inWidth);
                if (shouldResize(onlyScaleDown, inWidth, inHeight, targetWidth, targetHeight)) {
                    matrix.preScale(sx, sy);
                }
            }
        }
        if (exifRotation2 != 0) {
            matrix.preRotate((float) exifRotation2);
        }
        Bitmap newResult = Bitmap.createBitmap(result2, drawX, drawY, drawWidth, drawHeight, matrix, true);
        if (newResult == result2) {
            return result2;
        }
        result2.recycle();
        return newResult;
    }

    private static boolean shouldResize(boolean onlyScaleDown, int inWidth, int inHeight, int targetWidth, int targetHeight) {
        return !onlyScaleDown || inWidth > targetWidth || inHeight > targetHeight;
    }
}
