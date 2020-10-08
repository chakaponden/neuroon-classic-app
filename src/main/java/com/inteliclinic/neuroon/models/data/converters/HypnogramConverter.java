package com.inteliclinic.neuroon.models.data.converters;

import com.raizlabs.android.dbflow.converter.TypeConverter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class HypnogramConverter extends TypeConverter<byte[], double[][]> {
    public byte[] getDBValue(double[][] model) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            new ObjectOutputStream(out).writeObject(model);
            return out.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    public double[][] getModelValue(byte[] data) {
        try {
            return (double[][]) new ObjectInputStream(new ByteArrayInputStream(data)).readObject();
        } catch (Exception e) {
            return null;
        }
    }
}
