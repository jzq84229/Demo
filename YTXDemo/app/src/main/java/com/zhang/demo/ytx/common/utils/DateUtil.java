package com.zhang.demo.ytx.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 事件转换工具
 * Created by Administrator on 2016/7/15.
 */
public class DateUtil {
    public static final TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
    public static final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

    private static final long ONEDAY = 86400000;
    public static final int SHOW_TYPE_SIMPLE = 0;
    public static final int SHOW_TYPE_COMPLEX = 1;
    public static final int SHOW_TYPE_ALL = 2;
    public static final int SHOW_TYPE_CALL_LOG = 3;
    public static final int SHOW_TYPE_CALL_DETAIL = 4;

    /**
     * 获取当前当天日期的毫秒数 2012-03-21的毫秒数
     */
    public static long getCurrentDayTime() {
        Date d = new Date(System.currentTimeMillis());
        String formateDate = yearFormat.format(d);
        try {
            return yearFormat.parse(formateDate).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 将时间转换成指定的格式
     * @param time      转换时间
     * @param type      要转换的类型
     * @return          String
     */
    public static String getDateString(long time, int type) {
        Calendar c = Calendar.getInstance();
        c = Calendar.getInstance(tz);
        c.setTimeInMillis(time);
        long currentTime = System.currentTimeMillis();
        Calendar current_c = Calendar.getInstance();
        current_c = Calendar.getInstance(tz);
        current_c.setTimeInMillis(currentTime);

        int currentYear = current_c.get(Calendar.YEAR);
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH) + 1;
        int d = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        long t = currentTime - time;
        long t2 = currentTime - getCurrentDayTime();
        String dateStr = "";
        if (t < t2 && t > 0) {
            if (type == SHOW_TYPE_SIMPLE) {
                dateStr = (hour < 10 ? "0" + hour : hour) + ":"
                        + (minute < 10 ? "0" + minute : minute);
            } else if (type == SHOW_TYPE_COMPLEX) {
                dateStr = "今天  " + (hour < 10 ? "0" + hour : hour) + ":"
                        + (minute < 10 ? "0" + minute : minute);
            } else if (type == SHOW_TYPE_CALL_LOG) {
                dateStr = "今天  " + (hour < 10 ? "0" + hour : hour) + ":"
                        + (minute < 10 ? "0" + minute : minute);
            } else if (type == SHOW_TYPE_CALL_DETAIL) {
                dateStr = "今天  ";
            }else {
                dateStr = (hour < 10 ? "0" + hour : hour) + ":"
                        + (minute < 10 ? "0" + minute : minute) + ":"
                        + (second < 10 ? "0" + second : second);
            }
        } else if (t < (t2 + ONEDAY) && t > 0) {
            if (type == SHOW_TYPE_SIMPLE || type == SHOW_TYPE_CALL_DETAIL) {
                dateStr = "昨天  ";
            } else if (type == SHOW_TYPE_COMPLEX ) {
                dateStr = "昨天  " + (hour < 10 ? "0" + hour : hour) + ":"
                        + (minute < 10 ? "0" + minute : minute);
            } else if (type == SHOW_TYPE_CALL_LOG) {
                dateStr = "昨天  " + (hour < 10 ? "0" + hour : hour) + ":"
                        + (minute < 10 ? "0" + minute : minute);
            } else {
                dateStr = "昨天  " + (hour < 10 ? "0" + hour : hour) + ":"
                        + (minute < 10 ? "0" + minute : minute) + ":"
                        + (second < 10 ? "0" + second : second);
            }
        } else if (y == currentYear) {
            if (type == SHOW_TYPE_SIMPLE) {
                dateStr = (m < 10 ? "0" + m : m) + "/" + (d < 10 ? "0" + d : d);
            } else if (type == SHOW_TYPE_COMPLEX) {
                dateStr = (m < 10 ? "0" + m : m) + "月" + (d < 10 ? "0" + d : d)
                        + "日";
            } else if (type == SHOW_TYPE_CALL_LOG || type == SHOW_TYPE_COMPLEX) {
                dateStr = (m < 10 ? "0" + m : m) + /* 月 */"/"
                        + (d < 10 ? "0" + d : d) + /* 日 */" "
                        + (hour < 10 ? "0" + hour : hour) + ":"
                        + (minute < 10 ? "0" + minute : minute);
            } else if (type == SHOW_TYPE_CALL_DETAIL) {
                dateStr = y + "/" + (m < 10 ? "0" + m : m) + "/"
                        + (d < 10 ? "0" + d : d);
            } else {
                dateStr = (m < 10 ? "0" + m : m) + "月" + (d < 10 ? "0" + d : d)
                        + "日 " + (hour < 10 ? "0" + hour : hour) + ":"
                        + (minute < 10 ? "0" + minute : minute) + ":"
                        + (second < 10 ? "0" + second : second);
            }
        } else {
            if (type == SHOW_TYPE_SIMPLE) {
                dateStr = y + "/" + (m < 10 ? "0" + m : m) + "/"
                        + (d < 10 ? "0" + d : d);
            } else if (type == SHOW_TYPE_COMPLEX ) {
                dateStr = y + "年" + (m < 10 ? "0" + m : m) + "月"
                        + (d < 10 ? "0" + d : d) + "日";
            } else if (type == SHOW_TYPE_CALL_LOG || type == SHOW_TYPE_COMPLEX) {
                dateStr = y + /* 年 */"/" + (m < 10 ? "0" + m : m) + /* 月 */"/"
                        + (d < 10 ? "0" + d : d) + /* 日 */"  "
                /*
                 * + (hour < 10 ? "0" + hour : hour) + ":"
                 * + (minute < 10 ? "0" + minute : minute)
                 */;
            } else if (type == SHOW_TYPE_CALL_DETAIL) {
                dateStr = y + "/" + (m < 10 ? "0" + m : m) + "/"
                        + (d < 10 ? "0" + d : d);
            } else {
                dateStr = y + "年" + (m < 10 ? "0" + m : m) + "月"
                        + (d < 10 ? "0" + d : d) + "日 "
                        + (hour < 10 ? "0" + hour : hour) + ":"
                        + (minute < 10 ? "0" + minute : minute) + ":"
                        + (second < 10 ? "0" + second : second);
            }
        }
        return dateStr;
    }
}
