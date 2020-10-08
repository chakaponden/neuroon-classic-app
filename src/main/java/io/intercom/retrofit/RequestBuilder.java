package io.intercom.retrofit;

import com.raizlabs.android.dbflow.sql.language.Condition;
import io.fabric.sdk.android.services.network.HttpRequest;
import io.intercom.retrofit.RequestInterceptor;
import io.intercom.retrofit.client.Header;
import io.intercom.retrofit.client.Request;
import io.intercom.retrofit.converter.Converter;
import io.intercom.retrofit.http.Body;
import io.intercom.retrofit.http.EncodedPath;
import io.intercom.retrofit.http.EncodedQuery;
import io.intercom.retrofit.http.EncodedQueryMap;
import io.intercom.retrofit.http.Field;
import io.intercom.retrofit.http.FieldMap;
import io.intercom.retrofit.http.Part;
import io.intercom.retrofit.http.PartMap;
import io.intercom.retrofit.http.Path;
import io.intercom.retrofit.http.Query;
import io.intercom.retrofit.http.QueryMap;
import io.intercom.retrofit.mime.FormUrlEncodedTypedOutput;
import io.intercom.retrofit.mime.MultipartTypedOutput;
import io.intercom.retrofit.mime.TypedOutput;
import io.intercom.retrofit.mime.TypedString;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

final class RequestBuilder implements RequestInterceptor.RequestFacade {
    private final String apiUrl;
    private TypedOutput body;
    private String contentTypeHeader;
    private final Converter converter;
    private final FormUrlEncodedTypedOutput formBody;
    private List<Header> headers;
    private final boolean isObservable;
    private final boolean isSynchronous;
    private final MultipartTypedOutput multipartBody;
    private final Annotation[] paramAnnotations;
    private StringBuilder queryParams;
    private String relativeUrl;
    private final String requestMethod;

    RequestBuilder(String apiUrl2, RestMethodInfo methodInfo, Converter converter2) {
        this.apiUrl = apiUrl2;
        this.converter = converter2;
        this.paramAnnotations = methodInfo.requestParamAnnotations;
        this.requestMethod = methodInfo.requestMethod;
        this.isSynchronous = methodInfo.isSynchronous;
        this.isObservable = methodInfo.isObservable;
        if (methodInfo.headers != null) {
            this.headers = new ArrayList(methodInfo.headers);
        }
        this.contentTypeHeader = methodInfo.contentTypeHeader;
        this.relativeUrl = methodInfo.requestUrl;
        String requestQuery = methodInfo.requestQuery;
        if (requestQuery != null) {
            this.queryParams = new StringBuilder().append('?').append(requestQuery);
        }
        switch (methodInfo.requestType) {
            case FORM_URL_ENCODED:
                this.formBody = new FormUrlEncodedTypedOutput();
                this.multipartBody = null;
                this.body = this.formBody;
                return;
            case MULTIPART:
                this.formBody = null;
                this.multipartBody = new MultipartTypedOutput();
                this.body = this.multipartBody;
                return;
            case SIMPLE:
                this.formBody = null;
                this.multipartBody = null;
                return;
            default:
                throw new IllegalArgumentException("Unknown request type: " + methodInfo.requestType);
        }
    }

    public void addHeader(String name, String value) {
        if (name == null) {
            throw new IllegalArgumentException("Header name must not be null.");
        } else if (HttpRequest.HEADER_CONTENT_TYPE.equalsIgnoreCase(name)) {
            this.contentTypeHeader = value;
        } else {
            List<Header> headers2 = this.headers;
            if (headers2 == null) {
                headers2 = new ArrayList<>(2);
                this.headers = headers2;
            }
            headers2.add(new Header(name, value));
        }
    }

    public void addPathParam(String name, String value) {
        addPathParam(name, value, true);
    }

    public void addEncodedPathParam(String name, String value) {
        addPathParam(name, value, false);
    }

