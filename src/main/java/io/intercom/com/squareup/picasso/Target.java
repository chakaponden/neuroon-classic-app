package io.intercom.com.squareup.picasso;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import io.intercom.com.squareup.picasso.Picasso;

public interface Target {
    void onBitmapFailed(Drawable drawable);

    void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom);

    void onPrepareLoad(Drawable drawable);
}
