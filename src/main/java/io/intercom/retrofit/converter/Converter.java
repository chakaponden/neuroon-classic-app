package io.intercom.retrofit.converter;

import io.intercom.retrofit.mime.TypedInput;
import io.intercom.retrofit.mime.TypedOutput;
import java.lang.reflect.Type;

public interface Converter {
    Object fromBody(TypedInput typedInput, Type type) throws ConversionException;

    TypedOutput toBody(Object obj);
}
