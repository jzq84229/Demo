package com.zhang.common.lib.util;

import android.text.TextUtils;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

/**
 * Byte工具类
 */

public class ByteUtils {
    public static byte[] getBytes(short data) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        return bytes;
    }

    public static byte[] getBytes(char data) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (data);
        bytes[1] = (byte) (data >> 8);
        return bytes;
    }

    public static byte[] getBytes(int data) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        bytes[2] = (byte) ((data & 0xff0000) >> 16);
        bytes[3] = (byte) ((data & 0xff000000) >> 24);
        return bytes;
    }

    public static byte[] getBytes(long data) {
        byte[] bytes = new byte[8];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data >> 8) & 0xff);
        bytes[2] = (byte) ((data >> 16) & 0xff);
        bytes[3] = (byte) ((data >> 24) & 0xff);
        bytes[4] = (byte) ((data >> 32) & 0xff);
        bytes[5] = (byte) ((data >> 40) & 0xff);
        bytes[6] = (byte) ((data >> 48) & 0xff);
        bytes[7] = (byte) ((data >> 56) & 0xff);
        return bytes;
    }

    public static byte[] getBytes(float data) {
        int intBits = Float.floatToIntBits(data);
        return getBytes(intBits);
    }

    public static byte[] getBytes(double data) {
        long intBits = Double.doubleToLongBits(data);
        return getBytes(intBits);
    }

    public static byte[] getBytes(String data, String charsetName) {
        Charset charset = Charset.forName(charsetName);
        return data.getBytes(charset);
    }

    public static byte[] getBytes(String data) {
        return getBytes(data, "GBK");
    }


    public static short getShort(byte[] bytes) {
        return (short) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
    }

    public static char getChar(byte[] bytes) {
        return (char) ((0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)));
    }

    public static int getInt(byte[] bytes) {
        return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)) | (0xff0000 & (bytes[2] << 16)) | (0xff000000 & (bytes[3] << 24));
    }

    public static long getLong(byte[] bytes) {
        return (0xffL & (long) bytes[0]) | (0xff00L & ((long) bytes[1] << 8)) | (0xff0000L & ((long) bytes[2] << 16)) | (0xff000000L & ((long) bytes[3] << 24))
                | (0xff00000000L & ((long) bytes[4] << 32)) | (0xff0000000000L & ((long) bytes[5] << 40)) | (0xff000000000000L & ((long) bytes[6] << 48)) | (0xff00000000000000L & ((long) bytes[7] << 56));
    }

    public static float getFloat(byte[] bytes) {
        return Float.intBitsToFloat(getInt(bytes));
    }

    public static double getDouble(byte[] bytes) {
        long l = getLong(bytes);
        System.out.println(l);
        return Double.longBitsToDouble(l);
    }

    public static String getString(byte[] bytes, String charsetName) {
        return new String(bytes, Charset.forName(charsetName));
    }

    public static String getString(byte[] bytes) {
        return getString(bytes, "GBK");
    }

    /**
     * 将字符串转成固定长度byte数组
     * @param str
     * @param len
     * @return
     */
    public static byte[] getStrBytes(String str, int len){
        byte[] array = new byte[len];
        if (!TextUtils.isEmpty(str)) {
            str.getBytes();
            byte[] strArray = getBytes(str);
            System.arraycopy(strArray, 0, array, 0, strArray.length >= len ? len : strArray.length);
        }
        return array;
    }

    /**
     * 将byte数组转换为int数据
     *
     * @param b 字节数组
     * @return 生成的int数据
     */
    public static int parseBytesToInt(byte[] b) {
        int ret = 0;
        int len = b.length > 4 ? 4 : b.length;
        for (int i = len - 1; i >= 0; i--) {
            byte data = b[i];
            ret <<= 8;
            ret |= (data & 0xFF);
        }
        return ret;
    }

    /**
     * 将int转成固定长度byte[]
     *
     * @param val
     * @param arrLen        返回数据长度
     *
     */
    public static byte[] parseIntToBytes(int val, int arrLen) {
        if (arrLen > 4) {
            arrLen = 4;
        }
        byte[] bytes = new byte[arrLen];
        for (int i = 0; i < arrLen; i++) {
            bytes[i] = (byte) (val >> i * 8 & 0xff);
        }
        return bytes;
    }

    /**
     * 将short转成数组
     * @param val
     * @return
     */
    public static byte[] parseShortToBytes(short val) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (val & 0xff);
        bytes[1] = (byte) (val >> 8 & 0xff);
        return bytes;
    }

    /**
     * 将字符串转成固定长度byte[]
     *
     * @param str           字符串
     * @param arrLen        返回数组长度
     */
    public static byte[] parseStringToBytes(String str, int arrLen) {
        byte[] bytes = new byte[arrLen];
        if (!TextUtils.isEmpty(str)) {
            byte[] strArr = getBytes(str);
            int len = arrLen > strArr.length ? strArr.length : arrLen;
            for (int i = 0; i < len; i++) {
                bytes[i] = strArr[i];
            }
        }
        return bytes;
    }

    /**
     * 两个byte转带符号int
     * @param b1    低位
     * @param b2    高位
     * @return
     */
    public static int convertTwoBytesToSignedInt(byte b1, byte b2) {     // signed
        return (b2 << 8) | (b1 & 0xFF);
    }

    /**
     * 两个byte转无符号int
     * @param b1
     * @param b2
     * @return
     */
    public static int convertTwoBytesToUnsignedInt(byte b1, byte b2) {
        return (b2 & 0xFF) << 8 | (b1 & 0xFF);
    }


    /**
     * 合并byte数组
     * @param first
     * @param rest
     * @return
     */
    public static byte[] concatAll(byte[] first, byte[]... rest) {
        int totalLength = first.length;
        for (byte[] bytes : rest) {
            totalLength += bytes.length;
        }
        byte[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (byte[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    public static byte[] concatAll(List<byte[]> list) {
        int totalLength = 0;
        for (byte[] bytes : list) {
            totalLength += bytes.length;
        }
        byte[] result = new byte[totalLength];
        int offset = 0;
        for (byte[] array : list) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }


//    public static void main(String[] args) {
//        short s = 122;
//        int i = 122;
//        long l = 1222222;
//
//        char c = 'a';
//
//        float f = 122.22f;
//        double d = 122.22;
//
//        String string = "我是好孩子";
//        System.out.println(s);
//        System.out.println(i);
//        System.out.println(l);
//        System.out.println(c);
//        System.out.println(f);
//        System.out.println(d);
//        System.out.println(string);
//
//        System.out.println("**************");
//
//        System.out.println(getShort(getBytes(s)));
//        System.out.println(getInt(getBytes(i)));
//        System.out.println(getLong(getBytes(l)));
//        System.out.println(getChar(getBytes(c)));
//        System.out.println(getFloat(getBytes(f)));
//        System.out.println(getDouble(getBytes(d)));
//        System.out.println(getString(getBytes(string)));
//    }
}
