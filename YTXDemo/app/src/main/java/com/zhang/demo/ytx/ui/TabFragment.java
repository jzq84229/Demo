package com.zhang.demo.ytx.ui;

import android.view.KeyEvent;

/**
 * 自定义TabView的Fragment，将三个TabView共同属性方法统一处理
 * 三个TabView的Fragment页面需要继承该类
 * Created by Administrator on 2016/7/11.
 */
public abstract class TabFragment extends BaseFragment {

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /** 当前TabFragment被点击 */
    protected abstract void onTabFragmentClick();
    /** 当前TabFragment被释放 */
    protected abstract void onReleaseTabUI();

    @Override
    public void onDestroy() {
        onReleaseTabUI();
        super.onDestroy();
    }

    public int getTitleLayoutId() {
        return -1;
    }
}
