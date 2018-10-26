package com.zhang.demo.ytx.ui.chatting.model;

import android.database.Cursor;
import android.text.TextUtils;

import com.zhang.demo.ytx.storage.ContactSqlManager;
import com.zhang.demo.ytx.ui.contact.ECContacts;

/**
 * 会话Entity
 * Created by Administrator on 2016/7/12.
 */
public class Conversation {
    private String sessionId;
    private int msgType;
    private long dateTime;
    private int sendStatus;
    private int unreadCount;
    private String content;
    private String username;
    private String contactId;
    private boolean isNotice;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dataTime) {
        this.dateTime = dataTime;
    }

    public int getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(int sendStatus) {
        this.sendStatus = sendStatus;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public boolean isNotice() {
        return isNotice;
    }

    public void setNotice(boolean notice) {
        isNotice = notice;
    }

    public void setCursor(Cursor cursor) {
        this.unreadCount = cursor.getInt(0);
        this.msgType = cursor.getInt(1);
        this.sendStatus = cursor.getInt(2);
        this.dateTime = cursor.getLong(3);
        this.sessionId = cursor.getString(4);
        this.content = cursor.getString(5);
        this.username = cursor.getString(6);

        if(this.sessionId.toLowerCase().startsWith("g")) {
            this.username = cursor.getString(7);
        }
        if(this.username == null && !this.sessionId.toUpperCase().startsWith("G")) {
            ECContacts contacts = ContactSqlManager.getCacheContact(sessionId);
            if(contacts != null) {
                username = contacts.getNickname();
            } else {
                username = sessionId;
            }
        }
        if(TextUtils.isEmpty(this.username)) {
            this.username  = sessionId;
        }
        this.contactId = cursor.getString(8);
        this.isNotice = !(cursor.getInt(9) == 2);
    }
}
