package com.zhang.demo.ytx.storage;

import android.database.Cursor;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/15.
 */
public class GroupMemberSqlManager extends AbstractSQLManager {
    private static final String LOG_TAG = GroupMemberSqlManager.class.getSimpleName();
    Object mLock = new Object();
    private static GroupMemberSqlManager sInstance;

    private GroupMemberSqlManager() {}

    public static GroupMemberSqlManager getInstance() {
        if (sInstance == null) {
            sInstance = new GroupMemberSqlManager();
        }
        return sInstance;
    }


    /**
     * 根据群组ID查询群组成员账号
     * @param groupId       群组ID
     * @return              ArrayList<String>
     */
    public static ArrayList<String> getGroupMemberID(String groupId) {
        String sql = "select " + GroupMembersColumn.VOIPACCOUNT + " from " + DatabaseHelper.TABLES_NAME_GROUP_MEMBERS
                + " where " + GroupMembersColumn.OWN_GROUP_ID + " = ?";
        ArrayList<String> list = null;
        try {
            Cursor cursor = getInstance().sqliteDB().rawQuery(sql, new String[]{groupId});
            if (cursor != null && cursor.getCount() > 0) {
                list = new ArrayList<>();
                while (cursor.moveToNext()) {
                    list.add(cursor.getString(cursor.getColumnIndex(GroupMembersColumn.VOIPACCOUNT)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
