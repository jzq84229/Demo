package com.zhang.demo.ytx.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.im.ECGroupOption;
import com.yuntongxun.ecsdk.platformtools.ECHandlerHelper;
import com.zhang.demo.ytx.R;
import com.zhang.demo.ytx.common.CCPAppManager;
import com.zhang.demo.ytx.common.dialog.ECProgressDialog;
import com.zhang.demo.ytx.common.utils.ECPreferenceSettings;
import com.zhang.demo.ytx.common.utils.ECPreferences;
import com.zhang.demo.ytx.common.utils.LogUtil;
import com.zhang.demo.ytx.common.utils.ToastUtil;
import com.zhang.demo.ytx.common.view.NetWarnBannerView;
import com.zhang.demo.ytx.storage.GroupSqlManager;
import com.zhang.demo.ytx.storage.IMessageSqlManager;
import com.zhang.demo.ytx.ui.chatting.model.Conversation;
import com.zhang.demo.ytx.ui.contact.ContactLogic;
import com.zhang.demo.ytx.ui.group.GroupService;

/**
 * 会话页面（沟通）
 * Created by Administrator on 2016/7/11.
 */
public class ConversationListFragment extends TabFragment implements CCPListAdapter.OnListAdapterCallBackListener {
    private static final String LOG_TAG = ConversationListFragment.class.getSimpleName();

    /** 会话消息列表ListView */
    private ListView mListView;
    private NetWarnBannerView mBannerView;
    private ConversationAdapter mAdapter;
    private OnUpdateMsgUnreadCountsListener mAttachListener;
    private ECProgressDialog mPostingDialog;

    public interface OnUpdateMsgUnreadCountsListener {
        void OnUpdateMsgUnreadCounts();
    }

