package com.zhang.demo.ytx.common.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * 图像处理工具类
 * Created by Administrator on 2016/7/15.
 */
public class BitmapUtil {
    private static final String LOG_TAG = BitmapUtil.class.getSimpleName();

    /**
     * 将多张图片合成一张图片
     * @param mEntityList       图片信息
     * @param bitmaps           图片集合
     * @return                  Bitmap
     */
    public static Bitmap getCombineBitmaps(List<InnerBitmapEntity> mEntityList, Bitmap... bitmaps) {
        LogUtil.d(LOG_TAG , "count=" + mEntityList.size());
        Bitmap newBitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        LogUtil.d(LOG_TAG, "newBitmap=" + newBitmap.getWidth() + "," + newBitmap.getHeight());
        for (int i = 0; i < mEntityList.size(); i++) {
            newBitmap = mixtureBitmap(newBitmap, bitmaps[i], new PointF(mEntityList.get(i).x, mEntityList.get(i).y));
        }
        return newBitmap;
    }

    /**
     * 两张图片合成一张图片
     * @param first         第一张图片
     * @param second        第二章图片
     * @param fromPoint     第二章图片合成位置
     * @return              Bitmap
     */
    public static Bitmap mixtureBitmap(Bitmap first, Bitmap second, PointF fromPoint) {
        if (first == null || second == null || fromPoint == null) {
            return null;
        }
        Bitmap newBitmap = Bitmap.createBitmap(first.getWidth(), first.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newBitmap);
        cv.drawBitmap(first, 0, 0, null);
        cv.drawBitmap(second, fromPoint.x, fromPoint.y, null);
        cv.save(Canvas.ALL_SAVE_FLAG);
        cv.restore();
        return newBitmap;
    }

    /**
     * 保存图片到本地
     * @param outPath       图片路径
     * @param bitmap        图片
     * @return              图片本地路径
     */
    public static String saveBitmapToLocal(String outPath, Bitmap bitmap) {
        try {
            String imagePath = FileAccessor.getAvatarPathName() + "/" + DemoUtils.md5(outPath);
            File file = new File(imagePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bufferedOutputStream);
            bufferedOutputStream.close();
            return imagePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String ATTACT_ICON = "demo_default";

    /**
     * 保存图片到SD卡
     * @param bitmap        图片
     * @param fileName      图片名称
     * @return              图片保存路径
     */
    public static String saveBitmapToLocalSDCard(Bitmap bitmap, String fileName) {
        try {
            String imagePath = FileAccessor.IMESSAGE_RICH_TEXT + "/" + DemoUtils.md5(fileName)+".jpg";
            File file = new File(imagePath);
            if(!file.exists()) {
                file.createNewFile();
            }
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bufferedOutputStream);
            bufferedOutputStream.close();
            LogUtil.d(LOG_TAG, "photo image from data, path:" + imagePath);
            return imagePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class InnerBitmapEntity {
        public float x;
        public float y;
        public float width;
        public float height;
        public static int devide = 1;
        public int index = -1;

        @Override
        public String toString() {
            return "InnerBitmapEntity [x=" + x + ", y=" + y + ", width=" + width
                    + ", height=" + height + ", devide=" + devide + ", index="
                    + index + "]";
        }
    }

}
