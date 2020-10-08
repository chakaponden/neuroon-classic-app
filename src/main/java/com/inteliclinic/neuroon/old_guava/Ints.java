package com.inteliclinic.neuroon.old_guava;

import java.util.Collection;

public final class Ints {
    private Ints() {
    }

    public static int indexOf(int[] array, int target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static int lastIndexOf(int[] array, int target) {
        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    public static int[] toArray(Collection<Integer> collection) {
        Object[] boxedArray = collection.toArray();
        int len = boxedArray.length;
        int[] array = new int[len];
        for (int i = 0; i < len; i++) {
            array[i] = ((Integer) boxedArray[i]).intValue();
        }
        return array;
    }

    public static int[] toArray(double[] doubles) {
        int[] intArray = new int[doubles.length];
        for (int i = 0; i < intArray.length; i++) {
            intArray[i] = (int) doubles[i];
        }
        return intArray;
    }
}
