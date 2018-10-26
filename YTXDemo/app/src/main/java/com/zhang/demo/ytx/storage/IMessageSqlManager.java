package com.zhang.demo.ytx.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.text.TextUtils;

import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.im.ECCallMessageBody;
import com.yuntongxun.ecsdk.im.ECFileMessageBody;
import com.yuntongxun.ecsdk.im.ECGroup;
import com.yuntongxun.ecsdk.im.ECImageMessageBody;
import com.yuntongxun.ecsdk.im.ECLocationMessageBody;
import com.yuntongxun.ecsdk.im.ECPreviewMessageBody;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;
import com.yuntongxun.ecsdk.im.ECVideoMessageBody;
import com.yuntongxun.ecsdk.im.ECVoiceMessageBody;
import com.zhang.demo.ytx.common.utils.DemoUtils;
import com.zhang.demo.ytx.common.utils.FileAccessor;
import com.zhang.demo.ytx.common.utils.LogUtil;
import com.zhang.demo.ytx.ui.chatting.ChattingFragment;
import com.zhang.demo.ytx.ui.chatting.model.ImgInfo;
import com.zhang.demo.ytx.ui.contact.ContactLogic;
import com.zhang.demo.ytx.ui.contact.ECContacts;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.security.acl.Group;
import java.util.ArrayList;

/**
 * 消息数据库管理
 * Created by Administrator on 2016/7/12.
 */
public class IMessageSqlManager extends AbstractSQLManager {
    public static final int IMESSENGER_TYPE_UNREAD = 0;             //消息未读状态--未读
    public static final int IMESSENGER_TYPE_READ = 1;               //消息未读状态--已读
    /**
     * 信箱类型
     */
    public static final int IMESSENGER_BOX_TYPE_ALL = 0;
    public static final int IMESSENGER_BOX_TYPE_INBOX = 1;          //信箱类型--收件箱
    public static final int IMESSENGER_BOX_TYPE_SENT = 2;
    public static final int IMESSENGER_BOX_TYPE_DRAFT = 3;          //信箱类型--草稿箱
    public static final int IMESSENGER_BOX_TYPE_OUTBOX = 4;         //信箱类型--发件箱
    public static final int IMESSENGER_BOX_TYPE_FAILED = 5;
    public static final int IMESSENGER_BOX_TYPE_QUEUED = 6;

    public static final String ACTION_SESSION_DEL = "com.yuntonxun.ecdemo.ACTION_SESSION_DEL";
    public static final String ACTION_GROUP_DEL = "com.yuntonxun.ecdemo.ACTION_GROUP_DEL";

    private static IMessageSqlManager instance;

    private IMessageSqlManager() {
        super();
    }

    private static IMessageSqlManager getInstance() {
        if (instance == null) {
            instance = new IMessageSqlManager();
        }
        return instance;
    }

    @Override
    protected void release() {
        super.release();
        instance = null;
    }

    public static void reset() {
        getInstance().release();
    }

    public static void registerMsgObserver(OnMessageChange observer) {
        getInstance().registerObserver(observer);
    }

    public static void unregisterMsgObserver(OnMessageChange observer) {
        getInstance().unregisterObserver(observer);
    }

    public static void notifyMsgChanged(String session) {
        getInstance().notifyChanged(session);
    }

    public static void checkContact(String contactid) {
        checkContact(contactid, null);
    }

    public static void checkContact(String contactid, String username) {
        if (!ContactSqlManager.hasContact(contactid)) {
            ECContacts c = ContactSqlManager.getCacheContact(contactid);
            if (c == null) {
                c = new ECContacts(contactid);
                c.setNickname(contactid);
            }
            c.setContactid(contactid);
            if (TextUtils.isEmpty(username)) {
                int index = ContactSqlManager.getIntRandom(3, 0);
                String remark = ContactLogic.CONVER_PHONTO[index];
                c.setRemark(remark);
            }
            ContactSqlManager.insertContact(c);
        }
    }

