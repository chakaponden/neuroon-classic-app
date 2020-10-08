package io.fabric.sdk.android.services.settings;

import android.content.Context;
import android.graphics.BitmapFactory;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.common.CommonUtils;

public class IconRequest {
    public final String hash;
    public final int height;
    public final int iconResourceId;
    public final int width;

    public IconRequest(String hash2, int iconResourceId2, int width2, int height2) {
        this.hash = hash2;
        this.iconResourceId = iconResourceId2;
        this.width = width2;
        this.height = height2;
    }

    public static IconRequest build(Context context, String iconHash) {
        if (iconHash == null) {
            return null;
        }
        try {
            int iconId = CommonUtils.getAppIconResourceId(context);
            Fabric.getLogger().d(Fabric.TAG, "App icon resource ID is " + iconId);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(context.getResources(), iconId, options);
            return new IconRequest(iconHash, iconId, options.outWidth, options.outHeight);
        } catch (Exception e) {
            Fabric.getLogger().e(Fabric.TAG, "Failed to load icon", e);
            return null;
        }
    }
}
