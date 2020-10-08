package com.eowise.recyclerview.stickyheaders;

import android.support.v7.widget.RecyclerView;

public class StickyHeadersBuilder {
    private RecyclerView.Adapter adapter;
    private DrawOrder drawOrder = DrawOrder.OverItems;
    private OnHeaderClickListener headerClickListener;
    private StickyHeadersAdapter headersAdapter;
    private boolean isSticky = true;
    private boolean overlay;
    private RecyclerView recyclerView;

    public StickyHeadersBuilder setRecyclerView(RecyclerView recyclerView2) {
        this.recyclerView = recyclerView2;
        return this;
    }

    public StickyHeadersBuilder setStickyHeadersAdapter(StickyHeadersAdapter adapter2) {
        return setStickyHeadersAdapter(adapter2, false);
    }

    public StickyHeadersBuilder setStickyHeadersAdapter(StickyHeadersAdapter adapter2, boolean overlay2) {
        this.headersAdapter = adapter2;
        this.overlay = overlay2;
        return this;
    }

    public StickyHeadersBuilder setAdapter(RecyclerView.Adapter adapter2) {
        if (!adapter2.hasStableIds()) {
            throw new IllegalArgumentException("Adapter must have stable ids");
        }
        this.adapter = adapter2;
        return this;
    }

    public StickyHeadersBuilder setOnHeaderClickListener(OnHeaderClickListener headerClickListener2) {
        this.headerClickListener = headerClickListener2;
        return this;
    }

    public StickyHeadersBuilder setSticky(boolean isSticky2) {
        this.isSticky = isSticky2;
        return this;
    }

    public StickyHeadersBuilder setDrawOrder(DrawOrder drawOrder2) {
        this.drawOrder = drawOrder2;
        return this;
    }

    public StickyHeadersItemDecoration build() {
        HeaderStore store = new HeaderStore(this.recyclerView, this.headersAdapter, this.isSticky);
        StickyHeadersItemDecoration decoration = new StickyHeadersItemDecoration(store, this.overlay, this.drawOrder);
        decoration.registerAdapterDataObserver(this.adapter);
        if (this.headerClickListener != null) {
            StickyHeadersTouchListener touchListener = new StickyHeadersTouchListener(this.recyclerView, store);
            touchListener.setListener(this.headerClickListener);
            this.recyclerView.addOnItemTouchListener(touchListener);
        }
        return decoration;
    }
}
