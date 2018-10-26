package com.zhang.demo.ytx.ui.chatting.model;

import android.view.ContextMenu;
import android.view.View;

import com.yuntongxun.ecsdk.ECMessage;

import java.util.HashMap;

/**
 * <p>Title: BaseChattingRow.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: Beijing Speedtong Information Technology Co.,Ltd</p>
 *
 * @author Jorstin Chan
 * @version 1.0
 * @date 2014-4-17
 */
public abstract class BaseChattingRow implements IChattingRow {

    private static final String LOG_TAG = BaseChattingRow.class.getSimpleName();
    private HashMap<String, String> hashMap = new HashMap<>();
    int mRowType;

    public BaseChattingRow(int type) {
        mRowType = type;
    }

    /**
     *
     * @param contextMenu
     * @param targetView
     * @param detail
     * @return
     */
    public abstract boolean onCreateRowContextMenu(ContextMenu contextMenu , View targetView , ECMessage detail);

}
