package com.zhang.mydemo.chart;

import java.util.List;

/**
 * Created by zjun on 2015/9/11 0011.
 */
public class ChartInfo {
    private String title;
    private int yScaleNum;
    private String[] yScaleLeftLable;
    private String[] yScaleRightLable;
    private int xScaleNum;
    private String[] xScaleUpLable;
    private String[] xScaleDownLable;
    private List<LineInfo> lineInfos;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getyScaleNum() {
        return yScaleNum;
    }

    public void setyScaleNum(int yScaleNum) {
        this.yScaleNum = yScaleNum;
    }

    public String[] getyScaleLeftLable() {
        return yScaleLeftLable;
    }

    public void setyScaleLeftLable(String[] yScaleLeftLable) {
        this.yScaleLeftLable = yScaleLeftLable;
    }

    public String[] getyScaleRightLable() {
        return yScaleRightLable;
    }

    public void setyScaleRightLable(String[] yScaleRightLable) {
        this.yScaleRightLable = yScaleRightLable;
    }

    public int getxScaleNum() {
        return xScaleNum;
    }

    public void setxScaleNum(int xScaleNum) {
        this.xScaleNum = xScaleNum;
    }

    public String[] getxScaleUpLable() {
        return xScaleUpLable;
    }

    public void setxScaleUpLable(String[] xScaleUpLable) {
        this.xScaleUpLable = xScaleUpLable;
    }

    public String[] getxScaleDownLable() {
        return xScaleDownLable;
    }

    public void setxScaleDownLable(String[] xScaleDownLable) {
        this.xScaleDownLable = xScaleDownLable;
    }

    public List<LineInfo> getLineInfos() {
        return lineInfos;
    }

    public void setLineInfos(List<LineInfo> lineInfos) {
        this.lineInfos = lineInfos;
    }
}
