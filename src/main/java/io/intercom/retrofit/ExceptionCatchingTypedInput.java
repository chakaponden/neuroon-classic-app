package io.intercom.retrofit;

import io.intercom.retrofit.mime.TypedInput;
import java.io.IOException;
import java.io.InputStream;

class ExceptionCatchingTypedInput implements TypedInput {
    private final TypedInput delegate;
    private final ExceptionCatchingInputStream delegateStream;

    ExceptionCatchingTypedInput(TypedInput delegate2) throws IOException {
        this.delegate = delegate2;
        this.delegateStream = new ExceptionCatchingInputStream(delegate2.in());
    }

    public String mimeType() {
        return this.delegate.mimeType();
    }

    public long length() {
        return this.delegate.length();
    }

    public InputStream in() throws IOException {
        return this.delegateStream;
    }

    /* access modifiers changed from: package-private */
    public IOException getThrownException() {
        return this.delegateStream.thrownException;
    }

    /* access modifiers changed from: package-private */
    public boolean threwException() {
        return this.delegateStream.thrownException != null;
    }

    private static class ExceptionCatchingInputStream extends InputStream {
        private final InputStream delegate;
        /* access modifiers changed from: private */
        public IOException thrownException;

        ExceptionCatchingInputStream(InputStream delegate2) {
            this.delegate = delegate2;
        }

        public int read() throws IOException {
            try {
                return this.delegate.read();
            } catch (IOException e) {
                this.thrownException = e;
                throw e;
            }
        }

        public int read(byte[] buffer) throws IOException {
            try {
                return this.delegate.read(buffer);
            } catch (IOException e) {
                this.thrownException = e;
                throw e;
            }
        }

        public int read(byte[] buffer, int offset, int length) throws IOException {
            try {
                return this.delegate.read(buffer, offset, length);
            } catch (IOException e) {
                this.thrownException = e;
                throw e;
            }
        }

        public long skip(long byteCount) throws IOException {
            try {
                return this.delegate.skip(byteCount);
            } catch (IOException e) {
                this.thrownException = e;
                throw e;
            }
        }

        public int available() throws IOException {
            try {
                return this.delegate.available();
            } catch (IOException e) {
                this.thrownException = e;
                throw e;
            }
        }

        public void close() throws IOException {
            try {
                this.delegate.close();
            } catch (IOException e) {
                this.thrownException = e;
                throw e;
            }
        }

        public synchronized void mark(int readLimit) {
            this.delegate.mark(readLimit);
        }

        public synchronized void reset() throws IOException {
            try {
                this.delegate.reset();
            } catch (IOException e) {
                this.thrownException = e;
                throw e;
            }
        }

        public boolean markSupported() {
            return this.delegate.markSupported();
        }
    }
}
