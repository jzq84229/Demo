package com.zhang.demo.ytx.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yuntongxun.ecsdk.ECMessage;
import com.zhang.demo.ytx.ECApplication;
import com.zhang.demo.ytx.common.CCPAppManager;
import com.zhang.demo.ytx.common.utils.LogUtil;

/**
 * 数据库访问接口
 * Created by Administrator on 2016/7/14.
 */
public class AbstractSQLManager {
    public static final String LOG_TAG = AbstractSQLManager.class.getSimpleName();
    private static DatabaseHelper databaseHelper;
    private static SQLiteDatabase sqliteDB;

    private final MessageObservable mMsgObservable = new MessageObservable();

    public AbstractSQLManager() {
        openDatabase(ECApplication.getInstance(), CCPAppManager.getVersionCode());
    }

    private void openDatabase(Context context, int databaseVersion) {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(context, this, databaseVersion);
        }
        if (sqliteDB == null) {
            sqliteDB = databaseHelper.getWritableDatabase();
        }
    }
    /**
     * 打开数据库连接
     * @param isReadOnly        数据库连接是否为只读
     */
    private void open(boolean isReadOnly) {
        if (sqliteDB == null) {
            if (isReadOnly) {
                sqliteDB = databaseHelper.getReadableDatabase();
            } else {
                sqliteDB = databaseHelper.getWritableDatabase();
            }
        }
    }

    /**
     * 重新打开数据库连接
     */
    private final void reopen() {
        closeDB();
        open(false);
    }

    /**
     * 关闭数据库连接
     */
    private void closeDB() {
        if (sqliteDB != null) {
            sqliteDB.close();
            sqliteDB = null;
        }
    }

    /**
     * 获取数据库连接
     */
    protected final SQLiteDatabase sqliteDB() {
        open(false);
        return sqliteDB;
    }

    protected void release() {
        destroy();
        closeDB();
        databaseHelper = null;
    }

    public void destroy() {
        try {
            if (databaseHelper != null) {
                databaseHelper.close();
            }
            closeDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void registerObserver(OnMessageChange observer) {
        mMsgObservable.registerObserver(observer);
    }

    protected void unregisterObserver(OnMessageChange observer) {
        mMsgObservable.unregisterObserver(observer);
    }

    protected void notifyChanged(String session) {
        mMsgObservable.notifyChanged(session);
    }




    static class DatabaseHelper extends SQLiteOpenHelper {
        static final String DATABASE_NAME = "ECSDK_Msg.db";     //数据库名称
        static final String DESC = "DESC";
        static final String ASC = "ASC";
        static final String TABLES_NAME_IM_SESSION = "im_thread";
        static final String TABLES_NAME_IM_MESSAGE = "im_message";
        static final String TABLES_NAME_CONTACT = "contacts";
        static final String TABLES_NAME_GROUPS = "groups";
        static final String TABLES_NAME_GROUPS_2 = "groups2";
        static final String TABLES_NAME_GROUP_MEMBERS = "group_members";
        static final String TABLES_NAME_SYSTEM_NOTICE = "system_notice";
        static final String TABLES_NAME_IM_MESSAGE_HISTORY = "im_message_history";
        static final String TABLES_NAME_IMGINFO = "imginfo";
        private static final String CREATE_TABLE_IF_NOT_EXISTS = "CREATE TABLE IF NOT EXISTS ";


        private AbstractSQLManager mAbstractSQLManager;
        public DatabaseHelper(Context context, AbstractSQLManager manager, int version) {
            this(context, manager, CCPAppManager.getClientUser().getUserId() + "_" + DATABASE_NAME, null, version);
        }

        public DatabaseHelper(Context context, AbstractSQLManager manager, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            mAbstractSQLManager = manager;
        }



        @Override
        public void onCreate(SQLiteDatabase db) {
            createTables(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

        /**
         * 创建所有数据库表
         */
        private void createTables(SQLiteDatabase db) {
            createTableForContacts(db);         //创建联系人表
            createTableForIMessage(db);         //创建IM信息表
            createTableForISession(db);         //创建IM会话表
            createTriggerForIMessage(db);       //创建触发器
            createTableForIMGroups(db);         //创建群组表
            createTableGroupMembers(db);        //创建群组成员表

            createTableSystemNotice(db);        //创建群组通知消息表
            createTriggerForSystemNotice(db);   //创建群组通知消息触发器

            createTableImgInfo(db);             //创建图片信息表
        }

        /**
         * 创建联系人表
         */
        private void createTableForContacts(SQLiteDatabase db) {
            String sql = CREATE_TABLE_IF_NOT_EXISTS + TABLES_NAME_CONTACT + " ("
                    + ContactsColumn.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ContactsColumn.CONTACT_ID + " TEXT UNIQUE ON CONFLICT ABORT, "
                    + ContactsColumn.TYPE + " INTEGER, "
                    + ContactsColumn.USERNAME + " TEXT, "
                    + ContactsColumn.SUBACCOUNT + " TEXT, "
                    + ContactsColumn.TOKEN + " TEXT, "
                    + ContactsColumn.SUBTOKEN + " TEXT, "
                    + ContactsColumn.REMARK + " TEXT "
                    + ")";
            db.execSQL(sql);
        }

        /**
         * IM消息表
         */
        private void createTableForIMessage(SQLiteDatabase db) {
            String sql = CREATE_TABLE_IF_NOT_EXISTS + TABLES_NAME_IM_MESSAGE + " ("
                    + IMessageColumn.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + IMessageColumn.MESSAGE_ID + " TEXT UNIQUE ON CONFLICT ABORT, "
                    + IMessageColumn.OWN_THREAD_ID + " INTEGER, "
                    + IMessageColumn.CREATE_DATE + " TEXT, "
                    + IMessageColumn.RECEIVE_DATE + " TEXT, "
                    + IMessageColumn.SENDER + " TEXT, "
                    + IMessageColumn.BODY + " TEXT, "
                    + IMessageColumn.USER_DATA + " TEXT, "
                    + IMessageColumn.FILE_URL + " TEXT, "
                    + IMessageColumn.FILE_PATH + " TEXT, "
                    + IMessageColumn.BOX_TYPE + " INTEGER DEFAULT 0, "
                    + IMessageColumn.SEND_STATUS + " INTEGER DEFAULT 0, "
                    + IMessageColumn.MESSAGE_TYPE + " INTEGER DEFAULT 0, "
                    + IMessageColumn.READ_STATUS + "  INTEGER DEFAULT 0, "
                    + IMessageColumn.VERSION + "  INTEGER DEFAULT 0, "
                    + IMessageColumn.DURATION + "  INTEGER DEFAULT 0"
                    + ")";
            db.execSQL(sql);
        }

        /**
         * IM消息会话
         */
        private void createTableForISession(SQLiteDatabase db) {
            String sql = CREATE_TABLE_IF_NOT_EXISTS + TABLES_NAME_IM_SESSION + " ("
                    + IThreadColumn.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + IThreadColumn.THREAD_ID + " TEXT, "
                    + IThreadColumn.CONTACT_ID + " TEXT , "
                    + IThreadColumn.UNREAD_COUNT + " INTEGER DEFAULT 0, "
                    + IThreadColumn.SNIPPET + "  TEXT, "
                    + IThreadColumn.DATE + "  TEXT, "
                    + IThreadColumn.BOX_TYPE + " INTEGER DEFAULT 0, "
                    + IThreadColumn.SEND_STATUS + " INTEGER DEFAULT 0, "
                    + IThreadColumn.MESSAGE_TYPE + " INTEGER DEFAULT 0, "
                    + IThreadColumn.MESSAGE_COUNT + " INTEGER DEFAULT 0"
                    + ")";
            db.execSQL(sql);
        }

        /**
         * 创建群组
         */
        private void createTableForIMGroups(SQLiteDatabase db){
            String sql = CREATE_TABLE_IF_NOT_EXISTS + TABLES_NAME_GROUPS_2 + "("
                    + GroupColumn.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + GroupColumn.GROUP_ID + " TEXT UNIQUE ON CONFLICT ABORT, "
                    + GroupColumn.GROUP_NAME + " TEXT, "
                    + GroupColumn.GROUP_OWNER +" TEXT, "
                    + GroupColumn.GROUP_DECLARED + "  TEXT, "
                    + GroupColumn.GROUP_TYPE + " INTEGER DEFAULT 0, "
                    + GroupColumn.GROUP_PERMISSION + " INTEGER DEFAULT 0, "
                    + GroupColumn.GROUP_MEMBER_COUNTS + " INTEGER DEFAULT 0, "
                    + GroupColumn.GROUP_JOINED + " INTEGER DEFAULT 0, "
                    + GroupColumn.GROUP_ISNOTICE + " INTEGER DEFAULT 1, "
                    + GroupColumn.GROUP_DATE_CREATED + "  TEXT, "
                    + GroupColumn.GROUP_DISCUSSION + "  INTEGER DEFAULT 0"
                    + ")";
            db.execSQL(sql);
        }

        /**
         * 创建群组成员数据库
         */
        private void createTableGroupMembers(SQLiteDatabase db) {
            String sql = CREATE_TABLE_IF_NOT_EXISTS + TABLES_NAME_GROUP_MEMBERS + " ("
                    + GroupMembersColumn.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + GroupMembersColumn.OWN_GROUP_ID + " TEXT, "
                    + GroupMembersColumn.BIRTH + " TEXT, "
                    + GroupMembersColumn.MAIL + " TEXT, "
                    + GroupMembersColumn.REMARK + " TEXT, "
                    + GroupMembersColumn.TEL + " TEXT, "
                    + GroupMembersColumn.SIGN + " TEXT, "
                    + GroupMembersColumn.ROLE + "  INTEGER DEFAULT 1, "
                    + GroupMembersColumn.ISBAN + "  INTEGER DEFAULT 0, "
                    + GroupMembersColumn.RULE + "  INTEGER DEFAULT 0, "
                    + GroupMembersColumn.SEX + "  INTEGER DEFAULT 0, "
                    + GroupMembersColumn.VOIPACCOUNT + " TEXT "
                    + ")";
            db.execSQL(sql);
        }

        /**
         * 创建系统通知表
         */
        private void createTableSystemNotice(SQLiteDatabase db) {
            String sql = CREATE_TABLE_IF_NOT_EXISTS + TABLES_NAME_SYSTEM_NOTICE + " ("
                    + SystemNoticeColumn.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + SystemNoticeColumn.NOTICE_ID + " TEXT UNIQUE ON CONFLICT ABORT, "
                    + SystemNoticeColumn.OWN_THREAD_ID + " INTEGER, "
                    + SystemNoticeColumn.ADMIN + " TEXT, "
                    + SystemNoticeColumn.NOTICE_VERIFYMSG + " TEXT, "
                    + SystemNoticeColumn.NOTICE_DECLARED + " TEXT, "
                    + SystemNoticeColumn.NOTICE_GROUPID + " TEXT, "
                    + SystemNoticeColumn.NOTICE_GROUPNAME + " TEXT, "
                    + SystemNoticeColumn.NOTICE_NICKNAME + " TEXT, "
                    + SystemNoticeColumn.NOTICE_OPERATION_STATE + "  INTEGER, "
                    + SystemNoticeColumn.NOTICE_VERSION + "  INTEGER, "
                    + SystemNoticeColumn.NOTICE_READ_STATUS + "  INTEGER, "
                    + SystemNoticeColumn.NOTICE_TYPE + "  INTEGER, "
                    + SystemNoticeColumn.NOTICE_DATECREATED + " TEXT, "
                    + SystemNoticeColumn.NOTICE_WHO + " TEXT " + ")";
        }

        /**
         * 创建图片信息表
         */
        private void createTableImgInfo(SQLiteDatabase db) {
            String sql = CREATE_TABLE_IF_NOT_EXISTS + TABLES_NAME_IMGINFO + " ("
                    + ImgInfoColumn.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ImgInfoColumn.MSGSVR_ID + " TEXT, "
                    + ImgInfoColumn.OFFSET + " INTEGER, "
                    + ImgInfoColumn.TOTALLEN + " INTEGER, "
                    + ImgInfoColumn.BIG_IMGPATH + " TEXT, "
                    + ImgInfoColumn.THUMBIMG_PATH + " TEXT, "
                    + ImgInfoColumn.CREATE_TIME + " TEXT, "
                    + ImgInfoColumn.MSG_LOCAL_ID + " TEXT, "
                    + ImgInfoColumn.STATUS + " INTEGER, "
                    + ImgInfoColumn.NET_TIMES + " TEXT "
                    + ")";
            db.execSQL(sql);
        }

        /**
         * 创建IM消息会话表触发器
         */
        private void createTriggerForIMessage(SQLiteDatabase db) {
            //删除消息时同时删除没有消息的会话
            String sql = "CREATE TRIGGER IF NOT EXISTS delete_obsolete_threads_im AFTER DELETE ON " + TABLES_NAME_IM_MESSAGE
                    + " BEGIN   "
                    + " DELETE FROM " + TABLES_NAME_IM_SESSION + " WHERE id = old." + IMessageColumn.OWN_THREAD_ID + " AND id NOT IN ( SELECT " + IMessageColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_IM_MESSAGE + ");"
                    + " END;";
            LogUtil.d(LogUtil.getLogUtilsTag(AbstractSQLManager.class), sql);
            db.execSQL(sql);

            //删除消息时更新会话的消息数和最后一条消息内容
            sql = "CREATE TRIGGER IF NOT EXISTS im_update_thread_on_delete AFTER DELETE ON " + TABLES_NAME_IM_MESSAGE +" "
                    + "BEGIN   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.MESSAGE_COUNT + " = (SELECT COUNT(" + TABLES_NAME_IM_MESSAGE + ".id) FROM " + TABLES_NAME_IM_MESSAGE + " LEFT JOIN " + TABLES_NAME_IM_SESSION + " ON " + TABLES_NAME_IM_SESSION + ".id = " + IMessageColumn.OWN_THREAD_ID +  " WHERE " + IMessageColumn.OWN_THREAD_ID +  " = old." + IMessageColumn.OWN_THREAD_ID +  " AND " + TABLES_NAME_IM_MESSAGE + "." + IMessageColumn.BOX_TYPE + " != 3 )   WHERE " + TABLES_NAME_IM_SESSION + ".id = old." + IMessageColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.SNIPPET + " = (SELECT " + IThreadColumn.SNIPPET + " FROM (SELECT " + IMessageColumn.RECEIVE_DATE + ", " + IMessageColumn.BODY + " AS " + IThreadColumn.SNIPPET + ", " + IMessageColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_IM_MESSAGE + ")    WHERE " + IMessageColumn.OWN_THREAD_ID + " = old." + IMessageColumn.OWN_THREAD_ID + " ORDER BY " + IMessageColumn.RECEIVE_DATE + " DESC LIMIT 1)   WHERE " + TABLES_NAME_IM_SESSION + ".id = old." + IMessageColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.DATE + " =  (SELECT " + IMessageColumn.CREATE_DATE + " FROM (SELECT " + IMessageColumn.CREATE_DATE + ", " + IMessageColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_IM_MESSAGE + ")    WHERE " + IMessageColumn.OWN_THREAD_ID + " = old." + IMessageColumn.OWN_THREAD_ID + " ORDER BY " + IMessageColumn.CREATE_DATE + " DESC LIMIT 1)   WHERE " + TABLES_NAME_IM_SESSION + ".id = old." + IMessageColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.BOX_TYPE + " = (SELECT " + IMessageColumn.BOX_TYPE + " FROM (SELECT " + IMessageColumn.RECEIVE_DATE + ", " + IMessageColumn.BOX_TYPE + ", " + IMessageColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_IM_MESSAGE + ")    WHERE " + IMessageColumn.OWN_THREAD_ID + " = old." + IMessageColumn.OWN_THREAD_ID+ " ORDER BY " + IMessageColumn.RECEIVE_DATE + " DESC LIMIT 1)   WHERE " + TABLES_NAME_IM_SESSION+ ".id = old." + IMessageColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.SEND_STATUS + " = (SELECT " + IMessageColumn.SEND_STATUS + " FROM (SELECT " + IMessageColumn.RECEIVE_DATE + ", " + IMessageColumn.SEND_STATUS + ", " + IMessageColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_IM_MESSAGE + ")    WHERE " + IMessageColumn.OWN_THREAD_ID + " = old." + IMessageColumn.OWN_THREAD_ID+ " ORDER BY " + IMessageColumn.RECEIVE_DATE + " DESC LIMIT 1)   WHERE " + TABLES_NAME_IM_SESSION+ ".id = old." + IMessageColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.MESSAGE_TYPE + " = (SELECT " + IMessageColumn.MESSAGE_TYPE + " FROM (SELECT " + IMessageColumn.RECEIVE_DATE + ", " + IMessageColumn.MESSAGE_TYPE + ", " + IMessageColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_IM_MESSAGE + ")    WHERE " + IMessageColumn.OWN_THREAD_ID + " = old." + IMessageColumn.OWN_THREAD_ID+ " ORDER BY " + IMessageColumn.RECEIVE_DATE + " DESC LIMIT 1)   WHERE " + TABLES_NAME_IM_SESSION+ ".id = old." + IMessageColumn.OWN_THREAD_ID + ";   "

                    + "END;";
            LogUtil.d(LogUtil.getLogUtilsTag(AbstractSQLManager.class), sql);
            db.execSQL(sql);

            //删除消息时更新会话的最后联系人ID
            sql = "CREATE TRIGGER IF NOT EXISTS im_update_thread_on_delete2 AFTER DELETE ON " + TABLES_NAME_IM_MESSAGE +" "
                    + "BEGIN   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.CONTACT_ID
                    + " = (SELECT " + IMessageColumn.SENDER + " FROM (SELECT " + IMessageColumn.SENDER + ", " + IMessageColumn.CREATE_DATE + ", " + IMessageColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_IM_MESSAGE + ") "
                    + " WHERE " + IMessageColumn.OWN_THREAD_ID + " = old." + IMessageColumn.OWN_THREAD_ID + " ORDER BY " + IMessageColumn.CREATE_DATE + " DESC LIMIT 1)   WHERE " + TABLES_NAME_IM_SESSION + ".id = old." + IMessageColumn.OWN_THREAD_ID + ";   "
                    + "END;";
            LogUtil.d(LogUtil.getLogUtilsTag(AbstractSQLManager.class), sql);
            db.execSQL(sql);

            //插入消息时更新会话的最后一条消息内容
            sql = "CREATE TRIGGER IF NOT EXISTS im_update_thread_on_insert AFTER INSERT ON " + TABLES_NAME_IM_MESSAGE +" "
                    + "BEGIN  "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET "
                    + IThreadColumn.DATE + " = new." + IMessageColumn.CREATE_DATE + ","
                    + IThreadColumn.SNIPPET + " = new." + IMessageColumn.BODY + ", "
                    + IThreadColumn.BOX_TYPE + "=new." + IMessageColumn.BOX_TYPE + ","
                    + IThreadColumn.SEND_STATUS + "=new." + IMessageColumn.SEND_STATUS + ","
                    + IThreadColumn.MESSAGE_TYPE + "=new." + IMessageColumn.MESSAGE_TYPE + " WHERE " + TABLES_NAME_IM_SESSION+ ".id = new." + IMessageColumn.OWN_THREAD_ID + "; "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.MESSAGE_COUNT + " = (SELECT COUNT(" + TABLES_NAME_IM_MESSAGE+ ".id) FROM " + TABLES_NAME_IM_MESSAGE+ " LEFT JOIN " + TABLES_NAME_IM_SESSION + " ON " + TABLES_NAME_IM_SESSION + ".id = " + IMessageColumn.OWN_THREAD_ID + " WHERE " + IMessageColumn.OWN_THREAD_ID + " = new." + IMessageColumn.OWN_THREAD_ID + " AND " + TABLES_NAME_IM_MESSAGE + "." + IMessageColumn.BOX_TYPE + " != 3 )   WHERE " + TABLES_NAME_IM_SESSION + ".id = new." + IMessageColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.UNREAD_COUNT + " =(" +
                    "(SELECT " + IThreadColumn.UNREAD_COUNT + " FROM " + TABLES_NAME_IM_SESSION+ " WHERE " + IThreadColumn.ID + " = new." + IMessageColumn.OWN_THREAD_ID +")+1) "
                    + " WHERE " + TABLES_NAME_IM_SESSION + ".id = new." + IMessageColumn.OWN_THREAD_ID +  " AND new." + IMessageColumn.BOX_TYPE + " == 1 ;"
                    + "END;";
            LogUtil.d(LogUtil.getLogUtilsTag(AbstractSQLManager.class), sql);
            db.execSQL(sql);

            //插入消息时更新会话的最后联系人ID
            sql = "CREATE TRIGGER IF NOT EXISTS im_update_thread_on_insert2 AFTER INSERT ON " + TABLES_NAME_IM_MESSAGE +" "
                    + "BEGIN  "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET "
                    + IThreadColumn.CONTACT_ID + " = new." + IMessageColumn.SENDER + " WHERE " + TABLES_NAME_IM_SESSION+ ".id = new." + IMessageColumn.OWN_THREAD_ID + "; "
                    + "END;";
            LogUtil.d(LogUtil.getLogUtilsTag(AbstractSQLManager.class), sql);
            db.execSQL(sql);

            //更新消息时更新会话的消息状态、时间、最后一条消息内容等
            sql = "CREATE TRIGGER IF NOT EXISTS im_update_thread_on_update AFTER  UPDATE ON " + TABLES_NAME_IM_MESSAGE +" "
                    + "BEGIN   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.DATE + " = (SELECT " + IMessageColumn.CREATE_DATE + " FROM (SELECT " + IMessageColumn.CREATE_DATE + ", " + IMessageColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_IM_MESSAGE + ") WHERE " + IMessageColumn.OWN_THREAD_ID + " = old." + IMessageColumn.OWN_THREAD_ID+ " ORDER BY " + IMessageColumn.CREATE_DATE + " DESC LIMIT 1)   WHERE " + TABLES_NAME_IM_SESSION+ ".id = old." + IMessageColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.SNIPPET + " = (SELECT " + IThreadColumn.SNIPPET + " FROM (SELECT " + IMessageColumn.RECEIVE_DATE + ", " + IMessageColumn.BODY + " AS " + IThreadColumn.SNIPPET + ", " + IMessageColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_IM_MESSAGE + ") WHERE " + IMessageColumn.OWN_THREAD_ID + " = old." + IMessageColumn.OWN_THREAD_ID+ " ORDER BY " + IMessageColumn.RECEIVE_DATE + " DESC LIMIT 1)   WHERE " + TABLES_NAME_IM_SESSION+ ".id = old." + IMessageColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.BOX_TYPE + " = (SELECT " + IMessageColumn.BOX_TYPE + " FROM (SELECT " + IMessageColumn.RECEIVE_DATE + ", " + IMessageColumn.BOX_TYPE + ", " + IMessageColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_IM_MESSAGE + ") WHERE " + IMessageColumn.OWN_THREAD_ID + " = old." + IMessageColumn.OWN_THREAD_ID+ " ORDER BY " + IMessageColumn.RECEIVE_DATE + " DESC LIMIT 1)   WHERE " + TABLES_NAME_IM_SESSION+ ".id = old." + IMessageColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.SEND_STATUS + " = (SELECT " + IMessageColumn.SEND_STATUS + " FROM (SELECT " + IMessageColumn.RECEIVE_DATE + ", " + IMessageColumn.SEND_STATUS + ", " + IMessageColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_IM_MESSAGE + ") WHERE " + IMessageColumn.OWN_THREAD_ID + " = old." + IMessageColumn.OWN_THREAD_ID+ " ORDER BY " + IMessageColumn.RECEIVE_DATE + " DESC LIMIT 1)   WHERE " + TABLES_NAME_IM_SESSION+ ".id = old." + IMessageColumn.OWN_THREAD_ID + ";   "
                    + "END;";
            LogUtil.d(LogUtil.getLogUtilsTag(AbstractSQLManager.class), sql);
            db.execSQL(sql);

            //删除会话时同时删除会话下的消息
            sql = "CREATE TRIGGER IF NOT EXISTS thread_update_im_on_delete AFTER DELETE ON " + TABLES_NAME_IM_SESSION +" "
                    + "BEGIN DELETE FROM " + TABLES_NAME_IM_MESSAGE + " WHERE " + IMessageColumn.OWN_THREAD_ID + " = old." + IThreadColumn.ID + ";END;";
            LogUtil.d(LogUtil.getLogUtilsTag(AbstractSQLManager.class), sql);
            db.execSQL(sql);
        }

        /**
         * 创建系统通知表触发器
         */
        private void createTriggerForSystemNotice(SQLiteDatabase db) {
            //删除系统通知时更新会话的消息数、未读数、最后一条消息数据
            String sql = "CREATE TRIGGER IF NOT EXISTS system_update_thread_on_delete AFTER DELETE ON " + TABLES_NAME_SYSTEM_NOTICE +" "
                    + "BEGIN   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.MESSAGE_COUNT + " = (SELECT COUNT(" + TABLES_NAME_SYSTEM_NOTICE + ".id) FROM " + TABLES_NAME_SYSTEM_NOTICE + " LEFT JOIN " + TABLES_NAME_IM_SESSION + " ON " + TABLES_NAME_IM_SESSION + ".id = " + SystemNoticeColumn.OWN_THREAD_ID +  " WHERE " + SystemNoticeColumn.OWN_THREAD_ID +  " = old." + SystemNoticeColumn.OWN_THREAD_ID +  ")  WHERE " + TABLES_NAME_IM_SESSION + ".id = old." + SystemNoticeColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.UNREAD_COUNT + " =(SELECT COUNT(*) FROM " + TABLES_NAME_SYSTEM_NOTICE + " WHERE " + SystemNoticeColumn.NOTICE_READ_STATUS + " = 0 AND " + SystemNoticeColumn.OWN_THREAD_ID + " = old." + SystemNoticeColumn.OWN_THREAD_ID + ")  WHERE " + TABLES_NAME_IM_SESSION + ".id = old." + SystemNoticeColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.SNIPPET + " = (SELECT " + IThreadColumn.SNIPPET + " FROM (SELECT " + SystemNoticeColumn.NOTICE_DATECREATED + ", " + SystemNoticeColumn.NOTICE_VERIFYMSG + " AS " + IThreadColumn.SNIPPET + ", " + SystemNoticeColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_SYSTEM_NOTICE + ")    WHERE " + SystemNoticeColumn.OWN_THREAD_ID + " = old." + SystemNoticeColumn.OWN_THREAD_ID + " ORDER BY " + SystemNoticeColumn.NOTICE_DATECREATED + " DESC LIMIT 1)   WHERE " + TABLES_NAME_IM_SESSION + ".id = old." + SystemNoticeColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.DATE + " =    (SELECT " + SystemNoticeColumn.NOTICE_DATECREATED + " FROM (SELECT " + SystemNoticeColumn.NOTICE_DATECREATED + ", " + SystemNoticeColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_SYSTEM_NOTICE + ")    WHERE " + SystemNoticeColumn.OWN_THREAD_ID + " = old." + SystemNoticeColumn.OWN_THREAD_ID + " ORDER BY " + SystemNoticeColumn.NOTICE_DATECREATED + " DESC LIMIT 1)   WHERE " + TABLES_NAME_IM_SESSION + ".id = old." + SystemNoticeColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.BOX_TYPE + " = " + ECMessage.Direction.RECEIVE .ordinal()+ "; "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.SEND_STATUS + " = " + ECMessage.MessageStatus.SUCCESS.ordinal() + " ;   "
                    // + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.MESSAGE_TYPE + " = " + com.speedtong.example.storage.GroupNoticeSqlManager.NOTICE_MSG_TYPE + " ;   "
                    + "END;";
            LogUtil.d(LogUtil.getLogUtilsTag(AbstractSQLManager.class), sql);
            db.execSQL(sql);

            //插入系统消息时更新会话的最后一条消息数据
            sql = "CREATE TRIGGER IF NOT EXISTS system_update_thread_on_insert AFTER INSERT ON " + TABLES_NAME_SYSTEM_NOTICE +" "
                    + "BEGIN  "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.DATE + " = new." + SystemNoticeColumn.NOTICE_DATECREATED + "," + IThreadColumn.SNIPPET + " = new." + SystemNoticeColumn.NOTICE_VERIFYMSG + "," + IThreadColumn.BOX_TYPE + "=" + ECMessage.Direction.RECEIVE.ordinal() + "," + IThreadColumn.SEND_STATUS + "=" + ECMessage.MessageStatus.SUCCESS.ordinal() + "," + IThreadColumn.MESSAGE_TYPE + "=" + GroupNoticeSqlManager.NOTICE_MSG_TYPE + " WHERE " + TABLES_NAME_IM_SESSION+ ".id = new." + SystemNoticeColumn.OWN_THREAD_ID + "; "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.MESSAGE_COUNT + " = (SELECT COUNT(" + TABLES_NAME_SYSTEM_NOTICE+ ".id) FROM " + TABLES_NAME_SYSTEM_NOTICE+ " LEFT JOIN " + TABLES_NAME_IM_SESSION + " ON " + TABLES_NAME_IM_SESSION + ".id = " + SystemNoticeColumn.OWN_THREAD_ID + " WHERE " + SystemNoticeColumn.OWN_THREAD_ID + " = new." + SystemNoticeColumn.OWN_THREAD_ID + "  )   WHERE " + TABLES_NAME_IM_SESSION + ".id = new." + SystemNoticeColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.SNIPPET + " = (SELECT " + IThreadColumn.SNIPPET + " FROM (SELECT " + SystemNoticeColumn.NOTICE_DATECREATED + ", " + SystemNoticeColumn.NOTICE_VERIFYMSG + " AS " + IThreadColumn.SNIPPET + ", " + SystemNoticeColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_SYSTEM_NOTICE + ") WHERE " + SystemNoticeColumn.OWN_THREAD_ID + " = new." + SystemNoticeColumn.OWN_THREAD_ID+ " ORDER BY " + SystemNoticeColumn.NOTICE_DATECREATED + " DESC LIMIT 1)   WHERE " + TABLES_NAME_IM_SESSION+ ".id = new." + SystemNoticeColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.UNREAD_COUNT + " =(SELECT COUNT(*) FROM " + TABLES_NAME_SYSTEM_NOTICE+ " WHERE " + SystemNoticeColumn.NOTICE_READ_STATUS+ " = 0  AND " + SystemNoticeColumn.OWN_THREAD_ID + " = new." + SystemNoticeColumn.OWN_THREAD_ID + ")  WHERE " + TABLES_NAME_IM_SESSION + ".id = new." + SystemNoticeColumn.OWN_THREAD_ID + ";  "
                    + "END;";
            LogUtil.d(LogUtil.getLogUtilsTag(AbstractSQLManager.class), sql);
            db.execSQL(sql);

            //更新系统消息的已读状态时更新会话的未读消息数
            sql = "CREATE TRIGGER IF NOT EXISTS system_update_thread_read_on_update AFTER  UPDATE OF " + SystemNoticeColumn.NOTICE_READ_STATUS + "  ON " + TABLES_NAME_SYSTEM_NOTICE +" "
                    + "BEGIN   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.UNREAD_COUNT + " =(SELECT COUNT(*) FROM " + TABLES_NAME_SYSTEM_NOTICE + " WHERE " + SystemNoticeColumn.NOTICE_READ_STATUS + " = 0 AND " + SystemNoticeColumn.OWN_THREAD_ID + " = old." + SystemNoticeColumn.OWN_THREAD_ID + ")  WHERE " + TABLES_NAME_IM_SESSION + ".id = old." + SystemNoticeColumn.OWN_THREAD_ID + "; "
                    + "END;";
            LogUtil.d(LogUtil.getLogUtilsTag(AbstractSQLManager.class), sql);
            db.execSQL(sql);

            //更新系统消息时更新会话的最后一条消息数据
            sql = "CREATE TRIGGER IF NOT EXISTS system_update_thread_on_update AFTER  UPDATE ON " + TABLES_NAME_SYSTEM_NOTICE +" "
                    + "BEGIN   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.DATE + " = (SELECT " + SystemNoticeColumn.NOTICE_DATECREATED + " FROM (SELECT " + SystemNoticeColumn.NOTICE_DATECREATED + ", " + SystemNoticeColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_SYSTEM_NOTICE + ") WHERE " + SystemNoticeColumn.OWN_THREAD_ID + " = old." + SystemNoticeColumn.OWN_THREAD_ID+ " ORDER BY " + SystemNoticeColumn.NOTICE_DATECREATED + " DESC LIMIT 1)   WHERE " + TABLES_NAME_IM_SESSION+ ".id = old." + SystemNoticeColumn.OWN_THREAD_ID + ";   "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.SNIPPET + " = (SELECT " + IThreadColumn.SNIPPET + " FROM (SELECT " + SystemNoticeColumn.NOTICE_DATECREATED + ", " + SystemNoticeColumn.NOTICE_VERIFYMSG + " AS " + IThreadColumn.SNIPPET + ", " + SystemNoticeColumn.OWN_THREAD_ID + " FROM " + TABLES_NAME_SYSTEM_NOTICE + ") WHERE " + SystemNoticeColumn.OWN_THREAD_ID + " = old." + SystemNoticeColumn.OWN_THREAD_ID+ " ORDER BY " + SystemNoticeColumn.NOTICE_DATECREATED + " DESC LIMIT 1)   WHERE " + TABLES_NAME_IM_SESSION+ ".id = old." + SystemNoticeColumn.OWN_THREAD_ID + ";   "

                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.BOX_TYPE + " = " + ECMessage.Direction.RECEIVE .ordinal() + "; "
                    + "UPDATE " + TABLES_NAME_IM_SESSION + " SET " + IThreadColumn.SEND_STATUS + " = " + ECMessage.MessageStatus.SUCCESS.ordinal() + " ;   "
                    + "END;";
            LogUtil.d(LogUtil.getLogUtilsTag(AbstractSQLManager.class), sql);
            db.execSQL(sql);

            //删除会话时删除会话下的系统消息
            sql = "CREATE TRIGGER IF NOT EXISTS thread_update_system_on_delete AFTER DELETE ON " + TABLES_NAME_IM_SESSION +" "
                    + "BEGIN DELETE FROM " + TABLES_NAME_SYSTEM_NOTICE + " WHERE " + SystemNoticeColumn.OWN_THREAD_ID + " = old." + IThreadColumn.ID + ";END;";
            LogUtil.d(LogUtil.getLogUtilsTag(AbstractSQLManager.class), sql);
            db.execSQL(sql);
        }
    }

    public class BaseColumn{
        public static final String ID = "ID";
        public static final String UNREAD_NUM = "unreadcount";
    }

    /**
     * 联系人表
     */
    public class ContactsColumn extends BaseColumn {
        public static final String CONTACT_ID = "contact_id";       //联系人账号
        public static final String USERNAME = "username";           //联系人昵称
        public static final String TOKEN = "token";                 //联系人账号token
        public static final String SUBACCOUNT = "subAccount";       //联系人子账号
        public static final String SUBTOKEN = "subToken";           //联系人子账号token
        public static final String TYPE = "type";                   //联系人类型
        public static final String REMARK = "remark";               //备注
    }

    /**
     * 会话表
     */
    public class IThreadColumn extends BaseColumn {
        public static final String THREAD_ID = "sessionId";         //会话ID
        public static final String MESSAGE_COUNT = "sumCount";      //总消息数
        public static final String CONTACT_ID = "contactid";        //最后一条消息发送者
        public static final String SNIPPET = "text";                //文本
        public static final String UNREAD_COUNT = "unreadCount";    //消息未读数
        public static final String SEND_STATUS = "sendStatus";      //消息的发送状态
        public static final String BOX_TYPE = "boxType";            //信箱类型
        public static final String DATE = "dateTime";               //消息时间
        public static final String MESSAGE_TYPE = "type";           //消息类型
    }

    /**
     * 消息表
     */
    public class IMessageColumn extends BaseColumn{
        public static final String MESSAGE_ID = "msgid";            //消息ID
        public static final String MESSAGE_TYPE = "msgType";        //消息类型
        public static final String OWN_THREAD_ID = "sid";           //会话ID
        public static final String SENDER = "sender";               //消息创建者
        public static final String READ_STATUS = "isRead";          //是否已读
        public static final String BODY = "text";                   //文本
        public static final String BOX_TYPE = "box_type";           //信箱类型
        public static final String SEND_STATUS = "state";           //发送状态   -1发送失败 0发送成功 1发送中 2接收成功（默认为0 接收的消息）
        public static final String CREATE_DATE = "serverTime";      //服务器时间 毫秒
        public static final String RECEIVE_DATE = "createdTime";   // 入库本地时间 毫秒
        public static final String USER_DATA = "userData";          // 用户自定义数据
        public static final String FILE_URL = "url";                // 下载路径
        public static final String FILE_PATH = "localPath";         // 文件本地路径
        public static final String DURATION = "duration";           // 语音时间
        public static final String VERSION = "version";
        public static final String REMARK = "remark";               // 备注
    }

    /**
     * 群组表
     */
    public class GroupColumn extends BaseColumn {
        public static final String GROUP_ID = "groupid";                //群组ID
        public static final String GROUP_NAME = "name";                 //群组名称
        public static final String GROUP_OWNER = "owner";               //群组创建者
        public static final String GROUP_TYPE = "type";                 //群组类型 0:临时组(上限100人) 1:普通组(上限200人) 2:VIP组(上限500人)*
        public static final String GROUP_DECLARED = "declared";         //群组公告
        public static final String GROUP_DATE_CREATED = "create_date";  //群组创建日期
        public static final String GROUP_MEMBER_COUNTS = "count";       //群组成员数
        public static final String GROUP_PERMISSION = "permission";     //群组群组加入权限
        public static final String GROUP_JOINED = "joined";             //群组是否加入
        public static final String GROUP_ISNOTICE = "isnotice";
        public static final String GROUP_DISCUSSION = "isdiscussion";
    }

    /**
     * 群组成员表
     */
    public class GroupMembersColumn extends BaseColumn {
        public static final String OWN_GROUP_ID = "group_id";       // 群组ID
        public static final String ISBAN = "isban";                 // 是否禁言
        public static final String VOIPACCOUNT = "voipaccount";     // 用户voip账号
        public static final String SEX = "sex";                     // 性别
        public static final String BIRTH = "birth";                 // 用户生日
        public static final String TEL = "tel";                     // 用户电话
        public static final String SIGN = "sign";                   // 用户的签名
        public static final String MAIL = "mail";                   // 用户邮箱
        public static final String ROLE = "role";                   // 用户角色
        public static final String REMARK = "remark";               // 用户的备注
        public static final String RULE = "rule";                   // 是否接收群组消息
    }

    /**
     * 群组通知接口字段
     */
    public class SystemNoticeColumn extends BaseColumn {
        public static final String OWN_THREAD_ID = "sid";               //对应会话ID
        public static final String NOTICE_ID = "notice_id";             //通知消息ID
        public static final String NOTICE_VERIFYMSG = "verifymsg";      // 通知消息验证理由
        public static final String NOTICE_DECLARED = "declared";
        public static final String ADMIN = "admin";                     // 管理员
        public static final String NOTICE_TYPE = "type";                // 消息类型
        public static final String NOTICE_OPERATION_STATE = "confirm";  // 是否需要确认
        public static final String NOTICE_GROUPID = "groupId";          // 群组ID
        public static final String NOTICE_GROUPNAME = "groupName";      // 群组名称
        public static final String NOTICE_WHO = "member";               // 联系人账号
        public static final String NOTICE_NICKNAME = "nickName";        // 联系人名称
        public static final String NOTICE_READ_STATUS = "isRead";       // 已读状态
        public static final String NOTICE_VERSION = "version";
        public static final String NOTICE_DATECREATED = "dateCreated";  // 消息时间
    }

    /**
     * 图片信息表
     */
    public class ImgInfoColumn extends BaseColumn {
        public static final String MSGSVR_ID = "msgSvrId";
        public static final String OFFSET = "offset";
        public static final String TOTALLEN = "totalLen";
        public static final String BIG_IMGPATH = "bigImgPath";
        public static final String THUMBIMG_PATH = "thumbImgPath";
        public static final String CREATE_TIME = "createtime";
        public static final String STATUS = "status";
        public static final String MSG_LOCAL_ID = "msglocalid";
        public static final String NET_TIMES = "nettimes";
    }






}
