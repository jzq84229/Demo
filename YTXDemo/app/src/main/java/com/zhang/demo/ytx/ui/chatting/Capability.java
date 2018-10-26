package com.zhang.demo.ytx.ui.chatting;

/**
 * 插件Entity
 * Created by Administrator on 2016/7/22.
 */
public class Capability {
    private int id;                     //插件ID
    private String capabilityName;      //插件名称
    private int icon;                   //插件图标

    public Capability() {}

    public Capability(String capabilityName, int icon) {
        this.capabilityName = capabilityName;
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCapabilityName() {
        return capabilityName;
    }

    public void setCapabilityName(String capabilityName) {
        this.capabilityName = capabilityName;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
