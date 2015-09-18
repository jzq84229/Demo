package com.zhang.mydemo.chart.mychart;

import android.graphics.RectF;

/**
 * Created by zjun on 2015/9/14 0014.
 */
public class MyPoint {
    private float x;        //x坐标
    private float y;        //y坐标
    private float value;    //值
    private RectF clickableArea;    //可点击范围

    public MyPoint(float x, float y, float value, int radius) {
        this.x = x;
        this.y = y;
        this.value = value;
        clickableArea = new RectF(x - radius, y - radius, x + radius, y + radius);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public RectF getClickableArea() {
        return clickableArea;
    }

    public void setClickableArea(RectF clickableArea) {
        this.clickableArea = clickableArea;
    }
}
