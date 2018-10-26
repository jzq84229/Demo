package com.zhang.demo.ytx.common.utils;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * 读取头像合成所需要的坐标体系
 * Created by Administrator on 2016/7/15.
 */
public class ECPropertiesUtil {
    /**
     * 根据Key 读取Value
     * @param key
     * @return
     */
    public static String readData(Context mContext, String key, int resId) {
        Properties props = new Properties();
        try {
            InputStream in = new BufferedInputStream(mContext.getResources().openRawResource(resId));
            props.load(in);
            in.close();
            String value = props.getProperty(key);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
