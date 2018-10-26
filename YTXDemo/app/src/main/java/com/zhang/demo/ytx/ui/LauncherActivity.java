package com.zhang.demo.ytx.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.platformtools.ECHandlerHelper;
import com.zhang.demo.ytx.R;
import com.zhang.demo.ytx.common.CCPAppManager;
import com.zhang.demo.ytx.common.ECContentObservers;
import com.zhang.demo.ytx.common.base.CCPCustomViewPager;
import com.zhang.demo.ytx.common.base.CCPLauncherUITabView;
import com.zhang.demo.ytx.common.base.OverflowAdapter;
import com.zhang.demo.ytx.common.base.OverflowHelper;
import com.zhang.demo.ytx.common.utils.CrashHandler;
import com.zhang.demo.ytx.common.utils.ECPreferenceSettings;
import com.zhang.demo.ytx.common.utils.ECPreferences;
import com.zhang.demo.ytx.common.utils.LogUtil;
import com.zhang.demo.ytx.core.ClientUser;
import com.zhang.demo.ytx.core.ContactsCache;
import com.zhang.demo.ytx.storage.ContactSqlManager;
import com.zhang.demo.ytx.ui.account.LoginActivity;
import com.zhang.demo.ytx.ui.base.ECFragmentActivity;
import com.zhang.demo.ytx.ui.chatting.IMChattingHelper;
import com.zhang.demo.ytx.ui.contact.ECContacts;

import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.HashMap;

