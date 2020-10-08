package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.text.TextUtilsCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.appcompat.R;
import android.support.v7.widget.ActivityChooserView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import java.lang.reflect.Method;

public class ListPopupWindow {
    private static final boolean DEBUG = false;
    private static final int EXPAND_LIST_TIMEOUT = 250;
    public static final int INPUT_METHOD_FROM_FOCUSABLE = 0;
    public static final int INPUT_METHOD_NEEDED = 1;
    public static final int INPUT_METHOD_NOT_NEEDED = 2;
    public static final int MATCH_PARENT = -1;
    public static final int POSITION_PROMPT_ABOVE = 0;
    public static final int POSITION_PROMPT_BELOW = 1;
    private static final String TAG = "ListPopupWindow";
    public static final int WRAP_CONTENT = -2;
    private static Method sClipToWindowEnabledMethod;
    private static Method sGetMaxAvailableHeightMethod;
    private ListAdapter mAdapter;
    private Context mContext;
    private boolean mDropDownAlwaysVisible;
    private View mDropDownAnchorView;
    private int mDropDownGravity;
    private int mDropDownHeight;
    private int mDropDownHorizontalOffset;
    /* access modifiers changed from: private */
    public DropDownListView mDropDownList;
    private Drawable mDropDownListHighlight;
    private int mDropDownVerticalOffset;
    private boolean mDropDownVerticalOffsetSet;
    private int mDropDownWidth;
    private int mDropDownWindowLayoutType;
    private boolean mForceIgnoreOutsideTouch;
    /* access modifiers changed from: private */
    public final Handler mHandler;
    private final ListSelectorHider mHideSelector;
    private AdapterView.OnItemClickListener mItemClickListener;
    private AdapterView.OnItemSelectedListener mItemSelectedListener;
    private int mLayoutDirection;
    int mListItemExpandMaximum;
    private boolean mModal;
    private DataSetObserver mObserver;
    /* access modifiers changed from: private */
    public PopupWindow mPopup;
    private int mPromptPosition;
    private View mPromptView;
    /* access modifiers changed from: private */
    public final ResizePopupRunnable mResizePopupRunnable;
    private final PopupScrollListener mScrollListener;
    private Runnable mShowDropDownRunnable;
    private Rect mTempRect;
    private final PopupTouchInterceptor mTouchInterceptor;

    static {
        Class<PopupWindow> cls = PopupWindow.class;
        try {
            sClipToWindowEnabledMethod = cls.getDeclaredMethod("setClipToScreenEnabled", new Class[]{Boolean.TYPE});
        } catch (NoSuchMethodException e) {
            Log.i(TAG, "Could not find method setClipToScreenEnabled() on PopupWindow. Oh well.");
        }
        Class<PopupWindow> cls2 = PopupWindow.class;
        try {
            sGetMaxAvailableHeightMethod = cls2.getDeclaredMethod("getMaxAvailableHeight", new Class[]{View.class, Integer.TYPE, Boolean.TYPE});
        } catch (NoSuchMethodException e2) {
            Log.i(TAG, "Could not find method getMaxAvailableHeight(View, int, boolean) on PopupWindow. Oh well.");
        }
    }

    public ListPopupWindow(Context context) {
        this(context, (AttributeSet) null, R.attr.listPopupWindowStyle);
    }

