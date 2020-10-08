package io.intercom.retrofit.mime;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class TypedByteArray implements TypedInput, TypedOutput {
    private final byte[] bytes;
    private final String mimeType;

    public TypedByteArray(String mimeType2, byte[] bytes2) {
        mimeType2 = mimeType2 == null ? "application/unknown" : mimeType2;
        if (bytes2 == null) {
            throw new NullPointerException("bytes");
        }
        this.mimeType = mimeType2;
        this.bytes = bytes2;
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public String fileName() {
        return null;
    }

    public String mimeType() {
        return this.mimeType;
    }

    public long length() {
        return (long) this.bytes.length;
    }

    public void writeTo(OutputStream out) throws IOException {
        out.write(this.bytes);
    }

    public InputStream in() throws IOException {
        return new ByteArrayInputStream(this.bytes);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TypedByteArray that = (TypedByteArray) o;
        if (!Arrays.equals(this.bytes, that.bytes)) {
            return false;
        }
        if (!this.mimeType.equals(that.mimeType)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (this.mimeType.hashCode() * 31) + Arrays.hashCode(this.bytes);
    }

    public String toString() {
        return "TypedByteArray[length=" + length() + "]";
    }
}
