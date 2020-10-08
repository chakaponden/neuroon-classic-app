package com.raizlabs.android.dbflow.data;

public class Blob {
    private byte[] blob;

    public Blob() {
    }

    public Blob(byte[] blob2) {
        this.blob = blob2;
    }

    public void setBlob(byte[] blob2) {
        this.blob = blob2;
    }

    public byte[] getBlob() {
        return this.blob;
    }
}
