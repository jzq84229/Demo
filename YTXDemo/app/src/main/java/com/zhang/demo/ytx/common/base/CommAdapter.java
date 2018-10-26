package com.zhang.demo.ytx.common.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 自定义数据适配器(抽象buildView和bindData两个方法)
 * Created by Administrator on 2016/7/7.
 */
public abstract class CommAdapter extends BaseAdapter {

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int itemViewType = getItemViewType(position);
        if (isNullContentView(convertView, itemViewType)) {
            convertView = buildViewByType(position, parent, itemViewType);
        }
        bindData(convertView, position, itemViewType);
        return convertView;
    }

    /**
     * 判断是否为空，重复使用
     * @param contentView       contentView
     * @param itemViewType      itemViewType
     * @return                  boolean
     */
    protected boolean isNullContentView(View contentView, int itemViewType) {
        return contentView == null;
    }

    /**
     * 需要实现方法，子类根据提供的Item类型返回对应的View
     * @param position          position
     * @param parent            parent
     * @param itemViewType      itemViewType
     * @return                  view
     */
    protected abstract View buildViewByType(int position, ViewGroup parent, int itemViewType);

    /**
     * 绑定数据
     * @param convertView       convertView
     * @param position          position
     * @param itemViewType      itemViewType
     */
    protected abstract void bindData(View convertView, int position, int itemViewType);
}