    private void addPathParam(String name, String value, boolean urlEncodeValue) {
        if (name == null) {
            throw new IllegalArgumentException("Path replacement name must not be null.");
        } else if (value == null) {
            throw new IllegalArgumentException("Path replacement \"" + name + "\" value must not be null.");
        } else if (urlEncodeValue) {
            try {
                this.relativeUrl = this.relativeUrl.replace("{" + name + "}", URLEncoder.encode(String.valueOf(value), HttpRequest.CHARSET_UTF8).replace(Condition.Operation.PLUS, "%20"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Unable to convert path parameter \"" + name + "\" value to UTF-8:" + value, e);
            }
        } else {
            this.relativeUrl = this.relativeUrl.replace("{" + name + "}", String.valueOf(value));
        }
    }

    public void addQueryParam(String name, String value) {
        addQueryParam(name, value, false, true);
    }

    public void addEncodedQueryParam(String name, String value) {
        addQueryParam(name, value, false, false);
    }

    private void addQueryParam(String name, Object value, boolean encodeName, boolean encodeValue) {
        if (value instanceof Iterable) {
            for (Object iterableValue : (Iterable) value) {
                if (iterableValue != null) {
                    addQueryParam(name, iterableValue.toString(), encodeName, encodeValue);
                }
            }
        } else if (value.getClass().isArray()) {
            int arrayLength = Array.getLength(value);
            for (int x = 0; x < arrayLength; x++) {
                Object arrayValue = Array.get(value, x);
                if (arrayValue != null) {
                    addQueryParam(name, arrayValue.toString(), encodeName, encodeValue);
                }
            }
        } else {
            addQueryParam(name, value.toString(), encodeName, encodeValue);
        }
    }

    private void addQueryParam(String name, String value, boolean encodeName, boolean encodeValue) {
        if (name == null) {
            throw new IllegalArgumentException("Query param name must not be null.");
        } else if (value == null) {
            throw new IllegalArgumentException("Query param \"" + name + "\" value must not be null.");
        } else {
            try {
                StringBuilder queryParams2 = this.queryParams;
                if (queryParams2 == null) {
                    queryParams2 = new StringBuilder();
                    this.queryParams = queryParams2;
                }
                queryParams2.append(queryParams2.length() > 0 ? '&' : '?');
                if (encodeName) {
                    name = URLEncoder.encode(name, HttpRequest.CHARSET_UTF8);
                }
                if (encodeValue) {
                    value = URLEncoder.encode(value, HttpRequest.CHARSET_UTF8);
                }
                queryParams2.append(name).append('=').append(value);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Unable to convert query parameter \"" + name + "\" value to UTF-8: " + value, e);
            }
        }
    }

