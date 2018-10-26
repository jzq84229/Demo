package com.zhang.demo.ytx.ui.chatting;

import com.yuntongxun.ecsdk.ECMessage;

/**
 * Created by Administrator on 2016/8/1.
 */
public class ChattingsRowUtils {

    public static int getChattingMessageType(ECMessage.Type type) {
        if (type == ECMessage.Type.TXT) {
            return 2000;
        } else if (type == ECMessage.Type.VOICE) {
            return 60;
        } else if (type == ECMessage.Type.FILE) {
            return 1024;
        } else if (type == ECMessage.Type.IMAGE) {
            return 200;
        } else if (type == ECMessage.Type.VIDEO) {
            return 1024;
        } else if (type == ECMessage.Type.LOCATION) {
            return 2200;
        } else if (type == ECMessage.Type.CALL) {
            return 2400;
        } else if (type == ECMessage.Type.RICH_TEXT) {
            return 2600;
        }
        return 2000;
    }
}
