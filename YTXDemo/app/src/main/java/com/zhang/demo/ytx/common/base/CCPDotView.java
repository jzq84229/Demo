package com.zhang.demo.ytx.common.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zhang.demo.ytx.R;

/**
 * ViewPager指示器
 * Created by Administrator on 2016/7/22.
 */
public class CCPDotView extends LinearLayout {
    /** The default count of CCPDotView. */
    private int defaultCount = 9;
    private int defaultNormal = R.drawable.page_normal;
    private int defaultActive = R.drawable.page_active;

    public CCPDotView(Context context) {
        this(context, null);
    }

    public CCPDotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CCPDotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(R.styleable.AppPanel);
        int count = typedArray.getResourceId(R.styleable.AppPanel_dot_count, 0);
        typedArray.recycle();
        setDotCount(count);
    }

    public void setMaxCount(int count) {
        this.defaultCount = count;
    }

    public void initDotResource() {
        defaultNormal = R.drawable.page_normal_dark;
        defaultActive = R.drawable.page_active_dark;
    }

    /**
     * The total number of dot, namely the total number of pages
     * @param count
     */
    public void setDotCount(int count) {
        if (count < 0) {
            return;
        }
        if (count > this.defaultCount) {
            count = defaultCount;
        }
        removeAllViews();

        while (count != 0) {
            ImageView imageControl = (ImageView) View.inflate(getContext(), R.layout.ccppage_control_image, null);
            // dark_dot
            imageControl.setImageResource(defaultNormal);
            addView(imageControl);
            count--;
        }

        //white_dot
        ImageView imageView = (ImageView) getChildAt(0);
        if (imageView != null) {
            imageView.setImageResource(defaultActive);
        }
    }

    /**
     * Set the current sdotselected
     * @param selecteDot
     */
    public void setSelectedDot(int selecteDot) {
        if (selecteDot >= getChildCount()) {
            selecteDot = getChildCount() - 1;
        }

        for (int i = 0; i < getChildCount(); i++) {
            //dark_dot
            ((ImageView) getChildAt(i)).setImageResource(defaultNormal);
        }
        if (selecteDot < 0) {
            selecteDot = 0;
        }

        //white_dot
        ImageView localImageView = (ImageView) getChildAt(selecteDot);
        if (localImageView != null) {
            localImageView.setImageResource(defaultActive);
        }
    }

    public int getDotCount() {
        return defaultCount;
    }
}
