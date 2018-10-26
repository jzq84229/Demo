package com.zhang.demo.ytx.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhang.demo.ytx.R;

/**
 * 通讯录页面（联系人）
 * Created by Administrator on 2016/7/11.
 */
public class MobileContactFragment extends TabFragment {
    @Override
    public int getLayoutId() {
        return R.layout.mobile_contacts_activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    protected void onTabFragmentClick() {

    }

    @Override
    protected void onReleaseTabUI() {

    }
}
