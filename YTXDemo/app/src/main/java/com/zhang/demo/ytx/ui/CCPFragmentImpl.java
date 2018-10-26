package com.zhang.demo.ytx.ui;

import android.view.View;

import com.zhang.demo.ytx.ui.base.CCPFragment;

/**
 * Fragment公共实现类，是Fragment页面实际展示操作类
 * Created by Administrator on 2016/7/12.
 */
public class CCPFragmentImpl extends CCPActivityBase {
    private final CCPFragment mFragment;

    public CCPFragmentImpl(CCPFragment mFragment) {
        this.mFragment = mFragment;
    }

    @Override
    protected void onInit() {
        mFragment.onFragmentInit();
    }

    @Override
    protected int getLayoutId() {
        return mFragment.getLayoutId();
    }

    @Override
    protected View getContentLayoutView() {
        return null;
    }

    @Override
    protected String getClassName() {
        return mFragment.getClass().getName();
    }

    @Override
    protected void dealContentView(View contentView) {
        mFragment.onBaseContentViewAttach(contentView);
    }

    @Override
    public int getTitleLayout() {
        return mFragment.getTitleLayoutId();
    }
}
