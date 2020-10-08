package io.intercom.android.sdk.blocks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import io.intercom.android.sdk.blocks.models.Block;
import java.util.List;

public class Blocks {
    private final LayoutInflater inflater;

    public Blocks(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public LinearLayout createBlocks(List<Block> body, BlocksViewHolder viewHolder) {
        boolean isFirstObject;
        boolean isLastObject;
        LinearLayout layout = (LinearLayout) this.inflater.inflate(viewHolder.getLayout(), (ViewGroup) null);
        if (body != null) {
            for (int i = 0; i < body.size(); i++) {
                Block block = body.get(i);
                if (i == 0) {
                    isFirstObject = true;
                } else {
                    isFirstObject = false;
                }
                if (i == body.size() - 1) {
                    isLastObject = true;
                } else {
                    isLastObject = false;
                }
                View view = block.getType().generateView(viewHolder, block, layout, isFirstObject, isLastObject);
                if (view != null) {
                    layout.addView(view);
                }
            }
        }
        return layout;
    }
}
