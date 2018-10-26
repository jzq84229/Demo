package com.zhang.demo.ytx.ui.chatting;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.KeyEvent;

import com.zhang.demo.ytx.R;
import com.zhang.demo.ytx.common.CCPAppManager;
import com.zhang.demo.ytx.common.utils.CrashHandler;
import com.zhang.demo.ytx.common.utils.LogUtil;
import com.zhang.demo.ytx.ui.SDKCoreHelper;
import com.zhang.demo.ytx.ui.base.ECFragmentActivity;
import com.zhang.demo.ytx.ui.chatting.view.CCPChattingFooter2;

/**
 * 聊天界面
 */
public class ChattingActivity extends ECFragmentActivity implements ChattingFragment.OnChattingAttachListener {
    private static final String LOG_TAG = ChattingActivity.class.getSimpleName();
    public ChattingFragment mChattingFragment;
    private MyReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        //设置窗口支持透明
        getWindow().setFormat(PixelFormat.TRANSPARENT);

        myReceiver = new MyReceiver();
        String recipients = getIntent().getStringExtra(ChattingFragment.RECIPIENTS);
        if (recipients == null) {
            finish();
            LogUtil.e(LOG_TAG, "recipients is null !!");
            return;
        }
        setContentView(R.layout.activity_chatting);
        mChattingFragment = new ChattingFragment();
        Bundle bundle = getIntent().getExtras();
        bundle.putBoolean(ChattingFragment.FROM_CHATTING_ACTIVITY, true);
        mChattingFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.ccp_root_view, mChattingFragment).commit();
        onActivityCreate();

        if (isChatToSelf(recipients) || isPeerChat(recipients)) {
            AppPanelControl.setShowVoipCall(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CrashHandler.getInstance().setContext(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.yuntongxun.ecdemo.removemember");
        filter.addAction(SDKCoreHelper.ACTION_KICK_OFF);
        registerReceiver(myReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }

    /**
     * 是否自己聊天
     * @param sessionId
     * @return
     */
    private boolean isChatToSelf(String sessionId) {
        String userId = CCPAppManager.getUserId();
        return sessionId != null && (sessionId.equalsIgnoreCase(userId) ? true : false);
    }

    private boolean isPeerChat(String sessionId) {
        return sessionId.toLowerCase().startsWith("g");
    }

    @Override
    public void onChattingAttach() {
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (mChattingFragment != null
                && mChattingFragment.onKeyDown(event.getKeyCode(), event)) {
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtil.d(LOG_TAG, "chatting ui on key down, " + keyCode + ", " + event);
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && CCPChattingFooter2.isRecodering) {
            // do nothing.
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    private class MyReceiver extends BroadcastReceiver {
        // 可用Intent的getAction()区分接收到的不同广播
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            if (intent.getAction().equals(SDKCoreHelper.ACTION_KICK_OFF)
                    || intent.getAction().equals("com.yuntongxun.ecdemo.removemember")) {
                finish();
            }
        }
    }
}
