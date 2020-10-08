package com.inteliclinic.neuroon.models.data.converters;

import com.raizlabs.android.dbflow.converter.TypeConverter;

public class IntArrayConverter extends TypeConverter<byte[], int[]> {
    public static byte[] int2byte(int[] src) {
        int srcLength = src.length;
        byte[] dst = new byte[(srcLength << 2)];
        for (int i = 0; i < srcLength; i++) {
            int x = src[i];
            int j = i << 2;
            int j2 = j + 1;
            dst[j] = (byte) ((x >>> 0) & 255);
            int j3 = j2 + 1;
            dst[j2] = (byte) ((x >>> 8) & 255);
            int j4 = j3 + 1;
            dst[j3] = (byte) ((x >>> 16) & 255);
            int i2 = j4 + 1;
            dst[j4] = (byte) ((x >>> 24) & 255);
        }
        return dst;
    }

    public static int[] byte2int(byte[] src) {
        int srcLength = src.length;
        int[] dst = new int[(srcLength >> 2)];
        for (int i = 0; i < srcLength; i += 4) {
            dst[i >> 2] = (src[i + 3] << 24) | ((src[i + 2] & 255) << 16) | ((src[i + 1] & 255) << 8) | (src[i] & 255);
        }
        return dst;
    }

    public byte[] getDBValue(int[] model) {
        if (model == null) {
            return null;
        }
        return int2byte(model);
    }

    public int[] getModelValue(byte[] data) {
        if (data == null) {
            return null;
        }
        return byte2int(data);
    }
}
