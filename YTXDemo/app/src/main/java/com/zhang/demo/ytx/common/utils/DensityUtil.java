package com.zhang.demo.ytx.common.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.zhang.demo.ytx.ECApplication;

/**
 * TODO: 这个类的函数需重新整理
 * Created by Administrator on 2016/7/7.
 */
public class DensityUtil {
    //根据屏幕密度转换
    private static float mPixels = 0.0F;
    private static float density = -1.0F;

    public static int getDisplayMetrics(Context context, float pixels) {
        if (mPixels == 0.0F) {
            mPixels = context.getResources().getDisplayMetrics().density;
        }
        return (int) (0.5F + pixels * mPixels);
    }

    public static int getImageWidth(Context context, float pixels) {
        return context.getResources().getDisplayMetrics().widthPixels - 66 - getDisplayMetrics(context, pixels);
    }

    /**
     * 像素转化为dip
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * dip 转化像素
     */
    public static int dip2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int getMetricsDensity(Context context, float height) {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(localDisplayMetrics);
        return Math.round(height * localDisplayMetrics.densityDpi / 160.0F);
    }


    /** dp转px */
    public static int fromDPToPix(Context context, int dp) {
        return Math.round(getDensity(context) * dp);
    }

    /**
     * 获取屏幕密度
     */
    public static float getDensity(Context context) {
        if (context == null) {
            context = ECApplication.getInstance().getApplicationContext();
        }
        if (density < 0.0F) {
            density = context.getResources().getDisplayMetrics().density;
        }
        return density;
    }

    public static int round(Context context, int paramInt) {
        return Math.round(paramInt / getDensity(context));
    }
}
