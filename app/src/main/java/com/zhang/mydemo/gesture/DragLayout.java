package com.zhang.mydemo.gesture;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.zhang.mydemo.R;

/**
 * Created by Administrator on 2016/8/2.
 */
public class DragLayout extends LinearLayout {

    private ViewDragHelper mDragHelper;
    private View mDragView;

    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragHelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDragView = findViewById(R.id.drag_view);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mDragHelper.cancel();
            return false;       //返回false表示不拦截MotionEvent事件，将事件传递个子控件
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);   //将MotionEvent交给ViewDragHelper处理
        return true;    //返回true表示消费MotionEvent事件
    }


    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            final int leftBound = getPaddingLeft();
            final int rightBound = getWidth() - mDragView.getWidth();
            final int newLeft = Math.min(Math.max(left, leftBound), rightBound);
            return newLeft;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            final int topBound = getPaddingTop();
            final int bottomBound = getHeight() - mDragView.getHeight();
            final int newTop = Math.min(Math.max(top, topBound), bottomBound);
            return newTop;
        }
    }
}