    /**
     * 更新消息到本地数据库
     *
     * @param message 消息
     * @param boxType 消息保存的信箱类型
     *                tableName 表名a
     * @return 更新的消息ID
     */
    public static long insertIMessage(ECMessage message, int boxType) {
        long ownThreadId = 0;
        long row = 0L;
        try {
            if (!TextUtils.isEmpty(message.getSessionId())) {
                String contactIds = message.getSessionId();
                if (contactIds.toUpperCase().startsWith("G")) {
                    GroupSqlManager.checkGroup(contactIds);
                }
                checkContact(message.getForm(), message.getNickName());
                ownThreadId = ConversationSqlManager.querySessionIdForBySessionId(contactIds);
                if (ownThreadId == 0) {
                    try {
                        ownThreadId = ConversationSqlManager.insertSessionRecord(message);
                    } catch (Exception e) {
                        LogUtil.e(LOG_TAG + " " + e.toString());
                    }
                }
                if (ownThreadId > 0) {
                    int isread = IMESSENGER_TYPE_UNREAD;
                    if (boxType == IMESSENGER_BOX_TYPE_OUTBOX
                            || boxType == IMESSENGER_BOX_TYPE_DRAFT) {
                        isread = IMESSENGER_TYPE_READ;
                    }
                    ContentValues values = new ContentValues();
                    if (boxType == IMESSENGER_BOX_TYPE_DRAFT) {
                        try { // 草稿箱只保存文本
                            values.put(IMessageColumn.OWN_THREAD_ID, ownThreadId);
                            values.put(IMessageColumn.SENDER, message.getForm());
                            values.put(IMessageColumn.MESSAGE_ID, message.getMsgId());
                            values.put(IMessageColumn.MESSAGE_TYPE, message.getType().ordinal());
                            values.put(IMessageColumn.SEND_STATUS, message.getMsgStatus().ordinal());
                            values.put(IMessageColumn.READ_STATUS, isread);
                            values.put(IMessageColumn.BOX_TYPE, boxType);
                            values.put(IMessageColumn.BODY, ((ECTextMessageBody) message.getBody()).getMessage());
                            values.put(IMessageColumn.USER_DATA, message.getUserData());
                            values.put(IMessageColumn.RECEIVE_DATE, System.currentTimeMillis());
                            values.put(IMessageColumn.CREATE_DATE, message.getMsgTime());

                            row = getInstance().sqliteDB().insertOrThrow(DatabaseHelper.TABLES_NAME_IM_MESSAGE, null, values);
                        } catch (SQLException e) {
                            LogUtil.e(LOG_TAG + " " + e.toString());
                        } finally {
                            ChattingFragment.isFireMsg = false;
                            values.clear();
                        }
                    } else {
                        try {
                            values.put(IMessageColumn.OWN_THREAD_ID, ownThreadId);
                            values.put(IMessageColumn.MESSAGE_ID, message.getMsgId());
                            values.put(IMessageColumn.SEND_STATUS, message.getMsgStatus().ordinal());
                            values.put(IMessageColumn.READ_STATUS, isread);
                            values.put(IMessageColumn.BOX_TYPE, boxType);
                            // values.put(IMessageColumn.VERSION,
                            // message.getVersion());
                            values.put(IMessageColumn.USER_DATA, message.getUserData());
                            values.put(IMessageColumn.RECEIVE_DATE, System.currentTimeMillis());
                            values.put(IMessageColumn.CREATE_DATE, message.getMsgTime());
                            values.put(IMessageColumn.SENDER, message.getForm());
                            values.put(IMessageColumn.MESSAGE_TYPE, message.getType().ordinal());

                            if (message.getType() == ECMessage.Type.VIDEO
                                    && message.getDirection() == ECMessage.Direction.RECEIVE) {

                                ECVideoMessageBody videoBody = (ECVideoMessageBody) message.getBody();
                                values.put(IMessageColumn.BODY, videoBody.getLength() + "");
                            }

                            if(message.getType()== ECMessage.Type.RICH_TEXT){
                                ECPreviewMessageBody body=(ECPreviewMessageBody) message.getBody();
                                values.put(IMessageColumn.FILE_URL,body.getUrl());
                                values.put(IMessageColumn.BODY,body.getTitle());
                                values.put(IMessageColumn.FILE_PATH,body.getLocalUrl());
                            }
                            if (message.getType() == ECMessage.Type.IMAGE && message.getDirection() == ECMessage.Direction.RECEIVE) {
                                values.put(IMessageColumn.BODY, message.getUserData());
                            }
                            if (message.getType() == ECMessage.Type.IMAGE && message.getDirection() == ECMessage.Direction.SEND && ChattingFragment.isFireMsg) {
                                values.put(IMessageColumn.BODY, "fireMessage");
                            }

                            // 其他的数据存储
                            putValues(message, values);
                            LogUtil.d(LOG_TAG, "[insertIMessage] " + values.toString());
                            row = getInstance().sqliteDB().insertOrThrow(
                                    DatabaseHelper.TABLES_NAME_IM_MESSAGE,
                                    null, values);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            LogUtil.e(LOG_TAG + " " + e.toString());
                        } finally {
                            ChattingFragment.isFireMsg = false;
                            values.clear();
                        }
                    }
                    getInstance().notifyChanged(contactIds);
                }
            }
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }
        return row;
    }



