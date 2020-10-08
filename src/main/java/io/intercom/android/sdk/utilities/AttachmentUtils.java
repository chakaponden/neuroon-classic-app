package io.intercom.android.sdk.utilities;

import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import io.intercom.okio.BufferedSink;
import io.intercom.okio.BufferedSource;
import io.intercom.okio.Okio;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class AttachmentUtils {
    public static final String MIME_TYPE_AUDIO = "audio";
    public static final String MIME_TYPE_EXCEL = "excel";
    public static final String MIME_TYPE_IMAGE = "image";
    public static final String MIME_TYPE_OTHER = "other";
    public static final String MIME_TYPE_PDF = "pdf";
    public static final String MIME_TYPE_POWERPOINT = "powerpoint";
    public static final String MIME_TYPE_TEXT = "text";
    public static final String MIME_TYPE_VIDEO = "video";
    public static final String MIME_TYPE_WORD = "word";
    public static final String MIME_TYPE_XML = "xml";
    public static final String MIME_TYPE_ZIP = "zip";

    public static String getMimeType(ContentResolver contentResolver, Uri uri) {
        String mimeType = contentResolver.getType(uri);
        return TextUtils.isEmpty(mimeType) ? MIME_TYPE_OTHER : mimeType;
    }

    public static File getFile(String path) {
        return new File(path);
    }

    public static File getFile(Activity activity, Uri uri) {
        File file;
        InputStream inputStream = getInputStream(activity.getContentResolver(), uri);
        try {
            file = File.createTempFile("prefix", "extension", activity.getCacheDir());
        } catch (IOException e) {
            file = new File("");
        }
        try {
            BufferedSource bufferedSource = Okio.buffer(Okio.source(inputStream));
            BufferedSink sink = Okio.buffer(Okio.sink(file));
            sink.writeAll(bufferedSource);
            sink.close();
            bufferedSource.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (IllegalArgumentException e3) {
        }
        return file;
    }

    public static String getPath(File file) {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            return "";
        }
    }

    public static InputStream getInputStream(ContentResolver contentResolver, Uri uri) {
        try {
            return contentResolver.openInputStream(uri);
        } catch (IOException e) {
            return null;
        }
    }

    public static void populateBitmapOptions(String path, BitmapFactory.Options options) {
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
    }
}
