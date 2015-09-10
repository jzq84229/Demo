package com.zhang.mydemo.chart;

import android.graphics.Color;

/**
 * 用于处理线条上的点
 * Created by zjun on 2015/9/9 0009.
 */
public class PlotDot {
    private int mColor = Color.BLACK;
    private int mRingInnerColor = Color.WHITE;

    protected XEnum.DotStyle mDotStyle = XEnum.DotStyle.DOT;

    private float mRadius = 10.0f;
    private int mAlpha = 255;

    public PlotDot(){}

    /**
     * 设置颜色
     * @param color 颜色
     */
    public void setColor(int color){
        this.mColor = color;
    }

    /**
     * 返回颜色
     * @return  颜色
     */
    public int getColor(){
        return mColor;
    }

    /**
     * 设置点形状为环形时，内部所填充的颜色.仅对环形有效
     * @param color 内部填充颜色
     */
    public void setRingInnerCOlor(int color){
        this.mRingInnerColor = color;
    }

    /**
     * 返回当前环形点内部填充色
     * @return  内部填充颜色
     */
    public int getRingInnerColor(){
        return mRingInnerColor;
    }

    /**
     * 设置点的显示风格
     * @param style 显示风格
     */
    public void setDotStyle(XEnum.DotStyle style){
        this.mDotStyle = style;
    }

    /**
     * 返回点的显示风格
     * @return 显示风格
     */
    public XEnum.DotStyle getDotStyle(){
        return mDotStyle;
    }

    /**
     * 设置点的绘制半径大小，会依此指定的半径来绘制相关图形
     * @param radius 半径
     */
    public void setDotRadius(int radius){
        this.mRadius = radius;
    }

    /**
     * 返回点的绘制半径大小
     * @return 半径
     */
    public float getDotRadius(){
        return mRadius;
    }

    /**
     * 设置透明度
     * @param alpha 透明度
     */
    public void setAlpha(int alpha){
        this.mAlpha = alpha;
    }

    /**
     * 返回当前透明度
     * @return 透明度
     */
    public int getALpha(){
        return mAlpha;
    }

}
