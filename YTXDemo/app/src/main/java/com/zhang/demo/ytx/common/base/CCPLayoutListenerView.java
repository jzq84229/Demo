package com.zhang.demo.ytx.common.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;

import com.zhang.demo.ytx.common.utils.LogUtil;

/**
 * 自定义FrameLayout，
 * 可监听页面大小变化(onSizeChange)，页面布局(onLayout)
 * Created by Administrator on 2016/7/8.
 */
public class CCPLayoutListenerView extends FrameLayout {
    private OnCCPViewLayoutListener mOnLayoutListener;
    private OnCCPViewSizeChangedListener mOnSizeChangedListener;

    public CCPLayoutListenerView(Context context) {
        super(context);
    }

    public CCPLayoutListenerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.mOnLayoutListener != null) {
            this.mOnLayoutListener.onViewLayout();
        }
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        LogUtil.d(LogUtil.getLogUtilsTag(getClass()), "jorstinchan onInitializeAccessibilityEvent");
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        LogUtil.d(LogUtil.getLogUtilsTag(getClass()), "jorstinchan onInitializeAccessibilityNodeInfo");
    }

    @Override
    public void onPopulateAccessibilityEvent(AccessibilityEvent event) {
        super.onPopulateAccessibilityEvent(event);
        LogUtil.d(LogUtil.getLogUtilsTag(getClass()), "jorstinchan onPopulateAccessibilityEvent");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (this.mOnSizeChangedListener != null) {
            this.mOnSizeChangedListener.onSizeChanged(w, h, oldw, oldh);
        }
    }

    public void setOnLayoutListener(OnCCPViewLayoutListener mOnLayoutListener) {
        this.mOnLayoutListener = mOnLayoutListener;
    }

    public void setOnSizeChangedListener(OnCCPViewSizeChangedListener onSizeChangedListener) {
        this.mOnSizeChangedListener = onSizeChangedListener;
    }

    public void setRootConsumeWatcher() {}














    public interface OnCCPViewLayoutListener {
        void onViewLayout();
    }

    /**
     * View size change listener
     */
    public interface OnCCPViewSizeChangedListener {
        void onSizeChanged(int w, int h, int oldw, int oldh);
    }
}
