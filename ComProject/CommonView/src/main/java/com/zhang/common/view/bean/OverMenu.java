package com.zhang.common.view.bean;

/**
 * 悬浮菜单Item对象
 * Created by Administrator on 2017/11/9 0009.
 */
public class OverMenu {
    private int imgRid;     //图片ID
    private String name;    //名称
    public OverMenu(int imgRid, String name){
        this.imgRid = imgRid;
        this.name = name;
    }
    public int getImgRid() {
        return imgRid;
    }

    public void setImgRid(int imgRid) {
        this.imgRid = imgRid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
