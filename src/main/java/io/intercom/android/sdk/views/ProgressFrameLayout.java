package io.intercom.android.sdk.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import io.intercom.android.sdk.conversation.UploadProgressListener;
import io.intercom.android.sdk.logger.IntercomLogger;

@TargetApi(11)
public class ProgressFrameLayout extends FrameLayout implements UploadProgressListener {
    /* access modifiers changed from: private */
    public final UploadProgressBar uploadProgressBar;

    public ProgressFrameLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public ProgressFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.uploadProgressBar = new UploadProgressBar(context, attrs);
        addView(this.uploadProgressBar);
    }

    public void uploadNotice(final byte percentUploaded) {
        IntercomLogger.INTERNAL("progress", "" + percentUploaded);
        post(new Runnable() {
            public void run() {
                ProgressFrameLayout.this.uploadProgressBar.setProgress(percentUploaded);
                if (percentUploaded == 90) {
                    ProgressFrameLayout.this.uploadProgressBar.smoothEndAnimation();
                }
            }
        });
    }
}
