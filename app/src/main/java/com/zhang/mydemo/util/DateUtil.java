package com.zhang.mydemo.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


@SuppressLint("SimpleDateFormat")
public class DateUtil {
    @SuppressLint("SimpleDateFormat")
	private static final DateFormat dfForStr = new SimpleDateFormat("yyyy-MM-dd");
    
    public static Date convertSToDate(String dateStr){
		Date date = null;
		if(dateStr != null && !dateStr.isEmpty()){
			try {
				date = dfForStr.parse(dateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;
	}
    
    public static String convertDToString(Date date){
		if(date != null){
			return dfForStr.format(date);
		}
		return "";
	}
    
    public static String convertSecondsToString(String milliseconds){
    	return convertDToString(convertSecondsToDate(milliseconds));
    }
    
    public static Date convertSecondsToDate(String milliseconds){
    	Date date = null;
    	if (!TextUtils.isEmpty(milliseconds)) {
			date = new Date(Long.parseLong(milliseconds));
		}
    	return date;
    }
    
    /**
     * 将long日期专程指定格式
     * @param milliseconds
     * @param pattern
     * @return
     */
    public static String convertSecondsToPatternStr(String milliseconds, String pattern){
    	return parseDateToString(convertSecondsToDate(milliseconds), pattern);
    }
	
    /** 
     * 格式化日期 
     *  
     * @param date 
     * @param pattern 
     * @return 
     */  
    public static String parseDateToString (Date date, String pattern) {
    	if (date != null && !TextUtils.isEmpty(pattern)) {
    		SimpleDateFormat format = new SimpleDateFormat(pattern);
    		return format.format(date);  
		}
    	return "";
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
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w == 0)  
            w = 7;  
        return w;  
    }  
    
    public static String convertToDate(Date date) {
    	String dateString = "";
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	int month = calendar.get(Calendar.MONTH) + 1;
    	dateString += month+"月";
    	int day = calendar.get(Calendar.DAY_OF_MONTH);
    	dateString += day+"日 ";
    	int hour = calendar.get(Calendar.HOUR_OF_DAY);
    	if (hour > 12) {
        	dateString += "下午" + (hour - 12);
		} else {
        	dateString += "上午" + hour;
		}
    	dateString += String.format(":%tM", date);
    	
    	return dateString;
    }
  
}
