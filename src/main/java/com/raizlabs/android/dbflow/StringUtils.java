package com.raizlabs.android.dbflow;

public class StringUtils {
    public static boolean isNotNullOrEmpty(String inString) {
        return inString != null && !inString.equals("") && inString.length() > 0;
    }
}
