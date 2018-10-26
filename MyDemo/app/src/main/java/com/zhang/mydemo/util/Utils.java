package com.zhang.mydemo.util;

/**
 * Created by zjun on 2016/1/27 0027.
 */
public class Utils {

    public static int getIntValue(Integer value){
        return value == null ? 0 : value.intValue();
    }

    public static long getLongValue(Long value) {
        return value == null ? 0 : value.longValue();
    }

    public static String getImageFileUrl(String path){
        return "file://" + path;
    }
}
