package io.intercom.android.sdk.attachments;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.net.Uri;
import io.intercom.android.sdk.utilities.AttachmentUtils;
import io.intercom.android.sdk.utilities.FileUtils;
import java.io.File;

public class AttachmentData {
    private final File file;
    private final String fileName;
    private final String id;
    private int imageHeight = 0;
    private int imageWidth = 0;
    private final String mimeType;
    private String path;
    private final long size;
    private final Uri uri;

    public AttachmentData(Uri uri2, String id2, Activity activity) {
        this.uri = uri2;
        this.id = id2;
        this.path = FileUtils.getAbsolutePath(activity, uri2);
        this.fileName = FileUtils.getFileName(activity, uri2);
        this.mimeType = AttachmentUtils.getMimeType(activity.getContentResolver(), uri2);
        if (this.path.isEmpty()) {
            this.file = AttachmentUtils.getFile(activity, uri2);
            this.path = AttachmentUtils.getPath(this.file);
        } else {
            this.file = AttachmentUtils.getFile(this.path);
        }
        this.size = this.file.length();
        if (this.mimeType.contains(AttachmentUtils.MIME_TYPE_IMAGE)) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            AttachmentUtils.populateBitmapOptions(this.path, options);
            this.imageWidth = options.outWidth;
            this.imageHeight = options.outHeight;
        }
    }

    public String getId() {
        return this.id;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getPath() {
        return this.path;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public int getImageWidth() {
        return this.imageWidth;
    }

    public int getImageHeight() {
        return this.imageHeight;
    }

    public long getSize() {
        return this.size;
    }

    public Uri getUri() {
        return this.uri;
    }

    public File getFile() {
        return this.file;
    }
}
