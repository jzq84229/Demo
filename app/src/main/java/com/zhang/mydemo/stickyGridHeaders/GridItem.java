package com.zhang.mydemo.stickyGridHeaders;

/**
 * Created by zjun on 2015/6/17.
 */
public class GridItem {
    /**
     * 图片的路径
     */
    private String path;
    /**
     * 图片加入手机中的时间，只取了年月日
     */
    private String time;
    /**
     * 每个Item对应的HeaderId
     */
    private int headerId;
    public GridItem(String path, String time){
        this.path = path;
        this.time = time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getHeaderId() {
        return headerId;
    }

    public void setHeaderId(int headerId) {
        this.headerId = headerId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
