package io.intercom.com.google.gson.internal.bind;

import io.intercom.com.google.gson.Gson;
import io.intercom.com.google.gson.JsonSyntaxException;
import io.intercom.com.google.gson.TypeAdapter;
import io.intercom.com.google.gson.TypeAdapterFactory;
import io.intercom.com.google.gson.internal.bind.util.ISO8601Utils;
import io.intercom.com.google.gson.reflect.TypeToken;
import io.intercom.com.google.gson.stream.JsonReader;
import io.intercom.com.google.gson.stream.JsonToken;
import io.intercom.com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.Locale;

public final class DateTypeAdapter extends TypeAdapter<Date> {
    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            if (typeToken.getRawType() == Date.class) {
                return new DateTypeAdapter();
            }
            return null;
        }
    };
    private final DateFormat enUsFormat = DateFormat.getDateTimeInstance(2, 2, Locale.US);
    private final DateFormat localFormat = DateFormat.getDateTimeInstance(2, 2);

    public Date read(JsonReader in) throws IOException {
        if (in.peek() != JsonToken.NULL) {
            return deserializeToDate(in.nextString());
        }
        in.nextNull();
        return null;
    }

    private synchronized Date deserializeToDate(String json) {
        Date parse;
        try {
            parse = this.localFormat.parse(json);
        } catch (ParseException e) {
            try {
                parse = this.enUsFormat.parse(json);
            } catch (ParseException e2) {
                try {
                    parse = ISO8601Utils.parse(json, new ParsePosition(0));
                } catch (ParseException e3) {
                    throw new JsonSyntaxException(json, e3);
                }
            }
        }
        return parse;
    }

    public synchronized void write(JsonWriter out, Date value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(this.enUsFormat.format(value));
        }
    }
}
