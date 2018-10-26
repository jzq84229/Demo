package com.zhang.bdaywidget;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * Created by zjun on 2015/6/1.
 */
public abstract class APrefWidgetModel implements IWidgetModelSaveContract {
    private static String tag = "APrefWidgetModel";

    public int iid;
    public APrefWidgetModel(int instanceId){
        iid = instanceId;
    }

    //abstract methods
    public abstract String getPrefname();
    public abstract void init();
    public Map<String, String> getPrefsToSave(){return null;}

    public void savePreferences(Context context){
        Map<String, String> keyValuePairs = getPrefsToSave();
        if (keyValuePairs == null){
            return;
        }
        SharedPreferences.Editor prefs = context.getSharedPreferences(getPrefname(), 0).edit();
        for (String key : keyValuePairs.keySet()){
            String value = keyValuePairs.get(key);
            savePref(prefs, key, value);
        }

        prefs.commit();
    }

    private void savePref(SharedPreferences.Editor prefs, String key, String value){
        String newkey = getStoredKeyForFieldName(key);
        prefs.putString(newkey, value);
    }

    private void removePref(SharedPreferences.Editor prefs, String key){
        String newkey = getStoredKeyForFieldName(key);
        prefs.remove(newkey);
    }

    protected String getStoredKeyForFieldName(String fieldName){
        return fieldName + "_" + iid;
    }

    public static void clearAllPreferences(Context context, String prefname){
        SharedPreferences prefs = context.getSharedPreferences(prefname, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEdit = prefs.edit();
        prefsEdit.clear();
        prefsEdit.commit();
    }

    public boolean retrievePrefs(Context ctx){
        SharedPreferences prefs = ctx.getSharedPreferences(getPrefname(), Context.MODE_PRIVATE);
        Map<String, ?> keyValuePairs = prefs.getAll();
        boolean prefFound = false;
        for (String key : keyValuePairs.keySet()){
            if (isItMyPref(key) == true){
                String value = (String)keyValuePairs.get(key);
                setValueForPref(key, value);
                prefFound = true;
            }
        }

        return prefFound;
    }

    public void removePrefs(Context context){
        Map<String, String> keyValuePairs = getPrefsToSave();
        if(keyValuePairs == null){
            return;
        }
        SharedPreferences.Editor prefs = context.getSharedPreferences(getPrefname(), 0).edit();
        for (String key : keyValuePairs.keySet()){
            removePref(prefs, key);
        }
        prefs.commit();
    }

    private boolean isItMyPref(String keyname){
        if (keyname.indexOf("_" + iid) > 0){
            return true;
        }
        return false;
    }

    @Override
    public void setValueForPref(String key, String value) {
        return;
    }



}
