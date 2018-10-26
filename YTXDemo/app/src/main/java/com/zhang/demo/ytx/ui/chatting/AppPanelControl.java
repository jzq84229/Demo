package com.zhang.demo.ytx.ui.chatting;

import android.content.Context;

import com.zhang.demo.ytx.ECApplication;
import com.zhang.demo.ytx.R;
import com.zhang.demo.ytx.common.CCPAppManager;
import com.zhang.demo.ytx.ui.SDKCoreHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 聊天插件功能控制器
 * Created by Administrator on 2016/7/22.
 */
public class AppPanelControl {
    private Context mContext;
    private static boolean isShowVoipCall = true;
    public int[] cap = new int[]{R.string.app_panel_pic,
            R.string.app_panel_tackpic,
            R.string.app_panel_file,
            R.string.app_panel_location};
    public int[] capVoip = new int[]{
            R.string.app_panel_pic,
            R.string.app_panel_tackpic,
            R.string.app_panel_file,
            R.string.app_panel_voice,
            R.string.app_panel_video,
            R.string.app_panel_read_after_fire,
            R.string.app_panel_location};

    public AppPanelControl() {
        mContext = CCPAppManager.getContext();
    }

    public static void setShowVoipCall(boolean isShowVoipCall) {
        AppPanelControl.isShowVoipCall = isShowVoipCall;
    }

    public List<Capability> getCapability() {
        List<Capability> capabilities = new ArrayList<>();
        if (isShowVoipCall && SDKCoreHelper.getInstance().isSupportMedia()) {
            for (int i = 0; i < capVoip.length; i++) {
                Capability capability = getCapability(capVoip[i]);
                capabilities.add(capabilities.size(), capability);
            }
        } else {
            for (int i = 0; i < cap.length; i++) {
                Capability capability = getCapability(cap[i]);
                capabilities.add(capabilities.size(), capability);
            }
        }
        return capabilities;
    }

    private Capability getCapability(int resid) {
        Capability capability = null;
        switch (resid) {
            case R.string.app_panel_pic:
                capability = new Capability(getContext().getString(R.string.app_panel_pic), R.drawable.image_icon);
                break;
            case R.string.app_panel_tackpic:
                capability = new Capability(getContext().getString(R.string.app_panel_tackpic), R.drawable.photograph_icon);
                break;
            case R.string.app_panel_file:
                capability = new Capability(getContext().getString(R.string.app_panel_file), R.drawable.capability_file_icon);
                break;
            case R.string.app_panel_voice:
                capability = new Capability(getContext().getString(R.string.app_panel_voice), R.drawable.voip_call);
                break;
            case R.string.app_panel_video:
                capability = new Capability(getContext().getString(R.string.app_panel_video), R.drawable.video_icon);
                break;
            case R.string.app_panel_read_after_fire:
                capability = new Capability(getContext().getString(R.string.app_panel_read_after_fire), R.drawable.fire_msg);
                break;
            case R.string.app_panel_location:
                capability = new Capability(getContext().getString(R.string.app_panel_location), R.drawable.chat_location_normal);
                break;
        }
        capability.setId(resid);
        return capability;
    }

    private Context getContext() {
        if (mContext == null) {
            mContext = ECApplication.getInstance().getApplicationContext();
        }
        return mContext;
    }
}
