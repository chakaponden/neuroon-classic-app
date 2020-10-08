package io.intercom.android.sdk.blocks;

import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import io.intercom.android.sdk.blocks.models.Block;
import java.util.Locale;

public enum BlockType {
    PARAGRAPH {
        public View generateView(BlocksViewHolder viewHolder, Block block, ViewGroup layout, boolean isFirstObject, boolean isLastObject) {
            return viewHolder.getParagraph().addParagraph(Html.fromHtml(block.getText()), block.getAlign(), isFirstObject, isLastObject, layout);
        }
    },
    HEADING {
        public View generateView(BlocksViewHolder viewHolder, Block block, ViewGroup layout, boolean isFirstObject, boolean isLastObject) {
            return viewHolder.getHeading().addHeading(Html.fromHtml(block.getText()), block.getAlign(), isFirstObject, isLastObject, layout);
        }
    },
    SUBHEADING {
        public View generateView(BlocksViewHolder viewHolder, Block block, ViewGroup layout, boolean isFirstObject, boolean isLastObject) {
            return viewHolder.getSubheading().addSubheading(Html.fromHtml(block.getText()), block.getAlign(), isFirstObject, isLastObject, layout);
        }
    },
    CODE {
        public View generateView(BlocksViewHolder viewHolder, Block block, ViewGroup layout, boolean isFirstObject, boolean isLastObject) {
            return viewHolder.getCode().addCode(Html.fromHtml(block.getText()), isFirstObject, isLastObject, layout);
        }
    },
    UNORDEREDLIST {
        public View generateView(BlocksViewHolder viewHolder, Block block, ViewGroup layout, boolean isFirstObject, boolean isLastObject) {
            return viewHolder.getUnorderedList().addUnorderedList(block.getItems(), isFirstObject, isLastObject, layout);
        }
    },
    ORDEREDLIST {
        public View generateView(BlocksViewHolder viewHolder, Block block, ViewGroup layout, boolean isFirstObject, boolean isLastObject) {
            return viewHolder.getOrderedList().addOrderedList(block.getItems(), isFirstObject, isLastObject, layout);
        }
    },
    ATTACHMENTLIST {
        public View generateView(BlocksViewHolder viewHolder, Block block, ViewGroup layout, boolean isFirstObject, boolean isLastObject) {
            return viewHolder.getAttachmentList().addAttachmentList(block.getAttachments(), isFirstObject, isLastObject, layout);
        }
    },
    IMAGE {
        public View generateView(BlocksViewHolder viewHolder, Block block, ViewGroup layout, boolean isFirstObject, boolean isLastObject) {
            return viewHolder.getImage().addImage(block.getUrl(), getUrl(block), block.getWidth(), block.getHeight(), block.getAlign(), isFirstObject, isLastObject, layout);
        }
    },
    LWR {
        public View generateView(BlocksViewHolder viewHolder, Block block, ViewGroup layout, boolean isFirstObject, boolean isLastObject) {
            return viewHolder.getLwr().addLWR(block.getText(), isFirstObject, isLastObject, layout);
        }
    },
    BUTTON {
        public View generateView(BlocksViewHolder viewHolder, Block block, ViewGroup layout, boolean isFirstObject, boolean isLastObject) {
            return viewHolder.getButton().addButton(block.getText(), getUrl(block), block.getAlign(), isFirstObject, isLastObject, layout);
        }
    },
    FACEBOOKLIKEBUTTON {
        public View generateView(BlocksViewHolder viewHolder, Block block, ViewGroup layout, boolean isFirstObject, boolean isLastObject) {
            return viewHolder.getFacebookButton().addFacebookButton(block.getUrl(), block.getAlign(), isFirstObject, isLastObject, layout);
        }
    },
    TWITTERFOLLOWBUTTON {
        public View generateView(BlocksViewHolder viewHolder, Block block, ViewGroup layout, boolean isFirstObject, boolean isLastObject) {
            return viewHolder.getTwitterButton().addTwitterButton("http://twitter.com/" + block.getUsername(), block.getAlign(), isFirstObject, isLastObject, layout);
        }
    },
    VIDEO {
        public View generateView(BlocksViewHolder viewHolder, Block block, ViewGroup layout, boolean isFirstObject, boolean isLastObject) {
            return viewHolder.getVideo().addVideo(block.getEmbedUrl(), VideoProvider.videoValueOf(block.getProvider()), block.getId(), isFirstObject, isLastObject, layout);
        }
    },
    LOCALIMAGE {
        public View generateView(BlocksViewHolder viewHolder, Block block, ViewGroup layout, boolean isFirstObject, boolean isLastObject) {
            return viewHolder.getLocalImage().addImage(block.getUrl(), block.getWidth(), block.getHeight(), block.getAlign(), isFirstObject, isLastObject, layout);
        }
    },
    LOCAL_ATTACHMENT {
        public View generateView(BlocksViewHolder viewHolder, Block block, ViewGroup layout, boolean isFirstObject, boolean isLastObject) {
            return viewHolder.getLocalAttachment().addAttachment(block.getAttachments().get(0), isFirstObject, isLastObject, layout);
        }
    },
    UNKNOWN {
        public View generateView(BlocksViewHolder viewHolder, Block block, ViewGroup layout, boolean isFirstObject, boolean isLastObject) {
            if (!block.getText().isEmpty()) {
                return PARAGRAPH.generateView(viewHolder, block, layout, isFirstObject, isLastObject);
            }
            return null;
        }
    };

    public abstract View generateView(BlocksViewHolder blocksViewHolder, Block block, ViewGroup viewGroup, boolean z, boolean z2);

    protected static String getUrl(Block block) {
        return block.getTrackingUrl().isEmpty() ? block.getLinkUrl() : block.getTrackingUrl();
    }

    public static BlockType typeValueOf(String type) {
        BlockType blockType = UNKNOWN;
        try {
            return valueOf(type.toUpperCase(Locale.ENGLISH));
        } catch (IllegalArgumentException | NullPointerException e) {
            return blockType;
        }
    }
}
