package com.inteliclinic.neuroon.old_guava;

import android.support.annotation.Nullable;

public final class Strings {
    private Strings() {
    }

    public static boolean isNullOrEmpty(@Nullable String string) {
        return string == null || string.isEmpty();
    }
}