@ActivityTransition(3)
public class LauncherActivity extends ECFragmentActivity implements
        View.OnClickListener,
        View.OnLongClickListener,
        ConversationListFragment.OnUpdateMsgUnreadCountsListener{
    private static final String LOG_TAG = LauncherActivity.class.getSimpleName();
    /** 当前ECLauncherUI实例 */
    public static LauncherActivity mLauncherUI;
    /** 当前ECLauncherUI实例产生的个数 */
    public static int mLauncherInstanceCount = 0;
    /** 当前主界面RootView */
    public View mLauncherView;
    /** LauncherUI 主界面导航控制View，包含三个View Tab按钮 */
    private CCPLauncherUITabView mLauncherUITabView;
    /** 三个TabView所对应的三个页面的适配器 */
    private CCPCustomViewPager mCustomViewPager;

    /** 沟通、联系人、群组适配器 */
    public LauncherViewPagerAdapter mLauncherViewPagerAdapter;
    private OverflowHelper mOverflowHelper;
    /** 当前显示的TabView Fragment */
    private int mCurrentItemPosition = -1;
    /** 会话界面(沟通) */
    private static final int TAB_CONERSATION = 0;           //会话界面(沟通)
    /** 通讯录界面(联系人) */
    private static final int TAB_ADDRESS = 1;               //通讯录界面(联系人)
    /** 群组界面 */
    private static final int TAB_GROUP = 2;                 //群组界面
    private static final int TAB_DISCUSSION_GROUP = 3;      //讨论组界面
    /** {@link CCPLauncherUITabView} 是否已经被初始化 */
    private boolean mTabViewInit = false;



    /** 缓存三个TabView */
    private final HashMap<Integer, Fragment> mTabViewCache = new HashMap<>();
    private OverflowAdapter.OverflowItem[] mItems;
    private InternalReceiver internalReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int pid = Process.myPid();
        if (mLauncherUI != null) {
            LogUtil.i(LogUtil.getLogUtilsTag(LauncherActivity.class), "finish last LauncherUI");
            mLauncherUI.finish();
        }
        mLauncherUI = this;
        mLauncherInstanceCount++;

        super.onCreate(savedInstanceState);
        initWelcome();
        mOverflowHelper = new OverflowHelper(this);
        //设置页面默认为竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ECContentObservers.getInstance().initContentObserver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (internalReceiver != null) {
            unregisterReceiver(internalReceiver);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        CrashHandler.getInstance().setContext(this);
        boolean fullExit = ECPreferences.getSharedPreferences().getBoolean(ECPreferenceSettings.SETTINGS_FULLY_EXIT.getId(), false);
        if (fullExit) {
            try {
                ECHandlerHelper.removeCallbacksRunnOnUI(initRunable);
                ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_FULLY_EXIT, false, true);
                ContactsCache.getInstance().stop();
                CCPAppManager.setClientUser(null);
                ECDevice.unInitial();
                finish();

                Process.killProcess(Process.myPid());
                System.exit(0);
                return;
            } catch (InvalidClassException e) {
                e.printStackTrace();
            }
        }
        if (mLauncherUITabView == null) {
            String account = getAutoRegistAccount();
            if (TextUtils.isEmpty(account)) {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return;
            }
            // 注册第一次登陆同步消息
            registerReceiver(new String[] {
                    IMChattingHelper.INTENT_ACTION_SYNC_MESSAGE,
                    SDKCoreHelper.ACTION_SDK_CONNECT });
            ClientUser user = new ClientUser("").from(account);
            CCPAppManager.setClientUser(user);
            if (!ContactSqlManager.hasContact(user.getUserId())) {
                ECContacts contacts = new ECContacts();
                contacts.setClientUser(user);
                ContactSqlManager.insertContact(contacts);
            }

            if (SDKCoreHelper.getConnectState() != ECDevice.ECConnectState.CONNECT_SUCCESS
                    && !SDKCoreHelper.isKickOff()) {

                ContactsCache.getInstance().load();

                if(!TextUtils.isEmpty(getAutoRegistAccount())){
                    SDKCoreHelper.init(this);
                }
            }
            // 初始化主界面Tab资源
            if (!mInit) {
                initLauncherUIView();
            }
        }
        OnUpdateMsgUnreadCounts();
    }

    /**
     * 检查是否需要自动登录
     * @return
     */
    private String getAutoRegistAccount() {
        SharedPreferences sharedPreferences = ECPreferences.getSharedPreferences();
        ECPreferenceSettings registAuto = ECPreferenceSettings.SETTINGS_REGIST_AUTO;
        String registAccount = sharedPreferences.getString(registAuto.getId(), (String) registAuto.getDefaultValue());
        return registAccount;
    }



    @Override
    protected boolean isEnableSwipe() {
        return false;
    }

    private boolean mInit = false;
    private Runnable initRunable = new Runnable() {
        @Override
        public void run() {
            mInit = false;
            initLauncherUIView();
        }
    };

    /**
     * 初始化欢迎界面
     */
    private void initWelcome() {
        if (!mInit) {
            mInit = true;
            setContentView(R.layout.splash_activity);

            //程序启动开始创建一个splash用来初始化程序基本数据
            ECHandlerHelper.postDelayedRunnOnUI(initRunable, 3000);
        }
    }

    /**
     * 初始化主界面UI视图
     */
    private void initLauncherUIView() {
        mLauncherView = getLayoutInflater().inflate(R.layout.main_tab, null);
        setContentView(mLauncherView);

        mTabViewInit = true;
        mCustomViewPager = (CCPCustomViewPager) findViewById(R.id.pager);
        mCustomViewPager.setOffscreenPageLimit(4);

        if (mLauncherUITabView != null) {
            mLauncherUITabView.setOnUITabViewClickListener(null);
            mLauncherUITabView.setVisibility(View.VISIBLE);
        }

        mLauncherUITabView = (CCPLauncherUITabView) findViewById(R.id.launcher_tab_top);
        mCustomViewPager.setSlideEnable(true);

        mLauncherViewPagerAdapter = new LauncherViewPagerAdapter(getSupportFragmentManager(), mCustomViewPager);
        mLauncherUITabView.setOnUITabViewClickListener(mLauncherViewPagerAdapter);

        findViewById(R.id.btn_plus).setOnClickListener(this);
        findViewById(R.id.btn_plus).setOnLongClickListener(this);
        ctrlViewTab(0);

        Intent intent = getIntent();
        if (intent != null && intent.getIntExtra("launcher_from", -1) == 1) {
            //检测从登陆界面过来，判断是不是第一次安装使用
            checkFirstUse();
        }
        //如果是登陆过来的
        doInitAction();
    }

    /**
     * 根据提供的子Fragment index 切换到对应的页面
     * @param index     子Fragment对应的index
     */
    public void ctrlViewTab(int index) {


    }

    private boolean isFirstUse() {
        boolean firstUse = ECPreferences.getSharedPreferences().getBoolean(ECPreferenceSettings.SETTINGS_FIRST_USE.getId(),
                ((Boolean) ECPreferenceSettings.SETTINGS_FIRST_USE.getDefaultValue()).booleanValue());
        return firstUse;
    }

    private void checkFirstUse() {
        boolean firstUse = isFirstUse();

        //Display the welcome message?
        if (firstUse) {
//            if (IMChattingHel)


            //Don't display again this dialog
            try {
                ECPreferences.savePreference(ECPreferenceSettings.SETTINGS_FIRST_USE, Boolean.FALSE, true);
            } catch (InvalidClassException e) {
                /** NON　BLOCK */
            }
        }
    }

    /**
     * 根据TabFragment Index 查找Fragment
     * @param tabIndex
     * @return
     */
    public final BaseFragment getTabView(int tabIndex) {
        LogUtil.d(LogUtil.getLogUtilsTag(LauncherActivity.class), "get tab index " + tabIndex);
        if (tabIndex < 0) {
            return null;
        }

        if (mTabViewCache.containsKey(Integer.valueOf(tabIndex))) {
            return (BaseFragment) mTabViewCache.get(Integer.valueOf(tabIndex));
        }

        BaseFragment mFragment = null;
        switch (tabIndex) {
            case TAB_CONERSATION:
                //Fragment通过反射获取具体Fragment的对象
                mFragment = (BaseFragment) Fragment.instantiate(this, ConversationListFragment.class.getName(), null);
                break;
            case TAB_ADDRESS:
                mFragment = (BaseFragment) Fragment.instantiate(this, MobileContactFragment.class.getName(), null);
                break;
            case TAB_GROUP:
                mFragment = (BaseFragment) Fragment.instantiate(this, GroupListFragment.class.getName(), null);
                break;
            case TAB_DISCUSSION_GROUP:
                mFragment = (BaseFragment) Fragment.instantiate(this, DiscussionListFragment.class.getName(), null);
                break;
            default:
                break;
        }
        if (mFragment != null) {
            mFragment.setActionBarActivity(this);
        }
        mTabViewCache.put(Integer.valueOf(tabIndex), mFragment);
        return mFragment;
    }

    /**
     * 处理一些初始化操作
     */
    private void doInitAction() {


    }

    private void settingPersonInfo() {
//        if (isUpSetPersonInfo()) {
//
//        }

    }

