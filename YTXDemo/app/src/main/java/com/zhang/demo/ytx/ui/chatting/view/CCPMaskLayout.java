package com.zhang.demo.ytx.ui.chatting.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zhang.demo.ytx.R;
import com.zhang.demo.ytx.common.utils.LogUtil;

/**
 * 自定义头像显示控件
 * Created by Administrator on 2016/8/1.
 */
public class CCPMaskLayout extends RelativeLayout {
    private View mView;
    private ImageView mImageView;
    private Drawable mForeDrawable;

    public CCPMaskLayout(Context context) {
        this(context, null);
    }

    public CCPMaskLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CCPMaskLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.MaskLayout, defStyleAttr, 0);
        mForeDrawable = styledAttributes.getDrawable(0);
        styledAttributes.recycle();
    }

    public void initMaskLayoutRule() {
        removeView(mImageView);
        LayoutParams params = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        );

        addView(mImageView, params);
    }

    public final View getContentView() {
        return mView;
    }

    /** (non-Javadoc)
	 * @see android.view.View#onFinishInflate()
	 */
    protected void onFinishInflate() {
        super.onFinishInflate();

        mView = findViewById(R.id.content);
        if (mView == null) {
            LogUtil.e(LogUtil.getLogUtilsTag(CCPMaskLayout.class), "not found view by id, new one");
            mView = new View(getContext());
            LayoutParams layoutParams = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            mView.setLayoutParams(layoutParams);
            addView(mView);
        }

        mImageView = new ImageView(getContext());
        mImageView.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        mImageView.setImageDrawable(mForeDrawable);
        addView(mImageView);
    }

    public enum MaskLayoutRule {
        Rule_ALIGN_PARENT_LEFT,     // 9
        Rule_ALIGN_PARENT_RIGHT,    // 11
        Rule_ALIGN_PARENT_BOTTOM    // 12
    }

}
