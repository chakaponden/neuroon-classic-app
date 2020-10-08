package retrofit;

import io.fabric.sdk.android.services.network.HttpRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import retrofit.client.Header;
import retrofit.http.Body;
import retrofit.http.EncodedPath;
import retrofit.http.EncodedQuery;
import retrofit.http.EncodedQueryMap;
import retrofit.http.Field;
import retrofit.http.FieldMap;
import retrofit.http.Part;
import retrofit.http.PartMap;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;
import rx.Observable;

final class RestMethodInfo {
    private static final String PARAM = "[a-zA-Z][a-zA-Z0-9_-]*";
    private static final Pattern PARAM_NAME_REGEX = Pattern.compile(PARAM);
    private static final Pattern PARAM_URL_REGEX = Pattern.compile("\\{([a-zA-Z][a-zA-Z0-9_-]*)\\}");
    String contentTypeHeader;
    List<Header> headers;
    final boolean isObservable;
    boolean isStreaming;
    final boolean isSynchronous;
    boolean loaded = false;
    final Method method;
    boolean requestHasBody;
    String requestMethod;
    Annotation[] requestParamAnnotations;
    String requestQuery;
    RequestType requestType = RequestType.SIMPLE;
    String requestUrl;
    Set<String> requestUrlParamNames;
    Type responseObjectType;
    final ResponseType responseType;

    enum RequestType {
        SIMPLE,
        MULTIPART,
        FORM_URL_ENCODED
    }

    private enum ResponseType {
        VOID,
        OBSERVABLE,
        OBJECT
    }

    RestMethodInfo(Method method2) {
        boolean z;
        boolean z2 = true;
        this.method = method2;
        this.responseType = parseResponseType();
        if (this.responseType == ResponseType.OBJECT) {
            z = true;
        } else {
            z = false;
        }
        this.isSynchronous = z;
        this.isObservable = this.responseType != ResponseType.OBSERVABLE ? false : z2;
    }

    private RuntimeException methodError(String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        return new IllegalArgumentException(this.method.getDeclaringClass().getSimpleName() + "." + this.method.getName() + ": " + message);
    }

    private RuntimeException parameterError(int index, String message, Object... args) {
        return methodError(message + " (parameter #" + (index + 1) + ")", args);
    }

