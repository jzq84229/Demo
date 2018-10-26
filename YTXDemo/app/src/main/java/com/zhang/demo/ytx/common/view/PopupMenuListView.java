package com.zhang.demo.ytx.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.zhang.demo.ytx.common.utils.DensityUtil;

/**
 * 标题右边下拉菜单承载布局
 * 重写ListView，设置ListView最小宽度
 * Created by Administrator on 2016/7/7.
 */
public class PopupMenuListView extends SuperListView {
    private Context mContext;
    private static final float MIN_WIDTH = 112.0F;  //listView最小宽度

    public PopupMenuListView(Context context) {
        super(context);
        mContext = context;
    }

    public PopupMenuListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(
                measureWidth() + getPaddingLeft() + getPaddingRight(), MeasureSpec.EXACTLY),
                heightMeasureSpec);
    }

    /**
     * 计算宽度
     */
    private int measureWidth() {
        int maxWidth = 0;
        View convertView = null;
        for (int i = 0; i < getAdapter().getCount(); i++) {
            convertView = getAdapter().getView(i, convertView, this);
            if (convertView == null) {
                continue;
            }
            convertView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            if (convertView.getMeasuredWidth() <= maxWidth) {
                continue;
            }
            maxWidth = convertView.getMeasuredWidth();
        }

        int max = DensityUtil.dip2px(MIN_WIDTH);
        if (maxWidth < max) {
            maxWidth = max;
        }
        return maxWidth;
    }

}
