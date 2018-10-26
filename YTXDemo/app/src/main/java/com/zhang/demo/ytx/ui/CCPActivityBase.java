package com.zhang.demo.ytx.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.input.InputManager;
import android.media.AudioManager;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.zhang.demo.ytx.R;
import com.zhang.demo.ytx.common.AudioManagerTools;
import com.zhang.demo.ytx.common.base.CCPLayoutListenerView;
import com.zhang.demo.ytx.common.utils.EmotionUtil;
import com.zhang.demo.ytx.common.utils.LogUtil;
import com.zhang.demo.ytx.common.view.TopBarView;
import com.zhang.demo.ytx.common.view.VerticalImageSpan;

import java.util.ArrayList;
import java.util.List;

/**
 * 界面处理
 * Fragment、Activity界面操作抽象类，
 * Fragment具体实现类{@link CCPFragmentImpl}，
 * Activity具体实现类{@link CCPActivityImpl}
 * 设置页面根视图，actionBar视图
 * Created by Administrator on 2016/7/8.
 */
public abstract class CCPActivityBase {
    private FragmentActivity mActionBarActivity;
    private AudioManager mAudioManager;

    /** CCPActivity root view */
    private View mContentView;                      //用户定义的页面视图
    private LayoutInflater mLayoutInflater;
    /** CCPActivity root View container */
    private FrameLayout mContentFrameLayout;        //Activity 根视图
    /** Manager dialog */
    private List<Dialog> mAppDialogCache;

    /** The volume of music */
    private int mMusicMaxVolume;
    public View mBaseLayoutView;                    //Activity 视图
    private View mTransLayerView;

    public CharSequence mTitleText;
    /** The client mute, do not accept message Notification */
    private VerticalImageSpan mMuteIcon;

    /** 标题 */
    private View mTopBarView;

    /** Whether the mute of receive new message */
    private boolean isMute = false;

    /** 抽象方法, The sub Activity implement, set the UI layout */
    protected abstract int getLayoutId();
    protected abstract View getContentLayoutView();
    protected abstract String getClassName();

    protected abstract void onInit();
    protected abstract void dealContentView(View contentView);

    /**
     * 获取标题栏布局文件，
     * 子类重载该方法自定义标题布局文件
     */
    public int getTitleLayout() {
        return R.layout.ec_title_view_base;
    }

