package io.intercom.android.sdk.blocks;

import java.util.Locale;

public enum BlockAlignment {
    LEFT {
        public int getGravity() {
            return 3;
        }
    },
    CENTER {
        public int getGravity() {
            return 1;
        }
    },
    RIGHT {
        public int getGravity() {
            return 5;
        }
    };

    public abstract int getGravity();

    public static BlockAlignment alignValueOf(String align) {
        BlockAlignment blockAlignment = LEFT;
        try {
            return valueOf(align.toUpperCase(Locale.ENGLISH));
        } catch (IllegalArgumentException | NullPointerException e) {
            return blockAlignment;
        }
    }
}
