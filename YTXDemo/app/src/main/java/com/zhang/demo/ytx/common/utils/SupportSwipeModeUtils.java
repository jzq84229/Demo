package com.zhang.demo.ytx.common.utils;

import android.content.SharedPreferences;
import android.os.Build;

import com.zhang.demo.ytx.common.CCPAppManager;

/**
 * Created by Administrator on 2016/7/5.
 */
public class SupportSwipeModeUtils {
    private static final String LOG_TAG = SupportSwipeModeUtils.class.getSimpleName();

    private static int mode = 0;

    public static void switchSwipebackMode(boolean enable) {
        SharedPreferences sharedPreferences = CCPAppManager.getSharePreference();
        boolean supportSwipe = sharedPreferences.getBoolean("settings_support_swipe", true);
        if (supportSwipe != enable) {
            sharedPreferences.edit().putBoolean("settings_support_swipe", enable).commit();
        }
        LogUtil.d(LOG_TAG, "switchSwipebackMode, from " + supportSwipe + " to " + enable);
    }

    public static boolean isEnable() {
        //判断是否为魅族系统
        if (DemoUtils.nullAsNil(Build.VERSION.INCREMENTAL).toLowerCase().contains("flyme")
                || DemoUtils.nullAsNil(Build.DISPLAY).toLowerCase().contains("flyme")) {
            return false;
        }
        if (mode == 0) {
            if (!CCPAppManager.getSharePreference().getBoolean("setting_support_swipe", true)) {
                mode = 2;
            } else {
                mode = 1;
            }
        }
        return mode == 1;
    }
}
