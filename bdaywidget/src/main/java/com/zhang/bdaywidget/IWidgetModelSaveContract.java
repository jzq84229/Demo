package com.zhang.bdaywidget;

import java.util.Map;

/**
 * Created by zjun on 2015/6/1.
 */
public interface IWidgetModelSaveContract {
    public String getPrefname();

    public void setValueForPref(String key, String value);

    public Map<String, String> getPrefsToSave();

    public void init();
}
