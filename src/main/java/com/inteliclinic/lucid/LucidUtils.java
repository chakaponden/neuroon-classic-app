package com.inteliclinic.lucid;

import android.support.annotation.Nullable;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class LucidUtils {
    private static final String TAG = LucidUtils.class.getSimpleName();

    private LucidUtils() {
    }

    public static Object extractReleaseMemory(long sPointer) {
        return extractSexp(sPointer);
    }

    @Nullable
    public static Object extractSexp(long sPointer) {
        if (Sexp.NullPointer(sPointer)) {
            return null;
        }
        int type = Sexp.SexpGetType(sPointer);
        switch (type) {
            case 2:
                int sz = Sexp.SexpGetArraySize(sPointer);
                if (sz == 0) {
                    return new ArrayList();
                }
                if (Sexp.SexpIsSpecialFormAndSpecialQuote(sPointer)) {
                    return extractSexp(Sexp.SexpSubExpsHeadNextPointer(sPointer));
                }
                if (Sexp.isSpecialKeywordAlist(sPointer)) {
                    return extractMapFromSexp(sPointer);
                }
                long tmpPointer = Sexp.SexpSubExpsHeadPointer(sPointer);
                List<Object> objectList = new ArrayList<>(sz);
                for (int i = 0; i < sz; i++) {
                    objectList.add(extractSexp(tmpPointer));
                    tmpPointer = Sexp.SexpNextPointer(tmpPointer);
                }
                return objectList;
            case 5:
                Double[] array = new Double[Sexp.SexpGetValueSize(sPointer)];
                for (int i2 = 0; i2 < array.length; i2++) {
                    array[i2] = Double.valueOf(Sexp.SexpGetArrayValue(sPointer, i2));
                }
                return array;
            case 6:
            case 10:
            case 11:
                return Sexp.SexpGetStringValue(sPointer);
            case 7:
                return Integer.valueOf(Sexp.SexpGetIntegerValue(sPointer));
            case 8:
                return Double.valueOf(Sexp.SexpGetDoubleValue(sPointer));
            case 9:
                return Boolean.valueOf(Sexp.SexpGetBoolValue(sPointer));
            case 12:
            case 13:
            case 14:
                return new ArrayList();
            default:
                Log.e(TAG, "Error: extraction not supported from type: " + type);
                return null;
        }
    }

    private static Map<String, Object> extractMapFromSexp(long sPointer) {
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        long sPointer2 = Sexp.valueSexp(sPointer);
        while (!Sexp.NullPointer(sPointer2)) {
            String s = Sexp.SexpGetStringValue(sPointer2);
            long sPointer3 = Sexp.SexpNextPointer(sPointer2);
            Object o = extractSexp(sPointer3);
            sPointer2 = Sexp.SexpNextPointer(sPointer3);
            stringObjectHashMap.put(s, o);
        }
        return stringObjectHashMap;
    }

    public static long objectToSexp(Object object) {
        long retPointerPointer = Sexp.SexpAllocPointerPointer();
        if (object instanceof Boolean) {
            Sexp.SexpAllocAndCreateNilPointerPointer(retPointerPointer);
            LucidEnv.createBool(retPointerPointer, ((Boolean) object).booleanValue());
        } else if (object instanceof Integer) {
            Sexp.SexpAllocAndCreateNilPointerPointer(retPointerPointer);
            LucidEnv.createInteger(retPointerPointer, ((Integer) object).intValue());
        } else if (object instanceof Long) {
            Sexp.SexpAllocAndCreateNilPointerPointer(retPointerPointer);
            LucidEnv.createLong(retPointerPointer, ((Long) object).longValue());
        } else if (object instanceof Double) {
            Sexp.SexpAllocAndCreateNilPointerPointer(retPointerPointer);
            LucidEnv.createDouble(retPointerPointer, ((Double) object).doubleValue());
        } else if (object instanceof String) {
            Sexp.SexpAllocAndCreateNilPointerPointer(retPointerPointer);
            if (((String) object).charAt(0) == ':') {
                LucidEnv.createKeyword(retPointerPointer, (String) object);
            } else {
                LucidEnv.createString(retPointerPointer, (String) object);
            }
        } else if (object instanceof List) {
            List<Object> list = (List) object;
            Sexp.SexpAllocAndCreateNilPointerPointer(retPointerPointer);
            long qPointer = Sexp.SexpAllocAndInitPointer();
            LucidEnv.createSpecial(qPointer, SpecialType.SpecialQuote);
            long listPointer = Sexp.SexpAllocAndInitPointer();
            for (int i = list.size() - 1; i >= 0; i--) {
                Sexp.SexpListPrepend(listPointer, objectToSexp(list.get(i)));
            }
            Sexp.SexpListPrepend(Sexp.SexpFromTwoToOnePointer(retPointerPointer), listPointer);
            Sexp.SexpListPrepend(Sexp.SexpFromTwoToOnePointer(retPointerPointer), qPointer);
        } else if (object instanceof Map) {
            Sexp.SexpAllocAndCreateNilPointerPointer(retPointerPointer);
            for (Map.Entry<String, Object> objectEntry : ((Map) object).entrySet()) {
                long keyPointerPointer = Sexp.SexpAllocPointerPointer();
                Sexp.SexpAllocAndCreateNilPointerPointer(keyPointerPointer);
                LucidEnv.createKeyword(keyPointerPointer, objectEntry.getKey());
                long keyPointer = Sexp.SexpFromTwoToOnePointer(keyPointerPointer);
                Sexp.SexpListPrepend(Sexp.SexpFromTwoToOnePointer(retPointerPointer), objectToSexp(objectEntry.getValue()));
                Sexp.SexpListPrepend(Sexp.SexpFromTwoToOnePointer(retPointerPointer), keyPointer);
            }
        } else if (object instanceof int[]) {
            int[] array = (int[]) object;
            Sexp.SexpAllocAndCreateNilPointerPointer(retPointerPointer);
            LucidEnv.createIntArray(retPointerPointer, array, array.length);
        } else {
            if (object != null) {
                Log.d(TAG, "object: " + object.toString());
            } else {
                Log.d(TAG, "object null");
            }
            return Sexp.SexpAllocAndInitPointer();
        }
        long retPointer = Sexp.SexpFromTwoToOnePointer(retPointerPointer);
        Sexp.SetSexpEvaled(retPointer);
        Sexp.SexpFree(retPointerPointer);
        return retPointer;
    }
}
