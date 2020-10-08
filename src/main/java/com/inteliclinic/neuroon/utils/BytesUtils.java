package com.inteliclinic.neuroon.utils;

import android.support.v4.internal.view.SupportMenu;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class BytesUtils {
    private BytesUtils() {
    }

    public static int crc16(byte[] buffer) {
        int crc = SupportMenu.USER_MASK;
        for (byte aBuffer : buffer) {
            int crc2 = (((crc >>> 8) | (crc << 8)) & SupportMenu.USER_MASK) ^ (aBuffer & 255);
            int crc3 = crc2 ^ ((crc2 & 255) >> 4);
            int crc4 = crc3 ^ ((crc3 << 12) & SupportMenu.USER_MASK);
            crc = crc4 ^ (((crc4 & 255) << 5) & SupportMenu.USER_MASK);
        }
        return crc & SupportMenu.USER_MASK;
    }

    public static byte[] toByteArray(short i) {
        return new byte[]{(byte) i, (byte) (i >> 8), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public static byte[] extract(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (true) {
            int read = inputStream.read(buffer, 0, buffer.length);
            if (read != -1) {
                baos.write(buffer, 0, read);
            } else {
                baos.flush();
                return baos.toByteArray();
            }
        }
    }
}
