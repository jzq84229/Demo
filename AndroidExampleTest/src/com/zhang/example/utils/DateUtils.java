package com.zhang.example.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtils {
	
    /** 
     * 格式化日期 
     *  
     * @param date 
     * @param pattern 
     * @return 
     */  
    public static String parseDateToString (Date date, String pattern) {  
        SimpleDateFormat format = new SimpleDateFormat(pattern);  
        return format.format(date);  
    }  
  
    /** 
     * 根据给定日期字符串和日期格式 创建日期 
     *  
     * @param dateString 
     * @param pattern 
     * @return 
     * @throws ParseException 
     */  
    public static Date parseStringToDate (String dateString, String pattern) throws ParseException {  
        SimpleDateFormat format = new SimpleDateFormat(pattern);  
        return format.parse(dateString);  
    }  
	
    /** 
     * 计算两个日期之间的差距天数 
     *  
     * @param a 
     * @param b 
     * @return 
     */  
    public static int cutTwoDateToDay(Date a, Date b) {  
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
        Calendar calendar = Calendar.getInstance();  
        int theday = 0;  
        try {  
            Date beginDate = format.parse(format.format(a));  
            Date endDate = format.parse(format.format(b));  
  
            calendar.setTime(beginDate);  
            long begin = calendar.getTimeInMillis();  
            calendar.setTime(endDate);  
            long end = calendar.getTimeInMillis();  
  
            theday = (int) ((end - begin) / (86400000));  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        return theday;  
    }  
  
    /** 
     * 取出两个时间出较大的时间 
     *  
     * @param a 
     * @param b 
     * @return 
     */  
    public static Date MaxDate(Date a, Date b) {  
        if (a.before(b)) {  
            return b;  
        } else {  
            return a;  
        }  
    }  
  
    /** 
     * 取出两个时间出较小的时间 
     *  
     * @param a 
     * @param b 
     * @return 
     */  
    public static Date MinDate(Date a, Date b) {  
        if (a.before(b)) {  
            return a;  
        } else {  
            return b;  
        }  
    }  
  
    /** 
     * 计算给定日期是星期几 
     *  
     * @param date 
     * @return 
     */  
    public static int getWeekOfDate(Date date) {  
        java.util.Calendar cal = java.util.Calendar.getInstance();  
        cal.setTime(date);  
        int w = cal.get(java.util.Calendar.DAY_OF_WEEK) - 1;  
        if (w == 0)  
            w = 7;  
        return w;  
    }  
  
}