//    public static boolean isUpSetPersonInfo() {
//        ClientUser user = CCPAppManager.getClientUser();
//    }

    /**
     * 网络注册状态改变
     */
    public void onNetWorkNotify(ECDevice.ECConnectState connect) {
        BaseFragment tabView = getTabView(TAB_CONERSATION);
        if (tabView instanceof ConversationListFragment && tabView.isAdded()) {
            ((ConversationListFragment) tabView).updateConnectState();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_right:
                break;
        }

    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public void OnUpdateMsgUnreadCounts() {

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
        for (String action : actionArray) {
            if (internalReceiver == null) {
                internalReceiver = new InternalReceiver();
            }
            registerReceiver(internalReceiver, intentFilter);
        }
    }

    private class InternalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || TextUtils.isEmpty(intent.getAction())) {
                return;
            }
//            if ()
        }
    }


    /**
     *
     * TabViewPager 页面适配器
     */
    private class LauncherViewPagerAdapter extends FragmentStatePagerAdapter
            implements ViewPager.OnPageChangeListener, CCPLauncherUITabView.OnUITabViewClickListener {

        private int mClickTabCounts;
        private GroupListFragment mGroupListFragment;
        private DiscussionListFragment mDissListFragment;

        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<>();


        public LauncherViewPagerAdapter(FragmentManager fm, ViewPager pager) {
            super(fm);
            this.mViewPager = pager;
            this.mViewPager.setAdapter(this);
            this.mViewPager.addOnPageChangeListener(this);
        }

        public void addTab(String tabSpec, Class<?> clss, Bundle args) {
            String tag = tabSpec;

            TabInfo info = new TabInfo(tag, clss, args);
            mTabs.add(info);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Fragment getItem(int position) {
            return mLauncherUI.getTabView(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
//            LogUtil.d(LogUtil.getLogUtilsTag(LauncherViewPagerAdapter.class), "onPageScrollStateChanged state = " + state);
//            if (state != ViewPager.SCROLL_STATE_IDLE || mGroupListFragment == null) {
//                return;
//            }
//            if (mGroupListFragment != null) {
//                mGroupListFragment.onGroupFragmentVisible(true);
//                mGroupListFragment = null;
//            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (mLauncherUITabView != null) {
                mLauncherUITabView.doChangeTabViewDisplay(position);
                mCurrentItemPosition = position;
            }
        }

        @Override
        public void onTabClick(int tabIndex) {
            if (tabIndex == mCurrentItemPosition) {
                LogUtil.d(
                        LogUtil.getLogUtilsTag(LauncherViewPagerAdapter.class),
                        "on click same index " + tabIndex);
                //Perform a rolling
                TabFragment item = (TabFragment) getItem(tabIndex);
                item.onTabFragmentClick();
                return;
            }

            mClickTabCounts += mClickTabCounts;
            mViewPager.setCurrentItem(tabIndex);
        }


        /**
         * Tab信息封装类
         */
        final class TabInfo {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(String tag, Class<?> clss, Bundle args) {
                this.tag = tag;
                this.clss = clss;
                this.args = args;
            }
        }
    }
}
