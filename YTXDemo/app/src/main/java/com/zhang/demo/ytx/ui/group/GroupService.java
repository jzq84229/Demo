package com.zhang.demo.ytx.ui.group;

import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECGroupManager;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.im.ECGroup;
import com.yuntongxun.ecsdk.im.ECGroupMember;
import com.yuntongxun.ecsdk.im.ECGroupOption;
import com.zhang.demo.ytx.common.utils.ToastUtil;
import com.zhang.demo.ytx.storage.GroupSqlManager;
import com.zhang.demo.ytx.ui.SDKCoreHelper;

import java.security.acl.Group;
import java.util.List;

/**
 * 群组同步, 单列
 * Created by Administrator on 2016/7/12.
 */
public class GroupService {
    private static final String LOG_TAG = GroupService.class.getSimpleName();

    public static final String ACTION_SYNC_GROUP = "com.yuntongxun.ecdemo.ACTION_SYNC_GROUP";
    public static final String PRICATE_CHATROOM = "@priategroup.com";
    private static GroupService sInstance;
    private ECGroupManager mGroupManager;
    private List<String> mGroupIds;
    private Callback mCallback;
    private Callback mDisCallback;

    private boolean isSync = false;

    private GroupService() {
        mGroupManager = SDKCoreHelper.getECGroupManager();
        countGroups();
    }

    private static GroupService getInstance() {
        if (sInstance == null) {
            sInstance = new GroupService();
        }
        return sInstance;
    }

    /**
     * 同步所有的群组
     */
    private void countGroups() {
        mGroupIds = GroupSqlManager.getAllGroupId();
    }

    /**
     * 同步群组信息
     * @param groupId       群组ID
     */
    public static void syncGroupInfo(final String groupId) {
        ECGroupManager groupManager = SDKCoreHelper.getECGroupManager();
        if (groupManager == null) {
            return;
        }
        groupManager.getGroupDetail(groupId, new ECGroupManager.OnGetGroupDetailListener() {
            @Override
            public void onGetGroupDetailComplete(ECError ecError, ECGroup ecGroup) {
                if (getInstance().isSuccess(ecError)) {
                    if (ecGroup == null) {
                        return;
                    }
                    //更新群组信息
                    GroupSqlManager.updateGroup(ecGroup);
                    if (getInstance().mCallback != null) {
                        getInstance().mCallback.onSyncGroupInfo(groupId);
                    }
                    return;
                }
                onErrorCallback(ecError.errorCode, "修改群组信息失败");
            }
        });
    }

    private static void onErrorCallback(int code, String msg) {
        if (getInstance().mCallback != null) {
            getInstance().mCallback.onError(new ECError(code, msg));
        }
        ToastUtil.showMessage(msg + "[" + code + "]");
    }

    public static void setGroupMessageOption(final ECGroupOption option, final GroupOptionCallback listener) {
        getGroupManager();
        getInstance().mGroupManager.setGroupMessageOption(option, new ECGroupManager.OnSetGroupMessageOptionListener(){
            @Override
            public void onSetGroupMessageOptionComplete(ECError ecError, String groupId) {
                if (getInstance().isSuccess(ecError)) {
                    GroupSqlManager.updateGroupNotify(option.getRule().ordinal(), option.getGroupId());
                    if (listener != null) {
                        listener.onComplete(option.getGroupId());
                    }
                    return;
                }
                if (listener != null) {
                    listener.onError(ecError);
                }
                ToastUtil.showMessage("操作失败[" + ecError.errorCode + "]");
            }
        });

    }

    /**
     * 请求是否成功
     * @param error
     * @return
     */
    private boolean isSuccess(ECError error) {
        if (error.errorCode == SdkErrorCode.REQUEST_SUCCESS) {
            return true;
        }
        return false;
    }

    public static void getGroupManager() {
        getInstance().mGroupManager = SDKCoreHelper.getECGroupManager();
    }

    public interface Callback{
        void onSyncGroup();
        void onSyncGroupInfo(String groupId);
        void onGroupDel(String groupId);
        void onError(ECError error);
        void onUpdateGroupAnonymitySuccess(String groupId, boolean isAnonymity);
    }

    public interface GroupCardCallBack{
        void onQueryGroupCardSuccess(ECGroupMember member);
        void onQueryGroupCardFailed(ECError error);

        void onModifyGroupCardSuccess();
        void onModifyGroupCardFailed(ECError error);


    }

    public interface GroupOptionCallback {
        void onComplete(String groupId);
        void onError(ECError error);
    }

    public interface OnApplyGroupCallbackListener {
        void onApplyGroup(boolean success);
    }

    public interface OnAckGroupServiceListener {
        void onAckGroupService(boolean success);
    }
}
