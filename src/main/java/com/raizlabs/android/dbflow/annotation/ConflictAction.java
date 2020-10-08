package com.raizlabs.android.dbflow.annotation;

public enum ConflictAction {
    NONE,
    ROLLBACK,
    ABORT,
    FAIL,
    IGNORE,
    REPLACE;

    public static int getSQLiteDatabaseAlgorithmInt(ConflictAction conflictAction) {
        switch (conflictAction) {
            case ROLLBACK:
                return 1;
            case ABORT:
                return 2;
            case FAIL:
                return 3;
            case IGNORE:
                return 4;
            case REPLACE:
                return 5;
            default:
                return 0;
        }
    }
}
