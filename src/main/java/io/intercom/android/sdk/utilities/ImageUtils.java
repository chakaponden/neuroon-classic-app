package io.intercom.android.sdk.utilities;

public class ImageUtils {
    public static double getAspectRatio(int imageWidth, int imageHeight) {
        double aspectRatio = (1.0d * ((double) imageHeight)) / ((double) imageWidth);
        if (Double.isNaN(aspectRatio)) {
            return 0.0d;
        }
        return aspectRatio;
    }

    public static int getAspectHeight(int imageWidth, double aspectRatio) {
        return (int) (((double) imageWidth) * aspectRatio);
    }

    public static int getBoundedWidth(int width, int height, int eglMaxTextureSize, boolean openGlEnabled) {
        double aspectRatio = getAspectRatio(width, height);
        if (!openGlEnabled || eglMaxTextureSize == 0) {
            return width;
        }
        if (aspectRatio <= 1.0d) {
            return Math.min(eglMaxTextureSize, width);
        }
        return (int) Math.min((double) width, ((double) width) / ((((double) height) * 1.0d) / ((double) eglMaxTextureSize)));
    }

    public static int getBoundedHeight(int width, int height, int eglMaxTextureSize, boolean openGlEnabled) {
        double aspectRatio = getAspectRatio(width, height);
        if (!openGlEnabled || eglMaxTextureSize == 0) {
            return height;
        }
        if (aspectRatio > 1.0d) {
            return Math.min(eglMaxTextureSize, height);
        }
        return (int) Math.min((double) height, ((double) height) / ((((double) width) * 1.0d) / ((double) eglMaxTextureSize)));
    }
}
