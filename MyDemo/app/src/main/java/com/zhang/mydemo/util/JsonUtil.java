package com.zhang.mydemo.util;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class JsonUtil {
	
	public static String getString(JSONObject jo, String key){
		try {
			String value = jo.getString(key);
			return value;
		} catch (JSONException e) {
			return "";
		}
	}
	
	public static int getInt(JSONObject jo, String key){
		try {
			int value = jo.getInt(key);
			return value;
		} catch (JSONException e) {
			return 0;
		}

	}
	
	public static int getInt(JSONObject jo, String key, int defaltValue){
		try {
			int value = jo.getInt(key);
			return value;
		} catch (JSONException e) {
			return defaltValue;
		}
	}
	
	public static float getFloat(JSONObject jo, String key){
		try {
			String value = jo.getString(key);
			if(value!=null&&!("".equals(value))){
				return Float.parseFloat(value);
			}else{
				return 0;
			}
		} catch (JSONException e) {
			return 0;
		}
	}
	
	public static double getDouble(JSONObject jo, String key){
		try {
			String value = jo.getString(key);
			if (!TextUtils.isEmpty(value)) {
				return Double.parseDouble(value);
			} else {
				return 0;
			}
		} catch (JSONException e) {
			return 0;
		}
	}
	
	public static long getLong(JSONObject jo, String key){
		try {
			long value = jo.getLong(key);
			return value;
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static Long getLongObject(JSONObject jo, String key){
		try {
			Long value = jo.getLong(key);
			return value;
		} catch (Exception e) {
			return null;
		}
	}
	


	public static Date getDate(JSONObject jo, String key) {
		try {
			String dateStr = JsonUtil.getString(jo, key);
			return DateUtil.convertSecondsToDate(dateStr);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static boolean getBoolean(JSONObject jo, String key) {
		try {
			return jo.getBoolean(key);
		} catch (Exception e) {
			return false;
		}
	}

	public static JSONArray getArray(JSONObject obj, String key) {
		try {
			if (obj != null) {
				return obj.getJSONArray(key);
			} else {
				return null;
			}
		} catch (JSONException e) {
			return null;
		}
	}


}
