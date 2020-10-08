package io.intercom.android.sdk.blocks;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.blocks.blockInterfaces.ImageBlock;
import io.intercom.android.sdk.conversation.lightbox.LightboxActivity;
import io.intercom.android.sdk.logger.IntercomLogger;
import io.intercom.android.sdk.transforms.RoundedCornersTransform;
import io.intercom.android.sdk.utilities.BlockUtils;
import io.intercom.com.squareup.picasso.Callback;
import io.intercom.com.squareup.picasso.Picasso;
import io.intercom.com.squareup.picasso.RequestCreator;
import io.intercom.com.squareup.picasso.Transformation;

public class NetworkImage extends Image implements ImageBlock {
    public NetworkImage(Context context, StyleType style) {
        super(context);
    }

    public View addImage(String url, String linkUrl, int width, int height, BlockAlignment alignment, boolean isFirstObject, boolean isLastObject, ViewGroup parent) {
        FrameLayout rootView = (FrameLayout) this.inflater.inflate(R.layout.intercomsdk_blocks_image, parent, false);
        final ImageView imageView = (ImageView) rootView.findViewById(R.id.image_block);
        setBackgroundColor(imageView);
        final ProgressBar spinner = (ProgressBar) rootView.findViewById(R.id.loading_wheel);
        if (spinner != null) {
            spinner.getIndeterminateDrawable().setColorFilter(this.context.getResources().getColor(R.color.intercomsdk_image_loading_grey), PorterDuff.Mode.SRC_IN);
        }
        if (!TextUtils.isEmpty(url)) {
            RequestCreator requestCreator = Picasso.with(this.context).load(url);
            if (isFirstObject && isLastObject) {
                requestCreator.transform((Transformation) new RoundedCornersTransform(this.context.getResources().getDimensionPixelSize(R.dimen.intercomsdk_image_rounded_corners)));
            }
            setImageViewBounds(width, height, imageView, requestCreator);
            final String str = linkUrl;
            final String str2 = url;
            final int i = width;
            final int i2 = height;
            requestCreator.error(R.drawable.intercomsdk_error).into(imageView, new Callback() {
                public void onSuccess() {
                    IntercomLogger.INTERNAL("images", "SUCCESS");
                    NetworkImage.this.hideLoadingState(spinner, imageView);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            if (str.isEmpty()) {
                                Intent i = new Intent(NetworkImage.this.context, LightboxActivity.class);
                                i.putExtra(LightboxActivity.IMAGE_URL, str2);
                                i.putExtra(LightboxActivity.IMAGE_WIDTH, i);
                                i.putExtra(LightboxActivity.IMAGE_HEIGHT, i2);
                                i.setFlags(268435456);
                                NetworkImage.this.context.startActivity(i);
                                return;
                            }
                            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
                            intent.setFlags(268435456);
                            NetworkImage.this.context.startActivity(intent);
                        }
                    });
                }

                public void onError() {
                    NetworkImage.this.hideLoadingState(spinner, imageView);
                    IntercomLogger.INTERNAL("images", "FAILURE");
                }
            });
        } else {
            hideLoadingState(spinner, imageView);
            imageView.setImageResource(R.drawable.intercomsdk_error);
        }
        BlockUtils.setLayoutMarginsAndGravity(rootView, alignment.getGravity(), isLastObject);
        return rootView;
    }

    /* access modifiers changed from: private */
    public void hideLoadingState(ProgressBar spinner, ImageView imageView) {
        if (spinner != null) {
            spinner.setVisibility(8);
            imageView.setBackgroundResource(17170445);
        }
    }
}
