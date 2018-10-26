package com.zhang.imagepicker.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 图片文件夹
 * Created by admin on 2017/7/25.
 */

public class ImageFolder implements Serializable{
    private static final long serialVersionUID = -115387943077973846L;

    public String name;         //文件夹名字
    public String path;         //文件夹路径
    public ImageItem cover;     //文件显示的封面图片
    public ArrayList<ImageItem> images; //文件夹下图片集合

    /**
     * 只要文件夹的路径和名字相同，就认为是相同的文件夹
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ImageFolder) {
            ImageFolder other = (ImageFolder) obj;
            return this.path.equalsIgnoreCase(other.path) && this.name.equalsIgnoreCase(other.name);
        }
        return super.equals(obj);
    }
}
