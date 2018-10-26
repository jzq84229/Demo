package com.zhang.demo.ytx.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.View;

import com.zhang.demo.ytx.common.utils.LogUtil;
import com.zhang.demo.ytx.ui.chatting.model.ImgInfo;

import java.util.HashMap;
import java.util.List;

/**
 * 图片保存
 * Created by Administrator on 2016/7/15.
 */
public class ImgInfoSqlManager extends AbstractSQLManager {
    public HashMap<String, Bitmap> imgThumbCache = new HashMap<>(20);
    private static int column_index = 1;
    public static ImgInfoSqlManager mInstance;

    private ImgInfoSqlManager() {
        Cursor cursor = sqliteDB().query(DatabaseHelper.TABLES_NAME_IMGINFO, null, null, null, null, null, ImgInfoColumn.ID + " ASC ");
        if (cursor.getCount() > 0 && cursor.moveToLast()) {
            column_index = 1 + cursor.getInt(cursor.getColumnIndex(ImgInfoColumn.ID));
        }
        cursor.close();
        LogUtil.d(LogUtil.getLogUtilsTag(getClass()), "loading new img id:" + column_index);
    }

    public static ImgInfoSqlManager getInstance() {
        if (mInstance == null) {
            mInstance = new ImgInfoSqlManager();
        }
        return mInstance;
    }

    /**
     * 插入imgInfo
     * @param imgInfo       imgInfo
     * @return              数据库id
     */
    public long insertImageInfo(ImgInfo imgInfo) {
        if (imgInfo == null) {
            return -1;
        }
        ContentValues buildContentValues = imgInfo.buildContentValues();
        if (buildContentValues.size() == 0) {
            return -1;
        }
        try {
            return sqliteDB().insert(DatabaseHelper.TABLES_NAME_IMGINFO, null, buildContentValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 更新imgInfo
     */
    public long updateImageInfo(ImgInfo imgInfo) {
        if (imgInfo == null) {
            return -1;
        }
        ContentValues buildContentValues = imgInfo.buildContentValues();
        if (buildContentValues.size() == 0) {
            return -1;
        }
        try {
            String where = ImgInfoColumn.ID + " = " + imgInfo.getId();
            return sqliteDB().update(DatabaseHelper.TABLES_NAME_IMGINFO, buildContentValues, where, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 根据messageId查询ImgInfo
     * @param msgId     messageId
     * @return          ImgInfo
     */
    public ImgInfo getImgInfo(String msgId) {
        ImgInfo imgInfo = new ImgInfo();
        String where = ImgInfoColumn.MSG_LOCAL_ID + " = '" + msgId + "'";
        Cursor cursor = sqliteDB().query(DatabaseHelper.TABLES_NAME_IMGINFO, null, where, null, null, null, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();imgInfo.setCursor(cursor);
        }
        cursor.close();
        return imgInfo;
    }

    /**
     * 根据数据库ID查询ImgInfo
     * @param id        数据库ID
     * @return          ImgInfo
     */
    public ImgInfo getImgInfo(int id) {
        ImgInfo imgInfo = new ImgInfo();
        String where = ImgInfoColumn.ID + "=" + id;
        Cursor cursor = sqliteDB().query(DatabaseHelper.TABLES_NAME_IMGINFO, null, where, null, null, null, null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            imgInfo.setCursor(cursor);
        }
        cursor.close();
        return imgInfo;
    }

    /**
     * 根据messageId删除ImgInfo
     * @param msgId         messageId
     * @return              删除的条数
     */
    public long delImgInfo(String msgId) {
        return getInstance().sqliteDB().delete(DatabaseHelper.TABLES_NAME_IMGINFO, ImgInfoColumn.MSG_LOCAL_ID + "=?", new String[]{msgId});
    }





}
