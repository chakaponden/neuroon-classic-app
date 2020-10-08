package io.intercom.android.sdk.blocks;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import io.intercom.android.sdk.Bridge;
import io.intercom.android.sdk.R;
import io.intercom.android.sdk.blocks.blockInterfaces.LightWeightReplyBlock;
import io.intercom.android.sdk.interfaces.LWRListener;
import io.intercom.android.sdk.models.LWR;

public class LightWeightReply implements LightWeightReplyBlock, View.OnTouchListener {
    private static final String LWR_EMOTION_HAPPY = "happy";
    private static final String LWR_EMOTION_NEUTRAL = "neutral";
    private static final String LWR_EMOTION_SAD = "sad";
    private static final String LWR_THUMBS_DOWN = "thumbs_down";
    private static final String LWR_THUMBS_UP = "thumbs_up";
    private final String baseColour = Bridge.getIdentityStore().getAppConfig().getBaseColor();
    private final LayoutInflater inflater;
    /* access modifiers changed from: private */
    public final LWRListener lwrListener;
    /* access modifiers changed from: private */
    public LWR lwrObject = new LWR.Builder().build();

    private enum Type {
        thumbs,
        emotions,
        unknown
    }

    public LightWeightReply(Context context, StyleType style, LWRListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.lwrListener = listener;
    }

    public void setLwrObject(LWR lwr) {
        this.lwrObject = lwr;
    }

    public View addLWR(String lwrName, boolean isFirstObject, boolean isLastObject, ViewGroup parent) {
        Type type;
        try {
            type = Type.valueOf(lwrName);
        } catch (IllegalArgumentException e) {
            type = Type.unknown;
        }
        switch (type) {
            case thumbs:
                LinearLayout view = (LinearLayout) this.inflater.inflate(R.layout.intercomsdk_lwr_thumbs_layout, parent, false);
                configureThumbs(view);
                return view;
            case emotions:
                LinearLayout view2 = (LinearLayout) this.inflater.inflate(R.layout.intercomsdk_lwr_emotions_layout, parent, false);
                configureEmotions(view2);
                return view2;
            default:
                return (LinearLayout) this.inflater.inflate(R.layout.intercomsdk_lwr_unkown_layout, parent, false);
        }
    }

    private void configureThumbs(View view) {
        final ImageButton thumbsUp = (ImageButton) view.findViewById(R.id.thumbs_up);
        final ImageButton thumbsDown = (ImageButton) view.findViewById(R.id.thumbs_down);
        if (this.lwrObject.getOption().isEmpty()) {
            thumbsUp.setOnTouchListener(this);
            thumbsDown.setOnTouchListener(this);
            thumbsUp.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    thumbsUp.setEnabled(false);
                    thumbsDown.setEnabled(false);
                    LightWeightReply.this.lwrObject.setOption(LightWeightReply.LWR_THUMBS_UP);
                    LightWeightReply.this.lwrListener.sendLWRResponse(LightWeightReply.this.lwrObject);
                }
            });
            thumbsDown.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    thumbsDown.setEnabled(false);
                    thumbsUp.setEnabled(false);
                    LightWeightReply.this.lwrObject.setOption(LightWeightReply.LWR_THUMBS_DOWN);
                    LightWeightReply.this.lwrListener.sendLWRResponse(LightWeightReply.this.lwrObject);
                }
            });
        } else if (LWR_THUMBS_UP.equals(this.lwrObject.getOption())) {
            thumbsUp.setEnabled(false);
            thumbsUp.setColorFilter(Color.parseColor(this.baseColour));
            thumbsDown.setEnabled(false);
        } else if (LWR_THUMBS_DOWN.equals(this.lwrObject.getOption())) {
            thumbsDown.setEnabled(false);
            thumbsDown.setColorFilter(Color.parseColor(this.baseColour));
            thumbsUp.setEnabled(false);
        }
    }

    private void configureEmotions(View view) {
        final ImageButton happyButton = (ImageButton) view.findViewById(R.id.happy);
        final ImageButton neutralButton = (ImageButton) view.findViewById(R.id.neutral);
        final ImageButton sadButton = (ImageButton) view.findViewById(R.id.sad);
        if (this.lwrObject.getOption().isEmpty()) {
            happyButton.setOnTouchListener(this);
            neutralButton.setOnTouchListener(this);
            sadButton.setOnTouchListener(this);
            happyButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    happyButton.setEnabled(false);
                    neutralButton.setEnabled(false);
                    sadButton.setEnabled(false);
                    LightWeightReply.this.lwrObject.setOption(LightWeightReply.LWR_EMOTION_HAPPY);
                    LightWeightReply.this.lwrListener.sendLWRResponse(LightWeightReply.this.lwrObject);
                }
            });
            neutralButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    neutralButton.setEnabled(false);
                    happyButton.setEnabled(false);
                    sadButton.setEnabled(false);
                    LightWeightReply.this.lwrObject.setOption(LightWeightReply.LWR_EMOTION_NEUTRAL);
                    LightWeightReply.this.lwrListener.sendLWRResponse(LightWeightReply.this.lwrObject);
                }
            });
            sadButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    sadButton.setEnabled(false);
                    happyButton.setEnabled(false);
                    happyButton.setEnabled(false);
                    LightWeightReply.this.lwrObject.setOption(LightWeightReply.LWR_EMOTION_SAD);
                    LightWeightReply.this.lwrListener.sendLWRResponse(LightWeightReply.this.lwrObject);
                }
            });
        } else if (LWR_EMOTION_HAPPY.equals(this.lwrObject.getOption())) {
            happyButton.setEnabled(false);
            happyButton.setColorFilter(Color.parseColor(this.baseColour));
            neutralButton.setEnabled(false);
            sadButton.setEnabled(false);
        } else if (LWR_EMOTION_NEUTRAL.equals(this.lwrObject.getOption())) {
            neutralButton.setEnabled(false);
            neutralButton.setColorFilter(Color.parseColor(this.baseColour));
            happyButton.setEnabled(false);
            sadButton.setEnabled(false);
        } else if (LWR_EMOTION_SAD.equals(this.lwrObject.getOption())) {
            sadButton.setEnabled(false);
            sadButton.setColorFilter(Color.parseColor(this.baseColour));
            happyButton.setEnabled(false);
            neutralButton.setEnabled(false);
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        ImageButton button = (ImageButton) v;
        if (event.getAction() == 1 || event.getAction() == 0) {
            button.setColorFilter(Color.parseColor(this.baseColour));
            return false;
        } else if (event.getAction() != 3) {
            return false;
        } else {
            button.setColorFilter((ColorFilter) null);
            return false;
        }
    }
}
