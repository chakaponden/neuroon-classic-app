package io.intercom.android.sdk.blocks.models;

import android.os.Parcel;
import android.os.Parcelable;
import io.intercom.android.sdk.blocks.BlockAlignment;
import io.intercom.android.sdk.blocks.BlockType;
import java.util.ArrayList;
import java.util.List;

public class Block implements Parcelable {
    public static final Parcelable.Creator<Block> CREATOR = new Parcelable.Creator<Block>() {
        public Block createFromParcel(Parcel in) {
            return new Block(in);
        }

        public Block[] newArray(int size) {
            return new Block[size];
        }
    };
    private final BlockAlignment align;
    private final List<BlockAttachment> attachments;
    private final String embedUrl;
    private final int height;
    private final String id;
    private final List<String> items;
    private final String language;
    private final String linkUrl;
    private final String provider;
    private final String text;
    private final String trackingUrl;
    private final BlockType type;
    private final String url;
    private final String username;
    private final int width;

    public Block() {
        this(new Builder());
    }

    private Block(Builder builder) {
        this.type = BlockType.typeValueOf(builder.type);
        this.text = builder.text == null ? "" : builder.text;
        this.language = builder.language == null ? "" : builder.language;
        this.url = builder.url == null ? "" : builder.url;
        this.linkUrl = builder.linkUrl == null ? "" : builder.linkUrl;
        this.embedUrl = builder.embedUrl == null ? "" : builder.embedUrl;
        this.trackingUrl = builder.trackingUrl == null ? "" : builder.trackingUrl;
        this.username = builder.username == null ? "" : builder.username;
        this.provider = builder.provider == null ? "" : builder.provider;
        this.id = builder.id == null ? "" : builder.id;
        this.align = BlockAlignment.alignValueOf(builder.align);
        this.width = builder.width;
        this.height = builder.height;
        this.attachments = new ArrayList();
        if (builder.attachments != null) {
            for (BlockAttachment item : builder.attachments) {
                List<BlockAttachment> list = this.attachments;
                if (item == null) {
                    item = new BlockAttachment();
                }
                list.add(item);
            }
        }
        this.items = new ArrayList();
        if (builder.items != null) {
            for (String item2 : builder.items) {
                List<String> list2 = this.items;
                if (item2 == null) {
                    item2 = "";
                }
                list2.add(item2);
            }
        }
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public String align;
        /* access modifiers changed from: private */
        public List<BlockAttachment> attachments;
        /* access modifiers changed from: private */
        public String embedUrl;
        /* access modifiers changed from: private */
        public int height;
        /* access modifiers changed from: private */
        public String id;
        /* access modifiers changed from: private */
        public List<String> items;
        /* access modifiers changed from: private */
        public String language;
        /* access modifiers changed from: private */
        public String linkUrl;
        /* access modifiers changed from: private */
        public String provider;
        /* access modifiers changed from: private */
        public String text;
        /* access modifiers changed from: private */
        public String trackingUrl;
        /* access modifiers changed from: private */
        public String type;
        /* access modifiers changed from: private */
        public String url;
        /* access modifiers changed from: private */
        public String username;
        /* access modifiers changed from: private */
        public int width;

        public Builder withText(String text2) {
            this.text = text2;
            return this;
        }

        public Builder withType(String type2) {
            this.type = type2;
            return this;
        }

        public Builder withUrl(String url2) {
            this.url = url2;
            return this;
        }

        public Builder withAlign(String align2) {
            this.align = align2;
            return this;
        }

        public Builder withHeight(int height2) {
            this.height = height2;
            return this;
        }

        public Builder withWidth(int width2) {
            this.width = width2;
            return this;
        }

        public Builder withItems(List<String> items2) {
            this.items = items2;
            return this;
        }

        public Builder withAttachments(List<BlockAttachment> attachments2) {
            this.attachments = attachments2;
            return this;
        }

        public Block build() {
            return new Block(this);
        }
    }

    public BlockType getType() {
        return this.type;
    }

    public String getText() {
        return this.text;
    }

    public String getLanguage() {
        return this.language;
    }

    public String getUrl() {
        return this.url;
    }

    public String getLinkUrl() {
        return this.linkUrl;
    }

    public String getEmbedUrl() {
        return this.embedUrl;
    }

    public String getTrackingUrl() {
        return this.trackingUrl;
    }

    public String getUsername() {
        return this.username;
    }

    public String getProvider() {
        return this.provider;
    }

