package io.intercom.android.sdk.attachments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.logger.IntercomLogger;
import io.intercom.android.sdk.views.AttachedImageView;
import io.intercom.com.squareup.picasso.Callback;
import io.intercom.com.squareup.picasso.MemoryPolicy;
import io.intercom.com.squareup.picasso.Picasso;
import io.intercom.com.squareup.picasso.RequestCreator;
import java.io.File;

@TargetApi(15)
public class AttachmentActivity extends Activity implements View.OnClickListener {
    private TextView attachmentDetails;
    private ImageView attachmentIcon;
    /* access modifiers changed from: private */
    public AttachmentPresenter presenter;
    /* access modifiers changed from: private */
    public AttachedImageView previewImage;
    private Button sendButton;
    /* access modifiers changed from: private */
    public ProgressBar spinner;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intercomsdk_activity_attachment);
        this.spinner = (ProgressBar) findViewById(R.id.loading_wheel);
        this.sendButton = (Button) findViewById(R.id.send_button);
        this.previewImage = (AttachedImageView) findViewById(R.id.image_preview);
        this.attachmentIcon = (ImageView) findViewById(R.id.attachment_icon);
        this.attachmentDetails = (TextView) findViewById(R.id.attachment_details);
        this.spinner.getIndeterminateDrawable().setColorFilter(getResources().getColor(17170443), PorterDuff.Mode.SRC_IN);
        this.sendButton.setOnClickListener(this);
        findViewById(R.id.cancel_button).setOnClickListener(this);
        this.presenter = new AttachmentPresenter(this);
        this.previewImage.setOnAttachedToWindowListener(new AttachedImageView.OnAttachedToWindowListener() {
            public void callback() {
                AttachmentActivity.this.presenter.loadFile(AttachmentActivity.this.getIntent().getData(), AttachmentActivity.this.previewImage.isHardwareAccelerated());
            }
        });
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.send_button) {
            this.presenter.sendClicked();
        } else if (id == R.id.cancel_button) {
            this.presenter.cancelClicked();
        }
    }

    /* access modifiers changed from: protected */
    public void showDialog(String message) {
        new AlertDialog.Builder(this, 5).setTitle(getResources().getString(R.string.intercomsdk_failed_to_send)).setMessage(message).setNegativeButton(getResources().getString(R.string.intercomsdk_close), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }

    /* access modifiers changed from: protected */
    public void enableSendButton() {
        this.sendButton.setTextColor(getResources().getColor(17170443));
        this.sendButton.setEnabled(true);
    }

    /* access modifiers changed from: protected */
    public void displayAttachmentDetails(String fileName, long size, Drawable drawable) {
        this.attachmentDetails.setText(fileName + " (" + Formatter.formatFileSize(this, size) + ")");
        this.attachmentDetails.setVisibility(0);
        this.attachmentIcon.setVisibility(0);
        this.attachmentIcon.setImageDrawable(drawable);
        this.spinner.setVisibility(8);
    }

    /* access modifiers changed from: protected */
    public void displayFailedAttachment() {
        this.attachmentDetails.setText(getResources().getString(R.string.intercomsdk_file_failed));
        this.attachmentDetails.setVisibility(0);
        this.attachmentIcon.setVisibility(0);
        this.attachmentIcon.setImageDrawable(getResources().getDrawable(R.drawable.intercomsdk_error));
        this.spinner.setVisibility(8);
    }

    /* access modifiers changed from: protected */
    public void displayImage(File file, int width, int height) {
        RequestCreator requestCreator = Picasso.with(this).load(file);
        if (width > 0 || height > 0) {
            requestCreator.resize(width, height);
        }
        requestCreator.memoryPolicy(MemoryPolicy.NO_CACHE, new MemoryPolicy[0]).into(this.previewImage, new Callback() {
            public void onSuccess() {
                IntercomLogger.INTERNAL("images", "SUCCESS");
                AttachmentActivity.this.previewImage.setVisibility(0);
                AttachmentActivity.this.spinner.setVisibility(8);
            }

            public void onError() {
                IntercomLogger.INTERNAL("images", "FAILED");
                AttachmentActivity.this.presenter.imageFailedToLoad();
            }
        });
    }
}
