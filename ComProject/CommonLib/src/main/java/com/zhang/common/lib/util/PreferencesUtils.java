package com.zhang.common.lib.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


/**
 * PreferencesUtils, easy to get or put data
 * <ul>
 * <strong>Preference Name</strong>
 * <li>you can change preference name by {@link #PREFERENCE_NAME}</li>
 * </ul>
 * <ul>
 * <strong>Put Value</strong>
 * <li>put string {@link #putString(Context, String, String, String)}</li>
 * <li>put int {@link #putInt(Context, String, String, int)}</li>
 * <li>put long {@link #putLong(Context, String, String, long)}</li>
 * <li>put float {@link #putFloat(Context, String, String, float)}</li>
 * <li>put boolean {@link #putBoolean(Context, String, String, boolean)}</li>
 * </ul>
 * <ul>
 * <strong>Get Value</strong>
 * <li>get string {@link #getString(Context, String, String)}, {@link #getString(Context, String, String)}</li>
 * <li>get int {@link #getInt(Context, String, String)}, {@link #getInt(Context, String, String, int)}</li>
 * <li>get long {@link #getLong(Context, String, String)}, {@link #getLong(Context, String, String, long)}</li>
 * <li>get float {@link #getFloat(Context, String, String)}, {@link #getFloat(Context, String, String, float)}</li>
 * <li>get boolean {@link #getBoolean(Context, String, String)}, {@link #getBoolean(Context, String, String, boolean)}</li>
 * </ul>
 *
 */

public class PreferencesUtils {
    public static final String PREFERENCE_NAME = "preference";

    public static void putString(Context context, String spName, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Context context, String spName, String key) {
        return getString(context, spName, key, null);
    }

    public static String getString(Context context, String spName, String key, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }

    public static void putInt(Context context, String spName, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getInt(Context context, String spName, String key) {
        return getInt(context, spName, key, -1);
    }

    public static int getInt(Context context, String spName, String key, int defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultValue);
    }

    public static void putLong(Context context, String spName, String key, long value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static long getLong(Context context, String spName, String key) {
        return getLong(context, spName, key, -1);
    }

    public static long getLong(Context context, String spName, String key, long defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, defaultValue);
    }

    public static void putFloat(Context context, String spName, String key, float value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static float getFloat(Context context, String spName, String key) {
        return getFloat(context, spName, key, -1);
    }

    public static float getFloat(Context context, String spName, String key, float defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public static void putBoolean(Context context, String spName, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String spName, String key) {
        return getBoolean(context, spName, key, false);
    }

    public static boolean getBoolean(Context context, String spName, String key, boolean defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public static void clear(Context context, String spName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