    /* access modifiers changed from: package-private */
    public synchronized void init() {
        if (!this.loaded) {
            parseMethodAnnotations();
            parseParameters();
            this.loaded = true;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r11v0, resolved type: java.lang.annotation.Annotation[]} */
    /* JADX WARNING: type inference failed for: r3v0, types: [java.lang.annotation.Annotation] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void parseMethodAnnotations() {
        /*
            r15 = this;
            java.lang.reflect.Method r7 = r15.method
            java.lang.annotation.Annotation[] r9 = r7.getAnnotations()
            int r10 = r9.length
            r7 = 0
            r8 = r7
        L_0x0009:
            if (r8 >= r10) goto L_0x00f8
            r4 = r9[r8]
            java.lang.Class r0 = r4.annotationType()
            r5 = 0
            java.lang.annotation.Annotation[] r11 = r0.getAnnotations()
            int r12 = r11.length
            r7 = 0
        L_0x0018:
            if (r7 >= r12) goto L_0x0027
            r3 = r11[r7]
            java.lang.Class<retrofit.http.RestMethod> r13 = retrofit.http.RestMethod.class
            java.lang.Class r14 = r3.annotationType()
            if (r13 != r14) goto L_0x0043
            r5 = r3
            retrofit.http.RestMethod r5 = (retrofit.http.RestMethod) r5
        L_0x0027:
            if (r5 == 0) goto L_0x007d
            java.lang.String r7 = r15.requestMethod
            if (r7 == 0) goto L_0x0046
            java.lang.String r7 = "Only one HTTP method is allowed. Found: %s and %s."
            r8 = 2
            java.lang.Object[] r8 = new java.lang.Object[r8]
            r9 = 0
            java.lang.String r10 = r15.requestMethod
            r8[r9] = r10
            r9 = 1
            java.lang.String r10 = r5.value()
            r8[r9] = r10
            java.lang.RuntimeException r7 = r15.methodError(r7, r8)
            throw r7
        L_0x0043:
            int r7 = r7 + 1
            goto L_0x0018
        L_0x0046:
            java.lang.String r7 = "value"
            r11 = 0
            java.lang.Class[] r11 = new java.lang.Class[r11]     // Catch:{ Exception -> 0x006b }
            java.lang.reflect.Method r7 = r0.getMethod(r7, r11)     // Catch:{ Exception -> 0x006b }
            r11 = 0
            java.lang.Object[] r11 = new java.lang.Object[r11]     // Catch:{ Exception -> 0x006b }
            java.lang.Object r6 = r7.invoke(r4, r11)     // Catch:{ Exception -> 0x006b }
            java.lang.String r6 = (java.lang.String) r6     // Catch:{ Exception -> 0x006b }
            r15.parsePath(r6)
            java.lang.String r7 = r5.value()
            r15.requestMethod = r7
            boolean r7 = r5.hasBody()
            r15.requestHasBody = r7
        L_0x0067:
            int r7 = r8 + 1
            r8 = r7
            goto L_0x0009
        L_0x006b:
            r1 = move-exception
            java.lang.String r7 = "Failed to extract String 'value' from @%s annotation."
            r8 = 1
            java.lang.Object[] r8 = new java.lang.Object[r8]
            r9 = 0
            java.lang.String r10 = r0.getSimpleName()
            r8[r9] = r10
            java.lang.RuntimeException r7 = r15.methodError(r7, r8)
            throw r7
        L_0x007d:
            java.lang.Class<retrofit.http.Headers> r7 = retrofit.http.Headers.class
            if (r0 != r7) goto L_0x009b
            retrofit.http.Headers r4 = (retrofit.http.Headers) r4
            java.lang.String[] r2 = r4.value()
            int r7 = r2.length
            if (r7 != 0) goto L_0x0094
            java.lang.String r7 = "@Headers annotation is empty."
            r8 = 0
            java.lang.Object[] r8 = new java.lang.Object[r8]
            java.lang.RuntimeException r7 = r15.methodError(r7, r8)
            throw r7
        L_0x0094:
            java.util.List r7 = r15.parseHeaders(r2)
            r15.headers = r7
            goto L_0x0067
        L_0x009b:
            java.lang.Class<retrofit.http.Multipart> r7 = retrofit.http.Multipart.class
            if (r0 != r7) goto L_0x00b4
            retrofit.RestMethodInfo$RequestType r7 = r15.requestType
            retrofit.RestMethodInfo$RequestType r11 = retrofit.RestMethodInfo.RequestType.SIMPLE
            if (r7 == r11) goto L_0x00af
            java.lang.String r7 = "Only one encoding annotation is allowed."
            r8 = 0
            java.lang.Object[] r8 = new java.lang.Object[r8]
            java.lang.RuntimeException r7 = r15.methodError(r7, r8)
            throw r7
        L_0x00af:
            retrofit.RestMethodInfo$RequestType r7 = retrofit.RestMethodInfo.RequestType.MULTIPART
            r15.requestType = r7
            goto L_0x0067
        L_0x00b4:
            java.lang.Class<retrofit.http.FormUrlEncoded> r7 = retrofit.http.FormUrlEncoded.class
            if (r0 != r7) goto L_0x00cd
            retrofit.RestMethodInfo$RequestType r7 = r15.requestType
            retrofit.RestMethodInfo$RequestType r11 = retrofit.RestMethodInfo.RequestType.SIMPLE
            if (r7 == r11) goto L_0x00c8
            java.lang.String r7 = "Only one encoding annotation is allowed."
            r8 = 0
            java.lang.Object[] r8 = new java.lang.Object[r8]
            java.lang.RuntimeException r7 = r15.methodError(r7, r8)
            throw r7
        L_0x00c8:
            retrofit.RestMethodInfo$RequestType r7 = retrofit.RestMethodInfo.RequestType.FORM_URL_ENCODED
            r15.requestType = r7
            goto L_0x0067
        L_0x00cd:
            java.lang.Class<retrofit.http.Streaming> r7 = retrofit.http.Streaming.class
            if (r0 != r7) goto L_0x0067
            java.lang.reflect.Type r7 = r15.responseObjectType
            java.lang.Class<retrofit.client.Response> r11 = retrofit.client.Response.class
            if (r7 == r11) goto L_0x00f3
            java.lang.String r7 = "Only methods having %s as data type are allowed to have @%s annotation."
            r8 = 2
            java.lang.Object[] r8 = new java.lang.Object[r8]
            r9 = 0
            java.lang.Class<retrofit.client.Response> r10 = retrofit.client.Response.class
            java.lang.String r10 = r10.getSimpleName()
            r8[r9] = r10
            r9 = 1
            java.lang.Class<retrofit.http.Streaming> r10 = retrofit.http.Streaming.class
            java.lang.String r10 = r10.getSimpleName()
            r8[r9] = r10
            java.lang.RuntimeException r7 = r15.methodError(r7, r8)
            throw r7
        L_0x00f3:
            r7 = 1
            r15.isStreaming = r7
            goto L_0x0067
        L_0x00f8:
            java.lang.String r7 = r15.requestMethod
            if (r7 != 0) goto L_0x0106
            java.lang.String r7 = "HTTP method annotation is required (e.g., @GET, @POST, etc.)."
            r8 = 0
            java.lang.Object[] r8 = new java.lang.Object[r8]
            java.lang.RuntimeException r7 = r15.methodError(r7, r8)
            throw r7
        L_0x0106:
            boolean r7 = r15.requestHasBody
            if (r7 != 0) goto L_0x012a
            retrofit.RestMethodInfo$RequestType r7 = r15.requestType
            retrofit.RestMethodInfo$RequestType r8 = retrofit.RestMethodInfo.RequestType.MULTIPART
            if (r7 != r8) goto L_0x011a
            java.lang.String r7 = "Multipart can only be specified on HTTP methods with request body (e.g., @POST)."
            r8 = 0
            java.lang.Object[] r8 = new java.lang.Object[r8]
            java.lang.RuntimeException r7 = r15.methodError(r7, r8)
            throw r7
        L_0x011a:
            retrofit.RestMethodInfo$RequestType r7 = r15.requestType
            retrofit.RestMethodInfo$RequestType r8 = retrofit.RestMethodInfo.RequestType.FORM_URL_ENCODED
            if (r7 != r8) goto L_0x012a
            java.lang.String r7 = "FormUrlEncoded can only be specified on HTTP methods with request body (e.g., @POST)."
            r8 = 0
            java.lang.Object[] r8 = new java.lang.Object[r8]
            java.lang.RuntimeException r7 = r15.methodError(r7, r8)
            throw r7
        L_0x012a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: retrofit.RestMethodInfo.parseMethodAnnotations():void");
    }

    private void parsePath(String path) {
        if (path == null || path.length() == 0 || path.charAt(0) != '/') {
            throw methodError("URL path \"%s\" must start with '/'.", path);
        }
        String url = path;
        String query = null;
        int question = path.indexOf(63);
        if (question != -1 && question < path.length() - 1) {
            url = path.substring(0, question);
            query = path.substring(question + 1);
            if (PARAM_URL_REGEX.matcher(query).find()) {
                throw methodError("URL query string \"%s\" must not have replace block. For dynamic query parameters use @Query.", query);
            }
        }
        Set<String> urlParams = parsePathParameters(path);
        this.requestUrl = url;
        this.requestUrlParamNames = urlParams;
        this.requestQuery = query;
    }

    /* access modifiers changed from: package-private */
    public List<Header> parseHeaders(String[] headers2) {
        List<Header> headerList = new ArrayList<>();
        for (String header : headers2) {
            int colon = header.indexOf(58);
            if (colon == -1 || colon == 0 || colon == header.length() - 1) {
                throw methodError("@Headers value must be in the form \"Name: Value\". Found: \"%s\"", header);
            }
            String headerName = header.substring(0, colon);
            String headerValue = header.substring(colon + 1).trim();
            if (HttpRequest.HEADER_CONTENT_TYPE.equalsIgnoreCase(headerName)) {
                this.contentTypeHeader = headerValue;
            } else {
                headerList.add(new Header(headerName, headerValue));
            }
        }
        return headerList;
    }

    private ResponseType parseResponseType() {
        boolean hasReturnType;
        boolean hasCallback = true;
        Type returnType = this.method.getGenericReturnType();
        Type lastArgType = null;
        Class<?> lastArgClass = null;
        Type[] parameterTypes = this.method.getGenericParameterTypes();
        if (parameterTypes.length > 0) {
            Type typeToCheck = parameterTypes[parameterTypes.length - 1];
            lastArgType = typeToCheck;
            if (typeToCheck instanceof ParameterizedType) {
                typeToCheck = ((ParameterizedType) typeToCheck).getRawType();
            }
            if (typeToCheck instanceof Class) {
                lastArgClass = (Class) typeToCheck;
            }
        }
        if (returnType != Void.TYPE) {
            hasReturnType = true;
        } else {
            hasReturnType = false;
        }
        if (lastArgClass == null || !Callback.class.isAssignableFrom(lastArgClass)) {
            hasCallback = false;
        }
        if (hasReturnType && hasCallback) {
            throw methodError("Must have return type or Callback as last argument, not both.", new Object[0]);
        } else if (!hasReturnType && !hasCallback) {
            throw methodError("Must have either a return type or Callback as last argument.", new Object[0]);
        } else if (hasReturnType) {
            if (Platform.HAS_RX_JAVA) {
                Class rawReturnType = Types.getRawType(returnType);
                if (RxSupport.isObservable(rawReturnType)) {
                    this.responseObjectType = getParameterUpperBound((ParameterizedType) RxSupport.getObservableType(returnType, rawReturnType));
                    return ResponseType.OBSERVABLE;
                }
            }
            this.responseObjectType = returnType;
            return ResponseType.OBJECT;
        } else {
            Type lastArgType2 = Types.getSupertype(lastArgType, Types.getRawType(lastArgType), Callback.class);
            if (lastArgType2 instanceof ParameterizedType) {
                this.responseObjectType = getParameterUpperBound((ParameterizedType) lastArgType2);
                return ResponseType.VOID;
            }
            throw methodError("Last parameter must be of type Callback<X> or Callback<? super X>.", new Object[0]);
        }
    }

    private static Type getParameterUpperBound(ParameterizedType type) {
        Type[] types = type.getActualTypeArguments();
        for (int i = 0; i < types.length; i++) {
            Type paramType = types[i];
            if (paramType instanceof WildcardType) {
                types[i] = ((WildcardType) paramType).getUpperBounds()[0];
            }
        }
        return types[0];
    }

    private void parseParameters() {
        Class<?>[] methodParameterTypes = this.method.getParameterTypes();
        Annotation[][] methodParameterAnnotationArrays = this.method.getParameterAnnotations();
        int count = methodParameterAnnotationArrays.length;
        if (!this.isSynchronous && !this.isObservable) {
            count--;
        }
        Annotation[] requestParamAnnotations2 = new Annotation[count];
        boolean gotField = false;
        boolean gotPart = false;
        boolean gotBody = false;
        for (int i = 0; i < count; i++) {
            Class<?> methodParameterType = methodParameterTypes[i];
            Annotation[] methodParameterAnnotations = methodParameterAnnotationArrays[i];
            if (methodParameterAnnotations != null) {
                int length = methodParameterAnnotations.length;
                int i2 = 0;
                while (true) {
                    int i3 = i2;
                    if (i3 >= length) {
                        break;
                    }
                    Annotation methodParameterAnnotation = methodParameterAnnotations[i3];
                    Class<? extends Annotation> methodAnnotationType = methodParameterAnnotation.annotationType();
                    if (methodAnnotationType == Path.class) {
                        validatePathName(i, ((Path) methodParameterAnnotation).value());
                    } else if (methodAnnotationType == EncodedPath.class) {
                        validatePathName(i, ((EncodedPath) methodParameterAnnotation).value());
                    } else if (!(methodAnnotationType == Query.class || methodAnnotationType == EncodedQuery.class)) {
                        if (methodAnnotationType == QueryMap.class) {
                            if (!Map.class.isAssignableFrom(methodParameterType)) {
                                throw parameterError(i, "@QueryMap parameter type must be Map.", new Object[0]);
                            }
                        } else if (methodAnnotationType == EncodedQueryMap.class) {
                            if (!Map.class.isAssignableFrom(methodParameterType)) {
                                throw parameterError(i, "@EncodedQueryMap parameter type must be Map.", new Object[0]);
                            }
                        } else if (methodAnnotationType != retrofit.http.Header.class) {
                            if (methodAnnotationType == Field.class) {
                                if (this.requestType != RequestType.FORM_URL_ENCODED) {
                                    throw parameterError(i, "@Field parameters can only be used with form encoding.", new Object[0]);
                                }
                                gotField = true;
                            } else if (methodAnnotationType == FieldMap.class) {
                                if (this.requestType != RequestType.FORM_URL_ENCODED) {
                                    throw parameterError(i, "@FieldMap parameters can only be used with form encoding.", new Object[0]);
                                } else if (!Map.class.isAssignableFrom(methodParameterType)) {
                                    throw parameterError(i, "@FieldMap parameter type must be Map.", new Object[0]);
                                } else {
                                    gotField = true;
                                }
                            } else if (methodAnnotationType == Part.class) {
                                if (this.requestType != RequestType.MULTIPART) {
                                    throw parameterError(i, "@Part parameters can only be used with multipart encoding.", new Object[0]);
                                }
                                gotPart = true;
                            } else if (methodAnnotationType == PartMap.class) {
                                if (this.requestType != RequestType.MULTIPART) {
                                    throw parameterError(i, "@PartMap parameters can only be used with multipart encoding.", new Object[0]);
                                } else if (!Map.class.isAssignableFrom(methodParameterType)) {
                                    throw parameterError(i, "@PartMap parameter type must be Map.", new Object[0]);
                                } else {
                                    gotPart = true;
                                }
                            } else if (methodAnnotationType != Body.class) {
                                continue;
                                i2 = i3 + 1;
                            } else if (this.requestType != RequestType.SIMPLE) {
                                throw parameterError(i, "@Body parameters cannot be used with form or multi-part encoding.", new Object[0]);
                            } else if (gotBody) {
                                throw methodError("Multiple @Body method annotations found.", new Object[0]);
                            } else {
                                gotBody = true;
                            }
                        }
                    }
                    if (requestParamAnnotations2[i] != null) {
                        throw parameterError(i, "Multiple Retrofit annotations found, only one allowed: @%s, @%s.", requestParamAnnotations2[i].annotationType().getSimpleName(), methodAnnotationType.getSimpleName());
                    } else {
                        requestParamAnnotations2[i] = methodParameterAnnotation;
                        i2 = i3 + 1;
                    }
                }
            }
            if (requestParamAnnotations2[i] == null) {
                throw parameterError(i, "No Retrofit annotation found.", new Object[0]);
            }
        }
        if (this.requestType == RequestType.SIMPLE && !this.requestHasBody && gotBody) {
            throw methodError("Non-body HTTP method cannot contain @Body or @TypedOutput.", new Object[0]);
        } else if (this.requestType == RequestType.FORM_URL_ENCODED && !gotField) {
            throw methodError("Form-encoded method must contain at least one @Field.", new Object[0]);
        } else if (this.requestType != RequestType.MULTIPART || gotPart) {
            this.requestParamAnnotations = requestParamAnnotations2;
        } else {
            throw methodError("Multipart method must contain at least one @Part.", new Object[0]);
        }
    }

    private void validatePathName(int index, String name) {
        if (!PARAM_NAME_REGEX.matcher(name).matches()) {
            throw parameterError(index, "@Path parameter name must match %s. Found: %s", PARAM_URL_REGEX.pattern(), name);
        } else if (!this.requestUrlParamNames.contains(name)) {
            throw parameterError(index, "URL \"%s\" does not contain \"{%s}\".", this.requestUrl, name);
        }
    }

    static Set<String> parsePathParameters(String path) {
        Matcher m = PARAM_URL_REGEX.matcher(path);
        Set<String> patterns = new LinkedHashSet<>();
        while (m.find()) {
            patterns.add(m.group(1));
        }
        return patterns;
    }

    private static final class RxSupport {
        private RxSupport() {
        }

        public static boolean isObservable(Class rawType) {
            return rawType == Observable.class;
        }

        public static Type getObservableType(Type contextType, Class contextRawType) {
            return Types.getSupertype(contextType, contextRawType, Observable.class);
        }
    }
}
