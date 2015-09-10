package com.zhang.mydemo.chart;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * 用于处理线条
 * Created by zjun on 2015/9/9 0009.
 */
public class PlotLine {
    private Paint mPaintLine = null;
    private Paint mPaintLabel = null;
    private Paint mPaintDot = null;
    private PlotDot mPlotDot = null;

    public PlotLine() {
        if (null == mPlotDot) {
            mPlotDot = new PlotDot();
        }
    }

    private void initLinePaint() {
        if (null == mPaintLine) {
            mPaintLine = new Paint();
            mPaintLine.setColor(Color.BLUE);
            mPaintLine.setAntiAlias(true);
            mPaintLine.setStrokeWidth(5);
        }
    }

    private void initLabelPaint(){
        if (mPaintLabel == null) {
            mPaintLabel = new Paint();
            mPaintLabel.setColor(Color.BLUE);
            mPaintLabel.setTextSize(18);
            mPaintLabel.setTextAlign(Paint.Align.CENTER);
            mPaintLabel.setAntiAlias(true);
        }
    }

    /**
     * 开放线画笔
     * @return 画笔
     */
    public Paint getLinePaint(){
        initLinePaint();
        return mPaintLine;
    }

    /**
     * 开放标签画笔
     * @return 画笔
     */
    public Paint getDotLabelPaint(){
        initLabelPaint();
        return mPaintLabel;
    }

    /**
     * 开放点画笔
     * @return 画笔
     */
    public Paint getDotPaint(){
        if (mPaintDot == null) {
            mPaintDot = new Paint();
            mPaintDot.setColor(Color.BLUE);
            mPaintDot.setAntiAlias(true);
            mPaintDot.setStrokeWidth(5);
        }
        return mPaintDot;
    }

    /**
     * 开放点绘制类
     * @return 点绘制类
     */
    public PlotDot getPlotDot(){
        return mPlotDot;
    }

    /**
     * 设置点的显示风格
     * @param style 风格
     */
    public void setDotStyle(XEnum.DotStyle style) {
        mPlotDot.setDotStyle(style);
    }

    /**
     * 返回点的显示风格
     * @return 风格
     */
    public XEnum.DotStyle getDotStyle() {
        return mPlotDot.getDotStyle();
    }
}
