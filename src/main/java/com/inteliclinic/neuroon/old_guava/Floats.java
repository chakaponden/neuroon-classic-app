package com.inteliclinic.neuroon.old_guava;

import java.util.Collection;

public final class Floats {
    private Floats() {
    }

    public static float min(float... array) {
        float min = array[0];
        for (int i = 1; i < array.length; i++) {
            min = Math.min(min, array[i]);
        }
        return min;
    }

    public static float max(float... array) {
        float max = array[0];
        for (int i = 1; i < array.length; i++) {
            max = Math.max(max, array[i]);
        }
        return max;
    }

    public static float[] toArray(Collection<? extends Number> collection) {
        Object[] boxedArray = collection.toArray();
        int len = boxedArray.length;
        float[] array = new float[len];
        for (int i = 0; i < len; i++) {
            array[i] = ((Number) boxedArray[i]).floatValue();
        }
        return array;
    }
}
