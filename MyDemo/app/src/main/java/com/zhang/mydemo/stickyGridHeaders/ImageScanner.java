package com.zhang.mydemo.stickyGridHeaders;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;


/**
 * Created by zjun on 2015/6/18.
 */
public class ImageScanner {
    private Context mContext;

    public ImageScanner(Context context) {
        this.mContext = context;
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
     */
    public void scanImages(final ScanCompleteCallBack callback) {
        final Handler mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                callback.scanComplete((Cursor) msg.obj);
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                //先发送广播扫描下整个sd卡
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Intent mediaScanIntent = new Intent(
                            Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                    Uri contentUri = Uri.fromFile(out);
                    Uri contentUri = Uri.parse("file://"
                            + Environment.getExternalStorageDirectory());
                    //out is your output file
                    mediaScanIntent.setData(contentUri);
                    mContext.sendBroadcast(mediaScanIntent);
                } else {
                    mContext.sendBroadcast(new Intent(
                            Intent.ACTION_MEDIA_MOUNTED,
                            Uri.parse("file://"
                                    + Environment.getExternalStorageDirectory())));
                }
//                //先发送广播扫描下整个sd卡
//                mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
//                        Uri.parse("file://" + Environment.getExternalStorageDirectory())));

                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = mContext.getContentResolver();

                Cursor mCursor = mContentResolver.query(mImageUri, null, null, null, MediaStore.Images.Media.DATE_ADDED);
                Message msg = mHandler.obtainMessage();
                msg.obj = mCursor;
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    /**
     * 扫描完成之后的回调接口
     *
     */
    public static interface ScanCompleteCallBack {
        public void scanComplete(Cursor cursor);
    }
}
