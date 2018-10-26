package com.zhang.demo.ytx.common;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.zhang.demo.ytx.common.utils.ECPreferenceSettings;
import com.zhang.demo.ytx.common.utils.ECPreferences;
import com.zhang.demo.ytx.common.utils.LogUtil;
import com.zhang.demo.ytx.core.ClientUser;
import com.zhang.demo.ytx.ui.ECSuperActivity;
import com.zhang.demo.ytx.ui.chatting.ChattingActivity;
import com.zhang.demo.ytx.ui.chatting.ChattingFragment;
import com.zhang.demo.ytx.ui.chatting.view.ChatFooterPanel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 存储SDK一些全局性的常量
 * Created by Administrator on 2016/7/5.
 */
public class CCPAppManager {
    public static Md5FileNameGenerator md5FileNameGenerator = new Md5FileNameGenerator();
    private static Context mContext = null;
    public static String pkgName = "com.zhang.demo.ytx";                    //包名
    public static final String PREFIX = "com.zhang.demo.ytx";               //SharedPreferences 存储名字前缀
    public static final int FLAG_RECEIVER_REGISTERED_ONLY_BEFORE_BOOT = 0x10000000;

    public static final String USER_DATA = "yuntonxun.ecdemo";              //IM功能UserData字段默认文字
    public static HashMap<String, Integer> mPhotoCache = new HashMap<>();
    public static ArrayList<ECSuperActivity> activities = new ArrayList<>();

    /** IM聊天更多功能面板 */
    private static ChatFooterPanel mChatFooterPanel;

    /**
     * 获取包名
     */
    private static String getPackageName() {
        return pkgName;
    }
    private static ClientUser mClientUser;

    /**
     * 返回SharePreference配置文件名称
     */
    public static String getSharePreferenceName() {
        return pkgName + "_preferences";
    }

    public static SharedPreferences getSharePreference() {
        if (mContext != null) {
            return mContext.getSharedPreferences(getSharePreferenceName(), 0);
        }
        return null;
    }

    /**
     * 返回上下文对象
     */
    public static Context getContext() {
        return mContext;
    }

    /**
     * 设置上下文对象
     */
    public static void setContext(Context context) {
        mContext = context;
        pkgName = context.getPackageName();
        LogUtil.d(LogUtil.getLogUtilsTag(CCPAppManager.class), "setup application context for package: " + pkgName);
    }

    public static String getUserId() {
        return getClientUser().getUserId();
    }

    /**
     * 发送移除成员广播
     */
    public static void sendRemoveMemberBR() {
        getContext().sendBroadcast(new Intent("com.yuntongxun.ecdemo.removemember"));
    }

    /**
     * 缓存账号注册信息
     */
    public static void setClientUser(ClientUser user) {
        mClientUser = user;
    }

    /**
     * 保存注册账号信息
     * @return
     */
    public static ClientUser getClientUser() {
        if (mClientUser != null) {
            return mClientUser;
        }
        String registerAccount = getAutoRegisterAccount();
        if (!TextUtils.isEmpty(registerAccount)) {
            mClientUser = new ClientUser("");
            return mClientUser.from(registerAccount);
        }
        return null;
    }

    /**
     * 获取自动登录账号
     */
    private static String getAutoRegisterAccount() {
        SharedPreferences sharedPreferences = ECPreferences.getSharedPreferences();
        ECPreferenceSettings registerAuto = ECPreferenceSettings.SETTINGS_REGIST_AUTO;
        String registerAccount = sharedPreferences.getString(registerAuto.getId(), (String) registerAuto.getDefaultValue());
        return registerAccount;
    }

    public static void setPversion(int version) {
        if (mClientUser != null) {
            mClientUser.setpVersion(version);
        }
    }

    /**
     *  获取应用程序版本名称
     * @return      版本名称
     */
    public static String getVersion() {
        String version = "0.0.0";
        if (mContext == null) {
            return version;
        }
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 获取应用版本号
     * @return      版本号
     */
    public static int getVersionCode() {
        int code = 1;
        if (mContext == null) {
            return code;
        }
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(getPackageName(), 0);
            code = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }

    /**
     * 跳转到聊天界面
     * @param context       上下文
     * @param contactid     联系人ID
     * @param username      联系人名称
     */
    public static void startChattingAction(Context context, String contactid, String username) {
        startChattingAction(context, contactid, username, false);
    }

    /**
     * 跳转到聊天界面
     * @param context       上下文
     * @param contactid     联系人ID
     * @param username      联系人名称
     * @param clearTop      clearTop
     */
    public static void startChattingAction(Context context, String contactid, String username, boolean clearTop) {
        Intent intent = new Intent(context, ChattingActivity.class);
        if (clearTop) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        intent.putExtra(ChattingFragment.RECIPIENTS, contactid);
        intent.putExtra(ChattingFragment.CONTACT_USER, username);
        intent.putExtra(ChattingFragment.CUSTOMER_SERVICE, false);
        context.startActivity(intent);
    }

    public static ChatFooterPanel getChatFooterPanel(Context context) {
        return mChatFooterPanel;
    }
}
