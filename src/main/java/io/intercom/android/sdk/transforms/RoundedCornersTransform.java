package io.intercom.android.sdk.transforms;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import io.intercom.com.squareup.picasso.Transformation;

public class RoundedCornersTransform implements Transformation {
    private int radius;

    public RoundedCornersTransform(int radius2) {
        this.radius = radius2;
    }

    public Bitmap transform(Bitmap source) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        new Canvas(output).drawRoundRect(new RectF(0.0f, 0.0f, (float) source.getWidth(), (float) source.getHeight()), (float) this.radius, (float) this.radius, paint);
        if (source != output) {
            source.recycle();
        }
        return output;
    }

    public String key() {
        return "rounded(radius=" + this.radius + ")";
    }
}
