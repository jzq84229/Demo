/*
 *  Copyright (c) 2015 The CCP project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a Beijing Speedtong Information Technology Co.,Ltd license
 *  that can be found in the LICENSE file in the root of the web site.
 *
 *   http://www.yuntongxun.com
 *
 *  An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */
package com.zhang.common.lib.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件操作工具类
 */
public class FileUtils {

    public static final String TAG = FileUtils.class.getName();

    /**
     * 初始化应用文件夹目录
     */
    public static boolean initFileAccess(Context context) {
        if (!isExistExternalStore()) {
            return false;
        }
        return true;
    }

//    /**
//     * 文件临时保存路径
//     * @return
//     */
//    public static String getSaveFileDir(){
//        if(!isExistExternalStore()){
//            return null;
//        }
//        String path = getExternalStorePath()+"/file";
//        File file = new File(path);
//        if(!file.exists()){
//            boolean success =  file.mkdirs();
//            if(!success){
//                return null;
//            }
//        }
//        return path;
//    }

    /**
     * 外置存储卡的路径
     *
     * @return
     */
    public static String getExternalStorePath() {
        if (isExistExternalStore()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }

    /**
     * 是否有外存卡
     *
     * @return
     */
    public static boolean isExistExternalStore() {
        return (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED));
    }

