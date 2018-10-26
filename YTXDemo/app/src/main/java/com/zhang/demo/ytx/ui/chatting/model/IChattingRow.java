package com.zhang.demo.ytx.ui.chatting.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.meeting.ECMeeting;
import com.zhang.demo.ytx.ui.chatting.holder.BaseHolder;

/**
 * <p>Title: ChattingRow.java</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2014</p>
 * <p>Company: Beijing Speedtong Information Technology Co.,Ltd</p>
 * @author Jorstin Chan
 * @date 2014-4-16
 * @version 1.0
 */
public interface IChattingRow {

    /**
     * Get a View that displays the data at the specified position in the data set
     * @param convertView
     * @return
     */
    View buildChatView(LayoutInflater inflater, View convertView);

    void buildChattingBaseData(Context context, BaseHolder baseHolder, ECMessage detail, int position);

    int getChatViewType();
}
