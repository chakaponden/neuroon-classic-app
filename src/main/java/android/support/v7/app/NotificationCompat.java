package android.support.v7.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.session.MediaSessionCompat;

public class NotificationCompat extends android.support.v4.app.NotificationCompat {
    /* access modifiers changed from: private */
    public static void addMediaStyleToBuilderLollipop(NotificationBuilderWithBuilderAccessor builder, NotificationCompat.Style style) {
        if (style instanceof MediaStyle) {
            MediaStyle mediaStyle = (MediaStyle) style;
            NotificationCompatImpl21.addMediaStyle(builder, mediaStyle.mActionsToShowInCompact, mediaStyle.mToken != null ? mediaStyle.mToken.getToken() : null);
        }
    }

    /* access modifiers changed from: private */
    public static void addMediaStyleToBuilderIcs(NotificationBuilderWithBuilderAccessor builder, NotificationCompat.Builder b) {
        if (b.mStyle instanceof MediaStyle) {
            MediaStyle mediaStyle = (MediaStyle) b.mStyle;
            NotificationCompatImplBase.overrideContentView(builder, b.mContext, b.mContentTitle, b.mContentText, b.mContentInfo, b.mNumber, b.mLargeIcon, b.mSubText, b.mUseChronometer, b.mNotification.when, b.mActions, mediaStyle.mActionsToShowInCompact, mediaStyle.mShowCancelButton, mediaStyle.mCancelButtonIntent);
        }
    }

    /* access modifiers changed from: private */
    public static void addBigMediaStyleToBuilderJellybean(Notification n, NotificationCompat.Builder b) {
        if (b.mStyle instanceof MediaStyle) {
            MediaStyle mediaStyle = (MediaStyle) b.mStyle;
            NotificationCompatImplBase.overrideBigContentView(n, b.mContext, b.mContentTitle, b.mContentText, b.mContentInfo, b.mNumber, b.mLargeIcon, b.mSubText, b.mUseChronometer, b.mNotification.when, b.mActions, mediaStyle.mShowCancelButton, mediaStyle.mCancelButtonIntent);
        }
    }

    public static class Builder extends NotificationCompat.Builder {
        public Builder(Context context) {
            super(context);
        }

        /* access modifiers changed from: protected */
        public NotificationCompat.BuilderExtender getExtender() {
            if (Build.VERSION.SDK_INT >= 21) {
                return new LollipopExtender();
            }
            if (Build.VERSION.SDK_INT >= 16) {
                return new JellybeanExtender();
            }
            if (Build.VERSION.SDK_INT >= 14) {
                return new IceCreamSandwichExtender();
            }
            return super.getExtender();
        }
    }

    private static class IceCreamSandwichExtender extends NotificationCompat.BuilderExtender {
        private IceCreamSandwichExtender() {
        }

        public Notification build(NotificationCompat.Builder b, NotificationBuilderWithBuilderAccessor builder) {
            NotificationCompat.addMediaStyleToBuilderIcs(builder, b);
            return builder.build();
        }
    }

    private static class JellybeanExtender extends NotificationCompat.BuilderExtender {
        private JellybeanExtender() {
        }

        public Notification build(NotificationCompat.Builder b, NotificationBuilderWithBuilderAccessor builder) {
            NotificationCompat.addMediaStyleToBuilderIcs(builder, b);
            Notification n = builder.build();
            NotificationCompat.addBigMediaStyleToBuilderJellybean(n, b);
            return n;
        }
    }

    private static class LollipopExtender extends NotificationCompat.BuilderExtender {
        private LollipopExtender() {
        }

        public Notification build(NotificationCompat.Builder b, NotificationBuilderWithBuilderAccessor builder) {
            NotificationCompat.addMediaStyleToBuilderLollipop(builder, b.mStyle);
            return builder.build();
        }
    }

    public static class MediaStyle extends NotificationCompat.Style {
        int[] mActionsToShowInCompact = null;
        PendingIntent mCancelButtonIntent;
        boolean mShowCancelButton;
        MediaSessionCompat.Token mToken;

        public MediaStyle() {
        }

        public MediaStyle(NotificationCompat.Builder builder) {
            setBuilder(builder);
        }

        public MediaStyle setShowActionsInCompactView(int... actions) {
            this.mActionsToShowInCompact = actions;
            return this;
        }

        public MediaStyle setMediaSession(MediaSessionCompat.Token token) {
            this.mToken = token;
            return this;
        }

        public MediaStyle setShowCancelButton(boolean show) {
            this.mShowCancelButton = show;
            return this;
        }

        public MediaStyle setCancelButtonIntent(PendingIntent pendingIntent) {
            this.mCancelButtonIntent = pendingIntent;
            return this;
        }
    }
}
