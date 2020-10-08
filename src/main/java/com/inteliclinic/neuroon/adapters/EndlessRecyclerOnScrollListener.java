package com.inteliclinic.neuroon.adapters;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    private int mCurrentPage = 1;
    private int mFirstVisibleItem;
    private LinearLayoutManager mLinearLayoutManager;
    private boolean mLoading = true;
    private int mPreviousTotal = 0;
    private int mTotalItemCount;
    private int mVisibleItemCount;
    private int mVisibleThreshold = 20;

    public abstract void onLoadMore(int i);

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        this.mVisibleItemCount = recyclerView.getChildCount();
        this.mTotalItemCount = this.mLinearLayoutManager.getItemCount();
        this.mFirstVisibleItem = this.mLinearLayoutManager.findFirstVisibleItemPosition();
        if (this.mLoading && this.mTotalItemCount > this.mPreviousTotal) {
            this.mLoading = false;
            this.mPreviousTotal = this.mTotalItemCount;
        }
        if (!this.mLoading && this.mTotalItemCount - this.mVisibleItemCount <= this.mFirstVisibleItem + this.mVisibleThreshold) {
            this.mCurrentPage++;
            onLoadMore(this.mCurrentPage);
            this.mLoading = true;
        }
    }
}
