package io.fabric.sdk.android.services.persistence;

import android.annotation.SuppressLint;

public class PreferenceStoreStrategy<T> implements PersistenceStrategy<T> {
    private final String key;
    private final SerializationStrategy<T> serializer;
    private final PreferenceStore store;

    public PreferenceStoreStrategy(PreferenceStore store2, SerializationStrategy<T> serializer2, String preferenceKey) {
        this.store = store2;
        this.serializer = serializer2;
        this.key = preferenceKey;
    }

    @SuppressLint({"CommitPrefEdits"})
    public void save(T object) {
        this.store.save(this.store.edit().putString(this.key, this.serializer.serialize(object)));
    }

    public T restore() {
        return this.serializer.deserialize(this.store.get().getString(this.key, (String) null));
    }

    public void clear() {
        this.store.edit().remove(this.key).commit();
    }
}