    public String getId() {
        return this.id;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public List<String> getItems() {
        return this.items;
    }

    public List<BlockAttachment> getAttachments() {
        return this.attachments;
    }

    public BlockAlignment getAlign() {
        return this.align;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Block block = (Block) o;
        if (this.width != block.width || this.height != block.height) {
            return false;
        }
        if (this.attachments != null) {
            if (!this.attachments.equals(block.attachments)) {
                return false;
            }
        } else if (block.attachments != null) {
            return false;
        }
        if (this.items != null) {
            if (!this.items.equals(block.items)) {
                return false;
            }
        } else if (block.items != null) {
            return false;
        }
        if (this.type != block.type || this.align != block.align) {
            return false;
        }
        if (this.text != null) {
            if (!this.text.equals(block.text)) {
                return false;
            }
        } else if (block.text != null) {
            return false;
        }
        if (this.language != null) {
            if (!this.language.equals(block.language)) {
                return false;
            }
        } else if (block.language != null) {
            return false;
        }
        if (this.url != null) {
            if (!this.url.equals(block.url)) {
                return false;
            }
        } else if (block.url != null) {
            return false;
        }
        if (this.linkUrl != null) {
            if (!this.linkUrl.equals(block.linkUrl)) {
                return false;
            }
        } else if (block.linkUrl != null) {
            return false;
        }
        if (this.embedUrl != null) {
            if (!this.embedUrl.equals(block.embedUrl)) {
                return false;
            }
        } else if (block.embedUrl != null) {
            return false;
        }
        if (this.trackingUrl != null) {
            if (!this.trackingUrl.equals(block.trackingUrl)) {
                return false;
            }
        } else if (block.trackingUrl != null) {
            return false;
        }
        if (this.username != null) {
            if (!this.username.equals(block.username)) {
                return false;
            }
        } else if (block.username != null) {
            return false;
        }
        if (this.provider != null) {
            if (!this.provider.equals(block.provider)) {
                return false;
            }
        } else if (block.provider != null) {
            return false;
        }
        if (this.id == null ? block.id != null : !this.id.equals(block.id)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12 = 0;
        if (this.attachments != null) {
            result = this.attachments.hashCode();
        } else {
            result = 0;
        }
        int i13 = result * 31;
        if (this.items != null) {
            i = this.items.hashCode();
        } else {
            i = 0;
        }
        int i14 = (i13 + i) * 31;
        if (this.type != null) {
            i2 = this.type.hashCode();
        } else {
            i2 = 0;
        }
        int i15 = (i14 + i2) * 31;
        if (this.align != null) {
            i3 = this.align.hashCode();
        } else {
            i3 = 0;
        }
        int i16 = (i15 + i3) * 31;
        if (this.text != null) {
            i4 = this.text.hashCode();
        } else {
            i4 = 0;
        }
        int i17 = (i16 + i4) * 31;
        if (this.language != null) {
            i5 = this.language.hashCode();
        } else {
            i5 = 0;
        }
        int i18 = (i17 + i5) * 31;
        if (this.url != null) {
            i6 = this.url.hashCode();
        } else {
            i6 = 0;
        }
        int i19 = (i18 + i6) * 31;
        if (this.linkUrl != null) {
            i7 = this.linkUrl.hashCode();
        } else {
            i7 = 0;
        }
        int i20 = (i19 + i7) * 31;
        if (this.embedUrl != null) {
            i8 = this.embedUrl.hashCode();
        } else {
            i8 = 0;
        }
        int i21 = (i20 + i8) * 31;
        if (this.trackingUrl != null) {
            i9 = this.trackingUrl.hashCode();
        } else {
            i9 = 0;
        }
        int i22 = (i21 + i9) * 31;
        if (this.username != null) {
            i10 = this.username.hashCode();
        } else {
            i10 = 0;
        }
        int i23 = (i22 + i10) * 31;
        if (this.provider != null) {
            i11 = this.provider.hashCode();
        } else {
            i11 = 0;
        }
        int i24 = (i23 + i11) * 31;
        if (this.id != null) {
            i12 = this.id.hashCode();
        }
        return ((((i24 + i12) * 31) + this.width) * 31) + this.height;
    }

    public int describeContents() {
        return 0;
    }

    protected Block(Parcel in) {
        this.type = BlockType.typeValueOf(in.readString());
        this.text = in.readString();
        this.language = in.readString();
        this.url = in.readString();
        this.linkUrl = in.readString();
        this.embedUrl = in.readString();
        this.trackingUrl = in.readString();
        this.username = in.readString();
        this.provider = in.readString();
        this.id = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
        this.align = BlockAlignment.alignValueOf(in.readString());
        this.attachments = new ArrayList();
        if (in.readByte() == 1) {
            in.readList(this.attachments, BlockAttachment.class.getClassLoader());
        }
        this.items = new ArrayList();
        if (in.readByte() == 1) {
            in.readList(this.items, String.class.getClassLoader());
        }
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type.name());
        dest.writeString(this.text);
        dest.writeString(this.language);
        dest.writeString(this.url);
        dest.writeString(this.linkUrl);
        dest.writeString(this.embedUrl);
        dest.writeString(this.trackingUrl);
        dest.writeString(this.username);
        dest.writeString(this.provider);
        dest.writeString(this.id);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeString(this.align.name());
        if (this.attachments == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeList(this.attachments);
        }
        if (this.items == null) {
            dest.writeByte((byte) 0);
            return;
        }
        dest.writeByte((byte) 1);
        dest.writeList(this.items);
    }
}
