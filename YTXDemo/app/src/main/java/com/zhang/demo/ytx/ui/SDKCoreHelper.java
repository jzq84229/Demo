package com.zhang.demo.ytx.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.yuntongxun.ecsdk.ECChatManager;
import com.yuntongxun.ecsdk.ECDeskManager;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECGroupManager;
import com.yuntongxun.ecsdk.ECInitParams;
import com.yuntongxun.ecsdk.ECNotifyOptions;
import com.yuntongxun.ecsdk.ECVoIPCallManager;
import com.yuntongxun.ecsdk.ECVoIPSetupManager;
import com.zhang.demo.ytx.ECApplication;
import com.zhang.demo.ytx.R;
import com.zhang.demo.ytx.common.utils.LogUtil;
import com.zhang.demo.ytx.ui.chatting.IMChattingHelper;

/**
 * Created by Administrator on 2016/7/12.
 */
public class SDKCoreHelper implements ECDevice.InitListener, ECDevice.OnECDeviceConnectListener {
    private static final String LOG_TAG = SDKCoreHelper.class.getSimpleName();
    public static final String ACTION_LOGOUT = "com.yuntongxun.ECDemo_logout";
    public static final String ACTION_SDK_CONNECT = "com.yuntongxun.Intent_Action_SDK_CONNECT";
    public static final String ACTION_KICK_OFF = "com.yuntongxun.Intent_ACTION_KICK_OFF";
    private static SDKCoreHelper sInstance;

    private Context mContext;
    private ECDevice.ECConnectState mConnect = ECDevice.ECConnectState.CONNECT_FAILED;
    private ECInitParams mInitParams;
    private ECInitParams.LoginMode mMode = ECInitParams.LoginMode.FORCE_LOGIN;
    /** 初始化错误 */
    public static final int ERROR_CODE_INIT = -3;

    public static final int WHAT_SHOW_PROGRESS = 0x101A;
    public static final int WHAT_CLOSE_PROGRESS = 0x101B;
    private boolean mKickOff = false;
    private ECNotifyOptions mOptions;
//    public static SoftUdate

    private Handler handler;

    private SDKCoreHelper() {
        initNotifyOptions();
    }

    public static SDKCoreHelper getInstance() {
        if (sInstance == null) {
            sInstance = new SDKCoreHelper();
        }
        return sInstance;
    }

    public synchronized void setHandler(final Handler handler) {
        this.handler = handler;
    }

    public static boolean isKickOff() {
        return getInstance().mKickOff;
    }

    public static void init(Context ctx) {
        init(ctx, ECInitParams.LoginMode.AUTO);
    }

    public static void init(Context ctx, ECInitParams.LoginMode mode) {
        getInstance().mKickOff = false;
        ctx = ECApplication.getInstance().getApplicationContext();
        getInstance().mMode = mode;
        getInstance().mContext = ctx;
        //判断SDK是否已经初始化，没有初始化则先初始化SDK
        if (!ECDevice.isInitialized()) {
            getInstance().mConnect = ECDevice.ECConnectState.CONNECTING;
            ECDevice.initial(ctx, getInstance());

            postConnectNotify();
        }
        //已经初始化成功，直接进行注册
        getInstance().onInitialized();
    }

    private static void postConnectNotify() {
        if (getInstance().mContext instanceof LauncherActivity) {
            ((LauncherActivity) getInstance().mContext).onNetWorkNotify(getConnectState());
        }
    }

    /**
     * 获取连接状态
     * @return      ECDevice.ECConnectState
     */
    public static ECDevice.ECConnectState getConnectState() {
        return getInstance().mConnect;
    }

    private void initNotifyOptions() {
        if (mOptions == null) {
            mOptions = new ECNotifyOptions();
        }
        // 设置新消息是否提醒
        mOptions.setNewMsgNotify(true);
        // 设置状态栏通知图标
        mOptions.setIcon(R.drawable.ic_launcher);
        // 设置是否启用勿扰模式（不会声音/震动提醒）
        mOptions.setSilenceEnable(false);
        // 设置勿扰模式时间段（开始小时/开始分钟-结束小时/结束分钟）
        // 小时采用24小时制
        // 如果设置勿扰模式不启用，则设置勿扰时间段无效
        // 当前设置晚上11点到第二天早上8点之间不提醒
        mOptions.setSilenceTime(23, 0, 8, 0);
        // 设置是否震动提醒(如果处于免打扰模式则设置无效，没有震动)
        mOptions.enableShake(true);
        // 设置是否声音提醒(如果处于免打扰模式则设置无效，没有声音)
        mOptions.enableSound(true);
    }

    @Override
    public void onInitialized() {
        LogUtil.d(LOG_TAG, "ECSDK is ready");

        //设置消息提醒
        ECDevice.setNotifyOptions(mOptions);
        IMChattingHelper.getInstance().initManager();
        //设置接收VoIP来电事件通知Intent
        //呼入界面activity、开发者需修改该类
//        Intent intent = new Intent(getInstance().mContext, VoIPCallActivity.class);
    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onConnect() {

    }

    @Override
    public void onDisconnect(ECError ecError) {

    }

    @Override
    public void onConnectState(ECDevice.ECConnectState ecConnectState, ECError ecError) {

    }

    /**
     * 获取IM聊天功能接口
     * @return
     */
    public static ECChatManager getECChatManager() {
        return ECDevice.getECChatManager();
    }

    /**
     * 获取群组聊天接口
     * @return          ECGroupManager
     */
    public static ECGroupManager getECGroupManager() {
        return ECDevice.getECGroupManager();
    }

    public static ECDeskManager getECDeskManager() {
        return ECDevice.getECDeskManager();
    }

    /**
     * VoIP呼叫接口
     * @return      ECVoIPCallManager
     */
    public static ECVoIPCallManager getVoIPCallManager() {
        return ECDevice.getECVoIPCallManager();
    }

    public static ECVoIPSetupManager getVoIPSetupManager() {
        return ECDevice.getECVoIPSetupManager();
    }


    /**
     * 判断服务是否自动重启
     * @return      是否自动重启
     */
    public static boolean isUIShowing() {
        return ECDevice.isInitialized();
    }

    /**
     * 是否支持voip及会议功能
     * true 表示支持 false表示不支持
     * 请在sdk初始化完成之后调用
     */
    public boolean isSupportMedia() {
        return ECDevice.isSupportMedia();
    }

    public static class SoftUpdate  {
        public String version;
        public String desc;
        public boolean force;

        public SoftUpdate(String version ,String desc, boolean force) {
            this.version = version;
            this.force = force;
            this.desc = desc;
        }
    }
}
