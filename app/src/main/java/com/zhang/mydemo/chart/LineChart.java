package com.zhang.mydemo.chart;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * 折线图控件
 * Created by zjun on 2015/9/11 0011.
 */
public class LineChart extends View {
    protected Paint mPaintFill = null;

    Typeface font = Typeface.create("宋体", Typeface.NORMAL);
    Typeface blodFont = Typeface.create("宋体", Typeface.BOLD);

    private ChartInfo chartInfo;        //图表信息
    private List<LineInfo> lineInfos;   //折线信息

    public LineChart(Context context) {
        super(context);
    }

    public LineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LineChart(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
//        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        paint.setTextSize(20);
        paint.setTypeface(blodFont);
        canvas.drawText(chartInfo.getTitle(), 100, 20, paint);
        // canvas.drawText("最近24小时空气质量指数", 100, 20, paint);

        int ySpliteNum = chartInfo.getyScaleNum();
        int xSpliteNum = chartInfo.getxScaleNum();
        // int ySpliteNum = 6;
        // int xSpliteNum = 6;

        // X轴信息
        int xLineXStartCoord = this.getLeft() + 12;
        int xLineXEndCoord = this.getRight() - 50;
        int xLineYStartCoord = this.getBottom() - 70;
        int xLineYEndCoord = xLineYStartCoord;
        int xLineLength = xLineXEndCoord - xLineXStartCoord;

        // Y轴信息
        int yLineXStartCoord = xLineXStartCoord;
        int yLineXEndCoord = xLineXStartCoord;
        int yLineYStartCoord = xLineYStartCoord;
        int yLineYEndCoord = this.getTop() + 50;
        int yLineLength = yLineYStartCoord > yLineYEndCoord == true ? yLineYStartCoord
                - yLineYEndCoord
                : yLineYEndCoord - yLineYStartCoord;

        // 画坐标轴
        paint.setStrokeWidth(2);
        canvas.drawLine(xLineXStartCoord, xLineYStartCoord, xLineXEndCoord, xLineYEndCoord, paint);
        canvas.drawLine(yLineXStartCoord, yLineYStartCoord, yLineXEndCoord, yLineYEndCoord, paint);

        // 画X轴（事件轴）刻度
        paint.setColor(0xCCCCCCFF);
        int scale = 5;
        Integer[] xPoint = new Integer[xSpliteNum];
        int xStep = xLineLength / xSpliteNum;
        int xLine = xLineXStartCoord;
        for (int i = 0; i < xSpliteNum; i++) {
            xLine += xStep;
            xPoint[i] = xLine;
            canvas.drawLine(xLine, xLineYStartCoord + scale, xLine, xLineYEndCoord - scale, paint);
        }
        // 画X轴标签
        String[] xScaleDownLabel = chartInfo.getxScaleDownLable();
        for (int i = 0; i < xPoint.length; i++) {
            paint.setColor(Color.BLUE);
            paint.setTextSize(14);
            paint.setTypeface(font);
            paint.setTextAlign(Paint.Align.CENTER);
            if (i == 0) {
                canvas.drawText(xScaleDownLabel[i], yLineXStartCoord, xLineYStartCoord + 15, paint);
                canvas.drawText(xScaleDownLabel[i + 1], xPoint[i], xLineYStartCoord + 15, paint);
            } else {
                canvas.drawText(xScaleDownLabel[i + 1], xPoint[i], xLineYStartCoord + 15, paint);
            }
        }

        String[] yScaleLeftLabel = chartInfo.getyScaleLeftLable();
        String[] yScaleRightLabel = chartInfo.getyScaleRightLable();
        // Y轴划分
        Integer[] yPoint = new Integer[ySpliteNum];
        int yStep = yLineLength / ySpliteNum;
        int yLine = yLineYStartCoord;
        for (int i = 0; i < ySpliteNum; i++) {
            if (i == ySpliteNum - 1) {
                yLine = yLineYEndCoord;
            } else {
                yLine -= yStep;
            }
            yPoint[i] = yLine;
            canvas.drawLine(yLineXStartCoord, yLine, yLineXStartCoord + xLineLength, yLine, paint);
        }

        // Y轴色块、数字
        for (int i = 0; i < yPoint.length; i++) {
            paint.setStrokeWidth(6);
            switch (i) {
                case 0:
                    paint.setColor(Color.GREEN);
                    canvas.drawLine(yLineXStartCoord, xLineYStartCoord, yLineXStartCoord, yPoint[i], paint);

                    paint.setColor(Color.BLUE);
                    paint.setTextSize(14);
                    paint.setTypeface(font);
                    paint.setTextAlign(Paint.Align.RIGHT);
                    canvas.drawText("0", yLineXStartCoord - 6, xLineYStartCoord, paint);
                    break;
                case 1:
                    paint.setColor(Color.YELLOW);
                    canvas.drawLine(yLineXStartCoord, yPoint[i - 1], yLineXStartCoord, yPoint[i], paint);
                    break;
                case 2:
                    paint.setColor(0xFFFF7E00);
                    canvas.drawLine(yLineXStartCoord, yPoint[i - 1], yLineXStartCoord, yPoint[i], paint);
                    break;
                case 3:
                    paint.setColor(Color.RED);
                    canvas.drawLine(yLineXStartCoord, yPoint[i - 1], yLineXStartCoord, yPoint[i], paint);
                    break;
                case 4:
                    paint.setColor(0xFF8E1752);
                    canvas.drawLine(yLineXStartCoord, yPoint[i - 1], yLineXStartCoord, yPoint[i], paint);
                    break;
                case 5:
                    paint.setColor(0xFF781631);
                    canvas.drawLine(yLineXStartCoord, yPoint[i - 1], yLineXStartCoord, yPoint[i], paint);
                    break;
                default:
                    break;
            }
            paint.setColor(Color.BLUE);
            paint.setTextSize(14);
            paint.setTypeface(font);
            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(yScaleLeftLabel[i], yLineXStartCoord - 6, yPoint[i], paint);
            paint.setTextAlign(Paint.Align.LEFT);
            if (i == 0) {
                canvas.drawText(yScaleRightLabel[i], yLineXStartCoord + 6, (xLineYStartCoord + yPoint[i]) / 2, paint);
            } else {
                canvas.drawText(yScaleRightLabel[i], yLineXStartCoord + 6, (yPoint[i - 1] + yPoint[i]) / 2, paint);
            }
        }

        // 绘制折线
        /*float stepX = (float) xLineLength / 25.0f;
        float lowStepY = (float) (yLineLength - (yStep * 2)) / 200.0f;
        float middleStepY = (float) yStep / 100.0f;
        float highStepY = (float) yStep / 200.0f;
        for (int z = 0; z < lineInfos.size(); z++) {
            LineInfo lineInfo = lineInfos.get(z);

            int lastStepX = -1;
            int lastStepY = -1;
            for (int j = 0; j < lineInfo.getPoints().length; j++) {
                // 确定点的位置
                int pointX = xLineXStartCoord + (int) (j * stepX);
                int pointY = 0;
                if (lineInfo.getPoints()[j] >= 500) {
                    pointY = yLineYEndCoord;
                } else if (lineInfo.getPoints()[j] >= 200) {
                    if (lineInfo.getPoints()[j] > 300) {
                        int high = lineInfo.getPoints()[j] - 300;
                        pointY += (high * highStepY);
                        pointY += (100 * middleStepY);
                    } else {
                        int middle = lineInfo.getPoints()[j] - 200;
                        pointY += (middle * middleStepY);
                    }
                    pointY += (200 * lowStepY);
                    pointY = yLineYStartCoord - pointY;
                } else {
                    pointY = yLineYStartCoord - (int) (lowStepY * lineInfo.getPoints()[j]);
                }

                // 画点
//                paint.setColor(lineInfo.getPointColor());
//                canvas.drawCircle(pointX, pointY, 3, paint);
                renderRing(canvas, paint, 20, pointX, pointY, String.valueOf(lineInfo.getPoints()[j]));

                // 连线
                if (lastStepX != -1 && lastStepY != -1) {
                    paint.setColor(lineInfo.getLineColor());
                    paint.setStrokeWidth(1.5f);
                    canvas.drawLine(lastStepX, lastStepY, pointX, pointY, paint);
                }

                // 记录
                lastStepX = pointX;
                lastStepY = pointY;
            }

            // 在图表画折现标识
            int markY = this.getBottom() - 40;
            int markX = xLineXStartCoord
                    + (xLineLength / (lineInfos.size() + 1)) * (z + 1) - 30;

            paint.setColor(lineInfo.getPointColor());
            canvas.drawCircle(markX, markY, 3, paint);
            paint.setColor(lineInfo.getLineColor());
            paint.setStrokeWidth(1.5f);
            canvas.drawLine(markX - 10, markY, markX + 10, markY, paint);
            paint.setColor(Color.BLUE);
            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(lineInfo.getName(), markX + 10, markY + 5, paint);
        }*/
    }

    /**
     * 开放填充内部环形的画笔
     */
    public Paint getInnerFillPaint() {
        if(null == mPaintFill) {
            mPaintFill = new Paint();
            mPaintFill.setColor(Color.WHITE);
            mPaintFill.setStyle(Paint.Style.FILL);
            mPaintFill.setAntiAlias(true);
        }
        return mPaintFill;
    }

    private void renderRing(Canvas canvas,Paint paint,float radius, float cX ,float bottom, String value) {
        float ringRadius = radius * 0.7f; // MathHelper.getInstance().mul(radius, 0.7f);
        canvas.drawCircle(cX, bottom, radius, paint);

        canvas.drawCircle(cX, bottom, ringRadius, getInnerFillPaint());

        canvas.drawText(value, cX - 10, bottom + 5, paint);
    }

    public ChartInfo getChartInfo() {
        return chartInfo;
    }

    public void setChartInfo(ChartInfo chartInfo) {
        this.chartInfo = chartInfo;
    }

    public List<LineInfo> getLineInfos() {
        return lineInfos;
    }

    public void setLineInfos(List<LineInfo> lineInfos) {
        this.lineInfos = lineInfos;
    }
}
