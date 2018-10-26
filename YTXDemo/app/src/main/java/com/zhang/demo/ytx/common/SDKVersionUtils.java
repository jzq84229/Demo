package com.zhang.demo.ytx.common;

import android.os.Build;

/**
 * Created by Administrator on 2016/7/5.
 */
public class SDKVersionUtils {

    public static boolean isSmallerVersion(int version) {
        return Build.VERSION.SDK_INT < version;
    }

    public static boolean isGreaterorEqual(int version) {
        return Build.VERSION.SDK_INT >= version;
    }

    public static boolean isSmallerorEqual(int version) {
        return Build.VERSION.SDK_INT <= version;
    }
}

