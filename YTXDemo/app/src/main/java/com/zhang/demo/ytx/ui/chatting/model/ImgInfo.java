package com.zhang.demo.ytx.ui.chatting.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.zhang.demo.ytx.storage.AbstractSQLManager;

/**
 * 图片消息
 * Created by Administrator on 2016/7/15.
 */
public class ImgInfo {
    private static int VALUES_ENPTY = -1;

    private long id = -1;
    private int msgSvrId= -1;
    private int offset = -1;
    private int totalLen = -1;
    private String bigImgPath;
    private String thumbImgPath;
    private int createtime = -1;
    private String msglocalid;
    private int status = -1;
    private int nettimes = -1;
    public boolean isGif = false;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMsgSvrId() {
        return msgSvrId;
    }

    public void setMsgSvrId(int msgSvrId) {
        this.msgSvrId = msgSvrId;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getTotalLen() {
        return totalLen;
    }

    public void setTotalLen(int totalLen) {
        this.totalLen = totalLen;
    }

    public String getBigImgPath() {
        return bigImgPath;
    }

    public void setBigImgPath(String bigImgPath) {
        this.bigImgPath = bigImgPath;
    }

    public String getThumbImgPath() {
        return thumbImgPath;
    }

    public void setThumbImgPath(String thumbImgPath) {
        this.thumbImgPath = thumbImgPath;
    }

    public int getCreatetime() {
        return createtime;
    }

    public void setCreatetime(int createtime) {
        this.createtime = createtime;
    }

    public String getMsglocalid() {
        return msglocalid;
    }

    public void setMsglocalid(String msglocalid) {
        this.msglocalid = msglocalid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNettimes() {
        return nettimes;
    }

    public void setNettimes(int nettimes) {
        this.nettimes = nettimes;
    }

    public boolean isGif() {
        return isGif;
    }

    public void setGif(boolean gif) {
        isGif = gif;
    }

    public void setCursor(Cursor cursor) {
        this.id = cursor.getInt(cursor.getColumnIndex(AbstractSQLManager.ImgInfoColumn.ID));
        this.msgSvrId = cursor.getInt(cursor.getColumnIndex(AbstractSQLManager.ImgInfoColumn.MSGSVR_ID));
        this.offset = cursor.getInt(cursor.getColumnIndex(AbstractSQLManager.ImgInfoColumn.OFFSET));
        this.totalLen = cursor.getInt(cursor.getColumnIndex(AbstractSQLManager.ImgInfoColumn.TOTALLEN));
        this.bigImgPath = cursor.getString(cursor.getColumnIndex(AbstractSQLManager.ImgInfoColumn.BIG_IMGPATH));
        this.thumbImgPath = cursor.getString(cursor.getColumnIndex(AbstractSQLManager.ImgInfoColumn.THUMBIMG_PATH));
        this.createtime = cursor.getInt(cursor.getColumnIndex(AbstractSQLManager.ImgInfoColumn.CREATE_TIME));
        this.msglocalid = cursor.getString(cursor.getColumnIndex(AbstractSQLManager.ImgInfoColumn.MSG_LOCAL_ID));
        this.status = cursor.getInt(cursor.getColumnIndex(AbstractSQLManager.ImgInfoColumn.STATUS));
        this.nettimes = cursor.getInt(cursor.getColumnIndex(AbstractSQLManager.ImgInfoColumn.NET_TIMES));
    }

    public ContentValues buildContentValues() {
        ContentValues values = new ContentValues();
        if(id != VALUES_ENPTY) {
            values.put(AbstractSQLManager.ImgInfoColumn.ID, id);
        }
        if(msgSvrId != VALUES_ENPTY) {
            values.put(AbstractSQLManager.ImgInfoColumn.MSGSVR_ID, msgSvrId);
        }
        if(offset != VALUES_ENPTY) {
            values.put(AbstractSQLManager.ImgInfoColumn.OFFSET, offset);
        }
        if(totalLen != VALUES_ENPTY) {
            values.put(AbstractSQLManager.ImgInfoColumn.TOTALLEN, totalLen);
        }
        if(!TextUtils.isEmpty(bigImgPath)) {
            values.put(AbstractSQLManager.ImgInfoColumn.BIG_IMGPATH, bigImgPath);
        }
        if(!TextUtils.isEmpty(thumbImgPath)) {
            values.put(AbstractSQLManager.ImgInfoColumn.THUMBIMG_PATH, thumbImgPath);
        }
        if(createtime != VALUES_ENPTY) {
            values.put(AbstractSQLManager.ImgInfoColumn.CREATE_TIME, createtime);
        }
        if(!TextUtils.isEmpty(msglocalid)) {
            values.put(AbstractSQLManager.ImgInfoColumn.MSG_LOCAL_ID, msglocalid);
        }
        if(status != VALUES_ENPTY) {
            values.put(AbstractSQLManager.ImgInfoColumn.STATUS, status);
        }
        if(nettimes != VALUES_ENPTY) {
            values.put(AbstractSQLManager.ImgInfoColumn.NET_TIMES, nettimes);
        }
        return values;
    }
}
