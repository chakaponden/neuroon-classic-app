package io.intercom.com.squareup.picasso;

import android.graphics.Bitmap;

public interface Transformation {
    String key();

    Bitmap transform(Bitmap bitmap);
}
