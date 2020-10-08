package io.intercom.android.sdk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

public class ConversationListView extends ListView implements AbsListView.OnScrollListener {
    private OnBottomReachedListener bottomListener;
    private boolean isAtBottom;

    public interface OnBottomReachedListener {
        void onBottomReached();
    }

    public ConversationListView(Context context) {
        super(context);
        this.isAtBottom = false;
        setOnScrollListener(this);
    }

    public ConversationListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ConversationListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.isAtBottom = false;
        setOnScrollListener(this);
    }

    public void setOnBottomReachedListener(OnBottomReachedListener bottomListener2) {
        this.bottomListener = bottomListener2;
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.isAtBottom = false;
        if (totalItemCount > 0 && firstVisibleItem + visibleItemCount >= totalItemCount) {
            if (this.bottomListener != null) {
                this.bottomListener.onBottomReached();
            }
            this.isAtBottom = true;
        }
    }

    public boolean isAtBottom() {
        return this.isAtBottom;
    }
}
