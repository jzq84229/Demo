package com.zhang.demo.ytx.common.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Created by Administrator on 2016/7/5.
 */
public class DemoUtils {
    private static final String LOG_TAG = DemoUtils.class.getSimpleName();

    private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;
    private static boolean inNativeAllocAccessError = false;
    /** 当前SDK版本号 */
    private static int mSdkint = -1;

    /**
     * 过滤字符串为空
     */
    public static String nullAsNil(String str) {
        if (str == null) {
            return "";
        }
        return str;
    }

    /**
     * SDK版本号
     */
    public static int getSdkint() {
        if (mSdkint < 0) {
            mSdkint = Build.VERSION.SDK_INT;
        }
        return mSdkint;
    }

    public static final String PHONE_PREFIX = "+86";

    /**
     * 去除+86
     */
    public static String formatPhone(String phoneNumber) {
        if (phoneNumber == null) {
            return "";
        }
        if (phoneNumber.startsWith(PHONE_PREFIX)) {
            return phoneNumber.substring(PHONE_PREFIX.length()).trim();
        }
        return phoneNumber.trim();
    }

    /**
     * 删除号码中的所有非数字
     * @param str
     * @return
     */
    public static String filteUnNumber(String str) {
        if (str == null) {
            return null;
        }
        if (str.startsWith("+86")) {
            str = str.substring(3, str.length());
        }

        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        // 替换与模式匹配的所有字符（即非数字的字符将被""替换）
        // 对voip造成的负数号码，做处理
        if (str.startsWith("-")) {
            return "-" + m.replaceAll("").trim();
        } else {
            return m.replaceAll("").trim();
        }
    }

    /**
     * 将InputStream转成Bitmap
     * @param stream        二进制流
     * @param dip           屏幕密度density
     * @return              bitmap
     */
    public static Bitmap decodeStream(InputStream stream, float dip) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (dip != 0F) {
            options.inDensity = (int) (160F * dip);
        }
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        setInNativeAlloc(options);
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
            return bitmap;
        } catch (OutOfMemoryError e) {
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            setInNativeAlloc(options);
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
                return bitmap;
            } catch (OutOfMemoryError e2) {
            }
        }
        return null;
    }

    /**
     * 启用native图片内存，优化图片存储，防止OOM
     * @param options       options
     */
    public static void setInNativeAlloc(BitmapFactory.Options options) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
                && !inNativeAllocAccessError) {
            try {
                BitmapFactory.Options.class.getField("inNativeAlloc").setBoolean(options, true);
            } catch (Exception e) {
                inNativeAllocAccessError = true;
            }
        }
    }

    /**
     * 保存图片到本地
     * @param file      本地文件
     * @param bitmap    图片
     * @return          图片本地路径
     */
    public static String saveBitmapToLocal(File file, Bitmap bitmap) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bufferedOutputStream);
            bufferedOutputStream.close();
            LogUtil.d(LOG_TAG, "photo image from data, path:" + file.getAbsolutePath());
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 截取文件路径
     * @param userData      userData
     */
    public static String getFileNameFormUserdata(String userData) {
        if (TextUtils.isEmpty(userData) || "null".equals(userData)) {
            return "";
        }
        return userData.substring(userData.indexOf("fileName=") + "fileName=".length());
    }

    /**
     * 将集合转换成字符创， 用特殊字符做分隔符
     * @param srcList       转换前集合
     * @param separator     分隔符
     * @return              String
     */
    public static String listToString(List<String> srcList, String separator) {
        if (srcList == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < srcList.size(); i++) {
            if (i == srcList.size() - 1) {
                sb.append(srcList.get(i).trim());
            } else {
                sb.append(srcList.get(i).trim() + separator);
            }
        }
        return sb.toString();
    }

    /**
     * 获取资源图片
     * @param context       上下文
     * @param id            图片资源ID
     * @return              Drawable
     */
    public static Drawable getDrawables(Context context, int id) {
        Drawable drawable = getResources(context).getDrawable(id);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

    public static Resources getResources(Context context) {
        return context.getResources();
    }

    private static MessageDigest md = null;
    public static String md5(final String c) {
        if (md == null) {
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        if (md != null) {
            md.update(c.getBytes());
            return byte2hex(md.digest());
        }
        return "";
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int i = 0; i < b.length; i++) {
            stmp = Integer.toHexString(b[i] & 0xff);
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
            if (i < b.length - 1) {
                hs = new StringBuffer(String.valueOf(hs)).toString();
            }
        }
        return hs.toString();
    }

    static MediaPlayer mediaPlayer = null;

    /**
     * 播放通知提示音
     * @param context           上下文
     * @param voicePath         提示音路径
     * @throws IOException
     */
    public static void playNotifycationMusic(Context context, String voicePath) throws IOException {
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd(voicePath);
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.reset();
        mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
        mediaPlayer.prepare();
        mediaPlayer.setLooping(false);
        mediaPlayer.start();
    }

    /**
     * 计算语音文件的时间长度
     *
     * @param file
     * @return
     */
    public static int calculateVoiceTime(String file) {
        File _file = new File(file);
        if (!_file.exists()) {
            return 0;
        }
        // 650个字节就是1s
        int duration = (int) Math.ceil(_file.length() / 650);
        if (duration > 60) {
            return 60;
        }
        if (duration < 1) {
            return 1;
        }
        return duration;
    }

    /**
     * 将字符串转换成整型，如果为空则返回默认值
     *
     * @param str
     *            字符串
     * @param def
     *            默认值
     * @return
     */
    public static int getInt(String str, int def) {
        try {
            if (str == null) {
                return def;
            }
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return def;

    }


}
