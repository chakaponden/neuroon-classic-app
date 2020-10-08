package com.inteliclinic.neuroon.mask.bluetooth.scanning;

import android.bluetooth.BluetoothAdapter;
import android.util.SparseArray;
import com.raizlabs.android.dbflow.sql.language.Condition;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class BluetoothLeUtils {
    private BluetoothLeUtils() {
    }

    static String toString(SparseArray<byte[]> array) {
        if (array == null) {
            return "null";
        }
        if (array.size() == 0) {
            return "{}";
        }
        StringBuilder buffer = new StringBuilder();
        buffer.append('{');
        for (int i = 0; i < array.size(); i++) {
            buffer.append(array.keyAt(i)).append(Condition.Operation.EQUALS).append(Arrays.toString(array.valueAt(i)));
        }
        buffer.append('}');
        return buffer.toString();
    }

    static <T> String toString(Map<T, byte[]> map) {
        if (map == null) {
            return "null";
        }
        if (map.isEmpty()) {
            return "{}";
        }
        StringBuilder buffer = new StringBuilder();
        buffer.append('{');
        Iterator<Map.Entry<T, byte[]>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Object key = it.next().getKey();
            buffer.append(key).append(Condition.Operation.EQUALS).append(Arrays.toString(map.get(key)));
            if (it.hasNext()) {
                buffer.append(", ");
            }
        }
        buffer.append('}');
        return buffer.toString();
    }

    static boolean equals(SparseArray<byte[]> array, SparseArray<byte[]> otherArray) {
        if (array == otherArray) {
            return true;
        }
        if (array == null || otherArray == null) {
            return false;
        }
        if (array.size() != otherArray.size()) {
            return false;
        }
        for (int i = 0; i < array.size(); i++) {
            if (array.keyAt(i) != otherArray.keyAt(i) || !Arrays.equals(array.valueAt(i), otherArray.valueAt(i))) {
                return false;
            }
        }
        return true;
    }

    static <T> boolean equals(Map<T, byte[]> map, Map<T, byte[]> otherMap) {
        if (map == otherMap) {
            return true;
        }
        if (map == null || otherMap == null) {
            return false;
        }
        if (map.size() != otherMap.size()) {
            return false;
        }
        Set<T> keys = map.keySet();
        if (!keys.equals(otherMap.keySet())) {
            return false;
        }
        for (T key : keys) {
            if (!Objects.deepEquals(map.get(key), otherMap.get(key))) {
                return false;
            }
        }
        return true;
    }

    static void checkAdapterStateOn(BluetoothAdapter adapter) {
        if (adapter == null || adapter.getState() != 12) {
            throw new IllegalStateException("BT Adapter is not turned ON");
        }
    }
}
