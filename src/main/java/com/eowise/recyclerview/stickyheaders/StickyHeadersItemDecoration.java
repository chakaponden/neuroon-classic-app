package com.eowise.recyclerview.stickyheaders;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class StickyHeadersItemDecoration extends RecyclerView.ItemDecoration {
    private final AdapterDataObserver adapterDataObserver;
    private DrawOrder drawOrder;
    /* access modifiers changed from: private */
    public final HeaderStore headerStore;
    private boolean overlay;

    public StickyHeadersItemDecoration(HeaderStore headerStore2) {
        this(headerStore2, false);
    }

    public StickyHeadersItemDecoration(HeaderStore headerStore2, boolean overlay2) {
        this(headerStore2, overlay2, DrawOrder.OverItems);
    }

    public StickyHeadersItemDecoration(HeaderStore headerStore2, boolean overlay2, DrawOrder drawOrder2) {
        this.overlay = overlay2;
        this.drawOrder = drawOrder2;
        this.headerStore = headerStore2;
        this.adapterDataObserver = new AdapterDataObserver();
    }

    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (this.drawOrder == DrawOrder.UnderItems) {
            drawHeaders(c, parent, state);
        }
    }

    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (this.drawOrder == DrawOrder.OverItems) {
            drawHeaders(c, parent, state);
        }
    }

    private void drawHeaders(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        RecyclerView.LayoutManager lm = parent.getLayoutManager();
        Float lastY = null;
        for (int i = childCount - 1; i >= 0; i--) {
            View child = parent.getChildAt(i);
            RecyclerView.ViewHolder holder = parent.getChildViewHolder(child);
            if (!((RecyclerView.LayoutParams) child.getLayoutParams()).isItemRemoved()) {
                float translationY = ViewCompat.getTranslationY(child);
                if ((i == 0 && this.headerStore.isSticky()) || this.headerStore.isHeader(holder)) {
                    View header = this.headerStore.getHeaderViewByItem(holder);
                    int headerHeight = this.headerStore.getHeaderHeight(holder);
                    float y = getHeaderY(child, lm) + translationY;
                    if (this.headerStore.isSticky() && lastY != null && lastY.floatValue() < ((float) headerHeight) + y) {
                        y = lastY.floatValue() - ((float) headerHeight);
                    }
                    c.save();
                    c.translate(0.0f, y);
                    header.draw(c);
                    c.restore();
                    lastY = Float.valueOf(y);
                }
            }
        }
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.ViewHolder holder = parent.getChildViewHolder(view);
        boolean isHeader = ((RecyclerView.LayoutParams) view.getLayoutParams()).isItemRemoved() ? this.headerStore.wasHeader(holder) : this.headerStore.isHeader(holder);
        if (this.overlay || !isHeader) {
            outRect.set(0, 0, 0, 0);
        } else {
            outRect.set(0, this.headerStore.getHeaderHeight(holder), 0, 0);
        }
    }

    public void registerAdapterDataObserver(RecyclerView.Adapter adapter) {
        adapter.registerAdapterDataObserver(this.adapterDataObserver);
    }

    private float getHeaderY(View item, RecyclerView.LayoutManager lm) {
        if (!this.headerStore.isSticky() || lm.getDecoratedTop(item) >= 0) {
            return (float) lm.getDecoratedTop(item);
        }
        return 0.0f;
    }

    private class AdapterDataObserver extends RecyclerView.AdapterDataObserver {
        public AdapterDataObserver() {
        }

        public void onChanged() {
            StickyHeadersItemDecoration.this.headerStore.clear();
        }

        public void onItemRangeRemoved(int positionStart, int itemCount) {
            StickyHeadersItemDecoration.this.headerStore.onItemRangeRemoved(positionStart, itemCount);
        }

        public void onItemRangeInserted(int positionStart, int itemCount) {
            StickyHeadersItemDecoration.this.headerStore.onItemRangeInserted(positionStart, itemCount);
        }
    }
}
