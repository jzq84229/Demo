package com.zhang.demo.ytx.ui;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;

import com.zhang.demo.ytx.R;
import com.zhang.demo.ytx.common.utils.CrashHandler;
import com.zhang.demo.ytx.common.view.TopBarView;
import com.zhang.demo.ytx.ui.base.ECFragmentActivity;

import java.util.Set;

/**
 * Created by Administrator on 2016/7/12.
 */
public abstract class ECSuperActivity extends ECFragmentActivity {
    private static final String LOG_TAG = ECSuperActivity.class.getSimpleName();

    /**
     * 初始化应用ActionBar
     */
    private CCPActivityBase mBaseActivity = new CCPActivityImpl(this);
    /**
     * 初始化广播接收器
     */
    private InternalReceiver internalReceiver;
    private GestureDetector mGestureDetector = null;
    private boolean mIsHorizontalScrolling = false;
    private int mScrollLimit = 0;
    private boolean mIsChildScrolling = false;
    private int mMinExitScrollX = 0;

    /**屏幕资源*/
    private KeyguardManager.KeyguardLock mKeyguardLock = null;
    private KeyguardManager mKeyguardManager = null;
    private PowerManager.WakeLock mWakeLock;
    /**
     * The sub Activity implement, set the Ui Layout
     * @return
     */
    protected abstract int getLayoutId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseActivity.init(getBaseContext(), this);
        onActivityInit();
        abstracrRegist();
        onActivityCreate();
    }

    protected void onActivityInit() {}

    /** 注册广播 */
    public void abstracrRegist() {
        registerReceiver(new String[]{SDKCoreHelper.ACTION_KICK_OFF});
    }

    /**
     * 子类重载该方法自定义标题布局文件
     * @return
     */
    public int getTitleLayout() {
        return R.layout.ec_title_view_base;
    }


    /**
     * 唤醒屏幕资源
     */
    protected void enterIncallMode() {
        if (!mWakeLock.isHeld()) {
            // wake up screen
            // BUG java.lang.RuntimeException: WakeLock under-locked
            mWakeLock.setReferenceCounted(false);
            mWakeLock.acquire();
        }
        mKeyguardLock = this.mKeyguardManager.newKeyguardLock("");
        mKeyguardLock.disableKeyguard();
    }

    /**
     * 初始化资源
     */
    protected void initProwerManager() {
        mWakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,
                "CALL_ACTIVITY#" + super.getClass().getName());
        mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
    }

    /**
     * 释放资源
     */
    protected void releaseWakeLock() {
        try {
            if (this.mWakeLock.isHeld()) {
                if (this.mKeyguardLock != null) {
                    this.mKeyguardLock.reenableKeyguard();
                    this.mKeyguardLock = null;
                }
                this.mWakeLock.release();
            }
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /********************************************************/


    @Override
    protected void onPause() {
        super.onPause();
        mBaseActivity.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CrashHandler.getInstance().setContext(this);
        mBaseActivity.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBaseActivity.onDestroy();
        try {
            unregisterReceiver(internalReceiver);
        } catch (Exception e) {
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mBaseActivity.onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (mBaseActivity.onKeyUp(keyCode, event)) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 注册广播
     * @param actionArray
     */
    protected final void registerReceiver(String[] actionArray) {
        if (actionArray == null) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SDKCoreHelper.ACTION_KICK_OFF);
        for (String action : actionArray) {
            intentFilter.addAction(action);
        }
        if (internalReceiver == null) {
            internalReceiver = new InternalReceiver();
        }
        registerReceiver(internalReceiver, intentFilter);
    }

    /**
     * 如果子界面需要拦截处理注册的广播
     * 需要实现该方法
     * @param context
     * @param intent
     */
    protected void handleReceiver(Context context, Intent intent) {
        //广播处理
        if (intent == null) {
            return;
        }
        if (SDKCoreHelper.ACTION_KICK_OFF.equals(intent.getAction())) {
            finish();
        }
    }



    public void toggleSoftInput() {
        mBaseActivity.toggleSoftInput();
    }

    public void hideSoftKeyBoard() {
        mBaseActivity.hideSoftKeyboard();
    }

    public Activity getActivityContext() {
        if (getParent() != null) {
            return getParent();
        }
        return null;
    }

    /**
     * 跳转
     * @param clazz
     * @param intent
     */
    protected void startCCPActivity(Class<? extends Activity> clazz, Intent intent) {
        intent.setClass(this, clazz);
        startActivity(intent);
    }

    public void onBaseContentViewAttach(View contentView) {
        setContentView(contentView);
    }

    public FragmentActivity getActionBarActivityContext() {
        return mBaseActivity.getFragmentActivity();
    }

    public TopBarView getTopBarView() {
        return mBaseActivity.getTopBarView();
    }

    /**
     * 设置ActionBar标题
     * @param resid
     */
    public void setActionBarTitle(int resid) {
        mBaseActivity.setActionBarTitle(getString(resid));
    }

    /**
     * 设置ActionBar标题
     * @param text
     */
    public void setActionBarTitle(CharSequence text) {
        mBaseActivity.setActionBarTitle(text);
    }

    /**
     * 返回ActionBar 标题
     * @return
     */
    public final CharSequence getActionBarTitle() {
        return mBaseActivity.getActionBarTitle();
    }

    /**
     * getLaoutId()
     * @return
     */
    public View getActivityLayoutView() {
        return mBaseActivity.getActivityLayoutView();
    }

    /**
     * 显示标题栏
     */
    public final void showTitleView() {
        mBaseActivity.showTitleView();
    }

    /**
     * 隐藏标题栏
     */
    public final void hideTitleView() {
        mBaseActivity.hideTitleView();
    }

    public boolean isEnableRightSlideGesture() {
        return true;
    }

    protected Set<View> getReturnInvalidAreaView() {
        return null;
    }















    // Internal calss.
    private class InternalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) {
                return;
            }

            handleReceiver(context, intent);
        }
    }

}