    public void init(Context context, FragmentActivity activity) {
        mActionBarActivity = activity;
        onInit();

        mAudioManager = AudioManagerTools.getInstance().getAudioManager();
        mMusicMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        int layoutId = getLayoutId();
        mLayoutInflater = LayoutInflater.from(mActionBarActivity);
        mBaseLayoutView = mLayoutInflater.inflate(R.layout.ccp_activity, null);
        mTransLayerView = mBaseLayoutView.findViewById(R.id.ccp_trans_layer);
        LinearLayout mRootView = (LinearLayout) mBaseLayoutView.findViewById(R.id.ccp_root_view);   //页面布局容器
        mContentFrameLayout = (FrameLayout) mBaseLayoutView.findViewById(R.id.ccp_content_fl);      //页面根视图

        //设置标题栏视图
        if (getTitleLayout() != -1) {
            mTopBarView = mLayoutInflater.inflate(getTitleLayout(), null);
            mRootView.addView(mTopBarView,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        if (layoutId != -1) {
            mContentView = getContentLayoutView();
            if (mContentView == null) {
                mContentView = mLayoutInflater.inflate(getLayoutId(), null);
            }
            //设置页面视图
            mRootView.addView(mContentView,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
        }

        dealContentView(mBaseLayoutView);

        CCPLayoutListenerView listenerView = (CCPLayoutListenerView) mActionBarActivity.findViewById(R.id.ccp_content_fl);
        if (listenerView != null && mActionBarActivity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE) {
            listenerView.setOnSizeChangedListener(new CCPLayoutListenerView.OnCCPViewSizeChangedListener() {
                @Override
                public void onSizeChanged(int w, int h, int oldw, int oldh) {
                    LogUtil.d(LogUtil.getLogUtilsTag(getClass()), "oldh - h = " + (oldh - h));
                }
            });
        }
    }

    /** 隐藏ActionBar */
    public final void hideTitleView() {
        LogUtil.d(LogUtil.getLogUtilsTag(CCPActivityBase.class), "hideTitleView hasTitle :" + (mTopBarView != null ? true : false));
        if (mTopBarView != null) {
            mTopBarView.setVisibility(View.GONE);
        }
    }

    /** 显示ActionBar */
    public final void showTitleView() {
        if (mTopBarView != null) {
            mTopBarView.setVisibility(View.VISIBLE);
        }
    }

    /** ActionBar是否显示 */
    public final boolean isTitleShowing() {
        LogUtil.d(LogUtil.getLogUtilsTag(CCPActivityBase.class), "isTitleShowing hasTitle :" + (mTopBarView != null? true: false));
        if (mTopBarView == null) {
            return false;
        }
        return mTopBarView.getVisibility() == View.VISIBLE;
    }

    /** The height of actionBar */
    public final int getActionBarHeight() {
        if (mTopBarView == null) {
            return 0;
        }
        return mTopBarView.getHeight();
    }

    /**
     * 获取用户自定义页面视图
     */
    public View getActivityLayoutView() {
        return mContentView;
    }

    /**
     * 获取Activity页面视图
     */
    public View getContentView() {
        return mBaseLayoutView;
    }

    public void setActioniBarVisiable(int visiable) {
        if (mTopBarView == null) {
            return;
        }
        if (visiable == View.VISIBLE) {
            showTitleView();
            return;
        }
        hideTitleView();
    }

    /**
     * 获取activity对象
     * @return      mActionBarActivity
     */
    public FragmentActivity getFragmentActivity() {
        return mActionBarActivity;
    }

    public final void setActionContentDescription(CharSequence contentDescription) {
        if (TextUtils.isEmpty(contentDescription)) {
            return;
        }
        String description = mActionBarActivity.getString(R.string.common_enter_activity) + contentDescription;
        //设置View的备注说明，作为一种辅助功能提供,为一些没有文字描述的View提供说明
        mActionBarActivity.getWindow().getDecorView().setContentDescription(description);
    }

    /**
     * hide inputMethod
     */
    public void toggleSoftInput() {
        final FragmentActivity activity = mActionBarActivity;
        //Display the soft keyboard 1.得到InputMethodManager对象
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View localView = activity.getCurrentFocus();
            if (localView != null && localView.getWindowToken() != null) {
                //2.调用toggleSoftInput方法，实现切换显示软键盘的功能。
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /** 隐藏软件盘 */
    public void hideSoftKeyboard(View view) {
        if (view == null) {
            return;
        }

        InputMethodManager inputMethodManager = (InputMethodManager) mActionBarActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            IBinder localIBinder = view.getWindowToken();
            if (localIBinder != null) {
                inputMethodManager.hideSoftInputFromWindow(localIBinder, 0);
            }
        }
    }

    /** 隐藏软件盘 */
    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) mActionBarActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            View localView = mActionBarActivity.getCurrentFocus();
            if (localView != null && localView.getWindowToken() != null) {
                IBinder windowToken = localView.getWindowToken();
                inputMethodManager.hideSoftInputFromWindow(windowToken, 0);
            }
        }
    }

    /**
     * 设置页面尺寸变化监听
     * @param layoutListenerView
     */
    public final void setRootConsumeWather(CCPLayoutListenerView layoutListenerView) {
        if (!(this.mContentFrameLayout instanceof CCPLayoutListenerView)) {
            return;
        }
        ((CCPLayoutListenerView) this.mContentFrameLayout).setRootConsumeWatcher();
    }

    /** 获取最大音量值 */
    public int getStreamMaxVolume() {
        return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
    }

    /** 获取当前音量 */
    public int getStreamVolume() {
        return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    /**
     * 设置ActionBar标题
     * @param title     标题内容
     */
    public final void setActionBarTitle(CharSequence title) {
        if (mTopBarView == null) {
            return;
        }
        mTitleText = title;
        if (mTopBarView instanceof TopBarView) {
            ((TopBarView) mTopBarView).setTitle(title != null ? title.toString() : "");
        }
        setActionContentDescription(title);
    }

    /**
     * 获取ActionBar标题
     * @return      标题内容
     */
    public final CharSequence getActionBarTitle() {
        return mTitleText;
    }

    /**
     * 获取ActionBar对象
     * @return      mTopBarView
     */
    public final TopBarView getTitleBar() {
        return (TopBarView) mTopBarView;
    }

    /**
     * 监听按键点击事件
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //增加音量
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP
                && mAudioManager != null) {
            //获取当前音量
            int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (streamVolume >= mMusicMaxVolume) {
                LogUtil.d(LogUtil.getLogUtilsTag(BaseFragment.class), "has set the max volume");
                return true;
            }
            //将音量平均分为7份，mean为每份音量的值
            int mean = mMusicMaxVolume / 7;
            if (mean == 0) {
                mean = 1;
            }
            //设置音量
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                    streamVolume + mean,
                    AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
        }
        //降低音量
        if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN && mAudioManager != null) {
            int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            int mean = mMusicMaxVolume / 7;
            if (mean == 0) {
                mean = 1;
            }
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                    streamVolume - mean,
                    AudioManager.FLAG_PLAY_SOUND | AudioManager.FLAG_SHOW_UI);
            return true;
        }
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_UP) {
            /*if(mOverFlowAction != null && mOverFlowAction.isEnabled()) {
				callMenuCallback(mOverFlowMenuItem, mOverFlowAction);
				return true;
			}*/
        }
        return false;
    }

