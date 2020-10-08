package io.intercom.com.squareup.okhttp.internal.framed;

import io.intercom.com.squareup.okhttp.Protocol;
import io.intercom.okio.BufferedSink;
import io.intercom.okio.BufferedSource;

public interface Variant {
    Protocol getProtocol();

    FrameReader newReader(BufferedSource bufferedSource, boolean z);

    FrameWriter newWriter(BufferedSink bufferedSink, boolean z);
}
