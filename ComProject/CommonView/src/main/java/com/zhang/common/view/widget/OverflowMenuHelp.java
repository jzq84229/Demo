package com.zhang.common.view.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.zhang.common.lib.R;
import com.zhang.common.view.bean.OverMenu;

import java.util.List;


/**
 * 悬浮弹出菜单工具类
 * Created by Administrator on 2017/11/9 0009.
 */

public class OverflowMenuHelp implements AdapterView.OnItemClickListener {
    private Context context;
    private LinearLayout overView;
    private ListView menulist;
    private OverflowMenuAdapter overflowMenuAdapter;
    private PopupWindow mPopupWindow;

    public OverflowMenuHelp(Context context) {
        this.context = context;
        overView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.over_flow_view_layout, null);
        menulist = (ListView) overView.findViewById(R.id.menulist);
        overflowMenuAdapter = new OverflowMenuAdapter(context);
        menulist.setAdapter(overflowMenuAdapter);
        menulist.setOnItemClickListener(this);
        mPopupWindow = new PopupWindow(overView, context.getResources().getDimensionPixelSize(R.dimen.dp_140),
                ViewGroup.LayoutParams.WRAP_CONTENT, false);
        mPopupWindow.setContentView(overView);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setClippingEnabled(false);
        mPopupWindow.setFocusable(true);
    }

    public void setMenuData(List<OverMenu> data) {
        overflowMenuAdapter.setData(data);
        overflowMenuAdapter.notifyDataSetChanged();
        int popopheight;
        if (data != null) {
            popopheight = data.size() * (context.getResources().getDimensionPixelSize(R.dimen.dp_40))
                    + context.getResources().getDimensionPixelSize(R.dimen.dp_10);
        } else {
            popopheight = 0;
        }
        mPopupWindow.setHeight(popopheight);
//        mPopupWindow.update();
    }

    public boolean isShow() {
        return mPopupWindow.isShowing();
    }

    public void showAsDropDownWithLeft(final View anchor) {
        int x = context.getResources().getDimensionPixelSize(R.dimen.dp_140) - anchor.getWidth();
        mPopupWindow.showAsDropDown(anchor, -x, 0);
    }

    public void showAsDropDownWithRight(final View anchor, int x, int y) {
        int right = context.getResources().getDimensionPixelSize(R.dimen.dp_140) - anchor.getWidth();
        mPopupWindow.showAsDropDown(anchor, -right + x, y);
    }

    /**
     * 关闭菜单
     */
    public void dismiss() {
        if (!isShow()) {
            return;
        }
        mPopupWindow.dismiss();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OverMenu overMenu = (OverMenu) parent.getAdapter().getItem(position);
        if (onItemClickListner != null) {
            onItemClickListner.onItemClick(position, overMenu);
        }
    }

    private OnItemClickListner onItemClickListner;

    public void setOnItemClickListner(OnItemClickListner onItemClickListner) {
        this.onItemClickListner = onItemClickListner;
    }

    public interface OnItemClickListner {
        public void onItemClick(int position, OverMenu overMenu);
    }
}
