package com.zhang.demo.ytx.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhang.demo.ytx.R;
import com.zhang.demo.ytx.common.utils.LogUtil;
import com.zhang.demo.ytx.common.view.SwipeBackLayout;
import com.zhang.demo.ytx.common.view.TopBarView;
import com.zhang.demo.ytx.ui.CCPActivityBase;
import com.zhang.demo.ytx.ui.CCPFragmentImpl;
import com.zhang.demo.ytx.ui.SDKCoreHelper;

/**
 * 应用页面View基类，每个View必须要继承与该类并实现相应的方法
 * Fragment基类，封装Fragment公共事件,
 * 初始化页面实际展示操作类{@link CCPFragmentImpl}，
 * 注册通用广播接收事件{@link InternalReceiver}
 * Created by Administrator on 2016/7/7.
 */
public abstract class CCPFragment extends Fragment {
    private static final String LOG_TAG = CCPFragment.class.getSimpleName();

    /** 广播拦截器 */
    private InternalReceiver internalReceiver;
    /** 当前页面是否可以销毁 */
    private boolean isFinish = false;

    /** 初始化应用ActionBar */
    private CCPActivityBase mBaseActivity = new CCPFragmentImpl(this);
    public SwipeBackLayout mSwipeBackLayout;

    final Handler mSupperHandler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
        }
    };

    /** 每个页面需要实现该方法返回一个该页面所对应的资源ID */
    public abstract int getLayoutId();

    /** 如果需要自定义页面标题，则需要重载该方法 */
    public int getTitleLayoutId() {
        return R.layout.ec_title_view_base;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBaseActivity.init(getActivity().getBaseContext(), getActivity());
        onFragmentInit();
        abstracrRegist();
    }

    /** Fragment初始化数据 */
    public void onFragmentInit() {}

    /** 注册广播接收器 */
    public void abstracrRegist() {
        registerReceiver(new String[]{SDKCoreHelper.ACTION_KICK_OFF});
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mBaseActivity.setRootConsumeWather(null);
        return mBaseActivity.mBaseLayoutView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            //注销广播监听器
            getActivity().unregisterReceiver(internalReceiver);
        } catch (Exception e) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mBaseActivity.onDestroy();
    }

    @Override
    public void onResume() {
        long currentTimeMillis = System.currentTimeMillis();
        super.onResume();
        this.mBaseActivity.onResume();
        LogUtil.d(LOG_TAG, "KEVIN MMActivity onPause:" + (System.currentTimeMillis() - currentTimeMillis));
    }

    @Override
    public void onPause() {
        long currentTimeMillis = System.currentTimeMillis();
        super.onPause();
        this.mBaseActivity.onPause();
        LogUtil.d(LOG_TAG, "KEVIN MMActivity onPause:" + (System.currentTimeMillis() - currentTimeMillis));
    }

    @Override
    public void onStart() {
        super.onStart();
        this.mBaseActivity.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.mBaseActivity.onStop();
    }

    /**
     * 查找View
     * @param paramInt      viewId
     * @return
     */
    public View findViewById(int paramInt) {
        return getView().findViewById(paramInt);
    }



    /**
     * 注册广播Action，子类如果需要监听广播，
     * 可以调用该方法传入相应事件的Action
     * @param actionArray       监听事件Action
     */
    protected final void registerReceiver(String[] actionArray) {
        if (actionArray == null) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        for (String action : actionArray) {
            intentFilter.addAction(action);
        }
        if (internalReceiver == null) {
            internalReceiver = new InternalReceiver();
        }
        getActivity().registerReceiver(internalReceiver, intentFilter);
    }

    /**
     * 处理按钮按下事件
     * Fragment返回键监听
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mBaseActivity.onKeyDown(keyCode, event)) {
            return true;
        }
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
            return true;
        }
        return false;
    }


    /**
     * Fragment返回事件处理，返回上个fragment或退出Activity
     */
    public void finish() {
        if (getActivity() == null) {
            return;
        }
        if (isFinish) {
            getActivity().finish();
            return;
        }
        getActivity().getSupportFragmentManager().popBackStack();
    }


    /** 返回一个Handler 组线程 */
    public Handler getSupperHandler() {
        return mSupperHandler;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setIsFinish(boolean isFinish) {
        this.isFinish = isFinish;
    }




    public void onBaseContentViewAttach(View contentView) {}

    public TopBarView getTopBarView() {
        return mBaseActivity.getTopBarView();
    }

    public SpannableString setNewMessageMute(boolean mute) {
        mBaseActivity.setMute(mute);
        return mBaseActivity.buildActionTitle();
    }

    /** 设置ActionBar标题 */
    public void setActionBarTitle(int resid) {
        mBaseActivity.setActionBarTitle(getString(resid));
    }

    public void setActionBarTitle(CharSequence text) {
        mBaseActivity.setActionBarTitle(text);
    }

    /** 返回ActionBar标题 */
    public final CharSequence getActionBarTitle() {
        return mBaseActivity.getActionBarTitle();
    }

    public void toggleSoftInput() {
        mBaseActivity.toggleSoftInput();
    }

    public void hideSoftKeyboard() {
        mBaseActivity.hideSoftKeyboard();
    }













    /**
     * 自定义应用全局广播处理器，方便全局拦截广播并进行分发
     */
    private class InternalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || intent.getAction() == null) {
                return;
            }
            handlerReceiver(context, intent);
        }
    }

    /**
     * 如果子界面需要拦截处理注册的广播，需要实现该方法
     * @param context
     * @param intent
     */
    protected void handlerReceiver(Context context, Intent intent) {
        //广播处理
    }
}
