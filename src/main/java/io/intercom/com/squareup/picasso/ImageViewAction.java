package io.intercom.com.squareup.picasso;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import io.intercom.com.squareup.picasso.Picasso;

class ImageViewAction extends Action<ImageView> {
    Callback callback;

    ImageViewAction(Picasso picasso, ImageView imageView, Request data, int memoryPolicy, int networkPolicy, int errorResId, Drawable errorDrawable, String key, Object tag, Callback callback2, boolean noFade) {
        super(picasso, imageView, data, memoryPolicy, networkPolicy, errorResId, errorDrawable, key, tag, noFade);
        this.callback = callback2;
    }

    public void complete(Bitmap result, Picasso.LoadedFrom from) {
        if (result == null) {
            throw new AssertionError(String.format("Attempted to complete action with no result!\n%s", new Object[]{this}));
        }
        ImageView target = (ImageView) this.target.get();
        if (target != null) {
            Bitmap bitmap = result;
            Picasso.LoadedFrom loadedFrom = from;
            PicassoDrawable.setBitmap(target, this.picasso.context, bitmap, loadedFrom, this.noFade, this.picasso.indicatorsEnabled);
            if (this.callback != null) {
                this.callback.onSuccess();
            }
        }
    }

    public void error() {
        ImageView target = (ImageView) this.target.get();
        if (target != null) {
            if (this.errorResId != 0) {
                target.setImageResource(this.errorResId);
            } else if (this.errorDrawable != null) {
                target.setImageDrawable(this.errorDrawable);
            }
            if (this.callback != null) {
                this.callback.onError();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void cancel() {
        super.cancel();
        if (this.callback != null) {
            this.callback = null;
        }
    }
}
