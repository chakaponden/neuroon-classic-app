package io.intercom.android.sdk.preview;

import android.widget.RelativeLayout;

public enum IntercomPreviewPosition {
    BOTTOM_LEFT {
        public void setAlignment(RelativeLayout.LayoutParams params) {
            params.addRule(12);
            params.addRule(9);
        }

        public boolean isLeftAligned() {
            return true;
        }
    },
    BOTTOM_RIGHT {
        public void setAlignment(RelativeLayout.LayoutParams params) {
            params.addRule(12);
            params.addRule(11);
        }

        public boolean isLeftAligned() {
            return false;
        }
    },
    TOP_LEFT {
        public void setAlignment(RelativeLayout.LayoutParams params) {
            params.addRule(10);
            params.addRule(9);
        }

        public boolean isLeftAligned() {
            return true;
        }
    },
    TOP_RIGHT {
        public void setAlignment(RelativeLayout.LayoutParams params) {
            params.addRule(10);
            params.addRule(11);
        }

        public boolean isLeftAligned() {
            return false;
        }
    };

    public abstract boolean isLeftAligned();

    public abstract void setAlignment(RelativeLayout.LayoutParams layoutParams);

    public static IntercomPreviewPosition toPresentationModeEnum(String myEnumString) {
        try {
            return valueOf(myEnumString);
        } catch (Exception e) {
            return BOTTOM_LEFT;
        }
    }
}
