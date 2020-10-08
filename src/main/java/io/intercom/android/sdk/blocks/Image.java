package io.intercom.android.sdk.blocks;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.utilities.AttachmentUtils;
import io.intercom.android.sdk.utilities.ImageUtils;
import io.intercom.com.squareup.picasso.RequestCreator;

public class Image {
    public static String MIME_TYPE = AttachmentUtils.MIME_TYPE_IMAGE;
    protected final Context context;
    protected final LayoutInflater inflater;
    protected final int maxWidth;

    public Image(Context context2) {
        this.context = context2;
        this.inflater = LayoutInflater.from(context2);
        this.maxWidth = context2.getResources().getDimensionPixelSize(R.dimen.intercomsdk_max_image_width);
    }

    /* access modifiers changed from: protected */
    public void setImageViewBounds(int imageWidth, int imageHeight, ImageView imageView, RequestCreator requestCreator) {
        double aspectRatio = ImageUtils.getAspectRatio(imageWidth, imageHeight);
        if (imageWidth > 0 && imageHeight > 0) {
            imageView.setMinimumWidth(Math.min(imageWidth, this.maxWidth));
            imageView.setMinimumHeight((int) Math.min((double) imageHeight, ((double) this.maxWidth) * aspectRatio));
        }
        requestCreator.resize(this.maxWidth, ImageUtils.getAspectHeight(this.maxWidth, aspectRatio)).onlyScaleDown();
    }

    /* access modifiers changed from: protected */
    public void setBackgroundColor(ImageView imageView) {
        imageView.setBackgroundResource(R.drawable.intercomsdk_rounded_image_preview);
    }
}
