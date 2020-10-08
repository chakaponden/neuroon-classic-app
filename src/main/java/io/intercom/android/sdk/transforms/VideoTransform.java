package io.intercom.android.sdk.transforms;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.utilities.AttachmentUtils;
import io.intercom.com.squareup.picasso.Transformation;

public class VideoTransform implements Transformation {
    public Bitmap transform(Bitmap source) {
        Paint paint = new Paint(1140850688);
        paint.setColorFilter(new PorterDuffColorFilter(1140850688, PorterDuff.Mode.SRC_ATOP));
        Bitmap bmOverlay = Bitmap.createBitmap(source.getWidth(), source.getHeight(), source.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(source, new Matrix(), paint);
        Bitmap play = BitmapFactory.decodeResource(Bridge.getContext().getResources(), R.drawable.intercomsdk_play_button);
        canvas.drawBitmap(play, (float) ((source.getWidth() / 2) - (play.getWidth() / 2)), (float) ((source.getHeight() / 2) - (play.getHeight() / 2)), (Paint) null);
        source.recycle();
        play.recycle();
        return bmOverlay;
    }

    public String key() {
        return AttachmentUtils.MIME_TYPE_VIDEO;
    }
}
