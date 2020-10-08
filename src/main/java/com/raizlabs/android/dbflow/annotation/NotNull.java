package com.raizlabs.android.dbflow.annotation;

public @interface NotNull {
    ConflictAction onNullConflict() default ConflictAction.FAIL;
}