    /**
     * 检查SDCARD是否可写
     *
     * @return
     */
    public static boolean checkExternalStorageCanWrite() {
        try {
            if (isExistExternalStore()) {
                boolean canWrite = new File(Environment
                        .getExternalStorageDirectory().getAbsolutePath())
                        .canWrite();
                if (canWrite) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 读取文本文件
     *
     * @param fileName
     * @return
     */
    public static String read(String fileName) {
        try {
            File file = new File(fileName);
            InputStream in = new FileInputStream(file);
            return readInStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String readInStream(InputStream inStream) {
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[512];
            int length = -1;
            while ((length = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
            }
            outStream.close();
            inStream.close();
            return outStream.toString();
        } catch (IOException e) {
            Log.i("FileTest", e.getMessage());
        }
        return null;
    }

    /**
     * 保存数据到文件
     *
     * @param filePath
     * @param data
     * @return
     */
    public static boolean saveFile(String filePath, byte[] data) {
        boolean flag = false;
        if (isExistExternalStore()) {
            File file = new File(filePath);
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
            }
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
                out.write(data);
                flag = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 重命名文件
     * @param oldFile
     * @param newName
     */
    public static void rename(File oldFile, String newName) {
        if (oldFile.exists()) {
            //oldPath like "mnt/sda/sda1/我.png"
            String dirpath = oldFile.getParentFile().getAbsolutePath();
            File newfile = new File((dirpath + "/" + newName));
            if (!newfile.exists()) {
                //newPath like "mnt/sda/sda1/我的照片.png"
                oldFile.renameTo(newfile);
            } else {
                LogUtils.e(TAG, "rename file, newFile is already exists!");
            }
        } else {
            LogUtils.e(TAG, "rename file, oldFile is not exists!");
        }
    }

    /**
     * 检查SD卡是否有足够空间
     *
     * @param context
     * @return
     */
    public static boolean isSDHasFreeSpace(Context context) {
        if (isExistExternalStore()) {
            if (getSDFreeSize() > 50) {
                return true;
            } else {
                ToastUtils.showMessage(context, "存储设备没有足够的空间");
            }
        } else {
            ToastUtils.showMessage(context, "未检测到可用的设备");
        }
        return false;
    }

    /**
     * SD卡空间大小
     *
     * @return
     */
    public static long getSDFreeSize() {
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        // 返回SD卡空闲大小
        // return freeBlocks * blockSize; //单位Byte
        // return (freeBlocks * blockSize)/1024; //单位KB
        return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
    }

    /**
     * 转换成单位
     *
     * @param length
     * @return
     */
    public static String formatFileLength(long length) {
        if (length >> 30 > 0L) {
            float sizeGb = Math.round(10.0F * (float) length / 1.073742E+009F) / 10.0F;
            return sizeGb + "GB";
        }
        if (length >> 20 > 0L) {
            return formatSizeMb(length);
        }
        if (length >> 9 > 0L) {
            float sizekb = Math.round(10.0F * (float) length / 1024.0F) / 10.0F;
            return sizekb + "KB";
        }
        return length + "B";
    }

    /**
     * 转换成Mb单位
     *
     * @param length
     * @return
     */
    public static String formatSizeMb(long length) {
        float mbSize = Math.round(10.0F * (float) length / 1048576.0F) / 10.0F;
        return mbSize + " MB";
    }

    /**
     * 从URI获取文件地址
     *
     * @param context 上下文
     * @param uri     文件uri
     */
    @SuppressLint("NewApi")
    public static String getFilePath(Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    private static String getDataColumn(Context context, Uri uri, String selection,
                                        String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * 是否图片
     *
     * @param fileName
     * @return
     */
    public static boolean isPic(String fileName) {
        String lowerCase = StringUtils.getString(fileName).toLowerCase();
        return lowerCase.endsWith(".bmp") || lowerCase.endsWith(".png")
                || lowerCase.endsWith(".jpg") || lowerCase.endsWith(".jpeg")
                || lowerCase.endsWith(".gif");
    }

    /**
     * 是否压缩文件
     *
     * @param fileName
     * @return
     */
    public static boolean isCompresseFile(String fileName) {
        String lowerCase = StringUtils.getString(fileName).toLowerCase();
        return lowerCase.endsWith(".rar") || lowerCase.endsWith(".zip")
                || lowerCase.endsWith(".7z") || lowerCase.endsWith("tar")
                || lowerCase.endsWith(".iso");
    }

    /**
     * 是否音频
     *
     * @param fileName
     * @return
     */
    public static boolean isAudio(String fileName) {
        String lowerCase = StringUtils.getString(fileName).toLowerCase();
        return lowerCase.endsWith(".mp3") || lowerCase.endsWith(".wma")
                || lowerCase.endsWith(".mp4") || lowerCase.endsWith(".rm");
    }

    /**
     * 是否文档
     *
     * @param fileName
     * @return
     */
    public static boolean isDocument(String fileName) {
        String lowerCase = StringUtils.getString(fileName).toLowerCase();
        return lowerCase.endsWith(".doc") || lowerCase.endsWith(".docx")
                || lowerCase.endsWith("wps");
    }

    /**
     * 是否Pdf
     *
     * @param fileName
     * @return
     */
    public static boolean isPdf(String fileName) {
        return StringUtils.getString(fileName).toLowerCase().endsWith(".pdf");
    }

    /**
     * 是否Excel
     *
     * @param fileName
     * @return
     */
    public static boolean isXls(String fileName) {
        String lowerCase = StringUtils.getString(fileName).toLowerCase();
        return lowerCase.endsWith(".xls") || lowerCase.endsWith(".xlsx");
    }

    /**
     * 是否文本文档
     *
     * @param fileName
     * @return
     */
    public static boolean isTextFile(String fileName) {
        String lowerCase = StringUtils.getString(fileName).toLowerCase();
        return lowerCase.endsWith(".txt") || lowerCase.endsWith(".rtf");
    }

    /**
     * 是否Ppt
     *
     * @param fileName
     * @return
     */
    public static boolean isPPt(String fileName) {
        String lowerCase = StringUtils.getString(fileName).toLowerCase();
        return lowerCase.endsWith(".ppt") || lowerCase.endsWith(".pptx");
    }

    private static final String[][] MIME_MapTable = {
            //{后缀名，MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param filePath
     */
    public static String getMIMEType(String filePath) {
        String type = "*/*";
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = filePath.lastIndexOf(".");
        if (dotIndex < 0) {
            return null;
        }
        /* 获取文件的后缀名*/
        String end = filePath.substring(dotIndex, filePath.length()).toLowerCase();
        if (end == "") return null;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

//    /**
//     * OKhttp下载数据保存到文件
//     * @param body
//     * @param file
//     * @return
//     */
//    public static boolean writeResponseBodyToDisk(ResponseBody body, File file) {
//        try {
//            InputStream inputStream = null;
//            OutputStream outputStream = null;
//
//            try {
//                byte[] fileReader = new byte[4096];
//                long fileSize = body.contentLength();
//                long fileSizeDownloaded = 0;
//                inputStream = body.byteStream();
//                outputStream = new FileOutputStream(file);
//
//                while (true) {
//                    int read = inputStream.read(fileReader);
//                    if (read == -1) {
//                        break;
//                    }
//                    outputStream.write(fileReader, 0, read);
//                    fileSizeDownloaded += read;
//                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
//                }
//                outputStream.flush();
//                return true;
//            } catch (IOException e) {
//                return false;
//            } finally {
//                if (inputStream != null) {
//                    inputStream.close();
//                }
//                if (outputStream != null) {
//                    outputStream.close();
//                }
//            }
//        } catch (IOException e) {
//            return false;
//        }
//    }
}