    /**
     * 根据不同的消息类型将数据保存到数据库
     *
     * @param message 消息
     * @param values  存储字段
     */
    private static void putValues(ECMessage message, ContentValues values) {
        if (message.getType() == ECMessage.Type.TXT) {
            // 文本
            values.put(IMessageColumn.BODY, ((ECTextMessageBody) message.getBody()).getMessage());
        } else if (message.getType() == ECMessage.Type.LOCATION) {
            // 地理位置
            values.put(IMessageColumn.BODY, message.getBody().toString());
        } else if (message.getType() == ECMessage.Type.CALL) {
            // 呼叫类型
            ECCallMessageBody callBody = (ECCallMessageBody) message.getBody();
            values.put(IMessageColumn.BODY, callBody.getCallText());
        } else {
            // 富文本
            if (message.getType() == ECMessage.Type.RICH_TEXT) {
                ECPreviewMessageBody body = (ECPreviewMessageBody) message.getBody();
                values.put(IMessageColumn.FILE_URL, body.getUrl());
                values.put(IMessageColumn.BODY, body.getTitle());
            } else {
                // 其他文件类型的
                ECFileMessageBody body = (ECFileMessageBody) message.getBody();
                values.put(IMessageColumn.FILE_PATH, body.getLocalUrl());
                values.put(IMessageColumn.FILE_URL, body.getRemoteUrl());
                if (message.getType() == ECMessage.Type.VOICE) {
                    ECVoiceMessageBody VoiceBody = (ECVoiceMessageBody) message .getBody();
                    values.put(IMessageColumn.DURATION, VoiceBody.getDuration());
                }

            }
        }
    }

    /**
     * 删除会话及会话的消息
     * @param contactId     会话ID
     */
    public static void deleteChattingMessage(String contactId) {
        //查询会话的数据库ID
        long sessionId = ConversationSqlManager.querySessionIdForBySessionId(contactId);
        deleteChattingMessage(sessionId);
        ConversationSqlManager.getInstance().delSession(contactId);
    }

    /**
     * 删除会话消息
     * @param sessionId 会话的数据库ID
     */
    public static void deleteChattingMessage(long sessionId) {
        while (true) {
            ArrayList<ECMessage> iMessageList = IMessageSqlManager.queryIMessageList(sessionId, 50, null);
            if (iMessageList != null && !iMessageList.isEmpty()) {
                for (ECMessage detail : iMessageList) {
                    delSingleMsg(detail.getMsgId());
                }
                continue;
            }
            return;
        }
    }


