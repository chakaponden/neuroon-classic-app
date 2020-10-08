package io.intercom.com.google.gson.internal.bind;

import io.intercom.com.google.gson.Gson;
import io.intercom.com.google.gson.JsonSyntaxException;
import io.intercom.com.google.gson.TypeAdapter;
import io.intercom.com.google.gson.TypeAdapterFactory;
import io.intercom.com.google.gson.reflect.TypeToken;
import io.intercom.com.google.gson.stream.JsonReader;
import io.intercom.com.google.gson.stream.JsonToken;
import io.intercom.com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public final class TimeTypeAdapter extends TypeAdapter<Time> {
    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            if (typeToken.getRawType() == Time.class) {
                return new TimeTypeAdapter();
            }
            return null;
        }
    };
    private final DateFormat format = new SimpleDateFormat("hh:mm:ss a");

    public synchronized Time read(JsonReader in) throws IOException {
        Time time;
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            time = null;
        } else {
            try {
                time = new Time(this.format.parse(in.nextString()).getTime());
            } catch (ParseException e) {
                throw new JsonSyntaxException((Throwable) e);
            }
        }
        return time;
    }

    public synchronized void write(JsonWriter out, Time value) throws IOException {
        out.value(value == null ? null : this.format.format(value));
    }
}