    private final AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (mAdapter != null) {
                int headerViewsCount = mListView.getHeaderViewsCount();
                if (position < headerViewsCount) {
                    return;
                }
                int _position = position - headerViewsCount;
                if (mAdapter == null || mAdapter.getItem(_position) == null) {
                    return;
                }
                Conversation conversation = mAdapter.getItem(_position);
                int type = conversation.getMsgType();
                if (type == 1000) {
                    Intent intent = new Intent(getActivity(), GroupNoticeActivity.class);
                    startActivity(intent);
                    return;
                }
                if (ContactLogic.isCustomService(conversation.getSessionId())) {
                    showProcessDialog();
                    dispatchCustomerService(conversation.getSessionId());
                    return;
                }
                CCPAppManager.startChattingAction(getActivity(), conversation.getSessionId(), conversation.getUsername());
            }
        }
    };

    private void dispatchCustomerService(String sessionId) {

    }

    private final AdapterView.OnItemLongClickListener mOnLongItemClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            return false;
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.conversation;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        registerReceiver(new String[]{GroupService.ACTION_SYNC_GROUP, IMessageSqlManager.ACTION_SESSION_DEL});
    }

    @Override
    public void onResume() {
        super.onResume();
        updateConnectState();
        IMessageSqlManager.registerMsgObserver(mAdapter);
        mAdapter.notifyChange();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mAttachListener = (OnUpdateMsgUnreadCountsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnUpdateMsgUnreadCountsListener");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void initView() {
        if (mListView != null) {
            mListView.setAdapter(null);
            if (mBannerView != null) {
                mListView.removeHeaderView(mBannerView);
            }
        }

        mListView = (ListView) findViewById(R.id.main_chatting_lv);
        View mEmptyView = findViewById(R.id.empty_conversation_tv);
        mListView.setEmptyView(mEmptyView);
        mListView.setDrawingCacheEnabled(false);
        mListView.setScrollingCacheEnabled(false);

        mListView.setOnItemLongClickListener(mOnLongItemClickListener);
        mListView.setOnItemClickListener(mItemClickListener);
        mBannerView = new NetWarnBannerView(getActivity());
        mBannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reTryConnect();
            }
        });
        mListView.addHeaderView(mBannerView);
        mAdapter = new ConversationAdapter(getActivity(), this);
        mListView.setAdapter(mAdapter);

        registerForContextMenu(mListView);
    }

    /**
     * 获取自动登录账户名称
     */
    private String getAutoRegistAccount() {
        SharedPreferences sharedPreferences = ECPreferences.getSharedPreferences();
        ECPreferenceSettings registAuto = ECPreferenceSettings.SETTINGS_REGIST_AUTO;
        String registAccount = sharedPreferences.getString(registAuto.getId(), (String) registAuto.getDefaultValue());
        return registAccount;
    }

    /**
     * 重试连接
     */
    private void reTryConnect() {
        ECDevice.ECConnectState connectState = SDKCoreHelper.getConnectState();
        if (connectState == null || connectState == ECDevice.ECConnectState.CONNECT_FAILED) {
            if (!TextUtils.isEmpty(getAutoRegistAccount())) {
                SDKCoreHelper.init(getActivity());
            }
        }
    }

    /**
     * 更新网络连接BannerView状态
     */
    public void updateConnectState() {
        if (!isAdded()) {
            return;
        }
        ECDevice.ECConnectState connect = SDKCoreHelper.getConnectState();
        if (connect == ECDevice.ECConnectState.CONNECTING) {
            mBannerView.setNetWarnText(getString(R.string.connect_server_error));
        } else if (connect == ECDevice.ECConnectState.CONNECT_FAILED) {
            mBannerView.setNetWarnText(getString(R.string.connect_server_error));
        } else if (connect == ECDevice.ECConnectState.CONNECT_SUCCESS) {
            mBannerView.hideWarnBannerView();
        }
        LogUtil.d(LOG_TAG, "updateConnectState connect :" + connect.name());
    }

    private Boolean handleContentMenuClick(final int conversion, int position) {
        if (mAdapter != null) {
            int headerViewsCount = mListView.getHeaderViewsCount();
            if (conversion < headerViewsCount) {
                return false;
            }
            int _position = conversion - headerViewsCount;
            if (mAdapter == null || mAdapter.getItem(_position) == null) {
                return false;
            }
            final Conversation conversation = mAdapter.getItem(_position);
            switch (position) {
                case 0:     //删除会话
                    showProcessDialog();
                    ECHandlerHelper handlerHelper = new ECHandlerHelper();
                    handlerHelper.postRunnOnThead(new Runnable() {
                        @Override
                        public void run() {
                            //删除会话及会话消息
                            IMessageSqlManager.deleteChattingMessage(conversation.getSessionId());
                            ToastUtil.showMessage(R.string.clear_msg_success);
                            ConversationListFragment.this.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    dismissPostingDialog();
                                    mAdapter.notifyChange();
                                }
                            });
                        }
                    });
                    break;
                case 1:     //设置、取消群组免打扰
                    showProcessDialog();
                    final boolean notify = GroupSqlManager.isGroupNotify(conversation.getSessionId());
                    ECGroupOption option = new ECGroupOption();
                    option.setGroupId(conversation.getSessionId());
                    option.setRule(notify ? ECGroupOption.Rule.SILENCE : ECGroupOption.Rule.NORMAL);
                    GroupService.setGroupMessageOption(option, new GroupService.GroupOptionCallback(){
                        @Override
                        public void onComplete(String groupId) {
                            if (mAdapter != null) {
                                mAdapter.notifyChange();
                            }
                            ToastUtil.showMessage(notify ? R.string.new_msg_mute_notify : R.string.new_msg_notify);
                            dismissPostingDialog();
                        }

                        @Override
                        public void onError(ECError error) {
                            dismissPostingDialog();
                            ToastUtil.showMessage("设置失败");
                        }
                    });
                    break;
                default:
                    break;
            }
        }
        return null;
    }

    @Override
    protected void onTabFragmentClick() {

    }

    @Override
    protected void onReleaseTabUI() {

    }

    @Override
    public void OnListAdapterCallBack() {
        if (mAttachListener != null) {
            mAttachListener.OnUpdateMsgUnreadCounts();
        }

    }

    void showProcessDialog() {
        mPostingDialog = new ECProgressDialog(ConversationListFragment.this.getActivity(), R.string.login_posting_submit);
        mPostingDialog.show();
    }

    /**
     * 关闭对话框
     */
    private void dismissPostingDialog() {
        if (mPostingDialog == null || !mPostingDialog.isShowing()) {
            return;
        }
        mPostingDialog.dismiss();
        mPostingDialog = null;
    }


}
