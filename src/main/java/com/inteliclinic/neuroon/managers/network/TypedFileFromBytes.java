package com.inteliclinic.neuroon.managers.network;

import retrofit.mime.TypedByteArray;

public class TypedFileFromBytes extends TypedByteArray {
    private final String filename;

    public TypedFileFromBytes(String mimeType, String filename2, byte[] bytes) {
        super(mimeType, bytes);
        this.filename = filename2;
    }

    public String fileName() {
        return this.filename;
    }
}
