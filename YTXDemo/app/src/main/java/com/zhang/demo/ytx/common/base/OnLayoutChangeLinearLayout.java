package com.zhang.demo.ytx.common.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 自定义LinearLaout, 可监听layout改变
 * Created by Administrator on 2016/7/29.
 */
public class OnLayoutChangeLinearLayout extends LinearLayout {
    public OnLayoutChangedListener mListener;

    public interface OnLayoutChangedListener {
        void onLayoutChanged();
    }

    public OnLayoutChangeLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OnLayoutChangeLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mListener == null) {
            return;
        }
        mListener.onLayoutChanged();
    }

    public void setOnChattingLayoutChangedListener(OnLayoutChangedListener listener) {
        mListener = listener;
    }
}
