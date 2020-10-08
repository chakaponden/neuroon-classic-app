package io.intercom.android.sdk.api;

import android.support.v4.media.session.PlaybackStateCompat;
import io.intercom.android.sdk.conversation.UploadProgressListener;
import io.intercom.com.squareup.okhttp.MediaType;
import io.intercom.com.squareup.okhttp.RequestBody;
import io.intercom.com.squareup.okhttp.internal.Util;
import io.intercom.okio.BufferedSink;
import io.intercom.okio.Okio;
import io.intercom.okio.Source;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;

public class ProgressRequestBody extends RequestBody {
    private static final int SEGMENT_SIZE = 2048;
    private final MediaType contentType;
    private final File file;
    private final UploadProgressListener listener;

    public ProgressRequestBody(MediaType contentType2, File file2, UploadProgressListener listener2) {
        this.contentType = contentType2;
        this.file = file2;
        this.listener = listener2;
    }

    public long contentLength() {
        return this.file.length();
    }

    public MediaType contentType() {
        return this.contentType;
    }

    public void writeTo(BufferedSink sink) throws IOException {
        Source source = null;
        try {
            source = Okio.source(this.file);
            long total = 0;
            while (true) {
                long read = source.read(sink.buffer(), PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH);
                if (read != -1) {
                    total += read;
                    sink.flush();
                    this.listener.uploadNotice((byte) ((int) ((((double) ((100 * total) / this.file.length())) * 0.8d) + 10.0d)));
                } else {
                    return;
                }
            }
        } finally {
            Util.closeQuietly((Closeable) source);
        }
    }
}
