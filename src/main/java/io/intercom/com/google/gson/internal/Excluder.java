package io.intercom.com.google.gson.internal;

import io.intercom.com.google.gson.ExclusionStrategy;
import io.intercom.com.google.gson.FieldAttributes;
import io.intercom.com.google.gson.Gson;
import io.intercom.com.google.gson.TypeAdapter;
import io.intercom.com.google.gson.TypeAdapterFactory;
import io.intercom.com.google.gson.annotations.Expose;
import io.intercom.com.google.gson.annotations.Since;
import io.intercom.com.google.gson.annotations.Until;
import io.intercom.com.google.gson.reflect.TypeToken;
import io.intercom.com.google.gson.stream.JsonReader;
import io.intercom.com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Excluder implements TypeAdapterFactory, Cloneable {
    public static final Excluder DEFAULT = new Excluder();
    private static final double IGNORE_VERSIONS = -1.0d;
    private List<ExclusionStrategy> deserializationStrategies = Collections.emptyList();
    private int modifiers = 136;
    private boolean requireExpose;
    private List<ExclusionStrategy> serializationStrategies = Collections.emptyList();
    private boolean serializeInnerClasses = true;
    private double version = IGNORE_VERSIONS;

    /* access modifiers changed from: protected */
    public Excluder clone() {
        try {
            return (Excluder) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
    }

    public Excluder withVersion(double ignoreVersionsAfter) {
        Excluder result = clone();
        result.version = ignoreVersionsAfter;
        return result;
    }

    public Excluder withModifiers(int... modifiers2) {
        Excluder result = clone();
        result.modifiers = 0;
        int length = modifiers2.length;
        for (int i = 0; i < length; i++) {
            result.modifiers |= modifiers2[i];
        }
        return result;
    }

    public Excluder disableInnerClassSerialization() {
        Excluder result = clone();
        result.serializeInnerClasses = false;
        return result;
    }

    public Excluder excludeFieldsWithoutExposeAnnotation() {
        Excluder result = clone();
        result.requireExpose = true;
        return result;
    }

    public Excluder withExclusionStrategy(ExclusionStrategy exclusionStrategy, boolean serialization, boolean deserialization) {
        Excluder result = clone();
        if (serialization) {
            result.serializationStrategies = new ArrayList(this.serializationStrategies);
            result.serializationStrategies.add(exclusionStrategy);
        }
        if (deserialization) {
            result.deserializationStrategies = new ArrayList(this.deserializationStrategies);
            result.deserializationStrategies.add(exclusionStrategy);
        }
        return result;
    }

    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<? super T> rawType = type.getRawType();
        final boolean skipSerialize = excludeClass(rawType, true);
        final boolean skipDeserialize = excludeClass(rawType, false);
        if (!skipSerialize && !skipDeserialize) {
            return null;
        }
        final Gson gson2 = gson;
        final TypeToken<T> typeToken = type;
        return new TypeAdapter<T>() {
            private TypeAdapter<T> delegate;

            public T read(JsonReader in) throws IOException {
                if (!skipDeserialize) {
                    return delegate().read(in);
                }
                in.skipValue();
                return null;
            }

            public void write(JsonWriter out, T value) throws IOException {
                if (skipSerialize) {
                    out.nullValue();
                } else {
                    delegate().write(out, value);
                }
            }

            private TypeAdapter<T> delegate() {
                TypeAdapter<T> d = this.delegate;
                if (d != null) {
                    return d;
                }
                TypeAdapter<T> d2 = gson2.getDelegateAdapter(Excluder.this, typeToken);
                this.delegate = d2;
                return d2;
            }
        };
    }

    public boolean excludeField(Field field, boolean serialize) {
        Expose annotation;
        if ((this.modifiers & field.getModifiers()) != 0) {
            return true;
        }
        if (this.version != IGNORE_VERSIONS && !isValidVersion((Since) field.getAnnotation(Since.class), (Until) field.getAnnotation(Until.class))) {
            return true;
        }
        if (field.isSynthetic()) {
            return true;
        }
        if (this.requireExpose && ((annotation = (Expose) field.getAnnotation(Expose.class)) == null || (!serialize ? !annotation.deserialize() : !annotation.serialize()))) {
            return true;
        }
        if (!this.serializeInnerClasses && isInnerClass(field.getType())) {
            return true;
        }
        if (isAnonymousOrLocal(field.getType())) {
            return true;
        }
        List<ExclusionStrategy> list = serialize ? this.serializationStrategies : this.deserializationStrategies;
        if (!list.isEmpty()) {
            FieldAttributes fieldAttributes = new FieldAttributes(field);
            for (ExclusionStrategy exclusionStrategy : list) {
                if (exclusionStrategy.shouldSkipField(fieldAttributes)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean excludeClass(Class<?> clazz, boolean serialize) {
        if (this.version != IGNORE_VERSIONS && !isValidVersion((Since) clazz.getAnnotation(Since.class), (Until) clazz.getAnnotation(Until.class))) {
            return true;
        }
        if (!this.serializeInnerClasses && isInnerClass(clazz)) {
            return true;
        }
        if (isAnonymousOrLocal(clazz)) {
            return true;
        }
        for (ExclusionStrategy exclusionStrategy : serialize ? this.serializationStrategies : this.deserializationStrategies) {
            if (exclusionStrategy.shouldSkipClass(clazz)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAnonymousOrLocal(Class<?> clazz) {
        return !Enum.class.isAssignableFrom(clazz) && (clazz.isAnonymousClass() || clazz.isLocalClass());
    }

    private boolean isInnerClass(Class<?> clazz) {
        return clazz.isMemberClass() && !isStatic(clazz);
    }

    private boolean isStatic(Class<?> clazz) {
        return (clazz.getModifiers() & 8) != 0;
    }

    private boolean isValidVersion(Since since, Until until) {
        return isValidSince(since) && isValidUntil(until);
    }

    private boolean isValidSince(Since annotation) {
        if (annotation == null || annotation.value() <= this.version) {
            return true;
        }
        return false;
    }

    private boolean isValidUntil(Until annotation) {
        if (annotation == null || annotation.value() > this.version) {
            return true;
        }
        return false;
    }
}
