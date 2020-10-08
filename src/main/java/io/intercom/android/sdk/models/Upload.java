package io.intercom.android.sdk.models;

public class Upload {
    private final String acl;
    private final String awsAccessKey;
    private final String contentType;
    private final int id;
    private final String key;
    private final String policy;
    private final String publicUrl;
    private final String signature;
    private final String successActionStatus;
    private final String uploadDestination;

    public Upload() {
        this(new Builder());
    }

    private Upload(Builder builder) {
        this.id = builder.id;
        this.uploadDestination = builder.upload_destination == null ? "" : builder.upload_destination;
        this.publicUrl = builder.public_url == null ? "" : builder.public_url;
        this.key = builder.key == null ? "" : builder.key;
        this.awsAccessKey = builder.aws_access_key == null ? "" : builder.aws_access_key;
        this.acl = builder.acl == null ? "" : builder.acl;
        this.successActionStatus = builder.success_action_status == null ? "" : builder.success_action_status;
        this.contentType = builder.content_type == null ? "" : builder.content_type;
        this.policy = builder.policy == null ? "" : builder.policy;
        this.signature = builder.signature == null ? "" : builder.signature;
    }

    public int getId() {
        return this.id;
    }

    public String getAcl() {
        return this.acl;
    }

    public String getKey() {
        return this.key;
    }

    public String getPolicy() {
        return this.policy;
    }

    public String getSignature() {
        return this.signature;
    }

    public String getSuccessActionStatus() {
        return this.successActionStatus;
    }

    public String getContentType() {
        return this.contentType;
    }

    public String getAwsAccessKey() {
        return this.awsAccessKey;
    }

    public String getUploadDestination() {
        return this.uploadDestination;
    }

    public String getPublicUrl() {
        return this.publicUrl;
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public String acl;
        /* access modifiers changed from: private */
        public String aws_access_key;
        /* access modifiers changed from: private */
        public String content_type;
        /* access modifiers changed from: private */
        public int id;
        /* access modifiers changed from: private */
        public String key;
        /* access modifiers changed from: private */
        public String policy;
        /* access modifiers changed from: private */
        public String public_url;
        /* access modifiers changed from: private */
        public String signature;
        /* access modifiers changed from: private */
        public String success_action_status;
        /* access modifiers changed from: private */
        public String upload_destination;

        public Upload build() {
            return new Upload(this);
        }
    }
}
