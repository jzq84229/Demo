package com.zhang.demo.ytx.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.hp.hpl.sparta.Text;
import com.zhang.demo.ytx.common.CCPAppManager;
import com.zhang.demo.ytx.core.ClientUser;
import com.zhang.demo.ytx.core.ContactsCache;
import com.zhang.demo.ytx.ui.contact.ContactLogic;
import com.zhang.demo.ytx.ui.contact.ECContacts;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 联系人数据库管理
 * Created by Administrator on 2016/7/14.
 */
public class ContactSqlManager extends AbstractSQLManager {

    private static ContactSqlManager sInstance;

    private static ContactSqlManager getInstance() {
        if (sInstance == null) {
            sInstance = new ContactSqlManager();
        }
        return sInstance;
    }

    /**
     * 判断联系人是否存在
     * @param contactId     联系人ID
     */
    public static boolean hasContact(String contactId) {
        String sql = "select " + ContactsColumn.CONTACT_ID + " from " + DatabaseHelper.TABLES_NAME_CONTACT
                + " where " + ContactsColumn.CONTACT_ID + " = '" + contactId + "'";
        Cursor cursor = getInstance().sqliteDB().rawQuery(sql, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        return false;
    }

    /** 插入或跟新联系人信息 */
    public static long insertContact(ECContacts contact) {
        return insertContact(contact, 1);
    }

    /** 插入或更新联系人信息 */
    public static long insertContact(ECContacts contact, int sex) {
        return insertContact(contact, sex, false);
    }

    /**
     * 插入或更新联系人信息
     */
    public static long insertContact(ECContacts contact, int sex, boolean hasPhoto) {
        if (contact == null || TextUtils.isEmpty(contact.getContactid())) {
            return -1;
        }
        try {
            ContentValues values = contact.buildContentValues();
            if (!hasPhoto) {
                int index = getIntRandom(3, 0);
                if (sex == 2) {
                    index = 4;
                }
                String remark = ContactLogic.CONVER_PHONTO[index];
                contact.setRemark(remark);
            }
            values.put(ContactsColumn.REMARK, contact.getRemark());
            if (!hasContact(contact.getContactid())) {
                return getInstance().sqliteDB().insert(DatabaseHelper.TABLES_NAME_CONTACT, null, values);
            }
            getInstance().sqliteDB().update(DatabaseHelper.TABLES_NAME_CONTACT,
                    values,
                    ContactsColumn.CONTACT_ID + "= ?",
                    new String[]{contact.getContactid()});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 更新联系人头像
     */
    public static long updateContactPhoto(ECContacts contact) {
        return insertContact(contact, 1, true);
    }

    /**
     * 插入联系人到数据库
     * @param contacts      要插入的联系人
     * @return              ArrayList<Long>
     */
    public static ArrayList<Long> insertContacts(List<ECContacts> contacts) {
        ArrayList<Long> rows = new ArrayList<>();
        try {
            getInstance().sqliteDB().beginTransaction();
            for (ECContacts c : contacts) {
                long rowId = insertContact(c);
                if (rowId != -1L) {
                    rows.add(rowId);
                }
            }

            //初始化系统联系人
            insertSystemNoticeContact();
            getInstance().sqliteDB().setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            getInstance().sqliteDB().endTransaction();
        }

        return rows;
    }

    /**
     * 初始化系统联系人数据
     */
    private static long insertSystemNoticeContact() {
        ECContacts contacts = new ECContacts(GroupNoticeSqlManager.CONTACT_ID);
        contacts.setNickname("系统通知");
        contacts.setRemark("touxiang_notice.png");

        return insertContact(contacts);
    }

    /**
     * 根据手机号获取联系人对象
     * @param phoneNumber       手机号
     * @return                  EContacts
     */
    public static ECContacts getCacheContact(String phoneNumber) {
        if (ContactsCache.getInstance().getContacts() != null) {
            return ContactsCache.getInstance().getContacts().getValueByPhone(phoneNumber);
        }
        return null;
    }

    /**
     * 根据联系人账号查询联系人名称
     * @param contactId         联系人账号
     * @return                  ArrayList<String>
     */
    public static ArrayList<String> getContactname(String[] contactId) {
        ArrayList<String> contacts = null;
        try {
            String sql = "select " + ContactsColumn.USERNAME + ", " + ContactsColumn.CONTACT_ID + " from " + DatabaseHelper.TABLES_NAME_CONTACT
                    + " where " + ContactsColumn.CONTACT_ID + " in ";
            StringBuilder sb = new StringBuilder("(");
            for (int i = 0; i < contactId.length; i++) {
                sb.append("'").append(contactId[i]).append("'");
                if (i != contactId.length - 1) {
                    sb.append(",");
                }
            }
            sb.append(")");
            Cursor cursor = getInstance().sqliteDB().rawQuery(sql + sb.toString(), null);
            if (cursor != null && cursor.getCount() > 0) {
                contacts = new ArrayList<>();
                //过滤自己的联系人账号
                ClientUser clientUser = CCPAppManager.getClientUser();
                while (cursor.moveToNext()) {
                    if (clientUser != null && clientUser.getUserId().equals(cursor.getString(0))) {
                        continue;
                    }
                    String displayName = cursor.getString(0);
                    String contact_id = cursor.getString(1);
                    if (TextUtils.isEmpty(displayName) || TextUtils.isEmpty(contact_id)) {
                        continue;
                    }
                    contacts.add(displayName);
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contacts;
    }

    /**
     * 根据联系人账号查询
     * @param contactId     联系人ID
     * @return              ECContact
     */
    public static ECContacts getContact(String contactId) {
        if (TextUtils.isEmpty(contactId)) {
            return null;
        }
        ECContacts c = new ECContacts(contactId);
        c.setNickname(contactId);
        try {
            Cursor cursor = getInstance().sqliteDB().query(DatabaseHelper.TABLES_NAME_CONTACT,
                    new String[]{ContactsColumn.ID, ContactsColumn.USERNAME, ContactsColumn.CONTACT_ID, ContactsColumn.REMARK},
                    ContactsColumn.CONTACT_ID + " = ?", new String[]{contactId}, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    c = new ECContacts(cursor.getString(2));
                    c.setNickname(cursor.getString(1));
                    c.setRemark(cursor.getString(3));
                    c.setId(cursor.getInt(0));
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c;
    }

    /**
     * 根据联系人ID查询联系人备注
     * @param contactIds     联系人数组
     * @return              ArrayList<String>
     */
    public static ArrayList<String> getContactRemark(String[] contactIds) {
        ArrayList<String> contacts = null;
        try {
            String sql = "select " + ContactsColumn.REMARK + " from " + DatabaseHelper.TABLES_NAME_CONTACT
                    + " where " + ContactsColumn.CONTACT_ID + " in ";
            StringBuilder sb = new StringBuilder("(");
            for (int i = 0; i < contactIds.length; i++) {
                sb.append("'").append(contactIds[i]).append("'");
                if (i != contactIds.length - 1) {
                    sb.append(",");
                }
            }
            sb.append(")");
            Cursor cursor = getInstance().sqliteDB().rawQuery(sql + sb.toString(), null);
            if (cursor != null && cursor.getCount() > 0) {
                contacts = new ArrayList<>();
                while (cursor.moveToNext()) {
                    contacts.add(cursor.getString(0));
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contacts;
    }

    public static int getIntRandom(int max, int min) {
        Assert.assertTrue(max > min);
        return (new Random(System.currentTimeMillis()).nextInt(max - min + 1) + min);
    }
}