    /**
     * IM分页查询
     *
     * @param threadId 会话的数据库ID
     * @param num      分页条数
     * @param lastTime 上一页最后一条时间
     * @return ArrayList<ECMessage>
     */
    public static ArrayList<ECMessage> queryIMessageList(long threadId, int num, String lastTime) {
        ArrayList<ECMessage> al = null;
        Cursor cursor = null;
        StringBuffer sb = new StringBuffer();
        if (lastTime != null && !lastTime.equals("") && !lastTime.equals("0")) {
            sb.append(IMessageColumn.CREATE_DATE + " < ").append(lastTime);
        } else {
            sb.append("1=1");
        }
        // if (threadId != 0) {
        sb.append(" and " + IMessageColumn.OWN_THREAD_ID + " = ").append(threadId);
        // }
        sb.append(" and " + IMessageColumn.BOX_TYPE + " != " + ECMessage.Direction.DRAFT.ordinal());
        try {
            cursor = getInstance().sqliteDB().query(false,
                    DatabaseHelper.TABLES_NAME_IM_MESSAGE, null, sb.toString(),
                    null, null, null, IMessageColumn.RECEIVE_DATE + " desc",
                    num == 0 ? null : String.valueOf(num));
            if (cursor != null) {
                if (cursor.getCount() == 0) {
                    return null;
                }
                al = new ArrayList<ECMessage>();
                while (cursor.moveToNext()) {
                    ECMessage ecMessage = packageMessage(cursor);
                    if (ecMessage == null) {
                        continue;
                    }
                    al.add(0, ecMessage);
                }
            }
        } catch (Exception e) {
            LogUtil.e(LOG_TAG + " " + e.toString());
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return al;

    }

    /**
     * IM分页查询
     *
     * @param num
     * @param version
     * @return
     */
    public static ArrayList<ECMessage> queryIMessageVersionList(long threadId,
                                                                int num, String version) {
        ArrayList<ECMessage> al = null;
        Cursor cursor = null;
        StringBuffer sb = new StringBuffer();
        if (version != null && !version.equals("") && !version.equals("0")) {
            sb.append(IMessageColumn.ID + " < ").append(version);
        } else {
            sb.append("1=1");
        }
        // if (threadId != 0) {
        sb.append(" and " + IMessageColumn.OWN_THREAD_ID + " = ").append(
                threadId);
        // }
        sb.append(" and  " + IMessageColumn.BOX_TYPE + " != "
                + ECMessage.Direction.DRAFT.ordinal());
        try {
            cursor = getInstance().sqliteDB().query(false,
                    DatabaseHelper.TABLES_NAME_IM_MESSAGE, null, sb.toString(),
                    null, null, null, IMessageColumn.RECEIVE_DATE + " desc",
                    num == 0 ? null : String.valueOf(num));
            if (cursor != null) {
                if (cursor.getCount() == 0) {
                    return null;
                }
                al = new ArrayList<ECMessage>();
                while (cursor.moveToNext()) {

                    ECMessage ecMessage = packageMessage(cursor);
                    if (ecMessage == null) {
                        continue;
                    }
                    al.add(0, ecMessage);
                }
            }
        } catch (Exception e) {
            LogUtil.e(LOG_TAG + " " + e.toString());
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return al;
    }

    /**
     * 组装消息返回给getItem
     * @param cursor        cursor
     * @return              ECMessage
     */
    public static ECMessage packageMessage(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex(IMessageColumn.ID));
        String sender = cursor.getString(cursor.getColumnIndexOrThrow(IMessageColumn.SENDER));
        String msgId = cursor.getString(cursor.getColumnIndexOrThrow(IMessageColumn.MESSAGE_ID));
        // long ownThreadId = cursor.getLong(cursor.getColumnIndexOrThrow(IMessageColumn.OWN_THREAD_ID));
        long createDate = cursor.getLong(cursor.getColumnIndexOrThrow(IMessageColumn.CREATE_DATE));
        int version = cursor.getInt(cursor.getColumnIndexOrThrow(IMessageColumn.VERSION));
        String userData = cursor.getString(cursor.getColumnIndexOrThrow(IMessageColumn.USER_DATA));
        int read = cursor.getInt(cursor.getColumnIndexOrThrow(IMessageColumn.READ_STATUS));
        int boxType = cursor.getInt(cursor.getColumnIndexOrThrow(IMessageColumn.BOX_TYPE));
        int msgType = cursor.getInt(cursor.getColumnIndexOrThrow(IMessageColumn.MESSAGE_TYPE));
        int sendStatus = cursor.getInt(cursor.getColumnIndexOrThrow(IMessageColumn.SEND_STATUS));

        ECMessage ecMessage = ECMessage.createECMessage(ECMessage.Type.NONE);
        if (msgType == ECMessage.Type.TXT.ordinal()) {
            String content = cursor.getString(cursor.getColumnIndexOrThrow(IMessageColumn.BODY));
            ecMessage.setType(ECMessage.Type.TXT);
            ECTextMessageBody textBody = new ECTextMessageBody(content);
            ecMessage.setBody(textBody);
        } else if (msgType == ECMessage.Type.CALL.ordinal()) {
            String content = cursor.getString(cursor.getColumnIndexOrThrow(IMessageColumn.BODY));
            ECCallMessageBody body = new ECCallMessageBody(content);
            ecMessage.setType(ECMessage.Type.CALL);
            ecMessage.setBody(body);
        } else if (msgType == ECMessage.Type.LOCATION.ordinal()) {
            String content = cursor.getString(cursor.getColumnIndexOrThrow(IMessageColumn.BODY));
            ecMessage.setType(ECMessage.Type.LOCATION);

            String lon;
            String lat;
            try {
                JSONObject jsonObject = new JSONObject(content);
                lon = jsonObject.getString("lon");
                lat = jsonObject.getString("lat");
                ECLocationMessageBody textBody = new ECLocationMessageBody(Double.parseDouble(lat), Double.parseDouble(lon));
                textBody.setTitle(jsonObject.getString("title"));
                ecMessage.setBody(textBody);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            String fileUrl = cursor.getString(cursor.getColumnIndexOrThrow(IMessageColumn.FILE_URL));
            String fileLocalPath = cursor.getString(cursor.getColumnIndexOrThrow(IMessageColumn.FILE_PATH));

            if (msgType == ECMessage.Type.VOICE.ordinal()) {
                ecMessage.setType(ECMessage.Type.VOICE);
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(IMessageColumn.DURATION));
                ECVoiceMessageBody voiceBody = new ECVoiceMessageBody(new File(fileLocalPath), 0);
                voiceBody.setRemoteUrl(fileUrl);
                ecMessage.setBody(voiceBody);
                voiceBody.setDuration(duration);
            } else if (msgType == ECMessage.Type.IMAGE.ordinal()
                    || msgType == ECMessage.Type.VIDEO.ordinal()
                    || msgType == ECMessage.Type.FILE.ordinal()) {
                ECFileMessageBody fileBody = new ECFileMessageBody();
                if (msgType == ECMessage.Type.FILE.ordinal()) {
                    ecMessage.setType(ECMessage.Type.FILE);
                } else if (msgType == ECMessage.Type.IMAGE.ordinal()) {
                    fileBody = new ECImageMessageBody();
                    ecMessage.setType(ECMessage.Type.IMAGE);
                } else {
                    fileBody = new ECVideoMessageBody();
                    ecMessage.setType(ECMessage.Type.VIDEO);
                }
                fileBody.setLocalUrl(fileLocalPath);
                fileBody.setRemoteUrl(fileUrl);
                fileBody.setFileName(DemoUtils.getFileNameFormUserdata(userData));
                ecMessage.setBody(fileBody);
            } else if (msgType == ECMessage.Type.RICH_TEXT.ordinal()) {
                ECPreviewMessageBody body = new ECPreviewMessageBody();
                ecMessage.setType(ECMessage.Type.RICH_TEXT);
                String content = cursor.getString(cursor.getColumnIndexOrThrow(IMessageColumn.BODY));
                body.setTitle(content);
                body.setLocalUrl(cursor.getString(cursor.getColumnIndexOrThrow(IMessageColumn.FILE_PATH)));
                body.setUrl(cursor.getString(cursor.getColumnIndexOrThrow(IMessageColumn.FILE_URL)));
                ecMessage.setBody(body);
//				return null;
            }
        }
        ecMessage.setId(id);
        ecMessage.setFrom(sender);
        ecMessage.setMsgId(msgId);
        ecMessage.setMsgTime(createDate);
        ecMessage.setUserData(userData);
        if (sendStatus == ECMessage.MessageStatus.SENDING.ordinal()) {
            ecMessage.setMsgStatus(ECMessage.MessageStatus.SENDING);
        } else if (sendStatus == ECMessage.MessageStatus.RECEIVE.ordinal() || sendStatus == 4) {
            // sendStatus == 4 兼容以前版本
            ecMessage.setMsgStatus(ECMessage.MessageStatus.RECEIVE);
        } else if (sendStatus == ECMessage.MessageStatus.SUCCESS.ordinal()) {
            ecMessage.setMsgStatus(ECMessage.MessageStatus.SUCCESS);
        } else if (sendStatus == ECMessage.MessageStatus.FAILED.ordinal()) {
            ecMessage.setMsgStatus(ECMessage.MessageStatus.FAILED);
        }
        ecMessage.setDirection(getMessageDirect(boxType));
        return ecMessage;
    }

    /**
     * 返回消息的类型，发送、接收、草稿
     * @param type      消息类型
     * @return          ECMessage.Direction
     */
    public static ECMessage.Direction getMessageDirect(int type) {
        if (type == ECMessage.Direction.SEND.ordinal()) {
            return ECMessage.Direction.SEND;
        } else if (type == ECMessage.Direction.RECEIVE.ordinal()) {
            return ECMessage.Direction.RECEIVE;
        } else {
            return ECMessage.Direction.DRAFT;
        }
    }

    /**
     * 删除消息
     * @param id    消息ID
     */
    public static void delSingleMsg(String id) {
        ECMessage msg = getMsg(id);
        if (msg == null) {
            return;
        }
        delMessage(id);
        if (msg.getType() != ECMessage.Type.TXT && msg.getType() != ECMessage.Type.CALL) {
            ArrayList<String> fileList = new ArrayList<String>();
            ECFileMessageBody body = (ECFileMessageBody) msg.getBody();
            if (!TextUtils.isEmpty(body.getLocalUrl())) {
                if (body.getLocalUrl().startsWith("THUMBNAIL://")) {
                    //删除ImgInfo
                    ImgInfo imgInfo = ImgInfoSqlManager.getInstance().getImgInfo(msg.getMsgId());
                    if (imgInfo != null) {
                        ImgInfoSqlManager.getInstance().delImgInfo(imgInfo.getMsglocalid());
                        if (TextUtils.isEmpty(FileAccessor.getImagePathName() + "/" + imgInfo.getThumbImgPath())) {
                            return;
                        }
                        if (!new File(FileAccessor.getImagePathName() + "/" + imgInfo.getThumbImgPath()).exists()) {
                            return;
                        }
                        fileList.add(FileAccessor.getImagePathName() + "/" + imgInfo.getBigImgPath());
                        fileList.add(FileAccessor.getImagePathName() + "/" + imgInfo.getThumbImgPath());
                    }
                } else if (msg.getUserData() != null && msg.getUserData().indexOf("THUMBNAIL://") != -1) {
                    String userData = msg.getUserData();
                    int start = userData.indexOf("THUMBNAIL://");
                    if (start != -1) {
                        String thumbnail = userData.substring(start + "THUMBNAIL://".length());
                        ImgInfo imgInfo = ImgInfoSqlManager.getInstance().getImgInfo(thumbnail);
                        if (imgInfo != null) {      //删除ImgInfo
                            ImgInfoSqlManager.getInstance().delImgInfo(imgInfo.getMsglocalid());
                            if (TextUtils.isEmpty(FileAccessor.getImagePathName() + "/" + imgInfo.getThumbImgPath())) {
                                return;
                            }
                            if (!new File(FileAccessor.getImagePathName() + "/" + imgInfo.getThumbImgPath()).exists()) {
                                return;
                            }
                            fileList.add(FileAccessor.getImagePathName() + "/" + imgInfo.getBigImgPath());
                            fileList.add(FileAccessor.getImagePathName() + "/" + imgInfo.getThumbImgPath());
                        }
                    }
                } else {
                    fileList.add(body.getLocalUrl());
                }
            }
            FileAccessor.delFiles(fileList);        //删除Image文件
        }
    }

    /**
     * 根据messageId查询message
     * @param id        messageID
     * @return
     */
    public static ECMessage getMsg(String id) {
        String sql = "select * from " + DatabaseHelper.TABLES_NAME_IM_MESSAGE
                + " where " + IMessageColumn.MESSAGE_ID + " = '" + id + ",";
        Cursor cursor = null;
        try {
            cursor = getInstance().sqliteDB().rawQuery(sql, null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    return packageMessage(cursor);
                }
            }
        } catch (Exception e) {
            LogUtil.e(LOG_TAG + " " + e.toString());
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
        return null;
    }

    /**
     * 根据MessageId删除Message
     * @param id        messageID
     */
    public static void delMessage(String id) {
        getInstance().sqliteDB().delete(DatabaseHelper.TABLES_NAME_IM_MESSAGE,
                IMessageColumn.MESSAGE_ID + "=?", new String[]{id});
    }

    /**
     * 更新消息的状态
     *
     * @param msgId         消息ID
     * @param sendStatus    发送状态
     * @return
     */
    public static int setIMessageSendStatus(String msgId, int sendStatus) {
        return setIMessageSendStatus(msgId, sendStatus, 0);
    }

    /**
     * 设置Im消息发送状态
     *
     * @param msgId         消息ID
     * @param sendStatus    发送状态
     * @return
     */
    public static int setIMessageSendStatus(String msgId, int sendStatus, int duration) {
        int row = 0;
        ContentValues values = new ContentValues();
        try {
            String where = IMessageColumn.MESSAGE_ID + "=? and " + IMessageColumn.SEND_STATUS + "!=? ";
            values.put(IMessageColumn.SEND_STATUS, sendStatus);
            if (duration > 0) {
                values.put(IMessageColumn.DURATION, duration);
            }
            row = getInstance().sqliteDB().update(DatabaseHelper.TABLES_NAME_IM_MESSAGE, values, where, new String[]{msgId, String.valueOf(sendStatus)});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (values != null) {
                values.clear();
                values = null;
            }
        }
        return row;
    }

    /**
     * 查询某一会话所有信息条数
     * @return
     */
    public static int queryIMCountForSession(long threadId) {
        int count = 0;
        String[] columnsList = {"count(*)"};
        String where = IMessageColumn.OWN_THREAD_ID + " = " + threadId
                + " and " + IMessageColumn.BOX_TYPE + " != 3";
        Cursor cursor = null;
        try {
            cursor = getInstance().sqliteDB().query(DatabaseHelper.TABLES_NAME_IM_MESSAGE, columnsList, where,
                    null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    count = cursor.getInt(cursor.getColumnIndex("count(*)"));
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
        return count;
    }

    public static Cursor queryIMessageCursor(long threadId, int limit) {
        String sql = "Select * from " + DatabaseHelper.TABLES_NAME_IM_MESSAGE
                + " where " + IMessageColumn.OWN_THREAD_ID + " = " + threadId
                + " order by " + IMessageColumn.RECEIVE_DATE + " ASC LIMIT "
                + limit + " offset " + "(select count(*) from "
                + DatabaseHelper.TABLES_NAME_IM_MESSAGE + " where "
                + IMessageColumn.OWN_THREAD_ID + " = " + threadId + ") -" + limit;
        LogUtil.d(LOG_TAG, "getCursor threadId:" + threadId + " limit:" + limit
                + " [" + sql + "]");
        return getInstance().sqliteDB().rawQuery(sql, null);
    }

    public static Cursor getNullCursor() {
        return getInstance().sqliteDB().query(DatabaseHelper.TABLES_NAME_IM_MESSAGE, null, "msgid = ?", new String[]{"-1"}, null, null, null);
    }






}
