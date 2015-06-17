package com.zhang.bdaywidget;

import android.content.Context;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zjun on 2015/6/2.
 */
public class BDayWidgetModel extends APrefWidgetModel {
    private static final String tag = "BDayWidgetModel";
    private static String BDAY_WIDGET_PROVIDER_NAME = "com.zhang.bdaywidget.BDayWidgetProvider";

    private String name = "anon";
    private static String F_NAME = "name";
    private String bday = "1/1/2001";
    private static String F_BDAY = "bday";

    public BDayWidgetModel(int instanceId){
        super(instanceId);
    }

    public BDayWidgetModel(int instanceId, String inName, String inBday){
        super(instanceId);
        this.name = inName;
        this.bday = inBday;
    }

    @Override
    public void init() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBday() {
        return bday;
    }

    public void setBday(String bday) {
        this.bday = bday;
    }

    public long howManyDays(){
        try {
            return Utils.howfarInDays(Utils.getDate(this.bday));
        } catch (ParseException e) {
            return 20000;
        }
    }

    @Override
    public void setValueForPref(String key, String value) {
        if (key.equals(getStoredKeyForFieldName(BDayWidgetModel.F_NAME))){
            this.name = value;
            return;
        }
        if (key.equals(getStoredKeyForFieldName(BDayWidgetModel.F_BDAY))){
            this.bday = value;
            return;
        }
    }

    @Override
    public String getPrefname() {
        return BDayWidgetModel.BDAY_WIDGET_PROVIDER_NAME;
    }

    @Override
    public Map<String, String> getPrefsToSave() {
        Map<String, String> map = new HashMap<>();
        map.put(BDayWidgetModel.F_NAME, this.name);
        map.put(BDayWidgetModel.F_BDAY, this.bday);
        return map;
    }

    @Override
    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append("iid:" + iid);
        sbuf.append("name:" + name);
        sbuf.append("bday:" + bday);
        return sbuf.toString();
    }

    public static void clearAllPreferences(Context ctx){
        APrefWidgetModel.clearAllPreferences(ctx, BDayWidgetModel.BDAY_WIDGET_PROVIDER_NAME);
    }

    public static BDayWidgetModel retrieveModel(Context ctx, int widgetId){
        BDayWidgetModel m = new BDayWidgetModel(widgetId);
        boolean found = m.retrievePrefs(ctx);
        return found ? m : null;
    }
}
