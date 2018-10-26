package com.zhang.demo.ytx.common.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.zhang.demo.ytx.common.CCPAppManager;

/**
 * 资源文件帮助类
 * Created by Administrator on 2016/7/13.
 */
public class ResourceHelper {
    private static final String LOG_TAG = ResourceHelper.class.getSimpleName();
    private static float density;

    static {
        density = -1.0F;
    }

    /**
     * 把dp转成px
     * @param context   上下文
     * @param ratio     dp尺寸
     * @return          px
     */
    public static int fromDPToPix(Context context, int ratio) {
        return Math.round(getDensity(context) * ratio);
    }

    /**
     * 获取屏幕逻辑密度值density
     * @param context   上下文
     * @return          density
     */
    public static float getDensity(Context context) {
        if (context == null) {
            context = CCPAppManager.getContext();
        }
        if (density < 0F) {
            density = context.getResources().getDisplayMetrics().density;
        }
        return density;
    }

    /**
     * 获取图片Drawable
     * @param context
     * @param resId
     * @return
     */
    public static Drawable getDrawableById(Context context, int resId) {
        if (context == null) {
            return null;
        }
        return context.getResources().getDrawable(resId);
    }

    public static ColorStateList getColorStateList(Context context, int resId) {
        if (context == null) {
            LogUtil.e(LOG_TAG, "get drawable, resId " + resId + ", but context is null");
            return null;
        }
        return context.getResources().getColorStateList(resId);
    }
}
