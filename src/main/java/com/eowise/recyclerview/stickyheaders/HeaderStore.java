package com.eowise.recyclerview.stickyheaders;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerViewHelper;
import android.view.View;
import java.util.ArrayList;
import java.util.HashMap;

public class HeaderStore {
    private final StickyHeadersAdapter adapter;
    private final HashMap<Long, Integer> headersHeightsByItemsIds = new HashMap<>();
    private final HashMap<Long, View> headersViewByHeadersIds = new HashMap<>();
    private final ArrayList<Boolean> isHeaderByItemPosition = new ArrayList<>();
    private boolean isSticky;
    private final RecyclerView parent;
    private final HashMap<Long, Boolean> wasHeaderByItemId = new HashMap<>();

    public HeaderStore(RecyclerView parent2, StickyHeadersAdapter adapter2, boolean isSticky2) {
        this.parent = parent2;
        this.adapter = adapter2;
        this.isSticky = isSticky2;
    }

    public View getHeaderViewByItem(RecyclerView.ViewHolder itemHolder) {
        int itemPosition = RecyclerViewHelper.convertPreLayoutPositionToPostLayout(this.parent, itemHolder.getPosition());
        if (itemPosition == -1) {
            return null;
        }
        long headerId = this.adapter.getHeaderId(itemPosition);
        if (!this.headersViewByHeadersIds.containsKey(Long.valueOf(headerId))) {
            RecyclerView.ViewHolder headerViewHolder = this.adapter.onCreateViewHolder(this.parent);
            this.adapter.onBindViewHolder(headerViewHolder, itemPosition);
            layoutHeader(headerViewHolder.itemView);
            this.headersViewByHeadersIds.put(Long.valueOf(headerId), headerViewHolder.itemView);
        }
        return this.headersViewByHeadersIds.get(Long.valueOf(headerId));
    }

    public long getHeaderId(int itemPosition) {
        return this.adapter.getHeaderId(itemPosition);
    }

    public int getHeaderHeight(RecyclerView.ViewHolder itemHolder) {
        if (!this.headersHeightsByItemsIds.containsKey(Long.valueOf(itemHolder.getItemId()))) {
            this.headersHeightsByItemsIds.put(Long.valueOf(itemHolder.getItemId()), Integer.valueOf(getHeaderViewByItem(itemHolder).getMeasuredHeight()));
        }
        return this.headersHeightsByItemsIds.get(Long.valueOf(itemHolder.getItemId())).intValue();
    }

    public boolean isHeader(RecyclerView.ViewHolder itemHolder) {
        boolean z = false;
        int itemPosition = RecyclerViewHelper.convertPreLayoutPositionToPostLayout(this.parent, itemHolder.getPosition());
        if (this.isHeaderByItemPosition.size() < itemPosition) {
            for (int i = 0; i < itemPosition; i++) {
                this.isHeaderByItemPosition.add((Object) null);
            }
        }
        if (this.isHeaderByItemPosition.size() <= itemPosition) {
            ArrayList<Boolean> arrayList = this.isHeaderByItemPosition;
            if (itemPosition == 0 || this.adapter.getHeaderId(itemPosition) != this.adapter.getHeaderId(itemPosition - 1)) {
                z = true;
            }
            arrayList.add(itemPosition, Boolean.valueOf(z));
        } else if (this.isHeaderByItemPosition.get(itemPosition) == null) {
            ArrayList<Boolean> arrayList2 = this.isHeaderByItemPosition;
            if (itemPosition == 0 || this.adapter.getHeaderId(itemPosition) != this.adapter.getHeaderId(itemPosition - 1)) {
                z = true;
            }
            arrayList2.set(itemPosition, Boolean.valueOf(z));
        }
        return this.isHeaderByItemPosition.get(itemPosition).booleanValue();
    }

    public boolean wasHeader(RecyclerView.ViewHolder itemHolder) {
        boolean z = false;
        if (!this.wasHeaderByItemId.containsKey(Long.valueOf(itemHolder.getItemId()))) {
            int itemPosition = RecyclerViewHelper.convertPreLayoutPositionToPostLayout(this.parent, itemHolder.getPosition());
            if (itemPosition == -1) {
                return false;
            }
            HashMap<Long, Boolean> hashMap = this.wasHeaderByItemId;
            Long valueOf = Long.valueOf(itemHolder.getItemId());
            if (itemPosition == 0 || this.adapter.getHeaderId(itemPosition) != this.adapter.getHeaderId(itemPosition - 1)) {
                z = true;
            }
            hashMap.put(valueOf, Boolean.valueOf(z));
        }
        return this.wasHeaderByItemId.get(Long.valueOf(itemHolder.getItemId())).booleanValue();
    }

    public boolean isSticky() {
        return this.isSticky;
    }

    public void onItemRangeRemoved(int positionStart, int itemCount) {
        if (this.isHeaderByItemPosition.size() > positionStart + itemCount) {
            for (int i = 0; i < itemCount; i++) {
                RecyclerView.ViewHolder holder = this.parent.findViewHolderForPosition(positionStart + i);
                if (holder != null) {
                    this.wasHeaderByItemId.put(Long.valueOf(holder.getItemId()), this.isHeaderByItemPosition.get(positionStart + i));
                }
            }
            this.isHeaderByItemPosition.set(positionStart + itemCount, (Object) null);
            for (int i2 = 0; i2 < itemCount; i2++) {
                this.isHeaderByItemPosition.remove(positionStart);
            }
        }
    }

    public void onItemRangeInserted(int positionStart, int itemCount) {
        if (this.isHeaderByItemPosition.size() > positionStart) {
            for (int i = 0; i < itemCount; i++) {
                this.isHeaderByItemPosition.add(positionStart, (Object) null);
            }
        }
        if (this.isHeaderByItemPosition.size() > positionStart + itemCount) {
            this.isHeaderByItemPosition.set(positionStart + itemCount, (Object) null);
        }
    }

    public void clear() {
        this.headersViewByHeadersIds.clear();
        this.isHeaderByItemPosition.clear();
        this.wasHeaderByItemId.clear();
    }

    private void layoutHeader(View header) {
        header.measure(View.MeasureSpec.makeMeasureSpec(this.parent.getWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(0, 0));
        header.layout(0, 0, header.getMeasuredWidth(), header.getMeasuredHeight());
    }
}
