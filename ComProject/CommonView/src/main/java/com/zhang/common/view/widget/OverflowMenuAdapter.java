package com.zhang.common.view.widget;

import android.content.Context;

import com.zhang.common.lib.adapter.BaseListAdapter;
import com.zhang.common.lib.adapter.BaseListItemHolder;
import com.zhang.common.view.R;
import com.zhang.common.view.bean.OverMenu;


/**
 * 悬浮菜单列表适配器
 * Created by Administrator on 2017/11/9 0009.
 */

public class OverflowMenuAdapter extends BaseListAdapter<OverMenu> {
    public OverflowMenuAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.over_flow_menu_item_layout;
    }

    @Override
    public void onBindViewHolder(BaseListItemHolder holder, int position) {
        OverMenu overMenu = getItem(position);
        holder.setImageResource(R.id.iv_menu_icon, overMenu.getImgRid());
        holder.setText(R.id.tv_menu_name, overMenu.getName());
    }
}
