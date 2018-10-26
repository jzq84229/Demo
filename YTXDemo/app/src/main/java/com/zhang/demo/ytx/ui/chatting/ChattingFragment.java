package com.zhang.demo.ytx.ui.chatting;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.yuntongxun.ecsdk.ECChatManager;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.Parameters;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.im.ECUserStateMessageBody;
import com.yuntongxun.ecsdk.im.ECVoiceMessageBody;
import com.yuntongxun.ecsdk.platformtools.ECHandlerHelper;
import com.zhang.demo.ytx.R;
import com.zhang.demo.ytx.common.CCPAppManager;
import com.zhang.demo.ytx.common.utils.DemoUtils;
import com.zhang.demo.ytx.common.utils.FileAccessor;
import com.zhang.demo.ytx.common.utils.LogUtil;
import com.zhang.demo.ytx.common.utils.MediaPlayTools;
import com.zhang.demo.ytx.common.utils.ToastUtil;
import com.zhang.demo.ytx.ui.SDKCoreHelper;
import com.zhang.demo.ytx.ui.base.CCPFragment;
import com.zhang.demo.ytx.ui.chatting.base.ECPullDownView;
import com.zhang.demo.ytx.ui.chatting.view.CCPChattingFooter2;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2016/7/22.
 */
public class ChattingFragment extends CCPFragment implements
        View.OnClickListener,
        AbsListView.OnScrollListener,
        IMChattingHelper.OnMessageReportCallback,
        CustomerServiceHelper.OnCustomerServiceListener {

    public static final String LOG_TAG = ChattingFragment.class.getSimpleName();

    private static final int WHAT_ON_COMPUTATION_TIME = 10000;
    /** request code for tack pic */
    public static final int REQUEST_CODE_TAKE_PICTURE = 0x3;
    public static final int REQUEST_CODE_TAKE_LOCATION = 0x11;
    public static final int REQUEST_CODE_LOAD_IMAGE = 0x4;
    public static final int REQUEST_CODE_IMAGE_CROP = 0x5;
    /**查看名片*/
    public static final int REQUEST_VIEW_CARD = 0x6;
    /**选择回复联系人*/
    public static final int SELECT_AT_SOMONE = 0xD4;

    /** 会话ID，数据库主键 */
    public final static String THREAD_ID = "thread_id";
    /** 联系人账号 */
    public final static String RECIPIENTS = "recipients";
    /** 联系人名称 */
    public final static String CONTACT_USER = "contact_user";
    public final static String FROM_CHATTING_ACTIVITY = "from_chatting_activity";
    public final static String CUSTOMER_SERVICE = "is_customer_service";
    /** 按键振动时长 */
    public static final int TONE_LENGTH_MS = 200;
    /** 音量值 */
    private static final float TONE_RELATIVE_VOLUME = 100.0F;
    /** 待发送的语音文件最短时长 */
    private static final int MIX_TIME = 1000;
    /** 聊天界面消息适配器 */
    private ChattingListAdapter2 mChattingAdapter;
    /** 界面消息下拉刷新 */
    // private RefreshableView mPullDownView;
    // private long mPageCount;
    /** 历史聊天纪录消息显示View */
    private ListView mListView;
    private View mListViewHeadView;
    /** 聊天界面附加聊天控件面板 */
    private CCPChattingFooter2 mChattingFooter;
    /** 选择图片拍照路径 */
    private String mFilePath;
    /** 会话ID */
    private long mThread = -1;
    /** 会话联系人账号 */
    private String mRecipients;
    /** 联系人名称 */
    private String mUsername;
    /** 计算当前录音时长 */
    private long computationTime = -1L;
    /** 当前语言录制文件的时间长度 */
    private int mVoiceRecodeTime = 0;
    /** 是否使用边录制便传送模式发送语音 */
    private boolean isRecordAndSend = false;
    /** 手机震动API */
    private Vibrator mVibrator;
    private ToneGenerator mToneGenerator;
    /** 录音剩余时间Toast提示 */
    private Toast mRecordTipsToast;
    private ECHandlerHelper mHandlerHelper = new ECHandlerHelper();
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Handler mVoiceHandler;
    private Looper mChattingLooper;
    /** IM聊天管理工具 */
    private ECChatManager mChatManager;
    /** 聊天底部导航控件通知回调 */
    private OnChattingFooterImpl mChattingFooterImpl = new OnChattingFooterImpl(
            (ChattingActivity) getActivity());
    /** 聊天功能插件接口实现 */
    private OnChattingPanelImpl mChattingPanelImpl = new OnChattingPanelImpl();
    private ECPullDownView mECPullDownView;
    /** 是否查看消息模式 */
    private boolean isViewMode = false;
    private View mMsgLayoutMask;
    public boolean mAtsomeone = false;
    /** 在线客服 */
    private boolean mCustomerService = false;
    private OnChattingAttachListener mAttachListener;

    private Object mToneGeneratorLock = new Object();

    public static boolean isFireMsg = false;

    public interface OnChattingAttachListener {
        void onChattingAttach();
    }

    @Override
    public int getLayoutId() {
        return R.layout.chatting_activity;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public void onMessageReport(ECError error, ECMessage message) {

    }

    @Override
    public void onPushMessage(String sessionId, List<ECMessage> msgs) {

    }


    @Override
    public void onServiceFinish(String even) {

    }



    @Override
    public void onServiceStart(String event) {

    }

    @Override
    public void onError(ECError error) {

    }





    private void keepScreenOnState(boolean screenOn) {
        if (mListView != null) {
            mListView.setKeepScreenOn(screenOn);
        }
    }



    /**
     * 给予客户端震动提示
     */
    protected void readyOperation() {
        computationTime = -1L;
        mRecordTipsToast = null;
        playTone(ToneGenerator.TONE_PROP_BEEP, TONE_LENGTH_MS);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopTone();
            }
        }, TONE_LENGTH_MS);
        vibrate(50L);
    }

    /**
     * 停止播放声音
     */
    public void stopTone() {
        if (mToneGenerator != null) {
            mToneGenerator.stopTone();
        }
    }

    /**
     * 播放提示音
     * @param tone
     * @param durationMs
     */
    public void playTone(int tone, int durationMs) {
        synchronized (mToneGeneratorLock) {
            initToneGenerator();
            if (mToneGenerator == null) {
                return;
            }
            mToneGenerator.startTone(tone, durationMs);
        }
    }

    /**
     * 手机震动
     * @param milliseconds
     */
    public synchronized void vibrate(long milliseconds) {
        Vibrator mVibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        if (mVibrator == null) {
            return;
        }
        mVibrator.vibrate(milliseconds);
    }

    // 初始化
    private void initToneGenerator() {
        AudioManager mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        if (mToneGenerator == null) {
            try {
                int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                int streamMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                int volume = (int) (TONE_RELATIVE_VOLUME * (streamVolume / streamMaxVolume));
                mToneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, volume);

            } catch (RuntimeException e) {
                LogUtil.d("Exception caught while creating local tone generator: "
                        + e);
                mToneGenerator = null;
            }
        }
    }

    /**
     * 处理状态消息发送
     * @param text
     */
    private void handleSendUserStateMessage(CharSequence text) {
        if (text == null) {
            return;
        }
        if (text.toString().trim().length() <= 0) {
            return;
        }
        if (CCPAppManager.getUserId().equals(mRecipients)) {
            return;
        }

        //组建一个待发送的ECMessage
        ECMessage msg = ECMessage.createECMessage(ECMessage.Type.STATE);
        //设置消息接受者
        msg.setTo(mRecipients);
        //创建一个文本消息体， 并添加到消息对象中
        ECUserStateMessageBody msgBody = new ECUserStateMessageBody(text.toString());
        msg.setBody(msgBody);
        ECChatManager ecChatManager = ECDevice.getECChatManager();
        if (ecChatManager == null) {
            return;
        }
        ecChatManager.sendMessage(msg, new ECChatManager.OnSendMessageListener(){
            @Override
            public void onProgress(String s, int i, int i1) {
            }

            @Override
            public void onSendMessageComplete(ECError ecError, ECMessage ecMessage) {
            }
        });
    }

    /**
     * 聊天功能面板（发送、录音、切换输入选项）
     */
    private class OnChattingFooterImpl implements
            CCPChattingFooter2.OnChattingFooterLinstener{
        ChattingActivity mActivity;
        protected String mAmrPathName;
        /** 保存当前的录音状态 */
        public int mRecordState = RECORD_IDLE;
        /** 语音录制空闲 */
        public static final int RECORD_IDLE = 0;
        /** 语音录制中 */
        public static final int RECORD_ING = 1;
        /** 语音录制结束 */
        public static final int RECORD_DONE = 2;
        /** 待发的ECMessage消息 */
        private ECMessage mPreMessage;

        MediaPlayTools instance;
        public String bianShengFilePath;
        /** 同步锁 */
        Object mLock = new Object();

        private void changeVoiceInSDK(String appendName) {
            final File file = new File(FileAccessor.getVoicePathName().getAbsolutePath() + "/" + appendName + mAmrPathName);
            bianShengFilePath = file.getAbsolutePath();
            if (file != null && file.exists()) {
                instance.playVoice(file.getAbsolutePath(), false);
            } else {
                final Parameters parameters = getParameters(appendName);
                SDKCoreHelper.getECChatManager().changeVoice(parameters, new ECChatManager.OnChangeVoiceListener() {
                    @Override
                    public void onChangeVoice(ECError ecError, Parameters parameters) {
                        if (ecError.errorCode == SdkErrorCode.REQUEST_SUCCESS) {
                            instance.playVoice(parameters.outFileName, false);
                        } else {
                            file.delete();
                        }
                    }
                });
            }
        }

        private Parameters getParameters(String appendName) {
            Parameters parameters = new Parameters();
            parameters.inFileName = FileAccessor.getVoicePathName().getAbsolutePath() + "/" + mAmrPathName;
            parameters.outFileName = FileAccessor.getVoicePathName().getAbsolutePath() + "/" + appendName + mAmrPathName;
            if ("yuansheng".equals(appendName)) {

            } else if ("luoli".equals(appendName)) {
                parameters.pitch = 12;  //-12   12
                parameters.tempo = 1;   //-0.05 1
            } else if ("dashu".equals(appendName)) {
                parameters.pitch = 2;
                parameters.tempo = 1;
            } else if ("jingsong".equals(appendName)) {
                parameters.pitch = 1;
                parameters.tempo = -3;
            } else if ("gaoguai".equals(appendName)) {
                parameters.pitch = 5;
                parameters.tempo = 1;
            } else if ("kongling".equals(appendName)) {
                parameters.pitch = 1;
                parameters.tempo = -1;
            }
            return parameters;
        }

        public OnChattingFooterImpl(ChattingActivity ctx) {
            mActivity = ctx;
            instance = MediaPlayTools.getINstance();
        }





        @Override
        public void OnVoiceRcdInitReuqest() {
            mAmrPathName = DemoUtils.md5(String.valueOf(System.currentTimeMillis())) + ".amr";
            if (FileAccessor.getVoicePathName() == null) {
                ToastUtil.showMessage("Path to file could not be created");
                mAmrPathName = null;
                return;
            }

            keepScreenOnState(true);
            if (getRecordState() != RECORD_ING) {
                setRecordState(RECORD_ING);

                //手指按下按钮，按钮给予震动或者声音反馈
                readyOperation();
                //显示录音提示框
                mChattingFooter.showVoiceRecordWindow(findViewById(R.id.chatting_bg_ll).getHeight() - mChattingFooter.getHeight());

                final ECChatManager chatManager = SDKCoreHelper.getECChatManager();
                if (chatManager == null) {
                    return;
                }
                mVoiceHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ECMessage message = ECMessage.createECMessage(ECMessage.Type.VOICE);
                        message.setTo(mRecipients);
                        ECVoiceMessageBody messageBody = new ECVoiceMessageBody(new File(FileAccessor.getVoicePathName(), mAmrPathName), 0);
                        message.setBody(messageBody);
                        mPreMessage = message;
                        //仅录制语音消息，录制完成后需要调用发送接口发送消息
                        handleSendUserStateMessage("2");
                        chatManager.startVoiceRecording(messageBody, new ECChatManager.OnRecordTimeoutListener(){

                            @Override
                            public void onRecordingTimeOut(long l) {
                                //如果语音录制超过最大60s长度，怎发送
                                if (mChattingFooter.isChangeVoice) {
                                    OnVoiceRcdStopRequest(false);
                                    mChattingFooter.showBianShengView();
                                } else {
                                    doProcesOperationRecordOver(false, true);
                                }
                            }

                            @Override
                            public void onRecordingAmplitude(double amplitude) {
                                //显示声音振幅
                                if (mChattingFooter != null && getRecordState() == RECORD_ING) {
                                    mChattingFooter.showVoiceRecording();
                                    mChattingFooter.displayAmplitude(amplitude);
                                }
                            }
                        });
                    }
                });
            }
        }

        @Override
        public void sendChangeVoiceMsg(boolean isSendYuanSheng) {

        }

        @Override
        public void OnVoiceRcdStartRequest() {

        }

        @Override
        public void stopVoicePlay() {

        }

        @Override
        public void OnVoiceRcdCancelRequest() {

        }

        @Override
        public void OnVoiceRcdStopRequest(boolean isSend) {

        }

        @Override
        public void onVoiceChangeRequest(int position) {

        }

        @Override
        public void OnSendTextMessageRequest(CharSequence text) {

        }

        @Override
        public void OnUpdateTextOutBoxRequest(CharSequence text) {

        }

        @Override
        public void OnSendCustomEmojiRequest(int emojiid, String emojiName) {

        }

        @Override
        public void OnEmojiDelRequest() {

        }

        @Override
        public void OnInEditMode() {

        }

        @Override
        public void onPause() {

        }

        @Override
        public void onResume() {

        }

        @Override
        public void release() {

        }

        public int getRecordState() {
            synchronized (mLock) {
                return mRecordState;
            }
        }

        public void setRecordState(int state) {
            synchronized (mLock) {
                this.mRecordState = state;
            }
        }

        /**
         * 处理录音结束消息是否发送逻辑
         * @param cancle 是否取消发送
         */
        protected void doProcesOperationRecordOver(boolean cancle,boolean isSend) {
            if(getRecordState() == RECORD_ING) {
                // 当前是否有正在录音的操作
                // 定义标志位判断当前所录制的语音文件是否符合发送条件
                // 只有当录制的语音文件的长度超过1s才进行发送语音
                boolean isVoiceToShort = false;
                File amrPathFile = new File(FileAccessor.getVoicePathName() ,mAmrPathName);
                if(amrPathFile.exists()) {
                    mVoiceRecodeTime = DemoUtils.calculateVoiceTime(amrPathFile.getAbsolutePath());
                    if(!isRecordAndSend) {
                        if (mVoiceRecodeTime * 1000 < MIX_TIME) {
                            isVoiceToShort = true;
                        }
                    }
                } else {
                    isVoiceToShort = true;
                }
                // 设置录音空闲状态
                setRecordState(RECORD_IDLE);
                if(mChattingFooter != null ) {
                    if (isVoiceToShort && !cancle) {
                        // 提示语音文件长度太短
                        mChattingFooter.tooShortPopupWindow();
                        return;
                    }

                    if(!isSend&&mChattingFooter.isChangeVoice&&!cancle){
                        mChattingFooter.showBianShengView();
                    }
                    // 关闭语音录制对话框
                    mChattingFooter.dismissPopupWindow();
                }




                if(!cancle && mPreMessage != null&&isSend) {
                    if(!isRecordAndSend) {
                        // 如果当前的录音模式为非Chunk模式
                        try {
                            ECVoiceMessageBody body = (ECVoiceMessageBody) mPreMessage.getBody();
                            body.setDuration(mVoiceRecodeTime);
                            long rowId;
                            if(mCustomerService) {
                                rowId = CustomerServiceHelper.sendMCMessage(mPreMessage);
                            } else {
                                rowId = IMChattingHelper.sendECMessage(mPreMessage);
                            }
                            mPreMessage.setId(rowId);
                            notifyIMessageListView(mPreMessage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    return ;
                }

                // 删除语音文件
                amrPathFile.deleteOnExit();
                // 重置语音时间长度统计
                mVoiceRecodeTime = 0;
            }
        }
    }

    /**
     * 聊天插件功能实现
     */
    private class OnChattingPanelImpl implements CCPChattingFooter2.OnChattingPanelClickListener {
        @Override
        public void OnTakingPictureRequest() {
            showTakeStyle(getActivity());
            hideBottomPanel();
        }

        @Override
        public void OnSelectImageReuqest() {
            handleSelectImageIntent();
            hideBottomPanel();
        }

        @Override
        public void OnSelectFileRequest() {
            startActivityForResult(new Intent(getActivity(), FileExplorerActivity.class), 0x2a);
            hideBottomPanel();
        }

        @Override
        public void OnSelectVoiceRequest() {
            handleVoiceCall();
            hideBottomPanel();

        }

        @Override
        public void OnSelectVideoRequest() {
            handleVideoCall();
            hideBottomPanel();
        }

        @Override
        public void OnSelectFireMsg() {
            showTakeFireStyle(getActivity());
            hideBottomPanel();
        }

        @Override
        public void OnSelectLocationRequest() {     //位置
            Intent intent =new Intent(getActivity(), LocationActivity.class);
            startActivityForResult(intent, REQUEST_CODE_TAKE_LOCATION);
            hideBottomPanel();
        }

        private void hideBottomPanel() {
            mChattingFooter.hideBottomPanel();
        }
    }

}
