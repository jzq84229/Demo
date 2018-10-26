/*
 *  Copyright (c) 2013 The CCP project authors. All Rights Reserved.
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

import android.content.Context;

import com.zhang.common.lib.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 时间转换工具类
 *
 */
public class DateUtils {

    public static final TimeZone tz = TimeZone.getTimeZone("GMT+8:00");
    public static final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy-MM-dd");

    private static final long ONEDAY = 86400000;
    public static final int SHOW_TYPE_SIMPLE = 0;
    public static final int SHOW_TYPE_COMPLEX = 1;
    public static final int SHOW_TYPE_ALL = 2;
    public static final int SHOW_TYPE_CALL_LOG = 3;
    public static final int SHOW_TYPE_CALL_DETAIL = 4;

    /**
     * 获取当前当天日期0点 long值
     *
     * @return
     */
    public static long getCurrentDayTime() {
        Date d = new Date(System.currentTimeMillis());
        String formatDate = yearFormat.format(d);
        try {
            return (yearFormat.parse(formatDate)).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 格式化日期(yyyy-MM-dd)
     * @param year      年
     * @param month     月
     * @param day       日
     * @return
     */
    public static String formatDate(int year, int month, int day) {
        Date d = new Date(year - 1900, month, day);
        return yearFormat.format(d);
    }

    /**
     * string转Date (yyyy-MM-dd)
     * @param str
     * @return
     */
    public static Date parseStrToDate(String str) {
        try {
            return yearFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * String转为Date
     * @param date
     * @param format
     * @return
     */
    public static Date parseStrToDate(String date, String format) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Date转String
     * @param date
     * @return
     */
    public static String parseDateToStrShort(Date date) {
        return yearFormat.format(date);
    }

    /**
     * Date转String
     * @param date
     * @return
     */
    public static String parseDateToStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }


    /**
     * Date转String
     * @param date
     * @param format
     * @return
     */
    public static String parseDateToStr(Date date, String format) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取日期long值
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static long getDateMills(int year, int month, int day) {
        //Date d = new Date(year, month, day);
        // 1960 4 22
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.set(year, month, day);
        TimeZone tz = TimeZone.getDefault();
        calendar.setTimeZone(tz);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取当前时间Str(yyyy-MM-dd)
     * @return
     */
    public static String getCurrentDateShort() {
        return yearFormat.format(new Date());
    }

    /**
     * 获取当前时间Str(yyyy-MM-dd HH:mm:ss)
     * @return
     */
    public static String getCurrentDate() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sDateFormat.format(new Date());
        return date;
    }

    /**
     * 获取当前时间Str
     * @param format
     * @return
     */
    public static String getCurrentDateStr(String format) {
        return parseDateToStr(new Date(), format);
    }

    /**
     * String转为long(yyyy-MM-dd)
     * @param date
     * @return
     */
    public static long parseStrToLong(String date) {
        try {
            Date parse = yearFormat.parse(date);
            return parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    /**
     * String转为long值
     * @param dateStr
     * @param format
     * @return
     */
    public static long parseStrToLong(String dateStr, String format) {
        long time = 0L;
        try {
            Date date = parseStrToDate(dateStr, format);
            time = date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String getBeforeOneMonthDate() {
        Date beforeoneMonth = new Date();
        beforeoneMonth.setMonth(beforeoneMonth.getMonth() - 1);
        return yearFormat.format(beforeoneMonth);
    }

//    public static String shortStringDate(String dateString, String format) {
//        String result;
//        Date date = null;
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
//            date = sdf.parse(dateString);
//        } catch (ParseException e) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            try {
//                date = sdf.parse(dateString);
//            } catch (ParseException e1) {
//                e1.printStackTrace();
//            }
//        }
//        result = new SimpleDateFormat(format).format(date);
//        return result;
//    }

    /**
     * 将生日存储的时间格式转化为年龄（周岁，小数点后不计）
     *
     * @param dateStr 生日字段  "yyyymmdd"
     * @return 年龄
     */
    public static String birthdayToAge(String dateStr) {
        if (dateStr == null || dateStr.length() < 6) {
            return "";
        } else {
            int birthYear = Integer.parseInt(dateStr.substring(0, 4));
            int birthMonth = Integer.parseInt(dateStr.substring(4, 6));
            Calendar cal = new GregorianCalendar();
            int currYear = cal.get(Calendar.YEAR);
            int currMonth = cal.get(Calendar.MONTH);
            int age = currYear - birthYear;
            age -= (currMonth < birthMonth) ? 1 : 0;
            return age + "";
        }
    }

    /**
     * 功能描述:
     * 将年龄转换为生日
     *
     * @param age int
     * @return String
     */
    public static String ageToBirthday(int age) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, 0 - age);
        return yearFormat.format(c.getTime());
    }

//    public static long fromDateStringToLong(String inVal) { //此方法计算时间毫秒
//        Date date = null;   //定义时间类型
//        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
//        try {
//            date = inputFormat.parse(inVal); //将字符型转换成日期型
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return date.getTime();   //返回毫秒数
//    }


    /**
     * 获取当前星期
     * @return
     */
    public static int getCurrentWeekDay(){
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取星期
     * @param date
     * @return
     */
    public static String getWeekDayName(Date date){
        if(date.getDay() == 0){
            return "星期日";
        }
        if(date.getDay() == 1){
            return "星期一";
        }
        if(date.getDay() == 2){
            return "星期二";
        }
        if(date.getDay() == 3){
            return "星期三";
        }
        if(date.getDay() == 4){
            return "星期四";
        }
        if(date.getDay() == 5){
            return "星期五";
        }
        if(date.getDay() == 6){
            return "星期六";
        }
        return "";
    }


    public static double toBCD(double value){
        return ((int)(value / 10)*16) + value % 10;
    }

//    /**
//     * 将16位的short转换成byte数组
//     *
//     * @param s
//     *            short
//     * @return byte[] 长度为2
//     * */
//    public static byte[] shortToByteArray(short s) {
//        byte[] targets = new byte[2];
//        for (int i = 0; i < 2; i++) {
//            int offset = (targets.length - 1 - i) * 8;
//            targets[i] = (byte) ((s >>> offset) & 0xff);
//        }
//        return targets;
//    }
//
//    public static long getDateTime(String dateString) {
//        if (TextUtils.isEmpty(dateString)) {
//            return 0;
//        }
//        Date date = null;
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
//            date = sdf.parse(dateString);
//            return date.getTime();
//        } catch (ParseException e) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            try {
//                date = sdf.parse(dateString);
//                return date.getTime();
//            } catch (ParseException e1) {
//                e1.printStackTrace();
//            }
//        }
//        return 0;
//    }

    /**
     * 腾讯云时间转化为显示字符串
     *
     * @param timeStamp 单位为秒
     */
    public static String getTimeStr(long timeStamp) {
        if (timeStamp == 0) return "";
        Calendar inputTime = Calendar.getInstance();
        inputTime.setTimeInMillis(timeStamp * 1000);
        Date currenTimeZone = inputTime.getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        if (calendar.before(inputTime)) {
            //今天23:59在输入时间之前，解决一些时间误差，把当天时间显示到这里
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + "/" + "MM" + "/" + "dd");
            return sdf.format(currenTimeZone);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(currenTimeZone);
        }
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM" + "/" + "dd");
            return sdf.format(currenTimeZone);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + "/" + "MM" + "/" + "dd");
            return sdf.format(currenTimeZone);

        }
    }

    /**
     * 时间转化为聊天界面显示字符串
     *
     * @param timeStamp 单位为秒
     */
    public static String getChatTimeStr(Context context, long timeStamp) {
        if (timeStamp == 0) return "";
        Calendar inputTime = Calendar.getInstance();
        inputTime.setTimeInMillis(timeStamp * 1000);
        Date currenTimeZone = inputTime.getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
//        if (!calendar.after(inputTime)) {
//            //当前时间在输入时间之前
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + "/" + "MM" + "/" + "dd");
//            return sdf.format(currenTimeZone);
//        }

        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(currenTimeZone);
        }

        calendar.add(Calendar.DAY_OF_MONTH, -1);
        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return context.getString(R.string.time_yesterday) + " " + sdf.format(currenTimeZone);
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            if (calendar.before(inputTime)) {
                SimpleDateFormat sdf = new SimpleDateFormat("M" + "/" + "d" + " " + " HH:mm");
                return sdf.format(currenTimeZone);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + "/" + "MM" + "/" + "dd" + " " + " HH:mm");
                return sdf.format(currenTimeZone);
            }
        }
    }

    /**
     * 时间转化为聊天界面显示字符串
     *
     * @param timeStamp 单位为秒
     */
    public static String getChatTimeStr(Context context, long timeStamp,boolean isreall) {
        if (timeStamp == 0) return "";
        Calendar inputTime = Calendar.getInstance();
        inputTime.setTimeInMillis(timeStamp * 1000);
        Date currenTimeZone = inputTime.getTime();
        Calendar calendar = Calendar.getInstance();
        if (!calendar.after(inputTime)) {
            //当前时间在输入时间之前
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + "-" + "MM" + "-" + "dd");
            return sdf.format(currenTimeZone);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if(isreall){
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + "-" + "MM" + "-" + "dd" + " " + " HH:mm");
            return sdf.format(currenTimeZone);
        }
        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(currenTimeZone);
        }
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        if (calendar.before(inputTime)) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return context.getString(R.string.time_yesterday) + " " + sdf.format(currenTimeZone);
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.MONTH, Calendar.JANUARY);
            if (calendar.before(inputTime)) {
                SimpleDateFormat sdf = new SimpleDateFormat("M" + "-" + "d" + " " + " HH:mm");
                return sdf.format(currenTimeZone);
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy" + "-" + "MM" + "-" + "dd" + " " + " HH:mm");
                return sdf.format(currenTimeZone);
            }
        }
    }
}
