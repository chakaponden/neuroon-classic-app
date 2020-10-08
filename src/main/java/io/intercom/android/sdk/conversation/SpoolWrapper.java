package io.intercom.android.sdk.conversation;

import android.content.Context;
import android.media.SoundPool;
import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.logger.IntercomLogger;

public class SpoolWrapper {
    /* access modifiers changed from: private */
    public final int birdy1Id;
    /* access modifiers changed from: private */
    public boolean birdy1Loaded;
    private final SoundPool spool = new SoundPool(1, 5, 0);
    /* access modifiers changed from: private */
    public final int wood1Id;
    /* access modifiers changed from: private */
    public boolean wood1Loaded;
    /* access modifiers changed from: private */
    public final int wood2Id;
    /* access modifiers changed from: private */
    public boolean wood2Loaded;
    /* access modifiers changed from: private */
    public final int wood3Id;
    /* access modifiers changed from: private */
    public boolean wood3Loaded;

    public SpoolWrapper(Context context) {
        this.birdy1Id = this.spool.load(context, R.raw.intercomsdk_birdy_done_1, 1);
        this.wood1Id = this.spool.load(context, R.raw.intercomsdk_wood_done_1, 1);
        this.wood2Id = this.spool.load(context, R.raw.intercomsdk_wood_done_2, 1);
        this.wood3Id = this.spool.load(context, R.raw.intercomsdk_wood_done_3, 1);
        this.spool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if (status != 0) {
                    return;
                }
                if (sampleId == SpoolWrapper.this.birdy1Id) {
                    boolean unused = SpoolWrapper.this.birdy1Loaded = true;
                } else if (sampleId == SpoolWrapper.this.wood1Id) {
                    boolean unused2 = SpoolWrapper.this.wood1Loaded = true;
                } else if (sampleId == SpoolWrapper.this.wood2Id) {
                    boolean unused3 = SpoolWrapper.this.wood2Loaded = true;
                } else if (sampleId == SpoolWrapper.this.wood3Id) {
                    boolean unused4 = SpoolWrapper.this.wood3Loaded = true;
                }
            }
        });
    }

    public void playReplyFailSound() {
        if (this.wood1Loaded && Bridge.getIdentityStore().getAppConfig().isAudioEnabled()) {
            IntercomLogger.INTERNAL("sounds", "playing user reply fail sound");
            this.spool.play(this.wood1Id, 1.0f, 1.0f, 1, 0, 1.0f);
        }
    }

    public void playReplySendingSound() {
        if (this.wood2Loaded && Bridge.getIdentityStore().getAppConfig().isAudioEnabled()) {
            IntercomLogger.INTERNAL("sounds", "playing user reply sending sound");
            this.spool.play(this.wood2Id, 1.0f, 1.0f, 1, 0, 1.0f);
        }
    }

    public void playReplySuccessSound() {
        if (this.wood3Loaded && Bridge.getIdentityStore().getAppConfig().isAudioEnabled()) {
            IntercomLogger.INTERNAL("sounds", "playing user reply success sound");
            this.spool.play(this.wood3Id, 1.0f, 1.0f, 1, 0, 1.0f);
        }
    }

    public void playAdminReplySound() {
        if (this.birdy1Loaded && Bridge.getIdentityStore().getAppConfig().isAudioEnabled()) {
            IntercomLogger.INTERNAL("sounds", "playing admin reply sound");
            this.spool.play(this.birdy1Id, 1.0f, 1.0f, 1, 0, 1.0f);
        }
    }
}
