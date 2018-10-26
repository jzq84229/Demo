package com.zhang.common.lib.util;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;
import android.telephony.TelephonyManager;

/**
 */

public class SystemUtils {
    /**
     * IMEI
     * 获取手机DeviceId
     */
    public static String getDeviceId(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * 唤醒屏幕并解锁
     * 需要权限：android.permission.DISABLE_KEYGUARD
     */
    public static void wakeAndUnlock(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        if (!pm.isScreenOn()) {
            //获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
            //点亮屏幕
            wl.acquire();
            //释放
            wl.release();
            //得到键盘锁管理器对象
            KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
            //解锁
            kl.disableKeyguard();
        }
    }
}
