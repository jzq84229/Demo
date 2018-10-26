package com.zhang.common.lib.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 */

public class MD5Utils {

    /**
     * 获取字符串的文件值
     * @param content
     * @return
     */
    public static String md5(String content) {
        byte[] array;
        try {
            array = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }
        return toHexString(array);
    }

    /**
     * 获取文件的MD5值
     * @param file
     * @return
     */
    public static String md5(File file) {
        int buffersize = 1024;
        FileInputStream fis = null;
        DigestInputStream dis = null;

        try {
            //创建MD5转换器和文件流
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(file);
            dis = new DigestInputStream(fis, messageDigest);

            byte[] buffer = new byte[buffersize];
            //DigestInputStream实际上在流处理文件时就在内部就进行了一定的处理
            while (dis.read(buffer) > 0) ;

            //通过DigestInputStream对象得到一个最终的MessageDigest对象。
            messageDigest = dis.getMessageDigest();

            // 通过messageDigest拿到结果，也是字节数组，包含16个元素
            byte[] array = messageDigest.digest();
            // 同样，把字节数组转换成字符串
            return toHexString(array);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取byte数组的MD5值
     * @param arr
     * @return
     */
    public static String md5(byte[] arr) {
        if (arr == null) {
            return "";
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(arr);
            byte[] array = digest.digest();
            // 同样，把字节数组转换成字符串
            return toHexString(array);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String toHexString(byte[] arr) {
        StringBuilder hex = new StringBuilder(arr.length * 2);
        for (byte b : arr) {
            if ((b & 0xFF) < 0x10) {
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

}
