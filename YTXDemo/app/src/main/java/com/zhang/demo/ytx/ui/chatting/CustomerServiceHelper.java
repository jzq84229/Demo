package com.zhang.demo.ytx.ui.chatting;

import com.yuntongxun.ecsdk.ECDeskManager;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECFileMessageBody;
import com.zhang.demo.ytx.common.CCPAppManager;
import com.zhang.demo.ytx.common.utils.DemoUtils;
import com.zhang.demo.ytx.common.utils.LogUtil;
import com.zhang.demo.ytx.storage.IMessageSqlManager;
import com.zhang.demo.ytx.ui.SDKCoreHelper;

import java.io.IOException;

/**
 *
 * Created by Administrator on 2016/7/22.
 */
public class CustomerServiceHelper {
    private static final String LOG_TAG = CustomerServiceHelper.class.getSimpleName();
    private static CustomerServiceHelper mInstance = new CustomerServiceHelper();

    private ECDeskManager mDeskManager;
    private OnCustomerServiceListener mCustomerServiceListener;
    /**全局处理所有的IM消息发送回调*/
    private MessageListener mListener;

    public static CustomerServiceHelper getInstance() {
        return mInstance;
    }


    public interface OnStartCustomerServiceListener {
        void onServiceStart(String event);
        void onError(ECError error);
    }

    public interface OnCustomerServiceListener extends OnStartCustomerServiceListener{
        void onServiceFinish(String even);
        void onMessageReport(ECError error , ECMessage message);
    }

    /**
     * 发送ECMessage 消息
     * @param msg
     */
    public static long sendMCMessage(ECMessage msg) {
        initECDeskManager();
        //获取一个聊天管理器
        ECDeskManager manager = getInstance().mDeskManager;
        if (manager != null) {
            //调用接口发送IM消息
            msg.setMsgTime(System.currentTimeMillis());
            manager.sendtoDeskMessage(msg, getInstance().mListener);
            //保存发送的消息到数据库
            if (msg.getType() == ECMessage.Type.FILE && msg.getBody() instanceof ECFileMessageBody) {
                ECFileMessageBody fileMessageBody = (ECFileMessageBody) msg.getBody();
                msg.setUserData("fileName=" + fileMessageBody.getFileName());
            }
        } else {
            msg.setMsgStatus(ECMessage.MessageStatus.FAILED);
        }
        return IMessageSqlManager.insertIMessage(msg, ECMessage.Direction.SEND.ordinal());
    }

    private static boolean initECDeskManager() {
        if (getInstance().mDeskManager == null) {
            getInstance().mDeskManager = SDKCoreHelper.getECDeskManager();
        }
        if (getInstance().mDeskManager == null) {
            LogUtil.e(LOG_TAG, "SDK not ready.");
            return false;
        }
        return true;
    }


    private class MessageListener implements ECDeskManager.OnSendDeskMessageListener {
        @Override
        public void onSendMessageComplete(ECError ecError, ECMessage ecMessage) {
            if(ecMessage == null) {
                return ;
            }
            // 处理ECMessage的发送状态
            if(ecMessage != null) {
                if(ecMessage.getType() == ECMessage.Type.VOICE) {
                    try {
                        DemoUtils.playNotifycationMusic(CCPAppManager.getContext(), "sound/voice_message_sent.mp3");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                IMessageSqlManager.setIMessageSendStatus(ecMessage.getMsgId(), ecMessage.getMsgStatus().ordinal());
                IMessageSqlManager.notifyMsgChanged(ecMessage.getSessionId());
                OnCustomerServiceListener serviceListener = getInstance().mCustomerServiceListener;
                if(serviceListener != null) {
                    serviceListener.onMessageReport(ecError , ecMessage);
                }
                return ;
            }
        }

        @Override
        public void onProgress(String  msgId ,int total, int progress) {
            // 处理发送文件IM消息的时候进度回调
            LogUtil.d(LOG_TAG , "[MessageListener - onProgress] msgId：" +msgId + " ,total：" + total + " ,progress:" + progress);
        }
    }


}
