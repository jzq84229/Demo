package com.zhang.demo.ytx.ui.chatting.view;

import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by Administrator on 2016/7/27.
 */
public class RecordPopupWindow extends PopupWindow {
    public RecordPopupWindow(View contentView) {
        super(contentView);
    }

    public RecordPopupWindow(View contentView, int width, int height) {
        super(contentView, width, height, false);
    }

    public RecordPopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }

    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