    public ListPopupWindow(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.listPopupWindowStyle);
    }

    public ListPopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ListPopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this.mDropDownHeight = -2;
        this.mDropDownWidth = -2;
        this.mDropDownWindowLayoutType = 1002;
        this.mDropDownGravity = 0;
        this.mDropDownAlwaysVisible = false;
        this.mForceIgnoreOutsideTouch = false;
        this.mListItemExpandMaximum = ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
        this.mPromptPosition = 0;
        this.mResizePopupRunnable = new ResizePopupRunnable();
        this.mTouchInterceptor = new PopupTouchInterceptor();
        this.mScrollListener = new PopupScrollListener();
        this.mHideSelector = new ListSelectorHider();
        this.mTempRect = new Rect();
        this.mContext = context;
        this.mHandler = new Handler(context.getMainLooper());
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ListPopupWindow, defStyleAttr, defStyleRes);
        this.mDropDownHorizontalOffset = a.getDimensionPixelOffset(R.styleable.ListPopupWindow_android_dropDownHorizontalOffset, 0);
        this.mDropDownVerticalOffset = a.getDimensionPixelOffset(R.styleable.ListPopupWindow_android_dropDownVerticalOffset, 0);
        if (this.mDropDownVerticalOffset != 0) {
            this.mDropDownVerticalOffsetSet = true;
        }
        a.recycle();
        this.mPopup = new AppCompatPopupWindow(context, attrs, defStyleAttr);
        this.mPopup.setInputMethodMode(1);
        this.mLayoutDirection = TextUtilsCompat.getLayoutDirectionFromLocale(this.mContext.getResources().getConfiguration().locale);
    }

    public void setAdapter(ListAdapter adapter) {
        if (this.mObserver == null) {
            this.mObserver = new PopupDataSetObserver();
        } else if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(this.mObserver);
        }
        this.mAdapter = adapter;
        if (this.mAdapter != null) {
            adapter.registerDataSetObserver(this.mObserver);
        }
        if (this.mDropDownList != null) {
            this.mDropDownList.setAdapter(this.mAdapter);
        }
    }

    public void setPromptPosition(int position) {
        this.mPromptPosition = position;
    }

    public int getPromptPosition() {
        return this.mPromptPosition;
    }

    public void setModal(boolean modal) {
        this.mModal = modal;
        this.mPopup.setFocusable(modal);
    }

    public boolean isModal() {
        return this.mModal;
    }

    public void setForceIgnoreOutsideTouch(boolean forceIgnoreOutsideTouch) {
        this.mForceIgnoreOutsideTouch = forceIgnoreOutsideTouch;
    }

    public void setDropDownAlwaysVisible(boolean dropDownAlwaysVisible) {
        this.mDropDownAlwaysVisible = dropDownAlwaysVisible;
    }

    public boolean isDropDownAlwaysVisible() {
        return this.mDropDownAlwaysVisible;
    }

    public void setSoftInputMode(int mode) {
        this.mPopup.setSoftInputMode(mode);
    }

    public int getSoftInputMode() {
        return this.mPopup.getSoftInputMode();
    }

    public void setListSelector(Drawable selector) {
        this.mDropDownListHighlight = selector;
    }

    public Drawable getBackground() {
        return this.mPopup.getBackground();
    }

    public void setBackgroundDrawable(Drawable d) {
        this.mPopup.setBackgroundDrawable(d);
    }

    public void setAnimationStyle(int animationStyle) {
        this.mPopup.setAnimationStyle(animationStyle);
    }

    public int getAnimationStyle() {
        return this.mPopup.getAnimationStyle();
    }

    public View getAnchorView() {
        return this.mDropDownAnchorView;
    }

    public void setAnchorView(View anchor) {
        this.mDropDownAnchorView = anchor;
    }

    public int getHorizontalOffset() {
        return this.mDropDownHorizontalOffset;
    }

    public void setHorizontalOffset(int offset) {
        this.mDropDownHorizontalOffset = offset;
    }

    public int getVerticalOffset() {
        if (!this.mDropDownVerticalOffsetSet) {
            return 0;
        }
        return this.mDropDownVerticalOffset;
    }

    public void setVerticalOffset(int offset) {
        this.mDropDownVerticalOffset = offset;
        this.mDropDownVerticalOffsetSet = true;
    }

    public void setDropDownGravity(int gravity) {
        this.mDropDownGravity = gravity;
    }

    public int getWidth() {
        return this.mDropDownWidth;
    }

    public void setWidth(int width) {
        this.mDropDownWidth = width;
    }

    public void setContentWidth(int width) {
        Drawable popupBackground = this.mPopup.getBackground();
        if (popupBackground != null) {
            popupBackground.getPadding(this.mTempRect);
            this.mDropDownWidth = this.mTempRect.left + this.mTempRect.right + width;
            return;
        }
        setWidth(width);
    }

    public int getHeight() {
        return this.mDropDownHeight;
    }

    public void setHeight(int height) {
        this.mDropDownHeight = height;
    }

    public void setWindowLayoutType(int layoutType) {
        this.mDropDownWindowLayoutType = layoutType;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener clickListener) {
        this.mItemClickListener = clickListener;
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener selectedListener) {
        this.mItemSelectedListener = selectedListener;
    }

    public void setPromptView(View prompt) {
        boolean showing = isShowing();
        if (showing) {
            removePromptView();
        }
        this.mPromptView = prompt;
        if (showing) {
            show();
        }
    }

    public void postShow() {
        this.mHandler.post(this.mShowDropDownRunnable);
    }

    public void show() {
        int widthSpec;
        int heightSpec;
        int widthSpec2;
        int heightSpec2;
        int i;
        int i2;
        int i3;
        boolean z = true;
        boolean z2 = false;
        int i4 = -1;
        int height = buildDropDown();
        boolean noInputMethod = isInputMethodNotNeeded();
        PopupWindowCompat.setWindowLayoutType(this.mPopup, this.mDropDownWindowLayoutType);
        if (this.mPopup.isShowing()) {
            if (this.mDropDownWidth == -1) {
                widthSpec2 = -1;
            } else if (this.mDropDownWidth == -2) {
                widthSpec2 = getAnchorView().getWidth();
            } else {
                widthSpec2 = this.mDropDownWidth;
            }
            if (this.mDropDownHeight == -1) {
                if (noInputMethod) {
                    heightSpec2 = height;
                } else {
                    heightSpec2 = -1;
                }
                if (noInputMethod) {
                    PopupWindow popupWindow = this.mPopup;
                    if (this.mDropDownWidth == -1) {
                        i3 = -1;
                    } else {
                        i3 = 0;
                    }
                    popupWindow.setWidth(i3);
                    this.mPopup.setHeight(0);
                } else {
                    PopupWindow popupWindow2 = this.mPopup;
                    if (this.mDropDownWidth == -1) {
                        i2 = -1;
                    } else {
                        i2 = 0;
                    }
                    popupWindow2.setWidth(i2);
                    this.mPopup.setHeight(-1);
                }
            } else if (this.mDropDownHeight == -2) {
                heightSpec2 = height;
            } else {
                heightSpec2 = this.mDropDownHeight;
            }
            PopupWindow popupWindow3 = this.mPopup;
            if (!this.mForceIgnoreOutsideTouch && !this.mDropDownAlwaysVisible) {
                z2 = true;
            }
            popupWindow3.setOutsideTouchable(z2);
            PopupWindow popupWindow4 = this.mPopup;
            View anchorView = getAnchorView();
            int i5 = this.mDropDownHorizontalOffset;
            int i6 = this.mDropDownVerticalOffset;
            if (widthSpec2 < 0) {
                i = -1;
            } else {
                i = widthSpec2;
            }
            if (heightSpec2 >= 0) {
                i4 = heightSpec2;
            }
            popupWindow4.update(anchorView, i5, i6, i, i4);
            return;
        }
        if (this.mDropDownWidth == -1) {
            widthSpec = -1;
        } else if (this.mDropDownWidth == -2) {
            widthSpec = getAnchorView().getWidth();
        } else {
            widthSpec = this.mDropDownWidth;
        }
        if (this.mDropDownHeight == -1) {
            heightSpec = -1;
        } else if (this.mDropDownHeight == -2) {
            heightSpec = height;
        } else {
            heightSpec = this.mDropDownHeight;
        }
        this.mPopup.setWidth(widthSpec);
        this.mPopup.setHeight(heightSpec);
        setPopupClipToScreenEnabled(true);
        PopupWindow popupWindow5 = this.mPopup;
        if (this.mForceIgnoreOutsideTouch || this.mDropDownAlwaysVisible) {
            z = false;
        }
        popupWindow5.setOutsideTouchable(z);
        this.mPopup.setTouchInterceptor(this.mTouchInterceptor);
        PopupWindowCompat.showAsDropDown(this.mPopup, getAnchorView(), this.mDropDownHorizontalOffset, this.mDropDownVerticalOffset, this.mDropDownGravity);
        this.mDropDownList.setSelection(-1);
        if (!this.mModal || this.mDropDownList.isInTouchMode()) {
            clearListSelection();
        }
        if (!this.mModal) {
            this.mHandler.post(this.mHideSelector);
        }
    }

    public void dismiss() {
        this.mPopup.dismiss();
        removePromptView();
        this.mPopup.setContentView((View) null);
        this.mDropDownList = null;
        this.mHandler.removeCallbacks(this.mResizePopupRunnable);
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
        this.mPopup.setOnDismissListener(listener);
    }

    private void removePromptView() {
        if (this.mPromptView != null) {
            ViewParent parent = this.mPromptView.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(this.mPromptView);
            }
        }
    }

    public void setInputMethodMode(int mode) {
        this.mPopup.setInputMethodMode(mode);
    }

    public int getInputMethodMode() {
        return this.mPopup.getInputMethodMode();
    }

    public void setSelection(int position) {
        DropDownListView list = this.mDropDownList;
        if (isShowing() && list != null) {
            boolean unused = list.mListSelectionHidden = false;
            list.setSelection(position);
            if (Build.VERSION.SDK_INT >= 11 && list.getChoiceMode() != 0) {
                list.setItemChecked(position, true);
            }
        }
    }

    public void clearListSelection() {
        DropDownListView list = this.mDropDownList;
        if (list != null) {
            boolean unused = list.mListSelectionHidden = true;
            list.requestLayout();
        }
    }

    public boolean isShowing() {
        return this.mPopup.isShowing();
    }

    public boolean isInputMethodNotNeeded() {
        return this.mPopup.getInputMethodMode() == 2;
    }

    public boolean performItemClick(int position) {
        if (!isShowing()) {
            return false;
        }
        if (this.mItemClickListener != null) {
            DropDownListView list = this.mDropDownList;
            int i = position;
            this.mItemClickListener.onItemClick(list, list.getChildAt(position - list.getFirstVisiblePosition()), i, list.getAdapter().getItemId(position));
        }
        return true;
    }

    public Object getSelectedItem() {
        if (!isShowing()) {
            return null;
        }
        return this.mDropDownList.getSelectedItem();
    }

    public int getSelectedItemPosition() {
        if (!isShowing()) {
            return -1;
        }
        return this.mDropDownList.getSelectedItemPosition();
    }

    public long getSelectedItemId() {
        if (!isShowing()) {
            return Long.MIN_VALUE;
        }
        return this.mDropDownList.getSelectedItemId();
    }

    public View getSelectedView() {
        if (!isShowing()) {
            return null;
        }
        return this.mDropDownList.getSelectedView();
    }

    public ListView getListView() {
        return this.mDropDownList;
    }

    /* access modifiers changed from: package-private */
    public void setListItemExpandMax(int max) {
        this.mListItemExpandMaximum = max;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean below;
        if (isShowing() && keyCode != 62 && (this.mDropDownList.getSelectedItemPosition() >= 0 || !isConfirmKey(keyCode))) {
            int curIndex = this.mDropDownList.getSelectedItemPosition();
            if (!this.mPopup.isAboveAnchor()) {
                below = true;
            } else {
                below = false;
            }
            ListAdapter adapter = this.mAdapter;
            int firstItem = ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
            int lastItem = Integer.MIN_VALUE;
            if (adapter != null) {
                boolean allEnabled = adapter.areAllItemsEnabled();
                firstItem = allEnabled ? 0 : this.mDropDownList.lookForSelectablePosition(0, true);
                if (allEnabled) {
                    lastItem = adapter.getCount() - 1;
                } else {
                    lastItem = this.mDropDownList.lookForSelectablePosition(adapter.getCount() - 1, false);
                }
            }
            if ((!below || keyCode != 19 || curIndex > firstItem) && (below || keyCode != 20 || curIndex < lastItem)) {
                boolean unused = this.mDropDownList.mListSelectionHidden = false;
                if (this.mDropDownList.onKeyDown(keyCode, event)) {
                    this.mPopup.setInputMethodMode(2);
                    this.mDropDownList.requestFocusFromTouch();
                    show();
                    switch (keyCode) {
                        case 19:
                        case 20:
                        case 23:
                        case 66:
                            return true;
                    }
                } else if (!below || keyCode != 20) {
                    if (!below && keyCode == 19 && curIndex == firstItem) {
                        return true;
                    }
                } else if (curIndex == lastItem) {
                    return true;
                }
            } else {
                clearListSelection();
                this.mPopup.setInputMethodMode(1);
                show();
                return true;
            }
        }
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (!isShowing() || this.mDropDownList.getSelectedItemPosition() < 0) {
            return false;
        }
        boolean consumed = this.mDropDownList.onKeyUp(keyCode, event);
        if (!consumed || !isConfirmKey(keyCode)) {
            return consumed;
        }
        dismiss();
        return consumed;
    }

    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == 4 && isShowing()) {
            View anchorView = this.mDropDownAnchorView;
            if (event.getAction() == 0 && event.getRepeatCount() == 0) {
                KeyEvent.DispatcherState state = anchorView.getKeyDispatcherState();
                if (state == null) {
                    return true;
                }
                state.startTracking(event, this);
                return true;
            } else if (event.getAction() == 1) {
                KeyEvent.DispatcherState state2 = anchorView.getKeyDispatcherState();
                if (state2 != null) {
                    state2.handleUpEvent(event);
                }
                if (event.isTracking() && !event.isCanceled()) {
                    dismiss();
                    return true;
                }
            }
        }
        return false;
    }

    public View.OnTouchListener createDragToOpenListener(View src) {
        return new ForwardingListener(src) {
            public ListPopupWindow getPopup() {
                return ListPopupWindow.this;
            }
        };
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v2, resolved type: android.support.v7.widget.ListPopupWindow$DropDownListView} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v3, resolved type: android.support.v7.widget.ListPopupWindow$DropDownListView} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v0, resolved type: android.widget.LinearLayout} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v4, resolved type: android.support.v7.widget.ListPopupWindow$DropDownListView} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int buildDropDown() {
        /*
            r24 = this;
            r18 = 0
            r0 = r24
            android.support.v7.widget.ListPopupWindow$DropDownListView r2 = r0.mDropDownList
            if (r2 != 0) goto L_0x0172
            r0 = r24
            android.content.Context r9 = r0.mContext
            android.support.v7.widget.ListPopupWindow$2 r2 = new android.support.v7.widget.ListPopupWindow$2
            r0 = r24
            r2.<init>()
            r0 = r24
            r0.mShowDropDownRunnable = r2
            android.support.v7.widget.ListPopupWindow$DropDownListView r4 = new android.support.v7.widget.ListPopupWindow$DropDownListView
            r0 = r24
            boolean r2 = r0.mModal
            if (r2 != 0) goto L_0x0159
            r2 = 1
        L_0x0020:
            r4.<init>(r9, r2)
            r0 = r24
            r0.mDropDownList = r4
            r0 = r24
            android.graphics.drawable.Drawable r2 = r0.mDropDownListHighlight
            if (r2 == 0) goto L_0x0038
            r0 = r24
            android.support.v7.widget.ListPopupWindow$DropDownListView r2 = r0.mDropDownList
            r0 = r24
            android.graphics.drawable.Drawable r4 = r0.mDropDownListHighlight
            r2.setSelector(r4)
        L_0x0038:
            r0 = r24
            android.support.v7.widget.ListPopupWindow$DropDownListView r2 = r0.mDropDownList
            r0 = r24
            android.widget.ListAdapter r4 = r0.mAdapter
            r2.setAdapter(r4)
            r0 = r24
            android.support.v7.widget.ListPopupWindow$DropDownListView r2 = r0.mDropDownList
            r0 = r24
            android.widget.AdapterView$OnItemClickListener r4 = r0.mItemClickListener
            r2.setOnItemClickListener(r4)
            r0 = r24
            android.support.v7.widget.ListPopupWindow$DropDownListView r2 = r0.mDropDownList
            r4 = 1
            r2.setFocusable(r4)
            r0 = r24
            android.support.v7.widget.ListPopupWindow$DropDownListView r2 = r0.mDropDownList
            r4 = 1
            r2.setFocusableInTouchMode(r4)
            r0 = r24
            android.support.v7.widget.ListPopupWindow$DropDownListView r2 = r0.mDropDownList
            android.support.v7.widget.ListPopupWindow$3 r4 = new android.support.v7.widget.ListPopupWindow$3
            r0 = r24
            r4.<init>()
            r2.setOnItemSelectedListener(r4)
            r0 = r24
            android.support.v7.widget.ListPopupWindow$DropDownListView r2 = r0.mDropDownList
            r0 = r24
            android.support.v7.widget.ListPopupWindow$PopupScrollListener r4 = r0.mScrollListener
            r2.setOnScrollListener(r4)
            r0 = r24
            android.widget.AdapterView$OnItemSelectedListener r2 = r0.mItemSelectedListener
            if (r2 == 0) goto L_0x0088
            r0 = r24
            android.support.v7.widget.ListPopupWindow$DropDownListView r2 = r0.mDropDownList
            r0 = r24
            android.widget.AdapterView$OnItemSelectedListener r4 = r0.mItemSelectedListener
            r2.setOnItemSelectedListener(r4)
        L_0x0088:
            r0 = r24
            android.support.v7.widget.ListPopupWindow$DropDownListView r10 = r0.mDropDownList
            r0 = r24
            android.view.View r14 = r0.mPromptView
            if (r14 == 0) goto L_0x00f6
            android.widget.LinearLayout r12 = new android.widget.LinearLayout
            r12.<init>(r9)
            r2 = 1
            r12.setOrientation(r2)
            android.widget.LinearLayout$LayoutParams r13 = new android.widget.LinearLayout$LayoutParams
            r2 = -1
            r4 = 0
            r5 = 1065353216(0x3f800000, float:1.0)
            r13.<init>(r2, r4, r5)
            r0 = r24
            int r2 = r0.mPromptPosition
            switch(r2) {
                case 0: goto L_0x0164;
                case 1: goto L_0x015c;
                default: goto L_0x00ab;
            }
        L_0x00ab:
            java.lang.String r2 = "ListPopupWindow"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "Invalid hint position "
            java.lang.StringBuilder r4 = r4.append(r5)
            r0 = r24
            int r5 = r0.mPromptPosition
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r4 = r4.toString()
            android.util.Log.e(r2, r4)
        L_0x00c7:
            r0 = r24
            int r2 = r0.mDropDownWidth
            if (r2 < 0) goto L_0x016c
            r21 = -2147483648(0xffffffff80000000, float:-0.0)
            r0 = r24
            int r0 = r0.mDropDownWidth
            r22 = r0
        L_0x00d5:
            r0 = r22
            r1 = r21
            int r23 = android.view.View.MeasureSpec.makeMeasureSpec(r0, r1)
            r11 = 0
            r2 = 0
            r0 = r23
            r14.measure(r0, r2)
            android.view.ViewGroup$LayoutParams r13 = r14.getLayoutParams()
            android.widget.LinearLayout$LayoutParams r13 = (android.widget.LinearLayout.LayoutParams) r13
            int r2 = r14.getMeasuredHeight()
            int r4 = r13.topMargin
            int r2 = r2 + r4
            int r4 = r13.bottomMargin
            int r18 = r2 + r4
            r10 = r12
        L_0x00f6:
            r0 = r24
            android.widget.PopupWindow r2 = r0.mPopup
            r2.setContentView(r10)
        L_0x00fd:
            r19 = 0
            r0 = r24
            android.widget.PopupWindow r2 = r0.mPopup
            android.graphics.drawable.Drawable r8 = r2.getBackground()
            if (r8 == 0) goto L_0x0197
            r0 = r24
            android.graphics.Rect r2 = r0.mTempRect
            r8.getPadding(r2)
            r0 = r24
            android.graphics.Rect r2 = r0.mTempRect
            int r2 = r2.top
            r0 = r24
            android.graphics.Rect r4 = r0.mTempRect
            int r4 = r4.bottom
            int r19 = r2 + r4
            r0 = r24
            boolean r2 = r0.mDropDownVerticalOffsetSet
            if (r2 != 0) goto L_0x012f
            r0 = r24
            android.graphics.Rect r2 = r0.mTempRect
            int r2 = r2.top
            int r2 = -r2
            r0 = r24
            r0.mDropDownVerticalOffset = r2
        L_0x012f:
            r0 = r24
            android.widget.PopupWindow r2 = r0.mPopup
            int r2 = r2.getInputMethodMode()
            r4 = 2
            if (r2 != r4) goto L_0x019f
            r15 = 1
        L_0x013b:
            android.view.View r2 = r24.getAnchorView()
            r0 = r24
            int r4 = r0.mDropDownVerticalOffset
            r0 = r24
            int r17 = r0.getMaxAvailableHeight(r2, r4, r15)
            r0 = r24
            boolean r2 = r0.mDropDownAlwaysVisible
            if (r2 != 0) goto L_0x0156
            r0 = r24
            int r2 = r0.mDropDownHeight
            r4 = -1
            if (r2 != r4) goto L_0x01a1
        L_0x0156:
            int r2 = r17 + r19
        L_0x0158:
            return r2
        L_0x0159:
            r2 = 0
            goto L_0x0020
        L_0x015c:
            r12.addView(r10, r13)
            r12.addView(r14)
            goto L_0x00c7
        L_0x0164:
            r12.addView(r14)
            r12.addView(r10, r13)
            goto L_0x00c7
        L_0x016c:
            r21 = 0
            r22 = 0
            goto L_0x00d5
        L_0x0172:
            r0 = r24
            android.widget.PopupWindow r2 = r0.mPopup
            android.view.View r10 = r2.getContentView()
            android.view.ViewGroup r10 = (android.view.ViewGroup) r10
            r0 = r24
            android.view.View r0 = r0.mPromptView
            r20 = r0
            if (r20 == 0) goto L_0x00fd
            android.view.ViewGroup$LayoutParams r13 = r20.getLayoutParams()
            android.widget.LinearLayout$LayoutParams r13 = (android.widget.LinearLayout.LayoutParams) r13
            int r2 = r20.getMeasuredHeight()
            int r4 = r13.topMargin
            int r2 = r2 + r4
            int r4 = r13.bottomMargin
            int r18 = r2 + r4
            goto L_0x00fd
        L_0x0197:
            r0 = r24
            android.graphics.Rect r2 = r0.mTempRect
            r2.setEmpty()
            goto L_0x012f
        L_0x019f:
            r15 = 0
            goto L_0x013b
        L_0x01a1:
            r0 = r24
            int r2 = r0.mDropDownWidth
            switch(r2) {
                case -2: goto L_0x01c6;
                case -1: goto L_0x01e9;
                default: goto L_0x01a8;
            }
        L_0x01a8:
            r0 = r24
            int r2 = r0.mDropDownWidth
            r4 = 1073741824(0x40000000, float:2.0)
            int r3 = android.view.View.MeasureSpec.makeMeasureSpec(r2, r4)
        L_0x01b2:
            r0 = r24
            android.support.v7.widget.ListPopupWindow$DropDownListView r2 = r0.mDropDownList
            r4 = 0
            r5 = -1
            int r6 = r17 - r18
            r7 = -1
            int r16 = r2.measureHeightOfChildrenCompat(r3, r4, r5, r6, r7)
            if (r16 <= 0) goto L_0x01c3
            int r18 = r18 + r19
        L_0x01c3:
            int r2 = r16 + r18
            goto L_0x0158
        L_0x01c6:
            r0 = r24
            android.content.Context r2 = r0.mContext
            android.content.res.Resources r2 = r2.getResources()
            android.util.DisplayMetrics r2 = r2.getDisplayMetrics()
            int r2 = r2.widthPixels
            r0 = r24
            android.graphics.Rect r4 = r0.mTempRect
            int r4 = r4.left
            r0 = r24
            android.graphics.Rect r5 = r0.mTempRect
            int r5 = r5.right
            int r4 = r4 + r5
            int r2 = r2 - r4
            r4 = -2147483648(0xffffffff80000000, float:-0.0)
            int r3 = android.view.View.MeasureSpec.makeMeasureSpec(r2, r4)
            goto L_0x01b2
        L_0x01e9:
            r0 = r24
            android.content.Context r2 = r0.mContext
            android.content.res.Resources r2 = r2.getResources()
            android.util.DisplayMetrics r2 = r2.getDisplayMetrics()
            int r2 = r2.widthPixels
            r0 = r24
            android.graphics.Rect r4 = r0.mTempRect
            int r4 = r4.left
            r0 = r24
            android.graphics.Rect r5 = r0.mTempRect
            int r5 = r5.right
            int r4 = r4 + r5
            int r2 = r2 - r4
            r4 = 1073741824(0x40000000, float:2.0)
            int r3 = android.view.View.MeasureSpec.makeMeasureSpec(r2, r4)
            goto L_0x01b2
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.ListPopupWindow.buildDropDown():int");
    }

    public static abstract class ForwardingListener implements View.OnTouchListener {
        private int mActivePointerId;
        private Runnable mDisallowIntercept;
        private boolean mForwarding;
        private final int mLongPressTimeout;
        private final float mScaledTouchSlop;
        /* access modifiers changed from: private */
        public final View mSrc;
        private final int mTapTimeout;
        private final int[] mTmpLocation = new int[2];
        private Runnable mTriggerLongPress;
        private boolean mWasLongPress;

        public abstract ListPopupWindow getPopup();

        public ForwardingListener(View src) {
            this.mSrc = src;
            this.mScaledTouchSlop = (float) ViewConfiguration.get(src.getContext()).getScaledTouchSlop();
            this.mTapTimeout = ViewConfiguration.getTapTimeout();
            this.mLongPressTimeout = (this.mTapTimeout + ViewConfiguration.getLongPressTimeout()) / 2;
        }

        public boolean onTouch(View v, MotionEvent event) {
            boolean forwarding;
            boolean wasForwarding = this.mForwarding;
            if (!wasForwarding) {
                if (!onTouchObserved(event) || !onForwardingStarted()) {
                    forwarding = false;
                } else {
                    forwarding = true;
                }
                if (forwarding) {
                    long now = SystemClock.uptimeMillis();
                    MotionEvent e = MotionEvent.obtain(now, now, 3, 0.0f, 0.0f, 0);
                    this.mSrc.onTouchEvent(e);
                    e.recycle();
                }
            } else if (this.mWasLongPress) {
                forwarding = onTouchForwarded(event);
            } else {
                forwarding = onTouchForwarded(event) || !onForwardingStopped();
            }
            this.mForwarding = forwarding;
            if (forwarding || wasForwarding) {
                return true;
            }
            return false;
        }

        /* access modifiers changed from: protected */
        public boolean onForwardingStarted() {
            ListPopupWindow popup = getPopup();
            if (popup == null || popup.isShowing()) {
                return true;
            }
            popup.show();
            return true;
        }

        /* access modifiers changed from: protected */
        public boolean onForwardingStopped() {
            ListPopupWindow popup = getPopup();
            if (popup == null || !popup.isShowing()) {
                return true;
            }
            popup.dismiss();
            return true;
        }

        private boolean onTouchObserved(MotionEvent srcEvent) {
            View src = this.mSrc;
            if (!src.isEnabled()) {
                return false;
            }
            switch (MotionEventCompat.getActionMasked(srcEvent)) {
                case 0:
                    this.mActivePointerId = srcEvent.getPointerId(0);
                    this.mWasLongPress = false;
                    if (this.mDisallowIntercept == null) {
                        this.mDisallowIntercept = new DisallowIntercept();
                    }
                    src.postDelayed(this.mDisallowIntercept, (long) this.mTapTimeout);
                    if (this.mTriggerLongPress == null) {
                        this.mTriggerLongPress = new TriggerLongPress();
                    }
                    src.postDelayed(this.mTriggerLongPress, (long) this.mLongPressTimeout);
                    return false;
                case 1:
                case 3:
                    clearCallbacks();
                    return false;
                case 2:
                    int activePointerIndex = srcEvent.findPointerIndex(this.mActivePointerId);
                    if (activePointerIndex < 0 || pointInView(src, srcEvent.getX(activePointerIndex), srcEvent.getY(activePointerIndex), this.mScaledTouchSlop)) {
                        return false;
                    }
                    clearCallbacks();
                    src.getParent().requestDisallowInterceptTouchEvent(true);
                    return true;
                default:
                    return false;
            }
        }

        private void clearCallbacks() {
            if (this.mTriggerLongPress != null) {
                this.mSrc.removeCallbacks(this.mTriggerLongPress);
            }
            if (this.mDisallowIntercept != null) {
                this.mSrc.removeCallbacks(this.mDisallowIntercept);
            }
        }

        /* access modifiers changed from: private */
        public void onLongPress() {
            clearCallbacks();
            View src = this.mSrc;
            if (src.isEnabled() && !src.isLongClickable() && onForwardingStarted()) {
                src.getParent().requestDisallowInterceptTouchEvent(true);
                long now = SystemClock.uptimeMillis();
                MotionEvent e = MotionEvent.obtain(now, now, 3, 0.0f, 0.0f, 0);
                src.onTouchEvent(e);
                e.recycle();
                this.mForwarding = true;
                this.mWasLongPress = true;
            }
        }

        private boolean onTouchForwarded(MotionEvent srcEvent) {
            DropDownListView dst;
            boolean keepForwarding;
            boolean z = true;
            View src = this.mSrc;
            ListPopupWindow popup = getPopup();
            if (popup == null || !popup.isShowing() || (dst = popup.mDropDownList) == null || !dst.isShown()) {
                return false;
            }
            MotionEvent dstEvent = MotionEvent.obtainNoHistory(srcEvent);
            toGlobalMotionEvent(src, dstEvent);
            toLocalMotionEvent(dst, dstEvent);
            boolean handled = dst.onForwardedEvent(dstEvent, this.mActivePointerId);
            dstEvent.recycle();
            int action = MotionEventCompat.getActionMasked(srcEvent);
            if (action == 1 || action == 3) {
                keepForwarding = false;
            } else {
                keepForwarding = true;
            }
            if (!handled || !keepForwarding) {
                z = false;
            }
            return z;
        }

        private static boolean pointInView(View view, float localX, float localY, float slop) {
            return localX >= (-slop) && localY >= (-slop) && localX < ((float) (view.getRight() - view.getLeft())) + slop && localY < ((float) (view.getBottom() - view.getTop())) + slop;
        }

        private boolean toLocalMotionEvent(View view, MotionEvent event) {
            int[] loc = this.mTmpLocation;
            view.getLocationOnScreen(loc);
            event.offsetLocation((float) (-loc[0]), (float) (-loc[1]));
            return true;
        }

        private boolean toGlobalMotionEvent(View view, MotionEvent event) {
            int[] loc = this.mTmpLocation;
            view.getLocationOnScreen(loc);
            event.offsetLocation((float) loc[0], (float) loc[1]);
            return true;
        }

        private class DisallowIntercept implements Runnable {
            private DisallowIntercept() {
            }

            public void run() {
                ForwardingListener.this.mSrc.getParent().requestDisallowInterceptTouchEvent(true);
            }
        }

        private class TriggerLongPress implements Runnable {
            private TriggerLongPress() {
            }

            public void run() {
                ForwardingListener.this.onLongPress();
            }
        }
    }

    private static class DropDownListView extends ListViewCompat {
        private ViewPropertyAnimatorCompat mClickAnimation;
        private boolean mDrawsInPressedState;
        private boolean mHijackFocus;
        /* access modifiers changed from: private */
        public boolean mListSelectionHidden;
        private ListViewAutoScrollHelper mScrollHelper;

        public DropDownListView(Context context, boolean hijackFocus) {
            super(context, (AttributeSet) null, R.attr.dropDownListViewStyle);
            this.mHijackFocus = hijackFocus;
            setCacheColorHint(0);
        }

        public boolean onForwardedEvent(MotionEvent event, int activePointerId) {
            boolean handledEvent = true;
            boolean clearPressedItem = false;
            int actionMasked = MotionEventCompat.getActionMasked(event);
            switch (actionMasked) {
                case 1:
                    handledEvent = false;
                    break;
                case 2:
                    break;
                case 3:
                    handledEvent = false;
                    break;
            }
            int activeIndex = event.findPointerIndex(activePointerId);
            if (activeIndex < 0) {
                handledEvent = false;
            } else {
                int x = (int) event.getX(activeIndex);
                int y = (int) event.getY(activeIndex);
                int position = pointToPosition(x, y);
                if (position == -1) {
                    clearPressedItem = true;
                } else {
                    View child = getChildAt(position - getFirstVisiblePosition());
                    setPressedItem(child, position, (float) x, (float) y);
                    handledEvent = true;
                    if (actionMasked == 1) {
                        clickPressedItem(child, position);
                    }
                }
            }
            if (!handledEvent || clearPressedItem) {
                clearPressedItem();
            }
            if (handledEvent) {
                if (this.mScrollHelper == null) {
                    this.mScrollHelper = new ListViewAutoScrollHelper(this);
                }
                this.mScrollHelper.setEnabled(true);
                this.mScrollHelper.onTouch(this, event);
            } else if (this.mScrollHelper != null) {
                this.mScrollHelper.setEnabled(false);
            }
            return handledEvent;
        }

        private void clickPressedItem(View child, int position) {
            performItemClick(child, position, getItemIdAtPosition(position));
        }

        private void clearPressedItem() {
            this.mDrawsInPressedState = false;
            setPressed(false);
            drawableStateChanged();
            View motionView = getChildAt(this.mMotionPosition - getFirstVisiblePosition());
            if (motionView != null) {
                motionView.setPressed(false);
            }
            if (this.mClickAnimation != null) {
                this.mClickAnimation.cancel();
                this.mClickAnimation = null;
            }
        }

        private void setPressedItem(View child, int position, float x, float y) {
            View motionView;
            this.mDrawsInPressedState = true;
            if (Build.VERSION.SDK_INT >= 21) {
                drawableHotspotChanged(x, y);
            }
            if (!isPressed()) {
                setPressed(true);
            }
            layoutChildren();
            if (!(this.mMotionPosition == -1 || (motionView = getChildAt(this.mMotionPosition - getFirstVisiblePosition())) == null || motionView == child || !motionView.isPressed())) {
                motionView.setPressed(false);
            }
            this.mMotionPosition = position;
            float childX = x - ((float) child.getLeft());
            float childY = y - ((float) child.getTop());
            if (Build.VERSION.SDK_INT >= 21) {
                child.drawableHotspotChanged(childX, childY);
            }
            if (!child.isPressed()) {
                child.setPressed(true);
            }
            setSelection(position);
            positionSelectorLikeTouchCompat(position, child, x, y);
            setSelectorEnabled(false);
            refreshDrawableState();
        }

        /* access modifiers changed from: protected */
        public boolean touchModeDrawsInPressedStateCompat() {
            return this.mDrawsInPressedState || super.touchModeDrawsInPressedStateCompat();
        }

        public boolean isInTouchMode() {
            return (this.mHijackFocus && this.mListSelectionHidden) || super.isInTouchMode();
        }

        public boolean hasWindowFocus() {
            return this.mHijackFocus || super.hasWindowFocus();
        }

        public boolean isFocused() {
            return this.mHijackFocus || super.isFocused();
        }

        public boolean hasFocus() {
            return this.mHijackFocus || super.hasFocus();
        }
    }

    private class PopupDataSetObserver extends DataSetObserver {
        private PopupDataSetObserver() {
        }

        public void onChanged() {
            if (ListPopupWindow.this.isShowing()) {
                ListPopupWindow.this.show();
            }
        }

        public void onInvalidated() {
            ListPopupWindow.this.dismiss();
        }
    }

    private class ListSelectorHider implements Runnable {
        private ListSelectorHider() {
        }

        public void run() {
            ListPopupWindow.this.clearListSelection();
        }
    }

    private class ResizePopupRunnable implements Runnable {
        private ResizePopupRunnable() {
        }

        public void run() {
            if (ListPopupWindow.this.mDropDownList != null && ViewCompat.isAttachedToWindow(ListPopupWindow.this.mDropDownList) && ListPopupWindow.this.mDropDownList.getCount() > ListPopupWindow.this.mDropDownList.getChildCount() && ListPopupWindow.this.mDropDownList.getChildCount() <= ListPopupWindow.this.mListItemExpandMaximum) {
                ListPopupWindow.this.mPopup.setInputMethodMode(2);
                ListPopupWindow.this.show();
            }
        }
    }

    private class PopupTouchInterceptor implements View.OnTouchListener {
        private PopupTouchInterceptor() {
        }

        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (action == 0 && ListPopupWindow.this.mPopup != null && ListPopupWindow.this.mPopup.isShowing() && x >= 0 && x < ListPopupWindow.this.mPopup.getWidth() && y >= 0 && y < ListPopupWindow.this.mPopup.getHeight()) {
                ListPopupWindow.this.mHandler.postDelayed(ListPopupWindow.this.mResizePopupRunnable, 250);
                return false;
            } else if (action != 1) {
                return false;
            } else {
                ListPopupWindow.this.mHandler.removeCallbacks(ListPopupWindow.this.mResizePopupRunnable);
                return false;
            }
        }
    }

    private class PopupScrollListener implements AbsListView.OnScrollListener {
        private PopupScrollListener() {
        }

        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        }

        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == 1 && !ListPopupWindow.this.isInputMethodNotNeeded() && ListPopupWindow.this.mPopup.getContentView() != null) {
                ListPopupWindow.this.mHandler.removeCallbacks(ListPopupWindow.this.mResizePopupRunnable);
                ListPopupWindow.this.mResizePopupRunnable.run();
            }
        }
    }

    private static boolean isConfirmKey(int keyCode) {
        return keyCode == 66 || keyCode == 23;
    }

    private void setPopupClipToScreenEnabled(boolean clip) {
        if (sClipToWindowEnabledMethod != null) {
            try {
                sClipToWindowEnabledMethod.invoke(this.mPopup, new Object[]{Boolean.valueOf(clip)});
            } catch (Exception e) {
                Log.i(TAG, "Could not call setClipToScreenEnabled() on PopupWindow. Oh well.");
            }
        }
    }

    private int getMaxAvailableHeight(View anchor, int yOffset, boolean ignoreBottomDecorations) {
        if (sGetMaxAvailableHeightMethod != null) {
            try {
                return ((Integer) sGetMaxAvailableHeightMethod.invoke(this.mPopup, new Object[]{anchor, Integer.valueOf(yOffset), Boolean.valueOf(ignoreBottomDecorations)})).intValue();
            } catch (Exception e) {
                Log.i(TAG, "Could not call getMaxAvailableHeightMethod(View, int, boolean) on PopupWindow. Using the public version.");
            }
        }
        return this.mPopup.getMaxAvailableHeight(anchor, yOffset);
    }
}
