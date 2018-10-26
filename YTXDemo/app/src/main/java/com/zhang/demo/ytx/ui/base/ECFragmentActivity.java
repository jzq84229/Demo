package com.zhang.demo.ytx.ui.base;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhang.demo.ytx.R;
import com.zhang.demo.ytx.common.AnimatorUtils;
import com.zhang.demo.ytx.common.MethodInvoke;
import com.zhang.demo.ytx.common.SDKVersionUtils;
import com.zhang.demo.ytx.common.SwipeActivityManager;
import com.zhang.demo.ytx.common.utils.LogUtil;
import com.zhang.demo.ytx.common.utils.SupportSwipeModeUtils;
import com.zhang.demo.ytx.common.view.SwipeBackLayout;

/**
 * 在页面根视图添加SwipeBackLayout控件,
 * 并设置页面切换动画
 * Created by Administrator on 2016/7/5.
 */
public class ECFragmentActivity extends FragmentActivity
        implements SwipeActivityManager.SwipeListener, SwipeBackLayout.OnSwipeGestureDelegate{
    private static final String LOG_TAG = ECFragmentActivity.class.getSimpleName();

    public SwipeBackLayout mSwipeBackLayout;
    public boolean mOnDragging;
    private WindowAnimation mAnimation = new WindowAnimation();

    /**
     * 设置Activity的切换动画
     */
    private void onStartActivityAction(Intent intent) {
        if (intent == null) {
            super.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
            return;
        }
        String className = null;
        ComponentName component = intent.getComponent();
        if (component != null) {
            className = component.getClassName();
            if (!className.startsWith(component.getPackageName())) {
                className = component.getPackageName() + component.getClassName();
            }
        } else {
            return;
        }

        if ((0x2 & MethodInvoke.getTransitionValue(className)) != 0) {
            super.overridePendingTransition(mAnimation.openEnter, mAnimation.openExit);
            return;
        }

        if ((0x4 & MethodInvoke.getTransitionValue(className)) != 0) {
            MethodInvoke.startTransitionNotChange(this);
            return;
        }
        MethodInvoke.startTransitionPopin(this);
    }

    public boolean onActivityCreate() {
        if (isEnableSwipe()) {
            ViewGroup viewGroup = (ViewGroup) getWindow().getDecorView();
            mSwipeBackLayout = (SwipeBackLayout) LayoutInflater.from(this).inflate(R.layout.swipeback_layout, viewGroup, false);
            mSwipeBackLayout.init();
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getWindow().getDecorView().setBackgroundDrawable(null);
            ViewGroup childAtView = (ViewGroup) viewGroup.getChildAt(0);
            childAtView.setBackgroundResource(R.drawable.transparent);
            viewGroup.removeView(childAtView);
            mSwipeBackLayout.addView(childAtView);
            mSwipeBackLayout.setContentView(childAtView);
            viewGroup.addView(this.mSwipeBackLayout);
            mSwipeBackLayout.setSwipeGestureDelegate(this);
            return true;
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSwipeBackLayout != null) {
            mSwipeBackLayout.setEnableGesture(false);
        }
        if (!isFinishing()) {
            SwipeActivityManager.pushCallback(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SwipeActivityManager.popCallback(this);
        if (mSwipeBackLayout != null) {
            mSwipeBackLayout.setEnableGesture(true);
            mSwipeBackLayout.mScrolling = false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private boolean isSupperSwipe() {
        if (SupportSwipeModeUtils.isEnable()) {
            if (isEnableSwipe()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否允许滑动
     */
    protected boolean isEnableSwipe() {
        return true;
    }

    @Override
    public void onScrollParent(float scrollPercent) {
        LogUtil.v(LOG_TAG, "on swipe " + scrollPercent + " ,duration " + Long.valueOf(240L));
        View decorView = getWindow().getDecorView();
        if ((decorView instanceof ViewGroup) && ((ViewGroup) decorView).getChildCount() > 0) {
            decorView = ((ViewGroup) decorView).getChildAt(0);
        }
        if (Float.compare(1.0F, scrollPercent) <= 0) {
            AnimatorUtils.startViewAnimation(decorView, 0.0F);
            return;
        }
        AnimatorUtils.startViewAnimation(decorView, -1.0F * decorView.getWidth() / 4 * (1.0F - scrollPercent));
    }

    @Override
    public void notifySettle(boolean open, int speed) {
        LogUtil.v(LOG_TAG, "on settle " + open + ", speed " + speed);
        View decorView = getWindow().getDecorView();
        if ((decorView instanceof ViewGroup) && ((ViewGroup) decorView).getChildCount() > 0) {
            decorView = ((ViewGroup) decorView).getChildAt(0);
        }
        long duration = 120L;
        if (speed <= 0) {
            duration = 240L;
        }
        if (open) {
            AnimatorUtils.updateViewAnimation(decorView, duration, 0.0F, null);
            return;
        }
        AnimatorUtils.updateViewAnimation(decorView, duration, -1 * decorView.getWidth() / 4, null);
    }

    @Override
    public boolean isEnableGesture() {
        return false;
    }

    @Override
    public void onSwipeBack() {
        if (!isFinishing()) {
            finish();
        }
        mOnDragging = false;
    }

    @Override
    public void onDragging() {
        mOnDragging = true;
    }

    @Override
    public void onCancel() {
        mOnDragging = false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (isSupperSwipe() && event.getKeyCode() == KeyEvent.KEYCODE_BACK && mSwipeBackLayout.isSwipeBacking()) {
            LogUtil.d(LOG_TAG, "IS SwipeBack ING, ignore KeyBack Event");
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void finish() {
        super.finish();
        if (isEnableSwipe()) {
            SwipeActivityManager.notifySwipe(1.0F);
        }
        super.overridePendingTransition(mAnimation.closeEnter, mAnimation.closeExit);
        if ((0x2 & MethodInvoke.getAnnotationValue(super.getClass())) == 0) {
            return;
        }
        if ((0x4 & MethodInvoke.getAnnotationValue(super.getClass())) != 0) {
            MethodInvoke.startTransitionNotChange(this);
            return;
        }
        MethodInvoke.startTransitionPopout(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return;
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * 设置页面切换动画
     * @param intents   intents
     */
    @Override
    public void startActivities(Intent[] intents) {
        super.startActivities(intents);
        onStartActivityAction(null);
    }

    @Override
    public void startActivities(Intent[] intents, Bundle options) {
        super.startActivities(intents, options);
        onStartActivityAction(null);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        onStartActivityAction(intent);
    }

    @Override
    public void startActivity(Intent intent, Bundle options) {
        super.startActivity(intent, options);
        onStartActivityAction(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        onStartActivityAction(intent);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        onStartActivityAction(intent);
    }








    public static class WindowAnimation {
        public static int activityOpenEnterAnimation;
        public static int activityOpenExitAnimation;
        public static int activityCloseEnterAnimation;
        public static int activityCloseExitAnimation;

        public int openEnter = activityOpenEnterAnimation;
        public int openExit = activityOpenExitAnimation;
        public int closeEnter = activityCloseEnterAnimation;
        public int closeExit = activityCloseExitAnimation;

        static{
            if (SDKVersionUtils.isSmallerVersion(Build.VERSION_CODES.KITKAT) && SupportSwipeModeUtils.isEnable()) {
                activityOpenEnterAnimation = R.anim.slide_right_in;
                activityOpenExitAnimation = R.anim.slide_right_out;
                activityCloseEnterAnimation = R.anim.slide_left_in;
                activityCloseExitAnimation = R.anim.slide_left_out;
            } else {
                activityOpenEnterAnimation = R.anim.pop_in;
                activityOpenExitAnimation = R.anim.anim_not_change;
                activityCloseEnterAnimation = R.anim.anim_not_change;
                activityCloseExitAnimation = R.anim.pop_out;
            }
        }
    }
}
