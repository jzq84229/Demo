package com.zhang.demo.ytx.common;

import android.app.Activity;
import android.util.Log;

import com.zhang.demo.ytx.common.utils.LogUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Administrator on 2016/7/6.
 */
public class SwipeTranslucentMethodUtils {
    private static final String LOG_TAG = SwipeTranslucentMethodUtils.class.getSimpleName();

    private SwipeTranslucentMethodUtils() {
    }

    /**
     * Convert a translucent themed Activity
     * {@link android.R.attr#windowIsTranslucent} to a fullscreen opaque
     * Activity.
     * <p>
     * Call this whenever the background of a translucent Activity has changed
     * to become opaque. Doing so will allow the {@link android.view.Surface} of
     * the Activity behind to be released.
     * <p>
     * This call has no effect on non-translucent activities or on activities
     * with the {@link android.R.attr#windowIsFloating} attribute.
     */
    public static void convertActivityFromTranslucent(Activity activity) {
        try {
            Method method = Activity.class.getDeclaredMethod("convertFromTranslucent");
            method.setAccessible(true);
            method.invoke(activity);
        } catch (Throwable t) {
        }
    }

    /**
     * Convert a translucent themed Activity
     * {@link android.R.attr#windowIsTranslucent} back from opaque to
     * translucent following a call to
     * {@link #convertActivityFromTranslucent(android.app.Activity)} .
     * <p>
     * Calling this allows the Activity behind this one to be seen again. Once
     * all such Activities have been redrawn
     * <p>
     * This call has no effect on non-translucent activities or on activities
     * with the {@link android.R.attr#windowIsFloating} attribute.
     */
    public static void convertActivityToTranslucent(Activity activity, MethodInvoke.SwipeInvocationHandler handler) {
        try {
            Class<?>[] classes = Activity.class.getDeclaredClasses();
            Class<?> translucentConversionListenerClazz = null;
            for (Class clazz : classes) {
                if (clazz.getSimpleName().contains("TranslucentConversionListener")) {
                    translucentConversionListenerClazz = clazz;
                }
            }

            Object proxy = Proxy.newProxyInstance(translucentConversionListenerClazz.getClassLoader(), new Class[]{translucentConversionListenerClazz}, handler);
            LogUtil.d(LOG_TAG, "proxy" + proxy);

            if (SDKVersionUtils.isGreaterorEqual(21)) {
                Method method = Activity.class.getDeclaredMethod("convertToTranslucent", new Class[]{translucentConversionListenerClazz});
                method.setAccessible(true);
                method.invoke(activity, new Object[]{proxy});
            } else {
                Method method = Activity.class.getDeclaredMethod("convertToTranslucent", new Class[]{translucentConversionListenerClazz});
                method.setAccessible(true);
                method.invoke(activity, new Object[]{proxy, null});
            }
        } catch (Throwable t) {
            LogUtil.e(LOG_TAG, Log.getStackTraceString(t));
        }
    }


}
