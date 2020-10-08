package io.intercom.android.sdk.attachments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.utilities.AttachmentUtils;
import io.intercom.android.sdk.utilities.EglUtils;
import io.intercom.android.sdk.utilities.ImageUtils;

@TargetApi(15)
public class AttachmentPresenter {
    private static final int MAX_UPLOAD_SIZE = 41943040;
    /* access modifiers changed from: private */
    public final AttachmentActivity activity;
    /* access modifiers changed from: private */
    public AttachmentData data;
    /* access modifiers changed from: private */
    public boolean openGlEnabled = true;

    public AttachmentPresenter(AttachmentActivity activity2) {
        this.activity = activity2;
    }

    public void loadFile(Uri attachmentUri, boolean isHardwareAccelerated) {
        this.openGlEnabled = isHardwareAccelerated;
        new LoadFileTask().execute(new Uri[]{attachmentUri});
    }

    public void sendClicked() {
        if (this.data.getSize() == 0) {
            this.activity.showDialog(this.activity.getResources().getString(R.string.intercomsdk_file_failed));
        } else if (this.data.getSize() >= 41943040) {
            this.activity.showDialog(this.activity.getResources().getString(R.string.intercomsdk_file_too_big));
        } else {
            Intent output = new Intent();
            output.setData(this.data.getUri());
            this.activity.setResult(-1, output);
            this.activity.finish();
        }
    }

    public void cancelClicked() {
        this.activity.setResult(0);
        this.activity.finish();
    }

    public void imageFailedToLoad() {
        this.activity.displayAttachmentDetails(this.data.getFileName(), this.data.getSize(), findImageDrawable(this.data.getMimeType()));
    }

    /* access modifiers changed from: private */
    public Drawable findImageDrawable(String mimeType) {
        Resources r = this.activity.getResources();
        if (mimeType.contains(AttachmentUtils.MIME_TYPE_IMAGE)) {
            return r.getDrawable(R.drawable.intercomsdk_image_attachment);
        }
        if (mimeType.contains(AttachmentUtils.MIME_TYPE_VIDEO)) {
            return r.getDrawable(R.drawable.intercomsdk_video_attachment);
        }
        if (mimeType.contains(AttachmentUtils.MIME_TYPE_AUDIO)) {
            return r.getDrawable(R.drawable.intercomsdk_audio_attachment);
        }
        if (mimeType.contains(AttachmentUtils.MIME_TYPE_ZIP)) {
            return r.getDrawable(R.drawable.intercomsdk_zip_attachment);
        }
        if (mimeType.contains(AttachmentUtils.MIME_TYPE_TEXT) || mimeType.contains(AttachmentUtils.MIME_TYPE_WORD)) {
            return r.getDrawable(R.drawable.intercomsdk_text_attachment);
        }
        if (mimeType.contains(AttachmentUtils.MIME_TYPE_EXCEL) || mimeType.contains(AttachmentUtils.MIME_TYPE_POWERPOINT) || mimeType.contains(AttachmentUtils.MIME_TYPE_XML)) {
            return r.getDrawable(R.drawable.intercomsdk_document_attachment);
        }
        if (mimeType.contains(AttachmentUtils.MIME_TYPE_PDF)) {
            return r.getDrawable(R.drawable.intercomsdk_pdf_attachment);
        }
        return r.getDrawable(R.drawable.intercomsdk_general_attachment);
    }

    private class LoadFileTask extends AsyncTask<Uri, Void, AttachmentData> {
        private LoadFileTask() {
        }

        /* access modifiers changed from: protected */
        public AttachmentData doInBackground(Uri... attachmentUri) {
            try {
                return new AttachmentData(attachmentUri[0], "", AttachmentPresenter.this.activity);
            } catch (Exception e) {
                return null;
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(AttachmentData data) {
            if (data == null) {
                AttachmentPresenter.this.activity.displayFailedAttachment();
                return;
            }
            AttachmentData unused = AttachmentPresenter.this.data = data;
            int width = data.getImageWidth();
            int height = data.getImageHeight();
            int eglMaxTextureSize = EglUtils.getEGLMaxTextureSize();
            if (data.getMimeType().contains(AttachmentUtils.MIME_TYPE_IMAGE)) {
                AttachmentPresenter.this.activity.displayImage(data.getFile(), ImageUtils.getBoundedWidth(width, height, eglMaxTextureSize, AttachmentPresenter.this.openGlEnabled), ImageUtils.getBoundedHeight(width, height, eglMaxTextureSize, AttachmentPresenter.this.openGlEnabled));
            } else {
                AttachmentPresenter.this.activity.displayAttachmentDetails(data.getFileName(), data.getSize(), AttachmentPresenter.this.findImageDrawable(data.getMimeType()));
            }
            AttachmentPresenter.this.activity.enableSendButton();
        }
    }
}
