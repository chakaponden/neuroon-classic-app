package com.eowise.recyclerview.stickyheaders;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

public interface StickyHeadersAdapter<HeaderViewHolder extends RecyclerView.ViewHolder> {
    long getHeaderId(int i);

    void onBindViewHolder(HeaderViewHolder headerviewholder, int i);

    HeaderViewHolder onCreateViewHolder(ViewGroup viewGroup);
}
