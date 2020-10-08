package io.intercom.com.google.gson.internal.bind;

import io.intercom.com.google.gson.FieldNamingStrategy;
import io.intercom.com.google.gson.Gson;
import io.intercom.com.google.gson.JsonSyntaxException;
import io.intercom.com.google.gson.TypeAdapter;
import io.intercom.com.google.gson.TypeAdapterFactory;
import io.intercom.com.google.gson.annotations.SerializedName;
import io.intercom.com.google.gson.internal.ConstructorConstructor;
import io.intercom.com.google.gson.internal.Excluder;
import io.intercom.com.google.gson.internal.ObjectConstructor;
import io.intercom.com.google.gson.internal.Primitives;
import io.intercom.com.google.gson.reflect.TypeToken;
import io.intercom.com.google.gson.stream.JsonReader;
import io.intercom.com.google.gson.stream.JsonToken;
import io.intercom.com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class ReflectiveTypeAdapterFactory implements TypeAdapterFactory {
    private final ConstructorConstructor constructorConstructor;
    private final Excluder excluder;
    private final FieldNamingStrategy fieldNamingPolicy;

    public ReflectiveTypeAdapterFactory(ConstructorConstructor constructorConstructor2, FieldNamingStrategy fieldNamingPolicy2, Excluder excluder2) {
        this.constructorConstructor = constructorConstructor2;
        this.fieldNamingPolicy = fieldNamingPolicy2;
        this.excluder = excluder2;
    }

    public boolean excludeField(Field f, boolean serialize) {
        return excludeField(f, serialize, this.excluder);
    }

    static boolean excludeField(Field f, boolean serialize, Excluder excluder2) {
        return !excluder2.excludeClass(f.getType(), serialize) && !excluder2.excludeField(f, serialize);
    }

    private List<String> getFieldNames(Field f) {
        return getFieldName(this.fieldNamingPolicy, f);
    }

    static List<String> getFieldName(FieldNamingStrategy fieldNamingPolicy2, Field f) {
        SerializedName serializedName = (SerializedName) f.getAnnotation(SerializedName.class);
        List<String> fieldNames = new LinkedList<>();
        if (serializedName == null) {
            fieldNames.add(fieldNamingPolicy2.translateName(f));
        } else {
            fieldNames.add(serializedName.value());
            for (String alternate : serializedName.alternate()) {
                fieldNames.add(alternate);
            }
        }
        return fieldNames;
    }

    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<? super T> raw = type.getRawType();
        if (!Object.class.isAssignableFrom(raw)) {
            return null;
        }
        return new Adapter(this.constructorConstructor.get(type), getBoundFields(gson, type, raw));
    }

    private BoundField createBoundField(Gson context, Field field, String name, TypeToken<?> fieldType, boolean serialize, boolean deserialize) {
        final boolean isPrimitive = Primitives.isPrimitive(fieldType.getRawType());
        final Gson gson = context;
        final Field field2 = field;
        final TypeToken<?> typeToken = fieldType;
        return new BoundField(name, serialize, deserialize) {
            final TypeAdapter<?> typeAdapter = ReflectiveTypeAdapterFactory.this.getFieldAdapter(gson, field2, typeToken);

            /* access modifiers changed from: package-private */
            public void write(JsonWriter writer, Object value) throws IOException, IllegalAccessException {
                new TypeAdapterRuntimeTypeWrapper(gson, this.typeAdapter, typeToken.getType()).write(writer, field2.get(value));
            }

            /* access modifiers changed from: package-private */
            public void read(JsonReader reader, Object value) throws IOException, IllegalAccessException {
                Object fieldValue = this.typeAdapter.read(reader);
                if (fieldValue != null || !isPrimitive) {
                    field2.set(value, fieldValue);
                }
            }

            public boolean writeField(Object value) throws IOException, IllegalAccessException {
                if (this.serialized && field2.get(value) != value) {
                    return true;
                }
                return false;
            }
        };
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:2:0x000a, code lost:
        r0 = io.intercom.com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(r3.constructorConstructor, r4, r6, r1);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public io.intercom.com.google.gson.TypeAdapter<?> getFieldAdapter(io.intercom.com.google.gson.Gson r4, java.lang.reflect.Field r5, io.intercom.com.google.gson.reflect.TypeToken<?> r6) {
        /*
            r3 = this;
            java.lang.Class<io.intercom.com.google.gson.annotations.JsonAdapter> r2 = io.intercom.com.google.gson.annotations.JsonAdapter.class
            java.lang.annotation.Annotation r1 = r5.getAnnotation(r2)
            io.intercom.com.google.gson.annotations.JsonAdapter r1 = (io.intercom.com.google.gson.annotations.JsonAdapter) r1
            if (r1 == 0) goto L_0x0013
            io.intercom.com.google.gson.internal.ConstructorConstructor r2 = r3.constructorConstructor
            io.intercom.com.google.gson.TypeAdapter r0 = io.intercom.com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(r2, r4, r6, r1)
            if (r0 == 0) goto L_0x0013
        L_0x0012:
            return r0
        L_0x0013:
            io.intercom.com.google.gson.TypeAdapter r0 = r4.getAdapter(r6)
            goto L_0x0012
        */
        throw new UnsupportedOperationException("Method not decompiled: io.intercom.com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.getFieldAdapter(io.intercom.com.google.gson.Gson, java.lang.reflect.Field, io.intercom.com.google.gson.reflect.TypeToken):io.intercom.com.google.gson.TypeAdapter");
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: java.lang.Class<java.lang.Object>} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r22v2, resolved type: io.intercom.com.google.gson.reflect.TypeToken<?>} */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x00a4, code lost:
        r22 = io.intercom.com.google.gson.reflect.TypeToken.get(io.intercom.com.google.gson.internal.C$Gson$Types.resolve(r22.getType(), r23, r23.getGenericSuperclass()));
        r23 = r22.getRawType();
        r22 = r22;
     */
    /* JADX WARNING: Incorrect type for immutable var: ssa=java.lang.Class<?>, code=java.lang.Class, for r23v0, types: [java.lang.Class<?>, java.lang.Class] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.Map<java.lang.String, io.intercom.com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField> getBoundFields(io.intercom.com.google.gson.Gson r21, io.intercom.com.google.gson.reflect.TypeToken<?> r22, java.lang.Class r23) {
        /*
            r20 = this;
            java.util.LinkedHashMap r17 = new java.util.LinkedHashMap
            r17.<init>()
            boolean r2 = r23.isInterface()
            if (r2 == 0) goto L_0x000c
        L_0x000b:
            return r17
        L_0x000c:
            java.lang.reflect.Type r10 = r22.getType()
        L_0x0010:
            java.lang.Class<java.lang.Object> r2 = java.lang.Object.class
            r0 = r23
            if (r0 == r2) goto L_0x000b
            java.lang.reflect.Field[] r13 = r23.getDeclaredFields()
            int r0 = r13.length
            r19 = r0
            r2 = 0
            r18 = r2
        L_0x0020:
            r0 = r18
            r1 = r19
            if (r0 >= r1) goto L_0x00a4
            r4 = r13[r18]
            r2 = 1
            r0 = r20
            boolean r7 = r0.excludeField(r4, r2)
            r2 = 0
            r0 = r20
            boolean r8 = r0.excludeField(r4, r2)
            if (r7 != 0) goto L_0x003f
            if (r8 != 0) goto L_0x003f
        L_0x003a:
            int r2 = r18 + 1
            r18 = r2
            goto L_0x0020
        L_0x003f:
            r2 = 1
            r4.setAccessible(r2)
            java.lang.reflect.Type r2 = r22.getType()
            java.lang.reflect.Type r3 = r4.getGenericType()
            r0 = r23
            java.lang.reflect.Type r12 = io.intercom.com.google.gson.internal.C$Gson$Types.resolve(r2, r0, r3)
            r0 = r20
            java.util.List r11 = r0.getFieldNames(r4)
            r15 = 0
            r14 = 0
        L_0x0059:
            int r2 = r11.size()
            if (r14 >= r2) goto L_0x0083
            java.lang.Object r5 = r11.get(r14)
            java.lang.String r5 = (java.lang.String) r5
            if (r14 == 0) goto L_0x0068
            r7 = 0
        L_0x0068:
            io.intercom.com.google.gson.reflect.TypeToken r6 = io.intercom.com.google.gson.reflect.TypeToken.get((java.lang.reflect.Type) r12)
            r2 = r20
            r3 = r21
            io.intercom.com.google.gson.internal.bind.ReflectiveTypeAdapterFactory$BoundField r9 = r2.createBoundField(r3, r4, r5, r6, r7, r8)
            r0 = r17
            java.lang.Object r16 = r0.put(r5, r9)
            io.intercom.com.google.gson.internal.bind.ReflectiveTypeAdapterFactory$BoundField r16 = (io.intercom.com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.BoundField) r16
            if (r15 != 0) goto L_0x0080
            r15 = r16
        L_0x0080:
            int r14 = r14 + 1
            goto L_0x0059
        L_0x0083:
            if (r15 == 0) goto L_0x003a
            java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.StringBuilder r3 = r3.append(r10)
            java.lang.String r6 = " declares multiple JSON fields named "
            java.lang.StringBuilder r3 = r3.append(r6)
            java.lang.String r6 = r15.name
            java.lang.StringBuilder r3 = r3.append(r6)
            java.lang.String r3 = r3.toString()
            r2.<init>(r3)
            throw r2
        L_0x00a4:
            java.lang.reflect.Type r2 = r22.getType()
            java.lang.reflect.Type r3 = r23.getGenericSuperclass()
            r0 = r23
            java.lang.reflect.Type r2 = io.intercom.com.google.gson.internal.C$Gson$Types.resolve(r2, r0, r3)
            io.intercom.com.google.gson.reflect.TypeToken r22 = io.intercom.com.google.gson.reflect.TypeToken.get((java.lang.reflect.Type) r2)
            java.lang.Class r23 = r22.getRawType()
            goto L_0x0010
        */
        throw new UnsupportedOperationException("Method not decompiled: io.intercom.com.google.gson.internal.bind.ReflectiveTypeAdapterFactory.getBoundFields(io.intercom.com.google.gson.Gson, io.intercom.com.google.gson.reflect.TypeToken, java.lang.Class):java.util.Map");
    }

    static abstract class BoundField {
        final boolean deserialized;
        final String name;
        final boolean serialized;

        /* access modifiers changed from: package-private */
        public abstract void read(JsonReader jsonReader, Object obj) throws IOException, IllegalAccessException;

        /* access modifiers changed from: package-private */
        public abstract void write(JsonWriter jsonWriter, Object obj) throws IOException, IllegalAccessException;

        /* access modifiers changed from: package-private */
        public abstract boolean writeField(Object obj) throws IOException, IllegalAccessException;

        protected BoundField(String name2, boolean serialized2, boolean deserialized2) {
            this.name = name2;
            this.serialized = serialized2;
            this.deserialized = deserialized2;
        }
    }

    public static final class Adapter<T> extends TypeAdapter<T> {
        private final Map<String, BoundField> boundFields;
        private final ObjectConstructor<T> constructor;

        private Adapter(ObjectConstructor<T> constructor2, Map<String, BoundField> boundFields2) {
            this.constructor = constructor2;
            this.boundFields = boundFields2;
        }

        public T read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            T instance = this.constructor.construct();
            try {
                in.beginObject();
                while (in.hasNext()) {
                    BoundField field = this.boundFields.get(in.nextName());
                    if (field == null || !field.deserialized) {
                        in.skipValue();
                    } else {
                        field.read(in, instance);
                    }
                }
                in.endObject();
                return instance;
            } catch (IllegalStateException e) {
                throw new JsonSyntaxException((Throwable) e);
            } catch (IllegalAccessException e2) {
                throw new AssertionError(e2);
            }
        }

        public void write(JsonWriter out, T value) throws IOException {
            if (value == null) {
                out.nullValue();
                return;
            }
            out.beginObject();
            try {
                for (BoundField boundField : this.boundFields.values()) {
                    if (boundField.writeField(value)) {
                        out.name(boundField.name);
                        boundField.write(out, value);
                    }
                }
                out.endObject();
            } catch (IllegalAccessException e) {
                throw new AssertionError(e);
            }
        }
    }
}
