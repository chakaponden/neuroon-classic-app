package io.intercom.android.sdk.conversation.lightbox;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ProgressBar;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.logger.IntercomLogger;
import io.intercom.android.sdk.views.AttachedImageView;
import io.intercom.com.squareup.picasso.Callback;
import io.intercom.com.squareup.picasso.Picasso;
import io.intercom.com.squareup.picasso.RequestCreator;
import io.intercom.uk.co.senab.photoview.PhotoViewAttacher;

@TargetApi(15)
public class LightboxActivity extends Activity {
    public static final String IMAGE_HEIGHT = "image_height";
    public static final String IMAGE_URL = "image_url";
    public static final String IMAGE_WIDTH = "image_width";
    private static final float MAXIMUM_SCALE = 8.0f;
    private static final float MEDIUM_SCALE = 3.5f;
    /* access modifiers changed from: private */
    public AttachedImageView imageView;
    /* access modifiers changed from: private */
    public LightboxPresenter presenter;
    /* access modifiers changed from: private */
    public ProgressBar progressBar;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intercomsdk_activity_lightbox);
        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.presenter = new LightboxPresenter(this);
            this.imageView = (AttachedImageView) findViewById(R.id.lightbox_image);
            this.imageView.setOnAttachedToWindowListener(new AttachedImageView.OnAttachedToWindowListener() {
                public void callback() {
                    LightboxActivity.this.presenter.prepareImage(extras.getString(LightboxActivity.IMAGE_URL, ""), extras.getInt(LightboxActivity.IMAGE_WIDTH, 0), extras.getInt(LightboxActivity.IMAGE_HEIGHT, 0), LightboxActivity.this.imageView.isHardwareAccelerated());
                }
            });
            this.progressBar = (ProgressBar) findViewById(R.id.loading_wheel);
            return;
        }
        IntercomLogger.ERROR("Invalid parameters used, cannot display image");
        finish();
    }

    /* access modifiers changed from: protected */
    public void displayImage(String url, int targetWidth, int targetHeight) {
        RequestCreator requestCreator = Picasso.with(this).load(url);
        if (targetWidth > 0 || targetHeight > 0) {
            requestCreator.resize(targetWidth, targetHeight).onlyScaleDown();
        }
        requestCreator.into(this.imageView, new Callback() {
            public void onSuccess() {
                IntercomLogger.INTERNAL("lightbox", "SUCCESS");
                PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(LightboxActivity.this.imageView);
                photoViewAttacher.setMaximumScale(LightboxActivity.MAXIMUM_SCALE);
                photoViewAttacher.setMediumScale(LightboxActivity.MEDIUM_SCALE);
                LightboxActivity.this.progressBar.setVisibility(8);
                LightboxActivity.this.imageView.setVisibility(0);
            }

            public void onError() {
                IntercomLogger.INTERNAL("lightbox", "FAIL");
            }
        });
    }
}
