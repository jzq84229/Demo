package com.zhang.demo.ytx.ui;

import android.view.View;

/**
 * Created by Administrator on 2016/7/12.
 */
public class CCPActivityImpl extends CCPActivityBase {
    private final ECSuperActivity mActivity;

    public CCPActivityImpl(ECSuperActivity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    protected void onInit() {

    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected View getContentLayoutView() {
        return null;
    }

    @Override
    protected String getClassName() {
        return null;
    }

    @Override
    protected void dealContentView(View contentView) {
    }
}
