package com.zhang.demo.ytx.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.yuntongxun.ecsdk.im.ECGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 群组数据库管理，单例
 * Created by Administrator on 2016/7/15.
 */
public class GroupSqlManager extends AbstractSQLManager {
    Object mLock = new Object();

    private static GroupSqlManager sInstance;

    private GroupSqlManager(){}
    private static GroupSqlManager getInstance() {
        if (sInstance == null) {
            sInstance = new GroupSqlManager();
        }
        return sInstance;
    }

    public static void registerGroupObserver(OnMessageChange observer) {
        getInstance().registerObserver(observer);
    }

    public static void unregisterGroupObserver(OnMessageChange observer) {
        getInstance().unregisterObserver(observer);
    }

    public static void notifyGroupChanged(String session) {
        getInstance().notifyChanged(session);
    }

    public static void reset() {
        getInstance().release();
    }

    @Override
    protected void release() {
        super.release();
        sInstance = null;
    }





    /**
     * 群组是否免打扰
     * @param groupId       群组ID
     * @return              boolean
     */
    public static boolean isGroupNotify(String groupId) {
        String sql = "select " + GroupColumn.GROUP_ISNOTICE + " from " + DatabaseHelper.TABLES_NAME_GROUPS_2
                + " where " + GroupColumn.GROUP_ID + "=?";
        Cursor cursor = getInstance().sqliteDB().rawQuery(sql, new String[]{groupId});
        boolean isNotify = true;
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                isNotify = !(cursor.getInt(0) == 2);
            }
            cursor.close();
        }
        return isNotify;
    }

    /**
     * 根据群组Id查询群组
     * @param groupId       群组ID
     * @return              ECGroup
     */
    public static ECGroup getECGroup(String groupId) {
        try {
            String sql = "select " + GroupColumn.GROUP_NAME + ","
                    + GroupColumn.GROUP_TYPE + ", "
                    + GroupColumn.GROUP_MEMBER_COUNTS + ", "
                    + GroupColumn.GROUP_PERMISSION + ", "
                    + GroupColumn.GROUP_JOINED + ", "
                    + GroupColumn.GROUP_DECLARED + ", "
                    + GroupColumn.GROUP_OWNER + ", "
                    + GroupColumn.GROUP_ISNOTICE
                    + " from " + DatabaseHelper.TABLES_NAME_GROUPS_2
                    + " where " + GroupColumn.GROUP_ID + " = ?";
            Cursor cursor = getInstance().sqliteDB().rawQuery(sql, new String[]{groupId});
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                ECGroup group = new ECGroup();
                group.setGroupId(groupId);
                group.setName(cursor.getString(0));
                group.setGroupType(cursor.getInt(1));
                group.setCount(cursor.getInt(2));
                group.setPermission(ECGroup.Permission.values()[cursor.getInt(3)]);
                group.setDeclare(cursor.getString(5));
                group.setOwner(cursor.getString(6));
                group.setIsNotice(!(cursor.getInt(7) == 2));
                return group;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询所有的groupID
     * @return      List<String>
     */
    public static List<String> getAllGroupId() {
        ArrayList<String> groupsId = new ArrayList<>();
        Cursor cursor = null;
        try {
            String sql = "select " + GroupColumn.GROUP_ID + " from " + DatabaseHelper.TABLES_NAME_GROUPS_2;
            cursor = getInstance().sqliteDB().rawQuery(sql, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String groupid = cursor.getString(0);
                    if (TextUtils.isEmpty(groupid)) {
                        continue;
                    }
                    groupsId.add(groupid);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return groupsId;
    }

    /**
     * 更新群组信息
     */
    public static long updateGroup(ECGroup group) {
        if (group == null || TextUtils.isEmpty(group.getGroupId())) {
            return -1L;
        }
        ContentValues values = null;
        try {
            values = new ContentValues();
            values.put(GroupColumn.GROUP_ID, group.getGroupId());
            values.put(GroupColumn.GROUP_NAME, group.getName());
            values.put(GroupColumn.GROUP_PERMISSION, group.getPermission().ordinal());
            values.put(GroupColumn.GROUP_TYPE, group.getGroupType());
            values.put(GroupColumn.GROUP_OWNER, group.getOwner());
            values.put(GroupColumn.GROUP_DATE_CREATED, group.getDateCreated());
            values.put(GroupColumn.GROUP_DECLARED, group.getDeclare());
            values.put(GroupColumn.GROUP_MEMBER_COUNTS, group.getCount());
            if (group.isDiscuss()) {
                values.put(GroupColumn.GROUP_DISCUSSION, 1);
            }
            return getInstance().sqliteDB().update(DatabaseHelper.TABLES_NAME_GROUPS_2, values,
                    GroupColumn.GROUP_ID + " = ?", new String[]{group.getGroupId()});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (values != null) {
                values.clear();
                values = null;
            }
        }
        return -1L;
    }

    /**
     * 更新群组免打扰状态
     * @param ordinal       免打扰状态值
     * @param groupid       群组ID
     * @return              更新条数
     */
    public static long updateGroupNotify(int ordinal, String groupid) {
        //群组免打扰
        ContentValues values = new ContentValues();
        values.put(GroupColumn.GROUP_ISNOTICE, ordinal);
        return getInstance().sqliteDB().update(DatabaseHelper.TABLES_NAME_GROUPS_2, values, GroupColumn.GROUP_ID + " = ?", new String[]{groupid});
    }

    /**
     * 群组是否存在
     * @param groupId
     * @return
     */
    public static boolean isExitGroup(String groupId) {
        String sql = "select " + GroupColumn.GROUP_ID + " from " + DatabaseHelper.TABLES_NAME_GROUPS_2
                + " where " + GroupColumn.GROUP_ID + " = ?";
        try {
            Cursor cursor = getInstance().sqliteDB().rawQuery(sql, new String[]{groupId});
            if (cursor != null && cursor.getCount() > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void checkGroup(String contactid) {
        if(!isExitGroup(contactid)) {
            ECGroup group = new ECGroup();
            group.setGroupId(contactid);
            group.setName(contactid);
            insertGroup(group , true , false,false);
        }
    }

    /**
     * 更新群组到数据库
     * @param group
     * @param join
     */
    public static long insertGroup(ECGroup group, boolean join, boolean ignoreJoin, boolean isDiscussion) {
        if(group == null || TextUtils.isEmpty(group.getGroupId())) {
            return -1L;
        }
        ContentValues values = null;
        try {
            values = new ContentValues();
            values.put(GroupColumn.GROUP_ID, group.getGroupId());
            values.put(GroupColumn.GROUP_NAME, group.getName());
            values.put(GroupColumn.GROUP_PERMISSION, group.getPermission().ordinal());
            values.put(GroupColumn.GROUP_ISNOTICE, group.isNotice()?1:2);
            values.put(GroupColumn.GROUP_TYPE, group.getGroupType());

            if(isDiscussion){
                values.put(GroupColumn.GROUP_DISCUSSION, 1);
            }

            if(!TextUtils.isEmpty(group.getOwner())) {
                values.put(GroupColumn.GROUP_OWNER, group.getOwner());
                values.put(GroupColumn.GROUP_DECLARED, group.getDeclare());
            }
            values.put(GroupColumn.GROUP_DATE_CREATED, group.getDateCreated());
            values.put(GroupColumn.GROUP_MEMBER_COUNTS, group.getCount());
            if(!ignoreJoin)
                values.put(GroupColumn.GROUP_JOINED, join);

            if(isExitGroup(group.getGroupId())) {
                return getInstance().sqliteDB().update(DatabaseHelper.TABLES_NAME_GROUPS_2, values, "groupid = ?", new String[]{group.getGroupId()});
            }
            long rowId = getInstance().sqliteDB().insert(DatabaseHelper.TABLES_NAME_GROUPS_2, null, values);
            return rowId;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (values != null) {
                values.clear();
                values = null;
            }

        }
        return -1L;
    }


}
