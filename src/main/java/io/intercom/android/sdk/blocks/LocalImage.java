package io.intercom.android.sdk.blocks;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.blocks.blockInterfaces.LocalImageBlock;
import io.intercom.android.sdk.logger.IntercomLogger;
import io.intercom.android.sdk.transforms.RoundedCornersTransform;
import io.intercom.android.sdk.utilities.BlockUtils;
import io.intercom.android.sdk.views.ProgressFrameLayout;
import io.intercom.android.sdk.views.UploadProgressBar;
import io.intercom.com.squareup.picasso.Callback;
import io.intercom.com.squareup.picasso.Picasso;
import io.intercom.com.squareup.picasso.RequestCreator;
import io.intercom.com.squareup.picasso.Transformation;
import java.io.File;

public class LocalImage extends Image implements LocalImageBlock {
    public LocalImage(Context context, StyleType style) {
        super(context);
    }

    public View addImage(String url, int width, int height, BlockAlignment alignment, boolean isFirstObject, boolean isLastObject, ViewGroup parent) {
        ProgressFrameLayout rootView = (ProgressFrameLayout) this.inflater.inflate(R.layout.intercomsdk_blocks_local_image, parent, false);
        final ImageView imageView = (ImageView) rootView.findViewById(R.id.image_block);
        RequestCreator requestCreator = Picasso.with(this.context).load(new File(url));
        setImageViewBounds(width, height, imageView, requestCreator);
        View view = rootView.getChildAt(0);
        if (view instanceof UploadProgressBar) {
            UploadProgressBar uploadProgressBar = (UploadProgressBar) view;
            int spinnerSize = this.context.getResources().getDimensionPixelSize(R.dimen.intercomsdk_local_image_upload_size);
            uploadProgressBar.setLayoutParams(new FrameLayout.LayoutParams(spinnerSize, spinnerSize, 17));
            uploadProgressBar.bringToFront();
        }
        setBackgroundColor(imageView);
        requestCreator.transform((Transformation) new RoundedCornersTransform(this.context.getResources().getDimensionPixelSize(R.dimen.intercomsdk_image_rounded_corners))).into(imageView);
        requestCreator.into(imageView, new Callback() {
            public void onSuccess() {
                IntercomLogger.INTERNAL("images", "SUCCESS");
                imageView.setBackgroundResource(17170445);
            }

            public void onError() {
                IntercomLogger.INTERNAL("images", "FAILURE");
            }
        });
        BlockUtils.setLayoutMarginsAndGravity(rootView, alignment.getGravity(), isLastObject);
        return rootView;
    }
}
