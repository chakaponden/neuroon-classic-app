package io.intercom.android.sdk.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.logger.IntercomLogger;
import io.intercom.android.sdk.models.Avatar;
import io.intercom.android.sdk.transforms.RoundTransform;
import io.intercom.com.squareup.picasso.Picasso;
import io.intercom.com.squareup.picasso.Transformation;

public class AvatarUtils {
    public static void createAvatar(boolean isAdmin, Avatar avatar, ImageView imageView, Context context) {
        if (isAdmin && !avatar.getImageUrl().isEmpty()) {
            Picasso.with(context).load(avatar.getImageUrl()).placeholder(Bridge.getIdentityStore().getAppConfig().getBaseColorAvatar()).error(Bridge.getIdentityStore().getAppConfig().getBaseColorAvatar()).transform((Transformation) new RoundTransform()).into(imageView);
        } else if (!avatar.getInitials().isEmpty()) {
            imageView.setImageBitmap(getTextBitmap(avatar.getInitials(), Bridge.getIdentityStore().getAppConfig().getBaseColor(), context));
        } else {
            imageView.setImageDrawable(Bridge.getIdentityStore().getAppConfig().getBaseColorAvatar());
        }
    }

    public static Bitmap getTextBitmap(String initials, String color, Context context) {
        Bitmap output = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);
        output.eraseColor(createColor(color, context));
        Bitmap roundedBitmap = getRoundedBitmap(output);
        Canvas c = new Canvas(roundedBitmap);
        Paint paint = new Paint(1);
        paint.setColor(-1);
        paint.setTextSize((float) ((int) (20.0f * context.getResources().getDisplayMetrics().density)));
        Rect bounds = new Rect();
        paint.getTextBounds(initials, 0, initials.length(), bounds);
        c.drawText(initials, (float) ((roundedBitmap.getWidth() - bounds.width()) / 2), (float) ((roundedBitmap.getHeight() + bounds.height()) / 2), paint);
        return roundedBitmap;
    }

    private static Bitmap getRoundedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-12434878);
        canvas.drawCircle((float) (bitmap.getWidth() / 2), (float) (bitmap.getHeight() / 2), (float) (bitmap.getWidth() / 2), paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return Bitmap.createScaledBitmap(output, 128, 128, false);
    }

    public static int createColor(String color, Context context) {
        int bgColor = context.getResources().getColor(R.color.intercomsdk_main_blue);
        try {
            if (color.isEmpty()) {
                return bgColor;
            }
            if (color.startsWith("#")) {
                return Color.parseColor(color);
            }
            return Color.parseColor("#" + color);
        } catch (Exception e) {
            IntercomLogger.WARNING("Not a valid color string");
            return bgColor;
        }
    }

    public static Bitmap getNotificationDefaultBitmap(Context context) {
        Drawable drawable = Bridge.getIdentityStore().getAppConfig().getBaseColorAvatar();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.intercomsdk_avatar);
    }
}
