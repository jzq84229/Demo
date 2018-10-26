package com.zhang.demo.ytx.common.base;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 重载接口增加设置主界面是否可以进行滑动
 * Created by Administrator on 2016/7/8.
 */
public class CCPCustomViewPager extends ViewPager {
    /** 控制页面是否可以左右滑动 */
    private boolean mSlidenabled = true;

    public CCPCustomViewPager(Context context) {
        super(context);
    }

    public CCPCustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 设置是否可以滑动
     * @param enabled
     */
    public final void setSlideEnable(boolean enabled) {
        mSlidenabled = enabled;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!mSlidenabled) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!mSlidenabled) {
            return false;
        }
        return super.onTouchEvent(ev);
    }
}
