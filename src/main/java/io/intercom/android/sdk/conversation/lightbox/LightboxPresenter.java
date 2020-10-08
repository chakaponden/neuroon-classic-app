package io.intercom.android.sdk.conversation.lightbox;

import android.text.TextUtils;
import io.intercom.android.sdk.utilities.EglUtils;
import io.intercom.android.sdk.utilities.ImageUtils;

public class LightboxPresenter {
    private final LightboxActivity activity;

    public LightboxPresenter(LightboxActivity activity2) {
        this.activity = activity2;
    }

    /* access modifiers changed from: protected */
    public void prepareImage(String imageUrl, int width, int height, boolean isHardWareAccelerated) {
        if (!TextUtils.isEmpty(imageUrl)) {
            int eglMaxTextureSize = EglUtils.getEGLMaxTextureSize();
            this.activity.displayImage(imageUrl, ImageUtils.getBoundedWidth(width, height, eglMaxTextureSize, isHardWareAccelerated), ImageUtils.getBoundedHeight(width, height, eglMaxTextureSize, isHardWareAccelerated));
        }
    }
}
