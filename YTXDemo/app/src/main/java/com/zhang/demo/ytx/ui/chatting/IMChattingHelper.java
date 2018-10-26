package com.zhang.demo.ytx.ui.chatting;

import com.yuntongxun.ecsdk.ECChatManager;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.OnChatReceiveListener;
import com.yuntongxun.ecsdk.im.ECMessageNotify;
import com.yuntongxun.ecsdk.im.group.ECGroupNoticeMessage;
import com.zhang.demo.ytx.common.CCPAppManager;
import com.zhang.demo.ytx.common.utils.DemoUtils;
import com.zhang.demo.ytx.common.utils.LogUtil;
import com.zhang.demo.ytx.storage.IMessageSqlManager;
import com.zhang.demo.ytx.ui.SDKCoreHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 *
 * Created by Administrator on 2016/7/21.
 */
public class IMChattingHelper implements OnChatReceiveListener, ECChatManager.OnDownloadMessageListener {
    private static final String LOG_TAG = IMChattingHelper.class.getSimpleName();
    public static final String INTENT_ACTION_SYNC_MESSAGE = "com.yuntongxun.ecdemo_sync_message";
    public static final String INTENT_ACTION_CHAT_USER_STATE = "com.yuntongxun.ecdemo_chat_state";
    public static final String INTENT_ACTION_CHAT_EDITTEXT_FOUCU = "com.yuntongxun.ecdemo_chat_edit_foucs";
    public static final String USER_STATE = "chat_state";
    public static final String GROUP_PRIVATE_TAG = "@priategroup.com";
//    private static HashMap<String, SyncMsgEntry> syncMessage = new HashMap<String, SyncMsgEntry>();
    private static IMChattingHelper sInstance;
    private boolean isSyncOffline = false;

    public interface OnMessageReportCallback {
        void onMessageReport(ECError error, ECMessage message);

        void onPushMessage(String sessionId, List<ECMessage> msgs);
    }

    public static IMChattingHelper getInstance() {
        if (sInstance == null) {
            sInstance = new IMChattingHelper();
        }
        return sInstance;
    }

    /** 云通讯SDK聊天功能接口 */
    private ECChatManager mChatManager;
    /** 全局处理所有的IM消息发送回调 */
    private ChatManagerListener mListener;
    /** 是否是同步消息 */
    private boolean isFirstSync = false;

    /**
     * 消息发送报告
     */
    private OnMessageReportCallback mOnMessageReportCallback;

    private IMChattingHelper() {
        initManager();
        mListener = new ChatManagerListener();
    }

    public void initManager() {
        mChatManager = SDKCoreHelper.getECChatManager();
    }

    @Override
    public void OnReceivedMessage(ECMessage ecMessage) {

    }

    @Override
    public void onReceiveMessageNotify(ECMessageNotify ecMessageNotify) {

    }

    @Override
    public void OnReceiveGroupNoticeMessage(ECGroupNoticeMessage ecGroupNoticeMessage) {

    }

    @Override
    public void onOfflineMessageCount(int i) {

    }

    @Override
    public int onGetOfflineMessage() {
        return 0;
    }

    @Override
    public void onReceiveOfflineMessage(List<ECMessage> list) {

    }

    @Override
    public void onReceiveOfflineMessageCompletion() {

    }

    @Override
    public void onServicePersonVersion(int i) {

    }

    @Override
    public void onReceiveDeskMessage(ECMessage ecMessage) {

    }

    @Override
    public void onSoftVersion(String s, int i) {

    }

    @Override
    public void onDownloadMessageComplete(ECError ecError, ECMessage ecMessage) {

    }

    @Override
    public void onProgress(String s, int i, int i1) {

    }








    private class ChatManagerListener implements ECChatManager.OnSendMessageListener{

        @Override
        public void onSendMessageComplete(ECError ecError, ECMessage ecMessage) {
            if (ecMessage == null) {
                return;
            }
            //处理ECMessage的发送状态
            if (ecMessage != null) {
                if (ecMessage.getType() == ECMessage.Type.VOICE) {
                    try {
                        DemoUtils.playNotifycationMusic(CCPAppManager.getContext(), "sound/voice_message_sent.mp3");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                IMessageSqlManager.setIMessageSendStatus(ecMessage.getMsgId(), ecMessage.getMsgStatus().ordinal());
                IMessageSqlManager.notifyMsgChanged(ecMessage.getSessionId());
                if (mOnMessageReportCallback != null) {
                    mOnMessageReportCallback.onMessageReport(ecError, ecMessage);
                }
                return;
            }
        }

        @Override
        public void onProgress(String msgId, int total, int progress) {
            // 处理发送文件IM消息的时候进度回调
            LogUtil.d(LOG_TAG, "[IMChattingHelper - onProgress] msgId：" + msgId
                    + " ,total：" + total + " ,progress:" + progress);
        }
    }
}
