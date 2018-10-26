package com.zhang.demo.ytx.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yuntongxun.ecsdk.ECDevice;

/**
 * SDK通知会分三种提醒方式
 * <p>1、直接通过设置的回调接口（OnChatReceiveListener）Push给应用
 * <p>2、如果应用没有设置回调接口则采用（BroadcastReceiver）广播通知（v5.1.8版本）此时如果应用处于未运行状态则会直接唤醒应用处理
 * <p>3、如果应用未处于运行状态并且不想被唤醒应用运行则采用状态栏通知处理（SDK直接提醒，不会通知应用）,比如调用
 * {@link ECDevice#logout(ECDevice.NotifyMode, ECDevice.OnLogoutListener)}退出接口传入后台接收消息才会有提醒
 * @author 容联•云通讯
 * @version 5.1.8
 * @since 2015-10-23
 */
public class YuntxNotifyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

    }
}
