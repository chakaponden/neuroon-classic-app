package com.raizlabs.android.dbflow.structure.provider;

import android.net.Uri;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;

public interface ModelProvider {
    Uri getDeleteUri();

    Uri getInsertUri();

    Uri getQueryUri();

    Uri getUpdateUri();

    void load();

    void load(ConditionGroup conditionGroup, String str, String... strArr);
}
