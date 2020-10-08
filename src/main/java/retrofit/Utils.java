package retrofit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executor;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

final class Utils {
    private static final int BUFFER_SIZE = 4096;

    static byte[] streamToBytes(InputStream stream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (stream != null) {
            byte[] buf = new byte[4096];
            while (true) {
                int r = stream.read(buf);
                if (r == -1) {
                    break;
                }
                baos.write(buf, 0, r);
            }
        }
        return baos.toByteArray();
    }

    static Request readBodyToBytesIfNecessary(Request request) throws IOException {
        TypedOutput body = request.getBody();
        if (body == null || (body instanceof TypedByteArray)) {
            return request;
        }
        String bodyMime = body.mimeType();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        body.writeTo(baos);
        return new Request(request.getMethod(), request.getUrl(), request.getHeaders(), new TypedByteArray(bodyMime, baos.toByteArray()));
    }

    static Response readBodyToBytesIfNecessary(Response response) throws IOException {
        TypedInput body = response.getBody();
        if (body != null && !(body instanceof TypedByteArray)) {
            String bodyMime = body.mimeType();
            InputStream is = body.in();
            try {
                TypedByteArray typedByteArray = new TypedByteArray(bodyMime, streamToBytes(is));
                try {
                    response = replaceResponseBody(response, typedByteArray);
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                        }
                    }
                    TypedByteArray typedByteArray2 = typedByteArray;
                } catch (Throwable th) {
                    th = th;
                    TypedByteArray typedByteArray3 = typedByteArray;
                }
            } catch (Throwable th2) {
                th = th2;
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e2) {
                    }
                }
                throw th;
            }
        }
        return response;
    }

    static Response replaceResponseBody(Response response, TypedInput body) {
        return new Response(response.getUrl(), response.getStatus(), response.getReason(), response.getHeaders(), body);
    }

    static <T> void validateServiceClass(Class<T> service) {
        if (!service.isInterface()) {
            throw new IllegalArgumentException("Only interface endpoint definitions are supported.");
        } else if (service.getInterfaces().length > 0) {
            throw new IllegalArgumentException("Interface definitions must not extend other interfaces.");
        }
    }

    static class SynchronousExecutor implements Executor {
        SynchronousExecutor() {
        }

        public void execute(Runnable runnable) {
            runnable.run();
        }
    }

    private Utils() {
    }
}
