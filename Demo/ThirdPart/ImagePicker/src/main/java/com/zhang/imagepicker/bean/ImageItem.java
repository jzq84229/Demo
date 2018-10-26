package com.zhang.imagepicker.bean;

import java.io.Serializable;

/**
 * 图片信息
 * Created by admin on 2017/7/25.
 */

public class ImageItem implements Serializable {
    private static final long serialVersionUID = -7003642051729487787L;

    public String name;         //图片名字
    public String path;         //图片路径
    public long size;           //图片大小
    public int width;           //图片宽度
    public int height;          //图片高度
    public String mimeType;     //图片类型
    public long addTime;        //图片创建时间

    /**
     * 图片路径和创建时间相同就认为是同一张图片
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ImageItem) {
            ImageItem item = (ImageItem) obj;
            return this.path.equalsIgnoreCase(item.path) && this.addTime == item.addTime;
        }
        return super.equals(obj);
    }
}
