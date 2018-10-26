package com.zhang.mydemo.chart;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zjun on 2015/9/10 0010.
 */
public class ChartView extends GraphicalView {
    private int screentWidth;
    private int screentHeight;
    private int eachWidth;
    private int paddingLeft = 50;

    //坐标轴原点位置
    private float xPoint = 60; // 原点X坐标
    private float yPoint = 260; // 原点y坐标
    //刻度长度
    private int xScale = 8;     //8个单位构成一个刻度
    private int yScale = 40;
    //x与y坐标轴的长度
    private int xLength = 380;
    private int yLength = 240;

    private int maxDataSize = xLength/xScale; //横坐标最多可绘制的点

    private List<Integer> data = new ArrayList<>(); //存放纵坐标所描绘的点

    private String[] yLabel = new String[yLength/yScale];   //Y轴的刻度上显示的字的集合

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {    //判断接受消息类型
                ChartView.this.invalidate();  //刷新View
            }
        }
    };

    public ChartView(Context context) {
        super(context);
        initView();
    }

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ChartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }



    private void initView(){
//        screentWidth = ScreenUtil.screenWidthPixels(getContext().getApplicationContext());
//        screentHeight = ScreenUtil.screenHeightPixels(getContext().getApplicationContext());
////        eachWidth = (screentWidth - paddingLeft * 2) / 3;
//        //一屏显示三个月，每个月按31天划分，所以x的刻度为：（屏幕宽度-两边空白宽度）/ 3 / 31 = X轴单位刻度
//        xScale = (screentWidth - paddingLeft * 2) / 3 / 31;
//
//        yScale = yLength / 100;
        for (int i = 0; i < yLabel.length; i++) {
            yLabel[i] = (i + 1) + "M/s";
        }

        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (data.size() >= maxDataSize) {
                        data.remove(0);
                    }
                    data.add(new Random().nextInt(4) + 1);
                    handler.sendEmptyMessage(0);
                }
            }
        }).start();
    }

    @Override
    public void render(Canvas canvas) {
        drawView(canvas);
    }

    /**
     * 画月份线
     * @param canvas
     */
    private void drawMonthLine(Canvas canvas){

    }


    private void drawView(Canvas canvas){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true); // 去锯齿
        paint.setColor(Color.BLUE);

        // 画Y轴
        canvas.drawLine(xPoint, yPoint - yLength, xPoint, yPoint, paint);

//        // Y轴箭头
//        canvas.drawLine(xPoint, yPoint - yLength, xPoint - 3, yPoint - yLength + 6, paint);
//        // 箭头
//        canvas.drawLine(xPoint, yPoint - yLength, xPoint + 3, yPoint - yLength + 6, paint);

        // 添加刻度和文字
//        for (int i = 0; i * yScale < yLength; i++) {
//            canvas.drawLine(xPoint, yPoint - i * yScale, xPoint + 5, yPoint - i * yScale, paint); // 刻度
//
//            canvas.drawText(yLabel[i], xPoint - 50, yPoint - i * yScale, paint);// 文字
//        }

        // 画X轴
        canvas.drawLine(xPoint, yPoint, xPoint + xLength, yPoint, paint);

        // 绘折线
        /*
         * if(data.size() > 1){ for(int i=1; i<data.size(); i++){
         * canvas.drawLine(xPoint + (i-1) * xScale, yPoint - data.get(i-1) *
         * yScale, xPoint + i * xScale, yPoint - data.get(i) * yScale, paint); }
         * }
         */
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);

        Paint paint2 = new Paint();
        paint2.setColor(Color.BLUE);
        paint2.setStyle(Paint.Style.FILL);
        if (data.size() > 1) {
            Path path = new Path();
            Path path2 = new Path();
            path.moveTo(xPoint, yPoint - data.get(0) * yScale);
            path2.moveTo(xPoint, yPoint);
            for (int i = 0; i < data.size(); i++) {
                path.lineTo(xPoint + i * xScale, yPoint - data.get(i) * yScale);
                path2.lineTo(xPoint + i * xScale, yPoint - data.get(i) * yScale);
            }
            path2.lineTo(xPoint + (data.size() - 1) * xScale, yPoint);
            canvas.drawPath(path, paint);
//            canvas.drawPath(path2, paint2);
        }
    }

}
