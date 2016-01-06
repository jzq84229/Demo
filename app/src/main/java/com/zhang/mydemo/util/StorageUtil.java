package com.zhang.mydemo.util;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

/** 
 * @Description: 文件存储工具类
 * @author zhangjun
 * @date 2014年11月14日 上午8:28:12 
 * @version V1.0 
 */
public class StorageUtil {

    private static final int ERROR = -1;

    /**
     * 获取存储路径
     * @param context
     * @return
     */
    public static File getCacheDirectory(Context context) {
		File appCacheDir = null;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			appCacheDir = Environment.getExternalStorageDirectory();
		}
		if (appCacheDir == null) {
			appCacheDir = context.getCacheDir();
		}
		return appCacheDir;
	}

    /**
     * SDCARD是否存在
     */
    public static boolean externalMemoryAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取手机内部剩余存储空间
     * @return
     */
    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * 获取手机内部总的存储空间
     * @return
     */
    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    /**
     * 获取SDCARD剩余存储空间
     * @return
     */
    public static long getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return ERROR;
        }
    }

    /**
     * 获取SDCARD总的存储空间
     * @return
     */
    public static long getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            return ERROR;
        }
    }
}
