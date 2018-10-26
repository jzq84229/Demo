package com.zhang.common.lib.comm;

import android.view.View;

/**
 * 定义RecyclerView的OnItemLongClickListener的接口,
 * 便于在实例化的时候实现它的点击效果
 * Created by admin on 2017/1/11.
 */

public interface OnItemLongClickListener {
    void onItemLongClick(View view, int position);
}
