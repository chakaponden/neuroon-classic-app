package com.raizlabs.android.dbflow.structure.cache;

import android.support.annotation.NonNull;

public interface IMultiKeyCacheConverter<CacheKeyType> {
    @NonNull
    CacheKeyType getCachingKey(@NonNull Object[] objArr);
}
