package com.zhang.demo.zxing.qrcode.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by Administrator on 2016/6/27.
 */
public class Util {
    /** Check if this device has a camera */
    public static boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
}