    public void onResume() {}

    public void onPause() {}

    public void onStart() {}

    public void onStop() {}

    public void onDestroy() {
        releaseDialogList();
        mAudioManager = null;
        mTopBarView = null;
    }

    private void releaseDialogList() {
        if (mAppDialogCache == null) {
            return;
        }
        for (Dialog dialog : mAppDialogCache) {
            if (dialog == null || !dialog.isShowing()) {
                continue;
            }
            dialog.dismiss();
        }
        mAppDialogCache.clear();
        mAppDialogCache = null;
    }

    public TopBarView getTopBarView() {
        if (mTopBarView instanceof TopBarView) {
            return (TopBarView) mTopBarView;
        }
        return null;
    }


    public void addDialog(Dialog dialog) {
        if (dialog != null) {
            return;
        }
        if (mAppDialogCache == null) {
            mAppDialogCache = new ArrayList<>();
        }
        mAppDialogCache.add(dialog);
    }

    /**
     *
     * @return
     */
    public SpannableString buildActionTitle() {
        int dimensionPixelSize = mActionBarActivity.getResources().getDimensionPixelSize(R.dimen.BigTextSize);
        int mutIndex = 0;
        String format = "%s";
        if (isMute) {
            format = format + " #";
            mutIndex += 2;
        }

        SpannableString spannableString = EmotionUtil.getTextFormat(mActionBarActivity, String.format(format, new Object[]{mTitleText}), dimensionPixelSize);
        if (isMute) {
            if (mMuteIcon == null) {
                mMuteIcon = getTitleIconTips(dimensionPixelSize, R.drawable.chat_mute_notify_title_icon);
            }
            int length = spannableString.length() - mutIndex + 1;
            spannableString.setSpan(mMuteIcon, length, length + 1, SpannableStringBuilder.SPAN_POINT_MARK);
        }
        return spannableString;
    }

    /**
     * 生成标题Icon图标的VerticalImageSpan对象
     * @param padding       padding
     * @param iconRes       资源id
     * @return              VerticalImageSpan
     */
    private VerticalImageSpan getTitleIconTips(int padding, int iconRes) {
        Drawable drawable = mActionBarActivity.getResources().getDrawable(iconRes);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        VerticalImageSpan imageSpan = new VerticalImageSpan(drawable);
        imageSpan.setPadding((drawable.getIntrinsicHeight() - padding) / 2);
        return imageSpan;
    }

    /**
     *
     * @param mute
     * @return
     */
    public CharSequence setMute(boolean mute) {
        isMute = mute;
        return buildActionTitle();
    }

    /** 通知系统更新菜单 */
    public final void invalidateActionMenu() {
        mActionBarActivity.supportInvalidateOptionsMenu();
    }

    public void setScreenEnable(boolean screenEnable) {

    }









}
