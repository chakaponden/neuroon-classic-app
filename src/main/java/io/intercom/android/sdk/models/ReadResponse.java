package io.intercom.android.sdk.models;

import io.intercom.android.sdk.models.BaseResponse;

public class ReadResponse extends BaseResponse {
    private ReadResponse(Builder builder) {
        super(builder);
    }

    public static final class Builder extends BaseResponse.Builder {
        public ReadResponse build() {
            return new ReadResponse(this);
        }
    }
}
