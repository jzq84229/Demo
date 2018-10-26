package com.zhang.demo.ytx.common;

import android.app.Activity;
import android.content.Context;
import android.text.Spannable;

import com.zhang.demo.ytx.R;
import com.zhang.demo.ytx.common.utils.LogUtil;
import com.zhang.demo.ytx.ui.ActivityTransition;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 注解 ActivityTransition实现类
 * Created by Administrator on 2016/7/6.
 */
public class MethodInvoke {
    private static final String LOG_TAG = MethodInvoke.class.getSimpleName();

    public static int getTransitionValue(String componentName) {
        Class<?> clazz = getTransitionClass(componentName);
        if (clazz != null) {
            return getAnnotationValue(clazz);
        }
        return 0;
    }

    private static Class<?> getTransitionClass(String componentName) {
        try {
            Class clazz = Class.forName(componentName);
            return clazz;
        } catch (ClassNotFoundException e) {
            LogUtil.e(LOG_TAG, "Class " + componentName + " not found in dex");
            e.printStackTrace();
        }
        return null;
    }

    public static int getAnnotationValue(Class<?> clazz) {
        ActivityTransition clazzAnnotation = clazz.getAnnotation(ActivityTransition.class);
        if (clazzAnnotation != null) {
            return clazzAnnotation.value();
        }
        while (clazz.getSuperclass() != null) {
            clazz = clazz.getSuperclass();
            clazzAnnotation = clazz.getAnnotation(ActivityTransition.class);
            if (clazzAnnotation != null) {
                return clazzAnnotation.value();
            }
        }
        return 0;
    }

    /**
     * 设置Activity进入动画
     * @param context
     */
    public static void startTransitionPopin(Context context) {
        if (context == null || !(context instanceof Activity)) {
            return;
        }
        //设置activity切换动画
        ((Activity)context).overridePendingTransition(R.anim.pop_in, R.anim.anim_not_change);
    }

    /**
     * 设置Activity退出动画
     * @param context
     */
    public static void startTransitionPopout(Context context) {
        if (context == null || !(context instanceof Activity)) {
            return;
        }
        ((Activity) context).overridePendingTransition(R.anim.anim_not_change, R.anim.pop_out);
    }

    /**
     * 设置Activity切换动画
     * @param context
     */
    public static void startTransitionNotChange(Context context) {
        if (context == null || !(context instanceof Activity)) {
            return;
        }
        ((Activity)context).overridePendingTransition(R.anim.anim_not_change, R.anim.anim_not_change);
    }

    /**
     * 滑动监听器
     */
    public interface OnSwipeInvokeResultListener{
        void onSwipeInvoke(boolean success);
    }

    public static class SwipeInvocationHandler implements InvocationHandler {
        public WeakReference<OnSwipeInvokeResultListener> mWeakReference;
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (mWeakReference == null) {
                LogUtil.i(LOG_TAG, "swipe invoke fail, callbackRef NULL!");
                return null;
            }
            OnSwipeInvokeResultListener onSwipeInvokeResultListener = mWeakReference.get();
            if (onSwipeInvokeResultListener == null) {
                LogUtil.i(LOG_TAG, "swipe invoke fail, callback NULL!");
                return null;
            }
            boolean result = false;
            if (args != null) {
                if (args.length > 0) {
                    boolean isBoolArgs = (args[0] instanceof Boolean);
                    if (isBoolArgs) {
                        result = ((Boolean) args[0]).booleanValue();
                    }
                }
            }
            onSwipeInvokeResultListener.onSwipeInvoke(result);
            return null;
        }
    }

}
