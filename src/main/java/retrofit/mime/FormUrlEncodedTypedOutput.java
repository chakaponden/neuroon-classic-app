package retrofit.mime;

import io.fabric.sdk.android.services.network.HttpRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

public final class FormUrlEncodedTypedOutput implements TypedOutput {
    final ByteArrayOutputStream content = new ByteArrayOutputStream();

    public void addField(String name, String value) {
        addField(name, true, value, true);
    }

    public void addField(String name, boolean encodeName, String value, boolean encodeValue) {
        if (name == null) {
            throw new NullPointerException("name");
        } else if (value == null) {
            throw new NullPointerException("value");
        } else {
            if (this.content.size() > 0) {
                this.content.write(38);
            }
            if (encodeName) {
                try {
                    name = URLEncoder.encode(name, HttpRequest.CHARSET_UTF8);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (encodeValue) {
                value = URLEncoder.encode(value, HttpRequest.CHARSET_UTF8);
            }
            this.content.write(name.getBytes(HttpRequest.CHARSET_UTF8));
            this.content.write(61);
            this.content.write(value.getBytes(HttpRequest.CHARSET_UTF8));
        }
    }

    public String fileName() {
        return null;
    }

    public String mimeType() {
        return "application/x-www-form-urlencoded; charset=UTF-8";
    }

    public long length() {
        return (long) this.content.size();
    }

    public void writeTo(OutputStream out) throws IOException {
        out.write(this.content.toByteArray());
    }
}