    private void addQueryParamMap(int parameterNumber, Map<?, ?> map, boolean encodeNames, boolean encodeValues) {
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object entryKey = entry.getKey();
            if (entryKey == null) {
                throw new IllegalArgumentException("Parameter #" + (parameterNumber + 1) + " query map contained null key.");
            }
            Object entryValue = entry.getValue();
            if (entryValue != null) {
                addQueryParam(entryKey.toString(), entryValue.toString(), encodeNames, encodeValues);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setArguments(Object[] args) {
        if (args != null) {
            int count = args.length;
            if (!this.isSynchronous && !this.isObservable) {
                count--;
            }
            for (int i = 0; i < count; i++) {
                Object value = args[i];
                Annotation annotation = this.paramAnnotations[i];
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (annotationType == Path.class) {
                    Path path = (Path) annotation;
                    String name = path.value();
                    if (value == null) {
                        throw new IllegalArgumentException("Path parameter \"" + name + "\" value must not be null.");
                    }
                    addPathParam(name, value.toString(), path.encode());
                } else if (annotationType == EncodedPath.class) {
                    String name2 = ((EncodedPath) annotation).value();
                    if (value == null) {
                        throw new IllegalArgumentException("Path parameter \"" + name2 + "\" value must not be null.");
                    }
                    addPathParam(name2, value.toString(), false);
                } else if (annotationType == Query.class) {
                    if (value != null) {
                        Query query = (Query) annotation;
                        addQueryParam(query.value(), value, query.encodeName(), query.encodeValue());
                    }
                } else if (annotationType == EncodedQuery.class) {
                    if (value != null) {
                        addQueryParam(((EncodedQuery) annotation).value(), value, false, false);
                    }
                } else if (annotationType == QueryMap.class) {
                    if (value != null) {
                        QueryMap queryMap = (QueryMap) annotation;
                        addQueryParamMap(i, (Map) value, queryMap.encodeNames(), queryMap.encodeValues());
                    }
                } else if (annotationType == EncodedQueryMap.class) {
                    if (value != null) {
                        addQueryParamMap(i, (Map) value, false, false);
                    }
                } else if (annotationType == io.intercom.retrofit.http.Header.class) {
                    if (value != null) {
                        String name3 = ((io.intercom.retrofit.http.Header) annotation).value();
                        if (value instanceof Iterable) {
                            for (Object iterableValue : (Iterable) value) {
                                if (iterableValue != null) {
                                    addHeader(name3, iterableValue.toString());
                                }
                            }
                        } else if (value.getClass().isArray()) {
                            int arrayLength = Array.getLength(value);
                            for (int x = 0; x < arrayLength; x++) {
                                Object arrayValue = Array.get(value, x);
                                if (arrayValue != null) {
                                    addHeader(name3, arrayValue.toString());
                                }
                            }
                        } else {
                            addHeader(name3, value.toString());
                        }
                    }
                } else if (annotationType == Field.class) {
                    if (value != null) {
                        Field field = (Field) annotation;
                        String name4 = field.value();
                        boolean encodeName = field.encodeName();
                        boolean encodeValue = field.encodeValue();
                        if (value instanceof Iterable) {
                            for (Object iterableValue2 : (Iterable) value) {
                                if (iterableValue2 != null) {
                                    this.formBody.addField(name4, encodeName, iterableValue2.toString(), encodeValue);
                                }
                            }
                        } else if (value.getClass().isArray()) {
                            int arrayLength2 = Array.getLength(value);
                            for (int x2 = 0; x2 < arrayLength2; x2++) {
                                Object arrayValue2 = Array.get(value, x2);
                                if (arrayValue2 != null) {
                                    this.formBody.addField(name4, encodeName, arrayValue2.toString(), encodeValue);
                                }
                            }
                        } else {
                            this.formBody.addField(name4, encodeName, value.toString(), encodeValue);
                        }
                    }
                } else if (annotationType == FieldMap.class) {
                    if (value != null) {
                        FieldMap fieldMap = (FieldMap) annotation;
                        boolean encodeNames = fieldMap.encodeNames();
                        boolean encodeValues = fieldMap.encodeValues();
                        for (Map.Entry<?, ?> entry : ((Map) value).entrySet()) {
                            Object entryKey = entry.getKey();
                            if (entryKey == null) {
                                throw new IllegalArgumentException("Parameter #" + (i + 1) + " field map contained null key.");
                            }
                            Object entryValue = entry.getValue();
                            if (entryValue != null) {
                                this.formBody.addField(entryKey.toString(), encodeNames, entryValue.toString(), encodeValues);
                            }
                        }
                        continue;
                    } else {
                        continue;
                    }
                } else if (annotationType == Part.class) {
                    if (value != null) {
                        String name5 = ((Part) annotation).value();
                        String transferEncoding = ((Part) annotation).encoding();
                        if (value instanceof TypedOutput) {
                            this.multipartBody.addPart(name5, transferEncoding, (TypedOutput) value);
                        } else if (value instanceof String) {
                            this.multipartBody.addPart(name5, transferEncoding, new TypedString((String) value));
                        } else {
                            this.multipartBody.addPart(name5, transferEncoding, this.converter.toBody(value));
                        }
                    }
                } else if (annotationType == PartMap.class) {
                    if (value != null) {
                        String transferEncoding2 = ((PartMap) annotation).encoding();
                        for (Map.Entry<?, ?> entry2 : ((Map) value).entrySet()) {
                            Object entryKey2 = entry2.getKey();
                            if (entryKey2 == null) {
                                throw new IllegalArgumentException("Parameter #" + (i + 1) + " part map contained null key.");
                            }
                            String entryName = entryKey2.toString();
                            Object entryValue2 = entry2.getValue();
                            if (entryValue2 != null) {
                                if (entryValue2 instanceof TypedOutput) {
                                    this.multipartBody.addPart(entryName, transferEncoding2, (TypedOutput) entryValue2);
                                } else if (entryValue2 instanceof String) {
                                    this.multipartBody.addPart(entryName, transferEncoding2, new TypedString((String) entryValue2));
                                } else {
                                    this.multipartBody.addPart(entryName, transferEncoding2, this.converter.toBody(entryValue2));
                                }
                            }
                        }
                        continue;
                    } else {
                        continue;
                    }
                } else if (annotationType != Body.class) {
                    throw new IllegalArgumentException("Unknown annotation: " + annotationType.getCanonicalName());
                } else if (value == null) {
                    throw new IllegalArgumentException("Body parameter value must not be null.");
                } else if (value instanceof TypedOutput) {
                    this.body = (TypedOutput) value;
                } else {
                    this.body = this.converter.toBody(value);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public Request build() throws UnsupportedEncodingException {
        if (this.multipartBody == null || this.multipartBody.getPartCount() != 0) {
            String apiUrl2 = this.apiUrl;
            StringBuilder url = new StringBuilder(apiUrl2);
            if (apiUrl2.endsWith(Condition.Operation.DIVISION)) {
                url.deleteCharAt(url.length() - 1);
            }
            url.append(this.relativeUrl);
            StringBuilder queryParams2 = this.queryParams;
            if (queryParams2 != null) {
                url.append(queryParams2);
            }
            TypedOutput body2 = this.body;
            List<Header> headers2 = this.headers;
            if (this.contentTypeHeader != null) {
                if (body2 != null) {
                    body2 = new MimeOverridingTypedOutput(body2, this.contentTypeHeader);
                } else {
                    Header header = new Header(HttpRequest.HEADER_CONTENT_TYPE, this.contentTypeHeader);
                    if (headers2 == null) {
                        headers2 = Collections.singletonList(header);
                    } else {
                        headers2.add(header);
                    }
                }
            }
            return new Request(this.requestMethod, url.toString(), headers2, body2);
        }
        throw new IllegalStateException("Multipart requests must contain at least one part.");
    }

    private static class MimeOverridingTypedOutput implements TypedOutput {
        private final TypedOutput delegate;
        private final String mimeType;

        MimeOverridingTypedOutput(TypedOutput delegate2, String mimeType2) {
            this.delegate = delegate2;
            this.mimeType = mimeType2;
        }

        public String fileName() {
            return this.delegate.fileName();
        }

        public String mimeType() {
            return this.mimeType;
        }

        public long length() {
            return this.delegate.length();
        }

        public void writeTo(OutputStream out) throws IOException {
            this.delegate.writeTo(out);
        }
    }
}
