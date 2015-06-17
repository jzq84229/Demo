package com.zhang.bdaywidget;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zjun on 2015/6/2.
 */
public class Utils {
    private static String tag = "Utils";

    public static Date getDate(String dateString) throws ParseException {
        DateFormat a = getDateFormat();
        Date date = a.parse(dateString);
        return date;
    }

    public static DateFormat getDateFormat(){
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
//        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        df.setLenient(false);
        return df;
    }

    public static boolean validateDate(String dateString){
        try {
            SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            df.setLenient(false);
            Date date = df.parse(dateString);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static long howfarInDays(Date date){
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        long today_ms = today.getTime();
        long target_ms = date.getTime();
        return (target_ms - today_ms) / (1000 * 60 * 60 * 24);
    }

}
