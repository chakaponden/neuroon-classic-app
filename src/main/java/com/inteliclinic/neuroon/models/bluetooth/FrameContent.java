package com.inteliclinic.neuroon.models.bluetooth;

public abstract class FrameContent {
    public abstract byte[] toBytes();

    protected static int getUInt32(byte[] frame, int i) {
        return unsignedByteToInt(frame[i]) + (unsignedByteToInt(frame[i + 1]) << 8) + (unsignedByteToInt(frame[i + 2]) << 16) + (unsignedByteToInt(frame[i + 3]) << 24);
    }

    protected static int getUShort(byte[] frame, int i) {
        return unsignedByteToInt(frame[i]) + (unsignedByteToInt(frame[i + 1]) << 8);
    }

    protected static int unsignedByteToInt(byte b) {
        return b & 255;
    }
}
