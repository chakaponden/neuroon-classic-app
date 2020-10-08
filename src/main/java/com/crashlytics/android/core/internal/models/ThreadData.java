package com.crashlytics.android.core.internal.models;

public class ThreadData {
    public static final int IMPORTANCE_CRASHED_THREAD = 4;
    public final FrameData[] frames;
    public final int importance;
    public final String name;

    public static final class FrameData {
        public final long address;
        public final String file;
        public final int importance;
        public final long offset;
        public final String symbol;

        public FrameData(long address2, int importance2) {
            this(address2, "", importance2);
        }

        public FrameData(long address2, String symbol2, int importance2) {
            this(address2, symbol2, "", 0, importance2);
        }

        public FrameData(long address2, String symbol2, String file2, long offset2, int importance2) {
            this.address = address2;
            this.symbol = symbol2;
            this.file = file2;
            this.offset = offset2;
            this.importance = importance2;
        }
    }

    public ThreadData(int importance2, FrameData[] frames2) {
        this((String) null, importance2, frames2);
    }

    public ThreadData(String name2, int importance2, FrameData[] frames2) {
        this.name = name2;
        this.importance = importance2;
        this.frames = frames2;
    }
}
